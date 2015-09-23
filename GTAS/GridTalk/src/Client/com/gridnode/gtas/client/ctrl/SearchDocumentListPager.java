/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchDocumentListPager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-11-17     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.events.document.SearchDocumentEvent;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

class SearchDocumentListPager extends ListPager
{
  protected Long _searchUid;
  
  void setSearchUid(Long searchUid)
  {
    _searchUid = searchUid;
  }

  protected IEvent getGetListEvent() throws EventException
  { // 20031126DDJ
    IEvent event;
    if(_listId == null)
    {
      event = new SearchDocumentEvent(_searchUid, (IDataFilter)_filter, _pageSize, _pageStart);  
    }
    else
    {
      event = new SearchDocumentEvent(_searchUid, _listId, _pageSize, _pageStart);  
    }
    return event;
  }
}