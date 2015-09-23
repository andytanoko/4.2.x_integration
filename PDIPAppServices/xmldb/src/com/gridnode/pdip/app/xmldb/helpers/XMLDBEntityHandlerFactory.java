/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLDBEntityHandlerFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 18 2002    
 */
package com.gridnode.pdip.app.xmldb.helpers;

import com.gridnode.pdip.framework.db.DefaultEntityHandler;
import com.gridnode.pdip.framework.db.AbstractEntityHandler;

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

public class XMLDBEntityHandlerFactory //extends DefaultEntityHandler
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