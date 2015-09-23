/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetHousekeepingInfoEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2003      Mahesh         Created
 */ 
package com.gridnode.gtas.events.housekeeping;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
 
public class GetHousekeepingInfoEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2083140987762751979L;

	public GetHousekeepingInfoEvent()
  {
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetHousekeepingInfoEvent";
  }

}