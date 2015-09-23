/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationAcknowledgementReceiver.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 14 2002    Neo Sok Lay         Created
 * Dec 09 2002	  Jagadeesh			  Modified - IReceiveMessageHandler to 
 *									  include Hashtable additionalHeader.
 * Oct 01 2003    Neo Sok Lay         Use ChannelReceiveHeader/ChannelSendHeader
 *                                    to index the header.	
 * Feb 05 2004    Neo Sok Lay         GNDB00017037: Implement IReceiveFeedbackHandler
 *                                    for handling feedback for sent acknowledgement messages.	
 */
package com.gridnode.gtas.server.activation.receivers;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.activation.helpers.Logger;
import com.gridnode.gtas.server.activation.helpers.ServiceLookupHelper;

import com.gridnode.pdip.app.channel.helpers.ChannelReceiveHeader;
import com.gridnode.pdip.app.channel.helpers.ChannelSendHeader;
import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;

import java.io.File;
import java.util.Hashtable;
import com.gridnode.pdip.app.channel.helpers.IReceiveFeedbackHandler;

/**
 * This is a receiver for the acknowledgements intended for previously sent
 * activation (Activate/Deactivate/Reply/Abort) messages.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public class ActivationAcknowledgementReceiver
  implements IReceiveMessageHandler, IReceiveFeedbackHandler
{

  public ActivationAcknowledgementReceiver()
  {
  }

  public void handlerMessage(
    String[] header, String[] dataReceived, File[] filesReceived,Hashtable additionalHeader)
  {
    try
    {
      Logger.log("[ActivationAcknowledgementReceiver.handlerMessage] "+
        "Received acknowledgement for Record "+header[ChannelReceiveHeader.TRANSACTION_ID_POS]+", returned status: "+
        dataReceived[0]);

      Long recordUID = new Long(header[ChannelReceiveHeader.TRANSACTION_ID_POS]);

      ServiceLookupHelper.getActivationManager().receiveAcknowledgement(
        header[ChannelReceiveHeader.EVENT_ID_POS], recordUID, dataReceived, filesReceived);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_ACTIVATION_ACKNOWLEDGEMENT_RECEIVER,
        "[ActivationAcknowledgementReceiver.handlerMessage] Error processing received acknowledgement: "+ex.getMessage(),
        ex);
      /**@todo raise alert/log in future */
    }
  }

  public void handlerFeedback(String[] header, boolean success, String message)
  {
    try
    {
      Logger.log("[ActivationAcknowledgementReceiver.handlerFeedback] "+
      "Received feedback for sent message for RefTransID "+header[ChannelSendHeader.TRANSACTION_ID_POS]+", status: "+
        success + ", feedback message: "+message);

      String refTransID = header[ChannelSendHeader.TRANSACTION_ID_POS];

      ServiceLookupHelper.getActivationManager().receiveAcknowledgementFeedback(
        header[ChannelSendHeader.EVENT_ID_POS], refTransID, success, message);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_ACTIVATION_ACKNOWLEDGEMENT_RECEIVER,
        "[ActivationAcknowledgementReceiver.handlerFeedback] Error processing received acknowledgement feedback: "+ex.getMessage(),
        ex);
      /**@todo raise alert/log in future */
    }
  }
}