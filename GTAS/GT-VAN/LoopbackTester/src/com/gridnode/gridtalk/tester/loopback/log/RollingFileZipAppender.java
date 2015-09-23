/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RollingFileZipAppender.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 23, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.log;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @todo Class documentation
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class RollingFileZipAppender extends RollingFileAppender
{
  /**
   * By default, the Log file will not be closed until it is full or the systm exit.
   */
  protected int safeLog = -1;


  protected String datePattern = "'.'yyyy-MM-dd-HH-mm"; // must be yyyy, not YYYY -dmw

  protected String sFileName = "log"; // file name without extension

  protected String fileExt = "txt";


  /**
     The default constructor simply calls its {@link
     FileAppender#FileAppender parents constructor}.  */
  public RollingFileZipAppender()
  {
    super();
  }

  /**
    Instantiate a RollingFileZipAppender and open the file designated by
    <code>filename</code>. The opened filename will become the ouput
    destination for this appender.

    <p>If the <code>append</code> parameter is true, the file will be
    appended to. Otherwise, the file desginated by
    <code>filename</code> will be truncated before being opened.
  */
  public RollingFileZipAppender(Layout layout, String filename, boolean append)
    throws IOException
  {
    super(layout, filename, append);
  }

  /**
     Instantiate a FileAppender and open the file designated by
    <code>filename</code>. The opened filename will become the output
    destination for this appender.

    <p>The file will be appended to.  */
  public RollingFileZipAppender(Layout layout, String filename)
    throws IOException
  {
    super(layout, filename);
  }

//  /**
//     Retuns the option names for this component, namely {@link
//     #MAX_FILE_SIZE_OPTION} and {@link #MAX_BACKUP_INDEX_OPTION} in
//     addition to the options of {@link FileAppender#getOptionStrings
//     FileAppender}.  */
//  public  String[] getOptionStrings()
//  {
//    return OptionConverter.concatanateArrays(super.getOptionStrings(),
//		 new String[] {MAX_FILE_SIZE_OPTION, MAX_BACKUP_INDEX_OPTION, SAFE_LOG, DATE_PATTERN_OPTION});
//  }


  /**
     Implemetns the usual roll over behaviour.

     <p>If <code>MaxBackupIndex</code> is positive, then files
     {<code>File.1</code>, ..., <code>File.MaxBackupIndex -1</code>}
     are renamed to {<code>File.2</code>, ...,
     <code>File.MaxBackupIndex</code>}. Moreover, <code>File</code> is
     renamed <code>File.1</code> and closed. A new <code>File</code> is
     created to receive further log output. And the olderFiles are ziped
     to <code>File.time.zip</code>.
     <p>If <code>MaxBackupIndex</code> is equal to zero, then the
     <code>File</code> is truncated with no backup files created.
   */

  public // synchronization not necessary since doAppend is alreasy synched
  void rollOver()
  {
    File target;
    File file;

    // If maxBackups <= 0, then there is no file renaming to be done.
    if(maxBackupIndex > 0)
    {
      // Delete the oldest file, to keep Windows happy.
//      file = new File(fileName + '.' + maxBackupIndex + );
      file = new File(sFileName + '.' + maxBackupIndex + "." + fileExt);
      if (file.exists())
      {
        File[] files2Zip = new File[maxBackupIndex];
        int fileIndex = 0;
       // Map {(maxBackupIndex - 1), ..., 2, 1} to {maxBackupIndex, ..., 3, 2}
        for (int i = maxBackupIndex; i >= 1; i--)
        {
//          file = new File(fileName + "." + i);
          file = new File(sFileName + "." + i + "." + fileExt);
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
        SimpleDateFormat sdf = new SimpleDateFormat (datePattern);
        String dateString = sdf.format(now);

        String zipFileName = fileName+dateString+".zip";
        File zipfile = new File(zipFileName);

         if(zipfile.exists())
             zipfile.delete();
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
//            file = new File(fileName + "." + i);
            file = new File(sFileName + "." + i + "." + fileExt);
            if (file.exists())
            {
//              target = new File(fileName + dateString + '.' + (i));
              target = new File(sFileName + "." + dateString + '.' + (i) + "." + fileExt);
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
//          file = new File(fileName + "." + i);
          file = new File(sFileName + "." + i + "." + fileExt);
          if (file.exists())
          {
//            target = new File(fileName + '.' + (i + 1));
            target = new File(sFileName + '.' + (i + 1) + "." + fileExt);
            file.renameTo(target);
          }
        }
      }

      // Rename fileName to fileName.1
//      target = new File(fileName + "." + 1);
      target = new File(sFileName + "." + 1 + "." + fileExt);
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


  public
  synchronized
  void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize)
    throws IOException
  {
    super.setFile(fileName, append, this.bufferedIO, this.bufferSize);
    fileExt = fileName.substring(fileName.lastIndexOf('.')+1); //NSL20060103 incase filepath contains '.', assumes always have file extension
    sFileName = fileName.substring(0, fileName.lastIndexOf('.')); //NSL20060103 incase filepath contains '.'
  }

  /**
	 * @param safeLog  The safeLog to set.
	 * @uml.property  name="safeLog"
	 */
  public void setSafeLog(int safeLog)
  {
    this.safeLog = safeLog;
  }


  /**
	 * @param datePattern  The datePattern to set.
	 * @uml.property  name="datePattern"
	 */
  public void setDatePattern(String datePattern)
  {
    this.datePattern = datePattern;
  }

  /**
	 * @return  Returns the safeLog.
	 * @uml.property  name="safeLog"
	 */
  public int getSafeLog()
  {
    return this.safeLog;
  }


  /**
	 * @return  Returns the datePattern.
	 * @uml.property  name="datePattern"
	 */
  public String getDatePattern()
  {
    return this.datePattern;
  }

   /**
     Set RollingFileAppender specific options.
     In addition to {@link FileAppender#setOption FileAppender
     options} RollingFileAppender recognizes the options
     <b>MaxFileSize</b> and <b>MaxBackupIndex</b>.


     <p>The <b>MaxFileSize</b> determines the size of log file
     before it is rolled over to backup files. This option takes an
     long integer in the range 0 - 2^63. You can specify the value
     with the suffixes "KB", "MB" or "GB" so that the integer is
     interpreted being expressed respectively in kilobytes, megabytes
     or gigabytes. For example, the value "10KB" will be interpreted
     as 10240.

     <p>The <b>MaxBackupIndex</b> option determines how many backup
     files are kept before the oldest being erased. This option takes
     a positive integer value. If set to zero, then there will be no
     backup files and the log file will be truncated when it reaches
     <code>MaxFileSize</code>.

   */
//  public
//  void setOption(String key, String value) {
//    super.setOption(key, value);
//    if(key.equalsIgnoreCase(MAX_FILE_SIZE_OPTION)) {
//      maxFileSize = OptionConverter.toFileSize(value, maxFileSize + 1);
//    }
//    else if(key.equalsIgnoreCase(MAX_BACKUP_INDEX_OPTION)) {
//      maxBackupIndex = OptionConverter.toInt(value, maxBackupIndex);
//    }
//    else if(key.equalsIgnoreCase(SAFE_LOG)) {
//      safeLog = OptionConverter.toInt(value, safeLog);
//    }
//
//   else if(key.equalsIgnoreCase(DATE_PATTERN_OPTION)) {
//      datePattern = value;
//    }
//
//  }



  protected int _numberCount = 0;
  /**
     This method differentiates RollingFileAppender from its super
     class.

     @since 0.9.0
  */
  protected
  void subAppend(LoggingEvent event)
  {
    super.subAppend(event);
    _numberCount ++;
    if((fileName != null) &&
       ((CountingQuietWriter) qw).getCount() >= maxFileSize)
    {
       this.rollOver();
       _numberCount = 0;
    }

    else if( (this.safeLog>0) && (_numberCount >= this.safeLog) )
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


//  public
//  void activateOptions()
//   {
//    if(fileName != null)
//    {
//      try
//      {
//	     setFile(fileName, fileAppend);
//      }
//      catch(java.io.IOException e)
//      {
//        err("setFile("+fileName+","+fileAppend+") call failed. Error code is "+
//            ErrorCode.FILE_OPEN_FAILURE,  e);
//
//        fileName = fileName + ".retry";
//         try
//        {
//         setFile(fileName, fileAppend);
//        }
//        catch(java.io.IOException ex)
//        {
//           errorHandler.error("setFile("+fileName+","+fileAppend+") call  Retry failed.",
//              ex, ErrorCode.FILE_OPEN_FAILURE);
//        }
//      }
//    } else
//    {
//      //LogLog.error("File option not set for appender ["+name+"].");
//      LogLog.warn("File option not set for appender ["+name+"].");
//      LogLog.warn("Are you using FileAppender instead of ConsoleAppender?");
//    }
//  }

  public static void zip(String zipFileName, File[] fileToZip)
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
   *
   * @since 1.0a build 0.9.9.6
   */
  public static void addFileToZip(
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
  public static void debug(String msg)
  {
    LogLog.debug(msg);
  }

  /**
   * Print a debugging message.
   *
   * @param msg The message to be logged
   */
  public static void err(String msg, Throwable ex)
  {
    LogLog.error(msg,ex);
  }
}
