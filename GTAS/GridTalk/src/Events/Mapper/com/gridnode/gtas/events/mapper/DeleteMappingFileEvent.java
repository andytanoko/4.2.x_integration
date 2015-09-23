/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteMappingFileEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 27 2002    Koh Han Sing        Created
 * Jul 15 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.mapper;

import com.gridnode.gtas.model.mapper.IMappingFile;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for the deletion of a MappingFile.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteMappingFileEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7486155603989101155L;

	/**
   * Constructor for DeleteMappingFileEvent.
   * @param uids Collection of UIDs of the MappingFile entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteMappingFileEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteMappingFileEvent.
   * @param mappingFileUID UID of the MappingFile entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteMappingFileEvent(Long mappingFileUID) throws EventException
  {
    super(new Long[] { mappingFileUID });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteMappingFileEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IMappingFile.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IMappingFile.UID;
  }
}