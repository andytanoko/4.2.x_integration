/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateSearchQueryAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.searchquery.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gridnode.gtas.events.searchquery.UpdateSearchQueryEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.gtas.server.searchquery.helpers.ActionHelper;
import com.gridnode.pdip.app.searchquery.model.Condition;
import com.gridnode.pdip.app.searchquery.model.ICondition;
import com.gridnode.pdip.app.searchquery.model.SearchQuery;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * This Action class handles the update of a SearchQuery.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class UpdateSearchQueryAction
  extends    AbstractUpdateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6351034082614732628L;

	private SearchQuery _searchQuery;

  public static final String ACTION_NAME = "UpdateSearchQueryAction";

  protected Class getExpectedEventClass()
  {
    return UpdateSearchQueryEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertSearchQueryToMap((SearchQuery)entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateSearchQueryEvent updEvent = (UpdateSearchQueryEvent)event;
    _searchQuery = ActionHelper.getManager().findSearchQuery(updEvent.getQueryUid());
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateSearchQueryEvent updEvent = (UpdateSearchQueryEvent)event;

    _searchQuery.setDescription(updEvent.getQueryDesc());

    List conditions = updEvent.getConditions();
    ArrayList newConditions = new ArrayList();
    for (Iterator i = conditions.iterator(); i.hasNext(); )
    {
      Map conditionMap = (Map)i.next();
      Condition condition = constructCondition(conditionMap);
      newConditions.add(condition);
    }
    _searchQuery.setConditions(newConditions);
    _searchQuery.setPublic(updEvent.isPublic() == null ? false : updEvent.isPublic());
    
    return _searchQuery;
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
//    condition.setField((Number)conditionMap.get(ICondition.FIELD));
//    condition.setXPath((String)conditionMap.get(ICondition.XPATH));
    Short theOperator = new Short((String)conditionMap.get(ICondition.OPERATOR));
    condition.setOperator(theOperator);
    condition.setValues((List)conditionMap.get(ICondition.VALUES));
    condition.setType((Short)conditionMap.get(ICondition.TYPE));

    return condition;
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    ActionHelper.getManager().updateSearchQuery((SearchQuery)entity);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getManager().findSearchQuery(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateSearchQueryEvent updEvent = (UpdateSearchQueryEvent)event;
    return new Object[]
           {
             SearchQuery.ENTITY_NAME,
             String.valueOf(updEvent.getQueryUid())
           };
  }

}