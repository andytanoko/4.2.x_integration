/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEnterpriseHierarchyManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 05 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * LocalHome interface for EnterpriseHierarchyManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public interface IEnterpriseHierarchyManagerHome
  extends        EJBHome
{
  /**
   * Create the EnterpriseHierarchyManagerBean.
   *
   * @returns EJBObject for the EnterpriseHierarchyManagerBean.
   */
  public IEnterpriseHierarchyManagerObj create()
    throws CreateException, RemoteException;
}