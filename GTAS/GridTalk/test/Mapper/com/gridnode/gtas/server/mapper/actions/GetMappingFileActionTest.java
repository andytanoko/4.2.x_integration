package com.gridnode.gtas.server.mapper.actions;

import com.gridnode.gtas.events.mapper.GetMappingFileEvent;
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

import junit.framework.*;

import java.util.Map;

public class GetMappingFileActionTest extends TestCase
{
  static final String   _MAPPING_FILE_NAME  = "Test Get Name";
  static final String   _MAPPING_FILE_DESC  = "Test Get Description";
  static final String   _MAPPING_FILE_PATH  = "Test Get Path";
  static final Short    _MAPPING_FILE_TYPE  = MappingFile.REFERENCE_DOC;

  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  GetMappingFileEvent _event;
  IMappingManagerHome _home;
  IMappingManagerObj _remote;
  MappingFile _mappingFile;

  StateMachine _sm = new StateMachine(null, null);

  public GetMappingFileActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetMappingFileActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[GetMappingFileActionTest.setUp] Enter");

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

    Log.log("TEST", "[GetMappingFileActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[GetMappingFileActionTest.tearDown] Enter");
    deleteTestData();
    Log.log("TEST", "[GetMappingFileActionTest.tearDown] Exit");
  }

  private void createTestData()
  {
    Log.log("TEST", "[GetMappingFileActionTest.createTestData] Enter");

    try
    {
      MappingFile newMappingFile = new MappingFile();
      newMappingFile.setName(_MAPPING_FILE_NAME);
      newMappingFile.setDescription(_MAPPING_FILE_DESC);
      newMappingFile.setPath(_MAPPING_FILE_PATH);
      newMappingFile.setType(_MAPPING_FILE_TYPE);

      _remote.createMappingFile(newMappingFile);
    }
    catch (Exception ex)
    {

    }
    Log.log("TEST", "[GetMappingFileActionTest.createTestData] Exit");
  }

  private void deleteTestData()
  {
    Log.log("TEST", "[GetMappingFileActionTest.deleteTestData] Enter");

    try
    {
      MappingFile mappingFile = _remote.findMappingFile(_MAPPING_FILE_NAME);
      if (mappingFile != null)
      {
        _remote.deleteMappingFile((Long)mappingFile.getKey());
      }
    }
    catch (Exception ex)
    {

    }
    Log.log("TEST", "[GetMappingFileActionTest.deleteTestData] Exit");
  }


  private BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    GetMappingFileAction action = new GetMappingFileAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _sessionID);
    _sm.setAttribute(IAttributeKeys.USER_ID, _userID);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  public void testGetNull()
  {
    Log.log("TEST", "[GetMappingFileActionTest.testDeleteNull] Enter ");

    try
    {
      _response = performEvent(null);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[GetMappingFileActionTest.testDeleteNull]" +
        " Returning fail due to EventException: "+ex.getMessage());
      Log.log("TEST", "[GetMappingFileActionTest.testDeleteNull] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[GetMappingFileActionTest.testDeleteNull] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.FIND_ENTITY_BY_KEY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertNotNull("Error trace is null", _response.getErrorTrace());
    assertEquals("error type is incorrect", _response.getErrorType(), SystemException.SYSTEM);
    Log.log("TEST", "[GetMappingFileActionTest.testDeleteNull] Exit ");
  }

  public void testGet() throws Exception
  {
    Log.log("TEST", "[GetMappingFileActionTest.testDelete] Enter ");

    createTestData();
    _mappingFile = _remote.findMappingFile(_MAPPING_FILE_NAME);
    Long uid = (Long)(_mappingFile.getFieldValue(MappingFile.UID));
    _event = new GetMappingFileEvent(uid);
    assertNotNull("event doctype UID is null", _event.getMappingFileUID());
    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      Log.err("TEST", "[GetMappingFileActionTest.testDelete] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    Object returnData = _response.getReturnData();
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof Map);

    Map mappingFileMap = (Map)returnData;

    assertEquals("mappingFile is incorrect", _MAPPING_FILE_NAME, mappingFileMap.get(MappingFile.NAME));
    assertEquals("mappingFile desc is incorrect", _MAPPING_FILE_DESC, mappingFileMap.get(MappingFile.DESCRIPTION));
    assertEquals("mappingFile path is incorrect", _MAPPING_FILE_PATH, mappingFileMap.get(MappingFile.PATH));
    assertEquals("mappingFile type is incorrect", _MAPPING_FILE_TYPE, mappingFileMap.get(MappingFile.TYPE));

    Log.log("TEST", "[GetMappingFileActionTest.testDelete] Exit ");
  }

}