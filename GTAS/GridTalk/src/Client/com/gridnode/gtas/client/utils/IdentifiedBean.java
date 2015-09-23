/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IdentifiedBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-22     Andrew Hill         Created
 * 2002-10-24     Andrew Hill         Moved to utils package
 */
package com.gridnode.gtas.client.utils;


public class IdentifiedBean extends FreezeableConfigObject
{
  public static final String IDENTIFIED_BEAN_CLASS = "com.gridnode.gtas.client.utils.IdentifiedBean";

  private String _id;

  public void setId(String id)
  {
    assertNotFrozen();
    _id = id;
  }

  public String getId()
  { return _id; }
}