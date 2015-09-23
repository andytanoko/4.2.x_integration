/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2009 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetArchiveListEvent.java
 *
 ****************************************************************************
 * Date           	Author                        	Changes
 ****************************************************************************
 * Feb 10 2009		  Ong Eu Soon               			Created
 */

package com.gridnode.gtas.events.dbarchive;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

public class GetArchiveListEvent extends GetEntityListEvent
{
	public GetArchiveListEvent(IDataFilter filter)
	{
		super(filter);
	}

	public GetArchiveListEvent(IDataFilter filter, int maxRows)
	{
		super(filter, maxRows);
	}

	public GetArchiveListEvent(IDataFilter filter, int maxRows,
			int startRow)
	{
		super(filter, maxRows, startRow);
	}

	public GetArchiveListEvent(String listID, int maxRows,
			int startRow) throws EventException
	{
		super(listID, maxRows, startRow);
	}

	public String getEventName()
	{
		return "java:comp/env/param/event/GetArchiveListEvent";
	}

}
