/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchQueryManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.searchquery.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * Home interface for SearchQueryManagerBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public interface ISearchQueryManagerHome
  extends        EJBHome
{
  /**
   * Create the SearchQueryManagerBean.
   *
   * @returns EJBLObject for the SearchQueryManagerBean.
   */
  public ISearchQueryManagerObj create()
    throws CreateException, RemoteException;
}