/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IParamDef.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 31 2002    Jagadeesh              Created
 * Feb  10 2003    Jagadeesh              Added: The extracted value
 *                                        - to be stored in actualValue.
 * Feb 14 2003    Neo Sok Lay             Source field should be integer instead
 *                                        of string.
 */



package com.gridnode.pdip.base.userprocedure.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class ParamDef extends AbstractEntity implements IParamDef,IDataType
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8264555299674906348L;
	private String _name=null;
  private String _description = null;
  private Integer _source = null;
  private String _dateFormat = null;
  private int _type;
  private Object _value = null;
  private Object _actualValue = null;


  public ParamDef()
  {
  }

  public String getName()
  {
    return _name;
  }

  public String getDescription()
  {
    return _description;
  }

  public Integer getSource()
  {
    return _source;
  }

  public int getType()
  {
    return _type;
  }

  public Object getValue()
  {
    return _value;
  }

  public String getDateFormat()
  {
    return _dateFormat;
  }

  public void setName(String name)
  {
    _name = name;
  }

  public void setDescription(String description)
  {
    _description = description;
  }

  public void setSource(Integer source)
  {
    _source = source;
  }

  public void setType(int type)
  {
    _type = type;
  }

  public void setValue(Object value)
  {
    _value = value;
  }

  public void setDateFormat(String dateFormat)
  {
    _dateFormat = dateFormat;
  }

  public String toString()
  {
     return   getName()+"/"+getDescription()+"/"+getSource()+
     "/"+getType()+"/"+getValue();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
     return   getName()+"/"+getDescription()+"/"+getSource()+
     "/"+getType()+"/"+getValue();
  }

  public Number getKeyId()
  {
    return null;
  }

  public void setActualValue(Object actualValue)
  {
    _actualValue = actualValue;
  }

  public Object getActualValue()
  {
    return _actualValue;
  }


}