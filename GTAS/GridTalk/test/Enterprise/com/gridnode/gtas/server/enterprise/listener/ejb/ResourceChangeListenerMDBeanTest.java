/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResourceChangeListenerMDBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 14 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.listener.ejb;

import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.gtas.events.enterprise.SetBizEntityForPartnerEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.channel.IChannelInfo;
import com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper;

import com.gridnode.pdip.app.channel.model.ChannelInfo;

import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This Test case tests the ResourceChangeListenerMDBean class.<p>
 * This test case is design to test the update of enterprise hierarchy on
 * reception of entity event message. Note: the test case needs to wait for
 * a few seconds to allow time for the ResourceChangeListenerMDBean to
 * perform the necessary updates upon receival of entity event message. This
 * time to wait is machine-dependent (depends of speed & memory), thus some
 * tests may fail if the waiting time is insufficient. Try varying the
 * <code>TIMEOUT</code> attribute if such situations happen.<p>
 *
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class ResourceChangeListenerMDBeanTest extends ActionTestHelper
{
  private static final int TIMEOUT = 5000;
  private Long[] _beUIDs, _cnUIDs, _pnUIDs, _userUIDs;

  public ResourceChangeListenerMDBeanTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ResourceChangeListenerMDBeanTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *************************

  protected void cleanUp()
  {
    cleanUpBeUserLinks();
    cleanUpPartnerLinks();
    cleanUpBeChannelLinks();
    cleanUpBEs(null);
    cleanUpBEs(ENTERPRISE);
    cleanUpPartners();
    cleanUpChannels();
    cleanUpUsers();
  }

  protected void cleanTestData() throws Exception
  {
  }

  protected void prepareTestData() throws Exception
  {
    createBes(4, 2);
    _beUIDs = getUIDs(_bizEntities);
    createPartners(3);
    _pnUIDs = getUIDs(_partners);

    Long[] certUIDs = new Long[] {null, null, null, null} ;

    createChannels(4, certUIDs);
    _cnUIDs = getUIDs(_channels);
    createUsers(2);
    _userUIDs = getUIDs(_users);

    //user[0]->be[2],be[3]
    assignBesToUser(new Long[] { _beUIDs[2], _beUIDs[3] }, _userUIDs[0]);
    //user[1]->be[3]
    assignBesToUser(new Long[] { _beUIDs[3] }, _userUIDs[1]);
    //be[0]->channel[0]
    assignChannelsToBe(new Long[] { _cnUIDs[0] }, _beUIDs[0]);
    //be[1]->channel[1],channel[3]
    assignChannelsToBe(new Long[] { _cnUIDs[1], _cnUIDs[3] }, _beUIDs[1]);
    //be[2]->channel[2]
    assignChannelsToBe(new Long[] { _cnUIDs[2] }, _beUIDs[2]);
    //be[3]->channel[2]
    assignChannelsToBe(new Long[] { _cnUIDs[2] }, _beUIDs[3]);
    //partner[0]->be[0],channel[0]
    assignBeToPartner(_pnUIDs[0], _beUIDs[0]);
    assignChannelToPartner(_pnUIDs[0], _cnUIDs[0]);
    //partner[1]->be[1],channel[3]
    assignBeToPartner(_pnUIDs[1], _beUIDs[1]);
    assignChannelToPartner(_pnUIDs[1], _cnUIDs[3]);
    //partner[2]->be[1],channel[1]
    assignBeToPartner(_pnUIDs[2], _beUIDs[1]);
    assignChannelToPartner(_pnUIDs[2], _cnUIDs[1]);
  }

  protected void unitTest() throws Exception
  {
    checkDeleteUser(_userUIDs[0], new Long[] { _beUIDs[2], _beUIDs[3] });
    checkDeletePartner(_pnUIDs[0], _beUIDs[0], _cnUIDs[0]);
    checkDeleteChannel(_cnUIDs[3], new Long[] { _beUIDs[1] }, new Long[] {_pnUIDs[1]});
    checkDeleteBe(_beUIDs[3], new Long[] {_userUIDs[1]}, new Long[] { _cnUIDs[2] }, new Long[]{});
    checkDeleteBe(_beUIDs[1], new Long[] {}, new Long[] {_cnUIDs[1],_cnUIDs[3]}, new Long[] {_pnUIDs[2]});
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

  private void assignBesToUser(Long[] beUIDs, Long userUID)
  {
    ArrayList list = new ArrayList(beUIDs.length);
    for (int i=0; i<beUIDs.length; i++)
    {
      list.add(beUIDs[i]);
    }
    this.assignBesToUser(list, userUID);
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

  private void checkDeleteUser(Long userUID, Long[] affectedBeUIDs)
    throws Exception
  {
    _userMgr.deleteUserAccount(userUID, false);

    waitForTimeout();
    Collection existBeUIDs = _enterpriseMgr.getBizEntitiesForUser(userUID);
    if (existBeUIDs.isEmpty())
      return;

    for (int i=0; i<affectedBeUIDs.length; i++)
    {
      assertTrue("User-Be link not removed!", !existBeUIDs.contains(affectedBeUIDs[i]));
    }
  }

  private void checkDeletePartner(Long pnUID, Long affectedBeUID, Long affectedCnUID)
    throws Exception
  {
    _partnerMgr.deletePartner(pnUID, false);

    waitForTimeout();
    Long existBeUID = _enterpriseMgr.getBizEntityForPartner(pnUID);
    if (existBeUID != null)
      assertTrue("Partner-Be link not removed!", !existBeUID.equals(affectedBeUID));

    Long existCnUID = _enterpriseMgr.getChannelForPartner(pnUID);
    if (existCnUID != null)
      assertTrue("Partner-Channel link not removed!", !existCnUID.equals(affectedCnUID));
  }

  private void checkDeleteChannel(Long cnUID, Long[] affectedBeUIDs, Long[] affectedPnUIDs)
    throws Exception
  {
    _channelMgr.deleteChannelInfo(cnUID);

    waitForTimeout();
    for (int i=0; i<affectedBeUIDs.length; i++)
    {
      boolean linked = _enterpriseMgr.isChannelAssignedToBe(affectedBeUIDs[i], cnUID);
      assertTrue("Be-Channel link not removed!", !linked);
    }

    for (int i=0; i<affectedPnUIDs.length; i++)
    {
      Long existCnUID = _enterpriseMgr.getChannelForPartner(affectedPnUIDs[i]);
      if (existCnUID != null)
        assertTrue("Partner-Channel link not removed!", !existCnUID.equals(cnUID));
    }
  }

  private void checkDeleteBe(Long beUID, Long[] affectedUserUIDs,
    Long[] affectedCnUIDs, Long[] affectedPnUIDs)
    throws Exception
  {
    _bizRegMgr.markDeleteBusinessEntity(beUID);
    _bizRegMgr.purgeDeletedBusinessEntities();

    waitForTimeout();
    for (int i=0; i<affectedUserUIDs.length; i++)
    {
      Collection existBeUIDs = _enterpriseMgr.getBizEntitiesForUser(affectedUserUIDs[i]);
      assertTrue("User-Be link not removed!", !existBeUIDs.contains(beUID));
    }

    Collection existCnUIDs = _enterpriseMgr.getChannelsForBizEntity(beUID);
    assertTrue("Be-Channel links not removed!", existCnUIDs.isEmpty());

    for (int i=0; i<affectedPnUIDs.length; i++)
    {
      Long existBeUID = _enterpriseMgr.getBizEntityForPartner(affectedPnUIDs[i]);
      if (existBeUID != null)
        assertTrue("Partner-Be link not removed!", !existBeUID.equals(beUID));

      Long existCnUID = _enterpriseMgr.getChannelForPartner(affectedPnUIDs[i]);
      if (existCnUID != null)
      {
        for (int j=0; j<affectedCnUIDs.length; j++)
          assertTrue("Partner-Channel link not removed!", !existCnUID.equals(affectedCnUIDs[j]));
      }
    }
  }

  private void waitForTimeout()
  {
    try
    {
      System.out.println("Waiting for "+TIMEOUT+" ms timeout....");
      Thread.sleep(TIMEOUT);
    }
    catch (InterruptedException ex)
    {

    }
  }

}