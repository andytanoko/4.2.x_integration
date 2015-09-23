/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    Mahesh              Created
 * Jun 19 2002    Mathew              extend from IJMSEvent
 *
 */
package com.gridnode.pdip.app.workflow.engine;

import com.gridnode.pdip.app.workflow.exceptions.DispatcherException;
import com.gridnode.pdip.framework.jms.IJMSEvent;
 
public interface IGWFRouteDispatcher extends IJMSEvent {


    public static final String EVENT_ID="EVENT_ID";

    public static final String ENGINE_TYPE = "ENGINE_TYPE";

    public static final String PROCESSDEF_KEY = "processDefKey";

    public static final String STATE = "state";

    public static final String REASON = "reason";

    public static final String RTPROCESS_UID = "rtProcessUId";
    public static final String RTACTIVITY_UID = "rtActivityUId";
    public static final String RTRESTRICTION_UID = "rtRestrictionUId";

    public static final String PROCESS_TYPE = "processType";
    public static final String PROCESS_UID = "processUId";

    public static final String ACTIVITY_TYPE = "activityType";
    public static final String ACTIVITY_UID = "activityUId";

    public static final String BRANCH_NAME = "branchName";
    public static final String CONTEXT_UID = "contextUId";


    public static final int CREATE_EVENT = 1;
    public static final int ROUTE_EVENT=2;
    public static final int CHANGE_STATE_EVENT = 3;
    public static final int CANCEL_EVENT = 4;
    public static final int REMOVE_EVENT = 5;
    public static final int EXECUTE_EVENT = 6;
  

    public void createRtProcess(Long processUId, String processType, Long rtActivityUId,Long contextUId) throws DispatcherException;
    public void createRtActivity(Long activityUId, String activityType, Long rtProcessUId,String branchName,Long contextUId) throws DispatcherException;
    public void changeProcessState(Long rtProcessUId,int state) throws DispatcherException;
    public void changeActivityState(Long rtActivityUId,int state) throws DispatcherException;
    public void cancelProcess(Long rtProcessUId,Object reason) throws DispatcherException;
    public void removeProcess(Long rtProcessUId) throws DispatcherException;
    public void routeProcess(Long rtProcessUId)  throws DispatcherException;
    public void routeActivity(Long rtActivityUId)  throws DispatcherException;
    public void routeRestriction(Long rtRestrictionUId)  throws DispatcherException;

    public void createRtProcess(Long processUId, String processType, Long rtActivityUId,Long contextUId,String processDefKey) throws DispatcherException;
    public void createRtActivity(Long activityUId, String activityType, Long rtProcessUId,String branchName,Long contextUId,String processDefKey) throws DispatcherException;

}
