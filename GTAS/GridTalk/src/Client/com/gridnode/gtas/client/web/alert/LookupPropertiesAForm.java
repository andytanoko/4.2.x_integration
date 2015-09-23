/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LookupPropertiesAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 30 Dec 2005		SC									Created
 */
package com.gridnode.gtas.client.web.alert;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class LookupPropertiesAForm extends GTActionFormBase
{
	private boolean selected;
	private String lp_name;
	private String lp_value;
	
	/* unchecked the html checkbox */
	public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    selected = false;
  }
	
	/* getter/setter methods */
	
	public boolean isSelected()
	{
		return selected;
	}
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public String getLp_name()
	{
		return lp_name;
	}

	public void setLp_name(String lp_name)
	{
		this.lp_name = lp_name;
	}

	public String getLp_value()
	{
		return lp_value;
	}

	public void setLp_value(String lp_value)
	{
		this.lp_value = lp_value;
	}
	
}
