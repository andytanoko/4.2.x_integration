/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPartnerFunctionEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 11 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.partnerfunction;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a PartnerFunction based on
 * Uid.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetPartnerFunctionEvent extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7711966047337501168L;
	public static final String PARTNER_FUNCTION_UID = "PartnerFunction Uid";

  public GetPartnerFunctionEvent(Long PartnerFunctionUid)
  {
    setEventData(PARTNER_FUNCTION_UID, PartnerFunctionUid);
  }

  public Long getPartnerFunctionUid()
  {
    return (Long)getEventData(PARTNER_FUNCTION_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetPartnerFunctionEvent";
  }

}