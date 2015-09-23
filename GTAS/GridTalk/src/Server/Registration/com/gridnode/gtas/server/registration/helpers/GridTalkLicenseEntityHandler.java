/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkLicenseEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 04 2003    Koh Han Sing        Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.registration.helpers;

import java.util.Collection;

import com.gridnode.gtas.server.registration.entities.ejb.IGridTalkLicenseLocalHome;
import com.gridnode.gtas.server.registration.entities.ejb.IGridTalkLicenseLocalObj;
import com.gridnode.gtas.server.registration.model.GridTalkLicense;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the GridTalkLicenseBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public final class GridTalkLicenseEntityHandler
  extends          LocalEntityHandler
{
  private GridTalkLicenseEntityHandler()
  {
    super(GridTalkLicense.ENTITY_NAME);
  }

  /**
   * Get an instance of a GridTalkLicenseEntityHandler.
   */
  public static GridTalkLicenseEntityHandler getInstance()
  {
    GridTalkLicenseEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(GridTalkLicense.ENTITY_NAME, true))
    {
      handler = (GridTalkLicenseEntityHandler)EntityHandlerFactory.getHandlerFor(
                  GridTalkLicense.ENTITY_NAME, true);
    }
    else
    {
      handler = new GridTalkLicenseEntityHandler();
      EntityHandlerFactory.putEntityHandler(GridTalkLicense.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  /**
   * Find the GridTalkLicense which is link to the specific license.
   *
   * @param licenseUid The uid of the License.
   * @return the GridTalkLicense having the specified license.
   */
  public GridTalkLicense findByLicenseUid(Long licenseUid)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      GridTalkLicense.LICENSE_UID,
      filter.getEqualOperator(),
      licenseUid,
      false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (GridTalkLicense)result.iterator().next();
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IGridTalkLicenseLocalHome.class.getName(),
      IGridTalkLicenseLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IGridTalkLicenseLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IGridTalkLicenseLocalObj.class;
  }
}