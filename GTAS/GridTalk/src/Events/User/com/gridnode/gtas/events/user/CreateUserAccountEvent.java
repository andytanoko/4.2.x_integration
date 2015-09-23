/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateUserAccountEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2002    Neo Sok Lay         Created
 * Jun 03 2002    Neo Sok Lay         Event data check.
 * Jun 13 2002    Neo Sok Lay         Implement IGuardedEvent.
 */
package com.gridnode.gtas.events.user;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.util.PasswordMask;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for the creation of new User accounts.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class CreateUserAccountEvent
  extends    EventSupport
  implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -342665904500791784L;
	public static final String GUARDED_FEATURE = "USER.ADMIN";
  public static final String GUARDED_ACTION  = "CreateUserAccount";

  public static final String USER_ID      = "User ID";
  public static final String USER_NAME    = "User Name";
  public static final String INITIAL_PWD  = "Initial Password";
  public static final String PHONE        = "Phone";
  public static final String EMAIL        = "Email";
  public static final String PROPERTY     = "Property";
  public static final String IS_ENABLED   = "Is Enabled";

  public CreateUserAccountEvent(String userID, String userName,
    String initialPassword, String phone, String email, String property,
    boolean isEnabled) throws EventException
  {
    checkSetString(USER_ID, userID);
    checkSetString(USER_NAME, userName);
    setEventData(INITIAL_PWD, new PasswordMask(initialPassword));
    setEventData(PHONE, phone);
    setEventData(EMAIL, email);
    setEventData(PROPERTY, property);
    setEventData(IS_ENABLED, new Boolean(isEnabled));
  }

  public String getUserID()
  {
    return (String)getEventData(USER_ID);
  }

  public String getUserName()
  {
    return (String)getEventData(USER_NAME);
  }

  public PasswordMask getInitialPassword()
  {
    return (PasswordMask)getEventData(INITIAL_PWD);
  }

  public String getPhone()
  {
    return (String)getEventData(PHONE);
  }

  public String getEmail()
  {
    return (String)getEventData(EMAIL);
  }

  public String getProperty()
  {
    return (String)getEventData(PROPERTY);
  }

  public boolean isEnabled()
  {
    return ((Boolean)getEventData(IS_ENABLED)).booleanValue();
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreateUserAccountEvent";
  }

  // ************* From IGuardedEvent *************************

  public String getGuardedFeature()
  {
    return GUARDED_FEATURE;
  }

  public String getGuardedAction()
  {
    return GUARDED_ACTION;
  }

}