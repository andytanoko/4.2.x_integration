/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SoapLocalClassLoader.java
 *
 ****************************************************************************
 * Date           Author            Changes
 ****************************************************************************
 * Oct 29 2001    Qingsong         Created
 *
 */
package com.gridnode.pdip.base.userprocedure.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;


public class SoapLocalClassLoader extends ClassLoader
{
  boolean isFromjar = false;
  String  fileName = "";
  String  libpath = null;
  HashMap classHash = null;
  HashMap classIndHash = null;

  public SoapLocalClassLoader()
  {
  }

  protected static byte[] getBytes(Class c) throws IOException
  {
    InputStream fin = c.getResourceAsStream('/' + c.getName().replace('.', '/') + ".class");
    if(fin == null)
      throw new IOException("Cannot get bytes for " + c.getName());
    byte abyte0[];
    try
    {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      byte buf[] = new byte[1024];
      int actual;
      do
      {
        actual = fin.read(buf);
        if(actual > 0)
            out.write(buf, 0, actual);
      } while(actual > 0);
      abyte0 = out.toByteArray();
    }
    finally
    {
      fin.close();
    }
    return abyte0;
  }

  public byte[] loadClassBytes(String name) throws IOException, ClassNotFoundException
  {
    Class aclass = loadClass(name);
    return loadClassBytes(aclass);
  }

  public byte[] loadClassBytes(Class name) throws IOException
  {
    try
    {
      return getBytes(name);
    }
    catch (Exception ex)
    {
      return getBytes(name.getName());
    }
  }

  protected byte[] getBytes(String name) throws IOException
  {
    Object ob = classHash.get(name);
    if(ob != null)
     return (byte[])ob;
    else
     throw new IOException("Cannot get bytes for " + name);
  }

  public Class findClass(String name)
    throws ClassNotFoundException
  {
    try
    {
      return Class.forName(name);
    }
    catch (Exception ex)
    {
      try
      {
        Class aClass = findLoadedClass(name);
        if(aClass != null)
          return aClass;
        return super.findClass(name);
      }
      catch(Exception e)
      {
        byte[] b = (byte[])classHash.get(name);
        if(b == null)
        {
          try
          {
            File file = new File(libpath,name.substring(name.lastIndexOf(".") +1) + ".class");
            FileInputStream fileinputstream = new FileInputStream(file);
            b = new byte[fileinputstream.available()];
            fileinputstream.read(b);
            fileinputstream.close();
          }
          catch(Exception ex1)
          {
            throw new ClassNotFoundException("GNClassLoader Cannot Load Class:" + name);
          }
        }
        if(b != null)
        {
          return defineClass(null,b,0,b.length);
        }
        throw new ClassNotFoundException("GNClassLoader Cannot Load Class:" + name);
      }
    }
  }

  public Class[] loadClasses(String fileName) throws IOException, ClassNotFoundException
  {
    isFromjar = false;
    classHash = new HashMap();
    classIndHash = new HashMap();
    readClassFromFile(fileName);
    return buildClass();
  }

  public Class[] loadClasses(InputStream input, boolean isJar) throws IOException, ClassNotFoundException
  {
    if(isJar)
      return loadClassFromJar(input);
    else
      return loadClassFromClass(input);
  }


  public Class[] loadClassFromJar(InputStream input) throws IOException, ClassNotFoundException
  {
    isFromjar = true;
    classHash = new HashMap();
    classIndHash = new HashMap();
    readJarData(input);
    return buildClass();
  }

  public Class[] loadClassFromClass(InputStream input) throws IOException, ClassNotFoundException
  {
    isFromjar = false;
    classHash = new HashMap();
    classIndHash = new HashMap();
    readClassData(input);
    return buildClass();
  }

  private Class[] buildClass()
    throws ClassNotFoundException
  {
    if(classHash  == null || classHash.size() <= 0)
    {
      return null;
    }
    Class[] Classes = new Class[classHash.size()];
    byte[] cacheb = null;
    String cachename = null;
    for(int i = 0; i< classHash.size();i++)
    {
      Class newClass = loadClass(classIndHash.get(new Integer(i)).toString(), true);
      Classes[i] = newClass;
      if(!isFromjar && classIndHash.get(new Integer(i)).toString().equals(fileName))
      {
        cacheb = (byte[])classHash.get(fileName);
        cachename = newClass.getName();
      }
    }
    classIndHash.clear();
    classIndHash = null;
    if(cacheb != null)
      classHash.put(cachename, cacheb);
    return Classes;
  }

  private void readClassFromFile(String fileName)
    throws IOException
  {
    libpath = new File(fileName).getParentFile().getAbsolutePath();
    FileInputStream fileinputstream = new FileInputStream(fileName);
    if (!fileName.endsWith(".jar"))
      readClassData(fileinputstream, fileName);
    else
      readJarData(fileinputstream);
  }

  static public String[] v2A(Vector v)
  {
    if(v == null)
      return new String[0];
    String[] sa = new String[v.size()];
    for(int i =0; i < v.size();i++)
      sa[i] = v.get(i).toString();
    return sa;
  }

  static public String[] getJarClassNameList(InputStream input) throws IOException
  {
    Vector nameList = new Vector();
    JarInputStream jarIn = new JarInputStream(input);
    JarEntry jE = null;
    while((jE = jarIn.getNextJarEntry()) != null)
    {
      if (jE.getName().endsWith(".class"))
      {
        String classname = jE.getName();
        classname = classname.replace('/','.');
        classname = classname.replace('\\','.');
        classname = classname.substring(0,classname.indexOf(".class"));
        nameList.add(classname);
      }
    }
    return v2A(nameList);
  }

  private void readJarData(InputStream input)
    throws IOException
  {
    JarInputStream jarIn = new JarInputStream(input);
    JarEntry jE = null;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    while((jE = jarIn.getNextJarEntry()) != null)
    {
      if (jE.getName().endsWith(".class"))
      {
        int c = jarIn.read();
        while (c != -1)
        {
          out.write(c);
          c = jarIn.read();
        }
        String classname = jE.getName();
        classname = classname.replace('/','.');
        classname = classname.replace('\\','.');
        classname = classname.substring(0,classname.indexOf(".class"));
        if(classHash.get(classname) == null)
        {
          classHash.put(classname, out.toByteArray());
          classIndHash.put(new Integer(classHash.size()-1),classname);
        }
        out.reset();
      }
    }
  }

  private void readClassData(InputStream input)
    throws IOException
  {
    readClassData(input, "TempGNClassLoaderClass.class");
  }

  private void readClassData(InputStream input, String fileName)
    throws IOException
  {
    this.fileName = fileName;
    byte [] buf = new byte[input.available()];
    input.read(buf);
    input.close();
    classHash.put(fileName,buf);
    classIndHash.put(new Integer(classHash.size()-1),fileName);
  }
}