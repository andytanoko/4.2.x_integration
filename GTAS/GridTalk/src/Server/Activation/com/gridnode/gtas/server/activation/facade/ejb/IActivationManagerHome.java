/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IActivationManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 10 2002    Neo Sok Lay         Created
 * Oct 20 2005    Neo Sok Lay         method create found but does not throw javax.ejb.CreateException as required
 */
package com.gridnode.gtas.server.activation.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

/**
 * EJBHome for ActivationManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0 I6
 */
public interface IActivationManagerHome extends EJBHome
{
  public IActivationManagerObj create() throws CreateException, RemoteException;
}