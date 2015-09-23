/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchDocumentEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.document;

import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for executing a document searching.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class SearchDocumentEvent
  extends    GetEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 240285661417310959L;
	public static final String QUERY_UID = "Query Uid";

  public SearchDocumentEvent(Long queryUid)
  {
    super();
    setEventData(QUERY_UID, queryUid);
  }

  public SearchDocumentEvent(Long queryUid, IDataFilter filter)
  {
    super(filter);
    setEventData(QUERY_UID, queryUid);
  }

  public SearchDocumentEvent(Long queryUid, IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
    setEventData(QUERY_UID, queryUid);
  }

  public SearchDocumentEvent(Long queryUid, IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
    setEventData(QUERY_UID, queryUid);
  }

  public SearchDocumentEvent(Long queryUid, String listID, int maxRows, int startRow)
    throws EventException
  {
    super(listID, maxRows, startRow);
    setEventData(QUERY_UID, queryUid);
  }

  public Long getQueryUid()
  {
    return (Long)getEventData(QUERY_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/SearchDocumentEvent";
  }

}