package com.gridnode.gtas.server.document.actions;

import com.gridnode.gtas.events.document.DeleteFileTypeEvent;
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

public class DeleteFileTypeActionTest extends TestCase
{
  static final String   _FILE_TYPE    = "test";
  static final String   _DESCRIPTION  = "Delete Description";
  static final String   _PROGRAM_NAME = "Delete Program Name";
  static final String   _PROGRAM_PATH = "Delete Program Path";
  static final String   _PARAMETERS   = "Delete Parameters";
  static final String   _WORKING_DIR  = "Delete Working Directory";

  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  DeleteFileTypeEvent _event;
  IDocumentManagerHome _home;
  IDocumentManagerObj _remote;
  FileType _fileType;

  StateMachine _sm = new StateMachine(null, null);

  public DeleteFileTypeActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(DeleteFileTypeActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[DeleteFileTypeActionTest.setUp] Enter");

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

    Log.log("TEST", "[DeleteFileTypeActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[DeleteFileTypeActionTest.tearDown] Enter");
    Log.log("TEST", "[DeleteFileTypeActionTest.tearDown] Exit");
  }

  private void createTestData()
  {
    Log.log("TEST", "[DeleteFileTypeActionTest.createTestData] Enter");

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
    Log.log("TEST", "[DeleteFileTypeActionTest.createTestData] Exit");
  }

  private BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    DeleteFileTypeAction action = new DeleteFileTypeAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _sessionID);
    _sm.setAttribute(IAttributeKeys.USER_ID, _userID);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  public void testDeleteNull()
  {
    Log.log("TEST", "[DeleteFileTypeActionTest.testDeleteNull] Enter ");

    try
    {
      _response = performEvent(null);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[DeleteFileTypeActionTest.testDeleteNull]" +
        " Returning fail due to EventException: "+ex.getMessage());
      Log.log("TEST", "[DeleteFileTypeActionTest.testDeleteNull] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[DeleteFileTypeActionTest.testDeleteNull] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.DELETE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertNotNull("Error trace is null", _response.getErrorTrace());
    assertEquals("error type is incorrect", _response.getErrorType(), SystemException.SYSTEM);
    Log.log("TEST", "[DeleteFileTypeActionTest.testDeleteNull] Exit ");
  }

  public void testDelete() throws Exception
  {
    Log.log("TEST", "[DeleteFileTypeActionTest.testDelete] Enter ");

    createTestData();
    _fileType = _remote.findFileType(_FILE_TYPE);
    if (_fileType != null)
    {
      Long uid = (Long)(_fileType.getFieldValue(FileType.UID));
      _event = new DeleteFileTypeEvent(uid);
      assertNotNull("event fileType UID is null", _event.getUids());
      try
      {
        _response = performEvent(_event);
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        Log.err("TEST", "[DeleteFileTypeActionTest.testDelete] Error Exit ", ex);
        assertTrue("Event Exception", false);
      }

      //check response
      assertNotNull("response is null", _response);
      assertTrue("event is not successful", _response.isEventSuccessful());
      assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    }
    Log.log("TEST", "[DeleteFileTypeActionTest.testDelete] Exit ");
  }

}