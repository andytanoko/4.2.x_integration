/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RemoveAccessRightEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 30 2002    Neo Sok Lay             Created
 * Jul 14 2003    Neo Sok Lay             Extend from DeleteEntityListAction
 * Feb 25 2004    Neo Sok Lay             Implements IGuardedEvent
 */
package com.gridnode.gtas.events.acl;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.gtas.model.acl.IAccessRight;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This event class contains the data for removing access right from a Role.
 *
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2.10
 * @since 2.0
 */
public class RemoveAccessRightEvent extends DeleteEntityListEvent implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6891802446805549329L;
	private static final String GUARDED_FEATURE = "ROLE";
  private static final String GUARDED_ACTION = "RemoveAccessRight";
  /**
   * Constructor for RemoveAccessRightEvent.
   * @param uids Collection of UIDs of the AccessRight(s) to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public RemoveAccessRightEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for RemoveAccessRightEvent.
   * 
   * @param accessRightUID UID of the AccessRight to delete.
   * @throws EventException Invalid UID specified.
   */
  public RemoveAccessRightEvent(Long accessRightUID) throws EventException
  {
    super(new Long[] { accessRightUID });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/RemoveAccessRightEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IAccessRight.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IAccessRight.UID;
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