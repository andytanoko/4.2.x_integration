/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGridTalkMappingRuleAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 03 2002    Koh Han Sing        Created
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractGetEntityAction
 */
package com.gridnode.gtas.server.mapper.actions;

import java.util.Map;

import com.gridnode.gtas.events.mapper.GetGridTalkMappingRuleEvent;
import com.gridnode.gtas.model.mapper.GridTalkMappingRuleEntityFieldID;
import com.gridnode.gtas.server.mapper.facade.ejb.IGridTalkMappingManagerHome;
import com.gridnode.gtas.server.mapper.facade.ejb.IGridTalkMappingManagerObj;
import com.gridnode.gtas.server.mapper.model.GridTalkMappingRule;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the retrieving of a GridTalkMappingRule.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0
 */
public class GetGridTalkMappingRuleAction
  extends    AbstractGetEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -205004672916117431L;
	private static final String ACTION_NAME = "GetGridTalkMappingRuleAction";

	protected Map convertToMap(AbstractEntity entity)
	{
		return GridTalkMappingRule.convertToMap(entity, GridTalkMappingRuleEntityFieldID.getEntityFieldID(), null);
	}

	protected AbstractEntity getEntity(IEvent event) throws Exception
	{
		GetGridTalkMappingRuleEvent getEvent = (GetGridTalkMappingRuleEvent)event;
		return getGTMappingManager().findGridTalkMappingRule(getEvent.getGridTalkMappingRuleUID());
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		GetGridTalkMappingRuleEvent getEvent = (GetGridTalkMappingRuleEvent)event;
		return new Object[] {GridTalkMappingRule.ENTITY_NAME, getEvent.getGridTalkMappingRuleUID()};
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return GetGridTalkMappingRuleEvent.class;
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