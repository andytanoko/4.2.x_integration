/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConstrainedClassLoader.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 15 2003    Neo Sok Lay         Created
 * Nov 11 2003    Neo Sok Lay         Add print statements for use when in debug mode.
 */
package com.gridnode.pdip.framework.util.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * This class loader applies a constraint for force loading specific classes
 * from the URLs specified in this class loader. Other classes will be loaded
 * as per normal as in the URLClassLoader.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class ConstrainedClassLoader extends URLClassLoader
{
  private boolean IS_DEBUG = false;
  private String[] _forceLoadClassPaths;
  private String[] _forceLoadResources;
  
  /**
   * Constructs a ConstrainedClassLoader
   * 
   * @param urls the URLs from which to load classes and resources
   * @param parent the parent class loader for delegation
   * @see URLClassLoader#URLClassLoader(URL[],ClassLoader)
   */
  public ConstrainedClassLoader(URL[] urls, ClassLoader parent)
  {
    super(urls, parent);
  }

  /**
   * Constructs a ConstrainedClassLoader
   * 
   * @param urls the URLs from which to load classes and resources
   * @see URLClassLoader#URLClassLoader(URL[])
   */
  public ConstrainedClassLoader(URL[] urls)
  {
    super(urls);
  }

  /**
   * Constructs a ConstrainedClassLoader
   * 
   * @param urls  the URLs from which to load classes and resources
   * @param parent the parent class loader for delegation
   * @param factory the URLStreamHandlerFactory to use when creating URLs
   */
  public ConstrainedClassLoader(
    URL[] urls,
    ClassLoader parent,
    URLStreamHandlerFactory factory)
  {
    super(urls, parent, factory);
  }

  public void setForceLoadClassPaths(String[] classPaths)
  {
    _forceLoadClassPaths = classPaths;
    if (IS_DEBUG && classPaths != null)
    {
      for (int i=0; i<classPaths.length; i++)
        System.out.println("ForceLoadClassPath: "+classPaths[i]);
    }
  }
  
  public void setForceLoadResources(String[] resourceNames)
  {
    _forceLoadResources = resourceNames;
    if (IS_DEBUG && resourceNames != null)
    {
      for (int i=0; i<resourceNames.length; i++)
        System.out.println("ForceLoadResources: "+resourceNames[i]);
    }
  }
  
  /**
   * Loads the class with the specified name. Force loads the class
   * if it is on the force-load classpath.
   * 
   * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
   */
  protected Class loadClass(String className, boolean resolve) throws ClassNotFoundException
  {
    Class result;
    boolean filter = isForceLoadClass(className);
    if (filter)
    {
      result = forceLoadClass(className);
      if (result != null)
      {
        return result;
      }
    }

    return super.loadClass(className, resolve);
  }
  
  /**
   * Checks if a class is required to be force-loaded locally from the URLs specified in this
   * classloader.
   * 
   * @param className The name of the class to check
   * @return <b>true</b> if <code>className</code> starts with any of the
   * <code>forceLoadClasspaths</code> specified in this classloader.
   */
  protected boolean isForceLoadClass(String className)
  {
    boolean found = false;
    if (_forceLoadClassPaths != null)
    {
      for (int i=0; i<_forceLoadClassPaths.length && !found; i++)
      {
        found = (className.startsWith(_forceLoadClassPaths[i]));
      }
    }

    return found;
  }

  /**
   * Checks if a resource is required to be force-loaded locally from the URLs specified
   * in this classloader.
   * 
   * @param resourceName The name of the resource to check
   * @return <b>true</b> if <code>resourceName</code> starts with any of the
   * <code>forceLoadResources</code> specified in this classloader.
   */
  protected boolean isForceLoadResource(String resourceName)
  {
    boolean found = false;
    if (_forceLoadResources != null)
    {
      for (int i=0; i<_forceLoadResources.length && !found; i++)
      {
        found = (resourceName.startsWith(_forceLoadResources[i]));
      }
    }
      
    return found;
  }

  /**
   * Force-loads a class from the URLs specified in this classloader.
   * 
   * @param className The name of the class to force-load.
   * @return The loaded class.
   */
  protected Class forceLoadClass(String className)
  {
    Class result = null;

    result = findLoadedClass(className);
    if (result != null)
    {
      // Return an already-loaded class
      return result;
    }

    try
    {
      if (IS_DEBUG)
        System.out.println("Finding class from local class loader: "+className);
      result = findClass(className);
    }
    catch (ClassNotFoundException ex)
    {
      if (IS_DEBUG)
        System.out.println("Class not found in local class loader: "+className);
    }
    return result;
  }
  
  /**
   * If the resource is required to be force-loaded, the method tries primarily to
   * load it locally, otherwise it calls the parent's <code>getResource</code>.
   * 
   * @see java.lang.ClassLoader#getResource(java.lang.String)
   */
  public URL getResource(String name)
  {
    URL url = null;
    if (isForceLoadResource(name))
    {
      if (IS_DEBUG)
        System.out.println("ForceLoading Resource: "+name);
      url = findResource(name);
    }
    
    if (url == null)
    {
      if (IS_DEBUG)
        System.out.println("Using parentClassLoader's getResource to load Resource: "+name);
      url = super.getResource(name);
    }
    
    return url;  
  }

}
