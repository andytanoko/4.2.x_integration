/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendKeepAliveDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 30 2002    Neo Sok Lay         Created
 * Nov 29 2002    Neo Sok Lay         Do not decide here on what to do if
 *                                    keep alive fail. Let the invoker decide.
 *                                    Do not send KeepAlive if GridTalk is not
 *                                    online.
 * Apr 26 2004    Neo Sok Lay         GNDB00021907: Handle case when GM returns
 *                                    "false"  
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.server.connection.exceptions.ConnectionException;
import com.gridnode.gtas.server.connection.helpers.Logger;

import java.io.File;

/**
 * This Delegate class handles the sending of Keep Alive message to the
 * currently connected GridMaster. If Keep Alive fails, the GridTalk will be
 * set to offline and reconnection will be attempted (later on). This Delegate
 * is being called periodically.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3.3
 * @since 2.0 I6
 */
public class SendKeepAliveDelegate extends AbstractConnectionDelegate
{
  public SendKeepAliveDelegate(ConnectionContext ctx)
    throws Throwable
  {
    super(ctx);
  }

  public void execute() throws Throwable
  {
    Logger.debug("[SendKeepAliveDelegate.execute] Enter");

    if (!isMyGtOnline())
    {
      Logger.log(
        "[SendKeepAliveDelegate.execute] GridTalk is not online now, will skip sending keep alive.");

      return;
    }

    // send keep alive interval
    sendKeepAlive();
    // *** wait for result
    if (!_resultsReturned)
      _helper.sleep(DelegateHelper.getProperties().getKeepAliveTimeout());

    synchronized (_lock)
    {
      if (!_success)
      {
        //new DisconnectDelegate(_ctx).execute();
        if (_failureReason == null)
          _failureReason = "Fail to receive acknowledgement for Keep Alive!";
        throw new ConnectionException(_failureReason);
      }
    }
  }

  /**
   * Invoked when feedback is received for the keep-alive message.
   *
   * @param success Whether the keep-alive message was sent successfully.
   * @param message A feedback message description.
   */
  public void receiveFeedback(boolean success, String message) throws Throwable
  {
    if (!success)
    {
      synchronized (_lock)
      {
        _resultsReturned = true;
        _success = false;
        _failureReason = message;

        _helper.wakeUp();
      }
    }
  }

  public void receiveAck(String[] dataPayload, File[] filePayload) throws Throwable
  {
    // data: status
    synchronized (_lock)
    {
      _resultsReturned = true;

      try
      {
        // collate the results
        Boolean status = Boolean.valueOf(dataPayload[0]);
        _success = status.booleanValue();
        if (!_success)
          _failureReason = "GridMaster has expired the connection";
      }
      catch (Throwable t)
      {
        _success = false;
        _failureReason = t.getMessage();
        throw t;
      }
      finally
      {
        // wake up the sleeping thread
        _helper.wakeUp();
      }
    }

  }

  /**
   * Send the Keep Alive message
   */
  private void sendKeepAlive() throws Throwable
  {
    String[] dataPayload = new String[]
    {
      _ctx.getNetworkSetting().getKeepAliveInterval().toString(),
    };

    registerAndSend(dataPayload, null, _ctx.getConnectedGmChannel());
  }

  /**
   * Retrieve the event IDs that the Delegate can handle.
   */
  protected void getEventIDs() throws Throwable
  {
    _eventID = DelegateHelper.getProperties().getKeepAliveEventId();
    _ackEventID = DelegateHelper.getProperties().getKeepAliveAckEventId();
  }
}