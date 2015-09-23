/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IJaxrKeys.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.pub.provider.jaxr;

import com.gridnode.pdip.app.bizreg.pub.IRegistryConfig;

/**
 * This interface extends the public registry config to
 * define keys specific for the use by the JAXR registry provider.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IJaxrKeys extends IRegistryConfig
{
  // for use with connection properties
  /**
   * Property key for the inquiry url 
   */
  static final String CONN_BQM_URL = "javax.xml.registry.queryManagerURL";
  
  /**
   * Property key for the publish url
   */
  static final String CONN_BLM_URL = "javax.xml.registry.lifeCycleManagerURL";
  
  /**
   * Property key for the JAXR connection factory implementation class. 
   */
  static final String CONN_FACTORY_CLASS = "javax.xml.registry.factoryClass";
  
  /**
   * Property key for the Http Proxy Host.
   */
  static final String CONN_HTTP_PROXY_HOST = "com.sun.xml.registry.http.proxyHost";
  
  /**
   * Property key for the Http Proxy Port number.
   */
  static final String CONN_HTTP_PROXY_PORT = "com.sun.xml.registry.http.proxyPort";
  
  /**
   * Property key for the Https Proxy Host.
   */
  static final String CONN_HTTPS_PROXY_HOST = "com.sun.xml.registry.https.proxyHost";
  
  /**
   * Property key for the Https Proxy Port number.
   */
  static final String CONN_HTTPS_PROXY_PORT = "com.sun.xml.registry.https.proxyPort";
 
  // for use with Configuration
  /**
   * Configuration key for the JAXR connection factory implementation class
   * to use.
   */
  static final String KEY_FACTORY_IMPL = "jaxr.factory.impl"; 
  
}
