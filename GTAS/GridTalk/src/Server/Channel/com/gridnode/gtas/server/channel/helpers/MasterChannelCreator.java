/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MasterChannelCreator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 2, 2004    Neo Sok Lay         Created
 * Jul 23,2009    Tam Wei Xiang       #560 Change from RSA X509Certificate to Java
 *                                    based.
 */
package com.gridnode.gtas.server.channel.helpers;

import com.gridnode.gtas.model.channel.ICommInfo;
import com.gridnode.gtas.model.channel.IPackagingInfo;
import com.gridnode.pdip.app.channel.model.*;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.base.transport.comminfo.JMSCommInfo;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
//import com.rsa.certj.cert.X509Certificate;

import java.security.cert.X509Certificate;
import java.util.Collection;

import java.io.File;

/**
 * Helper class to create MasterChannel.
 *
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class MasterChannelCreator
{
  private static final String CONFIG_NAME = "master.channel";

  private String _refId;
  private boolean _isPartner;
  private boolean _isGt1;
  private File _certFile;
  private Configuration _config;

  private ChannelInfo _masterChannel;
  private Certificate _masterCert;

  /**
   * Constructs a MasterChannelCreator
   *
   * @param refId The reference id for the master channel
   * @param isPartner The isPartner flag for the master channel
   * @param isGt1 <b>true</b> if the master channel is to be created for a GridTalk 1.x
   * @param certFile The certificate file for the master channel.
   */
  public MasterChannelCreator(String refId, boolean isPartner, boolean isGt1, File certFile)
  {
    _config = ConfigurationManager.getInstance().getConfig(
                             CONFIG_NAME);
    if (_config == null)
    {
      _config = new Configuration();
      //Logger.err("Default Be configuration file not found!");
    }
    _refId = refId;
    _isPartner = isPartner;
    _isGt1 = isGt1;
    _certFile = certFile;
  }

  /**
   * Creates/Get an instance of the master channel
   * @return An instance of the master channel.
   */
  public synchronized ChannelInfo getMasterChannel()
    throws Exception
  {
    if (_masterChannel == null)
    {
      String namePrefix = "GT.".concat(_refId).concat(".");
      CommInfo commInfo = newCommInfo(namePrefix);
      PackagingInfo pkgInfo = newPackagingInfo(namePrefix);
      SecurityInfo secInfo = newSecurityInfo(namePrefix);

      _masterChannel = newChannelInfo(namePrefix, commInfo, pkgInfo, secInfo);
    }

    return _masterChannel;
  }

  /**
   * Creates/Get an instance of the master certificate
   *
   * @return An instance of the master certificate created.
   * @throws Exception Error loading the certificate file.
   */
  public synchronized Certificate getMasterCertificate() throws Exception
  {
    if (_masterCert == null)
    {
      _masterCert = newMasterCertificate();
    }

    return _masterCert;
  }

  /**
   * Create an instance of a CommInfo. Some of the fields are populated based on "master.channel" configuration.
   *
   * @param namePrefix The prefix to use for the name of the CommInfo.
   * @return An instance of CommInfo created.
   */
  protected CommInfo newCommInfo(String namePrefix)
  {
    String name = namePrefix.concat(_config.getString("comm.name", "MCOMM"));
    String desc = _config.getString("comm.desc", "GridTalk Master Communication Profile");
    int port = _config.getInt("comm.port", 443);
    String commVersion = _isGt1 ? _config.getString("comm.version.old", "020000") : CommInfo.DEFAULT_TPTIMPL_VERSION;

    CommInfo commInfo = new CommInfo();
    commInfo.setCanDelete(false);
    commInfo.setDescription(desc);
    commInfo.setName(name);
    commInfo.setProtocolType(JMSCommInfo.JMS);
    commInfo.setTptImplVersion(commVersion);
    commInfo.setIsDefaultTpt(true);
    commInfo.setRefId(_refId);
    commInfo.setPartnerCategory(ICommInfo.CATEGORY_GRIDTALK);
    commInfo.setIsPartner(_isPartner);

    JMSCommInfo jmsInfo = new JMSCommInfo();
    jmsInfo.setDestination("");
    jmsInfo.setDestType(JMSCommInfo.TOPIC);
    jmsInfo.setHost("");
    jmsInfo.setPassword("");
    jmsInfo.setPort(port);
    jmsInfo.setUserName("");

    commInfo.setURL(jmsInfo.toURL());

    return commInfo;
  }

  /**
   * Create an instance of a PackagingInfo. Some of the fields are populated based on "master.channel" configuration.
   *
   * @param namePrefix The prefix to use for the name of the PackagingInfo.
   * @return An instance of PackagingInfo created.
   */
  protected PackagingInfo newPackagingInfo(String namePrefix)
  {
    String desc = _config.getString("pkg.desc", "GridTalk Master Packaging Profile");
    String name = namePrefix.concat(_config.getString("pkg.name", "MPKG"));

    PackagingInfo pkgInfo = new PackagingInfo();
    pkgInfo.setCanDelete(false);
    pkgInfo.setDescription(desc);
    pkgInfo.setEnvelope(PackagingInfo.DEFAULT_ENVELOPE_TYPE);
    pkgInfo.setName(name);
    pkgInfo.setReferenceId(_refId);
    pkgInfo.setIsPartner(_isPartner);
    pkgInfo.setPartnerCategory(IPackagingInfo.CATEGORY_GRIDTALK);

    return pkgInfo;
  }

  /**
   * Create an instance of a SecurityInfo. Some of the fields are populated based on "master.channel" configuration.
   * The encryption certificate is not set.
   *
   * @param namePrefix The prefix to use for the name of the SecurityInfo.
   * @return An instance of SecurityInfo created.
   */
  protected SecurityInfo newSecurityInfo(String namePrefix)
    throws Exception
  {
    String desc = _config.getString("sec.desc", "GridTalk Master Security Profile");
    String name = namePrefix.concat(_config.getString("sec.name", "MSEC"));

    SecurityInfo secInfo = new SecurityInfo();

    secInfo.setCanDelete(false);
    secInfo.setDescription(desc);
    secInfo.setEncryptionLevel(64);
    secInfo.setEncryptionType(SecurityInfo.ENCRYPTION_TYPE_ASYMMETRIC);
    secInfo.setName(name);
    secInfo.setReferenceId(_refId);

    //secInfo.setEncryptionCertificateID(encryptCert);

    if (_isPartner)
    {
      secInfo.setSignatureType(SecurityInfo.SIGNATURE_TYPE_DEFAULT);
      secInfo.setDigestAlgorithm(SecurityInfo.DIGEST_ALGORITHM_MD5);
      secInfo.setSignatureEncryptionCertificateID(new Long(getMyMasterCert().getUId()));
    }
    else
      secInfo.setSignatureType(SecurityInfo.SIGNATURE_TYPE_NONE);
    return secInfo;
  }

  /**
   * Create an instance of a ChannelInfo. Some of the fields are populated based on "master.channel" configuration.
   *
   * @param namePrefix The prefix to use for the name of the ChannelInfo.
   * @param commInfo The tptCommInfo for the channel
   * @param pkgInfo The packagingProfile for the channel
   * @param secInfo The securityProfile for the channel.
   * @return An instance of ChannelInfo created.
   */
  protected ChannelInfo newChannelInfo(String namePrefix, CommInfo commInfo, PackagingInfo pkgInfo, SecurityInfo secInfo)
  {
    String desc = _config.getString("channel.desc", "GridTalk Master Communication Channel");
    String name = namePrefix.concat(_config.getString("channel.name", "MC"));

    ChannelInfo channel = new ChannelInfo();

    channel.setCanDelete(false);
    channel.setDescription(desc);
    channel.setName(name);
    channel.setReferenceId(_refId);
    channel.setTptProtocolType(JMSCommInfo.JMS);
    channel.setIsMaster(true);
    channel.setIsPartner(_isPartner);

    FlowControlInfo flowCtrlInfo = new FlowControlInfo();
    flowCtrlInfo.setIsZip(true);
    flowCtrlInfo.setIsSplit(true);

    channel.setFlowControlInfo(flowCtrlInfo);
    channel.setTptCommInfo(commInfo);
    channel.setPackagingProfile(pkgInfo);
    channel.setSecurityProfile(secInfo);

    return channel;
  }

  /**
   * Create the Master Certificate instance for the GridTalk.
   *
   * @return the instantiated Certificate instance based on the certificate file.
   */
  protected Certificate newMasterCertificate() throws Exception
  {
    X509Certificate x509Cert = GridCertUtilities.loadX509Certificate(
                                  _certFile.getAbsolutePath());

    String name = _config.getString("cert.name", "GridTalk");
    String certificate = GridCertUtilities.writeCertificateToString(x509Cert);
    String serialNum = GridCertUtilities.writeByteArrayToString(x509Cert.getSerialNumber().toByteArray());
    String issuerName = GridCertUtilities.writeIssuerNameToString(x509Cert.getIssuerX500Principal());
    int id = new Integer(_refId).intValue();

    Certificate cert = new Certificate();

    cert.setID(id);
    cert.setCertificate(certificate);
    cert.setCertName(name);
    cert.setIssuerName(issuerName);
    cert.setSerialNumber(serialNum);
    cert.setMaster(true);
    cert.setPartner(_isPartner);

    return cert;
  }

  public static Certificate getMyMasterCert() throws Exception
  {
    ICertificateManagerObj mgr = (ICertificateManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
                                  ICertificateManagerHome.class.getName(),
                                  ICertificateManagerHome.class,
                                  new Object[]{});
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Certificate.IS_MASTER, filter.getEqualOperator(),
      Boolean.TRUE, false);
    filter.addSingleFilter(filter.getAndConnector(), Certificate.IS_PARTNER, filter.getEqualOperator(),
      Boolean.FALSE, false);
    filter.addSingleFilter(filter.getAndConnector(), Certificate.REVOKEID, filter.getEqualOperator(),
      new Integer(0), false);
    Collection results = mgr.getCertificate(filter);

    return (Certificate)((results==null||results.isEmpty())?null:results.iterator().next());
  }
}
