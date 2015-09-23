/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PopulateFormEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    Jared Low           Created
 */
package com.gridnode.gtas.events.gridform;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * Event for populating a form object.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class OutputFormEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9029805337702750300L;

	public OutputFormEvent()
  {
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/OutputFormEvent";
  }
}
