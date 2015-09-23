/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetServiceAssignmentListEvent.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Feb 9, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.events.servicemgmt;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

public class GetServiceAssignmentListEvent
  extends    GetEntityListEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2301300310042520326L;

	public GetServiceAssignmentListEvent()
  {
    super();
  }

  public GetServiceAssignmentListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetServiceAssignmentListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetServiceAssignmentListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetServiceAssignmentListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetServiceAssignmentListEvent";
  }

}