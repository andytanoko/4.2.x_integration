/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteMessageTemplateEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 * 2003-07-14     Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.alert;

import com.gridnode.gtas.model.alert.IMessageTemplate;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a MessageTemplate based on
 * its UID.
 *
 * @author Daniel D'Cotta
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteMessageTemplateEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5282108170881534596L;

	/**
   * Constructor for DeleteMessageTemplateEvent.
   * @param uids Collection of UIDs of the MessageTemplate entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteMessageTemplateEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteMessageTemplateEvent.
   * @param uid UID of the MessageTemplate entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteMessageTemplateEvent(Long uid) throws EventException
  {
    super(new Long[] { uid });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteMessageTemplateEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IMessageTemplate.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IMessageTemplate.UID;
  }
}