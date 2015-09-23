/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportConfigHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 27 2003    Koh Han Sing        Created
 * May 30 2006    Neo Sok Lay         Resolve SunOS portability issue: config
 *                                    exported from windows GT cannot import
 *                                    to SunOS GT due to path separator difference.
 */
package com.gridnode.pdip.base.exportconfig.helpers;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;

/**
 * This class provides zipping services.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class ZipHelper
{
  public static final int BUFFER_SIZE = 2048;

  public static void zip(File fileToZip, File zipFile) throws Exception
  {
    ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));

    String masterDir = "";
    if (fileToZip.isDirectory())
    {
      masterDir = fileToZip.getAbsolutePath();
    }
    zip(fileToZip, zos, masterDir);
    zos.close();
  }

  public static File unzip(File zipFile, File unzipTo) throws Exception
  {
    ZipFile zFile = new ZipFile(zipFile);
    Enumeration enu = zFile.entries();
    while (enu.hasMoreElements())
    {
      ZipEntry entry = (ZipEntry)enu.nextElement();
      InputStream is = zFile.getInputStream(entry);
      BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);

      String unzipToPath = "";
      if (unzipTo != null)
      {
        unzipToPath = unzipTo.getAbsolutePath();
      }
      //NSL20060530 Allow unix systems to import config exported from windows systems (GT 4.0.1 and below)
      String entryName = entry.getName();
      if (File.separatorChar == '/')
      {
        entryName = entryName.replace('\\', File.separatorChar);
      }
      File outFile = new File(unzipToPath+"/"+entryName);
      File parentDir = outFile.getParentFile();
      if (parentDir != null)
      {
        parentDir.mkdirs();
      }
      FileOutputStream fos = new FileOutputStream(outFile);
      int bytesRead = -1;
      while ((bytesRead = bis.read()) != -1)
      {
        fos.write(bytesRead);
      }
      bis.close();
      is.close();
      fos.close();
    }
    zFile.close();
    return unzipTo;
  }

  private static void zip(File fileToZip, ZipOutputStream zos, String masterDir)
    throws Exception
  {
    if (fileToZip.isDirectory())
    {
      File[] filesInDir = fileToZip.listFiles();
      for (int i = 0; i < filesInDir.length; i++)
      {
        File aFileInDir = filesInDir[i];
        zip(aFileInDir, zos, masterDir);
      }
    }
    else
    {
      FileInputStream fis = new FileInputStream(fileToZip);
      String fullpath = fileToZip.getAbsolutePath();
      String entryPath = fullpath.substring(masterDir.length()+1);
      if (masterDir.equals(""))
      {
        entryPath = fileToZip.getName();
      }
      //NSL20060530 Use java path separator.
      if (File.separatorChar != '/')
      {
        entryPath = entryPath.replace(File.separatorChar, '/');
      }
      ZipEntry entry = new ZipEntry(entryPath);
      zos.putNextEntry(entry);
      //byte[] buffer = new byte[BUFFER_SIZE];
      int bytesRead = -1;
      BufferedInputStream bis = new BufferedInputStream(fis, BUFFER_SIZE);
      while ((bytesRead = bis.read()) != -1)
      {
        zos.write(bytesRead);
      }
      bis.close();
    }
  }

  public static void main(String[] args)
  {
    try
    {
      //ZipHelper helper = new ZipHelper();
      FileUtil.delete(IPathConfig.PATH_TEMP, "", "10540197698382.zip");
      File zipFile = FileUtil.createNewLocalFile(IPathConfig.PATH_TEMP, "", "10540197698382.zip");
      File fileToZip = FileUtil.getFile(IPathConfig.PATH_TEMP, "1054019769838");
      ZipHelper.zip(fileToZip, zipFile);

      File tempDir = FileUtil.getFile(IPathConfig.PATH_TEMP, "");
      File die = new File(tempDir, "9413");
      die.mkdir();
      ZipHelper.unzip(zipFile, die);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    System.exit(0);
  }

}