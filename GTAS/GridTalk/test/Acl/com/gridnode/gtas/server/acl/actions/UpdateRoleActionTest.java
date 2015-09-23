/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateRoleActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 21 2002    Neo Sok Lay         Re-write test case
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.events.acl.UpdateRoleEvent;
import com.gridnode.gtas.server.acl.helpers.ActionTestHelper;
import com.gridnode.gtas.exceptions.IErrorCode;

import com.gridnode.pdip.base.acl.model.Role;

import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;

import junit.framework.*;

public class UpdateRoleActionTest extends ActionTestHelper
{
  UpdateRoleEvent[] _events;

  public UpdateRoleActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(UpdateRoleActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ****************** ActionTestHelper methods **********************

  protected void unitTest() throws Exception
  {
    //null event
    updateCheckFail(_events[0], _sessions[0], _sm[0], true);

    //no session id
    updateCheckFail(_events[1], "", _sm[0], true);

    //non existing role
    updateCheckFail(_events[2], _sessions[0], _sm[0], true);

    /**@todo check duplicate not in place in base-acl */
    //duplicate role
    //updateCheckFail(_events[3], _sessions[0], _sm[0], false);

    //accepted
    updateCheckSuccess(_events[4], _sessions[0], _sm[0]);
  }

  protected void cleanUp()
  {
    deleteRoles(2);
    deleteRole(ROLE_NAME);
    deleteRole(UPD_ROLE_NAME);
  }

  protected void prepareTestData() throws Exception
  {
    createRoles(2);
    createSessions(1);
    createStateMachines(1);

    _events = new UpdateRoleEvent[]
              {
                //rejected
                null,
                //rejected: invalid session
                updateRoleEvent(_roleUIDs[0], UPD_ROLE_NAME, UPD_ROLE_DESC),
                //rejected: non existing
                updateRoleEvent(DUMMY_UID, ROLE_NAME, ROLE_DESC),
                //rejected: duplicate
                updateRoleEvent(_roleUIDs[0], _roles[1].getRole(), ROLE_DESC),
                //accepted
                updateRoleEvent(_roleUIDs[0], UPD_ROLE_NAME, UPD_ROLE_DESC),
              };
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected IEJBAction createNewAction()
  {
    return new UpdateRoleAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    UpdateRoleEvent updEvent = (UpdateRoleEvent)event;

    checkRoleInDb(updEvent.getUId(), updEvent.getUpdatedRole(), updEvent.getUpdatedDesc());
  }

  // *************** Own methods **********************
  private UpdateRoleEvent updateRoleEvent(
    Long uId, String role, String desc) throws Exception
  {
    return new UpdateRoleEvent(uId, role, desc);
  }

  private void updateCheckSuccess(
    IEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private void updateCheckFail(
    IEvent event, String session, StateMachine sm, boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.UPDATE_ENTITY_ERROR);
  }

  private void checkRoleInDb(Long uId, String roleName, String description)
  {
    Role dbRole = getRoleByUId(uId.longValue());
    assertEquals("RoleName are not the same.", roleName, dbRole.getRole());
    assertEquals("Description are not the same.", description, dbRole.getDescr());
  }

}