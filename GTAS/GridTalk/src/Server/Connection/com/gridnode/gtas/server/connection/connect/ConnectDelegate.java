/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 30 2002    Neo Sok Lay         Created
 * Dec 10 2002    Neo Sok Lay         Add indication on whether making Network
 *                                    or Local router connection.
 *                                    Set filePayload null if not sending certificate.
 *                                    Add token-based execution.
 * Dec 26 2002    Neo Sok Lay         Notify update of Utc Time.
 * Apr 28 2003    Neo Sok Lay         log start/end connect activity.
 * Dec 04 2003    Neo Sok Lay         Change in construction of JMSCommInfo.
 * May 04 2004    Neo Sok Lay         Cancel Reconnect (if in progress) if connect is
 *                                    successful.
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.server.connection.exceptions.ConnectionException;
import com.gridnode.gtas.server.connection.helpers.CommInfoFormatter;
import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.connection.model.JmsRouter;
import com.gridnode.gtas.server.connection.model.NetworkSetting;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.notify.TimeUpdateNotification;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.base.transport.comminfo.JMSCommInfo;
import com.gridnode.pdip.framework.db.entity.EntityOrderComparator;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

/**
 * This Delegate class handles the Connect step of the connection process.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3.3
 * @since 2.0 I6
 */
public class ConnectDelegate extends AbstractConnectionDelegate
{
  private String            _reconnectKey;
  private long              _utcTime;
  private File              _masterCert;
  private EntityOrderComparator _gmComparator, _routerComparator;
  protected CommInfoFormatter _myCIFormatter;

  private ConnectionContext.ConnectionToken   _myToken;

  public ConnectDelegate(ConnectionContext ctx)
    throws Throwable
  {
    super(ctx);

    _routerComparator = new EntityOrderComparator(JmsRouter.UID,
                          _ctx.getConnectionSetupResult().getAvailableRouterUIDs());

    _gmComparator = new EntityOrderComparator(GridNode.UID,
                     _ctx.getConnectionSetupResult().getAvailableGridMastersUIDs());
  }

  public void execute() throws Throwable
  {
    Logger.debug("[ConnectDelegate.execute] Enter");

    _myToken = ConnectionContext.seizeToken(this, ConnectionContext.CONNECT_TOKEN);

    try
    {
      boolean performConnect = true;
      // if previous token owner was doing reconnect/connect
      //   check if gridtalk is online, if yes don't perform the connect.
      if (_myToken.getPreviousType() == ConnectionContext.RECONNECT_TOKEN ||
          _myToken.getPreviousType() == ConnectionContext.CONNECT_TOKEN)
      {
        performConnect = !isMyGtOnline();
      }

      if (performConnect)
      {
        updateMyGtConnecting();
        performConnect();
      }
    }
    finally
    {
      _myToken.release();
      Logger.debug("[ConnectDelegate.execute] Exit");
    }
  }

  protected void performConnect() throws Throwable
  {
    // retrieve Gm nodes
    Collection gms = retrieveGmNodes();

    // retrieve jms routers
    Collection routers = retrieveJmsRouters();

    // retrieve Gm channel
    ChannelInfo gmChannel = retrieveGmChannel(getGmNodeIDs(gms));
    CommInfo gmCommInfo = gmChannel.getTptCommInfo();

    // retrieve master channel
    ChannelInfo masterChannel = retrieveMasterChannel();
    CommInfo myCommInfo = masterChannel.getTptCommInfo();

    _masterCert = DelegateHelper.getCertFile(masterChannel);

    String[] routerIPs = getRouterIPs(routers);

    boolean level0 = _ctx.getNetworkSetting().getConnectionLevel().shortValue()
                     == NetworkSetting.LEVEL_NO_FIREWALL;

    //construct my GridNodeCommInfo.
    constructMyCommInfoFormatter(myCommInfo, routerIPs);

    // try until connected or gm node list exhausted
    for (Iterator i=gms.iterator(); i.hasNext() && !_success; )
    {
      GridNode gmNode = (GridNode)i.next();

      //log the start of connect activity
      logStartConnect(gmNode.getID());

      updateGmConnecting(gmNode.getID());

      attemptConnection(gmNode.getID(), gmChannel, gmCommInfo,
        myCommInfo, routerIPs, level0);

      if (!_success)
        updateGmOffline(gmNode.getID(), true);

      //log the end of connect activity
      logEndConnect(gmNode.getID(), _success);
    }

    if (!_success)
      throw new ConnectionException(_failureReason);
      
    ReconnectDelegate.cancelReconnection();
  }

  /**
   * Invoked when feedback is received for the connect message.
   *
   * @param success Whether the connect message was sent successfully.
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
    // data: status, comminfo, utc time, reconnect key, failure reason
    synchronized (_lock)
    {
      _resultsReturned = true;

      try
      {
        // collate the results
        Boolean status = Boolean.valueOf(dataPayload[0]);
        _success = status.booleanValue();
        _failureReason = (_success ? null : dataPayload[4]);

        if (_success)
        {
          //_returnedCommInfo = CommInfoFormatter.toCommInfo(dataPayload[1]);
          _ctx.setMyCommInfoString(dataPayload[1]);

          _utcTime      = Long.parseLong(dataPayload[2]);

          _reconnectKey     = dataPayload[3];
        }
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
   * Connect to the Local Jms Router and listen to local topic.
   * Use for Connection Level 0 only.
   *
   * @param myCommInfo This GridTalk's commInfo.
   */
  protected void connectToLocalRouter(CommInfo myCommInfo)
    throws Throwable
  {
    // set local router topic, user, password, ip to master comminfo
    //JMSCommInfo jmsInfo = new JMSCommInfo(myCommInfo.getURL());
    JMSCommInfo jmsInfo = new JMSCommInfo();
    jmsInfo.setURL(myCommInfo.getURL());
    jmsInfo.setHost(_ctx.getNetworkSetting().getLocalJmsRouter());
    jmsInfo.setDestination(_ctx.getConnectionSetupResult().getLocalRouterTopic());
    jmsInfo.setUserName(_ctx.getConnectionSetupResult().getLocalRouterUser());
    jmsInfo.setPassword(_ctx.getConnectionSetupResult().getLocalRouterPassword());
    myCommInfo.setURL(jmsInfo.toURL());
    DelegateHelper.connectAndListen(myCommInfo, true);
  }

  /**
   * Connect to a Network Jms Router and listen to network topic.
   * Use for Connection Level > 0.
   *
   * @param myCommInfo This GridTalk's commInfo.
   * @param routerIP IP address of the network router to connect to.
   * @param port Port number.
   */
  private void connectToNetworkRouter(CommInfo myCommInfo,
    String routerIP)
    throws Throwable
  {
    // set router ip, topic, user, password to master comminfo
    JMSCommInfo jmsInfo = new JMSCommInfo();
    jmsInfo.setURL(myCommInfo.getURL());
    jmsInfo.setHost(routerIP);
    jmsInfo.setDestination(_ctx.getConnectionSetupResult().getNetworkRouterTopic());
    jmsInfo.setUserName(_ctx.getConnectionSetupResult().getNetworkRouterUser());
    jmsInfo.setPassword(_ctx.getConnectionSetupResult().getNetworkRouterPassword());
    myCommInfo.setURL(jmsInfo.toURL());
    // listen with master comminfo
    DelegateHelper.connectAndListen(myCommInfo, false);
  }

  /**
   * Attempt connection to a GridMaster.
   *
   * @param gmNodeID GridNodeID of the GridMaster.
   * @param gmChannel ChannelInfo of the GridMaster
   * @param gmCommInfo JMSCommInfo of the GridMaster
   * @param myCommInfo JMSCommInfo of this GridTalk
   * @param routerIPs Array of IP address of the network routers available
   * for connection.
   * @param level0 Whether this GridTalk's connection level is 0.
   */
  protected void attemptConnection(
    String gmNodeID, ChannelInfo gmChannel,
    CommInfo gmCommInfo, CommInfo myCommInfo,
    String[] routerIPs, boolean level0) throws Throwable
  {
    Logger.log("[ConnectDelegate.attemptConnection] Attempting connection to GridMaster "+
      gmNodeID);

    gmChannel.setReferenceId(gmNodeID);
    gmCommInfo.setRefId(gmNodeID);

    JMSCommInfo gmJmsInfo = new JMSCommInfo();
    gmJmsInfo.setURL(gmCommInfo.getURL());
    gmJmsInfo.setDestination(_ctx.getConnectionSetupResult().getGridMasterTopic(gmNodeID));

    _myCIFormatter.setConnectedGmNodeID(gmNodeID);
    String reconnectKey = getReconnectKey(gmNodeID);

    // try until connected or router list exhausted
    for (int j=0; j<routerIPs.length && !_success; j++ )
    {
      //  set router ip to gm comminfo
      gmJmsInfo.setHost(routerIPs[j]);
      gmCommInfo.setURL(gmJmsInfo.toURL());
      Logger.debug("[ConnectDelegate.attemptConnection] GMCommInfo URL = "+gmCommInfo.getURL());
      // send connect to gm
      try
      {
        if (level0)
        {
          connectToLocalRouter(myCommInfo);
          DelegateHelper.connectOnly(gmCommInfo, false);
        }
        else
          connectToNetworkRouter(myCommInfo, routerIPs[j]);

        connectToGm(myCommInfo, level0,
          gmChannel, gmJmsInfo.getDestination(), reconnectKey);

        checkResult(gmNodeID, gmChannel, gmCommInfo, myCommInfo);
      }
      catch (Throwable t)
      {
        DelegateHelper.disconnect(myCommInfo);
        if (level0)
          DelegateHelper.disconnect(gmCommInfo);
        Logger.log("[ConnectDelegate.attemptConnection] "
         + "Unable to connect to GridMaster on router "+ routerIPs[j]);
      }
    }

    if (!_success)
      Logger.log("[ConnectDelegate.execute] Unable to connect to GridMaster "+
        gmNodeID);
    else
    {
      Logger.log("[ConnectDelegate.execute] Connected to GridMaster "+
        gmNodeID);

      // start keep alive timer
      new KeepAliveTimerDelegate(_ctx).execute();

      // start retrieve pendings
      new RetrievePendingsDelegate(_ctx).execute();

      // start informing partner online
      new InformPartnerOnlineDelegate(_ctx).execute();
    }
  }

  /**
   * Get the reconnect key from the GridMaster GridNode.
   *
   * @param gmNodeID NodeID of the GridMaster GridNode.
   *
   * @return the ReconnectKey of the GridMaster, or <b>null</b> if none exists.
   */
  private String getReconnectKey(String gmNodeID) throws Throwable
  {
    String reconnectKey = ServiceLookupHelper.getGridNodeManager().findConnectionStatusByNodeID(
                            gmNodeID).getReconnectionKey();

    if (reconnectKey != null && reconnectKey.trim().length()==0)
      reconnectKey = null;

    return reconnectKey;
  }

  /**
   * Check the result of the Connect message sent to the GridMaster
   * via a particular Network router. This method waits for the
   * acknowledgement of the Connect message. If connection successful,
   * updates the CommInfos and also the online status of the GridMaster
   * and this GridTalk.
   * @param gmNodeID GridNodeID of the GridMaster
   * @param gmChannel ChannelInfo of the GridMaster
   * @param gmCommInfo CommInfo of the GridMaster
   * @param myCommInfo CommInfo of this GridTalk.
   */
  private void checkResult(
    String gmNodeID, ChannelInfo gmChannel, CommInfo gmCommInfo,
    CommInfo myCommInfo) throws Throwable
  {
    // *** wait for result
    synchronized (_lock)
    {
      Logger.debug("[ConnectDelegate.checkResult] Is connection successful? "+_success);
      if (_success)
      {
        // update Utc time
        notifyUtcTimeUpdate();

        // update gm channel
        gmChannel.getSecurityProfile().setReferenceId(gmNodeID);
        gmChannel.getPackagingProfile().setReferenceId(gmNodeID);
        gmChannel.setTptCommInfo(gmCommInfo);
        DelegateHelper.updateChannel(gmChannel);
        _ctx.setConnectedGmChannel(gmChannel);
        _ctx.setConnectionLost(null);

        // update master comminfo
        DelegateHelper.updateCommInfo(myCommInfo);

        // update reconnect key for gm
        updateGmOnline(gmNodeID, _reconnectKey);

        // set connected
        updateMyGtOnline(gmNodeID);
      }
    }
  }

  /**
   * Send Connect request to a GridMaster.
   *
   * @param myCommInfo This GridTalk's CommInfo.
   * @param isLevel0 Whether the Connection Level is 0.
   * @param gmChannel The Channel of the GridMaster to request connection.
   * @param gmTopic The Topic that the GridMaster is listening.
   */
  private void connectToGm(
    CommInfo myCommInfo,  boolean isLevel0,
    ChannelInfo gmChannel, String gmTopic, String reconnectKey) throws Throwable
  {
    String myIP = "127.0.0.1";

    //if (isLevel0)
    //  retrieveMyIP from gmdummy @ref IPRetriever

    _myCIFormatter.setIP(myIP);

    // my ip, comminfo string, reconnect key, prod key, cert
    String[] dataPayload = new String[]
    {
      myIP,
      _myCIFormatter.toString(),
      reconnectKey,
      _ctx.getProductKey(),
    };

    File[] filePayload = (_masterCert==null)? null :
                           new File[]
                           {
                             _masterCert,
                           };

    registerAndSend(dataPayload, filePayload, gmChannel);

    if (!_resultsReturned)
      _helper.sleep(DelegateHelper.getProperties().getConnectTimeout());
  }

  /**
   * Retrieve the event IDs that the Delegate can handle.
   */
  protected void getEventIDs() throws Throwable
  {
    _eventID = DelegateHelper.getProperties().getConnectEventId();
    _ackEventID = DelegateHelper.getProperties().getConnectAckEventId();
  }

  /**
   * Retrieve the available GridMaster Nodes.
   *
   * @return All available GridMaster GridNodes.
   */
  private Collection retrieveGmNodes() throws Throwable
  {
    ArrayList results = new ArrayList();
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, GridNode.STATE, filter.getEqualOperator(),
        new Short(GridNode.STATE_GM), false);

      results.addAll(ServiceLookupHelper.getGridNodeManager().findGridNodesByFilter(filter));

      Collections.sort(results, _gmComparator);
    }
    catch (Throwable t)
    {
      Logger.warn("[ConnectDelegate.retrieveGmNodes] Error ", t);
      throw new Exception("Error retrieving GridMaster nodes!");
    }

    if (results.isEmpty())
      throw new Exception("No GridMaster nodes found! Connection cannot proceed.");

    return results;
  }

  /**
   * Retrieve the available JmsRouters.
   *
   * @return All available JmsRouters in the database.
   */
  protected Collection retrieveJmsRouters() throws Throwable
  {
    ArrayList results = new ArrayList();
    try
    {
      results.addAll(ServiceLookupHelper.getConnectionService().getJmsRouters(null));

      Collections.sort(results, _routerComparator);
    }
    catch (Throwable t)
    {
      Logger.warn("[ConnectDelegate.retrieveJmsRouters] Error ", t);
      throw new Exception("Error retrieving Jms Routers!");
    }

    if (results.isEmpty())
      throw new Exception("No Jms Routers found! Connection cannot proceed.");

    return results;
  }

  /**
   * Get the IP addresses of the JmsRouters.
   *
   * @param routers Collection of JmsRouters.
   * @return Array of IP addresses of the JmsRouters in that order.
   */
  protected String[] getRouterIPs(Collection routers)
  {
    Object[] routerArray = routers.toArray();
    String[] ips = new String[routerArray.length];
    for (int i=0; i<ips.length; i++)
    {
      ips[i] = ((JmsRouter)routerArray[i]).getIpAddress();
    }

    return ips;
  }

  /**
   * Get the GridNode IDs of the GridMaster nodes.
   *
   * @param gmNodes Collection of GridNodes.
   * @return Collection of GridNode IDs in String.
   */
  private Collection getGmNodeIDs(Collection gmNodes)
  {
    ArrayList nodeIDs = new ArrayList();
    for (Iterator i=gmNodes.iterator(); i.hasNext(); )
    {
      GridNode node = (GridNode)i.next();
      nodeIDs.add(node.getID());
    }

    return nodeIDs;
  }

  /**
   * Retrieve the GridMaster Channel.
   *
   * @param gmNodeIDs Collection of the GridNode IDs of the available GridMasters.
   * @return the GridMaster Channel. There should be only one GridMaster channel.
   */
  private ChannelInfo retrieveGmChannel(Collection gmNodeIDs) throws Throwable
  {
    Collection results = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addDomainFilter(null, ChannelInfo.REF_ID, gmNodeIDs, false);

      results = ServiceLookupHelper.getChannelManager().getChannelInfo(filter);
    }
    catch (Throwable t)
    {
      Logger.warn("[ConnectDelegate.retrieveGmNodes] Error ", t);
      throw new Exception("Error retrieving GridMaster Channel!");
    }

    if (results.isEmpty())
      throw new Exception("No GridMaster Channel found! Connection cannot proceed.");

    return (ChannelInfo)results.toArray()[0];
  }

  /**
   * Retrieve the Master Channel for this GridTalk.
   *
   * @return the ChannelInfo for this GridTalk's Master Channel.
   */
  protected ChannelInfo retrieveMasterChannel() throws Throwable
  {
    ChannelInfo channel = DelegateHelper.retrieveMasterChannel(_ctx.getMyNodeID());

    if (channel == null)
      throw new ConnectionException("No Master Channel found! Connection cannot proceed.");

    return channel;
  }

  /**
   * Construct the CommInfoFormatter for this GridTalk's CommInfo.
   *
   * @param myCommInfo this GridTalk's CommInfo
   * @param routerIPs The IPs of the Network Routers available.
   */
  protected void constructMyCommInfoFormatter(
    CommInfo myCommInfo, String[] routerIPs)
  {
    //construct my GridNodeCommInfo.
    _myCIFormatter = CommInfoFormatter.newMyGtCommInfoFormatter(
                       myCommInfo.getRefId(),
                       null,
                       new String[] {
                         _ctx.getConnectionSetupResult().getNetworkRouterUser(),
                         _ctx.getConnectionSetupResult().getNetworkRouterPassword()},
                       routerIPs,
                       new String[] {
                         _ctx.getConnectionSetupResult().getLocalRouterUser(),
                         _ctx.getConnectionSetupResult().getLocalRouterPassword()},
                       _ctx.getNetworkSetting().getConnectionLevel().intValue(),
                       _ctx.getNetworkSetting().getKeepAliveInterval().intValue(),
                       false,
                       null,
                       _ctx.getConnectionSetupResult().getNetworkRouterTopic(),
                       _ctx.getConnectionSetupResult().getLocalRouterTopic()
                       );
  }

  /**
   * Notify update of Utc Time.
   */
  private void notifyUtcTimeUpdate()
  {
    Properties props = new Properties();
    props.setProperty("utc", String.valueOf(_utcTime));
    props.setProperty("local", String.valueOf(System.currentTimeMillis()));

    TimeUpdateNotification notification =
      new TimeUpdateNotification(TimeUpdateNotification.UPDATE_UTC_TIME, props);

    DelegateHelper.broadcast(notification);
  }

}