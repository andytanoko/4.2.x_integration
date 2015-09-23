package com.gridnode.gtas.server.document.actions;

import com.gridnode.gtas.events.document.DeleteDocumentTypeEvent;
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

public class DeleteDocumentTypeActionTest extends TestCase
{
  static final String   _DOC_TYPE    = "Test";
  static final String   _DOC_DESC    = "Test Delete Description";
  static final Boolean  _CAN_DELETE  = Boolean.TRUE;

  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  DeleteDocumentTypeEvent _event;
  IDocumentManagerHome _home;
  IDocumentManagerObj _remote;
  DocumentType _docType;

  StateMachine _sm = new StateMachine(null, null);

  public DeleteDocumentTypeActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(DeleteDocumentTypeActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[DeleteDocumentTypeActionTest.setUp] Enter");

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

    Log.log("TEST", "[DeleteDocumentTypeActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[DeleteDocumentTypeActionTest.tearDown] Enter");
    Log.log("TEST", "[DeleteDocumentTypeActionTest.tearDown] Exit");
  }

  private void createTestData()
  {
    Log.log("TEST", "[DeleteDocumentTypeActionTest.createTestData] Enter");

    try
    {
      DocumentType newDocType = new DocumentType();
      newDocType.setName(_DOC_TYPE);
      newDocType.setDescription(_DOC_DESC);

      _remote.createDocumentType(newDocType);
    }
    catch (Exception ex)
    {

    }
    Log.log("TEST", "[DeleteDocumentTypeActionTest.createTestData] Exit");
  }

  private BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    DeleteDocumentTypeAction action = new DeleteDocumentTypeAction();
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
    Log.log("TEST", "[DeleteDocumentTypeActionTest.testDeleteNull] Enter ");

    try
    {
      _response = performEvent(null);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[DeleteDocumentTypeActionTest.testDeleteNull]" +
        " Returning fail due to EventException: "+ex.getMessage());
      Log.log("TEST", "[DeleteDocumentTypeActionTest.testDeleteNull] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[DeleteDocumentTypeActionTest.testDeleteNull] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.DELETE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertNotNull("Error trace is null", _response.getErrorTrace());
    assertEquals("error type is incorrect", _response.getErrorType(), SystemException.SYSTEM);
    Log.log("TEST", "[DeleteDocumentTypeActionTest.testDeleteNull] Exit ");
  }

  public void testDelete() throws Exception
  {
    Log.log("TEST", "[DeleteDocumentTypeActionTest.testDelete] Enter ");

    createTestData();
    _docType = _remote.findDocumentType(_DOC_TYPE);
    if (_docType != null)
    {
      Long uid = (Long)(_docType.getFieldValue(DocumentType.UID));
      _event = new DeleteDocumentTypeEvent(uid);
      assertNotNull("event docType UID is null", _event.getUids());
      try
      {
        _response = performEvent(_event);
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        Log.err("TEST", "[DeleteDocumentTypeActionTest.testDelete] Error Exit ", ex);
        assertTrue("Event Exception", false);
      }

      //check response
      assertNotNull("response is null", _response);
      assertTrue("event is not successful", _response.isEventSuccessful());
      assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    }
    Log.log("TEST", "[DeleteDocumentTypeActionTest.testDelete] Exit ");
  }

}