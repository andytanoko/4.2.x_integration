/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbortGridNodeActivationEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 14 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "GRIDNODE"
 */
package com.gridnode.gtas.events.activation;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.Collection;

/**
 * This event contains the necessary data for triggering an AbortGridNodeActivation
 * action. An Activation request that has not been responded can be aborted (cancelled)
 * by providing the UID of the ActivationRecord for the request. Aborts of
 * multiple Activation requests is supported.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I6
 */
public class AbortGridNodeActivationEvent
  extends    EventSupport implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8473816377946600694L;
	public static final String FEATURE  = "GRIDNODE";
  public static final String ACTION   = "AbortGridNodeActivation";

  private static final String RECORD_UIDS  = "Record UID";

  public AbortGridNodeActivationEvent(
    Collection recordUIDs)
    throws EventException
  {
    checkSetCollection(RECORD_UIDS, recordUIDs, Long.class);
  }

  public Collection getRecordUIDs()
  {
    return (Collection)getEventData(RECORD_UIDS);
  }

  // *************************** Implements IGuardedEvent *********************

  public String getGuardedFeature()
  {
    return FEATURE;
  }
  public String getGuardedAction()
  {
    return ACTION;
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/AbortGridNodeActivationEvent";
  }

}