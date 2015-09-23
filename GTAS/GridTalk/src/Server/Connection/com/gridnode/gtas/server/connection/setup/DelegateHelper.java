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
 * Oct 23 2002    Neo Sok Lay         Created
 * Oct 01 2003    Neo Sok Lay         Pass fixed-size header array to ChannelManager when
 *                                    sending.
 * Jan 02 2004    Neo Sok Lay         Add FlowControlInfo to ChannelInfo.
 *                                    Remove Zip & split settings from PackagingInfo.
 */
package com.gridnode.gtas.server.connection.setup;

import com.gridnode.gtas.server.connection.helpers.IConnectionConfig;
import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.pdip.app.channel.helpers.ChannelSendHeader;
import com.gridnode.pdip.app.channel.model.*;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.base.transport.comminfo.JMSCommInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.io.File;
import java.util.Collection;
import java.util.Random;

/**
 * This is a helper class for the concrete Delegates.
 * Each Delegate should create an instance of the DelegateHelper for its own use.
 * Most of the methods are static which do not require state tracking except
 * the sleep() and wakeUp() methods which require synchronization and locking.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public class DelegateHelper
  implements IConnectionConfig
{
  private static final Object _lock = new Object();
  private static ConnectionSetupProperties _props = null;
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
    if (!_isNotified)
    {
      try
      {
        long duration = timeout * 60000L;
        _sleepingThread = Thread.currentThread();
        Logger.debug("[DelegateHelper.sleep] Waiting for results...");
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
   * Generates a random string of length 'len'.
   * For now, characters are only in hex range (ie: 0..9 and a..f )
   *
   * @param len The length of the random string.
   * @return the generated random string.
   */
  String generateRandomString(int len)
  {
    int number;
    String ret = "";

    for(int i=0; i<len; i++)
    {
      number = _randGen.nextInt(16);
      ret = ret.concat( Integer.toHexString(number) );
    }
    return ret;
  }

  /**
   * Send a message.
   *
   * @param eventID the Event ID of the message
   * @param dataPayload The data payload of the message
   * @param filePayload The file payload of the message.
   */
  void send(String eventID, String[] dataPayload, File[] filePayload)
    throws Throwable
  {
    if (getProperties().getIsTest())
    {
      Logger.log("[DelegateHelper.send] Testing only. No real sending initiated");
    }
    else
    {
      String transID = generateTransID();
      String fileID = null;
      if (filePayload != null)
        fileID = "connectionSetup_E"+eventID+"_T"+transID;

      ChannelInfo sendChannel = ConnectionSetupContext.getInstance().getSendChannel();
      Logger.debug("[DelegateHelper.send] SendChannel="+sendChannel);

      //031001NSL
      ChannelSendHeader header = new ChannelSendHeader(
                                     eventID, transID, null, fileID);
      header.setGridnodeHeaderInfo(
        ConnectionSetupContext.getInstance().getGridNodeID().toString(),
        sendChannel.getReferenceId(), null);

      // send via sendChannel
      ServiceLookupHelper.getChannelManager().send(
        sendChannel,
        dataPayload, filePayload,
        header.getHeaderArray());
        /*031001NSL
        new String[] {eventID, transID, null, fileID,
        ConnectionSetupContext.getInstance().getGridNodeID().toString(), //sender
        sendChannel.getReferenceId()}); //recipient
        */
    }
  }

  /**
   * Connect to the specified Jms router, and listen to the indicated topic.
   * @param commInfo The CommInfo indicating the Jms router to connect to and
   * the topic to listen to.
   */
  static void connect(CommInfo commInfo)
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
        commInfo, new String[] {"CONN.SETUP"});
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
      Logger.err("[DelegateHelper.disconnect] Error disconnecting.. ", t);
    }
  }

  /**
   * Get the Channel for sending messages to.
   *
   * @return The ChannelInfo for sending channel, or <b>null</b> if one cannot
   * be found.
   */
  static ChannelInfo getSendChannel()
  {
    ChannelInfo sendChannel = null;

    try
    {
      String refId = getProperties().getSendChannelRefId();

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ChannelInfo.REF_ID,
        filter.getEqualOperator(), refId, false);

      Collection results= ServiceLookupHelper.getChannelManager().getChannelInfo(filter);

      if (!results.isEmpty())
        sendChannel = (ChannelInfo)results.toArray()[0];
    }
    catch (Throwable ex)
    {
      Logger.err("[DelegateHelper.getSendChannel] Error: "+ex.getMessage());
    }
    return sendChannel;
  }

  /**
   * Create a CommInfo in the database.
   *
   * @param commInfo The CommInfo to be created.
   * @return The CommInfo created.
   */
  static CommInfo createCommInfo(CommInfo commInfo) throws Throwable
  {
    Long key = null;

    try
    {
      key = ServiceLookupHelper.getChannelManager().createCommInfo(
              commInfo);
    }
    catch (Throwable t)
    {
      Logger.err("[DelegateHelper.createCommInfo] Error ", t);
      throw new Exception("Unable to create CommInfo");
    }

    return ServiceLookupHelper.getChannelManager().getCommInfo(key);
  }

  /**
   * Update the CommInfo to the database.
   *
   * @param commInfo The CommInfo to be updated.
   */
  static void updateCommInfo(CommInfo commInfo) throws Throwable
  {
    try
    {
      ServiceLookupHelper.getChannelManager().updateCommInfo(commInfo);
    }
    catch (Throwable t)
    {
      Logger.err("[DelegateHelper.updateCommInfo] Error ", t);
      throw new Exception("Unable to update CommInfo");
    }
  }

  /**
   * Create a PackagingInfo in the database.
   *
   * @param pkgInfo The PackagingInfo to be created.
   * @return The PackagingInfo created.
   */
  static PackagingInfo createPackagingInfo(PackagingInfo pkgInfo) throws Throwable
  {
    Long key = null;

    try
    {
      key = ServiceLookupHelper.getChannelManager().createPackagingInfo(
                 pkgInfo);
    }
    catch (Throwable t)
    {
      Logger.err("[DelegateHelper.createPackagingInfo] Error ", t);
      throw new Exception("Unable to create PackagingInfo");
    }

    return (PackagingInfo)ServiceLookupHelper.getChannelManager().getPackagingInfo(key);
  }

  /**
   * Create a SecurityInfo in the database.
   *
   * @param secInfo The SecurityInfo to be created.
   * @return The SecurityInfo created.
   */
  static SecurityInfo createSecurityInfo(SecurityInfo secInfo) throws Throwable
  {
    Long key = null;

    try
    {
      key = ServiceLookupHelper.getChannelManager().createSecurityInfo(
              secInfo);
    }
    catch (Throwable t)
    {
      Logger.err("[DelegateHelper.createSecurityInfo] Error ", t);
      throw new Exception("Unable to create SecurityInfo");
    }

    return (SecurityInfo)ServiceLookupHelper.getChannelManager().getSecurityInfo(key);
  }

  /**
   * Update the SecurityInfo to the database.
   *
   * @param secInfo The SecurityInfo to be updated.
   */
  static void updateSecurityInfo(SecurityInfo secInfo) throws Throwable
  {
    try
    {
      ServiceLookupHelper.getChannelManager().updateSecurityInfo(secInfo);
    }
    catch (Throwable t)
    {
      Logger.err("[DelegateHelper.updateSecurityInfo] Error ", t);
      throw new Exception("Unable to update SecurityInfo");
    }
  }

  /**
   * Create a ChannelInfo in the database.
   *
   * @param channelInfo The ChannelInfo to be created.
   * @return the ChannelInfo created.
   */
  static ChannelInfo createChannelInfo(ChannelInfo channelInfo) throws Throwable
  {
    Long key = null;

    try
    {
      key = ServiceLookupHelper.getChannelManager().createChannelInfo(
              channelInfo);
    }
    catch (Throwable t)
    {
      Logger.err("[DelegateHelper.createChannelInfo] Error ", t);
      throw new Exception("Unable to create ChannelInfo");
    }

    return (ChannelInfo)ServiceLookupHelper.getChannelManager().getChannelInfo(key);
  }

  /**
   * Create a ChannelInfo entity instance with the specified attributes.
   *
   * @param refId The ReferenceId for the ChannelInfo.
   * @param name The Name for the ChannelInfo
   * @param desc The Description for the ChannelInfo.
   * @return The created ChannelInfo instance.
   */
  static ChannelInfo createChannelInfo(
    String refId, String name, String desc)
  {
    ChannelInfo channel = new ChannelInfo();

    channel.setCanDelete(false);
    channel.setDescription(desc);
    channel.setName(name);
    channel.setReferenceId(refId);
    channel.setTptProtocolType(JMSCommInfo.JMS);
    channel.setIsMaster(true);
    channel.setIsPartner(false);
    channel.setFlowControlInfo(getDefaultFlowControl());
    
    return channel;
  }

  /**
   * Get the default flow control settings for Master channel
   * 
   * @return The default flow control settings.
   */
  static FlowControlInfo getDefaultFlowControl()
  {
    FlowControlInfo flowCtrlInfo = new FlowControlInfo();
    updateFlowControl(flowCtrlInfo);
    return flowCtrlInfo;    
  }

  /**
   * Update the specified FlowControlInfo with the default flow control settings
   * for the master channel
   * 
   * @param flowCtrlInfo The FlowControlInfo to update
   */
  static void updateFlowControl(FlowControlInfo flowCtrlInfo)
  {
    flowCtrlInfo.setIsZip(true);
    flowCtrlInfo.setIsSplit(true);
  }
  
  /**
   * Update a ChannelInfo entity instance with the specified attributes.
   *
   * @param channel The ChannelInfo.
   * @param desc The Description for the ChannelInfo.
   * @param secInfo The SecurityInfo for the ChannelInfo
   * @param pkgInfo The PackagingInfo for the ChannelInfo
   * @param commInfo the TptCommInfo for the ChannelInfo.
   */
  static void updateChannelInfo(
    ChannelInfo channel, String desc, SecurityInfo secInfo,
      PackagingInfo pkgInfo, CommInfo commInfo)
  {
    channel.setCanDelete(false);
    channel.setDescription(desc);
    channel.setTptProtocolType(JMSCommInfo.JMS);
    channel.setIsMaster(true);
    channel.setIsPartner(false);
    channel.setSecurityProfile(secInfo);
    channel.setPackagingProfile(pkgInfo);
    channel.setTptCommInfo(commInfo);
    //ensure the flow control settings are correct
    if (channel.getFlowControlInfo() == null)
    {
      channel.setFlowControlInfo(getDefaultFlowControl());
    }
    else
    {
      updateFlowControl(channel.getFlowControlInfo());
    }
  }

  /* Update the ChannelInfo to the database.
   *
   * @param channelInfo The SecurityInfo to be updated.
   */
  static void updateChannelInfo(ChannelInfo channelInfo) throws Throwable
  {
    try
    {
      ServiceLookupHelper.getChannelManager().updateChannelInfo(channelInfo);
    }
    catch (Throwable t)
    {
      Logger.err("[DelegateHelper.updateChannelInfo] Error ", t);
      throw new Exception("Unable to update ChannelInfo");
    }
  }

  /**
   * Create a JMSCommInfo entity instance with the specified attributes.
   *
   * @param refId The RefId for the JMSCommInfo.
   * @param ip The Host for the JMSCommInfo.
   * @param port The Port for the JMSCommInfo.
   * @param topic The Destination for the JMSCommInfo.
   * @param user the User for the JMSCommInfo.
   * @param password The Password for the JMSCommInfo.
   * @param name The Name for the JMSCommInfo.
   * @param desc The Description for the JMSCommInfo.
   * @return the created JMSCommInfo instance.
   */
  static CommInfo createCommInfo(
    String refId, String ip, int port, String topic, String user,
    String password, String name, String desc)
    throws Throwable
  {
    return createCommInfo(refId, ip, port, topic, user, password,
             name, desc, getProperties().getCommInfoVersion());
  }

  /**
   * Create a CommInfo entity instance with the specified attributes.
   *
   * @param refId The RefId for the JMSCommInfo.
   * @param ip The Host for the JMSCommInfo.
   * @param port The Port for the JMSCommInfo.
   * @param topic The Destination for the JMSCommInfo.
   * @param user the User for the JMSCommInfo.
   * @param password The Password for the JMSCommInfo.
   * @param name The Name for the JMSCommInfo.
   * @param desc The Description for the JMSCommInfo.
   * @param commInfoVersion The CommInfo version.
   * @return the created CommInfo instance.
   */
  static CommInfo createCommInfo(
    String refId, String ip, int port, String topic, String user,
    String password, String name, String desc, String commInfoVersion)
    throws Throwable
  {
    JMSCommInfo jmsInfo = new JMSCommInfo();
    jmsInfo.setDestination(topic);
    jmsInfo.setDestType(JMSCommInfo.TOPIC);
    jmsInfo.setHost(ip);
    jmsInfo.setPassword(password);
    jmsInfo.setPort(port);
    jmsInfo.setUserName(user);

    CommInfo commInfo = new CommInfo();
    commInfo.setCanDelete(false);
    commInfo.setDescription(desc);
    commInfo.setName(name);
    commInfo.setProtocolType(JMSCommInfo.JMS);
    commInfo.setTptImplVersion(commInfoVersion);
    commInfo.setIsDefaultTpt(true);
    commInfo.setRefId(refId);
    commInfo.setURL(jmsInfo.toURL());

    return commInfo;
  }

  /**
   * Update a JMSCommInfo entity instance with the specified attributes.
   *
   * @param ip The Host for the JMSCommInfo.
   * @param port The Port for the JMSCommInfo.
   * @param topic The Destination for the JMSCommInfo.
   * @param user the User for the JMSCommInfo.
   * @param password The Password for the JMSCommInfo.
   * @param desc The Description for the JMSCommInfo.
   * @param commInfoVersion The CommInfo version.
   * @return the updated JMSCommInfo instance.
   */
  static void updateCommInfo(
    CommInfo commInfo,
    String ip, int port, String topic, String user,
    String password, String desc, String commInfoVersion)
    throws Throwable
  {
    //JMSCommInfo jmsInfo = new JMSCommInfo(commInfo.getURL());
    JMSCommInfo jmsInfo = new JMSCommInfo();
    jmsInfo.setURL(commInfo.getURL());
    jmsInfo.setDestination(topic);
    jmsInfo.setDestType(JMSCommInfo.TOPIC);
    jmsInfo.setHost(ip);
    jmsInfo.setPassword(password);
    jmsInfo.setPort(port);
    jmsInfo.setUserName(user);

    commInfo.setURL(jmsInfo.toURL());
    commInfo.setCanDelete(false);
    commInfo.setDescription(desc);
    commInfo.setIsDefaultTpt(true);
    commInfo.setProtocolType(JMSCommInfo.JMS);
    commInfo.setTptImplVersion(commInfoVersion);
  }

  /**
   * Create a PackagingInfo entity instance with the specified attributes.
   *
   * @param refId The ReferenceId for the PackagingInfo.
   * @param name the Name for the PackagingInfo.
   * @param desc the Description for the PackagingInfo.
   *
   * @return the create PackagingInfo instance.
   */
  static PackagingInfo createPackagingInfo(
    String refId, String name, String desc)
  {
    PackagingInfo pkgInfo = new PackagingInfo();
    pkgInfo.setCanDelete(false);
    pkgInfo.setDescription(desc);
    pkgInfo.setEnvelope(PackagingInfo.DEFAULT_ENVELOPE_TYPE);
    pkgInfo.setName(name);
    pkgInfo.setReferenceId(refId);
    //pkgInfo.setZip(true);

    return pkgInfo;
  }

  /**
   * Update a PackagingInfo entity instance with the specified attributes.
   *
   * @param pkgInfo The PackagingInfo to update.
   * @param desc the Description for the PackagingInfo.
   *
   * @return the updated PackagingInfo instance.
   */
  static void updatePackagingInfo(
    PackagingInfo pkgInfo, String desc)
  {
    pkgInfo.setCanDelete(false);
    pkgInfo.setDescription(desc);
    pkgInfo.setEnvelope(PackagingInfo.DEFAULT_ENVELOPE_TYPE);
    //pkgInfo.setZip(true);
  }

  /**
   * Update the PackagingInfo to the database.
   *
   * @param pkgInfo The PackagingInfo to be updated.
   */
  static void updatePackagingInfo(PackagingInfo pkgInfo) throws Throwable
  {
    try
    {
      ServiceLookupHelper.getChannelManager().updatePackagingInfo(pkgInfo);
    }
    catch (Throwable t)
    {
      Logger.err("[DelegateHelper.updatePackagingInfo] Error ", t);
      throw new Exception("Unable to update PackagingInfo");
    }
  }

  /**
   * Create a SecurityInfo entity instance with the specified attributes.
   *
   * @param refId The ReferenceId for the SecurityInfo.
   * @param encryptCert The UID of the Certificate to use for encryption.
   * @param signCert The UID of the Certificate to use for signing, may be <b>null</b>
   * if no signing required.
   * @param name the Name for the SecurityInfo.
   * @param desc The Description for the SecurityInfo.
   * @return the created SecurityInfo instance.
   */
  static SecurityInfo createSecurityInfo(
    String refId, Long encryptCert, Long signCert, String name, String desc)
  {
    SecurityInfo secInfo = new SecurityInfo();

    secInfo.setCanDelete(false);
    secInfo.setDescription(desc);
    secInfo.setEncryptionCertificateID(encryptCert);
    secInfo.setEncryptionLevel(64);
    secInfo.setEncryptionType(SecurityInfo.ENCRYPTION_TYPE_ASYMMETRIC);
    secInfo.setName(name);
    secInfo.setReferenceId(refId);
    if (signCert != null)
    {
      secInfo.setSignatureType(SecurityInfo.SIGNATURE_TYPE_DEFAULT);
      secInfo.setDigestAlgorithm(SecurityInfo.DIGEST_ALGORITHM_MD5);
      secInfo.setSignatureEncryptionCertificateID(signCert);
    }
    else
      secInfo.setSignatureType(SecurityInfo.SIGNATURE_TYPE_NONE);
    return secInfo;
  }

  /**
   * Update a SecurityInfo entity instance with the specified attributes.
   *
   * @param secInfo The SecurityInfo to update.
   * @param encryptCert The UID of the Certificate to use for encryption.
   * @param signCert The UID of the Certificate to use for signing, may be <b>null</b>
   * if no signing required.
   * @param desc The Description for the SecurityInfo.
   * @return the created SecurityInfo instance.
   */
  static void updateSecurityInfo(
    SecurityInfo secInfo, Long encryptCert, Long signCert, String desc)
  {
    secInfo.setCanDelete(false);
    secInfo.setDescription(desc);
    secInfo.setEncryptionCertificateID(encryptCert);
    secInfo.setEncryptionLevel(64);
    secInfo.setEncryptionType(SecurityInfo.ENCRYPTION_TYPE_ASYMMETRIC);
    if (signCert != null)
    {
      secInfo.setSignatureType(SecurityInfo.SIGNATURE_TYPE_DEFAULT);
      secInfo.setDigestAlgorithm(SecurityInfo.DIGEST_ALGORITHM_MD5);
      secInfo.setSignatureEncryptionCertificateID(signCert);
    }
    else
      secInfo.setSignatureType(SecurityInfo.SIGNATURE_TYPE_NONE);
  }

  /**
   * Retrieve a SecurityInfo with the specified data.
   *
   * @param refId ReferenceID of the SecurityInfo
   * @param secInfoName The Name of the SecurityInfo.
   *
   * @return The SecurityInfo retrieved, or <b>null</b> if one cannot
   * be found.
   */
  static SecurityInfo retrieveSecurityInfo(String refId, String secInfoName)
  {
    SecurityInfo secInfo = null;

    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, SecurityInfo.REF_ID,
        filter.getEqualOperator(), refId, false);
      filter.addSingleFilter(filter.getAndConnector(), SecurityInfo.NAME,
        filter.getEqualOperator(), secInfoName, false);

      Collection results= ServiceLookupHelper.getChannelManager().getSecurityInfo(
                              filter);

      if (!results.isEmpty())
        secInfo = (SecurityInfo)results.toArray()[0];
    }
    catch (Throwable ex)
    {
      Logger.err("[DelegateHelper.retrieveSecurityInfo] No SecurityInfo: "+ex.getMessage());
    }
    return secInfo;
  }

  /**
   * Retrieve a PackagingInfo with the specified data.
   *
   * @param refId ReferenceID of the PackagingInfo
   * @param pkgInfoName The Name of the PackagingInfo.
   *
   * @return The PackagingInfo retrieved, or <b>null</b> if one cannot
   * be found.
   */
  static PackagingInfo retrievePackagingInfo(String refId, String pkgInfoName)
  {
    PackagingInfo pkgInfo = null;

    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, PackagingInfo.REF_ID,
        filter.getEqualOperator(), refId, false);
      filter.addSingleFilter(filter.getAndConnector(), PackagingInfo.NAME,
        filter.getEqualOperator(), pkgInfoName, false);

      Collection results= ServiceLookupHelper.getChannelManager().getPackagingInfo(
                              filter);

      if (!results.isEmpty())
        pkgInfo = (PackagingInfo)results.toArray()[0];
    }
    catch (Throwable ex)
    {
      Logger.err("[DelegateHelper.retrievePackagingInfo] No PackagingInfo: "+ex.getMessage());
    }
    return pkgInfo;
  }

  /**
   * Retrieve a CommInfo with the specified data.
   *
   * @param refId ReferenceID of the CommInfo
   * @param commInfoName The Name of the CommInfo.
   *
   * @return The CommInfo retrieved, or <b>null</b> if one cannot
   * be found.
   */
  static CommInfo retrieveCommInfo(String refId, String commInfoName)
  {
    CommInfo commInfo = null;

    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, CommInfo.REF_ID,
        filter.getEqualOperator(), refId, false);
      filter.addSingleFilter(filter.getAndConnector(), CommInfo.NAME,
        filter.getEqualOperator(), commInfoName, false);

      Collection results= ServiceLookupHelper.getChannelManager().getCommInfo(
                              filter);

      if (!results.isEmpty())
        commInfo = (CommInfo)results.toArray()[0];
    }
    catch (Throwable ex)
    {
      Logger.err("[DelegateHelper.retrieveCommInfo] No CommInfo: "+ex.getMessage());
    }
    return commInfo;
  }

  /**
   * Retrieve a ChannelInfo with a particular reference id.
   *
   * @param refID The referenceID of the ChannelInfo.
   * @param channelName Name of the Channel
   * @return The channel retrieved, or <b>null</b> if no such channel found.
   */
  static ChannelInfo retrieveChannel(String refID, String channelName) throws Throwable
  {
    ChannelInfo channel = null;
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ChannelInfo.REF_ID,
        filter.getEqualOperator(), refID, false);
      filter.addSingleFilter(filter.getAndConnector(), ChannelInfo.NAME,
        filter.getEqualOperator(), channelName, false);

      Collection results = ServiceLookupHelper.getChannelManager().getChannelInfo(filter);

      if (!results.isEmpty())
        channel = (ChannelInfo)results.toArray()[0];
    }
    catch (Throwable t)
    {
      Logger.err("[DelegateHelper.retrieveChannel] No ChannelInfo ", t);
    }

    return channel;
  }


  static Long getMasterCertifcate(Integer nodeID) throws Throwable
  {
    Long uid = null;
    try
    {
      Certificate cert = ServiceLookupHelper.getCertificateManager().findCertificateByIDAndName(
                           nodeID.intValue(),
                           ConnectionSetupContext.getInstance().getMasterCertName());

      uid = (Long)cert.getKey();
    }
    catch (Exception ex)
    {
      Logger.debug("[DelegateHelper.getMasterCertificate] No master cert found: "+
        ex.getMessage());
      throw new Exception("No master certificate found!");
    }
    return uid;
  }

  /**
   * Loads the Connection Setup Properties.
   */
  private static void loadProperties()
  {
    try
    {
      _props = ConnectionSetupProperties.load();
    }
    catch (Throwable t)
    {
      Logger.err("[DelegateHelper.loadProperties] Error ", t);
      _props = ConnectionSetupProperties.getDefaultProperties();
    }
  }

  /**
   * Obtain the loaded ConnectionSetupProperties.
   *
   * @return The ConnectionSetupProperties.
   */
  static ConnectionSetupProperties getProperties() throws Throwable
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

}