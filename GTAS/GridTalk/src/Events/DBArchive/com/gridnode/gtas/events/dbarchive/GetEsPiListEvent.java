/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetEsPiListEvent.java
 *
 ****************************************************************************
 * Date           	Author                        	Changes
 ****************************************************************************
 * Oct 3 2005		Sumedh Chalermkanjana			Created
 */

package com.gridnode.gtas.events.dbarchive;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
//import com.gridnode.gtas.client.ctrl.*;

/**
 * This event class contains information to do process instance searching for
 * EStore.
 * 
 * 
 * @author Sumedh Chalermkanjana
 * 
 * @version 2.0
 * @since 2.0
 */
public class GetEsPiListEvent extends GetEntityListEvent
{
	public GetEsPiListEvent(IDataFilter filter)
	{
		super(filter);
	}

	public GetEsPiListEvent(IDataFilter filter, int maxRows)
	{
		super(filter, maxRows);
	}

	public GetEsPiListEvent(IDataFilter filter, int maxRows,
			int startRow)
	{
		super(filter, maxRows, startRow);
	}

	public GetEsPiListEvent(String listID, int maxRows,
			int startRow) throws EventException
	{
		super(listID, maxRows, startRow);
	}

	public String getEventName()
	{
		return "java:comp/env/param/event/GetEsPiListEvent";
	}

}
