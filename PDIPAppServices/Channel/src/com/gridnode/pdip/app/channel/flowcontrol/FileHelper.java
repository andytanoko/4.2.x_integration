/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileHelper.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Jan 30 2003    Goh Kan Mun                 Created
 * Mar 21 2003    Goh Kan Mun                 Modified - update getFileContent method
 * Jul 24 2003    Jagadeesh                   Moved from Base.Packaging to Channel
 */

package com.gridnode.pdip.app.channel.flowcontrol;

import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 * This class is used to help the file operations/methods that
 * the packaging need.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class FileHelper extends FileUtil
{
  //private static final String CLASS_NAME = "FileHelper";
  private static final int BUFFER_SIZE = 2048;
  //private static final String ZIP_FILE_EXT = ".jgz";
  //private static final String BLK_FILE_EXT = ".blk";
  //private static final String JOIN_FILE_EXT = ".joi";

  /**
   * To read the content of the specified file starting at the given offset with the
   * specified maximum number of bytes.
   *
   * @param           pathKey       the IPathConfig constant that contains the path name.
   * @param           subPath       the relative path based on the pathKey
   * @param           filename      the name of the file.
   * @param           offset        offset at which to read bytes from the beginning of file.
   * @param           len           maximum number of bytes to read
   * @return          the bytes actually read.
   *
   * @exception       FileAccessException thrown when error occurs accessing the file.
   */
  public static byte[] getFileContent(
    String pathKey,
    String subPath,
    String filename,
    int offset,
    int len)
    throws FileAccessException, EOFException
  {
    String fileFullPath = getPath(pathKey) + subPath + filename;
    if (!exist(pathKey, subPath, filename))
      throw new FileAccessException("File does not exist:" + fileFullPath);
    return _fileAccess.readByteFromFile(fileFullPath, offset, len);
  }

  /**
   * To read the content of the specified file.
   *
   * @param           pathKey       the IPathConfig constant that contains the path name.
   * @param           subPath       the relative path based on the pathKey
   * @param           filename      the name of the file.
   * @return          the byte content in the file.
   *
   * @exception       FileAccessException thrown when error occurs accessing the file.
   */
  public static byte[] getFileContent(
    String pathKey,
    String subPath,
    String filename)
    throws FileAccessException
  {
    String fileFullPath = getPath(pathKey) + subPath + filename;
    if (!exist(pathKey, subPath, filename))
      throw new FileAccessException("File does not exist:" + fileFullPath);
    long lengthOfFile = length(pathKey, subPath, filename);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    long offset = 0;
    try
    {
      byte[] buffer = null;
      while (offset < lengthOfFile)
      {
        buffer =
          _fileAccess.readByteFromFile(fileFullPath, offset, BUFFER_SIZE);
        baos.write(buffer, 0, buffer.length);
        offset += (long) buffer.length;
      }
      baos.flush();
      baos.close();
      return baos.toByteArray();
    }
    catch (EOFException eofe)
    {
      return baos.toByteArray();
    }
    catch (IOException ioe)
    {
      throw new FileAccessException("Unable to retrieve file content.", ioe);
    }
  }

}