/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISessionManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 14 2002    Ooi Hui Linn         Created
 */
package com.gridnode.pdip.base.session.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

/**
 * (Remote)Home interface for SessionManagerBean.
 *
 * @author Ooi Hui Linn
 *
 * @version 2.0
 * @since 2.0
 */
public interface ISessionManagerHome
  extends        EJBHome
{

  /**
   * Create the SessionManagerBean.
   *
   * @returns EJBLObject for the UserManagerBean.
   */
  public ISessionManagerObj create()
    throws CreateException, RemoteException;
}