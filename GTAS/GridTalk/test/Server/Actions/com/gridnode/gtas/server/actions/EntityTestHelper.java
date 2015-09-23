/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityTestHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Ang Meng Hua        Created
 */
package com.gridnode.gtas.server.actions;

import com.gridnode.pdip.framework.db.entity.IEntity;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This defines the interface for testing the GridTalk Action class.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.2
 */
public abstract class EntityTestHelper implements IEntityTestHelper
{
  private Map _helper = null;

  public abstract IEntity create(IEntity entity) throws Exception;

  public abstract void delete(Long uID) throws Exception;

  public void deleteAll() throws Exception
  {
    Collection collection = getAll();
    if ((collection != null) && (!collection.isEmpty()))
    {
      for (Iterator i=collection.iterator(); i.hasNext();)
        delete((Long)((IEntity)i.next()).getKey());
    }

    for (Iterator i=_helper.values().iterator(); i.hasNext();)
      ((IEntityTestHelper)i.next()).deleteAll();
  }

  public abstract IEntity get(Long uID) throws Exception;

  public abstract Collection getAll() throws Exception;

  protected void addHelper(String helperName, IEntityTestHelper helper)
  {
    if (_helper == null)
      _helper = new HashMap();

    _helper.put(helperName, helper);
  }

  protected IEntityTestHelper getHelper(String helperName)
  {
    return (IEntityTestHelper)_helper.get(helperName);
  }
}