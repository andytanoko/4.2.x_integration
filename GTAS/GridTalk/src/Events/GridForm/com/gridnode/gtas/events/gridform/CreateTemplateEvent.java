/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateTemplateEvent.java
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
 * Event for creating a template entity.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class CreateTemplateEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 651978219046945204L;
	public static final String NAME     = "Name";
  public static final String FILENAME = "Filename";

  public CreateTemplateEvent(String name, String filename)
    throws EventException
  {
    checkSetString(NAME, name);
    checkSetString(FILENAME, filename);
  }

  public String getName()
  {
    return (String)getEventData(NAME);
  }

  public String getFilename()
  {
    return (String)getEventData(FILENAME);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreateTemplateEvent";
  }
}