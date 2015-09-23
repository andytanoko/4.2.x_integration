/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetDefinitionEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 29 2002    Daniel D'Cotta      Created
 */
package com.gridnode.gtas.events.gridform;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a GF Definition based on
 * UID.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class GetDefinitionEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2261480597476929254L;
	public static final String DEFINITION_UID  = "UID";
  public static final String DEFINITION_NAME = "Name";

  public GetDefinitionEvent(Long definitionUID)
  {
    setEventData(DEFINITION_UID, definitionUID);
  }

  public GetDefinitionEvent(String definitionName)
  {
    setEventData(DEFINITION_NAME, definitionName);
  }

  public Long getDefinitionUID()
  {
    return (Long)getEventData(DEFINITION_UID);
  }

  public String getDefinitionName()
  {
    return (String)getEventData(DEFINITION_NAME);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetDefinitionEvent";
  }

}