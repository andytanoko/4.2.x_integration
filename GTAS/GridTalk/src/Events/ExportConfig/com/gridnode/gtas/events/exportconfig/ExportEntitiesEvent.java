/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportEntitiesEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 08 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.exportconfig;

import com.gridnode.gtas.model.exportconfig.ConfigEntitiesContainer;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains information for exporting configuration data.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public class ExportEntitiesEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3373785833773404385L;
	public static final String ENTITIES_CONTAINER = "Entities Container";

  public ExportEntitiesEvent(ConfigEntitiesContainer configEntitiesContainer)
  {
    setEventData(ENTITIES_CONTAINER, configEntitiesContainer);
  }

  public ConfigEntitiesContainer getConfigEntitiesContainer()
  {
    return (ConfigEntitiesContainer)getEventData(ENTITIES_CONTAINER);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/ExportEntitiesEvent";
  }

}