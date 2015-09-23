/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultEsDocManager.java
 *
 ****************************************************************************
 * Date               Author                  Changes
 ****************************************************************************
 *  6 Oct 2005	      Sumedh Chalermkanjana   Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.TimeZone;

import com.gridnode.gtas.events.dbarchive.*;
import com.gridnode.gtas.client.GTClientException;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.gtas.events.dbarchive.doc.*;

/**
 * The implementation of this class is based on com.gridnode.gtas.client.ctrl.DefaultEsPiManager
 *
 */
public class DefaultEsDocManager extends DefaultAbstractManager
    implements IGTEsDocManager
{
	DefaultEsDocManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_ES_DOC, session);
    loadFmi(IGTEntity.ENTITY_ES_DOC);
  }
  
  protected IEvent getGetEvent(Long uid) throws EventException
  {
  	return new GetEsDocEvent(uid);
//    throw new UnsupportedOperationException();
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
    return new DefaultEsDocEntity();
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
    return IGTManager.MANAGER_ES_DOC;
  }
  
  protected String getEntityType()
  {
    return IGTEntity.ENTITY_ES_DOC;
  }
  
  //TWX: 11 NOV 2005 add in TimeZone userSelectTZ
  public IGTListPager getEsDocListPager(EsDocSearchQuery searchQuery, Number[] sortField, boolean[] sortAscending, TimeZone userSelectTZ) 
  		throws GTClientException
  {
    try
    {
      EsDocListPager listPager = new EsDocListPager();
      listPager.setManager(this);
      listPager.setSession(_session);
      
      IDataFilter filter = DataFilterHelper.generateDocumentMetaInfoFilter(searchQuery, userSelectTZ);
		  if (sortField != null && sortAscending != null && sortField.length > 0 && sortAscending.length > 0)
		  {
		    filter.setOrderFields(sortField, sortAscending);
		  }
		  listPager.setFilter(filter);                      
      return listPager;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting EsDocListPager", t);
    }
  }
  
  public IGTListPager getAssocEsDocListPager(Long uid, Number[] sortField, boolean[] sortAscending) 
		throws GTClientException
	{
  	//TEST
  	debug("getAssocEsDocListPager");
  	if (sortField != null)
  	{
  		debug("sortField[0] = " + sortField[0]);
  	}
  	if (sortAscending != null)
  	{
  		debug("sortAscending[0] = " + sortAscending[0]);
  	}
  	
		try
		{
		  AssocEsDocListPager listPager = new AssocEsDocListPager();
		  listPager.setManager(this);
		  listPager.setSession(_session);
		  
		  IDataFilter filter = DataFilterHelper.generateAssocEsDocFilter(uid);
		  if (sortField != null && sortAscending != null && sortField.length > 0 && sortAscending.length > 0)
		  {
		  	
		  	//TEST
		  	debug("setOrderFields called");
		  	
		    filter.setOrderFields(sortField, sortAscending);
		  }
		  listPager.setFilter(filter);                      
		  return listPager;
		}
		catch(Throwable t)
		{
		  throw new GTClientException("Error getting AssocEsDocListPager", t);
		}
	}
  
  private void debug(String message)
	{
		com.gridnode.gtas.client.web.archive.helpers.Logger.debug("[DefaultEsDocManager] " + message);
	}
}
