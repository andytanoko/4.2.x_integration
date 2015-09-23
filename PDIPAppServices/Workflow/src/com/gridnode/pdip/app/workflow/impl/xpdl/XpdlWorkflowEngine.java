/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XpdlWorkflowEngine.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Apr 21, 2004 			Mahesh             	Created
 */
package com.gridnode.pdip.app.workflow.impl.xpdl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gridnode.pdip.app.workflow.adaptors.AppAdaptor;
import com.gridnode.pdip.app.workflow.exceptions.DataDefinitionException;
import com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.impl.xpdl.helpers.XpdlActivityHelper;
import com.gridnode.pdip.app.workflow.impl.xpdl.helpers.XpdlDefinitionHandler;
import com.gridnode.pdip.app.workflow.impl.xpdl.helpers.XpdlRouteDispatcher;
import com.gridnode.pdip.app.workflow.runtime.ejb.IGWFRtActivityObj;
import com.gridnode.pdip.app.workflow.runtime.ejb.IGWFRtProcessObj;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.IGWFRtActivity;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.base.contextdata.entities.model.ContextKey;
import com.gridnode.pdip.base.contextdata.facade.ejb.IDataManagerObj;
import com.gridnode.pdip.base.gwfbase.xpdl.helpers.XpdlConstants;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlApplication;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlDataField;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlFormalParam;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlSubFlow;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlTool;
import com.gridnode.pdip.framework.db.AbstractEntityHandler;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.KeyConverter;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.pdip.framework.util.UtilString;

public class XpdlWorkflowEngine
{

  private static HashMap senderKeyLockMap = new HashMap();
  private static Collection PROCESS_COMPLETION_STATES = new ArrayList();
  private static Collection ACTIVITY_COMPLETION_STATES = new ArrayList();
  
  private static XpdlWorkflowEngine _xpdlWorkflowEngine = new XpdlWorkflowEngine();
  
  private XpdlWorkflowEngine()
  {
    PROCESS_COMPLETION_STATES.add(new Integer(GWFRtProcess.CLOSED_COMPLETED));
    PROCESS_COMPLETION_STATES.add(new Integer(GWFRtProcess.CLOSED_ABNORMALCOMPLETED));
    PROCESS_COMPLETION_STATES.add(new Integer(GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED));
    PROCESS_COMPLETION_STATES.add(new Integer(GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED));

    ACTIVITY_COMPLETION_STATES.add(new Integer(GWFRtActivity.CLOSED_COMPLETED));
    ACTIVITY_COMPLETION_STATES.add(new Integer(GWFRtActivity.CLOSED_ABNORMALCOMPLETED));

  }
  
  public static XpdlWorkflowEngine getInstance()
  {
    return _xpdlWorkflowEngine;
  }
  
  public GWFRtProcess createRtProcess(GWFRtProcess rtProcess) throws WorkflowException, SystemException
  {
    Logger.log("[XpdlWorkflowEngine.createRtProcess] Enter, ProcessDefKey=" + rtProcess.getProcessDefKey());
    try
    {
      rtProcess.setState(new Integer(GWFRtProcess.OPEN_RUNNING));
      rtProcess.setStartTime(TimeUtil.localToUtcTimestamp());
      rtProcess.setMaxConcurrency(new Integer(1)); // since process created with running state
      rtProcess = (GWFRtProcess) getHandlerFor(GWFRtProcess.ENTITY_NAME).createEntity(rtProcess);

      XpdlDefinitionHandler defHandler = XpdlDefinitionHandler.getInstance(rtProcess.getProcessDefKey());
      XpdlProcess xpdlProcess = defHandler.getXpdlProcess(rtProcess.getProcessUId());

      // set the timeout alarm if declared
      if (xpdlProcess.getWorkingTime() != null && xpdlProcess.getWorkingTime().longValue() > 0)
      {
        rtProcess.setFinishInterval(new Long(xpdlProcess.getWorkingTime().longValue()));
        String key = KeyConverter.getKey((Long) rtProcess.getKey(), rtProcess.getEntityName(), IWorkflowConstants.XPDL_ENGINE);
        WorkflowUtil.addAlarm(key, XpdlProcess.ENTITY_NAME, IWorkflowConstants.XPDL_ENGINE, rtProcess.getFinishInterval().longValue());
      }

      // set the initial data from datafields
      Collection dataFieldColl = defHandler.getDataFields(xpdlProcess.getProcessId());
      if (dataFieldColl != null && dataFieldColl.size() > 0)
      {
        HashMap contextData = new HashMap();
        for (Iterator iterator = dataFieldColl.iterator(); iterator.hasNext();)
        {
          XpdlDataField xpdlDataField = (XpdlDataField) iterator.next();
          if (xpdlDataField.getInitialValue() != null)
            contextData.put(new ContextKey(xpdlDataField.getDataFieldId()), xpdlDataField.getInitialValue());
        }
        if (contextData.size() > 0)
        {
          WorkflowUtil.getDataManager().setContextData(rtProcess.getContextUId(), contextData);
        }
      }

      //start the activity
      XpdlActivity startActivity = defHandler.getXpdlActivity(XpdlConstants.START_ACTIVITYID);
      if (startActivity != null)
      {
        XpdlRouteDispatcher.createRtActivity((Long) startActivity.getKey(),(Long) rtProcess.getKey(),rtProcess.getContextUId(),rtProcess.getProcessDefKey(),true);
      }
      else
        throw new WorkflowException("No Start activity found for process = " + rtProcess.getProcessUId());

      return rtProcess;
    }
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlWorkflowEngine.createRtProcess] Error", th);
      throw new SystemException("Error in creating RtProcess: "+th.getMessage(), th);
    }
    finally
    {
      Logger.log("[XpdlWorkflowEngine.createRtProcess] Exit, rtProcessUId=" + rtProcess.getKey());
    }
  }

  public void completeProcess(Long rtProcessUId) throws WorkflowException, SystemException
  {
    Logger.log("[XpdlWorkflowEngine.changeRtProcessState] Enter, rtProcessUID=" + rtProcessUId);
    try
    {
      AbstractEntityHandler rtProcessHandler = getHandlerFor(GWFRtProcess.ENTITY_NAME);
      IGWFRtProcessObj rtProcessObj = (IGWFRtProcessObj) rtProcessHandler.findByPrimaryKey(rtProcessUId);
      GWFRtProcess rtProcess = (GWFRtProcess) rtProcessObj.getData();

      if (rtProcess.getFinishInterval() != null)
      {
        WorkflowUtil.cancelAlarm(
          KeyConverter.getKey((Long) rtProcess.getKey(), rtProcess.getEntityName(), IWorkflowConstants.XPDL_ENGINE),
          XpdlProcess.ENTITY_NAME,
          IWorkflowConstants.XPDL_ENGINE);
      }

      IDataManagerObj dataManager = WorkflowUtil.getDataManager();
      HashMap currentCtxData = null;
      if (rtProcess.getParentRtActivityUId() != null)
      { // ctx data is required to get the parent activity details
        currentCtxData = dataManager.getContextData(rtProcess.getContextUId());
      }

      //since we remove the process once completed, no need to change the state to completed
      //just remove the rtprocess
      removeProcess(rtProcessUId);
      
      //if rtprocess has parent activity complete the parent activity
      if (rtProcess.getParentRtActivityUId() != null)
      {
        Long parentContextUId = (Long) currentCtxData.get(new ContextKey("workflow.control.parentContextUId"));
        Map parameterMapping = (Map) currentCtxData.get(new ContextKey("workflow.control.parentParameterMapping"));
        if (parentContextUId!=null && parameterMapping!=null && !parameterMapping.isEmpty())
        {
          HashMap parentCtxData = new HashMap();
          for (Iterator iterator = parameterMapping.keySet().iterator(); iterator.hasNext();)
          {
            Object key = iterator.next();
            parentCtxData.put(parameterMapping.get(key), currentCtxData.get(new ContextKey(key.toString())));
          }
          dataManager.setContextData(parentContextUId, parentCtxData);
        }

        //completing the parent activity which started this process
        XpdlDefinitionHandler defHandler = XpdlDefinitionHandler.getInstance((String)currentCtxData.get(new ContextKey("workflow.control.parentProcessDefKey")));
        XpdlActivity xpdlActivity=defHandler.getXpdlActivity((Long)currentCtxData.get(new ContextKey("workflow.control.parentActivityUId")));
        checkAndCompleteActivity(xpdlActivity,rtProcess.getParentRtActivityUId());
      }
    }
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlWorkflowEngine.completeProcess] Error", th);
      throw new SystemException("Error in completing process: "+th.getMessage(), th);
    }
    finally
    {
      Logger.log("[XpdlWorkflowEngine.completeProcess] Exit, rtProcessUId=" + rtProcessUId);
    }
  }

  public void abortProcess(Long rtProcessUId, Integer abortState) throws WorkflowException, SystemException
  {
    Logger.log("[XpdlWorkflowEngine.abortProcess] Enter, rtProcessUId=" + rtProcessUId + ", abortState=" + abortState);
    try
    {
      AbstractEntityHandler rtProcessHandler = getHandlerFor(GWFRtProcess.ENTITY_NAME);
      IGWFRtProcessObj rtProcessObj = (IGWFRtProcessObj) rtProcessHandler.findByPrimaryKey(rtProcessUId);
      GWFRtProcess rtProcess = (GWFRtProcess) rtProcessObj.getData();
      rtProcess.setEndTime(TimeUtil.localToUtcTimestamp());
      rtProcess.setState(abortState);
      rtProcessObj.setData(rtProcess);

      //remove any timeout alarms if exists
      if (rtProcess.getFinishInterval() != null)
      {
        WorkflowUtil.cancelAlarm(
          KeyConverter.getKey((Long) rtProcess.getKey(), rtProcess.getEntityName(), IWorkflowConstants.XPDL_ENGINE),
          XpdlProcess.ENTITY_NAME,
          IWorkflowConstants.XPDL_ENGINE);
      }

      // abort the parent activity if exists     
      if (rtProcess.getParentRtActivityUId() != null)
        abortActivity(rtProcess.getParentRtActivityUId(),null);
    }
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlWorkflowEngine.abortProcess] Error", th);
      throw new SystemException("Error in aborting process: "+th.getMessage(), th);
    }
    finally
    {
      Logger.log("[XpdlWorkflowEngine.abortProcess] Exit, rtProcessUId=" + rtProcessUId);
    }

  }

  public void cancelProcess(Long rtProcessUId,String reason) throws WorkflowException, SystemException
  {
    Logger.log("[XpdlWorkflowEngine.cancelProcess] Enter, rtProcessUId=" + rtProcessUId);
    try
    {
      AbstractEntityHandler rtProcessHandler = getHandlerFor(GWFRtProcess.ENTITY_NAME);
      AbstractEntityHandler rtActivityHandler = getHandlerFor(GWFRtActivity.ENTITY_NAME);
      IGWFRtProcessObj rtProcessObj = (IGWFRtProcessObj) rtProcessHandler.findByPrimaryKey(rtProcessUId);
      GWFRtProcess rtProcess = (GWFRtProcess) rtProcessObj.getData();
      if(PROCESS_COMPLETION_STATES.contains(rtProcess.getState()))
        return;
      rtProcess.setState(new Integer(GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED)); 
      rtProcessObj.setData(rtProcess);
      try
      {
        WorkflowUtil.getDataManager().removeContextUId(rtProcess.getContextUId());
      }
      catch (Throwable th)
      {
        Logger.warn("[XpdlWorkflowEngine.cancelProcess] Error removing context with contextUId=" + rtProcess.getContextUId(), th);
      }

      // cancel activities in the process
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, GWFRtActivity.RT_PROCESS_UID, filter.getEqualOperator(), rtProcess.getKey(), false);
      filter.addDomainFilter(filter.getAndConnector(),GWFRtActivity.STATE,ACTIVITY_COMPLETION_STATES,true);
      Collection rtActivityObjColl = rtActivityHandler.findByFilter(filter);
      Collection rtActivityUIdColl = new ArrayList();
      for (Iterator iterator = rtActivityObjColl.iterator(); iterator.hasNext();)
      {
        IGWFRtActivityObj rtActivityObj = (IGWFRtActivityObj) iterator.next();
        GWFRtActivity rtActivity = (GWFRtActivity) rtActivityObj.getData();
        rtActivity.setState(new Integer(GWFRtActivity.CLOSED_ABNORMALCOMPLETED));
        rtActivityObj.setData(rtActivity);
        rtActivityUIdColl.add(rtActivity.getKey());
      }

      // cancel subprocesses of activities
      filter = new DataFilterImpl();
      filter.addDomainFilter(null, GWFRtProcess.PARENT_RTACTIVITY_UID, rtActivityUIdColl, false);
      filter.addDomainFilter(filter.getAndConnector(), GWFRtProcess.STATE, PROCESS_COMPLETION_STATES,true);
      Collection rtProcessUIdColl = rtProcessHandler.getKeyByFilterForReadOnly(filter);
      for (Iterator iterator = rtProcessUIdColl.iterator(); iterator.hasNext();)
      {
        cancelProcess((Long) iterator.next(),reason);
      }
    }
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlWorkflowEngine.cancelProcess] Error", th);
      throw new SystemException("Error in cancelling process: "+th.getMessage(), th);
    }
    finally
    {
      Logger.log("[XpdlWorkflowEngine.cancelProcess] Exit, rtProcessUId=" + rtProcessUId);
    }
  }


  public void removeProcess(Long rtProcessUId) throws WorkflowException, SystemException
  {
    Logger.log("[XpdlWorkflowEngine.removeProcess] Enter, rtProcessUId=" + rtProcessUId);
    try
    {
      AbstractEntityHandler rtProcessHandler = getHandlerFor(GWFRtProcess.ENTITY_NAME);
      AbstractEntityHandler rtActivityHandler = getHandlerFor(GWFRtActivity.ENTITY_NAME);
      IGWFRtProcessObj rtProcessObj = (IGWFRtProcessObj) rtProcessHandler.findByPrimaryKey(rtProcessUId);
      GWFRtProcess rtProcess = (GWFRtProcess) rtProcessObj.getData();
      rtProcessObj.remove();
      try
      {
        WorkflowUtil.getDataManager().removeContextUId(rtProcess.getContextUId());
      }
      catch (Throwable th)
      {
        Logger.warn("[XpdlWorkflowEngine.removeProcess] Error removing context with contextUId=" + rtProcess.getContextUId(), th);
      }

      // remove activities in the process
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, GWFRtActivity.RT_PROCESS_UID, filter.getEqualOperator(), rtProcess.getKey(), false);
      Collection rtActivityObjColl = rtActivityHandler.findByFilter(filter);
      Collection rtActivityUIdColl = new ArrayList();
      for (Iterator iterator = rtActivityObjColl.iterator(); iterator.hasNext();)
      {
        IGWFRtActivityObj rtActivityObj = (IGWFRtActivityObj) iterator.next();
        rtActivityUIdColl.add(rtActivityObj.getPrimaryKey());
        rtActivityObj.remove();
      }

      // remove subprocesses of activities
      filter = new DataFilterImpl();
      filter.addDomainFilter(null, GWFRtProcess.PARENT_RTACTIVITY_UID, rtActivityUIdColl, false);
      Collection rtProcessUIdColl = rtProcessHandler.getKeyByFilterForReadOnly(filter);
      for (Iterator iterator = rtProcessUIdColl.iterator(); iterator.hasNext();)
      {
        removeProcess((Long) iterator.next());
      }
    }
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlWorkflowEngine.removeProcess] Error", th);
      throw new SystemException("Error in removing process: "+th.getMessage(), th);
    }
    finally
    {
      Logger.log("[XpdlWorkflowEngine.removeProcess] Exit, rtProcessUId=" + rtProcessUId);
    }
  }

  public GWFRtActivity createRtActivity(GWFRtActivity rtActivity) throws WorkflowException, SystemException
  {
    Logger.log("[XpdlWorkflowEngine.createRtActivity] Enter, rtProcessUId=" + rtActivity.getRtProcessUId()+", ActivityUId="+rtActivity.getActivityUId());
    try
    {
      XpdlActivity activity = XpdlDefinitionHandler.getInstance(rtActivity.getProcessDefKey()).getXpdlActivity(rtActivity.getActivityUId());
      if (activity.getWorkingTime() != null && activity.getWorkingTime().longValue() > 0)
      {
        rtActivity.setFinishInterval(new Long(activity.getWorkingTime().longValue()));
      }

      if (activity.getActivityType() == XpdlActivity.AUTOMATIC_TYPE)
      {
        //only automatic activities can be suspended
        //if suspend parameters are defined then suspend the activity,dont start it.
        Map extAttr = UtilString.strToMap(activity.getExtendedAttributes());
        long dispatchInterval = getDispatchInterval(extAttr);
        int dispatchCount = getDispatchCount(extAttr);
        if (dispatchInterval > 0 && dispatchCount > 0)
        {
          rtActivity.setState(new Integer(GWFRtActivity.OPEN_NOTRUNNING_SUSPENDED));
          rtActivity = (GWFRtActivity) getHandlerFor(GWFRtActivity.ENTITY_NAME).createEntity(rtActivity);
          Logger.debug("[XpdlWorkflowEngine.createRtActivity] OPEN_NOTRUNNING_SUSPENDED, rtActivityUId=" + rtActivity.getUId());

          //add invocation timer
          String senderKey = rtActivity.getProcessDefKey() + "/" + XpdlActivity.ENTITY_NAME + "/" + activity.getUId();
          addSuspendInvocationTimer(senderKey, dispatchInterval);
        }
        else //otherwise start the activity
        {
          rtActivity.setState(new Integer(GWFRtActivity.OPEN_RUNNING));
          rtActivity.setStartTime(TimeUtil.localToUtcTimestamp());
          rtActivity = (GWFRtActivity) getHandlerFor(GWFRtActivity.ENTITY_NAME).createEntity(rtActivity);
          Logger.debug("[XpdlWorkflowEngine.createRtActivity] OPEN_RUNNING, rtActivityUId=" + rtActivity.getUId());
          //begin the activity
          executeActivity(rtActivity);
        }
      }
      else if (activity.getActivityType() == XpdlActivity.MANUAL_TYPE)
      {
        rtActivity.setState(new Integer(GWFRtActivity.OPEN_NOTRUNNING));
        rtActivity = (GWFRtActivity) getHandlerFor(GWFRtActivity.ENTITY_NAME).createEntity(rtActivity);
        Logger.debug("[XpdlWorkflowEngine.createRtActivity] OPEN_NOTRUNNING, add to workitem, rtActivityUId=" + rtActivity.getUId());
        //add it to worklist
        WorkflowUtil.addWorkItem(activity.getPerformerId(), (Long) rtActivity.getKey(), activity.getActivityId(), rtActivity.getProcessDefKey(), rtActivity.getContextUId());
      }

      return rtActivity;
    }
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlWorkflowEngine.createRtActivity] Exception", th);
      throw new SystemException("Exception in creating RtActivity: "+th.getMessage(), th);
    }
  }

  public void startActivity(Long rtActivityUId) throws WorkflowException, SystemException
  {
    try
    {
      AbstractEntityHandler rtActivityHandler = getHandlerFor(GWFRtActivity.ENTITY_NAME);
      IGWFRtActivityObj rtActivityObj = (IGWFRtActivityObj) rtActivityHandler.findByPrimaryKey(rtActivityUId);
      GWFRtActivity rtActivity = (GWFRtActivity) rtActivityObj.getData();
      if(rtActivity.getState().intValue()==GWFRtActivity.OPEN_RUNNING || ACTIVITY_COMPLETION_STATES.contains(rtActivity.getState()))
      {
      	/**
      	 * @todo Need review
      	 */
        Logger.warn("[XpdlWorkflowEngine.startActivity] rtActivity is already in running or completed state for rtActivityUId="+rtActivityUId);
        return;
      }
      rtActivity.setState(new Integer(GWFRtActivity.OPEN_RUNNING));
      rtActivity.setStartTime(TimeUtil.localToUtcTimestamp());
      rtActivityObj.setData(rtActivity);
      
      executeActivity(rtActivity);
    }
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlWorkflowEngine.startActivity] Exception", th);
      throw new SystemException("Exception in starting activity: "+th.getMessage(), th);
    }

  }
  
  public boolean resumeActivity(Long rtActivityUId) throws WorkflowException, SystemException
  {
    Logger.log("[XpdlWorkflowEngine.resumeActivity] Enter , rtActivityUId="+rtActivityUId);
    try
    {
      AbstractEntityHandler rtActivityHandler = getHandlerFor(GWFRtActivity.ENTITY_NAME);
      IGWFRtActivityObj rtActivityObj = (IGWFRtActivityObj) rtActivityHandler.findByPrimaryKey(rtActivityUId);
      GWFRtActivity rtActivity = (GWFRtActivity) rtActivityObj.getData();
      if(!(rtActivity.getState().intValue()==IGWFRtActivity.OPEN_NOTRUNNING || rtActivity.getState().intValue()==IGWFRtActivity.OPEN_NOTRUNNING_SUSPENDED))
      {
        Logger.warn("[XpdlWorkflowEngine.resumeActivity] rtActivity is not in suspended state for rtActivityUId="+rtActivityUId);
        return false;
      }
      rtActivity.setState(new Integer(GWFRtActivity.OPEN_RUNNING));
      rtActivity.setStartTime(TimeUtil.localToUtcTimestamp());
      rtActivityObj.setData(rtActivity);
      XpdlRouteDispatcher.executeActivity(rtActivityUId);
      return true;
    }
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlWorkflowEngine.resumeActivity] Exception", th);
      throw new SystemException("Exception in resuming activity: "+th.getMessage(), th);
    }
  }
  

  public void executeActivity(Long rtActivityUId) throws WorkflowException, SystemException
  {
    Logger.log("[XpdlWorkflowEngine.executeActivity] Enter , rtActivityUId="+rtActivityUId);
    try
    {
      AbstractEntityHandler rtActivityHandler = getHandlerFor(GWFRtActivity.ENTITY_NAME);
      IGWFRtActivityObj rtActivityObj = (IGWFRtActivityObj) rtActivityHandler.findByPrimaryKey(rtActivityUId);
      GWFRtActivity rtActivity = (GWFRtActivity) rtActivityObj.getData();
      executeActivity(rtActivity);
    }
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlWorkflowEngine.executeActivity] Exception", th);
      throw new SystemException("Exception in executing activity: "+th.getMessage(), th);
    }
  }
  
  public void executeActivity(GWFRtActivity rtActivity) throws WorkflowException, SystemException
  {
    try
    {
      if(rtActivity.getState().intValue()!=GWFRtActivity.OPEN_RUNNING)
      {
        Logger.warn("[XpdlWorkflowEngine.executeActivity] rtActivity is not in running state for rtActivityUId="+rtActivity.getKey());
        return;
      }
      
      if (rtActivity.getFinishInterval() != null && rtActivity.getFinishInterval().longValue() > 0)
      {
        String key = KeyConverter.getKey((Long) rtActivity.getKey(), rtActivity.getEntityName(), IWorkflowConstants.XPDL_ENGINE);
        WorkflowUtil.addAlarm(key, XpdlActivity.ENTITY_NAME, IWorkflowConstants.XPDL_ENGINE, rtActivity.getFinishInterval().longValue());
      }

      XpdlDefinitionHandler defHandler = XpdlDefinitionHandler.getInstance(rtActivity.getProcessDefKey());
      XpdlActivity activity = defHandler.getXpdlActivity(rtActivity.getActivityUId());
      String type = activity.getImplementationType();

      // call the appmanager
      if (Boolean.TRUE.equals(activity.getIsRoute()) || (type != null && type.equals(XpdlConstants.IT_NO)))
      {
        // ROUTE goes directly to complete status
        // No implemetation - complete this activity
        checkAndCompleteActivity(activity, (Long)rtActivity.getKey());
      }
      else if (type == null || type.equals(XpdlConstants.IT_LOOP) || !(type.equals(XpdlConstants.IT_SUBFLOW) || type.equals(XpdlConstants.IT_TOOL)))
      {
        throw new DataDefinitionException("ImplementationType is not supported, type=" + type + ", activity=" + activity);
      }
      else if (type.equals(XpdlConstants.IT_TOOL))
      {
        // TOOL will invoke a procedure (service) or an application
        Collection tools = defHandler.getTools(activity.getActivityId());
        if (tools != null && !tools.isEmpty())
        {
          XpdlTool thisTool = (XpdlTool) tools.iterator().next();
          callApplication(thisTool, rtActivity);
        }
        else
        {
          Logger.debug("[XpdlWorkflowEngine.executeActivity] No tool defined for activityUId="+activity.getKey());
          checkAndCompleteActivity(activity, (Long)rtActivity.getKey());
        }
      }
      else if (type.equals(XpdlConstants.IT_SUBFLOW))
      {
        Collection subFlows=XpdlDefinitionHandler.getInstance(rtActivity.getProcessDefKey()).getSubFlows(activity.getActivityId());
        if(subFlows!=null && !subFlows.isEmpty())
        {
          XpdlSubFlow subFlow = (XpdlSubFlow)subFlows.iterator().next(); 
          runSubFlow(subFlow,activity, rtActivity); // Begin a sub workflow
        }
        else
        {
          Logger.debug("[XpdlWorkflowEngine.executeActivity] No subFlow defined for activityUId="+activity.getKey());
          checkAndCompleteActivity(activity, (Long)rtActivity.getKey());
        }
      }
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlWorkflowEngine.executeActivity] Exception", th);
      throw new SystemException("Exception in executing activity: "+th.getMessage(), th);
    }
  }

  // Checks to see if we can complete
  public void checkAndCompleteActivity(XpdlActivity activity, Long rtActivityUId) throws WorkflowException, SystemException
  {
    try
    {
      String mode = activity.getFinishMode();
      
      // Default mode is MANUAL -- only finish if automatic
      if (mode != null && mode.equals(XpdlConstants.AUTOMATIC_MODE))
      {
        completeActivity( rtActivityUId);
      }
      else
        Logger.debug("[XpdlWorkflowEngine.checkAndCompleteActivity] Finish mode cannot be null for activity=" + activity);
    }
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlWorkflowEngine.checkComplete] Exception", th);
      throw new SystemException("Exception in checking and completing activity: "+th.getMessage(), th);
    }

  }

  public void completeActivity(Long rtActivityUId) throws WorkflowException, SystemException
  {
    try
    {
      AbstractEntityHandler rtActivityHandler = getHandlerFor(GWFRtActivity.ENTITY_NAME);
      IGWFRtActivityObj rtActivityObj = (IGWFRtActivityObj) rtActivityHandler.findByPrimaryKey(rtActivityUId);
      GWFRtActivity rtActivity = (GWFRtActivity) rtActivityObj.getData();
      rtActivity.setState(new Integer(GWFRtActivity.CLOSED_COMPLETED));
      rtActivity.setEndTime(TimeUtil.localToUtcTimestamp());
      rtActivityObj.setData(rtActivity);
      if (rtActivity.getFinishInterval() != null)
        XpdlActivityHelper.removeActivityAlarm(rtActivityUId);
      XpdlActivityHelper.processNextActivity(rtActivity);
    }
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlWorkflowEngine.completeActivity] Exception", th);
      throw new SystemException("Exception in completing activity: "+th.getMessage(), th);
    }
  }

  public void abortActivity(Long rtActivityUId, Map ctxData) throws WorkflowException, SystemException
  {
    try
    {
      AbstractEntityHandler rtActivityHandler = getHandlerFor(GWFRtActivity.ENTITY_NAME);
      IGWFRtActivityObj rtActivityObj = (IGWFRtActivityObj) rtActivityHandler.findByPrimaryKey(rtActivityUId);
      GWFRtActivity rtActivity = (GWFRtActivity) rtActivityObj.getData();
      if(rtActivity.getState().intValue()==GWFRtActivity.CLOSED_COMPLETED || rtActivity.getState().intValue()==GWFRtActivity.CLOSED_ABNORMALCOMPLETED)
      {
        Logger.debug("[XpdlWorkflowEngine.abortActivity] rtActivity is already completed or aborted, rtActivityUId="+rtActivityUId+", state="+rtActivity.getState());
        return;      
      }
      rtActivity.setState(new Integer(GWFRtActivity.CLOSED_ABNORMALCOMPLETED));
      rtActivity.setEndTime(TimeUtil.localToUtcTimestamp());
      rtActivityObj.setData(rtActivity);
      if (rtActivity.getFinishInterval() != null )
        XpdlActivityHelper.removeActivityAlarm(rtActivityUId);
      abortProcess(rtActivity.getRtProcessUId(),new Integer(GWFRtProcess.CLOSED_ABNORMALCOMPLETED));
      
    }
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlWorkflowEngine.completeActivity] Exception", th);
      throw new SystemException("Exception in aborting activity: "+th.getMessage(), th);
    }
  }


  // Runs a SUBFLOW activity
  protected void runSubFlow(XpdlSubFlow subFlow,XpdlActivity activity, GWFRtActivity rtActivity) throws WorkflowException,SystemException {
    Logger.log("[XpdlWorkflowEngine.runSubFlow] Enter, subFlowId="+subFlow.getSubFlowId()+", rtActivityUId="+rtActivity.getKey());
    try
    {
      if(subFlow.getSubFlowId()==null)
      {
        Logger.warn("[XpdlWorkflowEngine.runSubFlow] subflowId is null for activityUId="+activity.getKey()+", rtActivityUId="+rtActivity.getKey());
        abortActivity((Long)rtActivity.getKey(),null);
      }
      else
      {
        String subFlowId=subFlow.getSubFlowId();
        //String type = XpdlConstants.SE_SYNCHR;
        //if (subFlow.getSubFlowType() != null)
        //    type = subFlow.getSubFlowType();

        XpdlDefinitionHandler subflowDefHandler=XpdlDefinitionHandler.getInstance(subFlowId);
        XpdlProcess subProcess=subflowDefHandler.getXpdlProcess(KeyConverter.getValue(subFlowId,5));

        Collection formalParamColl=subflowDefHandler.getFormalParams(XpdlConstants.NA,subProcess.getProcessId());
        List actualParameters=UtilString.split(subFlow.getActualParameters(),",");
        if(formalParamColl==null)
          formalParamColl=Collections.EMPTY_LIST;
        if(actualParameters==null)
          actualParameters=Collections.EMPTY_LIST;
        if(formalParamColl.size()!=actualParameters.size())
        {
          Logger.debug("[XpdlWorkflowEngine.runSubFlow] actualParameters="+actualParameters+", formalParameters="+formalParamColl);
          Logger.warn("[XpdlWorkflowEngine.runSubFlow] actualParameters and formalParameters are not matching for subFlowUId="+subFlow.getKey()+", activityUId="+activity.getKey()+", rtActivityUId="+rtActivity.getKey());
          abortActivity((Long)rtActivity.getKey(),null);
          return;
        }
        else
        {
          IDataManagerObj dataManager=WorkflowUtil.getDataManager();
          HashMap subFlowContextData = new HashMap();
          if (formalParamColl != null && !formalParamColl.isEmpty())
          {
            HashMap contextData=dataManager.getContextData(rtActivity.getContextUId());
            Map activityExtAttr=UtilString.strToMap(activity.getExtendedAttributes());
            HashMap parentParamMap=new HashMap();
            for (Iterator iterator = formalParamColl.iterator(); iterator.hasNext();)
            {
              XpdlFormalParam xpdlFormalParam = (XpdlFormalParam) iterator.next();
              int ind = xpdlFormalParam.getIndexNumber().intValue() - 1;
              String actualParam = (String) actualParameters.get(ind);
              if (!XpdlConstants.FP_OUT.equals(xpdlFormalParam.getMode()))
              {
                subFlowContextData.put(xpdlFormalParam.getFormalParamId(),contextData.get(new ContextKey(actualParam)));
                if(activityExtAttr!=null && activityExtAttr.containsKey(actualParam)) //override with default value
                {
                  subFlowContextData.put(xpdlFormalParam.getFormalParamId(),activityExtAttr.get(actualParam));
                }
              }
              if (!XpdlConstants.FP_IN.equals(xpdlFormalParam.getMode()))
              {
                parentParamMap.put(xpdlFormalParam.getFormalParamId(),actualParam);
              }
            }
            if(!parentParamMap.isEmpty())
            {
              subFlowContextData.put("workflow.control.parentContextUId",rtActivity.getContextUId());
              subFlowContextData.put("workflow.control.parentParameterMapping",parentParamMap);
            }
          }
          subFlowContextData.put("workflow.control.parentProcessDefKey",rtActivity.getProcessDefKey());
          subFlowContextData.put("workflow.control.parentActivityUId",rtActivity.getActivityUId());
          Long subFlowContextUId = dataManager.createContextUId(subFlowContextData);
          XpdlRouteDispatcher.createRtProcess((Long)subProcess.getKey(),subFlowContextUId,(Long)rtActivity.getKey(),subFlowId,false);
        }
      }
    } 
    catch (WorkflowException e) 
    {
        throw e;
    } 
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th) 
    {
        Logger.warn("[XpdlActivityEngine.runSubFlow] Exception",th);
        throw new SystemException("Exception in running Subflow activity: "+th.getMessage(), th);
    }
  }


  public void callApplication(XpdlTool xpdlTool, GWFRtActivity rtActivity) throws WorkflowException, SystemException
  {
    Logger.debug("[XpdlWorkflowEngine.callApplication] rtActivityUId=" + rtActivity.getKey()+", xpdlTool="+xpdlTool.getToolId());
    try
    {
      IDataManagerObj dataManager = WorkflowUtil.getDataManager();
      XpdlDefinitionHandler defHandler = XpdlDefinitionHandler.getInstance(rtActivity.getProcessDefKey());

      Collection tempApplicationColl = defHandler.getApplications(xpdlTool.getToolId(), xpdlTool.getProcessId());
      if (tempApplicationColl == null || tempApplicationColl.isEmpty())
        tempApplicationColl = defHandler.getApplications(xpdlTool.getToolId(), null);

      if (tempApplicationColl != null && !tempApplicationColl.isEmpty())
      {
        XpdlApplication xpdlApplication = (XpdlApplication) tempApplicationColl.iterator().next();
        List actualParametersList = UtilString.split(xpdlTool.getActualParameters(), ",");
        Collection formalParamColl = defHandler.getFormalParams(xpdlApplication.getApplicationId(), xpdlApplication.getProcessId());
        
        if(formalParamColl==null)
          formalParamColl=Collections.EMPTY_LIST;
        if(actualParametersList==null)
          actualParametersList=Collections.EMPTY_LIST;
          
        if(formalParamColl.size()!=actualParametersList.size())
        {
          Logger.warn("[XpdlWorkflowEngine.callApplication] actualParameters and formalParameters are not matching for xpdlToolUId="+xpdlTool.getKey()+", rtActivityUId="+rtActivity.getKey());
          Logger.debug("[XpdlWorkflowEngine.callApplication] actualParameters="+actualParametersList+", formalParameters="+formalParamColl);
          abortActivity((Long)rtActivity.getKey(),null);
          return;
        }

        String formalParamNames = "";
        String formalParamTypes = "";
        HashMap applicationParamData = new HashMap();
        String outParamName = null;
        if (formalParamColl != null && !formalParamColl.isEmpty())
        {
          HashMap contextData = dataManager.getContextData(rtActivity.getContextUId());
          Map toolExtAttributes = UtilString.strToMap(xpdlTool.getExtendedAttributes());

          ArrayList formalList = new ArrayList(formalParamColl);
          for (Iterator iterator = formalParamColl.iterator(); iterator.hasNext();)
          {
            XpdlFormalParam xpdlFormalParam = (XpdlFormalParam) iterator.next();
            formalList.set(xpdlFormalParam.getIndexNumber().intValue() - 1, xpdlFormalParam);
          }
          for (Iterator iterator = formalList.iterator(); iterator.hasNext();)
          {
            XpdlFormalParam xpdlFormalParam = (XpdlFormalParam) iterator.next();
            int ind = xpdlFormalParam.getIndexNumber().intValue() - 1;
            if (!XpdlConstants.FP_OUT.equals(xpdlFormalParam.getMode()))
            {
              formalParamNames += xpdlFormalParam.getFormalParamId() + (iterator.hasNext() ? "," : "");
              formalParamTypes += xpdlFormalParam.getDataTypeName() + (iterator.hasNext() ? "," : "");
              String actualParam = (String) actualParametersList.get(ind);
              applicationParamData.put(xpdlFormalParam.getFormalParamId(), contextData.get(new ContextKey(actualParam)));
              if (toolExtAttributes != null && toolExtAttributes.containsKey(actualParam))
              { // overide with defined value
                applicationParamData.put(xpdlFormalParam.getFormalParamId(), toolExtAttributes.get(actualParam));
              }
            }
            if (outParamName == null && !XpdlConstants.FP_IN.equals(xpdlFormalParam.getMode()))
            {
              outParamName = (String) actualParametersList.get(ind);
            }
          }
        }
        //get the extended attributes
        Map adaptorInfoMap = new HashMap();
        Map map = UtilString.strToMap(xpdlApplication.getExtendedAttributes());
        if (map != null)
          adaptorInfoMap.putAll(map);
        adaptorInfoMap.put("adaptor.parameternames", formalParamNames);
        adaptorInfoMap.put("adaptor.parametertypes", formalParamTypes);
        try
        {
          Object obj = AppAdaptor.callApp(adaptorInfoMap, applicationParamData, null);
          if (outParamName != null)
            dataManager.setContextData(rtActivity.getContextUId(), new ContextKey(outParamName), obj);
        }
        catch (ApplicationException ex)
        {
          Logger.warn("[XpdlWorkflowEngine.callApplication] error while executing application, rtActivityUId="+rtActivity.getKey(),ex);
          abortActivity((Long)rtActivity.getKey(),null);
          return;
        }
        XpdlActivity xpdlActivity = defHandler.getXpdlActivity(rtActivity.getActivityUId());
        checkAndCompleteActivity(xpdlActivity, (Long)rtActivity.getKey());
      }
    }
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th)
    {
      Logger.warn("[XpdlWorkflowEngine.callApplication] Error", th);
      throw new SystemException("Error in calling application: "+th.getMessage(), th);
    }
  }

  public void checkTimeToPerform(String senderKey,String receiverKey) throws WorkflowException,SystemException{
    try
    {
      if(senderKey!=null && receiverKey!=null && receiverKey.equals(IWorkflowConstants.WORKFLOW_SUSPENDACTIVITY_KEY))
      {
        resumeSuspendedActivities(senderKey,receiverKey);
        return;
      }
      else
      {
        Long uId=KeyConverter.getUID(senderKey);
        String entityName=KeyConverter.getEntityName(senderKey);
        if(GWFRtProcess.ENTITY_NAME.equals(entityName)) 
        {
            abortProcess(uId,new Integer(GWFRtProcess.CLOSED_ABNORMALCOMPLETED));
        } 
        else if(GWFRtActivity.ENTITY_NAME.equals(entityName))
        {
          abortActivity(uId,null);
        }
      }
    } 
    catch (WorkflowException e)
    {
      throw e;
    }
    catch (SystemException e)
    {
      throw e;
    }
    catch (Throwable th) 
    {
        Logger.warn("[XpdlWorkflowEngine.checkTimeToPerform] Exception",th);
        throw new SystemException("Exception in checking time to perform: "+th.getMessage(), th);
    }
  }


  /**
   * This method adds retry alarm to invoke suspended activities
   * @param senderKey
   * @param dispatchInterval
   * @throws Exception
   */
  private void addSuspendInvocationTimer(String senderKey, long dispatchInterval) throws Exception
  {
    Object lockObj = senderKeyLockMap.get(senderKey);
    if (lockObj == null)
    {
      synchronized (senderKeyLockMap)
      {
        lockObj = senderKeyLockMap.get(senderKey);
        if (lockObj == null)
        {
          lockObj = new String(senderKey);
          senderKeyLockMap.put(senderKey, lockObj);
        }
      }
    }
    synchronized (lockObj)
    {
      WorkflowUtil.setRetryAlarm(senderKey, IWorkflowConstants.WORKFLOW_SUSPENDACTIVITY_KEY, IWorkflowConstants.XPDL_ENGINE, dispatchInterval * 1000, false);
    }
  }

  /**
   * This method resumes the suspended activities
   * @param senderKey
   * @param receiverKey
   */
  private void resumeSuspendedActivities(String senderKey, String receiverKey)
  {
    Logger.debug("[XpdlActivityEngine.resumeSuspendedActivities] Enter, senderKey=" + senderKey + ", receiverKey=" + receiverKey);
    try
    {
      String processDefKey = senderKey.substring(0, senderKey.indexOf("/" + XpdlActivity.ENTITY_NAME));
      Long activityUId = new Long(senderKey.substring(senderKey.lastIndexOf('/') + 1));
      XpdlActivity activity = XpdlDefinitionHandler.getInstance(processDefKey).getXpdlActivity(activityUId);
      Map extAttr = UtilString.strToMap(activity.getExtendedAttributes());
      int dispatchCount = getDispatchCount(extAttr);
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, GWFRtActivity.ACTIVITY_UID, filter.getEqualOperator(), activityUId, false);
      filter.addSingleFilter(filter.getAndConnector(),GWFRtActivity.STATE,filter.getEqualOperator(),new Long(GWFRtActivity.OPEN_NOTRUNNING_SUSPENDED),false);
      filter.addSingleFilter(filter.getAndConnector(), GWFRtActivity.ENGINE_TYPE, filter.getEqualOperator(), IWorkflowConstants.XPDL_ENGINE, false);
      Collection rtActivityUIdColl = getHandlerFor(GWFRtActivity.ENTITY_NAME).getKeyByFilterForReadOnly(filter);

      int totalSuspendedActivities = 0;

      if (rtActivityUIdColl != null && !rtActivityUIdColl.isEmpty())
      {
        totalSuspendedActivities = rtActivityUIdColl.size();
        Logger.debug(
          "[XpdlActivityEngine.resumeSuspendedActivities] totalSuspendedActivities=" + totalSuspendedActivities + ", dispatchCount=" + dispatchCount);

        //Integer runningState = new Integer(GWFRtActivity.OPEN_RUNNING);
        int index = 0;
        for (Iterator i = rtActivityUIdColl.iterator(); i.hasNext() && index < dispatchCount;)
        {
          Long rtActivityUId = (Long) i.next();
          try
          {
            boolean isResumed = resumeActivity(rtActivityUId);
            if(isResumed) index++;
          }
          catch (Throwable th)
          {
            Logger.warn("[XpdlActivityEngine.resumeSuspendedActivities] Error while resume for rtActivityUId=" + rtActivityUId, th);
          }
        }
      }

      if (dispatchCount > totalSuspendedActivities)
      {
        //double check that there are no new suspended activities
        int suspendedCount = getHandlerFor(GWFRtActivity.ENTITY_NAME).getEntityCount(filter);
        if (suspendedCount == 0)
        {
          Logger.debug(
            "[XpdlActivityEngine.resumeSuspendedActivities] removing the invocation alarm,since dispatchCount>totalSuspendedActivities ,dispatchCount="
              + dispatchCount
              + ", totalSuspendedActivities="
              + totalSuspendedActivities);
          senderKeyLockMap.remove(senderKey);
          WorkflowUtil.cancelAlarm(senderKey, receiverKey, IWorkflowConstants.XPDL_ENGINE);
        }
      }

    }
    catch (Throwable th)
    {
      Logger.error(ILogErrorCodes.WORKFLOW_RESUME_SUSPENDED_ACT,
                   "Error in resuming suspended activities (senderKey=" + senderKey + "): "+th.getMessage(), th);
    }
    finally
    {
      Logger.debug("[XpdlActivityEngine.resumeSuspendedActivities] Exit");
    }
  }

  private long getDispatchInterval(Map extAttr)
  {
    long dispatchInterval = -1;
    if (extAttr != null
      && extAttr.get(IWorkflowConstants.WORKFLOW_DISPATCH_INTERVAL) != null
      && extAttr.get(IWorkflowConstants.WORKFLOW_DISPATCH_COUNT) != null)
    {
      String str = (String) extAttr.get(IWorkflowConstants.WORKFLOW_DISPATCH_INTERVAL);
      try
      {
        if (str != null && str.trim().length() > 0)
          dispatchInterval = Long.parseLong(str);
      }
      catch (Throwable th)
      {
        Logger.warn("XpdlActivityEngine.getDispatchInterval] Error , dispatchInterval=" + str, th);
      }
    }
    return dispatchInterval;
  }

  private int getDispatchCount(Map extAttr)
  {
    int dispatchCount = -1;
    if (extAttr != null && extAttr.get(IWorkflowConstants.WORKFLOW_DISPATCH_COUNT) != null)
    {
      String str = (String) extAttr.get(IWorkflowConstants.WORKFLOW_DISPATCH_COUNT);
      try
      {
        if (str != null && str.trim().length() > 0)
          dispatchCount = Integer.parseInt(str);
      }
      catch (Throwable th)
      {
        Logger.warn("XpdlActivityEngine.getDispatchCount] Error , dispatchCount=" + str, th);
      }
    }
    return dispatchCount;
  }

  private static AbstractEntityHandler getHandlerFor(String entityName)
  {
    return EntityHandlerFactory.getHandlerFor(entityName, true);
  }

}
