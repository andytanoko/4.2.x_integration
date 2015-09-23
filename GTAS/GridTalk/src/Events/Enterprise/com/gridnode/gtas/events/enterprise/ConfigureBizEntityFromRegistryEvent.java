/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConfigureBizEntityFromRegistryEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 08 2003    Neo Sok Lay         Created
 * Sep 24 2003    Neo Sok Lay         Change SearchId to Long type.
 */
package com.gridnode.gtas.events.enterprise;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.Collection;

/**
 * This event contains the data required for configuring
 * searched business entities from registry as partner business
 * entities.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class ConfigureBizEntityFromRegistryEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9020822550768996987L;
	private static final String SEARCH_ID = "Search Id";
  private static final String UUIDS = "UUIDs";
  
  /**
   * Constructs a ConfigureBizEntityFromRegistryEvent.
   * 
   * @param searchId The SearchId of the registry search to configure from.
   * @param uuids UUIDs of the SearchedBusinessEntity(s) to configure into
   * GridTalk.
   * @throws EventException Invalid input parameters.
   */
  public ConfigureBizEntityFromRegistryEvent(Long searchId, Collection uuids)
    throws EventException
  {
    checkSetLong(SEARCH_ID, searchId);
    checkSetCollection(UUIDS, uuids, String.class);
  }
  
  /**
   * Gets the SearchId.
   * 
   * @return The SearchId parameter value.
   */
  public Long getSearchId()
  {
    return (Long)getEventData(SEARCH_ID);
  }
  
  /**
   * Gets the UUIDs.
   * 
   * @return The UUIDS parameter value.
   */
  public Collection getUuids()
  {
    return (Collection)getEventData(UUIDS);
  }
  
  /**
   * @see com.gridnode.pdip.framework.rpf.event.IEvent#getEventName()
   */
  public String getEventName()
  {
    return "java:comp/env/param/event/ConfigureBizEntityFromRegistryEvent";
  }

}
