/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SaveHousekeepingInfoEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2003      Mahesh         Created
 */ 
package com.gridnode.gtas.events.housekeeping;

import java.util.Map;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

public class SaveHousekeepingInfoEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8314238178081690085L;
	public static final String HOUSEKEEPING_INFO = "HousekeepingInfo";

  public SaveHousekeepingInfoEvent(Map housekeepingInfo)
     throws EventException
  {
    checkSetObject(HOUSEKEEPING_INFO, housekeepingInfo,Map.class);
  }

  public Map getHousekeepingInfo()
  {
    return (Map)getEventData(HOUSEKEEPING_INFO);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/SaveHousekeepingInfoEvent";
  }

}