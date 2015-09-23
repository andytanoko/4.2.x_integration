/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChangeAccountPasswordEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2002    Neo Sok Lay         Created
 * Jun 03 2002    Neo Sok Lay         Event data check.
 * Jun 14 2002    Neo Sok Lay         Implement IGuardedEvent.
 */
package com.gridnode.gtas.events.user;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.util.PasswordMask;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for changing a user account password.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class ChangeAccountPasswordEvent
  extends    EventSupport
  implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9042031647280392505L;
	public static final String GUARDED_FEATURE = "USER.PROFILE";
  public static final String GUARDED_ACTION  = "ChangeAccountPassword";

  public static final String ACCOUNT_UID  = "Account UID";
  public static final String CURRENT_PWD  = "Current Password";
  public static final String NEW_PWD      = "New Password";

  public ChangeAccountPasswordEvent(
    Long accountUID, String currPassword, String newPassword)
    throws EventException
  {
    checkSetLong(ACCOUNT_UID, accountUID);
    setEventData(CURRENT_PWD, new PasswordMask(currPassword));
    setEventData(NEW_PWD, new PasswordMask(newPassword));
  }

  public Long getAccountUID()
  {
    return (Long)getEventData(ACCOUNT_UID);
  }

  public PasswordMask getCurrentPassword()
  {
    return (PasswordMask)getEventData(CURRENT_PWD);
  }

  public PasswordMask getNewPassword()
  {
    return (PasswordMask)getEventData(NEW_PWD);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/ChangeAccountPasswordEvent";
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