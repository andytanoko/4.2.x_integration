/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTUserManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-21     Andrew Hill         Created
 * 2002-08-02     Andrew Hill         Moved up to main ctrl package
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.ctrl.IUserAdminOptions;

/**
 * Default implementation of IUserAdminOptiosn for use with the specialised update() method
 * of IGTUserManager.
 */
public class UserAdminOptions implements IUserAdminOptions
{
  private boolean _isUnfreezeAccount = false;
  private boolean _isResetBadLoginAttempts = false;
  private String  _newPassword = null;
  private short   _state = STATE_ENABLED;

  public UserAdminOptions()
  {
  }

  public UserAdminOptions(boolean isUnfreezeAccount,
                          boolean isResetBadLoginAttempts,
                          short state,
                          String password)
  {
    setUnfreezeAccount(isUnfreezeAccount);
    setResetBadLoginAttempts(isResetBadLoginAttempts);
    setState(state);
    setNewPassword(password);
  }


  public void setUnfreezeAccount(boolean flag)
  {
    _isUnfreezeAccount =  flag;
  }

  public void setResetBadLoginAttempts(boolean flag)
  {
    _isResetBadLoginAttempts = flag;
  }

  public void setNewPassword(String value)
  {
    _newPassword = value;
  }

  public void setState(short state)
  {
    switch(state)
    {
      case STATE_DISABLED:
      case STATE_DELETED:
      case STATE_ENABLED:
        _state = state;
        break;
      default:
        throw new IllegalArgumentException("Invalid state parameter");
    }
  }

  public boolean isUnfreezeAccount()
  {
    return _isUnfreezeAccount;
  }

  public String getNewPassword()
  {
    return _newPassword;
  }

  public short getState()
  {
    return _state;
  }

  public boolean isResetBadLoginAttempts()
  {
    return _isResetBadLoginAttempts;
  }

  public String toString()
  {
    StringBuffer b = new StringBuffer("UserAdminOptions:");
    if(isUnfreezeAccount()) b.append(" unfreeze");
    if(isResetBadLoginAttempts()) b.append(" reset");
    b.append(" state=" + getState());
    if(getNewPassword() != null) b.append(" password=" + getNewPassword());
    return b.toString();
  }

}