/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 * 2003-07-16     Andrew Hill         gridDocField property
 */
package com.gridnode.gtas.client.web.bp;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;


public class UserProcedureAForm extends GTActionFormBase
{
  private String _name;
  private String _description;
  private String _procType;
  private String _procDefFile;
  private String _isSynchronous;
  private String _returnDataType;
  private String _defAction;
  private String _defAlert;
  private String _procParamListOrder;
  private String[] _procParamListOrderExploded;
  private String _procReturnListOrder;
  private String[] _procReturnListOrderExploded;
  private String _gridDocField; //20030716AH

  public String getName()
  { return _name; }

  public void setName(String name)
  { _name=name; }

  public String getDescription()
  { return _description; }

  public void setDescription(String description)
  { _description=description; }

  public String getProcType()
  { return _procType; }

  public Integer getProcTypeInteger()
  { return StaticUtils.integerValue(_procType); }

  public void setProcType(String procType)
  { _procType=procType; }

  public String getProcDefFile()
  { return _procDefFile; }

  public Long getProcDefFileLong()
  { return StaticUtils.longValue(_procDefFile); }

  public void setProcDefFile(String procDefFile)
  { _procDefFile=procDefFile; }

  public String getIsSynchronous()
  { return _isSynchronous; }

  public Boolean getIsSynchronousBoolean()
  { return StaticUtils.booleanValue(_isSynchronous); }

  public boolean getIsSynchronousPrimitiveBoolean()
  { return StaticUtils.primitiveBooleanValue(_isSynchronous); }

  public void setIsSynchronous(String isSynchronous)
  { _isSynchronous=isSynchronous; }

  public String getReturnDataType()
  { return _returnDataType; }

  public Integer getReturnDataTypeInteger()
  { return StaticUtils.integerValue(_returnDataType); }

  public void setReturnDataType(String returnDataType)
  { _returnDataType=returnDataType; }

  public String getDefAction()
  { return _defAction; }

  public Integer getDefActionInteger()
  { return StaticUtils.integerValue(_defAction); }

  public void setDefAction(String defAction)
  { _defAction=defAction; }

  public String getDefAlert()
  { return _defAlert; }

  public Long getDefAlertLong()
  { return StaticUtils.longValue(_defAlert); }

  public void setDefAlert(String defAlert)
  { _defAlert=defAlert; }

  public void setProcParamListOrder(String values)
  {
    _procParamListOrder = values;
    _procParamListOrderExploded = StaticUtils.explode(values,",");
  }

  public void setProcParamListOrderExploded(String[] values)
  {
    _procParamListOrderExploded = values;
    _procParamListOrder = StaticUtils.implode(values,",");
  }

  public void initProcParamListOrder(int size)
  {
    _procParamListOrderExploded = new String[size];
    for(int i=0; i < size; i++)
    {
      _procParamListOrderExploded[i] = "" + i;
    }
    _procParamListOrder = StaticUtils.implode(_procParamListOrderExploded,",");
  }

  public String[] getProcParamListOrderExploded()
  {
    return _procParamListOrderExploded;
  }

  public String getProcParamListOrder()
  { return _procParamListOrder; }

  public void setProcReturnListOrder(String values)
  {
    _procReturnListOrder = values;
    _procReturnListOrderExploded = StaticUtils.explode(values,",");
  }

  public void setProcReturnListOrderExploded(String[] values)
  {
    _procReturnListOrderExploded = values;
    _procReturnListOrder = StaticUtils.implode(values,",");
  }

  public void initProcReturnListOrder(int size)
  {
    _procReturnListOrderExploded = new String[size];
    for(int i=0; i < size; i++)
    {
      _procReturnListOrderExploded[i] = "" + i;
    }
    _procReturnListOrder = StaticUtils.implode(_procReturnListOrderExploded,",");
  }

  public String[] getProcReturnListOrderExploded()
  {
    return _procReturnListOrderExploded;
  }

  public String getProcReturnListOrder()
  { return _procReturnListOrder; }

  public void doReset(ActionMapping actionMapping, HttpServletRequest httpServletRequest)
  {
    _isSynchronous = null;
  }
  
  public String getGridDocField()
  { //20030716AH
    return _gridDocField;
  }

  public void setGridDocField(String string)
  { //20030716AH
    _gridDocField = string;
  }
  
  public Integer getGridDocFieldInteger()
  { //20030716AH
    return StaticUtils.integerValue(_gridDocField);
  }

}