/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGFManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 21 2002    Jared Low           Created.
 */
package com.gridnode.pdip.app.gridform.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

/**
 * Home interface for GFManagerBean.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public interface IGFManagerHome extends EJBHome
{
  /**
   * Create the GFManagerBean.
   *
   * @since 2.0
   * @return The remote interface for GFManagerBean.
   */
  public IGFManagerObj create() throws CreateException, RemoteException;
}
