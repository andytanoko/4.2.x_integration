/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetBizEntityForPartnerEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 02 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Remove implementation for IGuardedEvent
 */
package com.gridnode.gtas.events.enterprise;

//import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for retrieve a the BusinessEntity and
 * Channel that a Partner is associated with.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I4
 */
public class GetBizEntityForPartnerEvent
  extends    EventSupport
//  implements IGuardedEvent
{
//  public static final String GUARDED_FEATURE = "ENTERPRISE";
//  public static final String GUARDED_ACTION  = "GetBizEntityForPartner";

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9170628381045351171L;
	public static final String PARTNER_UID     = "Partner UID";

  public GetBizEntityForPartnerEvent(Long partnerUID)
    throws EventException
  {
    checkSetLong(PARTNER_UID, partnerUID);
  }

  public Long getPartnerUID()
  {
    return (Long)getEventData(PARTNER_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetBizEntityForPartnerEvent";
  }

  // ************* From IGuardedEvent *************************

//  public String getGuardedFeature()
//  {
//    return GUARDED_FEATURE;
//  }
//
//  public String getGuardedAction()
//  {
//    return GUARDED_ACTION;
//  }

}