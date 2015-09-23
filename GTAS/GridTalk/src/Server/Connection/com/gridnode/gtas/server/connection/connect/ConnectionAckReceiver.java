/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionAckReceiver.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 29 2002    Neo Sok Lay         Created
 * Dec 09 2002	  Jagadeesh			  Modified - IReceiveMessageHandler to 
 *									  include Hashtable additionalHeader.	
 * Oct 01 2003    Neo Sok Lay         Use ChannelSendHeader/ChannelReceiveHeader
 *                                    to index the header array.	
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.connection.helpers.Logger;

import com.gridnode.pdip.app.channel.helpers.ChannelReceiveHeader;
import com.gridnode.pdip.app.channel.helpers.ChannelSendHeader;
import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;
import com.gridnode.pdip.app.channel.helpers.IReceiveFeedbackHandler;

import java.io.File;
import java.util.Hashtable;

/**
 * Receiver for acknowledgements during the Connection process. The
 * receiver will lookup the Delegate from the ConnectionContext to handle
 * the particular received acknowledgement.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since 2.0 I6
 */
public class ConnectionAckReceiver
  implements IReceiveMessageHandler,
             IReceiveFeedbackHandler
{

  public ConnectionAckReceiver()
  {
  }

  public void handlerMessage(
    String[] header, String[] dataReceived, File[] filesReceived,Hashtable additionalHeader)
  {
    try
    {
      Logger.log("[ConnectionAckReceiver.handlerMessage] "+
        "Received acknowledgement for Event "+header[ChannelReceiveHeader.EVENT_ID_POS]);

      IConnectionSenderDelegate delegate =
        ConnectionContext.getInstance().getDelegate(header[ChannelReceiveHeader.EVENT_ID_POS], 
        header[ChannelReceiveHeader.TRANSACTION_ID_POS]);

      delegate.receiveAck(dataReceived, filesReceived);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_CONNECTION_ACK_RECEIVER,
        "[ConnectionAckReceiver.handlerMessage] Error processing received acknowledgement",
        ex);
      /**@todo raise alert/log in future */
    }
  }

  public void handlerFeedback(String[] header, boolean success, String message)
  {
    try
    {
      Logger.log("[ConnectionAckReceiver.handlerFeedback] "+
        "Received feedback for Event "+header[ChannelSendHeader.EVENT_ID_POS]);

      IConnectionSenderDelegate delegate =
        ConnectionContext.getInstance().getDelegate(header[ChannelSendHeader.EVENT_ID_POS], 
        header[ChannelSendHeader.TRANSACTION_ID_POS]);

      delegate.receiveFeedback(success, message);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_CONNECTION_ACK_RECEIVER,
        "[ConnectionAckReceiver.handlerFeedback] Error processing received feedback",
        ex);

    }
  }

}