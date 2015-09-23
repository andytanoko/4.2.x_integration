/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRegistrationInfoEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 10 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.registration;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
 
/**
 * This Event class contains the data for retrieve the Product RegistrationInfo.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetRegistrationInfoEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 829433710813448543L;

	public GetRegistrationInfoEvent()
  {
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetRegistrationInfoEvent";
  }


}