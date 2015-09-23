/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ObjectMappingRegistry.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 24 2006    Neo Sok Lay         Created
 * Jun 11 2007    Tam Wei Xiang       Added method for retrieve the current node id
 */
package com.gridnode.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Utility class to provide access to global properties
 *  
 * @author Neo Sok Lay
 */
public class SystemUtil
{
  public static final String DEF_HOSTID = "gnode1";
  public static final String HOSTID_PROP_KEY = "hostid";
  
	/**
	 * Get the working directory for the application. This may or may not be
	 * a local directory path, depending on whether the application is running on
	 * a cluster. Any file access to application files must be relative to this workingDir.
	 * 
	 * @return The working directory for the application. If the system property "sys.global.dir" is set,
	 * the working directory is taken as "sys.global.dir"/GNapps, otherwise, the working directory
	 * is taken as the system property "user.dir".
	 */
	public static String getWorkingDirPath()
	{
		String globalDir = System.getProperty("sys.global.dir", null);
		if (globalDir == null)
		{
			return System.getProperty("user.dir");
		}
		globalDir += "/GNapps";
		return globalDir;
	}
	
	/**
	 * Get the URL to the working directory for the application.
	 * @return The URL for the working directory of the application
	 */
	public static URL getWorkingDirURL()
	{
		try
		{
			return getWorkingDir().toURI().toURL();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get the file handle to the application working directory
	 * @return The File handle to application working directory.
	 */
	public static File getWorkingDir()
	{
		return new File(getWorkingDirPath());
	}
  
  public static Properties getResourceProperties(String resPath)
  {
    Properties props = new Properties();
    InputStream is = ClassLoader.getSystemResourceAsStream(resPath);
    
    if (is != null)
    {
      try
      {
        props.load(is);
      }
      catch (IOException ex)
      {
        System.err.println("Unable to load resource "+resPath+": "+ex.getMessage());
      }
    }
    return props;
  }
  
  public static Properties getResourceProperties(Class currClass, String resPath)
  {
    Properties props = new Properties();
    InputStream is = currClass.getResourceAsStream(resPath);
    if (is == null)
    {
      is = ClassLoader.getSystemResourceAsStream(resPath);
    }
    
    if (is != null)
    {
      try
      {
        props.load(is);
      }
      catch (IOException ex)
      {
        System.err.println("Unable to load resource "+resPath+": "+ex.getMessage());
      }
    }
    System.out.println("Got resource properties: "+props);
    return props;
  }
  
  /**
   * Get the hostid for the running application. If the property is
   * not set of is an empty string, the default hostid {@link #DEF_HOSTID} will
   * be returned.
   * @return The "hostid" property set for the running application.
   */
  public static String getHostId()
  {
    String hostid = System.getProperty(HOSTID_PROP_KEY, DEF_HOSTID);
    if (hostid.length()==0)
    {
      hostid = DEF_HOSTID;
    }
    return hostid;
  }

}
