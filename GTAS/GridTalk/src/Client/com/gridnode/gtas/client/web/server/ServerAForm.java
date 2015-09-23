/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServerAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-03     Andrew Hill         Created
 * 2002-11-20     Andrew Hill         Added SecurityPassword
 */
package com.gridnode.gtas.client.web.server;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class ServerAForm extends GTActionFormBase
{
  private String _command;
  private String _securityPassword;

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _securityPassword = null;
  }

  public void setCommand(String command)
  { _command = command; }

  public String getCommand()
  { return _command; }

  public void setSecurityPassword(String securityPassword)
  { _securityPassword = securityPassword; }

  public String getSecurityPassword()
  { return _securityPassword; }
}