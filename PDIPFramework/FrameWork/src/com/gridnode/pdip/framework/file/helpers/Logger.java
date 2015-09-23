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
 * Jul 04 2002    Daniel D'Cotta      Created.
 * Jul 29 2002    Daniel D'Cotta      Moved from gtas-common.
 * Feb 07 2007		Alain Ah Ming				Deprecated err(...) method
 * 																		Added error(...) method with error code
 * 																		Added warn(...) method
 */
package com.gridnode.pdip.framework.file.helpers;

import com.gridnode.pdip.framework.log.Log;

/**
 * This class handles the logging for the File module.
 *
 * @author Daniel D'Cotta
 *
 * @version GT 4.0 VAN
 * @since 2.0
 */
public class Logger
{
  private static final String CATEGORY = "FILE";

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

  /**
   * Log a warning message with the specified message object
   * @param msg The message object
   */
  public static void warn(Object msg)
  {
  	Log.warn(CATEGORY, msg);
  }
  
  /**
   * Log a warning message with the specified message string and <code>Throwable</code> object
   * @param msg The warning message
   * @param t The Throwable object
   */
  public static void warn(String msg, Throwable t)
  {
  	Log.warn(CATEGORY, msg, t);
  }
  /**
   * @deprecated Use error(String, Object)
   * @see Logger#error(String, Object)
   * @param msg
   */
  public static void err(Object msg)
  {
    Log.err(CATEGORY, msg);
  }

  /**
   * Logs an error message with an error code
   * @param errorCode The error code
   * @param msg The error message
   */
  public static void error(String errorCode, Object msg)
  {
  	Log.error(errorCode, CATEGORY, msg);
  }
  
  /**
   * @deprecated Use error(String, String, Throwable)
   * @see Logger#error(String, String, Throwable)
   * @param msg
   * @param ex
   */
  public static void err(String msg, Throwable ex)
  {
    Log.err(CATEGORY, msg, ex);
  }
  
  /**
   * Log an error message with an error code and a <code>Throwable</code>
   * @param errorCode The error code
   * @param msg The error message
   * @param ex The related Throwable 
   */
  public static void error(String errorCode, String msg, Throwable ex)
  {
  	Log.error(errorCode, CATEGORY, msg, ex);
  }
}