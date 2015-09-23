/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    MAHESH              Created
 * Jul 11 2003    Neo Sok Lay         Add method: getRtProcessDocList(IDataFilter)
 * Jul 15 2003    Neo Sok Lay         Add method: getProcessInstanceKeys(IDataFilter)
 * Feb 06 2007    Neo Sok Lay         Send message to generic destination.
 */
package com.gridnode.pdip.app.workflow.facade.ejb;

import java.util.Collection;
import java.util.HashMap;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.app.workflow.engine.GWFFactory;
import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.BpssDefinitionHelper;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.IBpssConstants;
import com.gridnode.pdip.app.workflow.impl.xpdl.XpdlWorkflowEngine;
import com.gridnode.pdip.app.workflow.impl.xpdl.helpers.XpdlDefinitionHandler;
import com.gridnode.pdip.app.workflow.impl.xpdl.helpers.XpdlRouteDispatcher;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess;
import com.gridnode.pdip.framework.db.AbstractEntityHandler;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;

public class GWFWorkflowManagerBean implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7977233256815154422L;
	private SessionContext _sessionCtx = null;

  public void setSessionContext(SessionContext parm1)
  {
    _sessionCtx = parm1;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  public Long createRtProcess(String processDefKey, Long parentRtActivityUId, HashMap contextData) throws WorkflowException, SystemException
  {
    Logger.log("[GWFWorkflowManagerBean.createRtProcess] Enter, processDefKey=" + processDefKey + ", parentRtActivityUId=" + parentRtActivityUId);
    try
    {
      String processDefArr[] = processDefKey.replaceAll("//", "/").split("/");
      String engineType = processDefArr[1];
      if (GWFFactory.BPSS_ENGINE.equals(engineType))
      {
        String processSpecName = processDefArr[2];
        String processSpecVersion = processDefArr[3];
        String processSpecUUid = processDefArr[4];
        String processType = processDefArr[5];
        String processId = processDefArr[6];
        IEntity entity = (IEntity) BpssDefinitionHelper.getBpssDefinition(processId, processType, processSpecName, processSpecVersion, processSpecUUid);
        Long processUId = (Long) entity.getKey();
        return createRtProcess(processUId, processType, parentRtActivityUId, contextData, engineType);
      }
      else if (GWFFactory.XPDL_ENGINE.equals(engineType))
      {

        //String packageId = processDefArr[2];
        //String pkgVersionId = processDefArr[3];
        //String processType = processDefArr[4];
        String processId = processDefArr[5];
        XpdlProcess xpdlProcess = XpdlDefinitionHandler.getInstance(processDefKey).getXpdlProcess(processId);
        Logger.log("[GWFWorkflowManagerBean.createRtProcess] After getting defination , xpdlProcessUId=" + xpdlProcess.getKey());
        Long contextUId = WorkflowUtil.getDataManager().createContextUId(contextData);
        XpdlRouteDispatcher.createRtProcess((Long) xpdlProcess.getKey(), contextUId, parentRtActivityUId, processDefKey, false);
        return contextUId;
      }
      throw new WorkflowException("[GWFWorkflowManagerBean.createRtProcess] UnKnown engine type, processDefKey=" + processDefKey);
    }
    catch (ApplicationException ex)
    {
      throw new WorkflowException("[GWFWorkflowManagerBean.createRtProcess] cannot create rtprocess ", ex);
    }
    catch (Throwable th)
    {
      throw new SystemException("[GWFWorkflowManagerBean.createRtProcess] cannot create rtprocess ", th);
    }
  }

  /**
   * This method allows to create the runtime process
   * @param processUId This is the process definition uid
   * @param processType This is the processtype
   * @param rtActivityUId This is the parent activity which started this process
   * @param contextUId This is the context uid for this process
   * @param engineType This is engine type XPDL or BPSS
   * @return The contextUId for this process instance
   * @throws WorkflowException
   * @throws SystemException
   */
  public Long createRtProcess(Long processUId, String processType, Long rtActivityUId, HashMap contextData, String engineType)
    throws WorkflowException, SystemException
  {
    try
    {
      Long contextUId  = WorkflowUtil.getDataManager().createContextUId(contextData);
      if (GWFFactory.XPDL_ENGINE.equals(engineType))
      {
        XpdlProcess xpdlProcess = (XpdlProcess)getHandlerFor(XpdlProcess.ENTITY_NAME).getEntityByKey(processUId);
        String processDefKey = "http://"+XpdlProcess.ENTITY_NAME+xpdlProcess.getPackageId()+"/"+xpdlProcess.getPkgVersionId()+"/"+xpdlProcess.getProcessId();
        XpdlRouteDispatcher.createRtProcess(processUId,contextUId,rtActivityUId,processDefKey,false);
      }
      else  GWFFactory.getRouteDispatcher(engineType).createRtProcess(processUId, processType, rtActivityUId, contextUId);
      return contextUId;
    }
    catch (ApplicationException ex)
    {
      throw new WorkflowException("[GWFWorkflowManagerBean.createRtProcess] cannot create rtprocess ", ex);
    }
    catch(Throwable th)
    {
      throw new SystemException("[GWFWorkflowManagerBean.createRtProcess] cannot create rtprocess ", th);
    }
    
  }

  /**
   * This method allows to create the runtime process
   * @param activityUId This is the activity definition uid
   * @param activityType This is the activity type
   * @param rtProcessUId this is the runtime process uid
   * @param branchName This is the branch name
   * @param contextUId This is the context uid of the process
   * @param engineType This is engine type XPDL or BPSS
   * @throws WorkflowException
   * @throws SystemException
   */
  public void createRtActivity(Long activityUId, String activityType, Long rtProcessUId, String branchName, Long contextUId, String engineType)
    throws WorkflowException, SystemException
  {
    if (GWFFactory.XPDL_ENGINE.equals(engineType))
      throw new WorkflowException("Cannot create XpdlActivity from outside");
    GWFFactory.getRouteDispatcher(engineType).createRtActivity(activityUId, activityType, rtProcessUId, branchName, contextUId);
  }

  /**
   * This method changes the process state
   * @param rtProcessUId This is the runtime process uid
   * @param state This is the new state for this process
   * @param contextData This is to be inserted into context
   * @throws WorkflowException
   * @throws SystemException
   */
  public void changeProcessState(Long rtProcessUId, int state, HashMap contextData) throws WorkflowException, SystemException
  {
    try
    {
      GWFRtProcess rtProcess = (GWFRtProcess) getHandlerFor(GWFRtProcess.ENTITY_NAME).getEntityByKey(rtProcessUId);
      if (GWFFactory.XPDL_ENGINE.equals(rtProcess.getEngineType()))
        throw new WorkflowException("Cannot change XpdlProcess state from outside");
      if (contextData != null && contextData.size() > 0)
      {
        WorkflowUtil.getDataManager().setContextData(rtProcess.getContextUId(), contextData);
      }
      GWFFactory.getRouteDispatcher(rtProcess.getEngineType()).changeProcessState(rtProcessUId, state);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("[GWFWorkflowManagerBean.changeProcessState] Error ", th);
    }
  }

  /**
   * This method changes the activity state
   * @param rtActivityUId This is the runtime activity uid
   * @param state This is the new state for this activity
   * @param contextData This is to be inserted into context
   * @throws WorkflowException
   * @throws SystemException
   */
  public void changeActivityState(Long rtActivityUId, int state, HashMap contextData) throws WorkflowException, SystemException
  {
    try
    {
      GWFRtActivity rtActivity = (GWFRtActivity) getHandlerFor(GWFRtActivity.ENTITY_NAME).getEntityByKey(rtActivityUId);
      if (GWFFactory.XPDL_ENGINE.equals(rtActivity.getEngineType()))
        throw new WorkflowException("Cannot change XpdlActivity state from outside");
      
      if (contextData != null && contextData.size() > 0)
      {
        WorkflowUtil.getDataManager().setContextData(rtActivity.getContextUId(), contextData);
      }
      GWFFactory.getRouteDispatcher(rtActivity.getEngineType()).changeActivityState(rtActivityUId, state);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("[GWFWorkflowManagerBean.changeActivityState] Error ", th);
    }
  }

  public void worklistCallback(Long rtActivityUId,int state,String engineType) throws WorkflowException,SystemException
  {
    Logger.log("[GWFWorkflowManagerBean.worklistCallback] Enter, rtActivityUId="+rtActivityUId+", state="+state+", engineType="+engineType);
    try
    {
      if (GWFFactory.XPDL_ENGINE.equals(engineType))
      {
        if(GWFRtActivity.OPEN_RUNNING==state)
          XpdlWorkflowEngine.getInstance().startActivity(rtActivityUId);
        else XpdlWorkflowEngine.getInstance().abortActivity(rtActivityUId,null);
      }
      else 
      {
        WorkflowUtil.getActivityManager().changeActivityState(rtActivityUId,new Integer(state));
      } 
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("Error in worklist callback: "+th.getMessage(), th);
    }
    
  }

  /************************************************/
  //For BPSS

  public void insertDocument(String documentId, String documentType, Object documentObject, String senderKey) throws WorkflowException, SystemException
  {
    throw new WorkflowException("[WorkflowClient.insertDocument] Not Supported ");
  }

  public void insertDocument(
    String documentId,
    String documentType,
    Object documentObject,
    String senderKey,
    String initiatorPartnerKey,
    String responderPartnerKey)
    throws WorkflowException, SystemException
  {
    try
    {
      HashMap paramData = new HashMap();
      paramData.put(IBpssConstants.DOCUMENTID, documentId);
      paramData.put(IBpssConstants.DOCUMENT_TYPE, documentType);
      paramData.put(IBpssConstants.DOCUMENT_OBJECT, documentObject);
      paramData.put(IBpssConstants.SENDER_KEY, senderKey);
      paramData.put(IBpssConstants.INITIATOR_PARTNERKEY, initiatorPartnerKey);
      paramData.put(IBpssConstants.RESPONDER_PARTNERKEY, responderPartnerKey);
      //paramData.put(IBpssConstants.ISREQUEST_DOCUMENT,new Boolean(isRequestDocument));
      //WorkflowUtil.sendMessageToTopic(WorkflowUtil.getProperty(IWorkflowConstants.WORKFLOW_RECEIVEDOCUMENT_DEST), paramData);
      WorkflowUtil.sendMessage(WorkflowUtil.getProperty(IWorkflowConstants.WORKFLOW_RECEIVEDOCUMENT_DEST), paramData);
    }
    catch (ApplicationException ex)
    {
      throw new WorkflowException("[GWFWorkflowManagerBean.insertDocument] Error Unable to insert document", ex);
    }
    catch (Throwable th)
    {
      throw new SystemException("[GWFWorkflowManagerBean.insertDocument] Error Unable to insert document", th);
    }
  }

  public void insertSignal(String documentId, String signalType, Object reason, String senderKey) throws WorkflowException, SystemException
  {
    try
    {
      HashMap paramData = new HashMap();
      paramData.put(IBpssConstants.DOCUMENTID, documentId);
      paramData.put(IBpssConstants.SIGNAL_TYPE, signalType);
      paramData.put(IBpssConstants.REASON, reason);
      paramData.put(IBpssConstants.SENDER_KEY, senderKey);
      //WorkflowUtil.sendMessageToTopic(WorkflowUtil.getProperty(IWorkflowConstants.WORKFLOW_RECEIVESIGNAL_DEST), paramData);
      WorkflowUtil.sendMessage(WorkflowUtil.getProperty(IWorkflowConstants.WORKFLOW_RECEIVESIGNAL_DEST), paramData);
    }
    catch (ApplicationException ex)
    {
      throw new WorkflowException("[GWFWorkflowManagerBean.insertSignal] Error Unable to insert signal", ex);
    }
    catch (Throwable th)
    {
      throw new SystemException("[GWFWorkflowManagerBean.insertSignal] Error Unable to insert signal", th);
    }
  }

  //--------------------------------------------------------------------------------------------
  //For Process Management

  public Collection getXpdlProcessInstanceList(String processId, String packageId, String pkgVersionId) throws WorkflowException, SystemException
  {
    Logger.log("[GWFWorkflowManagerBean.getXpdlProcessInstanceList] Enter, processId=" + processId + ", packageId=" + packageId + ", packageId=" + packageId);
    String processDefKey = XpdlDefinitionHandler.getProcessDefKey(processId, packageId, pkgVersionId);
    try
    {
      XpdlProcess xpdlProcess = XpdlDefinitionHandler.getInstance(processDefKey).getXpdlProcess(processId);
      if (xpdlProcess == null)
        throw new WorkflowException("[GWFWorkflowManagerBean.getXpdlProcessInstanceList] No Definition found with processDefKey=" + processDefKey);
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, GWFRtProcess.PROCESS_UID, filter.getEqualOperator(), xpdlProcess.getKey(), false);
      filter.addSingleFilter(filter.getAndConnector(), GWFRtProcess.PROCESS_TYPE, filter.getEqualOperator(), xpdlProcess.getEntityName(), false);
      return getProcessInstanceList(filter);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("[GWFWorkflowManagerBean.getXpdlProcessInstanceList] Error ", th);
    }
  }

  public Collection getBpssProcessInstanceList(String processId, String processType, String processSpecName, String processSpecVersion, String processSpecUUid)
    throws WorkflowException, SystemException
  {
    try
    {
      IEntity entity = (IEntity) BpssDefinitionHelper.getBpssDefinition(processId, processType, processSpecName, processSpecVersion, processSpecUUid);
      if (entity == null)
        throw new WorkflowException(
          "[GWFWorkflowManagerBean.getBpssProcessInstanceList] No Definition found wirh params (processId,processType,processSpecName,processSpecVersion,processSpecUUid)=("
            + processId
            + ","
            + processType
            + ","
            + processSpecName
            + ","
            + processSpecVersion
            + ","
            + processSpecUUid
            + ")");
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, GWFRtProcess.PROCESS_UID, filter.getEqualOperator(), entity.getKey(), false);
      filter.addSingleFilter(filter.getAndConnector(), GWFRtProcess.PROCESS_TYPE, filter.getEqualOperator(), entity.getEntityName(), false);
      return getProcessInstanceList(filter);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException(
        "[GWFWorkflowManagerBean.getBpssProcessInstanceList] Error wirh params (processId,processType,processSpecName,processSpecVersion,processSpecUUid)=("
          + processId
          + ","
          + processType
          + ","
          + processSpecName
          + ","
          + processSpecVersion
          + ","
          + processSpecUUid
          + ")",
        th);
    }
  }

  public GWFRtProcess getProcessInstance(Long rtProcessUId) throws WorkflowException, SystemException
  {
    try
    {
      return (GWFRtProcess) getHandlerFor(GWFRtProcess.ENTITY_NAME).getEntityByKey(rtProcessUId);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("[GWFWorkflowManagerBean.getProcessInstance] Error ", th);
    }
  }

  public Collection getProcessInstanceList(IDataFilter filter) throws WorkflowException, SystemException
  {
    try
    {
      return getHandlerFor( GWFRtProcess.ENTITY_NAME).getEntityByFilter(filter);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("Error: "+th.getMessage(), th);
    }
  }

  /**
   * Retrieve the keys of the Process instances that satisfy the filtering
   * condition.
   * 
   * @param filter The filtering condition
   * @return Collection of UIDs of GWFRtProcess entities retrieved.
   * @since GT 2.2 I1
   */
  public Collection getProcessInstanceKeys(IDataFilter filter) throws WorkflowException, SystemException
  {
    try
    {
      return getHandlerFor(GWFRtProcess.ENTITY_NAME).getKeyByFilterForReadOnly(filter);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("[GWFWorkflowManagerBean.getProcessInstanceKeys] Error ", th);
    }
  }

  public void cancelProcessInstance(Long rtProcessUId, Object reason) throws WorkflowException, SystemException
  {
    try
    {
      
      GWFRtProcess rtProcess=(GWFRtProcess)getHandlerFor(GWFRtProcess.ENTITY_NAME).getEntityByKey(rtProcessUId);
      if (GWFFactory.XPDL_ENGINE.equals(rtProcess.getEngineType()))
        throw new WorkflowException("Cannot cancel XpdlProcess , rtProcessUId="+rtProcessUId);
      GWFFactory.getProcessEngine(rtProcess.getEngineType()).cancelProcess(rtProcess,reason);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("Error in cancelProcessInstance", th);
    }
  }

  public void removeProcessInstance(Long rtProcessUId) throws WorkflowException, SystemException
  {
    try
    {
      GWFRtProcess rtProcess=(GWFRtProcess)getHandlerFor(GWFRtProcess.ENTITY_NAME).getEntityByKey(rtProcessUId);
      if (GWFFactory.XPDL_ENGINE.equals(rtProcess.getEngineType()))
        XpdlWorkflowEngine.getInstance().removeProcess(rtProcessUId);
      else GWFFactory.getProcessEngine(rtProcess.getEngineType()).removeRtProcess(rtProcess);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("[GWFWorkflowManagerBean.removeProcessInstance] Error ", th);
    }
  }

  public Collection getRtProcessDocList(Long rtProcessUId) throws WorkflowException, SystemException
  {
    try
    {
      GWFRtProcess rtProcess = getProcessInstance(rtProcessUId);
      if (rtProcess.getEngineType() == null || !IWorkflowConstants.BPSS_ENGINE.equals(rtProcess.getEngineType()))
        throw new WorkflowException("[GWFWorkflowManagerBean.getRtProcessDocList] RtProcessDoc only exists for BPSS Processes");
      if (rtProcess.getProcessType() == null || !BpssBinaryCollaboration.ENTITY_NAME.equals(rtProcess.getProcessType()))
        throw new WorkflowException("[GWFWorkflowManagerBean.getRtProcessDocList] RtProcessDoc only exists for BpssBinaryCollaboration");
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, GWFRtProcessDoc.RT_BINARY_COLLABORATION_UID, filter.getEqualOperator(), rtProcessUId, false);
      return getHandlerFor(GWFRtProcessDoc.ENTITY_NAME).getEntityByFilter(filter);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("[GWFWorkflowManagerBean.getRtProcessDocList] Error ", th);
    }
  }

  /**
   * Get the GWFRtProcessDoc entities base on the specified filter.
   * 
   * @param filter The filter condition.
   * @return Collection of GWFRtProcessDoc entites that satisfy the
   * specified filter condition.
   * @since GT 2.2 I1
   */
  public Collection getRtProcessDocList(IDataFilter filter) throws WorkflowException, SystemException
  {
    try
    {
      return getHandlerFor(GWFRtProcessDoc.ENTITY_NAME).getEntityByFilter(filter);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException("[GWFWorkflowManagerBean.getRtProcessDocList] Error ", th);
    }
  }
  public Collection getBpssProcessInstanceKeys(String processId, String processType, String processSpecName, String processSpecVersion, String processSpecUUid)
    throws WorkflowException, SystemException
  {
    try
    {
      IEntity entity = (IEntity) BpssDefinitionHelper.getBpssDefinition(processId, processType, processSpecName, processSpecVersion, processSpecUUid);
      if (entity == null)
        throw new WorkflowException(
          "[GWFWorkflowManagerBean.getBpssProcessInstanceList] No Definition found with params (processId,processType,processSpecName,processSpecVersion,processSpecUUid)=("
            + processId
            + ","
            + processType
            + ","
            + processSpecName
            + ","
            + processSpecVersion
            + ","
            + processSpecUUid
            + ")");
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, GWFRtProcess.PROCESS_UID, filter.getEqualOperator(), entity.getKey(), false);
      filter.addSingleFilter(filter.getAndConnector(), GWFRtProcess.PROCESS_TYPE, filter.getEqualOperator(), entity.getEntityName(), false);
      return getProcessInstanceKeys(filter);
    }
    catch (WorkflowException ex)
    {
      throw ex;
    }
    catch (Throwable th)
    {
      throw new SystemException(
        "[GWFWorkflowManagerBean.getBpssProcessInstanceList] Error wirh params (processId,processType,processSpecName,processSpecVersion,processSpecUUid)=("
          + processId
          + ","
          + processType
          + ","
          + processSpecName
          + ","
          + processSpecVersion
          + ","
          + processSpecUUid
          + ")",
        th);
    }
  }

  // helper methods
  private AbstractEntityHandler getHandlerFor(String entityName)
  {
    return EntityHandlerFactory.getHandlerFor(entityName, true);
  }

}
