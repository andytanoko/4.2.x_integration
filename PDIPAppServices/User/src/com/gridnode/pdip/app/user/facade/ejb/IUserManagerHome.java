/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IUserManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 12 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.user.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * LocalHome interface for UserManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public interface IUserManagerHome
  extends        EJBHome
{
  /**
   * Create the UserManagerBean.
   *
   * @returns EJBLObject for the UserManagerBean.
   */
  public IUserManagerObj create()
    throws CreateException, RemoteException;
}