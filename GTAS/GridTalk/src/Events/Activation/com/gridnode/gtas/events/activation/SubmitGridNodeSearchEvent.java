/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SubmitGridNodeSearchEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.activation;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Map;

/**
 * This Event class contains the data for submitting a search for GridNodes
 * to the GridMaster.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class SubmitGridNodeSearchEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2677140548198886849L;
	public static final String SEARCH_CRITERIA     = "Search Criteria";

  public SubmitGridNodeSearchEvent(Map searchCriteria)
    throws EventException
  {
    checkSetObject(SEARCH_CRITERIA, searchCriteria, Map.class);
  }

  public Map getSearchCriteria()
  {
    return (Map)getEventData(SEARCH_CRITERIA);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/SubmitGridNodeSearchEvent";
  }

}