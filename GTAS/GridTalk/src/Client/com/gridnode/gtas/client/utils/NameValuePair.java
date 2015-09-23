/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NameValuePair.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-27     Andrew Hill         Created
 */
package com.gridnode.gtas.client.utils;


public class NameValuePair extends FreezeableConfigObject
{
  public static final String NAME_VALUE_PAIR_CLASS = "com.gridnode.gtas.client.utils.NameValuePair";

  private String _name;
  private String _value;

  public NameValuePair()
  {
  }

  public NameValuePair(String name, String value)
  {
    _name = name;
    _value = value;
  }

  public String toString()
  {
    return "NameValuePair[" + _name + ", " + _value + "]";
  }

  public void setName(String name)
  {
    assertNotFrozen();
    _name = name;
  }

  public String getName()
  {
    return _name;
  }

  public void setValue(String value)
  {
    assertNotFrozen();
    _value = value;
  }

  public String getValue()
  {
    return _value;
  }
}