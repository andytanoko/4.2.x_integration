/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAccessRightEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 30 2002    Neo Sok Lay             Created
 */

package com.gridnode.gtas.events.acl;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
 
/**
 * This event class contains the data for retrieving an access right record.
 *
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */

public class GetAccessRightEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5571136120688092139L;
	/**
   * FieldId for UID.
   */
  public static final String ACCESS_RIGHT_UID = "AccessRight UID";

  public GetAccessRightEvent(Long accessRightUID)
    throws EventException
  {
    checkSetLong(ACCESS_RIGHT_UID, accessRightUID);
  }

  public Long getAccessRightUID()
  {
    return (Long) getEventData(ACCESS_RIGHT_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetAccessRightEvent";
  }

}