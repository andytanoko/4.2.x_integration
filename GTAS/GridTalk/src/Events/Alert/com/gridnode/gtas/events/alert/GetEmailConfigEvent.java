/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetEmailConfigEvent.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 1, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.events.alert;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
 
public class GetEmailConfigEvent  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6132004048592128253L;

	public GetEmailConfigEvent()
  {
    
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/GetEmailConfigEvent";
  }
}
