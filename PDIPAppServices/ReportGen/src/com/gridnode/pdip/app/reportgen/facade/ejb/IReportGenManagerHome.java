/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IReportGenManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2002    H.Sushil              Created
 */
package com.gridnode.pdip.app.reportgen.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * Home interface for ReportGenManagerBean.
 *
 * @author H.Sushil
 *
 * @version 1.0
 * @since 1.0
 */
public interface IReportGenManagerHome
  extends        EJBHome
{
  /**
   * Create the ReportGenManagerBean
   *
   * @returns EJBLObject for the ReportGenManagerBean
   */
  public IReportGenManagerObj create()
    throws CreateException, RemoteException;
}