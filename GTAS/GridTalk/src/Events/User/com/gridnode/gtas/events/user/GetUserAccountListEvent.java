/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetUserAccountListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2002    Neo Sok Lay         Created
 * Jun 03 2002    Neo Sok Lay         Change due to exception thrown in
 *                                    super class.
 * Jun 14 2002    Neo Sok Lay         Implement IGuardedEvent
 */
package com.gridnode.gtas.events.user;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of UserAccounts,
 * optionally based on a filtering condition.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class GetUserAccountListEvent
  extends    GetEntityListEvent
  implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7484365198953363961L;
	public static final String GUARDED_FEATURE = "USER.PROFILE";
  public static final String GUARDED_ACTION  = "GetUserAccountList";

  public GetUserAccountListEvent()
  {
    super();
  }

  public GetUserAccountListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetUserAccountListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetUserAccountListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetUserAccountListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetUserAccountListEvent";
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