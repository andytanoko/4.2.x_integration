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

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtRestriction;
import com.gridnode.pdip.framework.exceptions.SystemException;
 
public interface IGWFRestrictionManagerObj  extends EJBObject {

    public GWFRtRestriction createRtRestriction(Long restrictionUId, String restrictionType, String subRestrictionType, Long rtProcessUId, String engineType) throws WorkflowException,SystemException, RemoteException;
    public void selectRestrictionRoute(Long rtRestrictionUId,String engineType) throws WorkflowException,SystemException,RemoteException;
    public GWFRtRestriction removeRtRestriction(Long rtRestrictionUId, String engineType) throws WorkflowException,SystemException, RemoteException;
}
