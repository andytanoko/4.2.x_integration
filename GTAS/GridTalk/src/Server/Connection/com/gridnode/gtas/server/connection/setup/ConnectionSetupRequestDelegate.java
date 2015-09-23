/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionSetupRequestDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 21 2002    Neo Sok Lay         Created
 * Jul 22 2003    Neo Sok Lay         Update the signature cert of sendChannel after
 *                                    successful setup to the cert returned by
 *                                    service router.
 * Dec 04 2003    Neo Sok Lay         Change in construction of JMSCommInfo.
 * Nov 15 2005    Neo Sok Lay         1. Only keep reference to _myChannel, get profiles from
 *                                       _myChannel. Re-retrieve the profiles just before 
 *                                       update.
 *                                    2. Use IssuerName and SerialNumber to retrieve new master cert
 * July 18 2009   Tam Wei Xiang       #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */
package com.gridnode.gtas.server.connection.setup;

import com.gridnode.gtas.server.connection.exceptions.ConnectionSetupException;
import com.gridnode.gtas.server.connection.helpers.CommInfoFormatter;
import com.gridnode.gtas.server.connection.helpers.JmsRouterEntityHandler;
import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.connection.model.JmsRouter;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerObj;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.base.transport.comminfo.JMSCommInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.io.File;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;

/**
 * This Delegate handles the Connection Setup request step.
 * This step will create a temporary connection for secure receiving of the
 * setup results returned from the GmPrime.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectionSetupRequestDelegate
  extends    AbstractConnectionSetupDelegate
{
  //NSL20051115 Only retain reference to _myChannel
  //private SecurityInfo  _mySecInfo;
  //private PackagingInfo _myPkgInfo;
  //private CommInfo      _myCommInfo;
  private ChannelInfo   _myChannel;
  private String[]      _receivedData;
  private File[]        _receivedFiles;

  public ConnectionSetupRequestDelegate(ConnectionSetupContext ctx)
  {
    super(ctx);
  }

  public void execute() throws Throwable
  {
    Logger.debug("[ConnectionSetupRequestDelegate.execute] Enter");

    registerDelegate();

    preCreateMasterChannel();

    connectToServicingRouter();
    sendSetupInfo();

    // sleep
    if (!_resultsReturned)
      _helper.sleep(_ctx.getResponseTimeout());

    // make sure must wait until receiveAck() finish processing.
    synchronized (_lock)
    {
      Logger.debug("[ConnectionSetupRequestDelegate.execute] Examine result success="+_success);

      // disconnect from temp connection
      DelegateHelper.disconnect((CommInfo)_ctx.getSecureCommInfo());

      storeSetupData();

      //check results
      if (!_success)
        throw new ConnectionSetupException(_failureReason);
    }
  }

  /**
   * Invoked when feedback is received for the setup-request message.
   *
   * @param success Whether the setup-request message was sent successfully.
   * @param message A feedback message description.
   */
  public void receiveFeedback(boolean success, String message) throws Throwable
  {
    Logger.debug("[ConnectionSetupRequestDelegate.receiveFeedback] Success="+success +
      ", Message="+message);

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
   * Invoked when acknowledgement is received for the setup request message.
   * @param dataPayload The data payload should contain, in order:<P><PRE>
   * 0) <ignore>
   * 1) Success/failure status, Boolean
   * 2) Error message from GMPrime, if not successful.
   * JMS Network Authentication Parameters for this GridTalk,
   *    3) JMS Network Topic
   *    4) JMS Network User
   *    6) JMS Network Password
   * GridMaster node 1,
   *    6) GridMaster Node Id
   *    7) GridMaster Topic
   *    8) GridMaster Description
   * GridMaster node 2 (optional),
   *    9) GridMaster Node Id
   *    10) GridMaster Topic
   *    11) GridMaster Description
   * GridMaster node 3 (optional),
   *    12) GridMaster Node Id
   *    13) GridMaster Topic
   *    14) GridMaster Description
   * JMS Router 1,
   *    15) JMS Router IP
   *    16) JMS Router Description
   * JMS Router 2 (optional),
   *    17) JMS Router IP
   *    18) JMS Router Description
   * JMS Router 3 (optional),
   *    19) JMS Router IP
   *    20) JMS Router Description</PRE>
   * @param filePayload The file payload should contain, if status indicates
   * success, the GridTalk's Certificate signed by GmPrime.
   */
  public void receiveAck(String[] dataPayload, File[] filePayload) throws Throwable
  {
    synchronized (_lock)
    {
      _resultsReturned = true;

      try
      {
        // collate the results
        Boolean status = Boolean.valueOf(dataPayload[1]);
        _success = status.booleanValue();
        _failureReason = (_success ? null : dataPayload[2]);

        Logger.debug("[ConnectionSetupRequestDelegate.receiveAck] Status="+status +
          " Failure Reason="+_failureReason);

        _receivedData = dataPayload;
        _receivedFiles = filePayload;
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

  private void storeSetupData()
  {
    try
    {
      Logger.debug("[ConnectionSetupRequestDelegate][storeSetupData][In storeSetupData() Begin]");
      if (_success)
      {
        if (_receivedFiles[0] != null)
          Logger.debug("[ConnectionSetupRequestDelegate][storeSetupData][Received File]"+_receivedFiles[0].getAbsolutePath());
        else
          Logger.debug("[ConnectionSetupRequestDelegate][storeSetupData][Received File is Null]");


        Long masterCert = createMasterCertificate(_receivedFiles[0]);

        updateMasterChannel(
          _receivedData[3], _receivedData[4], _receivedData[5], masterCert);

        clearExistingSetup();

        createGridMasterNode(_receivedData[6], _receivedData[8]);
        _ctx.getConnectionSetupResult().addGridMasterTopic(_receivedData[6], _receivedData[7]);
        if (_receivedData[9] != null)
        {
          createGridMasterNode(_receivedData[9], _receivedData[11]);
          _ctx.getConnectionSetupResult().addGridMasterTopic(_receivedData[9], _receivedData[10]);
        }
        if (_receivedData[12] != null)
        {
          createGridMasterNode(_receivedData[12], _receivedData[14]);
          _ctx.getConnectionSetupResult().addGridMasterTopic(_receivedData[12], _receivedData[13]);
        }
        createJmsRouter(_receivedData[15], _receivedData[16]);
        if (_receivedData[17] != null)
          createJmsRouter(_receivedData[17], _receivedData[18]);
        if (_receivedData[19] != null)
          createJmsRouter(_receivedData[19], _receivedData[20]);

        createGridMasterChannel(
          _receivedData[6], _receivedData[7], _receivedData[16],
          masterCert);

        _ctx.getConnectionSetupResult().setNetworkRouterTopic(_receivedData[3]);
        _ctx.getConnectionSetupResult().setNetworkRouterUser(_receivedData[4]);
        _ctx.getConnectionSetupResult().setNetworkRouterPassword(_receivedData[5]);
        generateLocalRouterPassword(_ctx.getGridNodeID());
        generateLocalRouterUser(_ctx.getGridNodeID());
        generateLocalRouterTopic(_ctx.getGridNodeID());

        //update SendChannel with new sign cert
        updateSendChannelSignCert(masterCert);
        _ctx.setSignCert(masterCert);
        Logger.debug("[ConnectionSetupRequestDelegate][storeSetupData][End storeSetupData()]");
      }
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      _success = false;
      _failureReason = t.getMessage();
    }
  }


  /**
   * Create a temporary connection to the setup service router.
   */
  private void connectToServicingRouter() throws Throwable
  {
    // construct comminfo to listen to public gt topic
    // this comminfo is only transient
    CommInfo commInfo = DelegateHelper.createCommInfo(
      "TMP", _ctx.getServicingRouter(),
      DelegateHelper.getProperties().getServicingRouterPort().intValue(),
      _ctx.getSecureTopic(), _ctx.getSecureUser(), _ctx.getSecurePassword(),
      "CON.SETUP.COMM.TMP",
      "Temporary Communication Profile for Connection Setup");

    // connect
    DelegateHelper.connect(commInfo);
    _ctx.setSecureCommInfo(commInfo);

    Logger.debug("[ConnectionSetupRequestDelegate.connectToServicingRouter] Connected.");
  }

  /**
   * Remove the existing setup: GridMaster nodes, Channels, and JmsRouters.
   */
  private void clearExistingSetup() throws Throwable
  {
    //retrieve GridMaster nodes
    Collection gmNodes = getGridMasterNodes();

    IGridNodeManagerObj mgr = ServiceLookupHelper.getGridNodeManager();
    for (Iterator i=gmNodes.iterator(); i.hasNext(); )
    {
      GridNode node = (GridNode)i.next();

      //delete GridMaster channel for each GridMaster node
      deleteChannel(node.getID());
      //delete each GridMaster node
      mgr.deleteGridNode((Long)node.getKey());
    }

    //delete existing JmsRouters
    deleteJmsRouters();

    //delete master channel
    //deleteChannel(_ctx.getGridNodeID().toString());

    _ctx.getConnectionSetupResult().getGridMasterTopics().clear();
    _ctx.getConnectionSetupResult().getAvailableGridMastersUIDs().clear();
    _ctx.getConnectionSetupResult().getAvailableRouterUIDs().clear();

  }

  /**
   * Pre-create the SecurityInfo for this GridTalk. Need to decrypt the
   * returned results from GmPrime.
   *
   * @param namePrefix Prefix for the Name of the SecurityInfo.
   * @param refId ReferenceId for the SecurityInfo
   * @param props Properties for Connection Setup.
   * @return The created SecurityInfo
   */
  private SecurityInfo preCreateSecurityInfo(
    String namePrefix, String refId, ConnectionSetupProperties props)
    throws Throwable
  {
    String name = namePrefix + props.getMasterChannelSecName();

    SecurityInfo _mySecInfo = DelegateHelper.retrieveSecurityInfo(refId, name);
    Long encryptCert = DelegateHelper.getMasterCertifcate(_ctx.getGridNodeID());
    if (_mySecInfo == null)
    {
      //  create securityinfo with refId, encryptCert
      _mySecInfo = DelegateHelper.createSecurityInfo(
                      DelegateHelper.createSecurityInfo(refId, encryptCert, null,
                      name,
                      props.getMasterChannelSecDesc()));
    }
    else
    {
      //update, make sure the info are correct
      DelegateHelper.updateSecurityInfo(_mySecInfo, encryptCert, null,
        props.getMasterChannelSecDesc());
      DelegateHelper.updateSecurityInfo(_mySecInfo);
    }
    return _mySecInfo;
  }

  /**
   * Pre-create the PackagingInfo for this GridTalk. Require this when
   * receive results from GmPrime.
   *
   * @param namePrefix Prefix for the Name of the PackagingInfo.
   * @param refId ReferenceID of the PackagingInfo.
   * @param props Properties for ConnectionSetup.
   * @return The created PackagingInfo
   */
  private PackagingInfo preCreatePackagingInfo(
    String namePrefix, String refId, ConnectionSetupProperties props)
    throws Throwable
  {
    String name = namePrefix + props.getMasterChannelPkgName();

    PackagingInfo _myPkgInfo = DelegateHelper.retrievePackagingInfo(refId, name);
    if (_myPkgInfo == null)
    {
      _myPkgInfo = DelegateHelper.createPackagingInfo(
                        DelegateHelper.createPackagingInfo(
                        refId,
                        name,
                        props.getMasterChannelPkgDesc()));
    }
    else
    {
      //update, make sure the info are correct
      DelegateHelper.updatePackagingInfo(_myPkgInfo, props.getMasterChannelPkgDesc());
      DelegateHelper.updatePackagingInfo(_myPkgInfo);
    }
    return _myPkgInfo;
  }

  /**
   * Retrieve CommInfo for the Master channel. Create one if does not exist.
   * If exist, update the properties.
   *
   * @param namePrefix Prefix for the Name of the CommInfo.
   * @param refId ReferenceId for the CommInfo
   * @param props Connection setup properties
   * @param topic The topic (Destination)
   * @param user The user
   * @param password The password
   * @return The created CommInfo
   */
  private CommInfo preCreateCommInfo(
    String namePrefix, String refId, ConnectionSetupProperties props)
    throws Throwable
  {
    String name = namePrefix + props.getMasterChannelCommName();

    CommInfo _myCommInfo = DelegateHelper.retrieveCommInfo(refId, name);
    if (_myCommInfo == null)
    {
      _myCommInfo = DelegateHelper.createCommInfo(
                       DelegateHelper.createCommInfo(refId,
                       "",
                       props.getMasterChannelCommPort().intValue(),
                       "", "", "",
                       name,
                       props.getMasterChannelCommDesc(),
                       CommInfoFormatter.CURRENT_COMMINFO_VERSION));
    }
    else
    {
      //update the commInfo with no settings
      DelegateHelper.updateCommInfo(_myCommInfo, "",
        props.getMasterChannelCommPort().intValue(),
        "", "", "",
        props.getMasterChannelCommDesc(),
        CommInfoFormatter.CURRENT_COMMINFO_VERSION);
      DelegateHelper.updateCommInfo(_myCommInfo);
    }
    return _myCommInfo;
  }

  /**
   * PreCreate the Master Channel for this GridTalk.
   */
  private void preCreateMasterChannel() throws Throwable
  {
    ConnectionSetupProperties props = DelegateHelper.getProperties();
    String refId = _ctx.getGridNodeID().toString();
    String namePrefix = "GT."+refId+".";

    //NSL20051115 Use return values from the preCreateXXX() methods
    CommInfo _myCommInfo = preCreateCommInfo(namePrefix, refId, props);
    SecurityInfo _mySecInfo = preCreateSecurityInfo(namePrefix, refId, props);
    PackagingInfo _myPkgInfo = preCreatePackagingInfo(namePrefix, refId, props);

    String channelName = namePrefix + props.getMasterChannelName();

    //  create channelinfo with refId, commInfo, securityinfo, packagingInfo
    _myChannel = DelegateHelper.retrieveChannel(refId, channelName);
    if (_myChannel == null)
    {
      _myChannel = DelegateHelper.createChannelInfo(
                  refId, channelName,
                  props.getMasterChannelDesc());
      _myChannel.setTptCommInfo(_myCommInfo);
      _myChannel.setPackagingProfile(_myPkgInfo);
      _myChannel.setSecurityProfile(_mySecInfo);

      _myChannel = DelegateHelper.createChannelInfo(_myChannel); //NSL20051115
    }
    else
    {
      //update the channel info with new profiles
      DelegateHelper.updateChannelInfo(_myChannel, props.getMasterChannelDesc(),
        _mySecInfo, _myPkgInfo,_myCommInfo);
      DelegateHelper.updateChannelInfo(_myChannel);
    }
  }

  /**
   * Create the Master Channel for this GridTalk.
   *
   * @parma topic The topic for the JmsCommInfo.
   * @param user the User for the JmsCommInfo.
   * @param password The Password for the JmsCommInfo.
   * @param encryptCert The UID of the Certificate to use for encryption.
   */
  private void updateMasterChannel(String topic,
    String user, String password, Long encryptCert) throws Throwable
  {
  	//NSL20051116 Ensure update is on up-to-date copy
  	CommInfo _myCommInfo = DelegateHelper.retrieveCommInfo(_myChannel.getTptCommInfo().getRefId(), _myChannel.getTptCommInfo().getName());
    //  update comminfo with refId, topic, routerIp
    //JMSCommInfo jmsInfo = new JMSCommInfo(_myCommInfo.getURL());
    JMSCommInfo jmsInfo = new JMSCommInfo();
    jmsInfo.setURL(_myCommInfo.getURL());
    jmsInfo.setDestination(topic);
    jmsInfo.setUserName(user);
    jmsInfo.setPassword(password);
    _myCommInfo.setURL(jmsInfo.toURL());
    DelegateHelper.updateCommInfo(_myCommInfo);

    //NSL20051116 Ensure update is on up-to-date copy
    SecurityInfo _mySecInfo = DelegateHelper.retrieveSecurityInfo(_myChannel.getSecurityProfile().getReferenceId(), _myChannel.getSecurityProfile().getName());
     // update securityinfo with new encryptCert
     _mySecInfo.setEncryptionCertificateID(encryptCert);
    DelegateHelper.updateSecurityInfo(_mySecInfo);
  }

  /**
   * Create a GridMaster node.
   *
   * @param nodeID the GridNodeID for the GridMaster.
   * @param desc The GridNode Name for the GridMaster.
   */
  private void createGridMasterNode(String nodeID, String desc) throws Throwable
  {
    //create
    GridNode gmNode = new GridNode();
    gmNode.setID(nodeID);
    gmNode.setName(desc);
    gmNode.setState(GridNode.STATE_GM);
    gmNode.setCategory(DelegateHelper.getProperties().getGmCategory());

    Long uid = ServiceLookupHelper.getGridNodeManager().createGridNode(gmNode);

    //add uid to gms
    _ctx.getConnectionSetupResult().getAvailableGridMastersUIDs().add(uid);
  }

  /**
   * Create a JmsRouter.
   *
   * @param name the Name for the JmsRouter.
   * @param ip The IP for the JmsRouter.
   */
  private void createJmsRouter(String name, String ip) throws Throwable
  {
    //create
    JmsRouter router = new JmsRouter();
    router.setIpAddress(ip);
    router.setName(name);
    router = (JmsRouter)JmsRouterEntityHandler.getInstance().createEntity(router);

    //add uid to routers
    _ctx.getConnectionSetupResult().getAvailableRouterUIDs().add(router.getKey());
  }

  /**
   * Create the Master Certificate for the GridTalk.
   *
   * @param certFile The file that contains the Certificate.
   * @return the UID of the created certificate.
   */
  private Long createMasterCertificate(File certFile) throws Throwable
  {
    Long certUID = null;

    X509Certificate cert = GridCertUtilities.loadX509Certificate(
                             certFile.getAbsolutePath());

    ICertificateManagerObj mgr = ServiceLookupHelper.getCertificateManager();
    //retrieve existing master cert
    Certificate existCert = mgr.findCertificateByIDAndName(
      _ctx.getGridNodeID().intValue(), _ctx.getMasterCertName());

    //revoke
    Logger.log(
      "[ConnectionSetupRequestDelegate.createMasterCertificate] Revoking cert "+
      existCert.getUId());
    mgr.revokeCertificateByUId((Long)existCert.getKey());

    //insert new cert
    mgr.insertCertificate(_ctx.getGridNodeID(), _ctx.getMasterCertName(),
      cert);

    /*NSL20051115 Somehow this method still returns the revoked cert... so alternative is to 
     * use issuername & serialnumber to retrieve -- guarantee to be unique
    Certificate newCert = mgr.findCertificateByIDAndName(
      _ctx.getGridNodeID().intValue(), _ctx.getMasterCertName());
      */
    String issuerName = GridCertUtilities.writeIssuerNameToString(cert.getIssuerX500Principal());
    String serialNum = GridCertUtilities.writeByteArrayToString(cert.getSerialNumber().toByteArray());
    Certificate newCert = mgr.findCertificateByIssureAndSerialNum(issuerName, serialNum);
    
    certUID = (Long)newCert.getKey();
    Logger.log("[ConnectionSetupRequestDelegate.createMasterCertificate] New cert UID="+certUID);
    //update private key
    mgr.updatePrivateKeyByCertificate(existCert.getPrivateKey(), newCert.getCertificate());

    //update IsMaster
    mgr.updateMasterAndPartnerByUId(certUID, true, false);

    return certUID;
  }

  /**
   * Create a Channel for sending messages to a GridMaster.
   *
   * @param refId the ReferenceId for the Channel.
   * @param topic The topic for the JMSCommInfo.
   * @param routerIp The Host for the JMSCommInfo.
   * @param signCert The UID of the Certificate to use to sign messages sending
   * to the GridMaster.
   */
  private void createGridMasterChannel(
    String refId, String topic, String routerIp, Long signCert)
    throws Throwable
  {
    ConnectionSetupProperties props = DelegateHelper.getProperties();

    //  create comminfo with refId, topic, routerIp
    CommInfo commInfo =
      DelegateHelper.createCommInfo(
         DelegateHelper.createCommInfo(refId,
         routerIp, props.getGmChannelCommPort().intValue(),
         topic, _ctx.getGridNodeID().toString(),
         props.getGmChannelCommPassword(),
         props.getGmChannelCommName(),
         props.getGmChannelCommDesc()));

    //  create packaginginfo with refId
    PackagingInfo pkgInfo =
      (PackagingInfo)DelegateHelper.createPackagingInfo(
      DelegateHelper.createPackagingInfo(refId,
      props.getGmChannelPkgName(),
      props.getGmChannelPkgDesc()));

    //  create securityinfo with refId, signCert, encryptCert=gmcert
    SecurityInfo secInfo =
      (SecurityInfo)DelegateHelper.createSecurityInfo(
      DelegateHelper.createSecurityInfo(refId, _ctx.getEncryptionCert(),
      signCert, props.getGmChannelSecName(),
      props.getGmChannelSecDesc()));

    //  create channelinfo with refId, commInfo, securityinfo, packagingInfo
    ChannelInfo channel =
      DelegateHelper.createChannelInfo(
      refId, props.getGmChannelName(),
      props.getGmChannelDesc());

    channel.setIsPartner(false);
    channel.setIsMaster(false);
    channel.setIsRelay(true);

    channel.setTptCommInfo(commInfo);
    channel.setPackagingProfile(pkgInfo);
    channel.setSecurityProfile(secInfo);
    channel = (ChannelInfo)DelegateHelper.createChannelInfo(channel);
  }

  private void registerDelegate() throws Throwable
  {
    _eventID = DelegateHelper.getProperties().getSetupEventId();

    String ackEventID = DelegateHelper.getProperties().getSetupAckEventId();
    _ctx.setDelegate(ackEventID, this);
    _ctx.setDelegate(_eventID, this);
  }

  /**
   * Send the setup information to GmPrime.
   */
  private void sendSetupInfo() throws Throwable
  {
    String[] dataPayload = new String[]
                           {
                             DelegateHelper.getProperties().getMsgFormatVersion(),
                             _ctx.getGridNodeID().toString(),
                             _ctx.getProductKey(),
                             _ctx.getCountryCode(),
                           };

    File[] filePayload = new File[]
                         {
                           _ctx.getCertRequest(),
                         };

    Logger.debug("[ConnectionSetupRequestDelegate.sendSetupInfo] Sending...");

    _helper.send(_eventID, dataPayload, filePayload);
  }

  /**
   * Retrieve all GridMaster nodes currently in the database.
   *
   * @return Collection of GridNode(s) which are GridMaster nodes.
   */
  private Collection getGridMasterNodes() throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, GridNode.STATE, filter.getEqualOperator(),
      new Short(GridNode.STATE_GM), false);

    return ServiceLookupHelper.getGridNodeManager().findGridNodesByFilter(filter);
  }

  /**
   * Delete Channels with the specified refId.
   * @param refId The ReferenceId of the ChannelInfo and related profiles.
   */
  private void deleteChannel(String refId) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.REF_ID, filter.getEqualOperator(),
      refId, false);
    //filter.addSingleFilter(filter.getAndConnector(), ChannelInfo.IS_MASTER,
    //  filter.getEqualOperator(), Boolean.TRUE, false);

    /**@todo to verify whether can just delete like that or don't delete */
    IChannelManagerObj mgr = ServiceLookupHelper.getChannelManager();
    Collection results = mgr.getChannelInfo(filter);
    for (Iterator i=results.iterator(); i.hasNext(); )
    {
      ChannelInfo channel = (ChannelInfo)i.next();
      mgr.deleteChannelInfo((Long)channel.getKey());
      mgr.deleteCommInfo((Long)channel.getTptCommInfo().getKey());
      mgr.deletePackigingInfo((Long)channel.getPackagingProfile().getKey());
      mgr.deleteSecurityInfo((Long)channel.getSecurityProfile().getKey());
    }
  }

  /**
   * Delete all JmsRouters in the database.
   */
  private void deleteJmsRouters() throws Throwable
  {
    JmsRouterEntityHandler.getInstance().removeByFilter(null);
  }

  /**
   * This method generates a local topic name for this GTS. It is just
   * a random string.
   *
   * IMPLEMENTATION NOTES: The GridNodeID is included in the topic name
   *   to help with debugging purposes (so later on, we know we are sending to
   *   the right topic).
   */
  private void generateLocalRouterTopic(Integer nodeID)
  {
    String topic = ( nodeID.toString() + "_topic_" + _helper.generateRandomString(25) );
    _ctx.getConnectionSetupResult().setLocalRouterTopic(topic);
  }

  /**
   * This method generates a local JMS username name for this GTS. It is just
   * a random string.
   *
   * IMPLEMENTATION NOTES: The GridNodeID is included in the user name
   *   to help with debugging purposes.
   */
  private void generateLocalRouterUser(Integer nodeID)
  {
    String user = ( nodeID.toString() + "_usr_" + _helper.generateRandomString(25) );
    _ctx.getConnectionSetupResult().setLocalRouterUser(user);
  }

  /**
   * This method generates a local JMS password for this GTS. It is just
   * a random string.
   *
   * IMPLEMENTATION NOTES: The GridNodeID is included in the password
   *   to help with debugging purposes.
   */
  private void generateLocalRouterPassword(Integer nodeID)
  {
    String password = ( nodeID.toString() + "_passwd_" + _helper.generateRandomString(25) );
    _ctx.getConnectionSetupResult().setLocalRouterPassword(password);
  }

  private void updateSendChannelSignCert(Long signCert) throws Throwable
  {
  	/*
    ChannelInfo sendChannel = _ctx.getSendChannel();
    sendChannel.getSecurityProfile().setSignatureEncryptionCertificateID(signCert);
    DelegateHelper.updateSecurityInfo(sendChannel.getSecurityProfile());
    */
  	//NSL20051116 Ensure update on up-to-date securityInfo
  	SecurityInfo secInfo = _ctx.getSendChannel().getSecurityProfile();
  	secInfo = DelegateHelper.retrieveSecurityInfo(secInfo.getReferenceId(), secInfo.getName());
  	secInfo.setSignatureEncryptionCertificateID(signCert);
  	DelegateHelper.updateSecurityInfo(secInfo);

  }
}