/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetSearchQueryEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 06 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.searchquery;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a SearchQuery based on
 * UID.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */
public class GetSearchQueryEvent extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4930876213039548017L;
	public static final String QUERY_UID  = "SearchQuery UID";

  public GetSearchQueryEvent(Long queryUid)
  {
    setEventData(QUERY_UID, queryUid);
  }

  public Long getSearchQueryUID()
  {
    return (Long)getEventData(QUERY_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetSearchQueryEvent";
  }

}