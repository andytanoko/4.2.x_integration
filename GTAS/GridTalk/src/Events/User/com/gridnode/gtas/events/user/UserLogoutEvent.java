/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LogoutEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.user;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for User Logout.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class UserLogoutEvent extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3985443466434760671L;

	public UserLogoutEvent()
  {
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UserLogoutEvent";
  }

}