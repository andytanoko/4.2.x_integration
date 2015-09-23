/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ValueFilterAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-17     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.query;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
 
public class ValueFilterAForm extends GTActionFormBase
{
  private String _connector = QueryUtils.CONNECTOR_AND;
  private String _field;
  private String _operator;
  private String[] _params = new String[2];
  private String _negate;
  private boolean _selected;
  
  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _selected = false;
  }
  
  public String getConnector()
  {
    return _connector;
  }

  public String getField()
  {
    return _field;
  }

  public String getNegate()
  {
    return _negate;
  }

  public String getOperator()
  {
    return _operator;
  }

  public String[] getParams()
  {
    return _params;
  }

  public void setConnector(String connector)
  {
    _connector = connector;
  }

  public void setField(String field)
  {
    _field = field;
  }

  public void setNegate(String negate)
  {
    _negate = negate;
  }

  public void setOperator(String operator)
  {
    _operator = operator;
  }

  public void setParams(String[] params)
  {
    _params = params;
  }

  public boolean isSelected()
  {
    return _selected;
  }

  public void setSelected(boolean selected)
  {
    _selected = selected;
  }

}