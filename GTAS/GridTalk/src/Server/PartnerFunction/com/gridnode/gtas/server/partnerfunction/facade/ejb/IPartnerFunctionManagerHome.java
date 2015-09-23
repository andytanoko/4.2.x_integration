/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPartnerFunctionManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 12 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerfunction.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * LocalHome interface for PartnerFunctionManagerBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IPartnerFunctionManagerHome
  extends        EJBHome
{
  /**
   * Create the PartnerFunctionManagerBean.
   *
   * @returns EJBLObject for the PartnerFunctionManagerBean.
   */
  public IPartnerFunctionManagerObj create()
    throws CreateException, RemoteException;
}