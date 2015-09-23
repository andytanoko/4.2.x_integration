/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportEntitiesEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 08 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.exportconfig;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
 
/**
 * This Event class contains information for importing configuration data.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public class ImportEntitiesEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6497616773279201394L;
	public static final String CONFIG_FILENAME   = "Configuration Filename";
  public static final String OVERWRITE         = "Overwrite";

  public ImportEntitiesEvent(String configurationFilename, Boolean overwrite)
  {
    setEventData(CONFIG_FILENAME, configurationFilename);
    setEventData(OVERWRITE, overwrite);
  }

  public String getConfigurationFilename()
  {
    return (String)getEventData(CONFIG_FILENAME);
  }

  public boolean isOverwite()
  {
    if (getEventData(OVERWRITE) != null)
    {
      return ((Boolean)getEventData(OVERWRITE)).booleanValue();
    }
    return false;
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/ImportEntitiesEvent";
  }

}