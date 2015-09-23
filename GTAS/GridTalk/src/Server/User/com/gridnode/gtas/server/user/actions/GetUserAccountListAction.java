/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetUserAccountListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 29 2002    Neo Sok Lay         Created
 * May 15 2002    Neo Sok Lay         Change of exception thrown at app-user.
 * May 21 2002    Neo Sok Lay         Filter off "DELETED" accounts by default.
 * Jun 03 2002    Neo Sok Lay         Extend from GetEntityListAction
 * Jun 18 2002    Neo Sok Lay         Extend from AbstractGetEntityListAction
 *                                    instead of GetEntityListAction.
 */
package com.gridnode.gtas.server.user.actions;

import java.util.BitSet;
import java.util.Collection;

import com.gridnode.gtas.events.user.GetUserAccountListEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.gtas.server.user.helpers.ActionHelper;
import com.gridnode.pdip.app.user.model.UserAccountState;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This Action class handles the retrieving of a list of User accounts.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class GetUserAccountListAction
  extends    AbstractGetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1766763756518286297L;
	public static final String CURSOR_PREFIX = "UserListCursor.";
  public static final String ACTION_NAME = "GetUserAccountListAction";

  private static final BitSet _stateSet = new BitSet();
  static
  {
    _stateSet.set((int)UserAccountState.STATE_DELETED);
  }

  // ****************** AbstractGetEntityListAction methods *******************

  protected Collection retrieveEntityList(IDataFilter filter)
    throws Exception
  {
    return ActionHelper.getUserManager().findUserAccounts(filter, _stateSet);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertUsersToMapObjects(entityList);
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
    return GetUserAccountListEvent.class;
  }

}