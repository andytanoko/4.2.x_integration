/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMappingFileListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 03 2002    Neo Sok Lay         Created
 * Jun 28 2002    Koh Han Sing        Modified to use for
 *                                    GetMappingFileListAction
 */
package com.gridnode.gtas.server.mapper.actions;

import com.gridnode.gtas.model.mapper.IMappingFile;
import com.gridnode.gtas.events.mapper.GetMappingFileListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;

import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.util.PasswordMask;

import junit.framework.*;

import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This Test case tests the GetMappingFileListActionTest class.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class GetMappingFileListActionTest extends TestCase
{
  static final String _MAPPING_FILE_NAME = "Test GetList Name";
  static final String _MAPPING_FILE_DESC = "Test GetList Description";
  static final String _MAPPING_FILE_PATH = "Test GetList Path";
  static final Short  _MAPPING_FILE_TYPE = MappingFile.XSL;

  static final int _LIST_COUNT = 9;
  static final int _MAX_PAGE_ENTRIES = 4;
  static final int _START_ROW = 6;
  static final ArrayList _TEST_MAPPINGFILES = new ArrayList();
  static final DataFilterImpl _FILTER = new DataFilterImpl();
  static
  {
    for (int i=0; i<_LIST_COUNT; i++)
      _TEST_MAPPINGFILES.add(_MAPPING_FILE_NAME+i);

    _FILTER.addDomainFilter(null, MappingFile.NAME, _TEST_MAPPINGFILES, false);
    _FILTER.setOrderFields(new Number[] {MappingFile.UID});
  }

  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  GetMappingFileListEvent _event;
  IMappingManagerHome _home;
  IMappingManagerObj _remote;
  MappingFile _mappingFile;
  StateMachine _sm = new StateMachine(null, null);

  public GetMappingFileListActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetMappingFileListActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[GetMappingFileListActionTest.setUp] Enter");

    _sessionHome = (ISessionManagerHome)ServiceLookup.getInstance(
                   ServiceLookup.CLIENT_CONTEXT).getHome(
                   ISessionManagerHome.class);
    assertNotNull("SessionHome is null", _sessionHome);
    _sessionRemote = _sessionHome.create();
    assertNotNull("SessionRemote is null", _sessionRemote);
    _sessionID = _sessionRemote.openSession();
    _sessionRemote.authSession(_sessionID, _userID);

    _home = (IMappingManagerHome)ServiceLookup.getInstance(
             ServiceLookup.CLIENT_CONTEXT).getHome(
             IMappingManagerHome.class);
    assertNotNull("Home is null", _home);
    _remote = _home.create();
    assertNotNull("remote is null", _remote);

    deleteTestData(); //prevent error in create
    for (int i=0; i<_LIST_COUNT; i++)
      createTestData(_MAPPING_FILE_NAME+i,
                     _MAPPING_FILE_DESC+i,
                     _MAPPING_FILE_PATH+i,
                     _MAPPING_FILE_TYPE);

    Log.log("TEST", "[GetMappingFileListActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[GetMappingFileListActionTest.tearDown] Enter");
    deleteTestData();
    Log.log("TEST", "[GetMappingFileListActionTest.tearDown] Exit");
  }

  private GetMappingFileListEvent createTestEvent(IDataFilter filter)
  {
    return new GetMappingFileListEvent(filter);
  }

  private GetMappingFileListEvent createTestEvent(
    IDataFilter filter, int maxRows)
  {
    return new GetMappingFileListEvent(filter, maxRows);
  }

  private GetMappingFileListEvent createTestEvent(
    IDataFilter filter, int maxRows, int startRow)
  {
    return new GetMappingFileListEvent(filter, maxRows, startRow);
  }

  private GetMappingFileListEvent createTestEvent(
    String listID, int maxRows, int startRow) throws Exception
  {
    return new GetMappingFileListEvent(listID, maxRows, startRow);
  }

  private BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    GetMappingFileListAction action = new GetMappingFileListAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _sessionID);
    _sm.setAttribute(IAttributeKeys.USER_ID, _userID);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  private void deleteTestData()
  {
    Log.log("TEST", "[GetMappingFileListActionTest.deleteTestData] Enter");

    //find existing acct
    try
    {
      Collection mappingFiles = _remote.findMappingFilesKeys(_FILTER);
      if (mappingFiles != null && !mappingFiles.isEmpty())
      {
        for (Iterator i=mappingFiles.iterator(); i.hasNext(); )
          _remote.deleteMappingFile((Long)i.next());
      }
    }
    catch (Exception ex)
    {

    }
    Log.log("TEST", "[GetMappingFileListActionTest.deleteTestData] Exit");
  }

  private void createTestData(String name, String desc, String path, Short type)
  {
    Log.log("TEST", "[GetMappingFileListActionTest.createTestData] Enter");

    _mappingFile = new MappingFile();
    _mappingFile.setName(name);
    _mappingFile.setDescription(desc);
    _mappingFile.setPath(path);
    _mappingFile.setType(type);
    try
    {
      _remote.createMappingFile(_mappingFile);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "GetMappingFileListActionTest.createTestData]", ex);
      assertTrue("Error in createTestData", false);
    }
    Log.log("TEST", "[GetMappingFileListActionTest.createTestData] Exit");
  }

  private Collection findMappingFilesKeys(IDataFilter filter)
  {
    Log.log("TEST", "[GetMappingFileListActionTest.findMappingFiles] Enter");

    Collection keys = null;
    try
    {
      keys = _remote.findMappingFilesKeys(filter);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[GetMappingFileListActionTest.findMappingFiles]", ex);
      assertTrue("Error in remote.findMappingFiles", false);
    }

    Log.log("TEST", "[GetUserAccountListActionTest.findUserAccountKeys] Exit");
    return keys;
  }

  /**
   * This simulates performing with a Null event input. This should give a
   * System type error in the event response.
   */
  public void testNullGetEvent() throws Exception
  {
    Log.log("TEST", "[GetMappingFileListActionTest.testNullGetEvent] Enter ");

    try
    {
      _response = performEvent(null);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[GetMappingFileListActionTest.testNullGetEvent]" +
        " Returning fail due to EventException: "+ex.getMessage());
      Log.log("TEST", "[GetUserAcccountListActionTest.testNullGetEvent] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[GetUserAccountListActionTeset.testNullGetEvent] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.FIND_ENTITY_LIST_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertNotNull("Error trace is null", _response.getErrorTrace());
    assertEquals("error type is incorrect", _response.getErrorType(), SystemException.SYSTEM);
    Log.log("TEST", "[GetMappingFileListActionTest.testNullGetEvent] Exit ");
  }

  /**
   * This simulates retrieving a list of user accounts with a retrieval condition
   * but no limiting number of rows to return. This should return all the records
   * that fit the retrieval condition.
   */
  public void testGetWithNoLimit() throws Exception
  {
    Log.log("TEST", "[GetMappingFileListActionTest.testGetWithNoLimit] Enter ");

//    for (int i=0; i<LIST_COUNT; i++)
//      createTestData(_DOC_TYPE+i, _DOC_DESC+i);

    Long[] uIDs = (Long[])findMappingFilesKeys(_FILTER).toArray(new Long[_LIST_COUNT]);

    _event = createTestEvent(_FILTER);
    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[GetUserAccountListActionTeset.testGetWithNoLimit] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    Object returnData = _response.getReturnData();
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof EntityListResponseData);

    EntityListResponseData listData = (EntityListResponseData)returnData;

    assertEquals("Rows remaining is incorrect", 0, listData.getRowsRemaining());
    assertEquals("start row is incorrect", 0, listData.getStartRow());
    assertNotNull("List ID is null", listData.getListID());

    Collection entityList = listData.getEntityList();
    assertNotNull("Entity list is null", entityList);
    assertEquals("Entity list count is incorrect", _LIST_COUNT, entityList.size());

    Object[] entityObjs = entityList.toArray();
    for (int i=0; i<_LIST_COUNT; i++ )
    {
      assertNotNull("Entity is null", entityObjs[i]);
      assertTrue("Entity is not Map", entityObjs[i] instanceof Map);

      Map mappingFileMap = (Map)entityObjs[i];

      assertEquals("UID is incorrect", uIDs[i], mappingFileMap.get(IMappingFile.UID));
      assertEquals("name incorrect", _MAPPING_FILE_NAME+i, mappingFileMap.get(IMappingFile.NAME));
      assertEquals("desc incorrect", _MAPPING_FILE_DESC+i, mappingFileMap.get(IMappingFile.DESCRIPTION));
      assertEquals("path incorrect", _MAPPING_FILE_PATH+i, mappingFileMap.get(IMappingFile.PATH));
      assertEquals("type incorrect", _MAPPING_FILE_TYPE, mappingFileMap.get(IMappingFile.TYPE));
    }
    Log.log("TEST", "[GetMappingFileListActionTest.testGetWithNoLimit] Exit ");
  }

  /**
   * This simulates retrieving a list of user accounts with a retrieval condition
   * and a maximum number of rows to return. This should return the first n number
   * of rows that satisfy the retrieval condition, where n <= max rows.
   */
  public void testGetWithLimit() throws Exception
  {
    Log.log("TEST", "[GetMappingFileListActionTest.testGetWithLimit] Enter ");

//    for (int i=0; i<LIST_COUNT; i++)
//      createTestData(_DOC_TYPE+i, _DOC_DESC+i);

    Long[] uIDs = (Long[])findMappingFilesKeys(_FILTER).toArray(new Long[_LIST_COUNT]);

    _event = createTestEvent(_FILTER, _MAX_PAGE_ENTRIES);
    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[GetUserAccountListActionTeset.testGetWithLimit] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    Object returnData = _response.getReturnData();
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof EntityListResponseData);

    EntityListResponseData listData = (EntityListResponseData)returnData;

    assertEquals("Rows remaining is incorrect", _LIST_COUNT-_MAX_PAGE_ENTRIES, listData.getRowsRemaining());
    assertEquals("start row is incorrect", 0, listData.getStartRow());
    assertNotNull("List ID is null", listData.getListID());

    Collection entityList = listData.getEntityList();
    assertNotNull("Entity list is null", entityList);
    assertEquals("Entity list count is incorrect", _MAX_PAGE_ENTRIES, entityList.size());

    Object[] entityObjs = entityList.toArray();
    for (int i=0; i<_MAX_PAGE_ENTRIES; i++ )
    {
      assertNotNull("Entity is null", entityObjs[i]);
      assertTrue("Entity is not Map", entityObjs[i] instanceof Map);

      Map mappingFileMap = (Map)entityObjs[i];

      assertEquals("UID is incorrect", uIDs[i], mappingFileMap.get(IMappingFile.UID));
      assertEquals("name incorrect", _MAPPING_FILE_NAME+i, mappingFileMap.get(IMappingFile.NAME));
      assertEquals("desc incorrect", _MAPPING_FILE_DESC+i, mappingFileMap.get(IMappingFile.DESCRIPTION));
      assertEquals("path incorrect", _MAPPING_FILE_PATH+i, mappingFileMap.get(IMappingFile.PATH));
      assertEquals("type incorrect", _MAPPING_FILE_TYPE, mappingFileMap.get(IMappingFile.TYPE));
    }
    Log.log("TEST", "[GetMappingFileListActionTest.testGetWithLimit] Exit ");
  }

  /**
   * This simulates retrieving a list of accounts with a retrieval condition,
   * specifying the maximum number of rows to return, and the starting row from
   * which list to return the results. This should return Row(i) - Row(m) user
   * accounts that satisfy that retrieval conditions, i = start row, m=n-i+1
   * and n <= max rows.
   */
  public void testGetStartWithLimit() throws Exception
  {
    Log.log("TEST", "[GetMappingFileListActionTest.testGetStartWithLimit] Enter ");

//    for (int i=0; i<LIST_COUNT; i++)
//      createTestData(_DOC_TYPE+i, _DOC_DESC+i);

    Long[] uIDs = (Long[])findMappingFilesKeys(_FILTER).toArray(new Long[_LIST_COUNT]);

    _event = createTestEvent(_FILTER, _MAX_PAGE_ENTRIES, _START_ROW);
    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[GetMappingFileListActionTest.testGetStartWithLimit] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    Object returnData = _response.getReturnData();
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof EntityListResponseData);

    EntityListResponseData listData = (EntityListResponseData)returnData;

    assertEquals("Rows remaining is incorrect",
      _MAX_PAGE_ENTRIES+_START_ROW>_LIST_COUNT? 0 : _LIST_COUNT-_MAX_PAGE_ENTRIES-_START_ROW, listData.getRowsRemaining());
    assertEquals("start row is incorrect", _START_ROW, listData.getStartRow());
    assertNotNull("List ID is null", listData.getListID());

    Collection entityList = listData.getEntityList();
    assertNotNull("Entity list is null", entityList);
    assertEquals("Entity list count is incorrect",
      _LIST_COUNT-_START_ROW<_MAX_PAGE_ENTRIES?_LIST_COUNT-_START_ROW:_MAX_PAGE_ENTRIES, entityList.size());

    Object[] entityObjs = entityList.toArray();
    for (int i=0; i<entityObjs.length; i++ )
    {
      assertNotNull("Entity is null", entityObjs[i]);
      assertTrue("Entity is not Map", entityObjs[i] instanceof Map);

      Map mappingFileMap = (Map)entityObjs[i];

      assertEquals("UID is incorrect", uIDs[i+_START_ROW], mappingFileMap.get(IMappingFile.UID));
      assertEquals("name incorrect", _MAPPING_FILE_NAME+(i+_START_ROW), mappingFileMap.get(IMappingFile.NAME));
      assertEquals("desc incorrect", _MAPPING_FILE_DESC+(i+_START_ROW), mappingFileMap.get(IMappingFile.DESCRIPTION));
      assertEquals("path incorrect", _MAPPING_FILE_PATH+(i+_START_ROW), mappingFileMap.get(IMappingFile.PATH));
      assertEquals("type incorrect", _MAPPING_FILE_TYPE, mappingFileMap.get(IMappingFile.TYPE));
    }
    Log.log("TEST", "[GetMappingFileListActionTest.testGetStartWithLimit] Exit ");
  }

  /**
   * This simulates retrieving list of user accounts with a retrieval condition
   * through several event calls. Each event call should return a List ID which
   * can be used in subsequent calls to retrieve the entire list, page by page.
   *
   * @since 2.0
   */
  public void testGetWithCursor() throws Exception
  {
    Log.log("TEST", "[GetMappingFileListActionTest.testGetWithCursor] Enter ");

//    for (int i=0; i<LIST_COUNT; i++)
//      createTestData(_DOC_TYPE+i, _DOC_DESC+i);

    Long[] uIDs = (Long[])findMappingFilesKeys(_FILTER).toArray(new Long[_LIST_COUNT]);
    int numPages = (int)java.lang.Math.ceil((double)_LIST_COUNT / (double)_MAX_PAGE_ENTRIES);
    Log.debug("TEST", "Number of Pages: "+numPages);

    _event = createTestEvent(_FILTER, _MAX_PAGE_ENTRIES, 0);

    for (int j=0; j<numPages; j++)
    {
      try
      {
        _response = performEvent(_event);
      }
      catch (Exception ex)
      {
        Log.err("TEST", "[GetMappingFileListActionTest.testGetWithCursor] Error Exit ", ex);
        assertTrue("Event Exception", false);
      }

      //check response
      assertNotNull("response is null", _response);
      assertTrue("event is not successful", _response.isEventSuccessful());
      assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

      Object returnData = _response.getReturnData();
      assertNotNull("responsedata is null", returnData);
      assertTrue("response data type incorrect", returnData instanceof EntityListResponseData);

      EntityListResponseData listData = (EntityListResponseData)returnData;

      boolean lastPage = (j+1 == numPages);
      int startRow = (_MAX_PAGE_ENTRIES*j);

      assertEquals("Rows remaining is incorrect",
        lastPage? 0 : _LIST_COUNT-startRow-_MAX_PAGE_ENTRIES, listData.getRowsRemaining());
      assertEquals("start row is incorrect", startRow, listData.getStartRow());
      assertNotNull("List ID is null", listData.getListID());

      Collection entityList = listData.getEntityList();
      assertNotNull("Entity list is null", entityList);
      assertEquals("Entity list count is incorrect",
        _LIST_COUNT-startRow<_MAX_PAGE_ENTRIES?_LIST_COUNT-startRow:_MAX_PAGE_ENTRIES, entityList.size());

      Object[] entityObjs = entityList.toArray();
      for (int i=0; i<entityObjs.length; i++ )
      {
        assertNotNull("Entity is null", entityObjs[i]);
        assertTrue("Entity is not Map", entityObjs[i] instanceof Map);

        Map mappingFileMap = (Map)entityObjs[i];

        assertEquals("UID is incorrect", uIDs[i+startRow], mappingFileMap.get(IMappingFile.UID));
        assertEquals("name incorrect", _MAPPING_FILE_NAME+(i+startRow), mappingFileMap.get(IMappingFile.NAME));
        assertEquals("desc incorrect", _MAPPING_FILE_DESC+(i+startRow), mappingFileMap.get(IMappingFile.DESCRIPTION));
        assertEquals("path incorrect", _MAPPING_FILE_PATH+(i+startRow), mappingFileMap.get(IMappingFile.PATH));
        assertEquals("type incorrect", _MAPPING_FILE_TYPE, mappingFileMap.get(IMappingFile.TYPE));
      }

      _event = createTestEvent(listData.getListID(), _MAX_PAGE_ENTRIES, startRow+_MAX_PAGE_ENTRIES);
    }
    Log.log("TEST", "[GetMappingFileListActionTest.testGetWithCursor] Exit ");
  }
}