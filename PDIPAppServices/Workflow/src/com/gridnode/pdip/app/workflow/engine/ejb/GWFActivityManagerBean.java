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
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.UtilEntity;
 
public class GWFActivityManagerBean implements SessionBean {
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4602562323252320545L;
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
     * Creates runtime activity
     * @param activity
     * @return GWFRtActivity
     */
    public GWFRtActivity createRtActivity(GWFRtActivity rtActivity) throws WorkflowException ,SystemException{
        String engineType=rtActivity.getEngineType();
        return GWFFactory.getActivityEngine(engineType).createRtActivity(rtActivity);
    }

    /**
     * calls the activity engine to change the activity state
     * @param rtActivityUId
     * @param state
     * @return GWFRtActivity
     */
    public GWFRtActivity changeActivityState(Long rtActivityUId,Integer state) throws WorkflowException,SystemException {
        GWFRtActivity rtActivity=getRtActivity(rtActivityUId);
        String engineType=rtActivity.getEngineType();
        return GWFFactory.getActivityEngine(engineType).changeRtActivityState(rtActivity,state);
    }

    /**
     * calls the activity engine to route the activity depending on state
     * @param rtActivityUId
     * @param state
     * @return GWFRtActivity
     */
    public void selectActivityRoute(Long rtActivityUId) throws WorkflowException,SystemException {
        GWFRtActivity rtActivity=getRtActivity(rtActivityUId);
        String engineType=rtActivity.getEngineType();
        GWFFactory.getActivityEngine(engineType).selectRoute(rtActivity);
    }

    /**
     * Removes runtime activity
     * @param rtActivityUId
     */
    public void removeRtActivity(Long rtActivityUId) throws WorkflowException,SystemException {
        GWFRtActivity rtActivity=getRtActivity(rtActivityUId);
        String engineType=rtActivity.getEngineType();
        GWFFactory.getActivityEngine(engineType).remove(rtActivity);
    }

    /**
     * helper method to reterive runtime activity
     */
    public GWFRtActivity getRtActivity(Long rtActivityUId) throws SystemException {
        try{
            return (GWFRtActivity)UtilEntity.getEntityByKey(rtActivityUId,GWFRtActivity.ENTITY_NAME,true);
        }catch(Throwable th){
            throw new SystemException("Error in getting RtActivity, rtActivityUId="+rtActivityUId,th);
        }
    }
}
