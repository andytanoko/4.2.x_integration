/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DependencyConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 10 2003    Neo Sok Lay         Created
 * Oct 27 2005    Neo Sok Lay         Return absolute path instead of relative
 */
package com.gridnode.pdip.framework.db.dependency;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.util.SystemUtil;

/**
 * This class represents the configuration information for dependency checking.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class DependencyConfig implements IDependencyConfig
{
  private Configuration _config;

  /**
   * Constructor for DependencyConfig.
   */
  public DependencyConfig()
  {
    load();
  }

  /**
   * Loads the dependency config file
   */
  private void load()
  {
    try
    {
      _config = ConfigurationManager.getInstance().getConfig(CONFIG_NAME);
    }
    catch (Exception ex)
    {
      _config = new Configuration();
    }
  }

  /**
   * Get the path to the dependency descriptor files.
   * 
   * @return The path configured in the dependency config file. Uses the 
   * value in DEFAULT_DESCR_PATH if no path is specified in the config file.
   */  
  String getDescriptorPath()
  {
    String path = _config.getString(KEY_DESCR_PATH, DEFAULT_DESCR_PATH);

    return SystemUtil.getWorkingDirPath() + "/" + path;  
  }
  
  /**
   * Get the filename pattern for the dependency descriptor files.
   * 
   * @return The filename pattern configured in the dependency config file. Uses the 
   * value in DEFAULT_DESCR_NAME_PATTERN if none is specified in the config file.
   */  
  String getDescriptorNamePattern()
  {
    String pattern = _config.getString(KEY_DESCR_NAME_PATTERN, DEFAULT_DESCR_NAME_PATTERN);
    
    return pattern;
  }
}
