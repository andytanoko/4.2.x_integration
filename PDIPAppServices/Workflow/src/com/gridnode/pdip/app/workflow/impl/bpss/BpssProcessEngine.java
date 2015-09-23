/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    MAHESH              Created
 * Dec 14 2005    Tam Wei Xiang       To resolve the pass by value issue:
 *                                    use UtilEntity.updateEntity instead of
 *                                    UtilEntity.update
 */

package com.gridnode.pdip.app.workflow.impl.bpss;


import java.util.*;

import com.gridnode.pdip.app.workflow.engine.*;
import com.gridnode.pdip.app.workflow.exceptions.*;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.*;
import com.gridnode.pdip.app.workflow.notification.ProcessTransactionNotifyHandler;
import com.gridnode.pdip.app.workflow.runtime.helpers.*;
import com.gridnode.pdip.app.workflow.runtime.model.*;
import com.gridnode.pdip.app.workflow.util.*;
import com.gridnode.pdip.base.contextdata.entities.model.*;
import com.gridnode.pdip.base.contextdata.facade.exceptions.*;
import com.gridnode.pdip.base.contextdata.facade.ejb.*;
import com.gridnode.pdip.base.gwfbase.bpss.model.*;
import com.gridnode.pdip.base.gwfbase.bpss.helpers.*;
import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.*;


public class BpssProcessEngine extends GWFAbstractProcessEngine
{

    public IGWFRouteDispatcher getDispatcher()
    {
        return GWFFactory.getRouteDispatcher(GWFFactory.BPSS_ENGINE);
    }

    public void onCreate(GWFRtProcess rtProcess) throws WorkflowException, SystemException
    {
        try
        {
            //send message to RouteManager
            selectRoute(rtProcess);
        }
        catch (WorkflowException ex)
        {
            throw ex;
        }
        catch (SystemException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            Logger.warn("[BpssProcessEngine.onCreate] Exception", th);
            throw new SystemException("Exception: "+th.getMessage(), th);
        }
    }

    public void onRemove(GWFRtProcess rtProcess) throws WorkflowException, SystemException
    {
        try
        {
            try{
                if(rtProcess.getContextUId()!=null)
                    WorkflowUtil.getDataManager().removeContextUId(rtProcess.getContextUId());
            }catch(DataException ex){
            }catch(Throwable th){
                Logger.warn("[BpssProcessEngine.onRemove] Exception while removing context,contextUId"+rtProcess.getContextUId(),th);
            }

	IDataFilter filter = null;

	//remove all GWFRtRestriction
	try{
		filter = new DataFilterImpl();
		filter.addSingleFilter(null, GWFRtRestriction.RT_PROCESS_UID, filter.getEqualOperator(), rtProcess.getKey(), false);
		UtilEntity.remove(filter, GWFRtRestriction.ENTITY_NAME, true);
            	}catch(Throwable th){
                		Logger.warn("[BpssProcessEngine.onRemove] Exception while removing GWFRtRestriction of process "+rtProcess.getKey(),th);
            	}

            //remove all activities
            filter = new DataFilterImpl();


            filter.addSingleFilter(null, GWFRtActivity.RT_PROCESS_UID, filter.getEqualOperator(), rtProcess.getKey(), false);
            Collection rtActColl = UtilEntity.getEntityByFilter(filter, GWFRtActivity.ENTITY_NAME, true);

            for (Iterator iterator = rtActColl.iterator(); iterator.hasNext();)
            {
                GWFRtActivity rtActivity = (GWFRtActivity) iterator.next();

                GWFFactory.getActivityEngine(rtProcess.getEngineType()).remove(rtActivity);
            }

            if (BpssBinaryCollaboration.ENTITY_NAME.equals(rtProcess.getProcessType()))
            {
                //remove all processDocs
                filter = new DataFilterImpl();
                filter.addSingleFilter(null, GWFRtProcessDoc.RT_BINARY_COLLABORATION_UID, filter.getEqualOperator(), rtProcess.getKey(), false);
                Collection coll = UtilEntity.getEntityByFilter(filter, GWFRtProcessDoc.ENTITY_NAME, true);

                for (Iterator iterator = coll.iterator(); iterator.hasNext();)
                {
                    GWFRtProcessDoc rtProcessDoc=(GWFRtProcessDoc) iterator.next();
                    BpssBusinessTransHelper.clearConcurrentRequests(rtProcessDoc.getDocumentId());
                    BpssBusinessTransHelper.removeTimers((Long)rtProcessDoc.getKey());
                }
                String key = KeyConverter.getKey((Long) rtProcess.getKey(), rtProcess.getEntityName(), IWorkflowConstants.BPSS_ENGINE);

                WorkflowUtil.cancelAlarm(key, BpssBinaryCollaboration.ENTITY_NAME, IWorkflowConstants.BPSS_ENGINE);
                UtilEntity.remove(filter, GWFRtProcessDoc.ENTITY_NAME, true);
            }
        }
        catch (SystemException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            throw new SystemException("Exception while removing process, rtProcess=" + rtProcess + ": "+th.getMessage(), th);
        }
        return;
    }

    public void onBegin(GWFRtProcess rtProcess) throws WorkflowException, SystemException
    {
        try
        {
            //send message to RouteManager
            //getDispatcher().routeProcess((Long) rtProcess.getKey());
            selectRoute(rtProcess);
        }
        catch (WorkflowException e)
        {
            throw e;
        }
        catch (SystemException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            Logger.warn("[BpssProcessEngine.onBegin] Exception", th);
            throw new SystemException("Exception while starting a process: "+th.getMessage(), th);
        }
    }

    public void onComplete(GWFRtProcess rtProcess) throws WorkflowException, SystemException
    {
        try
        {
            if (rtProcess.getFinishInterval() != null && BpssBinaryCollaboration.ENTITY_NAME.equals(rtProcess.getProcessType()))
                WorkflowUtil.cancelAlarm(KeyConverter.getKey((Long) rtProcess.getKey(), rtProcess.getEntityName(), IWorkflowConstants.BPSS_ENGINE), BpssBinaryCollaboration.ENTITY_NAME, IWorkflowConstants.BPSS_ENGINE);

            //send message to RouteManager
            getDispatcher().routeProcess((Long) rtProcess.getKey());
        }
        catch (WorkflowException e)
        {
            throw e;
        }
        catch (SystemException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            Logger.warn("[BpssProcessEngine.onComplete] Exception", th);
            throw new SystemException("Exception while completing process: "+th.getMessage(), th);
        }
    }

    public void onAbort(GWFRtProcess rtProcess) throws WorkflowException, SystemException
    {
        try
        {
            if (rtProcess.getFinishInterval() != null && BpssBinaryCollaboration.ENTITY_NAME.equals(rtProcess.getProcessType()))
                WorkflowUtil.cancelAlarm(KeyConverter.getKey((Long) rtProcess.getKey(), rtProcess.getEntityName(), IWorkflowConstants.BPSS_ENGINE), BpssBinaryCollaboration.ENTITY_NAME, IWorkflowConstants.BPSS_ENGINE);
            //send message to RouteManager
            getDispatcher().routeProcess((Long) rtProcess.getKey());
        }
        catch (WorkflowException e)
        {
            throw e;
        }
        catch (SystemException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            Logger.warn("[BpssProcessEngine.onAbort] Exception", th);
            throw new SystemException("Exception while aborting process: "+th.getMessage(), th);
        }
    }

    public void onPause(GWFRtProcess rtProcess) throws WorkflowException, SystemException
    {
        try
        {
            //send message to RouteManager
            getDispatcher().routeProcess((Long) rtProcess.getKey());
        }
        catch (WorkflowException e)
        {
            throw e;
        }
        catch (Throwable th)
        {
            Logger.warn("[BpssProcessEngine.onPause] Exception", th);
            throw new SystemException("Exception while pausing process: "+th.getMessage(), th);
        }
    }

    public void onResume(GWFRtProcess rtProcess) throws WorkflowException, SystemException
    {
        try
        {
            //send message to RouteManager
            getDispatcher().routeProcess((Long) rtProcess.getKey());
        }
        catch (WorkflowException e)
        {
            throw e;
        }
        catch (Throwable th)
        {
            Logger.warn("[BpssProcessEngine.onResume] Exception", th);
            throw new SystemException("Exception while resuming process: "+ th.getMessage(), th);
        }
    }

    public void cancelProcess(GWFRtProcess rtProcess, Object reason) throws WorkflowException, SystemException
    {
        Logger.debug("[BpssProcessEngine.cancelProcess] rtProcess=" + rtProcess);
        try
        {
            if (rtProcess.getState().intValue() == GWFRtProcess.OPEN_NOTRUNNING || rtProcess.getState().intValue() == GWFRtProcess.OPEN_NOTRUNNING_SUSPENDED || rtProcess.getState().intValue() == GWFRtProcess.OPEN_RUNNING ||
            		rtProcess.getState().intValue() == GWFRtProcess.CLOSED_COMPLETED)
            {
                rtProcess.setEndTime(TimeUtil.localToUtcTimestamp());
                rtProcess.setState(new Integer(GWFRtProcess.CLOSED_ABNORMALCOMPLETED));
                
                rtProcess = (GWFRtProcess)UtilEntity.updateEntity(rtProcess, true);

                if (BpssBinaryCollaboration.ENTITY_NAME.equals(rtProcess.getProcessType()))
                {
                    //remove all processDocs
                    IDataFilter filter = new DataFilterImpl();

                    filter.addSingleFilter(null, GWFRtProcessDoc.RT_BINARY_COLLABORATION_UID, filter.getEqualOperator(), rtProcess.getKey(), false);
                    Collection coll = UtilEntity.getEntityByFilter(filter, GWFRtProcessDoc.ENTITY_NAME, true);

                    for (Iterator iterator = coll.iterator(); iterator.hasNext();)
                    {
                        GWFRtProcessDoc rtProcessDoc = (GWFRtProcessDoc) iterator.next();
                        int status = rtProcessDoc.getStatus().intValue();

                        if (status == GWFRtProcess.OPEN_NOTRUNNING || status == GWFRtProcess.OPEN_NOTRUNNING_SUSPENDED || status == GWFRtProcess.OPEN_RUNNING ||
                        		status == GWFRtProcess.CLOSED_COMPLETED)
                        {
                            BpssBusinessTransHelper.removeTimers((Long) rtProcessDoc.getKey());
                            rtProcessDoc.setStatus(new Integer(GWFRtProcess.CLOSED_ABNORMALCOMPLETED));
                            rtProcessDoc.setExceptionSignalType(IBpssConstants.EXCEPTION_CANCEL);
                            rtProcessDoc.setReason(reason);
                            
                            rtProcessDoc = (GWFRtProcessDoc)UtilEntity.updateEntity(rtProcessDoc, true);
                            
                            //TWX 15112006 Trigger the updated status of the Process to OTC.
                            //The initial state of the Process in OTC only can be OPEN_RUNNING, thus while we cancel the process in GT,
                            //we only trigger those state in CLOSED_COMPLETED or OPEN_RUNNING.
                            if(status == GWFRtProcess.CLOSED_COMPLETED || status == GWFRtProcess.OPEN_RUNNING)
                            {
                              ProcessTransactionNotifyHandler.triggerProcessTransaction(rtProcessDoc);
                            }
                        }
                    }
                    String key = KeyConverter.getKey((Long) rtProcess.getKey(), rtProcess.getEntityName(), IWorkflowConstants.BPSS_ENGINE);

                    WorkflowUtil.cancelAlarm(key, BpssBinaryCollaboration.ENTITY_NAME, IWorkflowConstants.BPSS_ENGINE);
                }

                IDataFilter filter = new DataFilterImpl();

                filter.addSingleFilter(null, GWFRtActivity.RT_PROCESS_UID, filter.getEqualOperator(), rtProcess.getKey(), false);
                Collection rtActColl = UtilEntity.getEntityByFilter(filter, GWFRtActivity.ENTITY_NAME, true);

                if (rtActColl != null && rtActColl.size() > 0)
                {
                    for (Iterator iterator = rtActColl.iterator(); iterator.hasNext();)
                    {
                        GWFRtActivity rtActivity = (GWFRtActivity) iterator.next();

                        GWFFactory.getActivityEngine(rtProcess.getEngineType()).cancelActivity(rtActivity, reason);
                    }
                }
            }
        }
        catch (WorkflowException e)
        {
            throw e;
        }
        catch (SystemException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            Logger.warn("[BpssProcessEngine.cancelProcess] Exception", th);
            throw new SystemException("Exception while cancelling process: "+th.getMessage(), th);
        }
    }

    protected void executeProcessNotRunning(GWFRtProcess rtProcess) throws WorkflowException, SystemException
    {
        try
        {
            //begin the runtime process
            getDispatcher().changeProcessState((Long) rtProcess.getKey(), GWFRtProcess.OPEN_RUNNING);
        }
        catch (WorkflowException e)
        {
            throw e;
        }
        catch (Throwable th)
        {
            Logger.warn("[BpssProcessEngine.executeProcessNotRunning] Exception", th);
            throw new SystemException("Exception while executing a not-running process: "+th.getMessage(), th);
        }
    }

    protected void executeProcessRunning(GWFRtProcess rtProcess) throws WorkflowException, SystemException
    {
        Logger.debug("[BpssProcessEngine.executeProcessRunning] rtProcess=" + rtProcess);
        try
        {
            //start the activity in the process
            //call create runtime activity by calling the ActivityManager
            if (rtProcess.getProcessType().equals(BpssBusinessTrans.ENTITY_NAME))
            {
                IDataManagerObj dataManager = WorkflowUtil.getDataManager();
                Long rtProcessDocUId = (Long) dataManager.getContextData(rtProcess.getContextUId(), new ContextKey("workflow.control.rtProcessDocUId"));
                GWFRtProcessDoc rtProcessDoc = (GWFRtProcessDoc) UtilEntity.getEntityByKey(rtProcessDocUId, GWFRtProcessDoc.ENTITY_NAME, true);
                //String documentId = rtProcessDoc.getDocumentId();

                rtProcessDoc.setRtBusinessTransactionUId((Long) rtProcess.getKey());
                //HashMap contextData = new HashMap();
                String defCacheKey = (String) dataManager.getContextData(rtProcess.getContextUId(), new ContextKey("workflow.control.defCacheKey"));
                BpssDefinitionCache defCache = BpssDefinitionCache.getBpssDefinitionCache(defCacheKey);
                BpssBusinessTrans bpssBusinessTran = (BpssBusinessTrans) defCache.getDefinationEntity(rtProcess.getProcessUId(), BpssBusinessTrans.ENTITY_NAME);

                Collection docEnvelopes = defCache.getChildEntities(bpssBusinessTran.getBpssReqBusinessActivityUId(), BpssReqBusinessActivity.ENTITY_NAME, BpssDocumentEnvelope.ENTITY_NAME);
                BpssDocumentEnvelope docEnvelope = (BpssDocumentEnvelope) docEnvelopes.iterator().next();//since always reqDocument exists

                rtProcessDoc.setRequestDocType(docEnvelope.getBusinessDocumentIDRef());

                docEnvelopes = defCache.getChildEntities(bpssBusinessTran.getBpssResBusinessActivityUId(), BpssResBusinessActivity.ENTITY_NAME, BpssDocumentEnvelope.ENTITY_NAME);
                if (docEnvelopes != null && docEnvelopes.size() > 0)
                {
                    HashMap resTypeMap = new HashMap(docEnvelopes.size());

                    for (Iterator iterator = docEnvelopes.iterator(); iterator.hasNext();)
                    {
                        docEnvelope = (BpssDocumentEnvelope) iterator.next();
                        Logger.debug("[BpssProcessEngine.executeProcessRunning] in 7 docEnvelope=" + docEnvelope);
                        resTypeMap.put(docEnvelope.getBusinessDocumentIDRef(), docEnvelope.getIsPositiveResponse().toString());
                    }
                    rtProcessDoc.setResponseDocTypes(UtilString.mapToStr(resTypeMap));
                }
                Logger.debug("[BpssProcessEngine.executeProcessRunning] BEFORE UPDATE ,rtProcessDoc=" + rtProcessDoc);
                
                rtProcessDoc = (GWFRtProcessDoc)UtilEntity.updateEntity(rtProcessDoc, true);
                Logger.debug("[BpssProcessEngine.executeProcessRunning] AFTER UPDATE ,rtProcessDoc=" + rtProcessDoc);

                String roleType = rtProcessDoc.getRoleType();

                if (roleType != null && roleType.equals(IBpssConstants.INITIATING_ROLE))
                {
                    //start the Requesting Activity if the roleType is InitiatingRole
                    BpssTransition bpssTransition = new BpssTransition();

                    bpssTransition.setProcessType(rtProcess.getProcessType());
                    bpssTransition.setToBusinessStateKey(KeyConverter.getKey(bpssBusinessTran.getBpssReqBusinessActivityUId(), BpssReqBusinessActivity.ENTITY_NAME, "wfActivity"));
                    BpssRouteHelper.processTransition(bpssTransition, null, new Long(rtProcess.getUId()));
                } else if (roleType != null && roleType.equals(IBpssConstants.RESPONDING_ROLE))
                {
                    //start the Responding Activity if the roleType is RespondingRole
                    BpssTransition bpssTransition = new BpssTransition();

                    bpssTransition.setProcessType(rtProcess.getProcessType());
                    bpssTransition.setToBusinessStateKey(KeyConverter.getKey(bpssBusinessTran.getBpssResBusinessActivityUId(), BpssResBusinessActivity.ENTITY_NAME, "wfActivity"));
                    BpssRouteHelper.processTransition(bpssTransition, null, new Long(rtProcess.getUId()));
                } else throw new WorkflowException("[BpssProcessEngine.executeProcessRunning] roleType should be either InitiatingRole,RespondingRole, but roleType=" + roleType);
            } else if (rtProcess.getProcessType().equals(BpssMultiPartyCollaboration.ENTITY_NAME))
            {
                //if the process is of MultiPartyCollaboration type then start the all
                //BpssBinaryCollaborationActivity in this process and then create the runtime for this process.
                Iterator iterator = BpssRouteHelper.getBinaryCollaborationActivities(rtProcess).iterator();

                while (iterator.hasNext())
                {
                    BpssBinaryCollaborationActivity activity = (BpssBinaryCollaborationActivity) iterator.next();
                    BpssTransition bpssTransition = new BpssTransition();

                    bpssTransition.setProcessType(rtProcess.getProcessType());
                    bpssTransition.setToBusinessStateKey(KeyConverter.getKey(new Long(activity.getUId()), BpssBinaryCollaborationActivity.ENTITY_NAME, "wfActivity"));
                    BpssRouteHelper.processTransition(bpssTransition, null, new Long(rtProcess.getUId()));
                }
            } else if (rtProcess.getProcessType().equals(BpssBinaryCollaboration.ENTITY_NAME))
            {

                IDataManagerObj dataManager = WorkflowUtil.getDataManager();
                String defCacheKey = (String) dataManager.getContextData(rtProcess.getContextUId(), new ContextKey("workflow.control.defCacheKey"));
                BpssDefinitionCache defCache = BpssDefinitionCache.getBpssDefinitionCache(defCacheKey);

                BpssBinaryCollaboration bpssBinaryCollaboration = (BpssBinaryCollaboration) defCache.getDefinationEntity(rtProcess.getProcessUId(), BpssBinaryCollaboration.ENTITY_NAME);
                String timeToPerform = bpssBinaryCollaboration.getTimeToPerform();

                if (timeToPerform != null && timeToPerform.trim().length() > 0)
                {
                    long finishInterval = WorkflowUtil.getTimeInterval(timeToPerform);

                    rtProcess.setFinishInterval(new Long(finishInterval));
                    rtProcess = (GWFRtProcess)UtilEntity.updateEntity(rtProcess, true);
                    String key = KeyConverter.getKey((Long) rtProcess.getKey(), rtProcess.getEntityName(), IWorkflowConstants.BPSS_ENGINE);

                    WorkflowUtil.addAlarm(key, BpssBinaryCollaboration.ENTITY_NAME, IWorkflowConstants.BPSS_ENGINE, finishInterval);
                }

                //start using BpssStart
                BpssStart bpssStart = null;
                Collection startColl = defCache.getBpssStartColl(rtProcess.getProcessUId());

                for (Iterator iterator = startColl.iterator(); iterator.hasNext();)
                {
                    BpssStart tmpBpssStart = (BpssStart) iterator.next();

                    if (tmpBpssStart.getIsDownLink() == null || !tmpBpssStart.getIsDownLink().booleanValue())
                    {
                        bpssStart = tmpBpssStart;
                        break;
                    }
                }

                if (bpssStart == null)
                {
                    Logger.debug("[BpssProcessEngine.executeProcessRunning] No BpssStart for BinaryClollaboration uid=" + rtProcess.getProcessUId());
                } else
                {
                    // increment the max concurrency by 1 since BpssStart
                    rtProcess = GWFRuntimeUtil.addMaxConcurrency((Long) rtProcess.getKey(), 1);

                    BpssTransition bpssTransition = new BpssTransition();

                    bpssTransition.setProcessType(rtProcess.getProcessType());
                    bpssTransition.setToBusinessStateKey(bpssStart.getToBusinessStateKey());
                    BpssRouteHelper.processTransition(bpssTransition, null, new Long(rtProcess.getUId()));
                }
            }
        }
        catch (WorkflowException e)
        {
            throw e;
        }
        catch (SystemException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            Logger.warn("[BpssProcessEngine.executeProcessRunning] Exception", th);
            throw new SystemException("Exception while executing running process: "+th.getMessage(), th);
        }
    }

    protected void executeProcessCompleted(GWFRtProcess rtProcess) throws WorkflowException, SystemException
    {
        try
        {
            logDebug("[BpssProcessEngine.executeProcessCompleted] , Before removeing it :" + rtProcess);

            if (BpssBusinessTrans.ENTITY_NAME.equals(rtProcess.getProcessType()))
            {
                executeBusinessTransCompleted(rtProcess);
            }

            if (rtProcess.getParentRtActivityUId() != null)
            {
                copyContextToParentActivity(rtProcess);
                getDispatcher().changeActivityState(rtProcess.getParentRtActivityUId(), GWFRtActivity.CLOSED_COMPLETED);
            }
            // end of process
        }
        catch (WorkflowException e)
        {
            throw e;
        }
        catch (SystemException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            Logger.warn("[BpssProcessEngine.executeProcessCompleted] Exception", th);
            throw new SystemException("Exception while executing completed process: "+th.getMessage(), th);
        }
    }

    protected void executeProcessAborted(GWFRtProcess rtProcess) throws WorkflowException, SystemException
    {
        try
        {
            if (BpssBusinessTrans.ENTITY_NAME.equals(rtProcess.getProcessType()))
            {
                executeBusinessTransCompleted(rtProcess);
            }

            if (rtProcess.getParentRtActivityUId() != null)
            {
                copyContextToParentActivity(rtProcess);
                getDispatcher().changeActivityState(rtProcess.getParentRtActivityUId(), GWFRtActivity.CLOSED_ABNORMALCOMPLETED);
            }
        }
        catch (WorkflowException e)
        {
            throw e;
        }
        catch (SystemException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            Logger.warn("[BpssProcessEngine.executeProcessAborted] Exception", th);
            throw new SystemException("Exception while executing aborted process: "+th.getMessage(), th);
        }
    }

    private void executeBusinessTransCompleted(GWFRtProcess rtProcess) throws SystemException
    {
        try
        {
            String conditionGuard = getConditionGuard(rtProcess.getState());

            //set the GWFRtProcessDoc status to completed
            IDataFilter filter = new DataFilterImpl();

            filter.addSingleFilter(null, GWFRtProcessDoc.RT_BUSINESS_TRANSACTION_UID, filter.getEqualOperator(), rtProcess.getKey(), false);
            GWFRtProcessDoc rtProcessDoc = (GWFRtProcessDoc) UtilEntity.getEntityByFilter(filter, GWFRtProcessDoc.ENTITY_NAME, true).iterator().next();

            if (conditionGuard != null && conditionGuard.equals("Success") && !rtProcessDoc.getIsPositiveResponse().booleanValue())
            {
                rtProcessDoc.setStatus(new Integer(GWFRtProcess.CLOSED_ABNORMALCOMPLETED));
                UtilEntity.update(rtProcessDoc, true);
                conditionGuard = "BusinessFailure";
            }
            BpssBusinessTransHelper.removeTimers((Long) rtProcessDoc.getKey());
            //set the conditionGuard to context
            WorkflowUtil.getDataManager().setContextData(rtProcess.getContextUId(), new ContextKey("conditionGuard"), conditionGuard);
        }
        catch (SystemException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            throw new SystemException("Exception: "+th.getMessage(), th);
        }
    }

    private void copyContextToParentActivity(GWFRtProcess rtProcess) throws SystemException
    {
        try
        {
            if (rtProcess.getParentRtActivityUId() != null)
            {
                GWFRtActivity rtActivity = (GWFRtActivity) UtilEntity.getEntityByKey(rtProcess.getParentRtActivityUId(), GWFRtActivity.ENTITY_NAME, true);
                IDataManagerObj dataManager = WorkflowUtil.getDataManager();
                HashMap contextData = dataManager.getContextData(rtProcess.getContextUId());

                //remove all control data from the sub context which had to be passed to parent context
                if (contextData != null)
                {
                    HashMap newCtx = new HashMap();

                    for (Iterator iterator = contextData.keySet().iterator(); iterator.hasNext();)
                    {
                        Object key = iterator.next();

                        if (key != null && !key.toString().startsWith("workflow.control."))
                        {
                            newCtx.put(key, contextData.get(key));
                        }
                    }
                    if (newCtx.size() > 0)
                        dataManager.setContextData(rtActivity.getContextUId(), newCtx);
                }
                //remove the current context
                //dataManager.removeContextData(rtProcess.getContextUId());
            }
        }
        catch (SystemException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            throw new SystemException("Exception while copying context to parent activity: "+th.getMessage(), th);
        }
    }

    private String getConditionGuard(Integer processState)
    {
        int currState = processState.intValue();
        String conditionGuard = null;

        if (currState == GWFRtProcess.CLOSED_COMPLETED)
        {
            conditionGuard = "Success";
        } else if (currState == GWFRtProcess.CLOSED_ABNORMALCOMPLETED)
        {
            conditionGuard = "BusinessFailure";
        } else if (currState == GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED)
        {
            conditionGuard = "TechnicalFailure";
        } else if (currState == GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED)
        {
            conditionGuard = "AnyFailure";
        }
        return conditionGuard;
    }
   
}
