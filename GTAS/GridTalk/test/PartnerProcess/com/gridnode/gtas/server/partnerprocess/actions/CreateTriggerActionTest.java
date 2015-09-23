/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateTriggerActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import com.gridnode.gtas.events.partnerprocess.CreateTriggerEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.gtas.server.partnerprocess.model.*;
import com.gridnode.gtas.server.partnerprocess.facade.ejb.*;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class CreateTriggerActionTest extends TestCase
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
  CreateTriggerEvent _event;
  IPartnerProcessManagerHome _home;
  IPartnerProcessManagerObj _remote;
  Trigger _trigger;

  StateMachine _sm = new StateMachine(null, null);

  public CreateTriggerActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(CreateTriggerActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[CreateTriggerActionTest.setUp] Enter");

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

    Log.log("TEST", "[CreateTriggerActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[CreateTriggerActionTest.tearDown] Enter");
    deleteTestData();
    Log.log("TEST", "[CreateTriggerActionTest.tearDown] Exit");
  }

  protected CreateTriggerEvent createTestEvent()
  {
    CreateTriggerEvent event = new CreateTriggerEvent(_TRIGGER_LEVEL,
                                                      _PARTNER_FUNCTION_ID,
                                                      _PROCESS_ID,
                                                      _DOC_TYPE,
                                                      _PARTNER_TYPE,
                                                      _PARTNER_GROUP,
                                                      _PARTNER_ID,
                                                      Trigger.TRIGGER_IMPORT,
                                                      Boolean.FALSE,
                                                      Boolean.FALSE,
                                                      new Integer(0),
                                                      new Integer(0),
                                                      null);
    return event;
  }

  private void createTestData()
  {
    Log.log("TEST", "[CreateTriggerActionTest.createTestData] Enter");

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
    Log.log("TEST", "[CreateTriggerActionTest.createTestData] Exit");

  }

  private void deleteTestData()
  {
    Log.log("TEST", "[CreateTriggerActionTest.deleteTestData] Enter");

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
    Log.log("TEST", "[CreateTriggerActionTest.deleteTestData] Exit");
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
    CreateTriggerAction action = new CreateTriggerAction();
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
    Log.log("TEST", "[CreateTriggerActionTest.testCreateNull] Enter ");

    try
    {
      _response = performEvent(null);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[CreateTriggerActionTest.testCreateNull]" +
        " Returning fail due to EventException: "+ex.getMessage());
      Log.log("TEST", "[CreateTriggerActionTest.testCreateNull] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[CreateTriggerActionTest.testCreateNull] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.CREATE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertNotNull("Error trace is null", _response.getErrorTrace());
    assertEquals("error type is incorrect", _response.getErrorType(), SystemException.SYSTEM);
    Log.log("TEST", "[CreateTriggerActionTest.testCreateNull] Exit ");
  }

  public void testCreateNew() throws Exception
  {
    Log.log("TEST", "[CreateTriggerActionTest.testCreateNew] Enter ");

    deleteTestData();
    _event = createTestEvent();
    assertNotNull("event Trigger Level is null", _event.getTriggerLevel());
    assertNotNull("event PartnerFunction Id is null", _event.getPartnerFunctionId());
    assertNotNull("event Process Id is null", _event.getProcessId());
    assertNotNull("event Document Type is null", _event.getDocType());
    assertNotNull("event Partner Type is null", _event.getPartnerType());
    assertNotNull("event Partner Group is null", _event.getPartnerGroup());
    assertNotNull("event Partner Id is null", _event.getPartnerId());
    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.out);
      Log.err("TEST", "[CreateTriggerActionTest.testCreateNew] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    _trigger = findTrigger();
    assertNotNull("Trigger retrieved is null", _trigger);

    assertNotNull("UID is null", _trigger.getFieldValue(Trigger.UID));
    assertEquals("Trigger Level is incorrect", _TRIGGER_LEVEL, _trigger.getLevel());
    assertEquals("Partner Function Id is incorrect", _PARTNER_FUNCTION_ID, _trigger.getPartnerFunctionId());
    assertEquals("Process Id is incorrect", _PROCESS_ID, _trigger.getProcessId());
    assertEquals("Document Type is incorrect", _DOC_TYPE, _trigger.getDocumentType());
    assertEquals("Partner Type is incorrect", _PARTNER_TYPE, _trigger.getPartnerType());
    assertEquals("Partner Group is incorrect", _PARTNER_GROUP, _trigger.getPartnerGroup());
    assertEquals("Partner Id is incorrect", _PARTNER_ID, _trigger.getPartnerId());

    Log.log("TEST", "[CreateTriggerActionTest.testCreateNew] Exit ");
  }

  public void testCreateDuplicate() throws Exception
  {
    Log.log("TEST", "[CreateTriggerActionTest.testCreateDuplicate] Enter ");

    createTestData();
    _event = createTestEvent();

    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[CreateTriggerActionTest.testCreateDuplicate] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.CREATE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertEquals("error type is incorrect", _response.getErrorType(), DuplicateEntityException.APPLICATION);

    Log.log("TEST", "[CreateTriggerActionTest.testCreateDuplicate] Exit ");
  }
}