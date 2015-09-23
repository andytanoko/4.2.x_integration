/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DenyGridNodeActivationEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "GRIDNODE"
 */
package com.gridnode.gtas.events.activation;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.Collection;

/**
 * This event contains the data necessary for triggering the DenyGridNodeActivation
 * action. An incoming activation request can be denied by providing the UID
 * of the ActivationRecord. Denying multiple activation requests is supported.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I6
 */
public class DenyGridNodeActivationEvent
  extends    EventSupport implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2551701323404262506L;
	public static final String FEATURE  = "GRIDNODE";
  public static final String ACTION   = "DenyGridNodeActivation";

  private static final String RECORD_UIDS  = "Record UIDs";

  public DenyGridNodeActivationEvent(
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
    return "java:comp/env/param/event/DenyGridNodeActivationEvent";
  }

}