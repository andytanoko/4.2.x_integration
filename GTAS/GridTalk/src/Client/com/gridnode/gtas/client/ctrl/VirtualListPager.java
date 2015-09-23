/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: VirtualListPager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-24     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.utils.StaticUtils;

public final class VirtualListPager extends ListPager
{
  protected String LISTPAGER_TYPE_STRING = "VirtualListPager";
  protected List _allItems = null;

  public VirtualListPager(IGTSession session)
  { //20030331AH
    super();
    if(session == null) throw new NullPointerException("session is null");
    setPageSize(((DefaultGTSession)session)._context.getDefaultPageSize());
  }

  synchronized void setFilter(IFilter filter)
  {
    _filter = filter;
    try
    {
      setItems(_allItems); //refilter based on filter
    }
    catch(Throwable t)
    {
      throw new RuntimeException("Error setting filter!:" + t.getMessage());
    }
  }

  /**
   * nb: items are filtered immediately!
   */
  public synchronized void setItems(List items) throws GTClientException
  {
    try
    {
      if(items == null) items = Collections.EMPTY_LIST;
      IFilter filter = (IFilter)getFilter();
      _allItems = (filter == null) ? items : StaticUtils.getFilteredCollection(items, filter, this, false);
      _totalItemCount = _allItems.size();
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error setting items collection",t);
    }
  }

  public synchronized List getItems()
  {
    return _allItems;
  }

  public synchronized void free()
  {
    //no-op
  }

  public synchronized Collection getPage() throws GTClientException
  {
    try
    {
      if(_allItems == null)
      {
        return Collections.EMPTY_LIST;
      }
      else
      {
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
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to get list page",t);
    }
  }
}