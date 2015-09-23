/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteTriggerActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 10 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import com.gridnode.gtas.events.partnerprocess.DeleteTriggerEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.gtas.server.partnerprocess.model.*;
import com.gridnode.gtas.server.partnerprocess.facade.ejb.*;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class DeleteTriggerActionTest extends TestCase
{
  static final Integer _TRIGGER_LEVEL       = ITrigger.LEVEL_0;
  static final String  _PARTNER_FUNCTION_ID = "PF01";
  static final String  _PROCESS_ID          = "Process Id";
  static final String  _DOC_TYPE            = "UC";
  static final String  _PARTNER_TYPE        = "SU";
  static final String  _PARTNER_GROUP       = "LS";
  static final String  _PARTNER_ID          = "Partner Id";

  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  DeleteTriggerEvent _event;
  IPartnerProcessManagerHome _home;
  IPartnerProcessManagerObj _remote;
  Trigger _trigger;

  StateMachine _sm = new StateMachine(null, null);

  public DeleteTriggerActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(DeleteTriggerActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[DeleteTriggerActionTest.setUp] Enter");

    _sessionHome = (ISessionManagerHome)ServiceLookup.getInstance(
                   ServiceLookup.CLIENT_CONTEXT).getHome(
                   ISessionManagerHome.class);
    assertNotNull("SessionHome is null", _sessionHome);
    _sessionRemote = _sessionHome.create();
    assertNotNull("SessionRemote is null", _sessionRemote);
    _sessionID = _sessionRemote.openSession();
    _sessionRemote.authSession(_sessionID, _userID);

    _home = (IPartnerProcessManagerHome)ServiceLookup.getInstance(
             ServiceLookup.CLIENT_CONTEXT).getHome(
             IPartnerProcessManagerHome.class);
    assertNotNull("Home is null", _home);
    _remote = _home.create();
    assertNotNull("remote is null", _remote);

    Log.log("TEST", "[DeleteTriggerActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[DeleteTriggerActionTest.tearDown] Enter");
    deleteTestData();
    Log.log("TEST", "[DeleteTriggerActionTest.tearDown] Exit");
  }

  private void createTestData()
  {
    Log.log("TEST", "[DeleteTriggerActionTest.createTestData] Enter");

    try
    {
      Trigger trigger = new Trigger();
      trigger.setLevel(_TRIGGER_LEVEL);
      trigger.setPartnerFunctionId(_PARTNER_FUNCTION_ID);
      trigger.setProcessId(_PROCESS_ID);
      trigger.setDocumentType(_DOC_TYPE);
      trigger.setPartnerType(_PARTNER_TYPE);
      trigger.setPartnerGroup(_PARTNER_GROUP);
      trigger.setPartnerId(_PARTNER_ID);

      _remote.createTrigger(trigger);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    Log.log("TEST", "[DeleteTriggerActionTest.createTestData] Exit");

  }

  private void deleteTestData()
  {
    Log.log("TEST", "[DeleteTriggerActionTest.deleteTestData] Enter");

    try
    {
      Trigger trigger = findTrigger();
      if (trigger != null)
      {
        _remote.deleteTrigger((Long)trigger.getKey());
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    Log.log("TEST", "[DeleteTriggerActionTest.deleteTestData] Exit");
  }

  private Trigger findTrigger()
  {
    try
    {
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        Trigger.DOC_TYPE,
        filter.getEqualOperator(),
        _DOC_TYPE,
        false);
      filter.addSingleFilter(
        filter.getAndConnector(),
        Trigger.PARTNER_TYPE,
        filter.getEqualOperator(),
        _PARTNER_TYPE,
        false);
      filter.addSingleFilter(
        filter.getAndConnector(),
        Trigger.PARTNER_GROUP,
        filter.getEqualOperator(),
        _PARTNER_GROUP,
        false);
      filter.addSingleFilter(
        filter.getAndConnector(),
        Trigger.PARTNER_ID,
        filter.getEqualOperator(),
        _PARTNER_ID,
        false);
      Collection triggers = _remote.findTriggers(filter);
      if (!triggers.isEmpty())
      {
        return (Trigger)triggers.iterator().next();
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return null;
  }

  private BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    DeleteTriggerAction action = new DeleteTriggerAction();
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
    Log.log("TEST", "[DeleteTriggerActionTest.testDeleteNull] Enter ");

    try
    {
      _response = performEvent(null);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[DeleteTriggerActionTest.testDeleteNull]" +
        " Returning fail due to EventException: "+ex.getMessage());
      Log.log("TEST", "[DeleteTriggerActionTest.testDeleteNull] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[DeleteTriggerActionTest.testDeleteNull] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.DELETE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertNotNull("Error trace is null", _response.getErrorTrace());
    assertEquals("error type is incorrect", _response.getErrorType(), SystemException.SYSTEM);
    Log.log("TEST", "[DeleteTriggerActionTest.testDeleteNull] Exit ");
  }

  public void testDelete() throws Exception
  {
    Log.log("TEST", "[DeleteTriggerActionTest.testDelete] Enter ");

    createTestData();
    _trigger = findTrigger();
    if (_trigger != null)
    {
      Long uid = (Long)_trigger.getKey();
      _event = new DeleteTriggerEvent(uid);
      assertNotNull("event trigger UID is null", _event.getUids());
      try
      {
        _response = performEvent(_event);
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        Log.err("TEST", "[DeleteTriggerActionTest.testDelete] Error Exit ", ex);
        assertTrue("Event Exception", false);
      }

      //check response
      assertNotNull("response is null", _response);
      assertTrue("event is not successful", _response.isEventSuccessful());
      assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    }
    Log.log("TEST", "[DeleteTriggerActionTest.testDelete] Exit ");
  }

}