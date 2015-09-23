/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Log4jAdapter.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 23, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.log;

import com.gridnode.gridtalk.tester.loopback.util.FileUtil;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.LogLog;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * @todo Class documentation
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class Log4jAdapter
{
	private static final String LOG_CONF_FILENAME = "log.properties"; 
//  "log" + File.separatorChar + "log.properties";

  // this initialise GN as the super category
  public static final String LOG_GRIDNODE_CATEGORY = "GN";

  // _channel is true if using logging api,
  // otherwise false to log to System.out
  private static boolean _channel = false;
  private static Properties logProp = null;

  // static initialiser block to load config data for logging
  static
  {
    loadConfigData();
  }

  /**
   * Method to load configuration data for logging from the log properties file
  */
  private static void loadConfigData()
  {
    try
    {
    	if (logProp == null)
    	{
    		logProp = new Properties();
    		logProp.load(new FileInputStream(FileUtil.getFile(FileUtil.TYPE_CONF, LOG_CONF_FILENAME)));
    	}
      //LogLog.debug(Category.getRoot().getAllAppenders().toString());
      /*Properties prop = ConfigurationManager.getInstance().getConfig(
        ICommonConfig.LOG_CONFIG).getProperties();*/
      // ConfigManager.getInstance(LOG_CONFIG).getProperties();
      // Properties prop = ConfigManager.getInstance(LOG_CONFIG).getProperties();
      setLogProperties(logProp);
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
    //AbstractStarfishLogger.setLogProperties(prop);
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
   * Print an error message.
   *
   * @param category Category of the message
   * @param msg The error message
   *
   */
  public static void warn(String category, String msg)
  {
    if (_channel)
      getLogger(category).warn(msg);
    else
      System.out.println("WARN ["+category+"] "+msg);
  }

  /**
   * Print an error message.
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
   * Print an error message.
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
}

