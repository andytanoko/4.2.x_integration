/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultAuditFileManager.java
 *
 ****************************************************************************
 * Date             Author                  Changes
 ****************************************************************************
 * 29 Nov 2005      Sumedh Chalermkanjana   Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;

import com.gridnode.gtas.events.dbarchive.doc.temp.GetAuditFileEvent;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

public class DefaultAuditFileManager extends DefaultAbstractManager
    implements IGTAuditFileManager
{
  DefaultAuditFileManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_AUDIT_FILE, session);
    loadFmi(IGTEntity.ENTITY_AUDIT_FILE);
  }
  
  protected IEvent getGetEvent(Long uid) throws EventException
  {
    return new GetAuditFileEvent(uid);
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
    return new DefaultAuditFileEntity();
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
    return IGTManager.MANAGER_AUDIT_FILE;
  }
  
  protected String getEntityType()
  {
    return IGTEntity.ENTITY_AUDIT_FILE;
  }
}
//  //10 NOV 2005 TWX: modified to add in user select Timezone
//  public IGTListPager getEsPiListPager(EsPiSearchQuery searchQuery, Number[] sortField, boolean[] sortAscending,TimeZone userSelectTZ) 
//  		throws GTClientException
//  {
//    try
//    {
//      EsPiListPager listPager = new EsPiListPager();
//      listPager.setManager(this);
//      listPager.setSession(_session);
////      listPager.setData(searchQuery);
//      
////      IDataFilter filter = DataFilterHelper.generateProcessInstanceFilter(searchQuery.getDocNo(), 
////      																																		searchQuery.getDocType(), 
////                                                                          searchQuery.getTradingPartner(), 
////                                                                          searchQuery.getPartnerName(),
////                                                                          searchQuery.getDocDateFrom(), 
////                                                                          searchQuery.getDocDateTo());
//      IDataFilter filter = DataFilterHelper.generateProcessInstanceFilter(searchQuery, userSelectTZ);
//      
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

//  //TEST
//  private void log(String message)
//  {
//  	Logger.log("[DefaultEsPiManager] " + message);
//  }