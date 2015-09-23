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
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.framework.exceptions.SystemException;
 

public interface IGWFProcessManagerObj extends EJBObject {

    public GWFRtProcess createRtProcess(GWFRtProcess rtProcess) throws WorkflowException ,SystemException, RemoteException;
    public GWFRtProcess changeProcessState(Long rtProcessUId, Integer state) throws WorkflowException,SystemException, RemoteException;
    public void cancelRtProcess(Long rtProcessUId,Object reason) throws WorkflowException, SystemException, RemoteException;
    public void selectProcessRoute(Long rtProcessUId) throws WorkflowException,SystemException, RemoteException;
    public void removeRtProcess(Long rtProcessUId) throws WorkflowException, SystemException, RemoteException;
}
