/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCertPwAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-04-23     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.login;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
 

public class GetCertPwAForm extends GTActionFormBase
{
  private String _securityPassword;  

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _securityPassword = null;
  }

	public String getSecurityPassword()
	{
		return _securityPassword;
	}

	public void setSecurityPassword(String securityPassword)
	{
		_securityPassword = securityPassword;
	}

}