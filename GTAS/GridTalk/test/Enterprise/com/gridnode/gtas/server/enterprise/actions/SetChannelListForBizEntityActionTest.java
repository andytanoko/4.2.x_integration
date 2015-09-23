/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SetChannelListForBizEntityActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 08 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.gtas.events.enterprise.SetChannelListForBizEntityEvent;
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
 * This Test case tests the SetChannelListForBizEntityAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class SetChannelListForBizEntityActionTest extends ActionTestHelper
{
  SetChannelListForBizEntityEvent[] _events;

  public SetChannelListForBizEntityActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(SetChannelListForBizEntityActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *************************

  protected void cleanUp()
  {
    cleanUpBeChannelLinks();
    cleanUpBEs(ENTERPRISE);
    cleanUpChannels();
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createBes(2, 0);
    Long[] certUIDs = new Long[] {null, null, null} ;

    createChannels(3, certUIDs);
    _uIDs = getUIDs(_bizEntities);
    ArrayList[] channelUIDs = new ArrayList[] {
      new ArrayList(),
      new ArrayList(),
      new ArrayList(),
      new ArrayList(),
      new ArrayList(),
      new ArrayList(),
      new ArrayList(),
      };

    //1
    channelUIDs[0].add(_channels[0].getKey());
    //2
    channelUIDs[1].add(DUMMY_UID);
    channelUIDs[1].add(_channels[0].getKey());
    //3. **KIV!! no canDelete implemented for EnterpriseHierarchyManager
    channelUIDs[2].add(_channels[0].getKey());
    channelUIDs[2].add(_channels[1].getKey()); //canDelete=false
    //4
    channelUIDs[3].add(_channels[0].getKey());
    channelUIDs[3].add(_channels[1].getKey());
    //5
    channelUIDs[4].add(_channels[1].getKey());
    channelUIDs[4].add(_channels[0].getKey());
    //6
    channelUIDs[5].add(_channels[2].getKey());
    channelUIDs[5].add(_channels[1].getKey());
    //7: empty

    createSessions(1);
    createStateMachines(1);

    _events = new SetChannelListForBizEntityEvent[]
              {
                //1. rejected: invalid beUID
                createTestEvent(DUMMY_UID, channelUIDs[0]),
                //2. rejected: some invalid channleUIDs
                createTestEvent(_uIDs[0], channelUIDs[1]),
                //3. rejected: some cannot be removed. KIV not implemented
                createTestEvent(_uIDs[0], channelUIDs[2]),
                //4. accepted: new links
                createTestEvent(_uIDs[1], channelUIDs[3]),
                //5. accepted: change sequence
                createTestEvent(_uIDs[1], channelUIDs[4]),
                //6. accepted: some new, some remove
                createTestEvent(_uIDs[1], channelUIDs[5]),
                //7. accepted: all remove
                createTestEvent(_uIDs[1], channelUIDs[6]),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** REJECTED ***************************

    //1. invalid beUID
    getCheckFail(_events[0], _sessions[0], _sm[0], true);

    //2. invalid channelUID
    getCheckFail(_events[1], _sessions[0], _sm[0], true);

    // ************** ACCEPTED *****************************

    //4. assigned new channels
    getCheckSuccess(_events[3], _sessions[0], _sm[0]);

    //5. re-order channels
    getCheckSuccess(_events[4], _sessions[0], _sm[0]);

    //6. assigned & remove channels
    getCheckSuccess(_events[5], _sessions[0], _sm[0]);

    //7. remove channels
    getCheckSuccess(_events[6], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new SetChannelListForBizEntityAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    SetChannelListForBizEntityEvent setEvent = (SetChannelListForBizEntityEvent)event;
    assertNotNull("responsedata is null", response.getReturnData());
    assertTrue("response data type incorrect", response.getReturnData() instanceof EntityListResponseData);

    EntityListResponseData listData = (EntityListResponseData) response.getReturnData();

    Collection entityList = listData.getEntityList();
    assertNotNull("Entity list is null", entityList);

    checkReturnChannels(setEvent, entityList);
  }

  // ******************* Own methods ****************************

  private void checkReturnChannels(
    SetChannelListForBizEntityEvent event,
    Collection entityList)
  {
    System.out.println("***** expected order: "+event.getChannelList());
    Collection expected = getChannels(event.getChannelList());

    assertEquals("Entity list count is incorrect", expected.size(), entityList.size());

    Object[] entityObjs = entityList.toArray();
    Object[] channelArray = expected.toArray();
    for (int i = 0; i < entityObjs.length; i++ )
    {
      checkChannel(entityObjs[i], (ChannelInfo)channelArray[i]);
    }
  }

  private SetChannelListForBizEntityEvent createTestEvent(Long beUID, Collection channelUIDs)
    throws Exception
  {
    return new SetChannelListForBizEntityEvent(beUID, channelUIDs);
  }

  private void checkChannel(Object entityObj, ChannelInfo channel)
  {
    assertNotNull("responsedata is null", entityObj);
    assertTrue("response data type incorrect", entityObj instanceof Map);

    Map channelMap = (Map)entityObj;

 System.out.println("*** checking "+channelMap.get(ChannelInfo.UID) + " against "+channel.getUId());
    Object commInfoObj = channelMap.get(IChannelInfo.TPT_COMM_INFO);
    assertNotNull("comminfo is null", commInfoObj);
    assertTrue("comminfo data type incorrect", commInfoObj instanceof Map);

    Map commInfoMap = (Map)commInfoObj;

    checkChannel(channel, channelMap);
    checkCommInfo(channel.getTptCommInfo(), commInfoMap);
  }

  private void getCheckFail(
    SetChannelListForBizEntityEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.UPDATE_ASSOCIATION_ERROR);
  }

  private void getCheckSuccess(
    SetChannelListForBizEntityEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}