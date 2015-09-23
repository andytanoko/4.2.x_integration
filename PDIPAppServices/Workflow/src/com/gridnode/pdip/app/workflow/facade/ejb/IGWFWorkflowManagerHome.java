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
package com.gridnode.pdip.app.workflow.facade.ejb;


import java.rmi.*;

import javax.ejb.*;


public interface IGWFWorkflowManagerHome extends EJBHome {
    public IGWFWorkflowManagerObj create() throws CreateException, RemoteException;
}
