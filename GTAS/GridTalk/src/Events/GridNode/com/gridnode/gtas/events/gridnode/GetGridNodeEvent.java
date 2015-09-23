/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGridNodeEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 28 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.gridnode;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a GridNode based on
 * UID.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GetGridNodeEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2588938653060127403L;
	public static final String UID  = "GridNode UID";

  public GetGridNodeEvent(Long uid)
    throws EventException
  {
    checkSetLong(UID, uid);
  }

  public Long getGridNodeUID()
  {
    return (Long)getEventData(UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetGridNodeEvent";
  }

}