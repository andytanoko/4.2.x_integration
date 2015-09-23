/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SubmitGridNodeDeactivationEvent.java
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
 * This event contains the data necessary for triggering the SubmitGridNodeDeactivation
 * activation. An active GridNode can be deactivated by providing the UID of the
 * GridNode. Deactivating of multiple active GridNodes is supported.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I6
 */
public class SubmitGridNodeDeactivationEvent
  extends    EventSupport implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4417150197410436982L;
	public static final String FEATURE  = "GRIDNODE";
  public static final String ACTION   = "SubmitGridNodeDeactivation";

  private static final String GRIDNODE_UIDS  = "GridNode UIDs";

  public SubmitGridNodeDeactivationEvent(
    Collection gridnodeUIDs)
    throws EventException
  {
    checkSetCollection(GRIDNODE_UIDS, gridnodeUIDs, Long.class);
  }

  public Collection getGridNodeUIDs()
  {
    return (Collection)getEventData(GRIDNODE_UIDS);
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
    return "java:comp/env/param/event/SubmitGridNodeDeactivationEvent";
  }

}