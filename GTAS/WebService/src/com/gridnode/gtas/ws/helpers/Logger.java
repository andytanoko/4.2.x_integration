/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: WebserviceLogger.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 7, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.ws.helpers;

import com.gridnode.pdip.framework.log.Log;

/**
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class Logger
{
  private static final String CATEGORY = "GTAS.WEBSERVICE";

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
   */
  public static void warnLog(String className, String methodName, String message, Throwable t)
  {
    Log.warn(CATEGORY, formatMessage(className, methodName, message), t);
  }
  
  
  /**
   * To log error message with the invoked class name and method name.
   * @see com.gridnode.pdip.base.transport.exceptions.ILogErrorCodes
   *
   * @param           className     the name of the class that is invoking this method.
   * @param           methodName    the name of the method that is invoking this method.
   * @param           message       the message to be logged.
   */
  public static void errorLog(String errorCode, String className, String methodName, String message)
  {
    Log.error(errorCode, CATEGORY, formatMessage(className, methodName, message));
  }

  /**
   * To log error message with the invoked class name and method name.
   * @see com.gridnode.pdip.base.transport.exceptions.ILogErrorCodes
   *
   * @param errorCode The error code
   * @param className The name of the class that is invoking this method.
   * @param methodName The name of the method that is invoking this method.
   * @param message The message to be logged.
   * @param t The <p>Throwable</p> object to be included in the log.
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
