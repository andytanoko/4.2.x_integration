/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Logger.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 08, 2006   i00107              Created
 * Mar 05, 2007		Alain Ah Ming				Deprecated logError(String, Object[], String, Throwable)
 * 																		Added logError(String, Object[], String, String, Throwable)
 * 																		Added logWarn
 */

package com.gridnode.util.log;

import org.apache.log4j.MDC;


/**
 * @author i00107
 * This class provides simple functions for logging. 
 */
public class Logger
{  
  private static final String ENTRY     = "Enter";
  private static final String EXIT      = "Exit";

  private String _name;
  private org.apache.log4j.Logger _loggerImpl;

  /**
   * The MDC key in the Log4J output that refers to the GN error code for an ERROR message.
   */
  private static final String MDC_KEY_ERROR_CODE	= "GnErrorCode";
  
  Logger(String name, org.apache.log4j.Logger loggerImpl)
  {
    _loggerImpl = loggerImpl;
    _name = name;
  }

  /**
   * Log the Entry to a method in the facade.
   *
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   */
  public void logEntry(String method, Object[] params)
  {
    debugMessage(method, params, ENTRY);
  }

  /**
   * Log the Exit from a method in the facade.
   *
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   */
  public void logExit(String method, Object[] params)
  {
    debugMessage(method, params, EXIT);
  }

  /**
   * Log a message from a method in the facade.
   *
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   * @param message The message to log.
   */
  public void logMessage(String method, Object[] params, String message)
  {
    if (_loggerImpl != null && _loggerImpl.isInfoEnabled())
    {
      String prefix = getMsgPrefix(method, params);
      _loggerImpl.info(prefix+message);
    }
  }

  public void debugMessage(String method, Object[] params, String message)
  {
    if (_loggerImpl != null && _loggerImpl.isDebugEnabled())
    {
      String prefix = getMsgPrefix(method, params);
      _loggerImpl.debug(prefix+message);
    }
  }

  /**
   * Log a warning message
   * 
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   * @param warnMessage The message if it is an Application exception.
   * @param t The error that occurred.
   */
  public void logWarn(String method, Object[] params, String warnMessage, Throwable t)
  {
    if (_loggerImpl != null)
    {
      String prefix = getMsgPrefix(method, params);
      _loggerImpl.warn(prefix+warnMessage, t);
      MDC.remove(MDC_KEY_ERROR_CODE);
    }  	
  }
  
  /**
   * Logs an error.
   * @deprecated Use logError(String, String, Object[], String, Throwable)
   * @param method The method name.
   * @param params The parameters passed into the method. Can be <b>null</b>
   * if no parameters required.
   * @param appErrorMsg The message if it is an Application exception.
   * @param t The error that occurred.
   * @throws SystemException If the error that occurred is not an Application
   * exception.
   */
  public void logError(
    String method, Object[] params, String errorMsg, Throwable t)
  {
  	logError(null, method, params, errorMsg, t);
  }
 
 /**
  * Logs an error.
  * 
  * @param errorCode The error code.
  * @param method The method name.
  * @param params The parameters passed into the method. Can be <b>null</b>
  * if no parameters required.
  * @param errorMsg The message if it is an Application exception.
  * @param t The error that occurred.
  * @throws SystemException If the error that occurred is not an Application
  * exception.
  */
  public void logError(String errorCode, String method, Object[] params, 
                       String errorMsg, Throwable t)
  {
    if (_loggerImpl != null)
    {
    	if(errorCode != null && !errorCode.trim().equals(""))
    	{
    		MDC.put(MDC_KEY_ERROR_CODE, errorCode);
    	}
      String prefix = getMsgPrefix(method, params);
      _loggerImpl.error(prefix+errorMsg, t);
      MDC.remove(MDC_KEY_ERROR_CODE);
    }  	
  }

  private String getMsgPrefix(String method, Object[] params)
  {
    StringBuffer msg = new StringBuffer("[").append(_name).append('.').append(method).append('(');
    if (params != null)
    {
      for (int i=0; i<params.length; i++)
      {
        msg.append(getFormattedParam(params[i]));
        if (i+1 <params.length)
          msg.append(',');
      }
    }
    msg.append(")] ");

    return msg.toString();
  }


  //for formatting non-simple objects in future
  private Object getFormattedParam(Object param)
  {
    if (param instanceof Object[])
    {
      Object[] array = (Object[])param;
      StringBuffer buff = new StringBuffer("[");
      for (int i=0; i<array.length; i++)
      {
        buff.append(array[i]);
        if (i<array.length-1)
        {
          buff.append(',');
        }
      }
      buff.append("]");
      return buff.toString();
    }
    return param;
  }
  
  
}
