/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    Mahesh              Created
 * Jan 30 2007    Neo Sok Lay         Send message to generic destination.
 *
 */

package com.gridnode.pdip.app.workflow.engine;


import java.util.HashMap;

import com.gridnode.pdip.app.workflow.exceptions.DispatcherException;
import com.gridnode.pdip.app.workflow.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.framework.exceptions.domain.GWFException;
 

public class GWFJMSRouteDispatcher implements IGWFRouteDispatcher {

    //public static final String GWF_JMSCONFIG="workflow_jms.properties";

    public static String PROCESS_QUEUE;
    public static String ACTIVITY_QUEUE;
    public static String RESTRICTION_QUEUE;

    static {
        loadConfig();
    }

    private String engineType;

    public GWFJMSRouteDispatcher(String engineType) {
        this.engineType = engineType;
    }

    private static void loadConfig(){
        try{
            PROCESS_QUEUE=WorkflowUtil.getProperty(IWorkflowConstants.WORKFLOW_PROCESS_DEST);
            ACTIVITY_QUEUE=WorkflowUtil.getProperty(IWorkflowConstants.WORKFLOW_ACTIVITY_DEST);
            RESTRICTION_QUEUE=WorkflowUtil.getProperty(IWorkflowConstants.WORKFLOW_RESTRICTION_DEST);
        }catch(Throwable th){
            Logger.error(ILogErrorCodes.WORKFLOW_LOAD_CONFIG,
                         "[GWFJMSRouteDispatcher.loadConfig] Unable to load workflow properties ",th);
        }
    }

    public void createRtProcess(Long processUId, String processType, Long rtActivityUId,Long contextUId) throws DispatcherException{
        createRtProcess(processUId, processType, rtActivityUId,contextUId,null);
    }

    public void createRtActivity(Long activityUId, String activityType, Long rtProcessUId,String branchName,Long contextUId) throws DispatcherException{
        createRtActivity(activityUId, activityType, rtProcessUId,branchName,contextUId,null);
    }

    public void createRtProcess(Long processUId, String processType, Long rtActivityUId,Long contextUId,String processDefKey) throws DispatcherException{
        Logger.debug("[GWFJMSRouteDispatcher.createRtProcess] PROCESS_QUEUE="+PROCESS_QUEUE+",processUId="+processUId);
        try {
            if(processUId==null || processType==null || contextUId==null)
                throw new GWFException("IllegalArguments  (processUId, processType,contextUId) cannot be null");

            HashMap paramMap = new HashMap(7);
            paramMap.put(EVENT_ID, new Integer(CREATE_EVENT));
            paramMap.put(PROCESSDEF_KEY, processDefKey);
            paramMap.put(PROCESS_UID, processUId);
            paramMap.put(PROCESS_TYPE, processType);
            paramMap.put(RTACTIVITY_UID, rtActivityUId);
            paramMap.put(CONTEXT_UID, contextUId);
            paramMap.put(ENGINE_TYPE, engineType);
            WorkflowUtil.sendMessage(PROCESS_QUEUE, paramMap);
        } catch (Throwable th) {
            throw new DispatcherException("[GWFJMSRouteDispatcher.createRtProcess] Exception ", th);
        }
    }

    public void createRtActivity(Long activityUId, String activityType, Long rtProcessUId,String branchName,Long contextUId,String processDefKey) throws DispatcherException{
        Logger.debug("[GWFJMSRouteDispatcher.createRtActivity] ACTIVITY_QUEUE="+ACTIVITY_QUEUE+",activityUId="+activityUId);
        try {
            if(activityUId==null || activityType==null || rtProcessUId==null)
                throw new GWFException("IllegalArguments  (activityUId, activityType,rtProcessUId) cannot be null");
            HashMap paramMap = new HashMap(8);
            paramMap.put(EVENT_ID, new Integer(CREATE_EVENT));
            paramMap.put(PROCESSDEF_KEY, processDefKey);
            paramMap.put(ACTIVITY_UID, activityUId);
            paramMap.put(ACTIVITY_TYPE, activityType);
            paramMap.put(RTPROCESS_UID, rtProcessUId);
            paramMap.put(BRANCH_NAME, branchName);
            paramMap.put(CONTEXT_UID, contextUId);
            paramMap.put(ENGINE_TYPE, engineType);
            WorkflowUtil.sendMessage(ACTIVITY_QUEUE, paramMap);
        } catch (Throwable th) {
            throw new DispatcherException("[GWFJMSRouteDispatcher.createRtActivity] Exception ", th);
        }

    }


    public void changeProcessState(Long rtProcessUId,int state) throws DispatcherException{
        Logger.debug("[GWFJMSRouteDispatcher.changeProcessState] PROCESS_QUEUE="+PROCESS_QUEUE+",rtProcessUId="+rtProcessUId+", state="+state);
        try {
            HashMap paramMap = new HashMap(4);

            paramMap.put(EVENT_ID, new Integer(CHANGE_STATE_EVENT));
            paramMap.put(RTPROCESS_UID, rtProcessUId);
            paramMap.put(STATE, new Integer(state));
            paramMap.put(ENGINE_TYPE, engineType);
            WorkflowUtil.sendMessage(PROCESS_QUEUE, paramMap);
        } catch (Throwable th) {
            throw new DispatcherException("[GWFJMSRouteDispatcher.changeProcessState] Exception ", th);
        }
    }
    public void changeActivityState(Long rtActivityUId,int state) throws DispatcherException{
        Logger.debug("[GWFJMSRouteDispatcher.changeActivityState] rtActivityUId="+rtActivityUId+", state="+state+",ACTIVITY_QUEUE="+ACTIVITY_QUEUE);
        try {
            HashMap paramMap = new HashMap(4);

            paramMap.put(EVENT_ID, new Integer(CHANGE_STATE_EVENT));
            paramMap.put(RTACTIVITY_UID, rtActivityUId);
            paramMap.put(STATE, new Integer(state));
            paramMap.put(ENGINE_TYPE, engineType);
            WorkflowUtil.sendMessage(ACTIVITY_QUEUE, paramMap);
        } catch (Throwable th) {
            throw new DispatcherException("[GWFJMSRouteDispatcher.changeActivityState] Exception ", th);
        }
    }

    public void cancelProcess(Long rtProcessUId,Object reason) throws DispatcherException{
        Logger.debug("[GWFJMSRouteDispatcher.cancelProcess] PROCESS_QUEUE="+PROCESS_QUEUE+",rtProcessUId="+rtProcessUId+", reason="+reason);
        try {
            HashMap paramMap = new HashMap(4);

            paramMap.put(EVENT_ID, new Integer(CANCEL_EVENT));
            paramMap.put(RTPROCESS_UID, rtProcessUId);
            paramMap.put(REASON, reason);
            paramMap.put(ENGINE_TYPE, engineType);
            WorkflowUtil.sendMessage(PROCESS_QUEUE, paramMap);
        } catch (Throwable th) {
            throw new DispatcherException("[GWFJMSRouteDispatcher.cancelProcess] Exception ", th);
        }
    }

    public void removeProcess(Long rtProcessUId) throws DispatcherException{
        Logger.debug("[GWFJMSRouteDispatcher.removeProcess] PROCESS_QUEUE="+PROCESS_QUEUE+",rtProcessUId="+rtProcessUId);
        try {
            HashMap paramMap = new HashMap(3);

            paramMap.put(EVENT_ID, new Integer(REMOVE_EVENT));
            paramMap.put(RTPROCESS_UID, rtProcessUId);
            paramMap.put(ENGINE_TYPE, engineType);
            WorkflowUtil.sendMessage(PROCESS_QUEUE, paramMap);
        } catch (Throwable th) {
            throw new DispatcherException("[GWFJMSRouteDispatcher.removeProcess] Exception ", th);
        }
    }

    public void routeProcess(Long rtProcessUId)  throws DispatcherException{
        Logger.debug("[GWFJMSRouteDispatcher.routeProcess] PROCESS_QUEUE="+PROCESS_QUEUE+",rtProcessUId="+rtProcessUId);
        try {
            HashMap paramMap = new HashMap(3);
            paramMap.put(EVENT_ID, new Integer(ROUTE_EVENT));
            paramMap.put(RTPROCESS_UID, rtProcessUId);
            paramMap.put(ENGINE_TYPE, engineType);
            WorkflowUtil.sendMessage(PROCESS_QUEUE, paramMap);
        } catch (Throwable th) {
            throw new DispatcherException("[GWFJMSRouteDispatcher.routeProcess] Exception ", th);
        }
    }
    public void routeActivity(Long rtActivityUId)  throws DispatcherException{
        Logger.debug("[GWFJMSRouteDispatcher.routeActivity] ACTIVITY_QUEUE="+ACTIVITY_QUEUE+",rtActivityUId="+rtActivityUId);
        try {
            HashMap paramMap = new HashMap(3);
            paramMap.put(EVENT_ID, new Integer(ROUTE_EVENT));
            paramMap.put(RTACTIVITY_UID, rtActivityUId);
            paramMap.put(ENGINE_TYPE, engineType);
            WorkflowUtil.sendMessage(ACTIVITY_QUEUE, paramMap);
        } catch (Throwable th) {
            throw new DispatcherException("[GWFJMSRouteDispatcher.routeActivity] Exception ", th);
        }
    }

    public void routeRestriction(Long rtRestrictionUId)  throws DispatcherException{
        Logger.debug("[GWFJMSRouteDispatcher.routeRestriction] RESTRICTION_QUEUE="+RESTRICTION_QUEUE+",rtRestrictionUId="+rtRestrictionUId);
        try {
            HashMap paramMap = new HashMap(3);
            paramMap.put(EVENT_ID, new Integer(ROUTE_EVENT));
            paramMap.put(RTRESTRICTION_UID, rtRestrictionUId);
            paramMap.put(ENGINE_TYPE, engineType);
            WorkflowUtil.sendMessage(RESTRICTION_QUEUE, paramMap);
        } catch (Throwable th) {
            throw new DispatcherException("[GWFJMSRouteDispatcher.routeRestriction] Exception ", th);
        }
    }

}
