/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 27 2002   Mahesh              Created
 *
 */
package com.gridnode.pdip.base.contextdata.facade.ejb;

import java.rmi.*;

import javax.ejb.*;



public interface IDataManagerHome extends EJBHome {

    IDataManagerObj create() throws CreateException, RemoteException;

}
