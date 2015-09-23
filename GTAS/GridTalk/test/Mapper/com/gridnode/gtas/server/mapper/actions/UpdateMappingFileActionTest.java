package com.gridnode.gtas.server.mapper.actions;

import com.gridnode.gtas.events.mapper.CreateMappingFileEvent;
import com.gridnode.gtas.events.mapper.DeleteMappingFileEvent;
import com.gridnode.gtas.events.mapper.UpdateMappingFileEvent;
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

public class UpdateMappingFileActionTest extends TestCase
{
  static final String   _MAPPING_FILE_NAME         = "Test Update Name";
  static final String   _MAPPING_FILE_DESC         = "Test Update Description";
  static final String   _UPD_MAPPING_FILE_DESC     = "Test Updated Description";
  static final String   _MAPPING_FILE_FILENAME     = "IGridDocument.html";
  static final String   _UPD_MAPPING_FILE_FILENAME = "rnSPLIT_AMKBCI_RN_CON.xsl";
  static final String   _MAPPING_FILE_TEMP_PATH    = "data/temp/";
  static final Short    _MAPPING_FILE_TYPE         = MappingFile.XSL;
  static final Short    _UPD_MAPPING_FILE_TYPE     = MappingFile.CONVERSION_RULE;

  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  UpdateMappingFileEvent _event;
  IMappingManagerHome _home;
  IMappingManagerObj _remote;
  MappingFile _mappingFile;

  StateMachine _sm = new StateMachine(null, null);

  public UpdateMappingFileActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(UpdateMappingFileActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[UpdateMappingFileActionTest.setUp] Enter");

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

    Log.log("TEST", "[UpdateMappingFileActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[UpdateMappingFileActionTest.tearDown] Enter");
    //deleteTestData();
    Log.log("TEST", "[UpdateMappingFileActionTest.tearDown] Exit");
  }

  private void createTestData()
  {
    Log.log("TEST", "[UpdateMappingFileActionTest.createTestData] Enter");

    try
    {
      CreateMappingFileEvent cEvent = new CreateMappingFileEvent(
                                        _MAPPING_FILE_NAME,
                                        _MAPPING_FILE_DESC,
                                        _MAPPING_FILE_TEMP_PATH+_MAPPING_FILE_FILENAME,
                                        null,
                                        _MAPPING_FILE_TYPE);
      _response = performCreateEvent(cEvent);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      Log.err("TEST", "[UpdateMappingFileActionTest.createTestData] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }
    assertNotNull("response is null", _response);
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    Log.log("TEST", "[UpdateMappingFileActionTest.createTestData] Exit");
  }

  private void deleteTestData()
  {
    Log.log("TEST", "[UpdateMappingFileActionTest.deleteTestData] Enter");

    try
    {
      _mappingFile = _remote.findMappingFile(_MAPPING_FILE_NAME);
      if (_mappingFile != null)
      {
        Long uid = (Long)(_mappingFile.getFieldValue(MappingFile.UID));
        DeleteMappingFileEvent dEvent = new DeleteMappingFileEvent(uid);
        _response = performDeleteEvent(dEvent);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      Log.err("TEST", "[UpdateMappingFileActionTest.deleteTestData] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }
    assertNotNull("response is null", _response);
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    Log.log("TEST", "[UpdateMappingFileActionTest.deleteTestData] Exit");
  }

  private BasicEventResponse performCreateEvent(IEvent event)
    throws Exception
  {
    CreateMappingFileAction action = new CreateMappingFileAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _sessionID);
    _sm.setAttribute(IAttributeKeys.USER_ID, _userID);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  private BasicEventResponse performUpdateEvent(IEvent event)
    throws Exception
  {
    UpdateMappingFileAction action = new UpdateMappingFileAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _sessionID);
    _sm.setAttribute(IAttributeKeys.USER_ID, _userID);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  private BasicEventResponse performDeleteEvent(IEvent event)
    throws Exception
  {
    DeleteMappingFileAction action = new DeleteMappingFileAction();
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
    Log.log("TEST", "[UpdateMappingFileActionTest.testDeleteNull] Enter ");

    try
    {
      _response = performUpdateEvent(null);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[UpdateMappingFileActionTest.testDeleteNull]" +
        " Returning fail due to EventException: "+ex.getMessage());
      Log.log("TEST", "[UpdateMappingFileActionTest.testDeleteNull] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[UpdateMappingFileActionTest.testDeleteNull] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.UPDATE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertNotNull("Error trace is null", _response.getErrorTrace());
    assertEquals("error type is incorrect", _response.getErrorType(), SystemException.SYSTEM);
    Log.log("TEST", "[UpdateMappingFileActionTest.testDeleteNull] Exit ");
  }

  public void testUpdate() throws Exception
  {
    Log.log("TEST", "[UpdateMappingFileActionTest.testDelete] Enter ");

    createTestData();
    _mappingFile = _remote.findMappingFile(_MAPPING_FILE_NAME);
    if (_mappingFile != null)
    {
      Long uid = (Long)(_mappingFile.getFieldValue(MappingFile.UID));
      _event = new UpdateMappingFileEvent(uid,
                                          _UPD_MAPPING_FILE_DESC,
                                          _MAPPING_FILE_TEMP_PATH+_UPD_MAPPING_FILE_FILENAME,
                                          null,
                                          _UPD_MAPPING_FILE_TYPE);
      assertNotNull("event MappingFile UID is null", _event.getMappingFileUID());
      assertNotNull("event MappingFile updDesc is null", _event.getMappingFileDesc());
      assertNotNull("event MappingFile updPath is null", _event.getMappingFilePath());
      assertNotNull("event MappingFile updType is null", _event.getMappingFileType());
      try
      {
        _response = performUpdateEvent(_event);
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        Log.err("TEST", "[UpdateMappingFileActionTest.testDelete] Error Exit ", ex);
        assertTrue("Event Exception", false);
      }

      //check response
      assertNotNull("response is null", _response);
      assertTrue("event is not successful", _response.isEventSuccessful());
      assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

      _mappingFile = _remote.findMappingFile(_MAPPING_FILE_NAME);
      assertEquals("desc not updated", _UPD_MAPPING_FILE_DESC, _mappingFile.getFieldValue(MappingFile.DESCRIPTION));
      assertEquals("path not updated", _UPD_MAPPING_FILE_FILENAME, _mappingFile.getFieldValue(MappingFile.FILENAME));
      assertEquals("type not updated", _UPD_MAPPING_FILE_TYPE, _mappingFile.getFieldValue(MappingFile.TYPE));

    }
    deleteTestData();
    Log.log("TEST", "[UpdateMappingFileActionTest.testDelete] Exit ");
  }

}