/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SimpleEntityReference.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-29     Andrew Hill         Created 
 */
package com.gridnode.gtas.client.ctrl;

/*
 * Simple implementation of a mutable IGTEntityReference suitable for general purpose use
 * by both the ctlr package and its users.
 * Note that the getters and setters of this class are not threadsafe.
 */
public class SimpleEntityReference implements IGTEntityReference
{
  private String _type;
  private Number _keyFieldId;
  private Object _keyValue;
  private String _display;
  
  public SimpleEntityReference()
  {
  }
  
  public SimpleEntityReference(String entityType, Number keyFieldId, Object keyValue, String display)
  {
    this(entityType, keyFieldId, keyValue);
    setDisplay(display);
  }
  
  public SimpleEntityReference(String entityType, Number keyFieldId, Object keyValue)
  {
    setType(entityType);
    setKeyFieldId(keyFieldId);
    setKeyValue(keyValue);
  }
  
  public String toString()
  {
    if(getDisplay() == null)
    {
      return ("EntityReference[" + getType() + ":" + getKeyFieldId() + "==" + getKeyValue() + "]");
    }
    else
    {
      return getDisplay();
    }
  }
  
  /*
   * Returns true if the necessary values have been initialised.
   * Note that it merely checks for the existence of values, but not their validity within the
   * appropriate domains and constraints.
   * @return isInitialised
   */
  public boolean isInitialised()
  {
    return !(     getKeyFieldId() == null
              ||  getKeyValue() == null
              ||  getType() == null );
  }
  
  public Number getKeyFieldId()
  {
    return _keyFieldId;
  }

  public Object getKeyValue()
  {
    return _keyValue;
  }

  public String getType()
  {
    return _type;
  }

  public void setKeyFieldId(Number keyFieldId)
  {
    _keyFieldId = keyFieldId;
  }

  public void setKeyValue(Object keyValue)
  {
    _keyValue = keyValue;
  }

  public void setType(String type)
  {
    _type = type;
  }

  public String getDisplay()
  {
    return _display;
  }

  public void setDisplay(String display)
  {
    _display = display;
  }

}
