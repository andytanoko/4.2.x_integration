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
 * Dec 27 2002   Girish R         Created
 */
package com.gridnode.pdip.app.xmldb.helpers;

import com.gridnode.pdip.framework.log.Log;

public class Logger
{
  private static final String PREFIX = "APP.XMLDB";

  public static void log(Object msg)
  {
    Log.log(PREFIX, msg);
  }

  public static void debug(Object msg)
  {
    Log.debug(PREFIX, msg);
  }

  public static void debug(String msg, Throwable ex)
  {
    Log.debug(PREFIX, msg, ex);
  }

  public static void err(Object msg)
  {
    Log.err(PREFIX, msg);
  }

  public static void err(String msg, Throwable ex)
  {
    Log.err(PREFIX, msg, ex);
  }
}