/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAssocEsDocListEvent.java
 *
 ****************************************************************************
 * Date           	Author                    Changes
 ****************************************************************************
 * Oct 10 2005			Sumedh Chalermkanjana			Created
 */

package com.gridnode.gtas.events.dbarchive.doc.temp;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

public class GetAssocEsDocListEvent extends GetEntityListEvent
{
	public GetAssocEsDocListEvent(IDataFilter filter)
	{
		super(filter);
	}

	public GetAssocEsDocListEvent(IDataFilter filter, int maxRows)
	{
		super(filter, maxRows);
	}

	public GetAssocEsDocListEvent(IDataFilter filter, int maxRows,
			int startRow)
	{
		super(filter, maxRows, startRow);
	}

	public GetAssocEsDocListEvent(String listID, int maxRows,
			int startRow) throws EventException
	{
		super(listID, maxRows, startRow);
	}

	public String getEventName()
	{
		return "java:comp/env/param/event/GetAssocEsDocListEvent";
	}

}
