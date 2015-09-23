/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetScheduleEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 10 2004    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.scheduler;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a Schedule(iCalAlarm) based
 * on UID.
 *
 * @author Koh Han Sing
 *
 * @version 2.2.10
 * @since 2.2.10
 */
public class GetScheduleEvent extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3901443870995063423L;
	public static final String TASK_UID  = "Task UID";

  public GetScheduleEvent(Long uid)
  {
    setEventData(TASK_UID, uid);
  }

  public Long getTaskUID()
  {
    return (Long)getEventData(TASK_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetScheduleEvent";
  }

}
