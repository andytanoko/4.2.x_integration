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
 */
package com.gridnode.pdip.app.workflow.engine.ejb;


import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.app.workflow.engine.GWFFactory;
import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.domain.GWFException;
import com.gridnode.pdip.framework.util.UtilEntity;
 
public class GWFProcessManagerBean implements SessionBean {
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3182453165478719548L;
		private SessionContext _sessionCtx = null;

    public void setSessionContext(SessionContext parm1) {
        _sessionCtx = parm1;
    }

    public void ejbCreate() throws CreateException {
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    /**
     * Creates runtime Process
     * @param Process
     * @param rtProcessUId runtime process UID
     * @return GWFRtProcess runtime Process
     * @throws GWFException
     */
    public GWFRtProcess createRtProcess(GWFRtProcess rtProcess) throws WorkflowException ,SystemException {
        String engineType=rtProcess.getEngineType();
        return GWFFactory.getProcessEngine(engineType).createRtProcess(rtProcess);
    }

    /**
     * calls the process engine to change the state of the process
     * @param rtProcessUId
     * @param state
     * @return GWFRtProcess
     */
    public GWFRtProcess changeProcessState(Long rtProcessUId, Integer state) throws WorkflowException,SystemException {
        GWFRtProcess rtProcess=getRtProcess(rtProcessUId);
        String engineType=rtProcess.getEngineType();
        return GWFFactory.getProcessEngine(engineType).changeRtProcessState(rtProcess,state);
    }

    /**
     * calls the process engine to route process depending on the state
     * @param rtProcessUId
     * @throws GWFException
     */
    public void selectProcessRoute(Long rtProcessUId) throws WorkflowException,SystemException {
        GWFRtProcess rtProcess=getRtProcess(rtProcessUId);
        String engineType=rtProcess.getEngineType();
        GWFFactory.getProcessEngine(engineType).selectRoute(rtProcess);
    }

    /**
     * Cancel runtime Process
     * @param rtProcessUId
     * @param reason
     */
    public void cancelRtProcess(Long rtProcessUId,Object reason) throws WorkflowException, SystemException {
        GWFRtProcess rtProcess=getRtProcess(rtProcessUId);
        String engineType=rtProcess.getEngineType();
        GWFFactory.getProcessEngine(engineType).cancelProcess(rtProcess,reason);
    }


    /**
     * Removes runtime Process
     * @param rtProcessUId
     */
    public void removeRtProcess(Long rtProcessUId) throws WorkflowException, SystemException {
        GWFRtProcess rtProcess=getRtProcess(rtProcessUId);
        String engineType=rtProcess.getEngineType();
        GWFFactory.getProcessEngine(engineType).removeRtProcess(rtProcess);
    }


    /**
     * helper method to reterive runtime process
     */
    public GWFRtProcess getRtProcess(Long rtProcessUId) throws SystemException {
        try{
            return (GWFRtProcess)UtilEntity.getEntityByKey(rtProcessUId,GWFRtProcess.ENTITY_NAME,true);
        }catch(Throwable th){
            throw new SystemException("[GWFProcessManagerBean.getRtProcess] Error rtProcessUId"+rtProcessUId,th);
        }
    }



    //*********************************************
    /*
     public String createProcessInstance(boolean responseRequired,String key,boolean startImmediately,String observerKey,String name,String subject,String desc,Object contextData,String engineType) throws Exception {
     Long uId=KeyConverter.getUID(key);
     String entityName=KeyConverter.getEntityName(key);
     GWFRtProcess rtProcess=createRtProcess((GWFProcess)UtilEntity.getEntityByKey(uId,entityName,true),null,engineType);
     return rtProcess.getKey().toString();
     }

     public String changeProcessInstanceState(boolean responseRequired,String key,String state) throws Exception {
     String tempStr=null;
     Long uId=KeyConverter.getUID(key);
     String entityName=KeyConverter.getEntityName(key);
     if(uId==null || entityName==null)
     throw new WFXMLException(WFXMLException.WF_INVALID_KEY);
     GWFRtProcess rtProcess=(GWFRtProcess)UtilEntity.getEntityByKey(uId,entityName,true);
     Integer rtState=rtProcess.getState();

     switch(rtState.intValue()){

     case GWFRtProcess.OPEN_NOTRUNNING:
     if(getIntState(state)==GWFRtProcess.OPEN_RUNNING)
     rtProcess.begin();
     else throw new WFXMLException(WFXMLException.WF_INVALID_STATE_TRANSITION);
     break;
     case GWFRtProcess.OPEN_RUNNING :
     int tempState=getIntState(state);
     if(tempState==GWFRtProcess.CLOSED_COMPLETED)
     rtProcess.complete();
     else if(tempState==GWFRtProcess.CLOSED_ABNORMALCOMPLETED ||
     tempState==GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED ||
     tempState==GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED )
     rtProcess.abort(tempState);
     else throw new WFXMLException(WFXMLException.WF_INVALID_STATE_TRANSITION);
     break;
     case GWFRtProcess.OPEN_NOTRUNNING_SUSPENDED :
     if(getIntState(state)==GWFRtProcess.OPEN_RUNNING)
     rtProcess.resume();
     else if(getIntState(state)==GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED)
     rtProcess.abort(GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED);
     else throw new WFXMLException(WFXMLException.WF_INVALID_STATE_TRANSITION);
     break;
     case GWFRtProcess.CLOSED_ABNORMALCOMPLETED :
     if(getIntState(state)==GWFRtProcess.OPEN_RUNNING)
     rtProcess.resume();
     else if(getIntState(state)==GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED)
     rtProcess.abort(GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED);
     else throw new WFXMLException(WFXMLException.WF_INVALID_STATE_TRANSITION);
     break;
     case GWFRtProcess.CLOSED_COMPLETED :
     case GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED :
     case GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED :
     throw new WFXMLException(WFXMLException.WF_INVALID_STATE_TRANSITION);
     }
     return getStrState(rtProcess.getState().intValue());
     }

     public String getStrState(int state){
     if(state==GWFRtProcess.OPEN_NOTRUNNING)
     return "open.notrunning";
     else if(state==GWFRtProcess.OPEN_RUNNING)
     return "open.running";
     else if(state==GWFRtProcess.CLOSED_COMPLETED)
     return "closed.completed";
     else if(state==GWFRtProcess.OPEN_NOTRUNNING_SUSPENDED)
     return "open.notrunning.suspended";
     else if(state==GWFRtProcess.CLOSED_ABNORMALCOMPLETED)
     return "closed.abnormalCompleted";
     else if(state==GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED)
     return "closed.abnormalCompleted.terminated";
     else if(state==GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED)
     return "closed.abnormalCompleted.aborted";
     return null; // error state
     }

     public int getIntState(String state){
     if(state.equalsIgnoreCase("OPEN.NOTRUNNING"))
     return GWFRtProcess.OPEN_NOTRUNNING;
     else if(state.equalsIgnoreCase("OPEN.RUNNING"))
     return GWFRtProcess.OPEN_RUNNING;
     else if(state.equalsIgnoreCase("CLOSED.COMPLETED"))
     return GWFRtProcess.CLOSED_COMPLETED;
     else if(state.equalsIgnoreCase("OPEN.NOTRUNNING.SUSPENDED"))
     return GWFRtProcess.OPEN_NOTRUNNING_SUSPENDED;
     else if(state.equalsIgnoreCase("CLOSED.ABNORMALCOMPLETED"))
     return GWFRtProcess.CLOSED_ABNORMALCOMPLETED;
     else if(state.equalsIgnoreCase("CLOSED.ABNORMALCOMPLETED.TERMINATED"))
     return GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED;
     else if(state.equalsIgnoreCase("CLOSED.ABNORMALCOMPLETED.ABORTED"))
     return GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED;
     return -1; // error state
     }

     */
}
