/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FTPScheduler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 11, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.ftp;

import com.gridnode.ftp.handler.FTPPullClient;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.3 (GTVAN)
 */
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PrivateProcessScheduler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 14, 2008   Tam Wei Xiang       Created
 * Sep 20, 2010   Tam Wei Xiang       #1830 - Reference Inovis FTP Pull client
 *                                            and refactor it.
 */
/**
 * @author Tam Wei Xiang
 * @version
 * @since
 */
public class FTPPull
{
  private static String WORKING_DIRECTORY = System.getProperty("user.dir");
  private static String ftpFolder="";
  private static String be="";
  private static String docType="";
  private static String partner="";
  
  public static void main(String[] args) throws Exception
  { 
    FTPPull ftp = new FTPPull();
    ftp.startPull();
  }
  
  public void startPull() throws Exception
  {
//  start the FTP pull
    FTPPullClient conn = new FTPPullClient();
    conn.pullDocFromFTP();
    
    //Housekeep the log files
    /*
    File logFolder = new File("log");
    System.out.println("Log folder retrieve: "+logFolder.getAbsolutePath());
    FileUtil.housekeepFiles(logFolder, getHouseKeepInterval()); */
  }
  
  /*
  private static int getHouseKeepInterval() throws Exception
  {
    try
    {
      Properties configProps = new Properties();
      configProps.load(new FileInputStream(new File("./conf/ftp.cfg")));
      String runInterval = configProps.getProperty("log.keep.for", "30");
      return Integer.parseInt(runInterval);
    }
    catch(Exception ex)
    {
      throw new Exception("Configuration can't be read properly", ex);
    }
  }*/
}

