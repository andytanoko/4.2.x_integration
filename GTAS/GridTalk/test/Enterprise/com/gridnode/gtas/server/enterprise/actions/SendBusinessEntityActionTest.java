/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendBusinessEntityActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 28 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.gtas.server.enterprise.sync.models.SyncChannel;
import com.gridnode.gtas.server.enterprise.sync.models.SyncBusinessEntity;
import com.gridnode.gtas.server.enterprise.sync.models.SyncGridNode;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.gtas.events.enterprise.SendBusinessEntityEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.channel.IChannelInfo;
import com.gridnode.gtas.server.enterprise.model.SharedResource;
import com.gridnode.gtas.server.enterprise.model.IResourceTypes;
import com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper;

import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;

import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This Test case tests the SendBusinessEntityAction class.
 * <p>Note that this test can only be run once per session.
 * To run more than once, the Application server needs to be restarted.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I4
 */
public class SendBusinessEntityActionTest extends ActionTestHelper
{
  private static final String[] PN_ENTERPRISE = { "1111", "1112" };
  private static final String SER_DIR     = "D:/J2EE/jboss-3.0.0alpha/bin/gtas/data/temp/";
  private static final String[] CERT_FILE     = {
      DATA_PATH+"TestCert1.cer", DATA_PATH+"TestCert2.cer", DATA_PATH+"TestCert3.cer" };

  SendBusinessEntityEvent[] _events;
  Long[] _beUIDs, _cnUIDs, _certUIDs;
  int fileCnt = -1;

  public SendBusinessEntityActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(SendBusinessEntityActionTest.class);
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
    cleanUpBEs(null);
    cleanUpChannels();
    cleanUpCertificates(_certUIDs);
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createBes(3, 1);
    _beUIDs = getUIDs(_bizEntities);

    Long uid1 = createCertificate(Integer.parseInt(ENTERPRISE),
                  CERT_NAME, CERT_FILE[0]); // my cert
    Long uid2 = createCertificate(Integer.parseInt(PN_ENTERPRISE[1]),
                  CERT_NAME, CERT_FILE[1]); // partner cert
    Long uid3 = createCertificate(Integer.parseInt(PN_ENTERPRISE[0]),
                  CERT_NAME, CERT_FILE[2]);
    _certUIDs = new Long[] {uid1, uid1, uid1, uid3, uid2} ;

    createChannels(5, _certUIDs);
    _cnUIDs = getUIDs(_channels);

    //be[0]->channel[0]
    assignChannelsToBe(new Long[] { _cnUIDs[0] }, _beUIDs[1]);
    //be[1]->channel[1],channel[2]
    assignChannelsToBe(new Long[] { _cnUIDs[1], _cnUIDs[2] }, _beUIDs[2]);

    //share be[0]
    shareBe(new Long[] {_beUIDs[1]}, PN_ENTERPRISE[0]);

    createSessions(1);
    createStateMachines(1);

    _events = new SendBusinessEntityEvent[]
              {
                //1. rejected: some invalid beUID
                createTestEvent(
                  createCollection(new Object[]{_beUIDs[1], DUMMY_UID}),
                  PN_ENTERPRISE[0], _cnUIDs[3]),
                //2. rejected: some partner beUID
                createTestEvent(
                  createCollection(new Object[]{_beUIDs[0], _beUIDs[1]}),
                  PN_ENTERPRISE[0], _cnUIDs[3]),
                //3. accepted: new share
                createTestEvent(
                  createCollection(new Object[]{_beUIDs[2]}),
                  PN_ENTERPRISE[1], _cnUIDs[4]),
                //4. accepted: exist share
                createTestEvent(
                  createCollection(new Object[]{_beUIDs[1]}),
                  PN_ENTERPRISE[0], _cnUIDs[3]),
                //5. accepted: exist + new share
                createTestEvent(
                  createCollection(new Object[]{_beUIDs[1], _beUIDs[2]}),
                  PN_ENTERPRISE[0], _cnUIDs[3]),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** REJECTED ***************************

    //1. some invalid beUID
    sendCheckFail(_events[0], _sessions[0], _sm[0], true);

    //2. some partner beUID
    sendCheckFail(_events[1], _sessions[0], _sm[0], true);

    // ************** ACCEPTED *****************************

    //3. new share
    sendCheckSuccess(_events[2], _sessions[0], _sm[0]);

    //4. exist share
    sendCheckSuccess(_events[3], _sessions[0], _sm[0]);

    //5. exist + new share KIV: can't check because the second serialization
    //will overwrite the first one. need to devise a better scheme for testing.
    sendCheckSuccess(_events[4], _sessions[0], _sm[0]);

  }

  protected IEJBAction createNewAction()
  {
    return new SendBusinessEntityAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    SendBusinessEntityEvent sendEvent = (SendBusinessEntityEvent)event;

    Collection sentBes = sendEvent.getBeUIDs();

    fileCnt++;
    for (Iterator i=sentBes.iterator(); i.hasNext();  )
    {
      Long beUID = (Long)i.next();
  System.out.println("*************** check for BeUID:"+beUID + ", fileCnt="+fileCnt);
      checkSharedResource(beUID, sendEvent.getToEnterpriseID());
      checkSerContent(beUID, sendEvent.getToEnterpriseID(), fileCnt);
      if (i.hasNext())
        fileCnt++;
    }
  }

  // ******************* Own methods ****************************

  private void assignChannelsToBe(Long[] channelUIDs, Long beUID)
  {
    ArrayList list = new ArrayList(channelUIDs.length);
    for (int i=0; i<channelUIDs.length; i++)
    {
      list.add(channelUIDs[i]);
    }
    this.assignChannelsToBe(list, beUID);
  }

  private Collection createCollection(Object[] elements)
  {
    ArrayList list = new ArrayList();
    for (int i=0; i<elements.length; i++)
    {
      list.add(elements[i]);
    }
    return list;
  }

  private void checkSharedResource(
    Long beUID, String toEnterpriseID)
  {
    try
    {
      SharedResource sharedRes = getSharedResManager().getSharedResource(
                                   IResourceTypes.BUSINESS_ENTITY,
                                   beUID, toEnterpriseID);

      assertNotNull("SharedResource is not created", sharedRes);

      assertEquals("State is incorrect", SharedResource.STATE_UNSYNC, sharedRes.getState());


    }
    catch (Throwable ex)
    {
      Log.err("TEST", "[SendBusinessEntityActionTest.checkSharedResource]", ex);
      assertTrue("Error in checkSharedResource", false);
    }
  }

  private SendBusinessEntityEvent createTestEvent(
    Collection beUIDs, String toEnterpriseID, Long channelUID)
    throws Exception
  {
    return new SendBusinessEntityEvent(beUIDs, toEnterpriseID, channelUID);
  }

  private void checkSerContent(Long beUID, String recipient, int fileCnt)
  {
    try
    {
      //to check serialize file
      String serFile = SER_DIR+"syncBE_R"+recipient+"_T"+fileCnt+".ser";
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
    catch (Throwable ex)
    {
      Log.err("TEST", "[SendBusinessEntityActionTest.checkSerContent]", ex);
      assertTrue("Error in checkSerContent", false);
    }
  }

  private SyncBusinessEntity deserialize(String serFile) throws Exception
  {
    SyncGridNode syncGn = new SyncGridNode();
    syncGn = (SyncGridNode)syncGn.deserialize(serFile);

    return syncGn.getBusinessEntities()[0];
  }

  private void sendCheckFail(
    SendBusinessEntityEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.SEND_ENTITY_ERROR);
  }

  private void sendCheckSuccess(
    SendBusinessEntityEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}