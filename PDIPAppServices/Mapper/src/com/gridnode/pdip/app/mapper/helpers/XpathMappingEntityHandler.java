/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XpathMappingEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 13 2003    Koh Han Sing        Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.pdip.app.mapper.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.mapper.entities.ejb.IXpathMappingLocalHome;
import com.gridnode.pdip.app.mapper.entities.ejb.IXpathMappingLocalObj;
import com.gridnode.pdip.app.mapper.model.XpathMapping;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the XpathMappingBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public final class XpathMappingEntityHandler
  extends          LocalEntityHandler
{
  private XpathMappingEntityHandler()
  {
    super(XpathMapping.ENTITY_NAME);
  }

  /**
   * Get an instance of a XpathMappingEntityHandler.
   */
  public static XpathMappingEntityHandler getInstance()
  {
    XpathMappingEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(XpathMapping.ENTITY_NAME, true))
    {
      handler = (XpathMappingEntityHandler)EntityHandlerFactory.getHandlerFor(
                  XpathMapping.ENTITY_NAME, true);
    }
    else
    {
      handler = new XpathMappingEntityHandler();
      EntityHandlerFactory.putEntityHandler(XpathMapping.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  /**
   * Find the XpathMapping whose root element is the specified.
   *
   * @param rootElement The root element in the XpathMapping.
   * @return the XpathMapping having the specified name.
   */
  public XpathMapping findByRootName(String rootElement)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null,
                           XpathMapping.ROOT_ELEMENT,
                           filter.getEqualOperator(),
                           rootElement,
                           false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (XpathMapping)result.iterator().next();
  }

  /**
   * Find the XpathMapping whose xpath uid is the specified.
   *
   * @param uid The uid of the Xpath File.
   * @return the XpathMapping having the specified xpath uid.
   */
  public XpathMapping findByXpathUid(Long uid)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null,
                           XpathMapping.XPATH_UID,
                           filter.getEqualOperator(),
                           uid,
                           false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (XpathMapping)result.iterator().next();
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IXpathMappingLocalHome.class.getName(),
      IXpathMappingLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IXpathMappingLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IXpathMappingLocalObj.class;
  }
}