/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteGridDocumentEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 02 2002    Koh Han Sing        Created
 * Jul 15 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.document;

import com.gridnode.gtas.model.document.IGridDocument;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a GridDocument based on
 * UID.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteGridDocumentEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3665072872973484893L;

	/**
   * Constructor for DeleteGridDocumentEvent.
   * @param uids Collection of UIDs of the GridDocument entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteGridDocumentEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteGridDocumentEvent.
   * @param gDocUID UID of the GridDocument entity to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteGridDocumentEvent(Long gDocUID) throws EventException
  {
    super(new Long[] { gDocUID });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteGridDocumentEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IGridDocument.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IGridDocument.UID;
  }
}