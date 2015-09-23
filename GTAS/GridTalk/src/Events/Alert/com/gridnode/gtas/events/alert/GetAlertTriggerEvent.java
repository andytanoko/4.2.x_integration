/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAlertTriggerEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.alert;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a AlertTrigger based on
 * UID.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class GetAlertTriggerEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2043570384758595102L;
	public static final String UID  = "AlertTrigger UID";

  public GetAlertTriggerEvent(Long uid)
  {
    setEventData(UID, uid);
  }

  public Long getAlertTriggerUID()
  {
    return (Long)getEventData(UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetAlertTriggerEvent";
  }

}