/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionTestHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 16 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.helpers;

import com.gridnode.pdip.base.transport.comminfo.JMSCommInfo;
import com.gridnode.gtas.server.notify.Notifier;
import com.gridnode.gtas.server.notify.ConnectionNotification;
import com.gridnode.gtas.server.activation.model.ActivationRecord;
import com.gridnode.gtas.server.activation.facade.ejb.IActivationManagerObj;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerObj;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.enterprise.facade.ejb.IEnterpriseHierarchyManagerHome;
import com.gridnode.gtas.server.enterprise.sync.models.SyncCertificate;
import com.gridnode.gtas.server.enterprise.model.SharedResource;
import com.gridnode.gtas.server.enterprise.facade.ejb.ISharedResourceManagerObj;
//import com.gridnode.gtas.model.channel.IJMSCommInfo;
import com.gridnode.gtas.server.enterprise.model.IResourceTypes;
import com.gridnode.gtas.server.enterprise.facade.ejb.IEnterpriseHierarchyManagerObj;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.model.PartnerGroup;
import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.user.facade.ejb.IUserManagerObj;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.app.user.facade.ejb.IUserManagerHome;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.WhitePage;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.helpers.SecurityDBHandler;
import com.gridnode.pdip.base.certificate.model.Certificate;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.PasswordMask;

import java.util.*;
import java.io.File;

import junit.framework.TestCase;

/**
 * This helper class provides helper methods for use in the Action Test cases
 * of the Activation module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */

public abstract class ActionTestHelper extends TestCase
{
  protected static final Long   DUMMY_UID      = new Long(-9999);
  protected static final String DUMMY_STRING   = "DUMMY";
  protected static final String ID             = "BE";
  protected static final String DESC           = "BE Description ";
  protected static final String UPD_DESC       = "Upd BE Description";

  protected static final String CHANNEL_NAME   = "CHANNEL_NAME";
  protected static final String CHANNEL_DESC   = "CHANNEL_DESC";
  protected static final String COMMINFO_NAME  = "COMMINFO_NAME";
  protected static final String COMMINFO_DESC  = "COMMINFO_DESC";
  protected static final String COMMINFO_NAME2 = "MC";
  protected static final String PKGINFO_NAME   = "PKGINFO_NAME";
  protected static final String PKGINFO_DESC   = "PKGINFO_DESC";
  protected static final String PKGINFO_NAME2  = "PKG";
  protected static final String SECINFO_NAME   = "SECINFO_NAME";
  protected static final String SECINFO_DESC   = "SECINFO_DESC";
  protected static final String SECINFO_NAME2  = "SEC";
  protected static final String CERT_NAME      = "GridTalk";

  protected static final String USER_ID   = "testuserid";
  protected static final String USER_NAME = "testusername";
  protected static final String PASSWORD  = "testpassword";
  protected static final String EMAIL     = "testemail@gridnode.com";
  protected static final String PHONE     = "testphone";
  protected static final String PROPERTY  = "testproperty";

  protected static final String PARTNER_NAME   = "PARTNER_NAME";
  protected static final String PARTNER_DESC   = "PARTNER_DESC";
  protected static final String PARTNER_ID     = "PARTNER_ID";
  protected static final String PARTNER_TYPE_DESC= "PARTNER_TYPE_DESC";
  protected static final String PARTNER_TYPE     = "PT";

  protected static final String DATA_PATH        = "Activation/data/";

  protected IUserManagerObj _userMgr;
  protected IEnterpriseHierarchyManagerObj _enterpriseMgr;
  protected IBizRegistryManagerObj _bizRegMgr;
  protected IChannelManagerObj _channelMgr;
  protected IPartnerManagerObj _partnerMgr;
  protected ISessionManagerObj _sessionMgr;
  protected ICertificateManagerObj _certMgr;
  protected IGridNodeManagerObj _gnMgr;
  protected IActivationManagerObj _actMgr;
  protected ISharedResourceManagerObj _sharedResMgr;

  protected ArrayList _openedSessions = new ArrayList();
  protected static final String APPLICATION = "gridtalk";
  protected static final String ENTERPRISE = "521";
  protected static final String PARTNER_ENT = "999999";
  protected static final String PARTNER_GN_NAME = "GridNode 999999";
  protected static final String GRIDNODE_NAME  = "AmpWay Inc.";
  protected static final String ACTIVATE_REASON  = "some reason for activation";

  private static final DataFilterImpl[] DELETE_FILTERS = new DataFilterImpl[2];

  static
  {
    //GridNode&ConnectionStatus
    DELETE_FILTERS[0] = new DataFilterImpl();
    //DELETE_FILTERS[0].addSingleFilter(null, GridNode.NAME,
    //  DELETE_FILTERS[0].getLikeOperator(), GRIDNODE_NAME, false);
    DELETE_FILTERS[0].addSingleFilter(DELETE_FILTERS[0].getOrConnector(),
      GridNode.NAME, DELETE_FILTERS[0].getLikeOperator(), PARTNER_GN_NAME, false);

  }

  protected String[]              _sessions;
  protected StateMachine[]        _sm;
  protected Long[]                _uIDs;
  protected BusinessEntity[]      _bizEntities;
  protected ChannelInfo[]         _channels;
  protected UserAccount[]         _users;
  protected Partner[]             _partners;

  public ActionTestHelper(String name)
  {
    super(name);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.setUp] Enter");

      _bizRegMgr = ServiceLookupHelper.getBizRegManager();
      _channelMgr = getChannelManager();
      _enterpriseMgr = getEnterpriseHierarchyManager();
      _userMgr = getUserManager();
      _partnerMgr = getPartnerManager();
      _sessionMgr = getSessionMgr();
      _certMgr = getCertManager();
      _gnMgr = ServiceLookupHelper.getGridNodeManager();
      _actMgr = ServiceLookupHelper.getActivationManager();
      _sharedResMgr = ServiceLookupHelper.getSharedResourceManager();
      cleanUp();
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.setUp] Exit");
    }
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[ActionTestHelper.tearDown] Enter");
    //cleanUp();
    Log.log("TEST", "[ActionTestHelper.tearDown] Exit");
  }

  protected void createSessions(int numSessions) throws Exception
  {
    _sessions = new String[numSessions];
    for (int i=0; i<numSessions; i++)
      _sessions[i] = openSession();
  }

  protected void createStateMachines(int numSM)
  {
    _sm = new StateMachine[numSM];
    for (int i=0; i<numSM; i++)
      _sm[i] = new StateMachine(null, null);
  }

  protected void createBes(int numBes, int numPartner) throws Exception
  {
    createBes(numBes, numPartner, null);
  }

  protected void createBes(int numBes, int numPartner, String partnerEnterpriseID)
    throws Exception
  {
    _bizEntities = new BusinessEntity[numBes];
    for (int i=0; i<numBes; i++)
    {
      if (i<numPartner)
      {
        createBe(partnerEnterpriseID, ID+i, DESC+i, true);
        _bizEntities[i] = findBizEntity(partnerEnterpriseID, ID+i);
      }
      else
      {
        createBe(ENTERPRISE, ID+i, DESC+i, false);
        _bizEntities[i] = findBizEntity(ENTERPRISE, ID+i);
      }
    }
  }

  protected Long[] getUIDs(IEntity[] entities)
  {
    Long[] uIDs = new Long[entities.length];
    for (int i=0; i<uIDs.length; i++)
      uIDs[i] = (Long)entities[i].getKey();

    return uIDs;
  }

  protected void createChannels(int numChannels, Long[] encCerts) throws Exception
  {
    _channels = new ChannelInfo[numChannels];
    for (int i=0; i<numChannels; i++)
    {
      _channels[i] = createChannel(CHANNEL_NAME+i, CHANNEL_DESC+i,
                       createCommInfo(COMMINFO_NAME+i, COMMINFO_DESC+i),
                       createPackagingInfo(PKGINFO_NAME+i, PKGINFO_DESC+i),
                       createSecurityInfo(SECINFO_NAME+i, SECINFO_DESC+i,
                          encCerts[i]));
    }
  }

  protected ChannelInfo createChannel(
    String name, String desc, CommInfo commInfo, PackagingInfo pkgInfo,
    SecurityInfo secInfo)
  {
    ChannelInfo channel = new ChannelInfo();

    channel.setDescription(desc);
    channel.setName(name);
    channel.setReferenceId("REF_ID");
    channel.setTptProtocolType(JMSCommInfo.JMS);
    channel.setTptCommInfo(commInfo);
    channel.setPackagingProfile(pkgInfo);
    channel.setSecurityProfile(secInfo);

    try
    {
      Long uID = _channelMgr.createChannelInfo(channel);
      channel = _channelMgr.getChannelInfo(uID);
    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ActionTestHelper.createChannel]", t);
      assertTrue("Error in createChannel", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createChannel] Exit");
    }

    return channel;
  }

  protected CommInfo createCommInfo(String name, String desc) throws Exception
  {
    JMSCommInfo jmsInfo = new JMSCommInfo();
    jmsInfo.setDestination("TOPIC");
    jmsInfo.setDestType(JMSCommInfo.TOPIC);
    jmsInfo.setHost("HOST");
    jmsInfo.setPassword("Password");
    jmsInfo.setPort(1234);
    jmsInfo.setUserName("USER_NAME");

    CommInfo commInfo = new CommInfo();
    commInfo.setCanDelete(false);
    commInfo.setDescription(desc);
    commInfo.setName(name);
    commInfo.setProtocolType(JMSCommInfo.JMS);
    commInfo.setTptImplVersion("TPT_IM");
    commInfo.setIsDefaultTpt(true);
    commInfo.setRefId("REF_ID");
    commInfo.setURL(jmsInfo.toURL());

    try
    {
      Long uID = _channelMgr.createCommInfo(commInfo);
      commInfo = _channelMgr.getCommInfo(uID);
      //System.out.println("Created commInfo="+commInfo);
    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ActionTestHelper.createCommInfo]", t);
      assertTrue("Error in createCommInfo", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createCommInfo] Exit");
    }
    return commInfo;

  }

  protected PackagingInfo createPackagingInfo(String name, String desc) throws Exception
  {
    PackagingInfo pkgInfo = new PackagingInfo();
    pkgInfo.setDescription(desc);
    pkgInfo.setEnvelope("None");
    pkgInfo.setName(name);
    pkgInfo.setZip(true);
    pkgInfo.setZipThreshold(50);

    try
    {
      Long uID = _channelMgr.createPackagingInfo(pkgInfo);
      pkgInfo = _channelMgr.getPackagingInfo(uID);
    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ActionTestHelper.createPackagingInfo]", t);
      assertTrue("Error in createPackagingInfo", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createPackagingInfo] Exit");
    }
    return pkgInfo;
  }

  protected SecurityInfo createSecurityInfo(String name, String desc,
    Long encryptionCertID) throws Exception
  {
    SecurityInfo secInfo = new SecurityInfo();
    secInfo.setDescription(desc);
    secInfo.setDigestAlgorithm("None");
    secInfo.setEncryptionLevel(64);
    secInfo.setEncryptionType("ET");
    secInfo.setName(name);
    secInfo.setEncryptionCertificateID(encryptionCertID);

    try
    {
      Long uID = _channelMgr.createSecurityInfo(secInfo);
      secInfo = _channelMgr.getSecurityInfo(uID);
    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ActionTestHelper.createSecurityInfo]", t);
      assertTrue("Error in createSecurityInfo", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createSecurityInfo] Exit");
    }
    return secInfo;
  }

  protected Long createCertificate(int id, String name, String filename)
    throws Exception
  {
    Long uid = null;
    try
    {
      _certMgr.insertCertificate(new Integer(id),
        name, new File(filename).getAbsolutePath());
      Certificate cert = _certMgr.findCertificateByIDAndName(id, name);
SyncCertificate syncCert = new SyncCertificate(cert);
syncCert.serialize("certFile"+id+".cer");

      uid = (Long)cert.getKey();
    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ActionTestHelper.createCertificate]", t);
      assertTrue("Error in createCertificate", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createCertificate] Exit");
    }

    return uid;
  }

  protected void createPartners(int numPartners) throws Exception
  {
    _partners = new Partner[numPartners];
    for (int i=0; i<numPartners; i++)
    {
      _partners[i] = createPartner(PARTNER_ID+i, PARTNER_NAME+i, PARTNER_DESC+i,
                       createPartnerType(PARTNER_TYPE+i, PARTNER_TYPE_DESC+i), null);
    }
  }

  protected Partner createPartner(
    String id, String name, String desc, PartnerType type, PartnerGroup grp)
  {
    Partner partner = new Partner();

    partner.setDescription(desc);
    partner.setName(name);
    partner.setPartnerGroup(grp);
    partner.setPartnerType(type);
    partner.setPartnerID(id);

    try
    {
      Long key = _partnerMgr.createPartner(partner);
      partner = _partnerMgr.findPartner(key);
    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ActionTestHelper.createPartner]", t);
      assertTrue("Error in createPartner", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createPartner] Exit");
    }

    return partner;
  }

  protected PartnerType createPartnerType(String name, String desc)
  {
    PartnerType partnerType = new PartnerType();

    partnerType.setDescription(desc);
    partnerType.setName(name);

    try
    {
      Long key = _partnerMgr.createPartnerType(partnerType);
      partnerType = _partnerMgr.findPartnerType(key);
    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ActionTestHelper.createPartnerType]", t);
      assertTrue("Error in createPartner", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createPartnerType] Exit");
    }

    return partnerType;
  }

  protected void createUsers(int numUsers)
  {
    _users    = new UserAccount[numUsers];

    for (int i=0; i<numUsers; i++)
    {
      createUser(USER_ID+i, USER_NAME+i);
      _users[i] = findUserAccountByUserId(USER_ID+i);
    }
  }

  protected void createUser(String id, String name)
  {
    createUser(id, name, PASSWORD, EMAIL, PHONE, PROPERTY);
  }

  protected void createUser(
    String id, String name, String password, String email, String phone,
    String property)
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.createUser] Enter");

      UserAccount acct = new UserAccount();
      acct.setId(id);
      acct.setPassword(new PasswordMask(password));
      acct.setEmail(email);
      acct.setPhone(phone);
      acct.setProperty(property);
      acct.setUserName(name);

      _userMgr.createUserAccount(acct);
    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ActionTestHelper.createUser]", t);
      assertTrue("Error in createUser", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createUser] Exit");
    }
  }

  public void testPerform() throws Exception
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.testPerform] Enter ");
      prepareTestData();

      unitTest();

      cleanTestData();
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.testPerform] Exit ");
    }

  }

  protected ISessionManagerObj getSessionMgr() throws Exception
  {
    ISessionManagerHome sessionHome = (ISessionManagerHome)ServiceLocator.instance(
                                         ServiceLocator.CLIENT_CONTEXT).getHome(
                                         ISessionManagerHome.class.getName(),
                                         ISessionManagerHome.class);
    return sessionHome.create();
  }

  protected ICertificateManagerObj getCertManager() throws Exception
  {
    ICertificateManagerHome certHome =
      (ICertificateManagerHome)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getHome(
      ICertificateManagerHome.class.getName(),
      ICertificateManagerHome.class);
    return certHome.create();
  }

  protected IChannelManagerObj getChannelManager() throws Exception
  {
    IChannelManagerHome home =
      (IChannelManagerHome)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getHome(
      IChannelManagerHome.class.getName(),
      IChannelManagerHome.class);
    return home.create();
  }

  protected IUserManagerObj getUserManager() throws Exception
  {
    IUserManagerHome home =
      (IUserManagerHome)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getHome(
      IUserManagerHome.class.getName(),
      IUserManagerHome.class);
    return home.create();
  }

  protected IPartnerManagerObj getPartnerManager() throws Exception
  {
    IPartnerManagerHome home =
      (IPartnerManagerHome)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getHome(
      IPartnerManagerHome.class.getName(),
      IPartnerManagerHome.class);
    return home.create();
  }

  protected IEnterpriseHierarchyManagerObj getEnterpriseHierarchyManager() throws Exception
  {
    IEnterpriseHierarchyManagerHome home =
      (IEnterpriseHierarchyManagerHome)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getHome(
      IEnterpriseHierarchyManagerHome.class.getName(),
      IEnterpriseHierarchyManagerHome.class);
    return home.create();
  }

  protected HashMap getWhitePageData()
  {
    HashMap whitePage = new HashMap();
    whitePage.put(WhitePage.BUSINESS_DESC, "Business Description");
    whitePage.put(WhitePage.DUNS,"DUNS");
    whitePage.put(WhitePage.G_SUPPLY_CHAIN_CODE, "GSCC");
    whitePage.put(WhitePage.CONTACT_PERSON,"Contact Person");
    whitePage.put(WhitePage.EMAIL,"email@email.com");
    whitePage.put(WhitePage.TEL, "12234444");
    whitePage.put(WhitePage.FAX, "232434324");
    whitePage.put(WhitePage.WEBSITE, "http://abc@email.com");
    whitePage.put(WhitePage.ADDRESS, "some address");
    whitePage.put(WhitePage.CITY, "some city");
    whitePage.put(WhitePage.STATE, "STATE");
    whitePage.put(WhitePage.ZIP_CODE, "232444");
    whitePage.put(WhitePage.PO_BOX, "124434234");
    whitePage.put(WhitePage.COUNTRY, "ABC");
    whitePage.put(WhitePage.LANGUAGE, "XYZ");

    return whitePage;
  }

  protected HashMap getUpdWhitePageData(Long beUID)
  {
    HashMap whitePage = new HashMap();
    whitePage.put(WhitePage.BUSINESS_DESC, "Upd Business Description");
    whitePage.put(WhitePage.DUNS,"UPDDUNS");
    whitePage.put(WhitePage.G_SUPPLY_CHAIN_CODE, "UPDGSCC");
    whitePage.put(WhitePage.CONTACT_PERSON,"UPDContact Person");
    whitePage.put(WhitePage.EMAIL,"updemail@email.com");
    whitePage.put(WhitePage.TEL, "12234444");
    whitePage.put(WhitePage.FAX, "232434324");
    whitePage.put(WhitePage.WEBSITE, "http://abc@email.upd.com");
    whitePage.put(WhitePage.ADDRESS, "upd some address");
    whitePage.put(WhitePage.CITY, "upd some city");
    whitePage.put(WhitePage.STATE, "USTATE");
    whitePage.put(WhitePage.ZIP_CODE, "232444");
    whitePage.put(WhitePage.PO_BOX, "124434234");
    whitePage.put(WhitePage.COUNTRY, "UPD");
    whitePage.put(WhitePage.LANGUAGE, "UPD");
    whitePage.put(WhitePage.BE_UID, beUID);

    return whitePage;
  }

  protected void cleanUpBEs(String enterprise)
  {
    try
    {
      undoBeCannotDelete(enterprise);
      _bizRegMgr.markDeleteBusinessEntities(enterprise);
      _bizRegMgr.purgeDeletedBusinessEntities(enterprise);
    }
    catch (Exception ex)
    {
    }
  }

  protected void undoBeCannotDelete(String enterprise)
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, BusinessEntity.ENTERPRISE_ID,
      filter.getEqualOperator(), enterprise, false);
    filter.addSingleFilter(filter.getAndConnector(),
      BusinessEntity.CAN_DELETE, filter.getEqualOperator(), Boolean.FALSE, false);

    Collection bes = findBizEntitiesByFilter(filter);
    for (Iterator i=bes.iterator(); i.hasNext();)
    {
      try
      {
          BusinessEntity be = (BusinessEntity)i.next();
          be.setCanDelete(true);
          _bizRegMgr.updateBusinessEntity(be);
      }
      catch (Exception ex)
      {

      }
    }
  }

  protected void cleanUpChannels()
  {
    try
    {
      deleteChannels();
      deleteCommInfos();
      deletePackagingInfos();
      deleteSecurityInfos();
    }
    catch (Exception ex)
    {

    }

  }

  protected void deleteChannels() throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.NAME, filter.getLocateOperator(),
      CHANNEL_NAME, false);

    Collection channelUIDs =_channelMgr.getChannelInfoUIDs(filter);
    for (Iterator i=channelUIDs.iterator(); i.hasNext(); )
    {
      _channelMgr.deleteChannelInfo((Long)i.next());
    }
  }

  protected void deleteCommInfos() throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, CommInfo.NAME, filter.getLocateOperator(),
      COMMINFO_NAME, false);
    filter.addSingleFilter(filter.getOrConnector(), CommInfo.NAME,
      filter.getLocateOperator(), COMMINFO_NAME2, false);

    Collection commInfoUIDs =_channelMgr.getCommInfoUIDs(filter);
    for (Iterator i=commInfoUIDs.iterator(); i.hasNext(); )
    {
      _channelMgr.deleteCommInfo((Long)i.next());
    }
  }

  protected void deletePackagingInfos() throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, PackagingInfo.NAME, filter.getLocateOperator(),
      PKGINFO_NAME, false);
    filter.addSingleFilter(filter.getOrConnector(), PackagingInfo.NAME,
      filter.getLocateOperator(), PKGINFO_NAME2, false);

    Collection pkgInfoUIDs =_channelMgr.getPackagingInfoUIDs(filter);
    for (Iterator i=pkgInfoUIDs.iterator(); i.hasNext(); )
    {
      _channelMgr.deletePackigingInfo((Long)i.next());
    }
  }

  protected void deleteSecurityInfos() throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, SecurityInfo.NAME, filter.getLocateOperator(),
      SECINFO_NAME, false);
    filter.addSingleFilter(filter.getOrConnector(), SecurityInfo.NAME,
      filter.getLocateOperator(), SECINFO_NAME2, false);

    Collection secInfoUIDs =_channelMgr.getSecurityInfoUIDs(filter);
    for (Iterator i=secInfoUIDs.iterator(); i.hasNext(); )
    {
      _channelMgr.deleteSecurityInfo((Long)i.next());
    }
  }

  protected void cleanUpCertificates(Long[] certUIDs)
  {
    if (certUIDs == null) return;
    for (int i=0; i<certUIDs.length; i++)
    {
      try
      {
          deleteCertificate(certUIDs[i]);
      }
      catch (Throwable t)
      {
      }
    }
  }

  protected void cleanUpGridTalkCertificates(String[] ids)
  {
    for (int i=0; i<ids.length; i++)
    {
      try
      {
          _certMgr.deleteCertificate(
            new Integer(ids[i]).intValue(), "GridTalk");
      }
      catch (Throwable t)
      {
      }
    }
  }

  protected void deleteCertificate(Long uid) throws Exception
  {
    Certificate cert = _certMgr.findCertificateByUID(uid);
    _certMgr.deleteCertificate(
      cert.getID(), cert.getCertName());
  }

  protected void cleanUpUsers()
  {
    try
    {
      deleteUsers();
    }
    catch (Exception ex)
    {

    }
  }
  protected void deleteUsers() throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, UserAccount.ID, filter.getLikeOperator(),
      USER_ID, false);

    Collection userUIDs =findUserAccountKeys(filter);
    for (Iterator i=userUIDs.iterator(); i.hasNext(); )
    {
      _userMgr.deleteUserAccount((Long)i.next(), false);
    }
  }

  protected void cleanUpSharedBes()
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, SharedResource.RESOURCE_TYPE,
        filter.getEqualOperator(), IResourceTypes.BUSINESS_ENTITY, false);

      _sharedResMgr.removeSharedResourcesByFilter(filter);
      _sharedResMgr.purgeDeletedSharedResources(IResourceTypes.BUSINESS_ENTITY);
    }
    catch (Exception ex)
    {

    }
    finally
    {

    }

  }

  protected void cleanUpBeChannelLinks()
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, BusinessEntity.ID, filter.getLikeOperator(),
        ID, false);

      Collection uIDs = _bizRegMgr.findBusinessEntitiesKeys(filter);
      ArrayList emptyList = new ArrayList();
      for (Iterator i=uIDs.iterator(); i.hasNext(); )
      {
        _enterpriseMgr.setChannelsForBizEntity((Long)i.next(), emptyList);
      }
    }
    catch (Exception ex)
    {

    }
    finally
    {

    }

  }

  protected void cleanUpBeUserLinks()
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, UserAccount.ID, filter.getLikeOperator(),
        USER_ID, false);

      Collection uIDs = findUserAccountKeys(filter);
      ArrayList emptyList = new ArrayList();
      for (Iterator i=uIDs.iterator(); i.hasNext(); )
      {
        _enterpriseMgr.setBizEntitiesForUser((Long)i.next(), emptyList);
      }
    }
    catch (Exception ex)
    {

    }
    finally
    {

    }

  }

  protected void cleanUpPartners()
  {
    try
    {
      deletePartners();
      deletePartnerTypes();
    }
    catch (Exception ex)
    {

    }
  }
  protected void deletePartners() throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, Partner.PARTNER_ID, filter.getLikeOperator(),
      PARTNER_ID, false);

    Collection partners = _partnerMgr.findPartner(filter);
    for (Iterator i=partners.iterator(); i.hasNext(); )
    {
      Partner partner = (Partner)i.next();
      _partnerMgr.deletePartner((Long)partner.getKey(), false);
    }
  }

  protected void deletePartnerTypes() throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, PartnerType.NAME, filter.getLikeOperator(),
      PARTNER_TYPE, false);

    Collection partnerTypes = _partnerMgr.findPartnerType(filter);
    for (Iterator i=partnerTypes.iterator(); i.hasNext(); )
    {
      PartnerType partnerType = (PartnerType)i.next();
      _partnerMgr.deletePartnerType((Long)partnerType.getKey());
    }
  }

  protected void cleanUpPartnerLinks()
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, Partner.PARTNER_ID, filter.getLikeOperator(),
        PARTNER_ID, false);

      Collection partners = _partnerMgr.findPartner(filter);
      for (Iterator i=partners.iterator(); i.hasNext(); )
      {
        Partner partner = (Partner)i.next();
        Long key = (Long)partner.getKey();
        _enterpriseMgr.setChannelForPartner(key, null);
        _enterpriseMgr.setBizEntityForPartner(key, null);
      }
    }
    catch (Exception ex)
    {

    }
    finally
    {

    }

  }

  protected Long createBe(String enterpriseId, String beId, String description, boolean isPartner)
  {
    return createBe(enterpriseId, beId, description, isPartner, true);
  }

  protected Long createBe(String enterpriseId, String beId, String description, boolean isPartner,
    boolean canDelete)
  {
    WhitePage wp = new WhitePage();
    ActionHelper.copyEntityFields(getWhitePageData(), wp);
    return createBe(enterpriseId, beId, description, isPartner, wp, canDelete);
  }

  protected Long createBe(
    String enterpriseId, String beId, String description, boolean isPartner,
    WhitePage whitePage, boolean canDelete)
  {
    Long uId = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.createBe] Enter");

      BusinessEntity be = new BusinessEntity();
      be.setBusEntId(beId);
      be.setDescription(description);
      be.setEnterpriseId(enterpriseId);
      be.setPartner(isPartner);
      be.setWhitePage(whitePage);
      be.setCanDelete(canDelete);

      uId = createToDb(be);
    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ActionTestHelper.createBe]", t);
      assertTrue("Error in createBe", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createBe] Exit");
    }
    return uId;
  }


  protected Long createToDb(BusinessEntity bizEntity) throws Throwable
  {
    Long uID = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.createToDb] Enter");

      uID = _bizRegMgr.createBusinessEntity(bizEntity);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createToDb] Exit");
    }
    return uID;
  }

  protected void updateToDb(BusinessEntity bizEntity) throws Throwable
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.updateToDb] Enter");
      _bizRegMgr.updateBusinessEntity(bizEntity);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.updateToDb] Exit");
    }
  }

  protected BusinessEntity findBizEntityByUId(Long uId)
  {
    BusinessEntity bizEntity = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findBizEntityByUId] Enter");

      bizEntity = _bizRegMgr.findBusinessEntity(uId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findBizEntityByUId]", ex);
      assertTrue("Error in findBizEntityByUId", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findBizEntityByUId] Exit");
    }
    return bizEntity;
  }

  protected BusinessEntity findBizEntity(String enterpriseId, String beId)
  {
    BusinessEntity bizEntity = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findBizEntity] Enter: enterpriseID="+enterpriseId);

      bizEntity = _bizRegMgr.findBusinessEntity(enterpriseId, beId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findBizEntity]", ex);
      assertTrue("Error in _bizRegMgr.findBusinessEntity", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findBizEntity] Exit");
    }
    return bizEntity;
  }

  protected Collection findBizEntitiesByFilter(IDataFilter filter)
  {
    Collection results = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findBizEntitiesByFilter] Enter");

      results = _bizRegMgr.findBusinessEntities(filter);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findBizEntitiesByFilter]", ex);
      assertTrue("Error in _bizRegMgr.findBusinessEntities", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findBizEntitiesByFilter] Exit");
    }
    return results;
  }
/*
  protected Collection findBusinessEntities(Collection beUIDs)
  {
    Collection result = null;
    try
    {
      if (beUIDs.isEmpty())
        result = beUIDs;
      else
        result = ActionHelper.getBusinessEntities(beUIDs);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findBusinessEntities] Error", ex);
      assertTrue("unable to findBusinessEntities", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.findBusinessEntities] Exit ");
    }
    return result;
  }

  protected Collection getChannelsForBizEntity(Long beUID)
  {
    Collection result = null;
    try
    {
      result = _enterpriseMgr.getChannelsForBizEntity(beUID);
      if (!result.isEmpty())
        result = ActionHelper.getChannels(result);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.getChannelsForBizEntity] Error", ex);
      assertTrue("unable to getChannelsForBizEntity", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.getChannelsForBizEntity] Exit ");
    }

    return result;

  }

  protected Collection getChannels(Collection channelUIDs)
  {
    Collection result = null;
    try
    {
      if (channelUIDs.isEmpty())
        result = channelUIDs;
      else
        result = ActionHelper.getChannels(channelUIDs);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.getChannels] Error", ex);
      assertTrue("unable to getChannels", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.getChannels] Exit ");
    }
    return result;
  }
*/
  protected ChannelInfo getChannelByUID(Long channelUID)
  {
    ChannelInfo result = null;
    try
    {
      result = _channelMgr.getChannelInfo(channelUID);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.getChannelByUID] Error", ex);
      assertTrue("unable to getChannelByUID", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.getChannelByUID] Exit ");
    }

    return result;
  }

  protected void assignChannelsToBe(Collection channelUIDs, Long beUID)
  {
    try
    {
      _enterpriseMgr.setChannelsForBizEntity(beUID, channelUIDs);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.assignChannelsToBe] Error", ex);
      assertTrue("unable to assignChannelsToBe", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.assignChannelsToBe] Exit ");
    }

  }

  protected void assignBeToPartner(
    Long partnerUID, Long beUID)
  {
    try
    {
      _enterpriseMgr.setBizEntityForPartner(partnerUID, beUID);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.assignBeToPartner] Error", ex);
      assertTrue("unable to assignBeToPartner", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.assignBeToPartner] Exit ");
    }
  }

  protected void assignChannelToPartner(
    Long partnerUID, Long channelUID)
  {
    try
    {
      _enterpriseMgr.setChannelForPartner(partnerUID, channelUID);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.assignChannelToPartner] Error", ex);
      assertTrue("unable to assignChannelToPartner", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.assignChannelToPartner] Exit ");
    }
  }

  protected void assignBesToUser(Collection beUIDs, Long userUID)
  {
    try
    {
      _enterpriseMgr.setBizEntitiesForUser(userUID, beUIDs);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.assignBesToUser] Error", ex);
      assertTrue("unable to assignBesToUser", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.assignBesToUser] Exit ");
    }
  }

  protected UserAccount findUserAccountByUserId(String userId)
  {
    UserAccount acct = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findUserAccountByUserId] Enter");

      acct = _userMgr.findUserAccount(userId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findUserAccountByUserId]", ex);
      assertTrue("Error in findUserAccountByUserId", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findUserAccountByUserId] Exit");
    }
    return acct;
  }

  protected UserAccount findUserAccountByUId(Long uId)
  {
    UserAccount acct = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findUserAccountByUId] Enter");

      acct = _userMgr.findUserAccount(uId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findUserAccountByUId]", ex);
      assertTrue("Error in findUserAccountByUId", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findUserAccountByUId] Exit");
    }
    return acct;
  }

  protected Collection findUserAccountKeys(IDataFilter filter)
  {
    Log.log("TEST", "[ActionTestHelper.findUserAccountKeys] Enter");

    Collection keys = null;
    try
    {
      keys = _userMgr.findUserAccountsKeys(filter);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findUserAccountKeys]", ex);
      assertTrue("Error in findUserAccountKeys", false);
    }

    Log.log("TEST", "[ActionTestHelper.findUserAccountKeys] Exit");
    return keys;
  }
/*
  protected Collection getBizEntitiesForUser(Long userUID)
  {
    Collection result = null;
    try
    {
      result = _enterpriseMgr.getBizEntitiesForUser(userUID);
      if (!result.isEmpty())
        result = ActionHelper.getBusinessEntities(result);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.getBizEntitiesForUser] Error", ex);
      assertTrue("unable to getBizEntitiesForUser", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.getBizEntitiesForUser] Exit ");
    }

    return result;

  }
*/
  protected Long getChannelForPartner(Long partnerUID)
  {
    Long result = null;
    try
    {
      result = _enterpriseMgr.getChannelForPartner(partnerUID);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.getChannelForPartner] Error", ex);
      assertTrue("unable to getChannelForPartner", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.getChannelForPartner] Exit ");
    }

    return result;
  }

  protected Long getBizEntityForPartner(Long partnerUID)
  {
    Long result = null;
    try
    {
      result = _enterpriseMgr.getBizEntityForPartner(partnerUID);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.getBizEntityForPartner] Error", ex);
      assertTrue("unable to getBizEntityForPartner", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.getBizEntityForPartner] Exit ");
    }

    return result;
  }

  protected Collection getPartnerUIDsForBizEntity(Long beUID)
  {
    Collection result = null;
    try
    {
      result = _enterpriseMgr.getPartnersForBizEntity(beUID);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.getPartnerUIDsForBizEntity] Error", ex);
      assertTrue("unable to getPartnerUIDsForBizEntity", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.getPartnerUIDsForBizEntity] Exit ");
    }

    return result;
  }

  protected Collection getPartnerUIDsForBizEntity(String enterpriseID,
    String beID)
  {
    Collection result = null;
    try
    {
      result = _enterpriseMgr.getPartnersForBizEntity(enterpriseID, beID);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.getPartnerUIDsForBizEntity] Error", ex);
      assertTrue("unable to getPartnerUIDsForBizEntity", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.getPartnerUIDsForBizEntity] Exit ");
    }

    return result;
  }

  protected Certificate findCertificateByUId(Long uId)
  {
    Certificate cert = null;
    try
    {
      Log.log("TEST", "[ActionTestHelper.findCertificateByUId] Enter");

      cert = _certMgr.findCertificateByUID(uId);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findCertificateByUId]", ex);
      assertTrue("Error in findCertificateByUId", false);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.findCertificateByUId] Exit");
    }
    return cert;
  }

  protected String openSession() throws Exception
  {
    String session = _sessionMgr.openSession();
    _openedSessions.add(session);
    _sessionMgr.authSession(session, USER_ID);
    return session;
  }

  protected void closeSession(String sessionId) throws Exception
  {
    _sessionMgr.closeSession(sessionId);
    _openedSessions.remove(sessionId);
  }

  protected void purgeSessions()
  {
    try
    {
      _sessionMgr.deleteSessions(new Date());
    }
    catch (Exception ex)
    {

    }

  }

  protected void closeAllSessions() throws Exception
  {
    String[] sessions = (String[])_openedSessions.toArray(new String[0]);
    for (int i=0; i<sessions.length; i++)
      closeSession(sessions[i]);
  }

  protected BasicEventResponse performEvent(
    IEJBAction action, IEvent event, String session, StateMachine sm)
    throws Exception
  {
    sm.setAttribute(IAttributeKeys.SESSION_ID, session);
    sm.setAttribute(IAttributeKeys.USER_ID, USER_ID);
    sm.setAttribute(IAttributeKeys.ENTERPRISE_ID, ENTERPRISE);

    action.init(sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  protected void assertResponseFail(BasicEventResponse response, short msgCode)
  {
    //check response
    assertNotNull("response is null", response);
    assertTrue("event status is incorrect", !response.isEventSuccessful());
    assertEquals("Msg code incorrect", msgCode, response.getMessageCode());
    assertNotNull("Error reason is null", response.getErrorReason());
    assertEquals("Error type is incorrect",
      ApplicationException.APPLICATION, response.getErrorType());
  }

  protected void assertResponsePass(BasicEventResponse response, short msgCode)
  {
    assertNotNull("response is null", response);
    assertTrue("event is not successful", response.isEventSuccessful());
    assertEquals("Msg code incorrect", msgCode, response.getMessageCode());
  }

  protected void checkFail(
    IEvent event, String session, StateMachine sm, boolean eventEx,
    short msgCode)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(createNewAction(), event, session, sm);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[ActionTestHelper.checkFail]" +
        " Returning fail due to EventException: "+ex.getMessage());
      if (!eventEx)
        assertTrue("Unexpected event exception caught: "+ex.getMessage(), false);

      Log.log("TEST", "[ActionTestHelper.checkFail] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.checkFail] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponseFail(response, msgCode);
  }

  protected void checkSuccess(
    IEvent event, String session, StateMachine sm, short msgCode)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(createNewAction(), event, session, sm);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.checkSuccess] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponsePass(response, msgCode);

    checkActionEffect(response, event, sm);
  }

  protected void checkBe(BusinessEntity origin, Map beMap)
  {
    //check be information
    assertEquals("UID is incorrect", origin.getKey(), beMap.get(BusinessEntity.UID));
    assertEquals("Id incorrect", origin.getBusEntId(), beMap.get(BusinessEntity.ID));
    assertEquals("Description incorrect", origin.getDescription(), beMap.get(BusinessEntity.DESCRIPTION));
    assertEquals("Enterprise is incorrect", origin.getEnterpriseId(), beMap.get(BusinessEntity.ENTERPRISE_ID));
    assertEquals("IsPartner is incorrect", new Boolean(origin.isPartner()), beMap.get(BusinessEntity.IS_PARTNER));
    assertEquals("State is incorrect", new Integer(origin.getState()), beMap.get(BusinessEntity.STATE));
    assertEquals("CanDelete is incorrect", new Boolean(origin.canDelete()), beMap.get(BusinessEntity.CAN_DELETE));

  }

  protected void checkWp(WhitePage origin, Map wpMap, boolean checkUID)
  {
    //check white page
    if (checkUID)
      assertEquals("UID in whitePage incorrect", origin.getKey(), wpMap.get(WhitePage.UID));
    assertEquals("BEUID is incorrect", origin.getBeUId(), wpMap.get(WhitePage.BE_UID));
    assertEquals("Biz Description is incorrect", origin.getBusinessDesc(), wpMap.get(WhitePage.BUSINESS_DESC));
    assertEquals("DUNS is incorrect", origin.getDUNS(), wpMap.get(WhitePage.DUNS));
    assertEquals("GCCC is incorrect", origin.getGlobalSupplyChainCode(), wpMap.get(WhitePage.G_SUPPLY_CHAIN_CODE));
    assertEquals("ContactPerson is incorrect", origin.getContactPerson(), wpMap.get(WhitePage.CONTACT_PERSON));
    assertEquals("Email is incorrect", origin.getEmail(),wpMap.get(WhitePage.EMAIL));
    assertEquals("Tel is incorrect", origin.getTel(), wpMap.get(WhitePage.TEL));
    assertEquals("Fax is incorrect", origin.getFax(), wpMap.get(WhitePage.FAX));
    assertEquals("Website is incorrect", origin.getWebsite(), wpMap.get(WhitePage.WEBSITE));
    assertEquals("Address is incorrect", origin.getAddress(), wpMap.get(WhitePage.ADDRESS));
    assertEquals("City is incorrect", origin.getCity(), wpMap.get(WhitePage.CITY));
    assertEquals("State is incorrect", origin.getState(), wpMap.get(WhitePage.STATE));
    assertEquals("ZipCode is incorrect", origin.getZipCode(), wpMap.get(WhitePage.ZIP_CODE));
    assertEquals("POBox is incorrect", origin.getPOBox(), wpMap.get(WhitePage.PO_BOX));
    assertEquals("Country is incorrect", origin.getCountry(), wpMap.get(WhitePage.COUNTRY));
    assertEquals("Language is incorrect", origin.getLanguage(), wpMap.get(WhitePage.LANGUAGE));
  }

  protected void checkChannel(ChannelInfo origin, Map channelMap)
  {
    assertEquals("UID is incorrect", origin.getKey(), channelMap.get(ChannelInfo.UID));
    assertEquals("Description incorrect", origin.getDescription(), channelMap.get(ChannelInfo.DESCRIPTION));
    assertEquals("Name incorrect", origin.getName(), channelMap.get(ChannelInfo.NAME));
    assertEquals("RefId is incorrect", origin.getReferenceId(), channelMap.get(ChannelInfo.REF_ID));
    assertEquals("ProtocolType is incorrect", origin.getTptProtocolType(), channelMap.get(ChannelInfo.TPT_PROTOCOL_TYPE));
  }

  protected void checkCommInfo(CommInfo origin, Map commInfoMap)
  {
    assertEquals("UID is incorrect", origin.getKey(), commInfoMap.get(CommInfo.UID));
    assertEquals("Description incorrect", origin.getDescription(), commInfoMap.get(CommInfo.DESCRIPTION));
    assertEquals("URL incorrect", origin.getURL(), commInfoMap.get(CommInfo.URL));
    assertEquals("ProtocolType is incorrect", origin.getProtocolType(), commInfoMap.get(CommInfo.PROTOCOL_TYPE));
    assertEquals("RefId incorrect", origin.getRefId(), commInfoMap.get(CommInfo.REF_ID));
    assertEquals("TptImplVersion is incorrect", origin.getTptImplVersion(), commInfoMap.get(CommInfo.TPT_IMPL_VERSION));
  }

  protected void checkDeserBe(BusinessEntity origin, BusinessEntity deserBe)
  {
    //check be information
    assertEquals("Id incorrect", origin.getBusEntId(), deserBe.getBusEntId());
    assertEquals("Description incorrect", origin.getDescription(), deserBe.getDescription());
    assertEquals("Enterprise is incorrect", origin.getEnterpriseId(), deserBe.getEnterpriseId());
    assertEquals("IsPartner is incorrect", origin.isPartner(), deserBe.isPartner());
    assertEquals("State is incorrect", origin.getState(), deserBe.getState());
    assertEquals("CanDelete is incorrect", origin.canDelete(), deserBe.canDelete());
    checkDeserWp(origin.getWhitePage(), deserBe.getWhitePage());
  }

  protected void checkDeserWp(WhitePage origin, WhitePage deserWp)
  {
    //check white page
    assertEquals("BEUID is incorrect", origin.getBeUId(), deserWp.getBeUId());
    assertEquals("Biz Description is incorrect", origin.getBusinessDesc(), deserWp.getBusinessDesc());
    assertEquals("DUNS is incorrect", origin.getDUNS(), deserWp.getDUNS());
    assertEquals("GCCC is incorrect", origin.getGlobalSupplyChainCode(), deserWp.getGlobalSupplyChainCode());
    assertEquals("ContactPerson is incorrect", origin.getContactPerson(), deserWp.getContactPerson());
    assertEquals("Email is incorrect", origin.getEmail(),deserWp.getEmail());
    assertEquals("Tel is incorrect", origin.getTel(), deserWp.getTel());
    assertEquals("Fax is incorrect", origin.getFax(), deserWp.getFax());
    assertEquals("Website is incorrect", origin.getWebsite(), deserWp.getWebsite());
    assertEquals("Address is incorrect", origin.getAddress(), deserWp.getAddress());
    assertEquals("City is incorrect", origin.getCity(), deserWp.getCity());
    assertEquals("State is incorrect", origin.getState(), deserWp.getState());
    assertEquals("ZipCode is incorrect", origin.getZipCode(), deserWp.getZipCode());
    assertEquals("POBox is incorrect", origin.getPOBox(), deserWp.getPOBox());
    assertEquals("Country is incorrect", origin.getCountry(), deserWp.getCountry());
    assertEquals("Language is incorrect", origin.getLanguage(), deserWp.getLanguage());
  }

  protected void checkDeserChannel(ChannelInfo origin, ChannelInfo deserCn)
  {
    assertEquals("Description incorrect", origin.getDescription(), deserCn.getDescription());
    assertEquals("Name incorrect", origin.getName(), deserCn.getName());
    assertEquals("RefId is incorrect", origin.getReferenceId(), deserCn.getReferenceId());
    assertEquals("ProtocolType is incorrect", origin.getTptProtocolType(), deserCn.getTptProtocolType());
    checkDeserCommInfo(origin.getTptCommInfo(), deserCn.getTptCommInfo());
    checkDeserPkgInfo(origin.getPackagingProfile(), deserCn.getPackagingProfile());
    checkDeserSecInfo(origin.getSecurityProfile(), deserCn.getSecurityProfile());
  }

  protected void checkDeserCommInfo(CommInfo origin, CommInfo deserCi)
  {
    assertEquals("Name incorrect", origin.getName(), deserCi.getName());
    assertEquals("Description incorrect", origin.getDescription(), deserCi.getDescription());
    assertEquals("URL incorrect", origin.getURL(), deserCi.getURL());
    //assertEquals("Port is incorrect", origin.getPort(), deserCi.getPort());
    assertEquals("ProtocolType is incorrect", origin.getProtocolType(), deserCi.getProtocolType());
    //assertEquals("ProtocolVersion incorrect", origin.getProtocolVersion(), deserCi.getProtocolVersion());
    assertEquals("RefId incorrect", origin.getRefId(), deserCi.getRefId());
    assertEquals("TptImplVersion is incorrect", origin.getTptImplVersion(), deserCi.getTptImplVersion());
    assertEquals("IsDefaultTpt is incorrect", origin.isDefaultTpt(), deserCi.isDefaultTpt());
    //assertEquals("ProtocolDetail is incorrect", origin.getProtocolDetailItems(), deserCi.getProtocolDetailItems());
  }

  protected void checkDeserPkgInfo(PackagingInfo origin, PackagingInfo deserPi)
  {
    assertEquals("Name incorrect", origin.getName(), deserPi.getName());
    assertEquals("Description incorrect", origin.getDescription(), deserPi.getDescription());
    assertEquals("Envelope incorrect", origin.getEnvelope(), deserPi.getEnvelope());
    assertEquals("ZipThreshold is incorrect", origin.getZipThreshold(), deserPi.getZipThreshold());
    assertEquals("IsZip is incorrect", origin.isZip(), deserPi.isZip());
  }

  protected void checkDeserSecInfo(SecurityInfo origin, SecurityInfo deserSi)
  {
    assertEquals("Name incorrect", origin.getName(), deserSi.getName());
    assertEquals("Description incorrect", origin.getDescription(), deserSi.getDescription());
    assertEquals("DigestAlgo incorrect", origin.getDigestAlgorithm(), deserSi.getDigestAlgorithm());
    assertEquals("EncryptLevel is incorrect", origin.getEncryptionLevel(), deserSi.getEncryptionLevel());
    assertEquals("EncryptType incorrect", origin.getEncryptionType(), deserSi.getEncryptionType());
  }

  protected void checkDeserCert(SyncCertificate syncCert, Long certUID)
  {
    Certificate cert = findCertificateByUId(certUID);

    assertEquals("CertName incorrect", syncCert.getCert().getCertName(), cert.getCertName());
    assertEquals("ID incorrect", syncCert.getCert().getID(), cert.getID());
    assertEquals("IssuerName incorrect", syncCert.getCert().getIssuerName(), cert.getIssuerName());
    assertEquals("SerialNumber incorrect", syncCert.getCert().getSerialNumber(), cert.getSerialNumber());
    assertEquals("IsMaster incorrect", syncCert.getCert().isMaster(), cert.isMaster());
    assertEquals("Bytecontent incorrect", syncCert.getCert().getCertificate(), cert.getCertificate());
  }

  protected void cleanUpGridNodes()
  {
    try
    {
      Log.debug("TEST", "[ActionTestHelper.cleanUpGridNodes] Enter");

      Collection results = _gnMgr.findGridNodesKeys(DELETE_FILTERS[0]);
      for (Iterator i=results.iterator(); i.hasNext(); )
      {
        _gnMgr.deleteGridNode((Long)i.next());
      }
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.cleanUpGridNodes] Error", ex);
    }
  }

  protected void createMyGridNode() throws Exception
  {
    try
    {
      GridNode gridnode = new GridNode();
      gridnode.setCategory("GNL");
      gridnode.setID(ENTERPRISE);
      gridnode.setName("AmpWay Inc.");
      gridnode.setState(GridNode.STATE_ME);

      ServiceLookupHelper.getGridNodeManager().createGridNode(gridnode);
    }
    catch (Throwable t)
    {
      Logger.err("[RegistrationLogic.createGridNode] Error creating GridNode ", t);
      throw new Exception(t.getMessage());
    }
  }

  protected Collection findActivationRecords(DataFilterImpl filter)
  {
    Collection result = null;
    try
    {
      result = _actMgr.findActivationRecordsByFilter(filter);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findActivationRecords] Error", ex);
      assertTrue("unable to findActivationRecords", false);
    }
    finally
    {
      Log.debug("TEST", "[ActionTestHelper.findActivationRecords] Exit ");
    }

    return result;
  }

  protected void cleanUpActivationRecords(Integer gridnodeID)
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ActivationRecord.GRIDNODE_ID,
        filter.getEqualOperator(), gridnodeID, false);

      Object[] records = _actMgr.findActivationRecordsByFilter(filter).toArray();
      for (int i=0; i<records.length; i++)
      {
        ActivationRecord record = (ActivationRecord)records[i];
        _actMgr.deleteActivationRecord(new Long(record.getUId()));
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

  }

  protected void simulateConnected() throws Exception
  {
    ConnectionNotification notification = new ConnectionNotification(
                                              ConnectionNotification.STATE_ONLINE,
                                              ConnectionNotification.TYPE_GM,
                                              "911");

    Notifier.getInstance().broadcast(notification);
  }

  protected void simulateDisconnected() throws Exception
  {
    ConnectionNotification notification = new ConnectionNotification(
                                              ConnectionNotification.STATE_OFFLINE,
                                              ConnectionNotification.TYPE_GM,
                                              "911");

    Notifier.getInstance().broadcast(notification);
  }

  protected abstract void cleanUp();
  protected abstract void prepareTestData() throws Exception;
  protected abstract void cleanTestData() throws Exception;
  protected abstract void unitTest() throws Exception;
  protected abstract IEJBAction createNewAction();
  protected abstract void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm);


}