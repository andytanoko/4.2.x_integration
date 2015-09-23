/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateRoleEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Goh Kan Mun         Created
 * Jun 03 2002    Neo Sok Lay         Event data checking.
 * Feb 25 2004    Neo Sok Lay             Implements IGuardedEvent
 */
package com.gridnode.gtas.events.acl;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

public class UpdateRoleEvent extends EventSupport implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7884978817941571488L;
	private static final String GUARDED_FEATURE = "ROLE";
  private static final String GUARDED_ACTION = "UpdateRole";
  
  /**
   * FieldId for UId (Static).
   */
  public static final String UPD_STATIC_UID = "Static Updated UId";

  /**
   * FieldId for role.
   */
  public static final String UPD_ROLE = "Updated Role";

  /**
   * FieldId for description.
   */
  public static final String UPD_DESCRIPTION = "Updated Description";

  public UpdateRoleEvent(Long uId, String updatedRole, String updatedDesc)
    throws EventException
  {
    checkSetLong(UPD_STATIC_UID, uId);
    checkSetString(UPD_ROLE, updatedRole);
    checkSetString(UPD_DESCRIPTION, updatedDesc);
  }

  public Long getUId()
  {
    return (Long) getEventData(UPD_STATIC_UID);
  }

  public String getUpdatedRole()
  {
    return (String) getEventData(UPD_ROLE);
  }

  public String getUpdatedDesc()
  {
    return (String) getEventData(UPD_DESCRIPTION);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateRoleEvent";
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