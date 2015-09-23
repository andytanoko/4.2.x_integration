package com.gridnode.gtas.server.document.actions;

import com.gridnode.gtas.events.document.DeleteGridDocumentEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.gtas.server.document.model.*;
import com.gridnode.gtas.server.document.facade.ejb.*;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;


import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;

import java.util.ArrayList;

public class DeleteGridDocumentActionTest extends TestCase
{
  static final Long _GDOC_UID = new Long(136);

  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  DeleteGridDocumentEvent _event;
  IDocumentManagerHome _home;
  IDocumentManagerObj _remote;

  StateMachine _sm = new StateMachine(null, null);

  public DeleteGridDocumentActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(DeleteGridDocumentActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[DeleteGridDocumentActionTest.setUp] Enter");

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

    Log.log("TEST", "[DeleteGridDocumentActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[DeleteGridDocumentActionTest.tearDown] Enter");
    Log.log("TEST", "[DeleteGridDocumentActionTest.tearDown] Exit");
  }

  private void createTestData()
  {
    Log.log("TEST", "[DeleteGridDocumentActionTest.createTestData] Enter");

    Log.log("TEST", "[DeleteGridDocumentActionTest.createTestData] Exit");
  }

  private BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    DeleteGridDocumentAction action = new DeleteGridDocumentAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _sessionID);
    _sm.setAttribute(IAttributeKeys.USER_ID, _userID);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }


  public void testImport() throws Exception
  {
    Log.log("TEST", "[DeleteGridDocumentActionTest.testDelete] Enter ");

    createTestData();
    _event = new DeleteGridDocumentEvent(_GDOC_UID);
//    assertNotNull("event fileType UID is null", _event.getFileTypeUID());
      try
      {
        _response = performEvent(_event);
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        Log.err("TEST", "[DeleteGridDocumentActionTest.testDelete] Error Exit ", ex);
        assertTrue("Event Exception", false);
      }

      //check response
      assertNotNull("response is null", _response);
      assertTrue("event is not successful", _response.isEventSuccessful());
      assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    Log.log("TEST", "[DeleteGridDocumentActionTest.testDelete] Exit ");
  }

}