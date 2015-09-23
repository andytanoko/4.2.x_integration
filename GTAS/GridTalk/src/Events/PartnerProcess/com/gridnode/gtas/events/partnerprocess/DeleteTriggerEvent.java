/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteTriggerEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Koh Han Sing        Created
 * Jul 15 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.partnerprocess;

import com.gridnode.gtas.model.partnerprocess.ITrigger;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for updating a Trigger.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteTriggerEvent extends DeleteEntityListEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3028980594594714129L;

	/**
   * Constructor for DeleteTriggerEvent.
   * @param uids Collection of UIDs of the Trigger entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteTriggerEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteTriggerEvent.
   * @param triggerUid UID of the Trigger entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteTriggerEvent(Long triggerUid) throws EventException
  {
    super(new Long[] { triggerUid });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteTriggerEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return ITrigger.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return ITrigger.UID;
  }
}