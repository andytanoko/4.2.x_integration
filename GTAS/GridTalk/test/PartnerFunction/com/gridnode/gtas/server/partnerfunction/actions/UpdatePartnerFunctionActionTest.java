package com.gridnode.gtas.server.partnerfunction.actions;

import com.gridnode.gtas.events.partnerfunction.UpdatePartnerFunctionEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.gtas.server.partnerfunction.model.*;
import com.gridnode.gtas.server.partnerfunction.facade.ejb.*;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;

import java.util.Iterator;
import java.util.Vector;

public class UpdatePartnerFunctionActionTest extends TestCase
{
  static final String  _PF_ID             = "PF01";
  static final String  _PF_DESC           = "Partner Function Description";
  static final Integer _TRIGGER_ON        = IPartnerFunction.TRIGGER_IMPORT;
  static final Integer _ACTIVITY_TYPE     = IWorkflowActivity.MAPPING_RULE;
  static final Integer _MAPPING_RULE_UID  = new Integer(1);
  static final String  _ACTIVITY_DESC     = "Activity Description";

  static final String  _UPD_PF_DESC          = "Updated Partner Function Description";
  static final Integer _UPD_TRIGGER_ON       = IPartnerFunction.TRIGGER_RECEIVE;
  static final Integer _UPD_ACTIVITY_TYPE    = IWorkflowActivity.USER_PROCEDURE;
  static final Integer _USER_PROCEDURE_UID   = new Integer(2);
  static final String  _UPD_ACTIVITY_DESC    = "Updated Activity Description";


  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  UpdatePartnerFunctionEvent _event;
  IPartnerFunctionManagerHome _home;
  IPartnerFunctionManagerObj _remote;
  PartnerFunction _partnerFunction;

  StateMachine _sm = new StateMachine(null, null);

  public UpdatePartnerFunctionActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(UpdatePartnerFunctionActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[UpdatePartnerFunctionActionTest.setUp] Enter");

    _sessionHome = (ISessionManagerHome)ServiceLookup.getInstance(
                   ServiceLookup.CLIENT_CONTEXT).getHome(
                   ISessionManagerHome.class);
    assertNotNull("SessionHome is null", _sessionHome);
    _sessionRemote = _sessionHome.create();
    assertNotNull("SessionRemote is null", _sessionRemote);
    _sessionID = _sessionRemote.openSession();
    _sessionRemote.authSession(_sessionID, _userID);

    _home = (IPartnerFunctionManagerHome)ServiceLookup.getInstance(
             ServiceLookup.CLIENT_CONTEXT).getHome(
             IPartnerFunctionManagerHome.class);
    assertNotNull("Home is null", _home);
    _remote = _home.create();
    assertNotNull("remote is null", _remote);

    Log.log("TEST", "[UpdatePartnerFunctionActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[UpdatePartnerFunctionActionTest.tearDown] Enter");
    deleteTestData();
    Log.log("TEST", "[UpdatePartnerFunctionActionTest.tearDown] Exit");
  }

  protected UpdatePartnerFunctionEvent createTestEvent()
    throws Exception
  {
    PartnerFunction pf = _remote.findPartnerFunction(_PF_ID);
    Long pfUid = new Long(pf.getUId());
    Vector activities = new Vector();
    Vector activity1 = new Vector();
    activity1.add(_UPD_ACTIVITY_TYPE);
    activity1.add(_UPD_ACTIVITY_DESC);
    activity1.add(_USER_PROCEDURE_UID);
    activities.add(activity1);
    UpdatePartnerFunctionEvent event = new UpdatePartnerFunctionEvent(pfUid,
                                             _UPD_PF_DESC,
                                             _UPD_TRIGGER_ON,
                                             activities);
    return event;
  }

  protected UpdatePartnerFunctionEvent createTestMultiEvent()
    throws Exception
  {
    PartnerFunction pf = _remote.findPartnerFunction(_PF_ID);
    Long pfUid = new Long(pf.getUId());
    Vector activities = new Vector();

    Vector activity1 = new Vector();
    activity1.add(_UPD_ACTIVITY_TYPE);
    activity1.add(_UPD_ACTIVITY_DESC);
    activity1.add(_USER_PROCEDURE_UID);
    activities.add(activity1);

    Vector activity2 = new Vector();
    activity2.add(_ACTIVITY_TYPE);
    activity2.add(_ACTIVITY_DESC);
    activity2.add(_MAPPING_RULE_UID);
    activities.add(activity2);

    UpdatePartnerFunctionEvent event = new UpdatePartnerFunctionEvent(pfUid,
                                             _UPD_PF_DESC,
                                             _UPD_TRIGGER_ON,
                                             activities);
    return event;
  }

  private void createTestData()
  {
    Log.log("TEST", "[UpdatePartnerFunctionActionTest.createTestData] Enter");

    try
    {
      PartnerFunction partnerFunction = new PartnerFunction();
      partnerFunction.setPartnerFunctionId(_PF_ID);
      partnerFunction.setDescription(_PF_DESC);
      partnerFunction.setTriggerOn(_TRIGGER_ON);
      WorkflowActivity activity = new WorkflowActivity();
      activity.setActivityType(_ACTIVITY_TYPE);
      activity.setDescription(_ACTIVITY_DESC);
      Long uid = _remote.createWorkflowActivity(activity);
      partnerFunction.addWorkflowActivityUid(uid);

      _remote.createPartnerFunction(partnerFunction);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    Log.log("TEST", "[UpdatePartnerFunctionActionTest.createTestData] Exit");

  }

  private void deleteTestData()
  {
    Log.log("TEST", "[UpdatePartnerFunctionActionTest.deleteTestData] Enter");

    try
    {
      PartnerFunction partnerFunction = _remote.findPartnerFunction(_PF_ID);
      if (partnerFunction != null)
      {
        _remote.deletePartnerFunction((Long)partnerFunction.getKey());
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    Log.log("TEST", "[UpdatePartnerFunctionActionTest.deleteTestData] Exit");
  }

  private BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    UpdatePartnerFunctionAction action = new UpdatePartnerFunctionAction();
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
    Log.log("TEST", "[UpdatePartnerFunctionActionTest.testUpdate] Enter ");

    deleteTestData();
    createTestData();
    _event = createTestEvent();
    assertNotNull("event Description is null", _event.getPartnerFunctionDesc());
    assertNotNull("event ProgramName is null", _event.getTriggerOn());
    assertNotNull("event ProgramPath is null", _event.getWorkflowActivities());
    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      Log.err("TEST", "[UpdatePartnerFunctionActionTest.testUpdate] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    _partnerFunction = _remote.findPartnerFunction(_PF_ID);
    Log.debug("TEST", "Found doctype : "+_partnerFunction);
    assertNotNull("Doctype retrieved is null", _partnerFunction);

    assertNotNull("UID is null", _partnerFunction.getFieldValue(PartnerFunction.UID));
    assertEquals("Description is incorrect", _UPD_PF_DESC, _partnerFunction.getDescription());
    assertEquals("Trigger is incorrect", _UPD_TRIGGER_ON, _partnerFunction.getTriggerOn());

    WorkflowActivity workflow = (WorkflowActivity)_partnerFunction.getWorkflowActivities().get(0);
    assertEquals("WorkflowActivity Type is incorrect", _UPD_ACTIVITY_TYPE, workflow.getActivityType());
    assertEquals("WorkflowActivity Desc is incorrect", _UPD_ACTIVITY_DESC, workflow.getDescription());
    Vector params = workflow.getParamList();
    assertEquals("WorkflowActivity param is incorrect", _USER_PROCEDURE_UID, (Integer)params.get(0));

    Log.log("TEST", "[UpdatePartnerFunctionActionTest.testUpdate] Exit ");
  }

  public void testUpdateMultiWFActivities() throws Exception
  {
    Log.log("TEST", "[UpdatePartnerFunctionActionTest.testUpdateMultiWFActivities] Enter ");

    deleteTestData();
    createTestData();
    _event = createTestMultiEvent();
    assertNotNull("event Description is null", _event.getPartnerFunctionDesc());
    assertNotNull("event ProgramName is null", _event.getTriggerOn());
    assertNotNull("event ProgramPath is null", _event.getWorkflowActivities());
    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      Log.err("TEST", "[UpdatePartnerFunctionActionTest.testUpdateMultiWFActivities] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event is not successful", _response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.NO_ERROR, _response.getMessageCode());

    _partnerFunction = _remote.findPartnerFunction(_PF_ID);
    Log.debug("TEST", "Found doctype : "+_partnerFunction);
    assertNotNull("Doctype retrieved is null", _partnerFunction);

    assertNotNull("UID is null", _partnerFunction.getFieldValue(PartnerFunction.UID));
    assertEquals("Description is incorrect", _UPD_PF_DESC, _partnerFunction.getDescription());
    assertEquals("Trigger is incorrect", _UPD_TRIGGER_ON, _partnerFunction.getTriggerOn());

    WorkflowActivity workflow = (WorkflowActivity)_partnerFunction.getWorkflowActivities().get(0);
    assertEquals("WorkflowActivity 1 Type is incorrect", _UPD_ACTIVITY_TYPE, workflow.getActivityType());
    assertEquals("WorkflowActivity 1 Desc is incorrect", _UPD_ACTIVITY_DESC, workflow.getDescription());
    Vector params = workflow.getParamList();
    assertEquals("WorkflowActivity 1 param is incorrect", _USER_PROCEDURE_UID, (Integer)params.get(0));

    workflow = (WorkflowActivity)_partnerFunction.getWorkflowActivities().get(1);
    assertEquals("WorkflowActivity 2 Type is incorrect", _ACTIVITY_TYPE, workflow.getActivityType());
    assertEquals("WorkflowActivity 2 Desc is incorrect", _ACTIVITY_DESC, workflow.getDescription());
    Vector params2 = workflow.getParamList();
    assertEquals("WorkflowActivity 2 param is incorrect", _MAPPING_RULE_UID, (Integer)params2.get(0));

    Log.log("TEST", "[UpdatePartnerFunctionActionTest.testUpdateMultiWFActivities] Exit ");
  }
}