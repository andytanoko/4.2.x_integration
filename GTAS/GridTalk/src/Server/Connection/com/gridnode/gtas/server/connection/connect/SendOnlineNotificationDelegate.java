/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendOnlineNotificationDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 31 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.server.connection.helpers.CommInfoFormatter;
import com.gridnode.gtas.server.connection.helpers.Logger;

import java.io.File;

/**
 * This Delegate handles the sending of Online Notification message to an Online
 * active partner GridTalk. This Delegate is used by the InformPartnerOnlineDelegate.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class SendOnlineNotificationDelegate
  extends    AbstractOnlineNotificationDelegate
{
  public SendOnlineNotificationDelegate(
    ConnectionContext ctx, CommInfoFormatter partnerCI)
    throws Throwable
  {
    super(ctx);
    _partnerCI = partnerCI;
  }

  public void execute() throws Throwable
  {
    Logger.debug("[SendOnlineNotificationDelegate.execute] Enter");
    try
    {
      _partnerChannel = retrievePartnerChannel();

      if (_partnerChannel != null)
      {
        // send my comminfo string to partner channel
        String[] dataPayload = new String[]
        {
          _ctx.getMyCommInfoString(),
        };

        registerAndSend(dataPayload, null, _partnerChannel);
      }
    }
    catch (Throwable t)
    {
      Logger.err("[SendOnlineNotificationDelegate.execute] Error ", t);
    }
  }

  /**
   * Invoked when feedback is received for the online-notification message.
   *
   * @param success Whether the online-notification message was sent successfully.
   * @param message A feedback message description.
   */
  public void receiveFeedback(boolean success, String message) throws Throwable
  {
    Logger.log("Received feedback for Online notification: success="+success +
      ", "+message);
  }

  public void receiveAck(String[] dataPayload, File[] filePayload) throws Throwable
  {
    Boolean status = Boolean.valueOf(dataPayload[0]);
    _success = status.booleanValue();

    if (_success)
    {
      updatePartnerOnline();
    }
  }

}