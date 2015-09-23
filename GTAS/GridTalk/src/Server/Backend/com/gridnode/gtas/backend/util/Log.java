/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Log.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 19 2002    Koh Han Sing        Port to GTAS
 * Sep 30 2003    Koh Han Sing        Changed to use log4j
 */
package com.gridnode.gtas.backend.util;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Log 
{
  static
  {
    init();
  }

  public static void init()
  {
    Properties prop = new Properties();
    try
    {
      prop.load(new FileInputStream("log.properties"));
    }
    catch (Exception ex)
    {
      prop = setDefaultProp();
    }

    PropertyConfigurator.configure(prop);
  }

  private static Properties setDefaultProp()
  {
    Properties p = new Properties();
    p.put("log4j.rootCategory", "DEBUG, stdout, R");
    p.put("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
    p.put("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
    p.put("log4j.appender.stdout.layout.ConversionPattern", "%-4r [%t] %-5p %c %x - %m%n");
    p.put("log4j.appender.R", "org.apache.log4j.RollingFileAppender");
    p.put("log4j.appender.R.File", "log.txt");
    //p.put("log4j.appender.R.SafeLog", "-1");
    p.put("log4j.appender.R.MaxFileSize", "500KB");
    p.put("log4j.appender.R.MaxBackupIndex", "10");
    p.put("log4j.appender.R.layout", "org.apache.log4j.PatternLayout");
    p.put("log4j.appender.R.layout.ConversionPattern", "%d{DATE} [%c] [%t] %m%n");

    return p;
  }

  public static void log(String category, Object msg)
  {
    Logger.getInstance(category).info(msg);
  }

  public static void log(String category, String msg)
  {
    Logger.getInstance(category).info(msg);
  }

  public static void log(String category, String msg, Throwable ex)
  {
    Logger.getInstance(category).info(msg, ex);
  }

  public static void debug(String category, Object msg)
  {
    Logger.getInstance(category).debug(msg);
  }

  public static void debug(String category, String msg)
  {
    Logger.getInstance(category).debug(msg);
  }

  public static void debug(String category, String msg, Throwable ex)
  {
    Logger.getInstance(category).debug(msg, ex);
  }

  public static void err(String category, Object msg)
  {
    Logger.getInstance(category).error(msg);
  }

  public static void err(String category, String msg)
  {
    Logger.getInstance(category).error(msg);
  }

  public static void err(String category, String msg, Throwable ex)
  {
    Logger.getInstance(category).error(msg, ex);
  }
}
