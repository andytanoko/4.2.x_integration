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
 * Oct 31 2003    Guo Jianyu          Modified getUdocFile() to include subpaths
 * Mar 12 2007    Neo Sok Lay         Use UUID for unique filename.
 */
package com.gridnode.gtas.server.mapper.helpers;

import com.gridnode.gtas.server.mapper.model.IDocumentMetaInfo;

import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.UUIDUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

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
      BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFile));
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
    File tempFile = File.createTempFile(generateRandomFileName(), getFileExt(sourceFilename));
    return copy(sourceFilename, tempFile.getAbsolutePath());
  }

  public static File getUdocFile(IDocumentMetaInfo gdoc)
    throws Exception
  {
    File udocFile = null;
    String udocFilename = gdoc.getUdocFilename();
    String tempUdocFilename = gdoc.getTempUdocFilename();
    Logger.debug("[FileHelper.getUdocFile] udocFilename = "+udocFilename);
    if (!tempUdocFilename.equals(""))
    {
      String tmpdir = System.getProperty("java.io.tmpdir");
      Logger.debug("[FileHelper.getUdocFile] tempUdocFilename = "+tmpdir+"/"+tempUdocFilename);
      udocFile = new File(tmpdir+"/"+tempUdocFilename);
      if (!udocFile.exists())
      {
        udocFile = null;
      }
    }
    else
    {
      udocFile = FileUtil.getFile(IGTMapperPathConfig.PATH_UDOC,
        gdoc.getFolder() + File.separator, udocFilename);
    }
    return udocFile;
  }

  protected static String generateRandomFileName()
  {
    //Random random = new Random();
    //return String.valueOf(random.nextInt());
    //NSL20070312 Ensure uniqueness.
    return UUIDUtil.getRandomUUIDInStr();
  }

  protected static String getFileExt(String fullFilename)
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

}
