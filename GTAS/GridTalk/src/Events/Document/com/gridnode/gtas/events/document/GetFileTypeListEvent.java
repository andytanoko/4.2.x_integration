/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetFileTypeListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 23 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of FileTypes,
 * optionally based on a filtering condition.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetFileTypeListEvent
  extends    GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5807184847234283817L;

	public GetFileTypeListEvent()
  {
    super();
  }

  public GetFileTypeListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetFileTypeListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetFileTypeListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetFileTypeListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetFileTypeListEvent";
  }

}