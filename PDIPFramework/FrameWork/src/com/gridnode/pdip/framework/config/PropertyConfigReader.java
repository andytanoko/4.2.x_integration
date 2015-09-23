package com.gridnode.pdip.framework.config;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Jagadeesh.
 * @version 1.0
 */

 import java.io.File;
 import java.io.FileInputStream;
 import java.util.Properties;

 /**
  * Reads the Properties from the external data store.
  *
  *  This in further release will be deprecated since the File Services
  *  will be used for Reading/Writing to external data store.
  */
 public class PropertyConfigReader implements IConfigReader
 {
    private String g_filePath = null;

  public PropertyConfigReader()
  {
  }

  /**
   * Reads the Properties from the path specified.
   *
   * @param p_path Path/Location of the File.
   * @return Properties read from data store.
   */
  public Properties read(String p_path)
  {
    g_filePath = p_path;
    Properties l_props=null;
    try
    {
      l_props = new Properties();
      File f = new File(g_filePath);
      FileInputStream fis = new FileInputStream(f);
      l_props.load(fis);
      return l_props;
    }
    catch(Exception ex)
    {
       System.out.println(" Cannot Load Properties  - read() - PropertyConfigReader "+ex.getMessage());
       ex.printStackTrace(System.err);
    }
    return null;
  }


   public void setPath(String p_path)
   {
      this.g_filePath = p_path;
   }

   public String getPath()
   {
     return g_filePath;
   }


  }