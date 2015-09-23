/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridNodeManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 09 2002    Neo Sok Lay         Created
 * Oct 20 2005    Neo Sok Lay         method create found but does not throw javax.ejb.CreateException 
 *                                    as required
 */
package com.gridnode.gtas.server.gridnode.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * EJBHome for GridNodeManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.0 I5
 */
public interface IGridNodeManagerHome extends EJBHome
{
  public IGridNodeManagerObj create() throws CreateException, RemoteException;
}