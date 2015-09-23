/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SetBizEntityListForUserEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 02 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "USER.PROFILE"
 */
package com.gridnode.gtas.events.enterprise;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for updating the association of BusinessEntities
 * to a User.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I4
 */
public class SetBizEntityListForUserEvent
  extends    EventSupport
  implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7485547158684707982L;
	public static final String GUARDED_FEATURE = "USER.PROFILE";
  public static final String GUARDED_ACTION  = "SetBizEntityListForUser";

  public static final String USER_ACCT_UID   = "UserAccount UID";
  public static final String BE_LIST         = "BusinessEntity List";

  public SetBizEntityListForUserEvent(Long userAcctUID, Collection beList)
    throws EventException
  {
    checkSetLong(USER_ACCT_UID, userAcctUID);
    checkSetCollection(BE_LIST, beList, Long.class);
  }

  public Long getUserAccountUID()
  {
    return (Long)getEventData(USER_ACCT_UID);
  }

  public Collection getBizEntityList()
  {
    return (Collection)getEventData(BE_LIST);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/SetBizEntityListForUserEvent";
  }

  // ************* From IGuardedEvent *************************

  public String getGuardedFeature()
  {
    return GUARDED_FEATURE;
  }

  public String getGuardedAction()
  {
    return GUARDED_ACTION;
  }

}