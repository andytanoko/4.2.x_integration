/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.backend.helpers;

import com.gridnode.gtas.server.backend.openapi.net.APIListener;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;

public class BackendHelper
{
  protected static final String CONFIG_NAME           = "backend";
  protected static final String BACKEND_LISTENER_PORT = "gtas.backend.listener.port";

  public static int getBackendPort()
  {
    Configuration backendConfig = ConfigurationManager.getInstance().getConfig(CONFIG_NAME);
    int port = backendConfig.getInt(BACKEND_LISTENER_PORT);
    if (port == 0)
    {
      port = APIListener.DEFAULT_PORT;
    }
    return port;
  }

}