/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRfcEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.backend;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a Rfc based on
 * Uid.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetRfcEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5499793970491085003L;
	public static final String RFC_UID = "Rfc Uid";

  public GetRfcEvent(Long RfcUid)
  {
    setEventData(RFC_UID, RfcUid);
  }

  public Long getRfcUid()
  {
    return (Long)getEventData(RFC_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetRfcEvent";
  }

}