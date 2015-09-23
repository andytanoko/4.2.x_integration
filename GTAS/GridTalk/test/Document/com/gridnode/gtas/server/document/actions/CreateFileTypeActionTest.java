package com.gridnode.gtas.server.document.actions;

import com.gridnode.gtas.events.document.CreateFileTypeEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.gtas.server.document.model.*;
import com.gridnode.gtas.server.document.facade.ejb.*;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;

import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;

public class CreateFileTypeActionTest extends TestCase
{
  static final String   _FILE_TYPE    = "test";
  static final String   _DESCRIPTION  = "Create Description";
  static final String   _PROGRAM_NAME = "Create Program Name";
  static final String   _PROGRAM_PATH = "Create Program Path";
  static final String   _PARAMETERS   = "Create Parameters";
  static final String   _WORKING_DIR  = "Create Working Directory";

  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  CreateFileTypeEvent _event;
  IDocumentManagerHome _home;
  IDocumentManagerObj _remote;
  FileType _fileType;

  StateMachine _sm = new StateMachine(null, null);

  public CreateFileTypeActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(CreateFileTypeActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[CreateFileTypeActionTest.setUp] Enter");

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

    Log.log("TEST", "[CreateFileTypeActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[CreateFileTypeActionTest.tearDown] Enter");
    deleteTestData();
    Log.log("TEST", "[CreateFileTypeActionTest.tearDown] Exit");
  }

  protected CreateFileTypeEvent createTestEvent()
  {
    return new CreateFileTypeEvent(_FILE_TYPE,
                                   _DESCRIPTION,
                                   _PROGRAM_NAME,
                                   _PROGRAM_PATH,
                                   _PARAMETERS,
                                   _WORKING_DIR);
  }

  private void createTestData()
  {
    Log.log("TEST", "[CreateFileTypeActionTest.createTestData] Enter");

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
      ex.printStackTrace();
    }
    Log.log("TEST", "[CreateFileTypeActionTest.createTestData] Exit");

  }

  private void deleteTestData()
  {
    Log.log("TEST", "[CreateFileTypeActionTest.deleteTestData] Enter");

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
      ex.printStackTrace();
    }
    Log.log("TEST", "[CreateFileTypeActionTest.deleteTestData] Exit");
  }

  private BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    CreateFileTypeAction action = new CreateFileTypeAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _sessionID);
    _sm.setAttribute(IAttributeKeys.USER_ID, _userID);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  public void testCreateNull()
  {
    Log.log("TEST", "[CreateFileTypeActionTest.testCreateNull] Enter ");

    try
    {
      _response = performEvent(null);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[CreateFileTypeActionTest.testCreateNull]" +
        " Returning fail due to EventException: "+ex.getMessage());
      Log.log("TEST", "[CreateFileTypeActionTest.testCreateNull] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[CreateFileTypeActionTest.testCreateNull] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.CREATE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertNotNull("Error trace is null", _response.getErrorTrace());
    assertEquals("error type is incorrect", _response.getErrorType(), SystemException.SYSTEM);
    Log.log("TEST", "[CreateFileTypeActionTest.testCreateNull] Exit ");
  }

  public void testCreateNew() throws Exception
  {
    Log.log("TEST", "[CreateFileTypeActionTest.testCreateNew] Enter ");

    deleteTestData();
    _event = createTestEvent();
    assertNotNull("event FileType is null", _event.getFileTypeName());
    assertNotNull("event Description is null", _event.getFileTypeDesc());
    assertNotNull("event ProgramName is null", _event.getProgramName());
    assertNotNull("event ProgramPath is null", _event.getProgramPath());
    assertNotNull("event Parameters is null", _event.getParameters());
    assertNotNull("event WorkingDir is null", _event.getWorkingDir());
    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      Log.err("TEST", "[CreateFileTypeActionTest.testCreateNew] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    _fileType = _remote.findFileType(_FILE_TYPE);
    Log.debug("TEST", "Found doctype : "+_fileType);
    assertNotNull("Doctype retrieved is null", _fileType);

    assertNotNull("UID is null", _fileType.getFieldValue(FileType.UID));
    assertEquals("Name is incorrect", _FILE_TYPE, _fileType.getName());
    assertEquals("Description is incorrect", _DESCRIPTION, _fileType.getDescription());
    assertEquals("ProgramName is incorrect", _PROGRAM_NAME, _fileType.getProgramName());
    assertEquals("ProgramPath is incorrect", _PROGRAM_PATH, _fileType.getProgramPath());
    assertEquals("Parameters is incorrect", _PARAMETERS, _fileType.getParameters());
    assertEquals("WorkingDirectory is incorrect", _WORKING_DIR, _fileType.getWorkingDirectory());

    Log.log("TEST", "[CreateFileTypeActionTest.testCreateNew] Exit ");
  }

  public void testCreateDuplicate() throws Exception
  {
    Log.log("TEST", "[CreateFileTypeActionTest.testCreateDuplicate] Enter ");

    createTestData();
    _event = createTestEvent();

    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[CreateFileTypeActionTest.testCreateDuplicate] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.CREATE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertEquals("error type is incorrect", _response.getErrorType(), DuplicateEntityException.APPLICATION);

    Log.log("TEST", "[CreateFileTypeActionTest.testCreateDuplicate] Exit ");
  }
}