/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRegistryConnectInfoEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 19 2003    Neo Sok Lay         Created
 * Sep 24 2003    Neo Sok Lay         Get by UID instead of Name.
 */
package com.gridnode.gtas.events.bizreg;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This event contains the data required to obtain a RegistryConnectInfo
 * entity from the server.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class GetRegistryConnectInfoEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7862804366389099918L;
	public static final String UID = "UID";
  
  /**
   * Constructs a GetRegistryConnectInfoEvent.
   * 
   * @param uid The UID of the RegistryConnectInfo to get.
   * 
   * @throws EventException Invalid input parameters.
   */
  public GetRegistryConnectInfoEvent(Long uid)
    throws EventException
  {
    checkSetLong(UID, uid);
  }
  
  public Long getUid()
  {
    return (Long)getEventData(UID);
  }
  
  /**
   * @see com.gridnode.pdip.framework.rpf.event.IEvent#getEventName()
   */
  public String getEventName()
  {
    return "java:comp/env/param/event/GetRegistryConnectInfoEvent";
  }
  
}
