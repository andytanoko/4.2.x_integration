/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionSetupAckReceiver.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 25 2002    Neo Sok Lay         Created
 * Dec 09 2002	  Jagadeesh			  Modified - IReceiveMessageHandler to 
 *									  include Hashtable additionalHeader.		
 * Jul 28 2003    Neo Sok Lay         GNDB00012297: Filter the messages not 
 *                                    for this GridTalk.
 * Oct 01 2003    Neo Sok Lay         Use ChannelReceiveHeader/ChannelSendHeader
 *                                    to index the header array.
 */
package com.gridnode.gtas.server.connection.setup;

import com.gridnode.gtas.server.connection.helpers.Logger;

import com.gridnode.pdip.app.channel.helpers.ChannelReceiveHeader;
import com.gridnode.pdip.app.channel.helpers.ChannelSendHeader;
import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;
import com.gridnode.pdip.app.channel.helpers.IReceiveFeedbackHandler;

import java.io.File;
import java.util.Hashtable;

/**
 * Receiver for acknowledgements during the Connection Setup process. The
 * receiver will lookup the Delegate from the ConnectionSetupContext to handle
 * the particular received acknowledgement.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since 2.0 I6
 */
public class ConnectionSetupAckReceiver
  implements IReceiveMessageHandler, IReceiveFeedbackHandler
{

  public ConnectionSetupAckReceiver()
  {
  }

  public void handlerMessage(
    String[] header, String[] dataReceived, File[] filesReceived,Hashtable additionalHeader)
  {
    try
    {
      Logger.log("[ConnectionSetupAckReceiver.handlerMessage] "+
        "Received acknowledgement for Event "+header[ChannelReceiveHeader.EVENT_ID_POS]);

      //**** Refer to base-packaging AbstractPackagingHandler.getDefaultUnpackagedHeader
      String intendedRecipient = header[ChannelReceiveHeader.RECIPIENT_NODEID_POS]; 
      String myId = ConnectionSetupContext.getInstance().getGridNodeID().toString();

      // Only process the message if intended recipient is this
      // GridTalk.      
      if (myId.equals(intendedRecipient))
      {
        IConnectionSetupDelegate delegate =
          ConnectionSetupContext.getInstance().getDelegate(header[ChannelReceiveHeader.EVENT_ID_POS]);
  
        delegate.receiveAck(dataReceived, filesReceived);
      }
      else
        Logger.log("[ConnectionSetupAckReceiver.handlerMessage] "+
          "Message is for "+intendedRecipient+", not intended for me. Don't peek.");
    }
    catch (Throwable ex)
    {
      Logger.err(
        "[ConnectionSetupAckReceiver.handlerMessage] Error processing received acknowledgement",
        ex);
      /**@todo raise alert/log in future */
    }
  }

  public void handlerFeedback(String[] header, boolean success, String message)
  {
    try
    {
      Logger.log("[ConnectionSetupAckReceiver.handlerFeedback] "+
        "Received feedback for Event "+header[ChannelSendHeader.EVENT_ID_POS]);

      IConnectionSetupDelegate delegate =
        ConnectionSetupContext.getInstance().getDelegate(header[ChannelSendHeader.EVENT_ID_POS]);

      delegate.receiveFeedback(success, message);
    }
    catch (Throwable ex)
    {
      Logger.err(
        "[ConnectionSetupAckReceiver.handlerFeedback] Error processing received feedback",
        ex);
      /**@todo raise alert/log in future */
    }
  }


}