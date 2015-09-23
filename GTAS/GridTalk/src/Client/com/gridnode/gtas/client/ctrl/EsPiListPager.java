/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EsPiListPager.java
 *
 ****************************************************************************
 * Date           	Author                        	Changes
 ****************************************************************************
 * Oct 3 2005		Sumedh Chalermkanjana			Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.gtas.client.web.archive.helpers.Logger;
import com.gridnode.gtas.events.dbarchive.*;

public class EsPiListPager extends ListPager
{
//	private EsSearchQuery searchQuery;
//
//	public void setData(EsSearchQuery searchQuery)
//	{
//		this.searchQuery = searchQuery;
//	}
	
	protected IEvent getGetListEvent() throws EventException
	{
		IEvent event;
		
		//TEST
		log("_listId == null: " + (_listId == null));
		
		if (_listId == null)
		{
			event = new GetEsPiListEvent((IDataFilter) _filter,
					_pageSize, _pageStart);
		} else
		{
			event = new GetEsPiListEvent(_listId, _pageSize,
					_pageStart);
		}
		return event;
	}

	//TEST
  private void log(String message)
  {
  	Logger.log("[EsPiListPager] " + message);
  }
}
