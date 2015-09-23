/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PublishBusinessEntityEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 * Sep 19 2003    Neo Sok Lay         Just reference to saved connection info
 *                                    by name instead of all information.
 */
package com.gridnode.gtas.events.enterprise;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.Collection;

/**
 * This event class contains the data required for initiating
 * a publish of BusinessEntity(s) to a given registry.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class PublishBusinessEntityEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5603950922765581280L;
	public static final String BE_UIDS = "BusinessEntity UIDs";
  public static final String CONNINFO_NAME = "RegistryConnectInfo Name";
  
  /**
   * Constructs a PublishBusinessEntityEvent.
   * 
   * @param beUIds Collection of UIDs of BusinessEntity(s)
   * @param connInfoName Name of the RegistryConnectInfo for the target registry.
   */
  public PublishBusinessEntityEvent(
    Collection beUIds, 
    String connInfoName) throws EventException
  {
    checkSetCollection(BE_UIDS, beUIds, Long.class);
    checkSetString(CONNINFO_NAME, connInfoName);    
  }  
  
  /**
   * Returns the UIDs of the BusinessEntity(s) to be published.
   * @return Collection of Long (UIDs).
   */
  public Collection getBeUIdS()
  {
    return (Collection)getEventData(BE_UIDS);
  }

  /**
   * Returns the name of the RegistryConnectInfo for the target registry.
   * @return Name of RegistryConnectInfo for the target registry.
   */
  public String getConnInfoName()
  {
    return (String)getEventData(CONNINFO_NAME);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/PublishBusinessEntityEvent";
  }
  
}
