/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SubmitRegistrySearchEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.enterprise;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Map;

/**
 * This Event class contains the data for submitting a search for BusinessEntities
 * to a public registry.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class SubmitRegistrySearchEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1334523308389553368L;
	public static final String SEARCH_CRITERIA     = "Search Criteria";

  /**
   * Constructs a SubmitRegistrySearchEvent
   * 
   * @param searchCriteria Map of SearchRegistryCriteria fields.
   * 
   * @throws EventException Invalid searchCriteria object specified.
   */
  public SubmitRegistrySearchEvent(Map searchCriteria)
    throws EventException
  {
    checkSetObject(SEARCH_CRITERIA, searchCriteria, Map.class);
  }

  /**
   * Gets the search criteria
   * 
   * @return Map of SearchRegistryCriteria fields.
   */
  public Map getSearchCriteria()
  {
    return (Map)getEventData(SEARCH_CRITERIA);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/SubmitRegistrySearchEvent";
  }

}