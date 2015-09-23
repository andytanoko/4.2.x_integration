/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionPresetupDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 24 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.setup;

import java.io.File;

import com.gridnode.gtas.server.connection.exceptions.ConnectionSetupException;
import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;

/**
 * This Delegate handles the Connection Pre-Setup step.
 * This step will create a connection to the setup service router,
 * for initiating setup request with the GmPrime.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectionPresetupDelegate
  extends    AbstractConnectionSetupDelegate
{
  private String _sendChannelRefId;

  public ConnectionPresetupDelegate(ConnectionSetupContext ctx)
  {
    super(ctx);
  }

  public void execute() throws Throwable
  {
    Logger.debug("[ConnectionPresetupDelegate.execute] Enter");

    registerDelegate();
    retrieveSendChannel();
    connectToServicingRouter();
    sendSecureCommInfo();

    // sleep
    if (!_resultsReturned)
      _helper.sleep(_ctx.getResponseTimeout());

    // make sure must wait until receiveAck() finish processing.
    synchronized (_lock)
    {
      Logger.debug("[ConnectionPresetupDelegate.execute] Examine result success="+_success);
      //check results
      if (!_success)
        throw new ConnectionSetupException(_failureReason);
    }
  }

  /**
   * Invoked when acknowledgement for the pre-setup message is received.
   *
   * @param dataPayload The data payload should contain the following:<p>
   * <pre>
   * 0) ignore
   * 1) Success/Failure status, Boolean
   * 2) Failure reason
   * </pre>
   * @param filePayload None.
   */
  public void receiveAck(String[] dataPayload, File[] filePayload) throws Throwable
  {
    synchronized (_lock)
    {
      _resultsReturned = true;

      try
      {
        // element 0 is version number
        Boolean status = Boolean.valueOf(dataPayload[1]);
        _success = status.booleanValue();
        _failureReason = (_success ? null : dataPayload[2]);

        Logger.debug("[ConnectionPresetupDelegate.receiveAck] Status="+status +
          "Failure Reason="+_failureReason);
      }
      catch (Throwable t)
      {
        _success = false;
        _failureReason = t.getMessage();
        throw t;
      }
      finally
      {
        _helper.wakeUp();
      }
    }
  }

  /**
   * Invoked when feedback is received for the pre-setup message.
   *
   * @param success Whether the pre-setup message was sent successfully.
   * @param message A feedback message description.
   */
  public void receiveFeedback(boolean success, String message) throws Throwable
  {
    Logger.debug("[ConnectionPresetupDelegate.receiveFeedback] Success="+success
      + ", Message="+message);
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

  /**
   * Register this Delegate to receive the acknowledgement for Setup Auto Config
   * message.
   */
  private void registerDelegate() throws Throwable
  {
    ConnectionSetupProperties props = _helper.getProperties();
    _eventID = props.getPreSetupEventId();
    _sendChannelRefId = props.getSendChannelRefId();

    String ackEventID = props.getPreSetupAckEventId();
    _ctx.setDelegate(_eventID, this);
    _ctx.setDelegate(ackEventID, this);
  }

  /**
   * Retrieve the Channel to use for sending messages to GmPrime.
   * If Channel is not found, one will be created.
   */
  private void retrieveSendChannel() throws Throwable
  {
    ChannelInfo sendChannel = (ChannelInfo)_helper.getSendChannel();

    SecurityInfo secInfo = retrieveSecurityInfo();
    PackagingInfo pkgInfo = retrievePackagingInfo();
    CommInfo commInfo = retrieveCommInfo();

    if (sendChannel == null)
    {
      // create channel
      sendChannel = createSendChannel(secInfo, pkgInfo, commInfo);
    }
    else
    {
      sendChannel.setIsPartner(false);
      sendChannel.setIsMaster(false);
      //sendChannel.setPartnerCategory(IChannelInfo.CATEGORY_OTHERS);

      sendChannel.setTptCommInfo(commInfo);
      sendChannel.setPackagingProfile(pkgInfo);
      sendChannel.setSecurityProfile(secInfo);
      _helper.updateChannelInfo(sendChannel);
    }

    Logger.debug("[ConnectionPresetupDelegate.retrieveSendChannel] SetSendChannel="+sendChannel);
    _ctx.setSendChannel(sendChannel);
  }

  /**
   * Retrieve the SecurityInfo for the Send Channel. Create one if not exist
   *
   * @return the retrieved SecurityInfo.
   */
  private SecurityInfo retrieveSecurityInfo() throws Throwable
  {
    ConnectionSetupProperties props = _helper.getProperties();

    SecurityInfo secInfo = _helper.retrieveSecurityInfo(
                             _sendChannelRefId,
                             props.getSendChannelSecName());

    if (secInfo == null)
    {
      secInfo = (SecurityInfo)_helper.createSecurityInfo(
                  _helper.createSecurityInfo(
                    _sendChannelRefId, _ctx.getEncryptionCert(),
                    _ctx.getSignCert(),
                    props.getSendChannelSecName(),
                    props.getSendChannelSecDesc()
                    ));
    }
    else
    {
      _helper.updateSecurityInfo(secInfo, _ctx.getEncryptionCert(),
        _ctx.getSignCert(), props.getSendChannelSecDesc());
      _helper.updateSecurityInfo(secInfo);
    }

    return secInfo;
  }

  /**
   * Retrieve the PackagingInfo for the Send Channel. Create one if not exist
   *
   * @return the retrieved PackagingInfo.
   */
  private PackagingInfo retrievePackagingInfo() throws Throwable
  {
    ConnectionSetupProperties props = _helper.getProperties();

    PackagingInfo pkgInfo = _helper.retrievePackagingInfo(
                             _sendChannelRefId,
                             props.getSendChannelPkgName());

    if (pkgInfo == null)
    {
      pkgInfo = (PackagingInfo)_helper.createPackagingInfo(
                  _helper.createPackagingInfo(
                    _sendChannelRefId,
                    props.getSendChannelPkgName(),
                    props.getSendChannelPkgDesc()));
    }
    else
    {
      _helper.updatePackagingInfo(pkgInfo, props.getSendChannelPkgDesc());
      _helper.updatePackagingInfo(pkgInfo);
    }

    return pkgInfo;
  }

  /**
   * Retrieve the CommInfo for the Send Channel. Create one if not exist
   *
   * @return the retrieved CommInfo.
   */
  private CommInfo retrieveCommInfo() throws Throwable
  {
    ConnectionSetupProperties props = _helper.getProperties();

    CommInfo commInfo = _helper.retrieveCommInfo(
                             _sendChannelRefId,
                             props.getSendChannelCommName());

    if (commInfo == null)
    {
      commInfo = _helper.createCommInfo(
                   createCommInfo(props.getSetupSendTopic()));
    }
    else
    {
      _helper.updateCommInfo(
        commInfo, _ctx.getServicingRouter(),
        props.getServicingRouterPort().intValue(), props.getSetupSendTopic(),
        props.getSetupListenUser(), props.getSetupListenPassword(),
        props.getSendChannelCommDesc(),
        props.getCommInfoVersion());
      _helper.updateCommInfo(commInfo);
    }

    return commInfo;
  }

  /**
   * Create the Channel for sending messages to the GmPrime.
   */
  private ChannelInfo createSendChannel(
    SecurityInfo secInfo, PackagingInfo pkgInfo, CommInfo commInfo) throws Throwable
  {
    ConnectionSetupProperties props = _helper.getProperties();

    ChannelInfo channel =
      _helper.createChannelInfo(
      _sendChannelRefId,
      props.getSendChannelName(),
      props.getSendChannelDesc());

    channel.setIsPartner(false);
    channel.setIsMaster(false);
    //channel.setPartnerCategory(IChannelInfo.CATEGORY_OTHERS);

    channel.setTptCommInfo(commInfo);
    channel.setPackagingProfile(pkgInfo);
    channel.setSecurityProfile(secInfo);
    channel = (ChannelInfo)_helper.createChannelInfo(channel);

    return channel;
  }

  /**
   * Create a CommInfo instance for listening to the public gt topic.
   */
  private CommInfo createCommInfo(
    String topic) throws Throwable
  {
    ConnectionSetupProperties props = _helper.getProperties();

    return _helper.createCommInfo(
             _sendChannelRefId, _ctx.getServicingRouter(),
             props.getServicingRouterPort().intValue(),
             topic,
             props.getSetupListenUser(),
             props.getSetupListenPassword(),
             props.getSendChannelCommName(),
             props.getSendChannelCommDesc());
  }

  /**
   * Connect to the setup service Jms Router and listen to the public gt topic.
   */
  private void connectToServicingRouter() throws Throwable
  {
    // construct comminfo to listen to public gt topic
    CommInfo commInfo =
      createCommInfo(_helper.getProperties().getSetupListenTopic());

    // connect
    _helper.connect(commInfo);
    _ctx.setPublicCommInfo(commInfo);

    Logger.debug("[ConnectionPresetupDelegate.connectToServicingRouter] Connected.");
  }

  /**
   * Send the CommInfo for secure receive (in Request step) to the GmPrime.
   */
  private void sendSecureCommInfo() throws Throwable
  {
    Integer nodeID = _ctx.getGridNodeID();
    String topic = generateSecureTopic(nodeID);
    String user = generateSecureUser(nodeID);
    String password = generateSecurePassword(nodeID);

    String[] dataPayload = new String[]
                           {
                             _helper.getProperties().getMsgFormatVersion(),
                             nodeID.toString(),
                             _ctx.getProductKey(),
                             user,
                             password,
                             topic,
                           };

    Logger.debug("[ConnectionPresetupDelegate.sendSecureCommInfo] Sending...");
    _helper.send(_eventID, dataPayload, null);
  }

  /**
   * This method generates a local topic name for this GridTalk. It is just
   * a random string.
   *
   * IMPLEMENTATION NOTES: The GridNodeID is included in the topic name
   *   to help with debugging purposes (so later on, we know we are sending to
   *   the right topic).
   */
  private String generateSecureTopic(Integer nodeID)
  {
    String topic = ( nodeID.toString() + "_topic_" + _helper.generateRandomString(25) );
    _ctx.setSecureTopic(topic);
    return topic;
  }

  /**
   * This method generates a local JMS username name for this GridTalk. It is just
   * a random string.
   *
    * IMPLEMENTATION NOTES: The GridNodeID is included in the user name
   *   to help with debugging purposes.
   */
  private String generateSecureUser(Integer nodeID)
  {
    String user = ( nodeID.toString() + "_usr_" + _helper.generateRandomString(25) );
    _ctx.setSecureUser(user);
    return user;
  }

  /**
   * This method generates a local JMS password for this GridTalk. It is just
   * a random string.
   *
   * IMPLEMENTATION NOTES: The GridNodeID is included in the password
   *   to help with debugging purposes.
   */
  private String generateSecurePassword(Integer nodeID)
  {
    String password = ( nodeID.toString() + "_passwd_" + _helper.generateRandomString(25) );
    _ctx.setSecurePassword(password);
    return password;
  }

}