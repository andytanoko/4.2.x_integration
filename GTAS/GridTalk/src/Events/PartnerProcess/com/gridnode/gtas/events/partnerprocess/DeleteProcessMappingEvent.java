/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteProcessMappingEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 21 2002    Neo Sok Lay         Created
 * Jul 15 2003    Neo Sok Lay         extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.partnerprocess;

import com.gridnode.gtas.model.partnerprocess.IProcessMapping;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a ProcessMapping.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.0 I7
 */
public class DeleteProcessMappingEvent extends DeleteEntityListEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2921652770333607613L;

	/**
   * Constructor for DeleteProcessMappingEvent.
   * @param uids Collection of UIDs of the ProcessMapping entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteProcessMappingEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteProcessMappingEvent.
   * @param uid UID of the ProcessMapping entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteProcessMappingEvent(Long uid) throws EventException
  {
    super(new Long[] { uid });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteProcessMappingEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IProcessMapping.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IProcessMapping.UID;
  }
}