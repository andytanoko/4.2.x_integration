/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileJoiner.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Jan 30 2003    Goh Kan Mun                 Created
 * Mar 21 2003    Goh Kan Mun                 Change the Buffer Size to 10k.
 *                                            Added method to retrieve block file name.
 *                                            Change process and signature for
 *                                            getOriginalFiles method.
 * Jul 24 2003    Jagadeesh                   Moved From Base.Packaging to Channel.
 */

package com.gridnode.pdip.app.channel.flowcontrol;

import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.FileAccessException;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * This class is used to help the file operations/methods that
 * the packaging need.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class FileJoiner
{
  private static final String CLASS_NAME = "FileJoiner";

  private static int BUFFER_SIZE = 10240;
  //  private String _pathKey = null;
  //  private String _subPath = null;

  //  public FileJoiner(String pathKey, String subPath, File block, int blockNo) throws Exception
  //  {
  //    _pathKey = pathKey;
  //    _subPath = subPath;
  //    store(block, Integer.toString(blockNo));
  //  }

  public static void store(
    String pathKey,
    String subPath,
    byte[] blockContent,
    String fileId,
    int blockNo)
    throws FileAccessException
  {
    String filename = getBlockFileName(fileId, blockNo);
    if (FileHelper.exist(pathKey, subPath, filename))
      if (FileHelper.length(pathKey, subPath, filename)
        != (long) blockContent.length)
        throw new FileAccessException("Size is not the same");
      else
        return;
    ByteArrayInputStream bais = new ByteArrayInputStream(blockContent);
    FileHelper.create(pathKey, subPath, filename, bais, false);
    try
    {
      bais.close();
      bais = null;
    }
    catch (IOException ignore)
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "store",
        "Unable to close ByteArrayInputStream. Should be ok",
        ignore);
    }
  }

  /**
   * To join the splitted file into their original form.
   *
   * @param           pathKey       the IPathConfig constant that contains the path name.
   * @param           subPath       the relative path based on the pathKey
   * @param           joinFilename  the name of the joined file.
   * @param           filenames     an array of the original names of the files.
   * @param           size          an array of the original sizes of the files.
   * @param           iszip         whether the join file is in zipped form.
   *
   * @return          an array of files in their original form.
   *
   * @exception       FileAccessException thrown when error occurs accessing the file.
   * @exception       ApplicationException thrown when error occurs.
   */
  public static File[] getOriginalFiles(
    String pathKey,
    String subPath,
    String[] filenames,
    long[] size,
    String fileId,
    boolean isZip)
    throws FileAccessException, ApplicationException
  {
    try
    {
      if (filenames.length != size.length)
        throw new ApplicationException("Array is of different size.");

      File[] newFile = new File[filenames.length];
      int blockNo = 0;
      int sizeRead = -1;
      byte[] buffer = null;
      ByteArrayInputStream bais = null;
      GZIPInputStream gzipis = null;
      BufferedInputStream bis = null;

      for (int i = 0; i < newFile.length; i++)
      {
        if (filenames[i] == null)
          newFile[i] = null;
        else
        {
          if (FileHelper.exist(pathKey, subPath, filenames[i]))
            FileHelper.delete(pathKey, subPath, filenames[i]);
          newFile[i] =
            FileHelper.createNewLocalFile(pathKey, subPath, filenames[i]);
          if (newFile[i] == null)
            throw new IOException(
              "Unable to create empty file " + filenames[i]);
          if (size[i] < 1)
            continue; //Create empty file.

          long totalSizeRemaining = size[i];
          int sizeToRead = 0;
          FileOutputStream fos = new FileOutputStream(newFile[i]);
          BufferedOutputStream bos = new BufferedOutputStream(fos);
          byte[] buf = null;
          while (totalSizeRemaining > 0)
          {
            if (sizeRead < 0) // eof reached, read next block
            {
              ChannelLogger.debugLog(
                CLASS_NAME,
                "getOriginalFiles",
                "Opening block #" + blockNo);
              buffer = getBlockContent(pathKey, subPath, fileId, blockNo++);
              bais = new ByteArrayInputStream(buffer);
              gzipis = new GZIPInputStream(bais);
              bis = new BufferedInputStream(gzipis);

              /*if (isZip)  This below block is commented while testing with GT1.x. Please refer to FileSplitter.
              {
                gzipis = new GZIPInputStream(bais);
                bis = new BufferedInputStream(gzipis);
              }
              else
              {
                bis = new BufferedInputStream(bais);
              }
              */
            }

            if (totalSizeRemaining > BUFFER_SIZE)
              sizeToRead = BUFFER_SIZE;
            else
              sizeToRead = (int) totalSizeRemaining;

            buf = new byte[sizeToRead];
            sizeRead = bis.read(buf, 0, sizeToRead);

            if (sizeRead > 0)
            {
              bos.write(buf, 0, sizeRead);
              bos.flush();
              totalSizeRemaining -= sizeRead;
            }
            else //eof reach for current block
              {
              bis.close();
              ChannelLogger.debugLog(
                CLASS_NAME,
                "getOriginalFiles",
                "end of current block #" + (blockNo - 1));
            }
          }
          bos.flush();
          bos.close();
        }
      }
      return newFile;
    }
    catch (IOException ioe)
    {
      throw new FileAccessException(ioe);
    }
  }

  private static byte[] getBlockContent(
    String pathKey,
    String subPath,
    String fileId,
    int blockNo)
    throws FileAccessException
  {
    return FileHelper.getFileContent(
      pathKey,
      subPath,
      getBlockFileName(fileId, blockNo));
  }

  public static String getBlockFileName(String fileId, int blockNo)
  {
    return fileId + "_" + blockNo;
  }

  /**
   * To join the splitted file into their original form.
   *
   * @param           pathKey       the IPathConfig constant that contains the path name.
   * @param           subPath       the relative path based on the pathKey
   * @param           joinFilename  the name of the joined file.
   * @param           filenames     an array of the original names of the files.
   * @param           size          an array of the original sizes of the files.
   * @param           iszip         whether the join file is in zipped form.
   *
   * @return          an array of files in their original form.
   *
   * @exception       FileAccessException thrown when error occurs accessing the file.
   * @exception       ApplicationException thrown when error occurs.
   */
  public static File[] getBlockFiles(
    String pathKey,
    String subPath,
    String fileId,
    boolean isZip)
    throws FileAccessException, ApplicationException
  {
    return null;
  }

}