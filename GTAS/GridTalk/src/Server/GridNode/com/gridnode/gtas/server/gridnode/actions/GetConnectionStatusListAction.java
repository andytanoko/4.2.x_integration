/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetConnectionStatusListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 05 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.actions;

import java.util.Collection;

import com.gridnode.gtas.events.gridnode.GetConnectionStatusListEvent;
import com.gridnode.gtas.server.gridnode.helpers.ActionHelper;
import com.gridnode.gtas.server.gridnode.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This Action class handles the retrieving of a list of ConnectionStatus(s).
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GetConnectionStatusListAction
  extends    AbstractGetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2229395532033011597L;
	public static final String CURSOR_PREFIX = "ConnStatusListCursor.";
  public static final String ACTION_NAME = "GetConnectionStatusListAction";

  // ****************** AbstractGetEntityListAction methods *******************

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
    return ServiceLookupHelper.getGridNodeManager().findConnectionStatusByFilter(
             filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertConnStatusesToMapObjects(entityList);
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
    return GetConnectionStatusListEvent.class;
  }

}