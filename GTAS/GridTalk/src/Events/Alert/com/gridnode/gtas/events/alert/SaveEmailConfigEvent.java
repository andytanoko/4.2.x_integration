/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SaveEmailConfigEvent.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 1, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.events.alert;

import java.util.Map;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

public class SaveEmailConfigEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1876915402556888307L;
	public static final String EMAIL_CONFIG = "EmailConfig";
  
  public SaveEmailConfigEvent(Map emailConfigMap) throws EventException
  {
    checkSetObject(EMAIL_CONFIG, emailConfigMap,Map.class);
  }

  public Map getEmailConfig()
  {
    return (Map)getEventData(EMAIL_CONFIG);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/SaveEmailConfigEvent";
  }  
}
