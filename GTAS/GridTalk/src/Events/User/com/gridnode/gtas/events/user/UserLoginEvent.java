/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LoginEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2002    Neo Sok Lay         Created
 * Jun 03 2002    Neo Sok Lay         Event data check.
 */
package com.gridnode.gtas.events.user;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.util.PasswordMask;

/**
 * This Event class contains the data for User Login Authentication.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class UserLoginEvent extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2363022115458945383L;
	public static final String USER_ID   = "User ID";
  public static final String PASSWORD  = "Password";

  public UserLoginEvent(String userID, String password)
    throws EventException
  {
    checkSetString(USER_ID, userID);
    setEventData(PASSWORD, new PasswordMask(password));
  }

  public String getUserID()
  {
    return (String)getEventData(USER_ID);
  }

  public PasswordMask getPassword()
  {
    return (PasswordMask)getEventData(PASSWORD);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UserLoginEvent";
  }

}