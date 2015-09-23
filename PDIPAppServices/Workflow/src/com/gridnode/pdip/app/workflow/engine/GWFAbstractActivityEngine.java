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
 * Dec 14 2005    Tam Wei Xiang       To resolve the pass by value issue:
 *                                    use UtilEntity.updateEntity instead of
 *                                    UtilEntity.update
 */
package com.gridnode.pdip.app.workflow.engine;


import com.gridnode.pdip.app.workflow.exceptions.*;
import com.gridnode.pdip.app.workflow.runtime.model.*;
import com.gridnode.pdip.app.workflow.util.*;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.*;


public abstract class GWFAbstractActivityEngine extends GWFAbstractWorkflowEngine {

    //public static final String module=Logger.GWF_ENGINE_ACTIVITY;

    abstract public IGWFRouteDispatcher getDispatcher();

    abstract public void onCreate(GWFRtActivity rtActivity) throws WorkflowException,SystemException;

    abstract public void onRemove(GWFRtActivity rtActivity) throws WorkflowException,SystemException;

    abstract public void onBegin(GWFRtActivity rtActivity) throws WorkflowException,SystemException;

    abstract public void onComplete(GWFRtActivity rtActivity) throws WorkflowException,SystemException;

    abstract public void onAbort(GWFRtActivity rtActivity) throws WorkflowException,SystemException;

    abstract public void onPause(GWFRtActivity rtActivity) throws WorkflowException,SystemException;

    abstract public void onResume(GWFRtActivity rtActivity) throws WorkflowException,SystemException;

    abstract public void cancelActivity(GWFRtActivity rtActivity,Object reason) throws WorkflowException,SystemException;

    abstract protected void executeActivityNotRunning(GWFRtActivity rtActivity) throws WorkflowException,SystemException;

    abstract protected void executeActivityRunning(GWFRtActivity rtActivity) throws WorkflowException,SystemException;

    abstract protected void executeActivityCompleted(GWFRtActivity rtActivity) throws WorkflowException,SystemException;

    abstract protected void executeActivityAborted(GWFRtActivity rtActivity) throws WorkflowException,SystemException;

    abstract protected void executeActivitySuspended(GWFRtActivity rtActivity) throws WorkflowException,SystemException;

    public abstract void checkTimeToPerform(String senderKey,String receiverKey) throws WorkflowException,SystemException;

    /**
     * Creates runtime activity
     * @param activity
     * @param rtProcessUId runtime process UID
     * @return GWFRtActivity runtime Activity
     * @throws GWFException
     */
    public GWFRtActivity createRtActivity(GWFRtActivity rtActivity) throws WorkflowException,SystemException {
        try {
            rtActivity.setState(new Integer(GWFRtActivity.OPEN_NOTRUNNING));
            rtActivity = (GWFRtActivity) UtilEntity.createEntity(rtActivity, true);
            Logger.log(Logger.auditCategory,"[GWFAbstractActivityEngine.createRtActivity] OPEN_NOTRUNNING, rtActivityUId="+rtActivity.getUId());
            onCreate(rtActivity);
            return rtActivity;
        } catch (WorkflowException e) {
            throw e;
        } catch (Throwable th) {
        	logWarn("[GWFAbstractActivityEngine.createRtActivity] Exception",th);
            throw new SystemException("Exception in creating RtActivity: "+th.getMessage(), th);
        }
    }

    public GWFRtActivity changeRtActivityState(GWFRtActivity rtActivity,Integer state) throws WorkflowException,SystemException {
        Logger.debug("[GWFAbstractActivityEngine.changeRtActivityState] params rtActivityUID="+rtActivity.getUId()+",ActivityUID="+rtActivity.getActivityUId()+",fromState="+rtActivity.getState()+",toState="+state);
        try{
            int fromState=rtActivity.getState().intValue();
            int toState=state.intValue();

            switch(toState){
                case GWFRtActivity.OPEN_RUNNING:
                            if (fromState == IGWFRtActivity.OPEN_NOTRUNNING || fromState == IGWFRtActivity.OPEN_NOTRUNNING_SUSPENDED){
                                rtActivity.setStartTime(TimeUtil.localToUtcTimestamp());
                                rtActivity.setState(state);
                                rtActivity = (GWFRtActivity)UtilEntity.updateEntity(rtActivity,true);
                                Logger.log(Logger.auditCategory,"[GWFAbstractActivityEngine.changeRtActivityState] OPEN_RUNNING, rtActivityUId="+rtActivity.getUId());
                                if(fromState == IGWFRtActivity.OPEN_NOTRUNNING)
                                    onBegin(rtActivity);
                                else onResume(rtActivity);
                            } else throw new InvalidStateException("cannot change the state  ,UID="+rtActivity.getUId()+",formState="+fromState+",toState="+toState);
                            break;
                case GWFRtActivity.CLOSED_COMPLETED:
                            if (fromState == IGWFRtActivity.OPEN_RUNNING){
                                rtActivity.setEndTime(TimeUtil.localToUtcTimestamp());
                                rtActivity.setState(state);
                                rtActivity = (GWFRtActivity)UtilEntity.updateEntity(rtActivity,true);
                                Logger.log(Logger.auditCategory,"[GWFAbstractActivityEngine.changeRtActivityState] CLOSED_COMPLETED, rtActivityUId="+rtActivity.getUId());
                                onComplete(rtActivity);
                            } else throw new InvalidStateException("cannot change the state  ,UID="+rtActivity.getUId()+",formState="+fromState+",toState="+toState);
                            break;
                case GWFRtActivity.CLOSED_ABNORMALCOMPLETED:
                            if (fromState == IGWFRtActivity.OPEN_RUNNING || fromState == IGWFRtActivity.OPEN_NOTRUNNING_SUSPENDED){
                                rtActivity.setEndTime(TimeUtil.localToUtcTimestamp());
                                rtActivity.setState(state);
                                rtActivity = (GWFRtActivity)UtilEntity.updateEntity(rtActivity,true);
                                Logger.log(Logger.auditCategory,"[GWFAbstractActivityEngine.changeRtActivityState] CLOSED_ABNORMALCOMPLETED, rtActivityUId="+rtActivity.getUId());
                                onAbort(rtActivity);
                            } else throw new InvalidStateException("cannot change the state  ,UID="+rtActivity.getUId()+",formState="+fromState+",toState="+toState);
                            break;
                case GWFRtActivity.OPEN_NOTRUNNING_SUSPENDED:
                            if (fromState == IGWFRtActivity.OPEN_RUNNING){
                                rtActivity.setState(state);
                                rtActivity = (GWFRtActivity)UtilEntity.updateEntity(rtActivity,true);
                                Logger.log(Logger.auditCategory,"[GWFAbstractActivityEngine.changeRtActivityState] OPEN_NOTRUNNING_SUSPENDED, rtActivityUId="+rtActivity.getUId());
                                onPause(rtActivity);
                            } else throw new InvalidStateException("cannot change the state  ,UID="+rtActivity.getUId()+",formState="+fromState+",toState="+toState);
                            break;
                default : throw new InvalidStateException("cannot change the state  ,UID="+rtActivity.getUId()+",formState="+fromState+",toState="+toState);
            }
            return rtActivity;
        }catch(WorkflowException ex){
            throw ex;
        }catch(Throwable th){
            throw new SystemException("Error in changing RtActivity state: "+th.getMessage(),th);
        }
    }

    /**
     * Removes runtime activity
     * @param rtActivityUId
     * @throws GWFException
     */
    public void remove(GWFRtActivity rtActivity) throws WorkflowException,SystemException {
        try {
            UtilEntity.remove(rtActivity.getUId(),GWFRtActivity.ENTITY_NAME,true);
            onRemove(rtActivity);
        } catch (WorkflowException e) {
            throw e;
        } catch (Throwable th) {
        	logWarn("[GWFAbstractActivityEngine.remove] Exception",th);
            throw new SystemException("Exception in removing RtActivity: "+th.getMessage(), th);
        }
    }

    public void selectRoute(GWFRtActivity rtActivity) throws WorkflowException,SystemException {
        try {
            int rtState = rtActivity.getState().intValue();

            if (rtState == GWFRtActivity.OPEN_NOTRUNNING) {
                executeActivityNotRunning(rtActivity);
            } else if (rtState == GWFRtActivity.OPEN_RUNNING) {
                executeActivityRunning(rtActivity);
            } else if (rtState == GWFRtActivity.CLOSED_COMPLETED){// || rtState == GWFRtActivity.CLOSED_ABNORMALCOMPLETED) {
                executeActivityCompleted(rtActivity);
            } else if(rtState == GWFRtActivity.CLOSED_ABNORMALCOMPLETED) {
                executeActivityAborted(rtActivity);
            } else if (rtState == GWFRtActivity.OPEN_NOTRUNNING_SUSPENDED) {
                executeActivitySuspended(rtActivity);
            }
        } catch (WorkflowException e) {
            throw e;
        } catch (Throwable th) {
        	logWarn("[GWFAbstractActivityEngine.selectRoute] Exception",th);
            throw new SystemException("Exception in selecting route: "+th.getMessage(), th);
        }
    }

    protected void logInfo(String msg){
        Logger.log(Logger.activityCategory,msg);
    }

    protected void logDebug(String msg){
        Logger.debug(Logger.activityCategory,msg);
    }
    
    protected void logWarn(String msg,Throwable th) {
    	Logger.warn(Logger.activityCategory,msg,th);
    }

    /**
     * @deprecated Use logError(String,String,Throwable)
     * @param msg
     * @param th
     */
    protected void logError(String msg,Throwable th){
        Logger.err(Logger.activityCategory,msg,th);
    }

    protected void logError(String errorCode, String msg, Throwable th) {
    	Logger.error(errorCode,Logger.activityCategory, msg,th);
    }
}
