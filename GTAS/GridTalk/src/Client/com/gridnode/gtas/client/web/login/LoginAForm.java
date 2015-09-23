/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LoginAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-04-20     Andrew Hill         Created
 * 2003-01-10     Andrew Hill         timeZone selection field
 */
package com.gridnode.gtas.client.web.login;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
 

public class LoginAForm extends GTActionFormBase
{
  private String _username;
  private String _password;
  private String _timeZone;   //20030110AH
  private String _gmtOffset;  //20030113AH

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _password = null;
  }

  public void setUsername(String value)
  {
    _username = value;
  }

  public String getUsername()
  {
    return _username;
  }

  public void setPassword(String value)
  {
    _password = value;
  }

  public String getPassword()
  {
    return _password;
  }

  public String getTimeZone()
  { //20030110AH
    return _timeZone;
  }

  public void setTimeZone(String timeZone)
  { //20030110AH
    _timeZone = timeZone;
  }

  public void setGmtOffset(String gmtOffset)
  { //20030113AH
    _gmtOffset = gmtOffset;
  }

  public String getGmtOffset()
  { //20030113AH
    return _gmtOffset;
  }
}