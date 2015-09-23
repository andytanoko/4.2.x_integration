/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMappingManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 27 2002    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * Home interface for MappingManagerBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IMappingManagerHome
  extends        EJBHome
{
  /**
   * Create the MappingManagerBean.
   *
   * @returns EJBLObject for the MappingManagerBean.
   */
  public IMappingManagerObj create()
    throws CreateException, RemoteException;
}