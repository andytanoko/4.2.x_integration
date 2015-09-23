package com.gridnode.gtas.server.document.actions;

import com.gridnode.gtas.events.document.UpdateFileTypeEvent;
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

public class UpdateFileTypeActionTest extends TestCase
{
  static final String   _FILE_TYPE        = "test";
  static final String   _DESCRIPTION      = "Update Description";
  static final String   _UPD_DESCRIPTION  = "Updated Description";
  static final String   _PROGRAM_NAME     = "Update Program Name";
  static final String   _UPD_PROGRAM_NAME = "Updated Program Name";
  static final String   _PROGRAM_PATH     = "Update Program Path";
  static final String   _UPD_PROGRAM_PATH = "Updated Program Path";
  static final String   _PARAMETERS       = "Update Parameters";
  static final String   _UPD_PARAMETERS   = "Updated Parameters";
  static final String   _WORKING_DIR      = "Update Working Directory";
  static final String   _UPD_WORKING_DIR  = "Updated Working Directory";

  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  UpdateFileTypeEvent _event;
  IDocumentManagerHome _home;
  IDocumentManagerObj _remote;
  FileType _fileType;

  StateMachine _sm = new StateMachine(null, null);

  public UpdateFileTypeActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(UpdateFileTypeActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[UpdateFileTypeActionTest.setUp] Enter");

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

    Log.log("TEST", "[UpdateFileTypeActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[UpdateFileTypeActionTest.tearDown] Enter");
    deleteTestData();
    Log.log("TEST", "[UpdateFileTypeActionTest.tearDown] Exit");
  }

  private void createTestData()
  {
    Log.log("TEST", "[UpdateFileTypeActionTest.createTestData] Enter");

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
    Log.log("TEST", "[UpdateFileTypeActionTest.createTestData] Exit");
  }

  private void deleteTestData()
  {
    Log.log("TEST", "[UpdateFileTypeActionTest.deleteTestData] Enter");

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
    Log.log("TEST", "[UpdateFileTypeActionTest.deleteTestData] Exit");
  }

  private BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    UpdateFileTypeAction action = new UpdateFileTypeAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _sessionID);
    _sm.setAttribute(IAttributeKeys.USER_ID, _userID);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  public void testUpdateNull()
  {
    Log.log("TEST", "[UpdateFileTypeActionTest.testDeleteNull] Enter ");

    try
    {
      _response = performEvent(null);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[UpdateFileTypeActionTest.testDeleteNull]" +
        " Returning fail due to EventException: "+ex.getMessage());
      Log.log("TEST", "[UpdateFileTypeActionTest.testDeleteNull] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[UpdateFileTypeActionTest.testDeleteNull] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.UPDATE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertNotNull("Error trace is null", _response.getErrorTrace());
    assertEquals("error type is incorrect", _response.getErrorType(), SystemException.SYSTEM);
    Log.log("TEST", "[UpdateFileTypeActionTest.testDeleteNull] Exit ");
  }

  public void testUpdate() throws Exception
  {
    Log.log("TEST", "[UpdateFileTypeActionTest.testDelete] Enter ");

    createTestData();
    _fileType = _remote.findFileType(_FILE_TYPE);
    if (_fileType != null)
    {
      Long uid = (Long)(_fileType.getFieldValue(FileType.UID));
      _event = new UpdateFileTypeEvent(uid,
                                       _UPD_DESCRIPTION,
                                       _UPD_PROGRAM_NAME,
                                       _UPD_PROGRAM_PATH,
                                       _UPD_PARAMETERS,
                                       _UPD_WORKING_DIR);
      assertNotNull("event fileType UID is null", _event.getFileTypeUID());
      assertNotNull("event fileType updDescription is null", _event.getFileTypeDesc());
      assertNotNull("event fileType updProgramName is null", _event.getProgramName());
      assertNotNull("event fileType updProgramPath is null", _event.getProgramPath());
      assertNotNull("event fileType updParameters is null", _event.getParameters());
      assertNotNull("event fileType updWorkingDir is null", _event.getWorkingDir());
      try
      {
        _response = performEvent(_event);
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        Log.err("TEST", "[UpdateFileTypeActionTest.testDelete] Error Exit ", ex);
        assertTrue("Event Exception", false);
      }

      //check response
      assertNotNull("response is null", _response);
      assertTrue("event is not successful", _response.isEventSuccessful());
      assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

      _fileType = _remote.findFileType(_FILE_TYPE);
      assertEquals("Description not updated", _UPD_DESCRIPTION, _fileType.getFieldValue(FileType.DESCRIPTION));
      assertEquals("ProgramName is incorrect", _UPD_PROGRAM_NAME, _fileType.getFieldValue(FileType.PROGRAM_NAME));
      assertEquals("ProgramPath is incorrect", _UPD_PROGRAM_PATH, _fileType.getFieldValue(FileType.PROGRAM_PATH));
      assertEquals("Parameters is incorrect", _UPD_PARAMETERS, _fileType.getFieldValue(FileType.PARAMETERS));
      assertEquals("WorkingDirectory is incorrect", _UPD_WORKING_DIR, _fileType.getFieldValue(FileType.WORKING_DIR));

    }
    Log.log("TEST", "[UpdateFileTypeActionTest.testDelete] Exit ");
  }

}