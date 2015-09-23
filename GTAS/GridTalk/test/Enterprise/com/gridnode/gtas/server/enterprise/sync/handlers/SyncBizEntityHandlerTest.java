/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncBizEntityHandlerTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 26 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.sync.handlers;

import com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper;
import com.gridnode.gtas.server.enterprise.model.IResourceTypes;
import com.gridnode.gtas.server.enterprise.model.SharedResource;
import com.gridnode.gtas.server.enterprise.sync.MessageReceiver;
import com.gridnode.gtas.server.enterprise.sync.SyncMessage;
import com.gridnode.gtas.server.enterprise.sync.SyncResourceController;
import com.gridnode.gtas.server.enterprise.sync.models.SyncBusinessEntity;
import com.gridnode.gtas.server.enterprise.sync.models.SyncChannel;
import com.gridnode.gtas.server.enterprise.sync.models.SyncGridNode;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

//TODO To review the tests for sendack and receive. some information maybe missing.
/**
 * This Test case tests the SyncBizEntityHandler class.<p>
 * This test case is design to test the synchronizing of BusinessEntity shared
 * to partners.
 * <p>
 * This test case merely simulate the synchronizing capabilities of the
 * SyncBizEntityHandler and does not do the physical transportation of
 * synchronizing files to the intended recipient.
 *
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I4
 */
public class SyncBizEntityHandlerTest extends ActionTestHelper
{
  private static final String[] PN_ENTERPRISE = { "1111", "1112" };
  private static final String CONTENT_MSG_ID  = "200";
  private static final String ACK_MSG_ID      = "201";
  private static final String[] CERT_FILE     = {
      DATA_PATH+"TestCert1.cer", DATA_PATH+"TestCert2.cer", DATA_PATH+"TestCert3.cer" };

  private Long[] _beUIDs, _cnUIDs, _certUIDs;
  private Collection[] _sharedRes;
  private int fileCnt = -1;

  public SyncBizEntityHandlerTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(SyncBizEntityHandlerTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *************************

  protected void cleanUp()
  {
    cleanUpSharedBes();
    cleanUpBeChannelLinks();
    cleanUpBEs(PN_ENTERPRISE[0]);
    cleanUpBEs(PN_ENTERPRISE[1]);
    cleanUpBEs(ENTERPRISE);
    cleanUpChannels();
    cleanUpCertificates(_certUIDs);
  }

  protected void cleanTestData() throws Exception
  {
  }

  protected void prepareTestData() throws Exception
  {
    createBes(2, 0);
    _beUIDs = getUIDs(_bizEntities);

    Long uid1 = createCertificate(Integer.parseInt(ENTERPRISE),
                  CERT_NAME, CERT_FILE[0]); // my cert
    Long uid2 = createCertificate(Integer.parseInt(PN_ENTERPRISE[1]),
                  CERT_NAME, CERT_FILE[1]); // partner cert
//    Long uid3 = createCertificate(Integer.parseInt(PN_ENTERPRISE[0]),
//                  CERT_NAME, CERT_FILE[2]);
//    _certUIDs = new Long[] {uid1, uid2, uid3} ;
    _certUIDs = new Long[] {uid1, uid1, uid1, uid2} ;

    createChannels(4, _certUIDs);
    _cnUIDs = getUIDs(_channels);

    //be[0]->channel[0]
    assignChannelsToBe(new Long[] { _cnUIDs[0] }, _beUIDs[0]);
    //be[1]->channel[1],channel[2]
    assignChannelsToBe(new Long[] { _cnUIDs[1], _cnUIDs[2] }, _beUIDs[1]);

    _sharedRes = new Collection[2];

    //share be[0]
    _sharedRes[0] = shareBe(new Long[] {_beUIDs[0]}, PN_ENTERPRISE[0]);
    //share be[1]
    _sharedRes[1] = shareBe(new Long[] {_beUIDs[1]}, PN_ENTERPRISE[1]);

  }

  protected void unitTest() throws Exception
  {
    //send
    Long sharedResUID = (Long)_sharedRes[0].iterator().next();
    sendBe(sharedResUID, _cnUIDs[3], DUMMY_UID, PN_ENTERPRISE[0], _beUIDs[0]);
    //receiveAck (fail)
    receiveAck(createAckMessage(sharedResUID, false));

    //send
    sharedResUID = (Long)_sharedRes[1].iterator().next();
    sendBe(sharedResUID, _cnUIDs[3], DUMMY_UID, PN_ENTERPRISE[1], _beUIDs[1]);
    //receiveAck (success)
    receiveAck(createAckMessage(sharedResUID, true));

    //receive(not exist) + sendAck
    receive(createReceiveMessage(
      DATA_PATH+"syncBE_R"+ENTERPRISE+"_T0.ser", PN_ENTERPRISE[0]));

    //receive(exist) + sendAck
    receive(createReceiveMessage(
      DATA_PATH+"syncBE_R"+ENTERPRISE+"_T1.ser", PN_ENTERPRISE[0]));
  }

  protected IEJBAction createNewAction()
  {
    return null;
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
  }

  // ******************* Own methods ****************************

  private void receive(SyncMessage msg) throws Exception
  {
    new MessageReceiver().handlerMessage(
      new String[]{
        msg.getThisMsgID(),
        "0",
        null,
        "some_fileid",
        msg.getReplyTo(),
        msg.getSendTo(),
      },
      msg.getDataPayload(),
      msg.getFilePayload(),
      null);

    String serFile = msg.getFilePayload()[0].getAbsolutePath();
    SyncBusinessEntity syncBe = deserialize(serFile);
    BusinessEntity expectedBe = syncBe.getBusinessEntity();
    BusinessEntity recBe = findBizEntity(expectedBe.getEnterpriseId(), expectedBe.getBusEntId());
    expectedBe.setPartner(true);
    expectedBe.setCanDelete(false);
    expectedBe.getWhitePage().setBeUId((Long)recBe.getKey());
    checkDeserBe(expectedBe, recBe);

    Collection channels = getChannelsForBizEntity((Long)recBe.getKey());
    SyncChannel[] syncCns = syncBe.getChannels();
    int i=0;
    for (Iterator iter=channels.iterator(); iter.hasNext(); i++)
    {
      ChannelInfo recCn = (ChannelInfo)iter.next();
      ChannelInfo expectedCn = syncCns[i].getChannel();
      expectedCn.setName("GN"+expectedBe.getEnterpriseId()+expectedCn.getName());
      expectedCn.setReferenceId(expectedBe.getEnterpriseId());
      expectedCn.getTptCommInfo().setName(
        "GN"+expectedBe.getEnterpriseId()+expectedCn.getTptCommInfo().getName());
      expectedCn.getTptCommInfo().setRefId(expectedBe.getEnterpriseId());
      expectedCn.getPackagingProfile().setName(
        "GN"+expectedBe.getEnterpriseId()+expectedCn.getPackagingProfile().getName());
      expectedCn.getSecurityProfile().setName(
        "GN"+expectedBe.getEnterpriseId()+expectedCn.getSecurityProfile().getName());
      checkDeserChannel(expectedCn, recCn);
      checkDeserCert(syncCns[i].getSecurityProfile().getEncryptionCert(),
        recCn.getSecurityProfile().getEncryptionCertificateID());
    }

  }

  private SyncMessage createReceiveMessage(String beFile, String replyTo)
  {
    SyncMessage msg = new SyncMessage(CONTENT_MSG_ID);
    File[] filePayload = new File[1];
    filePayload[0] = new File(beFile);
    msg.setFilePayload(filePayload);
    msg.setResourceType(IResourceTypes.BUSINESS_ENTITY);
    msg.setSharedResource(DUMMY_UID.longValue());
    msg.setReplyTo(replyTo);
    msg.setAckMsgID(ACK_MSG_ID);

    return msg;
  }


  private void sendBe(
    Long sharedResUID, Long channelUID, Long routeChannelUID,
    String recipient, Long beUID)
    throws Exception
  {
    fileCnt++;

    SharedResource sharedRes = getSharedResource(sharedResUID);
    SyncResourceController.getInstance().sendResource(
      sharedRes, channelUID);

    //to check serialize file
    String serFile = "gtas/data/temp/syncBE_R"+recipient+"_T"+fileCnt+".ser";
    BusinessEntity expectedBe = findBizEntityByUId(beUID);

    SyncBusinessEntity syncBe = deserialize(serFile);
    BusinessEntity deserBe    = syncBe.getBusinessEntity();
    deserBe.getWhitePage().setBeUId((Long)expectedBe.getKey());

    checkDeserBe(expectedBe, deserBe);

    Collection channels = getChannelsForBizEntity(beUID);
    SyncChannel[] syncCns = syncBe.getChannels();
    int i=0;
    for (Iterator iter=channels.iterator(); iter.hasNext(); i++)
    {
      ChannelInfo expectedCn = (ChannelInfo)iter.next();
      ChannelInfo deserCn    = syncCns[i].getChannel();
      checkDeserChannel(expectedCn, deserCn);
      checkDeserCert(syncCns[i].getSecurityProfile().getEncryptionCert(),
        expectedCn.getSecurityProfile().getEncryptionCertificateID());
    }
  }

  private SyncBusinessEntity deserialize(String serFile) throws Exception
  {
    SyncGridNode syncGn = new SyncGridNode();
    syncGn = (SyncGridNode)syncGn.deserialize(serFile);

    return syncGn.getBusinessEntities()[0];
  }

  private void receiveAck(SyncMessage msg)
  {
    new MessageReceiver().handlerMessage(
      new String[]{
        msg.getThisMsgID(),
        "0",
        null,
        "some_fileid",
        msg.getReplyTo(),
        msg.getSendTo(),        
      },
      msg.getDataPayload(),
      msg.getFilePayload(),
      null);

    //to check sharedRes sync status: if success -> sync, else -> unsync
    boolean success = Boolean.valueOf(msg.getDataPayload()[0]).booleanValue();
    SharedResource sharedRes = getSharedResource(new Long(msg.getSharedResource()));

    assertEquals("Sync status is incorrect", success, sharedRes.getState()==SharedResource.STATE_SYNC);
  }

  private SyncMessage createAckMessage(Long sharedResUID, boolean success)
  {
    SyncMessage msg = new SyncMessage(ACK_MSG_ID);
    String[] data = {
                      String.valueOf(success)
                    };

    msg.setDataPayload(data);
    msg.setResourceType(IResourceTypes.BUSINESS_ENTITY);
    msg.setSharedResource(sharedResUID.longValue());

    return msg;
  }

  private void assignChannelsToBe(Long[] channelUIDs, Long beUID)
  {
    ArrayList list = new ArrayList(channelUIDs.length);
    for (int i=0; i<channelUIDs.length; i++)
    {
      list.add(channelUIDs[i]);
    }
    this.assignChannelsToBe(list, beUID);
  }


}