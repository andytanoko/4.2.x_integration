/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConfigHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 11 2003    Koh Han Sing        Create
 */
package com.gridnode.gtas.server.registration.helpers;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;

public class ConfigHelper
{
  private static ConfigHelper _self = null;
  private static Object _lock = new Object();

  private static final String CONFIG_NAME = "registration";
  private static final String WARNING_PERIOD = "license.expiry.warning.period";
  private Configuration config;

  private ConfigHelper()
  {
    config = ConfigurationManager.getInstance().getConfig(CONFIG_NAME);
  }

  public static ConfigHelper getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
        {
          _self = new ConfigHelper();
        }
      }
    }
    return _self;
  }

  public int expiryWarningPeriod()
  {
    return config.getInt(WARNING_PERIOD);
  }
}