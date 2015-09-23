/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridTalkAlertManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 21 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * LocalHome interface for GridTalkAlertManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public interface IGridTalkAlertManagerHome
  extends        EJBHome
{
  /**
   * Create the GridTalkAlertManagerBean.
   *
   * @returns EJBLObject for the GridTalkAlertManagerBean.
   */
  public IGridTalkAlertManagerObj create()
    throws CreateException, RemoteException;
}