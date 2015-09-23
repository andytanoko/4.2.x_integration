/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2004 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteScheduleEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 09 2004    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.scheduler;

import com.gridnode.gtas.model.scheduler.IiCalAlarm;

import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a Schedule(iCalAlarm) based
 * on UID.
 *
 * @author Koh Han Sing
 *
 * @version 2.2.10
 * @since 2.2.10
 */
public class DeleteScheduleEvent extends DeleteEntityListEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2123352416645715237L;

	/**
   * Constructor for DeleteScheduleEvent.
   * @param uids Collection of Uids of the iCalAlarm entities to delete.
   * @throws EventException Invalid Uids specified.
   */
  public DeleteScheduleEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteScheduleEvent.
   * @param scheduleUid Uid of the iCalAlarm entity to delete.
   * @throws EventException Invalid Uid specified.
   */
  public DeleteScheduleEvent(Long scheduleUid) throws EventException
  {
    super(new Long[] { scheduleUid });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteScheduleEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IiCalAlarm.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IiCalAlarm.UID;
  }
}
