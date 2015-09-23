/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateRoleActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 21 2002    Neo Sok Lay         Re-write test case.
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.gtas.events.acl.CreateRoleEvent;
import com.gridnode.gtas.server.acl.helpers.ActionTestHelper;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.pdip.base.acl.model.Role;

import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;

import junit.framework.*;

public class CreateRoleActionTest extends ActionTestHelper
{
  CreateRoleEvent[] _events;

  public CreateRoleActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(CreateRoleActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }


  protected void unitTest() throws Exception
  {
    createCheckFail(_events[0], _sessions[0], _sm[0], true);
    createCheckFail(_events[1], "", _sm[0], true);

    createCheckSuccess(_events[2], _sessions[0], _sm[0]);

    /**@todo check duplicate not in place in base-acl*/
    //createCheckFail(_events[3], _sessions[0], _sm[0], false);

    createCheckSuccess(_events[4], _sessions[0], _sm[0]);
  }

  protected void cleanUp()
  {
    deleteRole(NEW_ROLE_NAME);
    deleteRole(ROLE_NAME);
  }

  protected void prepareTestData() throws Exception
  {
    createSessions(1);
    createStateMachines(1);

    _events = new CreateRoleEvent[]
              {
                //rejected
                null,
                //rejected: invalid session
                createRoleEvent(NEW_ROLE_NAME, NEW_ROLE_DESC),
                //accepted
                createRoleEvent(NEW_ROLE_NAME, NEW_ROLE_DESC),
                //duplicate
                createRoleEvent(NEW_ROLE_NAME, ROLE_DESC),
                //accepted
                createRoleEvent(ROLE_NAME, ROLE_DESC),
              };
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected IEJBAction createNewAction()
  {
    return new CreateRoleAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    CreateRoleEvent createEvent = (CreateRoleEvent)event;

    checkRoleInDb(createEvent.getRole(), createEvent.getDescription(), createEvent.canDelete());
  }

  // ******************** Own methods ******************************

  private CreateRoleEvent createRoleEvent(String roleName, String description)
    throws Exception
  {
    return new CreateRoleEvent(roleName, description, true);
  }

  private void createCheckSuccess(
    IEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private void createCheckFail(
    IEvent event, String session, StateMachine sm, boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.CREATE_ENTITY_ERROR);
  }

  private void checkRoleInDb(String roleName, String desc, boolean canDelete)
  {
    Role dbRole = getRoleByName(roleName);
    assertEquals("Description are not the same.", desc, dbRole.getDescr());
    assertEquals("CanDelete are not the same", canDelete, dbRole.canDelete());
  }

}