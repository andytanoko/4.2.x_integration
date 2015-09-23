/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRoleListForUserEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 15 2002    Goh Kan Mun             Created
 * Jun 03 2002    Neo Sok Lay             Event data checking
 */

package com.gridnode.gtas.events.acl;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This event class contains the data for retrieving of a list of Roles for a User.
 *
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class GetRoleListForUserEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8109172934152924114L;

	/**
   * FieldId for userId.
   */
  public static final String USER_ID = "User Id";

  /**
   * FieldId for userUId.
   */
  public static final String USER_UID = "User UId";

  public GetRoleListForUserEvent(Long userUId)
    throws EventException
  {
    checkSetLong(USER_UID, userUId);
  }

  public GetRoleListForUserEvent(String userId)
    throws EventException
  {
    checkSetString(USER_ID, userId);
  }

  public Long getUserUId()
  {
    return (Long) getEventData(USER_UID);
  }

  public String getUserId()
  {
    return (String) getEventData(USER_ID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetRoleListForUserEvent";
  }

}