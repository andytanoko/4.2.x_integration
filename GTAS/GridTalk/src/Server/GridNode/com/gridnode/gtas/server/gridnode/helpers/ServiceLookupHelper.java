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
 * Sep 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.helpers;

import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerHome;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerObj;

import com.gridnode.pdip.app.coyprofile.facade.ejb.ICoyProfileManagerHome;
import com.gridnode.pdip.app.coyprofile.facade.ejb.ICoyProfileManagerObj;

import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;

/**
 * This class contains helper methods for looking up the EJBs from within
 * the GridNode module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class ServiceLookupHelper
{

  public ServiceLookupHelper()
  {
  }

  /**
   * Get the CoyProfileManagerBean proxy object.
   *
   * @return The EJB proxy object for CoyProfileManagerBean.
   * @exception ServiceLookupException Error in looking up the CoyProfileManagerBean.
   *
   * @since 2.0 I5
   */
  public static ICoyProfileManagerObj getCoyProfileManager()
    throws ServiceLookupException
  {
    return (ICoyProfileManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      ICoyProfileManagerHome.class.getName(),
      ICoyProfileManagerHome.class,
      new Object[0]);
  }

  /**
   * Get the GridNodeManagerBean proxy object.
   *
   * @return The EJB proxy object for GridNodeManagerBean.
   * @exception ServiceLookupException Error in looking up the GridNodeManagerBean.
   *
   * @since 2.0 I5
   */
  public static IGridNodeManagerObj getGridNodeManager()
    throws ServiceLookupException
  {
    return (IGridNodeManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IGridNodeManagerHome.class.getName(),
      IGridNodeManagerHome.class,
      new Object[0]);
  }

}