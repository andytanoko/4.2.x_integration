/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CleanupFilePartMessageHandler.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Mar 21 2003    Goh Kan Mun                 Created - for clean up after file splitting.
 * Aug 10 2003    Jagadeesh                   Modified - To enable p2p file split channel.
 * Dec 17 2003    Jagadeeh                    Modified - To use new implementation of CommInfoFactory
 * Dec 17 2003    Jagadeesh                   Modified - To remove dependency on Package Data Structure.
 */

package com.gridnode.pdip.app.channel.helpers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.gridnode.pdip.app.channel.flowcontrol.IFilePartEvent;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.ISecurityInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;

public class CleanupFilePartMessageHandler
{
  private static final String CLASS_NAME = "CleanupFilePartMessageHandler";

  public CleanupFilePartMessageHandler(
    ChannelInfo currentSender,
    String fileId,
    String ownRefId)
    throws Exception
  {
    String[] sendHeader = new String[6];
    sendHeader[0] = Integer.toString(IFilePartEvent._SEND_FILE_PARTS_FINISHED);
    //eventId
    sendHeader[1] = "3"; //transaction Id
    //Those below is not that important.
    sendHeader[2] = "0"; //eventSubId
    sendHeader[3] = null; //fileId
    sendHeader[4] = ownRefId; //Own referenceId
    sendHeader[5] = currentSender.getReferenceId();
    //RefId of sender (can be relay)
    String[] data = { fileId };
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
        + sendHeader[5]
        + ", data[0] = "
        + data[0]);

    processSendMessage(currentSender, data, null, sendHeader);
  }

  /** @todo To Verify  which channel to use when we send Cleanup fileparts.
   *  Assumption is to use the same channel, that was used for send initiation.
   *  */

  /*  public static void sendCleanupFileParts(Package packageData,String channelName)
      throws Exception
    {
      Hashtable header = packageData.getHeader();
      String[] initdataContent = packageData.getDataContent();
      String fileId = initdataContent[0];
      FlowControlServiceHandler.cleanup(packageData); //Cleanup at Sender side.
  
      //String currentSenderNodeId = (String)header.get(ITransportConstants.PROCESS_ID);
      ChannelInfo recipientChannel = ChannelUtil.getInstance().getChannelInfoByName(channelName);
      String recipientNodeId = recipientChannel.getReferenceId();
      //ChannelInfo receipentMasterChannel = getReceipentMasterChannel(currentSenderNodeId);
      String myNodeId = ChannelUtil.getInstance().getMyNodeId();
      //String channelName = receipentMasterChannel.getName();
  
      Hashtable packagedHeader = preparePackagedHeader(header,
                                  myNodeId,recipientNodeId,channelName);
      String[] dataContent = {fileId};
  
      String[] encryptedDataContent = SecurityServiceHandler.encryptData(
                                recipientChannel.getSecurityProfile(),
                                dataContent
                                );
      ICommInfo commInfo = getCommInfo(recipientChannel.getTptCommInfo());
      ChannelLogger.infoLog(CLASS_NAME,"sendCleanupFileParts()",
              "ChannelInfo["+recipientChannel.toString()+"]");
  
      ChannelUtil.getInstance().send(commInfo,packagedHeader,
                                     packageData.getUnPackFileContent(),
                                     encryptedDataContent);
    }
  
  */

  public static void sendCleanupFileParts(Message message, String channelName)
    throws Exception
  {
    Map header = message.getCommonHeaders();
    String[] initdataContent = message.getData();
    String fileId = initdataContent[0];
    FlowControlServiceHandler.cleanup(message); //Cleanup at Sender side.

    //String currentSenderNodeId = (String)header.get(ITransportConstants.PROCESS_ID);
    ChannelInfo recipientChannel =
      ChannelServiceDelegate.getChannelInfoByName(channelName);
    String recipientNodeId = recipientChannel.getReferenceId();
    //ChannelInfo receipentMasterChannel = getReceipentMasterChannel(currentSenderNodeId);
    String myNodeId = ChannelServiceDelegate.getMyNodeId();
    //String channelName = receipentMasterChannel.getName();

    ChannelLogger.debugLog(
      CLASS_NAME,
      "[sendCleanupFileParts()]",
      "[ChannelName=]" + recipientChannel.getName());
    Map messageHeader =
      prepareMessageHeader(
        header,
        myNodeId,
        recipientNodeId,
        recipientChannel.getName());
    String[] dataContent = { fileId };

    SecurityInfo secInfo = recipientChannel.getSecurityProfile();
    secInfo.setSecuritylevel(ISecurityInfo.SECURITY_LEVEL_3);
    Message messageToEncypt = new Message();
    messageToEncypt.setData(dataContent);
    Message encyMessage =
      SecurityServiceDelegate.encrypt(secInfo, messageToEncypt);
    String[] encryptedDataContent = encyMessage.getData();

    //ICommInfo commInfo = getCommInfo(recipientChannel.getTptCommInfo());
    ChannelLogger.infoLog(
      CLASS_NAME,
      "sendCleanupFileParts()",
      "ChannelInfo[" + recipientChannel.toString() + "]");
    Message processedMessage = new Message();
    processedMessage.setCommonHeaders(messageHeader);
    ChannelLogger.debugLog(
      CLASS_NAME,
      "[sendCleanupFileParts()]",
      "[CommonHeaders]" + messageHeader);
    processedMessage.setData(encryptedDataContent);
    processedMessage.setPayLoad(message.getPayLoadData());
    processedMessage.setMessageHeaders(new HashMap());

    TransportServiceDelegate.send(
      recipientChannel.getTptCommInfo(),
      processedMessage);

    /*    ChannelUtil.getInstance().send(commInfo,packagedHeader,
                                       packageData.getUnPackFileContent(),
                                       encryptedDataContent);
    */
  }

  /*  private static ChannelInfo getReceipentMasterChannel(String refId)
      throws Exception
    {
      return ChannelUtil.getInstance().getChannelInfoByRefId(refId);
    }
  */
  /*
  private static Hashtable preparePackagedHeader(
    Hashtable header,
    String myNodeId,
    String recpNodeId,
    String channelName)
  {
    Hashtable defaultHeader = new Hashtable();
    String eventId = Integer.toString(IFilePartEvent._SEND_FILE_PARTS_FINISHED);
    //eventId
    String transactionId = "3";
    String eventSubId = "0";
    String fileIdName = null;
    defaultHeader.put(ITransportConstants.EVENT_ID, eventId);
    defaultHeader.put(ITransportConstants.TRANSACTION_ID, transactionId);
    defaultHeader.put(ITransportConstants.EVENT_SUB_ID, eventSubId);
    defaultHeader.put(ITransportConstants.FILE_ID, ""); //Set no fileID
    defaultHeader.put(ITransportConstants.SENDER_NODE_ID, myNodeId);
    //Sender's Master Channel.
    defaultHeader.put(ITransportConstants.RECEIPENT_NODE_ID, recpNodeId);
    defaultHeader.put(ITransportConstants.PROCESS_ID, myNodeId);
    // ProcessId is set to be consistent.
    defaultHeader.put(IPackagingConstants.CHANNEL_NAME, channelName);
    return defaultHeader;
  }*/

  private static Map prepareMessageHeader(
    Map header,
    String myNodeId,
    String recpNodeId,
    String channelName)
  {
    Map defaultHeader = new HashMap();
    String eventId = Integer.toString(IFilePartEvent._SEND_FILE_PARTS_FINISHED);
    //eventId
    String transactionId = "3";
    String eventSubId = "0";
    //String fileIdName = null;
    defaultHeader.put(ICommonHeaders.MSG_EVENT_ID, eventId);
    defaultHeader.put(ICommonHeaders.MSG_TRANSACTION_ID, transactionId);
    defaultHeader.put(ICommonHeaders.MSG_EVENT_SUB_ID, eventSubId);
    defaultHeader.put(ICommonHeaders.PAYLOAD_ID, ""); //Set no fileID
    /** @todo To check the header for SENDER_NODE_ID */
    defaultHeader.put(ICommonHeaders.SENDER_BE_GNID, myNodeId);
    //Sender's Master Channel.
    defaultHeader.put(ICommonHeaders.RECIPENT_BE_GNID, recpNodeId);
    defaultHeader.put(ICommonHeaders.MSG_PROCESS_ID, myNodeId);
    // ProcessId is set to be consistent.
    defaultHeader.put(ICommonHeaders.COMM_CHANNEL, channelName);
    return defaultHeader;
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
    }*/

    /*
    ICommInfo commInfoimpl = CommInfoFactory.getCommInfo(commInfo.getProtocolType(),
                                                          commInfo.getURL(),
                                                          commInfo.getTptImplVersion());
    return commInfoimpl;
    */
  //}

  private void processSendMessage(
    ChannelInfo info,
    String[] dataToSend,
    File[] file,
    String[] header)
    throws Exception
  {
    //ChannelServiceHandler handler = new ChannelServiceHandler();
    // handler.send(info, dataToSend, file, header);
  }

}