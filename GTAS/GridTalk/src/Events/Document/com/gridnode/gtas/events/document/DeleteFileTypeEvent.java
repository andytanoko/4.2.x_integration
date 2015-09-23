/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteFileTypeEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 23 2002    Koh Han Sing        Created
 * Jul 15 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.document;

import com.gridnode.gtas.model.document.IFileType;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a FileType based on
 * UID.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteFileTypeEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7730323630611297598L;

	/**
   * Constructor for DeleteFileTypeEvent.
   * @param uids Collection of UIDs of the FileType entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteFileTypeEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteFileTypeEvent.
   * @param fileTypeUID UID of the FileType entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteFileTypeEvent(Long fileTypeUID) throws EventException
  {
    super(new Long[] { fileTypeUID });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteFileTypeEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IFileType.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IFileType.UID;
  }
}