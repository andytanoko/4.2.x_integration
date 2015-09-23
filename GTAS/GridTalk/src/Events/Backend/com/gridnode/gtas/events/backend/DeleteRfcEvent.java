/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteRfcEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    Koh Han Sing        Created
 * Jul 14 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.backend;

import com.gridnode.gtas.model.backend.IRfc;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for the deletion of a Rfc.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteRfcEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9142302043930272865L;

	/**
   * Constructor for DeleteRfcEvent.
   * @param uids Collection of UIDs of the Rfc entities to delete
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteRfcEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteRfcEvent.
   * @param rfcUid UID of the Rfc entity to delete
   * @throws EventException Invalid UID specified.
   */
  public DeleteRfcEvent(Long rfcUid) throws EventException
  {
    super(new Long[] { rfcUid });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteRfcEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IRfc.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IRfc.UID;
  }
}