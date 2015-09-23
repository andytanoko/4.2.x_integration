/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConditionAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-10-27     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.document;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class ConditionAForm extends GTActionFormBase
{
  private boolean _selected;
  private String _type;
  private String _field;
  private String _xpath;
  private String _operator;
  private String _values;
  
  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _selected = false;
  }
  
  public boolean isSelected()
  {
    return _selected;
  }

  public void setSelected(boolean selected)
  {
    _selected = selected;
  }

  public String getType()
  {
    return _type;
  }

  public Short getTypeAsShort()
  {
    return StaticUtils.shortValue(_type);
  }

  public void setType(String type)
  {
    _type = type;
  }

  public String getField()
  {
    return _field;
  }

  public Integer getFieldAsInteger()
  {
    return StaticUtils.integerValue(_field);
  }

  public void setField(String field)
  {
    _field = field;
  }

  public String getXpath()
  {
    return _xpath;
  }

  public void setXpath(String xpath)
  {
    _xpath = xpath;
  }

  public String getOperator()
  {
    return _operator;
  }

  public void setOperator(String operator)
  {
    _operator = operator;
  }

  public String getValues()
  {
    return _values;
  }

  public Collection getValuesAsCollection()
  {
    return StaticUtils.explodeCollection(_values, ",;"); // Use either ',' or ';' as a delimiter
  }

  public void setValues(String values)
  {
    _values = values;
  }
}