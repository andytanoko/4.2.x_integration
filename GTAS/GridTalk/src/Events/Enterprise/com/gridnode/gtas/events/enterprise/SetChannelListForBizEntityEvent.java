/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SetChannelListForBizEntityEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 02 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "ENTERPRISE"
 */
package com.gridnode.gtas.events.enterprise;

import com.gridnode.gtas.events.IGuardedEvent;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for updating the association of Channels
 * to a Business Entity.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I4
 */
public class SetChannelListForBizEntityEvent
  extends    EventSupport
  implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4681741892132912154L;
	public static final String GUARDED_FEATURE = "ENTERPRISE";
  public static final String GUARDED_ACTION  = "SetChannelListForBizEntity";

  public static final String BE_UID          = "BusinessEntity UID";
  public static final String CHANNEL_LIST    = "ChannelList";

  public SetChannelListForBizEntityEvent(Long beUID, Collection channelList)
    throws EventException
  {
    checkSetLong(BE_UID, beUID);
    checkSetCollection(CHANNEL_LIST, channelList, Long.class);
  }

  public Long getBizEntityUID()
  {
    return (Long)getEventData(BE_UID);
  }

  public Collection getChannelList()
  {
    return (Collection)getEventData(CHANNEL_LIST);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/SetChannelListForBizEntityEvent";
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