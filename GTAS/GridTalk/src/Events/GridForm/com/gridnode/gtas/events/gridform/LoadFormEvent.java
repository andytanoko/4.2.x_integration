/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LoadFormEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    Jared Low           Created
 */
package com.gridnode.gtas.events.gridform;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * Event for loading a form object.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class LoadFormEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8603284922095221756L;
	public static final String XML_FILENAME = "XML Filename";

  public LoadFormEvent(String xmlFile)
  {
    setEventData(XML_FILENAME, xmlFile);
  }

  public String getXMLFilename()
  {
    return (String)getEventData(XML_FILENAME);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/LoadFormEvent";
  }
}