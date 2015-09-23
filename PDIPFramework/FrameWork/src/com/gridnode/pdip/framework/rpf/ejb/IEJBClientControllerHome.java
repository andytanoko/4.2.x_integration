/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEJBClientControllerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 06 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.rpf.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * The Home interface for EJBClientControllerBean
 */
public interface IEJBClientControllerHome extends EJBHome
{
  public IEJBClientControllerObj create()
    throws  CreateException, RemoteException;
}

