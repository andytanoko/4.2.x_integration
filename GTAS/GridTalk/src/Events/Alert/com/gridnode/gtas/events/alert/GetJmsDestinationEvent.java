/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetJmsDestinationEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 4 Jan 06				SC									Created
 */
package com.gridnode.gtas.events.alert;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

public class GetJmsDestinationEvent
  extends    EventSupport
{
	private static final long serialVersionUID = -8726102418072691512L;
	public static final String UID  = "UID";

  public GetJmsDestinationEvent(Long uid)
    throws EventException
  {
    checkSetLong(UID, uid);
  }

  public Long getUid()
  {
    return (Long) getEventData(UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetJmsDestinationEvent";
  }
}