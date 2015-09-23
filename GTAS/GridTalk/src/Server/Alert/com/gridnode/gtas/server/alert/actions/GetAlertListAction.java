/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAlertListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 * Feb 07 2003    Neo Sok Lay         Also retrieve UIDs of Actions bound to
 *                                    the Alerts.
 */
package com.gridnode.gtas.server.alert.actions;

import java.util.Collection;

import com.gridnode.gtas.events.alert.GetAlertListEvent;
import com.gridnode.gtas.server.alert.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This Action class handles the retrieving of a list of Alert.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class GetAlertListAction
  extends    AbstractGetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 178186008668950337L;
	public static final String CURSOR_PREFIX = "AlertListCursor.";
  public static final String ACTION_NAME = "GetAlertListAction";

  protected Class getExpectedEventClass()
  {
    return GetAlertListEvent.class;
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
    return ActionHelper.getAlertsByFilter(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertAlertToMapObjects(entityList);
  }

}
