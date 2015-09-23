/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityInfo
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 03-June-2002    Jagadeesh           Created.
 */

package com.gridnode.pdip.base.security.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;


/**
 * The ISecuirtyServiceHome interface extends EJBHome interface.
 *
 * The home interface defines the method that allows the remote Client to create,
 * find and remove EJB Objects.
 *
 */


public interface ISecurityServiceHome extends EJBHome
{
  public ISecurityServiceObj create() throws CreateException,RemoteException;

}