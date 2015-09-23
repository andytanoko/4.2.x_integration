/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConfigurationManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 27 2002    Neo Sok Lay         Remove config bug in refreshConfiguration()
 * Oct 27 2005    Neo Sok Lay         Use working directory from "sys.global.dir" property, if specified.
 *                                    Otherwise, use "user.dir"
 */
package com.gridnode.pdip.framework.config;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Jagadeesh.
 * @version 1.0
 */

import java.util.*;
import java.io.*;

import com.gridnode.pdip.framework.util.SystemUtil;

/**
 * <p>
 * Defines a Factory API that enables applications to obtain a ConfigurationManager, that
 * creates a Configuration object.
 *
 * The Client gets a ConfigurationManager instance by using getInstance static method. A ConfigurationManager
 * instance can be assoicated with one or more Configuration Instances.
 * </p>
 *
 * <br>
 * To obtain a ConfigurationManager.,
 * <br>
 * <I>ConfigurationManager configmanager = ConfigurationManager.getInstance();</I>
 *
 * <br>
 * To obtain a Configuration for logging .,
 * <br>
 *<I> Configuration  clog = configManager.getConfig(ConfigurationManager.LOG); </I>
 *
 * <br>
 * To get the Log Properties .,
 * <br>
 *
 * <I> Properties logprops  =  clog.getProperties(); </I>
 */


public class ConfigurationManager implements IConfigurationManager
{
  private Vector g_configs = new Vector(); //Containment of Configuration Objects
  private Properties g_mappings= new Properties(); //Root Properties Container
  private IConfigReader g_reader = null; // Reader to Read mappings from external store.
  private IConfigWriter g_writer = null; // Writer to Write updated Configuratin to external store.
  private String g_configDir=null;       // Root Configuration Directory
  private ObservableConfiguration g_oblConfiguration=null; //ObservableConfiguration for updating the Configurations.
  private static ConfigurationManager g_configManager;  //Single Instance of ConfigurationManager per JVM.


  /**
   * Private Constructor for ConfigurationManager.
   */
  private ConfigurationManager()
  {
    loadConfig();
  }


  /**
   * Gets the Configuration for the given Configuration name.
   *
   *
   * @param configName Name of the configuration to find.
   * @return The configuration found, or <B>null</B> if config properties does not
   * contain a configuration with <I>configName</I>.
   *
   */
  public Configuration getConfig(String configName)
  {
    int size = g_configs.size();

    for (int i=0; i<size; i++)
    {
      Configuration config = (Configuration)g_configs.get(i);
      if (config.getName().equals(configName))
        return config;
    }
   //Get The Config from
    Configuration l_config = createConfiguration(configName);
    if (l_config != null)
      g_configs.add(l_config);

    return l_config;
  }

  /**
   * Creates Configuration for the given config name.
   *
   * @param p_configName Name of the Configuration to create.
   * @return The Configuration Object.
   */
  private Configuration createConfiguration(String p_configName)
  {
    String l_fileLocation = null;
    if(g_mappings.containsKey(p_configName))
    {
      l_fileLocation = (String)g_mappings.get(p_configName);
    }

    if(l_fileLocation != null)
    {
      Properties l_props = g_reader.read(g_configDir+l_fileLocation);
      Configuration l_config = new Configuration();
      l_config.setName(p_configName);
      Enumeration l_keys = l_props.keys();
      while(l_keys.hasMoreElements())
      {
        String l_key = (String)l_keys.nextElement();
        Property l_prop = new Property();
        String l_value = (String)l_props.get(l_key);
        l_prop.setKey(l_key);
        l_prop.setValue(l_value);
        l_config.addProperty(l_prop);
      }
      //g_configs.add(l_config);
      return l_config;
    }
    else
    {
      System.out.println("Please Check /default/config.properties for this Config Entry "+p_configName);
      return null;
    }
  }

  /**
   * Gives a short description of this configuration group object.
   *
   * @return A summary of this configuration group.
   *
   * @since 1.0a build 0.9.9.6
   */
/*  public String toString()
  {
    return "Group:       " + getName() + "\n" +
           "  Configurations: " + "\n" +
           "    " + _configs.toString();
  }
*/

  /**
   * Load's the initial configuration data for external data store.
   *
   * This method is called only once when instance of ConfigurationManager
   * is been created.
   */
  private void loadConfig()
  {
    try
    {
      //g_configDir = "."+File.separatorChar+"conf"+File.separatorChar+"default" + File.separatorChar;
    	g_configDir = SystemUtil.getWorkingDirPath() + "/conf/default/";
      g_mappings.load(new FileInputStream(g_configDir + "config.properties"));
      String l_readerClassName = (String)g_mappings.get("reader");
      String l_writerClassName = (String)g_mappings.get("writer");
      if(l_readerClassName != null)
      {
        g_reader = (IConfigReader)Class.forName(l_readerClassName).newInstance();
      }
      if(l_writerClassName != null)
      {
        g_writer = (IConfigWriter)Class.forName(l_writerClassName).newInstance();
      }
      g_oblConfiguration = new ObservableConfiguration();
    }
    catch(Exception ex)
    {
      System.out.println("Exception in Configuraing the Service " +ex.getMessage());
    }
  }
  
  /**
   *  Gets the ConfigurationManager instance.
   *  @return The ConfigurationManager instance.
   */
  public static synchronized ConfigurationManager getInstance()
  {
    if(g_configManager != null)
    {
      return g_configManager;
    }
    else
    {
      g_configManager = new ConfigurationManager();
    }
    return g_configManager;
  }

  /**
   * Add's the Configuration Object to be Observed.
   *
   * Since any Configuration can be observed for update/refresh., The client
   * registers the Configuration to be observed.
   *
   * @param p_configuration The Configuration object to be observed.
   */
  public void  addObservableConfiguration(Configuration p_configuration)
  {
    g_oblConfiguration.addObserver(p_configuration);
  }

  /**
   * Refersh the Configuration object.
   *
   * If a configuration is modified/changed externally., this method enables
   * the configuration object to reflect the changes made externally.
   * @param l_config The configuration object to be refreshed.
   */
  public void refreshConfiguration(Configuration l_config)
  {
    String l_configName = l_config.getName();

    refreshConfiguration(l_configName);
  }


  /**
   *  Refersh the Configuration object.
   *
   *  @param p_configName The Configuration name to be refreshed.
   */
  public void refreshConfiguration(String p_configName)
  {
    int found = -1;
    synchronized (g_configs)
    {
      int size = g_configs.size();
      for (int i=0; i<size; i++)
      {
        Configuration config = (Configuration)g_configs.get(i);
        if (config.getName().equals(p_configName))
        {
          //g_configs.remove(i);
          found = i;
          break;
        }
      }

      Configuration l_newconfig = createConfiguration(p_configName);
      if (found == -1)
        g_configs.add(l_newconfig);
      else
        g_configs.set(found, l_newconfig);

      //System.out.println("Configs: "+g_configs);
      g_oblConfiguration.updateConfiguration(l_newconfig);
    }
  }

  /**
   * Updates the Configuration.
   *
   * Update's the configuration, and notify the observers with this
   * updated configuration.
   *
   * @param p_config Configuration to be updated.
   */
  public synchronized void  updateConfiguration(Configuration p_config)
  {
    String l_configName = p_config.getName();

    int found = -1;
    synchronized (g_configs)
    {
      int size = g_configs.size();
      for(int i=0;i<size;i++)
      {
        Configuration config = (Configuration)g_configs.get(i);
        if(config.getName().equals(l_configName))
        {
           //g_configs.remove(i);
           found = i;
           break;
        }
      }
      writeConfig(p_config);
      if (found == -1) //add if not already exists, will not be written out
        g_configs.add(p_config);
      else
        g_configs.set(found, p_config);

      g_oblConfiguration.updateConfiguration(p_config);
    }
  }

  /**
   * Writes the modified configuration to external data source.
   *
   * @param p_config Configuration Object to be written
   */
  private void writeConfig(Configuration p_config)
  {
    try
    {
      String l_configName = p_config.getName();
      if(g_mappings.containsKey(l_configName))
      {
        String l_path  = (String)g_mappings.get(l_configName);
        l_path = g_configDir+l_path;
        Properties l_props = p_config.getProperties();
        g_writer.write(l_path,l_props);
      }
    }
    catch(Exception ex)
    {
      System.out.println(" Cannot Write Config to File "+ex.getMessage());
      ex.printStackTrace();
    }
  }

  /*
  public static void main(String args[])
  {
    try
    {
      ConfigurationManager l_manager = ConfigurationManager.getInstance();
      Configuration config = ConfigurationManager.getInstance().getConfig(IFrameworkConfig.FRAMEWORK_DATABASE_CONFIG);
      System.out.println("Are...."+config.getProperties());
      Configuration config1 = ConfigurationManager.getInstance().getConfig(IFrameworkConfig.FRAMEWORK_JNDI_CONFIG);
      System.out.println("Are...."+config1.getProperties());
     // Configuration config2 = l_manager.getConfig(ConfigurationManager.TRANSPORT);
  //    Configuration uconfig = ConfigurationManager.getInstance().getConfig("user");
  //    System.out.println("B$  "+config.toString());
  //    System.out.println("B$  "+uconfig.toString());
//      ConfigurationManager.getInstance().addObservableConfiguration(config);
//      l_manager.addObservableConfiguration(config2);
  //    ConfigurationManager.getInstance().addObservableConfiguration(uconfig);
//      Property l_property = new Property();
//      l_property.setKey("TopicConnectionFactory66");
//      l_property.setValue("java:/ConnectionFactory88");
//      config.addProperty(l_property);
//      l_property = new Property();
//      l_property.setKey("conntection16");
//      l_property.setValue("java:/value45");
//      config2.addProperty(l_property);
//      ConfigurationManager.getInstance().updateConfiguration(config);
//      l_manager.updateConfiguration(config2);
      //Thread.currentThread().sleep(10000);

      //ConfigurationManager.getInstance().updateConfiguration("transport");
     // ConfigurationManager.getInstance().updateConfiguration("user");
      //      Configuration lpconfig = ConfigurationManager.getInstance().getConfig("transport");
    //  System.out.println("A$  "+config.toString());
    //  System.out.println("A$  "+uconfig.toString());
    }
    catch(Exception ex)
    {
      System.out.println(" Exception is "+ex.getClass());
      ex.printStackTrace(System.err);
    }

  }*/
}