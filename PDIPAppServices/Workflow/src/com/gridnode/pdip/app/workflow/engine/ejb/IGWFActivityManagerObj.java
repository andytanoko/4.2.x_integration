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
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtActivity;
import com.gridnode.pdip.framework.exceptions.SystemException;
 

public interface IGWFActivityManagerObj  extends EJBObject {
    public GWFRtActivity createRtActivity(GWFRtActivity rtActivity)  throws WorkflowException, SystemException, RemoteException;
    public GWFRtActivity changeActivityState(Long rtActivityUId,Integer state) throws WorkflowException, SystemException,RemoteException;
    public void selectActivityRoute(Long rtActivityUId) throws WorkflowException,SystemException, RemoteException;
    public void removeRtActivity(Long rtActivityUId) throws WorkflowException, SystemException, RemoteException;
}