/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractManagerFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-12     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.Hashtable;

abstract class AbstractManagerFactory implements IGTManagerFactory
{
  AbstractManagerFactory()
  {
  }

  protected Hashtable _managers = new Hashtable();

  public synchronized IGTManager getManager(int type) throws GTClientException
  {
    Integer manType = new Integer(type);
    IGTManager manager = (IGTManager)_managers.get(manType);
    if(manager == null)
    {
      manager = createManager(type);
      _managers.put(manType, manager);
    }
    return manager;
  }

  protected abstract IGTManager createManager(int type) throws GTClientException;

  public abstract int getManagerType(String entityType) throws GTClientException;
}