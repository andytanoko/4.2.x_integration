/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPartnerManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 18 2002    Ang Meng Hua        Created
 */
package com.gridnode.pdip.app.partner.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * The home interface of the PartnerManagerBean.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.1
 */
public interface IPartnerManagerHome extends EJBHome
{
  /**
   * Create the PartnerManagerBean.
   *
   * @returns The remote EJB object of the PartnerManagerBean.
   */
  public IPartnerManagerObj create()
    throws CreateException, RemoteException;
}