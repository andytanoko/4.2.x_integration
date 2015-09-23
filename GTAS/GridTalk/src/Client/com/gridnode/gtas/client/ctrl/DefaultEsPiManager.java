/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultEStoreSearchManager.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * Sep 16 2005      Sumedh Chalermkanjana   Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.TimeZone;

import com.gridnode.gtas.events.dbarchive.*;
import com.gridnode.gtas.client.GTClientException;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.gtas.client.web.archive.helpers.*;


public class DefaultEsPiManager extends DefaultAbstractManager
    implements IGTEsPiManager
{
  DefaultEsPiManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_ES_PI, session);
    loadFmi(IGTEntity.ENTITY_ES_PI);
  }
  
  protected IEvent getGetEvent(Long uid) throws EventException
  {
    return new GetEsPiEvent(uid);
  }

  protected IEvent getGetListEvent(IDataFilter filter) throws EventException
  {
	throw new UnsupportedOperationException();
    //return new GetEsPiListEvent(filter);
  }

  protected IEvent getDeleteEvent(Collection uids) throws EventException
  {
    throw new UnsupportedOperationException();
  }
  
  protected AbstractGTEntity createEntityObject(String entityType)
  throws GTClientException
  {
    return new DefaultEsPiEntity();
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
    return IGTManager.MANAGER_ES_PI;
  }
  
  protected String getEntityType()
  {
    return IGTEntity.ENTITY_ES_PI;
  }
  
  //10 NOV 2005 TWX: modified to add in user select Timezone
  public IGTListPager getEsPiListPager(EsPiSearchQuery searchQuery, Number[] sortField, boolean[] sortAscending,TimeZone userSelectTZ) 
  		throws GTClientException
  {
    try
    {
      EsPiListPager listPager = new EsPiListPager();
      listPager.setManager(this);
      listPager.setSession(_session);
//      listPager.setData(searchQuery);
      
//      IDataFilter filter = DataFilterHelper.generateProcessInstanceFilter(searchQuery.getDocNo(), 
//      																																		searchQuery.getDocType(), 
//                                                                          searchQuery.getTradingPartner(), 
//                                                                          searchQuery.getPartnerName(),
//                                                                          searchQuery.getDocDateFrom(), 
//                                                                          searchQuery.getDocDateTo());
      IDataFilter filter = DataFilterHelper.generateProcessInstanceFilter(searchQuery, userSelectTZ);
      
      if (sortField != null && sortAscending != null && sortField.length > 0 && sortAscending.length > 0)
      {
        filter.setOrderFields(sortField, sortAscending);
      }
      listPager.setFilter(filter);

      return listPager;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting EsPiListPager", t);
    }
  }
  
  //TEST
  private void log(String message)
  {
  	Logger.log("[DefaultEsPiManager] " + message);
  }
}
