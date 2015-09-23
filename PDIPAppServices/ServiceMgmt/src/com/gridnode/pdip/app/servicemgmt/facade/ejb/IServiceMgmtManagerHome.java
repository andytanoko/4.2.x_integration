/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IServiceMgmtManagerHome.java
 *
 ****************************************************************************
 * Date      			Author              Changes
 ****************************************************************************
 * Feb 6, 2004   	Mahesh             	Created
 */
package com.gridnode.pdip.app.servicemgmt.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

public interface IServiceMgmtManagerHome extends EJBHome
{
  /**
   * Create the ServiceMgmtManagerBean.
   *
   * @returns EJBObject for the ServiceMgmtManagerBean.
   */
  public IServiceMgmtManagerObj create() throws CreateException, RemoteException;
}
