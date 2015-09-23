/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetTriggerEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.partnerprocess;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for updating a Trigger.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetTriggerEvent
       extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 538203965119456300L;
	public static final String TRIGGER_UID = "Trigger Uid";

  public GetTriggerEvent(Long triggerUid)
  {
    setEventData(TRIGGER_UID, triggerUid);
  }

  public Long getTriggerUid()
  {
    return (Long)getEventData(TRIGGER_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetTriggerEvent";
  }

}