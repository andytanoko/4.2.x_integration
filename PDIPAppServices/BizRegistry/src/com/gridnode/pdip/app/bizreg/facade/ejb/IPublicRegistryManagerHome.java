/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPublicRegistryManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * LocalHome interface for PublicRegistryManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public interface IPublicRegistryManagerHome
  extends        EJBHome
{
  /**
   * Create the PublicRegistryManagerBean.
   *
   * @returns EJBObject for the PublicRegistryManagerBean.
   */
  public IPublicRegistryManagerObj create()
    throws CreateException, RemoteException;
}