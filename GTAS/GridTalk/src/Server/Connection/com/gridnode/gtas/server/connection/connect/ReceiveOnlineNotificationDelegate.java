/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveOnlineNotificationDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 31 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.helpers.CommInfoFormatter;

import java.io.File;

/**
 * This Delegate handles the receiving of Online Notification message from an Online
 * active partner GridTalk.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ReceiveOnlineNotificationDelegate
  extends    AbstractOnlineNotificationDelegate
{
  public ReceiveOnlineNotificationDelegate(ConnectionContext ctx)
    throws Throwable
  {
    super(ctx);
  }

  /**
   * Should be called only when an Online Notification message is received
   * from a GridTalk partner.
   *
   * @param eventSubID Not used.
   * @param refTransID Reference TransID from the partner
   * @param dataPayload Data payload received
   * @param filePayload File payload received.
   */
  public void receive(
    String eventSubID, String refTransID, String[] dataPayload, File[] filePayload)
    throws Throwable
  {
    _partnerCI = CommInfoFormatter.toCommInfo(dataPayload[0]);

    try
    {
      _partnerChannel = retrievePartnerChannel();

      updatePartnerOnline();

      _success = true;
    }
    catch (Throwable t)
    {
      Logger.err("[ReceiveOnlineNotificationDelegate.receive] Error ", t);
    }

    sendAck(refTransID);
  }

  /**
   * Send acknowledgement for Online Notification message received.
   *
   * @param refTransID Reference Trans ID of received message.
   */
  private void sendAck(String refTransID)
  {
    try
    {
      // send my comminfo string to partner channel
      String[] dataPayload = new String[]
      {
        String.valueOf(_success),
      };

      _helper.send(_ackEventID, refTransID, dataPayload, null, _partnerChannel);
    }
    catch (Throwable t)
    {
      Logger.err("[ReceiveOnlineNotificationDelegate.sendAck] Error ", t);
    }
  }

}