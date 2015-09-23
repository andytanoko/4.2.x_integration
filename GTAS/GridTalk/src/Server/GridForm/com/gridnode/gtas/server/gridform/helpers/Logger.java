/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Logger.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Daniel D'Cotta      Created
 */
package com.gridnode.gtas.server.gridform.helpers;

import com.gridnode.pdip.framework.log.Log;

/**
 * This class handles the logging for the GridForm module.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.0
 * @since 2.0
 */
public class Logger
{
  private static final String CATEGORY = "GTAS.GF";

  public static void log(Object msg)
  {
    Log.log(CATEGORY, msg);
  }

  public static void debug(Object msg)
  {
    Log.debug(CATEGORY, msg);
  }

  public static void debug(String msg, Throwable ex)
  {
    Log.debug(CATEGORY, msg, ex);
  }

  public static void err(Object msg)
  {
    Log.err(CATEGORY, msg);
  }

  public static void err(String msg, Throwable ex)
  {
    Log.err(CATEGORY, msg, ex);
  }
}