/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LayoutDefinition.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    Jared Low           Created.
 * May 24 2002    Jared Low           Implements Serializable.
 */
package com.gridnode.pdip.app.gridform.model;

import java.io.Serializable;
import java.util.List;

public class LayoutDefinition implements Serializable
{
  private String _rootElement;
  private List _mapping;

  public LayoutDefinition()
  {
  }

  public void setRootElement(String rootElement)
  {
    _rootElement = rootElement;
  }

  public void setMapping(List mapping)
  {
    _mapping = mapping;
  }

  public String getRootElement()
  {
    return _rootElement;
  }

  public List getMapping()
  {
    return _mapping;
  }
}