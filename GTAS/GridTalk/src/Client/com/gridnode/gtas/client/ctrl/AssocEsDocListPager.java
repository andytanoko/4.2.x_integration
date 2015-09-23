/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AssocEsDocListPager.java
 *
 ****************************************************************************
 * Date           	Author                    Changes
 ****************************************************************************
 * Oct 10 2005			Sumedh Chalermkanjana			Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.gtas.events.dbarchive.doc.temp.*;

public class AssocEsDocListPager extends ListPager
{
	protected IEvent getGetListEvent() throws EventException
	{
		IEvent event;
		if (_listId == null)
		{
			event = new GetAssocEsDocListEvent((IDataFilter) _filter,
					_pageSize, _pageStart);
		} else
		{
			event = new GetAssocEsDocListEvent(_listId, _pageSize,
					_pageStart);
		}
		return event;
	}

}
