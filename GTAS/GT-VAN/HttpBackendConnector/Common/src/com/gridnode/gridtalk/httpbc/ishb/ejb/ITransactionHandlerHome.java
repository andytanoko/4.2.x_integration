/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITransactionHandlerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 16, 2007   i00107              Created
 */

package com.gridnode.gridtalk.httpbc.ishb.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author i00107
 * This defines the remote home interface for TransactionHandlerBean.
 */
public interface ITransactionHandlerHome extends EJBHome
{
  /**
   * Creates a remote object for TransactionHandlerBean
   * @return The remote object for TransactionHandlerBean.
   * @throws CreateException
   * @throws RemoteException
   */
  public ITransactionHandler create() throws CreateException, RemoteException;

}
