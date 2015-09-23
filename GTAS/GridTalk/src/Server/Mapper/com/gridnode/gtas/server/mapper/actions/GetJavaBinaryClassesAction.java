/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: ${file_name}
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * ??             Wong Ming Qian
 * Jun 02 2010    Tam Wei Xiang       #1100: load classname directly from Zip entries
 */

package com.gridnode.gtas.server.mapper.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.gridnode.gtas.events.mapper.GetJavaBinaryClassesEvent;
import com.gridnode.gtas.model.mapper.IMappingFile;

import com.gridnode.gtas.server.mapper.helpers.Logger;
import com.gridnode.gtas.server.mapper.helpers.MappingFilePathHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;
import com.gridnode.pdip.app.mapper.model.IMappingRule;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.classloader.JarClassLoader;

/**
 * 
 * @author Wong Ming Qian
 * @since 4.2.2
 */
public class GetJavaBinaryClassesAction extends AbstractGridTalkAction
{
  /**
   * 
   */
  private static final long serialVersionUID = -5209678151748416156L;
  public static final String ACTION_NAME = "GetJavaBinaryClassesAction";

  protected IEventResponse constructErrorResponse(IEvent event,
                                                  TypedException ex)
  {
    // TODO Auto-generated method stub
    return null;
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    GetJavaBinaryClassesEvent binaryEvent = (GetJavaBinaryClassesEvent) event;

    String path = MappingFilePathHelper.getInstance()
        .getConfigPath(IMappingFile.JAVA_BINARY);
    Collection fullClassList = new ArrayList();
    Collection classList = new ArrayList();
    MappingFile mappingFile = getManager()
        .findMappingFile(binaryEvent.getJavaBinaryClassesUid());

    File javaFile = FileUtil.getFile(mappingFile.getPath(), mappingFile
        .getFilename());

    if (javaFile == null)
    {
      throw new ApplicationException("Error in getting java binary class name, javaFile is null!");
    }
    else
    {
      // java binary file is found
//      JarClassLoader loader = new JarClassLoader(javaFile.getParent(), javaFile
//          .getName(), getClass().getClassLoader());
      
      fullClassList = getClassnameEntries(javaFile);
      classList = filterClassNames(getDocumentConfig(), fullClassList);

      // loader = null;
      // mappingFile = null;

      // sm.setAttribute(CLASSLOADER_KEY, loader);
      return constructEventResponse(classList);
    }
  }
  
  //TWX 20100602 #1100 instead of using JarClassLoader for the list of classname, 
  //                   we will generate base on ZipFile entry to prevent the
  //                   jar file get locked by the system.
  private Collection<String> getClassnameEntries(File javaFile) throws SystemException
  {
    Logger.debug("[GetJavaBinaryClassesAction] load classname entries");
    
    ZipFile zip;
    try
    {
      zip = new ZipFile(javaFile.getAbsoluteFile());
    }
    catch (ZipException e)
    {
      throw new SystemException("Zip error has occured when getting Classname from jar="+javaFile.getAbsolutePath(), e);
    }
    catch (IOException e)
    {
      throw new SystemException("IO error has occured when getting Classname from jar="+javaFile.getAbsolutePath(), e);
    }
    ArrayList<String> classList = new ArrayList<String>();
    Enumeration what = zip.entries();
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
    
    if(zip != null)
    {
      try
      {
        zip.close();
      }
      catch (IOException e)
      {
        Logger.warn("[GetJavaBinaryClassesAction] error in closing zip file", e);
      }
    }
    
    return classList;
  }

  // method removes all class names according to configuration file
  private Collection filterClassNames(Configuration config,
                                      Collection collection)
  {
    Collection classList1 = new ArrayList();
    Collection classList2 = new ArrayList();

    if (config == null)
    {
      System.out.println("CONFIG NULL!");
    }
    else
    {
      // retrieve the exclude/include list (filter will exclude/include class
      // names with the following)
      String exclude = config.getString("mapper.javabinary.exclude");
      String include = config.getString("mapper.javabinary.include");
      
      

      // to differentiate the different strings to exclude
      String[] excludeArray = exclude.split(",");
      String[] includeArray = include.split(",");

      Iterator excludeIterator = collection.iterator();
      CharSequence cs;
      // scans the array to detect the whole list of string to exclude
      
      if (!exclude.trim().equals("") && exclude != null)
      {
        for (int i = 0; i < excludeArray.length; i++)
        {
          cs = excludeArray[i];
          while (excludeIterator.hasNext())
          {
            String className = (String) excludeIterator.next();
            // if it contains, exclude

            if (!className.contains(cs))
            {
              classList1.add(className);
            }
          }
        }
      }
     
      if (exclude.trim().equals("") || exclude == null)
      {
        while (excludeIterator.hasNext())
        {
          String className = (String) excludeIterator.next();
          classList1.add(className);
        }
      }
      
      
      Iterator includeIterator = classList1.iterator();
      // scans the array to include only the list of strings to include
      for (int i = 0; i < includeArray.length; i++)
      {
        cs = includeArray[i];
        while (includeIterator.hasNext())
        {
          String className = (String) includeIterator.next();
          // if it contains, exclude
          if (className.contains(cs))
          {
            classList2.add(className);
          }
        }
      }
    }
    return classList2;
  }

  private Configuration getDocumentConfig()
  {
    return ConfigurationManager.getInstance().getConfig("mapper.java.binary");
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Class getExpectedEventClass()
  {
    return GetJavaBinaryClassesEvent.class;
  }

  private IMappingManagerObj getManager() throws ServiceLookupException
  {
    return (IMappingManagerObj) ServiceLocator
        .instance(ServiceLocator.CLIENT_CONTEXT)
        .getObj(IMappingManagerHome.class.getName(), IMappingManagerHome.class,
                new Object[0]);
  }
}
