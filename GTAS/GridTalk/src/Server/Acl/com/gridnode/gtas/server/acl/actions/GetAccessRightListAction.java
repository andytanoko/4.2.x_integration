/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAccessRightListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 30 2002    Neo Sok Lay         Created
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractGetEntityListAction
 *                                    instead of GetEntityListAction (to be
 *                                    phased out).
 */
package com.gridnode.gtas.server.acl.actions;

import java.util.Collection;

import com.gridnode.gtas.events.acl.GetAccessRightListEvent;
import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This Action class handles the retrieving of a list of access rights.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class GetAccessRightListAction
  extends    AbstractGetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4768657651387936603L;
	public static final String CURSOR_PREFIX = "ACRListCursor.";
  public static final String ACTION_NAME = "GetAccessRightListAction";

  // ****************** AbstractGetEntityListAction methods *******************

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
    return ActionHelper.getACLManager().getAccessRights(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertAccessRightsToMapObjects(entityList);
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
    return GetAccessRightListEvent.class;
  }

}