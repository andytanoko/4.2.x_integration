/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRegistrationServiceHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.product.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

import java.rmi.RemoteException;

/**
 * Home interface for the RegistrationBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public interface IRegistrationServiceHome extends EJBHome
{
  public IRegistrationServiceObj create() throws CreateException, RemoteException;
}