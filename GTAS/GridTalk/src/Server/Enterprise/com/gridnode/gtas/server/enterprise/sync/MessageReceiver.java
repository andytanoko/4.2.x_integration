/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncResourceController.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 06 2002    Neo Sok Lay         Created
 * Oct 28 2002    Neo Sok Lay         Modified handlerMessage() params in
 *                                    IReceiveMessageHandler.
 * Dec 09 2002	  Jagadeesh			  Modified - IReceiveMessageHandler to 
 *									  include Hashtable additionalHeader.	
 * Oct 01 2003    Neo Sok Lay         Use ChannelReceiveHeader to index
 *                                    the header array.	
 * Jan 05 2004    Neo Sok Lay         Set transId and Datapayload into SyncMessage.
 */
package com.gridnode.gtas.server.enterprise.sync;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.enterprise.helpers.Logger;
import com.gridnode.pdip.app.channel.helpers.ChannelReceiveHeader;
import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;

import java.io.File;
import java.util.Hashtable;

/**
 * Handles the receiving of message from the Channel module.
 * @author Neo Sok Lay
 * @version GT 2.3 I1
 */
public class MessageReceiver implements IReceiveMessageHandler
{

  public MessageReceiver()
  {
  }

  public void handlerMessage(String[] header, String[] dataReceived, File[] filesReceived,
																	 Hashtable additionalHeader)
  {
    try
    {
  Logger.debug("[MessageReceiver.handlerMessage] EventId = "+header[ChannelReceiveHeader.EVENT_ID_POS]);
  Logger.debug("[MessageReceiver.handlerMessage] TransId = "+header[ChannelReceiveHeader.TRANSACTION_ID_POS]);    
  Logger.debug("[MessageReceiver.handlerMessage] SenderNodeId = "+header[ChannelReceiveHeader.SENDER_NODEID_POS]);
  Logger.debug("[MessageReceiver.handlerMessage] DataPayload = "+dataReceived);
  Logger.debug("[MessageReceiver.handlerMessage] FilePayload = "+filesReceived);
  Logger.debug("[MessageReceiver.handlerMessage] DataPayloadLength = "+(dataReceived==null?0:dataReceived.length));
  Logger.debug("[MessageReceiver.handlerMessage] FilePayloadLength = "+(filesReceived==null?0:filesReceived.length));
      SyncMessage msg = new SyncMessage(header[ChannelReceiveHeader.EVENT_ID_POS]);
      msg.setReplyTo(header[ChannelReceiveHeader.SENDER_NODEID_POS]); //sender, may be GT or GM
      
      //050104NSL: Just set the data payload and transId
      //msg.setReceivedData(dataReceived);
      msg.setTransId(header[ChannelReceiveHeader.TRANSACTION_ID_POS]);
      msg.setDataPayload(dataReceived);
      msg.setFilePayload(filesReceived);

      SyncResourceController.getInstance().receive(msg);
    }
    catch (Throwable t)
    {
      Logger.error(ILogErrorCodes.GT_SYNC_MESSAGE_RECEIVER,
                   "[MessageReceiver.handlerMessage] Error in handling received message: "+header[ChannelReceiveHeader.EVENT_ID_POS], t);
    }
  }
}