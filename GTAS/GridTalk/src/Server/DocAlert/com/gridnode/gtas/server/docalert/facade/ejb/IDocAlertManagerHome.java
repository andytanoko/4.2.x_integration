/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocAlertManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Neo Sok Lay        Created
 */
package com.gridnode.gtas.server.docalert.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * LocalHome interface for DocAlertManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IDocAlertManagerHome
  extends        EJBHome
{
  /**
   * Create the DocAlertManagerBean.
   *
   * @returns EJBLObject for the DocAlertManagerBean.
   */
  public IDocAlertManagerObj create()
    throws CreateException, RemoteException;
}