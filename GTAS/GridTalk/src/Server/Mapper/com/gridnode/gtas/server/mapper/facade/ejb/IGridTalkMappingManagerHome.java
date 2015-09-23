/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridTalkMappingManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 04 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.mapper.facade.ejb;

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
public interface IGridTalkMappingManagerHome
  extends        EJBHome
{
  /**
   * Create the GridTalkMappingManagerBean.
   *
   * @returns EJBLObject for the GridTalkMappingManagerBean.
   */
  public IGridTalkMappingManagerObj create()
    throws CreateException, RemoteException;
}