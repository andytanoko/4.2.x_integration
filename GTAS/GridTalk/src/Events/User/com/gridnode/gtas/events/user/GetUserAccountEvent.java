/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetUserAccountEvent.java
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

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a User Account based on
 * UID.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class GetUserAccountEvent
  extends    EventSupport
  implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5527486638228743198L;
	public static final String GUARDED_FEATURE = "USER.PROFILE";
  public static final String GUARDED_ACTION  = "GetUserAccount";

  public static final String ACCOUNT_UID  = "Account UID";
  public static final String USER_ID      = "User ID";

  public GetUserAccountEvent(Long accountUID)
    throws EventException
  {
    checkSetLong(ACCOUNT_UID, accountUID);
  }

  public GetUserAccountEvent(String userID)
    throws EventException
  {
    checkSetString(USER_ID, userID);
  }

  public Long getAccountUID()
  {
    return (Long)getEventData(ACCOUNT_UID);
  }

  public String getUserID()
  {
    return (String)getEventData(USER_ID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetUserAccountEvent";
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