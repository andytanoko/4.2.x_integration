/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetBusinessEntityListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.model.bizreg.IBusinessEntity;
import com.gridnode.gtas.model.bizreg.IWhitePage;
import com.gridnode.gtas.events.bizreg.GetBusinessEntityListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.bizreg.helpers.ActionTestHelper;

import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.WhitePage;

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
 * This Test case tests the GetBusinessEntityListAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class GetBusinessEntityListActionTest extends ActionTestHelper
{
  private static final int NUM_ROWS = 9;
  private static final int NUM_PARTNERS = 6;
  private static final int MAX_ROWS_PER_PAGE = 4;
  private static final int START_ROW = 3;

  private static final ArrayList TEST_IDS = new ArrayList();
  private static final DataFilterImpl FILTER = new DataFilterImpl();

  GetBusinessEntityListEvent[] _events;
  String _listID;

  public GetBusinessEntityListActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetBusinessEntityListActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *************************

  protected void cleanUp()
  {
    cleanUpBEs(null);
    cleanUpBEs(ENTERPRISE);
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createBes(NUM_ROWS, NUM_PARTNERS);

    for (int i=0; i<NUM_ROWS; i++)
      TEST_IDS.add(_bizEntities[i].getBusEntId());

    FILTER.addDomainFilter(null, BusinessEntity.ID, TEST_IDS, false);
    FILTER.setOrderFields(new Number[]{BusinessEntity.UID});

    createSessions(1);
    createStateMachines(1);

    _events = new GetBusinessEntityListEvent[]
              {
                //accepted
                createTestEvent(FILTER),
                createTestEvent(FILTER, MAX_ROWS_PER_PAGE),
                createTestEvent(FILTER, MAX_ROWS_PER_PAGE, START_ROW),
                createTestEvent(FILTER, MAX_ROWS_PER_PAGE, 0),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** REJECTED ***************************

    // ************** ACCEPTED *****************************
    getCheckSuccess(_events[0], _sessions[0], _sm[0]);
    getCheckSuccess(_events[1], _sessions[0], _sm[0]);
    getCheckSuccess(_events[2], _sessions[0], _sm[0]);

    getCheckSuccess(_events[3], _sessions[0], _sm[0]);
    for (int i=0; i<NUM_ROWS; i+=MAX_ROWS_PER_PAGE)
    {
      GetBusinessEntityListEvent getEvent =
        new GetBusinessEntityListEvent(_listID, MAX_ROWS_PER_PAGE, i);
        getCheckSuccess(getEvent, _sessions[0], _sm[0]);
    }
  }

  protected IEJBAction createNewAction()
  {
    return new GetBusinessEntityListAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    GetBusinessEntityListEvent getEvent = (GetBusinessEntityListEvent)event;
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
      checkBusinessEntity(entityObjs[i], _bizEntities[expStart + i]);
    }
  }

  // ******************* Own methods ****************************

  private GetBusinessEntityListEvent createTestEvent(IDataFilter filter)
  {
    return new GetBusinessEntityListEvent(filter);
  }

  private GetBusinessEntityListEvent createTestEvent(
    IDataFilter filter, int maxRows)
  {
    return new GetBusinessEntityListEvent(filter, maxRows);
  }

  private GetBusinessEntityListEvent createTestEvent(
    IDataFilter filter, int maxRows, int startRow)
  {
    return new GetBusinessEntityListEvent(filter, maxRows, startRow);
  }

  private GetBusinessEntityListEvent createTestEvent(
    String listID, int maxRows, int startRow) throws Exception
  {
    return new GetBusinessEntityListEvent(listID, maxRows, startRow);
  }

  private void checkBusinessEntity(Object entityObj, BusinessEntity be)
  {
    assertNotNull("responsedata is null", entityObj);
    assertTrue("response data type incorrect", entityObj instanceof Map);

    Map beMap = (Map)entityObj;

    Object wpObj = beMap.get(IBusinessEntity.WHITE_PAGE);
    assertNotNull("whitepage is null", wpObj);
    assertTrue("whitepage data type incorrect", wpObj instanceof Map);

    Map wpMap = (Map)wpObj;

    for (Iterator i=wpMap.keySet().iterator(); i.hasNext(); )
    {
      Object key = i.next();
      Log.debug("TEST", "Key="+key + ",value="+wpMap.get(key));
    }

    checkBe(be, beMap);
    checkWp(be.getWhitePage(), wpMap, true);
  }

  private void getCheckFail(
    GetBusinessEntityListEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_LIST_ERROR);
  }

  private void getCheckSuccess(
    GetBusinessEntityListEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}