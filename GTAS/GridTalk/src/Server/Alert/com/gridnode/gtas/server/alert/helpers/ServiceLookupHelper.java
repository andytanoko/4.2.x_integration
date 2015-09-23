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
 * Apr 22 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.helpers;

import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerHome;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.gtas.server.alert.facade.ejb.IGridTalkAlertManagerHome;
import com.gridnode.gtas.server.alert.facade.ejb.IGridTalkAlertManagerObj;

import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;

/**
 * This is a helper class that provides the service lookups.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class ServiceLookupHelper
{

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
   * Obtain the EJBObject for the AlertManagerBean.
   *
   * @return The EJBObject to the AlertManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.1
   */
  public static IAlertManagerObj getAlertManager()
         throws ServiceLookupException
  {
    return (IAlertManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IAlertManagerHome.class.getName(),
      IAlertManagerHome.class,
      new Object[0]);
  }

}