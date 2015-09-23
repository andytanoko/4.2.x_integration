/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetUserProcedureEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh             Created
 */
package com.gridnode.gtas.events.userprocedure;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;


public class GetUserProcedureEvent extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5354011518738964206L;
	public static final String UID = "UID";

  public GetUserProcedureEvent(Long uID)
    throws EventException
  {
      checkSetLong(UID, uID);
  }

  public Long getUID()
  {
    return (Long) getEventData(UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetUserProcedureEvent";
  }
}



