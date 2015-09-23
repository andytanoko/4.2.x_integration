/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetSearchQueryAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 06 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.searchquery.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGetEntityAction;

import com.gridnode.gtas.events.searchquery.GetSearchQueryEvent;
import com.gridnode.gtas.server.searchquery.helpers.ActionHelper;

import com.gridnode.pdip.app.searchquery.model.SearchQuery;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the retrieving of a SearchQuery.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class GetSearchQueryAction
  extends    AbstractGetEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4515714663821525613L;
	public static final String ACTION_NAME = "GetSearchQueryAction";

  protected Class getExpectedEventClass()
  {
    return GetSearchQueryEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity getEntity(IEvent event) throws Exception
  {
    GetSearchQueryEvent getEvent = (GetSearchQueryEvent)event;
    return ActionHelper.getManager().findSearchQuery(getEvent.getSearchQueryUID());
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertSearchQueryToMap((SearchQuery)entity);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    GetSearchQueryEvent getEvent = (GetSearchQueryEvent)event;
    return new Object[]
           {
             SearchQuery.ENTITY_NAME,
             getEvent.getSearchQueryUID()
           };
  }
}