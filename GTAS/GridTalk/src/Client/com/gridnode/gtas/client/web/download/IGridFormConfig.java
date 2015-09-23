/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPathConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 09 2002    Daniel D'Cotta      Created.
 * Jul 29 2002    Daniel D'Cotta      Moved from gtas-common.
 *
 */
package com.gridnode.gtas.client.web.download;

public interface IGridFormConfig
{
  public static final String CONFIG_NAME = "gridform";

  // Server variables
  public static final String SERVER_ADDRESS = "server.address";
  public static final String SERVER_PORT    = "server.port";
  public static final String SERVER_URL     = "server.url";
}