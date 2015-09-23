/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SourceMap.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    Jared Low           Created.
 * May 24 2002    Jared Low           Implements Serializable.
 */
package com.gridnode.pdip.app.gridform.model;

import java.io.Serializable;

/**
 * Bean that represents a single map from field name to a XML data source.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class SourceMap implements Serializable
{
  private String _name;
  private String _type;
  private String _xpath;

  public SourceMap()
  {
  }

  public void setName(String name)
  {
    _name = name;
  }

  public void setType(String type)
  {
    _type = type;
  }

  public void setXpath(String xpath)
  {
    _xpath = xpath;
  }

  public String getName()
  {
    return _name;
  }

  public String getType()
  {
    return _type;
  }

  public String getXpath()
  {
    return _xpath;
  }
}