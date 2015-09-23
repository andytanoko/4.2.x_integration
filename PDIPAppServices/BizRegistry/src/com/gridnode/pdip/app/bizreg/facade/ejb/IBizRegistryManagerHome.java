/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IBizRegistryManagerHome.java
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
 * LocalHome interface for BizRegistryManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public interface IBizRegistryManagerHome
  extends        EJBHome
{
  /**
   * Create the BizRegistryManagerBean.
   *
   * @returns EJBObject for the BizRegistryManagerBean.
   */
  public IBizRegistryManagerObj create()
    throws CreateException, RemoteException;
}