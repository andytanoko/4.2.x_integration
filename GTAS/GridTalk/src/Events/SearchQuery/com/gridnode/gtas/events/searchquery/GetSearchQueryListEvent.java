/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetSearchQueryListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 06 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.searchquery;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of SearchQuery,
 * optionally based on a filtering condition.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class GetSearchQueryListEvent
  extends    GetEntityListEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5967219757699225069L;

	public GetSearchQueryListEvent()
  {
    super();
  }

  public GetSearchQueryListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetSearchQueryListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetSearchQueryListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetSearchQueryListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetSearchQueryListEvent";
  }

}