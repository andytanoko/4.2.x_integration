/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateTriggerActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 10 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerprocess.actions;

import com.gridnode.gtas.events.partnerprocess.UpdateTriggerEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.gtas.server.partnerprocess.model.*;
import com.gridnode.gtas.server.partnerprocess.facade.ejb.*;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class UpdateTriggerActionTest extends TestCase
{
  static final Integer _TRIGGER_LEVEL       = ITrigger.LEVEL_0;
  static final String  _PARTNER_FUNCTION_ID = "PF01";
  static final String  _PROCESS_ID          = "Process Id";
  static final String  _DOC_TYPE            = "UC";
  static final String  _PARTNER_TYPE        = "SU";
  static final String  _PARTNER_GROUP       = "LS";
  static final String  _PARTNER_ID          = "Partner Id";

  static final Integer _UPD_TRIGGER_LEVEL       = ITrigger.LEVEL_1;
  static final String  _UPD_PARTNER_FUNCTION_ID = "PF02";
  static final String  _UPD_PROCESS_ID          = "Process Id 2";
  static final String  _UPD_DOC_TYPE            = "PO";
  static final String  _UPD_PARTNER_TYPE        = "CU";
  static final String  _UPD_PARTNER_GROUP       = "LC";
  static final String  _UPD_PARTNER_ID          = "Partner Id 2";

  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  UpdateTriggerEvent _event;
  IPartnerProcessManagerHome _home;
  IPartnerProcessManagerObj _remote;
  Trigger _trigger;

  StateMachine _sm = new StateMachine(null, null);

  public UpdateTriggerActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(UpdateTriggerActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[UpdateTriggerActionTest.setUp] Enter");

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

    Log.log("TEST", "[UpdateTriggerActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[UpdateTriggerActionTest.tearDown] Enter");
    deleteTestData(_UPD_TRIGGER_LEVEL, _UPD_DOC_TYPE, _UPD_PARTNER_TYPE, _UPD_PARTNER_GROUP, _UPD_PARTNER_ID);
    Log.log("TEST", "[UpdateTriggerActionTest.tearDown] Exit");
  }

  protected UpdateTriggerEvent createTestEvent(Long triggerUid)
  {
    UpdateTriggerEvent event = new UpdateTriggerEvent(triggerUid,
                                                      _UPD_TRIGGER_LEVEL,
                                                      _UPD_PARTNER_FUNCTION_ID,
                                                      _UPD_PROCESS_ID,
                                                      _UPD_DOC_TYPE,
                                                      _UPD_PARTNER_TYPE,
                                                      _UPD_PARTNER_GROUP,
                                                      _UPD_PARTNER_ID,
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
    Log.log("TEST", "[UpdateTriggerActionTest.createTestData] Enter");

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
    Log.log("TEST", "[UpdateTriggerActionTest.createTestData] Exit");

  }

  private void deleteTestData(Integer triggerLevel, String docType, String partnerType,
                              String partnerGroup, String partnerID)
  {
    Log.log("TEST", "[UpdateTriggerActionTest.deleteTestData] Enter");

    try
    {
      Trigger trigger = findTrigger(triggerLevel, docType, partnerType, partnerGroup, partnerID);
      if (trigger != null)
      {
        _remote.deleteTrigger((Long)trigger.getKey());
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    Log.log("TEST", "[UpdateTriggerActionTest.deleteTestData] Exit");
  }

  private Trigger findTrigger(Integer triggerLevel, String docType, String partnerType,
                              String partnerGroup, String partnerID)
  {
    try
    {
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(
        null,
        Trigger.TRIGGER_LEVEL,
        filter.getEqualOperator(),
        triggerLevel,
        false);
      filter.addSingleFilter(
        filter.getAndConnector(),
        Trigger.DOC_TYPE,
        filter.getEqualOperator(),
        docType,
        false);
      filter.addSingleFilter(
        filter.getAndConnector(),
        Trigger.PARTNER_TYPE,
        filter.getEqualOperator(),
        partnerType,
        false);
      filter.addSingleFilter(
        filter.getAndConnector(),
        Trigger.PARTNER_GROUP,
        filter.getEqualOperator(),
        partnerGroup,
        false);
      filter.addSingleFilter(
        filter.getAndConnector(),
        Trigger.PARTNER_ID,
        filter.getEqualOperator(),
        partnerID,
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
    UpdateTriggerAction action = new UpdateTriggerAction();
    _sm.setAttribute(IAttributeKeys.SESSION_ID, _sessionID);
    _sm.setAttribute(IAttributeKeys.USER_ID, _userID);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }


  public void testUpdate() throws Exception
  {
    Log.log("TEST", "[UpdateTriggerActionTest.testCreateNew] Enter ");

    deleteTestData(_TRIGGER_LEVEL, _DOC_TYPE, _PARTNER_TYPE, _PARTNER_GROUP, _PARTNER_ID);
    createTestData();
    _trigger = findTrigger(_TRIGGER_LEVEL, _DOC_TYPE, _PARTNER_TYPE, _PARTNER_GROUP, _PARTNER_ID);
    _event = createTestEvent((Long)_trigger.getKey());
    assertNotNull("event Trigger Uid is null", _event.getTriggerUid());
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
      Log.err("TEST", "[UpdateTriggerActionTest.testCreateNew] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    _trigger = findTrigger(_UPD_TRIGGER_LEVEL, _UPD_DOC_TYPE, _UPD_PARTNER_TYPE, _UPD_PARTNER_GROUP, _UPD_PARTNER_ID);
    assertNotNull("Trigger retrieved is null", _trigger);

    assertNotNull("UID is null", _trigger.getFieldValue(Trigger.UID));
    assertEquals("Trigger Level is incorrect", _UPD_TRIGGER_LEVEL, _trigger.getLevel());
    assertEquals("Partner Function Id is incorrect", _UPD_PARTNER_FUNCTION_ID, _trigger.getPartnerFunctionId());
    assertEquals("Process Id is incorrect", _UPD_PROCESS_ID, _trigger.getProcessId());
    assertEquals("Document Type is incorrect", _UPD_DOC_TYPE, _trigger.getDocumentType());
    assertEquals("Partner Type is incorrect", _UPD_PARTNER_TYPE, _trigger.getPartnerType());
    assertEquals("Partner Group is incorrect", _UPD_PARTNER_GROUP, _trigger.getPartnerGroup());
    assertEquals("Partner Id is incorrect", _UPD_PARTNER_ID, _trigger.getPartnerId());

    Log.log("TEST", "[UpdateTriggerActionTest.testCreateNew] Exit ");
  }

}