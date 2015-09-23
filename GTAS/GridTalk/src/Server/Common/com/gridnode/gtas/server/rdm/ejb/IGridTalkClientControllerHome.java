/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridTalkClientControllerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 06 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.rdm.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * The Home interface for GridTalkClientControllerBean
 */
public interface IGridTalkClientControllerHome extends EJBHome
{
  public IGridTalkClientControllerObj create()
    throws CreateException, RemoteException;
}

