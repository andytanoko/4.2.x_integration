/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ErrorLog.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Aug 2, 2004 			Mahesh             	Created
 * Feb 15, 2007     Chong SoonFui       Changed "err" log to "warn"
 */
package com.gridnode.gtas.server.dbarchive.helpers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class ErrorLog
{
  
  File logFile=null;
  PrintStream ps = null;
  public ErrorLog() throws Exception
  {
    
  }
  
  public void open() throws Exception
  {
    logFile=File.createTempFile("dbarchive",".log");
    ps=new PrintStream(new BufferedOutputStream(new FileOutputStream(logFile)));
  }
  
  public synchronized void logError(String message,Throwable th)
  {
  	// CSF20070215 this log is changed from "error" to "warn" 
  	// because these logs will be printed in the log files while the end-user 
  	// will receive summary message after the archive process done.
    Logger.warn(message,th);
    try
    {
      if(ps!=null)
      {
        ps.println(message);
        if(th!=null)
          th.printStackTrace(ps);
        ps.flush();
      }
    }
    catch(Throwable th1)
    {
    }
  }
  
  public void close()
  {
    if(ps!=null)
    {
      ps.close();
      ps=null;
    }
  }

  public File getLogFile()
  {
    return logFile;
  }
  
}
