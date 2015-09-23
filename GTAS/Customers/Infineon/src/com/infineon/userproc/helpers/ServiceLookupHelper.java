/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceLookupHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 04 2003    Neo Sok Lay         Created
 */
package com.infineon.userproc.helpers;

import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerHome;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerObj;
import com.gridnode.gtas.server.docalert.facade.ejb.IDocAlertManagerHome;
import com.gridnode.gtas.server.docalert.facade.ejb.IDocAlertManagerObj;

import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;

/**
 * This is a helper class that provides the service lookups.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class ServiceLookupHelper
{

  /**
   * Get the DocAlertManagerBean.
   *
   * @return The EJBObject for the DocAlertManagerBean.
   */
  public static IDocAlertManagerObj getDocAlertMgr() throws ServiceLookupException
  {
    return (IDocAlertManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IDocAlertManagerHome.class.getName(),
             IDocAlertManagerHome.class,
             new Object[0]);
  }

  /**
   * Get the DocumentManagerBean.
   *
   * @return The EJBObject for the DocumentManagerBean.
   */
  public static IDocumentManagerObj getDocumentMgr() throws ServiceLookupException
  {
    return (IDocumentManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IDocumentManagerHome.class.getName(),
             IDocumentManagerHome.class,
             new Object[0]);
  }

  public static IRnifManagerObj getRnifManager()
    throws ServiceLookupException
  {
    return (IRnifManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IRnifManagerHome.class.getName(),
             IRnifManagerHome.class,
             new Object[0]);
  }


}