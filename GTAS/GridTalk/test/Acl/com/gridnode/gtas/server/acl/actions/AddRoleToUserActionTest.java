/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AddRoleToUserActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 09 2002    Neo Sok Lay         Refactor.
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.gtas.events.acl.AddRoleToUserEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.acl.helpers.ActionTestHelper;

import com.gridnode.pdip.base.acl.model.SubjectRole;

import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;

import junit.framework.*;

public class AddRoleToUserActionTest extends ActionTestHelper
{
  AddRoleToUserEvent[] _events;

  public AddRoleToUserActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(AddRoleToUserActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

//  private static final String ROLE_NAME_1 = "Role Name 1";
//  private static final String DESCRIPTION_1 = "Description 1";
//  private static final boolean CAN_DELETE_1 = true;
//
//  private static final String ROLE_NAME_2 = "Role Name 2";
//  private static final String DESCRIPTION_2 = "Description 2";
//  private static final boolean CAN_DELETE_2 = false;
//
//  private static final String USER_ID_1 = "User Id 1";
//  private static final String USER_NAME_1 = "User Name 1";
//  private static final String PASSWORD_1 = "Password 1";
//
//  private static final String USER_ID_2 = "User Id 2";
//  private static final String USER_NAME_2 = "User Name 2";
//  private static final String PASSWORD_2 = "Password Id 2";

  protected void cleanUp()
  {
    deleteRoles(2);
    deleteUsers(2);
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createRoles(2);
    createUsers(2);
    createSessions(1);
    createStateMachines(1);
    _events = new AddRoleToUserEvent[] {
                //bad events
                null,
                addRoleToUserEvent(DUMMY_STRING, _users[0].getId()),
                addRoleToUserEvent(_roles[0].getRole(), DUMMY_STRING),
                addRoleToUserEvent(DUMMY_UID, _userUIDs[1]),
                addRoleToUserEvent(_roleUIDs[1], DUMMY_UID),
                //good events
                addRoleToUserEvent(_roles[0].getRole(), _users[0].getId()),
                addRoleToUserEvent(_roleUIDs[1], _userUIDs[1]),
                //duplicate, silently return
                addRoleToUserEvent(_roles[0].getRole(), _users[0].getId()),
                addRoleToUserEvent(_roleUIDs[1], _userUIDs[1]),
                addRoleToUserEvent(_roles[1].getRole(), _users[1].getId()),
                addRoleToUserEvent(_roleUIDs[0], _userUIDs[0]),
              };
  }

  protected void unitTest() throws Exception
  {
    // ***************** REJECTED ************************
    for (int i=0; i< 5; i++)
      addCheckFail(_events[i], _sessions[0], _sm[0], true);

    // ***************** ACCEPTED ************************
    for (int i=5; i<11; i++)
      addCheckSuccess(_events[i], _sessions[0], _sm[0]);
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    AddRoleToUserEvent addEvent = (AddRoleToUserEvent)event;

    SubjectRole db = getSubjectRole(
                       addEvent.getUserUId(), addEvent.getRoleUId(),
                       addEvent.getUserId(), addEvent.getRoleName());
    assertNotNull("SubjectRole not inserted.", db);
  }

  protected IEJBAction createNewAction()
  {
    return new AddRoleToUserAction();
  }

  // ********************** Own Methods ********************************

  private AddRoleToUserEvent addRoleToUserEvent(String roleName, String userId)
    throws Exception
  {
    return new AddRoleToUserEvent(userId, roleName);
  }

  private AddRoleToUserEvent addRoleToUserEvent(Long roleUId, Long userUId)
    throws Exception
  {
    return new AddRoleToUserEvent(userUId, roleUId);
  }

  private void addCheckFail(
    IEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.CREATE_ENTITY_ERROR);
  }

  private void addCheckSuccess(
    IEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}