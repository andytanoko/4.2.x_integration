/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteRoleEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Goh Kan Mun         Created
 * Jun 03 2002    Neo Sok Lay         event data checking.
 * Feb 25 2004    Neo Sok Lay         Implements IGuardedEvent
 */
package com.gridnode.gtas.events.acl;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.gtas.model.acl.IRole;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

public class DeleteRoleEvent extends DeleteEntityListEvent implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5047057118963935987L;
	private static final String GUARDED_FEATURE = "ROLE";
  private static final String GUARDED_ACTION = "DeleteRole";

  /**
   * Constructor for DeleteRoleEvent.
   * @param uids Collection of UIDs for Role entities to be deleted
   * @throws EventException Invalid <code>uids</code> specified
   */
  public DeleteRoleEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for DeleteRoleEvent.
   * @param uid UID for Role entity to be deleted
   * @throws EventException Invalid <code>uid</code> specified
   */
  public DeleteRoleEvent(Long uId) throws EventException
  {
    super(new Long[] { uId });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteRoleEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IRole.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IRole.UID;
  }

  
  /**
   * @see com.gridnode.gtas.events.IGuardedEvent#getGuardedAction()
   */
  public String getGuardedAction()
  {
    return GUARDED_ACTION;
  }

  /**
   * @see com.gridnode.gtas.events.IGuardedEvent#getGuardedFeature()
   */
  public String getGuardedFeature()
  {
    return GUARDED_FEATURE;
  }
  
}