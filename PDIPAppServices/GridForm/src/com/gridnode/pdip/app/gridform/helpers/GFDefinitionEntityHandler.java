/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GFDefinitionEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 * May 24 2002    Jared Low           Added the findByName method.
 * Jun 25 2002    Daniel D'Cotta      Return proxy object to created definition.
 * Jul 25 2002    Daniel D'Cotta      Remove dependency on ejbEntityMap.
 */
package com.gridnode.pdip.app.gridform.helpers;

import com.gridnode.pdip.app.gridform.model.*;
import com.gridnode.pdip.app.gridform.entities.ejb.IGFDefinitionLocalHome;
import com.gridnode.pdip.app.gridform.entities.ejb.IGFDefinitionLocalObj;

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
 * GFDefinitionBean.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class GFDefinitionEntityHandler extends LocalEntityHandler
{
  /**
   * Can only be instantiated by itself. Use <code>getInstance()</code> instead.
   *
   * @since 2.0
   */
  private GFDefinitionEntityHandler()
  {
    super(GFDefinition.ENTITY_NAME);
  }

  /**
   * Get an instance of a GFDefinitionEntityHandler from the
   * EntityHandlerFactory. If none is available, a new instance will be created
   * and stored into the EntityHandlerFactory for reuse.
   *
   * @return An instance of the GFDefinitionEntityHandler.
   * @since 2.0
   */
  public static GFDefinitionEntityHandler getInstance()
  {
    GFDefinitionEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(GFDefinition.ENTITY_NAME, true))
    {
      handler = (GFDefinitionEntityHandler)EntityHandlerFactory.getHandlerFor(GFDefinition.ENTITY_NAME, true);
    }
    else
    {
      handler = new GFDefinitionEntityHandler();
      EntityHandlerFactory.putEntityHandler(GFDefinition.ENTITY_NAME, true, handler);
    }
    return handler;
  }

  /**
   * Create a new GFDefinition.
   *
   * @param definition The GFDefinition entity.
   * @return The proxy object to the created GFDefinition.
   * @since 2.0
   */
  public IGFDefinitionLocalObj createGFDefinition(GFDefinition definition) throws Throwable
  {
    return (IGFDefinitionLocalObj)create(definition);
  }

  /**
   * Update a GFDefinition.
   *
   * @param definition The GFDefinition entity.
   * @since 2.0
   */
  public void updateGFDefinition(GFDefinition definition) throws Throwable
  {
    update(definition);
  }

  /**
   * Delete a GFDefinition. This will physically delete the record from the
   * database.
   *
   * @param uid The UID of the GFDefinition to delete.
   * @since 2.0
   */
  public void deleteGFDefinition(Long uid) throws Throwable
  {
    remove(uid);
  }

  /**
   * Find the GFDefinition that matches the name specified.
   *
   * @param name The name of the GFDefinition.
   * @return The GFDefinition that matches the name specified, null is no match
   * is found.
   * @since 2.0
   */
  public GFDefinition findByName(String name) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, GFDefinition.NAME, filter.getEqualOperator(), name, false);

    Collection result = getEntityByFilterForReadOnly(filter);
    return ((result == null || result.isEmpty()) ? null : (GFDefinition)result.iterator().next());
  }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IGFDefinitionLocalHome.class.getName(),
      IGFDefinitionLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
  {
    return IGFDefinitionLocalHome.class;
  }

  protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IGFDefinitionLocalObj.class;
  }
}