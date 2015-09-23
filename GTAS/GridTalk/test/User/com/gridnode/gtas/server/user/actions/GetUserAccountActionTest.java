/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetUserAccountActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 03 2002    Neo Sok Lay         Created
 * Jun 20 2002    Neo Sok Lay         Re-org test case.
 */
package com.gridnode.gtas.server.user.actions;

import com.gridnode.gtas.model.user.IUserAccount;
import com.gridnode.gtas.model.user.IUserAccountState;
import com.gridnode.gtas.events.user.GetUserAccountEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.user.helpers.ActionTestHelper;

import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.app.user.model.UserAccountState;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.PasswordMask;

import junit.framework.*;

import java.util.Map;
import java.util.Iterator;

/**
 * This Test case tests the GetUserAccountAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class GetUserAccountActionTest extends ActionTestHelper
{
  GetUserAccountEvent[] _events;

  public GetUserAccountActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetUserAccountActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *********************

  protected void cleanUp()
  {
    deleteUsers(2);
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createUsers(2);
    createSessions(1);
    createStateMachines(1);

    _events = new GetUserAccountEvent[]
              {
                //rejected
                null,
                createTestEvent(new Long(_accts[0].getUId())),
                createTestEvent(DUMMY_UID),

                //accepted
                createTestEvent(new Long(_accts[1].getUId())),
              };
  }

  protected void unitTest() throws Exception
  {
    signon(_sessions[0], _accts[0].getId(), PASSWORD, _sm[0]);

    // ************** REJECTED ***************************
    //null event
    getCheckFail(_events[0], _sessions[0], _sm[0], true);

    //invalid session
    getCheckFail(_events[1], "", _sm[0], true);

    //non existing user
    getCheckFail(_events[2], _sessions[0], _sm[0], false);

    // ************** ACCEPTED *****************************
    getCheckSuccess(_events[3], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new GetUserAccountAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    Object returnData = response.getReturnData();
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof Map);

    Map acctMap = (Map)returnData;

    Object stateObj = acctMap.get(IUserAccount.ACCOUNT_STATE);
    assertNotNull("account state is null", stateObj);
    assertTrue("account state data type incorrect", stateObj instanceof Map);

    Map stateMap = (Map)stateObj;

    for (Iterator i=stateMap.keySet().iterator(); i.hasNext(); )
    {
      Object key = i.next();
      Log.debug("TEST", "Key="+key + ",value="+stateMap.get(key));
    }

    GetUserAccountEvent getEvent = (GetUserAccountEvent)event;
    UserAccount acct = findUserAccountByUId(getEvent.getAccountUID());

    assertEquals("UID is incorrect", acct.getKey(), acctMap.get(IUserAccount.UID));
    assertEquals("Id incorrect", acct.getId(), acctMap.get(IUserAccount.ID));
    assertEquals("Password incorrect", acct.getPassword(), acctMap.get(IUserAccount.PASSWORD));
    assertEquals("Name is incorrect", acct.getUserName(), acctMap.get(IUserAccount.NAME));
    assertEquals("Phone is incorrect", acct.getPhone(), acctMap.get(IUserAccount.PHONE));
    assertEquals("Email is incorrect", acct.getEmail(), acctMap.get(IUserAccount.EMAIL));
    assertEquals("Property is incorrect", acct.getProperty(), acctMap.get(IUserAccount.PROPERTY));
    assertEquals("State is incorrect", new Short(acct.getAccountState().getState()), stateMap.get(IUserAccountState.STATE));
    assertEquals("Create by is incorrect", acct.getAccountState().getCreateBy(), stateMap.get(IUserAccountState.CREATE_BY));
  }


  // ********************* Utiltity methods *************************
  private GetUserAccountEvent createTestEvent(Long acctUId) throws Exception
  {
    return new GetUserAccountEvent(acctUId);
  }

  private void getCheckFail(
    GetUserAccountEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_BY_KEY_ERROR);
  }

  private void getCheckSuccess(
    GetUserAccountEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}