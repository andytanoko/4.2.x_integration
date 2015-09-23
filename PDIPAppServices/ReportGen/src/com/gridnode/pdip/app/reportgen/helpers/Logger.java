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
 * Nov 11 2002    H.Sushil            Created
 */
package com.gridnode.pdip.app.reportgen.helpers;

import com.gridnode.pdip.framework.log.Log;

/**
 * This class handles the logging for the Report Generation module.
 *
 * @author H.Sushil
 *
 * @version 1.0
 * @since 1.0
 */
public class Logger
{
  private static final String CATEGORY = "APP.REPORTGEN";

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