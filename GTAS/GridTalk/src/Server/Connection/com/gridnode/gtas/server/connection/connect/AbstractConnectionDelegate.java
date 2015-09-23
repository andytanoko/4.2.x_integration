/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractConnectionDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 31 2002    Neo Sok Lay         Created
 * Dec 10 2002    Neo Sok Lay         Add updateGmOffline(gmNodeID, clearReconnectKey)
 * Apr 28 2003    Neo Sok Lay         Add logging of connection related activities,
 *                                    raise alert notification.
 *                                    Log on partner lost connection.
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.connection.helpers.Logger;
import java.io.File;

/**
 * Abstract class for Connection related delegates.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.0 I6
 */
public abstract class AbstractConnectionDelegate
  implements IConnectionSenderDelegate, IConnectionReceiverDelegate
{
  protected ConnectionContext _ctx;
  protected DelegateHelper    _helper;

  protected String            _eventID;
  protected String            _ackEventID;

  protected Object            _lock = new Object();
  protected boolean           _resultsReturned = false;

  protected boolean           _success = false;
  protected String            _failureReason;

  public AbstractConnectionDelegate(ConnectionContext ctx)
    throws Throwable
  {
    _ctx = ctx;
    _helper = new DelegateHelper();
    getEventIDs();
  }

  // receive
  public void receive(String eventSubID, String refTransID, String[] dataPayload, File[] filePayload) throws Throwable
  {
    /**@todo: Implement this com.gridnode.gtas.server.connection.connect.IConnectionReceiverDelegate method*/
    throw new java.lang.UnsupportedOperationException("Method receive() not yet implemented.");
  }

  // send
  public void execute() throws Throwable
  {
    /**@todo: Implement this com.gridnode.gtas.server.connection.connect.IConnectionDelegate method*/
    throw new java.lang.UnsupportedOperationException("Method execute() not yet implemented.");
  }

  public void receiveAck(String[] dataPayload, File[] filePayload) throws Throwable
  {
    /**@todo: Implement this com.gridnode.gtas.server.connection.connect.IConnectionDelegate method*/
    throw new java.lang.UnsupportedOperationException("Method receiveAck() not yet implemented.");
  }

  public void receiveFeedback(boolean success, String message) throws Throwable
  {
    /**@todo: Implement this com.gridnode.gtas.server.connection.connect.IConnectionDelegate method*/
    throw new java.lang.UnsupportedOperationException("Method receiveFeedback() not yet implemented.");
  }

  /**
   * Retrieve the Event IDs that the concrete delegate class is handling.
   */
  protected abstract void getEventIDs() throws Throwable;

  /**
   * Stop the Keep Alive Timer when Keep Alive acknowledgement returns fail or
   * timeout.
   */
  protected void stopKeepAliveTimer()
  {
    try
    {
      ServiceLookupHelper.getTimeManager().cancelAlarm(_ctx.getKeepAliveTimerID());
    }
    catch (Exception ex)
    {
      Logger.warn("[AbstractConnectionDelegate.stopKeepAliveTimer] Error ", ex);
    }

  }

  /**
   * Update that a Partner GridNode is online. Broadcast that the partner is
   * online.
   *
   * @param nodeID GridNode ID of the Partner
   * @param connectedGmNodeID GridNode ID of the GridMaster the partner is
   * connected to.
   */
  protected void updatePartnerOnline(
    String nodeID, String connectedGmNodeID) throws Throwable
  {
    ConnectionStatus connStatus = _helper.getConnectionStatus(nodeID);
    connStatus.setConnectedServerNode(connectedGmNodeID);
    connStatus.setStatusFlag(ConnectionStatus.STATUS_ONLINE);
    connStatus.setDTLastOnline(_helper.getCurrentTimestamp());
    _helper.updateConnectionStatus(connStatus);

    //broadcast that the partner has come online
    _helper.broadcastPartnerGtOnline(nodeID);
  }

  /**
   * Update that a Partner GridNode is offline. Broadcast that the partner is
   * offline.
   *
   * @param nodeID The GridNodeID of the GridNode.
   */
  protected void updatePartnerOffline(String nodeID)
  {
    updatePartnerOffline(nodeID, false);
  }

  /**
   * Update that a Partner GridNode is offline. Broadcast that the partner is
   * offline.
   *
   * @param nodeID The GridNodeID of the GridNode.
   * @param expired Whether the GridNode goes offline due to network connection
   * lost (not proper disconnect).
   */
  protected void updatePartnerOffline(String nodeID, boolean expired)
  {
    try
    {
      if (expired)
        logPartnerLostConnection(nodeID);

      _helper.broadcastPartnerGtOffline(nodeID);

      ConnectionStatus connStatus = _helper.getConnectionStatus(nodeID);
      if (connStatus.getStatusFlag() != ConnectionStatus.STATUS_OFFLINE)
      {
        short status = expired ? ConnectionStatus.STATUS_EXPIRED : ConnectionStatus.STATUS_OFFLINE;
        connStatus.setConnectedServerNode(null);

        connStatus.setStatusFlag(status);
        connStatus.setDTLastOffine(_helper.getCurrentTimestamp());
        _helper.updateConnectionStatus(connStatus);
      }
    }
    catch (Throwable t)
    {
      Logger.err("[AbstractConnectionDelegate.updatePartnerOffline] Error ", t);
    }
  }

  /**
   * Update that this GridTalk is Online. Broadcast that the GridTalk is online.
   *
   * @param connectedGmNode GridNode ID of the GridMaster that this GridTalk
   * is connected to.
   */
  protected void updateMyGtOnline(String connectedGmNode) throws Throwable
  {
    ConnectionStatus connStatus = _helper.getConnectionStatus(_ctx.getMyNodeID());

    connStatus.setDTLastOnline(_helper.getCurrentTimestamp());
    connStatus.setStatusFlag(ConnectionStatus.STATUS_ONLINE);
    connStatus.setConnectedServerNode(connectedGmNode);
    _helper.updateConnectionStatus(connStatus);
    _helper.broadcastMyGtOnlne(_ctx.getMyNodeID());
  }

  /**
   * Update that the GridMaster is online. Broadcast that the GridMaster is
   * online.
   *
   * @param gmNodeID the GridNodeID of the GridMaster that this GridTalk is
   * connected to.
   */
  protected void updateGmOnline(
    String gmNodeID, String reconnectKey) throws Throwable
  {
    ConnectionStatus connStatus = _helper.getConnectionStatus(gmNodeID);

    connStatus.setDTLastOnline(_helper.getCurrentTimestamp());
    connStatus.setReconnectionKey(reconnectKey);
    connStatus.setStatusFlag(ConnectionStatus.STATUS_ONLINE);
    _helper.updateConnectionStatus(connStatus);
    _helper.broadcastGmOnline(gmNodeID);
  }

  /**
   * Broadcast and update that this GridTalk is offline.
   */
  protected void updateMyGtOffline() throws Throwable
  {
    // broadcast offline first
    _helper.broadcastMyGtOffline(_ctx.getMyNodeID());

    ConnectionStatus connStatus = _helper.getConnectionStatus(_ctx.getMyNodeID());

    connStatus.setDTLastOffine(_helper.getCurrentTimestamp());
    connStatus.setStatusFlag(ConnectionStatus.STATUS_OFFLINE);
    connStatus.setConnectedServerNode(null);
    _helper.updateConnectionStatus(connStatus);
  }

  /**
   * Broadcast and update that the GridMaster is offline.
   *
   * @param gmNodeID the GridNodeID of the GridMaster that this GridTalk was
   * connected to.
   */
  protected void updateGmOffline(String gmNodeID) throws Throwable
  {
    updateGmOffline(gmNodeID, true);
  }

  /**
   * Broadcast and update that the GridMaster is offline.
   *
   * @param gmNodeID the GridNodeID of the GridMaster that this GridTalk was
   * connected to.
   * @param clearReconnectionKey Whether to clear the Reconnection Key
   */
  protected void updateGmOffline(String gmNodeID, boolean clearReconnectionKey)
    throws Throwable
  {
    _helper.broadcastGmOffline(gmNodeID);

    ConnectionStatus connStatus = _helper.getConnectionStatus(gmNodeID);

    connStatus.setDTLastOffine(_helper.getCurrentTimestamp());
    if (clearReconnectionKey)
      connStatus.setReconnectionKey(null);
    connStatus.setStatusFlag(ConnectionStatus.STATUS_OFFLINE);
    _helper.updateConnectionStatus(connStatus);
  }

  /**
   * Update that this GridTalk is attempting connection to the specified
   * GridMaster.
   *
   * @param gmNodeID GridNode ID of the GridMaster to connect to.
   */
  protected void updateGmConnecting(String gmNodeID) throws Throwable
  {
    updateConnStatus(gmNodeID, ConnectionStatus.STATUS_CONNECTING);
    _helper.broadcastGmConnecting(gmNodeID);
  }

  /**
   * Update that this GridTalk is attempting reconnection to the specified
   * GridMaster.
   *
   * @param gmNodeID GridNodeID of the GridMaster to reconnect to.
   */
  protected void updateGmReconnecting(String gmNodeID) throws Throwable
  {
    updateConnStatus(gmNodeID, ConnectionStatus.STATUS_RECONNECTING);
    _helper.broadcastGmReconnecting(gmNodeID);
  }

  /**
   * Update that this GridTalk is attempting disconnection to the specified
   * GridMaster.
   *
   * @param gmNodeID GridNode ID of the GridMaster to disconnect from.
   */
  protected void updateGmDisconnecting(String gmNodeID) throws Throwable
  {
    updateConnStatus(gmNodeID, ConnectionStatus.STATUS_DISCONNECTING);
    _helper.broadcastGmDisconnecting(gmNodeID);
  }

  /**
   * Update the connection status of a GridNode.
   * @param status The connection status: CONNECTING, DISCONNECTING, RECONNECTING.
   */
  private void updateConnStatus(String nodeID, short status) throws Throwable
  {
    ConnectionStatus connStatus = _helper.getConnectionStatus(nodeID);
    connStatus.setStatusFlag(status);
    _helper.updateConnectionStatus(connStatus);
  }


  /**
   * Update that this GridTalk is attempting connection to GridMaster.
   */
  protected void updateMyGtConnecting() throws Throwable
  {
    updateMyGtConnStatus(ConnectionStatus.STATUS_CONNECTING);
    _helper.broadcastMyGtConnecting(_ctx.getMyNodeID());
  }

  /**
   * Update that this GridTalk is attempting reconnection to GridMaster.
   */
  protected void updateMyGtReconnecting() throws Throwable
  {
    updateMyGtConnStatus(ConnectionStatus.STATUS_RECONNECTING);
    _helper.broadcastMyGtReconnecting(_ctx.getMyNodeID());
  }

  /**
   * Update that this GridTalk is attempting disconnection to GridMaster.
   */
  protected void updateMyGtDisconnecting() throws Throwable
  {
    updateMyGtConnStatus(ConnectionStatus.STATUS_DISCONNECTING);
    _helper.broadcastMyGtDisconnecting(_ctx.getMyNodeID());
  }

  /**
   * Update the connection status of this GridTalk
   * @param status The connection status: CONNECTING, DISCONNECTING, RECONNECTING.
   */
  private void updateMyGtConnStatus(short status) throws Throwable
  {
    updateConnStatus(_ctx.getMyNodeID(), status);
  }

  /**
   * Register the delegate to receive feedback & acknowledgement for
   * the message to be sent, and then send the message.
   *
   * @param dataPayload Data payload to be sent
   * @param filePayload File payload to be sent
   * @param sendChannel ChannelInfo to use for sending.
   */
  protected void registerAndSend(
    String[] dataPayload, File[] filePayload,
    ChannelInfo sendChannel) throws Throwable
  {
     String transID = _helper.generateTransID();
    _ctx.setDelegate(_eventID, transID, this);
    if (_ackEventID != null)
      _ctx.setDelegate(_ackEventID, transID, this);

    _helper.send(_eventID, transID, dataPayload, filePayload,
      sendChannel);
  }

  /**
   * Checks if this GridTalk is currently online.
   * @return <b>true</b> if GridTalk is connected to GridMaster, <b>false</b>
   * otherwise.
   */
  protected boolean isMyGtOnline() throws Throwable
  {
    boolean online = false;
    ConnectionStatus connStatus = _helper.getConnectionStatus(_ctx.getMyNodeID());

    online = (connStatus.getStatusFlag() == ConnectionStatus.STATUS_ONLINE);

    return online;
  }

  protected void logStartConnect(String nodeID)
  {
    ActivityTracker.getInstance().startActivity(
      nodeID,
      ActivityTracker.TYPE_CONNECT);
  }

  protected void logEndConnect(String nodeID, boolean success)
  {
    ActivityTracker.getInstance().endActivity(
      nodeID,
      ActivityTracker.TYPE_CONNECT,
      success? ActivityTracker.STATUS_SUCCESS : ActivityTracker.STATUS_FAILURE);
  }

  protected void logStartDisconnect(String nodeID)
  {
    ActivityTracker.getInstance().startActivity(
      nodeID,
      ActivityTracker.TYPE_DISCONNECT);
  }

  protected void logEndDisconnect(String nodeID, boolean success)
  {
    ActivityTracker.getInstance().endActivity(
      nodeID,
      ActivityTracker.TYPE_DISCONNECT,
      success? ActivityTracker.STATUS_SUCCESS : ActivityTracker.STATUS_FAILURE);
  }

  protected void logStartReconnect(String nodeID)
  {
    ActivityTracker.getInstance().startActivity(
      nodeID,
      ActivityTracker.TYPE_RECONNECT);
  }

  protected void logEndReconnect(String nodeID, boolean success)
  {
    ActivityTracker.getInstance().endActivity(
      nodeID,
      ActivityTracker.TYPE_RECONNECT,
      success? ActivityTracker.STATUS_SUCCESS : ActivityTracker.STATUS_FAILURE);
  }

  protected void logLostConnection(String nodeID)
  {
    ActivityTracker.getInstance().logActivity(
      nodeID,
      ActivityTracker.TYPE_LOST_CONNECTION);
  }

  protected void logPartnerLostConnection(String nodeID)
  {
    ActivityTracker.getInstance().logActivity(
          nodeID,
          ActivityTracker.TYPE_LOST_PARTNER);
  }

}