/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateActionEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.events.alert;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
 
/**
 * This Event class contains the data for the creation of new Action.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class CreateActionEvent
  extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 915512452082138051L;
	public static final String NAME             = "Name";
  public static final String DESCRIPTION      = "Description";
  public static final String MESSAGE_TEMPLATE = "Message Template";

  public CreateActionEvent(
    String      name,
    String      description,
    Long        messageTemplate)
    throws EventException
  {
    checkSetString(NAME, name);
    checkSetString(DESCRIPTION, description);
    checkSetLong(MESSAGE_TEMPLATE, messageTemplate);
  }

  public String getName()
  {
    return (String)getEventData(NAME);
  }

  public String getDescription()
  {
    return (String)getEventData(DESCRIPTION);
  }

  public Long getMessageTemplate()
  {
    return (Long)getEventData(MESSAGE_TEMPLATE);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreateActionEvent";
  }
}