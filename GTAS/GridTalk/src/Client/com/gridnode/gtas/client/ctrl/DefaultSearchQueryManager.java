/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultSearchQueryManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-10-27     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.events.searchquery.CreateSearchQueryEvent;
import com.gridnode.gtas.events.searchquery.DeleteSearchQueryEvent;
import com.gridnode.gtas.events.searchquery.GetSearchQueryEvent;
import com.gridnode.gtas.events.searchquery.GetSearchQueryListEvent;
import com.gridnode.gtas.events.searchquery.UpdateSearchQueryEvent;
import com.gridnode.gtas.model.searchquery.ISearchQuery;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

class DefaultSearchQueryManager extends DefaultAbstractManager
  implements IGTSearchQueryManager
{
  DefaultSearchQueryManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_SEARCH_QUERY, session);
  }

  protected void doUpdate(IGTEntity entity) throws GTClientException
  {
    try
    {
      IGTSearchQueryEntity searchQuery = (IGTSearchQueryEntity)entity;

      Long uid = searchQuery.getUidLong();
      String description = searchQuery.getFieldString(IGTSearchQueryEntity.DESCRIPTION);
      List conditions = (List)searchQuery.getFieldValue(IGTSearchQueryEntity.CONDITIONS);
      Boolean isPublic = StaticUtils.booleanValue((String)searchQuery.getFieldValue(IGTSearchQueryEntity.IS_PUBLIC));

      System.out.println("DefaultSearchQueryManager isPublic: "+isPublic);
      
      IEvent event = new UpdateSearchQueryEvent(uid,
                                                description,
                                                convertConditionEntitiesToMaps(conditions),
                                                isPublic);

      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws GTClientException
  {
    try
    {
      IGTSearchQueryEntity searchQuery = (IGTSearchQueryEntity)entity;

      String name = searchQuery.getFieldString(IGTSearchQueryEntity.NAME);
      String description = searchQuery.getFieldString(IGTSearchQueryEntity.DESCRIPTION);
      String createdBy = searchQuery.getFieldString(IGTSearchQueryEntity.CREATED_BY);
      List conditions = (List)searchQuery.getFieldValue(IGTSearchQueryEntity.CONDITIONS);
      Boolean isPublic = StaticUtils.booleanValue((String)searchQuery.getFieldValue(IGTSearchQueryEntity.IS_PUBLIC));
      

      IEvent event = new CreateSearchQueryEvent(name,
                                                description,
                                                createdBy,
                                                convertConditionEntitiesToMaps(conditions),
                                                isPublic);
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_SEARCH_QUERY;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_SEARCH_QUERY;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetSearchQueryEvent(uid);
  }

  
  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    if(filter == null)
    {
      filter = new DataFilterImpl();
    }
    
    filter.addSingleFilter(null, ISearchQuery.IS_PUBLIC, filter.getEqualOperator(), "true", false);
    filter.addSingleFilter(filter.getOrConnector(), ISearchQuery.CREATED_BY, filter.getEqualOperator(), _session.getUserId(), false);
    
    return new GetSearchQueryListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  {
    DeleteSearchQueryEvent event = new DeleteSearchQueryEvent(uids);
    event.setIsAdmin(_session.isAdmin());
    event.setLoginID(_session.getUserId());
    
    return event;
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_SEARCH_QUERY.equals(entityType))
    {
      return new DefaultSearchQueryEntity();
    }
    if(IGTEntity.ENTITY_CONDITION.equals(entityType))
    {
      return new DefaultConditionEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  protected void setDefaultFieldValues(AbstractGTEntity entity)
    throws GTClientException
  {
    // Get the user name from the session
    String userName = entity.getSession().getUserId();
    entity.setNewFieldValue(IGTSearchQueryEntity.CREATED_BY, userName);
  }
  
  public IGTConditionEntity newCondition() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_CONDITION);
    entity.setNewEntity(true);
    entity.setNewFieldValue(IGTConditionEntity.VALUES, new ArrayList(2));
    return (IGTConditionEntity)entity;
  }
  
  private List convertConditionEntitiesToMaps(List conditionEntities) throws GTClientException
  {
    List conditionMaps = new ArrayList(conditionEntities.size());
    
    Iterator i = conditionEntities.iterator();
    while(i.hasNext())
    {
      IGTConditionEntity condition = (IGTConditionEntity)i.next();
      Short type        = (Short)condition.getFieldValue(IGTConditionEntity.TYPE);
      Integer field     = (Integer)condition.getFieldValue(IGTConditionEntity.FIELD);
      String xpath      = (String)condition.getFieldValue(IGTConditionEntity.XPATH);
      String operator   = (String)condition.getFieldValue(IGTConditionEntity.OPERATOR);
      Collection values = (Collection)condition.getFieldValue(IGTConditionEntity.VALUES);

      Map map = new HashMap();
      map.put(IGTConditionEntity.TYPE, type);
      if(IGTConditionEntity.TYPE_GDOC.equals(type))
      {
        map.put(IGTConditionEntity.FIELD, field);
      }
      else
      {
        map.put(IGTConditionEntity.XPATH, xpath);
      }
      map.put(IGTConditionEntity.OPERATOR, operator);
      map.put(IGTConditionEntity.VALUES, values);
      
      conditionMaps.add(map);
    }
    
    return conditionMaps;
  }
  
  /**
   * To retrieve only the search query that either created by own or those marked as "Public".
   */
  /*
  public Collection getAll() throws GTClientException
  {
    try
    {
      IDataFilter impl = new DataFilterImpl();
      impl.addSingleFilter(null, ISearchQuery.IS_PUBLIC, impl.getEqualOperator(), "true", false);
      impl.addSingleFilter(impl.getOrConnector(), ISearchQuery.CREATED_BY, impl.getEqualOperator(), _session.getUserId(), false);
      
      System.out.println("DefaultSearchQueryManager getAll: "+impl.getFilterExpr());
      
      IEvent event = getGetListEvent(impl);
      return handleGetListEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error processing getAll() request", t);
    }
  } */
}