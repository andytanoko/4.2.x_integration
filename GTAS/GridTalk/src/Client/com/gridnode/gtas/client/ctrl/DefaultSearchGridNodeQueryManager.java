/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultSearchGridNodeQueryManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-17     Andrew Hill         Created
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.HashMap;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.NotApplicableException;
import com.gridnode.gtas.events.activation.RetrieveGridNodeSearchEvent;
import com.gridnode.gtas.events.activation.SubmitGridNodeSearchEvent;
import com.gridnode.gtas.model.activation.ISearchGridNodeCriteria;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultSearchGridNodeQueryManager extends DefaultAbstractManager
  implements IGTSearchGridNodeQueryManager
{
  DefaultSearchGridNodeQueryManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_SEARCH_GRIDNODE_QUERY, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new UnsupportedOperationException("Submitted searches may not be updated");
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTSearchGridNodeQueryEntity query = (IGTSearchGridNodeQueryEntity)entity;
    try
    {
      IGTSearchGridNodeCriteriaEntity criteria = (IGTSearchGridNodeCriteriaEntity)query.getFieldValue(IGTSearchGridNodeQueryEntity.CRITERIA);
      HashMap map = new HashMap();
      map.put( ISearchGridNodeCriteria.CRITERIA, criteria.getFieldValue(IGTSearchGridNodeCriteriaEntity.CRITERIA_TYPE) );
      map.put( ISearchGridNodeCriteria.MATCH, criteria.getFieldValue(IGTSearchGridNodeCriteriaEntity.MATCH_TYPE) );
      map.put( ISearchGridNodeCriteria.GRIDNODE_ID, criteria.getFieldValue(IGTSearchGridNodeCriteriaEntity.GRIDNODE_ID) );
      map.put( ISearchGridNodeCriteria.GRIDNODE_NAME, criteria.getFieldValue(IGTSearchGridNodeCriteriaEntity.GRIDNODE_NAME) );
      map.put( ISearchGridNodeCriteria.CATEGORY, criteria.getFieldValue(IGTSearchGridNodeCriteriaEntity.CATEGORY) );
      map.put( ISearchGridNodeCriteria.BUSINESS_DESC, criteria.getFieldValue(IGTSearchGridNodeCriteriaEntity.BUSINESS_DESC) );
      map.put( ISearchGridNodeCriteria.DUNS, criteria.getFieldValue(IGTSearchGridNodeCriteriaEntity.DUNS) );
      map.put( ISearchGridNodeCriteria.CONTACT_PERSON, criteria.getFieldValue(IGTSearchGridNodeCriteriaEntity.CONTACT_PERSON) );
      map.put( ISearchGridNodeCriteria.EMAIL, criteria.getFieldValue(IGTSearchGridNodeCriteriaEntity.EMAIL) );
      map.put( ISearchGridNodeCriteria.TEL, criteria.getFieldValue(IGTSearchGridNodeCriteriaEntity.TEL) );
      map.put( ISearchGridNodeCriteria.FAX, criteria.getFieldValue(IGTSearchGridNodeCriteriaEntity.FAX) );
      map.put( ISearchGridNodeCriteria.WEBSITE, criteria.getFieldValue(IGTSearchGridNodeCriteriaEntity.WEBSITE) );
      map.put( ISearchGridNodeCriteria.COUNTRY, criteria.getFieldValue(IGTSearchGridNodeCriteriaEntity.COUNTRY) );

      SubmitGridNodeSearchEvent event = new SubmitGridNodeSearchEvent(map);
      Long searchId = (Long)handleEvent(event);
      ((AbstractGTEntity)query).setNewFieldValue(IGTSearchGridNodeQueryEntity.SEARCH_ID, searchId);
      if(searchId == null)
      {
        throw new NullPointerException("null searchId returned by gtas");
      }
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to submit query", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_SEARCH_GRIDNODE_QUERY;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_SEARCH_GRIDNODE_QUERY;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long searchId)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new RetrieveGridNodeSearchEvent(searchId);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new NotApplicableException("No event exists to retrieve list of searches from gtas");
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    throw new NotApplicableException("Explicitly deleting searches is not supported");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_SEARCH_GRIDNODE_QUERY.equals(entityType))
    {
      return new DefaultSearchGridNodeQueryEntity();
    }
    else if(IGTEntity.ENTITY_SEARCH_GRIDNODE_CRITERIA.equals(entityType))
    {
      return new DefaultSearchGridNodeCriteriaEntity();
    }
    else if(IGTEntity.ENTITY_SEARCH_GRIDNODE_QUERY.equals(entityType))
    {
      return new DefaultSearchedGridNodeEntity();
    }
    else if(IGTEntity.ENTITY_SEARCHED_GRIDNODE.equals(entityType))
    {
      return new DefaultSearchedGridNodeEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  public Collection getAll() throws GTClientException
  {
    throw new java.lang.UnsupportedOperationException("getAll() is not supported for SearchGridNodeQuery");
  }
}