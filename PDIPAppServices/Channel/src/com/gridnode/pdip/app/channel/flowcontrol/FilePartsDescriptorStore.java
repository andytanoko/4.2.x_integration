package com.gridnode.pdip.app.channel.flowcontrol;

import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.base.packaging.helper.IPackagingConstants;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.FileAccessException;

import java.io.File;
import java.util.Hashtable;

public class FilePartsDescriptorStore
{

  private static final String CLASS_NAME = "FilePartsDescriptorStore";
  private static Hashtable _allPartsDescriptorStore = new Hashtable();

  public FilePartsDescriptorStore()
  {
  }

  public static synchronized FilePartsDescriptor retrieveFilePartsDescriptor(String fileId)
    throws FileAccessException, ApplicationException
  {
    if (fileId == null)
      throw new ApplicationException("FileId cannot null!");
    if (_allPartsDescriptorStore.containsKey(fileId))
      return (FilePartsDescriptor) _allPartsDescriptorStore.get(fileId);
    else
      return null;
  }

  public static synchronized FilePartsDescriptor retrieveFilePartsDescriptor(
    String fileId,
    int totalBlks)
    throws FileAccessException, ApplicationException
  {
    if (fileId == null)
      throw new ApplicationException("FileId cannot null!");
    if (_allPartsDescriptorStore.containsKey(fileId))
    {
      FilePartsDescriptor meta =
        (FilePartsDescriptor) _allPartsDescriptorStore.get(fileId);
      meta.checkFilePartsDescriptor(fileId);
      if (meta.getTotalBlocks() < totalBlks)
        meta.setTotalBlocks(totalBlks);
      return meta;
    }
    else
      return null;
  }

  /**
   * Creates a FilePartsDescriptor if not exists, else return from the cache
   * store.
   * @param fileId
   * @return
   */

  public static synchronized FilePartsDescriptor getFilePartsDescriptor(
    File[] files,
    String fileId,
    boolean isZip,
    int blockSize,
    int thresholdSize,
    String transId,
    String originalEventId,
    String[] originalData)
    throws FileAccessException, ApplicationException
  {
    try
    {
      FilePartsDescriptor filePartsDescriptor = getFilePartsDescriptor(fileId);
      //Retrieve any existing metaInfo if it exist.
      filePartsDescriptor.setOriginalData(originalData);
      return filePartsDescriptor;
    }
    catch (FileAccessException fae)
    {
      int totalBlocks =
        FileSplitter.getTotalBlocks(files, blockSize, thresholdSize);
      int lastBlockSize =
        FileSplitter.getLastBlockSize(files, blockSize, thresholdSize);
      String subPath = fileId + File.separator;

      FilePartsDescriptor filePartsDescriptor =
        checkAndRetrieveFilePartsDescriptor(
          fileId,
          blockSize,
          thresholdSize,
          transId,
          originalEventId,
          totalBlocks,
          lastBlockSize);
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getFilePartsDescriptor()",
        "[B4 Checking for Files Stored into Blocks]");

      for (int i = 0; i < files.length; i++)
      {
        if (files[i] != null)
          ChannelLogger.debugLog(
            CLASS_NAME,
            "getFilePartsDescriptor()",
            "[File=]" + files[i].getAbsolutePath());
      }

      if (!FileSplitter
        .checkFilesStoredIntoBlocks(
          IPackagingConstants.PACKAGING_PATH,
          subPath,
          files,
          totalBlocks,
          isZip,
          fileId))
      {
        FileSplitter.storeFilesIntoBlocks(
          IPackagingConstants.PACKAGING_PATH,
          subPath,
          files,
          blockSize,
          totalBlocks,
          lastBlockSize,
          isZip,
          fileId);
        String[] blocksFilename = new String[totalBlocks];
        for (int i = 0; i < totalBlocks; i++)
          blocksFilename[i] = FileSplitter.getBlockFileName(fileId, i);
        if (blocksFilename != null)
        {
          for (int i = 0; i < blocksFilename.length; i++)
            ChannelLogger.debugLog(
              CLASS_NAME,
              "getFilePartsDescriptor()",
              "[BLOCK File Name=]" + blocksFilename[i]);
        }

        filePartsDescriptor.setBlocksFilename(blocksFilename);
      }
      else
        ChannelLogger.debugLog(
          CLASS_NAME,
          "getFilePartsDescriptor()",
          "[seems Blocks are Stored ready ..]");
      filePartsDescriptor.setOriginalData(originalData);
      return filePartsDescriptor;
    }

  }

  public static synchronized FilePartsDescriptor getFilePartsDescriptor(
    String fileId,
    int totalBlocks)
    throws FileAccessException, ApplicationException
  {
    FilePartsDescriptor meta = new FilePartsDescriptor(fileId, totalBlocks);
    _allPartsDescriptorStore.put(fileId, meta);
    return meta;
  }

  public static synchronized FilePartsDescriptor getFilePartsDescriptor(String fileId)
    throws FileAccessException, ApplicationException
  {
    FilePartsDescriptor meta = new FilePartsDescriptor(fileId);
    _allPartsDescriptorStore.put(fileId, meta);
    return meta;
  }

  public static void addFilePartsDescriptor(
    FilePartsDescriptor filePartsDesc,
    String fileId)
  {
    _allPartsDescriptorStore.put(fileId, filePartsDesc);
  }

  public static void removeFilePartsDescriptor(String fileId)
    throws FileAccessException
  {
    if (fileId == null)
      return;
    _allPartsDescriptorStore.remove(fileId);
    FilePartsDescriptor.removeFilePartsDescriptor(fileId);
  }

  private static FilePartsDescriptor checkAndRetrieveFilePartsDescriptor(
    String fileId,
    int blockSize,
    int thresholdSize,
    String transId,
    String originalEventId,
    int totalBlocks,
    int lastBlockSize)
    throws FileAccessException, ApplicationException
  {
    if (fileId == null)
      throw new ApplicationException("FileId cannot null!");
    if (transId == null)
      throw new ApplicationException("Transaction Id cannot null!");
    if (originalEventId == null)
      throw new ApplicationException("Original event Id cannot null!");
    if (_allPartsDescriptorStore.containsKey(fileId))
    {
      FilePartsDescriptor meta =
        (FilePartsDescriptor) _allPartsDescriptorStore.get(fileId);
      meta.checkFilePartsDescriptor(
        fileId,
        blockSize,
        thresholdSize,
        transId,
        originalEventId,
        totalBlocks,
        lastBlockSize);
      return meta;
    }
    else
    {
      FilePartsDescriptor meta =
        new FilePartsDescriptor(
          fileId,
          blockSize,
          thresholdSize,
          transId,
          originalEventId,
          totalBlocks,
          lastBlockSize);
      _allPartsDescriptorStore.put(fileId, meta);
      return meta;
    }

  }

}