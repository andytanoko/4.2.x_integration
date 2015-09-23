/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerTypeEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 05 2002    Ang Meng Hua        Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.pdip.app.partner.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.partner.entities.ejb.IPartnerTypeLocalHome;
import com.gridnode.pdip.app.partner.entities.ejb.IPartnerTypeLocalObj;
import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

public final class PartnerTypeEntityHandler extends LocalEntityHandler
{
  private PartnerTypeEntityHandler()
  {
    super(PartnerType.ENTITY_NAME);
  }

  /**
   * Get an instance of a PartnerTypeEntityHandler.
   */
  public static PartnerTypeEntityHandler getInstance()
  {
    PartnerTypeEntityHandler handler = null;
    if (EntityHandlerFactory.hasEntityHandlerFor(PartnerType.ENTITY_NAME, true))
    {
      handler = (PartnerTypeEntityHandler)EntityHandlerFactory.getHandlerFor(
                  PartnerType.ENTITY_NAME, true);
    }
    else
    {
      handler = new PartnerTypeEntityHandler();
      EntityHandlerFactory.putEntityHandler(PartnerType.ENTITY_NAME, true, handler);
    }
    return handler;
  }

  /**
   * Find Partner Type based on Partner Type Name
   *
   * @param name The name of the partner type to find.
   * @return The PartnerType found, or <B>null</B> if none exists with that
   * exists.
   */
  public PartnerType findByName(String name)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, PartnerType.NAME, filter.getEqualOperator(), name, false);

    // use this method for fast-lane reader implementation
    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (PartnerType)result.iterator().next();
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IPartnerTypeLocalHome.class.getName(),
      IPartnerTypeLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IPartnerTypeLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IPartnerTypeLocalObj.class;
  }
}