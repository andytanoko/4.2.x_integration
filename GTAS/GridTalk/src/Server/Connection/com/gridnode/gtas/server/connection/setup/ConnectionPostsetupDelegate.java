/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionPostsetupDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 24 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.setup;

import java.io.File;

import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.pdip.app.channel.model.CommInfo;

/**
 * This Delegate handles the Connection Post-setup step.
 * In this step, the GmPrime will be notified to end the Connection Setup process,
 * and all connections to the setup service router should be removed.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectionPostsetupDelegate
  extends    AbstractConnectionSetupDelegate
{
  public ConnectionPostsetupDelegate(ConnectionSetupContext ctx)
  {
    super(ctx);
  }

  public void execute() throws Throwable
  {
    Logger.debug("[ConnectionPostsetupDelegate.execute] Enter");

    if (_ctx.getPublicCommInfo() != null)
    {
      Logger.debug("[ConnectionPostsetupDelegate.execute] Sending post-setup message...");
      String eventID = DelegateHelper.getProperties().getPostSetupEventId();
      _ctx.setDelegate(eventID, this);
      _helper.send(eventID, null, null);

      // sleep
      if (!_resultsReturned)
        _helper.sleep(_ctx.getResponseTimeout());

      Logger.debug("[ConnectionPostsetupDelegate.execute] Disconnecting from servicing router...");
      // disconnect from gmprime connection
      DelegateHelper.disconnect((CommInfo)_ctx.getPublicCommInfo());
    }
  }

  public void receiveAck(String[] dataPayload, File[] filePayload) throws Throwable
  {
    throw new java.lang.UnsupportedOperationException("Method receiveAck() not yet implemented.");
  }

  /**
   * Invoked when feedback is received for the post-setup message.
   *
   * @param success Whether the post-setup message was sent successfully.
   * @param message A feedback message description.
   */
  public void receiveFeedback(boolean success, String message) throws Throwable
  {
    Logger.log("Received Feedback for Connection Post-setup: Success="+success+
      ", "+message);

    synchronized (_lock)
    {
      _resultsReturned = true;
      _success = success;
      _failureReason = message;

      _helper.wakeUp();
    }
  }

}