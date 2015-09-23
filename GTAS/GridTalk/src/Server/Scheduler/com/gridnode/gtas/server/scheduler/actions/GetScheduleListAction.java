/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetScheduleListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 10 2004    Koh Han Sing        Created
 * Feb 18 2004    Neo Sok Lay         Factored out functionalities to ActionHelper
 */
package com.gridnode.gtas.server.scheduler.actions;

import com.gridnode.gtas.events.scheduler.GetScheduleListEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.gtas.server.scheduler.helpers.ActionHelper;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

/**
 * This Action class handles the retrieving of a list of Schedules(iCalAlarm).
 *
 * @author Koh Han Sing
 *
 * @version 2.2.10
 * @since 2.2.10
 */
public class GetScheduleListAction
  extends    AbstractGetEntityListAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5595226161191748296L;
	public static final String CURSOR_PREFIX = "ScheduleListCursor.";
  public static final String ACTION_NAME = "GetScheduleListAction";

  protected Class getExpectedEventClass()
  {
    return GetScheduleListEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected String getListIDPrefix()
  {
    return CURSOR_PREFIX;
  }

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
    return ActionHelper.findSchedulesByFilter(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertScheduleToMapObjects(entityList);
  }

}
