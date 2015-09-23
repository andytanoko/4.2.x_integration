package com.gridnode.pdip.base.userprocedure.helpers;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;

import org.apache.axis.*;
import org.apache.axis.components.compiler.*;
import org.apache.axis.components.compiler.Compiler;
import org.apache.axis.transport.http.*;


public class WebServicesCompiler
{
  Compiler compiler = null;
  String   compilererror;
  public static boolean isJar(InputStream is)
  {
    try
    {
      JarInputStream jis = new JarInputStream(is);
      if(jis.getNextEntry() != null)
        return true;
      return false;
    }
    catch (Exception ex)
    {
      return false;
    }
  }

  private String getDefaultClasspath(MessageContext msgContext)
  {
    StringBuffer classpath = new StringBuffer();
    for(ClassLoader cl = Thread.currentThread().getContextClassLoader(); cl != null; cl = cl.getParent())
    {
      if(cl instanceof URLClassLoader)
      {
        URL urls[] = ((URLClassLoader)cl).getURLs();
        for(int i = 0; urls != null && i < urls.length; i++)
        {
          String path = urls[i].getPath();
          if(path.length() >= 3 && path.charAt(0) == '/' && path.charAt(2) == ':')
              path = path.substring(1);
          classpath.append(path);
          classpath.append(File.pathSeparatorChar);
          File file = new File(urls[i].getFile());
          if(file.isFile())
          {
            FileInputStream fis = null;
            try
            {
              fis = new FileInputStream(file);
              if(isJar(fis))
              {
                JarFile jar = new JarFile(file);
                Manifest manifest = jar.getManifest();
                if(manifest != null)
                {
                  Attributes attributes = manifest.getMainAttributes();
                  if(attributes != null)
                  {
                    String s = attributes.getValue(java.util.jar.Attributes.Name.CLASS_PATH);
                    String base = file.getParent();
                    if(s != null)
                    {
                      for(StringTokenizer st = new StringTokenizer(s, " "); st.hasMoreTokens(); classpath.append(File.pathSeparatorChar))
                      {
                        String t = st.nextToken();
                        classpath.append(base + File.separatorChar + t);
                      }
                    }
                  }
                }
              }
            }
            catch(IOException ioe)
            {
              if(fis != null)
              {
                try
                {
                    fis.close();
                }
                catch(IOException ioe2) { }
              }
            }
          }
        }

      }
    }

    if(msgContext != null)
    {
      String webBase = (String)msgContext.getProperty(HTTPConstants.MC_HTTP_SERVLETLOCATION);
      if(webBase != null)
      {
        classpath.append(webBase + File.separatorChar + "classes" + File.pathSeparatorChar);
        try
        {
          String libBase = webBase + File.separatorChar + "lib";
          File libDir = new File(libBase);
          String jarFiles[] = libDir.list();
          for(int i = 0; i < jarFiles.length; i++)
          {
            String jarFile = jarFiles[i];
            if(jarFile.endsWith(".jar"))
              classpath.append(libBase + File.separatorChar + jarFile + File.pathSeparatorChar);
          }
        }
        catch(Exception e) { }
      }
    }
    String bootClassPath = AxisProperties.getProperty("sun.boot.class.path");
    if(bootClassPath != null)
      classpath.append(bootClassPath);
    return classpath.toString();
  }


  public boolean compile(String javasource, String destDir) throws IOException
  {
    compiler.setDestination(destDir);
    compiler.addFile(javasource);
    boolean result = compiler.compile();
    if(!result)
    {
      StringBuffer message = new StringBuffer("Error compiling ");
      message.append(javasource);
      message.append(":\r\n");
      List errors = compiler.getErrors();
      int count = errors.size();
      for(int i = 0; i < count; i++)
      {
        CompilerError error = (CompilerError)errors.get(i);
        if(i > 0)
          message.append("\r\n");
        message.append("Line ");
        message.append(error.getStartLine());
        message.append(", column ");
        message.append(error.getStartColumn());
        message.append(": ");
        message.append(error.getMessage());
      }
      compilererror = message.toString();
    }
    else
      compilererror =  null;
    return result;
  }

  public WebServicesCompiler()
  {
    compiler = CompilerFactory.getCompiler();
    compiler.setClasspath(getDefaultClasspath(MessageContext.getCurrentContext()));
  }

  public static void main(String[] args) throws Exception
  {
    String jfile ="d:/user/qingsong/Project/GT_2.1_Webservices/GTAS/GridTalk/src/WebServicesProvider/com/gridnode/gtas/client/webservicesprovider/TestClass.java";
    WebServicesCompiler webServicesCompiler1 = new WebServicesCompiler();
    if(webServicesCompiler1.compile(jfile, "c:\\"))
      System.out.println("success");
    else
      System.out.println(webServicesCompiler1.getCompilererror());
  }

  public String getCompilererror()
  {
    return compilererror;
  }
}