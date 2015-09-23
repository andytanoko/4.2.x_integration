/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: StartBackendListenerEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 20 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.backend;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class will startup the GTAS backend listener.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class StartBackendListenerEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1693873939739504598L;


	public StartBackendListenerEvent()
  {
  }


  public String getEventName()
  {
    return "java:comp/env/param/event/StartBackendListenerEvent";
  }

}