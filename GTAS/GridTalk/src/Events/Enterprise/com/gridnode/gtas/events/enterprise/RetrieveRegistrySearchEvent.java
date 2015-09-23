/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RetrieveRegistrySearchEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 03 2003    Neo Sok Lay         Created
 * Sep 24 2003    Neo Sok Lay         Change SearchId to Long type.
 */
package com.gridnode.gtas.events.enterprise;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieving search results for a
 * previously submitted Registry search.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class RetrieveRegistrySearchEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -347081757426906877L;
	public static final String SEARCH_ID     = "Search Id";

  /**
   * Constructs a RetrieveRegistrySearchEvent.
   * 
   * @param searchId SearchId of the submitted search request.
   * @throws EventException No SearchId specified.
   */
  public RetrieveRegistrySearchEvent(Long searchId)
    throws EventException
  {
    checkSetLong(SEARCH_ID, searchId);
  }

  /**
   * Gets the search id that identifies the search request.
   * 
   * @return Search Id of the search request.
   */
  public Long getSearchId()
  {
    return (Long)getEventData(SEARCH_ID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/RetrieveRegistrySearchEvent";
  }

}