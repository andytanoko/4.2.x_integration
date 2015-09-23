/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTNetworkSettingEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-31     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.connection.*;

public interface IGTNetworkSettingEntity extends IGTEntity
{
  //Constants for CONNECTION_LEVEL
  public static final Short CONNECTION_LEVEL_NO_FIREWALL        = new Short(INetworkSetting.LEVEL_NO_FIREWALL);
  public static final Short CONNECTION_LEVEL_FIREWALL_NO_PROXY  = new Short(INetworkSetting.LEVEL_FIREWALL_NO_PROXY);
  public static final Short CONNECTION_LEVEL_PROXY_NO_AUTH      = new Short(INetworkSetting.LEVEL_PROXY_NO_AUTH);
  public static final Short CONNECTION_LEVEL_PROXY_WITH_AUTH    = new Short(INetworkSetting.LEVEL_PROXY_WITH_AUTH);

  // Field ids
  public static final Number CONNECTION_LEVEL     = INetworkSetting.CONNECTION_LEVEL;
  public static final Number LOCAL_JMS_ROUTER     = INetworkSetting.LOCAL_JMS_ROUTER;
  public static final Number HTTP_PROXY_SERVER    = INetworkSetting.HTTP_PROXY_SERVER;
  public static final Number HTTP_PROXY_PORT      = INetworkSetting.HTTP_PROXY_PORT;
  public static final Number PROXY_AUTH_USER      = INetworkSetting.PROXY_AUTH_USER;
  public static final Number PROXY_AUTH_PASSWORD  = INetworkSetting.PROXY_AUTH_PASSWORD;
  public static final Number KEEP_ALIVE_INTERVAL  = INetworkSetting.KEEP_ALIVE_INTERVAL;
  public static final Number RESPONSE_TIMEOUT     = INetworkSetting.RESPONSE_TIMEOUT;
}