/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendBusinessEntityEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 21 2002    Neo Sok Lay         Created
 * Jan 07 2003    Neo Sok Lay         To allow send to GridNodes. Deprecate the
 *                                    send to Channel.
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "HIERARCHY"
 */
package com.gridnode.gtas.events.enterprise;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for sending BusinessEntity(s) to
 * partner enterprises.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I4
 */
public class SendBusinessEntityEvent
  extends    EventSupport
  implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4870682792988411521L;
	public static final String GUARDED_FEATURE = "ENTERPRISE";
  public static final String GUARDED_ACTION  = "SendBusinessEntity";

  public static final String BE_UIDS         = "BusinessEntity UIDs";

  public static final String GN_UIDS         = "GridNode UIDs";

  // Note: these are temporary for mock-up scenario
  // in actual implementation, user would select a number of partner
  // enterprises to send the business entities to, and no
  // channel would need to be specified.
  public static final String TO_ENTERPRISE_ID= "To Enterprise ID";
  public static final String VIA_CHANNEL_UID = "Via Channel UID";

  /**
   * @deprecated Replaced by SendBusinessEntityEvent(beUIDs, gnUIDs)
   */
  public SendBusinessEntityEvent(
    Collection beUIDs, String toEnterpriseID, Long viaChannelUID)
    throws EventException
  {
    checkSetCollection(BE_UIDS, beUIDs, Long.class);
    checkSetString(TO_ENTERPRISE_ID, toEnterpriseID);
    checkSetLong(VIA_CHANNEL_UID, viaChannelUID);
  }

  /**
   * Construct a SendBusinessEntityEvent to send multiple BusinessEntity(s)
   * to one or more partner GridNodes (Enterprise).
   *
   * @param beUIDs UIDs of BusinessEntity(s) to send.
   * @param partnerGnUIDs UIDs of partner GridNode(s) to send to.
   */
  public SendBusinessEntityEvent(
    Collection beUIDs, Collection parnterGnUIDs)
    throws EventException
  {
    checkSetCollection(BE_UIDS, beUIDs, Long.class);
    checkSetCollection(GN_UIDS, parnterGnUIDs, Long.class);
  }

  public Collection getBeUIDs()
  {
    return (Collection)getEventData(BE_UIDS);
  }

  public Collection getPartnerGnUIDs()
  {
    return (Collection)getEventData(GN_UIDS);
  }

  /**
   * @deprecated
   */
  public String getToEnterpriseID()
  {
    return (String)getEventData(TO_ENTERPRISE_ID);
  }

  /**
   * @deprecated
   */
  public Long getViaChannelUID()
  {
    return (Long)getEventData(VIA_CHANNEL_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/SendBusinessEntityEvent";
  }

  // ************* From IGuardedEvent *************************

  public String getGuardedFeature()
  {
    return GUARDED_FEATURE;
  }

  public String getGuardedAction()
  {
    return GUARDED_ACTION;
  }

}