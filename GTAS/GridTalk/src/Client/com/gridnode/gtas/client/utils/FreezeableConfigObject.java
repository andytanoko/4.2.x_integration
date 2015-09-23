/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FreezableConfigObject.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-23     Andrew Hill         Created
 * 2002-10-24     Andrew Hill         Moved to utils package
 */
package com.gridnode.gtas.client.utils;


public class FreezeableConfigObject
{
  private boolean _frozen;

  public void freeze()
  {
    _frozen = true;
  }

  public boolean isFrozen()
  {
    return _frozen;
  }

  protected void assertNotFrozen()
  {
    if(_frozen) throw new java.lang.IllegalStateException("Object is frozen");
  }
}