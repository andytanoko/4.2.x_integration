/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetBizEntityListForUserEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 02 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Remove implementation for IGuardedEvent
 */
package com.gridnode.gtas.events.enterprise;

//import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of BusinessEntities
 * that a User is associated with.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I4
 */
public class GetBizEntityListForUserEvent
  extends    EventSupport
//  implements IGuardedEvent
{
//  public static final String GUARDED_FEATURE = "USER.PROFILE";
//  public static final String GUARDED_ACTION  = "GetBizEntityListForUser";

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7792840460432704996L;
	public static final String USER_ACCT_UID   = "UserAccount UID";

  public GetBizEntityListForUserEvent(Long userAcctUID)
    throws EventException
  {
    checkSetLong(USER_ACCT_UID, userAcctUID);
  }

  public Long getUserAccountUID()
  {
    return (Long)getEventData(USER_ACCT_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetBizEntityListForUserEvent";
  }

  // ************* From IGuardedEvent *************************

//  public String getGuardedFeature()
//  {
//    return GUARDED_FEATURE;
//  }
//
//  public String getGuardedAction()
//  {
//    return GUARDED_ACTION;
//  }

}