/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRoleEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 14 2002    Goh Kan Mun             Created
 * Jun 03 2002    Neo Sok Lay             Allow retrieve by UID.
 */
package com.gridnode.gtas.events.acl;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a Role based on
 * the UID or name of the Role.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class GetRoleEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2767750917019577852L;
	public static final String GET_ROLE_NAME = "Get Role Name";
  public static final String GET_ROLE_UID  = "Get Role UID";

  public GetRoleEvent(String roleName) throws EventException
  {
    checkSetString(GET_ROLE_NAME, roleName);
  }

  public GetRoleEvent(Long roleUID) throws EventException
  {
    checkSetLong(GET_ROLE_UID, roleUID);
  }

  public String getRoleName()
  {
    return (String) getEventData(GET_ROLE_NAME);
  }

  public Long getRoleUID()
  {
    return (Long)getEventData(GET_ROLE_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetRoleEvent";
  }

}