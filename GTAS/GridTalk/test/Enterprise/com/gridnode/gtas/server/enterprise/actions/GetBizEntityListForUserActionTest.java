/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetBizEntityListForUserActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.gtas.events.enterprise.GetBizEntityListForUserEvent;
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
 * This Test case tests the GetBizEntityListForUserAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class GetBizEntityListForUserActionTest extends ActionTestHelper
{
  GetBizEntityListForUserEvent[] _events;

  public GetBizEntityListForUserActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetBizEntityListForUserActionTest.class);
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
    cleanUpUsers();
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createBes(2, 0);
    createUsers(2);
    _uIDs = getUIDs(_users);

    //3
    ArrayList beUIDs = new ArrayList();
    beUIDs.add(_bizEntities[1].getKey());
    beUIDs.add(_bizEntities[0].getKey());
    assignBesToUser(beUIDs, _uIDs[1]);

    createSessions(1);
    createStateMachines(1);

    _events = new GetBizEntityListForUserEvent[]
              {
                //1. rejected: invalid userUID
                createTestEvent(DUMMY_UID),
                //2. accepted: no be links
                createTestEvent(_uIDs[0]),
                //3. accepted: with be links
                createTestEvent(_uIDs[1]),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** REJECTED ***************************

    //1. invalid userUID
    getCheckFail(_events[0], _sessions[0], _sm[0], true);

    // ************** ACCEPTED *****************************

    //4. no assigned bizEntities
    getCheckSuccess(_events[1], _sessions[0], _sm[0]);

    //5. with assigned bizEntities
    getCheckSuccess(_events[2], _sessions[0], _sm[0]);

  }

  protected IEJBAction createNewAction()
  {
    return new GetBizEntityListForUserAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    GetBizEntityListForUserEvent getEvent = (GetBizEntityListForUserEvent)event;
    assertNotNull("responsedata is null", response.getReturnData());
    assertTrue("response data type incorrect", response.getReturnData() instanceof EntityListResponseData);

    EntityListResponseData listData = (EntityListResponseData) response.getReturnData();

    Collection entityList = listData.getEntityList();
    assertNotNull("Entity list is null", entityList);

    checkReturnBizEntities(getEvent, entityList);
  }

  // ******************* Own methods ****************************

  private void checkReturnBizEntities(
    GetBizEntityListForUserEvent event,
    Collection entityList)
  {
    Collection expected = this.getBizEntitiesForUser(event.getUserAccountUID());

    assertEquals("Entity list count is incorrect", expected.size(), entityList.size());

    Object[] entityObjs = entityList.toArray();
    Object[] beArray = expected.toArray();
    for (int i = 0; i < entityObjs.length; i++ )
    {
      checkBe(entityObjs[i], (BusinessEntity)beArray[i]);
    }
  }

  private GetBizEntityListForUserEvent createTestEvent(Long userUID)
    throws Exception
  {
    return new GetBizEntityListForUserEvent(userUID);
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
    GetBizEntityListForUserEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ASSOCIATION_ERROR);
  }

  private void getCheckSuccess(
    GetBizEntityListForUserEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}