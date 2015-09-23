/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetActivationRecordEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.events.activation;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieving an ActivationRecord.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class GetActivationRecordEvent
  extends    EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3743338521490794195L;
	public static final String RECORD_UID    = "Record UID";

  public GetActivationRecordEvent(Long actRecordUID)
    throws EventException
  {
    checkSetLong(RECORD_UID, actRecordUID);
  }

  public Long getRecordUID()
  {
    return (Long)getEventData(RECORD_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetActivationRecordEvent";
  }

}