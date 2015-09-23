/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 31 2002    MAHESH              Created
 */
package com.gridnode.pdip.app.workflow.runtime.ejb;


import java.rmi.*;

import javax.ejb.*;


public interface IGWFRtManagerHome extends EJBHome {
    public IGWFRtManagerObj create() throws CreateException, RemoteException;
}
