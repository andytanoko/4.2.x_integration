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
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtRestriction;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.domain.GWFException;
import com.gridnode.pdip.framework.util.UtilEntity;
 
public class GWFRestrictionManagerBean implements SessionBean {
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 560991441375466673L;
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
     * creates the runtime restriction
     * @param restrictionUId
     * @param restrictionType
     * @param subRestrictionType
     * @param rtProcessUId
     * @param engineType
     * @return
     * @throws GWFException
     * @throws SystemException
     */
    public GWFRtRestriction createRtRestriction(Long restrictionUId, String restrictionType, String subRestrictionType, Long rtProcessUId, String engineType) throws WorkflowException,SystemException  {
        return GWFFactory.getRestrictionEngine(engineType).createRtRestriction(restrictionUId, restrictionType, subRestrictionType, rtProcessUId);
    }
    /**
     * calls the restriction engine to route the restriction
     * @param rtRestrictionUId
     * @param engineType
     * @throws GWFException
     * @throws SystemException
     */
    public void selectRestrictionRoute(Long rtRestrictionUId,String engineType) throws WorkflowException,SystemException {
        try{
            GWFRtRestriction rtRestriction=(GWFRtRestriction)UtilEntity.getEntityByKey(rtRestrictionUId,GWFRtRestriction.ENTITY_NAME,true);
            GWFFactory.getRestrictionEngine(engineType).selectRoute(rtRestriction);
        }catch(WorkflowException ex){
            throw ex;
        }catch(Throwable th){
            throw new SystemException("[GWFRestrictionManagerBean.selectRestrictionRoute] Error ",th);
        }
    }


    /**
     * Removes runtime activity
     * @param rtRestrictionUId
     * @param engineType
     * @throws GWFException
     * @throws SystemException
     */
    public GWFRtRestriction removeRtRestriction(Long rtRestrictionUId, String engineType) throws WorkflowException,SystemException {
        try{
            GWFRtRestriction rtRestriction=(GWFRtRestriction)UtilEntity.getEntityByKey(rtRestrictionUId,GWFRtRestriction.ENTITY_NAME,true);
            GWFFactory.getRestrictionEngine(engineType).remove(rtRestriction);
            return rtRestriction;
        }catch(WorkflowException ex){
            throw ex;
        }catch(Throwable th){
            throw new SystemException("[GWFRestrictionManagerBean.removeRtRestriction] Error ",th);
        }
    }
}
