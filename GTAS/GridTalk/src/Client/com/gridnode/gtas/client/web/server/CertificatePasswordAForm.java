/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificatePasswordAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-04-14     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.server;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
 
public class CertificatePasswordAForm extends GTActionFormBase
{
  private String _oldPassword;
  private String _securityPassword;
  private String _confirmPassword;

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _oldPassword = null;
    _securityPassword = null;
    _confirmPassword = null;
  }

  public void setOldPassword(String oldPassword)
  { _oldPassword = oldPassword; }

  public String getOldPassword()
  { return _oldPassword; }

  public void setConfirmPassword(String confirmPassword)
  { _confirmPassword = confirmPassword; }

  public String getConfirmPassword()
  { return _confirmPassword; }

  public void setSecurityPassword(String securityPassword)
  { _securityPassword = securityPassword; }

  public String getSecurityPassword()
  { return _securityPassword; }
}