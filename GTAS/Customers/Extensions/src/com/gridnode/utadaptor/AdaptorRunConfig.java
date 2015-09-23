/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AdaptorRunConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 27 2003    Neo Sok Lay         Created
 * Jan 14 2004    Neo Sok Lay         Refactor to extend from EnhancedRunnerConfig
 */
package com.gridnode.utadaptor;

import com.gridnode.ext.util.EnhancedRunnerConfig;
import com.gridnode.ext.util.exceptions.ConfigurationException;

/**
 * Configuration properties to be used for running the Adaptor.
 *
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class AdaptorRunConfig extends EnhancedRunnerConfig
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4060617055086957882L;

	/**
   * Property key: xpath.error.code
   */
  public static final String KEY_ERROR_CODE_XPATH = "xpath.error.code";

  /**
   * Property key: xpath.error.desc
   */
  public static final String KEY_ERROR_DESC_XPATH = "xpath.error.desc";

  /**
   * Constructs an AdaptorRunConfig
   *
   * @param configFile The name of the configuration file to base on.
   * @throws ConfigurationException Unable to load the configuration file, or
   * invalid properties specified in the configuration file.
   */
  public AdaptorRunConfig(String configFile) throws ConfigurationException
  {
    super(configFile);
  }

  /**
   * Validate the properties loaded into this object.
   *
   * @throws ConfigurationException Invalid properties loaded.
   */
  protected void validate() throws ConfigurationException
  {
    checkNonBlank(KEY_ERROR_CODE_XPATH, getErrorCodeXpath());
    checkNonBlank(KEY_ERROR_DESC_XPATH, getErrorDescXpath());
  }

  /**
   * Get the XPath to obtain the Error Code from the error message returned
   * by the adaptor.
   * @return The Xpath to the Error code.
   */
  public String getErrorCodeXpath()
  {
    return getProperty(KEY_ERROR_CODE_XPATH, null);
  }

  /**
   * Get the Xpath to obtain the Error description from the error message returned
   * by the adaptor.
   *
   * @return The Xpath to the Error description.
   */
  public String getErrorDescXpath()
  {
    return getProperty(KEY_ERROR_DESC_XPATH, null);
  }


}