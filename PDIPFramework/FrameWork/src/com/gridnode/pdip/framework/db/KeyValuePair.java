/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: KayValuePair.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 26 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db;
import java.io.Serializable;

/**
 * This class can be used when there is a need to keep a key-value pair
 * of objects together. This enables the key-value pairs to be accessible
 * through a Collection instead of Maps.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class KeyValuePair
  implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4394842872328111730L;
	private Object _key;
  private Object _value;

  public KeyValuePair()
  {
  }

  public KeyValuePair(Object key, Object value)
  {
    setKey(key);
    setValue(value);
  }

  public void setKey(Object key)
  {
    _key = key;
  }

  public Object getKey()
  {
    return _key;
  }

  public void setValue(Object value)
  {
    _value = value;
  }

  public Object getValue()
  {
    return _value;
  }

  public String toString()
  {
    return "Key: "+_key + " Value: "+_value;
  }
}