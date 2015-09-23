/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IArchiveServiceManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 29, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public interface IArchiveServiceManagerHome extends EJBHome
{
  public IArchiveServiceManagerObj create() throws RemoteException, CreateException;
}
