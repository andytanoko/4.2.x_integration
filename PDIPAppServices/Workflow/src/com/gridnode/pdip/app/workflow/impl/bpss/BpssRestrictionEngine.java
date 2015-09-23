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
package com.gridnode.pdip.app.workflow.impl.bpss;
 
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.gridnode.pdip.app.workflow.engine.GWFAbstractRestrictionEngine;
import com.gridnode.pdip.app.workflow.engine.GWFFactory;
import com.gridnode.pdip.app.workflow.engine.IGWFRouteDispatcher;
import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.expression.ExpressionParser;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.BpssBusinessTransHelper;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.BpssRouteHelper;
import com.gridnode.pdip.app.workflow.runtime.helpers.GWFRuntimeUtil;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtRestriction;
import com.gridnode.pdip.app.workflow.runtime.model.GWFTransActivationState;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.base.contextdata.entities.model.ContextKey;
import com.gridnode.pdip.base.gwfbase.baseentity.GWFActivity;
import com.gridnode.pdip.base.gwfbase.baseentity.GWFProcess;
import com.gridnode.pdip.base.gwfbase.bpss.helpers.BpssDefinitionCache;
import com.gridnode.pdip.base.gwfbase.bpss.model.*;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.KeyConverter;
import com.gridnode.pdip.framework.util.UtilEntity;


public class BpssRestrictionEngine extends GWFAbstractRestrictionEngine
{

    public IGWFRouteDispatcher getDispatcher()
    {
        return GWFFactory.getRouteDispatcher(GWFFactory.BPSS_ENGINE);
    }

    public void onCreate(GWFRtRestriction rtRestriction) throws WorkflowException, SystemException
    {
        try
        {
            String restrictionType = rtRestriction.getRestrictionType();
            Long rtProcessUId = rtRestriction.getRtProcessUId();
            GWFRtProcess rtProcess = (GWFRtProcess) UtilEntity.getEntityByKey(rtProcessUId, GWFRtProcess.ENTITY_NAME, true);
            String defCacheKey = (String) WorkflowUtil.getDataManager().getContextData(rtProcess.getContextUId(), new ContextKey("workflow.control.defCacheKey"));
            BpssDefinitionCache defCache = BpssDefinitionCache.getBpssDefinitionCache(defCacheKey);

            if (restrictionType != null)
            {
                if (restrictionType.equals(BpssFork.ENTITY_NAME))
                {
                    String key = KeyConverter.getKey(rtRestriction.getRestrictionUId(), restrictionType, "wfRestriction");
                    Collection transColl = defCache.getBpssTransitionsFrom(key, rtProcess.getProcessUId(), rtProcess.getProcessType());

                    if (transColl.size() > 0)
                    {
                        GWFRuntimeUtil.createTransActivationStates(rtRestriction, transColl);
                        //update the max conucrrency of this runtime process ie max+(n-1)
                        rtProcess = GWFRuntimeUtil.addMaxConcurrency(rtProcessUId, (transColl.size() - 1));
                    }
                } else if (restrictionType.equals(BpssJoin.ENTITY_NAME))
                {
                    String key = KeyConverter.getKey(rtRestriction.getRestrictionUId(), restrictionType, "wfRestriction");
                    Collection transColl = defCache.getBpssTransitionsTo(key, rtProcess.getProcessUId(), rtProcess.getProcessType());

                    if (transColl.size() > 0)
                    {
                        GWFRuntimeUtil.createTransActivationStates(rtRestriction, transColl);
                        //update the max conucrrency of this runtime process ie max-(n-1)
                        rtProcess = GWFRuntimeUtil.subMaxConcurrency((Long) rtProcess.getKey(), (transColl.size() - 1));
                    }
                } else if (restrictionType.equals(BpssCompletionState.ENTITY_NAME))
                {
                    //defCache.getBpssTransitionsFrom()
                    BpssCompletionState compState = (BpssCompletionState) defCache.getDefinationEntity(rtRestriction.getRestrictionUId(), BpssCompletionState.ENTITY_NAME);

                    if (compState != null && BpssCompletionState.FAILURE_TYPE.equals(compState.getCompletionType()))
                    {
                        BpssBusinessTransHelper.doFailure(rtProcess);
                    } else
                    {
                        //do nothing
                    }
                }
            } else
            {//if restrictionType is null
                logDebug("[BpssRestrictionEngine.onCreate] restrictionType is null " + rtRestriction);
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
        	logWarn("[BpssRestrictionEngine.onCreate] Exception", th);
            throw new SystemException("[BpssRestrictionEngine.onCreate] Exception", th);
        }
    }

    public void onRemove(GWFRtRestriction rtRestriction) throws WorkflowException, SystemException
    {
        //do nothing
        return;
    }

    public void selectRoute(GWFRtRestriction rtRestriction) throws WorkflowException, SystemException
    {
        try
        {
            if (rtRestriction.getRestrictionType().equals(BpssFork.ENTITY_NAME))
                processFork(rtRestriction);
            else if (rtRestriction.getRestrictionType().equals(BpssJoin.ENTITY_NAME))
                processJoin(rtRestriction);
            else if (rtRestriction.getRestrictionType().equals(BpssCompletionState.ENTITY_NAME))
                processCompletionState(rtRestriction);
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
        	logWarn("[BpssRestrictionEngine.selectRoute] Exception", th);
            throw new SystemException("[BpssRestrictionEngine.selectRoute] Exception", th);
        }
    }

    public void processFork(GWFRtRestriction rtRestriction) throws WorkflowException, SystemException
    {
        try
        {
            logDebug("[BpssRestrictionEngine.processFork] , " + rtRestriction);

            GWFRtProcess rtProcess = (GWFRtProcess) UtilEntity.getEntityByKey(rtRestriction.getRtProcessUId(), GWFRtProcess.ENTITY_NAME, true);
            HashMap ctxData = WorkflowUtil.getDataManager().getContextData(rtProcess.getContextUId());
            String defCacheKey = (String) ctxData.get(new ContextKey("workflow.control.defCacheKey"));
            BpssDefinitionCache defCache = BpssDefinitionCache.getBpssDefinitionCache(defCacheKey);

            Collection transActStateColl = GWFRuntimeUtil.getTransActivationStates(null, rtRestriction.getTransActivationStateListUId());
            Iterator iterator = transActStateColl.iterator();
            boolean allTrue = true;

            while (iterator.hasNext())
            {
                GWFTransActivationState transActState = (GWFTransActivationState) iterator.next();

                if (Boolean.FALSE.equals(transActState.getState()))
                {

                    //code for condition checking
                    BpssTransition bpssTransition = (BpssTransition) defCache.getDefinationEntity(transActState.getTransitionUId(), BpssTransition.ENTITY_NAME);
                    String conditionExpression = bpssTransition.getConditionExpression();
                    boolean conditioneval = ExpressionParser.processExpression(conditionExpression, ctxData);

                    logDebug("[BpssRestrictionEngine.processFork] ,Condition Value " + conditioneval + "\t" + transActState.getTransitionUId());
                    if (conditioneval == false)
                    {// todo check condition for activition state=false
                        //todo for false, setup timer for next condition check
                        allTrue = false;
                    } else
                    {
                        // change the activition state to true
                        transActState = GWFRuntimeUtil.changeTransActivationState((Long) transActState.getKey(), true);
                        logDebug("[BpssRestrictionEngine.processFork] , after changing transActState to true");
                    }
                }
            }
            if (allTrue)
            {//if all conditions are true
                logDebug("[BpssRestrictionEngine.processFork] , conditions are true , " + transActStateColl);
                iterator = transActStateColl.iterator();
                while (iterator.hasNext())
                {
                    GWFTransActivationState transActState = (GWFTransActivationState) iterator.next();
                    BpssTransition bpssTransition = (BpssTransition) defCache.getDefinationEntity(transActState.getTransitionUId(), BpssTransition.ENTITY_NAME);
                    String key = bpssTransition.getToBusinessStateKey();
                    Object entity = defCache.getDefinationEntity(KeyConverter.getUID(key), KeyConverter.getEntityName(key));

                    if (entity instanceof GWFActivity)
                        BpssRouteHelper.processTransition(bpssTransition, ((GWFActivity) entity).getActivityName(), rtRestriction.getRtProcessUId());
                    else BpssRouteHelper.processTransition(bpssTransition, null, rtRestriction.getRtProcessUId());

                    BpssRouteHelper.processTransition(bpssTransition, null, rtRestriction.getRtProcessUId());
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
        	logWarn("[BpssRestrictionEngine.processFork] Exception", th);
            throw new SystemException("[BpssRestrictionEngine.processFork] Exception", th);
        }
    }

    public void processJoin(GWFRtRestriction rtRestriction) throws WorkflowException, SystemException
    {
        try
        {
            logDebug("[BpssRestrictionEngine.processJoin] : " + rtRestriction);
            Collection transActStateColl = GWFRuntimeUtil.getTransActivationStates(null, rtRestriction.getTransActivationStateListUId());
            Iterator iterator = transActStateColl.iterator();
            boolean allTrue = true;

            while (iterator.hasNext() && allTrue)
            {
                GWFTransActivationState transActState = (GWFTransActivationState) iterator.next();

                allTrue = Boolean.TRUE.equals(transActState.getState());
            }
            if (allTrue)
            {//if all activation states are true
                GWFRtProcess rtProcess = (GWFRtProcess) UtilEntity.getEntityByKey(rtRestriction.getRtProcessUId(), GWFRtProcess.ENTITY_NAME, true);
                String defCacheKey = (String) WorkflowUtil.getDataManager().getContextData(rtProcess.getContextUId(), new ContextKey("workflow.control.defCacheKey"));
                BpssDefinitionCache defCache = BpssDefinitionCache.getBpssDefinitionCache(defCacheKey);

                String key = KeyConverter.getKey(rtRestriction.getRestrictionUId(), BpssJoin.ENTITY_NAME, "wfRestriction");
                Collection transitionColl = defCache.getBpssTransitionsFrom(key, rtProcess.getProcessUId(), rtProcess.getProcessType());

                if (transitionColl != null && transitionColl.size() == 1)
                {
                    BpssTransition bpssTransition = (BpssTransition) transitionColl.toArray()[0];
                    String actKey = bpssTransition.getToBusinessStateKey();
                    Object entity = defCache.getDefinationEntity(KeyConverter.getUID(actKey), KeyConverter.getEntityName(actKey));

                    if (entity instanceof GWFActivity)
                        BpssRouteHelper.processTransition(bpssTransition, ((GWFActivity) entity).getActivityName(), new Long(rtProcess.getUId()));
                    else BpssRouteHelper.processTransition(bpssTransition, null, new Long(rtProcess.getUId()));
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
        	logWarn("[BpssRestrictionEngine.processJoin] Exception", th);
            throw new SystemException("[BpssRestrictionEngine.processJoin] Exception", th);
        }
    }

    public void processCompletionState(GWFRtRestriction rtRestriction) throws WorkflowException, SystemException
    {
        try
        {
            logDebug("[BpssRestrictionEngine.processCompletionState] : " + rtRestriction);
            // since the completion state is reached decrement the maxconcurrency by 1
            GWFRtProcess rtProcess = GWFRuntimeUtil.subMaxConcurrency(rtRestriction.getRtProcessUId(), 1);
            String defCacheKey = (String) WorkflowUtil.getDataManager().getContextData(rtProcess.getContextUId(), new ContextKey("workflow.control.defCacheKey"));
            BpssDefinitionCache defCache = BpssDefinitionCache.getBpssDefinitionCache(defCacheKey);

            GWFProcess process = (GWFProcess) defCache.getDefinationEntity(rtProcess.getProcessUId(), rtProcess.getProcessType());

            logDebug("[BpssRestrictionEngine.processCompletionState] ,BEFORE CHECKING rtProcess key=" + rtProcess.getKey() + "\tMAXCONC=" + rtProcess.getMaxConcurrency());
            if (rtProcess.getMaxConcurrency().intValue() == 0)
            {
                // completion state reached
                BpssCompletionState compState = (BpssCompletionState) defCache.getDefinationEntity(rtRestriction.getRestrictionUId(), BpssCompletionState.ENTITY_NAME);

                if (BpssCompletionState.SUCCESS_TYPE.equals(compState.getCompletionType()))
                    getDispatcher().changeProcessState((Long) rtProcess.getKey(), GWFRtProcess.CLOSED_COMPLETED);
                else
                {
                    Integer newState = getProcessState(compState.getConditionGuard());

                    if (newState == null)
                        newState = new Integer(GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED);
                    getDispatcher().changeProcessState((Long) rtProcess.getKey(), newState.intValue());
                }

                //if this process is BpssBinaryCollaboration then there can be uplinks to be processed so check and process it.
                if (rtProcess.getProcessType().equals(BpssBinaryCollaboration.ENTITY_NAME))
                {
                    //after completion of the process ,process all UpLink Completion States.
                    IDataFilter filter = new DataFilterImpl();

                    filter.addSingleFilter(null, GWFRtRestriction.RT_PROCESS_UID, filter.getEqualOperator(), rtRestriction.getRtProcessUId(), false);
                    filter.addSingleFilter(filter.getAndConnector(), GWFRtRestriction.RESTRICTION_TYPE, filter.getEqualOperator(), rtRestriction.getRestrictionType(), false);
                    filter.addSingleFilter(filter.getAndConnector(), GWFRtRestriction.SUB_RESTRICTION_TYPE, filter.getEqualOperator(), "GwfUpLink", false);
                    Collection rtCompColl = UtilEntity.getEntityByFilter(filter, GWFRtRestriction.ENTITY_NAME, true);

                    if (rtCompColl.size() > 0)
                    {
                        Iterator iterator = rtCompColl.iterator();
                        Long rtActivityUId = rtProcess.getParentRtActivityUId();

                        while (iterator.hasNext())
                        {
                            rtRestriction = (GWFRtRestriction) iterator.next();
                            BpssRouteHelper.processUpLink(rtRestriction, process, rtActivityUId);
                        }
                    }
                }
            } else
            {
                //todo still some transitions need to completed
                //?????????????
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
        	logWarn("[BpssRestrictionEngine.processCompletionState] Exception", th);
            throw new SystemException("[BpssRestrictionEngine.processCompletionState] Exception", th);
        }
    }

    private Integer getProcessState(String conditionGuard)
    {
        int state = -1;

        if ("Success".equals(conditionGuard))
            state = GWFRtProcess.CLOSED_COMPLETED;
        else if ("BusinessFailure".equals(conditionGuard))
            state = GWFRtProcess.CLOSED_ABNORMALCOMPLETED;
        else if ("TechnicalFailure".equals(conditionGuard))
            state = GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED;
        else if ("AnyFailure".equals(conditionGuard))
            state = GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED;
        if (state != -1)
            return new Integer(state);
        return null;
    }
}
