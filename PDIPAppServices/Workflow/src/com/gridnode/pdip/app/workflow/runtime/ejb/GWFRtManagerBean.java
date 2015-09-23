/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 03 2002   MAHESH              Created
 * Dec 14 2005    Tam Wei Xiang       To resolve the pass by value issue:
 *                                    use UtilEntity.updateEntity instead of
 *                                    UtilEntity.update
 */
package com.gridnode.pdip.app.workflow.runtime.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtRestriction;
import com.gridnode.pdip.app.workflow.runtime.model.GWFTransActivationState;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.keygen.KeyGen;
import com.gridnode.pdip.framework.exceptions.domain.GWFException;
import com.gridnode.pdip.framework.util.UtilEntity;

public class GWFRtManagerBean implements SessionBean {
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3419459437486061437L;
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


    public GWFRtRestriction createRtRestrictionIfNotExists(String subRestrictionType, Long restrictionUId, String restrictionType, Long rtProcessUId) throws GWFException {
        try {
            GWFRtRestriction rtRestriction = getRtRestriction(subRestrictionType, restrictionUId, restrictionType, rtProcessUId);
            if(rtRestriction==null){
                rtRestriction=createRtRestriction(restrictionUId, restrictionType, subRestrictionType, rtProcessUId);
            }
            return rtRestriction;
        } catch (GWFException e) {
            throw e;
        } catch (Throwable th) {
            throw new GWFException(th.getLocalizedMessage(), th);
        }
    }

    public GWFRtRestriction createRtRestriction(Long restrictionUId, String restrictionType, String subRestrictionType, Long rtProcessUId) throws GWFException {
        try {
            GWFRtRestriction rtRestriction = new GWFRtRestriction();
            rtRestriction.setRestrictionUId(restrictionUId);
            rtRestriction.setRtProcessUId(rtProcessUId);
            rtRestriction.setRestrictionType(restrictionType);
            rtRestriction.setSubRestrictionType(subRestrictionType);
            rtRestriction.setTransActivationStateListUId(KeyGen.getNextId("TransActivationStateListUId", true));
            rtRestriction = (GWFRtRestriction) UtilEntity.createEntity(rtRestriction, true);
            return rtRestriction;
        } catch (GWFException e) {
            throw e;
        } catch (Throwable th) {
            throw new GWFException(th.getLocalizedMessage(), th);
        }
    }



    public GWFRtProcess incrementMaxConcurrency(Long rtProcessUId,int incrementValue) throws GWFException {
        try {
            GWFRtProcess rtProcess = (GWFRtProcess)UtilEntity.getEntityByKey(rtProcessUId,GWFRtProcess.ENTITY_NAME,true);
            rtProcess.setMaxConcurrency(new Integer(rtProcess.getMaxConcurrency().intValue()+incrementValue));
            rtProcess = (GWFRtProcess)UtilEntity.updateEntity(rtProcess,false);
            return rtProcess;
        } catch (GWFException e) {
            throw e;
        } catch (Throwable th) {
            throw new GWFException(th.getLocalizedMessage(), th);
        }
    }



    /**
     * Removes runtime Restriction
     */
    public void removeRtRestriction(Long rtRestrictionUId) throws GWFException {
        try {
            GWFRtRestriction rtRestriction=(GWFRtRestriction)UtilEntity.getEntityByKey(rtRestrictionUId, GWFRtRestriction.ENTITY_NAME, true);
            UtilEntity.remove(rtRestrictionUId, GWFRtRestriction.ENTITY_NAME, true);
            IDataFilter filter = new DataFilterImpl();
            filter.addSingleFilter(null, GWFTransActivationState.LIST_UID, filter.getEqualOperator(), rtRestriction.getTransActivationStateListUId(), false);
            UtilEntity.remove(filter,GWFTransActivationState.ENTITY_NAME,true);
        } catch (GWFException e) {
            throw e;
        } catch (Throwable th) {
            throw new GWFException(th.getLocalizedMessage(), th);
        }
    }

    public GWFRtProcess getRtProcess(Long rtProcessUId) throws GWFException {
        try{
            return (GWFRtProcess)UtilEntity.getEntityByKey(rtProcessUId,GWFRtProcess.ENTITY_NAME,true);
        } catch (Throwable th) {
            throw new GWFException(th.getLocalizedMessage(), th);
        }
    }

    public GWFRtRestriction getRtRestriction(Long rtRestrictionUId) throws GWFException {
        try{
            return (GWFRtRestriction)UtilEntity.getEntityByKey(rtRestrictionUId,GWFRtRestriction.ENTITY_NAME,true);
        } catch (Throwable th) {
            throw new GWFException(th.getLocalizedMessage(), th);
        }
    }

    public GWFRtActivity getRtActivity(Long rtActivityUId) throws GWFException {
        try{
            return (GWFRtActivity)UtilEntity.getEntityByKey(rtActivityUId,GWFRtActivity.ENTITY_NAME,true);
        } catch (Throwable th) {
            throw new GWFException(th.getLocalizedMessage(), th);
        }
    }

    public Collection getRtActivityByFilter(IDataFilter filter) throws GWFException {
        try{
            return UtilEntity.getEntityByFilter(filter,GWFRtActivity.ENTITY_NAME,true);
        }catch(Throwable th){
            throw new GWFException(th.getLocalizedMessage(), th);
        }
    }
    public Collection getRtProcessByFilter(IDataFilter filter) throws GWFException {
        try{
            return UtilEntity.getEntityByFilter(filter,GWFRtProcess.ENTITY_NAME,true);
        }catch(Throwable th){
            throw new GWFException(th.getLocalizedMessage(), th);
        }
    }
    public Collection getRtRestrictionByFilter(IDataFilter filter) throws GWFException {
        try{
            return UtilEntity.getEntityByFilter(filter,GWFRtRestriction.ENTITY_NAME,true);
        }catch(Throwable th){
            throw new GWFException(th.getLocalizedMessage(), th);
        }
    }

    /**
     * Change TransActivationState
     */
    public GWFTransActivationState changeTransActivationState(Long transActivationStateUId ,boolean state) throws GWFException {
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

    public GWFRtRestriction getRtRestriction(String subRestrictionType, Long restrictionUId, String restrictionType, Long rtProcessUId) throws GWFException {
        Collection rtRestrictionColl =getRtRestrictions(subRestrictionType, restrictionUId, restrictionType, rtProcessUId);
        if(rtRestrictionColl!=null && rtRestrictionColl.size()>0)
            return (GWFRtRestriction)rtRestrictionColl.iterator().next();
        else return null;
    }

    public Collection getRtRestrictions(String subRestrictionType, Long restrictionUId, String restrictionType, Long rtProcessUId) throws GWFException {
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
        } catch (GWFException e) {
            throw e;
        } catch (Throwable th) {
            throw new GWFException(th.getLocalizedMessage(), th);
        }
    }


    public Collection getTransActivationStates(Long transitionUId, Long listUId) throws GWFException {
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
