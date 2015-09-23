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
 */

package com.gridnode.pdip.app.workflow.impl.bpss.helpers;


import java.util.*;

import com.gridnode.pdip.app.workflow.engine.GWFFactory;
import com.gridnode.pdip.app.workflow.exceptions.TransitionException;
import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.expression.ExpressionParser;
import com.gridnode.pdip.app.workflow.runtime.helpers.GWFRuntimeUtil;
import com.gridnode.pdip.app.workflow.runtime.model.*;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.base.contextdata.entities.model.ContextKey;
import com.gridnode.pdip.base.gwfbase.baseentity.GWFActivity;
import com.gridnode.pdip.base.gwfbase.baseentity.GWFProcess;
import com.gridnode.pdip.base.gwfbase.baseentity.GWFTransition;
import com.gridnode.pdip.base.gwfbase.bpss.helpers.BpssDefinitionCache;
import com.gridnode.pdip.base.gwfbase.bpss.model.*;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.KeyConverter;
import com.gridnode.pdip.framework.util.UtilEntity;


public class BpssRouteHelper {

    /**
     * This method processes the Transitions
     * if toBusinessStateKey is of type GWFRestriction then creates the
     * runtime Restriction and calls the selectRoute with runtime Restriction
     *
     * if toBusinessStateKey is of type GWFActivity then creates the runtime activity
     *
     * @param transition
     * @param rtProcessUId
     * @throws Exception
     */
    public static void processTransition(GWFTransition transition, String branchName ,Long rtProcessUId) throws Exception {
        try {
            BpssTransition bpssTransition = (BpssTransition) transition;

            Logger.debug("[BpssRouteHelper.processTransition] , " + bpssTransition + "\t\trtProcessUId:" + rtProcessUId);
            String key = bpssTransition.getToBusinessStateKey();
            String entityName = KeyConverter.getEntityName(key);

            if (KeyConverter.getType(key).equals("wfRestriction")) {
                if (entityName.equals("BpssSuccess") || entityName.equals("BpssFailure") || entityName.equals("GwfUpLink") || entityName.equals(BpssCompletionState.ENTITY_NAME)) {
                    Logger.debug("[BpssRouteHelper.processTransition] (success,failure) :" + entityName);
                    GWFRtRestriction rtRestriction = GWFRuntimeUtil.getRtRestriction(entityName, KeyConverter.getUID(key), BpssCompletionState.ENTITY_NAME, rtProcessUId);
                    if(rtRestriction==null){
                        rtRestriction = WorkflowUtil.getRestrictionManager().createRtRestriction(KeyConverter.getUID(key), entityName, "End", rtProcessUId, IWorkflowConstants.BPSS_ENGINE);
                    }
                    GWFFactory.getRouteDispatcher(GWFFactory.BPSS_ENGINE).routeRestriction((Long) rtRestriction.getKey());
                } else if (entityName.equals(BpssFork.ENTITY_NAME)) {
                    GWFRtRestriction rtRestriction = GWFRuntimeUtil.getRtRestriction(null, KeyConverter.getUID(key), entityName, rtProcessUId);
                    GWFFactory.getRouteDispatcher(GWFFactory.BPSS_ENGINE).routeRestriction((Long) rtRestriction.getKey());

                    GWFRtProcess rtProcess=(GWFRtProcess)UtilEntity.getEntityByKey(rtProcessUId,GWFRtProcess.ENTITY_NAME,true);
                    String defCacheKey=(String)WorkflowUtil.getDataManager().getContextData(rtProcess.getContextUId(),new ContextKey("workflow.control.defCacheKey"));
                    BpssDefinitionCache defCache=BpssDefinitionCache.getBpssDefinitionCache(defCacheKey);

                    Collection transColl = defCache.getBpssTransitionsFrom(key, rtProcess.getProcessUId(), rtProcess.getProcessType());
                    if (transColl.size() == 0)
                        throw new TransitionException("[BpssRouteHelper.processTransition] No transitions from "+key);
                    HashMap ctxData=WorkflowUtil.getDataManager().getContextData(rtProcess.getContextUId());
                    for(Iterator iterator=transColl.iterator();iterator.hasNext();){
                        BpssTransition forkTransition=(BpssTransition)iterator.next();
                        boolean conditioneval = ExpressionParser.processExpression(forkTransition.getConditionExpression(), ctxData);
                        if(!conditioneval)
                            throw new TransitionException("[BpssRouteHelper.processTransition] Transition condition is false for "+forkTransition);
                    }
                    for(Iterator iterator=transColl.iterator();iterator.hasNext();){
                        BpssTransition forkTransition=(BpssTransition)iterator.next();
                        processTransition(forkTransition,getBranchNameFromKey(forkTransition.getToBusinessStateKey()),rtProcessUId);
                    }
                } else if (entityName.equals(BpssJoin.ENTITY_NAME)) {
                    // get the activation states ,and change the state to true for the transition which started it
                    GWFRtRestriction rtRestriction = GWFRuntimeUtil.getRtRestriction(null, KeyConverter.getUID(key), entityName, rtProcessUId);
                    GWFTransActivationState transActState = (GWFTransActivationState) GWFRuntimeUtil.getTransActivationStates((Long) transition.getKey(), rtRestriction.getTransActivationStateListUId()).toArray()[0];
                    GWFRuntimeUtil.changeTransActivationState((Long)transActState.getKey(),true);

                    GWFFactory.getRouteDispatcher(GWFFactory.BPSS_ENGINE).routeRestriction((Long) rtRestriction.getKey());
                }
            } else {
                Logger.debug("[BpssRouteHelper.processTransition] , before create RtActivity:" + entityName + "\t\tKEY:" + key);
                GWFRtProcess rtProcess=(GWFRtProcess)UtilEntity.getEntityByKey(rtProcessUId,GWFRtProcess.ENTITY_NAME,true);
                GWFFactory.getRouteDispatcher(GWFFactory.BPSS_ENGINE).createRtActivity(KeyConverter.getUID(key), entityName, rtProcessUId,branchName,rtProcess.getContextUId());
            }
        }catch(WorkflowException e){
            throw e;
        } catch (Throwable th) {
            Logger.warn("[BpssRouteHelper.processTransition] Exception", th);
            throw new SystemException("[BpssRouteHelper.processTransition] Exception", th);
        }
    }

    /**
     * get the downlink for this uplink, and then start the activity that downlink is pointing to.
     * @param rtCompState
     * @param process
     * @throws Exception
     */
    public static void processUpLink(GWFRtRestriction rtRestriction, GWFProcess process, Long rtActivityUId) throws Exception {
        Logger.log("[BpssRouteHelper.processUpLink] Enter");
        try {
            IDataFilter filter = null;

            //fetch RtActivity for  BpssBinaryCollaborationActivity using rtActivityUId
            GWFRtActivity rtActivity = (GWFRtActivity)UtilEntity.getEntityByKey(rtActivityUId,GWFRtActivity.ENTITY_NAME,true);

            //fetch RtProcess which started this RtActivity i.e runtime of MPC
            //increment the maxconcurrency of runtime MPC by 1
            GWFRtProcess rtProcess=GWFRuntimeUtil.addMaxConcurrency(rtActivity.getRtProcessUId(),1);

            //fetch the tansition which is having the fromBusinessStateKey pointing to fromBusinessStateKey of
            //the uplink CompletionState.
            BpssCompletionState compState = (BpssCompletionState) UtilEntity.getEntityByKey(rtRestriction.getRestrictionUId(), BpssCompletionState.ENTITY_NAME, true);
            BpssTransition transition = (BpssTransition) getTransitions(rtProcess.getProcessUId(), rtProcess.getProcessType(), compState.getFromBusinessStateKey(), true).toArray()[0];

            //fetch the downlink which is having toBusinessStateKey pointing to toBusinessStateKey of the
            //BpssStart
            filter = new DataFilterImpl();
            filter.addSingleFilter(null, BpssStart.TO_BUSINESS_STATE_KEY, filter.getEqualOperator(), transition.getToBusinessStateKey(), false);
            filter.addSingleFilter(filter.getAndConnector(), BpssStart.ISDOWNLINK, filter.getEqualOperator(), new Integer(1), false);
            //BpssStart bpssStart = (BpssStart) UtilEntity.getEntityByFilterForReadOnly(filter, BpssStart.ENTITY_NAME, true).toArray()[0];
            Collection downLinkColl=UtilEntity.getEntityByFilter(filter, BpssStart.ENTITY_NAME, true);
            BpssStart bpssStart = (BpssStart) downLinkColl.iterator().next();
            Logger.debug("[BpssRouteHelper.processUpLink] downlinks fetched:="+downLinkColl.size()+",downlink_bpssStart="+bpssStart);

            Object[] binaryCollActivityArr = getBinaryCollaborationActivities(rtProcess).toArray();

            //BpssBinaryCollaborationActivity[] binaryCollActivityArr=(BpssBinaryCollaborationActivity[])getBinaryCollaborationActivities(rtProcess).toArray();
            for (int i = 0; i < binaryCollActivityArr.length; i++) {
                BpssBinaryCollaborationActivity binaryCollActivity = (BpssBinaryCollaborationActivity) binaryCollActivityArr[i];

                if (binaryCollActivity.getDownLinkUId() != null && binaryCollActivity.getDownLinkUId().longValue() == bpssStart.getUId()) {
                    // fetch the runtime of this BpssBinaryCollaborationActivity

                    filter = new DataFilterImpl();
                    filter.addSingleFilter(null, GWFRtActivity.ACTIVITY_UID, filter.getEqualOperator(), new Long(binaryCollActivity.getUId()), false);
                    filter.addSingleFilter(filter.getAndConnector(), GWFRtActivity.ACTIVITY_TYPE, filter.getEqualOperator(), BpssBinaryCollaborationActivity.ENTITY_NAME, false);
                    filter.addSingleFilter(filter.getAndConnector(), GWFRtActivity.RT_PROCESS_UID, filter.getEqualOperator(), new Long(rtProcess.getUId()), false);
                    //GWFRtActivity rtBinCollActivity = (GWFRtActivity)RtManagerDelegate.getRtActivityByFilter(filter).toArray()[0];
                    GWFRtActivity rtBinCollActivity = (GWFRtActivity)UtilEntity.getEntityByFilter(filter,GWFRtActivity.ENTITY_NAME,true).iterator().next();
                    //actRemote.begin(rtBinCollActivity);
                    GWFFactory.getRouteDispatcher(GWFFactory.BPSS_ENGINE).changeActivityState((Long) rtBinCollActivity.getKey(),GWFRtActivity.OPEN_RUNNING);

                    break;
                }
            }
        } catch(WorkflowException e){
            throw e;
        } catch (Throwable th) {
            Logger.warn("[BpssRouteHelper.processUpLink] Exception", th);
            throw new SystemException("[BpssRouteHelper.processUpLink] Exception", th);
        }
    }
    /*
    public static Object getBusinessDocument(String businessDocName){
        IDataFilter filter=new DataFilterImpl();
        //filter.addSingleFilter(null,GWFRtProcessDoc.);
        return null;
    }*/

    /**
     * This method creates the RtRestriction if it does not exist for the restrictionUId
     * else it will reterive the RtRestriction and returns it.
     *
     * @param rtRestriction it is the empty runtime restriction Object for eg. GWFRtFork,GWFRtJoin
     * @param restrictionUId it is the Definition UId of the restriction
     * @param rtProcessUId
     * @param entityName it is the Class name of the Definition
     * @return
     * @throws Exception
     */

    /*  public static GWFRtRestriction getRtRestriction(GWFRtRestriction rtRestriction,Long restrictionUId,Long rtProcessUId,String entityName) throws Exception {
     try{
     Log.debug(module,"Inside getRtRestriction "+rtRestriction+"\t"+entityName);
     IDataFilter filter=new DataFilterImpl();
     filter.addSingleFilter(null,rtRestriction.RT_PROCESS_UID,filter.getEqualOperator(),rtProcessUId,false);
     filter.addSingleFilter(filter.getAndConnector(),rtRestriction.RESTRICTION_UID,filter.getEqualOperator(),restrictionUId,false);
     Collection rtRestrictionColl=UtilEntity.getEntityByFilter(filter,rtRestriction.getEntityName(),true);
     if(rtRestrictionColl==null || rtRestrictionColl.size()==0){
     // create rtRestriction if one does not exist
     GWFRestriction restriction=(GWFRestriction)UtilEntity.getEntityByKey(restrictionUId,entityName,true);
     IGWFRestrictionManagerHome restHome=(IGWFRestrictionManagerHome)ServiceLookup.getInstance(ServiceLookup.LOCAL_CONTEXT).getHome(IGWFRestrictionManagerHome.class);
     IGWFRestrictionManagerObject restRemote=restHome.create();
     rtRestriction=(GWFRtRestriction)restRemote.createRtRestriction(rtRestriction,restriction,rtProcessUId);
     } else if(rtRestrictionColl.size()==1){
     rtRestriction=(GWFRtRestriction)rtRestrictionColl.toArray()[0];
     }
     return rtRestriction;
     }catch(Exception e){
     Log.err(module,"Error in getRtRestriction ",e);
     throw e;
     }
     }
     */



    /**
     * This method will return all transitions with this processUId,processType
     * if key is null then it will only filter on processUId,processType else it will filter depending on processUId,processType,key and equalsFrom
     * if key is not null and equalsFrom is true then it will filter transitions with this processUId ,processType and fromBusinessState matching this key
     * if key is not null and equalsFrom is false then it will filter transitions with this processUId ,processType and toBusinessState matching this key
     *
     * @param processUId
     * @param key
     * @param equalsFrom
     * @return Collection
     * @throws Exception
     */
    public static Collection getTransitions(Long processUId, String processType, String key, boolean equalsFrom) throws SystemException {
        Logger.log("[BpssRouteHelper.getTransitions] Enter");
        try{
            IDataFilter filter = new DataFilterImpl();

            filter.addSingleFilter(null, BpssTransition.PROCESS_UID, filter.getEqualOperator(), processUId, false);
            filter.addSingleFilter(filter.getAndConnector(), BpssTransition.PROCESS_TYPE, filter.getEqualOperator(), processType, false);
            if (key != null)
                filter.addSingleFilter(filter.getAndConnector(), (equalsFrom) ? BpssTransition.FROM_BUSINESS_STATE_KEY : BpssTransition.TO_BUSINESS_STATE_KEY, filter.getLikeOperator(), key, false);
            return UtilEntity.getEntityByFilter(filter, BpssTransition.ENTITY_NAME, true);
        }catch(Throwable  th){
           throw new SystemException("[BpssRouteHelper.getTransitions] Exception ", th);
        }
    }

    public static Collection getBinaryCollaborationActivities(GWFRtProcess rtProcess) throws SystemException {
        try {
            IDataFilter filter = null;
            Iterator iterator = null;

            // get ProcessSpecEntry's for MPC
            filter = new DataFilterImpl();
            filter.addSingleFilter(null, BpssProcessSpecEntry.ENTRY_UID, filter.getEqualOperator(), rtProcess.getProcessUId(), false);
            filter.addSingleFilter(filter.getAndConnector(), BpssProcessSpecEntry.ENTRY_TYPE, filter.getEqualOperator(), rtProcess.getProcessType(), false);
            BpssProcessSpecEntry mpcProcessSpecEntry = (BpssProcessSpecEntry) UtilEntity.getEntityByFilter(filter, BpssProcessSpecEntry.ENTITY_NAME, true).toArray()[0];

            // get ProcessSpecEntry's for BinaryCollaborationActivitys
            filter = new DataFilterImpl();
            filter.addSingleFilter(null, BpssProcessSpecEntry.PARENT_ENTRY_UID, filter.getEqualOperator(), new Long(mpcProcessSpecEntry.getUId()), false);
            filter.addSingleFilter(filter.getAndConnector(), BpssProcessSpecEntry.ENTRY_TYPE, filter.getEqualOperator(), BpssBinaryCollaborationActivity.ENTITY_NAME, false);
            Collection bcaProcessSpecEntryColl = UtilEntity.getEntityByFilter(filter, BpssProcessSpecEntry.ENTITY_NAME, true);

            // get all uid's of BpssBinaryCollaborationActivity
            ArrayList bcaUIdList = new ArrayList();

            iterator = bcaProcessSpecEntryColl.iterator();
            while (iterator.hasNext()) {
                BpssProcessSpecEntry processSpecEntry = (BpssProcessSpecEntry) iterator.next();

                bcaUIdList.add(new Long(processSpecEntry.getEntryUId()));
            }

            //BpssBinaryCollaborationActivity
            //get BpssBinaryCollaborationActivity with the above feteced BpssBinaryCollaborationActivity uid's
            filter = new DataFilterImpl();
            filter.addDomainFilter(null, BpssBinaryCollaborationActivity.UID, bcaUIdList, false);
            return UtilEntity.getEntityByFilter(filter, BpssBinaryCollaborationActivity.ENTITY_NAME, true);
        }catch(Throwable th){
            Logger.warn("[BpssRouteHelper.getBinaryCollaborationActivities] Exception", th);
            throw new SystemException("[BpssRouteHelper.getBinaryCollaborationActivities] Exception", th);
        }

    }

    /**
     * helper method to query BpssCompletionState, if mpcUId is null it will not be included in the filter.
     *
     * @param fromBusinessStateKey
     * @param processUId
     * @param processType
     * @param mpcUId
     * @return Collection of BpssCompletionState
     * @throws Exception
     */
    public static Collection getBpssCompletionStates(String fromBusinessStateKey, Long processUId, String processType, Long mpcUId) throws SystemException {
        try{

            IDataFilter filter = new DataFilterImpl();

            filter.addSingleFilter(null, BpssCompletionState.FROM_BUSINESS_STATE_KEY, filter.getEqualOperator(), fromBusinessStateKey, false);
            filter.addSingleFilter(filter.getAndConnector(), BpssCompletionState.PROCESS_UID, filter.getEqualOperator(), processUId, false);
            filter.addSingleFilter(filter.getAndConnector(), BpssCompletionState.PROCESS_TYPE, filter.getEqualOperator(), processType, false);
            if (mpcUId != null)
                filter.addSingleFilter(filter.getAndConnector(), BpssCompletionState.MPC_UID, filter.getEqualOperator(), mpcUId, false);
            return UtilEntity.getEntityByFilter(filter, BpssCompletionState.ENTITY_NAME, true);
        }catch(Throwable th){
            throw new SystemException("[BpssRouteHelper.getBpssCompletionStates] Exception",th);
        }
    }

    public static List getTransActivationStates(GWFRtRestriction rtRestriction, Collection transColl) {
        Logger.log("[BpssRouteHelper.getTransActivationStates] Enter");
        List list = new LinkedList();
        Iterator transIterator = transColl.iterator();

        while (transIterator.hasNext()) {
            BpssTransition bpssTrans = (BpssTransition) transIterator.next();
            GWFTransActivationState transActState = new GWFTransActivationState();

            transActState.setListUId(rtRestriction.getTransActivationStateListUId());
            transActState.setRtRestrictionUId((Long) rtRestriction.getKey());
            transActState.setRestrictionType(rtRestriction.getEntityName());
            transActState.setTransitionUId((Long) bpssTrans.getKey());
            transActState.setState(Boolean.FALSE);
            list.add(transActState);
        }
        return list;
    }

    public static boolean checkTransitionState(String state, int rtState) {
        Logger.log("[BpssRouteHelper.checkTransitionState] Enter");
        if (state == null || state.trim().length() == 0) {
            //if there is no GuardCondition then transition can be done.
            return true;
        } else if (state.equalsIgnoreCase(IBpssConstants.SUCCESS_GUARD)) {
            return (rtState == IGWFRtActivity.CLOSED_COMPLETED);
        } else if (state.equalsIgnoreCase(IBpssConstants.BUSINESSFAILURE_GUARD)) {
            return (rtState == IGWFRtActivity.CLOSED_ABNORMALCOMPLETED);
        } else if (state.equalsIgnoreCase(IBpssConstants.TECHNICALFAILURE_GUARD)) {
            return (rtState == IGWFRtActivity.OPEN_RUNNING);
        } else if (state.equalsIgnoreCase(IBpssConstants.ANYFAILURE_GUARD)) {
            return (rtState == IGWFRtActivity.CLOSED_ABNORMALCOMPLETED || rtState == IGWFRtActivity.OPEN_RUNNING);
        }
        return false;
    }

    public static String getBranchNameFromKey(String key) throws Throwable {
        Long uId=KeyConverter.getUID(key);
        String entityName=KeyConverter.getEntityName(key);
        //String type=KeyConverter.getType(key);
        IEntity entity=UtilEntity.getEntityByKey(uId,entityName,true);
        if(BpssFork.ENTITY_NAME.equals(entity.getEntityName()))
            return ((BpssFork)entity).getRestrictionName();
        else if(BpssJoin.ENTITY_NAME.equals(entity.getEntityName()))
            return ((BpssJoin)entity).getRestrictionName();
        else if(entity instanceof GWFActivity)
            return ((GWFActivity)entity).getActivityName();
        else return null;
    }


}
