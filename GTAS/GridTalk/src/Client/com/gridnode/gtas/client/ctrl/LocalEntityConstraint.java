/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LocalEntityConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.Properties;

class LocalEntityConstraint extends AbstractEntityConstraint implements ILocalEntityConstraint
{
  LocalEntityConstraint(Properties detail)
    throws GTClientException
  {
    super(IConstraint.TYPE_LOCAL_ENTITY, detail);
  }

  protected void initialiseEntityConstraint(int type, Properties detail)
  {
    _entityType = detail.getProperty("embedded.type",null);
    if(_entityType == null) throw new java.lang.IllegalArgumentException("Embedded entity type not specified");
  }

  public String getEntityType()
  {
    return _entityType;
  }
}