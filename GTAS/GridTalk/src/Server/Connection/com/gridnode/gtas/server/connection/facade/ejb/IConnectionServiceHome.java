/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IConnectionServiceHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.facade.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

import java.rmi.RemoteException;

/**
 * Home interface for the ConnectionBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface IConnectionServiceHome extends EJBHome
{
  public IConnectionServiceObj create() throws CreateException, RemoteException;
}