/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChangeAccountPasswordActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 06 2002    Neo Sok Lay         Created
 * Jun 20 2002    Neo Sok Lay         Re-org test cases.
 */
package com.gridnode.gtas.server.user.actions;

import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.events.user.ChangeAccountPasswordEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.user.helpers.ActionTestHelper;

import com.gridnode.pdip.app.user.model.*;

import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.PasswordMask;

import junit.framework.*;

/**
 * This test cases tests the ChangeAccountPasswordAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class ChangeAccountPasswordActionTest extends ActionTestHelper
{
  ChangeAccountPasswordEvent[] _events;

  public ChangeAccountPasswordActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ChangeAccountPasswordActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ********************* ActionTestHelper methods ************************

  protected void unitTest() throws Exception
  {
    signon(_sessions[0], ID+0, PASSWORD, _sm[0]);

    // **** REJECT BY ACTION **************

    //null event
    changeCheckFail(_events[0], _sessions[0], _sm[0], true);

    //invalid session
    changeCheckFail(_events[1], "", _sm[0], true);

    //incorrect user
    changeCheckFail(_events[2], _sessions[0], _sm[0], true);

    //non existing user
    changeCheckFail(_events[3], _sessions[0], _sm[0], true);

    //password not match
    changeCheckFail(_events[4], _sessions[0], _sm[0], true);

    // ************** ACCEPTED **************

    changeCheckSuccess(_events[5], _sessions[0], _sm[0]);

    //signon must be possible after change
    signon(_sessions[1], ID+0, UPD_PASSWORD, _sm[1]);

    signoff(_sessions[0], ID+0, _sm[0]);
    signoff(_sessions[1], ID+0, _sm[1]);
  }

  protected IEJBAction createNewAction()
  {
    return new ChangeAccountPasswordAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    //check that password is changed.
    ChangeAccountPasswordEvent chgEvent = (ChangeAccountPasswordEvent)event;
    String userID = (String)sm.getAttribute(IAttributeKeys.USER_ID);

    UserAccount acct = findUserAccountByUserId(userID);

    chgEvent.getNewPassword().applyMaskLength(acct.getPasswordMaskLength());

    assertEquals("Changed password incorrect", chgEvent.getNewPassword().toString(), acct.getPassword());
  }

  protected void prepareTestData() throws Exception
  {
    createUsers(2);
    createSessions(2);
    createStateMachines(2);

    _events = new ChangeAccountPasswordEvent[]
              {
                //null event
                null,
                //normal
                createTestEvent(_uIDs[0], PASSWORD, UPD_PASSWORD),
                //incorrect user
                createTestEvent(_uIDs[1], PASSWORD, UPD_PASSWORD),
                //non existing user
                createTestEvent(DUMMY_UID, PASSWORD, UPD_PASSWORD),
                //password not match
                createTestEvent(_uIDs[0], DUMMY_PASSWORD, UPD_PASSWORD),
                //normal
                createTestEvent(_uIDs[0], PASSWORD, UPD_PASSWORD),
              };
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void cleanUp()
  {
    deleteUsers(2);
  }

  // ************************ Utility methods *************************

  private ChangeAccountPasswordEvent createTestEvent(
    Long uID, String currPassword, String newPassword) throws Exception
  {
    return new ChangeAccountPasswordEvent(uID, currPassword, newPassword);
  }

  private void changeCheckFail(
    ChangeAccountPasswordEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.CHANGE_PASSWORD_ERROR);
  }

  private void changeCheckSuccess(
    ChangeAccountPasswordEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }
}