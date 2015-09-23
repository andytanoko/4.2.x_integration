/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ApproveGridNodeActivationEvent.java
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
 * This event contains the data for triggering the ApproveGridNodeActivation
 * action. An incoming activation request can be approved by providing the
 * UID of the ActivationRecord and the UIDs of the BusinessEntity(s) to trade
 * with the potential partner.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I6
 */
public class ApproveGridNodeActivationEvent
  extends    EventSupport implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5032429317407179559L;
	public static final String FEATURE  = "GRIDNODE";
  public static final String ACTION   = "ApproveGridNodeActivation";

  private static final String RECORD_UID  = "Record UID";
  private static final String EXCHANGE_BE = "Exchange Be UIDs";

  public ApproveGridNodeActivationEvent(
    Long recordUID,
    Collection exchangeBeUIDs)
    throws EventException
  {
    checkSetLong(RECORD_UID, recordUID);
    checkSetCollection(EXCHANGE_BE, exchangeBeUIDs, Long.class);
  }

  public Long getRecordUID()
  {
    return (Long)getEventData(RECORD_UID);
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
    return "java:comp/env/param/event/ApproveGridNodeActivationEvent";
  }

}