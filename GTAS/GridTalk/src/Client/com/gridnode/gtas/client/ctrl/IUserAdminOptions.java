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
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.user.IUserAccountState;

public interface IUserAdminOptions
{
  public static final short STATE_DISABLED = IUserAccountState.STATE_DISABLED;
  public static final short STATE_ENABLED  = IUserAccountState.STATE_ENABLED;
  public static final short STATE_DELETED  = IUserAccountState.STATE_DELETED;

  public boolean isUnfreezeAccount();
  public String  getNewPassword();
  public short   getState();
  public boolean isResetBadLoginAttempts();
}