/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-12     Andrew Hill         Created
 * 2002-12-18     Andrew Hill         Add userTab
 */
package com.gridnode.gtas.client.web.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;
 
public class UserAForm extends GTActionFormBase
{
  // UI fields
  private String _userTab;

  // User entity fields
  private String _userID;
  private String _userName;
  private String _password;
  private String _newPassword;
  private String _phone;
  private String _email;
  private String _property;

  // User profile fields
  private String _state;
  private String _lastLoginTime;
  private String _lastLogoutTime;
  private String _frozen;
  private String _freezeTime;
  private String _badLoginAttempts;
  private String _created;
  private String _createdBy;

  // Roles (uids)
  private String[] _roles;
  private String[] _bes;


  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _roles = null;
    _bes = null;
  }

  public void doNetscape6Reset(ActionMapping mapping, HttpServletRequest request)
  { //20021218AH
    String tab = request.getParameter("userTabOld");
    if("details_tab".equals(tab))
    {
      _roles = null;
      _bes = null;
    }
    else if("state_tab".equals(tab))
    {
      
    }
  }

  public void setUserTab(String tab)
  { //20021218AH
    _userTab = tab;
  }

  public String getUserTab()
  { //20021218AH
    return _userTab;
  }

  public String[] getBes()
  {
    return _bes;
  }

  public void setBes(String[] bes)
  {
    _bes = bes;
  }

  public void setRoles(String[] roles)
  {
    _roles = roles;
  }

  public String[] getRoles()
  {
    return _roles;
  }

  //Setters (must take String)
  //Setters for User:

  public void setUserId(String value)
  {
    _userID = value;
  }

  public void setUserName(String value)
  {
    _userName = value;
  }

  public void setPassword(String value)
  {
    _password = value;
  }

  public void setNewPassword(String value)
  {
    _newPassword = value;
  }

  public void setPhone(String value)
  {
    _phone = value;
  }

  public void setEmail(String value)
  {
    _email = value;
  }

  public void setProperty(String value)
  {
    _property = value;
  }

  // Setters for profile:

  public void setState(String value)
  {
    _state = value;
  }

  public void setLastLoginTime(String value)
  {
    _lastLoginTime = value;
  }

  public void setLastLogoutTime(String value)
  {
    _lastLogoutTime = value;
  }

  public void setFrozen(String value)
  {
    _frozen = value;
  }

  public void setFreezeTime(String value)
  {
    _freezeTime = value;
  }

  public void setLoginAttempts(String value)
  {
    _badLoginAttempts = value;
  }

  public void setCreated(String value)
  {
    _created = value;
  }

  public void setCreatedBy(String value)
  {
    _createdBy = value;
  }

  // Getters (String versions)
  // Getters for User:

  public String getUserId()
  {
    return _userID;
  }

  public String getUserName()
  {
    return _userName;
  }

  public String getPassword()
  {
    return _password;
  }

  public String getNewPassword()
  {
    return _newPassword;
  }

  public String getPhone()
  {
    return _phone;
  }

  public String getEmail()
  {
    return _email;
  }

  public String getProperty()
  {
    return _property;
  }

  //Getters for profile:

  public String getState()
  {
    return _state;
  }

  public String getLastLoginTime()
  {
    return _lastLoginTime;
  }

  public String getLastLogoutTime()
  {
    return _lastLogoutTime;
  }

  public String getFrozen()
  {
    return _frozen;
  }

  public String getFreezeTime()
  {
    return _freezeTime;
  }

  public String getLoginAttempts()
  {
    return _badLoginAttempts;
  }

  public String getCreated()
  {
    return _created;
  }

  public String getCreatedBy()
  {
    return _createdBy;
  }

//  /**
//   * We are using the actionform as a convienient place to store the entity for use by the renderer
//   * in obtaining the field meta info.
//   * We keep this method package protected as any public method can be called by struts in response
//   * to a query string.
//   */
//  void setGTUser(IGTUserEntity user)
//  {
//    _user = user;
//  }
//
//  IGTUserEntity getGTUser()
//  {
//    return _user;
//  }

}