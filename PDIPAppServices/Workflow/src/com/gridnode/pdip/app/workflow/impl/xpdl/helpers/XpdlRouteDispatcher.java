/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XpdlRouteDispatcher.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Apr 23, 2004 			Mahesh             	Created
 * Jan 30 2007      Neo Sok Lay         Send message to generic destination.
 */
package com.gridnode.pdip.app.workflow.impl.xpdl.helpers;

import java.util.HashMap;

import com.gridnode.pdip.app.workflow.engine.IGWFRouteDispatcher;
import com.gridnode.pdip.app.workflow.exceptions.DispatcherException;
import com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.workflow.impl.xpdl.XpdlWorkflowEngine;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess;

public class XpdlRouteDispatcher
{


  private static String PROCESS_QUEUE;
  private static String ACTIVITY_QUEUE;
  //private static String RESTRICTION_QUEUE;
 
  static {
      loadConfig();
  }

  private static void loadConfig(){
      try{
          PROCESS_QUEUE=WorkflowUtil.getProperty(IWorkflowConstants.WORKFLOW_PROCESS_DEST);
          ACTIVITY_QUEUE=WorkflowUtil.getProperty(IWorkflowConstants.WORKFLOW_ACTIVITY_DEST);
      }catch(Throwable th){
          Logger.error(ILogErrorCodes.WORKFLOW_LOAD_CONFIG,
                       "[XpdlRouteDispatcher.loadConfig] Unable to load workflow properties ",th);
      }
  }

  public static void createRtProcess(Long processUId,  Long contextUId, Long parentRtActivityUId,String processDefKey,boolean async) throws DispatcherException
  {
    Logger.debug("[XpdlRouteDispatcher.createRtProcess] processUId="+processUId+", processDefKey=" + processDefKey +", parentRtActivityUId="+parentRtActivityUId+", async="+async);
    try
    {
      if(async)
      {            
        HashMap paramMap = new HashMap(7);
        paramMap.put(IGWFRouteDispatcher.EVENT_ID, new Integer(IGWFRouteDispatcher.CREATE_EVENT));
        paramMap.put(IGWFRouteDispatcher.PROCESSDEF_KEY, processDefKey);
        paramMap.put(IGWFRouteDispatcher.PROCESS_UID, processUId);
        paramMap.put(IGWFRouteDispatcher.PROCESS_TYPE, XpdlProcess.ENTITY_NAME);
        paramMap.put(IGWFRouteDispatcher.RTACTIVITY_UID, parentRtActivityUId);
        paramMap.put(IGWFRouteDispatcher.CONTEXT_UID, contextUId);
        paramMap.put(IGWFRouteDispatcher.ENGINE_TYPE,IWorkflowConstants.XPDL_ENGINE);
        WorkflowUtil.sendMessage(PROCESS_QUEUE, paramMap);
      }
      else
      {
        GWFRtProcess rtProcess = new GWFRtProcess();
        rtProcess.setProcessDefKey(processDefKey);
        rtProcess.setProcessUId(processUId);
        rtProcess.setProcessType(XpdlProcess.ENTITY_NAME);
        rtProcess.setParentRtActivityUId(parentRtActivityUId);
        rtProcess.setContextUId(contextUId);
        rtProcess.setEngineType(IWorkflowConstants.XPDL_ENGINE);      
        XpdlWorkflowEngine.getInstance().createRtProcess(rtProcess);       
      }
    }
    catch (Throwable th)
    {
      throw new DispatcherException("Exception in creating RtProcess: "+th.getMessage(), th);
    }
  }

  public static void createRtActivity(Long activityUId, Long rtProcessUId, Long contextUId, String processDefKey, boolean async) throws DispatcherException
  {
    Logger.debug("[XpdlRouteDispatcher.createRtActivity] activityUId=" + activityUId + ", rtProcessUId=" + rtProcessUId+", async="+async);
    try
    {
      if(async)
      {
        HashMap paramMap = new HashMap(8);
        paramMap.put(IGWFRouteDispatcher.EVENT_ID, new Integer(IGWFRouteDispatcher.CREATE_EVENT));
        paramMap.put(IGWFRouteDispatcher.PROCESSDEF_KEY, processDefKey);
        paramMap.put(IGWFRouteDispatcher.ACTIVITY_UID, activityUId);
        paramMap.put(IGWFRouteDispatcher.ACTIVITY_TYPE, XpdlActivity.ENTITY_NAME);
        paramMap.put(IGWFRouteDispatcher.RTPROCESS_UID, rtProcessUId);
        paramMap.put(IGWFRouteDispatcher.CONTEXT_UID, contextUId);
        paramMap.put(IGWFRouteDispatcher.ENGINE_TYPE, IWorkflowConstants.XPDL_ENGINE);
        WorkflowUtil.sendMessage(ACTIVITY_QUEUE, paramMap);
      }
      else
      {
        GWFRtActivity rtActivity = new GWFRtActivity();
        rtActivity.setProcessDefKey(processDefKey);
        rtActivity.setActivityUId(activityUId);
        rtActivity.setActivityType(XpdlActivity.ENTITY_NAME);
        rtActivity.setRtProcessUId(rtProcessUId);
        rtActivity.setContextUId(contextUId);
        rtActivity.setEngineType(IWorkflowConstants.XPDL_ENGINE);
        XpdlWorkflowEngine.getInstance().createRtActivity(rtActivity);
      }
    }
    catch (Throwable th)
    {
      throw new DispatcherException("Exception in creating RtActivity: "+th.getMessage(), th);
    }
  }
  
  public static void executeActivity(Long rtActivityUId) throws DispatcherException
  {
    Logger.debug("[XpdlRouteDispatcher.executeActivity] rtActivityUId=" + rtActivityUId+", async=true");
    try
    {
      HashMap paramMap = new HashMap(8);
      paramMap.put(IGWFRouteDispatcher.EVENT_ID, new Integer(IGWFRouteDispatcher.EXECUTE_EVENT));
      paramMap.put(IGWFRouteDispatcher.RTACTIVITY_UID, rtActivityUId);
      paramMap.put(IGWFRouteDispatcher.ENGINE_TYPE, IWorkflowConstants.XPDL_ENGINE);
      WorkflowUtil.sendMessage(ACTIVITY_QUEUE, paramMap);
    }
    catch (Throwable th)
    {
      throw new DispatcherException("Exception when sending message: "+th.getMessage(), th);
    }
  }

}
