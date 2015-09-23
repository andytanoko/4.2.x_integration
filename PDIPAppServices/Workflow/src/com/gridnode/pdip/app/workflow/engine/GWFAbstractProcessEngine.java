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
 * Dec 14 2005    Tam Wei Xiang       To resolve the pass by value issue. Use
 *                                    UtilEntity.updateEntity instead of UtilEntity.update
 */
package com.gridnode.pdip.app.workflow.engine;


import com.gridnode.pdip.app.workflow.exceptions.InvalidStateException;
import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.notification.ProcessTransactionNotifyHandler;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.IGWFRtProcess;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaboration;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.domain.GWFException;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.pdip.framework.util.UtilEntity;

public abstract class GWFAbstractProcessEngine extends GWFAbstractWorkflowEngine {


    //public static final String module=Logger.GWF_ENGINE_PROCESS;

    abstract public IGWFRouteDispatcher getDispatcher();

    abstract public void onCreate(GWFRtProcess rtProcess) throws WorkflowException,SystemException;

    abstract public void onRemove(GWFRtProcess rtProcess) throws WorkflowException,SystemException;

    abstract public void onBegin(GWFRtProcess rtProcess) throws WorkflowException,SystemException;

    abstract public void onComplete(GWFRtProcess rtProcess) throws WorkflowException,SystemException;

    abstract public void onAbort(GWFRtProcess rtProcess) throws WorkflowException,SystemException;

    abstract public void onPause(GWFRtProcess rtProcess) throws WorkflowException,SystemException;

    abstract public void onResume(GWFRtProcess rtProcess) throws WorkflowException,SystemException;

    abstract public void cancelProcess(GWFRtProcess rtProcess,Object reason) throws WorkflowException,SystemException;

    abstract protected void executeProcessNotRunning(GWFRtProcess rtProcess) throws WorkflowException,SystemException;

    abstract protected void executeProcessRunning(GWFRtProcess rtProcess) throws WorkflowException,SystemException;

    abstract protected void executeProcessCompleted(GWFRtProcess rtProcess) throws WorkflowException,SystemException;

    abstract protected void executeProcessAborted(GWFRtProcess rtProcess) throws WorkflowException,SystemException;

    public GWFRtProcess changeRtProcessState(GWFRtProcess rtProcess,Integer state) throws WorkflowException,SystemException{
        Logger.debug("[GWFAbstractProcessEngine.changeRtProcessState] params rtProcessUID="+rtProcess.getUId()+",ProcessUID="+rtProcess.getProcessUId()+",fromState="+rtProcess.getState()+", toState="+state);
        try{
            int fromState=rtProcess.getState().intValue();
            int toState=state.intValue();
            switch(toState){
                case GWFRtProcess.OPEN_RUNNING:
                        if(fromState == GWFRtProcess.OPEN_NOTRUNNING || fromState == GWFRtProcess.OPEN_NOTRUNNING_SUSPENDED) {
                            rtProcess.setStartTime(TimeUtil.localToUtcTimestamp());
                            rtProcess.setState(state);
                            
                            rtProcess = (GWFRtProcess)UtilEntity.updateEntity(rtProcess,true);
                            Logger.log(Logger.auditCategory,"[GWFAbstractProcessEngine.changeRtProcessState] OPEN_RUNNING, rtProcessUId="+rtProcess.getUId());
                            if(fromState == GWFRtProcess.OPEN_NOTRUNNING)
                                onBegin(rtProcess);
                            else onResume(rtProcess);
                        } else throw new InvalidStateException("[GWFAbstractProcessEngine.changeRtProcessState] cannot change the state  ,UID="+rtProcess.getUId());
                        break;
                case GWFRtProcess.CLOSED_COMPLETED:
                        if(fromState == GWFRtProcess.OPEN_RUNNING) {
                            rtProcess.setEndTime(TimeUtil.localToUtcTimestamp());
                            rtProcess.setState(state);
                            
                            rtProcess = (GWFRtProcess)UtilEntity.updateEntity(rtProcess,true);
                            Logger.log(Logger.auditCategory,"[GWFAbstractProcessEngine.changeRtProcessState] CLOSED_COMPLETED, rtProcessUId="+rtProcess.getUId());
                            
                            //TWX 14112006 Trigger the rtprocess state status
                            triggerProcessStateChangeStatus(rtProcess);
                            
                            onComplete(rtProcess);
                        } else throw new InvalidStateException("[GWFAbstractProcessEngine.changeRtProcessState] cannot change the state  ,UID="+rtProcess.getUId());
                        break;
                case GWFRtProcess.CLOSED_ABNORMALCOMPLETED:
                case GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED:
                case GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED:
                        if(fromState==GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED)
                            return rtProcess;
                        if(fromState == GWFRtProcess.OPEN_RUNNING) {
                            rtProcess.setEndTime(TimeUtil.localToUtcTimestamp());
                            rtProcess.setState(state);
                            
                            rtProcess = (GWFRtProcess)UtilEntity.updateEntity(rtProcess,true);
                            Logger.log(Logger.auditCategory,"[GWFAbstractProcessEngine.changeRtProcessState] CLOSED_ABNORMAL, rtProcessUId="+rtProcess.getUId());
                            
                            //TWX 14112006 Trigger the rtprocess state status
                            triggerProcessStateChangeStatus(rtProcess);
                            
                            onAbort(rtProcess);
                        } else throw new InvalidStateException("[GWFAbstractProcessEngine.changeRtProcessState] cannot change the state  ,UID="+rtProcess.getUId());
                        break;
                case GWFRtProcess.OPEN_NOTRUNNING_SUSPENDED:
                        if(fromState == GWFRtProcess.OPEN_RUNNING) {
                            rtProcess.setState(state);
                            
                            rtProcess = (GWFRtProcess)UtilEntity.updateEntity(rtProcess,true);
                            Logger.log(Logger.auditCategory,"[GWFAbstractProcessEngine.changeRtProcessState] OPEN_NOTRUNNING_SUSPENDED, rtProcessUId="+rtProcess.getUId());
                            onPause(rtProcess);
                        } else throw new InvalidStateException("[GWFAbstractProcessEngine.changeRtProcessState] cannot change the state  ,UID="+rtProcess.getUId());
                        break;
                default : throw new InvalidStateException("[GWFProcessManagerBean.changeProcessState] Unknown runtime process state ="+toState);
            }
            return rtProcess;
        }catch(WorkflowException ex){
            throw ex;
        }catch(Throwable th){
            throw new SystemException("[GWFProcessManagerBean.changeProcessState] Error",th);
        }
    }

    /**
     * Creates runtime Process
     * @param Process
     * @param rtProcessUId runtime process UID
     * @return GWFRtProcess runtime Process
     */
    public GWFRtProcess createRtProcess(GWFRtProcess rtProcess) throws WorkflowException,SystemException {
        try {
            //creating runtime process
            rtProcess.setState(new Integer(GWFRtProcess.OPEN_NOTRUNNING));
            rtProcess = (GWFRtProcess) UtilEntity.createEntity(rtProcess, true);
            Logger.log(Logger.auditCategory,"[GWFAbstractProcessEngine.createRtProcess] OPEN_NOTRUNNING, rtProcessUId="+rtProcess.getUId());
            onCreate(rtProcess);
            return rtProcess;
        } catch (WorkflowException e) {
            throw e;
        } catch (Throwable th) {
        	logWarn("[GWFAbstractProcessEngine.createRtProcess] Exception",th);
            throw new SystemException("[GWFAbstractProcessEngine.createRtProcess] Exception", th);
        }
    }

    /**
     * Removes runtime Process
     * @param rtProcessUId
     * @throws GWFException
     */
    public void removeRtProcess(GWFRtProcess rtProcess) throws WorkflowException,SystemException {
        try {
            UtilEntity.remove(rtProcess.getUId(),GWFRtProcess.ENTITY_NAME,true);
            onRemove(rtProcess);
        } catch (WorkflowException e) {
            throw e;
        } catch (Throwable th) {
        	logWarn("[GWFAbstractProcessEngine.removeRtProcess] Exception",th);
            throw new SystemException("[GWFAbstractProcessEngine.removeRtProcess] Exception", th);
        }
    }


    public void selectRoute(GWFRtProcess rtProcess) throws WorkflowException,SystemException {
        try {
            int rtState = rtProcess.getState().intValue();

            if (rtState == IGWFRtProcess.OPEN_NOTRUNNING) {
                executeProcessNotRunning(rtProcess);
            } else if (rtState == IGWFRtProcess.OPEN_RUNNING) {
                executeProcessRunning(rtProcess);
            } else if (rtState == IGWFRtProcess.CLOSED_COMPLETED) {
                executeProcessCompleted(rtProcess);
            } else if (rtState == IGWFRtProcess.CLOSED_ABNORMALCOMPLETED) {
                executeProcessAborted(rtProcess);
                //throw new GWFException();
            } else if (rtState == IGWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED || rtState == IGWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED) {
                executeProcessAborted(rtProcess);
            }
        } catch (WorkflowException e) {
            throw e;
        } catch (Throwable th) {
        	logWarn("[GWFAbstractProcessEngine.selectRoute] Exception",th);
            throw new SystemException("[GWFAbstractProcessEngine.selectRoute] Exception", th);
        }

    }
    
    /**
     * TWX 16112006 Trigger the change of the status of the GWFRtProcess to OTC plug-in. Currently, we only
     * support the processType IBpssBinaryCollaboration.ENTITY_NAME & engine type IWorkflowConstants.BPSS_ENGINE
     * @param rtprocess
     */
    private void triggerProcessStateChangeStatus(GWFRtProcess rtprocess) throws SystemException
    {
      if(BpssBinaryCollaboration.ENTITY_NAME.equals(rtprocess.getProcessType()) 
               && IWorkflowConstants.BPSS_ENGINE.equals(rtprocess.getEngineType()))
      {
        ProcessTransactionNotifyHandler.triggerProcessTransaction(rtprocess);
      }
    }
    
/*
    private void logFlow(GWFRtProcess rtProcess, String state) {
        if (rtProcess != null)
            Logger.log(Logger.GWF_FLOW, "Process [UID=" + rtProcess.getProcessUId() + ",\t RtUID=" + rtProcess.getUId() + ",\t RtState=" + rtProcess.getState() + ",\t Type=" + rtProcess.getProcessType() + ",\t\t STATE=" + state + "]");
        else Logger.log(Logger.GWF_FLOW, "Process [STATE=RtProcess:" + state + "]");
    }
*/
    protected void logInfo(String msg){
        Logger.log(Logger.processCategory,msg);
    }

    protected void logDebug(String msg){
        Logger.debug(Logger.processCategory,msg);
    }

    protected void logWarn(String msg,Throwable th) {
    	Logger.warn(Logger.processCategory,msg,th);
    }

    /**
     * @deprecated Use logError(String,String,Throwable)
     * @param msg
     * @param th
     */
    protected void logError(String msg,Throwable th){
        Logger.err(Logger.processCategory,msg,th);
    }

    protected void logError(String errorCode, String msg, Throwable th) {
    	Logger.error(errorCode,Logger.processCategory, msg,th);
    }
}
