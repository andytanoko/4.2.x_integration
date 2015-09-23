/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPortEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.backend;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a Port based on
 * Uid.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetPortEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4608713983122915656L;
	public static final String PORT_UID = "Port Uid";

  public GetPortEvent(Long PortUid)
  {
    setEventData(PORT_UID, PortUid);
  }

  public Long getPortUid()
  {
    return (Long)getEventData(PORT_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetPortEvent";
  }

}