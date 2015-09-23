/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTUserEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-12     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.user.IUserAccount;

public interface IGTUserEntity extends IGTEntity
{
  public static final Number UID          = IUserAccount.UID;
  public static final Number USER_ID      = IUserAccount.ID;
  public static final Number USER_NAME    = IUserAccount.NAME;
  public static final Number PASSWORD     = IUserAccount.PASSWORD;
  public static final Number NEW_PASSWORD = IUserAccount.PASSWORD;
  public static final Number PHONE        = IUserAccount.PHONE;
  public static final Number EMAIL        = IUserAccount.EMAIL;
  public static final Number PROPERTY     = IUserAccount.PROPERTY;
  public static final Number PROFILE      = IUserAccount.ACCOUNT_STATE;

  public static final Number ROLES        = new Integer(-10);
  public static final Number BES          = new Integer(-20);
}