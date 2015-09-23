/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 * 2002-09-02     Andrew Hill         No longer hold ref to properties object once initialised
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.Properties;

abstract class AbstractConstraint implements IConstraint
{
  protected int _constraintType;

  AbstractConstraint()
  {
  }

  AbstractConstraint(int constraintType, Properties detail)
    throws GTClientException
  {
    init(constraintType,detail);
  }

  protected void init(int constraintType, Properties detail)
    throws GTClientException
  {
    _constraintType = constraintType;
    if(detail != null)
    {
      initialise(constraintType, detail);
    }
  }

  protected abstract void initialise(int type, Properties detail)
    throws GTClientException;

  public int getType()
  {
    return _constraintType;
  }
}