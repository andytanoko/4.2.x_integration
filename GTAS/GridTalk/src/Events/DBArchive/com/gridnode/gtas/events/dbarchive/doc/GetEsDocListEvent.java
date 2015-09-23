/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetEsDocListEvent.java
 *
 ****************************************************************************
 * Date           	Author                    Changes
 ****************************************************************************
 * Oct 3 2005			  Sumedh Chalermkanjana			Created
 */

package com.gridnode.gtas.events.dbarchive.doc;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This event class contains information to do document searching for
 * EStore.
 * 
 * 
 * @author Sumedh Chalermkanjana
 * 
 * @version 2.0
 * @since 2.0
 */
public class GetEsDocListEvent extends GetEntityListEvent
{
	public GetEsDocListEvent()
	{
	}

	public GetEsDocListEvent(IDataFilter filter)
	{
		super(filter);
	}

	public GetEsDocListEvent(IDataFilter filter, int maxRows)
	{
		super(filter, maxRows);
	}

	public GetEsDocListEvent(IDataFilter filter, int maxRows,
			int startRow)
	{
		super(filter, maxRows, startRow);
	}

	public GetEsDocListEvent(String listID, int maxRows,
			int startRow) throws EventException
	{
		super(listID, maxRows, startRow);
	}

	public String getEventName()
	{
		return "java:comp/env/param/event/GetEsDocListEvent";
	}

}
