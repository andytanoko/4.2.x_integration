/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGnCategoryListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 09 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.actions;

import com.gridnode.gtas.model.gridnode.IGnCategory;
import com.gridnode.gtas.events.gridnode.GetGnCategoryListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.gridnode.helpers.ActionTestHelper;
import com.gridnode.gtas.server.gridnode.model.GnCategory;

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
 * This Test case tests the GetGnCategoryListAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetGnCategoryListActionTest extends ActionTestHelper
{
  private static final int NUM_ROWS = 2; //2 rows of GnCategory with Name '%ial%'
  private static final int MAX_ROWS_PER_PAGE = 10;
  private static final int START_ROW = 3;

  private static final DataFilterImpl FILTER = new DataFilterImpl();
  private static final DataFilterImpl FILTER_CHECK = new DataFilterImpl();

  GetGnCategoryListEvent[] _events;
  String _listID;

  public GetGnCategoryListActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetGnCategoryListActionTest.class);
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
    FILTER.addSingleFilter(null, GnCategory.CATEGORY_NAME,
      FILTER.getLocateOperator(), "ial", false);

    FILTER_CHECK.addSingleFilter(null, GnCategory.CATEGORY_NAME,
      FILTER.getLocateOperator(), "ial", false);
    FILTER_CHECK.setOrderFields(new Number[]{GnCategory.CATEGORY_NAME});

    _categories = (GnCategory[])findGnCategoriesByFilter(FILTER_CHECK).toArray(
                  new GnCategory[0]);

    createSessions(1);
    createStateMachines(1);

    _events = new GetGnCategoryListEvent[]
              {
                //accepted
                createTestEvent(FILTER),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** REJECTED ***************************

    // ************** ACCEPTED *****************************
    getCheckSuccess(_events[0], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new GetGnCategoryListAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    GetGnCategoryListEvent getEvent = (GetGnCategoryListEvent)event;
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
      checkGnCategory(entityObjs[i], _categories[expStart + i]);
    }
  }

  // ******************* Own methods ****************************

  private GetGnCategoryListEvent createTestEvent(IDataFilter filter)
  {
    return new GetGnCategoryListEvent(filter);
  }

  private void checkGnCategory(Object entityObj, GnCategory country)
  {
    assertNotNull("responsedata is null", entityObj);
    assertTrue("response data type incorrect", entityObj instanceof Map);

    Map countryMap = (Map)entityObj;

    checkGnCategory(country, countryMap);
  }

  private void getCheckFail(
    GetGnCategoryListEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_LIST_ERROR);
  }

  private void getCheckSuccess(
    GetGnCategoryListEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}