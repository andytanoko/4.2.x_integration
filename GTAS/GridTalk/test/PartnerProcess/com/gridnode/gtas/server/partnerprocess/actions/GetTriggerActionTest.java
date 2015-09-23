/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetTriggerActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 10 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import com.gridnode.gtas.events.partnerprocess.GetTriggerEvent;
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
import java.util.Map;

public class GetTriggerActionTest extends TestCase
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
  GetTriggerEvent _event;
  IPartnerProcessManagerHome _home;
  IPartnerProcessManagerObj _remote;
  Trigger _trigger;

  StateMachine _sm = new StateMachine(null, null);

  public GetTriggerActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetTriggerActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[GetTriggerActionTest.setUp] Enter");

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

    Log.log("TEST", "[GetTriggerActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[GetTriggerActionTest.tearDown] Enter");
    deleteTestData();
    Log.log("TEST", "[GetTriggerActionTest.tearDown] Exit");
  }

  private void createTestData()
  {
    Log.log("TEST", "[GetTriggerActionTest.createTestData] Enter");

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

    }
    Log.log("TEST", "[GetTriggerActionTest.createTestData] Exit");
  }

  private void deleteTestData()
  {
    Log.log("TEST", "[GetTriggerActionTest.deleteTestData] Enter");

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

    }
    Log.log("TEST", "[GetTriggerActionTest.deleteTestData] Exit");
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
    GetTriggerAction action = new GetTriggerAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _sessionID);
    _sm.setAttribute(IAttributeKeys.USER_ID, _userID);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  public void testGetNull()
  {
    Log.log("TEST", "[GetTriggerActionTest.testDeleteNull] Enter ");

    try
    {
      _response = performEvent(null);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[GetTriggerActionTest.testDeleteNull]" +
        " Returning fail due to EventException: "+ex.getMessage());
      Log.log("TEST", "[GetTriggerActionTest.testDeleteNull] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[GetTriggerActionTest.testDeleteNull] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.FIND_ENTITY_BY_KEY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertNotNull("Error trace is null", _response.getErrorTrace());
    assertEquals("error type is incorrect", _response.getErrorType(), SystemException.SYSTEM);
    Log.log("TEST", "[GetTriggerActionTest.testDeleteNull] Exit ");
  }

  public void testGet() throws Exception
  {
    Log.log("TEST", "[GetTriggerActionTest.testDelete] Enter ");

    createTestData();
    _trigger = findTrigger();
    Long uid = (Long)_trigger.getKey();
    _event = new GetTriggerEvent(uid);
    assertNotNull("event trigger UID is null", _event.getTriggerUid());
    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      Log.err("TEST", "[GetTriggerActionTest.testDelete] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    Object returnData = _response.getReturnData();
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof Map);

    Map triggerMap = (Map)returnData;

    assertEquals("trigger level is incorrect", _TRIGGER_LEVEL, triggerMap.get(Trigger.TRIGGER_LEVEL));
    assertEquals("partner function Id is incorrect", _PARTNER_FUNCTION_ID, triggerMap.get(Trigger.PARTNER_FUNCTION_ID));
    assertEquals("process Id is incorrect", _PROCESS_ID, triggerMap.get(Trigger.PROCESS_ID));
    assertEquals("document type is incorrect", _DOC_TYPE, triggerMap.get(Trigger.DOC_TYPE));
    assertEquals("partner type is incorrect", _PARTNER_TYPE, triggerMap.get(Trigger.PARTNER_TYPE));
    assertEquals("partner group is incorrect", _PARTNER_GROUP, triggerMap.get(Trigger.PARTNER_GROUP));
    assertEquals("Partner Id is incorrect", _PARTNER_ID, triggerMap.get(Trigger.PARTNER_ID));

    Log.log("TEST", "[GetTriggerActionTest.testDelete] Exit ");
  }

}