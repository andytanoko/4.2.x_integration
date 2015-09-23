/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SaveNetworkSettingEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2002    Neo Sok Lay         Created
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "SYSTEM"
 */
package com.gridnode.gtas.events.connection;

import java.util.Map;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.gtas.events.IGuardedEvent;

/**
 * This Event class contains the data for retrieve the NetworkSetting.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0 I6
 */
public class SaveNetworkSettingEvent
  extends    EventSupport
  implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4982484525176917452L;
	private static final String NETWORK_SETTING = "Network Setting";
  public static final String GUARDED_ACTION   = "SaveNetworkSetting";
  public static final String GUARDED_FEATURE  = "SYSTEM";

  public SaveNetworkSettingEvent(Map networkSetting)
    throws EventException
  {
    checkSetObject(NETWORK_SETTING, networkSetting, Map.class);
  }

  public Map getNetworkSetting()
  {
    return (Map)getEventData(NETWORK_SETTING);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/SaveNetworkSettingEvent";
  }

  public String getGuardedFeature()
  {
    return GUARDED_FEATURE;
  }

  public String getGuardedAction()
  {
    return GUARDED_ACTION;
  }
}