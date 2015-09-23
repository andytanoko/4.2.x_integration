/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SetBizEntityListForUserActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 09 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.gtas.events.enterprise.SetBizEntityListForUserEvent;
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
 * This Test case tests the SetBizEntityListForUserAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class SetBizEntityListForUserActionTest extends ActionTestHelper
{
  SetBizEntityListForUserEvent[] _events;

  public SetBizEntityListForUserActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(SetBizEntityListForUserActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *************************

  protected void cleanUp()
  {
    cleanUpBeUserLinks();
    cleanUpBEs(ENTERPRISE);
    cleanUpBEs(null);
    cleanUpUsers();
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createBes(4, 1);
    createUsers(2);
    _uIDs = getUIDs(_users);
    ArrayList[] beUIDs = new ArrayList[] {
      new ArrayList(),
      new ArrayList(),
      new ArrayList(),
      new ArrayList(),
      new ArrayList(),
      new ArrayList(),
      };

    //1
    beUIDs[0].add(_bizEntities[1].getKey());
    //2
    beUIDs[1].add(DUMMY_UID);
    beUIDs[1].add(_bizEntities[1].getKey());
    //3
    beUIDs[2].add(_bizEntities[1].getKey());
    beUIDs[2].add(_bizEntities[0].getKey()); //partner be
    //4
    beUIDs[3].add(_bizEntities[1].getKey());
    beUIDs[3].add(_bizEntities[2].getKey());
    //5
    beUIDs[4].add(_bizEntities[3].getKey());
    beUIDs[4].add(_bizEntities[2].getKey());
    //6: empty

    // **KIV!! no canDelete implemented for EnterpriseHierarchyManager

    createSessions(1);
    createStateMachines(1);

    _events = new SetBizEntityListForUserEvent[]
              {
                //1. rejected: invalid userUID
                createTestEvent(DUMMY_UID, beUIDs[0]),
                //2. rejected: some invalid beUIDs
                createTestEvent(_uIDs[0], beUIDs[1]),
                //3. rejected: some partner be
                createTestEvent(_uIDs[0], beUIDs[2]),
                //4. accepted: new links
                createTestEvent(_uIDs[1], beUIDs[3]),
                //5. accepted: some new, some remove
                createTestEvent(_uIDs[1], beUIDs[4]),
                //6. accepted: all remove
                createTestEvent(_uIDs[1], beUIDs[5]),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** REJECTED ***************************

    //1. invalid userUID
    getCheckFail(_events[0], _sessions[0], _sm[0], true);

    //2. invalid beUID
    getCheckFail(_events[1], _sessions[0], _sm[0], true);

    //3. partner be
    getCheckFail(_events[2], _sessions[0], _sm[0], false);

    // ************** ACCEPTED *****************************

    //4. assigned new bizEntities
    getCheckSuccess(_events[3], _sessions[0], _sm[0]);

    //5. assigned & remove bizEntities
    getCheckSuccess(_events[4], _sessions[0], _sm[0]);

    //6. remove bizEntities
    getCheckSuccess(_events[5], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new SetBizEntityListForUserAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    SetBizEntityListForUserEvent setEvent = (SetBizEntityListForUserEvent)event;
    assertNotNull("responsedata is null", response.getReturnData());
    assertTrue("response data type incorrect", response.getReturnData() instanceof EntityListResponseData);

    EntityListResponseData listData = (EntityListResponseData) response.getReturnData();

    Collection entityList = listData.getEntityList();
    assertNotNull("Entity list is null", entityList);

    checkReturnBizEntities(setEvent, entityList);
  }

  // ******************* Own methods ****************************

  private void checkReturnBizEntities(
    SetBizEntityListForUserEvent event,
    Collection entityList)
  {
    Collection expected = findBusinessEntities(event.getBizEntityList());

    assertEquals("Entity list count is incorrect", expected.size(), entityList.size());

    Object[] entityObjs = entityList.toArray();
    Object[] beArray = expected.toArray();
    for (int i = 0; i < entityObjs.length; i++ )
    {
      checkBe(entityObjs[i], (BusinessEntity)beArray[i]);
    }
  }

  private SetBizEntityListForUserEvent createTestEvent(Long beUID, Collection channelUIDs)
    throws Exception
  {
    return new SetBizEntityListForUserEvent(beUID, channelUIDs);
  }

  private void checkBe(Object entityObj, BusinessEntity be)
  {
    assertNotNull("responsedata is null", entityObj);
    assertTrue("response data type incorrect", entityObj instanceof Map);

    Map beMap = (Map)entityObj;

    Object wpObj = beMap.get(BusinessEntity.WHITE_PAGE);
    assertNotNull("whitepage is null", wpObj);
    assertTrue("whitepage data type incorrect", wpObj instanceof Map);

    Map wpMap = (Map)wpObj;

    checkBe(be, beMap);
    checkWp(be.getWhitePage(), wpMap, true);
  }

  private void getCheckFail(
    SetBizEntityListForUserEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.UPDATE_ASSOCIATION_ERROR);
  }

  private void getCheckSuccess(
    SetBizEntityListForUserEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}