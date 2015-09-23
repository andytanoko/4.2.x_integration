/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileSplitter.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Jan 30 2003    Goh Kan Mun                 Created
 * Feb 06 2003    Goh Kan Mun                 Rename to FileSplitter and used it
 *                                            for splitting files.
 * Mar 21 2003    Goh Kan Mun                 Change the Buffer Size to 10k.
 *                                            Provide method to retrieve block file name.
 *                                            Fixed the zip file bug. Missing the last few bytes.
 *                                            Change some of the method to public.
 * Jul 24 2003   Jagadeesh                    Moved from Base.Packaging to Channel.
 */

package com.gridnode.pdip.app.channel.flowcontrol;

import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.FileAccessException;

import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * This class is used to help the file operations/methods that
 * the packaging need.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class FileSplitter
{
  private static final String CLASS_NAME = "FileSplitter";
  private static int BUFFER_SIZE = 10240;

  //  private int _blockSize = 0;
  //  private int _totalBlocks = 0;
  //  private int _lastBlockSize = 0;
  //  private String _pathKey = null;
  //  private String _subPath = null;
  //  private String[] _filenames = null;
  //  private long[] _fileLengths = null;
  //  private boolean _isZip = false;
  /**
   * To split the joined file into their original form.
   *
   * @param           pathKey       the IPathConfig constant that contains the path name.
   * @param           subPath       the relative path based on the pathKey
   * @param           files         the array of File to be sent.
   * @param           blockSize     the size of a block.
   * @param           thresholdSize the maximum size the last block to split.
   * @param           isZip         whether the block is in zipped form.
   *
   * @return          the number of blocks created.
   * @exception       FileAccessException thrown when error occurs accessing the file.
   * @exception       ApplicationException thrown when error occurs.
   */
  public static int storeFilesIntoBlocks(
    String pathKey,
    String subPath,
    File[] files,
    int blockSize,
    int totalBlocks,
    int lastBlockSize,
    boolean isZip,
    String fileId)
    throws FileAccessException, ApplicationException
  {
    if (files == null || files.length < 1)
      return 0;
    if (blockSize <= 0)
      throw new ApplicationException("Invalid block size: " + blockSize);
    int currentFileIndex = 0;
    int currentFilePointer = 0;

    ByteArrayOutputStream baos = null;
    GZIPOutputStream gzos = null;
    BufferedOutputStream bos = null;
    int remainingSizeToRead = 0;

    try
    {
      FileInputStream fis = null;
      BufferedInputStream bis = null;
      for (int currentBlock = 0; currentBlock < totalBlocks; currentBlock++)
      {
        baos = new ByteArrayOutputStream();

        gzos = new GZIPOutputStream(baos);
        bos = new BufferedOutputStream(gzos);

        /*if (isZip)  This block was commented while testing with GT1.x, since we do not want to zip, if we want to split(Split dose both that is split and zip)
        {
        
          gzos = new GZIPOutputStream(baos);
          bos = new BufferedOutputStream(gzos);
        }
        else
          bos = new BufferedOutputStream(baos);
        */
        // Get the size for current block.
        if (currentBlock == (totalBlocks - 1)) // Last block
          remainingSizeToRead = lastBlockSize;
        else
          remainingSizeToRead = blockSize;

        // Read file(s) until block size.
        while (remainingSizeToRead > 0 && currentFileIndex < files.length)
        {
          if (currentFilePointer == 0) // file pointer resetted
          {
            if (files[currentFileIndex] == null
              || //File is null or
            !files[currentFileIndex].exists()
              || //File does not exist or
            files[currentFileIndex].length()
                < 1 //File length is 0 or less
            )
            {
              ChannelLogger.debugLog(
                CLASS_NAME,
                "storeFilesIntoBlock",
                "Skip file: "
                  + ((files[currentFileIndex] == null)
                    ? ("" + currentFileIndex)
                    : files[currentFileIndex].getAbsolutePath()));
              currentFileIndex++; //process with next file
              continue;
            }
            else
            {
              ChannelLogger.debugLog(
                CLASS_NAME,
                "storeFilesIntoBlock",
                "Open file to read: "
                  + files[currentFileIndex].getAbsolutePath());
              // open new current file
              fis = new FileInputStream(files[currentFileIndex]);
              bis = new BufferedInputStream(fis);
            }
          }

          //Read file until eof or until the block data has been read.
          while (remainingSizeToRead > 0)
          {
            int bufferSize =
              (remainingSizeToRead > BUFFER_SIZE)
                ? BUFFER_SIZE
                : remainingSizeToRead;
            byte[] buffer = new byte[bufferSize];
            int bytesRead = bis.read(buffer);

            if (bytesRead < 0) //Reach eof.
              break;
            remainingSizeToRead -= bytesRead;
            currentFilePointer += bytesRead;
            bos.write(buffer, 0, bytesRead);
            bos.flush();
          }

          if (currentFilePointer >= files[currentFileIndex].length())
            //more to read => reach eof
          {
            currentFileIndex++; //move to next file
            currentFilePointer = 0; //reset file pointer.
            bis.close();
            ChannelLogger.debugLog(
              CLASS_NAME,
              "storeFilesIntoBlock",
              "Reach eof! Moving to next file: #" + currentFileIndex);
          }
        }
        if (currentFileIndex <= files.length)
        {
          bos.close();
          ChannelLogger.debugLog(
            CLASS_NAME,
            "storeFilesIntoBlock",
            "Storing " + baos.size() + " bytes");
          storeBlock(
            pathKey,
            subPath,
            currentBlock,
            new ByteArrayInputStream(baos.toByteArray()),
            fileId);
        }
        else
          throw new FileAccessException("Error converting files to blocks");
      }
      return totalBlocks;
    }
    catch (FileAccessException fae)
    {
      throw fae;
    }
    catch (Throwable t)
    {
      throw new FileAccessException(t);
    }
  }

  private static void storeBlock(
    String pathKey,
    String subPath,
    int blockNo,
    InputStream blockData,
    String fileId)
    throws FileAccessException
  {
    FileHelper.create(
      pathKey,
      subPath,
      getBlockFileName(fileId, blockNo),
      blockData,
      true);
  }

  public static int getTotalBlocks(
    File[] files,
    int blockSize,
    int thresholdSize)
  {
    long totalFileSize = 0;
    for (int i = 0; i < files.length; i++)
    {
      if (files[i] != null)
        totalFileSize += files[i].length();
    }
    int totalBlocks = (int) (totalFileSize / blockSize) + 1; //
    int lastBlockSize = (int) (totalFileSize % blockSize);
    if ((blockSize + lastBlockSize) <= thresholdSize)
      totalBlocks--; // combine last block together with the previous block.
    if (totalFileSize < (blockSize + lastBlockSize))
      totalBlocks++;
    return totalBlocks;
  }

  public static int getLastBlockSize(
    File[] files,
    int blockSize,
    int thresholdSize)
  {
    long totalFileSize = 0;
    for (int i = 0; i < files.length; i++)
    {
      if (files[i] != null)
        totalFileSize += files[i].length();
    }
    int lastBlockSize = (int) (totalFileSize % blockSize);
    if ((blockSize + lastBlockSize) <= thresholdSize)
      lastBlockSize += blockSize;
    // combine last block together with the previous block.
    return lastBlockSize;
  }

  public static byte[] getBlock(
    String pathKey,
    String subPath,
    String filename)
    throws FileAccessException
  {
    return FileHelper.getFileContent(pathKey, subPath, filename);
  }

  public static boolean checkFilesStoredIntoBlocks(
    String pathKey,
    String subPath,
    File[] files,
    int totalBlock,
    boolean isZip,
    String fileId)
    throws FileAccessException
  {
    for (int i = 0; i < totalBlock; i++)
      if (!checkBlockExist(pathKey, subPath, getBlockFileName(fileId, i)))
        return false;
    return true;
  }

  public static boolean checkBlockExist(
    String pathKey,
    String subPath,
    String filename)
    throws FileAccessException
  {
    return FileHelper.exist(pathKey, subPath, filename);
  }

  public static String getBlockFileName(String fileId, int blockNo)
  {
    return fileId + "_" + blockNo;
  }

}