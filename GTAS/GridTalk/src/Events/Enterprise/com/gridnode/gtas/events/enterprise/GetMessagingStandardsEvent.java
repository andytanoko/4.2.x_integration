/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMessagingStandardsEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 03 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.enterprise;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve the Messaging standards
 * available for selection in Search Registry.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class GetMessagingStandardsEvent
  extends    EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1291243335864123177L;

	public GetMessagingStandardsEvent()
  {
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetMessagingStandardsEvent";
  }

}