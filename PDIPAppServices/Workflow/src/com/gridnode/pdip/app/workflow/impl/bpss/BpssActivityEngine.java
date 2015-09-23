/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.a
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    MAHESH              Created
 * Dec 14 2005    Tam Wei Xiang       To resolve the pass by value issue:
 *                                    use UtilEntity.updateEntity instead of
 *                                    UtilEntity.update
 * Sep 07 2011    Tam Wei Xiang       #2594: Make the process of processRequestDocument
 *                                    and processConcurrentRequests under the same lock "block".
 *                                                                   
 * Feb 28 2007    Tam Wei Xiang       SVN:T1 : To include the prefix in the 
 *                                             Process Instance ID we generate.
 *                                             The prefix will be the GridNode ID.                                   
 */

package com.gridnode.pdip.app.workflow.impl.bpss;

 
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.gridnode.pdip.app.workflow.engine.GWFAbstractActivityEngine;
import com.gridnode.pdip.app.workflow.engine.GWFFactory;
import com.gridnode.pdip.app.workflow.engine.IGWFRouteDispatcher;
import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.*;
import com.gridnode.pdip.app.workflow.notification.ProcessTransactionNotifyHandler;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.base.contextdata.entities.model.ContextKey;
import com.gridnode.pdip.base.contextdata.facade.ejb.IDataManagerObj;
import com.gridnode.pdip.base.contextdata.facade.exceptions.DataException;
import com.gridnode.pdip.base.gwfbase.baseentity.GWFActivity;
import com.gridnode.pdip.base.gwfbase.bpss.helpers.BpssDefinitionCache;
import com.gridnode.pdip.base.gwfbase.bpss.model.*;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.KeyConverter;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.pdip.framework.util.UtilEntity;


public class BpssActivityEngine extends GWFAbstractActivityEngine
{

    public IGWFRouteDispatcher getDispatcher()
    {
        return GWFFactory.getRouteDispatcher(GWFFactory.BPSS_ENGINE);
    }

    public void onCreate(GWFRtActivity rtActivity) throws WorkflowException, SystemException
    {
        try
        {
            Logger.debug("[BpssActivityEngine.onCreate] " + rtActivity);
            //getDispatcher().routeActivity((Long) rtActivity.getKey());
            selectRoute(rtActivity);
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
            Logger.warn("[BpssActivityEngine.onCreate] Exception", th);
            throw new SystemException("Exception while creating activity: "+th.getMessage(), th);
        }
    }

    public void onRemove(GWFRtActivity rtActivity) throws WorkflowException, SystemException
    {
        try
        {
            IDataFilter filter = new DataFilterImpl();

            filter.addSingleFilter(null, GWFRtProcess.PARENT_RTACTIVITY_UID, filter.getEqualOperator(), rtActivity.getKey(), false);
            Collection rtProcessColl = UtilEntity.getEntityByFilter(filter, GWFRtProcess.ENTITY_NAME, true);

            for (Iterator iterator = rtProcessColl.iterator(); iterator.hasNext();)
            {
                GWFRtProcess rtProcess = (GWFRtProcess) iterator.next();

                GWFFactory.getProcessEngine(rtActivity.getEngineType()).removeRtProcess(rtProcess);
            }
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
            throw new SystemException("Exception while removing activity: "+th.getMessage(), th);
        }
        return;
    }

    public void onBegin(GWFRtActivity rtActivity) throws WorkflowException, SystemException
    {
        Logger.debug("[BpssActivityEngine.onBegin] " + rtActivity);
        try
        {
            //getDispatcher().routeActivity((Long) rtActivity.getKey());
            selectRoute(rtActivity);
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
            logWarn("[BpssActivityEngine.onBegin] Exception", th);
            throw new SystemException("Exception while starting activity: "+th.getMessage(), th);
        }
    }

    public void onComplete(GWFRtActivity rtActivity) throws WorkflowException, SystemException
    {
        try
        {
            String activityType = rtActivity.getActivityType();

            if (rtActivity.getFinishInterval() != null && BpssBusinessTransActivity.ENTITY_NAME.equals(activityType))
                WorkflowUtil.cancelAlarm(KeyConverter.getKey((Long) rtActivity.getKey(), rtActivity.getEntityName(), IWorkflowConstants.BPSS_ENGINE), BpssBusinessTransActivity.ENTITY_NAME, IWorkflowConstants.BPSS_ENGINE);

            getDispatcher().routeActivity((Long) rtActivity.getKey());
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
        	logWarn("[BpssActivityEngine.onComplete] Exception", th);
            throw new SystemException("Exception while completing activity: "+th.getMessage(), th);
        }
    }

    public void onAbort(GWFRtActivity rtActivity) throws WorkflowException, SystemException
    {
        logDebug("[BpssActivityEngine.onAbort] rtActivity=" + rtActivity);
        try
        {
            if (rtActivity.getFinishInterval() != null && BpssBusinessTransActivity.ENTITY_NAME.equals(rtActivity.getActivityType()))
                WorkflowUtil.cancelAlarm(KeyConverter.getKey((Long) rtActivity.getKey(), rtActivity.getEntityName(), IWorkflowConstants.BPSS_ENGINE), BpssBusinessTransActivity.ENTITY_NAME, IWorkflowConstants.BPSS_ENGINE);
            getDispatcher().routeActivity((Long) rtActivity.getKey());
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
        	logWarn("[BpssActivityEngine.onAbort] Exception", th);
            throw new SystemException("Exception while aborting activity: "+th.getMessage(), th);
        }
    }

    public void onPause(GWFRtActivity rtActivity) throws WorkflowException, SystemException
    {
        try
        {
            getDispatcher().routeActivity((Long) rtActivity.getKey());
        }
        catch (WorkflowException e)
        {
            throw e;
        }
        catch (Throwable th)
        {
        	logWarn("[BpssActivityEngine.onPause] Exception", th);
            throw new SystemException("Exception while pausing activity: "+th.getMessage(), th);
        }
    }

    public void onResume(GWFRtActivity rtActivity) throws WorkflowException, SystemException
    {
        try
        {
            //getDispatcher().callActivityRoute((Long) rtActivity.getKey());
            WorkflowUtil.getDataManager().setContextData(rtActivity.getContextUId(), new ContextKey("workflow.activityResumed"), "true");

            /**todo:commented on 24-10-2002 */
            //getDispatcher().callAppManager(1, (Long) rtActivity.getKey());
        }
        catch (DataException e)
        {
            throw new WorkflowException("[BpssActivityEngine.onResume] Exception", e);
        }
        catch (SystemException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
        	logWarn("[BpssActivityEngine.onResume] Exception", th);
            throw new SystemException("Exception while resuming activity: "+th.getMessage(), th);
        }
    }

    public void cancelActivity(GWFRtActivity rtActivity, Object reason) throws WorkflowException, SystemException
    {
        Logger.debug("[BpssActivityEngine.cancelActivity] rtActivity=" + rtActivity);
        try
        {
            int state = rtActivity.getState().intValue();
            
            //twx: remove && state != GWFRtActivity.CLOSED_COMPLETED
            if (state != GWFRtActivity.CLOSED_ABNORMALCOMPLETED )
            {
                rtActivity.setEndTime(TimeUtil.localToUtcTimestamp());
                rtActivity.setState(new Integer(GWFRtActivity.CLOSED_ABNORMALCOMPLETED));
                rtActivity = (GWFRtActivity)UtilEntity.updateEntity(rtActivity, true);

                IDataFilter filter = new DataFilterImpl();

                filter.addSingleFilter(null, GWFRtProcess.PARENT_RTACTIVITY_UID, filter.getEqualOperator(), rtActivity.getKey(), false);
                filter.addSingleFilter(filter.getAndConnector(), GWFRtProcess.STATE, filter.getNotEqualOperator(), new Integer(GWFRtProcess.CLOSED_ABNORMALCOMPLETED), false);
                filter.addSingleFilter(filter.getAndConnector(), GWFRtProcess.STATE, filter.getNotEqualOperator(), new Integer(GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED), false);
                filter.addSingleFilter(filter.getAndConnector(), GWFRtProcess.STATE, filter.getNotEqualOperator(), new Integer(GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED), false);
                //filter.addSingleFilter(filter.getAndConnector(), GWFRtProcess.STATE, filter.getNotEqualOperator(), new Integer(GWFRtProcess.CLOSED_COMPLETED), false);
                Collection rtProcessColl = UtilEntity.getEntityByFilter(filter, GWFRtProcess.ENTITY_NAME, true);

                if (rtProcessColl != null && rtProcessColl.size() > 0)
                {
                    for (Iterator iterator = rtProcessColl.iterator(); iterator.hasNext();)
                    {
                        GWFRtProcess rtProcess = (GWFRtProcess) iterator.next();

                        GWFFactory.getProcessEngine(rtActivity.getEngineType()).cancelProcess(rtProcess, reason);
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
        	logWarn("[BpssActivityEngine.cancelActivity] Exception", th);
            throw new SystemException("Exception while canceling activity: "+th.getMessage(), th);
        }
    }

    protected void executeActivityNotRunning(GWFRtActivity rtActivity) throws WorkflowException, SystemException
    {
        try
        {
            getDispatcher().changeActivityState((Long) rtActivity.getKey(), GWFRtActivity.OPEN_RUNNING);
        }
        catch (WorkflowException e)
        {
            throw e;
        }
        catch (Throwable th)
        {
        	logWarn("[BpssActivityEngine.executeActivityNotRunning] Exception", th);
            throw new SystemException("Exception: "+th.getMessage(), th);
        }
    }

    protected void executeActivityRunning(GWFRtActivity rtActivity) throws WorkflowException, SystemException
    {
        Logger.debug("[BpssActivityEngine.executeActivityRunning] rtActivity=" + rtActivity);
        try
        {
            IDataManagerObj dataManager = WorkflowUtil.getDataManager();
            HashMap contextData = dataManager.getContextData(rtActivity.getContextUId());
            String defCacheKey = (String) contextData.get(new ContextKey("workflow.control.defCacheKey"));
            BpssDefinitionCache defCache = BpssDefinitionCache.getBpssDefinitionCache(defCacheKey);
            GWFActivity activity = (GWFActivity) defCache.getDefinationEntity(rtActivity.getActivityUId(), rtActivity.getActivityType());

            if (BpssReqBusinessActivity.ENTITY_NAME.equals(rtActivity.getActivityType()))
            {
                Long rtProcessDocUId = (Long) contextData.get(new ContextKey("workflow.control.rtProcessDocUId"));
                GWFRtProcessDoc rtProcessDoc = (GWFRtProcessDoc) UtilEntity.getEntityByKey(rtProcessDocUId, GWFRtProcessDoc.ENTITY_NAME, true);
                String documentId = rtProcessDoc.getDocumentId();
                String documentType = rtProcessDoc.getRequestDocType();

                rtProcessDoc.setDocumentType(rtProcessDoc.getRequestDocType());
                
                rtProcessDoc = (GWFRtProcessDoc)UtilEntity.updateEntity(rtProcessDoc, true);

                Object documentObject = contextData.get(new ContextKey(documentType));

                Logger.debug("[BpssActivityEngine.executeActivityRunning] documentObject=" + documentObject);
                if (documentObject == null)
                {
                    // so call xpdl ,so that it will insert document
                    HashMap xpdlContext = new HashMap();

                    xpdlContext.put(IBpssConstants.DOCUMENTID, documentId);
                    xpdlContext.put("requestDocumentType", documentType);
                    BpssBusinessTransHelper.callXpdlProcess((BpssReqBusinessActivity) activity, xpdlContext);
                } else
                {
                  logDebug("[BpssActivityEngine.executeActivityRunning] Calling performRequestingActivity");

                  BpssBusinessTransHelper.performRequestingActivity(documentType, documentObject, rtProcessDoc);
                  logDebug("[BpssActivityEngine.executeActivityRunning] Finish performRequestingActivity");
  
                }
            } else if (BpssResBusinessActivity.ENTITY_NAME.equals(rtActivity.getActivityType()))
            {
                Long rtProcessDocUId = (Long) contextData.get(new ContextKey("workflow.control.rtProcessDocUId"));
                GWFRtProcessDoc rtProcessDoc = (GWFRtProcessDoc) UtilEntity.getEntityByKey(rtProcessDocUId, GWFRtProcessDoc.ENTITY_NAME, true);

                String documentId = rtProcessDoc.getDocumentId();
                String documentType = rtProcessDoc.getRequestDocType();
                String partnerKey = rtProcessDoc.getPartnerKey();
                Object documentObject = contextData.get(new ContextKey(documentType));

                
                //TWX #2594 20110907: The invoking of the BpssBusinessTransHelper.processRequestDocument() and
                //                    BpssBusinessTransHelper.processConcurrentRequests should be grouped under
                //                    the same Reentrant lock block to prevent the deadlock. See the ticket for more detail.
                
                BpssBusinessTransHelper.acquireRespondRoleLock(); //start of lock block
                try
                {
                  logDebug("[BpssActivityEngine.executeActivityRunning] acquiring RespondRoleLock");
                  if (documentObject != null)
                  {
                    logDebug("[BpssActivityEngine.executeActivityRunning] Calling processRequestDocument");
                      //means responder already has request document,so process request document
                    BpssBusinessTransHelper.processRequestDocumentNoLock(documentType, documentObject, rtProcessDoc, partnerKey);
                    logDebug("[BpssActivityEngine.executeActivityRunning] Finish processRequestDocument");
                  }
                  //else means yet to receive request document
  
                  //process concurrent requests if any.
                  if(documentId!=null){
                    BpssBusinessTransHelper.processConcurrentRequests(documentId,rtProcessDocUId);
                  }
                }
                finally
                {
                  BpssBusinessTransHelper.releaseRespondRoleLock(); //end of lock block
                  logDebug("[BpssActivityEngine.executeActivityRunning] RespondRoleLock released.");
                }

            } else if (BpssCollaborationActivity.ENTITY_NAME.equals(rtActivity.getActivityType()))
            {
                Long binCollUId = ((BpssCollaborationActivity) activity).getBinaryCollaborationUId();
                Long contextUId = dataManager.createContextUId();

                dataManager.setContextData(contextUId, contextData);
                getDispatcher().createRtProcess(binCollUId, BpssBinaryCollaboration.ENTITY_NAME, new Long(rtActivity.getUId()), contextUId);
            } else if (BpssBusinessTransActivity.ENTITY_NAME.equals(rtActivity.getActivityType()))
            {
                // create a RtProcessDoc record for each BpssBusinessTransActivity
                BpssBusinessTransActivity businessTransActivity = (BpssBusinessTransActivity) activity;

                GWFRtProcess rtProcess = (GWFRtProcess) UtilEntity.getEntityByKey(rtActivity.getRtProcessUId(), GWFRtProcess.ENTITY_NAME, true);

                String initiatorPartnerKey = (String) contextData.get(new ContextKey("workflow.control.initiatorPartnerKey"));
                String responderPartnerKey = (String) contextData.get(new ContextKey("workflow.control.responderPartnerKey"));

                //set the role info
                String roleType = null;
                String partnerKey = null;

                if (initiatorPartnerKey.equals(IBpssConstants.PARTNER_CONSTANT))
                {
                    roleType = IBpssConstants.INITIATING_ROLE;
                    partnerKey = responderPartnerKey;
                } else if (responderPartnerKey.equals(IBpssConstants.PARTNER_CONSTANT))
                {
                    roleType = IBpssConstants.RESPONDING_ROLE;
                    partnerKey = initiatorPartnerKey;
                }

                String documentId = (String) contextData.get(new ContextKey("workflow.control.documentId"));

                if (documentId == null)
                {
                	//TWX SVN:T1 To check whether to include the Process Instance Prefix
                	if(BpssKeyHelper.isIncludedProcessInstancePrefix(partnerKey))
                	{
                		documentId = BpssKeyHelper.makeDocumentId((Long) rtProcess.getKey(), businessTransActivity.getActivityName(), BpssKeyHelper.getGridnodeID());
                	}
                	else
                	{
                		documentId = BpssKeyHelper.makeDocumentId((Long) rtProcess.getKey(), businessTransActivity.getActivityName());
                	}
                }   
                else documentId = BpssKeyHelper.changeDocumentId(documentId, businessTransActivity.getActivityName());

                GWFRtProcessDoc rtProcessDoc = new GWFRtProcessDoc();

                rtProcessDoc.setDocumentId(documentId);
                rtProcessDoc.setRoleType(roleType);
                rtProcessDoc.setBusinessTransActivityId(businessTransActivity.getActivityName());
                rtProcessDoc.setBinaryCollaborationUId(rtProcess.getProcessUId());
                rtProcessDoc.setRtBinaryCollaborationUId((Long) rtProcess.getKey());
                rtProcessDoc.setPartnerKey(partnerKey);
                rtProcessDoc.setStatus(new Integer(GWFRtProcess.OPEN_NOTRUNNING));
                rtProcessDoc = (GWFRtProcessDoc) UtilEntity.createEntity(rtProcessDoc, true);
                
                if (roleType == IBpssConstants.INITIATING_ROLE)
                {
                    long timeToPerform = WorkflowUtil.getTimeInterval(businessTransActivity.getTimeToPerform());

                    if (timeToPerform > 0)
                    {
                        String key = KeyConverter.getKey((Long) rtProcessDoc.getKey(), rtProcessDoc.getEntityName(), IWorkflowConstants.BPSS_ENGINE);

                        WorkflowUtil.addAlarm(key, BpssBusinessTransActivity.ENTITY_NAME, IWorkflowConstants.BPSS_ENGINE, timeToPerform);
                    }
                }
                //contextData.put("workflow.control.documentId",documentId);
                contextData.put("workflow.control.rtProcessDocUId", rtProcessDoc.getKey());
                Long busTransUId = ((BpssBusinessTransActivity) activity).getBusinessTransUId();
                Long contextUId = dataManager.createContextUId();

                dataManager.setContextData(contextUId, contextData);
                getDispatcher().createRtProcess(busTransUId, BpssBusinessTrans.ENTITY_NAME, new Long(rtActivity.getUId()), contextUId);
            } else if (BpssBinaryCollaborationActivity.ENTITY_NAME.equals(rtActivity.getActivityType()))
            {
                Long binCollUId = ((BpssBinaryCollaborationActivity) activity).getBinaryCollaborationUId();
                Long contextUId = dataManager.createContextUId();

                dataManager.setContextData(contextUId, contextData);
                getDispatcher().createRtProcess(binCollUId, BpssBinaryCollaboration.ENTITY_NAME, new Long(rtActivity.getUId()), contextUId);
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
        	logWarn("[BpssActivityEngine.executeActivityRunning] Exception", th);
            throw new SystemException("Exception: "+th.getMessage(), th);
        }
    }

    protected void executeActivityCompleted(GWFRtActivity rtActivity) throws WorkflowException, SystemException
    {
        try
        {
            IDataManagerObj dataManager = WorkflowUtil.getDataManager();
            GWFRtProcess rtProcess = (GWFRtProcess) UtilEntity.getEntityByKey(rtActivity.getRtProcessUId(), GWFRtProcess.ENTITY_NAME, true);
            String defCacheKey = (String) dataManager.getContextData(rtProcess.getContextUId(), new ContextKey("workflow.control.defCacheKey"));
            //BpssDefinitionCache defCache = 
            BpssDefinitionCache.getBpssDefinitionCache(defCacheKey);

            //String key = KeyConverter.getKey(rtActivity.getActivityUId(), rtActivity.getActivityType(), "wfActivity");

            //boolean isSuccess = false;
            String activityType = rtActivity.getActivityType();

            if (activityType.equals(BpssReqBusinessActivity.ENTITY_NAME) || activityType.equals(BpssResBusinessActivity.ENTITY_NAME))
            {
                // complete the process
                logDebug("[BpssActivityEngine.executeActivityCompleted] , " + activityType + ", calling complete process " + rtProcess);
                getDispatcher().changeProcessState((Long) rtProcess.getKey(), GWFRtProcess.CLOSED_COMPLETED);
            } else if (activityType.equals(BpssBusinessTransActivity.ENTITY_NAME) || activityType.equals(BpssCollaborationActivity.ENTITY_NAME))
            {
                processNextActivity(rtActivity, rtProcess);
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
        	logWarn("[BpssActivityEngine.executeActivityCompleted] Exception", th);
            throw new SystemException("Exception: "+th.getMessage(), th);
        }
    }

    protected void executeActivityAborted(GWFRtActivity rtActivity) throws WorkflowException, SystemException
    {
        logDebug("[BpssActivityEngine.executeActivityAborted] " + rtActivity);
        try
        {
            String activityType = rtActivity.getActivityType();

            if (activityType.equals(BpssBusinessTransActivity.ENTITY_NAME) || activityType.equals(BpssCollaborationActivity.ENTITY_NAME))
            {
                GWFRtProcess rtProcess = (GWFRtProcess) UtilEntity.getEntityByKey(rtActivity.getRtProcessUId(), GWFRtProcess.ENTITY_NAME, true);

                processNextActivity(rtActivity, rtProcess);
            } else if (activityType.equals(BpssReqBusinessActivity.ENTITY_NAME) || activityType.equals(BpssResBusinessActivity.ENTITY_NAME))
            {
                IDataFilter filter = new DataFilterImpl();

                filter.addSingleFilter(null, GWFRtProcessDoc.RT_BUSINESS_TRANSACTION_UID, filter.getEqualOperator(), rtActivity.getRtProcessUId(), false);
                Collection coll = UtilEntity.getEntityByFilter(filter, GWFRtProcessDoc.ENTITY_NAME, true);

                if (coll != null && coll.size() > 0)
                {
                    GWFRtProcessDoc rtProcessDoc = (GWFRtProcessDoc) coll.iterator().next();
                    GWFRtProcess rtProcess = (GWFRtProcess) UtilEntity.getEntityByKey(rtActivity.getRtProcessUId(), GWFRtProcess.ENTITY_NAME, true);

                    GWFFactory.getProcessEngine(IWorkflowConstants.BPSS_ENGINE).changeRtProcessState(rtProcess, rtProcessDoc.getStatus());
                } else throw new WorkflowException("[BpssActivityEngine.executeActivityAborted] No GWFRtProcessDoc with RT_BUSINESS_TRANSACTION_UID=" + rtActivity.getRtProcessUId());
            }
            return;
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
        	logWarn("[BpssActivityEngine.executeActivityAborted] Exception ", th);
            throw new SystemException("Exception: "+th.getMessage(), th);
        }
    }

    protected void executeActivitySuspended(GWFRtActivity rtActivity) throws WorkflowException, SystemException
    {

        /**@todo:what to do if the activity state is OPEN_NOTRUNNING_SUSPENDED ??????????  */
        return;
    }

    public void checkTimeToPerform(String senderKey, String receiverKey) throws WorkflowException, SystemException
    {
        try
        {
            BpssBusinessTransHelper.checkTimeTo(senderKey, receiverKey);
        }
        catch (Throwable th)
        {
        	logWarn("[BpssActivityEngine.checkTimeToPerform] Exception", th);
            throw new SystemException("Exception: "+th.getMessage(), th);
        }
    }

    public void processNextActivity(GWFRtActivity rtActivity, GWFRtProcess rtProcess) throws SystemException, WorkflowException
    {
        try
        {
            IDataManagerObj dataManager = WorkflowUtil.getDataManager();
            HashMap contextData = dataManager.getContextData(rtActivity.getContextUId());
            String defCacheKey = (String) contextData.get(new ContextKey("workflow.control.defCacheKey"));
            BpssDefinitionCache defCache = BpssDefinitionCache.getBpssDefinitionCache(defCacheKey);

            String conditionGuard = (String) contextData.get(new ContextKey("conditionGuard"));

            Logger.debug("[BpssActivityEngine.processNextActivity] conditionGuard=" + conditionGuard + ",rtActivity=" + rtActivity + ",cacheKey=" + defCacheKey);
            String key = KeyConverter.getKey(rtActivity.getActivityUId(), rtActivity.getActivityType(), "wfActivity");
            Collection transitionColl = defCache.getBpssTransitionsFrom(key, rtProcess.getProcessUId(), rtProcess.getProcessType());

            boolean isSuccess = false;

            if (transitionColl != null && transitionColl.size() > 0)
            {
                Iterator iterator = transitionColl.iterator();

                while (iterator.hasNext())
                {
                    BpssTransition bpssTransition = (BpssTransition) iterator.next();
                    boolean transitionOk = true;
                    String btGuard = "Success";//for transitions the default condition guard is 'Success'

                    if (bpssTransition.getConditionGuard() != null && bpssTransition.getConditionGuard().trim().length() > 0)
                    {
                        btGuard = bpssTransition.getConditionGuard();
                    }
                    //if transition has a ConditionGuard then it has to be equal to the previous BTA
                    transitionOk = btGuard.equals(conditionGuard);
                    if (transitionOk && (bpssTransition.getConditionExpression() != null && bpssTransition.getConditionExpression().trim().length() > 0))
                    {
                        //if transition has a condition expression then it has to satisfy the condition expression
                        transitionOk = WorkflowUtil.executeExpression(bpssTransition.getConditionExpression(), rtActivity.getRtProcessUId());
                    }
                    if (transitionOk)
                    {
                        //remove(rtActivity);
                        logDebug("[BpssActivityEngine.processNextActivity] , after removing rtActivity " + rtActivity.getKey());
                        //since this activity is completed ,process the next transition.
                        BpssRouteHelper.processTransition(bpssTransition, rtActivity.getBranchName(), rtActivity.getRtProcessUId());
                        isSuccess = true;
                        break;
                    }
                }
                /////////
            }
            if (!isSuccess)
            {
                // no more transitions which satisfy the ConditionGuard to process so fetch the completion states.
                // process the completion state which satisfies the guard condition.
                Collection bpssCompColl = defCache.getBpssCompletionStates(key, rtProcess.getProcessUId(), rtProcess.getProcessType(), null);
                Iterator compIterator = bpssCompColl.iterator();

                while (compIterator.hasNext())
                {
                    BpssCompletionState compState = (BpssCompletionState) compIterator.next();

                    boolean transitionOk = true;

                    if (compState.getConditionGuard() != null && compState.getConditionGuard().trim().length() > 0)
                    {
                        //if transition has a ConditionGuard then it has to be equal to the previous BTA
                        transitionOk = compState.getConditionGuard().equals(conditionGuard);
                    }
                    if (transitionOk && (compState.getConditionExpression() != null && compState.getConditionExpression().trim().length() > 0))
                    {
                        //if transition has a condition expression then it has to satisfy the condition expression
                        transitionOk = WorkflowUtil.executeExpression(compState.getConditionExpression(), rtActivity.getRtProcessUId());
                    }
                    if (transitionOk)
                    {
                        //remove(rtActivity);
                        logDebug("[BpssActivityEngine.processNextActivity] , -completion state block, after removing rtActivity " + rtActivity.getKey());
                        logDebug("[BpssActivityEngine.processNextActivity] , compState=" + compState);
                        //process the completion state
                        BpssTransition bpssTransition = new BpssTransition();

                        bpssTransition.setProcessType(rtProcess.getProcessType());
                        bpssTransition.setToBusinessStateKey(KeyConverter.getKey((Long) compState.getKey(), ((compState.getMpcUId() != null) ? "GwfUpLink" : BpssCompletionState.ENTITY_NAME), "wfRestriction"));
                        BpssRouteHelper.processTransition(bpssTransition, rtActivity.getBranchName(), new Long(rtProcess.getUId()));
                        isSuccess = true;
                        break;
                    }
                }
            }
            if (!isSuccess)
            {
                //It should not come here ,
                logDebug("[BpssActivityEngine.processNextActivity] Inside invalid block ,after aborting rtProcess  " + rtProcess.getKey());
                getDispatcher().changeProcessState((Long) rtProcess.getKey(), GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED);
            }
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
            th.printStackTrace();
            logWarn("[BpssActivityEngine.processNextActivity] Error ", th);
            throw new SystemException("Exception: "+th.getMessage(), th);
        }
    }

}
