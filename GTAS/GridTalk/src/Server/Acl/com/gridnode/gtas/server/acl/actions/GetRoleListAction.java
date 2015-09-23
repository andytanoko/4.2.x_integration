/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRoleListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Goh Kan Mun         Created
 * Jun 03 2002    Neo Sok Lay         Extend from GetEntityListAction.
 * Jun 19 2002    Neo Sok Lay         Extend from AbstractGetEntityListAction
 *                                    instead of GetEntityListAction (to be
 *                                    phased out).
 */
package com.gridnode.gtas.server.acl.actions;

import java.util.Collection;

import com.gridnode.gtas.events.acl.GetRoleListEvent;
import com.gridnode.gtas.server.acl.helpers.ActionHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.pdip.framework.db.filter.IDataFilter;


public class GetRoleListAction extends AbstractGetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 288010082725829206L;
	public static final String CURSOR_PREFIX = "RoleListCursor.";
  public static final String ACTION_NAME = "GetRoleListAction";

  // ****************** AbstractGetEntityListAction methods *******************

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
    return ActionHelper.getACLManager().getRolesByFilter(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertRolesToMapObjects(entityList);
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
    return GetRoleListEvent.class;
  }
}