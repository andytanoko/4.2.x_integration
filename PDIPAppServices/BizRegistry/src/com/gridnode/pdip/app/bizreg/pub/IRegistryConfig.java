/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRegistryConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 * Sep 16 2003    Neo Sok Lay         Adds constants for configure the
 *                                    specific registry service implementation.
 */
package com.gridnode.pdip.app.bizreg.pub;

import com.gridnode.pdip.framework.config.IFrameworkConfig;

/**
 * This interface defines the constants used for obtaining
 * properties from the public registry configuration file.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IRegistryConfig extends IFrameworkConfig
{
  /**
   * Name of the configuration.
   */
  static final String CONFIG_NAME = "registry.public";
  
  /**
   * Key for obtaining the Http Proxy Host.
   */
  static final String KEY_HTTP_PROXY_HOST = "http.proxy.host";
  
  /**
   * Key for obtaining the Http Proxy Port number.
   */
  static final String KEY_HTTP_PROXY_PORT = "http.proxy.port";
  
  /**
   * Key for obtaining the Https Proxy Host.
   */
  static final String KEY_HTTPS_PROXY_HOST = "https.proxy.host";
  
  /**
   * Key for obtaining the Https Proxy Port number.
   */
  static final String KEY_HTTPS_PROXY_PORT = "https.proxy.port";
  
  /**
   * Key for obtaining the RegistryServiceManager implementation class.
   */
  static final String KEY_REGISTRY_SERVICE_MGR = "service.manager.class";

  /**
   * Key for obtaining the method name to create an instance of the RegistryServiceManager.
   * This method is assume to be public static accessible.
   */
  static final String KEY_CREATE_MGR_METHOD = "service.manager.createMethod";
  
  /**
   * Key for obtaining the list of required libraries for the registry service implementation.
   */  
  static final String KEY_SERVICE_REQLIBS = "service.reqlibs";
  
  /**
   * Key for obtaining the list of classes (or package names) that must be loaded from
   * the required libraries.
   */
  static final String KEY_SERVICE_KEYCLASSES = "service.reqlibs.keyclasses";
  
  /**
   * Key for obtaining the list of resources (or path names) that must be loaded from
   * the required libraries.
   */
  static final String KEY_SERVICE_KEYRESOURCES = "service.reqlibs.keyresources";
  
  /**
   * The Path key to obtain the root location of the required libraries.
   */
  static final String PATH_KEY_REQLIB = "common.path.extlib";
  
}

