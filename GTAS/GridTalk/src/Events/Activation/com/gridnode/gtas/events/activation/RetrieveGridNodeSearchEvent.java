/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RetrieveGridNodeSearchEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.activation;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieving search results for a
 * previously submitted GridNode search.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class RetrieveGridNodeSearchEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 620189913072265491L;
	public static final String SEARCH_ID     = "Search ID";

  public RetrieveGridNodeSearchEvent(Long searchID)
    throws EventException
  {
    checkSetLong(SEARCH_ID, searchID);
  }

  public Long getSearchID()
  {
    return (Long)getEventData(SEARCH_ID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/RetrieveGridNodeSearchEvent";
  }

}