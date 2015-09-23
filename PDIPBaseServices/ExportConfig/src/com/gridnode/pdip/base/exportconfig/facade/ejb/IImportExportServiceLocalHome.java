/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IImportExportServiceLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 14 2003    Koh Han Sing        Created
 * Oct 20 2005    Neo Sok Lay         The throws clause of any method in the local home 
 *                                    interface must not include the java.rmi.RemoteException.
 *                                    - Method create in the local home interface throws java.rmi.RemoteException 
 *                                      or a subclass of it
 */
package com.gridnode.pdip.base.exportconfig.facade.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * LocalHome interface for ImportExportServiceBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public interface IImportExportServiceLocalHome
  extends        EJBLocalHome
{
  /**
   * Create the ExportConfigManagerBean.
   *
   * @returns EJBLObject for the ImportExportServiceBean.
   */
  public IImportExportServiceLocalObj create()
    throws CreateException;
}