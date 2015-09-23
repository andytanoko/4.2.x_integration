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
 * Oct 20 2005    Neo Sok Lay         The throws clause of any method in the local 
 *                                    home interface must not include the java.rmi.RemoteException.
 *                                    - Method create in the local home interface throws java.rmi.RemoteException 
 *                                      or a subclass of it
 */
package com.gridnode.pdip.base.xml.facade.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
 
public interface IXMLServiceLocalHome extends EJBLocalHome
{
  public IXMLServiceLocalObj create() throws CreateException;
}