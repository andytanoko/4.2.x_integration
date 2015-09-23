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
package com.gridnode.ftp.scheduler.FTPScheduler;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import com.gridnode.ftp.FTPPull;
import com.gridnode.ftp.facade.FTPBackendConnector;

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
 * Oct 14, 2008        Tam Wei Xiang       Created
 */
/**
 * @author Tam Wei Xiang
 * @version
 * @since
 */
public class FTPScheduler
{
  private static String WORKING_DIRECTORY = System.getProperty("user.dir");
  private static String ftpFolder="";
  private static String be="";
  private static String docType="";
  private static String partner="";
  
  public static void main(String[] args) throws Exception
  {    
    RunFTPPullTask task = new RunFTPPullTask();
    
    long runInterval = getRunInterval() * 1000;
    System.out.println("FTP Pull task started. Run interval is "+runInterval+"s");
    
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(task, new Date(), runInterval);
  }
  
  private static long getRunInterval() throws Exception
  {
    try
    {
      Properties configProps = new Properties();
      configProps.load(new FileInputStream(new File(WORKING_DIRECTORY+"/conf/ftp.cfg")));
      String runInterval = configProps.getProperty("invoke.interval", "7200");
      return Long.parseLong(runInterval);
    }
    catch(Exception ex)
    {
      throw new Exception("Configuration can't be read properly", ex);
    }
  }
}

class RunFTPPullTask extends TimerTask
{
  public void run()
  {
    try
    {
      System.out.println("Start FTPPull task at: "+new Date());   
      FTPThread analyser = new FTPThread();
      new Thread(analyser).start();
      
    }
    catch(Exception ex)
    {
      System.err.println("Can't invoke FTPThread "+ex.getMessage());;
    }
  }
}

class FTPThread implements Runnable
{
  public void run()
  {
    try
    {      
      FTPPull ftp = new FTPPull();
      ftp.startPull();
    }
    catch(Exception ex)
    {
      System.err.println("Can't invoke FTPPull "+ex.getMessage());
    }
  }
}

