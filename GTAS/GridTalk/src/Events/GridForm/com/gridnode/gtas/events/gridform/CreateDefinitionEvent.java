/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateDefinitionEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    Jared Low           Created.
 * Aug 13 2002    Daniel D'Cotta      Modified for new field meta info.
 */
package com.gridnode.gtas.events.gridform;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * Event for creating a definition entity.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class CreateDefinitionEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8629621448878309375L;
	public static final String NAME     = "Name";
  public static final String FILENAME = "Filename";
  public static final String TEMPLATE = "Template";

  public CreateDefinitionEvent(String name, String filename, Long template)
    throws EventException
  {
    checkSetString(NAME, name);
    checkSetString(FILENAME, filename);
    checkSetLong(TEMPLATE, template);
  }

  public String getName()
  {
    return (String)getEventData(NAME);
  }

  public String getFilename()
  {
    return (String)getEventData(FILENAME);
  }

  public Long getTemplate()
  {
    return (Long)getEventData(TEMPLATE);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreateDefinitionEvent";
  }
}