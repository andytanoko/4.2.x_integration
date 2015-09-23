/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReturnDefAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 * 2002-11-12     Daniel D'Cotta      Change action to actionType due to a
 *                                    JavaScript bug with the action keyword
 * 2003-06-13     Daniel D'Cotta      Added value1 & value2 when operator is 'between'
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;


public class ReturnDefAForm extends GTActionFormBase
{
  private String _operator;
  private String _value;
  private String _actionType;
  private String _alert;

  public String getOperator()
  { return _operator; }

  public Integer getOperatorInteger()
  { return StaticUtils.integerValue(_operator); }

  public void setOperator(String operator)
  { _operator=operator; }

  public String getValue()
  { return _value; }

  public void setValue(String value)
  { _value=value; }

  public String getActionType()
  { return _actionType; }

  public Integer getActionTypeInteger()
  { return StaticUtils.integerValue(_actionType); }

  public void setActionType(String actionType)
  { _actionType=actionType; }

  public String getAlert()
  { return _alert; }

  public void setAlert(String alert)
  { _alert=alert; }

  // 20030613 DDJ: Display value1 & value2 when operator is 'between'
  // However, still store it into 1 field for B-Tier
  private final static char VALUE_SEPARATOR = '-';
  private String _value1;
  private String _value2;

  public String getValue1()
  {
    initValues();
    return _value1;
  }

  public void setValue1(String value1)
  {
    _value1=value1;
    storeValues();
  }

  public String getValue2()
  {
    initValues();
    return _value2;
  }

  public void setValue2(String value2)
  {
    _value2=value2;
    storeValues();
  }

  private void initValues()
  {
    int pos = _value.indexOf(VALUE_SEPARATOR);
    if(pos != -1)
    {
      _value1 = _value.substring(0, pos);
      _value2 = _value.substring(pos + 1);
    }
    else
    {
      _value1 = _value;
      _value2 = "";
    }
  }

  private void storeValues()
  {
    if(StaticUtils.stringNotEmpty(_value1) ||
       StaticUtils.stringNotEmpty(_value2))
    {
      _value = _value1 + VALUE_SEPARATOR + _value2;
    }
    else
      _value = "";
  }
}