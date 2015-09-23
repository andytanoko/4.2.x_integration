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
package com.gridnode.gtas.client.web.be;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class DomainIdentifierAForm extends GTActionFormBase
{
  private boolean _selected;
  private String _type;
  private String _value;
  
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

  public void setType(String type)
  {
    _type = type;
  }

  public String getValue()
  {
    return _value;
  }

  public void setValue(String value)
  {
    _value = value;
  }
}