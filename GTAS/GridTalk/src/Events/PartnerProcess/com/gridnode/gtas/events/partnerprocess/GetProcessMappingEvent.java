/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetProcessMappingEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.partnerprocess;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieving a ProcessMapping base on
 * UID.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class GetProcessMappingEvent
       extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8215930713966586778L;
	public static final String UID = "ProcessMapping UID";

  public GetProcessMappingEvent(Long uid)
    throws EventException
  {
    checkSetLong(UID, uid);
  }

  public Long getUID()
  {
    return (Long)getEventData(UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetProcessMappingEvent";
  }

}