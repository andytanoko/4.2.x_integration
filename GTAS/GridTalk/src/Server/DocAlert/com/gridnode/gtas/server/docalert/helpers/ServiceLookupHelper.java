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
 * Jan 30 2003    Neo Sok Lay         Created
 * Apr 24 2003    Neo Sok Lay         Add getGridTalkAlertMgr().
 */
package com.gridnode.gtas.server.docalert.helpers;

import com.gridnode.gtas.server.alert.facade.ejb.IGridTalkAlertManagerHome;
import com.gridnode.gtas.server.alert.facade.ejb.IGridTalkAlertManagerObj;
import com.gridnode.gtas.server.docalert.facade.ejb.IDocAlertManagerHome;
import com.gridnode.gtas.server.docalert.facade.ejb.IDocAlertManagerObj;

import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerHome;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;

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
   * Get the GridTalkAlertManagerBean.
   *
   * @return The EJBObject for the GridTalkAlertManagerBean.
   */
  public static IGridTalkAlertManagerObj getGridTalkAlertMgr()
    throws ServiceLookupException
  {
    return (IGridTalkAlertManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IGridTalkAlertManagerHome.class.getName(),
             IGridTalkAlertManagerHome.class,
             new Object[0]);
  }

  /**
   * Get the AlertManagerBean.
   *
   * @return The EJBObject for the AlertManagerBean.
   */
  public static IAlertManagerObj getAlertMgr()
    throws ServiceLookupException
  {
    return (IAlertManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IAlertManagerHome.class.getName(),
             IAlertManagerHome.class,
             new Object[0]);
  }

}