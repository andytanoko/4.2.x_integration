/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetUtcTimeEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 27 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.locale;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve the Utc Time Offset.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class GetUtcTimeEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4391988902830273682L;

	public GetUtcTimeEvent()
    throws EventException
  {
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetUtcTimeEvent";
  }

}