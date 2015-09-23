package com.gridnode.gtas.server.document.actions;

import com.gridnode.gtas.events.document.GetFileTypeEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.gtas.server.document.model.*;
import com.gridnode.gtas.server.document.facade.ejb.*;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;

import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;

import java.util.Map;

public class GetFileTypeActionTest extends TestCase
{
  static final String   _FILE_TYPE    = "test";
  static final String   _DESCRIPTION  = "Get Description";
  static final String   _PROGRAM_NAME = "Get Program Name";
  static final String   _PROGRAM_PATH = "Get Program Path";
  static final String   _PARAMETERS   = "Get Parameters";
  static final String   _WORKING_DIR  = "Get Working Directory";

  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  GetFileTypeEvent _event;
  IDocumentManagerHome _home;
  IDocumentManagerObj _remote;
  FileType _fileType;

  StateMachine _sm = new StateMachine(null, null);

  public GetFileTypeActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetFileTypeActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[GetFileTypeActionTest.setUp] Enter");

    _sessionHome = (ISessionManagerHome)ServiceLookup.getInstance(
                   ServiceLookup.CLIENT_CONTEXT).getHome(
                   ISessionManagerHome.class);
    assertNotNull("SessionHome is null", _sessionHome);
    _sessionRemote = _sessionHome.create();
    assertNotNull("SessionRemote is null", _sessionRemote);
    _sessionID = _sessionRemote.openSession();
    _sessionRemote.authSession(_sessionID, _userID);

    _home = (IDocumentManagerHome)ServiceLookup.getInstance(
             ServiceLookup.CLIENT_CONTEXT).getHome(
             IDocumentManagerHome.class);
    assertNotNull("Home is null", _home);
    _remote = _home.create();
    assertNotNull("remote is null", _remote);

    Log.log("TEST", "[GetFileTypeActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[GetFileTypeActionTest.tearDown] Enter");
    deleteTestData();
    Log.log("TEST", "[GetFileTypeActionTest.tearDown] Exit");
  }

  private void createTestData()
  {
    Log.log("TEST", "[GetFileTypeActionTest.createTestData] Enter");

    try
    {
      FileType fileType = new FileType();
      fileType.setName(_FILE_TYPE);
      fileType.setDescription(_DESCRIPTION);
      fileType.setProgramName(_PROGRAM_NAME);
      fileType.setProgramPath(_PROGRAM_PATH);
      fileType.setParameters(_PARAMETERS);
      fileType.setWorkingDirectory(_WORKING_DIR);

      _remote.createFileType(fileType);
    }
    catch (Exception ex)
    {

    }
    Log.log("TEST", "[GetFileTypeActionTest.createTestData] Exit");
  }

  private void deleteTestData()
  {
    Log.log("TEST", "[GetFileTypeActionTest.deleteTestData] Enter");

    try
    {
      FileType fileType = _remote.findFileType(_FILE_TYPE);
      if (fileType != null)
      {
        _remote.deleteFileType((Long)fileType.getKey());
      }
    }
    catch (Exception ex)
    {

    }
    Log.log("TEST", "[GetFileTypeActionTest.deleteTestData] Exit");
  }


  private BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    GetFileTypeAction action = new GetFileTypeAction();
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
    Log.log("TEST", "[GetFileTypeActionTest.testDeleteNull] Enter ");

    try
    {
      _response = performEvent(null);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[GetFileTypeActionTest.testDeleteNull]" +
        " Returning fail due to EventException: "+ex.getMessage());
      Log.log("TEST", "[GetFileTypeActionTest.testDeleteNull] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[GetFileTypeActionTest.testDeleteNull] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.FIND_ENTITY_BY_KEY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertNotNull("Error trace is null", _response.getErrorTrace());
    assertEquals("error type is incorrect", _response.getErrorType(), SystemException.SYSTEM);
    Log.log("TEST", "[GetFileTypeActionTest.testDeleteNull] Exit ");
  }

  public void testGet() throws Exception
  {
    Log.log("TEST", "[GetFileTypeActionTest.testDelete] Enter ");

    createTestData();
    _fileType = _remote.findFileType(_FILE_TYPE);
    Long uid = (Long)(_fileType.getFieldValue(FileType.UID));
    _event = new GetFileTypeEvent(uid);
    assertNotNull("event filetype UID is null", _event.getFileTypeUID());
    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      Log.err("TEST", "[GetFileTypeActionTest.testDelete] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    Object returnData = _response.getReturnData();
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof Map);

    Map fileTypeMap = (Map)returnData;

    assertEquals("fileType is incorrect", _FILE_TYPE, fileTypeMap.get(FileType.FILE_TYPE));
    assertEquals("fileType desc is incorrect", _DESCRIPTION, fileTypeMap.get(FileType.DESCRIPTION));
    assertEquals("ProgramName is incorrect", _PROGRAM_NAME, fileTypeMap.get(FileType.PROGRAM_NAME));
    assertEquals("ProgramPath is incorrect", _PROGRAM_PATH, fileTypeMap.get(FileType.PROGRAM_PATH));
    assertEquals("Parameters is incorrect", _PARAMETERS, fileTypeMap.get(FileType.PARAMETERS));
    assertEquals("WorkingDirectory is incorrect", _WORKING_DIR, fileTypeMap.get(FileType.WORKING_DIR));

    Log.log("TEST", "[GetFileTypeActionTest.testDelete] Exit ");
  }

}