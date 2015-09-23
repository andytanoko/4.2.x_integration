/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetUserAccountListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 03 2002    Neo Sok Lay         Created
 * Jun 21 2002    Neo Sok Lay         Re-org test case.
 */
package com.gridnode.gtas.server.user.actions;

import com.gridnode.gtas.model.user.IUserAccount;
import com.gridnode.gtas.model.user.IUserAccountState;
import com.gridnode.gtas.events.user.GetUserAccountListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.user.helpers.ActionTestHelper;

import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.app.user.model.UserAccountState;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import junit.framework.*;

import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This Test case tests the GetUserAccountListAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class GetUserAccountListActionTest extends ActionTestHelper
{
  private static final int NUM_ROWS = 9;
  private static final int MAX_ROWS_PER_PAGE = 4;
  private static final int START_ROW = 3;

  private static final ArrayList TEST_IDS = new ArrayList();
  private static final DataFilterImpl FILTER = new DataFilterImpl();

  GetUserAccountListEvent[] _events;
  String _listID;

  public GetUserAccountListActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetUserAccountListActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *************************

  protected void cleanUp()
  {
    deleteUsers(NUM_ROWS);
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createUsers(NUM_ROWS);

    for (int i=0; i<NUM_ROWS; i++)
      TEST_IDS.add(_accts[i].getId());

    FILTER.addDomainFilter(null, UserAccount.ID, TEST_IDS, false);
    FILTER.setOrderFields(new Number[]{UserAccount.UID});

    createSessions(1);
    createStateMachines(1);

    _events = new GetUserAccountListEvent[]
              {
                //rejected
                null,
                createTestEvent(FILTER),

                //accepted
                createTestEvent(FILTER),
                createTestEvent(FILTER, MAX_ROWS_PER_PAGE),
                createTestEvent(FILTER, MAX_ROWS_PER_PAGE, START_ROW),
                createTestEvent(FILTER, MAX_ROWS_PER_PAGE, 0),
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

    // ************** ACCEPTED *****************************
    getCheckSuccess(_events[2], _sessions[0], _sm[0]);
    getCheckSuccess(_events[3], _sessions[0], _sm[0]);
    getCheckSuccess(_events[4], _sessions[0], _sm[0]);

    getCheckSuccess(_events[5], _sessions[0], _sm[0]);
    for (int i=0; i<NUM_ROWS; i+=MAX_ROWS_PER_PAGE)
    {
      GetUserAccountListEvent getEvent =
        new GetUserAccountListEvent(_listID, MAX_ROWS_PER_PAGE, i);
        getCheckSuccess(getEvent, _sessions[0], _sm[0]);
    }
  }

  protected IEJBAction createNewAction()
  {
    return new GetUserAccountListAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    GetUserAccountListEvent getEvent = (GetUserAccountListEvent)event;
    assertNotNull("responsedata is null", response.getReturnData());
    assertTrue("response data type incorrect", response.getReturnData() instanceof EntityListResponseData);

    EntityListResponseData listData = (EntityListResponseData) response.getReturnData();

    int expStart = getEvent.getStartRow();
    int expRows = getEvent.getMaxRows() > 0 ?
                  (NUM_ROWS-expStart >= getEvent.getMaxRows() ?
                   getEvent.getMaxRows() :
                   NUM_ROWS-expStart) :
                  NUM_ROWS;

    int remainingRows = NUM_ROWS - expStart - expRows;
    assertEquals("Rows remaining is incorrect", remainingRows, listData.getRowsRemaining());
    assertEquals("start row is incorrect", expStart, listData.getStartRow());
    assertNotNull("List ID is null", listData.getListID());
    _listID = listData.getListID();

    Collection entityList = listData.getEntityList();
    assertNotNull("Entity list is null", entityList);

    int numPage = (int)Math.ceil((double) (NUM_ROWS - expStart) / (double)MAX_ROWS_PER_PAGE);
    assertEquals("Entity list count is incorrect", expRows, entityList.size());

    Object[] entityObjs = entityList.toArray();
    for (int i = 0; i < expRows; i++ )
    {
      checkAccount(entityObjs[i], _accts[expStart + i]);
    }
  }

  // ******************* Own methods ****************************

  private GetUserAccountListEvent createTestEvent(IDataFilter filter)
  {
    return new GetUserAccountListEvent(filter);
  }

  private GetUserAccountListEvent createTestEvent(
    IDataFilter filter, int maxRows)
  {
    return new GetUserAccountListEvent(filter, maxRows);
  }

  private GetUserAccountListEvent createTestEvent(
    IDataFilter filter, int maxRows, int startRow)
  {
    return new GetUserAccountListEvent(filter, maxRows, startRow);
  }

  private GetUserAccountListEvent createTestEvent(
    String listID, int maxRows, int startRow) throws Exception
  {
    return new GetUserAccountListEvent(listID, maxRows, startRow);
  }

  private void checkAccount(Object entityObj, UserAccount acct)
  {
    assertNotNull("responsedata is null", entityObj);
    assertTrue("response data type incorrect", entityObj instanceof Map);

    Map acctMap = (Map)entityObj;

    Object stateObj = acctMap.get(IUserAccount.ACCOUNT_STATE);
    assertNotNull("account state is null", stateObj);
    assertTrue("account state data type incorrect", stateObj instanceof Map);

    Map stateMap = (Map)stateObj;

    for (Iterator i=stateMap.keySet().iterator(); i.hasNext(); )
    {
      Object key = i.next();
      Log.debug("TEST", "Key="+key + ",value="+stateMap.get(key));
    }

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

  private void getCheckFail(
    GetUserAccountListEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_LIST_ERROR);
  }

  private void getCheckSuccess(
    GetUserAccountListEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}