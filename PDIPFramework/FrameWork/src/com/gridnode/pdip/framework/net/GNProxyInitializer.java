/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNProxyInitializer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 11, 2007   i00107              Created
 */

package com.gridnode.pdip.framework.net;

import java.net.Proxy;
import java.net.URI;
import java.util.List;
import java.util.Properties;

import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.net.GNProxySelector.ILogger;

/**
 * @author i00107
 * This class initialize the proxy setting for the VM and allows client
 * to retrieve list of proxies for a specified URI.
 */
public class GNProxyInitializer
{
  private static GNProxyInitializer _self = null;
  /**
   * 
   */
  private GNProxyInitializer()
  {
    installProxySetting();
  }

  private GNProxyInitializer(Properties props)
  {
    installProxySetting(props);
  }
  
  /**
   * Asks the initializer to ensure that the proxy setting
   * is initialized using the default proxy configuration.
   */
  public static final synchronized void init()
  {
    if (_self == null)
    {
      _self = new GNProxyInitializer();
    }
  }

  /**
   * Asks the initializer to ensure that the proxy setting is
   * initialized using the specified proxy configuration.
   * 
   * @param props The proxy configuration required by the GNProxySelector.
   */
  public static final synchronized void init(Properties props)
  {
    if (_self == null)
    {
      _self = new GNProxyInitializer(props);
    }
  }

  private void installProxySetting()
  {
    Properties props = ConfigurationManager.getInstance().getConfig(IFrameworkConfig.FRAMEWORK_PROXY_CONFIG).getProperties();
    installProxySetting(props);
  }
  
  private void installProxySetting(Properties props)
  {
    if (props != null)
    {
      GNProxySelector.setLogger(new ProxyLogger());
      GNProxySelector.install(props);
    }
  }
  
  /**
   * Select the proxies that are available for connecting to the specified
   * URI.
   * 
   * @param uri The URI to connect to.
   * @return The list of proxies available. At least one Proxy will be returned
   * in the List.
   */
  public static final synchronized List<Proxy> selectProxies(URI uri)
  {
    return GNProxySelector.getDefault().select(uri);
  }
  
  /**
   * Implementation of the ILogger interface for GNProxySelector.
   * @author i00107
   */
  class ProxyLogger implements ILogger
  {
    private static final String LOG_CAT = "FRAMEWORK.GNProxySelector";
    public void debug(String msg)
    {
      Log.debug(LOG_CAT, msg);
    }
    public void log(String msg)
    {
      Log.log(LOG_CAT, msg);
    }
    
    public void error(String msg, Throwable t)
    {
      Log.err(LOG_CAT, msg, t);
    }
  }
}
