/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateLogger
 *
 ****************************************************************************
 * Date            Author                  Changes
 ****************************************************************************
 * JUL 03 2002    Jagadeesh               Created
 * Feb 09 2007		Alain Ah Ming							Deprecated err(...) methods
 * 																					Added error(...) with error code
 * 																					Added warn(...) methods
 */
package com.gridnode.pdip.base.certificate.helpers;

//import com.gridnode.pdip.framework.log.Log;

/**
 * Logger class for Certificate Module
 * @author Jagadeesh
 * @since
 * @version GT 4.0 VAN
 */
public class CertificateLogger
{

  private static final String CATEGORY = "BASE.CERTIFICATE";

  public static void log(Object msg)
  {
    System.out.println(msg);
   // Log.log(CATEGORY, msg);
  }

  public static void debug(Object msg)
  {
    //Log.debug(CATEGORY, msg);
  }

  public static void debug(String msg, Throwable ex)
  {
    //Log.debug(CATEGORY, msg, ex);
  }

  /**
   * Log a warning message
   * @param msg The warning message
   */
  public static void warn(Object msg)
  {
    //Log.warn(CATEGORY, msg);
  }

  /**
   * Log a warning message with the specified <code>Throwable</code> object
   * @param msg The warning message
   * @param ex The <code>Throwable</code> object
   */
  public static void warn(String msg, Throwable ex)
  {
    //Log.warn(CATEGORY, msg, ex);
  }

  /**
   * @deprecated Use error(String, Object)
   * @see CertificateLogger#error(String, Object)
   * @param msg
   */
  public static void err(Object msg)
  {
    //Log.err(CATEGORY, msg);
  }

  /**
   * @deprecated Use err(String, String, Throwable)
   * @see CertificateLogger#error(String, String, Throwable)
   * @param msg
   * @param ex
   */
  public static void err(String msg, Throwable ex)
  {
    ex.printStackTrace();
    //Log.err(CATEGORY, msg, ex);
  }

  /**
   * Log an error message with the specified error code
   * @param errorCode The error code
   * @param msg The error message
   */
  public static void error(String errorCode, Object msg)
  {
    //Log.error(errorCode, CATEGORY, msg);
  }

  /**
   * Log an error message with the specified error code and 
   * the <code>Throwable </code> object of origin
   * 
   * @param errorCode The error code
   * @param msg The error message
   * @param ex The <code>Throwable</code> object of origin
   */
  public static void error(String errorCode, String msg, Throwable ex)
  {
    //Log.error(errorCode, CATEGORY, msg, ex);
  }
}