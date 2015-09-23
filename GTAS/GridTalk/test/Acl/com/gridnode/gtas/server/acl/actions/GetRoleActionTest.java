/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRoleActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 21 2002    Neo Sok Lay         Re-write test case
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.events.acl.GetRoleEvent;
import com.gridnode.gtas.server.acl.helpers.ActionTestHelper;
import com.gridnode.gtas.exceptions.IErrorCode;

import com.gridnode.pdip.base.acl.model.Role;

import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;

import junit.framework.*;

import java.util.Map;

public class GetRoleActionTest extends ActionTestHelper
{
  GetRoleEvent[] _events;

  public GetRoleActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetRoleActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ******************* ActionTestHelper methods *********************

  protected void unitTest() throws Exception
  {
    // null.
    getCheckFail(_events[0], _sessions[0], _sm[0], true);

    // no session id.
    getCheckFail(_events[1], "", _sm[0], true);

    // get non existing by uid.
    getCheckFail(_events[2], _sessions[0], _sm[0], false);

    // get non existing by name.
    getCheckSuccess(_events[3], _sessions[0], _sm[0]);

    // get existing by uid.
    getCheckSuccess(_events[4], _sessions[0], _sm[0]);

    // get existing by name.
    getCheckSuccess(_events[5], _sessions[0], _sm[0]);
  }

  protected void cleanUp()
  {
    deleteRoles(2);
  }

  protected void prepareTestData() throws Exception
  {
    createRoles(2);
    createSessions(1);
    createStateMachines(1);

    _events = new GetRoleEvent[]
              {
                //rejected
                null,
                //rejected: invalid session
                getRoleEvent(_roleUIDs[0]),
                //rejected: non existing by uid
                getRoleEvent(DUMMY_UID),
                //accepted: not exisiting by name
                getRoleEvent(ROLE_NAME),
                //accepted: existing by uid
                getRoleEvent(_roleUIDs[0]),
                //accepted: existing by name
                getRoleEvent(_roles[1].getRole()),
              };
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected IEJBAction createNewAction()
  {
    return new GetRoleAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    GetRoleEvent getEvent = (GetRoleEvent)event;

    if (getEvent.getRoleUID() == null)
      getCheckReturnRole(getRoleByName(getEvent.getRoleName()), response.getReturnData());
    else
      getCheckReturnRole(getRoleByUId(getEvent.getRoleUID().longValue()), response.getReturnData());
  }

  // ******************* Own methods ******************************
  private GetRoleEvent getRoleEvent(String roleName) throws Exception
  {
    return new GetRoleEvent(roleName);
  }

  private GetRoleEvent getRoleEvent(Long roleUId) throws Exception
  {
    return new GetRoleEvent(roleUId);
  }

  private void getCheckSuccess(IEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private void getCheckFail(
    IEvent event, String session, StateMachine sm, boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_BY_KEY_ERROR);
  }

  private void getCheckReturnRole(Role mockRole, Object returnData)
  {
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof Map);

    Map roleMap = (Map) returnData;

    if (mockRole == null)
    {
      assertTrue("responsedata is not empty", roleMap.isEmpty());
      return;
    }

    assertEquals("Role is incorrect", mockRole.getRole(), roleMap.get(Role.ROLE));
    assertEquals("Description is incorrect", mockRole.getDescr(), roleMap.get(Role.DESCRIPTION));
    assertEquals("CanDelete is incorrect",
                            mockRole.canDelete() ? Boolean.TRUE : Boolean.FALSE,
                            roleMap.get(Role.CAN_DELETE));
  }

}