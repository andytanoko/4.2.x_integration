/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteResponseTrackRecordEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 27 2003    Neo Sok Lay         Created
 * Jul 15 2003    Neo Sok Lay         Extend from DeleteEntityListEvent.
 */
package com.gridnode.gtas.events.docalert;

import com.gridnode.gtas.model.docalert.IResponseTrackRecord;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a ResponseTrackRecord based on
 * UID.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.0 I7
 */
public class DeleteResponseTrackRecordEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7240505409610108482L;

	/**
   * Constructor for DeleteResponseTrackRecordEvent.
   * 
   * @param uids Collection of UIDs of ResponseTrackRecord entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteResponseTrackRecordEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteResponseTrackRecordEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IResponseTrackRecord.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IResponseTrackRecord.UID;
  }
}