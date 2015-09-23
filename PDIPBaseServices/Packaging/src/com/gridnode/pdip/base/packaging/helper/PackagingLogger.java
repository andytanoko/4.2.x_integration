/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PackagingLogger.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * OCT 16 2002    Jagadeesh             Created
 * Feb 12 2007    Chong SoonFui				Deprecate errorLog(...)
 *                                    Add warnLog(...) and errorLog(errorCode, ...)
 */


package com.gridnode.pdip.base.packaging.helper;

import com.gridnode.pdip.framework.log.Log;

public class PackagingLogger
{
  /**
   * Category of the Packaging Module.
   */
  private static final String CATEGORY = "BASE.PACKAGING";

  public PackagingLogger()
  {
  }

  /**
   * To log informative message with the invoked class name and method name.
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   *
   * @since 2.0
   * @version 2.0
   */
  public static void infoLog(String className, String methodName, String message)
  {
    Log.log(CATEGORY, formatMessage(className, methodName, message));
  }

  /**
   * To log informative message with the invoked class name and method name.
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   * @param           t             the <p>Throwable</p> object to be included in the log.
   *
   * @since 2.0
   * @version 2.0
   */
  public static void infoLog(String className, String methodName, String message, Throwable t)
  {
    Log.log(CATEGORY, formatMessage(className, methodName, message), t);
  }

  /**
   * To log debugging message with the invoked class name and method name.
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   *
   * @since 2.0
   * @version 2.0
   */
  public static void debugLog(String className, String methodName, String message)
  {
    Log.debug(CATEGORY, formatMessage(className, methodName, message));
  }

  /**
   * To log debugging message with the invoked class name and method name.
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   * @param           t             the <p>Throwable</p> object to be included in the log.
   *
   * @since 2.0
   * @version 2.0
   */
  public static void debugLog(String className, String methodName, String message, Throwable t)
  {
    Log.debug(CATEGORY, formatMessage(className, methodName, message), t);
  }
  
  /**
   * To log warning message with the invoked class name and method name.
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   *
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void warnLog(String className, String methodName, String message)
  {
    Log.warn(CATEGORY, formatMessage(className, methodName, message));
  }

  /**
   * To log warning message with the invoked class name and method name.
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   * @param           t             the <p>Throwable</p> object to be included in the log.
   *
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void warnLog(String className, String methodName, String message, Throwable t)
  {
    Log.warn(CATEGORY, formatMessage(className, methodName, message), t);
  }

  /**
   * @deprecated Use error(String,String,String,String)
   * To log error message with the invoked class name and method name.
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   *
   * @since 2.0
   * @version 2.0
   */
  public static void errorLog(String className, String methodName, String message)
  {
    Log.err(CATEGORY, formatMessage(className, methodName, message));
  }

  /**
   * @deprecated Use error(String,String,String,String,Throwable)
   * To log error message with the invoked class name and method name.
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   * @param           t             the <p>Throwable</p> object to be included in the log.
   *
   * @since 2.0
   * @version 2.0
   */
  public static void errorLog(String className, String methodName, String message, Throwable t)
  {
    Log.err(CATEGORY, formatMessage(className, methodName, message), t);
  }
  
  /**
   * To log error message with the invoked class name and method name.
   * @see com.gridnode.pdip.base.packaging.exceptions.ILogErrorCodes
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   *
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void errorLog(String errorCode, String className, String methodName, String message)
  {
    Log.error(errorCode, CATEGORY, formatMessage(className, methodName, message));
  }

  /**
   * To log error message with the invoked class name and method name.
   * @see com.gridnode.pdip.base.packaging.exceptions.ILogErrorCodes
   *
   * @param errorCode The error code
   * @param className The name of the class that is invoking this method.
   * @param methodName The name of the method that is invoking this method.
   * @param message The message to be logged.
   * @param t The <p>Throwable</p> object to be included in the log.
   *
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void errorLog(String errorCode, String className, String methodName, String message, Throwable t)
  {
    Log.error(errorCode, CATEGORY, formatMessage(className, methodName, message), t);
  }

  /**
   * To format the message with the class name and the method name.
   *
   * @param errorCode The error code
   * @param className The name of the class that is invoking this method.
   * @param methodName The name of the method that is invoking this method.
   * @param message The message to be logged.
   *
   * @return the formatted message to be logged.
   */
  private static String formatMessage(String className, String methodName, String message)
  {
    StringBuffer buff = new StringBuffer();
    buff.append("[");
    buff.append(className);
    buff.append(".");
    buff.append(methodName);
    buff.append("]");
    buff.append(" ");
    buff.append(message);
    return buff.toString();
  }



}





