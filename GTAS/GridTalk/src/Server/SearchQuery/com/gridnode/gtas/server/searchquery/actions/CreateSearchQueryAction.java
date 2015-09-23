/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateProcessMappingAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.searchquery.actions;

import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;

import com.gridnode.gtas.events.searchquery.CreateSearchQueryEvent;
import com.gridnode.gtas.server.searchquery.helpers.ActionHelper;

import com.gridnode.pdip.app.searchquery.model.SearchQuery;
import com.gridnode.pdip.app.searchquery.model.Condition;
import com.gridnode.pdip.app.searchquery.model.ICondition;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This Action class handles the creation of a new SearchQuery.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class CreateSearchQueryAction
  extends    AbstractCreateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7836116467453582228L;
	public static final String ACTION_NAME = "CreateSearchQueryAction";

  protected Class getExpectedEventClass()
  {
    return CreateSearchQueryEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findSearchQuery(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateSearchQueryEvent createEvent = (CreateSearchQueryEvent)event;

    SearchQuery newSearchQuery = new SearchQuery();
    newSearchQuery.setName(createEvent.getQueryName());
    newSearchQuery.setDescription(createEvent.getQueryDesc());
    newSearchQuery.setCreateBy(createEvent.getCreatedBy());
    List conditions = createEvent.getConditions();
    for (Iterator i = conditions.iterator(); i.hasNext(); )
    {
      Map conditionMap = (Map)i.next();
      Condition condition = constructCondition(conditionMap);
      newSearchQuery.addCondition(condition);
    }
    
    newSearchQuery.setPublic(createEvent.isPublic() == null ? false : createEvent.isPublic());
    
    return newSearchQuery;
  }

  private Condition constructCondition(Map conditionMap)
  {
    Condition condition = new Condition();
    if (conditionMap.get(ICondition.FIELD) != null)
    {
//      Integer fieldId = new Integer(((Short)conditionMap.get(ICondition.FIELD)).intValue());
//      condition.setField(fieldId);
      condition.setField((Integer)conditionMap.get(ICondition.FIELD));
    }
    if (conditionMap.get(ICondition.XPATH) != null)
    {
      condition.setXPath((String)conditionMap.get(ICondition.XPATH));
    }
    Short theOperator = new Short((String)conditionMap.get(ICondition.OPERATOR));
    condition.setOperator(theOperator);
    condition.setValues((List)conditionMap.get(ICondition.VALUES));
    condition.setType((Short)conditionMap.get(ICondition.TYPE));

    return condition;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateSearchQueryEvent createEvent = (CreateSearchQueryEvent)event;
    return new Object[]
           {
             SearchQuery.ENTITY_NAME,
             createEvent.getQueryName(),
             createEvent.getQueryDesc(),
           };
  }

  protected Long createEntity(AbstractEntity entity) throws Exception
  {
    return ActionHelper.getManager().createSearchQuery((SearchQuery)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertSearchQueryToMap((SearchQuery)entity);
  }


}