/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetResponseTrackRecordEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 27 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.docalert;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a ResponseTrackRecord based on
 * UID.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class GetResponseTrackRecordEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9049030026474636966L;
	public static final String UID  = "ResponseTrackRecord UID";

  public GetResponseTrackRecordEvent(Long uid)
  {
    setEventData(UID, uid);
  }

  public Long getResponseTrackRecordUID()
  {
    return (Long)getEventData(UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetResponseTrackRecordEvent";
  }

}