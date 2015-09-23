/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityHandlerHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 08 2003    Koh Han Sing        Created
 * Oct 17 2005    Neo Sok Lay         For JDK1.5 compliance: reflection invocation
 *                                    must explicit cast for null args.
 * Feb 09 2007		Alain Ah Ming				Add error code to error log or use warning log                                   
 */
package com.gridnode.pdip.base.exportconfig.helpers;

import com.gridnode.pdip.framework.db.AbstractEntityHandler;
import com.gridnode.pdip.framework.db.DefaultEntityHandler;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;
import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * This helper class helps to retrieve the EntityHandler for different types
 * of entity.
 *
 * @author Koh Han Sing
 *
 * @version GT 4.0 VAN
 * @since 2.0
 */
public final class EntityHandlerHelper
{
  public static AbstractEntityHandler getHandler(String entityName)
    throws Exception
  {
    Logger.debug("[EntityHandlerHelper.getHandler] Get Handler for entity : "+ entityName);
    AbstractEntityHandler handler = null;
    if (EntityHandlerFactory.hasEntityHandlerFor(entityName, true))
    {
      handler = EntityHandlerFactory.getHandlerFor(entityName, true);
    }
    else
    {
      try
      {
        EntityMetaInfo metaInfo =
          MetaInfoFactory.getInstance().getMetaInfoFor(entityName);
        String fullClassName = metaInfo.getObjectName();
        String parentName = fullClassName.substring(0, fullClassName.indexOf(IMetaInfoConstants.PACKAGE_MODEL));
        String handlerClassName = parentName + IMetaInfoConstants.PACKAGE_HELPERS_DOT + entityName + IMetaInfoConstants.CLASS_ENTITY_HANDLER;
        Logger.debug("[EntityHandlerHelper.getHandler] handlerClassName : "+handlerClassName);
        Class handlerClass = Class.forName(handlerClassName);
        handler = (AbstractEntityHandler)handlerClass.getMethod(IMetaInfoConstants.MTD_GET_INSTANCE, (Class[])null).invoke(null, (Object[])null);
      }
      catch (ClassNotFoundException ex)
      {
        Logger.warn("[EntityHandlerHelper.getHandler] ClassNotFoundException", ex);
        handler = new DefaultEntityHandler(entityName);
      }
      catch (NoSuchMethodException nsmex)
      {
        Logger.warn("[EntityHandlerHelper.getHandler] NoSuchMethodException", nsmex);
        handler = new DefaultEntityHandler(entityName);
      }
      catch (InvocationTargetException itex)
      {
        Logger.warn("[EntityHandlerHelper.getHandler] InvocationTargetException", itex);
        handler = new DefaultEntityHandler(entityName);
      }

      EntityHandlerFactory.putEntityHandler(entityName, true, handler);
    }

    return handler;
  }
}