/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LocalClassLoader.java
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

import java.io.*;
import java.net.*;

public class LocalClassLoader extends AbstractMultiClassLoader
{
  URL _classpathURL = null;

  public LocalClassLoader(String localclasspath) throws Exception
  {
    super(new URL(
            DEFAULT_PROTOCOL_TYPE +
            (localclasspath.endsWith("/")||localclasspath.endsWith("\\")?
            localclasspath:localclasspath+File.separator)));
    _classpathURL = new URL(
            DEFAULT_PROTOCOL_TYPE +
            (localclasspath.endsWith("/")||localclasspath.endsWith("\\")?
            localclasspath:localclasspath+File.separator));
  }

  public LocalClassLoader(String localclasspath,ClassLoader parent) throws Exception
  {
    super(new URL(
            DEFAULT_PROTOCOL_TYPE +
            (localclasspath.endsWith("/")||localclasspath.endsWith("\\")?
            localclasspath:localclasspath+File.separator)),parent);
    _classpathURL = new URL(
            DEFAULT_PROTOCOL_TYPE +
            (localclasspath.endsWith("/")||localclasspath.endsWith("\\")?
            localclasspath:localclasspath+File.separator));
  }


  public LocalClassLoader(URL classpathURL) throws Exception
  {
    super(classpathURL);
    _classpathURL = classpathURL;
  }

  public LocalClassLoader(URL classpathURL,ClassLoader parent) throws Exception
  {
    super(classpathURL,parent);
    _classpathURL = classpathURL;
  }

  public LocalClassLoader(URL[] classpathURLs) throws Exception
  {
    super(classpathURLs);
  }

  public LocalClassLoader(URL[] classpathURLs,ClassLoader parent) throws Exception
  {
    super(classpathURLs,parent);
  }


  public String getClassName()
  {
    if (_classpathURL != null)
    {
      try
      {
        InputStream is = _classpathURL.openStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int c = bis.read();
        while (c != -1)
        {
          out.write(c);
          c = bis.read();
        }
        byte[] ba = out.toByteArray();
        Class theClass = defineClass(null, ba, 0, ba.length);
        return theClass.getName();
      }
      catch (IOException e)
      {
      }
    }
    return null;
  }
}