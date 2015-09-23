/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Logger.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Feb 23, 2004 			Mahesh             	Created
 * Feb 09 2007			Alain Ah Ming				  Deprecate all err(...) methods
 * 																				Create new error(...) methods with error code
 * 																				Create warn(...) methods for error log without code  
 */
package com.gridnode.pdip.base.appinterface.helpers;

import com.gridnode.pdip.framework.log.Log;

/**
 * 
 * @author Mahesh
 * @since
 * @version GT 4.0 VAN
 */
public class Logger
{
  private static final String CAT = "base.appinterface";

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
   * Log a warning message with the specified <code>Throwable</code> object
   * @param msg The warning message
   * @param t The <code>Throwable</code> object that resulted in the warning
   * 
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void warn(String msg, Throwable t)
  {
  	Log.warn(CAT, msg, t);
  }
  
  /**
   * @deprecated Use error(String, Object)
   * @see Logger#error(String, Object)
   * @param msg 
   */
  public static void err(Object msg)
  {
    Log.err(CAT, msg);
  }

  /**
   * Log an error message with the specified error code.
   * @param errorCode The error code
   * @param msg The error message
   * 
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void error(String errorCode, Object msg)
  {
  	Log.error(errorCode, CAT, msg);
  }
    
  /**
   * @deprecated Use error(String, String, Throwable)
   * @param msg
   * @param ex
   */
  public static void err(String msg, Throwable ex)
  {
    Log.err(CAT, msg, ex);
  }
  
  /**
   * Log an error message with the specified error code and <code>Throwable</code> object.
   * @param errorCode The error code
   * @param msg The error message
   * @param t The <code>Throwable</code> object that resulted in the error
   * 
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void error(String errorCode, String msg, Throwable t)
  {
  	Log.error(errorCode, CAT, msg, t);
  }
}