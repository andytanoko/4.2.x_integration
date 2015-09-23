/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetProcessMappingListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.partnerprocess;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of ProcessMapping(s),
 * optionally based on a filtering condition.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class GetProcessMappingListEvent
       extends GetEntityListEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8084232797629143933L;

	public GetProcessMappingListEvent()
  {
    super();
  }

  public GetProcessMappingListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetProcessMappingListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetProcessMappingListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetProcessMappingListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetProcessMappingListEvent";
  }

}