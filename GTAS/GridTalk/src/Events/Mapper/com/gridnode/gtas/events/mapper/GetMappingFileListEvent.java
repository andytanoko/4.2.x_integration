/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMappingFileListEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 27 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.mapper;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a list of MappingFile,
 * optionally based on a filtering condition.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetMappingFileListEvent
  extends    GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 361257226303101177L;

	public GetMappingFileListEvent()
  {
    super();
  }

  public GetMappingFileListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetMappingFileListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetMappingFileListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetMappingFileListEvent(String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetMappingFileListEvent";
  }

}