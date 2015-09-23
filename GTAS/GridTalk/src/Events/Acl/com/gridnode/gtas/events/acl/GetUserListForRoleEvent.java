/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetUserListForRoleEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 05 2002    Goh Kan Mun             Created
 */

package com.gridnode.gtas.events.acl;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This event class contains the data for retrieving of a list of Users for a Role.
 *
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class GetUserListForRoleEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1846777863775660162L;

	/**
   * FieldId for roleName.
   */
  public static final String ROLE_NAME = "Role Name";

  /**
   * FieldId for roleUId.
   */
  public static final String ROLE_UID = "Role UId";

  public GetUserListForRoleEvent(Long roleUId)
    throws EventException
  {
    checkSetLong(ROLE_UID, roleUId);
  }

  public GetUserListForRoleEvent(String roleName)
    throws EventException
  {
    checkSetString(ROLE_NAME, roleName);
  }

  public Long getRoleUId()
  {
    return (Long) getEventData(ROLE_UID);
  }

  public String getRoleName()
  {
    return (String) getEventData(ROLE_NAME);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetUserListForRoleEvent";
  }

}