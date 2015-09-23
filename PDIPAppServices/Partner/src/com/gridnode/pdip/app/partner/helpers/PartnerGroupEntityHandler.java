/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerGroupEntityHandler.java
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

import com.gridnode.pdip.app.partner.entities.ejb.IPartnerGroupLocalHome;
import com.gridnode.pdip.app.partner.entities.ejb.IPartnerGroupLocalObj;
import com.gridnode.pdip.app.partner.model.PartnerGroup;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.util.ServiceLocator;

public final class PartnerGroupEntityHandler extends LocalEntityHandler
{
  private PartnerGroupEntityHandler()
  {
    super(PartnerGroup.ENTITY_NAME);
  }

  /**
   * Get an instance of a PartnerGroupEntityHandler.
   */
  public static PartnerGroupEntityHandler getInstance()
  {
    PartnerGroupEntityHandler handler = null;
    if (EntityHandlerFactory.hasEntityHandlerFor(PartnerGroup.ENTITY_NAME, true))
    {
      handler = (PartnerGroupEntityHandler)EntityHandlerFactory.getHandlerFor(
                  PartnerGroup.ENTITY_NAME, true);
    }
    else
    {
      handler = new PartnerGroupEntityHandler();
      EntityHandlerFactory.putEntityHandler(PartnerGroup.ENTITY_NAME, true, handler);
    }
    return handler;
  }

  /**
   * Find Partner Group based on Partner Group Name
   *
   * @param name The name of the partner group to find.
   * @return The PartnerGroup found, or <B>null</B> if none exists with that
   * exists.
   */
  public PartnerGroup findByName(String name)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, PartnerGroup.NAME, filter.getEqualOperator(), name, false);

    // use this method for fast-lane reader implementation
    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (PartnerGroup)result.iterator().next();
  }

  /**
   * Find all Partner Group using the Partner Type.
   *
   * @param partnerType The Partner Type of the partner group to find.
   * @return A Collection of Parner Group found, or empty collection if none
   * exists.
   */
  public Collection findByPartnerType(String partnerType)
    throws Throwable
  {
    IEntity entity = PartnerTypeEntityHandler.getInstance().findByName(partnerType);

    if (partnerType == null)
      throw new ApplicationException("Partner Type does not exit: " + partnerType);

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      PartnerGroup.PARTNER_TYPE,
      filter.getEqualOperator(),
      entity.getKey(),
      false);

    // use this method for fast-lane reader implementation
    return getEntityByFilterForReadOnly(filter);
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IPartnerGroupLocalHome.class.getName(),
      IPartnerGroupLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IPartnerGroupLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IPartnerGroupLocalObj.class;
  }
}