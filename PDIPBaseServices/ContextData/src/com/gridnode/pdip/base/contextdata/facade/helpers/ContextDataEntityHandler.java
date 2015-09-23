/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ContextDataEntityHandler.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Apr 22, 2004      Mahesh              Created
 * Aug 24, 2006     Neo Sok Lay         GNDB00027716: Synchronize all methods to access the context data.
 */
package com.gridnode.pdip.base.contextdata.facade.helpers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.gridnode.pdip.base.contextdata.entities.ejb.IContextDataObj;
import com.gridnode.pdip.base.contextdata.entities.model.ContextData;
import com.gridnode.pdip.base.contextdata.entities.model.ContextKey;
import com.gridnode.pdip.base.contextdata.facade.exceptions.DataException;
import com.gridnode.pdip.framework.db.DefaultEntityHandler;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

public class ContextDataEntityHandler extends DefaultEntityHandler
{
  private static final String ENTITY_NAME = ContextData.ENTITY_NAME;
  private static final Object lock = new Object();

  public ContextDataEntityHandler()
  {
    super(ENTITY_NAME, false);
  }

  public static ContextDataEntityHandler getInstance()
  {
    if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
    {
      synchronized (lock)
      {
        // Check again so that it will not create another instance unnecessary.
        if (!EntityHandlerFactory.hasEntityHandlerFor(ENTITY_NAME, true))
          EntityHandlerFactory.putEntityHandler(ENTITY_NAME, true, new ContextDataEntityHandler());
      }
    }
    return getFromEntityHandlerFactory();
  }

  private static ContextDataEntityHandler getFromEntityHandlerFactory()
  {
    return (ContextDataEntityHandler) EntityHandlerFactory.getHandlerFor(ENTITY_NAME, true);
  }

  public synchronized void updateContextData(Long contextUId, ContextKey key, Object objData) throws Throwable
  {
    if (key == null)
      throw new DataException(
        "dataKey cannot be null,contextUId=" + contextUId);
    IContextDataObj ctxDataObj = findByContextUId(contextUId);
    ContextData ctxData = (ContextData) ctxDataObj.getData();
    if (ctxData == null)
      throw new DataException(
        "Context does not exist contextUId=" + contextUId);
    ctxData.getContextData().put(key, objData);
    ctxDataObj.setData(ctxData);
  }

  public synchronized void updateContextData(Long contextUId, HashMap contexDataMap) throws Throwable
  {
    IContextDataObj ctxDataObj = findByContextUId(contextUId);
    ContextData ctxData = (ContextData) ctxDataObj.getData();
    for (Iterator iterator = contexDataMap.keySet().iterator(); iterator.hasNext();)
    {
      Object key = iterator.next();
      if (key == null)
        throw new DataException(
          "[ContextDataEntityHandler.updateContextData(Long contextUId,HashMap contexDataMap)] dataKey cannot be null ,contextUId=" + contextUId);
      if (!(key instanceof ContextKey))
        key = new ContextKey(key.toString());
      ctxData.getContextData().put(key, contexDataMap.get(key));
    }
    ctxDataObj.setData(ctxData);
  }

  public synchronized void clearContextData(Long contextUId, ContextKey key) throws Throwable
  {
    IContextDataObj ctxDataObj = findByContextUId(contextUId);
    ContextData ctxData = (ContextData) ctxDataObj.getData();
    ctxData.getContextData().remove(key);
    ctxDataObj.setData(ctxData);
  }

  public synchronized void clearContextData(Long contextUId, Collection contextKeyColl) throws Throwable
  {
    IContextDataObj ctxDataObj = findByContextUId(contextUId);
    ContextData ctxData = (ContextData) ctxDataObj.getData();
    for (Iterator iterator = contextKeyColl.iterator(); iterator.hasNext();)
    {
      ctxData.getContextData().remove(iterator.next());
    }
    ctxDataObj.setData(ctxData);
  }

  public synchronized void clearContextData(Long contextUId) throws Throwable
  {
    IContextDataObj ctxDataObj = findByContextUId(contextUId);
    ContextData ctxData = (ContextData) ctxDataObj.getData();
    ctxData.getContextData().clear();
    ctxDataObj.setData(ctxData);
  }

  public synchronized void removeByContextUId(Long contextUId) throws Throwable
  {
    IContextDataObj ctxDataObj = findByContextUId(contextUId);
    ctxDataObj.remove();
  }

  public synchronized ContextData getEntityByContextUId(Long contextUId) throws Throwable
  {
    IContextDataObj ctxDataObj = findByContextUId(contextUId);
    ContextData ctxData = (ContextData) ctxDataObj.getData();
    return ctxData;
  }

  public synchronized IContextDataObj findByContextUId(Long contextUId) throws Throwable
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, ContextData.CONTEXT_UID, filter.getEqualOperator(), contextUId, false);
    Collection coll = findByFilter(filter);
    if (coll == null || coll.isEmpty())
      throw new DataException("No ContextData found with contextUId=" + contextUId);
    return (IContextDataObj) coll.iterator().next();
  }

}
