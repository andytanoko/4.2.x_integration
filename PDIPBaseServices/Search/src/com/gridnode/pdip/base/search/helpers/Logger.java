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
 * Dec 18 2002    H.Sushil            Created
 */
package com.gridnode.pdip.base.search.helpers;

import com.gridnode.pdip.framework.log.Log;

/**
 * This class handles the logging for the Search module.
 *
 * @author H.Sushil
 *
 * @version 1.0
 * @since 1.0
 */
public class Logger
{
  private static final String CATEGORY = "BASE.SEARCH";

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