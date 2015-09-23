/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetActionEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.events.alert;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieving Action based on
 * UID or Name
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class GetActionEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2019678527293733540L;
	public static final String UID  = "UID";
  public static final String NAME = "Name";

  public GetActionEvent(Long uid)
    throws EventException
  {
    checkSetLong(UID, uid);
  }

  public GetActionEvent(String name)
    throws EventException
  {
    checkSetString(NAME, name);
  }

  public Long getUid()
  {
    return (Long)getEventData(UID);
  }

  public String getName()
  {
    return (String)getEventData(NAME);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetActionEvent";
  }
}