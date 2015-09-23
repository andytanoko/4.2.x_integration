/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LoggerManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 8, 2006    i00107              Created
 * Apr 25 2007    i00107              Add a method to not configureAndWatch log config.
 */

package com.gridnode.util.log;

import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;

import com.gridnode.util.SystemUtil;


/**
 * @author i00107
 * This class provides Logger objects.
 */
public class LoggerManager
{
  public static final String TYPE_UTIL = "GN.Util";
  
  private static LoggerManager _self;
  private LoggerManager()
  {
    initConfig(true);
  }

  private LoggerManager(boolean watchConfig)
  {
    initConfig(watchConfig);
  }

  private void initConfig(boolean watchConfig)
  {
    try
    {
      if (watchConfig)
      {
        DOMConfigurator.configureAndWatch(getLogConfig());
      }
      else
      {
        DOMConfigurator.configure(getLogConfig());
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  private String getLogConfig()
  {
    //return ConfigurationStore.getInstance().getProperty("log", "config.loc", "log.xml");
    File f = new File(SystemUtil.getWorkingDir(), "conf/gtvan-log.xml");
    return f.getAbsolutePath();
  }
  
  public static final synchronized LoggerManager getInstance()
  {
    if (_self == null)
    {
      _self = new LoggerManager();
    }
    return _self;
  }
  
  public Logger getLogger(String type, String name)
  {
    org.apache.log4j.Logger logger = LogManager.getLogger(type);
    
    return new Logger(name, logger);
  }
  
  /**
   * For classes that will be loaded frequently
   * @return A LoggerManager for one-time usage
   */
  public static final LoggerManager getOneTimeInstance()
  {
    return new LoggerManager(false);
  }
}
