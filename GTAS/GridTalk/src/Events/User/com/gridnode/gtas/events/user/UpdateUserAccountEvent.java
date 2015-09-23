/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateUserAccountEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2002    Neo Sok Lay         Created
 * Jun 03 2002    Neo Sok Lay         Event data check.
 * Jun 14 2002    Neo Sok Lay         Implement IGuardedEvent
 */
package com.gridnode.gtas.events.user;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.util.PasswordMask;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for update a user account.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateUserAccountEvent
  extends    EventSupport
  implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5137563841356514757L;
	public static final String GUARDED_FEATURE_MAIN = "USER.ADMIN";
  public static final String GUARDED_FEATURE_SUB  = "USER.PROFILE";
  public static final String GUARDED_ACTION       = "UpdateUserAccount";

  public static final String ACCOUNT_UID          = "Account UID";
  public static final String UPD_USER_NAME        = "Updated User Name";
  public static final String UPD_PHONE            = "Updated Phone";
  public static final String UPD_EMAIL            = "Updated Email";
  public static final String UPD_PROPERTY         = "Updated Property";
  public static final String UPD_STATE            = "Updated State";
  public static final String RESET_PWD            = "Reset Password";
  public static final String IS_UNFREEZE_ACCT     = "Is Unfreeze Account";
  public static final String IS_RESET_LOGIN_TRIES = "Is Reset Login Tries";

  private String _guardedFeature;

  public UpdateUserAccountEvent(Long uID, String userName,
    String phone, String email, String property) throws EventException
  {
    checkSetLong(ACCOUNT_UID, uID);
    checkSetString(UPD_USER_NAME, userName);
    setEventData(UPD_PHONE, phone);
    setEventData(UPD_EMAIL, email);
    setEventData(UPD_PROPERTY, property);
    _guardedFeature = GUARDED_FEATURE_SUB;
  }

  public UpdateUserAccountEvent(Long uID, String userName,
    String phone, String email, String property,
    boolean isUnfreezeAcct, boolean isResetLoginTries,
    short state, String resetPassword) throws EventException
  {
    this(uID, userName, phone, email, property);
    setEventData(UPD_STATE, new Short(state));
    if (resetPassword != null)
      setEventData(RESET_PWD, new PasswordMask(resetPassword));
    setEventData(IS_UNFREEZE_ACCT, new Boolean(isUnfreezeAcct));
    setEventData(IS_RESET_LOGIN_TRIES, new Boolean(isResetLoginTries));
    _guardedFeature = GUARDED_FEATURE_MAIN;
  }

  public Long getAccountUID()
  {
    return (Long)getEventData(ACCOUNT_UID);
  }

  public PasswordMask getResetPassword()
  {
    return (PasswordMask)getEventData(RESET_PWD);
  }

  public String getUpdUserName()
  {
    return (String)getEventData(UPD_USER_NAME);
  }

  public String getUpdPhone()
  {
    return (String)getEventData(UPD_PHONE);
  }

  public String getUpdEmail()
  {
    return (String)getEventData(UPD_EMAIL);
  }

  public String getUpdProperty()
  {
    return (String)getEventData(UPD_PROPERTY);
  }

  public Short getUpdState()
  {
    return (Short)getEventData(UPD_STATE);
  }

  public boolean isUnfreezeAccount()
  {
    Boolean bool = (Boolean)getEventData(IS_UNFREEZE_ACCT);
    return (bool==null? false : bool.booleanValue());
  }

  public boolean isResetLoginTries()
  {
    Boolean bool = (Boolean)getEventData(IS_RESET_LOGIN_TRIES);
    return (bool==null? false : bool.booleanValue());
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateUserAccountEvent";
  }

  // ************* From IGuardedEvent *************************

  public String getGuardedFeature()
  {
    return _guardedFeature;
  }

  public String getGuardedAction()
  {
    return GUARDED_ACTION;
  }


}