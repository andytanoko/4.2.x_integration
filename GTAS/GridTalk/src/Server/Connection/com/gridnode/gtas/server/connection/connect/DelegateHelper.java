/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DelegateHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 30 2002    Neo Sok Lay         Created
 * Nov 29 2002    Neo Sok Lay         Add broadcastReconnecting() method.
 * Dec 10 2002    Neo Sok Lay         Add sleep() in terms of seconds.
 *                                    Pass in header when making Channel connection.
 *                                    The header will differentiate the type
 *                                    of router (network/local) being connected to.
 * Dec 26 2002    Neo Sok Lay         Add broadcast() method.
 * Sep 03 2003    Neo Sok Lay         Use INotification & notifier from pdip.framework
 * Oct 01 2003    Neo Sok Lay         Pass fixed-size header array to ChannelManager when
 *                                    sending.
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.server.notify.ConnectionNotification;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.helpers.ChannelSendHeader;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.notification.INotification;
import com.gridnode.pdip.framework.notification.Notifier;

import java.io.File;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Random;

public class DelegateHelper
{
  private static final Object _lock = new Object();
  private static final String[] _networkConnectHeader =
    new String[] {"GM.CONNECTION", ConnectionContext.CONN_LOST_NETWORK};
  private static final String[] _localConnectHeader =
    new String[] {"GM.CONNECTION", ConnectionContext.CONN_LOST_LOCAL};

  private static ConnectionProperties _props = null;

  private Random _randGen = new Random(System.currentTimeMillis());

  private boolean _isNotified = false;
  private Thread _sleepingThread;

  /**
   * Let the current thread sleep for the specified number of minutes, or until
   * a WakeUp is called.
   *
   * @param timeout The number of minutes to sleep.
   */
  synchronized void sleep(int timeout)
  {
    sleep((long)timeout*60L);
  }

  /**
   * Let the current thread sleep for the specified number of seconds, or until
   * a WakeUp is called.
   *
   * @param timeoutSeconds The number of seconds to sleep.
   */
  synchronized void sleep(long timeoutSeconds)
  {
    if (!_isNotified)
    {
      try
      {
        long duration = timeoutSeconds * 1000L; // to give discount??
        _sleepingThread = Thread.currentThread();
        Logger.debug(
          "[DelegateHelper.sleep] Sleeping for "+timeoutSeconds+" seconds... Please remember to wake me up.");
        wait(duration);
      }
      catch(Throwable ex)
      {
        Logger.debug("[DelegateHelper.sleep] ", ex);
      }
    }
  }

  /**
   * Wake up the sleeping thread, if any.
   */
  synchronized void wakeUp()
  {
    if (_sleepingThread != null && _sleepingThread.isAlive())
    {
      try
      {
        Logger.debug("[DelegateHelper.wakeUp] Waking up...");
        notify();
      }
      catch (Exception ex)
      {
        Logger.debug("[DelegateHelper.wakeUp] ", ex);
      }
      finally
      {
        _sleepingThread = null;
      }
    }
    _isNotified = true;
  }

  /**
   * Connect to the specified Jms router, and listen to the indicated topic.
   * @param commInfo The CommInfo indicating the Jms router to connect to and
   * the topic to listen to.
   */
  static void connectAndListen(CommInfo commInfo, boolean local)
    throws Throwable
  {
    if (getProperties().getIsTest())
    {
      Logger.log("[DelegateHelper.connect] Testing only. No real connect initiated");
    }
    else
    {
      //connectAndListen
      ServiceLookupHelper.getChannelManager().connectAndListen(
        commInfo, local? _localConnectHeader : _networkConnectHeader);
    }
  }

  /**
   * Connect to the specified Jms router.
   * @param commInfo The CommInfo indicating the Jms router to connect to.
   */
  static void connectOnly(CommInfo commInfo, boolean local)
    throws Throwable
  {
    if (getProperties().getIsTest())
    {
      Logger.log("[DelegateHelper.connect] Testing only. No real connect initiated");
    }
    else
    {
      //connectAndListen
      ServiceLookupHelper.getChannelManager().connect(
        commInfo, local? _localConnectHeader : _networkConnectHeader);
    }
  }

  /**
   * Disconnect from the specified Jms router.
   * @param commInfo the CommInfo indicating the Jms router to disconnect from.
   */
  static void disconnect(CommInfo commInfo)
  {
    try
    {
      if (getProperties().getIsTest())
      {
        Logger.log("[DelegateHelper.disconnect] Testing only. No real disconnect initiated");
      }
      else
      {
        //disconnect, should not throw exception
        ServiceLookupHelper.getChannelManager().disconnect(commInfo);
      }
    }
    catch (Throwable t)
    {
      Logger.warn("[DelegateHelper.disconnect] Error disconnecting.. ", t);
    }
  }

  /**
   * Send a message.
   *
   * @param eventID the Event ID of the message
   * @param transID The Trans ID for the message instance.
   * @param dataPayload The data payload of the message
   * @param filePayload The file payload of the message.
   * @param sendChannel The channel to send via.
   */
  void send(
    String eventID, String transID,
    String[] dataPayload, File[] filePayload,
    ChannelInfo sendChannel)
    throws Throwable
  {
    if (getProperties().getIsTest())
    {
      Logger.log("[DelegateHelper.send] Testing only. No real sending initiated");
    }
    else
    {
      String fileID = null;
      if (filePayload != null)
        fileID = "connection_E"+eventID+"_T"+transID;

      //031001NSL
      ChannelSendHeader header = new ChannelSendHeader(
                                     eventID, transID, null, fileID);
      header.setGridnodeHeaderInfo(
        ConnectionContext.getInstance().getMyNodeID(),
        sendChannel.getReferenceId(), null);
                                       
      // send via sendChannel
      ServiceLookupHelper.getChannelManager().send(
        sendChannel, dataPayload, filePayload, header.getHeaderArray());
        /*031001NSL
        new String[] {eventID, transID, null, fileID,
        ConnectionContext.getInstance().getMyNodeID(), //sender
        sendChannel.getReferenceId()});  //recipient
        */
    }
  }

  /**
   * Loads the Connection Properties.
   */
  private static void loadProperties()
  {
    try
    {
      _props = ConnectionProperties.load();
    }
    catch (Throwable t)
    {
      Logger.err("[DelegateHelper.loadProperties] Error ", t);
      _props = ConnectionProperties.getDefaultProperties();
    }
  }

  /**
   * Obtain the loaded ConnectionProperties.
   *
   * @return The ConnectionProperties.
   */
  static ConnectionProperties getProperties() throws Throwable
  {
    if (_props == null)
    {
      synchronized (_lock)
      {
        if (_props == null)
          loadProperties();
      }
    }

    return _props;
  }

  /**
   * Generate a arbitrary TransID for the message to send.
   *
   * @return The generated TransID.
   */
  String generateTransID()
  {
    int transId = _randGen.nextInt();
    if (transId == Integer.MIN_VALUE)
      return "0";
    return String.valueOf(Math.abs(transId));
  }

  /**
   * Get the Timestamp of the current time.
   */
  static Timestamp getCurrentTimestamp()
  {
    return new Timestamp(System.currentTimeMillis());
  }

  /**
   * Update the ChannelInfo and its related profiles to the database.
   */
  static void updateChannel(ChannelInfo channel) throws Throwable
  {
    try
    {
      IChannelManagerObj mgr = ServiceLookupHelper.getChannelManager();
      mgr.updateChannelInfo(channel);
      mgr.updateCommInfo(channel.getTptCommInfo());
      mgr.updatePackagingInfo(channel.getPackagingProfile());
      mgr.updateSecurityInfo(channel.getSecurityProfile());
    }
    catch (Throwable t)
    {
      Logger.warn("[DelegateHelper.updateChannel] Error ", t);
      throw new Exception("Unable to update Channel related information");
    }

  }

  /**
   * Retrieve the master channel with a particular reference id.
   *
   * @param refID The referenceID.
   * @return The channel retrieved, or <b>null</b> if no such channel found.
   */
  static ChannelInfo retrieveMasterChannel(String refID) throws Throwable
  {
    Collection results = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ChannelInfo.IS_MASTER, filter.getEqualOperator(),
        Boolean.TRUE, false);
      filter.addSingleFilter(filter.getAndConnector(), ChannelInfo.REF_ID,
        filter.getEqualOperator(), refID, false);

      results = ServiceLookupHelper.getChannelManager().getChannelInfo(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[ConnectDelegate.retrieveMasterChannel] Error ", t);
      throw new Exception("Error retrieving Master Channel for RefID: "+refID);
    }

    return (results.isEmpty() ? null : (ChannelInfo)results.toArray()[0]);
  }

  /**
   * Update the CommInfo to the database.
   *
   * @param commInfo The CommInfo to update.
   */
  static void updateCommInfo(CommInfo commInfo) throws Throwable
  {
    try
    {
      ServiceLookupHelper.getChannelManager().updateCommInfo(commInfo);
    }
    catch (Throwable t)
    {
      Logger.warn("[DelegateHelper.updateCommInfo] Error ", t);
      throw new Exception("Unable to update CommInfo");
    }
  }

  /**
   * Retrieve the ConnectionStatus for a particular GridNode.
   *
   * @param nodeID The GridNodeID of the GridNode.
   * @return The ConnectionStatus retrieved.
   * @throws Exception No such GridNode.
   */
  static ConnectionStatus getConnectionStatus(String nodeID)
    throws Throwable
  {
    ConnectionStatus connStatus = null;

    try
    {
      connStatus = ServiceLookupHelper.getGridNodeManager().findConnectionStatusByNodeID(nodeID);
    }
    catch (Throwable t)
    {
      Logger.warn("[DelegateHelper.getConnectionStatus] Error ", t);
      throw new Exception("Unable to retrieve Connection Status");
    }
    return connStatus;
  }

  /**
   * Update the ConnectionStatus to the database.
   *
   * @param connStatus The ConnectionStatus to update.
   */
  static ConnectionStatus updateConnectionStatus(ConnectionStatus connStatus) throws Throwable
  {
    try
    {
      ServiceLookupHelper.getGridNodeManager().updateConnectionStatus(connStatus);

      return getConnectionStatus(connStatus.getGridNodeID());
    }
    catch (Throwable t)
    {
      Logger.warn("[DelegateHelper.updateConnectionStatus] Error ", t);
      throw new Exception("Unable to update Connection Status");
    }
  }

  /**
   * Retrieve the encryption Certificate used by a particular channel in a File.
   *
   * @param channel The Channel.
   * @return The certificate file.
   */
  static File getCertFile(ChannelInfo channel) throws Throwable
  {
    ICertificateManagerObj certMgr = ServiceLookupHelper.getCertificateManager();

    Certificate cert = certMgr.findCertificateByUID(
                         channel.getSecurityProfile().getEncryptionCertificateID());
    String filename = FileUtil.getFile(IPathConfig.PATH_TEMP, "").getAbsolutePath()
                      + "/myCert.cert";

    GridCertUtilities.writeX509Certificate(
      filename,
      GridCertUtilities.loadX509Certificate(
      GridCertUtilities.decode(cert.getCertificate())));

    return new File(filename);
  }

  /**
   * Broadcast that the GridMaster is online.
   *
   * @param gmNodeID GridNode ID of the GridMaster.
   */
  static void broadcastGmOnline(String gmNodeID)
  {
    broadcastConnectionState(ConnectionNotification.STATE_ONLINE,
      ConnectionNotification.TYPE_GM, gmNodeID,
      ConnectionContext.getInstance().getMyCommInfoString());
  }

  /**
   * Broadcast that the GridMaster is offline.
   *
   * @param gmNodeID GridNode ID of the GridMaster.
   */
  static void broadcastGmOffline(String gmNodeID)
  {
    broadcastConnectionState(ConnectionNotification.STATE_OFFLINE,
      ConnectionNotification.TYPE_GM, gmNodeID);
  }

  /**
   * Broadcast that GridTalk is currently attempting connection to
   * the GridMaster.
   *
   * @param gmNodeID GridNode ID of the GridMaster.
   */
  static void broadcastGmConnecting(String gmNodeID)
  {
    broadcastConnectionState(ConnectionNotification.STATE_CONNECTING,
      ConnectionNotification.TYPE_GM, gmNodeID);
  }

  /**
   * Broadcast that the GridTalk is currently attempting disconnection from
   * the GridMaster.
   *
   * @param gmNodeID GridNode ID of the GridMaster.
   */
  static void broadcastGmDisconnecting(String gmNodeID)
  {
    broadcastConnectionState(ConnectionNotification.STATE_DISCONNECTING,
      ConnectionNotification.TYPE_GM, gmNodeID);
  }

  /**
   * Broadcast that GridTalk is currently reconnecting to the GridMaster.
   *
   * @param gmNodeID GridNode ID of the GridMaster.
   */
  static void broadcastGmReconnecting(String gmNodeID)
  {
    broadcastConnectionState(ConnectionNotification.STATE_RECONNECTING,
      ConnectionNotification.TYPE_GM, gmNodeID);
  }

  /**
   * Broadcast that this GridTalk is online.
   *
   * @param myNodeID GridNode ID of this GridTalk.
   */
  static void broadcastMyGtOnlne(String myNodeID)
  {
    broadcastConnectionState(ConnectionNotification.STATE_ONLINE,
      ConnectionNotification.TYPE_ME, myNodeID);
  }

  /**
   * Broadcast that this GridTalk is offline.
   *
   * @param myNodeID GridNode ID of this GridTalk.
   */
  static void broadcastMyGtOffline(String myNodeID)
  {
    broadcastConnectionState(ConnectionNotification.STATE_OFFLINE,
      ConnectionNotification.TYPE_ME, myNodeID);
  }

  /**
   * Broadcast that this GridTalk is currently attempting connection to
   * GridMaster.
   *
   * @param myNodeID GridNode ID of this GridTalk.
   */
  static void broadcastMyGtConnecting(String myNodeID)
  {
    broadcastConnectionState(ConnectionNotification.STATE_CONNECTING,
      ConnectionNotification.TYPE_ME, myNodeID);
  }

  /**
   * Broadcast that this GridTalk is attempting disconnection from GridMaster.
   *
   * @param myNodeID GridNode ID of this GridTalk.
   */
  static void broadcastMyGtDisconnecting(String myNodeID)
  {
    broadcastConnectionState(ConnectionNotification.STATE_DISCONNECTING,
      ConnectionNotification.TYPE_ME, myNodeID);
  }

  /**
   * Broadcast that GridTalk is currently reconnecting to the GridMaster.
   *
   * @param myNodeID GridNode ID of the GridTalk.
   */
  static void broadcastMyGtReconnecting(String myNodeID)
  {
    broadcastConnectionState(ConnectionNotification.STATE_RECONNECTING,
      ConnectionNotification.TYPE_ME, myNodeID);
  }

  /**
   * Broadcast that a partner GridTalk is online.
   *
   * @param partnerNodeID GridNode ID of partner GridTalk.
   */
  static void broadcastPartnerGtOnline(String partnerNodeID)
  {
    broadcastConnectionState(ConnectionNotification.STATE_ONLINE,
      ConnectionNotification.TYPE_PARTNER, partnerNodeID);
  }

  /**
   * Broadcast that a partner GridTalk is offline.
   *
   * @param partnerNodeID GridNode ID of partner GridTalk.
   */
  static void broadcastPartnerGtOffline(String partnerNodeID)
  {
    broadcastConnectionState(ConnectionNotification.STATE_OFFLINE,
      ConnectionNotification.TYPE_PARTNER, partnerNodeID);
  }

  /**
   * Broadcast a Notification message.
   *
   * @param notification The Notification message to broadcast.
   */
  static void broadcast(INotification notification)
  {
    try
    {
      Notifier.getInstance().broadcast(notification);
    }
    catch (Exception ex)
    {
      Logger.error(ILogErrorCodes.GT_CONNECTION_DELEGATE_HELPER,
                   "[DelegateHelper.broadcastConnectionState] Fail to broadcast. Error: "+ex.getMessage(), ex);
    }
  }

  /**
   * Broadcast a ConnectionNotification message.
   *
   * @param state The state of the connection
   * @param nodeType Type of GridNode
   * @param nodeID GridNode ID.
   */
  private static void broadcastConnectionState(
    short state, short nodeType, String nodeID)
  {
    ConnectionNotification notification = new ConnectionNotification(
                                              state, nodeType, nodeID);
    broadcast(notification);
  }

  /**
   * Broadcast a ConnectionNotification message.
   *
   * @param state The state of the connection
   * @param nodeType Type of GridNode
   * @param nodeID GridNode ID.
   * @param myGNCI My GridNodeCommInfo String.
   */
  private static void broadcastConnectionState(
    short state, short nodeType, String nodeID, String myGNCI)
  {
    ConnectionNotification notification = new ConnectionNotification(
                                              state, nodeType, nodeID, myGNCI);
    broadcast(notification);
  }

}