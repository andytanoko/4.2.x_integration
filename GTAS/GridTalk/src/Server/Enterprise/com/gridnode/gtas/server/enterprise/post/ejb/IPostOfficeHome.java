/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPostOfficeHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 06 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.post.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * LocalHome interface for PostOfficeBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface IPostOfficeHome
  extends        EJBHome
{
  /**
   * Create the PostOfficeBean.
   *
   * @returns EJBObject for the PostOfficeBean.
   */
  public IPostOfficeObj create()
    throws CreateException, RemoteException;
}