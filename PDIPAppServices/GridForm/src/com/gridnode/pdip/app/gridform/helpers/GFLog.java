/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GFLog.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 */
package com.gridnode.pdip.app.gridform.helpers;

import com.gridnode.pdip.framework.log.Log;

/**
 * Logger for the GridForm module.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class GFLog
{
  private static final String CATEGORY = "APP.GRIDFORM";

  /**
   * Don't allow this class to be instantiated.
   *
   * @since 2.0
   */
  private GFLog()
  {
  }

  /**
   * Log a message with INFO priority.
   *
   * @param msg The message to be logged.
   * @since 2.0
   */
  public static void log(Object msg)
  {
    Log.log(CATEGORY, msg);
  }

  /**
   * Log a message with DEBUG priority.
   *
   * @param msg The message to be logged.
   * @since 2.0
   */
  public static void debug(Object msg)
  {
    Log.debug(CATEGORY, msg);
  }

  /**
   * Log a message and exception with DEBUG priority.
   *
   * @param msg The message to be logged.
   * @param ex The exception.
   * @since 2.0
   */
  public static void debug(String msg, Throwable ex)
  {
    Log.debug(CATEGORY, msg, ex);
  }

  /**
   * Log a message with ERROR priority.
   *
   * @param msg The message to be logged.
   * @since 2.0
   */
  public static void err(Object msg)
  {
    Log.err(CATEGORY, msg);
  }

  /**
   * Log a message and exception with ERROR priority.
   *
   * @param msg The message to be logged.
   * @since 2.0
   */
  public static void err(String msg, Throwable ex)
  {
    Log.err(CATEGORY, msg, ex);
  }
}