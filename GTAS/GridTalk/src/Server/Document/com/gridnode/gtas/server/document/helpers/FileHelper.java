/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 03 2002    Koh Han Sing        Created
 * Oct 07 2003    Koh Han Sing        Organize Gdoc and Udoc into their
 *                                    respective folders.
 * Mar 26 2004    Neo Sok Lay         Add changeFileExtension() method.
 * May 31 2004    Neo Sok Lay         GNDB00024914: Create directory concurrency
 *                                    issue.
 * Apr 05 2006    Tam Wei Xiang       modified getUdocFile(griddocument) to check
 *                                    whether the return file obj is a directory
 *                                    or a physical file.
 * Mar 12 2007    Neo Sok Lay         Use UUID for unique filename.                                                                     
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.gtas.server.document.model.GridDocument;

import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.UUIDUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.UUID;

public class FileHelper
{
  public static final int DEFAULT_BUFFER_SIZE = 20000;

  public static String copy(String sourceFile, String targetFile)
    throws Exception
  {
    String result = null;
    File srcFile = new File(sourceFile);
    if (srcFile.isFile())
    {
      BufferedInputStream in =
        new BufferedInputStream(new FileInputStream(srcFile));
      File tmpFile = new File(targetFile);
      FileOutputStream out = new FileOutputStream(tmpFile);
      int len=0;
      long readLength=0;
      byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
      while (readLength < srcFile.length())
      {
        len = in.read(buffer);
        out.write(buffer, 0, len);
        readLength += len;
      }
      in.close();
      out.close();
      result = tmpFile.getName();
    }
    return result;
  }

  public static String copyToTemp(String sourceFilename)
    throws Exception
  {
    File tempFile = File.createTempFile(generateRandomFileName(),
                                        getFileExt(sourceFilename));
    return copy(sourceFilename, tempFile.getAbsolutePath());
  }

  public static File getUdocFile(GridDocument gdoc)
    throws Exception
  {
    File udocFile = null;
    String udocFilename = gdoc.getUdocFilename();
    String tempUdocFilename = gdoc.getTempUdocFilename();
    Logger.debug("[FileHelper.getUdocFile] udocFilename = "+udocFilename);
    if (tempUdocFilename.length() > 0)
    {
      String tmpdir = System.getProperty("java.io.tmpdir");
      Logger.debug("[FileHelper.getUdocFile] tempUdocFilename = "
                    +tmpdir+"/"+tempUdocFilename);
      udocFile = new File(tmpdir+"/"+tempUdocFilename);
      if (!udocFile.exists())
      {
        udocFile = null;
      }
    }
    else
    {
      String folder = gdoc.getFolder();
      if (folder != null && folder.length()>0)
      {
        udocFile = FileUtil.getFile(IDocumentPathConfig.PATH_UDOC,
                                    folder+File.separator,
                                    udocFilename);
      }
    }
    
    //TWX: 2006 05 April 2006 if the udocFile is a directory, set udoc file to null.
    if(udocFile != null && udocFile.isDirectory())
    {
    	udocFile = null;
    }
    
    return udocFile;
  }

  public static GridDocument delTempUdocFile(GridDocument gdoc)
    throws Exception
  {
    String tempUdocFilename = gdoc.getTempUdocFilename();
    if (tempUdocFilename.length() > 0)
    {
      String tmpdir = System.getProperty("java.io.tmpdir");
      Logger.debug("[FileHelper.delTempUdocFile] tempUdocFilename = "
                    +tmpdir+"/"+tempUdocFilename);
      File udocFile = new File(tmpdir+"/"+tempUdocFilename);
      if (udocFile.exists())
      {
        udocFile.delete();
      }
      gdoc.setTempUdocFilename("");
      File udoc = getUdocFile(gdoc);
      if (udoc != null && udoc.exists())
      {
        gdoc.setUdocFullPath(udoc.getAbsolutePath());
      }
    }
    return gdoc;
  }

  public static GridDocument duplicateTempUdocFile(GridDocument gdoc)
    throws Exception
  {
    String tempUdocFilename = gdoc.getTempUdocFilename();

    if (tempUdocFilename.length() > 0)
    {
      String tmpdir = System.getProperty("java.io.tmpdir");
      Logger.debug("[FileHelper.duplicateTempUdocFile] tempUdocFilename = "
                    +tmpdir+"/"+tempUdocFilename);
      File udocFile = new File(tmpdir+"/"+tempUdocFilename);
      if (udocFile.exists())
      {
        File newTempFile = File.createTempFile(generateRandomFileName(),
                                               getFileExt(tempUdocFilename));
        String newTempFilename =
          copy(udocFile.getAbsolutePath(), newTempFile.getAbsolutePath());
        gdoc.setTempUdocFilename(newTempFilename);
        gdoc.setUdocFullPath(newTempFile.getAbsolutePath());
      }
    }
    return gdoc;
  }

  public static File getGdocFile(GridDocument gdoc)
    throws Exception
  {
    File gdocFile = null;
    String gdocFilename = gdoc.getGdocFilename();
    Logger.debug("[FileHelper.getGdocFile] gdocFilename = "+gdocFilename);
    gdocFile = FileUtil.getFile(IDocumentPathConfig.PATH_GDOC,
                                gdoc.getFolder()+File.separator,
                                gdocFilename);
    return gdocFile;
  }

  /**
   * This method will create the specific directory.
   */
  public static synchronized void createDirectory(File directory)
  {
    if (!directory.exists())
    {
      directory.mkdirs();
      //040531NSL -- should make use of File.mkdirs()
      //File parentDir = directory.getParentFile();
      //createDirectory(parentDir);
      //directory.mkdir();
    }
  }

  protected static String generateRandomFileName()
  {
    //Random random = new Random();
    //return String.valueOf(random.nextInt());
    //NSL20070312 Ensure uniqueness
    return UUIDUtil.getRandomUUIDInStr();
  }

  public static String getFileExt(String fullFilename)
  {
    String ext = "";
    if (fullFilename.lastIndexOf(File.separator) != -1)
    {
      fullFilename = fullFilename.substring(fullFilename.lastIndexOf(File.separator));
    }
    if ((fullFilename.indexOf(".") > 0) &&
        (fullFilename.lastIndexOf(".") != fullFilename.length()))
    {
      ext = fullFilename.substring(fullFilename.lastIndexOf("."));
    }

    return ext;
  }

  /**
   * Change the file extension in a filename if the temporary filename contains
   * a file extension.
   * 
   * @param filename The filename whose extension needs to be changed
   * @param tempFilename The temporary filename which may or may not contain a file extension.
   * @return The <code>filename</code> with file extension changed, if any. If the <code>tempFilename</code> does not contain
   * a file extension, no change is done. Otherwise, the file extension in <code>tempFilename</code> is replaced
   * into the <code>filename</code>.
   */
  public static String changeFileExtension(String filename, String tempFilename)
  {
    if (tempFilename == null || tempFilename.length() == 0)
      return filename;
      
    String newExt = getFileExt(tempFilename);
    if (newExt.length() > 0)
    {
      filename = FileUtil.changeName(filename, null, newExt);
    }
    return filename;
  }
  

}
