/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetChannelListForBizEntityActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 08 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.gtas.events.enterprise.GetChannelListForBizEntityEvent;
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
 * This Test case tests the GetChannelListForBizEntityAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class GetChannelListForBizEntityActionTest extends ActionTestHelper
{
  GetChannelListForBizEntityEvent[] _events;

  public GetChannelListForBizEntityActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetChannelListForBizEntityActionTest.class);
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
    Long[] certUIDs = new Long[] {null, null} ;

    createChannels(2, certUIDs);
    _uIDs = getUIDs(_bizEntities);
    ArrayList channelUIDs = new ArrayList();
    for (int i=0; i<_channels.length; i++)
      channelUIDs.add(_channels[i].getKey());

    createSessions(1);
    createStateMachines(1);

    assignChannelsToBe(channelUIDs, _uIDs[1]);

    _events = new GetChannelListForBizEntityEvent[]
              {
                //rejected
                createTestEvent(DUMMY_UID),
                //accepted
                createTestEvent(_uIDs[0]),
                createTestEvent(_uIDs[1]),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** REJECTED ***************************

    //1. invalid beUID
    getCheckFail(_events[0], _sessions[0], _sm[0], true);

    // ************** ACCEPTED *****************************

    //2. no assigned channels
    getCheckSuccess(_events[1], _sessions[0], _sm[0]);

    //3. have assigned channels
    getCheckSuccess(_events[2], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new GetChannelListForBizEntityAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    GetChannelListForBizEntityEvent getEvent = (GetChannelListForBizEntityEvent)event;
    assertNotNull("responsedata is null", response.getReturnData());
    assertTrue("response data type incorrect", response.getReturnData() instanceof EntityListResponseData);

    EntityListResponseData listData = (EntityListResponseData) response.getReturnData();

    Collection entityList = listData.getEntityList();
    assertNotNull("Entity list is null", entityList);

    checkReturnChannels(getEvent, entityList);
  }

  // ******************* Own methods ****************************

  private void checkReturnChannels(
    GetChannelListForBizEntityEvent event,
    Collection entityList)
  {
    Collection expected = getChannelsForBizEntity(event.getBizEntityUID());

    assertEquals("Entity list count is incorrect", expected.size(), entityList.size());

    Object[] entityObjs = entityList.toArray();
    Object[] channelArray = expected.toArray();
    for (int i = 0; i < entityObjs.length; i++ )
    {
      checkChannel(entityObjs[i], (ChannelInfo)channelArray[i]);
    }
  }

  private GetChannelListForBizEntityEvent createTestEvent(Long beUID)
    throws Exception
  {
    return new GetChannelListForBizEntityEvent(beUID);
  }

  private void checkChannel(Object entityObj, ChannelInfo channel)
  {
    assertNotNull("responsedata is null", entityObj);
    assertTrue("response data type incorrect", entityObj instanceof Map);

    Map channelMap = (Map)entityObj;

    Object commInfoObj = channelMap.get(IChannelInfo.TPT_COMM_INFO);
    assertNotNull("comminfo is null", commInfoObj);
    assertTrue("comminfo data type incorrect", commInfoObj instanceof Map);

    Map commInfoMap = (Map)commInfoObj;

    checkChannel(channel, channelMap);
    checkCommInfo(channel.getTptCommInfo(), commInfoMap);
  }

  private void getCheckFail(
    GetChannelListForBizEntityEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ASSOCIATION_ERROR);
  }

  private void getCheckSuccess(
    GetChannelListForBizEntityEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}