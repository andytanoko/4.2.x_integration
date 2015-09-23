/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteAlertEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 * 2003-07-14     Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.alert;

import com.gridnode.gtas.model.alert.IAlert;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a Alert based on
 * its UID
 *
 * @author Daniel D'Cotta
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteAlertEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1587333754945202090L;

	/**
   * Constructor for DeleteAlertEvent.
   * @param uid UID of Alert to delete.
   * @throws EventException Invalid uid specified.
   */
  public DeleteAlertEvent(Long uid) throws EventException
  {
    super(new Long[] { uid });
  }

  /**
   * Constructor for DeleteAlertEvent.
   * @param uids Collection of UIDs of the Alert entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteAlertEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteAlertEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IAlert.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IAlert.UID;
  }
}