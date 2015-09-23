/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGWFBaseManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 6, 2008   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.gwfbase.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public interface IGWFBaseManagerHome extends EJBHome
{
  public IGWFBaseManagerObj create() throws CreateException, RemoteException;
}
