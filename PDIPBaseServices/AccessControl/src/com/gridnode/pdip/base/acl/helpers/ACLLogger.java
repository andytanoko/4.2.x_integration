/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ACLLogger.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 07 2002    Goh Kan Mun             Created
 * Feb 08 2007		Alain Ah Ming						Deprecated errorLog(...) without error code parameter.
 * 																				Added new errorLog(...) with error code parameter.
 * 																				Added warnLog(...) for reporting unhandled errors.
 */
package com.gridnode.pdip.base.acl.helpers;

import com.gridnode.pdip.framework.log.Log;

/**
 * This class provides static methods to log message and Throwable object that is used in the
 * ACL module.
 *
 * @author Goh Kan Mun
 *
 * @version GT 4.0 VAN
 * @since 2.0
 */

public class ACLLogger
{
  /**
   * The category used by the ACL module.
   */
  private static final String CATEGORY = "BASE.ACL";

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
   * Log a warning message with the specified class name, method name and message.
   * @param className The name of the class of origin of the warning.
   * @param methodName The name of the method of origin of the warning. 
   * @param message The warning message
   * 
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void warnLog(String className, String methodName, String message)
  {
  	Log.warn(CATEGORY, formatMessage(className, methodName, message));
  }
  
  /**
   * Log a warning message with the specified class name, method name, message and throwable.
   * @param className The name of the class of origin of the warning.
   * @param methodName The name of the method of origin of the warning. 
   * @param message The warning message
   * @param t The throwable resulting in the warning
   * 
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void warnLog(String className, String methodName, String message, Throwable t)
  {
  	Log.warn(CATEGORY, formatMessage(className, methodName, message),t);
  }
  
  /**
   * @deprecated Use errorLog(String errorCode, String className, String methodName, String message)
   * @see ACLLogger#errorLog(String, String, String, String)
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
   * To log error message with the invoked class name and method name.
   * @param errorCode		The error code for this error message
   * @param className		The name of the class that is invoking this method.
   * @param methodName	The name of the method that is invoking this method.
   * @param message			The message to be logged.
   *
   * @since GT 4.0 VAN
   * @version GT 4.0 VAN
   */
  public static void errorLog(String errorCode, String className, String methodName, String message)
  {
    Log.error(errorCode, CATEGORY, formatMessage(className, methodName, message));
  }

  /**
   * @deprecated Use errorLog(String errorCode, String className, String methodName, String message, Throwable t)
   * @see ACLLogger#errorLog(String, String, String, String, Throwable)
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
   * To log error message with the specified error code, class name and method name.
   * @param errorCode			The error code for this error message
   * @param className     the name of the class that is invoking this method.
   * @param methodName    the name of the method that is invoking this method.
   * @param message       the message to be logged.
   * @param t             the <p>Throwable</p> object to be included in the log.
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
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   *
   * @return          the formatted message to be logged.
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