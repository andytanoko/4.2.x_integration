/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetConnectionStatusEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 05 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.gridnode;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve the Connection Status of
 * a GridNode base on UID pr GridNode ID.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GetConnectionStatusEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6073522622998876692L;
	public static final String UID  = "Connection Status UID";
  public static final String GRIDNODE_ID = "GridNode ID";

  private boolean _getMyConnStatus = false;

  public GetConnectionStatusEvent(Long uid)
    throws EventException
  {
    checkSetLong(UID, uid);
  }

  public GetConnectionStatusEvent(String gnId)
    throws EventException
  {
    checkSetString(GRIDNODE_ID, gnId);
  }

  /**
   * Use for getting ConnectionStatus for my GridNode.
   */
  public GetConnectionStatusEvent()
  {
    _getMyConnStatus = true;
  }

  public String getGridNodeID()
  {
    return (String)getEventData(GRIDNODE_ID);
  }

  public Long getConnStatusUID()
  {
    return (Long)getEventData(UID);
  }

  public boolean isGetMyConnStatus()
  {
    return _getMyConnStatus;
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetConnectionStatusEvent";
  }

}