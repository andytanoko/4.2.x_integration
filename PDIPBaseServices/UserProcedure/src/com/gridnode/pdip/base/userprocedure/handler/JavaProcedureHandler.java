/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JavaProcedureHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 02 2002    Jagadeesh               Created
 * Oct 17 2002    Ang Meng Hua            Refactor to enhance code hygiene
 * Jan 18 2003    Jagadeesh               Added:To handle Pre/Post Processing of
 *                                        UserProcedure.
 * Feb 02 2003    Jagadeesh               Added:To get parsed CmdLine Expression
 * Feb 04 2003    Jagadeesh               Modified: PARAM Args to be replaced in
 *                                        AbstractProcedureHandler.
 * Nov 28 2003    Koh Han Sing            Modified to extends from
 *                                        AbstractJavaRelatedProcedureHandler so
 *                                        that the param type conversion can be
 *                                        moved up to be used by other Java
 *                                        related userprocedure
 * Mar 21 2006    Neo Sok Lay             GNDB00017182: return exit value from native
 *                                        execution.                                       
 */
package com.gridnode.pdip.base.userprocedure.handler;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import com.gridnode.pdip.base.userprocedure.exceptions.UserProcedureExecutionException;
import com.gridnode.pdip.base.userprocedure.helpers.IProcedureDefFilePathConfig;
import com.gridnode.pdip.base.userprocedure.helpers.Logger;
import com.gridnode.pdip.base.userprocedure.helpers.NativeExecutable;
import com.gridnode.pdip.base.userprocedure.model.JavaProcedure;
import com.gridnode.pdip.base.userprocedure.model.ProcedureHandlerInfo;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.classloader.IMultiClassLoader;
import com.gridnode.pdip.framework.util.classloader.JarClassLoader;
import com.gridnode.pdip.framework.util.classloader.LocalClassLoader;



public class JavaProcedureHandler extends AbstractJavaRelatedProcedureHandler
{
  private static final String CLASS_NAME =  "JavaProcedureHandler";
  //private static final int UNDEFINED_STATUS_CODE = -1;

  public JavaProcedureHandler()
  {
  }

  public Object execute(ProcedureHandlerInfo info)
      throws UserProcedureExecutionException,SystemException
  {
   final String MESSAGE_FORMAT = "["+CLASS_NAME+"][execute]";
   String pathToClass = null;
   String className = null;
   String fileName = null;
   String fullPath = null;
   //HashMap paramMap = null;
   try
   {
      Logger.log(MESSAGE_FORMAT+"Executing Java Procedure begin ...");

      fullPath = info.getFullPath();
      fileName = info.getFileName();
      //paramMap = info.getParamMap();

      JavaProcedure javaProcedure = (JavaProcedure)info.getProcedureDef();

      Logger.debug(MESSAGE_FORMAT+" FileName:"+fullPath);
      Logger.debug(MESSAGE_FORMAT+" FileName:"+fileName);

      boolean isLocal = javaProcedure.isLocal();
      className = javaProcedure.getClassName();
      if(fileName.endsWith(".class"))
       pathToClass = createFileAtPath(fullPath,fileName,javaProcedure.getClassName());
      if(isLocal)
      {
        return exceuteLocalUserProcedure(info);
      }
      else
      {
        int status = executeNativeUserProcedure(info);
        Logger.debug(MESSAGE_FORMAT+" native userprocedure exec status="+status);
        return status;
      }
   }
   catch(Exception ex)
   {
    throw new UserProcedureExecutionException(MESSAGE_FORMAT+
        " Cannot Execute UserProcedure\n"+ex.getMessage(),ex);
   }
   finally
   {
     try{
         if((pathToClass != null))
         {
           FileUtil.delete(IProcedureDefFilePathConfig.PATH_CLASSES,
             getSubPathByClassName(className),fileName);
         }
     }catch(Exception ex){
         throw new UserProcedureExecutionException(MESSAGE_FORMAT+
            " Cannot Delete UserProcedure File\n"+ex.getMessage(),ex);
     }
   }

  // return null;
  }


  private Object exceuteLocalUserProcedure(ProcedureHandlerInfo info)
    throws Exception
  {
    String fullPath = info.getFullPath();
    String fileName = info.getFileName();
    Object resultValue = null;
    if(fileName.endsWith(".jar"))
    {
       JarClassLoader jarClassLoader = new JarClassLoader(fullPath, fileName,
        getClass().getClassLoader());
       resultValue =  invokeClass(jarClassLoader, info);
    }
    else if(fileName.endsWith(".class"))
    {
      Logger.log("FullPath:" + fullPath);
      LocalClassLoader localClassLoader = new LocalClassLoader(fullPath,getClass().getClassLoader());
      resultValue = invokeClass(localClassLoader,info);
    }
    return resultValue;
  }

  private int executeNativeUserProcedure(ProcedureHandlerInfo info)
    throws Exception
  {
     int status = -1;
     final String MESSAGE_FORMAT = "["+CLASS_NAME+"][executeNativeUserProcedure()]";
     JavaProcedure javaProcedure = (JavaProcedure)info.getProcedureDef();
     Vector paramVect = info.getParamDef();
     String fileName = info.getFileName();
     String fullPath = info.getFullPath();
     String className   = javaProcedure.getClassName();
     String cmdExpr = null;
     String delimitedArgs = javaProcedure.getArguments();
     String actualArgs = replaceDelimitedText(delimitedArgs,
                                              IProcedureHandler.ARGUMENT_DELIMITER,
                                              paramVect);

     if(fileName.endsWith(".jar")) //Ex Path: java -cp [..\PATH_TO_JAR\+JAR_NAME] CLASS_NAME ARGUMENTS
     {
       cmdExpr = "java -cp "+fullPath+fileName+" "+className+" "+ actualArgs;
       Logger.debug(MESSAGE_FORMAT+"Invoking NativeExecutable with arguments " + cmdExpr);
       status = NativeExecutable.executeNative(cmdExpr);
       Logger.debug(MESSAGE_FORMAT+" Exit Status "+status);
     }
     else if(fileName.endsWith(".class")) //Ex Path: java -cp [..\PATH_TO_CLASSES\] CLASS_NAME ARGUMENTS
     {
       cmdExpr = "java -cp "+fullPath+" "+className+" "+actualArgs;
       Logger.debug(MESSAGE_FORMAT+"Invoking NativeExecutable with arguments " + cmdExpr);
       status = NativeExecutable.executeNative(cmdExpr);
       Logger.debug(MESSAGE_FORMAT+" Exit Status "+status);
     }
     return status;
  }


  public Object invokeClass(
    IMultiClassLoader classLoader,
    ProcedureHandlerInfo info)
    throws UserProcedureExecutionException,SystemException
  {
   final String MESSAGE_HEADER = "["+ CLASS_NAME +"][invokeClass()]";
   String className = null;
   String methodName = null;
   try
   {
    //HashMap paramMap = info.getParamMap();
    Vector paramDefVect  = info.getParamDef();

    JavaProcedure javaProcedure = (JavaProcedure)info.getProcedureDef();
    className  = javaProcedure.getClassName();
    methodName = javaProcedure.getMethodName();
    String arguments  = javaProcedure.getArguments();

    Logger.debug(MESSAGE_HEADER+"ClassName="+className);
    Logger.debug(MESSAGE_HEADER+"Arguments="+arguments);
    Logger.debug(MESSAGE_HEADER+"MethodName="+methodName);

    Object result = null;

    if(classLoader instanceof JarClassLoader)
    {
      result = invokeJarClassLoader(classLoader,className,methodName,paramDefVect);
      return result;
    }
    else if(classLoader instanceof LocalClassLoader)
    {
      result = invokeLocalClassLoader(classLoader,className,methodName,paramDefVect);
      return result;
    }
   }catch(ClassNotFoundException clex){
     throw new UserProcedureExecutionException(MESSAGE_HEADER+"Class Not Found With"+
     "Class Name"+className+"\n"+clex.getMessage(),clex);
   }catch(NoSuchMethodException nex){
     throw new UserProcedureExecutionException(MESSAGE_HEADER+"No Method Defined With"+
     "Method Name"+methodName+"\n"+nex.getMessage(),nex);
   }catch(InvocationTargetException iex){
     iex.printStackTrace();
     throw new UserProcedureExecutionException(MESSAGE_HEADER+"Could Not Invoke on Class"+
     " or Method\n"+iex.getMessage(),iex);
   }catch(IllegalAccessException iaex){
     throw new UserProcedureExecutionException(MESSAGE_HEADER+"\n"+iaex.getMessage(),iaex);
   }catch(InstantiationException insexp){
     throw new UserProcedureExecutionException(MESSAGE_HEADER+"Could Not Instantiate\n"+
      insexp.getMessage(),insexp);
   }catch(IOException ioex){
     throw new UserProcedureExecutionException(MESSAGE_HEADER+"\n"+ioex.getMessage(),ioex);
   }catch(Exception ex){
     throw new UserProcedureExecutionException(MESSAGE_HEADER+"Unable To Execute UserProcedure\n"+
     ex.getMessage(),ex);
   }
   return null;
  }
  /*
  private String resolvePathToClass(String fullpath)
  {
    // eg. fullpath  = "..\.....\data\sys\classes\com\...\"
    // calling this method will return "..\.....\data\sys\classes"
    return fullpath.substring(0, fullpath.indexOf(File.separator+"classes")+8);
  }

  private String resolveClassName(String filename, String fullpath)
  {
    // eg. fullpath  = ..\.....\data\sys\classes\com\...\
    //     filename = Test.class
    // calling this method will return com.xxx.xxxx....xxx.Test
    String classname = filename.substring(0, filename.lastIndexOf("."));
    String qualifiedPath = fullpath.substring(fullpath.indexOf(
                             File.separator+"classes"+File.separator)+9);
    qualifiedPath = qualifiedPath.replace(File.separatorChar,'.');
    classname = qualifiedPath + classname;
    return classname;
  }

  private String resolveClassName(String filename)
  {
    return filename.substring(0, filename.indexOf("class")-1);
  }
  */


  private String createFileAtPath(String filePath,String fileName,String className)
    throws Exception
  {
    final String MESSAGE_FORMAT = "["+CLASS_NAME+"][createFileAtPath()] ";
    try
    {
      String actualClassPath = getSubPathByClassName(className);
      Logger.debug(MESSAGE_FORMAT+actualClassPath);
      String file =  FileUtil.create(IProcedureDefFilePathConfig.PATH_CLASSES,
                                    actualClassPath,
                                    fileName,
                                    new File(filePath+File.separator+fileName));
      return file;
    }
    catch(FileAccessException ex)
    {
      ex.printStackTrace();
      Logger.warn(MESSAGE_FORMAT+"Cannot Create File At Path"+ex.getMessage());
      throw ex;
    }
  }

  private String getSubPathByClassName(String className)
  {
    if((className != null) && (className.indexOf(".") > -1))
    {
      String actualClassPath = className.substring(0,className.lastIndexOf("."));
      actualClassPath = actualClassPath.replace('.','/')+"/";
      return actualClassPath;
    }
    return className;
  }

  private Object invokeJarClassLoader(
    IMultiClassLoader classLoader,
    String className,
    String methodName,
    Vector paramVect)
      throws ClassNotFoundException,
             NoSuchMethodException,
             InvocationTargetException,
             IllegalAccessException,
             InstantiationException,
             IOException,
             Exception

  {
      if(className != null)
      {
        if(methodName != null)
        {
           Vector paramList = setParamTypeValue(paramVect);
           Class[]  paramType = (Class[])paramList.get(0);
           Object[] paramValue = (Object[])paramList.get(1);
           return classLoader.invokeClassMethod(
                   className,
                   methodName,
                   null,
                   null,
                   paramType,
                   paramValue);
        }
        else
        {
          // invoke the main method
          classLoader.invokeClass(className, new String[]{});
          return null;
        } // end of method.
      }
      else // if classname is null
      {
        String mainClass = classLoader.getClassName(); // get the main class
        if(methodName != null)
        {
          Vector paramList = setParamTypeValue(paramVect);
          Class[]  paramType = (Class[])paramList.get(0);
          Object[] paramValue = (Object[])paramList.get(1);

          return classLoader.invokeClassMethod(
                   mainClass,
                   methodName,
                   null,
                   null,
                   paramType,
                   paramValue);
        }
        else
        {
          classLoader.invokeClass(mainClass, new String[]{});
          return null;
        }
      }
  }


  private Object invokeLocalClassLoader(
    IMultiClassLoader classLoader,
    String className,
    String methodName,
    Vector paramVect)
      throws ClassNotFoundException,
             NoSuchMethodException,
             InvocationTargetException,
             IllegalAccessException,
             InstantiationException,
             IOException,
             Exception
  {
    if(methodName != null)
    {
      Vector paramList = setParamTypeValue(paramVect);
      Class[]  paramType = (Class[])paramList.get(0);
      Object[] paramValue = (Object[])paramList.get(1);
      return classLoader.invokeClassMethod(
               className,
               methodName,
               null,
               null,
               paramType,
               paramValue);
    }
    else
    {
      classLoader.invokeClass(className, new String[]{});
      return null;
    }

  }


  public void castValues(Object obj)
  {

  }


/*

  private void setParamTypeValue(
    HashMap paramMap,
    Class[] paramType,
    Object[] paramValue)
  {
    Set enm = paramMap.keySet();
    enm.iterator();
    paramType  = new Class[col.size()];
    paramValue = new Object[col.size()];
    Iterator iter = col.iterator();
    Hashtable ht=null;
    int i=0;
    while(iter.hasNext())
    {
      Object value  = iter.next();
      paramType[i]  = (Class)classMaps.get(value.getClass().getName());
      try
      {
        paramType[i] = Class.forName((String)classMaps.get(value.getClass().getName()));
        paramValue[i] = value;
        i++;
      }
      catch(Exception e)
      {
        e.printStackTrace();
     }
    }
  }

*/


}
