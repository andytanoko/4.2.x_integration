/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityLogger.java
 *
 ****************************************************************************
 * Date            Author                  Changes
 ****************************************************************************
 * June 06 2002    Jagadeesh               Created
 * Feb 12 2007		Chong SoonFui				Deprecate err(...) 
 * 																		Add error(...) and warn(...)
 */

package com.gridnode.pdip.base.security.helpers;

import com.gridnode.pdip.framework.log.Log;

public class SecurityLogger
{

  private static final String CATEGORY = "BASE.SECURITY";

  public static void log(Object msg)
  {
    System.out.println(msg);
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
   * Log a warning message with the specified category
   * @param msg The warning message
   * 
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void warn(Object msg)
  {
    Log.warn(CATEGORY, msg);
  }

  /**
   * Log a warning message with the specified category and
   * <code>Throwable</code> object of origin
   * 
   * @param msg The warning message
   * @param ex The <code>Throwable</code> object of origin
   * 
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void warn(String msg, Throwable ex)
  {
    Log.warn(CATEGORY, msg, ex);
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
   * @deprecated Use error(String, String, Throwable)
   * @see Logger#error(String, String, Throwable)
   * @param msg
   * @param ex
   */
  public static void err(String msg, Throwable ex)
  {
    ex.printStackTrace();
    Log.err(CATEGORY, msg, ex);
  }

  /**
   * Log an error message with the specified error code and category
   * 
   * @see com.gridnode.pdip.base.data.exceptions.ILogErrorCodes
   * 
   * @param errorCode The error code
   * @param msg The error message
   * 
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void error(String errorCode, Object msg)
  {
    Log.error(errorCode, CATEGORY, msg);
  }

  /**
   * Log an error message with the specified error code, category and
   * <code>Throwable</code> object of origin
   * @see com.gridnode.pdip.base.data.exceptions.ILogErrorCodes
   * 
   * @param errorCode The error code
   * @param msg The error message
   * @param ex The <code>Throwable</code> object of origin
   * 
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void error(String errorCode, String msg, Throwable ex)
  {
    Log.error(errorCode, CATEGORY, msg, ex);
  }

 }