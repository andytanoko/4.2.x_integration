/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityHandlerFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 12 2002    Neo Sok Lay         Add method to add custom EntityHandlers.
 */
package com.gridnode.pdip.framework.db;

/**
 * <p>Title: </p>
 * <p>Description: This class keeps the single instance of the DefaultEntityHandler
 * for each entity and returns the respective DefaultEntityHandler on calling getHandlerFor method</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Mahesh
 * @version 1.0
 */

import java.util.*;

public class EntityHandlerFactory
{
  private static Hashtable _handlerHt=new Hashtable();

  public static AbstractEntityHandler getHandlerFor(
    String entityName, boolean isLocal)
  {
    AbstractEntityHandler handler = (AbstractEntityHandler)_handlerHt.get(
                                     entityName+isLocal);

    if(handler==null)
    {
      handler = new DefaultEntityHandler(entityName,isLocal);
      putEntityHandler(entityName, isLocal, handler);
    }

    return handler;
  }

  public static boolean hasEntityHandlerFor(String entityName, boolean isLocal)
  {
    return _handlerHt.containsKey(entityName+isLocal);
  }

  public static void putEntityHandler(
    String entityName, boolean isLocal, AbstractEntityHandler handler)
  {
    _handlerHt.put(entityName+isLocal, handler);
  }

}