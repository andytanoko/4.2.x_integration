/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateSearchQueryEvent.java
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
 * This Event class contains the data for the update of a SearchQuery.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class UpdateSearchQueryEvent
  extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2654136058849398194L;
	public static final String QUERY_UID  = "Query Uid";
  public static final String QUERY_DESC = "Query Desc";
  public static final String CONDITIONS = "Conditions";
  public static final String IS_PUBLIC = "Is Public";

  public UpdateSearchQueryEvent(Long queryUid, String queryDesc,
                                List conditions, Boolean isPublic)
  {
    setEventData(QUERY_UID, queryUid);
    setEventData(QUERY_DESC, queryDesc);
    setEventData(CONDITIONS, conditions);
    setEventData(IS_PUBLIC, isPublic);
  }

  public Long getQueryUid()
  {
    return (Long)getEventData(QUERY_UID);
  }

  public String getQueryDesc()
  {
    return (String)getEventData(QUERY_DESC);
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
    return "java:comp/env/param/event/UpdateSearchQueryEvent";
  }

}