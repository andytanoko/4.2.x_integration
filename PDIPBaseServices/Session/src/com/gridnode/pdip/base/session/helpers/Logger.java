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
 * May 14 2002    Ooi Hui Linn         Created
 * Feb 12 2007		Chong SoonFui				Deprecate err(...) 
 * 																		Add error(...) and warn(...)
 */
package com.gridnode.pdip.base.session.helpers;

import com.gridnode.pdip.framework.log.Log;

/**
 * This class handles the logging for the Session Management module.
 *
 * @author Ooi Hui Linn
 *
 * @version GT 4.0 VAN
 * @since 2.0
 */
public class Logger
{
  private static final String CAT = "BASE.SESSION";

  public static void log(Object msg)
  {
    Log.log(CAT, msg);
  }

  public static void debug(Object msg)
  {
    Log.debug(CAT, msg);
  }

  public static void debug(String msg, Throwable ex)
  {
    Log.debug(CAT, msg, ex);
  }

  /**
   * Log a warning message
   * @param msg The warning message
   * 
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void warn(Object msg)
  {
    Log.warn(CAT, msg);
  }

  /**
   * Log a warning message with the specified <code>Throwable</code>
   * object of origin
   * 
   * @param msg The warning message
   * @param ex The <code>Throwable</code> object of origin
   * 
   * @since GT 4.0 VAN
   * @since GT 4.0 VAN
   */
  public static void warn(String msg, Throwable ex)
  {
    Log.warn(CAT, msg, ex);
  }

  /**
   * @deprecated Use error(String,String)
   * @param msg
   */
  public static void err(Object msg)
  {
    Log.err(CAT, msg);
  }

  /**
   * @deprecated Use error(String,String,Throwable)
   * @param msg
   * @param ex
   */
  public static void err(String msg, Throwable ex)
  {
    Log.err(CAT, msg, ex);
  }

  /**
   * Log an error message with the specified error code
   * @see com.gridnode.pdip.base.session.exceptions.ILogErrorCodes
   * 
   * @param errorCode
   * @param msg
   */
  public static void error(String errorCode, Object msg)
  {
    Log.error(errorCode, CAT, msg);
  }

  /**
   * Log an error message with the specified error code and
   * <code>Throwable</code> object of origin
   * @see com.gridnode.pdip.base.session.exceptions.ILogErrorCodes
   * 
   * @param errorCode The error code
   * @param msg The error message
   * @param t The <code>Throwable</code> object of origin
   */
  public static void error(String errorCode, String msg, Throwable t)
  {
    Log.error(errorCode, CAT, msg, t);
  }
}