/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WriteLog.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 30 2002    Srinath	                Created
 * May 14 2003    Neo Sok Lay             Log in designated directory.
 * Jun 23 2003    Neo Sok Lay             Moved from helpers package.
 */

package com.gridnode.pdip.app.alert.engine;

import com.gridnode.pdip.app.alert.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.app.alert.helpers.ITempDirConfig;
import com.gridnode.pdip.framework.file.access.FileAccess;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.OutputStream;

/**
 * This class is used to write the alert details into the log files.
 * @author Srinath
 * @version 2.1
 */

public class WriteLog
{

  protected static final WriteLog _logFile = new WriteLog();

  private WriteLog()
  {
  }

  public static WriteLog getInstance()
  {
    return _logFile;
  }

  /**
   * Write a message to a log file.
   *
   * @param relativeFilePath Relative file path to the designated log directory.
   * @param logMessage The message to log.
   * @param isAppend <b>true</b> to append the logMessage to the log file, <b>false</b>
   * otherwise.
   */
  public synchronized boolean writeToLog(String relativeFilePath,
                                          String logMessage,
                                          boolean isAppend)
                                          throws Throwable
  {
    FileOutputStream fos = null;
    DataOutputStream dos = null;
    try
    {
      String filePath = getLogFilePath(relativeFilePath);

      if(isAppend)
        fos = new FileOutputStream(filePath, isAppend);
      else
        fos = new FileOutputStream(filePath);

      dos = new DataOutputStream(fos);
      dos.writeBytes(logMessage + "\r\n");
      dos.flush();
      return true;
    }
    catch (Exception e)
    {
      AlertLogger.warnLog("WriteLog", "appendToLog", "Error appending to Alert Log file", e);
      return false;
    }
    finally
    {
      close(dos);
      close(fos);
    }
  }

  /**
   * Close an output stream
   *
   * @param os The output stream to close.
   */
  private void close(OutputStream os)
  {
    try
    {
      if (os != null)
        os.close();
    }
    catch (Exception e)
    {
      AlertLogger.warnLog("WriteLog", "close", "Exception while closing the outputstream ", e);
    }
  }

  /**
   * Get the path to the designated log directory.
   *
   * @return The full path to the designated log directory.
   */
  private String getLogDir()
  {
    try
    {
      // make sure the base log dir is created
      if (!FileUtil.exist(ITempDirConfig.LOG_DIR, ""))
      {
        String path = FileUtil.getPath(ITempDirConfig.LOG_DIR);
        FileAccess fA = new FileAccess(FileUtil.getDomain());
        fA.createFolder(path);
      }
      return FileUtil.getFile(ITempDirConfig.LOG_DIR, "").getCanonicalPath();
    }
    catch (Exception ex)
    {
      AlertLogger.warnLog("WriteLog", "getLogDir", "Error", ex);
      return "";
    }
  }

  /**
   * Get the full path to a log file.
   *
   * @param relativeFilePath Relative path of the log file from the designated
   * log directory.
   *
   * @return The full path to the log file.
   */
  private String getLogFilePath(String relativeFilePath) throws Exception
  {
    String logFilePath = null;
    try
    {
      File logDir = new File(getLogDir(), relativeFilePath);

      //make sure any directories under the base log dir exist before write the log.
      logDir.getParentFile().mkdirs();

      logFilePath = logDir.getCanonicalPath();
    }
    catch (IOException ex)
    {
      AlertLogger.warnLog("WriteLog", "getLogFilePath", "Error", ex);
      throw new Exception(ex.getMessage());
    }
    return logFilePath;
  }

/*
  public static void main(String[] args) throws Throwable
  {
    WriteLog.getInstance().appendToLog("test1.log", "log message 1- no append", false);
    WriteLog.getInstance().appendToLog("test2.log", "log message 2- append", true);
    WriteLog.getInstance().appendToLog("test1.log", "log message 3- no append", false);
    WriteLog.getInstance().appendToLog("test2.log", "log message 4- append", true);
  }
*/
}
