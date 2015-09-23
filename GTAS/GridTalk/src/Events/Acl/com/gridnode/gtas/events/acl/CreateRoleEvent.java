/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateRoleEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 07 2002    Goh Kan Mun             Created
 * Jun 03 2002    Neo Sok Lay             Event data checking.
 * Feb 25 2004    Neo Sok Lay             Implements IGuardedEvent
 */
package com.gridnode.gtas.events.acl;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This event class contains the data for creation of a role.
 *
 *
 * @author Goh Kan Mun
 *
 * @version GT 2.1.19
 * @since 2.0
 */

public class CreateRoleEvent extends EventSupport implements IGuardedEvent
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1626974691637457871L;
	private static final String GUARDED_FEATURE = "ROLE";
  private static final String GUARDED_ACTION = "CreateRole";

  /**
   * FieldId for role.
   */
  public static final String ROLE = "Role";

  /**
   * FieldId for description.
   */
  public static final String DESCRIPTION = "Description";

  /**
   * FieldId for can_delete.
   */
  public static final String CAN_DELETE = "Can Delete";

  public CreateRoleEvent(String role, String description, boolean canDelete)
    throws EventException
  {
    checkSetString(ROLE, role);
    checkSetString(DESCRIPTION, description);
    setEventData(CAN_DELETE, new Boolean(canDelete));
  }

  public String getRole()
  {
    return (String) getEventData(ROLE);
  }

  public String getDescription()
  {
    return (String) getEventData(DESCRIPTION);
  }

  public boolean canDelete()
  {
    return ((Boolean) getEventData(CAN_DELETE)).booleanValue();
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreateRoleEvent";
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