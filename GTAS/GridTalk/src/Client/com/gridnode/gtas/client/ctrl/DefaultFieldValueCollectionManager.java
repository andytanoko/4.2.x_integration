/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultFieldValueCollectionManager.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * 20 Oct 2005	    Sumedh Chalermkanjana   Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.events.dbarchive.searchpage.GetFieldValueCollectionEvent;
import com.gridnode.gtas.client.GTClientException;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * The implementation of this class is based on DefaultEsPiManager.java.
 *
 */
public class DefaultFieldValueCollectionManager extends DefaultAbstractManager
    implements IGTFieldValueCollectionManager
{
	DefaultFieldValueCollectionManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_FIELD_VALUE_COLLECTION, session);
    loadFmi(IGTEntity.ENTITY_FIELD_VALUE_COLLECTION);
  }
  
  protected IEvent getGetEvent(Long uid) throws EventException
  {
    return new GetFieldValueCollectionEvent(uid);
  }

  protected IEvent getGetListEvent(IDataFilter filter) throws EventException
  {
  	throw new UnsupportedOperationException();
  }

  protected IEvent getDeleteEvent(Collection uids) throws EventException
  {
    throw new UnsupportedOperationException();
  }
  
  protected AbstractGTEntity createEntityObject(String entityType)
  throws GTClientException
  {
    return new DefaultFieldValueCollectionEntity();
  }
  
  protected void doUpdate(IGTEntity entity)
  throws GTClientException
  {
    throw new UnsupportedOperationException();
  }

  protected void doCreate(IGTEntity entity)
  throws GTClientException
  {
    throw new UnsupportedOperationException();
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_FIELD_VALUE_COLLECTION;
  }
  
  protected String getEntityType()
  {
    return IGTEntity.ENTITY_FIELD_VALUE_COLLECTION;
  }
  
//  public IGTListPager getEsPiListPager(EsSearchQuery searchQuery, Number[] sortField, boolean[] sortAscending) 
//  		throws GTClientException
//  {
//    try
//    {
//      EsPiListPager listPager = new EsPiListPager();
//      listPager.setManager(this);
//      listPager.setSession(_session);
////      listPager.setData(searchQuery);
//      
//      IDataFilter filter = DataFilterHelper.generateProcessInstanceFilter(searchQuery.getDocNo(), searchQuery.getDocType(), 
//                                                                             searchQuery.getTradingPartner(), searchQuery.getDocDateFrom(), 
//                                                                             searchQuery.getDocDateTo());
//      if (sortField != null && sortAscending != null && sortField.length > 0 && sortAscending.length > 0)
//      {
//        filter.setOrderFields(sortField, sortAscending);
//      }
//      listPager.setFilter(filter);
//
//      return listPager;
//    }
//    catch(Throwable t)
//    {
//      throw new GTClientException("Error getting EsPiListPager", t);
//    }
//  }
}
