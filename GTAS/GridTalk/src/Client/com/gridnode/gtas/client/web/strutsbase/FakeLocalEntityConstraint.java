/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FakeForeignEntityConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-10-09     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.util.Properties;

import com.gridnode.gtas.client.ctrl.IConstraint;
import com.gridnode.gtas.client.ctrl.ILocalEntityConstraint;
 
public class FakeLocalEntityConstraint implements ILocalEntityConstraint
{
  private String _entityType;

  public FakeLocalEntityConstraint(String entityType)
  {
    _entityType = entityType;
  }

  public String getEntityType()
  {
    return _entityType;
  }

  public int getType()
  {
    return IConstraint.TYPE_LOCAL_ENTITY;
  }

  public Properties getProperties()
  {
    throw new java.lang.UnsupportedOperationException("Fake constraint has no properties");
  }
}