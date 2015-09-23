/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteDocumentTypeEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2002    Koh Han Sing        Created
 * Jul 15 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.document;

import com.gridnode.gtas.model.document.IDocumentType;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a DocumentType based on
 * UID.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteDocumentTypeEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2672295211478927458L;

	/**
   * Constructor for DeleteDocumentTypeEvent.
   * @param uids Collection of UIDs of DocumentType entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteDocumentTypeEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteDocumentTypeEvent.
   * @param docTypeUID UID of DocumentType entity to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteDocumentTypeEvent(Long docTypeUID) throws EventException
  {
    super(new Long[] { docTypeUID });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteDocumentTypeEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IDocumentType.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IDocumentType.UID;
  }
}