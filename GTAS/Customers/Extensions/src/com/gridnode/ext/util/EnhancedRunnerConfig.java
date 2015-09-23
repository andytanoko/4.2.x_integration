/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2004 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EnhancedRunnerConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 14 2004    Neo Sok Lay         Created
 */
package com.gridnode.ext.util;

import com.gridnode.ext.util.exceptions.ConfigurationException;

import java.util.Properties;
import java.io.FileInputStream;

/**
 * Enhanced startup config file for use with EnhancedJavaRunner.
 *
 * @version GT 2.3 I1
 * @since GT 2.3 I1
 */
public class EnhancedRunnerConfig extends Properties
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2601991670609879246L;

	/**
   * Property key: classpath.libs
   */
  public static final String KEY_CP_LIBS = "classpath.libs";

  /**
   * Suffix for Dir property key
   */
  public static final String KEY_DIR_SUFFIX = ".dir";

  /**
   * Suffix for Lib property key
   */
  public static final String KEY_LIBS_SUFFIX = ".libs";

  /**
   * Property key: working.dir
   */
  public static final String KEY_WORKING_DIR = "working.dir";

  /**
   * Constructs an EnhancedRunnerConfig
   *
   * @param configFile The name of the configuration file to base on.
   * @throws ConfigurationException Unable to load the configuration file, or
   * invalid properties specified in the configuration file.
   */
  public EnhancedRunnerConfig(String configFile) throws ConfigurationException
  {
    super();
    loadConfig(configFile);
    validate();
  }

  /**
   * Loads the configuration file properties into this AdaptorRunConfig object.
   *
   * @param configFile The configuration file to load.
   * @throws ConfigurationException Error loading the configuration file.
   */
  protected void loadConfig(String configFile) throws ConfigurationException
  {
    try
    {
      FileInputStream fis = new FileInputStream(configFile);
      load(fis);
    }
    catch (Exception ex)
    {
      throw new ConfigurationException(
        "Unable to load adaptor run config file: " + ex.getMessage());
    }
  }

  /**
   * Validate the properties loaded into this object.
   *
   * @throws ConfigurationException Invalid properties loaded.
   */
  protected void validate() throws ConfigurationException
  {

  }

  protected void checkNonBlank(String propertyName, String value)
    throws ConfigurationException
  {
    if (StringUtil.isBlank(value))
      throw new ConfigurationException(propertyName + " is a required configuration property!");
  }

  /**
   * Get the list of the logical library names linking to sets of
   * libraries to included in the classpath of the Adaptor.
   * @return Array of logical library names.
   */
  public String[] getClasspathLibNames()
  {
    String libStr = getProperty(KEY_CP_LIBS, "");
    return StringUtil.split(libStr, ",");
  }

  /**
   * Get the base directory containing a set of libraries.
   *
   * @param libName Logical name of the set of libraries
   * @return The base directory for the set of libraries.
   */
  public String getLibDir(String libName)
  {
    String key = libName.concat(KEY_DIR_SUFFIX);
    return getProperty(key, "");
  }

  /**
   * Get the names of the set of libraries specified by a logical library name.
   *
   * @param libName The logical library name.
   * @return Array of the names of the set of libraries.
   */
  public String[] getLibSet(String libName)
  {
    String key = libName.concat(KEY_LIBS_SUFFIX);
    String libStr = getProperty(key, "");
    return StringUtil.split(libStr, ",");
  }

  /**
   * Set a property to use for substitution in other property values later on.
   *
   * @param key The property key.
   * @param value The value of the property.
   */
  public void putSubstitutionProperty(String key, String value)
  {
    String substKey = getSubstitutionPropertyKey(key);
    setProperty(substKey, value);
  }

  /**
   * Get the value of the property previous set using <code>putSubstitutionProperty</code>.
   *
   * @param key The property key.
   * @return The value of the property.
   */
  public String getSubstitutionProperty(String key)
  {
    String substKey = getSubstitutionPropertyKey(key);
    return getProperty(substKey);
  }

  /**
   * Get the actual property key for the property used for substitution.
   *
   * @param key The property key.
   * @return Actual property key that can be used directly to access the
   * property value using <code>getProperty</code>.
   */
  public String getSubstitutionPropertyKey(String key)
  {
    return "${".concat(key).concat("}");
  }

}