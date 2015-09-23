/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RemoveRoleFromUserActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 09 2002    Neo Sok Lay         Refactor
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.gtas.events.acl.RemoveRoleFromUserEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.acl.helpers.ActionTestHelper;

import com.gridnode.pdip.base.acl.model.SubjectRole;

import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;

import junit.framework.*;

public class RemoveRoleFromUserActionTest extends ActionTestHelper
{
  RemoveRoleFromUserEvent[] _events;

  public RemoveRoleFromUserActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(RemoveRoleFromUserActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

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
    createSubjectRole(_userUIDs[0], _roleUIDs[0]);
    createSubjectRole(_userUIDs[1], _roleUIDs[1]);

    createSessions(1);
    createStateMachines(1);

    _events = new RemoveRoleFromUserEvent[]
              {
                //bad events
                null,
                //dummy params
                removeRoleFromUserEvent(DUMMY_UID, _userUIDs[0]),
                removeRoleFromUserEvent(_roleUIDs[0], DUMMY_UID),
                removeRoleFromUserEvent(DUMMY_STRING, _users[1].getId()),
                removeRoleFromUserEvent(_roles[1].getRole(), DUMMY_STRING),
                //non-existing relationships
                removeRoleFromUserEvent(_roleUIDs[0], _userUIDs[1]),
                removeRoleFromUserEvent(_roles[1].getRole(), _users[0].getId()),
                //good events
                removeRoleFromUserEvent(_roles[0].getRole(), _users[0].getId()),
                removeRoleFromUserEvent(_roleUIDs[1], _userUIDs[1]),
              };
  }

  protected void unitTest() throws Exception
  {
    for (int i=0; i<5; i++)
      removeCheckFail(_events[i], _sessions[0], _sm[0], true);

    for (int i=5; i<9; i++)
      removeCheckSuccess(_events[i], _sessions[0], _sm[0]);
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    RemoveRoleFromUserEvent removeEvent = (RemoveRoleFromUserEvent)event;

    SubjectRole db = getSubjectRole(
                     removeEvent.getUserUId(), removeEvent.getRoleUId(),
                     removeEvent.getUserId(), removeEvent.getRoleName());
    assertNull("SubjectRole not removed", db);
  }

  protected IEJBAction createNewAction()
  {
    return new RemoveRoleFromUserAction();
  }

  // ************************** Own methods ***********************************

  private RemoveRoleFromUserEvent removeRoleFromUserEvent(String roleName, String userId)
    throws Exception
  {
    return new RemoveRoleFromUserEvent(userId, roleName);
  }

  private RemoveRoleFromUserEvent removeRoleFromUserEvent(Long roleUId, Long userUId)
    throws Exception
  {
    return new RemoveRoleFromUserEvent(userUId, roleUId);
  }

  private void removeCheckFail(
    IEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.DELETE_ENTITY_ERROR);
  }

  private void removeCheckSuccess(
    IEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}