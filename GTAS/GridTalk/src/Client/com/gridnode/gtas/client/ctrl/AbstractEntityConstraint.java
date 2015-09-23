/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractEntityConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.Properties;

abstract class AbstractEntityConstraint extends AbstractConstraint implements IEntityConstraint
{
  protected String _entityType;

  AbstractEntityConstraint(int constraintType, Properties detail)
    throws GTClientException
  {
    super(constraintType, detail);
  }

  protected final void initialise(int type, Properties detail)
    throws GTClientException
  {
    initialiseEntityConstraint(type,detail);
  }

  protected abstract void initialiseEntityConstraint(int type, Properties detail)
    throws GTClientException;
}