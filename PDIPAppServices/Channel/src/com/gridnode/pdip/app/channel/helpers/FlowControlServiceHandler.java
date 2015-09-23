/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FlowControlServiceHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 24 2003    Jagadeesh             Created.
 * Dec  17 2003    Jagadeesh             Modified : Removed reference to Package Data Structure,
 *                                       replaced with framework.Message.
 * Feb 06 2004    Neo Sok Lay             Comment of test code that cause
 *                                        concurrency issue in file parts descriptor.
 *
 */

package com.gridnode.pdip.app.channel.helpers;

import com.gridnode.pdip.app.channel.exceptions.FlowControlException;
import com.gridnode.pdip.app.channel.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.channel.flowcontrol.*;
import com.gridnode.pdip.app.channel.flowcontrol.FileDescriptor;
import com.gridnode.pdip.app.channel.model.FlowControlInfo;
import com.gridnode.pdip.base.packaging.helper.IFilePartEvent;
import com.gridnode.pdip.base.packaging.helper.IPackagingConstants;
import com.gridnode.pdip.base.transport.helpers.ITransportConstants;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;
import com.gridnode.pdip.framework.util.TimeUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.zip.*;

/** @todo Insted of a Direct call to this Service by Message Handling Framework, we can have a delegate which invokes this class. */

public class FlowControlServiceHandler implements IFlowControlServiceHandler
{

  private static final String CLASS_NAME = "FlowControlServiceHandler";
  private final static int MAX_PACKET_SEND_RECEIVE = 10;
  private final static int KILO_BLOCK = 1024;
  private static final int BUFFER = 2048;
  private static final String ORIGINAL_TRANSACTION_ID = "OriginalTransactionId";
  //private String _subPath = null;

  public FlowControlServiceHandler()
  {
  }

  public static Message[] split(
    FlowControlInfo flowControlInfo,
    Message message)
    throws FlowControlException
  {
    final String METHOD_NAME = "split()";
    try
    {

      if (flowControlInfo != null)
      {
        Map flowControlMessageMap =
          perProcessMessageFlow(flowControlInfo, message);
        return performSplitMessageCollection(
          flowControlInfo,
          flowControlMessageMap);
      }
      else
      {
        logInfoCategory(
          METHOD_NAME,
          "[FlowControlInfo is Null - Return Original Message]");
        return new Message[] { message };
      }
    }
    catch (IOException ex)
    {
      throw new FlowControlException(
        "IOException in spliting message",
        ex);
    }
    catch (Exception ex)
    {
      throw new FlowControlException(
        "Exception in spliting message: "+ex.getMessage(),
        ex);
    }

  }

  private static Map perProcessMessageFlow(
    FlowControlInfo info,
    Message flowMessage)
    throws IOException, Exception
  {
    flowMessage.getCommonHeaders().put(
      ICommonHeaders.PAYLOAD_IS_ZIP,
      String.valueOf(info.isZip()));
    Map commonHeaders =
      setSplitCommonHeaders(info, flowMessage.getCommonHeaders());
    flowMessage.setCommonHeaders(commonHeaders);

    if ((flowMessage.getPayLoad() == null)
      || (flowMessage.getPayLoad().length <= 0))
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "split()",
        "[PayLoad is Null or Empty PayLoad - Message]");
      byte[] payload = null;
      flowMessage.setPayLoad(payload); //Set the dataPayLoad as Null.
    }

    return checkSetFlowMessageCollection(info, flowMessage);
  }

  private static Map setSplitCommonHeaders(
    FlowControlInfo info,
    Map commonHeaders)
  {
    commonHeaders.put(
      ICommonHeaders.PAYLOAD_IS_ZIP,
      String.valueOf(info.isZip()));
    commonHeaders.put(
      ICommonHeaders.PAYLOAD_IS_SPLIT,
      String.valueOf(info.isSplit()));
    if (info.isSplit())
    {
      commonHeaders.put(
        ICommonHeaders.PAYLOAD_BLK_SPLIT_SIZE,
        String.valueOf(info.getSplitSize()));
      commonHeaders.put(
        ICommonHeaders.PAYLOAD_BLK_SPLIT_THRESHOLD,
        String.valueOf(info.getSplitThreshold()));
    }
    return commonHeaders;
  }

  private static Map checkSetFlowMessageCollection(
    FlowControlInfo info,
    Message message)
    throws IOException, Exception
  {
    Map flowMessageMap = new HashMap();
    //Message[] flowMessageCollection = new Message[]{message};

    if (!info.isZip() && !info.isSplit())
      //If no Zip and No Split we assume there is only one file
    {
      flowMessageMap
        .put(String.valueOf(IFlowControlServiceHandler.FLOWCONTROL_LEVEL_0),
      //Level #0
      getMessageNoZipSplit(message));
      return flowMessageMap;
    }

    if (info.isZip()) //If Zip is turn on,zip the files.
    {
      Message zippedMessage = null;
      String blfileId =
        (String) message.getCommonHeaders().get(ICommonHeaders.PAYLOAD_ID);
      File zippedFile = getZippedFile(message.getPayLoad(), blfileId);
      if (!info.isSplit())
        //If Zip is true and Split is False, getBytes from Zipped File
      {
        zippedMessage = new Message();
        zippedMessage.setCommonHeaders(message.getCommonHeaders());
        zippedMessage.setMessageHeaders(message.getMessageHeaders());
        zippedMessage.setPayLoad(getBytesFromFile(zippedFile));
        zippedMessage.setData(message.getData());
        flowMessageMap
          .put(String.valueOf(IFlowControlServiceHandler.FLOWCONTROL_LEVEL_2),
        //Level #2
        new Message[] { zippedMessage });
        return flowMessageMap;
      }
      else
      {
        zippedMessage = new Message();
        zippedMessage.setCommonHeaders(message.getCommonHeaders());
        zippedMessage.setMessageHeaders(message.getMessageHeaders());
        zippedMessage.setPayLoad(new File[] { zippedFile });
        zippedMessage.setData(message.getData());
        flowMessageMap
          .put(String.valueOf(IFlowControlServiceHandler.FLOWCONTROL_LEVEL_1),
        //Level #1
        new Message[] { zippedMessage });
        return flowMessageMap;
      }
    }

    /** @todo Check with Menghua, regd this condition...,  */
    if (info.isSplit()) //No Zip but Split --   Level #3
    {
      //This uses proprietry protocol to split the file and zip, which is specific to GridTalk.
      //Hence if the message is intended to third party.., the receiver may not be able to handle this split message.
      flowMessageMap.put(
        String.valueOf(IFlowControlServiceHandler.FLOWCONTROL_LEVEL_3),
        new Message[] { message });
      //Message messageToSplit = flowMessageCollection[0]; //
      //flowMessageCollection = getSplitMessage(info, messageToSplit);
    }
    return flowMessageMap;
  }

  private static Message[] getMessageNoZipSplit(Message message)
    throws IOException
  {
    /** @todo Check with Menghua.., can we do this, or do we need another alogrithm... */
    File[] fileContent = message.getPayLoad();
    byte[] byteFileContent = null;
    if (fileContent != null)
      byteFileContent = getBytesFromFile(fileContent[0]);
    else
      ChannelLogger.infoLog(
        CLASS_NAME,
        "getMessageNoZipSplit()",
        "[File Content is Null]");
    Message flowCnrlMessage = new Message();
    flowCnrlMessage.setCommonHeaders(message.getCommonHeaders());
    flowCnrlMessage.setMessageHeaders(message.getMessageHeaders());
    flowCnrlMessage.setData(message.getData());
    flowCnrlMessage.setPayLoad(byteFileContent);
    return new Message[] { flowCnrlMessage };
  }

  private static Message[] performSplitMessageCollection(
    FlowControlInfo info,
    Map flowControlMap)
    throws FlowControlException, Exception
  {
    String key = (String) flowControlMap.keySet().iterator().next();
    Message[] flowMessage = (Message[]) flowControlMap.get(key);
    Message message = flowMessage[0];
    if (isSplitAckMessageSend(message))
    {
      return getAckMessageSend(message);
    }
    if (IFlowControlServiceHandler.FLOWCONTROL_LEVEL_0 == Integer.parseInt(key)
      || IFlowControlServiceHandler.FLOWCONTROL_LEVEL_2 == Integer.parseInt(key))
    {
      return (Message[]) flowControlMap.get(key);
    }

    if (IFlowControlServiceHandler.FLOWCONTROL_LEVEL_1 == Integer.parseInt(key)
      || IFlowControlServiceHandler.FLOWCONTROL_LEVEL_3 == Integer.parseInt(key))
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "performSplitMessageCollection()",
        "[FlowControl Level]" + Integer.parseInt(key));
      ChannelLogger.debugLog(
        CLASS_NAME,
        "performSplitMessageCollection()",
        "[Files to Split]" + message.getPayLoad().length);
      return getSplitMessage(info, message);
    }
    return null;
  }

  private static boolean isSplitAckMessageSend(Message message)
  {
    String eventId =
      (String) message.getCommonHeaders().get(ICommonHeaders.MSG_EVENT_ID);
    return (GNCompatibleEventRegistry.getInstance().isSplitAck(eventId));
  }
  
//  private static Message[] performMessageSplit(
//    FlowControlInfo info,
//    Message[] flowControlmessage)
//    throws FlowControlException, Exception
//  {
//    Message message = flowControlmessage[0];
//    String eventId =
//      (String) message.getCommonHeaders().get(ICommonHeaders.MSG_EVENT_ID);
//    /**
//     * The below check is needed since, When BL triggers SendAck, Channel need to translate this event
//     * to Send an Actual Ack to "Intermediate Sender" - (in our case GM - Example Scenario is when
//     * BL sends a 3205 event (DownloadAck) Channel need to translate this event to 7010 FilePartAck.
//     */
//    if (GNCompatibleEventRegistry.getInstance().isSplitAck(eventId))
//    {
//      return getAckMessageSend(message); //Perform Ack Split (usually we send
//    }
//    else
//      return getSplitMessage(info, message); //Perform Normal Split
//  }

  /**
   * This method is essentialy responsible to perform Message Split.
   * @param info
   * @param message
   * @return
   * @throws Exception
   */

//  private static Message[] performSplitPackageCollection(
//    FlowControlInfo info,
//    Message message)
//    throws Exception
//  {
//    final String METHOD_NAME = "performSplitPackageCollection";
//    File zippedFile = null;
//    Message[] flowMessageCollection = { message };
//    //Initilize with packageData.
//    String eventId =
//      (String) message.getCommonHeaders().get(ICommonHeaders.MSG_EVENT_ID);
//
//    ChannelLogger.debugLog(
//      CLASS_NAME,
//      METHOD_NAME,
//      "[Begin splitPackageCollection-EventId=][" + eventId + "]");
//    if (GNCompatibleEventRegistry.getInstance().isSplitAck(eventId))
//    {
//      /**
//       * This Condition sets to be applicable when Ack Message is sent to GM,
//       * GT should  translate this Event to File_Part_Ack Event(7010).
//       */
//      return getAckMessageSend(message);
//    }
//
//    if (!info.isZip() && !info.isSplit())
//      //If no Zip and No Split we assume there is only one file
//    {
//      File[] fileContent = message.getPayLoad();
//      String[] dataContent = message.getData();
//      Map header = message.getCommonHeaders();
//      byte[] byteFileContent = null;
//      if (fileContent != null)
//        byteFileContent = getBytesFromFile(fileContent[0]);
//      else
//        ChannelLogger.infoLog(
//          CLASS_NAME,
//          "performPackageFlowControl()",
//          "[File Content is Null]");
//      Message flowCnrlMessage = new Message();
//      flowCnrlMessage.setCommonHeaders(header);
//      flowCnrlMessage.setMessageHeaders(message.getMessageHeaders());
//      flowCnrlMessage.setData(dataContent);
//      flowCnrlMessage.setPayLoad(byteFileContent);
//      flowMessageCollection = new Message[] { flowCnrlMessage };
//      return flowMessageCollection;
//    }
//
//    if (info.isZip()) //If Zip is turn on,zip the files.
//    {
//      Message zippedMessage = null;
//      String blfileId =
//        (String) message.getCommonHeaders().get(ICommonHeaders.PAYLOAD_ID);
//      zippedFile = getZippedFile(message.getPayLoad(), blfileId);
//      if (!info.isSplit())
//        //If Zip is true and Split is False, getBytes from Zipped File
//      {
//        zippedMessage = new Message();
//        zippedMessage.setCommonHeaders(message.getCommonHeaders());
//        zippedMessage.setMessageHeaders(message.getMessageHeaders());
//        zippedMessage.setPayLoad(getBytesFromFile(zippedFile));
//        zippedMessage.setData(message.getData());
//      }
//      else
//      {
//        zippedMessage = new Message();
//        zippedMessage.setCommonHeaders(message.getCommonHeaders());
//        zippedMessage.setMessageHeaders(message.getMessageHeaders());
//        zippedMessage.setPayLoad(new File[] { zippedFile });
//        zippedMessage.setData(message.getData());
//      }
//      flowMessageCollection = new Message[] { zippedMessage };
//      //Override flowControlPackageCollection.
//    }
//
//    if (info.isSplit())
//    {
//      ChannelLogger.debugLog(CLASS_NAME, METHOD_NAME, "[Only Split Enabled]");
//      //Message messageToSplit = flowMessageCollection[0]; //
//      ChannelLogger.debugLog(
//        CLASS_NAME,
//        METHOD_NAME,
//        message.getCommonHeaders().toString());
//      //Debug purpose only.
//      flowMessageCollection = getSplitMessage(info, message);
//
//    }
//    return flowMessageCollection;
//  }

  private static Message[] getAckMessageSend(Message message)
    throws FlowControlException
  {
    try
    {
      String fileId =
        (String) message.getCommonHeaders().get(
          ICommonHeaders.MSG_TRANSACTION_ID);
      byte[] payLoad = null;
      //String fileId = info.getTransactionId();
      //_subPath = fileId + File.separator;
      //FileDescriptor fileDesc = FileDescriptor.retrieve(fileId);
      FileDescriptor.retrieve(fileId);
      FilePartsDescriptor filePartsDesc =
        FilePartsDescriptorStore.getFilePartsDescriptor(fileId);
      message.setPayLoad(payLoad);
      message.setCommonHeaders(
        getAckMessageSendHeader(message.getCommonHeaders()));
      if (filePartsDesc != null)
      {
        int[] filesReceived = filePartsDesc.getAllBlocksReceived();
        for (int i = 0; i < filesReceived.length; i++)
        {
          ChannelLogger.debugLog(
            CLASS_NAME,
            "getAckMessageSend()",
            "[FilePart]" + i);
        }
      }
      else
      {
        ChannelLogger.debugLog(
          CLASS_NAME,
          "getAckMessageSend()",
          "[FilePartsDescriptor is Null]");
      }

      String[] data =
        getAckMessageSendData(message.getData(), filePartsDesc, fileId);
      if (data != null)
      {
        for (int i = 0; i < data.length; i++)
        {
          ChannelLogger.debugLog(
            CLASS_NAME,
            "getAckMessageSend()",
            "[Data=]" + data[i]);
        }
      }
      else
      {
        ChannelLogger.debugLog(
          CLASS_NAME,
          "getAckMessageSend()",
          "[Data is Null]");
      }
      message.setData(data);
      return new Message[] { message };
    }
    catch (FileAccessException ex)
    {
      throw new FlowControlException(
        "[getAckMessageSend()][Cannot Not Send AckMessage]",
        ex);
    }
    catch (ApplicationException ex)
    {
      throw new FlowControlException(
        "[getAckMessageSend()][Cannot Not Send AckMessage]",
        ex);
    }
  }

  private static Map getAckMessageSendHeader(Map commonHeader)
  {
    if (commonHeader == null || commonHeader.isEmpty())
      return commonHeader;
    else
    {
      commonHeader.put(
        ICommonHeaders.MSG_EVENT_ID,
        String.valueOf(IFilePartEvent._ACK_FILE_PARTS));
      commonHeader.put(ICommonHeaders.MSG_TRANSACTION_ID, "3");
      return commonHeader;
    }
  }

  private static String[] getAckMessageSendData(
    String[] originalData,
    FilePartsDescriptor filePartsDesc,
    String fileId)
  {
    int originalDataLength = 0;
    int blockReceivedLength = 0;
    if (originalData != null)
      originalDataLength = originalData.length;
    int[] blockReceived = filePartsDesc.getAllBlocksReceived();
    if (blockReceived != null)
      blockReceivedLength = blockReceived.length;
    String[] data = new String[originalDataLength + blockReceivedLength + 2];
    data[0] = fileId;
    data[1] = Integer.toString(blockReceivedLength);
    for (int i = 0, j = 2; i < blockReceivedLength; i++, j++)
      data[j] = Integer.toString(blockReceived[i]);

    for (int i = 0, j = 2 + blockReceivedLength;
      i < originalDataLength;
      i++, j++)
      data[j] = originalData[i];
    return data;

  }

  public static Message[] getSplitMessage(
    FlowControlInfo info,
    Message messageToSplit)
    throws Exception
  {
    String fileId =
      (String) messageToSplit.getCommonHeaders().get(ICommonHeaders.PAYLOAD_ID);
    String transactionId =
      (String) messageToSplit.getCommonHeaders().get(
        ICommonHeaders.MSG_TRANSACTION_ID);
    String eventId =
      (String) messageToSplit.getCommonHeaders().get(
        ICommonHeaders.MSG_EVENT_ID);
    String channelName =
      (String) messageToSplit.getCommonHeaders().get(
        ICommonHeaders.COMM_CHANNEL);
    String gnci =
      (String) messageToSplit.getCommonHeaders().get(ICommonHeaders.COMM_GNCI);

    ChannelLogger.debugLog(
      CLASS_NAME,
      "getSplitMessage()",
      "[FileId=][" + fileId + "]");

    if (fileId == null) //14/01/2004 Modified to check if fileid == null
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getSplitMessage()",
        "[FileId was null]");
      return new Message[] { messageToSplit };
    }
    //we cannot split any payload, without a file id, and without fileContent

    String subPath = fileId + File.separator;
    //Get File Descriptor .
    FileDescriptor fileDesc =
      FileDescriptor.retrieve(messageToSplit.getPayLoad(), fileId, channelName);

    ChannelLogger.debugLog(
      CLASS_NAME,
      "getSplitMessage()",
      "[After Getting FileDescriptor]");
    //Creates if fileDescriptor dose not exists else return the existing MetaInfo object.
    /*
                  UnComment this after test for missing packets.
                FilePartsDescriptor filePartsDescriptor =
    	FilePartsDescriptorStore.retrieveFilePartsDescriptor(fileId);
                */

    /*040206NSL: code for testing? causing concurrency issue
    FilePartsDescriptor filePartsDescriptor = null;
    try
    {
      filePartsDescriptor = new FilePartsDescriptor(fileId);
    }
    catch (FileAccessException ex)
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getSplitMessage()",
        "[FilePartsDescriptor was Not Created...]");
    }
    */
    //040206NSL
    FilePartsDescriptor filePartsDescriptor =
      FilePartsDescriptorStore.retrieveFilePartsDescriptor(fileId);

    if (filePartsDescriptor == null)
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getSplitMessage()",
        "[Making FilePartsDescriptor]");
      filePartsDescriptor =
        FilePartsDescriptorStore.getFilePartsDescriptor(
          messageToSplit.getPayLoad(),
          fileId,
          info.isZip(),
          info.getSplitSize() * KILO_BLOCK,
          info.getSplitThreshold() * KILO_BLOCK,
          transactionId,
          eventId,
          messageToSplit.getData());
      if (gnci != null)
        filePartsDescriptor.setGNCI(gnci);

      FilePartsDescriptorStore.addFilePartsDescriptor(
        filePartsDescriptor,
        fileId);
    }

    ChannelLogger.debugLog(
      CLASS_NAME,
      "getSplitMessage()",
      "[B4 Getting Blocks To Send]");
    Message[] blockMessage =
      getNextBlockToSend(
        filePartsDescriptor,
        messageToSplit,
        info,
        fileId,
        fileDesc,
        subPath);
    return blockMessage;
  }

  private static Message[] getNextBlockToSend(
    FilePartsDescriptor filePartsDescriptor,
    Message messageData,
    FlowControlInfo info,
    String fileId,
    FileDescriptor fileDesc,
    String subPath)
    throws Exception
  {
    Message splitMessages[] = {
    }; //splitPackages
    int[] blocksNotReceived =
      filePartsDescriptor.getBlocksNotReceived(MAX_PACKET_SEND_RECEIVE);
    //int currentBlock = retrieveNextBlockNo(filePartsDescriptor);
    boolean allReceived = checkBlocksReceivedStatus(blocksNotReceived);
    if (allReceived) //Send back the first block.
    {
      filePartsDescriptor.unsetBlockReceivedStatus(0);
      int currentBlock = retrieveNextBlockNo(filePartsDescriptor);
      byte[] splitBloc =
        retrieveBlock(filePartsDescriptor, currentBlock, subPath);
      HashMap envelopeHeader =
        getSplitPackagedHeader(
          messageData,
          filePartsDescriptor.getBlockFilename(currentBlock),
          filePartsDescriptor.getGNCI());
      String[] packagedDataContent =
        processPackagedDataContent(
          messageData.getData(),
          filePartsDescriptor.getOriginalEventId(),
          fileId,
          currentBlock,
          filePartsDescriptor.getTotalBlocks(),
          fileDesc.getFilesDesciptor());

      Message sendFirstMessage = new Message();
      sendFirstMessage.setCommonHeaders(envelopeHeader);
      sendFirstMessage.setMessageHeaders(messageData.getMessageHeaders());
      sendFirstMessage.setData(packagedDataContent);
      sendFirstMessage.setPayLoad(splitBloc);
      //new Package(envelopeHeader, packagedDataContent, splitBloc);
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getNextBlockToSend()",
        "Sending back the First Block!");
      return new Message[] { sendFirstMessage };
    }
    else
    {
      //int currentBlock = blocksNotReceived[0];
      int totalBlksReceived = filePartsDescriptor.getAllBlocksReceived().length;
      ChannelLogger.infoLog(
        CLASS_NAME,
        "getNextBlockToSend()",
        "IS_FINISHED_ONCE=[" + filePartsDescriptor.isFinishedOnce() + "]");
      ChannelLogger.infoLog(
        CLASS_NAME,
        "getNextBlockToSend()",
        "No Of Blocks Not Received=[" + blocksNotReceived.length + "]");

      /* if (filePartsDescriptor.isFinishedOnce()
         && blocksNotReceived.length == 1
         && filePartsDescriptor.getTotalBlocks() > 1)
       {
         int[] remainingBlocks =
           filePartsDescriptor.getBlocksNotReceived(MAX_PACKET_SEND_RECEIVE - 1);
         ChannelLogger.infoLog(
           CLASS_NAME,
           "getNextBlockToSend()",
           "Remaining Blocks Not Received=[" + remainingBlocks.length + "]");
         int[] resultArray = new int[remainingBlocks.length + 1];
         ChannelLogger.infoLog(
           CLASS_NAME,
           "getNextBlockToSend()",
           "ResultArray Size=[" + resultArray.length + "]");
         System.arraycopy(blocksNotReceived, 0, resultArray, 0, 1);
         ChannelLogger.infoLog(
           CLASS_NAME,
           "getNextBlockToSend()",
           "ResultArray Size=["
             + resultArray.length
             + "]"
             + "ResultArray Element=["
             + resultArray[0]
             + "]"); System.arraycopy(
       * remainingBlocks, 0, resultArray, 1, remainingBlocks.length);
       * ChannelLogger.infoLog( CLASS_NAME, "getNextBlockToSend()",
       * "ResultArray Size=[" + resultArray.length + "]"); for (int i = 0;
       * i < resultArray.length; i++) { ChannelLogger.infoLog(
       * CLASS_NAME, "getNextBlockToSend()", "ResultArray AT=[" + i + "]" +
       * "ResultArray Element=[" + resultArray[i] + "]"); }
       * blocksNotReceived = resultArray; }
       */

      // This Info provides info abt LastBlockSent (nth block):
      ChannelLogger.infoLog(
        CLASS_NAME,
        "getNextBlockToSend()",
        "Last Block Sent=[" + filePartsDescriptor.getLastBlockSent() + "]");

      // Total Blcks Received as in Acknowledged - (i.e Blocks Received
      // Acknowledged )
      ChannelLogger.infoLog(
        CLASS_NAME,
        "getNextBlockToSend()",
        "All Bclks Received=[" + totalBlksReceived + "]");

      //Total Blocks for this FilePartDescriptor - (i.e 'n' no of Blocks
      // = totalfilesize/blocksize).
      ChannelLogger.infoLog(
        CLASS_NAME,
        "getNextBlockToSend()",
        "Total BLKS =[" + filePartsDescriptor.getTotalBlocks() + "]");

      /*
       * ChannelLogger.infoLog( CLASS_NAME, "getNextBlockToSend()", "All
       * Bclks Received=[" + totalBlksReceived + "]");
       * ChannelLogger.infoLog( CLASS_NAME, "getNextBlockToSend()",
       * "Total BLKS =[" + filePartsDescriptor.getTotalBlocks() + "]");
       * //ChannelLogger.infoLog(CLASS_NAME,"getNextBlockToSend()","Current
       * BLK=["+currentBlock+"]"); ChannelLogger.infoLog( CLASS_NAME,
       * "getNextBlockToSend()", "No Of Blocks Not Received=[" +
       * blocksNotReceived.length + "]");
       */

      for (int i = 0; i < blocksNotReceived.length; i++)
      {
        ChannelLogger.infoLog(
          CLASS_NAME,
          "getNextBlockToSend()",
          "BLOCK NOTRECEIVED=["
            + blocksNotReceived[i]
            + "]BlockName=["
            + filePartsDescriptor.getBlockFilename(blocksNotReceived[i])
            + "]");
      }
      splitMessages = new Message[blocksNotReceived.length];

      ChannelLogger.debugLog(
        CLASS_NAME,
        "getNextBlockToSend()",
        "SplitPackage[] Size" + splitMessages.length);

      for (int i = 0; i < splitMessages.length; i++)
      {
        ChannelLogger.debugLog(
          CLASS_NAME,
          "getNextBlockToSend()",
          "Packaging Packet " + i);
        byte[] splitBloc =
          retrieveBlock(filePartsDescriptor, blocksNotReceived[i], subPath);
        ChannelLogger.debugLog(
          CLASS_NAME,
          "getNextBlockToSend()",
          "After Packaging Packet" + i);
        HashMap envelopeHeader =
          getSplitPackagedHeader(
            messageData,
            filePartsDescriptor.getBlockFilename(blocksNotReceived[i]),
            filePartsDescriptor.getGNCI());
        ChannelLogger.debugLog(
          CLASS_NAME,
          "getNextBlockToSend()",
          "EnvelopeHeader=[" + envelopeHeader + "]");
        String[] packagedDataContent =
          processPackagedDataContent(
            messageData.getData(),
            filePartsDescriptor.getOriginalEventId(),
            fileId,
            blocksNotReceived[i],
            filePartsDescriptor.getTotalBlocks(),
            fileDesc.getFilesDesciptor());
        Message splitMessage = new Message();
        splitMessage.setCommonHeaders(envelopeHeader);
        splitMessage.setMessageHeaders(messageData.getMessageHeaders());
        splitMessage.setPayLoad(splitBloc);
        splitMessage.setData(packagedDataContent);
        //new Package(envelopeHeader, packagedDataContent, splitBloc);
        splitMessages[i] = splitMessage;
        //currentBlock+=1;
      }

    }
    return splitMessages;
  }

  private static HashMap getSplitPackagedHeader(
    Message messageData,
    String filename,
    String gnci)
    throws FlowControlException
  {
    HashMap envelopeheader = null;
    try
    {
      if (messageData.getCommonHeaders() != null)
      {
        envelopeheader = new HashMap(messageData.getCommonHeaders());
        ChannelLogger.debugLog(
          CLASS_NAME,
          "getSplitPackagedHeader()",
          "[Cloning Header ...]");
      }
      else
      {
        envelopeheader = new HashMap();
        ChannelLogger.debugLog(
          CLASS_NAME,
          "getSplitPackagedHeader()",
          "[Creating Hashtable for Header]");
      }
      String originalTransactionId =
        (String) envelopeheader.get(ICommonHeaders.MSG_TRANSACTION_ID);
      envelopeheader.put(
        ICommonHeaders.MSG_EVENT_ID,
        Integer.toString(IFilePartEvent._SEND_FILE_PART));
      envelopeheader.put(ICommonHeaders.MSG_TRANSACTION_ID, "3");
      envelopeheader.put(ICommonHeaders.PAYLOAD_ID, filename);
      envelopeheader.put(ICommonHeaders.COMM_GNCI, gnci);

      /** @todo To consider to put this header in Message Headers... */
      if (originalTransactionId != null)
        envelopeheader.put(ORIGINAL_TRANSACTION_ID, originalTransactionId);

      ChannelLogger.debugLog(
        CLASS_NAME,
        "getSplitPackagedHeader()",
        "SplitPackage-Header=[" + envelopeheader.toString() + "]");
      return envelopeheader;
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getSplitPackagedHeader",
        "Could not build package header",
        t);
      throw new FlowControlException("Could not build package header", t);
    }
  }

  /**
   * join - The Splitted Packages..
   *
   * @param info
   * @param packageData
   * @return @throws
   *         FlowControlException
   * @throws Exception
   */

  public static Message join(FlowControlInfo info, Message message)
    throws FlowControlException
  {
    Map header = null;
    if (message == null)
      return message;
    else
    {
      header = message.getCommonHeaders();
      try
      {
        String eventID = (String) header.get(ICommonHeaders.MSG_EVENT_ID);
        ChannelLogger.infoLog(
          CLASS_NAME,
          "join()",
          "EventId=[" + eventID + "]");

        if (GNCompatibleEventRegistry.getInstance().isSplitAckEvent(eventID))
          return handleReceiveSplitAck(info, message);
        else if (GNCompatibleEventRegistry.getInstance().isSplitEvent(eventID))
          return handleReceiveSplit(info, message);
        else
          return handleDefaultReceive(info, message);
      }
      catch (Exception ex)
      {
        throw new FlowControlException(
          "[" + CLASS_NAME + "][join()][Cannot perform join]",
          ex);
      }
    }

  }

  private static Message handleDefaultReceive(
    FlowControlInfo info,
    Message message)
    throws Exception
  {
    try
    {
      if ((message.getPayLoadData() == null)
        || (message.getPayLoadData().length == 0)
        || (info == null))
      {
        ChannelLogger.debugLog(
          CLASS_NAME,
          "join()",
          "[No PayLoad or PayLoad was Null or FlowControlInfo was Null.Return original Message]");
        return message;
      }
      ChannelLogger.debugLog(
        CLASS_NAME,
        "handleDefaultReceive()",
        "DefaultReceive");
      String fileId =
        (String) message.getCommonHeaders().get(ICommonHeaders.PAYLOAD_ID);
      byte[] payLoad = message.getPayLoadData();
      File[] unZipFiles = {
      };

      if (null == payLoad)
      {
        //Return empty File[] if payLoad is Null.
        message.setPayLoad(unZipFiles);
        //packageData.setValue(IPackageConstants.PACKAGE_FILECONTENT,
        // unZipFiles);
        return message;
      }

      if (info.isZip())
      {
        ChannelLogger.debugLog(
          CLASS_NAME,
          "handleDefaultReceive()",
          "Unzipping");
        unZipFiles = unZipFile(payLoad, fileId);
      }
      else
      {
        //Added on 05/Jan/2003
        //return message; //Return original message, since we cannot handle this message, if not zipped.
        //The above logic dose not apply for messages received from GridMaster. So we check the paylode.type=GN_BACKWARDCOMPATIBLE

        if (ITransportConstants
          .PACKAGE_TYPE_GNBACKWARDCOMPATIBLE
          .equals(message.getCommonHeaders().get(ICommonHeaders.PAYLOAD_TYPE)))
        {
          ChannelLogger.debugLog(
            CLASS_NAME,
            "handleDefaultReceive()",
            "Getting File from PayLoad (without unzip as is). Since receive from (GM or GT1.x)");

          File file = getFileFromBytes(payLoad, fileId);
          if (file != null)
          {
            ChannelLogger.debugLog(
              CLASS_NAME,
              "handleDefaultReceive()",
              "File Path=" + file.getAbsolutePath());
            unZipFiles = new File[] { file };
          }
          else
            unZipFiles = new File[] {
          };

        }
        else
          return message;
        //Return original message, since we cannot handle this message, if not zipped.

        /*
        * Commeted to test for normal Send And Receive...... File file =
        * getFileFromBytes(payLoad, fileId); if (file != null)
        * unZipFiles = new File[] { file }; else unZipFiles = new
        * File[] { };
        */

      }
      Message joinMessage = new Message();
      joinMessage.setCommonHeaders(message.getCommonHeaders());
      joinMessage.setMessageHeaders(message.getMessageHeaders());
      joinMessage.setData(message.getData());
      joinMessage.setPayLoad(unZipFiles);
      return joinMessage;
    }
    catch (Exception ex)
    {
      throw new FlowControlException(
        "["
          + CLASS_NAME
          + "][handleDefaultReceive()]"
          + "Could Not perform Default Receive",
        ex);
    }
  }

  public static Message handleReceiveSplitAck(
    FlowControlInfo info,
    Message message)
    throws FlowControlException
  {
    Message splitAckmessage = null;
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "handleReceiveSplitAck", "[Start]");
      String[] data = message.getData();
      String fileId = data[0];
      int[] filePartRec = getFilePartReceived(data);
      String[] originalData = getOriginalData(data);

      //String subPath = fileId + File.separator;
      FileDescriptor fileDesc = FileDescriptor.retrieve(fileId);
      FilePartsDescriptor filePartsDesc =
        FilePartsDescriptorStore.retrieveFilePartsDescriptor(fileId);
      filePartsDesc.setBlockReceivedStatus(filePartRec);

      String originalEventId = filePartsDesc.getOriginalEventId();
      String transactionId = filePartsDesc.getTransId();
      String channelName = fileDesc.getChannelName();

      if (filePartsDesc.checkAllBlocksReceived()
        || originalData != null) // All block received.
      {
        ChannelLogger.debugLog(
          CLASS_NAME,
          "handleReceiveSplitAck",
          "[All blocks received]");
        //Needs a Mapping from original eventid to
        //Integer.parseInt(originalEventId)+1;

        String eventId =
          GNCompatibleEventRegistry.getInstance().getSplitEventAckId(
            originalEventId);

        splitAckmessage = new Message();
        Map commonHeaders = message.getCommonHeaders();
        commonHeaders.put(ICommonHeaders.MSG_EVENT_ID, eventId);
        commonHeaders.put(ICommonHeaders.MSG_TRANSACTION_ID, transactionId);
        splitAckmessage.setCommonHeaders(commonHeaders);
        splitAckmessage.setData(originalData);
        splitAckmessage.setMessageHeaders(message.getMessageHeaders());
        splitAckmessage.setPayLoad(message.getPayLoadData());
        //Call CleanupFilePartMessageHandler to perform the cleanup
        // and perform a direct send.
        //Parameters need to send are fileId, transactionId,modifed
        // dataContent,
        //Note we are using the same channel used for send initiation.
        CleanupFilePartMessageHandler.sendCleanupFileParts(
          message,
          channelName);

      }
      else //To invoke SendFilePartMessageHandler.
        {
        ChannelLogger.debugLog(
          CLASS_NAME,
          "handleReceiveSplitAck",
          "[Not all blocks received. Send Next 10]");
        SendFilePartMessageHandler.sendNextBlock(
          fileId,
          originalEventId,
          transactionId,
          channelName,
          message);
      }
      //return info;

    }
    catch (Exception ex)
    {
      throw new FlowControlException("[Cannont perform split]", ex);
    }
    return splitAckmessage;
  }

  public static Message handleReceiveSplit(
    FlowControlInfo info,
    Message message)
    throws FlowControlException
  {
    try
    {
      if (!isCleanup(message))
        return joinSplitMessage(info, message);
      else
      {
        cleanup(message);
        return null;
      }
    }
    catch (Exception ex)
    {
      throw new FlowControlException("Cannot perform FlowControlService", ex);
    }
  }

  private static boolean isCleanup(Message message)
  {
    String eventId =
      (String) message.getCommonHeaders().get(ICommonHeaders.MSG_EVENT_ID);
    if (eventId != null)
      return Integer.toString(IFilePartEvent._SEND_FILE_PARTS_FINISHED).equals(
        eventId);
    else
      return false;
  }

  private static Message joinSplitMessage(
    FlowControlInfo info,
    Message message)
    throws FlowControlException
  {
    try
    {
      Message unpackagedMessage = null;

      Map header = message.getCommonHeaders();
      String[] dataContent = message.getData();

      String fileId = dataContent[1];
      int blockNo = Integer.parseInt(dataContent[2]);
      int totalBlock = Integer.parseInt(dataContent[3]);
      String originalEventId = dataContent[4];
      String fileSplitDesc = dataContent[7];
      String originalTransId =
        (String) header.get(ICommonHeaders.MSG_TRANSACTION_ID);

      /**
       * @todo To consider this option as we need the same transactionid
       * at recipient. As per Soklay. For now we will live with as above
       */
      //String originalTransId = (String)
      // header.get(ORIGINAL_TRANSACTION_ID); -- This can be done if a
      // direct send from gtas -to - gtas. if (via GM this is useless)

      if (originalTransId == null)
        originalTransId =
          (String) header.get(ITransportConstants.TRANSACTION_ID);
      String filename = (String) header.get(ICommonHeaders.PAYLOAD_ID);
      //String sender = (String) header.get(ICommonHeaders.SENDER_BE_GNID);
      //String recipient = (String) header.get(ICommonHeaders.RECIPENT_BE_GNID);
      //String processId = (String) header.get(ICommonHeaders.MSG_PROCESS_ID);
      //String channelName = (String) header.get(ICommonHeaders.COMM_CHANNEL);
      //String eventSubId = (String) header.get(ICommonHeaders.MSG_EVENT_SUB_ID);

      String subPath = fileId + File.separator;
      FileDescriptor fileDesc = FileDescriptor.retrieve(fileSplitDesc, fileId);
      //Get FileDescriptor.
      FilePartsDescriptor filePartsDescriptor =
        FilePartsDescriptorStore.retrieveFilePartsDescriptor(
          fileId,
          totalBlock);

      if (filePartsDescriptor == null)
      {
        filePartsDescriptor =
          FilePartsDescriptorStore.getFilePartsDescriptor(fileId, totalBlock);
        FilePartsDescriptorStore.addFilePartsDescriptor(
          filePartsDescriptor,
          fileId);
      }
      synchronized (filePartsDescriptor)
      {
        filePartsDescriptor.setOriginalEventId(originalEventId);
        storeReceivedFile(
          blockNo,
          message.getPayLoadData(),
          filename,
          filePartsDescriptor,
          fileId);
        if (((filePartsDescriptor.getNoOfBlocksReceived()
          % MAX_PACKET_SEND_RECEIVE)
          == 0))
        {
          //Send null dataContent and null fileContent to
          // SplitFilePartAckMessageHandler.
          Message splitAckMessage = new Message();
          splitAckMessage.setCommonHeaders(message.getCommonHeaders());
          splitAckMessage.setMessageHeaders(message.getMessageHeaders());
          splitAckMessage.setData(null);
          splitAckMessage.setPayLoad(new byte[] {
          });
          SendFilePartAckMessageHandler.sendFilePartsAcknowledge(
            fileId,
            splitAckMessage);
          return null;
        }

      }

      if (filePartsDescriptor.checkAllBlocksReceived() // All block
        // received
        && !filePartsDescriptor.isProcessing()) // and nobody is
        // processing
      {
        File[] unpackagedPayload =
          getUnpackagedPayload(
            filePartsDescriptor,
            false,
            info.isZip(),
            fileDesc,
            fileId,
            subPath);
        ChannelLogger.infoLog(
          CLASS_NAME,
          "joinSplitPackage()",
          "After Unpackaging Files");
        if (unpackagedPayload != null)
        {
          if (info.isZip())
          {
            File[] unZipFiles =
              unZipFile(getBytesFromFile(unpackagedPayload[0]), fileId);
            unpackagedPayload = unZipFiles;
            ChannelLogger.infoLog(
              CLASS_NAME,
              "joinSplitPackage()",
              "UnZipped FileLength=[" + unZipFiles.length + "]");
          }
          String[] unpackageDataContent =
            processUnpackagedDataContent(
              dataContent,
              filePartsDescriptor.getTimeCreated());
          ChannelLogger.infoLog(
            CLASS_NAME,
            "joinSplitPackage()",
            "After Unpackaging Files");
          Map defaultHeader =
            getUnpackagedHeader(
              originalEventId,
              originalTransId,
              fileId,
              header);
          ChannelLogger.infoLog(
            CLASS_NAME,
            "joinSplitPackage()",
            "After getting Default Header");
          unpackagedMessage = new Message();
          unpackagedMessage.setData(unpackageDataContent);
          unpackagedMessage.setPayLoad(unpackagedPayload);
          unpackagedMessage.setCommonHeaders(defaultHeader);
          unpackagedMessage.setMessageHeaders(message.getMessageHeaders());

          ChannelLogger.infoLog(
            CLASS_NAME,
            "joinSplitPackage()",
            "B4 returning"
              + "No of Files=["
              + unpackagedPayload.length
              + "]"
              + "Header=["
              + header
              + "]");
          for (int i = 0; i < unpackageDataContent.length; i++)
          {
            ChannelLogger.infoLog(
              CLASS_NAME,
              "joinSplitPackage()",
              "DataContent[" + i + "]Value[" + unpackageDataContent[i] + "]");
          }
          return unpackagedMessage;
          //here we return All packets received, to BL to invoke BL
          // listener.
        }
        else
          ChannelLogger.debugLog(
            CLASS_NAME,
            "joinSplitPackage",
            "Exception in unPackage & join");
      }

      //boolean sentAck = false;
      //int blockReceived = filePartsDescriptor.getNoOfBlocksReceived();

      /*
       * synchronized(filePartsDescriptor) {
       *
       * if (((filePartsDescriptor.getNoOfBlocksReceived() %
       * MAX_PACKET_SEND_RECEIVE) == 0)) { //Send null dataContent and
       * null fileContent to SplitFilePartAckMessageHandler. int i=0;
       * //Test Purpose only. Message splitAckMessage = new Message();
       * splitAckMessage.setCommonHeaders(message.getCommonHeaders());
       * splitAckMessage.setMessageHeaders(message.getMessageHeaders());
       * splitAckMessage.setData(null); splitAckMessage.setPayLoad(new
       * byte[]{}); if (!sentAck) { sentAck = true; i++;
       * SendFilePartAckMessageHandler.sendFilePartsAcknowledge( fileId,
       * splitAckMessage);
       * ChannelLogger.debugLog(CLASS_NAME,"joinSplitMessage()","[SentAck]"+i);
       * return null; }
       * //ChannelLogger.debugLog(CLASS_NAME,"joinSplitMessage()","[BlockReceived
       * Size="+blockReceived+"]");
       * //ChannelLogger.debugLog(CLASS_NAME,"joinSplitMessage()","[Result
       * of="+(blockReceived % MAX_PACKET_SEND_RECEIVE)+"]");
       *  }
       */
      return unpackagedMessage;
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "joinSplitPackage",
        "Exception in unpackage and join",
        t);
      throw new FlowControlException("Exception in unpackage and join", t);
    }
  }

  /*
  private synchronized void sendFilePartAck()
  {
  }*/

  private static Map getUnpackagedHeader(
    String originalEventId,
    String originalTransId,
    String fileId,
    Map header)
  {
    ChannelLogger.infoLog(
      CLASS_NAME,
      "getUnpackagedHeader()",
      "In unpackageHeader");
    if (header != null)
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "getUnpackagedHeader()",
        "Overwriting EventId");
      header.put(ICommonHeaders.MSG_EVENT_ID, originalEventId);
      /** @todo Varify that we are setting fileid to transactionId. */
      //041203 Set TransactionId to PayLoadId, since when sending Ack,
      // we use this fileId to send Ack.
      //header.put(ICommonHeaders.MSG_TRANSACTION_ID, originalTransId);
      header.put(ICommonHeaders.MSG_TRANSACTION_ID, fileId);
      header.put(ICommonHeaders.PAYLOAD_ID, fileId);
    }
    ChannelLogger.infoLog(
      CLASS_NAME,
      "getUnpackagedHeader()",
      "EventId=[" + header.get(ITransportConstants.EVENT_ID));
    return header;
  }

  private static int retrieveNextBlockNo(FilePartsDescriptor filePartsDescriptor)
    throws FileAccessException
  {
    int[] blocksNotReceived = filePartsDescriptor.getBlocksNotReceived(1);
    if (blocksNotReceived == null || blocksNotReceived.length < 1)
      return -1;
    return blocksNotReceived[0];
  }

  private static boolean checkBlocksReceivedStatus(int[] blocksReceived)
  {
    if (blocksReceived == null || blocksReceived.length < 1)
      return true;
    else
      return false;
  }

  private static byte[] retrieveBlock(
    FilePartsDescriptor filePartsDescriptor,
    int blockNo,
    String subPath)
    throws FileAccessException
  {
    if (blockNo < 0)
      return null;
    return FileSplitter.getBlock(
      IPackagingConstants.PACKAGING_PATH,
      subPath,
      filePartsDescriptor.getBlockFilename(blockNo));
  }

  private static String[] processPackagedDataContent(
    String[] originalDataContent,
    String originalEventId,
    String fileId,
    int blockNo,
    int totalBlock,
    String fileSplitDesc)
  {
    String[] dataContent;
    boolean isDataContentNull = (originalDataContent == null) ? true : false;
    if (isDataContentNull)
      dataContent = new String[8];
    else
      dataContent = new String[originalDataContent.length + 8];
    dataContent[0] = "1";
    dataContent[1] = fileId;
    dataContent[2] = Integer.toString(blockNo);
    dataContent[3] = Integer.toString(totalBlock);
    dataContent[4] = originalEventId;
    dataContent[5] = Long.toString(TimeUtil.localToUtc());
    dataContent[6] = Long.toString(600000);
    dataContent[7] = fileSplitDesc;
    //dataContent[8] = transId;
    if (!isDataContentNull)
      System.arraycopy(
        originalDataContent,
        0,
        dataContent,
        8,
        originalDataContent.length);
    return dataContent;
  }

  public static void cleanup(Message message) throws FlowControlException
  {
    try
    {
      String[] originalData = message.getData();
      String fileId = originalData[0];
      FilePartsDescriptorStore.removeFilePartsDescriptor(fileId);
      FileHelper.deleteFolder(IPackagingConstants.PACKAGING_PATH, fileId);
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "cleanup()",
        "Exception in building package ack",
        t);
      throw new FlowControlException("Exception in building package ack", t);
    }
  }

  private static void storeReceivedFile(
    int blockNo,
    byte[] payload,
    String filename,
    FilePartsDescriptor filePartsDesc,
    String fileId)
    throws ApplicationException
  {
    if (filePartsDesc.checkBlockReceivedStatus(blockNo))
    {
      String metaFilename = filePartsDesc.getBlockFilename(blockNo);
      if (metaFilename != null)
      {
        if (metaFilename.equals(filename))
          return;
      }
      else if (filename == null)
        return;
      throw new ApplicationException(
        "Filename is not the same: " + filename + ", " + metaFilename);
    }
    else
    {
      FileJoiner.store(
        IPackagingConstants.PACKAGING_PATH,
        filePartsDesc.getSubPath(),
        payload,
        fileId,
        blockNo);
      filePartsDesc.setBlockReceivedStatus(blockNo, filename);
    }
  }

  private static File[] getUnpackagedPayload(
    FilePartsDescriptor filePartsDescriptor,
    boolean isRelay,
    boolean isZip,
    FileDescriptor filedesc,
    String fileId,
    String subPath)
    throws FileAccessException, ApplicationException
  {
    synchronized (filePartsDescriptor)
    {
      if (filePartsDescriptor.isProcessing())
        return null;
      else
        filePartsDescriptor.setProcessing(true);
    }
    if (isRelay)
      return FileJoiner.getBlockFiles(
        IPackagingConstants.PACKAGING_PATH,
        subPath,
        fileId,
        isZip);
    else
      return FileJoiner.getOriginalFiles(
        IPackagingConstants.PACKAGING_PATH,
        subPath,
        filedesc.getFilename(),
        filedesc.getFileSize(),
        fileId,
        isZip);
  }

  private static String[] processUnpackagedDataContent(
    String[] packagedDataContent,
    String utcTimestamp)
  {
    int dataContentLength = packagedDataContent.length - 8;
    String[] dataContent = new String[dataContentLength + 1];
    System.arraycopy(packagedDataContent, 8, dataContent, 1, dataContentLength);
    dataContent[0] = utcTimestamp;
    return dataContent;
  }

  /**
   * Performs Zipping of files.
   *
   * @param files
   * @return @throws
   *         Exception
   */
  private static File getZippedFile(File[] files, String fileId)
    throws IOException, Exception
  {
    if (files == null || files.length == 0)
      return null;
    else
    {
      String tempDir = System.getProperty("java.io.tmpdir") + fileId;
      String tempFileName = tempDir + File.separator + fileId;

      createTempDirByFileId(tempDir);
      File zippedFile = performZipService(files, tempFileName);
      return zippedFile;
    }
  }

  private static void debugLog(String methodName, String message)
  {
    ChannelLogger.debugLog(
      "[FlowControlServiceHandler]",
      "[" + methodName + "]",
      "[" + message + "]");
  }

  private static void createTempDirByFileId(String tempDir)
  {
    new File(tempDir).mkdirs();
  }

  private static File performZipService(File[] files, String fileName)
    throws IOException, Exception
  {
    BufferedInputStream origin = null;
    FileOutputStream dest = new FileOutputStream(fileName);
    debugLog("creatZipFile", "ZipString File Name " + fileName);

    CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
    ZipOutputStream out =
      new ZipOutputStream(new BufferedOutputStream(checksum));
    out.setMethod(ZipOutputStream.DEFLATED);
    out.setLevel(9);
    byte data[] = new byte[BUFFER];

    for (int i = 0; i < files.length; i++)
    {
      debugLog("createZipFile()", "Adding: " + files[i]);
      FileInputStream fi = new FileInputStream(files[i]);
      origin = new BufferedInputStream(fi, BUFFER);
      ZipEntry entry = new ZipEntry(files[i].getName());
      entry.setSize(files[i].length());
      entry.setTime(files[i].lastModified());
      debugLog(
        "creatZipFile",
        "File Name of " + i + " is " + files[i].getName());
      out.putNextEntry(entry);
      int count;
      while ((count = origin.read(data, 0, BUFFER)) != -1)
      {
        out.write(data, 0, count);
      }
      origin.close();
    }

    out.close();
    debugLog("creatZipFile", "After Creating The Zip");
    return new File(fileName);
  }

  // To retrieve the file parts received from the data of the file part
  // acknowledgement.
  private static int[] getFilePartReceived(String[] data)
  {
    int noOfFilePartRec = Integer.parseInt(data[1]);
    int[] filePartRec = new int[noOfFilePartRec];
    for (int i = 0, j = 2; i < noOfFilePartRec; i++, j++)
    {
      filePartRec[i] = Integer.parseInt(data[j]);
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getFilePartReceived",
        "Files part received is " + filePartRec[i]);
    }
    return filePartRec;
  }

  // To retrieve original data from the data of the file part
  // acknowledgement.
  private static String[] getOriginalData(String[] data)
  {
    int noOfFilePartRec = Integer.parseInt(data[1]);
    String[] originalData = null;
    if ((noOfFilePartRec + 2) < data.length)
    {
      originalData = new String[data.length - noOfFilePartRec - 2];
      for (int i = 0, j = noOfFilePartRec + 2; j < data.length; i++, j++)
      {
        originalData[i] = data[j];
        ChannelLogger.debugLog(
          CLASS_NAME,
          "getOriginalData",
          "data[" + i + "] is " + originalData[i]);
      }
    }
    return originalData;
  }

  /**
   * To retrieve the contents from the given file.
   *
   * @param file
   *            the file to be read.
   * @return the contents of the file in byte[].
   *
   * @exception IOException
   *                if an Exception occurs reading the file.
   *
   * @since 2.0
   * @version 2.0
   */
  private static byte[] getBytesFromFile(File file) throws IOException
  {
    if (file == null || !file.exists())
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "getBytesFromFile()",
        "[Input File is Null - Return Empty ByteArray]");
      return new byte[] {
      };
    }
    else
    {
      InputStream is = new FileInputStream(file);

      long length = file.length();

      byte[] bytes = new byte[(int) length];
      int offset = 0;
      int numRead = 0;

      while (offset < bytes.length
        && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
      {
        offset += numRead;
      }

      if (offset < bytes.length)
      {
        throw new IOException(
          "Could not completely read file " + file.getName());
      }
      is.close();
      return bytes;
    } /*
       		   * else { ChannelLogger.infoLog( CLASS_NAME, "getBytesFromFile()",
       		   * "[Input File is Null]"); return null;
       		   */
  }

  private static File[] unZipFile(byte[] payLoad, String fileName)
  {
    Vector fileNames = new Vector();
    final String TEMP_PATH = System.getProperty("java.io.tmpdir");
    try
    {
      ChannelLogger.infoLog(CLASS_NAME, "unZipFile()", "In unZip Begin");
      String tempFolder = TEMP_PATH + fileName;
      //boolean createFolder = createTempFolder(tempFolder);
      createTempFolder(tempFolder);
      String actualPath = tempFolder + fileName;
      File unzipFile = getFileFromBytes(payLoad, actualPath);
      BufferedOutputStream dest = null;
      FileInputStream fis = new FileInputStream(unzipFile);
      CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
      ZipInputStream zis =
        new ZipInputStream(new BufferedInputStream(checksum));
      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null)
      {
        int count;
        byte data[] = new byte[BUFFER];
        fileNames.add(entry.getName());
        ChannelLogger.infoLog(
          CLASS_NAME,
          "unZipFile",
          "File Name is[" + entry.getName() + "]");
        FileOutputStream fos = new FileOutputStream(entry.getName());
        dest = new BufferedOutputStream(fos, BUFFER);
        while ((count = zis.read(data, 0, BUFFER)) != -1)
        {
          dest.write(data, 0, count);
        }
        dest.flush();
        dest.close();
      }
      zis.close();
      File[] mpayLoad = new File[fileNames.size()];
      for (int i = 0; i < fileNames.size(); i++)
      {
        String actName = (String) fileNames.get(i);
        ChannelLogger.infoLog(
          CLASS_NAME,
          "unZipFile()",
          "Actual Files Added Are[" + actName + "]");
        mpayLoad[i] = new File(actName);
      }
      ChannelLogger.infoLog(CLASS_NAME, "unZipFile", "End Of UNZIP Success ..");
      return mpayLoad;
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "unZip()",
        "Cannot UnPackagePayLoad",
        ex);
    }
    return null;
  }

  private static File getFileFromBytes(byte[] payLoad, String actualPath)
    throws IOException
  {
    if (actualPath != null && payLoad != null)
    {
      File f = new File(actualPath);
      FileOutputStream ous = new FileOutputStream(f);
      BufferedOutputStream bufOus = new BufferedOutputStream(ous);
      bufOus.write(payLoad);
      bufOus.flush();
      bufOus.close();
      ChannelLogger.infoLog(
        CLASS_NAME,
        "getFileFromBytes()",
        "End of getting File");
      return f;
    }
    else
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "getFileFromBytes()",
        "[PayLoad or Actual Path is Null]");
      return null;
    }
  }

  private static boolean createTempFolder(String folderName)
  {
    return new File(folderName).mkdir();
  }

  private static void logInfoCategory(String methodName, String message)
  {
    ChannelLogger.infoLog(CLASS_NAME, methodName, "[" + message + "]");
  }

  private static void logDebugCategory(String methodNane, String message)
  {
    ChannelLogger.debugLog(CLASS_NAME, methodNane, "[" + message + "]");
  }

}