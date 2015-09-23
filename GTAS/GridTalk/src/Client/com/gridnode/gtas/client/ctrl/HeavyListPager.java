/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HeavyListPager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-21     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.*;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.IFilter; //nb: dependency outside ctrl package! (naughty)
import com.gridnode.gtas.client.utils.StaticUtils; //nb: dependency outside ctrl package! (very naughty)

import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.db.filter.*;

class HeavyListPager extends ListPager
{
  protected String LISTPAGER_TYPE_STRING = "HeavyListPager";
  protected List _allItems = null;

  void setFilter(IDataFilter filter)
  {
    throw new UnsupportedOperationException("HeavyListPager does not support IDataFilter");
  }

  synchronized void setFilter(IFilter filter)
  {
    _filter = filter;
  }

  public synchronized void free()
  {
    super.free();
    _allItems = null;
  }

  protected void loadAllItems() throws GTClientException
  {
    try
    {
      if(_allItems == null)
      {
        IEvent event =_manager.getGetListEvent(null, _listId, -1, -1);
        EntityListResponseData elrd = (EntityListResponseData)_manager.handleEvent(event);
        List all =  _manager.processEntityListResponse(elrd, event);
        if(_filter != null)
        {
          _allItems = (List)StaticUtils.getFilteredCollection(all, (IFilter)_filter, this, false);
        }
        else
        {
          _allItems = all;
        }
        _listId = elrd.getListID();
        _totalItemCount = _allItems.size();
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error loading all list items",t);
    }
  }

  public synchronized Collection getPage() throws GTClientException
  {
    if(_manager == null) throw new NullPointerException("manager attribute is null"); //20030331AH
    try
    {
      if(_allItems == null)
      {
        loadAllItems(); //20030324AH - factored out the loading from the page splitting stuff
      }
      int allSize = _allItems.size();
      if(_pageStart >= allSize) _pageStart = 0;
      int pageStop = _pageStart + _pageSize;
      ArrayList results = new ArrayList(_pageSize);
      for(int i=_pageStart; (i < pageStop) && (i < allSize); i++ )
      { //Extract only the required rows for the page from all the rows
        results.add(_allItems.get(i));
      }
      return results;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to get list page",t);
    }
  }
}