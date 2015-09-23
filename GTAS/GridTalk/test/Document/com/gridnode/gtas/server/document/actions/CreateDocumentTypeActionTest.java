package com.gridnode.gtas.server.document.actions;

import com.gridnode.gtas.events.document.CreateDocumentTypeEvent;
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

public class CreateDocumentTypeActionTest extends TestCase
{
  static final String   _DOC_TYPE    = "Test";
  static final String   _DOC_DESC    = "Test Create Description";
  static final Boolean  _CAN_DELETE  = Boolean.TRUE;

  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  CreateDocumentTypeEvent _event;
  IDocumentManagerHome _home;
  IDocumentManagerObj _remote;
  DocumentType _docType;

  StateMachine _sm = new StateMachine(null, null);

  public CreateDocumentTypeActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(CreateDocumentTypeActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[CreateDocumentTypeActionTest.setUp] Enter");

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

    Log.log("TEST", "[CreateDocumentTypeActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[CreateDocumentTypeActionTest.tearDown] Enter");
    deleteTestData();
    Log.log("TEST", "[CreateDocumentTypeActionTest.tearDown] Exit");
  }

  protected CreateDocumentTypeEvent createTestEvent()
  {
    return new CreateDocumentTypeEvent(_DOC_TYPE, _DOC_DESC);
  }

  private void createTestData()
  {
    Log.log("TEST", "[CreateDocumentTypeActionTest.createTestData] Enter");

    try
    {
      DocumentType docType = new DocumentType();
      docType.setName(_DOC_TYPE);
      docType.setDescription(_DOC_DESC);

      _remote.createDocumentType(docType);
    }
    catch (Exception ex)
    {

    }
    Log.log("TEST", "[CreateDocumentTypeActionTest.createTestData] Exit");

  }

  private void deleteTestData()
  {
    Log.log("TEST", "[CreateDocumentTypeActionTest.deleteTestData] Enter");

    try
    {
      DocumentType docType = _remote.findDocumentType(_DOC_TYPE);
      if (docType != null)
      {
        _remote.deleteDocumentType((Long)docType.getKey());
      }
    }
    catch (Exception ex)
    {

    }
    Log.log("TEST", "[CreateDocumentTypeActionTest.deleteTestData] Exit");
  }

  private BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    CreateDocumentTypeAction action = new CreateDocumentTypeAction();
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
    Log.log("TEST", "[CreateDocumentTypeActionTest.testCreateNull] Enter ");

    try
    {
      _response = performEvent(null);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[CreateDocumentTypeActionTest.testCreateNull]" +
        " Returning fail due to EventException: "+ex.getMessage());
      Log.log("TEST", "[CreateDocumentTypeActionTest.testCreateNull] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[CreateDocumentTypeActionTest.testCreateNull] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.CREATE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertNotNull("Error trace is null", _response.getErrorTrace());
    assertEquals("error type is incorrect", _response.getErrorType(), SystemException.SYSTEM);
    Log.log("TEST", "[CreateDocumentTypeActionTest.testCreateNull] Exit ");
  }

  public void testCreateNew() throws Exception
  {
    Log.log("TEST", "[CreateDocumentTypeActionTest.testCreateNew] Enter ");

    deleteTestData();
    _event = createTestEvent();
    assertNotNull("event DocType is null", _event.getdocTypeName());
    assertNotNull("event DocDesc is null", _event.getdocTypeDesc());
    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      Log.err("TEST", "[CreateDocumentTypeActionTest.testCreateNew] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    _docType = _remote.findDocumentType(_DOC_TYPE);
    Log.debug("TEST", "Found doctype : "+_docType);
    assertNotNull("Doctype retrieved is null", _docType);

    assertNotNull("UID is null", _docType.getFieldValue(DocumentType.UID));
    assertEquals("Name is incorrect", _DOC_TYPE, _docType.getName());
    assertEquals("Description is incorrect", _DOC_DESC, _docType.getDescription());

    Log.log("TEST", "[CreateDocumentTypeActionTest.testCreateNew] Exit ");
  }

  public void testCreateDuplicate() throws Exception
  {
    Log.log("TEST", "[CreateDocumentTypeActionTest.testCreateDuplicate] Enter ");

    createTestData();
    _event = createTestEvent();

    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[CreateDocumentTypeActionTest.testCreateDuplicate] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.CREATE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertEquals("error type is incorrect", _response.getErrorType(), DuplicateEntityException.APPLICATION);

    Log.log("TEST", "[CreateDocumentTypeActionTest.testCreateDuplicate] Exit ");
  }
}