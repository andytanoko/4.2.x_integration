/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateAlertEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.events.alert;

import java.util.Collection;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for updating a Alert.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateAlertEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4523169772345848350L;
	public static final String UID          = "UID";
  public static final String NAME         = "Name";
  public static final String DESCRIPTION  = "Description";
  public static final String TYPE         = "Type";
  public static final String ACTIONS      = "Actions";

  public UpdateAlertEvent(
    Long        uid,
    String      name,
    String      description,
    Long        type,
    Collection  actions)
    throws EventException
  {
    checkSetLong(UID, uid);
    checkSetString(NAME, name);
    checkSetString(DESCRIPTION, description);
    checkSetLong(TYPE, type);
//    checkSetCollection(ACTIONS, actions, Long.TYPE);
    setEventData(ACTIONS, actions);
  }

  public Long getUid()
  {
    return (Long)getEventData(UID);
  }

  public String getName()
  {
    return (String)getEventData(NAME);
  }

  public String getDescription()
  {
    return (String)getEventData(DESCRIPTION);
  }

  public Long getType()
  {
    return (Long)getEventData(TYPE);
  }

  public Collection getActions()
  {
    return (Collection)getEventData(ACTIONS);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateAlertEvent";
  }

}