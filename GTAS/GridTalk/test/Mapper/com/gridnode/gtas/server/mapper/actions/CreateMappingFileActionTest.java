package com.gridnode.gtas.server.mapper.actions;

import com.gridnode.gtas.events.mapper.CreateMappingFileEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;

import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;
//import org.apache.webdav.lib.WebdavResource;
//import org.apache.webdav.lib.WebdavSession;

public class CreateMappingFileActionTest extends TestCase
{
  static final String   _MAPPING_FILE_NAME      = "Test Create Name";
  static final String   _MAPPING_FILE_DESC      = "Test Create Description";
  static final String   _MAPPING_FILE_FILENAME  = "IGridDocument.html";
  static final String   _MAPPING_FILE_TEMP_PATH = "data/temp/";
  static final Short    _MAPPING_FILE_TYPE      = MappingFile.XSL;
  static final String   _MAPPING_FILE_PATH      = "path.xsl";
  static final Boolean  _CAN_DELETE             = Boolean.TRUE;

  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  CreateMappingFileEvent _event;
  IMappingManagerHome _home;
  IMappingManagerObj _remote;
  MappingFile _mappingFile;

  StateMachine _sm = new StateMachine(null, null);

  public CreateMappingFileActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(CreateMappingFileActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[CreateMappingFileActionTest.setUp] Enter");

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

    Log.log("TEST", "[CreateMappingFileActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[CreateMappingFileActionTest.tearDown] Enter");
    deleteTestData();
    Log.log("TEST", "[CreateMappingFileActionTest.tearDown] Exit");
  }

  protected CreateMappingFileEvent createTestEvent()
  {
    return new CreateMappingFileEvent(_MAPPING_FILE_NAME,
                                      _MAPPING_FILE_DESC,
                                      _MAPPING_FILE_TEMP_PATH+_MAPPING_FILE_FILENAME,
                                      null,
                                      _MAPPING_FILE_TYPE);
  }

  private void createTestData()
  {
    Log.log("TEST", "[CreateMappingFileActionTest.createTestData] Enter");

    try
    {
      MappingFile mappingFile = new MappingFile();
      mappingFile.setName(_MAPPING_FILE_NAME);
      mappingFile.setDescription(_MAPPING_FILE_DESC);
      mappingFile.setFilename(_MAPPING_FILE_FILENAME);
      mappingFile.setPath(_MAPPING_FILE_PATH);
      mappingFile.setType(_MAPPING_FILE_TYPE);

      _remote.createMappingFile(mappingFile);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    Log.log("TEST", "[CreateMappingFileActionTest.createTestData] Exit");

  }

  private void deleteTestData()
  {
    Log.log("TEST", "[CreateMappingFileActionTest.deleteTestData] Enter");

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
      ex.printStackTrace();
    }
    Log.log("TEST", "[CreateMappingFileActionTest.deleteTestData] Exit");
  }

  private BasicEventResponse performEvent(IEvent event)
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

  public void testCreateNull()
  {
    Log.log("TEST", "[CreateMappingFileActionTest.testCreateNull] Enter ");

    try
    {
      _response = performEvent(null);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[CreateMappingFileActionTest.testCreateNull]" +
        " Returning fail due to EventException: "+ex.getMessage());
      Log.log("TEST", "[CreateMappingFileActionTest.testCreateNull] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[CreateMappingFileActionTest.testCreateNull] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.CREATE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertNotNull("Error trace is null", _response.getErrorTrace());
    assertEquals("error type is incorrect", _response.getErrorType(), SystemException.SYSTEM);
    Log.log("TEST", "[CreateMappingFileActionTest.testCreateNull] Exit ");
  }

  public void testCreateNew() throws Exception
  {
    Log.log("TEST", "[CreateMappingFileActionTest.testCreateNew] Enter ");

    deleteTestData();
    _event = createTestEvent();
    assertNotNull("event MappingFile name is null", _event.getMappingFileName());
    assertNotNull("event MappingFile desc is null", _event.getMappingFileDesc());
    assertNotNull("event MappingFile path is null", _event.getMappingFilePath());
    assertNotNull("event MappingFile type is null", _event.getMappingFileType());
    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      Log.err("TEST", "[CreateMappingFileActionTest.testCreateNew] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    System.out.println(_response.getErrorType());
    System.out.println(_response.getErrorReason());
    System.out.println(_response.getErrorTrace());
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    _mappingFile = _remote.findMappingFile(_MAPPING_FILE_NAME);
    Log.debug("TEST", "Found Mapping File : "+_mappingFile);
    assertNotNull("Mapping File retrieved is null", _mappingFile);

    assertNotNull("UID is null", _mappingFile.getFieldValue(MappingFile.UID));
    assertEquals("Name is incorrect", _MAPPING_FILE_NAME, _mappingFile.getName());
    assertEquals("Description is incorrect", _MAPPING_FILE_DESC, _mappingFile.getDescription());
    assertEquals("Filename is incorrect", _MAPPING_FILE_FILENAME, _mappingFile.getFilename());
    assertEquals("Type is incorrect", _MAPPING_FILE_TYPE, _mappingFile.getType());

    Log.log("TEST", "[CreateMappingFileActionTest.testCreateNew] Exit ");
  }

  public void testCreateDuplicate() throws Exception
  {
    Log.log("TEST", "[CreateMappingFileActionTest.testCreateDuplicate] Enter ");

    createTestData();
    _event = createTestEvent();

    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[CreateMappingFileActionTest.testCreateDuplicate] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.CREATE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertEquals("error type is incorrect", _response.getErrorType(), DuplicateEntityException.APPLICATION);

    Log.log("TEST", "[CreateMappingFileActionTest.testCreateDuplicate] Exit ");
  }
}