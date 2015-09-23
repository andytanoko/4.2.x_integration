/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2009 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteArchiveEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 10 2009    Ong Eu Soon        Created
 */
package com.gridnode.gtas.events.dbarchive;

import com.gridnode.gtas.model.dbarchive.IArchiveMetaInfo;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

public class DeleteArchiveEvent extends DeleteEntityListEvent
{

	/**
   * Constructor for DeleteFileTypeEvent.
   * @param uids Collection of UIDs of the FileType entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteArchiveEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteFileTypeEvent.
   * @param fileTypeUID UID of the FileType entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteArchiveEvent(Long archiveUID) throws EventException
  {
    super(new Long[] { archiveUID });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteArchiveEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IArchiveMetaInfo.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IArchiveMetaInfo.UID;
  }
}