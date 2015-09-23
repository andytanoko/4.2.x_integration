/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NetworkSettingEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.helpers;

import com.gridnode.gtas.server.connection.model.AbstractXmlEntity;
import com.gridnode.gtas.server.connection.model.NetworkSetting;

/**
 * This EntityHandler handles the NetworkSetting entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class NetworkSettingEntityHandler
  extends ConnectionEntityHandler
{
  //private static final Object _lock = new Object();
  private static NetworkSettingEntityHandler _self = null;

  private int[] _keepAliveRange;
  private int[] _responseTimeoutRange;
  private int[] _proxyPortRange;

  private NetworkSettingEntityHandler() throws Throwable
  {
    super(NETWORK_SETTING_ENTITY);
  }

  /**
   * Get an instance of the NetworkSettingEntityHandler.
   */
  public static synchronized final NetworkSettingEntityHandler getInstance() throws Throwable
  {
    if (_self == null)
    {
      _self = new NetworkSettingEntityHandler();
    }
    return _self;
  }


  // *****************  Utility methods *******************************

  /**
   * Validate the fields in the specified NetworkSetting.
   *
   * @param setting The NetworkSetting to be validated.
   */
  protected void validate(AbstractXmlEntity entity)
    throws Throwable
  {
    NetworkSetting setting = (NetworkSetting)entity;

    checkConnectionLevel(setting);

    validateNumRange(setting.getKeepAliveInterval(),
      _keepAliveRange[MIN_INDEX], _keepAliveRange[MAX_INDEX],  "Bad KeepAlive Interval");

    validateNumRange(setting.getResponseTimeout(),
      _responseTimeoutRange[MIN_INDEX], _responseTimeoutRange[MAX_INDEX], "Bad Response Timeout");
  }

  /**
   * Check the fields based on Connection Level.
   *
   * @param setting The NetworkSetting to be validated.
   */
  private void checkConnectionLevel(NetworkSetting setting)
    throws Throwable
  {
    String err = "Bad Connection Level: "+setting.getConnectionLevel();
    int connLevel = getShort(setting.getConnectionLevel(), err);
    switch (connLevel)
    {
      case NetworkSetting.LEVEL_FIREWALL_NO_PROXY :
           setting.setLocalJmsRouter(null);
           setting.setHttpProxyPort(null);
           setting.setHttpProxyServer(null);
           setting.setProxyAuthPassword(null);
           setting.setProxyAuthUser(null);
           break;
      case NetworkSetting.LEVEL_NO_FIREWALL :
           validateString(setting.getLocalJmsRouter(), "Bad Local Jms Router");
           setting.setHttpProxyPort(null);
           setting.setHttpProxyServer(null);
           setting.setProxyAuthPassword(null);
           setting.setProxyAuthUser(null);
           break;
      case NetworkSetting.LEVEL_PROXY_NO_AUTH :
           validateString(setting.getHttpProxyServer(), "Bad Http Proxy Server");
           validateNumRange(setting.getHttpProxyPort(),
             _proxyPortRange[MIN_INDEX], _proxyPortRange[MAX_INDEX], "Bad Http Proxy Port");
           setting.setProxyAuthPassword(null);
           setting.setProxyAuthUser(null);
           setting.setLocalJmsRouter(null);
           break;
      case NetworkSetting.LEVEL_PROXY_WITH_AUTH :
           validateString(setting.getHttpProxyServer(), "Bad Http Proxy Server");
           validateNumRange(setting.getHttpProxyPort(),
             _proxyPortRange[MIN_INDEX], _proxyPortRange[MAX_INDEX], "Bad Http Proxy Port");
           validateString(setting.getProxyAuthPassword(), "Bad Proxy Auth Password");
           validateString(setting.getProxyAuthUser(), "Bad Proxy Auth User");
           setting.setLocalJmsRouter(null);
           break;
      default :
        throw new Exception(err);
    }
  }

  protected void readConfig() throws Throwable
  {
    _keepAliveRange = readRange(KEEP_ALIVE_RANGE);
    _responseTimeoutRange = readRange(RESPONSE_TIMEOUT_RANGE);
    _proxyPortRange = readRange(PROXY_PORT_RANGE);
  }

  protected AbstractXmlEntity getDefaultEntity()
  {
    return new NetworkSetting();
  }
}