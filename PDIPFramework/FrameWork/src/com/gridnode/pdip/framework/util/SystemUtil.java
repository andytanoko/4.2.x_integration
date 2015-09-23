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
 * Oct 27 2005    Neo Sok Lay         Created
 * Mar 10 2008    Tam Wei Xiang       #6 Get the global log dir
 * May 21 2008    Tam Wei Xiang       #46 Get the sys temp dir
 */
package com.gridnode.pdip.framework.util;

import java.io.File;
import java.net.URL;

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
		//System.setProperty("user.dir", globalDir);
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
  
  /**
   * Get the global log dir.
   * @return the property set in "logs.home.dir" or return "" if the property is not set
   */
  public static String getLogDir()
  {
	  return System.getProperty("logs.home.dir", "");
  }
  
  /**
   * Get the system temp dir
   * @return the property set in "java.io.tmpdir" or return "" if it has not been set
   */
  public static String getSysTempDir()
  {
    return System.getProperty("java.io.tmpdir", "");
  }
}
