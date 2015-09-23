/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GFTemplateEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    Jared Low           Created.
 * May 24 2002    Jared Low           Added the findByName method.
 * Jun 25 2002    Daniel D'Cotta      Return proxy object to created template.
 * Jul 25 2002    Daniel D'Cotta      Remove dependency on ejbEntityMap.
 */
package com.gridnode.pdip.app.gridform.helpers;

import com.gridnode.pdip.app.gridform.model.*;
import com.gridnode.pdip.app.gridform.entities.ejb.IGFTemplateLocalHome;
import com.gridnode.pdip.app.gridform.entities.ejb.IGFTemplateLocalObj;

import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.dao.*;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Helper class to handle calls to the home and remote interfaces of the
 * GFTemplateBean.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class GFTemplateEntityHandler extends LocalEntityHandler
{
  /**
   * Can only be instantiated by itself. Use <code>getInstance()</code> instead.
   *
   * @since 2.0
   */
  private GFTemplateEntityHandler()
  {
    super(GFTemplate.ENTITY_NAME);
  }

  /**
   * Get an instance of a GFTemplateEntityHandler from the
   * EntityHandlerFactory. If none is available, a new instance will be created
   * and stored into the EntityHandlerFactory for reuse.
   *
   * @since 2.0
   * @return An instance of the GFTemplateEntityHandler.
   */
  public static GFTemplateEntityHandler getInstance()
  {
    GFTemplateEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(GFTemplate.ENTITY_NAME, true))
    {
      handler = (GFTemplateEntityHandler)EntityHandlerFactory.getHandlerFor(GFTemplate.ENTITY_NAME, true);
    }
    else
    {
      handler = new GFTemplateEntityHandler();
      EntityHandlerFactory.putEntityHandler(GFTemplate.ENTITY_NAME, true, handler);
    }
    return handler;
  }

  /**
   * Create a new GFTemplate.
   *
   * @param template The GFTemplate entity.
   * @return The proxy object to the created GFTemplate.
   */
  public IGFTemplateLocalObj createGFTemplate(GFTemplate template) throws Throwable
  {
    return (IGFTemplateLocalObj)create(template);
  }

  /**
   * Update a GFTemplate.
   *
   * @param template The GFTemplate entity.
   */
  public void updateGFTemplate(GFTemplate template) throws Throwable
  {
    update(template);
  }

  /**
   * Delete a GFTemplate. This will physically delete the record from the
   * database.
   *
   * @param uid The UID of the GFTemplate to delete.
   */
  public void deleteGFTemplate(Long uid) throws Throwable
  {
    remove(uid);
  }

  /**
   * Find the GFTemplate that matches the name specified.
   *
   * @param name The name of the GFTemplate.
   * @return The GFTemplate that matches the name specified, null is no match is
   * found.
   * @since 2.0
   */
  public GFTemplate findByName(String name) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, GFTemplate.NAME, filter.getEqualOperator(), name, false);

    Collection result = getEntityByFilterForReadOnly(filter);
    return ((result == null || result.isEmpty()) ? null : (GFTemplate)result.iterator().next());
  }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IGFTemplateLocalHome.class.getName(),
      IGFTemplateLocalHome.class);
  }
  */

  protected Class getHomeInterfaceClass() throws Exception
  {
    return IGFTemplateLocalHome.class;
  }

  protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IGFTemplateLocalObj.class;
  }
}