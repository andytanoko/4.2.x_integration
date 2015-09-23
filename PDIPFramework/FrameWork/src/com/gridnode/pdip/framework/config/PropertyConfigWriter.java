package com.gridnode.pdip.framework.config;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Jagadeesh.
 * @version 1.0
 */


 import java.io.*;
 import java.util.Properties;

  /**
   * Writes the Configuration Properties to the external data store.
   *
   *  This in further release will be deprecated since the File Services
   *  will be used for Reading/Writing to external data store.
   */

 public class PropertyConfigWriter implements IConfigWriter
 {

  public PropertyConfigWriter()
  {
  }

  /**
   * Writes to the external data store.
   *
   * @param p_path Path to the external data store.
   * @param p_properties Properties to be written.
   */

  public void write(String p_path,Properties p_properties)
  {
    try
    {
      Properties l_properties  = p_properties;
      FileOutputStream l_fis = new FileOutputStream(new File(p_path));
      l_properties.store(l_fis,"");
      l_fis.close();
    }
    catch(Exception ex)
    {
      System.out.println(" Exception in writing the File - write() - PropertyConfigWriter() "+ex.getMessage());
      ex.printStackTrace();
    }

  }


 }