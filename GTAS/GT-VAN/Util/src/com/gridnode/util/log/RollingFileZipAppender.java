/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RollingFileZipAppender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 24, 2006   i00107              Created
 */
package com.gridnode.util.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Layout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.spi.LoggingEvent;

import com.gridnode.util.SystemUtil;

 
/**
   RollingFileZipAppender extends FileAppender to backup and zip the log files when
   they reach a certain size.

*/
public class RollingFileZipAppender extends RollingFileAppender
{
  /**
   * By default, the Log file will not be closed until it is full or the systm exit.
   */
  protected int _safeLog = -1;

  protected String _datePattern = "'.'yyyy-MM-dd-HH-mm"; // must be yyyy, not YYYY -dmw

  protected String _logFN = "log"; // file name without extension

  protected String _logFileExt = "txt";

  private java.io.PrintWriter _pw;
  private FileOutputStream _logOS;

  public RollingFileZipAppender()
  {
  }

  /**
   * Instantiate a RollingFileZipAppender and open the file designated by
   * <code>filename</code>. The opened filename will become the ouput
   * destination for this appender.
   * <p>If the <code>append</code> parameter is true, the file will be
   * appended to. Otherwise, the file desginated by
   * <code>filename</code> will be truncated before being opened.
   */
  public RollingFileZipAppender(Layout layout, String filename, boolean append)
    throws IOException
  {
    super(layout, filename, append);
  }

  /**
   * Instantiate a FileAppender and open the file designated by
   * <code>filename</code>. The opened filename will become the output
   * destination for this appender.
   * <p>The file will be appended to.  
   */
  public RollingFileZipAppender(Layout layout, String filename)
    throws IOException
  {
    super(layout, filename);
  }

  /**
   * Implemetns the usual roll over behaviour.
   * <p>If <code>MaxBackupIndex</code> is positive, then files
   * {<code>File.1</code>, ..., <code>File.MaxBackupIndex -1</code>}
   * are renamed to {<code>File.2</code>, ...,
   * <code>File.MaxBackupIndex</code>}. Moreover, <code>File</code> is
   * renamed <code>File.1</code> and closed. A new <code>File</code> is
   * created to receive further log output. And the olderFiles are ziped
   * to <code>File.time.zip</code>.
   * <p>If <code>MaxBackupIndex</code> is equal to zero, then the
   * <code>File</code> is truncated with no backup files created.
   */
  public void rollOver()
  {
    File target;
    File file;

    // If maxBackups <= 0, then there is no file renaming to be done.
    if(maxBackupIndex > 0)
    {
      // Delete the oldest file, to keep Windows happy.
      file = new File(_logFN + '.' + maxBackupIndex + "." + _logFileExt);
      if (file.exists())
      {
        File[] files2Zip = new File[maxBackupIndex];
        int fileIndex = 0;
       // Map {(maxBackupIndex - 1), ..., 2, 1} to {maxBackupIndex, ..., 3, 2}
        for (int i = maxBackupIndex; i >= 1; i--)
        {
          file = new File(_logFN + "." + i + "." + _logFileExt);
          if (file.exists())
          {
            files2Zip[fileIndex] = file;
            fileIndex++;
          }
        }

        File[] fileArray = new File[fileIndex];
        System.arraycopy(files2Zip, 0, fileArray,0, fileIndex);

        Date now = new Date();
        now.setTime(System.currentTimeMillis()) ;
        SimpleDateFormat sdf = new SimpleDateFormat (_datePattern);
        String dateString = sdf.format(now);

        String zipFileName = fileName+dateString+".zip";
        File zipfile = new File(zipFileName);

        if(zipfile.exists())
        {
          zipfile.delete();
        }
        else try
        {
          zip(zipFileName, fileArray);
          for(int i = 0; i<fileIndex; i++)
          {
            fileArray[i].delete();
          }
        }
        catch(Throwable t)
        {
          err("Error in zipping the Log Files", t);
          // Map {(maxBackupIndex - 1), ..., 2, 1} to {maxBackupIndex, ..., 3, 2}
          for (int i = maxBackupIndex; i >= 1; i--)
          {
            file = new File(_logFN + "." + i + "." + _logFileExt);
            if (file.exists())
            {
              target = new File(_logFN + "." + dateString + '.' + (i) + "." + _logFileExt);
              file.renameTo(target);
            }
          }
        }
      }
      else
      {
        // Map {(maxBackupIndex - 1), ..., 2, 1} to {maxBackupIndex, ..., 3, 2}
        for (int i = maxBackupIndex - 1; i >= 1; i--)
        {
          file = new File(_logFN + "." + i + "." + _logFileExt);
          if (file.exists())
          {
            target = new File(_logFN + '.' + (i + 1) + "." + _logFileExt);
            file.renameTo(target);
          }
        }
      }

      // Rename fileName to fileName.1
      target = new File(_logFN + "." + 1 + "." + _logFileExt);
      this.closeFile(); // keep windows happy.
      file = new File(fileName);
      file.renameTo(target);
    }

    try
    {
      // This will also close the file. This is OK since multiple
      // close operations are safe.
      this.setFile(fileName, false, this.bufferedIO, this.bufferSize);
    }
    catch(IOException e)
    {
      err("setFile("+fileName+", false) call failed.", e);
    }
  }


  public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize)
    throws IOException
  {
  	//NSL20051109 If log filename is not absolute, make it relative to the SystemUtil.workingDir
  	if (!new File(fileName).isAbsolute())
  	{
  		fileName = new File(SystemUtil.getWorkingDirPath(), fileName).getCanonicalPath();
  	}
    super.setFile(fileName, append, this.bufferedIO, this.bufferSize);
    _logFileExt = fileName.substring(fileName.indexOf('.')+1);
    _logFN = fileName.substring(0, fileName.indexOf('.'));
  }

  public void setSafeLog(int safeLog)
  {
    this._safeLog = safeLog;
  }


  public void setDatePattern(String datePattern)
  {
    this._datePattern = datePattern;
  }

  public int getSafeLog()
  {
    return this._safeLog;
  }


  public String getDatePattern()
  {
    return this._datePattern;
  }

  protected int _numberCount = 0;
  /**
   *  This method differentiates RollingFileAppender from its super
   *  class.
   */
  protected void subAppend(LoggingEvent event)
  {
    super.subAppend(event);
    _numberCount ++;
    if((fileName != null) &&
       ((CountingQuietWriter) qw).getCount() >= maxFileSize)
    {
       this.rollOver();
       _numberCount = 0;
    }

    else if( (this._safeLog>0) && (_numberCount >= this._safeLog) )
    {
      _numberCount = 0;

      try
      {
        setFile(fileName, true, this.bufferedIO, this.bufferSize);
      }
      catch(IOException e)
      {
        err("setFile("+fileName+", false) call failed.", e);
      }
    }
  }

  /**
   * Zip the specified files into a single zip file
   * @param zipFileName  The name of the zip file to create
   * @param fileToZip The files to zip up
   * @throws IOException
   */
  public void zip(String zipFileName, File[] fileToZip)
    throws IOException
  {
    debug("Creating zip "+zipFileName);
    File zipFile = new File(zipFileName);

    //make sure any parent directories exist before creating zip file.
    zipFile.getParentFile().mkdirs();

    ZipOutputStream zipOS = null;
    FileOutputStream os = new FileOutputStream(zipFile.getAbsolutePath(), false);

    try
    {
      zipOS = new ZipOutputStream(os);
      zipOS.setMethod(ZipOutputStream.DEFLATED);
      zipOS.setLevel(9);

      for (int i = 0; i < fileToZip.length; i++)
        addFileToZip(zipOS, fileToZip[i], "");

      debug("Finish creating zip file " + zipFileName);
    }
    catch (Exception ex)
    {
      throw new IOException(ex.getLocalizedMessage());
    }
    finally
    {
      try
      {
        if (zipOS != null) zipOS.close();
        if (os != null) os.close();
      }
      catch (Exception ex) {}
    }
  }

 /**
   * Adds new zip entries into a zip file.
   *
   * @param zipOS The ZIP file to add to
   * @param fileToAdd The File(s) to add to the zip. If it is a directory,
   * any sub-directories & files under it are also added to the zip file.
   * @param path The "Path" attribute for <I>fileToAdd</I> in zip file
   * @exception Exception Error adding a file to zip
   */
  public void addFileToZip(
    ZipOutputStream zipOS, File fileToAdd, String path)
    throws Exception
  {
    String newPath = path + fileToAdd.getName();
    if (fileToAdd.isFile())
    {
      debug("Adding file "+newPath + " to zip");

      ZipEntry zipEntry = new ZipEntry(newPath);
      zipEntry.setSize(fileToAdd.length());
      zipEntry.setTime(fileToAdd.lastModified());
      zipOS.putNextEntry(zipEntry);

      CRC32 checksum = new CRC32();
      FileInputStream is = new FileInputStream(fileToAdd);

      try
      {
        byte[] buff = new byte[1024];
        int num = 0;
        while ((num=is.read(buff)) > 0)
        {
          zipOS.write(buff,0,num);
          checksum.update(buff,0,num);
        }
      }
      finally
      {
        try
        {
          if (is != null) is.close();
        }
        catch (Exception ex) { }
        zipEntry.setCrc(checksum.getValue());
        zipOS.closeEntry();
      }
    }
    else
    {
      newPath += File.separator;
      debug("Adding directory "+newPath + " to zip");
      ZipEntry zipEntry = new ZipEntry(newPath);
      zipEntry.setSize(fileToAdd.length());
      zipEntry.setTime(fileToAdd.lastModified());
      zipOS.putNextEntry(zipEntry);
      zipOS.closeEntry();

      File[] fileList = fileToAdd.listFiles();
      for (int i=0; i<fileList.length; i++)
      {
        try
        {
          addFileToZip(zipOS, fileList[i], newPath);
        }
        catch (IOException ex)
        {
          err("Error adding directory " + newPath + " to zip", ex);
          throw ex;
        }
      }
    }
  }

  /**
   * Print a debugging message.
   *
   * @param msg The message to be logged
   */
  public void debug(String msg)
  {
    //LogLog.debug(msg);
    createLogOS();
    if (_pw != null)
      _pw.println(msg);

  }

  /**
   * Print a debugging message.
   *
   * @param msg The message to be logged
   */
  public void err(String msg, Throwable ex)
  {
    //LogLog.error(msg,ex);
    createLogOS();
    if (_pw != null)
    {
      _pw.println(msg);
      ex.printStackTrace(_pw);
    }

  }
  
  private synchronized void createLogOS()
  {
    if (_pw == null)
    {
      try
      {
        _logOS = new FileOutputStream("log.log");
        _pw = new java.io.PrintWriter(_logOS, true);
      }
      catch (Exception ex) {

      }
    }
  }

}
