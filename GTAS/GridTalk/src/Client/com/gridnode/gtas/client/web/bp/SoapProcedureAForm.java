/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SoapProcedureAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-30     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class SoapProcedureAForm extends GTActionFormBase
{
  private String _methodName;
  private String _userName;
  private String _password;
  private String _confirmPassword;

  public String getMethodName()
  { return _methodName; }

  public void setMethodName(String methodName)
  { _methodName=methodName; }

  public String getUserName()
  {
    return _userName;
  }

  public void setUserName(String string)
  {
    _userName = string;
  }

  public String getPassword()
  {
    return _password;
  }

  public void setPassword(String string)
  {
    _password = string;
  }

  public String getConfirmPassword()
  {
    return _confirmPassword;
  }

  public void setConfirmPassword(String string)
  {
    _confirmPassword = string;
  }
}