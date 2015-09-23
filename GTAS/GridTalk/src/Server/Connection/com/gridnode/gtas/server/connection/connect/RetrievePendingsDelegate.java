/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RetrievePendingsDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 30 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.server.connection.helpers.Logger;
import java.io.File;

/**
 * This Delegate class handles the Retrieve Pending step of the Connection
 * process. It sends a Request Pending message to the connected GridMaster
 * which would send any pending Activation, document, etc messages for this
 * GridTalk.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class RetrievePendingsDelegate extends AbstractConnectionDelegate
{
  public RetrievePendingsDelegate(ConnectionContext ctx)
    throws Throwable
  {
    super(ctx);
  }

  public void execute() throws Throwable
  {
    Logger.debug("[RetrievePendingsDelegate.execute] Enter");

    // send retrieve pending event to gm channel
    registerAndSend(null, null, _ctx.getConnectedGmChannel());
  }

  public void receiveAck(String[] dataPayload, File[] filePayload) throws Throwable
  {
    // data: true
    Boolean status = Boolean.valueOf(dataPayload[0]);
    Logger.log("[RetrievePendingsDelegate.receiveAck] Retrieve Pendings ended with status: "+
      status);
  }

  /**
   * Invoked when feedback is received for the retrieve-pendings message.
   *
   * @param success Whether the retrieve-pendings message was sent successfully.
   * @param message A feedback message description.
   */
  public void receiveFeedback(boolean success, String message) throws Throwable
  {
    Logger.log("Received feedback for Retrieve Pendings: Success="+success
      + ", "+ message);
  }

  /**
   * Retrieve the event IDs that the Delegate can handle.
   */
  protected void getEventIDs() throws Throwable
  {
    _eventID = _helper.getProperties().getRetrievePendingEventId();
    _ackEventID = _helper.getProperties().getRetrievePendingAckEventId();
  }

}