/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteUserProcedure.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh           Created
 * Jul 15 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.userprocedure;

import com.gridnode.gtas.model.userprocedure.IUserProcedure;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This event class contains the data for deletion of a UserProcedure.
 *
 * @author Jagadeesh.
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteUserProcedureEvent extends DeleteEntityListEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6174344024220831091L;

	/**
   * Constructor for DeleteUserProcedureEvent.
   * @param uids Collection of UIDs of UserProcedure entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteUserProcedureEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteUserProcedureEvent.
   * @param uID UID of UserProcedure entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteUserProcedureEvent(Long uID) throws EventException
  {
    super(new Long[] { uID });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteUserProcedureEvent";
  }
  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IUserProcedure.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IUserProcedure.UID;
  }
}
