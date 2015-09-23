/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertLogger.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 31 2002    Srinath	             Created
 * Mar 03 2003    Neo Sok Lay          Add FacadeLogger for AlertManagerBean.
 */

package com.gridnode.pdip.app.alert.helpers;

import com.gridnode.pdip.framework.log.FacadeLogger;
import com.gridnode.pdip.framework.log.Log;

/**
 * This class provides static methods to log message and Throwable object that is used in the
 * Alert module.
 *
 * @author Srinath
 *
 */

public class AlertLogger
{
  /**
   * The category used by the Alert module.
   */
  private static final String CATEGORY = "APP.ALERT";

  private static final String[] MGR_FACADE =
  {
    CATEGORY + ".MGR",
    "AlertManagerBean",
  };

  private static final int FACADE_INDEX = 1;
  private static final int CATEGORY_INDEX    = 0;

  /**
   * Get the FacadeLogger for the AlertManagerBean.
   */
  public static FacadeLogger getManagerFacadeLogger()
  {
    return FacadeLogger.getLogger(
            MGR_FACADE[FACADE_INDEX], MGR_FACADE[CATEGORY_INDEX]);
  }


  /**
   * To log informative message with the invoked class name and method name.
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   *
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
   */
  public static void errorLog(String className, String methodName, String message, Throwable t)
  {
    Log.err(CATEGORY, formatMessage(className, methodName, message), t);
  }
  
  /**
   * To log error message with the invoked class name and method name.
   * @see com.gridnode.pdip.app.alert.exceptions.ILogErrorCodes
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
   * @see com.gridnode.pdip.app.alert.exceptions.ILogErrorCodes
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