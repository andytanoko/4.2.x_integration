/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConvertorHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 23 2003    Koh Han Sing        Created
 * Mar 12 2007    Neo Sok Lay         Generate unique filename using UUID.                                   
 */
package com.gridnode.pdip.base.xml.helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.gridnode.pdip.framework.util.UUIDUtil;

/**
 * This class contains common utilities used by the XML module.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class UtilHelper
{

  public static File getTempFile(ByteArrayOutputStream baos, String ext)
   throws FileNotFoundException, IOException
  {
    File tempFile = File.createTempFile(generateRandomFileName(), "."+ext);
    FileOutputStream fos = new FileOutputStream(tempFile);
    fos.write(baos.toByteArray());
    fos.flush();
    fos.close();
    return tempFile;
  }

  public static ArrayList getFileList(ArrayList baosList, String ext)
    throws FileNotFoundException, IOException
  {
    ArrayList fileList = new ArrayList();
    for (Iterator i = baosList.iterator(); i.hasNext();)
    {
      ByteArrayOutputStream baos = (ByteArrayOutputStream)i.next();
      File tempFile = getTempFile(baos, ext);
      fileList.add(tempFile);
    }
    return fileList;
  }

  public static String generateRandomFileName()
  {
    //Random random = new Random();
    //return String.valueOf(random.nextInt())+String.valueOf(random.nextInt());
    //NSL20070312
    return UUIDUtil.getRandomUUIDInStr();
  }

  public static String getOriginalFileExt(String fullFilename)
  {
    Logger.debug("[UtilHelper.getOriginalFileExt]", "fullFilename = "+fullFilename);
    String ext = "";
    if ((fullFilename.lastIndexOf("/") >= 0) && (fullFilename.lastIndexOf("/")+1 != fullFilename.length()))
    {
      fullFilename = fullFilename.substring(fullFilename.lastIndexOf("/")+1);
    }
    else if ((fullFilename.lastIndexOf("\\") >= 0) && (fullFilename.lastIndexOf("\\")+1 != fullFilename.length()))
    {
      fullFilename = fullFilename.substring(fullFilename.lastIndexOf("\\")+1);
    }

    // check to see if last char is not "/" or "\"
    if ((fullFilename.lastIndexOf("/")+1 != fullFilename.length()) &&
        (fullFilename.lastIndexOf("\\")+1 != fullFilename.length()))
    {
      if ((fullFilename.indexOf(".") > 0) && (fullFilename.lastIndexOf(".")+1 != fullFilename.length()))
      {
        Logger.debug("[UtilHelper.getOriginalFileExt]", "found . in filename");
        ext = fullFilename.substring(fullFilename.lastIndexOf(".")+1);
      }
    }
    Logger.debug("[UtilHelper.getOriginalFileExt]", "ext = "+ext);

    return ext;
  }
}