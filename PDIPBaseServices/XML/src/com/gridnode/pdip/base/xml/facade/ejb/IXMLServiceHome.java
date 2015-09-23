/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IXMLServiceHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Koh Han Sing        Modified to confront to coding standard
 */
package com.gridnode.pdip.base.xml.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface IXMLServiceHome extends EJBHome
{
  public IXMLServiceObj create() throws CreateException, RemoteException;
}