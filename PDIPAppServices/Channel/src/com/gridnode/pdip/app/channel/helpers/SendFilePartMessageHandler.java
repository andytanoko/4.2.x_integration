/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendFilePartMessageHandler.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Apr 03 2002    Goh Kan Mun                 Created
 * Aug 10 2003    Jagadeesh                   Modified: To Send message via ChannelSelector.
 */
package com.gridnode.pdip.app.channel.helpers;

/**
 * This class is used to invoke the process to send the next 10 packets of data.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

import com.gridnode.pdip.app.channel.IMessageContext;
import com.gridnode.pdip.app.channel.IMessageSender;
import com.gridnode.pdip.app.channel.MessageContext;
import com.gridnode.pdip.app.channel.exceptions.ChannelException;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;
import com.gridnode.pdip.framework.messaging.Message;

import java.io.File;
import java.util.Map;

public class SendFilePartMessageHandler
{
  private static final String CLASS_NAME = "SendFilePartMessageHandler";

  public SendFilePartMessageHandler(
    ChannelInfo finalRecipient,
    String[] data,
    File[] file,
    String[] header,
    String originalSenderNodeId)
    throws Exception
  {
    String[] sendHeader = new String[6];
    sendHeader[0] = header[0]; //eventId
    sendHeader[1] = header[1]; //transaction Id
    //Those below is not that important.
    sendHeader[2] = header[4]; //eventSubId
    sendHeader[3] = header[6]; //fileId
    sendHeader[4] = originalSenderNodeId; //Own referenceId
    sendHeader[5] = finalRecipient.getReferenceId(); //RefId of final recipient
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
        + ", finalRecipient = "
        + sendHeader[5]);
    processSendMessage(finalRecipient, data, file, sendHeader);
  }

  /*	public static void sendNextBlock(
      String fileId,
      String originalEventId,
      String transId,
      String channelName,
      Package packageData)
      throws Exception
    {
      ChannelInfo channelToSend =
        ChannelUtil.getInstance().getChannelInfoByName(channelName);
      if (channelToSend == null)
        throw new ChannelException(
          "[SendFilePartMessageHandler]"
            + "[sendNextBlock()][Check for Channel]"
            + channelName);
      // The receipentNodeId is take from Header's SenderNodeId, since GM(can also trigger SEND_FILE_PART)
      String receipentNodeId =
        (String) packageData.getHeader().get(ITransportConstants.SENDER_NODE_ID);
      ChannelLogger.debugLog(
        CLASS_NAME,
        "sendNextBlock()",
        "[ReceipentNodID=][" + receipentNodeId + "]");
      if (receipentNodeId == null)
        receipentNodeId = channelToSend.getReferenceId();
      //This is done as a backup,
      String[] header =
        perpareDefaultHeader(
          fileId,
          originalEventId,
          transId,
          receipentNodeId,
          channelName,
          packageData.getHeader());
      DispatchMessage messageToSend = new DispatchMessage();
      messageToSend.setChannleInfo(channelToSend);
      messageToSend.setData(null);
      messageToSend.setFile(null);
      messageToSend.setHeader(header);
      MessageContext msgcontext = new MessageContext();
      msgcontext.setAttribute(IMessageContext.OUTBOUND_MESSAGE, messageToSend);
      IMessageSender handlerToSend = null;
      //ChannelSelector.getChannelHandlerToSend(msgcontext);
      //handlerToSend.sendMessage();
    }
  */

  public static void sendNextBlock(
    String fileId,
    String originalEventId,
    String transId,
    String channelName,
    Message message)
    throws Exception
  {
    ChannelInfo channelToSend =
      ChannelServiceDelegate.getChannelInfoByName(channelName);
    if (channelToSend == null)
      throw new ChannelException(
        "[SendFilePartMessageHandler]"
          + "[sendNextBlock()][Check for Channel]"
          + channelName);
    // The receipentNodeId is take from Header's SenderNodeId, since GM(can also trigger SEND_FILE_PART)
    /** @todo Check for Sender Node Id Header. */
    String receipentNodeId =
      (String) message.getCommonHeaders().get(ICommonHeaders.SENDER_BE_GNID);
    ChannelLogger.debugLog(
      CLASS_NAME,
      "sendNextBlock()",
      "[ReceipentNodID=][" + receipentNodeId + "]");
    if (receipentNodeId == null)
      receipentNodeId = channelToSend.getReferenceId();
    //This is done as a backup,
    String[] header =
      perpareDefaultHeader(
        fileId,
        originalEventId,
        transId,
        receipentNodeId,
        channelName,
        message.getCommonHeaders());

    Message messageToSend = new Message();
    messageToSend.setData(null);
    messageToSend.setPayLoad(new File[] {
    });
    messageToSend.setCommonHeaders(ChannelSendHeader.getCommonHeaders(header));

    MessageContext msgcontext = new MessageContext();
    msgcontext.setAttribute(IMessageContext.CHANNEL_INFO, channelToSend);
    msgcontext.setAttribute(IMessageContext.OUTBOUND_MESSAGE, messageToSend);
    IMessageSender handlerToSend =
      MessageHandlerFactory.getMessageSender(msgcontext);
    handlerToSend.send();

    //		messageToSend.setChannleInfo(channelToSend);
    //		messageToSend.setData(null);
    //		messageToSend.setFile(null);
    //		messageToSend.setHeader(header);

    //IMessageSender handlerToSend = ChannelSelector.getChannelHandlerToSend(msgcontext);
    //handlerToSend.sendMessage();
  }

  /*  private static String[] perpareDefaultHeader(
      String fileId,
      String originalEventId,
      String transId,
      String receipentNodeId,
      String channelNane,
      Hashtable header)
      throws Exception
    {
      String[] headerToSend = new String[6];
      headerToSend[0] = originalEventId;
      headerToSend[1] = transId;
      headerToSend[2] = (String) header.get(ITransportConstants.EVENT_SUB_ID);
      headerToSend[3] = fileId;
      headerToSend[4] = ChannelUtil.getInstance().getMyNodeId();
      headerToSend[5] = receipentNodeId;
      return headerToSend;
    }
  */

  private static String[] perpareDefaultHeader(
    String fileId,
    String originalEventId,
    String transId,
    String receipentNodeId,
    String channelName,
    Map header)
    throws Exception
  {
    ChannelSendHeader defaultHeader =
      new ChannelSendHeader(
        originalEventId,
        transId,
        (String) header.get(ICommonHeaders.MSG_EVENT_ID),
        fileId);
    defaultHeader.setGridnodeHeaderInfo(
      ChannelServiceDelegate.getMyNodeId(),
      receipentNodeId,
      null);
    return defaultHeader.getHeaderArray();
    /*String[] headerToSend = new String[6];
    headerToSend[0] = originalEventId;
    headerToSend[1] = transId;
    headerToSend[2] = (String)header.get(ICommonHeaders.MSG_EVENT_ID);
    headerToSend[3] = fileId;
    headerToSend[4] = ChannelServiceDelegate.getMyNodeId();
    headerToSend[5] = receipentNodeId;
    return headerToSend;
    */
  }

  private void processSendMessage(
    ChannelInfo info,
    String[] dataToSend,
    File[] file,
    String[] header)
    throws Exception
  {
    // ChannelServiceHandler handler = new ChannelServiceHandler();
    // handler.send(info, dataToSend, file, header);
  }

}