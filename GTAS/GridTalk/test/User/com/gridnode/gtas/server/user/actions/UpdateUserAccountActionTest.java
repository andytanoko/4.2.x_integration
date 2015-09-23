/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateUserAccountActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 03 2002    Neo Sok Lay         Created
 * Jun 20 2002    Neo Sok Lay         Re-org test case.
 */
package com.gridnode.gtas.server.user.actions;

import com.gridnode.gtas.events.user.UpdateUserAccountEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.user.helpers.ActionTestHelper;

import com.gridnode.pdip.app.user.model.*;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.PasswordMask;

import junit.framework.*;
import java.util.Date;
import java.sql.Timestamp;

/**
 * This test cases tests the UpdateUserAccountAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateUserAccountActionTest extends ActionTestHelper
{
  UpdateUserAccountEvent[] _events;

  public UpdateUserAccountActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(UpdateUserAccountActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ******************* ActionTestHelper methods **********************

  protected void cleanUp()
  {
    deleteUsers(4);
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createUsers(3);

//    Long adminRole = setupAdminRole();
//    _aclMgr.assignRoleToSubject(adminRole, _uIDs[0], UserAccount.ENTITY_NAME);

    createSessions(1);
    createStateMachines(1);

    _events = new UpdateUserAccountEvent[]
              {
                //rejected
                null,
                createTestEvent(_uIDs[0], false, false),
                createTestEvent(DUMMY_UID, false, false),

                //accepted
                createTestEvent(_uIDs[0], false, false),
                createTestEvent(_uIDs[1], true, false),
                createTestEvent(_uIDs[2], true, true),
              };

    for (int i=1; i<3; i++)
    {
      _accts[i].getAccountState().setIsFreeze(IS_FREEZE);
      _accts[i].getAccountState().setFreezeTime(FREEZE_TIME);
      _accts[i].getAccountState().setNumLoginTries(LOGIN_TRIES);
      _userMgr.updateUserAccount(_accts[i]);
    }
  }

  protected void unitTest() throws Exception
  {
    signon(_sessions[0], _accts[0].getId(), PASSWORD, _sm[0]);

    // ************** REJECTED ***************************
    //null event
    updateCheckFail(_events[0], _sessions[0], _sm[0], true);

    //invalid session
    updateCheckFail(_events[1], "", _sm[0], true);

    //non existing user
    updateCheckFail(_events[2], _sessions[0], _sm[0], true);

    // ************** ACCEPTED *****************************
    //change user profile only
    updateCheckSuccess(_events[3], _sessions[0], _sm[0]);

    //change state w/o password
    updateCheckSuccess(_events[4], _sessions[0], _sm[0]);
    //change state with password
    updateCheckSuccess(_events[5], _sessions[0], _sm[0]);

  }

  protected IEJBAction createNewAction()
  {
    return new UpdateUserAccountAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    UpdateUserAccountEvent updEvent = (UpdateUserAccountEvent)event;

    UserAccount acct = findUserAccountByUId(updEvent.getAccountUID());
    assertNotNull("Acct retrieved is null", acct);

    UserAccountState acctState = acct.getAccountState();
    assertNotNull("Acct state is null", acctState);

    assertNotNull("UID is null", acct.getFieldValue(UserAccount.UID));

    if (updEvent.getResetPassword() != null)
    {
      updEvent.getResetPassword().applyMaskLength(acct.getPasswordMaskLength());

      assertEquals("Password incorrect",
        updEvent.getResetPassword().toString(),
        acct.getPassword());
    }
    else
    {
      assertEquals("Password was changed",
        new PasswordMask(PASSWORD, acct.getPasswordMaskLength()).toString(),
        acct.getPassword());
    }

    assertEquals("Name is incorrect", updEvent.getUpdUserName(), acct.getUserName());
    assertEquals("Phone is incorrect", updEvent.getUpdPhone(), acct.getPhone());
    assertEquals("Email is incorrect", updEvent.getUpdEmail(), acct.getEmail());
    assertEquals("Property is incorrect", updEvent.getUpdProperty(), acct.getProperty());

    boolean admin = updEvent.getUpdState()!=null;

    if (admin)
    {
      assertEquals("State is incorrect",  updEvent.getUpdState(), new Short(acctState.getState()));
      assertEquals("IsFreeze is incorrect",
        (UPD_UNFREEZE)?false:IS_FREEZE, acctState.isFreeze());
      if (UPD_UNFREEZE)
         assertNull("FreezeTime is not null", acctState.getFreezeTime());
      else
         assertEquals("FreezeTime is incorrect", FREEZE_TIME.getTime(),
          acctState.getFreezeTime().getTime());

      assertEquals("Login Tries is incorrect",
          UPD_RESET_TRIES?0:LOGIN_TRIES, acctState.getNumLoginTries());
    }
    else
    {
      assertEquals("State is incorrect",  UserAccountState.STATE_ENABLED, acctState.getState());
      assertEquals("IsFreeze is incorrect", false, acctState.isFreeze());
      assertNull("FreezeTime is not null", acctState.getFreezeTime());
      assertEquals("Login Tries is incorrect", 0, acctState.getNumLoginTries());
    }

  }


  // ************* Utility methods ***************************

  private UpdateUserAccountEvent createTestEvent(
    Long uID, boolean admin, boolean resetPassword) throws Exception
  {
    if (admin)
    {
      return new UpdateUserAccountEvent(uID, UPD_NAME, UPD_PHONE, UPD_EMAIL,
                   UPD_PROPERTY, UPD_UNFREEZE, UPD_RESET_TRIES, UPD_STATE,
                   resetPassword?UPD_PASSWORD:null);
    }
    else
    {
      return new UpdateUserAccountEvent(uID, UPD_NAME, UPD_PHONE, UPD_EMAIL,
                   UPD_PROPERTY);
    }
  }

  private void updateCheckFail(
    UpdateUserAccountEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.UPDATE_ENTITY_ERROR);
  }

  private void updateCheckSuccess(
    UpdateUserAccountEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }
}