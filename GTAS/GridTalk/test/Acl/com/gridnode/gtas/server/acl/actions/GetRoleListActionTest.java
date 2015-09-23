/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRoleListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;

import com.gridnode.pdip.base.acl.model.Role;

import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.events.acl.GetRoleListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.acl.helpers.ActionTestHelper;

import junit.framework.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class GetRoleListActionTest extends ActionTestHelper
{
  private static final int NUM_ROWS = 9;
  private static final int MAX_ROWS_PER_PAGE = 4;
  private static final int START_ROW = 3;

  private static final ArrayList TEST_IDS = new ArrayList();
  private static final DataFilterImpl FILTER = new DataFilterImpl();

  GetRoleListEvent[] _events;
  String _listID;

  public GetRoleListActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetRoleListActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void prepareTestData() throws Exception
  {
    createRoles(NUM_ROWS);

    for (int i=0; i<NUM_ROWS; i++)
      TEST_IDS.add(_roles[i].getRole());

    FILTER.addDomainFilter(null, Role.ROLE, TEST_IDS, false);
    FILTER.setOrderFields(new Number[]{Role.UID});

    createSessions(1);
    createStateMachines(1);

    _events = new GetRoleListEvent[]
              {
                //rejected
                null,
                getRoleListEvent(FILTER),

                //accepted
                getRoleListEvent(FILTER),
                getRoleListEvent(FILTER, MAX_ROWS_PER_PAGE),
                getRoleListEvent(FILTER, MAX_ROWS_PER_PAGE, START_ROW),
                getRoleListEvent(FILTER, MAX_ROWS_PER_PAGE, 0),
              };
  }

  protected void cleanUp()
  {
    deleteRoles(NUM_ROWS);
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void unitTest() throws Exception
  {
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
      GetRoleListEvent getEvent =
        new GetRoleListEvent(_listID, MAX_ROWS_PER_PAGE, i);
        getCheckSuccess(getEvent, _sessions[0], _sm[0]);
    }
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    GetRoleListEvent getEvent = (GetRoleListEvent)event;
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
      checkRole(entityObjs[i], _roles[expStart + i]);
    }
  }

  protected IEJBAction createNewAction()
  {
    return new GetRoleListAction();
  }

  // ******************** Own methods ****************************

  private GetRoleListEvent getRoleListEvent()
  {
    return new GetRoleListEvent();
  }

  private GetRoleListEvent getRoleListEvent(DataFilterImpl filter)
  {
    return new GetRoleListEvent(filter);
  }

  private GetRoleListEvent getRoleListEvent(DataFilterImpl filter, int maxRow)
  {
    return new GetRoleListEvent(filter, maxRow);
  }

  private GetRoleListEvent getRoleListEvent(DataFilterImpl filter, int maxRow, int startRow)
  {
    return new GetRoleListEvent(filter, maxRow, startRow);
  }

  private GetRoleListEvent getRoleListEvent(String listID, int maxRow, int startRow)
    throws Exception
  {
    return new GetRoleListEvent(listID, maxRow, startRow);
  }

  private void getCheckFail(
    IEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_LIST_ERROR);
  }

  private void getCheckSuccess(
    IEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private void checkRole(Object entityObject, Role role)
  {
    assertNotNull("Entity is null", entityObject);
    assertTrue("Entity data type incorrect", entityObject instanceof Map);

    Map roleMap = (Map) entityObject;

    assertEquals("Role is incorrect", role.getRole(), roleMap.get(Role.ROLE));
    assertEquals("Description is incorrect", role.getDescr(), roleMap.get(Role.DESCRIPTION));
    assertEquals("CanDelete is incorrect",
                            role.canDelete() ? Boolean.TRUE : Boolean.FALSE,
                            roleMap.get(Role.CAN_DELETE));
  }

}