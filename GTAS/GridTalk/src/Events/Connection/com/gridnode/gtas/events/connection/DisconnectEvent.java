/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DisconnectEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.connection;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
 
/**
 * This Event class contains the data for disconnecting from the GridMaster.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class DisconnectEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7133738046853548440L;

	public DisconnectEvent()
  {
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DisconnectEvent";
  }


}