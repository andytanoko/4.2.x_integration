/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    Mahesh              Created
 *
 */
package com.gridnode.pdip.app.workflow.engine;

import com.gridnode.pdip.app.workflow.exceptions.*;
import com.gridnode.pdip.app.workflow.runtime.model.*;
import com.gridnode.pdip.base.gwfbase.util.*;
import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.db.keygen.*;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.*;

public abstract class GWFAbstractRestrictionEngine extends GWFAbstractWorkflowEngine {

    public static final String module=Logger.GWF_ENGINE_RESTRICTION;

    abstract public IGWFRouteDispatcher getDispatcher();

    abstract public void onCreate(GWFRtRestriction rtRestriction) throws WorkflowException,SystemException;

    abstract public void onRemove(GWFRtRestriction rtRestriction) throws WorkflowException,SystemException;

    abstract public void selectRoute(GWFRtRestriction rtRestriction) throws WorkflowException,SystemException;


    public GWFRtRestriction createRtRestriction(Long restrictionUId, String restrictionType, String subRestrictionType, Long rtProcessUId) throws WorkflowException,SystemException {
        return createRtRestriction(restrictionUId, restrictionType, subRestrictionType, rtProcessUId,null);
    }

    public GWFRtRestriction createRtRestriction(Long restrictionUId, String restrictionType, String subRestrictionType, Long rtProcessUId,String processDefKey) throws WorkflowException,SystemException {
        try {
            //rtRestriction.setTransActivationStateListUId(KeyGen.getNextId("OneToMany", false));
            GWFRtRestriction rtRestriction = new GWFRtRestriction();
            rtRestriction.setProcessDefKey(processDefKey);
            rtRestriction.setRestrictionUId(restrictionUId);
            rtRestriction.setRtProcessUId(rtProcessUId);
            rtRestriction.setRestrictionType(restrictionType);
            rtRestriction.setSubRestrictionType(subRestrictionType);
            rtRestriction.setTransActivationStateListUId(KeyGen.getNextId("TransActivationStateListUId", true));
            rtRestriction = (GWFRtRestriction) UtilEntity.createEntity(rtRestriction, true);
            onCreate(rtRestriction);
            return rtRestriction;
        } catch (WorkflowException e) {
            throw e;
        } catch (Throwable th) {
        	logWarn("[GWFAbstractRestrictionEngine.createRtRestriction] Exception",th);
            throw new SystemException("[GWFAbstractRestrictionEngine.createRtRestriction] ", th);
        }
    }


    public void remove(GWFRtRestriction rtRestriction) throws WorkflowException,SystemException {
        try {
            UtilEntity.remove((Long)rtRestriction.getKey(), GWFRtRestriction.ENTITY_NAME, true);
            IDataFilter filter = new DataFilterImpl();
            filter.addSingleFilter(null, GWFTransActivationState.LIST_UID, filter.getEqualOperator(), rtRestriction.getTransActivationStateListUId(), false);
            UtilEntity.remove(filter,GWFTransActivationState.ENTITY_NAME,true);

            //RtManagerDelegate.removeRtRestriction(rtRestrictionUId);
            onRemove(rtRestriction);
        } catch (WorkflowException e) {
            throw e;
        } catch (Throwable th) {
        	logWarn("[GWFAbstractRestrictionEngine.remove] Exception",th);
            throw new SystemException("[GWFAbstractRestrictionEngine.remove] ", th);
        }
    }


    protected void logInfo(String msg){
        Logger.log(module,msg);
    }

    protected void logDebug(String msg){
        Logger.debug(module,msg);
    }
    protected void logWarn(String msg,Throwable th) {
    	Logger.warn(module,msg,th);
    }

    /**
     * @deprecated Use logError(String,String,Throwable)
     * @param msg
     * @param th
     */
    protected void logError(String msg,Throwable th){
        Logger.err(module,msg,th);
    }

    protected void logError(String errorCode, String msg, Throwable th) {
    	Logger.error(errorCode, module, msg, th);
    }
}
