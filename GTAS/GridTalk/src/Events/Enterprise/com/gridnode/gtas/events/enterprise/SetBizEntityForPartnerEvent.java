/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SetBizEntityForPartnerEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 02 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "PARTNER"
 */
package com.gridnode.gtas.events.enterprise;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This Event class contains the data for updating the association of Business
 * Entiy and Channel to a Partner.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I4
 */
public class SetBizEntityForPartnerEvent
  extends    EventSupport
  implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -994562550639913970L;
	public static final String GUARDED_FEATURE = "PARTNER";
  public static final String GUARDED_ACTION  = "SetBizEntityForPartner";

  public static final String PARTNER_UID     = "Partner UID";
  public static final String CHANNEL_UID     = "Channel UID";
  public static final String BE_UID          = "BusinessEntity UID";

  public SetBizEntityForPartnerEvent(Long partnerUID, Long beUID, Long channelUID)
    throws EventException
  {
    checkSetLong(PARTNER_UID, partnerUID);
    if (beUID != null)
    {
      checkSetLong(BE_UID, beUID);
      checkSetLong(CHANNEL_UID, channelUID);
    }
    else
    {
      setEventData(BE_UID, null);
      setEventData(CHANNEL_UID, null);
    }
  }

  public Long getPartnerUID()
  {
    return (Long)getEventData(PARTNER_UID);
  }

  public Long getBizEntityUID()
  {
    return (Long)getEventData(BE_UID);
  }

  public Long getChannelUID()
  {
    return (Long)getEventData(CHANNEL_UID);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/SetBizEntityForPartnerEvent";
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