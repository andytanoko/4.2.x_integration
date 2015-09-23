/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: INetworkSetting.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.model.connection;

/**
 * This interface defines the field IDs and constants for the NetworkSetting
 * entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface INetworkSetting
{
  /**
   * Entity name for NetworkSetting entity.
   */
  static final String ENTITY_NAME     = "NetworkSetting";

  /**
   * FieldID for ConnectionLevel. A Short.
   */
  static final Number CONNECTION_LEVEL     = new Integer(0);

  /**
   * FieldID for LocalJmsRouter. A String.
   */
  static final Number LOCAL_JMS_ROUTER   = new Integer(1); // String(50)

  /**
   * FieldID for HttpProxyServer. A String.
   */
  static final Number HTTP_PROXY_SERVER  = new Integer(2); // String(50)

  /**
   * FieldID for HttpProxyPort. An Integer.
   */
  static final Number HTTP_PROXY_PORT = new Integer(3);

  /**
   * FieldID for ProxyAuthUser. A String.
   */
  static final Number PROXY_AUTH_USER = new Integer(4); // String(50)

  /**
   * FieldID for ProxyAuthPassword. A String.
   */
  static final Number PROXY_AUTH_PASSWORD  = new Integer(5); // String(20)

  /**
   * FieldID for KeepAliveInterval. An Integer.
   */
  static final Number KEEP_ALIVE_INTERVAL  = new Integer(6);

  /**
   * FieldID for ResponseTimeout. An Integer.
   */
  static final Number RESPONSE_TIMEOUT  = new Integer(7);


  // values for CONNECTION_LEVEL
  /**
   * Possible value for CONNECTION_LEVEL.
   * This indicates that the host is not behind a firewall.
   */
  static final short LEVEL_NO_FIREWALL = 0;

  /**
   * Possible value for CONNECTION_LEVEL.
   * This indicates that the host is behind a firewall but not proxy restricted.
   */
  static final short LEVEL_FIREWALL_NO_PROXY = 1;

  /**
   * Possible value for CONNECTION_LEVEL.
   * This indicates that the host is behind a proxy restricted firewall but no
   * authentication is required.
   */
  static final short LEVEL_PROXY_NO_AUTH = 2;

  /**
   * Possible value for CONNECTION_LEVEL.
   * This indicates that the host is behind a proxy restricted firewall and
   * authentication is required.
   */
  static final short LEVEL_PROXY_WITH_AUTH = 3;


}