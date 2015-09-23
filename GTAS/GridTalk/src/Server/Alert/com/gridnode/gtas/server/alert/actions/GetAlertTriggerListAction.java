/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAlertTriggerListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 22 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.actions;

import com.gridnode.gtas.server.alert.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.gtas.events.alert.GetAlertTriggerListEvent;

import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;

/**
 * This Action class handles the retrieving of a list of AlertTrigger.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class GetAlertTriggerListAction
  extends    AbstractGetEntityListAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5822696387489335208L;
	public static final String CURSOR_PREFIX = "AlertTriggerListCursor.";
  public static final String ACTION_NAME = "GetAlertTriggerListAction";

  // ****************** AbstractGetEntityListAction methods *******************

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
    return ServiceLookupHelper.getGridTalkAlertMgr().findAlertTriggers(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertAlertTriggersToMapObjects(entityList);
  }

  protected String getListIDPrefix()
  {
    return CURSOR_PREFIX;
  }

  // ***************** AbstractGridTalkAction methods ***********************

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Class getExpectedEventClass()
  {
    return GetAlertTriggerListEvent.class;
  }
}