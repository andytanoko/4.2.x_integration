/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetResponseTrackRecordListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 27 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.docalert;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of ResponseTrackRecords,
 * optionally based on a filtering condition.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class GetResponseTrackRecordListEvent
  extends    GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6685571939637596194L;

	public GetResponseTrackRecordListEvent()
  {
    super();
  }

  public GetResponseTrackRecordListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetResponseTrackRecordListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetResponseTrackRecordListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetResponseTrackRecordListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetResponseTrackRecordListEvent";
  }

}