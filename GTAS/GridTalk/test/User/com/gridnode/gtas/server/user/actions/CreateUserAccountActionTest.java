/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateUserAccountActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Neo Sok Lay         Created
 * Jun 20 2002    Neo Sok Lay         Re-org test cases.
 */
package com.gridnode.gtas.server.user.actions;

import com.gridnode.gtas.events.user.CreateUserAccountEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.user.helpers.ActionTestHelper;

import com.gridnode.pdip.app.user.model.*;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.PasswordMask;

import junit.framework.*;

public class CreateUserAccountActionTest extends ActionTestHelper
{
  static final String NEW_ID = "newid";
  static final String NEW_NAME = "newname";

  CreateUserAccountEvent[] _events;

  public CreateUserAccountActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(CreateUserAccountActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }


  // *************** ActionTestHelper methods ****************************

  protected void cleanUp()
  {
    deleteUsers(1);
    deleteUser(NEW_ID);
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createUsers(1);
    createSessions(1);
    createStateMachines(1);

    _events = new CreateUserAccountEvent[]
              {
                //rejected
                null,
                createTestEvent(NEW_ID, NEW_NAME, true),
                createTestEvent(_accts[0].getId(), NEW_NAME, true),

                //accepted
                createTestEvent(NEW_ID, NEW_NAME, true),
              };
  }

  protected void unitTest() throws Exception
  {
    signon(_sessions[0], _accts[0].getId(), PASSWORD, _sm[0]);

    // ************** REJECTED ***************************
    //null event
    createCheckFail(_events[0], _sessions[0], _sm[0], true);

    //invalid session
    createCheckFail(_events[1], "", _sm[0], true);

    //existing user
    createCheckFail(_events[2], _sessions[0], _sm[0], false);

    // ************** ACCEPTED *****************************
    createCheckSuccess(_events[3], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new CreateUserAccountAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    CreateUserAccountEvent createEvent = (CreateUserAccountEvent)event;

    UserAccount acct = findUserAccountByUserId(createEvent.getUserID());
    assertNotNull("Acct retrieved is null", acct);

    UserAccountState acctState = acct.getAccountState();
    assertNotNull("Acct state is null", acctState);

    assertNotNull("UID is null", acct.getFieldValue(UserAccount.UID));

    createEvent.getInitialPassword().applyMaskLength(acct.getPasswordMaskLength());

    assertEquals("Password incorrect",
      createEvent.getInitialPassword().toString(),
      acct.getPassword());
    assertEquals("Name is incorrect", createEvent.getUserName(), acct.getUserName());
    assertEquals("Phone is incorrect", createEvent.getPhone(), acct.getPhone());
    assertEquals("Email is incorrect", createEvent.getEmail(), acct.getEmail());
    assertEquals("Property is incorrect", createEvent.getProperty(), acct.getProperty());
    assertEquals("State is incorrect",
      createEvent.isEnabled()?UserAccountState.STATE_ENABLED:UserAccountState.STATE_DISABLED,
      acctState.getState());
  }

  // ****************** Utility methods ****************************

  private CreateUserAccountEvent createTestEvent(
    String id, String name, boolean enabled) throws Exception
  {
    return new CreateUserAccountEvent(id, name, PASSWORD,
                 PHONE, EMAIL, PROPERTY, enabled);
  }

  private void createCheckFail(
    CreateUserAccountEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.CREATE_ENTITY_ERROR);
  }

  private void createCheckSuccess(
    CreateUserAccountEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }
}