/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRoleListForUserActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 09 2002    Neo Sok Lay         Refactor
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.gtas.events.acl.GetRoleListForUserEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.acl.helpers.ActionTestHelper;

import com.gridnode.pdip.base.acl.model.Role;

import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;

import junit.framework.*;

import java.util.Collection;
import java.util.Map;

public class GetRoleListForUserActionTest extends ActionTestHelper
{
  GetRoleListForUserEvent[] _events;

  public GetRoleListForUserActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetRoleListForUserActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ********************** ActionTestHelper methods ***************************

  protected void cleanUp()
  {
    deleteRoles(2);
    deleteUsers(3);
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createRoles(2);
    createUsers(3);

    createSubjectRole(_userUIDs[0], _roleUIDs[0]);
    createSubjectRole(_userUIDs[0], _roleUIDs[1]);
    createSubjectRole(_userUIDs[1], _roleUIDs[0]);

    createSessions(1);
    createStateMachines(1);

    _events = new GetRoleListForUserEvent[]
              {
                //bad events
                null,
                getRoleListForUserEvent(DUMMY_UID),
                getRoleListForUserEvent(DUMMY_STRING),

                //good events
                getRoleListForUserEvent(_userUIDs[0]),
                getRoleListForUserEvent(_users[0].getId()),
                getRoleListForUserEvent(_userUIDs[1]),
                getRoleListForUserEvent(_users[1].getId()),
                getRoleListForUserEvent(_userUIDs[2]),
                getRoleListForUserEvent(_users[2].getId()),
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
    GetRoleListForUserEvent getEvent = (GetRoleListForUserEvent)event;

    Collection expRoles = getRolesForSubject(
                            getEvent.getUserUId(), getEvent.getUserId());

    checkRoleList(response.getReturnData(), expRoles);
  }

  protected IEJBAction createNewAction()
  {
    return new GetRoleListForUserAction();
  }

  // ******************* Own Methods *********************************

  private GetRoleListForUserEvent getRoleListForUserEvent(String userId)
    throws Exception
  {
    return new GetRoleListForUserEvent(userId);
  }

  private GetRoleListForUserEvent getRoleListForUserEvent(Long userUId)
    throws Exception
  {
    return new GetRoleListForUserEvent(userUId);
  }

  private void checkRoleList(Object returnData, Collection roles)
  {
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof EntityListResponseData);

    EntityListResponseData listData = (EntityListResponseData) returnData;

    Collection entityList = listData.getEntityList();
    assertNotNull("Entity list is null", entityList);

    Object[] entityObjs = entityList.toArray();
    Object[] rolesArray = roles.toArray();
    for (int i = 0; i < roles.size(); i++ )
    {
      checkReturnRole(entityObjs[i], (Role) rolesArray[i]);
    }
  }

  private void checkReturnRole(Object entityObject, Role role)
  {
    assertNotNull("Entity is null", entityObject);
    assertTrue("Entity data type incorrect", entityObject instanceof Map);

    Map roleMap = (Map) entityObject;

    assertEquals("Role is incorrect", role.getRole(), roleMap.get(Role.ROLE));
    assertEquals("Description is incorrect", role.getDescr(), roleMap.get(Role.DESCRIPTION));
    assertEquals("CanDelete is incorrect",
      role.canDelete() ? Boolean.TRUE : Boolean.FALSE,
      roleMap.get(Role.CAN_DELETE));
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