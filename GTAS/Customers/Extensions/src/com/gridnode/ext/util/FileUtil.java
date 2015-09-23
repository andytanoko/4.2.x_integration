/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 18, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.ext.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Calendar;

import com.gridnode.pdip.framework.util.UUIDUtil;

/**
 * @author Tam Wei Xiang
 * @version GT 4.1.3
 */
public class FileUtil
{
  public static boolean isFileExist(String filepath)
  {
    return (new File(filepath)).exists();
  }
  
  public static boolean isDirectory(String filepath)
  {
    return (new File(filepath)).isDirectory();
  }
  
  public static File createTempFile(String fileExtension) throws IOException
  {
    return File.createTempFile(generateRandomFileName(), fileExtension);
  }
  
  public static String getFileExtension(File file)
  {
    String filename = file.getName();
    int index = filename.lastIndexOf(".");
    if(index <= -1)
    {
      return "";
    }
    else
    {
      return filename.substring(index+1, filename.length());
    }
  }
  
  private static String generateRandomFileName()
  {
    return UUIDUtil.getRandomUUIDInStr();
  }
  
  /**
   * Housekeep the file if the file lastModified date older than the specified number of day
   * @param file
   * @param day
   */
  public static void housekeepFiles(File file, int day) throws Exception
  {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - day);
    long timeInMilli = c.getTimeInMillis();
    
    deleteFiles(file, timeInMilli);
  }
  
  /**
   * This method deletes the files whose lastModified <= timeout
   * If the file object represents the folder then it will delete
   * all the files from folder and its subfolders matching the 
   * above criteria
   * 
   * @param file
   * @param timeOut 
   * @throws Exception
   */
  public static void deleteFiles(File file,long timeOut) throws Exception
  {
    if(file!=null)
    {
      if(file.isFile())
      {
        if(file.lastModified() <=timeOut)
          file.delete();
      }
      else if(file.isDirectory())
      {
        File[] files=file.listFiles();
        if(files!=null)
        {
          for(int i=0;i<files.length;i++)
          {
            if(files[i].isDirectory())
            {
              deleteFiles(files[i],timeOut);
            }
            else
            {
              if(files[i].lastModified() <=timeOut)
                files[i].delete();
            }
          }
        }
      }
    }
  }
  
  /**
   * Lock the given file
   * @param file
   */
  public static boolean lockFile(File file) throws Exception
  {
    FileLock lock = null;
    FileOutputStream output = null;
      output = new FileOutputStream(file);
      FileChannel channel = output.getChannel();
      lock = channel.tryLock();
      if(lock == null)
      {
        return false;
      }
      else
      {
        return true;
      }

  }
  
  public static File saveFiles(File destFolder, String filename, byte[] content) throws Exception
  {
    ByteArrayInputStream in = null;
    FileOutputStream out = null;
    
    try
    {
      File outFile = new File(destFolder+"/"+filename);
      in = new ByteArrayInputStream(content);
      out = new FileOutputStream(outFile);
      byte[] buffer = new byte[512];
      int readSoFar = 0;
      while( (readSoFar = in.read(buffer)) != -1)
      {
        out.write(buffer, 0, readSoFar);
      }
      return outFile;
    }
    catch(Exception ex)
    {
      throw new Exception("Failed to save files:"+ex.getMessage(), ex);
    }
    finally
    {
      try
      {
        if(in != null)
        {
          in.close();
        }
        if(out != null)
        {
          out.close();
        }
      }
      catch(Exception ex)
      {
        ex.printStackTrace();
      }
    }
  }
  
  public static String getFilenameWithoutExtension(String filename)
  {
    return filename.lastIndexOf(".") >=0 ? filename.substring(0, filename.lastIndexOf(".")) : filename;
  }
}
