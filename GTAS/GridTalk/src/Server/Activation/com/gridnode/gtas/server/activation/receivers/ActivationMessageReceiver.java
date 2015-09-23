/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationMessageReceiver.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 14 2002    Neo Sok Lay         Created
 * Oct 01 2003    Neo Sok Lay         Use ChannelReceiveHeader to index the
 *                                    header array.
 * Feb 05 2004    Neo Sok Lay         Move implementation of handlerFeedback()
 *                                    from ActivationAcknowledgementReceiver -
 *                                    for feedback of Activation messages sent.
 */
package com.gridnode.gtas.server.activation.receivers;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.activation.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.activation.helpers.Logger;

import com.gridnode.pdip.app.channel.helpers.ChannelReceiveHeader;
import com.gridnode.pdip.app.channel.helpers.ChannelSendHeader;
import com.gridnode.pdip.app.channel.helpers.IReceiveFeedbackHandler;
import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;

import java.io.File;
import java.util.Hashtable;

/**
 * This is a receiver for activation (Activate/Deactivate/Reply/Abort) messages.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public class ActivationMessageReceiver
  implements IReceiveMessageHandler, IReceiveFeedbackHandler
{

  public ActivationMessageReceiver()
  {
  }

  public void handlerMessage(
    String[] header, String[] dataReceived, File[] filesReceived,Hashtable additionalHeader)
  {
    try
    {
      Logger.log("[ActivationMessageReceiver.handlerMessage] "+
        "Received activation Record refTransID: "+header[ChannelReceiveHeader.TRANSACTION_ID_POS]+" from potential partner");

      ServiceLookupHelper.getActivationManager().receiveRequest(
        header[ChannelReceiveHeader.EVENT_ID_POS], header[ChannelReceiveHeader.TRANSACTION_ID_POS], dataReceived, filesReceived);
    }
    catch (Exception ex)
    {
      Logger.error(ILogErrorCodes.GT_ACTIVATION_MESSAGE_RECEIVER,
        "[ActivationMessageReceiver.handlerMessage] Error processing received activation message: "+ex.getMessage(),
        ex);
      /**@todo raise alert/log in future */
    }
  }

  public void handlerFeedback(String[] header, boolean success, String message)
  {
    try
    {
      Logger.log("[ActivationAcknowledgementReceiver.handlerFeedback] "+
        "Received feedback for sent acknowledgement for Record "+header[ChannelSendHeader.TRANSACTION_ID_POS]+", status: "+
        success + ", feedback message: "+message);

      Long recordUID = new Long(header[ChannelSendHeader.TRANSACTION_ID_POS]);

      ServiceLookupHelper.getActivationManager().receiveSubmissionFeedback(
        header[ChannelSendHeader.EVENT_ID_POS], recordUID, success, message);
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_ACTIVATION_MESSAGE_RECEIVER,
        "[ActivationMessageReceiver.handlerFeedback] Error processing received feedback: "+ex.getMessage(),
        ex);
      /**@todo raise alert/log in future */
    }
  }
}