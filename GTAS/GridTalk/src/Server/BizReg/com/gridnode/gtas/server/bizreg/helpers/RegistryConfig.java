/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 15, 2003    Neo Sok Lay         Created
 * Oct 27 2005     Neo Sok Lay         Change getTechSpecsPath() to return absolute path
 */
package com.gridnode.gtas.server.bizreg.helpers;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.util.SystemUtil;

/**
 * This class represents the configuration information for public registry.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class RegistryConfig implements IRegistryConfig
{
  private Configuration _config;

  /**
   * Constructs the RegistryConfig.
   */
  public RegistryConfig()
  {
    load();
  }

  /**
   * Loads the registry config file
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
   * Get the path to the technicalspecs files.
   * 
   * @return The path configured in the registry config file. Uses the 
   * value in DEFAULT_SPECS_PATH if no path is specified in the config file.
   */  
  String getTechSpecsPath()
  {
    String path = _config.getString(KEY_SPECS_PATH, DEFAULT_SPECS_PATH);

    return SystemUtil.getWorkingDirPath() + "/" + path;  //NSL20051027
  }
  
  /**
   * Get the filename pattern for the technicalspecs files.
   * 
   * @return The filename pattern configured in the registry config file. Uses the 
   * value in DEFAULT_SPECS_NAME_PATTERN if none is specified in the config file.
   */  
  String getTechSpecsNamePattern()
  {
    String pattern = _config.getString(KEY_SPECS_NAME_PATTERN, DEFAULT_SPECS_NAME_PATTERN);
    
    return pattern;
  }
}
