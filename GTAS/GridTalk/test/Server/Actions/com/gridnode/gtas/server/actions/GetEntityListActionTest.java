/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetEntityListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Ang Meng Hua        Created
 */
package com.gridnode.gtas.server.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.event.*;

import java.util.Collection;
import java.util.Iterator;

/**
 * This defines the interface for testing the GridTalk Action class.
 *
 * @author Ang Meng Hua
 *
 * @version 2.0
 * @since 2.0.2
 */
public abstract class GetEntityListActionTest extends EntityActionTest
{
  public static final String[] DEFAULT_LIST_ID = new String[1];
  public static final int DEFAULT_MAX_ROW      = 2;
  public static final int DEFAULT_START_ROW    = 1;

  protected DataFilterImpl _filter = null;

  public GetEntityListActionTest(String name)
  {
    super(name);
  }

  protected abstract DataFilterImpl getListFilter();

  protected short getDefaultMsgCode()
  {
    return IErrorCode.FIND_ENTITY_LIST_ERROR;
  }

  protected IEvent createTestEvent(IEntity entity) throws Exception
  {
    // no special handling requires here, simply return null
    return null;
  }

  protected IEvent createTestEvent(DataFilterImpl filter)
  {
    Object[] params = new Object[] { filter };
    Class[] paramClass = new Class[] { IDataFilter.class };
    return createTestEvent(getEventClass(), paramClass, params);
  }

  protected IEvent createTestEvent(DataFilterImpl filter, int maxRow)
  {
    Object[] params = new Object[] { filter, new Integer(maxRow) };
    Class[] paramClass = new Class[] { IDataFilter.class, Integer.TYPE };
    return createTestEvent(getEventClass(), paramClass, params);
  }

  protected IEvent createTestEvent(DataFilterImpl filter, int maxRow, int startRow)
  {
    Object[] params = new Object[] { filter, new Integer(maxRow), new Integer(startRow) };
    Class[] paramClass = new Class[] { IDataFilter.class, Integer.TYPE, Integer.TYPE };
    return createTestEvent(getEventClass(), paramClass, params);
  }

  protected IEvent createTestEvent(String listID, int maxRow, int startRow)
  {
    Object[] params = new Object[] { listID, new Integer(maxRow), new Integer(startRow) };
    Class[] paramClass = new Class[] { String.class, Integer.TYPE, Integer.TYPE };
    return createTestEvent(getEventClass(), paramClass, params);
  }

  /**
   * This simulates retrieving a list of entities with a retrieval condition
   * but no limiting number of rows to return. This should return all the records
   * that fit the retrieval condition.
   */
  public void testGetWithNoLimit() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".testGetWithNoLimit] Enter ");
    for (Iterator i=_testDataSet.values().iterator(); i.hasNext();)
      createEntity((IEntity)i.next());

    try
    {
      _response = performEvent(createTestEvent(getListFilter()));
    }
    catch (Exception ex)
    {
      Log.err("TEST", "["+getClassName()+".testGetWithNoLimit] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    assertOnNormalExit();
    assertOnGetListData(
      _response.getReturnData(),
      _testDataSet.values(),
      0,
      _testDataSet.size(),
      DEFAULT_LIST_ID);
    Log.log("TEST", "["+getClassName()+".testGetWithNoLimit] Exit ");
  }

  /**
   * This simulates retrieving a list of entities with a retrieval condition
   * and a maximum number of rows to return. This should return the first n number
   * of rows that satisfy the retrieval condition, where n <= max rows.
   */
  public void testGetWithLimit() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".testGetWithLimit] Enter ");
    for (Iterator i=_testDataSet.values().iterator(); i.hasNext();)
      createEntity((IEntity)i.next());

    try
    {
      _response = performEvent(createTestEvent(getListFilter(), DEFAULT_MAX_ROW));
    }
    catch (Exception ex)
    {
      Log.err("TEST", "["+getClassName()+".testGetWithLimit] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    assertOnNormalExit();
    assertOnGetListData(
      _response.getReturnData(),
      _testDataSet.values(),
      0,
      DEFAULT_MAX_ROW,
      DEFAULT_LIST_ID);
    Log.log("TEST", "["+getClassName()+".testGetWithLimit] Exit ");
  }

  /**
   * This simulates retrieving a list of entities with a retrieval condition,
   * specifying the maximum number of rows to return, and the starting row from
   * which list to return the results. This should return Row(i) - Row(m) entities
   * that satisfy that retrieval conditions, i = start row, m=n-i+1
   * and n <= max rows.
   */
  public void testGetStartWithLimit() throws Exception
  {
    Log.log("TEST", "["+getClassName()+".testGetStartWithLimit] Enter ");
    for (Iterator i=_testDataSet.values().iterator(); i.hasNext();)
      createEntity((IEntity)i.next());

    try
    {
      _response = performEvent(createTestEvent(
                                 getListFilter(), DEFAULT_MAX_ROW, DEFAULT_START_ROW));
    }
    catch (Exception ex)
    {
      Log.err("TEST", "["+getClassName()+".testGetStartWithLimit] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    assertOnNormalExit();
    assertOnGetListData(
      _response.getReturnData(),
      _testDataSet.values(),
      DEFAULT_START_ROW,
      DEFAULT_MAX_ROW,
      DEFAULT_LIST_ID);
   Log.log("TEST", "["+getClassName()+".testGetStartWithLimit] Exit ");
  }

  /**
   * Assert on the return entity list to verify that it matches the event
   * inputs and all return entities matches the simulated test data
   *
   * @param returnData the return entity list
   * @param fullList the entity list based on the filter condition set
   * @param startRow starting row of the first entity in the return list, with
   * respect to the entire list in the cursor.
   * @param maxRows the maximum number of rows in the return list
   * @param listID the cursor identifier
   */
  protected void assertOnGetListData(
    Object     returnData,
    Collection fullList,
    int        startRows,
    int        maxRows,
    String[]   listID)
  {
    assertNotNull("Null Response Data ", returnData);
    assertTrue(
      "Incorrect Response Data Type ",
      returnData instanceof EntityListResponseData);

    EntityListResponseData listData = (EntityListResponseData)returnData;
    int remainingRows = fullList.size() - maxRows - startRows;
    assertEquals("Incorrect remaining rows", remainingRows, listData.getRowsRemaining());
    assertEquals("Incorrect start Row", startRows, listData.getStartRow());
    assertNotNull("Null List ID", listData.getListID());
    listID[0] = listData.getListID();

    Collection entityList = listData.getEntityList();
    assertNotNull("Null Entity list", entityList);
    int numPage = (fullList.size() - startRows) / maxRows;
    int entitySize = 0;
    if (numPage == 0)
      entitySize = (fullList.size() - startRows) % maxRows;
    else
      entitySize = maxRows;
    assertEquals("Incorrect Entity List Count", entitySize, entityList.size());

    Object[] entityObjs = entityList.toArray();
    Object[] entities = fullList.toArray();
    for (int i = 0; i < entitySize; i++)
      assertOnListItem(entityObjs[i], (IEntity)entities[startRows + i]);
  }

  protected abstract void assertOnListItem(Object entityMap, IEntity entity);
}