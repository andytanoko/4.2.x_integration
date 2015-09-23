/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All rights reserved.
 *
 * File: SendFilePartAckMessageHandler.java
 *
 * **********************************************************
 * Date           Author            Changes
 * **********************************************************
 * Aug 20 2002    Jagadeesh         Repackaged - From base.packaging to app.channel.
 *
 */

package com.gridnode.pdip.app.channel.helpers;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.flowcontrol.FileDescriptor;
import com.gridnode.pdip.app.channel.flowcontrol.FilePartsDescriptor;
import com.gridnode.pdip.app.channel.flowcontrol.FilePartsDescriptorStore;
import com.gridnode.pdip.app.channel.flowcontrol.IFilePartEvent;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.ISecurityInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.base.transport.helpers.ITransportConstants;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class SendFilePartAckMessageHandler
{
  private static final String CLASS_NAME = "SendFilePartAckMessageHandler";

  public SendFilePartAckMessageHandler(
    ChannelInfo currentSender,
    String[] data,
    File[] file,
    String[] header,
    String ownRefId)
    throws Exception
  {
    String[] sendHeader = new String[6];
    sendHeader[0] = header[0]; //eventId
    sendHeader[1] = header[1]; //transaction Id
    //Those below is not that important.
    sendHeader[2] = header[4]; //eventSubId
    sendHeader[3] = header[6]; //fileId
    sendHeader[4] = ownRefId; //Own referenceId
    sendHeader[5] = currentSender.getReferenceId();
    //RefId of sender (can be relay)
    ChannelLogger.debugLog(
      CLASS_NAME,
      "constructor",
      "eventId = "
        + sendHeader[0]
        + ", transactionId = "
        + sendHeader[1]
        + ", eventSubId = "
        + sendHeader[2]
        + ", fileId = "
        + sendHeader[3]
        + ", own = "
        + sendHeader[4]
        + ", sender = "
        + sendHeader[5]);
    processSendMessage(currentSender, data, file, sendHeader);
  }
  /**
   *
   * @param fileId - FileId is passed as param bcoz fileId is retreived from dataContent[0].
   * @param packageData
   * @throws Exception
   */

  /*  public static void sendFilePartsAcknowledge(String fileId,Package packageData) throws Exception
    {
      try
      {
        Hashtable header = packageData.getHeader();
        //String fileId = (String)header.get(ITransportConstants.FILE_ID);
        String currentSenderNodeId = (String)header.get(ITransportConstants.PROCESS_ID);
        ChannelInfo receipentMasterChannel = getReceipentMasterChannel(currentSenderNodeId);
        String myNodeId = ChannelUtil.getInstance().getMyNodeId();
        String channelName = receipentMasterChannel.getName();
        Hashtable packagedHeader = preparePackagedHeader(fileId,header,myNodeId,currentSenderNodeId,
                                                        channelName);
        String[] dataContent = prepareDataContent(packageData.getDataContent(),fileId);
  
        String[] encryptedDataContent = SecurityServiceHandler.encryptData(
                                  receipentMasterChannel.getSecurityProfile(),
                                  dataContent
                                  );
        ICommInfo commInfo = getCommInfo(receipentMasterChannel.getTptCommInfo());
        ChannelLogger.infoLog(CLASS_NAME,"sendFilePartsAcknowledge()","ChannelInfo["+receipentMasterChannel.toString()+"]");
  
        ChannelUtil.getInstance().send(commInfo,packagedHeader,
                                       packageData.getUnPackFileContent(),
                                       encryptedDataContent);
  
       /** @todo To write a CommInfo creator at base.Transport and do a direct Send. */

  /*    }
      catch (Exception ex)
      {
        throw new Exception("Cannot Send SendFilePartAcknowledgment ");
      }
  
    }
  */

  public static void sendFilePartsAcknowledge(String fileId, Message message)
    throws Exception
  {
    try
    {
      Map header = message.getCommonHeaders();
      //String fileId = (String)header.get(ITransportConstants.FILE_ID);
      String currentSenderNodeId =
        (String) header.get(ICommonHeaders.MSG_PROCESS_ID);
      ChannelLogger.debugLog(
        CLASS_NAME,
        "sendFilePartsAcknowledge()",
        "[CurrentNodeId=]" + currentSenderNodeId);
      ChannelInfo receipentMasterChannel =
        getReceipentMasterChannel(currentSenderNodeId);
      String myNodeId = ChannelServiceDelegate.getMyNodeId();
      //String myNodeId = ChannelUtil.getInstance().getMyNodeId();
      String channelName = receipentMasterChannel.getName();
      Map packagedHeader =
        preparePackagedHeader(
          fileId,
          header,
          myNodeId,
          currentSenderNodeId,
          channelName);
      String[] dataContent = prepareDataContent(message.getData(), fileId);

      Message toEncyData = new Message();
      toEncyData.setCommonHeaders(packagedHeader);
      toEncyData.setData(dataContent);
      SecurityInfo secInfo = receipentMasterChannel.getSecurityProfile();
      secInfo.setSecuritylevel(ISecurityInfo.SECURITY_LEVEL_3);
      Message encyptMessage =
        SecurityServiceDelegate.encrypt(secInfo, toEncyData);
      String[] encryptedDataContent = encyptMessage.getData();

      //ICommInfo commInfo = getCommInfo(receipentMasterChannel.getTptCommInfo());
      ChannelLogger.infoLog(
        CLASS_NAME,
        "sendFilePartsAcknowledge()",
        "ChannelInfo[" + receipentMasterChannel.toString() + "]");
      ChannelLogger.infoLog(
        CLASS_NAME,
        "sendFilePartsAcknowledge()",
        "EncyptDataLength[" + encryptedDataContent.length + "]");

      /*Message filePartsAckMessage = new Message();
      filePartsAckMessage.setData(message.getData());
      filePartsAckMessage.setCommonHeaders(encyptMessage.getCommonHeaders());
      filePartsAckMessage.setPayLoad(message.getPayLoadData());
      filePartsAckMessage.setMessageHeaders(message.getMessageHeaders());
      */
      message.setCommonHeaders(packagedHeader);
      message.setData(encryptedDataContent);
      logHeader(packagedHeader);
      TransportServiceDelegate.send(
        receipentMasterChannel.getTptCommInfo(),
        message);

      //      ChannelUtil.getInstance().send(commInfo,packagedHeader,
      //                                     packageData.getUnPackFileContent(),
      //                                     encryptedDataContent);

      /** @todo To write a CommInfo creator at base.Transport and do a direct Send. */

    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "sendFilePartsAcknowledge()",
        "[Cannot Send SendFilePartAcknowledgment]",
        ex);
      throw new Exception("Cannot Send SendFilePartAcknowledgment ");
    }

  }

  private static Map preparePackagedHeader(
    String fileId,
    Map header,
    String myNodeId,
    String recpNodeId,
    String channelName)
    throws Exception
  {
    Map defaultHeader = new HashMap();
    String eventSubId = (String) header.get(ICommonHeaders.MSG_EVENT_SUB_ID);
    defaultHeader.put(
      ICommonHeaders.MSG_EVENT_ID,
      Integer.toString(IFilePartEvent._ACK_FILE_PARTS));
    defaultHeader.put(ICommonHeaders.MSG_TRANSACTION_ID, "3");
    defaultHeader.put(ICommonHeaders.MSG_EVENT_SUB_ID, eventSubId);
    defaultHeader.put(ICommonHeaders.PAYLOAD_ID, fileId);
    defaultHeader.put(ICommonHeaders.SENDER_BE_GNID, myNodeId);
    defaultHeader.put(ICommonHeaders.RECIPENT_BE_GNID, recpNodeId);
    defaultHeader.put(ICommonHeaders.COMM_CHANNEL, channelName);
    defaultHeader.put(
      ICommonHeaders.COMM_GNCI,
      header.get(ICommonHeaders.COMM_GNCI));
    defaultHeader.put(
      ICommonHeaders.MSG_PROCESS_ID,
      ChannelServiceDelegate.getMyNodeId());
    defaultHeader.put(
      ITransportConstants.EVENT_TYPE,
      header.get(ITransportConstants.EVENT_TYPE));
    //header.putAll(defaultHeader);
    return defaultHeader;
  }

  private static ChannelInfo getReceipentMasterChannel(String refId)
    throws Exception
  {
    ChannelInfo info = null;
    if (refId == null)
      ChannelServiceDelegate.getRelayChannelInfo();
    else
      info = ChannelServiceDelegate.getMasterChannelInfoByRefId(refId);
    if (info == null)
    {
      //info = ChannelServiceDelegate.getRelayChannelInfoByRefId(refId);
      //if (info == null)
      info = ChannelServiceDelegate.getRelayChannelInfo();
      /** @todo To Check with menghua, shld we send back Ack if No MasterChannel available and its not comming from relay ...*/
      return info;
    }
    else
      return info;
  }

  /* private static Hashtable preparePackagedHeader(String fileId,Hashtable header,String myNodeId,
     String recpNodeId,String channelName)
   {
     Hashtable defaultHeader = new Hashtable();
     String eventSubId = (String)header.get(ITransportConstants.EVENT_SUB_ID);
     defaultHeader.put(ITransportConstants.EVENT_ID,
                       Integer.toString(IFilePartEvent._ACK_FILE_PARTS));
     defaultHeader.put(ITransportConstants.TRANSACTION_ID,"3");
     defaultHeader.put(ITransportConstants.EVENT_SUB_ID,eventSubId);
     defaultHeader.put(ITransportConstants.FILE_ID,fileId);
     defaultHeader.put(ITransportConstants.SENDER_NODE_ID,myNodeId);
     defaultHeader.put(ITransportConstants.RECEIPENT_NODE_ID,recpNodeId);
     defaultHeader.put(IPackagingConstants.CHANNEL_NAME,channelName);
     return defaultHeader;
  
   /*
     String eventId = Integer.toString(IFilePartEvent._ACK_FILE_PARTS);
     String transactionId = "3";
  
     String fileIdName = fileId;
     String senderNodeId = myNodeId; //Sender's Master Channel.
     String receipentNodeId = recpNodeId;
     String processId = myNodeId; // ProcessId is set to be consistent.
     String channelSent = channelName;
     String [] defaultHeader = {eventId,transactionId,eventId,fileIdName,senderNodeId,
                                  receipentNodeId,processId,channelSent};
   */

  //  }

  private static String[] prepareDataContent(
    String[] dataContent,
    String fileId)
    throws FileAccessException, ApplicationException
  {
    //String subPath = fileId + File.separator;
    FileDescriptor fileDesc = FileDescriptor.retrieve(fileId);
    FilePartsDescriptor filePartsDesc =
      FilePartsDescriptorStore.retrieveFilePartsDescriptor(fileId);
    String[] processedDataContent =
      getDataContentFromFilePartsDescriptor(
        fileId,
        dataContent,
        fileDesc,
        filePartsDesc);
    return processedDataContent;
  }

  private static String[] getDataContentFromFilePartsDescriptor(
    String fileId,
    String[] originalData,
    FileDescriptor fileDesc,
    FilePartsDescriptor filePartsDesc)
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

  private void processSendMessage(
    ChannelInfo info,
    String[] dataToSend,
    File[] file,
    String[] header)
    throws ServiceLookupException, Exception
  {
    getChannelManager().send(info, dataToSend, file, header);
    //ChannelServiceHandler handler = new ChannelServiceHandler();
    //handler.send(info, dataToSend, file, header);

  }
  /*
  private static ICommInfo getCommInfo(CommInfo commInfo)
    throws InvalidCommInfoException, ApplicationException
  {
    try
    {
      ICommInfo tptCommInfo =
        CommInfoFactory.getTransportCommInfo(commInfo.getProtocolType());
      tptCommInfo.setTptImplVersion(commInfo.getTptImplVersion());
      tptCommInfo.setURL(commInfo.getURL());
      return tptCommInfo;
    }
    catch (java.net.MalformedURLException ex)
    {
      throw new ApplicationException(ex.getMessage(), ex);
    }

  }
  */
  private IChannelManagerObj getChannelManager() throws ServiceLookupException
  {
    return (IChannelManagerObj) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        IChannelManagerHome.class.getName(),
        IChannelManagerHome.class,
        new Object[0]);
  }

  private static void logHeader(Map header)
  {
    if (header != null)
    {
      String[] headerNames = (String[]) header.keySet().toArray(new String[] {
      });
      Object[] headerValues = header.entrySet().toArray(new Object[] {
      });
      for (int i = 0; i < headerNames.length; i++)
        infoLog(
          "logHeader()",
          "HeaderName=["
            + headerNames[i]
            + "]HeaderValue["
            + headerValues[i]
            + "]");
    }
  }

  private static void infoLog(String methodName, String message)
  {
    ChannelLogger.infoLog(
      CLASS_NAME,
      "[" + methodName + "]",
      "[" + message + "]");
  }

}