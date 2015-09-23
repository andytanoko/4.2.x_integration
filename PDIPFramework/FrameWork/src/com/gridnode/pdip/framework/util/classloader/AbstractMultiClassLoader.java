/* This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractMultiClassLoader.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 02 2002    Jagadeesh               Created
 * Oct 17 2002    Ang Meng Hua            Refactor to enhance code hygiene
 * Jul 11 2003    Koh Han Sing            Repackage into framework from base
 *                                        userprocedure
 */
package com.gridnode.pdip.framework.util.classloader;

import org.apache.axis.utils.bytecode.ParamReader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractMultiClassLoader extends URLClassLoader
  implements IMultiClassLoader
{
  public AbstractMultiClassLoader(String classpath)
    throws Exception
  {
    super(new URL[] {new URL(classpath)});
  }

  public AbstractMultiClassLoader(String classpath,ClassLoader parent)
    throws Exception
  {
    super(new URL[] {new URL(classpath)},parent);
  }

  public AbstractMultiClassLoader(URL url)
    throws Exception
  {
    super(new URL[] {url});
  }

  public AbstractMultiClassLoader(URL url,ClassLoader parent)
    throws Exception
  {
    super(new URL[] {url},parent);
  }


  public AbstractMultiClassLoader(URL[] urls)
  {
    super(urls);
  }

  public AbstractMultiClassLoader(URL[] urls,ClassLoader parent)
  {
    super(urls,parent);
  }


  public void invokeClass(String mainclass, String[] args)
    throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException
  {
    //loadClass method here synchronized by default(See URLClassLoader).
    Class c = loadClass(mainclass);
    Method m = c.getMethod("main", new Class[] { args.getClass() });
    m.setAccessible(true);
    int mods = m.getModifiers();
    if (m.getReturnType() != void.class || !Modifier.isStatic(mods) ||
      !Modifier.isPublic(mods))
    {
      throw new NoSuchMethodException("main");
    }
    try
    {
      m.invoke(null, new Object[] { args });
    }
    catch (IllegalAccessException e)
    {
      // This should not happen, as we have disabled access checks
    }
  }

  public Object invokeClassMethod(
    String className,
    String methodName,
    Class [] cstParameterTypes,
    Object [] cstParameters,
    Class [] mtdParameterTypes,
    Object [] mtdParameters)
      throws ClassNotFoundException,
             NoSuchMethodException,
             InvocationTargetException,
             IllegalAccessException,
             InstantiationException,
             IOException
  {
    Class lClass = null;
    Object classObject = null;
    Method  toInvokeMethod = null;
    Object result = null;

    if (cstParameterTypes == null || cstParameters == null
        ||cstParameters.length < 1 ||cstParameterTypes.length < 1)
    {
      lClass = loadClass(className);
      classObject = lClass.newInstance();
    }
    else //Using Constructors that Have Arguments
    {
      lClass = loadClass(className, true);
      Constructor constructor = lClass.getConstructor(cstParameterTypes);
      classObject = constructor.newInstance(cstParameters);
    }

    toInvokeMethod = lClass.getMethod(methodName,mtdParameterTypes);
    int mods = toInvokeMethod.getModifiers();
    if(!Modifier.isPublic(mods))
      return null;
    result = toInvokeMethod.invoke(classObject,mtdParameters);
    return result;
  }

  public Collection getMethods(String className) throws ClassNotFoundException
  {
    ArrayList methods = new ArrayList();
    Class aClass = loadClass(className);
    Method[] dMethods = aClass.getMethods();
    for (int i = 0; i < dMethods.length; i++)
    {
      Method aMethod = dMethods[i];
      Object[] methodInfo = new Object[5];
      //System.out.println("Method : "+aMethod.toString());
      methodInfo[0] = aMethod.getName();
      //System.out.println("Method name : "+aMethod.getName());
      methodInfo[1] = getTypeName(aMethod.getReturnType());
      //System.out.println("Return type : "+getTypeName(aMethod.getReturnType()));
      methodInfo[2] = getParamNames(aMethod);
      Class[] paramsTypes = aMethod.getParameterTypes();
      String[] pt = new String[paramsTypes.length];
      for (int j = 0; j < paramsTypes.length; j++)
      {
        pt[j] = getTypeName(paramsTypes[j]);
        //System.out.println("Param type : "+getTypeName(paramsTypes[j]));
      }
      methodInfo[3] = pt;
      methodInfo[4] = null;
      //System.out.println("**************************");
      methods.add(methodInfo);
    }
    return methods;
  }

  private String[] getParamNames(Method aMethod)
  {
    String[] params = null;
    Class aClass = aMethod.getDeclaringClass();
    try
    {
      ParamReader reader = new ParamReader(aClass);
      params = reader.getParameterNames(aMethod);
    }
    catch (IOException ex)
    {
      params = null;
    }

    if (params == null)
    {
      params = getDefaultParamNames(aMethod);
    }
    return params;
  }

  private String[] getDefaultParamNames(Method aMethod)
  {
    int noOfParams = aMethod.getParameterTypes().length;
    String[] params = new String[noOfParams];
    for (int i = 0; i < noOfParams; i++)
    {
      params[i] = "arg"+(i+1);
    }
    return params;
  }

  public static String getTypeName(Class type)
  {
    if (type.isArray())
    {
      try
      {
        Class cl = type;
        int dimensions = 0;
        while (cl.isArray())
        {
          dimensions++;
          cl = cl.getComponentType();
        }
        StringBuffer sb = new StringBuffer();
        sb.append(cl.getName());
        for (int i = 0; i < dimensions; i++)
        {
          sb.append("[]");
        }
        return sb.toString();
      }
      catch (Throwable e) { /*FALLTHRU*/ }
    }
    return type.getName();
  }


}