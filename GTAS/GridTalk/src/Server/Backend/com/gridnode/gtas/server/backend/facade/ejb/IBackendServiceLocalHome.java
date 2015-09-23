/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IBackendServiceLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.backend.facade.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * LocalHome interface for BackendServiceBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public interface IBackendServiceLocalHome
  extends        EJBLocalHome
{
  /**
   * Create the BackendServiceBean.
   *
   * @returns EJBLocalHome for the BackendServiceBean.
   */
  public IBackendServiceLocalObj create()
    throws CreateException;
}