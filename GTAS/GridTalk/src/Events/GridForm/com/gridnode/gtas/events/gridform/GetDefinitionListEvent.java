/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetDefinitionListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    Jared Low           Created.
 */
package com.gridnode.gtas.events.gridform;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;

/**
 * Event to retrieve a list of definitions based on a filtering condition.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class GetDefinitionListEvent extends GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3226652100403957643L;

	public GetDefinitionListEvent()
  {
    super();
  }

  public GetDefinitionListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetDefinitionListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetDefinitionListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetDefinitionListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetDefinitionListEvent";
  }

}