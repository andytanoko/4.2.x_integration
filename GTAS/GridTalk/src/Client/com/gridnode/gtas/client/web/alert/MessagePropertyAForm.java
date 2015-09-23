/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MessagePropertiesAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 5 Jan 06				SC									Created
 */
package com.gridnode.gtas.client.web.alert;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class MessagePropertyAForm extends GTActionFormBase
{
	private boolean selected;
	private String key;
	private String type;
	private String value;
	
	public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    selected = false;
  }
  
  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected(boolean selected)
  {
  	this.selected = selected;
  }

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
  
  
}
