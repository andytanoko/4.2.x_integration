/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReconnectDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 29 2002    Neo Sok Lay         Created
 * Apr 28 2003    Neo Sok Lay         Log start/end of reconnect activity.
 * Apr 28 2004    Neo Sok Lay         Do not proceed with Reconnect if other
 *                                    activity in progress, or Disconnect has
 *                                    just been performed.
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.server.connection.exceptions.ConnectionException;
import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.model.NetworkSetting;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;

import java.util.Collection;

/**
 * This Delegate class handles the Reconnection to GridMaster on connection lost.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3.3
 * @since 2.0 I7
 */
public class ReconnectDelegate extends ConnectDelegate
{
  private ConnectionContext.ConnectionToken _myToken;
  private boolean _abort;
  private static Object _reconnectState;

  public ReconnectDelegate(ConnectionContext ctx)
    throws Throwable
  {
    super(ctx);
  }

  public void execute() throws Throwable
  {
    Logger.debug("[ReconnectDelegate.execute] Enter");

    // get numReconnect tries (may be infinity)
    int numRetries = DelegateHelper.getProperties().getReconnectRetries();
    if (numRetries == 0) //to throw reconnect exception
    {
      Logger.log("[ReconnectDelegate.execute] Reconnection not configured. No reconnect attempt will be made.");
      return;
    }

    try
    {
      //do not proceed if activity in progress or already disconnected or reconnection process started
      if (!hasReconnectionStarted() && !isCancelReconnect())
      {
        try
        {
          setReconnectionState(this); // set that reconnection started
          performReconnect(numRetries);
        }
        finally
        {
          clearReconnectionState(); //clear reconnection state
        }
      }
      else
      {
        Logger.debug("[ReconnectDelegate.execute] Not proceeding with Reconnect.");
      }
      
      if (!_abort && !_success) //only throw exception if the process is not cancelled and not successfully
      {
        throw new ConnectionException("Unable to reconnect to GridMaster");
      }
    }
    finally
    {
      Logger.debug("[ReconnectDelegate.execute] Exit");
    }
  }


  /**
   * Perform the Reconnect procedure
   *
   * @parma numRetries Number of retries if unsuccessful.
   */
  protected void performReconnect(int numRetries) throws Throwable
  {
    // gm comm info
    CommInfo gmCommInfo = _ctx.getConnectedGmChannel().getTptCommInfo();
    String gmNodeID = gmCommInfo.getRefId();

    // retrieve master channel
    ChannelInfo masterChannel = retrieveMasterChannel();
    CommInfo myCommInfo = masterChannel.getTptCommInfo();

    boolean level0 = _ctx.getNetworkSetting().getConnectionLevel().shortValue()
                     == NetworkSetting.LEVEL_NO_FIREWALL;

    // retrieve jms routers
    Collection routers = retrieveJmsRouters();
    String[] routerIPs = getRouterIPs(routers);

    constructMyCommInfoFormatter(myCommInfo, routerIPs);

    int interval = DelegateHelper.getProperties().getReconnectInterval();
    String waitMsg = "[ReconnectDelegate.performReconnect] Wait for next attempt in "+
                       interval + " seconds.";

    boolean performDisconnect = true;
    boolean retry = numRetries != 0;
    // while (! numReconnectTries used up && !succeed && !interrupted)
    while (retry)
    {
      Logger.debug("[ReconnectDelegate.performReconnect] Attempting Reconnect...");

      // check for interruption,
      // e.g if interrupted by Disconnect process, do not continue retrying
      //if (ConnectionContext.getCurrentTokenType() == ConnectionContext.DISCONNECT_TOKEN)
      if (isCancelReconnect())
      {
        Logger.debug("[ReconnectDelegate.performReconnect] Aborting Reconnect...");
        retry = false;
        continue;
      }

      //???? Is there a possibility that the ConnectDelegate starts executing between here?
      //so after the RECONNECT_TOKEN is seized, the Connect could be performed successfully
      _myToken = ConnectionContext.seizeToken(this, ConnectionContext.RECONNECT_TOKEN);

      if (_abort)
      {
        Logger.debug("[ReconnectDelegate.performReconnect] Connection re-established, Aborting Reconnect...");
        retry = false;
        continue;
      }
      try
      {
        if (performDisconnect)
        {
          // set all nodes offline first
          new DisconnectDelegate(_ctx, true).execute();
          performDisconnect = false;
        }

        boolean performReconnect = !isMyGtOnline();

        // do not try reconnect if already connected now
        // (becoz maybe intercepted by Connect process)
        if (performReconnect)
        {
          reconnectToGm(gmNodeID, gmCommInfo, myCommInfo, routerIPs, level0);
          if (!_success)
          {
            Logger.debug("[ReconnectDelegate.performReconnect] Unable to reconnect to GridMaster "+ gmNodeID);
            if (numRetries > 0) // if negative, will retry indefinitely
              numRetries--;
            retry = (numRetries != 0);
          }
          else
          {
            Logger.debug("[ReconnectDelegate.performReconnect] Reconnected to GridMaster "+ gmNodeID);
            retry = false;
          }
        }
        else
          retry = false;
      }
      finally
      {
        // release token to allow for interception by Connect/Disconnect process
        _myToken.release();

        // wait for next retry
        if (retry)
        {
          Logger.debug(waitMsg);
          _helper.sleep((long)interval);
        }

        Logger.debug("[ReconnectDelegate.performReconnect] Ending one round of reconnecting...");
      }
    }
  }

  /**
   * Reconnect to GridMaster
   *
   * @param gmNodeID GridNode ID of the GridMaster
   * @param gmCommInfo CommInfo of the GridMaster
   * @param myCommInfo CommInfo of this GridTalk
   * @param routerIPs Network Router IPs
   * @param level0 Whether connection level is 0 (no firewall).
   */
  protected void reconnectToGm(
    String gmNodeID, CommInfo gmCommInfo, CommInfo myCommInfo,
    String[] routerIPs, boolean level0) throws Throwable
  {
    //log the start of reconnect activity
    logStartReconnect(gmNodeID);

    updateMyGtReconnecting();
    updateGmReconnecting(gmNodeID);

    attemptConnection(
      gmNodeID,
      _ctx.getConnectedGmChannel(),
      gmCommInfo,
      myCommInfo,
      routerIPs,
      level0);

    //log the end of reconnect activity
    logEndReconnect(gmNodeID, _success);
  }
  
  /**
   * Check if the reconnection should not proceed. The reconnection process should not start or proceed if
   * any of the following is true:
   * <p>
   * <ol>
   *   <li>Another connection activity already in progress</li>
   *   <li>A Connected GM Channel is not found which means Reconnect could not be performed</li> 
   * </ol>
   * 
   * @return <b>true</b> if any of the above mentioned condition is true, <b>false</b> otherwise.
   */
  private boolean isCancelReconnect()
  {
    _abort = false;
    if (ConnectionContext.getCurrentTokenType() != ConnectionContext.UNKNOWN_TOKEN || 
        _ctx.getConnectedGmChannel() == null)
    {
      Logger.log("[ReconnectDelegate.isCancelReconnect] GridMaster Connection could not be found or Dis/Re/connection activity already in progress!");
      _abort = true;
    }
    return _abort;
  }
  
  /**
   * Set the Reconnection state.
   * 
   * @param state The reconnection state (process) to set
   */
  private static synchronized void setReconnectionState(Object state)
  {
    _reconnectState = state;
  }
  
  /**
   * Check if another Reconnection process has already started.
   * 
   * @return <b>true</b> if Reconnection state has been set, <b>false</b> otherwise.
   */
  private static synchronized boolean hasReconnectionStarted()
  {
    boolean started = _reconnectState != null;
    if (started)
    { 
      Logger.log("[ReconnectDelegate.hasReconnectionStarted] Another Reconnect process already started.");
    }
    
    return started;
  }

  /**
   * Unset the Reconnection state
   */  
  private static synchronized void clearReconnectionState()
  {
    _reconnectState = null;
  }
  
  public static synchronized void cancelReconnection()
  {
    if (_reconnectState != null)
    {
      ((ReconnectDelegate)_reconnectState)._abort = true;
    }
  }
}