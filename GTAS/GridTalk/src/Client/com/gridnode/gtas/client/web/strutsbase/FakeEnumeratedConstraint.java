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
 * 2003-05-13     Andrew Hill         Created
 * 2003-09-15     Daniel D'Cotta      Added i18n support, currently only for 
 *                                    FakeEnumeratedConstraint
 */
package com.gridnode.gtas.client.web.strutsbase;

import com.gridnode.gtas.client.ctrl.IConstraint;
import com.gridnode.gtas.client.ctrl.IEnumeratedConstraint;

public class FakeEnumeratedConstraint implements IEnumeratedConstraint
{
  protected int _size;
  protected String[] _labels;
  protected String[] _values;
  protected boolean _requiresI18n = true;

  public FakeEnumeratedConstraint(String[] labels, String[] values)
  {
    if(labels.length != values.length)
    {
      throw new IllegalArgumentException("Number of labels and values does not match");
    }
    _labels = labels;
    _values = values;
    _size = labels.length;
  }

  // 20030915 DDJ
  public FakeEnumeratedConstraint(String[] labels, String[] values, boolean requiresI18n)
  {
    this(labels, values);
    _requiresI18n = requiresI18n;
  }

  public int getType()
  {
    return IConstraint.TYPE_ENUMERATED;
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
    for(int i=0; i < _values.length; i++)
    {
      if(_values[i].equals(value)) return _labels[i];
    }
    return "";
  }

  public String getValue(String label)
  { // Not very efficient...
    for(int i=0; i < _labels.length; i++)
    {
      if(_labels[i].equals(label)) return _values[i];
    }
    return "";
  }
  
  public boolean getRequiresI18n()
  {
    return _requiresI18n;
  }

}