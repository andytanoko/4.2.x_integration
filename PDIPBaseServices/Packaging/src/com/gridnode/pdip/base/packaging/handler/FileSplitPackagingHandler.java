/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileSplitPackagingHandler.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Jan 07 2003    Goh Kan Mun                 Created
 *
 * Mar 20 2003    Goh Kan Mun                 Change the name to FileSplitPackagingHandler
 *                                            Make the File Split process works.
 * Mar 28 2003    Goh Kan Mun                 Modified - Add in the feature to clean up temp
 *                                                       folder when the receiving is finished.
 *                                                     - To store and use data content for subsequent
 *                                                       send.
 *                                                     - To attempt to retrieve MetaInfo from file first,
 *                                                       if not found, then create new MetaInfo.
 *                                                     - To join and process the files once when all parts
 *                                                       have been received.
 *                                                     - To retrieve FileDescription using its static method.
 * Dec 17 2003    Jagadeesh                   Modified - This class is moved to Channel at App Services.
 */

package com.gridnode.pdip.base.packaging.handler;
 

/**
 * This class extends the DefaultPackagingHandler and handlers the file splitting
 * process.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class FileSplitPackagingHandler
//extends DefaultPackagingHandlerimplements IPackagingConstants
{
/*  private final static String CLASS_NAME = "FileSplitPackagingHandler";
  private final static int MAX_PACKET_SEND_RECEIVE = 10;
  private final static int KILO_BLOCK = 1024;
  private String _subPath = null;

  public FileSplitPackagingHandler()
  {
  }

  public PackagingInfo packageAndEnvelope(PackagingInfo info)
                       throws PackagingException
  {
    if (info.isSplitAck())
      return getSplitAckPackage(info);
    else if (info.isSplit())
    {
      if (!isCleanup(info))
        return getSplitPackage(info);
      else
        cleanup(info);
    }
    return super.packageAndEnvelope(info);
  }

  public PackagingInfo unPackage(PackagingInfo info) throws PackagingException
  {
    if (info.isSplitAck())
      return handleSplitPackageAck(info);
    else if (info.isSplit())
    {
      if (!isCleanup(info))
        return joinSplitPackage(info);
      else
      {
        cleanup(info);
        return handleCleanupPackage(info);
      }
    }
    return super.unPackage(info);
  }

  private PackagingInfo getSplitPackage(PackagingInfo info) throws PackagingException
  {
    try
    {
      String fileId = info.getFileId();
      _subPath = fileId + File.separator;
      FileDescriptor fileDesc = FileDescriptor.retrieve(info.getPayLoadToPackage(), fileId);
      MetaInfo metaInfo = getMetaInfo(info.getPayLoadToPackage(),
                                      fileId,
                                      info.isZip(),
                                      info.getSplitSize() * KILO_BLOCK,
                                      info.getSplitThreshold() * KILO_BLOCK,
                                      info.getTransactionId(),
                                      info.getEventId(),
                                      info.getDataContent()
                                     );
      if (info.getGNCI() != null)
        metaInfo.setGNCI(info.getGNCI());
      metaInfo.setOriginalEventId(info.getEventId());

      info.setFileDesc(fileDesc.getFilesDesciptor());
      if (info.isResendAll()) //Resend all
      {
        metaInfo.resetBlockReceivedStatus();
        info.setResendAll(false); // reset this status so that it won't repeat
        PackagingLogger.debugLog(CLASS_NAME, "getSplitPackage", "Resend All");
      }
      else if (info.isResend()) //Resend first block.
      {
        metaInfo.unsetBlockReceivedStatus(0);
        info.setResend(false); // reset this status so that it won't repeat
        PackagingLogger.debugLog(CLASS_NAME, "getSplitPackage", "Resend");
      }
      int currentBlock = retrieveNextBlockNo(metaInfo);
      if (currentBlock < 0)
      {
        info.setAllPacketsProcessed(true);
        info.setMoreBlocksToProcess(false);
        PackagingLogger.debugLog(CLASS_NAME, "getSplitPackage", "No more block to Sent!");
      }
      else
      {
        PackagingLogger.debugLog(CLASS_NAME, "getSplitPackage", "Packaging Packet " + currentBlock);
        info.setAllPacketsProcessed(false);
        info.setPackagedSplitContent(retrieveBlock(metaInfo,
                                                   currentBlock
                                                  ));
        info.setEnvelopeHeader(getSplitPackagedHeader(info,
                                                      metaInfo.getBlockFilename(currentBlock),
                                                      metaInfo.getGNCI()
                                                      ));
        info.setPackagedDataContent(processPackagedDataContent(metaInfo.getOriginalData(),
                                                       metaInfo.getOriginalEventId(),
                                                       fileId,
                                                       currentBlock,
                                                       metaInfo.getTotalBlocks(),
                                                       fileDesc.getFilesDesciptor()
                                                       ));
        info.setPackagedEventId(Integer.toString(IFilePartEvent._SEND_FILE_PART));
        int blockSend = info.getNoOfBlocksProcessed();
        blockSend++;
        if (blockSend >= MAX_PACKET_SEND_RECEIVE)
        {
          info.setNoOfBlocksProcessed(0);
          info.setMoreBlocksToProcess(false);
          PackagingLogger.debugLog(CLASS_NAME,
                                   "getSplitPackage",
                                   "blockSend > Max Packet Sent, More Block to Process = false");
        }
        else
        {
          info.setNoOfBlocksProcessed(blockSend);
          info.setMoreBlocksToProcess(true);
          PackagingLogger.debugLog(CLASS_NAME,
                                   "getSplitPackage",
                                   "blockSend < Max Packet Sent, More Block to Process = true");
        }
      }
      return info;
    }
    catch (Throwable t)
    {
      PackagingLogger.errorLog(CLASS_NAME, "getSplitPackage", "Exception in retrieving split package", t);
      throw new PackagingException("Exception in retrieving split package", t);
    }
  }

  private MetaInfo getMetaInfo(File[] files, String fileId, boolean isZip,
          int blockSize, int thresholdSize, String transId, String originalEventId,
          String[] originalData)
         throws FileAccessException, ApplicationException
  {
    try
    {
      MetaInfo metaInfo = MetaInfo.retrieveMetaInfo(fileId); //Retrieve any existing metaInfo if it exist.
      metaInfo.setOriginalData(originalData);
      return metaInfo;
    }
    catch (FileAccessException fae)
    {
      //MetaInfo not found, creating new metaInfo.
      int totalBlocks = FileSplitter.getTotalBlocks(files, blockSize, thresholdSize);
      int lastBlockSize = FileSplitter.getLastBlockSize(files, blockSize, thresholdSize);
      MetaInfo metaInfo = MetaInfo.retrieveMetaInfo(fileId,
                                           blockSize,
                                           thresholdSize,
                                           transId,
                                           originalEventId,
                                           totalBlocks,
                                           lastBlockSize
                                          );
      if (!FileSplitter.checkFilesStoredIntoBlocks(PACKAGING_PATH,
                                                  _subPath,
                                                  files,
                                                  totalBlocks,
                                                  isZip,
                                                  fileId
                                                  ))
      {
        FileSplitter.storeFilesIntoBlocks(IPackagingConstants.PACKAGING_PATH,
                                          _subPath,
                                          files,
                                          blockSize,
                                          totalBlocks,
                                          lastBlockSize,
                                          isZip,
                                          fileId);
        String[] blocksFilename = new String[totalBlocks];
        for (int i = 0; i < totalBlocks; i++)
          blocksFilename[i] = FileSplitter.getBlockFileName(fileId, i);
        metaInfo.setBlocksFilename(blocksFilename);
      }
      metaInfo.setOriginalData(originalData);
      return metaInfo;
    }
  }

  private int retrieveNextBlockNo(MetaInfo metaInfo)
    throws FileAccessException
  {
    int[] blocksNotReceived = metaInfo.getBlocksNotReceived(1);
    if (blocksNotReceived == null || blocksNotReceived.length < 1)
      return -1;
    return blocksNotReceived[0];
  }

  private byte[] retrieveBlock(MetaInfo metaInfo, int blockNo)
          throws FileAccessException
  {
    if (blockNo < 0)
      return null;
    return FileSplitter.getBlock(PACKAGING_PATH,
                                 _subPath,
                                 metaInfo.getBlockFilename(blockNo)
                                );
  }

  private Hashtable getSplitPackagedHeader(PackagingInfo info, String filename, String gnci)
                  throws PackagingException
  {
    Hashtable envelopeheader = null;
    try
    {

      if((envelopeheader = info.getEnvelopeHeader())== null)
      {
        envelopeheader = new Hashtable();
      }

      envelopeheader.put(ITransportConstants.EVENT_ID,
                         Integer.toString(IFilePartEvent._SEND_FILE_PART));
      envelopeheader.put(ITransportConstants.TRANSACTION_ID, "3");
      envelopeheader.put(ITransportConstants.RECEIPENT_NODE_ID,
                         info.getRecepientNodeId() == null ? "0" :
                         info.getRecepientNodeId());
      envelopeheader.put(ITransportConstants.SENDER_NODE_ID,
                         info.getSenderNodeId()== null ? "0" :
                         info.getSenderNodeId());
      envelopeheader.put(ITransportConstants.EVENT_SUB_ID,
                         info.getEventSubId() == null ? "0" :
                         info.getEventSubId());
      envelopeheader.put(ITransportConstants.PROCESS_ID,
                         info.getProcessId()== null ? "0" :
                         info.getProcessId()
                        );
      envelopeheader.put(ITransportConstants.FILE_ID,
                         filename);
      envelopeheader.put(IPackagingConstants.CHANNEL_NAME,
                         info.getChannelName() == null ? IPackagingConstants.UNDEFINDED_CHANNEL :
                         info.getChannelName());
      if (gnci != null)
        envelopeheader.put(ITransportConstants.GRIDNODE_COMM_INFO_STR,
                           gnci);
      return envelopeheader;
    }
    catch(Throwable t)
    {
      PackagingLogger.errorLog(CLASS_NAME, "getSplitPackagedHeader",
            "Could not build package header", t);
      throw new PackagingException("Could not build package header", t);
    }
  }

  private String[] processPackagedDataContent(String[] originalDataContent,
          String originalEventId, String fileId, int blockNo, int totalBlock,
          String fileSplitDesc)
  {
    String[] dataContent = new String[originalDataContent.length + 8];
    dataContent[0] = "1";
    dataContent[1] = fileId;
    dataContent[2] = Integer.toString(blockNo);
    dataContent[3] = Integer.toString(totalBlock);
    dataContent[4] = originalEventId;
    dataContent[5] = Long.toString(TimeUtil.localToUtc());
    dataContent[6] = Long.toString(600000);
    dataContent[7] = fileSplitDesc;

    System.arraycopy(originalDataContent, 0, dataContent, 8, originalDataContent.length);
    return dataContent;
  }

  private PackagingInfo joinSplitPackage(PackagingInfo info) throws PackagingException
  {
    try
    {
      String[] data = info.getDataContent();
      String fileId = data[1];
      int blockNo = Integer.parseInt(data[2]);
      int totalBlock = Integer.parseInt(data[3]);
      String originalEventId = data[4];
      String fileSplitDesc = data[7];

      Hashtable unpackagedHeader = info.getUnPackagedHeader();
      String filename = (String) unpackagedHeader.get(ITransportConstants.FILE_ID);
      String sender = (String) unpackagedHeader.get(ITransportConstants.SENDER_NODE_ID);
      String recipient = (String) unpackagedHeader.get(ITransportConstants.RECEIPENT_NODE_ID);
      String processId = (String) unpackagedHeader.get(ITransportConstants.PROCESS_ID);
      String channelName = (String) unpackagedHeader.get(IPackagingConstants.CHANNEL_NAME);
      String eventSubId = (String) unpackagedHeader.get(ITransportConstants.EVENT_SUB_ID);

      _subPath = fileId + File.separator;
      FileDescriptor fileDesc = FileDescriptor.retrieve(fileSplitDesc, fileId);
      MetaInfo metaInfo = getMetaInfo(fileId, totalBlock);
      metaInfo.setOriginalEventId(originalEventId);
      storeReceivedFile(blockNo, info.getPayLoadToUnPackage(), filename, metaInfo, fileId);
      if (metaInfo.checkAllBlocksReceived()  // All block received
          && !metaInfo.isProcessing())       // and nobody is processing
      {
        File[] unpackagedPayload = getUnpackagedPayload(
                                        metaInfo,
                                        info.isRelay(),
                                        info.isZip(),
                                        fileDesc,
                                        fileId
                                        );
        if (unpackagedPayload != null)
        {
          info.setUnPackagedPayLoad(unpackagedPayload);
          info.setDataContent(processUnpackagedDataContent(
                  info.getPackagedDataContent(),
                  metaInfo.getTimeCreated()));
          info.setAllPacketsProcessed(true); // To invoke BL to process
          info.setMoreBlocksToProcess(true); // Don't send ack. Wait for the BL to send ack.
          info.setDefaultUnPackagedHeader(getUnpackagedHeader(
                                                      originalEventId,
                                                      fileId,
                                                      fileId,
                                                      info.getUnPackagedHeader()));
          return info;
        }
        else
          PackagingLogger.debugLog(CLASS_NAME,
                                   "joinSplitPackage",
                                   "Exception in unpackage and join");

      }

      info.setUnPackagedPayLoad(null);
      info.setDataContent(null);
      info.setAllPacketsProcessed(false); // Don't Invoke BL
      int blockReceived = metaInfo.getNoOfBlocksReceived();
      if ((blockReceived % MAX_PACKET_SEND_RECEIVE) == 0) // Received every 10 packets
        info.setMoreBlocksToProcess(false); // Send ack.
      else
        info.setMoreBlocksToProcess(true); // Don't send ack.
      info.setDefaultUnPackagedHeader(
              getUnpackagedHeader(
                  GNCompatibleEventMap.getInstance().getSplitEventAckId(originalEventId),
                  fileId,
                  fileId,
                  info.getUnPackagedHeader()
              ));
      return info;
    }
    catch (Throwable t)
    {
      PackagingLogger.errorLog(CLASS_NAME, "joinSplitPackage", "Exception in unpackage and join", t);
      throw new PackagingException("Exception in unpackage and join", t);
    }
  }

  /**
   * For Receiving split event.
   */
/*  private MetaInfo getMetaInfo(String fileId, int totalBlock)
         throws FileAccessException, ApplicationException
  {
    return MetaInfo.retrieveMetaInfo(fileId, totalBlock);
  }

  private String[] processUnpackagedDataContent(String[] packagedDataContent,
          String utcTimestamp)
  {
    int dataContentLength = packagedDataContent.length - 8;
    String[] dataContent = new String[dataContentLength + 1];
    System.arraycopy(packagedDataContent, 8, dataContent, 1, dataContentLength);
    dataContent[0] = utcTimestamp;
    return dataContent;
  }

  private String[] getUnpackagedHeader(String originalEventId, String transactionId,
          String fileId, Hashtable unpackagedHeader)
  {
    String[] unpackHeader = new String[8];
    unpackHeader[0] = originalEventId;
    unpackHeader[1] = transactionId;
    unpackHeader[2] = (String)unpackagedHeader.get(
                       ITransportConstants.RECEIPENT_NODE_ID);
    unpackHeader[3] = (String)unpackagedHeader.get(
                       ITransportConstants.SENDER_NODE_ID);
    unpackHeader[4] = (String)unpackagedHeader.get(
                       ITransportConstants.EVENT_SUB_ID);
    unpackHeader[5] = (String)unpackagedHeader.get(
                       ITransportConstants.PROCESS_ID);
    unpackHeader[6] = fileId;
    unpackHeader[7] = (String)unpackagedHeader.get(
                       IPackagingConstants.CHANNEL_NAME);
    for (int i = 0; i < unpackHeader.length; i++)
      PackagingLogger.debugLog(CLASS_NAME,
                              "getUnpackagedHeader",
                              "Header[" + i + "] = " + unpackHeader[i]);
    return unpackHeader;
  }

  private void storeReceivedFile(int blockNo, byte[] payload, String filename,
          MetaInfo metaInfo, String fileId) throws ApplicationException
  {
    if (metaInfo.checkBlockReceivedStatus(blockNo))
    {
      String metaFilename = metaInfo.getBlockFilename(blockNo);
      if (metaFilename != null)
      {
        if (metaFilename.equals(filename))
          return;
      }
      else if (filename == null)
        return;
      throw new ApplicationException("Filename is not the same: " +
                                     filename + ", " + metaFilename);
    }
    else
    {
      FileJoiner.store(PACKAGING_PATH, metaInfo.getSubPath(), payload, fileId, blockNo);
      metaInfo.setBlockReceivedStatus(blockNo, filename);
    }
  }

  private File[] getUnpackagedPayload(MetaInfo metaInfo,
          boolean isRelay, boolean isZip, FileDescriptor filedesc, String fileId)
          throws FileAccessException, ApplicationException
  {
    synchronized (metaInfo)
    {
      if (metaInfo.isProcessing())
        return null;
      else
        metaInfo.setProcessing(true);
    }
    if (isRelay)
      return FileJoiner.getBlockFiles(PACKAGING_PATH,
                                      _subPath,
                                      fileId,
                                      isZip
                                      );
    else
      return FileJoiner.getOriginalFiles(PACKAGING_PATH,
                                         _subPath,
                                         filedesc.getFilename(),
                                         filedesc.getFileSize(),
                                         fileId,
                                         isZip
                                         );
  }

  // To get file part acknowlegment package information.
  private PackagingInfo getSplitAckPackage(PackagingInfo info) throws PackagingException
  {
    try
    {
      String fileId = info.getTransactionId();
      _subPath = fileId + File.separator;
      FileDescriptor fileDesc = FileDescriptor.retrieve(fileId);
      MetaInfo metaInfo = getMetaInfo(fileId);
      info.setAllPacketsProcessed(true);
      info.setPackagedSplitContent(null);

      info.setEnvelopeHeader(getSplitPackagedHeaderForAck(info));

      info.setPackagedDataContent(processPackagedDataContentForAck(info.getDataContent(),
                                  metaInfo,
                                  fileId
                                  ));
      return info;
    }
    catch (Throwable t)
    {
      PackagingLogger.errorLog(CLASS_NAME, "getSplitAckPackage", "Exception in building package ack", t);
      throw new PackagingException("Exception in building package ack", t);
    }
  }

  // To retrieve metaInfo to send file part acknowledgement
  private MetaInfo getMetaInfo(String fileId)
         throws FileAccessException, ApplicationException
  {
    return MetaInfo.retrieveMetaInfo(fileId);
  }

  // Method to package header for sending file part acknowlegement
  private Hashtable getSplitPackagedHeaderForAck(PackagingInfo info)
          throws PackagingException
  {
    Hashtable envelopeheader = null;
    if((envelopeheader = info.getEnvelopeHeader())== null)
    {
      envelopeheader = new Hashtable();
    }

    envelopeheader.put(ITransportConstants.EVENT_ID,
                       Integer.toString(IFilePartEvent._ACK_FILE_PARTS));
    envelopeheader.put(ITransportConstants.TRANSACTION_ID, "3");
    envelopeheader.put(ITransportConstants.RECEIPENT_NODE_ID,
                       info.getRecepientNodeId() == null ? "0" :
                       info.getRecepientNodeId());
    envelopeheader.put(ITransportConstants.SENDER_NODE_ID,
                       info.getSenderNodeId()== null ? "0" :
                       info.getSenderNodeId());
    envelopeheader.put(ITransportConstants.EVENT_SUB_ID,
                       info.getEventSubId() == null ? "0" :
                       info.getEventSubId());
    envelopeheader.put(ITransportConstants.PROCESS_ID,
                       info.getProcessId()== null ? "0" :
                       info.getProcessId()
                      );
    envelopeheader.put(IPackagingConstants.CHANNEL_NAME,
                       info.getChannelName() == null ? IPackagingConstants.UNDEFINDED_CHANNEL :
                       info.getChannelName());
    if (info.getGNCI() != null)
      envelopeheader.put(ITransportConstants.GRIDNODE_COMM_INFO_STR,
                         info.getGNCI());
    return envelopeheader;
  }

  // Method to package data for send file part acknowledgement
  private String[] processPackagedDataContentForAck(String[] originalData, MetaInfo metaInfo,
          String fileId)
  {
    int originalDataLength = 0;
    int blockReceivedLength = 0;
    if (originalData != null)
      originalDataLength = originalData.length;
    int[] blockReceived = metaInfo.getAllBlocksReceived();
    if (blockReceived != null)
      blockReceivedLength = blockReceived.length;
    String[] data = new String[originalDataLength + blockReceivedLength + 2];
    data[0] = fileId;
    data[1] = Integer.toString(blockReceivedLength);
    for (int i = 0, j = 2; i < blockReceivedLength; i++,j++)
      data[j] = Integer.toString(blockReceived[i]);

    for (int i = 0, j = 2 + blockReceivedLength; i < originalDataLength; i++,j++)
      data[j] = originalData[i];
    return data;
  }

  private PackagingInfo handleSplitPackageAck(PackagingInfo info) throws PackagingException
  {
    try
    {
      PackagingLogger.debugLog(CLASS_NAME, "handleSplitPackageAck", "Start");
      String[] data = info.getDataContent();
      String fileId = data[0];
      int[] filePartRec = getFilePartReceived(data);
      String[] originalData = getOriginalData(data);

      _subPath = fileId + File.separator;
      FileDescriptor fileDesc = FileDescriptor.retrieve(fileId);
      MetaInfo metaInfo = getMetaInfo(fileId);
      metaInfo.setBlockReceivedStatus(filePartRec);
      String originalEventId = metaInfo.getOriginalEventId();
      String transactionId = metaInfo.getTransId();

      if (metaInfo.checkAllBlocksReceived() || originalData != null) // All block received.
      {
        PackagingLogger.debugLog(CLASS_NAME, "handleSplitPackageAck", "All blocks received");
        info.setUnPackagedPayLoad(null);
        info.setDataContent(originalData);
        info.setAllPacketsProcessed(true); // To invoke BL to process
        info.setMoreBlocksToProcess(false); // Don't send ack. Wait for the BL to send ack.
        info.setDefaultUnPackagedHeader(getUnpackagedHeader(
                GNCompatibleEventMap.getInstance().getSplitEventAckId(originalEventId),
                transactionId,
                fileId,
                info.getUnPackagedHeader()));
      }
      else
      {
        PackagingLogger.debugLog(CLASS_NAME,
                                 "handleSplitPackageAck",
                                 "Not all blocks received. Send Next 10");
        info.setUnPackagedPayLoad(null);
        info.setDataContent(null);
        info.setAllPacketsProcessed(false); // Don't Invoke BL
        info.setMoreBlocksToProcess(true); // Send the next 10 or remaining packets.
        info.setDefaultUnPackagedHeader(getUnpackagedHeader(
                                                    originalEventId,
                                                    transactionId,
                                                    fileId,
                                                    info.getUnPackagedHeader()));
      }
      return info;
    }
    catch (Throwable t)
    {
      PackagingLogger.errorLog(CLASS_NAME, "handleSplitPackageAck", "Exception in building package ack", t);
      throw new PackagingException("Exception in building package ack", t);
    }
  }

  // To retrieve original data from the data of the file part acknowledgement.
  private String[] getOriginalData(String[] data)
  {
    int noOfFilePartRec = Integer.parseInt(data[1]);
    String[] originalData = null;
    if ((noOfFilePartRec + 2) < data.length)
    {
      originalData = new String[data.length - noOfFilePartRec - 2];
      for (int i = 0, j = noOfFilePartRec + 2; j < data.length; i++, j++)
      {
        originalData[i] = data[j];
        PackagingLogger.debugLog(CLASS_NAME,
                                 "getOriginalData",
                                 "data[" + i + "] is " + originalData[i]);
      }
    }
    return originalData;
  }

  // To retrieve the file parts received from the data of the file part acknowledgement.
  private int[] getFilePartReceived(String[] data)
  {
    int noOfFilePartRec = Integer.parseInt(data[1]);
    int[] filePartRec = new int[noOfFilePartRec];
    for (int i = 0, j = 2; i < noOfFilePartRec; i++, j++)
    {
      filePartRec[i] = Integer.parseInt(data[j]);
      PackagingLogger.debugLog(CLASS_NAME,
                               "getFilePartReceived",
                               "Files part received is " + filePartRec[i]);
    }
    return filePartRec;
  }

  private void cleanup(PackagingInfo info) throws PackagingException
  {
    try
    {
      String[] originalData = info.getDataContent();
      String fileId = originalData[0];
      MetaInfo.removeMetaInfo(fileId);
      FileHelper.deleteFolder(IPackagingConstants.PACKAGING_PATH,
                              fileId
                              );
    }
    catch (Throwable t)
    {
      PackagingLogger.errorLog(CLASS_NAME, "getSplitAckPackage", "Exception in building package ack", t);
      throw new PackagingException("Exception in building package ack", t);
    }
  }

  private boolean isCleanup(PackagingInfo info)
  {
   String eventId = info.getEventId();
    if (eventId == null)
    {
      Hashtable packagedHeader = info.getUnPackagedHeader();
      eventId = (String) packagedHeader.get(ITransportConstants.EVENT_ID);
    }
    return Integer.toString(IFilePartEvent._SEND_FILE_PARTS_FINISHED).equals(eventId);
  }

  private PackagingInfo handleCleanupPackage(PackagingInfo info) throws PackagingException
  {
    try
    {
      PackagingLogger.debugLog(CLASS_NAME, "handleCleanupPackageAck", "Start");
      info.setUnPackagedPayLoad(null);
      info.setDataContent(null);
      info.setAllPacketsProcessed(false); // Don't Invoke BL
      info.setMoreBlocksToProcess(true); // Don't send any acknowledgement.
      info.setDefaultUnPackagedHeader(super.getDefaultUnPackagedHeader(info));
      return info;
    }
    catch (Throwable t)
    {
      PackagingLogger.errorLog(CLASS_NAME, "handleSplitPackageAck", "Exception in building package ack", t);
      throw new PackagingException("Exception in building package ack", t);
    }
  }
*/
}