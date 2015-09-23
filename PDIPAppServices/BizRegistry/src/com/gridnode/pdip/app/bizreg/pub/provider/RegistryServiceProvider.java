/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryServiceProvider.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 15 2003    Neo Sok Lay         Created
 * Oct 17 2005    Neo Sok Lay         For JDK1.5 compliance: Reflection invocation
 *                                    must explicit cast null args.
 */
package com.gridnode.pdip.app.bizreg.pub.provider;

import com.gridnode.pdip.app.bizreg.pub.IRegistryConfig;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.classloader.ConstrainedClassLoader;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

/**
 * This class acts as the middle-man between the registry client and a specific
 * implementation of registry provider. The client is able to obtain the RegistryServiceManager
 * from this class to connect to a registry.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class RegistryServiceProvider implements IRegistryConfig
{
  private static final RegistryServiceProvider _self = new RegistryServiceProvider();
  private boolean isLoaded = false;
  private ConstrainedClassLoader _classLoader;
  private Class _providerManagerClass;
  private IRegistryServiceManager _regServiceManager;
  
  /**
   * Constructs an instance of RegistryServiceProvider.
   */
  private RegistryServiceProvider()
  {
  }

  /**
   * Obtains an instance of RegistryServiceProvider.
   * 
   * @return An instance of RegistryServiceProvider
   * @throws Exception Error loading the specific implementation of
   * RegistryServiceManager.
   */
  public static synchronized RegistryServiceProvider getInstance() throws Exception
  {
    if (!_self.isLoaded)
    {
      _self.load();
      _self.isLoaded = true;
    }
    return _self;
  }
  
  /**
   * Gets an implementation instance of RegistryServiceManager.
   * 
   * @return An implementation instance of RegistryServiceManager
   * @throws Exception Error loading the implementation instance of RegistryServiceManager.
   */
  public static IRegistryServiceManager getRegistryServiceManager()
    throws Exception
  {
    Thread.currentThread().setContextClassLoader(getInstance()._classLoader);
    return getInstance()._regServiceManager;  
  }
  
  /**
   * Loads configurations to initialize an implementation instance of RegistryServiceManager.
   * 
   * @throws Exception Error loading configurations or initializing RegistryServiceManager, probably
   * due to error in the public registry configuration file.
   */
  private void load() throws Exception
  {
    Configuration config = getConfig();
    
    URL[] reqLibs = getReqLibs(config.getList(KEY_SERVICE_REQLIBS, ","));
    String[] keyClasses = (String[])config.getList(KEY_SERVICE_KEYCLASSES, ",").toArray(new String[0]);
    String[] keyResources = (String[])config.getList(KEY_SERVICE_KEYRESOURCES, ",").toArray(new String[0]);
    _classLoader = new ConstrainedClassLoader(reqLibs, this.getClass().getClassLoader());
    _classLoader.setForceLoadClassPaths(keyClasses);
    _classLoader.setForceLoadResources(keyResources);
    
    Thread.currentThread().setContextClassLoader(_classLoader);
    
    _providerManagerClass = _classLoader.loadClass(config.getString(KEY_REGISTRY_SERVICE_MGR));
    Method newInstanceMethod = _providerManagerClass.getMethod(
                                  config.getString(KEY_CREATE_MGR_METHOD), (Class[])null); 
    
    _regServiceManager = (IRegistryServiceManager)newInstanceMethod.invoke(null, (Object[])null);      
  }
  
  /**
   * Get the URLs to the libraries required for the registry provider implementation to plug-in.
   * 
   * @param libs List of filenames of the libraries (without the path). These libraries are must exist at the 
   * <code>common.path.extlib</code> directory under the application domain in order for the libraries
   * to be loaded successfully.
   * @return Array of URLs of the libraries loaded.
   * @throws Exception Any of the specified libraries does not exist.
   */
  private URL[] getReqLibs(List libs) throws Exception
  {
    URL[] urls = new URL[libs.size()];

    for (int i=0; i<urls.length; i++)
    {
      urls[i] = FileUtil.getFile(PATH_KEY_REQLIB, (String)libs.get(i)).toURL(); 
    }
    return urls; 
  }
  
  /**
   * Gets the Configuration for public registry. 
   * 
   * @return Configuration for public registry.
   * @throws Exception Error getting the configuration.
   */
  private Configuration getConfig() throws Exception
  {
    return ConfigurationManager.getInstance().getConfig(CONFIG_NAME);
  }
  
}
