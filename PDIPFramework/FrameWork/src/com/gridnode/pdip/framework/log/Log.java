/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceLocator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *
 * May 14 2002    Ang Meng Hua        Tidy up to conform to coding std.
 *                                    Added super category GN
 *
 * Jul 30 2002    Jagadeesh           Modify all reference of ConfigManager to
 *                                    use new ConfigurationManager.Restructur
 *                                    the Configuration Constants to IFrameworkConfig.
 * Feb 07 2007		Alain Ah Ming				Deprecate existing err(...) methods and 
 * 																		add new ones with error code.
 * 																		Add warn(...) methods
 */
package com.gridnode.pdip.framework.log;

//import com.gridnode.pdip.framework.config.ConfigManager;
import java.util.Properties;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.LogLog;

import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.IFrameworkConfig;

/**
 * Framework logging class
 * @author Ang Meng Hua
 * @since
 * @version GT 4.0 VAN
 */
public class Log implements ILog
{
  public static final String LOG_CONFIG = IFrameworkConfig.FRAMEWORK_LOG_CONFIG;
//  "log" + File.separatorChar + "log.properties";

  // this initialise GN as the super category
  public static final String LOG_GRIDNODE_CATEGORY = "GN";

  // _channel is true if using logging api,
  // otherwise false to log to System.out
  private static boolean _channel = false;

  // static initialiser block to load config data for logging
  static
  {
    loadConfigData();
  }

  /**
   * Method to load configuration data for logging from the log properties file
  */
  public static void loadConfigData()
  {
    try
    {
      LogLog.debug(Category.getRoot().getAllAppenders().toString());
      Properties prop = ConfigurationManager.getInstance().getConfig(
        IFrameworkConfig.FRAMEWORK_LOG_CONFIG).getProperties();
      // ConfigManager.getInstance(LOG_CONFIG).getProperties();
      // Properties prop = ConfigManager.getInstance(LOG_CONFIG).getProperties();
      setLogProperties(prop);
    }
    catch(Exception ex)
    {
      LogLog.error("Error in loading config data for logging ", ex);
    }
  }

  /**
   * Initialise the logging properties. Subsequent calls to debug/log/err will print
   * messages to a physical file.
   *
   * @param prop Properties to set
   */
  public static void setLogProperties(Properties prop)
  {
    PropertyConfigurator.configure(prop);
    channelToLogFile(true);
  }

  /**
   * Set whether to print messages to a physical file.
   *
   * @param channel <B>true</B> to print to physical file,
   * <B>false</B> otherwise.
   *
   */
  public static void channelToLogFile(boolean channel)
  {
    _channel = channel;
  }

  /**
   * Print a debugging message.
   *
   * @param category Category of message
   * @param msg Message to be logged
   */
  public static void debug(String category, String msg)
  {
    if (_channel)
      getLogger(category).debug(msg);
    else
      System.out.println("DEBUG ["+category+"] " + msg);
  }

  /**
   * Print a debugging message.
   *
   * @param category Category of message
   * @param msg The message to be logged
   * @param ex The error/exception to print
   */
  public static void debug(String category, String msg, Throwable ex)
  {
    if (ex == null)
    {
      debug(category, msg);
    }
    else
    {
      if (_channel)
        getLogger(category).debug(msg, ex);
      else
      {
        debug(category, msg);
        ex.printStackTrace(System.out);
      }
    }
  }

  /**
   * Print a debugging message.
   *
   * @param category Category of the message
   * @param msg The message object
   */
  public static void debug(String category, Object msg)
  {
    if (_channel)
      getLogger(category).debug(msg);
    else
      System.out.println("DEBUG ["+category+"] "+msg);
  }

  /**
   * Formally log a message.
   *
   * @param category Category of the message
   * @param msg Message to be logged
   */
  public static void log(String category, String msg)
  {
    if (_channel)
      getLogger(category).info(msg);
    else
      System.out.println("INFO ["+category+"] "+msg);
  }

  /**
   * Formally log a message.
   *
   * @param category Category of the message
   * @param msg Message to be logged
   * @param ex The error/exception to print
   *
   */
  public static void log(String category, String msg, Throwable ex)
  {
    if (ex == null)
      log(category, msg);
    else
    {
      if (_channel)
        getLogger(category).info(msg, ex);
      else
      {
        log(category, msg);
        ex.printStackTrace(System.out);
      }
    }
  }

  /**
   * Formally log a message.
   *
   * @param category Category of the message
   * @param msg The object message
   *
   */
  public static void log(String category, Object msg)
  {
    if (_channel)
      getLogger(category).info(msg);
    else
      System.out.println("INFO ["+category+"] "+msg);
  }

  /**
   * Print a warning message.
   *
   * @param category Category of the message
   * @param msg The object message
   *
   */
  public static void warn(String category, Object msg)
  {
    if (_channel)
      getLogger(category).warn(msg);
    else
      System.out.println("WARN ["+category+"] "+msg);
  }

  /**
   * Print a warning message
   * @param category Category of the message
   * @param msg The error message
   */
  public static void warn(String category, String msg)
  {
    if (_channel)
      getLogger(category).warn(msg);
    else
      System.out.println("WARN ["+category+"] "+msg);  	
  }
  
  /**
   * Print a warning message.
   *
   * @param category Category of the message
   * @param msg The message
   * @param ex The error/exception to print
   *
   */
  public static void warn(String category, String msg, Throwable ex)
  {
    if (ex == null)
      warn(category, msg);
    else
    {
      if (_channel)
      {
        getLogger(category).warn(msg, ex);
      }
      else
      {
        warn(category, msg);
        ex.printStackTrace(System.out);
      }
    }
  }
  
  /**
   * @deprecated Use err(String, String, String)
   * @see Log#error(String, String, String)
   * Print an error message.
   *
   * @param category Category of the message
   * @param msg The error message
   * 
   */
  public static void err(String category, String msg)
  {
    if (_channel)
      getLogger(category).error(msg);
    else
      System.out.println("ERROR ["+category+"] "+msg);
  }

  /**
   * Log error message to the logging mechanism
   * @param errorCode The error code.
   * @param category The category of the message
   * @param msg The error message
   */
  public static void error(String errorCode, String category, String msg)
  {
    if (_channel)
    {
    	MDC.put(MDC_KEY_ERROR_CODE, errorCode);
      getLogger(category).error(msg);
      MDC.remove(MDC_KEY_ERROR_CODE);
    }
    else
    {
    	StringBuffer buf = new StringBuffer("ERROR [");
    	buf.append(category);
    	buf.append("] ");
    	buf.append(prependErrorCode(errorCode, msg));
      System.out.println(buf.toString());
    }
  }
  
  /**
   * @deprecated Use err(String, String, String, Throwable)
   * @see Log#error(String, String, String, Throwable)
   * Print an error message.
   *
   * @param category Category of the message
   * @param msg The message
   * @param ex The error/exception to print
   *
   */
  public static void err(String category, String msg, Throwable ex)
  {
    if (ex == null)
      err(category, msg);
    else
    {
      if (_channel)
      {
        getLogger(category).error(msg, ex);
      }
      else
      {
        err(category, msg);
        ex.printStackTrace(System.out);
      }
    }
  }

  /**
   * Print an error message.
   *	
   * @param errorCode The error code
   * @param category Category of the message
   * @param msg The message
   * @param ex The error/exception to print
   */
  public static void error(String errorCode, String category, String msg, Throwable ex)
  {
    if (ex == null)
    {    	
    	MDC.put(MDC_KEY_ERROR_CODE, errorCode);
      error(errorCode, category, msg);
      MDC.remove(MDC_KEY_ERROR_CODE);
    }
    else
    {
      if (_channel)
      {
      	MDC.put(MDC_KEY_ERROR_CODE, errorCode);
        getLogger(category).error(msg, ex);
        MDC.remove(MDC_KEY_ERROR_CODE);
      }
      else
      {
        error(errorCode,category, msg);
        ex.printStackTrace(System.out);
      }
    }  	
  }
  
  /**
   * @deprecated Use error(String, String, Object)
   * @see Log#error(String, String, Object)
   * Print an error message.
   *
   * @param category Category of the message
   * @param msg The object message
   *
   */
  public static void err(String category, Object msg)
  {
    if (_channel)
      getLogger(category).error(msg);
    else
      System.out.println("ERROR ["+category+"] "+msg);
  }

  /**
   * Print an error message
   * @param errorCode The error code
   * @param category The category of the message
   * @param msg The error messag
   */
  public static void error(String errorCode, String category, Object msg)
  {
    if (_channel)
    {
    	MDC.put(MDC_KEY_ERROR_CODE, errorCode);    	
      getLogger(category).error(msg);
      MDC.remove(MDC_KEY_ERROR_CODE);
    }
    else
    {
    	StringBuffer buf = new StringBuffer("ERROR [");
    	buf.append(category);
    	buf.append("] ");
    	buf.append(prependErrorCode(errorCode, msg));
      System.out.println(buf.toString());
    }
  }
  
  /**
   * @deprecated Use assertCondition(String, String, boolean, String)
   * Asserts a condition. If condition is false, prints an error message and
   * throws exception.
   *
   * @param category The category of message if condition is false
   * @param condition The condition to assert
   * @param errMsg The message to print if condition is false
   * @exception IllegalArgumentException Condition is false.
   *
   */
  public static void assertCondition(String category, boolean condition, String errMsg)
  {
    if (_channel)
      getLogger(category).assertLog(condition, errMsg);
    else
    {
      if (!condition)
      {
        err(category, errMsg);
        throw new IllegalArgumentException(errMsg);
      }
    }
  }

  /**
   * Asserts a condition. If condition is false, prints an error message and
   * throws exception.
   *
   * @param category The category of message if condition is false
   * @param condition The condition to assert
   * @param errMsg The message to print if condition is false
   * @exception IllegalArgumentException Condition is false.
   *
   */
  public static void assertCondition(String errorCode, String category, boolean condition, String errMsg)
  {
    if (_channel)
      getLogger(category).assertLog(condition, errMsg);
    else
    {
      if (!condition)
      {
        error(errorCode, category, errMsg);
        throw new IllegalArgumentException(errMsg);
      }
    }
  }

  /**
   * Returns a Logger instance
   *
   * @param category Category name identifier
   *
   * @return the instance of the Category object
   * Note: This class has been deprecated and replaced by the Logger subclass.
   * It will be kept around to preserve backward compatibility until mid 2003.
   *
  */
  private static Category getLogger(String category)
  {
    return Logger.getInstance(LOG_GRIDNODE_CATEGORY + "." + category);
  }  
  
  /**
   * Utility method to prepend an error code to an error message
   * @param errCode The error code
   * @param msg The error message
   * @return The message prepended with the error code (within square brackets)
   */
  private static String prependErrorCode(String errCode, Object msg)
  {
  	StringBuffer buf = new StringBuffer("[");
  	buf.append(errCode);
  	buf.append("] ");
  	buf.append(msg);
  	return buf.toString();  
  }
}

