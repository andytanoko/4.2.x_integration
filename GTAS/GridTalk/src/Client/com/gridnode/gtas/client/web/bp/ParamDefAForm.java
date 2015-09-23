/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ParamDefAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;


public class ParamDefAForm extends GTActionFormBase
{
  private String _name;
  private String _description;
  private String _type;
  private String _dateFormat;
  private String _source;
  private String _value;

  public String getName()
  { return _name; }

  public void setName(String name)
  { _name=name; }

  public String getDescription()
  { return _description; }

  public void setDescription(String description)
  { _description=description; }

  public String getType()
  { return _type; }

  public Integer getTypeInteger()
  { return StaticUtils.integerValue(_type); }

  public void setType(String type)
  { _type=type; }

  public String getDateFormat()
  { return _dateFormat; }

  public void setDateFormat(String dateFormat)
  { _dateFormat=dateFormat; }

  public String getSource()
  { return _source; }

  public Integer getSourceInteger()
  { return StaticUtils.integerValue(_source); }

  public void setSource(String source)
  { _source=source; }

  public String getValue()
  { return _value; }

  public void setValue(String value)
  { _value=value; }
}