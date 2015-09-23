/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveOfflineNotificationDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 01 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.connect;

import java.io.File;

import com.gridnode.gtas.server.connection.helpers.Logger;
 
/**
 * This Delegate handles the receiving of Offline Notification message from an Offline
 * active partner GridTalk.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ReceiveOfflineNotificationDelegate
  extends    AbstractConnectionDelegate
{
  private String _expiredEventID;
  private String _partnerNodeID;

  /**
   * Use to receive Offline notifications from GridTalk partners.
   *
   * @param ctx Current connection context.
   */
  public ReceiveOfflineNotificationDelegate(ConnectionContext ctx)
    throws Throwable
  {
    super(ctx);
  }

  /**
   * Use to set a GridTalk partner offline. The offline notification was received
   * from other sources.
   *
   * @param ctx Current ConnectionContext.
   * @param partnerNodeID GridNode ID of the GridTalk partner.
   */
  public ReceiveOfflineNotificationDelegate(ConnectionContext ctx, String partnerNodeID)
    throws Throwable
  {
    this(ctx);
    _partnerNodeID = partnerNodeID;
  }

  /**
   * Should be called only when the delegate is instantiated using
   * <code>ReceiveOfflineNotificationDelegate(ConnectionContext ctx, String partnerNodeID)</code>.
   */
  public void execute() throws Throwable
  {
    if (_partnerNodeID == null)
      throw new Exception(
      "Illegal invocation to ReceiveOfflineNotificationDelegate.execute()");

    updatePartnerOffline(_partnerNodeID, false);
  }

  /**
   * Should be called only when an Offline Notification message is received
   * for a GridTalk partner from the GridMaster.
   *
   * @param eventSubID Sub event id to indicate whether the partner is offline
   * due to expired connection. If present and this matches the event id for
   * expired connection, the connection state of the partner will be reflected
   * accordingly.
   * @param refTransID Reference TransID from GridMaster
   * @param dataPayload Data payload received
   * @param filePayload File payload received.
   */
  public void receive(
    String eventSubID, String refTransID, String[] dataPayload, File[] filePayload)
    throws Throwable
  {
    updatePartnerOffline(dataPayload[0], _expiredEventID.equals(eventSubID));
    sendAck(refTransID);
  }

  /**
   * Send acknowledgement for Offline Notification received.
   *
   * @param refTransID Reference TransID of the received message.
   */
  private void sendAck(String refTransID)
  {
    try
    {
      String[] dataPayload = null;

      _helper.send(_ackEventID, refTransID, null, null, _ctx.getConnectedGmChannel());
    }
    catch (Throwable t)
    {
      Logger.err("[ReceiveOfflineNotificationDelegate.sendAck] Error ", t);
    }
  }

  protected void getEventIDs() throws java.lang.Throwable
  {
    _eventID = _helper.getProperties().getOfflineNotificationEventId();
    _ackEventID = _helper.getProperties().getOfflineNotificationAckEventId();
    _expiredEventID = _helper.getProperties().getExpiredNotificationEventId();
  }

}