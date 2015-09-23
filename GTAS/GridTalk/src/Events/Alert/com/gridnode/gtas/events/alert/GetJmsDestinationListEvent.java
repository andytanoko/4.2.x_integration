/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetJmsDestinationListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 4 Jan 06				SC									Created
 */
package com.gridnode.gtas.events.alert;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;

public class GetJmsDestinationListEvent
  extends    GetEntityListEvent
{
	private static final long serialVersionUID = -8726102418072691512L;
	
	public GetJmsDestinationListEvent()
  {
    super();
  }

  public GetJmsDestinationListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetJmsDestinationListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetJmsDestinationListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetJmsDestinationListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetJmsDestinationListEvent";
  }
}