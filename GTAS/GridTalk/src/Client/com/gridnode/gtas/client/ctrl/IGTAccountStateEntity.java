/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTUserProfileEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-15     Andrew Hill         Created
 * 2002-05-21     Neo Sok Lay         Change fields to Number.
 * 2002-06-22     Andrew Hill         Add some methods to enhance ui handling of state field values
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;

import com.gridnode.gtas.model.user.IUserAccountState;

public interface IGTAccountStateEntity extends IGTEntity
{
  public static final Number UID                = IUserAccountState.UID;
  public static final Number STATE              = IUserAccountState.STATE;
  public static final Number LAST_LOGIN_TIME    = IUserAccountState.LAST_LOGIN_TIME;
  public static final Number LAST_LOGOUT_TIME   = IUserAccountState.LAST_LOGOUT_TIME;
  public static final Number FROZEN             = IUserAccountState.IS_FREEZE;
  public static final Number FREEZE_TIME        = IUserAccountState.FREEZE_TIME;
  public static final Number BAD_LOGIN_ATTEMPTS = IUserAccountState.NUM_LOGIN_TRIES;
  public static final Number CREATED            = IUserAccountState.CREATE_TIME;
  public static final Number CREATED_BY         = IUserAccountState.CREATE_BY;

  //Possible values for STATE
  public static final Short STATE_DISABLED = new Short(IUserAccountState.STATE_DISABLED);
  public static final Short STATE_ENABLED  = new Short(IUserAccountState.STATE_ENABLED);
  public static final Short STATE_DELETED  = new Short(IUserAccountState.STATE_DELETED);

  public static final String STATE_DISABLED_LABEL = "accountState.state.disabled";
  public static final String STATE_ENABLED_LABEL  = "accountState.state.enabled";
  public static final String STATE_DELETED_LABEL  = "accountState.state.deleted";

  public String getStateLabelKey() throws GTClientException;
  public String getStateLabelKey(Short state) throws GTClientException;
  public Collection getAllowedStates() throws GTClientException;

}