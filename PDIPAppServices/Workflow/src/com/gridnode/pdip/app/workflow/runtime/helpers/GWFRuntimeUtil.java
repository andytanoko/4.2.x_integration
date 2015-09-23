/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 31 2002    Mahesh              Created
 * Dec 14 2005    Tam Wei Xiang       To resolve the pass by value issue:
 *                                    use UtilEntity.updateEntity instead of
 *                                    UtilEntity.update
 *
 */
package com.gridnode.pdip.app.workflow.runtime.helpers;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtRestriction;
import com.gridnode.pdip.app.workflow.runtime.model.GWFTransActivationState;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.domain.GWFException;
import com.gridnode.pdip.framework.util.UtilEntity;

public abstract class GWFRuntimeUtil {

    public static final String WORKLIST_TOPIC = "topic/worklisttopic";
    public static final String WORKFLOW_JMSCONFIG = "workflow_jms.properties";


    public static GWFRtProcess addMaxConcurrency(Long rtProcessUId,int mc) throws WorkflowException{
        try{
            GWFRtProcess rtProcess=(GWFRtProcess)UtilEntity.getEntityByKey(rtProcessUId,GWFRtProcess.ENTITY_NAME,true);
            rtProcess.setMaxConcurrency(new Integer(rtProcess.getMaxConcurrency().intValue()+mc));
            rtProcess = (GWFRtProcess)UtilEntity.updateEntity(rtProcess,true);
            return rtProcess;
        }catch(Throwable th){
            throw new WorkflowException("[WorkflowUtil.addMaxConcurrency] unable to add MaxConcurrency,rtProcessUId="+rtProcessUId+", maxConcurrency="+mc,th);
        }
    }

    public static GWFRtProcess subMaxConcurrency(Long rtProcessUId,int mc) throws WorkflowException{
        try{
            GWFRtProcess rtProcess=(GWFRtProcess)UtilEntity.getEntityByKey(rtProcessUId,GWFRtProcess.ENTITY_NAME,true);
            rtProcess.setMaxConcurrency(new Integer(rtProcess.getMaxConcurrency().intValue()-mc));
            rtProcess = (GWFRtProcess)UtilEntity.updateEntity(rtProcess,true);
            return rtProcess;
        }catch(Throwable th){
            throw new WorkflowException("[WorkflowUtil.subMaxConcurrency] unable to sub MaxConcurrency,rtProcessUId="+rtProcessUId+", maxConcurrency="+mc,th);
        }
    }


    public static GWFRtRestriction getRtRestriction(String subRestrictionType, Long restrictionUId, String restrictionType, Long rtProcessUId) throws SystemException {
        Collection rtRestrictionColl =getRtRestrictions(subRestrictionType, restrictionUId, restrictionType, rtProcessUId);
        if(rtRestrictionColl!=null && rtRestrictionColl.size()>0)
            return (GWFRtRestriction)rtRestrictionColl.iterator().next();
        else return null;
    }

    public static Collection getRtRestrictions(String subRestrictionType, Long restrictionUId, String restrictionType, Long rtProcessUId) throws SystemException {
        try {
            //GWFRtRestriction rtRestriction = null;
            IDataFilter filter = new DataFilterImpl();
            filter.addSingleFilter(null, GWFRtRestriction.RT_PROCESS_UID, filter.getEqualOperator(), rtProcessUId, false);
            if(restrictionUId!=null)
                filter.addSingleFilter(filter.getAndConnector(), GWFRtRestriction.RESTRICTION_UID, filter.getEqualOperator(), restrictionUId, false);
            if(restrictionType!=null)
                filter.addSingleFilter(filter.getAndConnector(), GWFRtRestriction.RESTRICTION_TYPE, filter.getEqualOperator(), restrictionType, false);
            if(subRestrictionType!=null)
                filter.addSingleFilter(filter.getAndConnector(), GWFRtRestriction.SUB_RESTRICTION_TYPE, filter.getEqualOperator(), subRestrictionType, false);
            return UtilEntity.getEntityByFilter(filter, GWFRtRestriction.ENTITY_NAME, true);
        } catch (Throwable th) {
            throw new SystemException("[WorkflowUtil.getRtRestrictions] Exception ", th);
        }
    }

    public static Collection createTransActivationStates(GWFRtRestriction rtRestriction, Collection transitionColl) throws GWFException {
        try {
            List transActStatesColl = new ArrayList(transitionColl.size());
            Iterator transIterator = transitionColl.iterator();
            while (transIterator.hasNext()) {
                GWFTransActivationState transActState = new GWFTransActivationState();
                transActState.setListUId(rtRestriction.getTransActivationStateListUId());
                transActState.setRtRestrictionUId((Long) rtRestriction.getKey());
                transActState.setRestrictionType(rtRestriction.getEntityName());
                transActState.setTransitionUId((Long)transIterator.next());
                transActState.setState(Boolean.FALSE);
                transActStatesColl.add(transActState);
            }
            transActStatesColl=UtilEntity.createEntities(transActStatesColl,true);
            return transActStatesColl;
        } catch (GWFException e) {
            throw e;
        } catch (Throwable th) {
            Logger.warn("[GWFRuntimeUtil.createTransActivationStates] Error ",th);
            throw new GWFException(th.getLocalizedMessage(), th);
        }
    }

    /**
     * Change TransActivationState
     */
    public static GWFTransActivationState changeTransActivationState(Long transActivationStateUId ,boolean state) throws GWFException {
        try {
            GWFTransActivationState transActivationState = (GWFTransActivationState)UtilEntity.getEntityByKey(transActivationStateUId,GWFTransActivationState.ENTITY_NAME,true);
            transActivationState.setState(new Boolean(state));
            transActivationState = (GWFTransActivationState)UtilEntity.updateEntity(transActivationState,false);
            return transActivationState;
        } catch (GWFException e) {
            throw e;
        } catch (Throwable th) {
            throw new GWFException(th.getLocalizedMessage(), th);
        }
    }

    public static Collection getTransActivationStates(Long transitionUId, Long listUId) throws GWFException {
        try{
            IDataFilter filter = new DataFilterImpl();
            filter.addSingleFilter(null, GWFTransActivationState.LIST_UID, filter.getEqualOperator(), listUId, false);
            if (transitionUId != null)
                filter.addSingleFilter(filter.getAndConnector(), GWFTransActivationState.TRANSITION_UID, filter.getEqualOperator(), transitionUId, false);
            return UtilEntity.getEntityByFilter(filter, GWFTransActivationState.ENTITY_NAME, true);
        } catch (GWFException e) {
            throw e;
        } catch (Throwable th) {
            throw new GWFException(th.getLocalizedMessage(), th);
        }
    }


}
