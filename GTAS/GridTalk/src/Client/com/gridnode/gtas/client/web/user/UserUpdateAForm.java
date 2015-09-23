/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserUpdateAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2005-03-16     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.user;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

/**
 * Form for the user details update page, where users can maintain their own
 * details and password. 
 */
public class UserUpdateAForm extends GTActionFormBase
{
  // UI fields
  private String _userTab;

  // User entity fields
  private String _userId;
  private String _userName;
  private String _password;
  private String _newPassword;
  private String _confirmPassword;
  private String _phone;
  private String _email;

  public String getEmail()
  {
    return _email;
  }

  public String getNewPassword()
  {
    return _newPassword;
  }

  public String getPassword()
  {
    return _password;
  }

  public String getPhone()
  {
    return _phone;
  }

  public String getUserId()
  {
    return _userId;
  }

  public String getUserName()
  {
    return _userName;
  }

  public String getUserTab()
  {
    return _userTab;
  }

  public void setEmail(String string)
  {
    _email = string;
  }

  public void setNewPassword(String string)
  {
    _newPassword = string;
  }

  public void setPassword(String string)
  {
    _password = string;
  }

  public void setPhone(String string)
  {
    _phone = string;
  }

  public void setUserId(String string)
  {
    _userId = string;
  }

  public void setUserName(String string)
  {
    _userName = string;
  }

  public void setUserTab(String string)
  {
    _userTab = string;
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