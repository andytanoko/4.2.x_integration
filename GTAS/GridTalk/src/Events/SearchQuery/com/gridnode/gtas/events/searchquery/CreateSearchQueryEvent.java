/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateSearchQueryEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.searchquery;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.List;

/**
 * This Event class contains the data for the creation of new SearchQuery.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class CreateSearchQueryEvent
  extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3388291517317449918L;
	public static final String QUERY_NAME = "Query Name";
  public static final String QUERY_DESC = "Query Desc";
  public static final String CREATED_BY = "Create By";
  public static final String CONDITIONS = "Conditions";
  public static final String IS_PUBLIC = "Is Public";

  public CreateSearchQueryEvent(String queryName, String queryDesc,
                                String createdBy, List conditions,
                                Boolean isPublic)
  {
    setEventData(QUERY_NAME, queryName);
    setEventData(QUERY_DESC, queryDesc);
    setEventData(CREATED_BY, createdBy);
    setEventData(CONDITIONS, conditions);
    setEventData(IS_PUBLIC, isPublic);
  }

  public String getQueryName()
  {
    return (String)getEventData(QUERY_NAME);
  }

  public String getQueryDesc()
  {
    return (String)getEventData(QUERY_DESC);
  }

  public String getCreatedBy()
  {
    return (String)getEventData(CREATED_BY);
  }

  public List getConditions()
  {
    return (List)getEventData(CONDITIONS);
  }

  public Boolean isPublic()
  {
    return (Boolean)getEventData(IS_PUBLIC);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/CreateSearchQueryEvent";
  }

}