/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteActionEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 * 2003-07-14     Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.alert;

import com.gridnode.gtas.model.alert.IAction;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a Action based on
 * its UID.
 *
 * @author Daniel D'Cotta
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteActionEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4135202268827366815L;

	/**
   * Constructor for DeleteActionEvent.
   * @param uids Collection of UIDs of the Action entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteActionEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteActionEvent.
   * 
   * @param uid UID of the Action to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteActionEvent(Long uid) throws EventException
  {
    super(new Long[] { uid });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteActionEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IAction.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IAction.UID;
  }
}