/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetLanguageCodeListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 04 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.locale.actions;

import com.gridnode.gtas.model.locale.ILanguageCode;
import com.gridnode.gtas.events.locale.GetLanguageCodeListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.locale.helpers.ActionTestHelper;

import com.gridnode.pdip.base.locale.model.LanguageCode;

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
 * This Test case tests the GetLanguageCodeListAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetLanguageCodeListActionTest extends ActionTestHelper
{
  private static final int NUM_ROWS = 11; //11 rows of LanguageCode with Name 'A%'
  private static final int MAX_ROWS_PER_PAGE = 5;
  private static final int START_ROW = 3;

  private static final DataFilterImpl FILTER = new DataFilterImpl();
  private static final DataFilterImpl FILTER_CHECK = new DataFilterImpl();

  GetLanguageCodeListEvent[] _events;
  String _listID;

  public GetLanguageCodeListActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetLanguageCodeListActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *************************

  protected void cleanUp()
  {
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    FILTER.addSingleFilter(null, LanguageCode.NAME,
      FILTER.getLikeOperator(), "A", false);

    FILTER_CHECK.addSingleFilter(null, LanguageCode.NAME,
      FILTER.getLikeOperator(), "A", false);
    FILTER_CHECK.setOrderFields(new Number[]{LanguageCode.NAME});

    _languages = (LanguageCode[])findLanguageCodesByFilter(FILTER_CHECK).toArray(
                  new LanguageCode[0]);

    createSessions(1);
    createStateMachines(1);

    _events = new GetLanguageCodeListEvent[]
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
      GetLanguageCodeListEvent getEvent =
        new GetLanguageCodeListEvent(_listID, MAX_ROWS_PER_PAGE, i);
        getCheckSuccess(getEvent, _sessions[0], _sm[0]);
    }
  }

  protected IEJBAction createNewAction()
  {
    return new GetLanguageCodeListAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    GetLanguageCodeListEvent getEvent = (GetLanguageCodeListEvent)event;
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
      checkLanguageCode(entityObjs[i], _languages[expStart + i]);
    }
  }

  // ******************* Own methods ****************************

  private GetLanguageCodeListEvent createTestEvent(IDataFilter filter)
  {
    return new GetLanguageCodeListEvent(filter);
  }

  private GetLanguageCodeListEvent createTestEvent(
    IDataFilter filter, int maxRows)
  {
    return new GetLanguageCodeListEvent(filter, maxRows);
  }

  private GetLanguageCodeListEvent createTestEvent(
    IDataFilter filter, int maxRows, int startRow)
  {
    return new GetLanguageCodeListEvent(filter, maxRows, startRow);
  }

  private GetLanguageCodeListEvent createTestEvent(
    String listID, int maxRows, int startRow) throws Exception
  {
    return new GetLanguageCodeListEvent(listID, maxRows, startRow);
  }

  private void checkLanguageCode(Object entityObj, LanguageCode country)
  {
    assertNotNull("responsedata is null", entityObj);
    assertTrue("response data type incorrect", entityObj instanceof Map);

    Map countryMap = (Map)entityObj;

    checkLanguageCode(country, countryMap);
  }

  private void getCheckFail(
    GetLanguageCodeListEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_LIST_ERROR);
  }

  private void getCheckSuccess(
    GetLanguageCodeListEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}