/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteServiceAssignmentEvent.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */

package com.gridnode.gtas.events.servicemgmt;

import com.gridnode.gtas.model.servicemgmt.*;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;


public class DeleteServiceAssignmentEvent extends DeleteEntityListEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2269081534564842681L;

	/**
   * Constructor for DeleteServiceAssignmentEvent.
   * @param uids Collection of Uids of the ServiceAssignment entities to delete.
   * @throws EventException Invalid Uids specified.
   */
  public DeleteServiceAssignmentEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteServiceAssignmentEvent.
   * @param serviceAssignmentUid Uid of the ServiceAssignment entity to delete.
   * @throws EventException Invalid Uid specified.
   */
  public DeleteServiceAssignmentEvent(Long serviceAssignmentUid) throws EventException
  {
    super(new Long[] { serviceAssignmentUid });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteServiceAssignmentEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IServiceAssignment.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IServiceAssignment.UID;
  }
}