/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ListPager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-20     Andrew Hill         Created
 * 2003-04-03     Andrew Hill         Fixed logic error in hasNext()
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

class ListPager implements IGTListPager
{
  protected String LISTPAGER_TYPE_STRING = "Listpager";
  protected Object _filter;
  protected DefaultAbstractManager _manager;
  protected IGTSession _session;
  protected String _listId;
  protected int _pageSize = 0;
  protected int _pageStart = 0;
  protected int _totalItemCount = UNKNOWN;

  public synchronized IGTManager getManager()
  { return _manager; }

  synchronized void setManager(DefaultAbstractManager manager)
  {
    _manager = manager;
    if(manager != null)
    {
      _pageSize = ((DefaultGTSession)manager._session)._context.getDefaultPageSize();
    }
    else
    {
      throw new NullPointerException("manager is null");
    }
  }

  synchronized String getListId()
  { return _listId; }

  synchronized void setListId(String listId)
  { _listId = listId; }

  synchronized Object getFilter()
  { return _filter;}

  synchronized void setFilter(IDataFilter filter)
  {
    _filter = filter;
  }

  public synchronized IGTSession getSession()
  { return _session; }

  synchronized void setSession(IGTSession session)
  { _session = session; }

  public synchronized void free()
  {
    //@todo: when gtas supports it we will inform gtas that we dont need that list anymore
    _listId = null; //set to null , so next getPage results in new list
  }

  public synchronized int getPageSize()
  { return _pageSize; }

  public synchronized void setPageSize(int pageSize)
  { _pageSize = pageSize; }

  public synchronized int getPageStart()
  { return _pageStart; }

  public synchronized void setPageStart(int pageStart)
  { _pageStart = pageStart > 0 ? pageStart : 0; }

  public synchronized int getTotalItemCount()
  { return _totalItemCount; }

  public synchronized boolean nextPage()
  {
    if(hasNext())
    {
      _pageStart += _pageSize;
      return true;
    }
    else
    {
      return false;
    }
  }

  public synchronized boolean prevPage()
  {
    if(hasPrev())
    {
      _pageStart -= _pageSize;
      return true;
    }
    else
    {
      return false;
    }
  }

  public boolean hasNext()
  {
    return ( (_pageStart + _pageSize) < _totalItemCount); //20030403AH
  }

  public boolean hasPrev()
  {
    return (_pageStart > 0);
  }

  public synchronized String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(LISTPAGER_TYPE_STRING);
    buffer.append("[");
    buffer.append("listId=");
    buffer.append(_listId);
    buffer.append(", pageStart=");
    buffer.append(_pageStart);
    buffer.append(", pageSize=");
    buffer.append(_pageSize);
    buffer.append(",totalItemCount=");
    buffer.append(_totalItemCount);
    buffer.append("]");
    return buffer.toString();
  }

  public synchronized Collection getPage() throws GTClientException
  {
    if(_manager == null) throw new NullPointerException("manager attribute is null"); //20030331AH
    try
    {
      //IEvent event = _manager.getGetListEvent((IDataFilter)_filter, _listId, _pageSize, _pageStart);
      IEvent event = getGetListEvent(); // 20031126 DDJ
      EntityListResponseData elrd = (EntityListResponseData)_manager.handleEvent(event);
      Collection results =  _manager.processEntityListResponse(elrd, event);
      _listId = elrd.getListID();
      _pageStart = elrd.getStartRow();
      _totalItemCount = _pageStart + results.size() + elrd.getRowsRemaining();
      return results;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to get list page",t);
    }
  }
  
  protected IEvent getGetListEvent() throws EventException
  { // 20031126DDJ
    return _manager.getGetListEvent((IDataFilter)_filter, _listId, _pageSize, _pageStart);
  }
}