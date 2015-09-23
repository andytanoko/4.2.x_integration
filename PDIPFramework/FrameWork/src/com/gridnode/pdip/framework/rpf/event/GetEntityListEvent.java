/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetEntityListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 29 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.rpf.event;

import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This Event class contains the data for retrieving a list of homogeneous
 * type entities, optionally based on a filtering condition.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class GetEntityListEvent
  extends    EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7677804221538686321L;
	public static final String FILTER             = "Filter";
  public static final String START_ROW          = "Start Row";
  public static final String MAX_ROWS           = "Max Rows";
  public static final String LIST_ID            = "List ID";

  public GetEntityListEvent()
  {
  }

  public GetEntityListEvent(IDataFilter filter)
  {
    setEventData(FILTER, filter);
  }

  public GetEntityListEvent(IDataFilter filter, int maxRows)
  {
    this(filter);
    setEventData(MAX_ROWS, new Integer(maxRows));
  }

  public GetEntityListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    this(filter, maxRows);
    setEventData(START_ROW, new Integer(startRow));
  }

  public GetEntityListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    checkSetString(LIST_ID, listID);
    setEventData(MAX_ROWS, new Integer(maxRows));
    setEventData(START_ROW, new Integer(startRow));
  }

  public IDataFilter getFilter()
  {
    return (IDataFilter)getEventData(FILTER);
  }

  public int getStartRow()
  {
    Integer startRow = (Integer)getEventData(START_ROW);
    if (startRow == null)
      return 0;
    else
      return startRow.intValue();
  }

  public int getMaxRows()
  {
    Integer maxRows = (Integer)getEventData(MAX_ROWS);
    if (maxRows == null)
      return 0;
    else
      return maxRows.intValue();
  }

  public String getListID()
  {
    return (String)getEventData(LIST_ID);
  }
}