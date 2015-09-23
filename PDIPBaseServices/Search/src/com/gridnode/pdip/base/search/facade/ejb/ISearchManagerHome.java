/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    H.Sushil              Created
 */
package com.gridnode.pdip.base.search.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * Home interface for SearchManagerBean.
 *
 * @author H.Sushil
 *
 * @version 1.0
 * @since 1.0
 */
public interface ISearchManagerHome
  extends        EJBHome
{
  /**
   * Create the SearchManagerBean
   *
   * @returns EJBLObject for the SearchManagerBean
   */
  public ISearchManagerObj create()
    throws CreateException, RemoteException;
}