/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteDefinitionEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    Jared Low           Created.
 */
package com.gridnode.gtas.events.gridform;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * Event for deleting a definition entity.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class DeleteDefinitionEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -244326836331271422L;
	public static final String UID           = "UID";
  public static final String NAME          = "Name";

  public DeleteDefinitionEvent(Long uid)
  {
    setEventData(UID, uid);
  }

  public DeleteDefinitionEvent(String name)
  {
    setEventData(NAME, name);
  }

  public Long getUID()
  {
    return (Long)getEventData(UID);
  }

  public String getName()
  {
    return (String)getEventData(NAME);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteDefinitionEvent";
  }
}