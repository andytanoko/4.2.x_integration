/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JarClassLoader.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 02 2002    Jagadeesh               Created
 * Oct 17 2002    Ang Meng Hua            Refactor to enhance code hygiene
 * Jul 07 2003    Koh Han Sing            Added methods getAllClassName,
 *                                        getMethods
 * Jul 11 2003    Koh Han Sing            Repackage into framework from base
 *                                        userprocedure
 */
package com.gridnode.pdip.framework.util.classloader;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.Attributes;

public class JarClassLoader extends AbstractMultiClassLoader
  implements Serializable
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8063157808030894334L;
	private static final String JAR_PROTOCOL =  "jar";
  private static final String JAR_SEPARATOR  =  "!/";

  public JarClassLoader(URL url) throws Exception
  {
    super(url);
  }

  public JarClassLoader(URL[] urls) throws Exception
  {
    super(urls);
  }

  public JarClassLoader(String protocol, String jarPath, String jarName)
    throws Exception
  {
    // syntax of a JAR URL is: jar:<url>!/{entry}
    super(new URL(
            JAR_PROTOCOL,
            "",
            protocol +
            ((jarPath.endsWith("/")||jarPath.endsWith("\\"))?
            jarPath:jarPath + File.separator) +
            jarName + JAR_SEPARATOR));
  }

  public JarClassLoader(String jarPath, String jarName)
          throws Exception
  {
    // syntax of a JAR URL is: jar:<url>!/{entry}
    super(new URL(
            JAR_PROTOCOL,
            "",
            DEFAULT_PROTOCOL_TYPE +
            ((jarPath.endsWith("/")||jarPath.endsWith("\\"))?
            jarPath:jarPath + File.separator) +
            jarName + JAR_SEPARATOR));
  }

  public JarClassLoader(String jarPath, String jarName,ClassLoader parentClassLoader)
          throws Exception
  {
    // syntax of a JAR URL is: jar:<url>!/{entry}
    super(new URL(
            JAR_PROTOCOL,
            "",
            DEFAULT_PROTOCOL_TYPE +
            ((jarPath.endsWith("/")||jarPath.endsWith("\\"))?
            jarPath:jarPath + File.separator) +
            jarName + JAR_SEPARATOR),parentClassLoader);
  }



  public String getClassName() throws IOException
  {
    URL[] jarURLs = getURLs();
    for(int i=0;i<jarURLs.length;i++)
    {
      JarURLConnection uc = (JarURLConnection)jarURLs[i].openConnection();
      Attributes attr = uc.getMainAttributes();
      if(attr != null)
        return attr.getValue(Attributes.Name.MAIN_CLASS);
    }
    return null;
  }

//  static public Collection getJarClassNameList(InputStream input) throws IOException
//  {
//    ArrayList classList = new ArrayList();
//    JarInputStream jarIn = new JarInputStream(input);
//    JarEntry jE = null;
//    while((jE = jarIn.getNextJarEntry()) != null)
//    {
//      if (jE.getName().endsWith(".class"))
//      {
//        String classname = jE.getName();
//        classname = classname.replace('/','.');
//        classname = classname.replace('\\','.');
//        classname = classname.substring(0,classname.indexOf(".class"));
//        classList.add(classname);
//      }
//    }
//    return classList;
//  }

  public Collection getAllClassName() throws IOException
  {
    ArrayList classList = new ArrayList();
    URL[] jarURLs = getURLs();
    for(int i=0;i<jarURLs.length;i++)
    {
      JarURLConnection uc = (JarURLConnection)jarURLs[i].openConnection();
      
      Enumeration what = uc.getJarFile().entries();
      while (what.hasMoreElements())
      {
        String entry = what.nextElement().toString();
        if (entry.endsWith(".class"))
        {
          entry = entry.replace('/', '.');
          entry = entry.replace('\\', '.');
          entry = entry.substring(0, entry.indexOf(".class"));
          classList.add(entry);
        }
      }
    }
    
    return classList;
  }
  
  //TWX 20100603 the workaround for preventing the jar file being locked is
  //kind of work around, and we are not sure the impact to the rest of the system.
//  public Collection getAllJavaBinaryClassName() throws IOException
//  {
//    ArrayList classList = new ArrayList();
//    URL[] jarURLs = getURLs();
//    
//    for(int i=0;i<jarURLs.length;i++)
//    {
//      JarURLConnection uc = (JarURLConnection)jarURLs[i].openConnection();
//      JarFile test = uc.getJarFile();
//      Enumeration what = test.entries();
//      
//      while (what.hasMoreElements())
//      {
//        String entry = what.nextElement().toString();
//        if (entry.endsWith(".class"))
//        {
//          entry = entry.replace('/', '.');
//          entry = entry.replace('\\', '.');
//          entry = entry.substring(0, entry.indexOf(".class"));
//          classList.add(entry);
//        }
//      }
//
//      try
//      {
//       Class jarFileFactory = Class.forName("sun.net.www.protocol.jar.JarFileFactory");
//
//       Field fileCache = jarFileFactory.getDeclaredField("fileCache");
//       Field urlCache = jarFileFactory.getDeclaredField("urlCache");
//
//       fileCache.setAccessible(true);
//       fileCache.set(null,new HashMap());
//
//       urlCache.setAccessible(true);
//       urlCache.set(null,new HashMap());
//      }
//      catch (Exception e)
//      {
//        e.printStackTrace();
//      }
//      
//      test.close();
//      
//      //workaround to allow GridTalk to delete jar
//      /*File actualFile = new File(uc.getJarFileURL().getFile());
//      File tempFile = new File(uc.getJarFileURL().getFile()+".bak");
//      
//      InputStream in = new FileInputStream(actualFile);
//      OutputStream out = new FileOutputStream(tempFile);
//
//      byte[] buf = new byte[1024];
//      int len;
//      while ((len = in.read(buf)) > 0)
//      {
//        out.write(buf, 0, len);
//      }
//      in.close();
//      out.close();
//      
//      System.out.println("Delete status:" + actualFile.delete());
//      
//      tempFile.renameTo(new File(uc.getJarFileURL().getFile()));*/
//      //end of workaround to allow GridTalk to delete jar
//      
//      
//
//      //uc = (JarURLConnection) new URL("jar:://file_path/").openConnection();
//      
//
//    }
// 
//    return classList;
//  }
}
