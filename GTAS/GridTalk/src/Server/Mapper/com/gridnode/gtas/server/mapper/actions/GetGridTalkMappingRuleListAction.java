/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGridTalkMappingRuleListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 28 2002    Koh Han Sing        Created
 * Mar 27 2002    Neo Sok Lay         Extend from AbstractGetEntityListAction
 */
package com.gridnode.gtas.server.mapper.actions;

import java.util.Collection;

import com.gridnode.gtas.events.mapper.GetGridTalkMappingRuleListEvent;
import com.gridnode.gtas.model.mapper.GridTalkMappingRuleEntityFieldID;
import com.gridnode.gtas.server.mapper.facade.ejb.IGridTalkMappingManagerHome;
import com.gridnode.gtas.server.mapper.facade.ejb.IGridTalkMappingManagerObj;
import com.gridnode.gtas.server.mapper.model.GridTalkMappingRule;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityListAction;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the retrieving of a list of GridTalkMappingRule.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetGridTalkMappingRuleListAction
  extends    AbstractGetEntityListAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2220002099037088172L;
	public static final String CURSOR_PREFIX = "GridTalkMappingRuleListCursor.";
  public static final String ACTION_NAME = "GetGridTalkMappingRuleListAction";

  protected Class getExpectedEventClass()
  {
    return GetGridTalkMappingRuleListEvent.class;
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
    return getGTMappingManager().findGridTalkMappingRules(filter);
  }

  protected Collection convertToMapObjects(Collection entityList)
  {
    return GridTalkMappingRule.convertEntitiesToMap(
             (GridTalkMappingRule[])entityList.toArray(new GridTalkMappingRule[entityList.size()]),
             GridTalkMappingRuleEntityFieldID.getEntityFieldID(),
             null);
  }

  private IGridTalkMappingManagerObj getGTMappingManager()
    throws ServiceLookupException
  {
    return (IGridTalkMappingManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IGridTalkMappingManagerHome.class.getName(),
      IGridTalkMappingManagerHome.class,
      new Object[0]);
  }
}