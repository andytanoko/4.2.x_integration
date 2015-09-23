/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SubmitGridNodeActivationEvent.java
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
 * This event contains the data necessary for triggering the SubmitGridNodeActivation
 * action. An activation request can be submit by providing the ID and name of
 * the GridNode to activate, the reason for activation, and the UIDs of own
 * BusinessEntity(s) to trade with the potential partner.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I6
 */
public class SubmitGridNodeActivationEvent
  extends    EventSupport implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2645226123302723531L;
	public static final String FEATURE  = "GRIDNODE";
  public static final String ACTION   = "SubmitGridNodeActivation";

  private static final String GRIDNODE_ID = "GridNode ID";
  private static final String GRIDNODE_NAME = "GridNode Name";
  private static final String ACTIVATE_REASON = "Activate Reason";
  private static final String EXCHANGE_BE   = "Exchange Be UIDs";

  public SubmitGridNodeActivationEvent(
    Integer gridnodeID, String gridnodeName, String activateReason,
    Collection exchangeBeUIDs)
    throws EventException
  {
    checkSetInteger(GRIDNODE_ID, gridnodeID);
    checkSetString(GRIDNODE_NAME, gridnodeName);
    checkSetString(ACTIVATE_REASON, activateReason);
    checkSetCollection(EXCHANGE_BE, exchangeBeUIDs, Long.class);
  }

  public Integer getGridNodeID()
  {
    return (Integer)getEventData(GRIDNODE_ID);
  }

  public String getGridNodeName()
  {
    return (String)getEventData(GRIDNODE_NAME);
  }

  public String getActivateReason()
  {
    return (String)getEventData(ACTIVATE_REASON);
  }

  public Collection getExchangeBeUIDs()
  {
    return (Collection)getEventData(EXCHANGE_BE);
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
    return "java:comp/env/param/event/SubmitGridNodeActivationEvent";
  }

}