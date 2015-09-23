/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMyCompanyProfileEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 05 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.gridnode;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a My CompanyProfile.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetMyCompanyProfileEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7536736843448952124L;

	public GetMyCompanyProfileEvent()
    throws EventException
  {
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetMyCompanyProfileEvent";
  }


}