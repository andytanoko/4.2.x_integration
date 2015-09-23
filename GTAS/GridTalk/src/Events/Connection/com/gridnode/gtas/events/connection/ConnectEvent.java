/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2002    Neo Sok Lay         Created
 * Nov 16 2002    Neo Sok Lay         Requires Security Password.
 */
package com.gridnode.gtas.events.connection;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for Connecting to the GridMaster
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1742541824313136697L;
	private static final String SECURITY_PASSWORD = "Security Password";

  public ConnectEvent(String secPassword)
    throws EventException
  {
    checkSetString(SECURITY_PASSWORD, secPassword);
  }

  public String getSecurityPassword()
  {
    return (String)getEventData(SECURITY_PASSWORD);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/ConnectEvent";
  }


}