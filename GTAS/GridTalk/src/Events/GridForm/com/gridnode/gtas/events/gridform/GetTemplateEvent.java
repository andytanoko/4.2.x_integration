/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetTemplateEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 29 2002    Daniel D'Cotta      Created
 */
package com.gridnode.gtas.events.gridform;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * This Event class contains the data for retrieve a GF Template based on
 * UID.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class GetTemplateEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 457469568069976370L;
	public static final String TEMPLATE_UID  = "UID";
  public static final String TEMPLATE_NAME = "Name";

  public GetTemplateEvent(Long templateUID)
  {
    setEventData(TEMPLATE_UID, templateUID);
  }

  public GetTemplateEvent(String templateName)
  {
    setEventData(TEMPLATE_NAME, templateName);
  }

  public Long getTemplateUID()
  {
    return (Long)getEventData(TEMPLATE_UID);
  }

  public String getTemplateName()
  {
    return (String)getEventData(TEMPLATE_NAME);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetTemplateEvent";
  }

}