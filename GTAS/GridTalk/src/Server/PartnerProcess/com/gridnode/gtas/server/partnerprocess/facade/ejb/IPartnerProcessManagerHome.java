/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPartnerProcessManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerprocess.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * LocalHome interface for PartnerProcessManagerBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IPartnerProcessManagerHome
  extends        EJBHome
{
  /**
   * Create the PartnerProcessManagerBean.
   *
   * @returns EJBLObject for the PartnerProcessManagerBean.
   */
  public IPartnerProcessManagerObj create()
    throws CreateException, RemoteException;
}