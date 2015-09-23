/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetUserListForRoleActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 09 2002    Neo Sok Lay         Refactor
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.gtas.events.acl.GetUserListForRoleEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.acl.helpers.ActionTestHelper;

import com.gridnode.pdip.app.user.model.UserAccount;

import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;

import junit.framework.*;

import java.util.Collection;
import java.util.Map;

public class GetUserListForRoleActionTest extends ActionTestHelper
{
  private GetUserListForRoleEvent[] _events;

  public GetUserListForRoleActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetUserListForRoleActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ******************** ActionTestHelper methods *************************

  protected void cleanUp()
  {
    deleteRoles(3);
    deleteUsers(2);
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createRoles(3);
    createUsers(2);

    createSubjectRole(_userUIDs[0], _roleUIDs[0]);
    createSubjectRole(_userUIDs[0], _roleUIDs[1]);
    createSubjectRole(_userUIDs[1], _roleUIDs[0]);

    createSessions(1);
    createStateMachines(1);

    _events = new GetUserListForRoleEvent[]
              {
                null,
                getUserListForRoleEvent(DUMMY_UID),
                getUserListForRoleEvent(DUMMY_STRING),

                //good events
                getUserListForRoleEvent(_roleUIDs[0]),
                getUserListForRoleEvent(_roles[0].getRole()),
                getUserListForRoleEvent(_roleUIDs[1]),
                getUserListForRoleEvent(_roles[1].getRole()),
                getUserListForRoleEvent(_roleUIDs[2]),
                getUserListForRoleEvent(_roles[2].getRole()),
              };
  }

  protected void unitTest() throws Exception
  {
    for (int i=0; i<3; i++)
      getCheckFail(_events[i], _sessions[0], _sm[0], true);

    for (int i=3; i<9; i++)
      getCheckSuccess(_events[i], _sessions[0], _sm[0]);
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    GetUserListForRoleEvent getEvent = (GetUserListForRoleEvent)event;

    Collection expUserUIDs = getSubjectUIdsForRole(
                            getEvent.getRoleUId(), getEvent.getRoleName());

    checkUserList(response.getReturnData(), expUserUIDs);
  }

  protected IEJBAction createNewAction()
  {
    return new GetUserListForRoleAction();
  }

  // ****************** Own Methods *********************************

  private void checkUserList(Object returnData, Collection userUIds)
  {
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof EntityListResponseData);

    EntityListResponseData listData = (EntityListResponseData) returnData;

    Collection entityList = listData.getEntityList();
    assertNotNull("Entity list is null", entityList);

    Object[] entityObjs = entityList.toArray();
    Object[] userUIdsArray = userUIds.toArray();

    assertEquals("return list size incorrect", userUIdsArray.length, entityObjs.length);

    for (int i = 0; i < userUIdsArray.length; i++ )
    {
      checkUser(entityObjs[i], userUIds);
    }

    assertTrue("Expected users not returned:"+userUIds, userUIds.isEmpty());
  }

  private void checkUser(Object entityObject, Collection userUIds)
  {
    assertNotNull("Entity is null", entityObject);
    assertTrue("Entity data type incorrect", entityObject instanceof Map);

    Map userMap = (Map) entityObject;
    assertTrue("Return user not expected", userUIds.remove(userMap.get(UserAccount.UID)));
  }

  private GetUserListForRoleEvent getUserListForRoleEvent(String roleName)
    throws Exception
  {
    return new GetUserListForRoleEvent(roleName);
  }

  private GetUserListForRoleEvent getUserListForRoleEvent(Long roleUId)
    throws Exception
  {
    return new GetUserListForRoleEvent(roleUId);
  }

  private void getCheckFail(
    IEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_LIST_ERROR);
  }

  private void getCheckSuccess(
    IEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}