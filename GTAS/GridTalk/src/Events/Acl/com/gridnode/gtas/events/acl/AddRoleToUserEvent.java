/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AddRoleToUserEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 15 2002    Goh Kan Mun             Created
 * Jun 03 2002    Neo Sok Lay             Event data checking.
 * Feb 25 2004    Neo Sok Lay             Implements IGuardedEvent
 */

package com.gridnode.gtas.events.acl;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This event class contains the data for creation of a Role-User relationship.
 *
 *
 * @author Goh Kan Mun
 *
 * @version GT 2.1.19
 * @since 2.0
 */

public class AddRoleToUserEvent extends EventSupport implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4246252347442990660L;
	private static final String GUARDED_FEATURE = "USER.ADMIN";
  private static final String GUARDED_ACTION = "AddRoleFromUser";

  /**
   * FieldId for userId.
   */
  public static final String USER_ID = "User Id";

  /**
   * FieldId for userUId.
   */
  public static final String USER_UID = "User UId";

  /**
   * FieldId for roleId.
   */
  public static final String ROLE_NAME = "ROLE NAME";

  /**
   * FieldId for roleUId.
   */
  public static final String ROLE_UID = "Role UId";

  public AddRoleToUserEvent(Long userUId, Long roleUId)
    throws EventException
  {
    checkSetLong(USER_UID, userUId);
    checkSetLong(ROLE_UID, roleUId);
  }

  public AddRoleToUserEvent(String userId, String roleName)
    throws EventException
  {
    checkSetString(USER_ID, userId);
    checkSetString(ROLE_NAME, roleName);
  }

  public Long getUserUId()
  {
    return (Long) getEventData(USER_UID);
  }

  public Long getRoleUId()
  {
    return (Long) getEventData(ROLE_UID);
  }

  public String getUserId()
  {
    return (String) getEventData(USER_ID);
  }

  public String getRoleName()
  {
    return (String) getEventData(ROLE_NAME);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/AddRoleToUserEvent";
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