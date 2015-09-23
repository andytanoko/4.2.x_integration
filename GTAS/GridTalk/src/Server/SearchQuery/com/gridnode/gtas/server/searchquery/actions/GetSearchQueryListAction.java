/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetSearchQueryListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 14 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.searchquery.actions;

import java.util.Collection;

import com.gridnode.gtas.events.searchquery.GetSearchQueryListEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.gtas.server.searchquery.helpers.ActionHelper;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This Action class handles the retrieving of a list of SearchQueries.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class GetSearchQueryListAction
  extends    AbstractGetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1815177030129757459L;
	public static final String CURSOR_PREFIX = "SearchQueryListCursor.";
  public static final String ACTION_NAME = "GetSearchQueryListAction";

  protected Class getExpectedEventClass()
  {
    return GetSearchQueryListEvent.class;
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
    Collection queries = ActionHelper.getManager().findSearchQuerys(filter);
    return queries;
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return ActionHelper.convertSearchQueryToMapObjects(entityList);
  }

}
