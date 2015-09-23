/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveConfirmAliveDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 20 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.connect;

import java.io.File;

import com.gridnode.gtas.server.connection.helpers.Logger;

/**
 * This Delegate handles the receiving of Confirm Alive message from the GridMaster.
 * GridMaster will send this Confirm Alive request to the GridTalk if GridTalk
 * fails to send keep alive within specified interval. By acknowledging this
 * message, GridMaster would know that the GridTalk is still alive.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ReceiveConfirmAliveDelegate
  extends    AbstractConnectionDelegate
{
  /**
   * Use to receive Confirm Alive request from GridMaster
   *
   * @param ctx Current connection context.
   */
  public ReceiveConfirmAliveDelegate(ConnectionContext ctx)
    throws Throwable
  {
    super(ctx);
  }

  /**
   * Should be called only when an Confirm Alive message is received
   * for a GridTalk partner from the GridMaster.
   *
   * @param eventSubID Not used.
   * @param refTransID Reference TransID from GridMaster
   * @param dataPayload Data payload received
   * @param filePayload File payload received.
   */
  public void receive(
    String eventSubID, String refTransID, String[] dataPayload, File[] filePayload)
    throws Throwable
  {
    //check for msg defn id=confirmAlive?? dataPayload[4]
    //what can be done?? check keep alive timer?

    sendAck(refTransID);
  }

  /**
   * Send acknowledgement for Confirm Alive received.
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
      Logger.warn("[ReceiveConfirmAliveDelegate.sendAck] Error ", t);
    }
  }

  protected void getEventIDs() throws java.lang.Throwable
  {
    _eventID = _helper.getProperties().getConfirmAliveEventId();
    _ackEventID = _helper.getProperties().getConfirmAliveAckEventId();
  }

}