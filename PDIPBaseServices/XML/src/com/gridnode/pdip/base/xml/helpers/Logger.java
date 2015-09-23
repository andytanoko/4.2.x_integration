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
 * Jun 23 2003    Koh Han Sing        Created
 * Feb 13 2007		Chong SoonFui				Deprecate err(...) 
 * 																		Add error(...) and warn(...)
 */
package com.gridnode.pdip.base.xml.helpers;

import com.gridnode.pdip.framework.log.Log;

/**
 * This class handles the logging for the XML module.
 *
 * @author Koh Han Sing
 *
 * @version GT 4.0 VAN
 * @since 2.0
 */
public class Logger
{
  private static final String CATEGORY = "BASE.XML";

  public static void log(String className, Object msg)
  {
    Log.log(CATEGORY, className+" "+msg);
  }

  public static void debug(String className, Object msg)
  {
    Log.debug(CATEGORY, className+" "+msg);
  }

  public static void debug(String className, String msg, Throwable ex)
  {
    Log.debug(CATEGORY, className+" "+msg, ex);
  }

  /**
   * Log a warning message with the specified category
   * @param className The class name
   * @param msg The warning message
   * 
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void warn(String className, Object msg)
  {
    Log.warn(CATEGORY, className+" "+msg);
  }

  /**
   * Log a warning message with the specified category and
   * <code>Throwable</code> object of origin
   * 
   * @param className The class name
   * @param msg The warning message
   * @param ex The <code>Throwable</code> object of origin
   * 
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void warn(String className, String msg, Throwable ex)
  {
    Log.warn(CATEGORY, className+" "+msg, ex);
  }
  
  /**
   * @deprecated Use error(String, String, Object)
   * @see Logger#error(String, String, Object)
   * @param className
   * @param msg
   */
  public static void err(String className, Object msg)
  {
    Log.err(CATEGORY, className+" "+msg);
  }

  /**
   * @deprecated Use error(String, String, String, ex)
   * @see Logger#error(String, String, String, ex)
   * @param className
   * @param msg
   * @param ex
   */
  public static void err(String className, String msg, Throwable ex)
  {
    Log.err(CATEGORY, className+" "+msg, ex);
  }

  /**
   * Log an error message with the specified error code
   * @see com.gridnode.pdip.base.xml.exceptions.ILogErrorCodes
   * 
   * @param errorCode
   * @param msg
   */
  public static void error(String errorCode, String className, Object msg)
  {
    Log.error(errorCode, CATEGORY, className+" "+msg);
  }

  /**
   * Log an error message with the specified error code and
   * <code>Throwable</code> object of origin
   * @see com.gridnode.pdip.base.xml.exceptions.ILogErrorCodes
   * 
   * @param errorCode The error code
   * @param msg The error message
   * @param t The <code>Throwable</code> object of origin
   */
  public static void error(String errorCode, String className, String msg, Throwable t)
  {
    Log.error(errorCode, CATEGORY, className+" "+msg, t);
  }
  
//  public void log(String message)
//  {
//    Log.log(CATEGORY, message);
//  }
//
//  public void err(String message, Exception ex)
//  {
//    Log.err(CATEGORY, message, ex);
//  }
//
//  public void debug(String message)
//  {
//    Log.debug(CATEGORY, message);
//  }
//
}