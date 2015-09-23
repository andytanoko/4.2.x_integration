/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeletePortEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    Koh Han Sing        Created
 * Jul 14 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.backend;

import com.gridnode.gtas.model.backend.IPort;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for the deletion of a Port.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeletePortEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3119019691163395934L;

	/**
   * Constructor for DeletePortEvent.
   * @param uids Collection of UIDs of the Port entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeletePortEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeletePortEvent.
   * @param portUid UID of the Port entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeletePortEvent(Long portUid) throws EventException
  {
    super(new Long[] { portUid });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeletePortEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IPort.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IPort.UID;
  }
}