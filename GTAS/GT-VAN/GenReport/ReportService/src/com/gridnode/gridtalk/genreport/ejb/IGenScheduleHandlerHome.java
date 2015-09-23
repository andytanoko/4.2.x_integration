/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGenScheduleHandlerHome.java
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
 * The Remote interface for the EJB Home of GenScheduleHandlerBean.
 */
public interface IGenScheduleHandlerHome extends EJBHome
{
  /**
   * Create the remote interface for GenScheduleHandlerBean.
   * @return The remote EJB object for GenScheduleHandlerBean.
   * @throws CreateException
   * @throws RemoteException
   */
  public IGenScheduleHandler create() throws CreateException, RemoteException;
}
