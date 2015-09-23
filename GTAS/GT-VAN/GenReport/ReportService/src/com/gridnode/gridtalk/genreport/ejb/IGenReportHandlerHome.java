/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGenReportHandlerHome.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Jan 19, 2007        Regina Zeng          Created
 */

package com.gridnode.gridtalk.genreport.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author Regina Zeng
 * This defines the remote home interface for GenReportHandlerBean. 
 */
public interface IGenReportHandlerHome extends EJBHome
{
  /**
   * Creates a remote object for GenReportHandlerBean
   * @return The remote object for GenReportHandlerBean
   * @throws CreateException
   * @throws RemoteException
   */
  public IGenReportHandler create() throws CreateException, RemoteException; 
}
