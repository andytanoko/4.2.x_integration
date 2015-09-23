/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IConnectionConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.helpers;

import com.gridnode.pdip.framework.config.IFrameworkConfig;

/**
 * This interface defines the constants for retrieving configurations for
 * the Connection module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public interface IConnectionConfig extends IFrameworkConfig
{
  /**
   * Name of the Connnection Configuration file.
   */
  static final String CONFIG_NAME = "connection";

  /**
   * Path key for storing entity files for the Connection module.
   */
  static final String PATH_CONNECTION = "connection.path.entity";

  /**
   * Filename for the NetworkSetting entity file.
   */
  static final String NETWORK_SETTING_ENTITY = "entity.network.setting";

  /**
   * Filename for the Connection Setup Properties file.
   */
  static final String SETUP_PROPERTIES_ENTITY= "entity.setup.properties";

  /**
   * Filename for the Connection Properties file.
   */
  static final String CONN_PROPERTIES_ENTITY = "entity.conn.properties";

  /**
   * Range setting for the KeepAliveInterval in NetworkSetting.
   */
  static final String KEEP_ALIVE_RANGE = "keep.alive.range";

  /**
   * Range setting for ResponseTimeout in NetworkSetting.
   */
  static final String RESPONSE_TIMEOUT_RANGE = "response.timeout.range";

  /**
   * Range setting for HttpProxyPort in NetworkSetting.
   */
  static final String PROXY_PORT_RANGE  = "proxy.port.range";

  /**
   * Filename for the ConnectionSetupResult entity file.
   */
  static final String CONNECTION_SETUP_ENTITY  = "entity.connection.setup";

  /**
   * Partial key for Servicing router in Connection setup.
   */
  static final String SERVICING_ROUTER = "servicing.router.";

}