/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EnumeratedConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 * 2002-09-02     Andrew Hill         Hold ref to details here in subclass
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import java.util.*;

class EnumeratedConstraint extends AbstractConstraint implements IEnumeratedConstraint
{
  protected int _size;
  protected String[] _labels;
  protected String[] _values;
  protected Properties _detail;

  EnumeratedConstraint()
    throws GTClientException
  {
    super(IConstraint.TYPE_ENUMERATED, null);
  }

  EnumeratedConstraint(Properties detail)
    throws GTClientException
  {
    super(IConstraint.TYPE_ENUMERATED, detail);
  }

  protected void initialise(int type, Properties detail)
  {
    detail = (Properties)detail.clone();
    // Remove from detail all properties that arent part of the enumeration
    detail.remove("type");
    detail.remove("collection");
    detail.remove("collection.element");
    detail.remove("isAvailableInCache");
    _size = detail.size();
    _labels = new String[_size];
    _values = new String[_size];

    int i = 0;
    Enumeration e = detail.propertyNames();
    while(e.hasMoreElements())
    {
      String name = (String)e.nextElement();
      _labels[i] = name;
      String value = detail.getProperty(name,"");
      _values[i] = value;
      i++;
    }
    _detail = detail;
  }

  public int getSize()
  {
    return _size;
  }

  public String getLabel(int i)
  {
    return _labels[i];
  }

  public String getValue(int i)
  {
    return _values[i];
  }

  public String getLabel(String value)
  {
    // Dont envisage this being called more than once for a particular field rendering
    // so little point doing funny business
    for(int i=0; i < _values.length; i++)
    {
      if(_values[i].equals(value)) return _labels[i];
    }
    return "";
  }

  public String getValue(String label)
  {
    return _detail.getProperty(label,"");
  }
  
  public boolean getRequiresI18n()
  {
    return true;  // TODO: allow for configuring this, currently no need for it
  }
}