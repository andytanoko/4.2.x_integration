package com.gridnode.pdip.app.workflow.client;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;

import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerHome;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerObj;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.base.contextdata.facade.ejb.IDataManagerHome;
import com.gridnode.pdip.base.contextdata.facade.ejb.IDataManagerObj;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class WorkflowClient {
    String _configName;
    IGWFWorkflowManagerObj _workflowManager=null;

    public WorkflowClient() throws SystemException{
        this(null);
    }

    public WorkflowClient(String configName) throws SystemException{
        _configName=configName;
        getWorkflowManager();
    }

    /**
     * This method allows to create the runtime process
     * @param processDefKey This is the process definition key
     * @param rtActivityUId This is the parent activity which started this process
     * @param contextData This is the context data for this process
     * @return The contextUId for this process instance
     * @throws WorkflowException
     * @throws SystemException
     */
    public Long createRtProcess(String processDefKey, Long rtActivityUId,HashMap contextData) throws WorkflowException,SystemException{
        try{
            return getWorkflowManager().createRtProcess(processDefKey, rtActivityUId,contextData);
        }catch(RemoteException ex){
            throw new SystemException("[WorkflowClient.createRtProcess] Error ",ex);
        }

    }


    /**
     * This method allows to create the runtime process
     * @param processUId This is the process definition uid
     * @param processType This is the processtype
     * @param rtActivityUId This is the parent activity which started this process
     * @param contextData This is the context data for this process
     * @param engineType This is engine type XPDL or BPSS
     * @return The contextUId for this process instance
     * @throws WorkflowException
     * @throws SystemException
     */
    public Long createRtProcess(Long processUId, String processType, Long rtActivityUId,HashMap contextData,String engineType) throws WorkflowException,SystemException{
        try{
            return getWorkflowManager().createRtProcess(processUId, processType, rtActivityUId,contextData,engineType);
        }catch(RemoteException ex){
            throw new SystemException("[WorkflowClient.createRtProcess] Error ",ex);
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
    public void createRtActivity(Long activityUId, String activityType, Long rtProcessUId,String branchName,Long contextUId,String engineType) throws WorkflowException,SystemException{
        try{
            getWorkflowManager().createRtActivity(activityUId, activityType, rtProcessUId,branchName,contextUId,engineType);
        }catch(RemoteException ex){
            throw new SystemException("[WorkflowClient.createRtActivity] Error ",ex);
        }
    }

    /**
     * This method changes the process state
     * @param rtProcessUId This is the runtime process uid
     * @param state This is the new state for this process
     * @param engineType This is engine type XPDL or BPSS
     * @throws WorkflowException
     * @throws SystemException
     */
    public void changeProcessState(Long rtProcessUId,int state,HashMap contextData) throws WorkflowException,SystemException{
        try{
            getWorkflowManager().changeProcessState(rtProcessUId,state,contextData);
        }catch(RemoteException ex){
            throw new SystemException("[WorkflowClient.changeProcessState] Error ",ex);
        }
    }

    /**
     * This method changes the activity state
     * @param rtActivityUId This is the runtime activity uid
     * @param state This is the new state for this activity
     * @param engineType This is engine type XPDL or BPSS
     * @throws WorkflowException
     * @throws SystemException
     */
    public void changeActivityState(Long rtActivityUId,int state,HashMap contextData) throws WorkflowException,SystemException{
        try{
            getWorkflowManager().changeActivityState(rtActivityUId,state,contextData);
        }catch(RemoteException ex){
            throw new SystemException("[WorkflowClient.changeActivityState] Error ",ex);
        }
    }

    /**
     * Returns DataManager
     * @return IDataManagerObj
     * @throws ServiceLookupException
     */
    public IDataManagerObj getDataManager() throws SystemException {
        try{
            //return ((IDataManagerHome)ServiceLookup.getInstance(_configName).getHome(IDataManagerHome.class)).create();
        	return (IDataManagerObj)ServiceLocator.instance(_configName).getObj(
        	           IDataManagerHome.class.getName(),
        	           IDataManagerHome.class,
        	           new Object[0]);
        }catch(Exception e){
            throw new SystemException("[WorkflowClient.getDataManager] unable to create IDataManagerObj ",e);
        }
    }

    /**
     * This method inserts doccument into the engine
     * used for BPSS
     * @param processInstanceKey
     * @param processDefinitionKey
     * @param documentType
     * @param documentObject
     * @throws WorkflowException
     * @throws SystemException
     */
    public void insertDocument(String documentId,String documentType,Object documentObject,String senderKey) throws WorkflowException,SystemException{
        try{
            getWorkflowManager().insertDocument(documentId,documentType,documentObject,senderKey);
        }catch(RemoteException ex){
            throw new SystemException("[WorkflowClient.insertDocument] Error ",ex);
        }
    }


    public void insertDocument(String documentId,String documentType,Object documentObject,String senderKey,String initiatorPartnerKey,String responderPartnerKey) throws WorkflowException,SystemException{
        try{
            getWorkflowManager().insertDocument(documentId,documentType,documentObject,senderKey,initiatorPartnerKey,responderPartnerKey);
        }catch(RemoteException ex){
            throw new SystemException("[WorkflowClient.insertDocument] Error ",ex);
        }
    }

    /**
     * When signal is received from other side then this method is called
     * @param documentId
     * @param signalType
     * @param reason
     * @param senderKey
     * @throws WorkflowException
     * @throws SystemException
     */
    public void insertSignal(String documentId,String signalType,Object reason,String senderKey) throws WorkflowException,SystemException{
        try{
            getWorkflowManager().insertSignal(documentId,signalType,reason,senderKey);
        }catch(RemoteException ex){
            throw new SystemException("[WorkflowClient.insertSignal] Error ",ex);
        }
    }

    //----------------------------------------
    //For Process instance Management


    public Collection getXpdlProcessInstanceList(String processId,String packageId,String pkgVersionId) throws WorkflowException,SystemException{
        try{
            return getWorkflowManager().getXpdlProcessInstanceList(processId,packageId,pkgVersionId);
        }catch(RemoteException ex){
            throw new SystemException("[WorkflowClient.getXpdlProcessInstanceList] Error wirh params (processId,packageId,pkgVersionId)=("+processId+","+packageId+","+pkgVersionId+")",ex);
        }
    }

    public Collection getBpssProcessInstanceList(String processId,String processType,String processSpecName,String processSpecVersion,String processSpecUUid) throws WorkflowException,SystemException{
        try{
            return getWorkflowManager().getBpssProcessInstanceList(processId,processType,processSpecName,processSpecVersion,processSpecUUid);
        }catch(RemoteException ex){
            throw new SystemException("[WorkflowClient.getBpssProcessInstanceList] Error wirh params (processId,processType,processSpecName,processSpecVersion,processSpecUUid)=("+processId+","+processType+","+processSpecName+","+processSpecVersion+","+processSpecUUid+")",ex);
        }
    }


    public GWFRtProcess getProcessInstance(Long rtProcessUId) throws WorkflowException,SystemException{
        try{
            return getWorkflowManager().getProcessInstance(rtProcessUId);
        }catch(Throwable th){
            throw new SystemException("[WorkflowClient.getProcessInstance] Error ",th);
        }
    }

    public Collection getProcessInstanceList(IDataFilter filter) throws WorkflowException,SystemException{
        try{
            return getWorkflowManager().getProcessInstanceList(filter);
        }catch(Throwable th){
            throw new SystemException("[WorkflowClient.getProcessInstanceList] Error ",th);
        }
    }

    public void cancelProcessInstance(Long rtProcessUId,Object reason) throws WorkflowException,SystemException{
        try{
            getWorkflowManager().cancelProcessInstance(rtProcessUId,reason);
        }catch(RemoteException ex){
            throw new SystemException("[WorkflowClient.cancelProcessInstance] Error ",ex);
        }
    }

    public void removeProcessInstance(Long rtProcessUId) throws WorkflowException,SystemException{
        try{
            getWorkflowManager().removeProcessInstance(rtProcessUId);
        }catch(RemoteException ex){
            throw new SystemException("[WorkflowClient.removeProcessInstance] Error ",ex);
        }
    }

    public Collection getRtProcessDocList(Long rtProcessUId) throws WorkflowException,SystemException{
        try{
            return getWorkflowManager().getRtProcessDocList(rtProcessUId);
        }catch(RemoteException ex){
            throw new SystemException("[WorkflowClient.getRtProcessDocList] Error ",ex);
        }
    }

    protected IGWFWorkflowManagerObj getWorkflowManager() throws SystemException{
        if(_workflowManager==null){
            try{
                //IGWFWorkflowManagerHome home=(IGWFWorkflowManagerHome) ServiceLookup.getInstance(_configName).getHome(IGWFWorkflowManagerHome.class);
                //_workflowManager=home.create();
            	_workflowManager = (IGWFWorkflowManagerObj)ServiceLocator.instance(_configName).getObj(
            	                      IGWFWorkflowManagerHome.class.getName(),
            	                      IGWFWorkflowManagerHome.class,
            	                      new Object[0]);
            }catch(Exception e){
                throw new SystemException("[WorkflowClient.getWorkflowManager] unable to create IGWFWorkflowManagerObject ",e);
            }
        }
        return _workflowManager;
    }

}