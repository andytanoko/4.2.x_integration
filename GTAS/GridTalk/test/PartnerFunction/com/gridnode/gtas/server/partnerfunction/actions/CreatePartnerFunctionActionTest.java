package com.gridnode.gtas.server.partnerfunction.actions;

import com.gridnode.gtas.events.partnerfunction.CreatePartnerFunctionEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.gtas.server.partnerfunction.model.*;
import com.gridnode.gtas.server.partnerfunction.facade.ejb.*;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;

import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class CreatePartnerFunctionActionTest extends TestCase
{
  static final String  _PF_ID            = "PF01";
  static final String  _PF_DESC          = "Partner Function Description";
  static final Integer _TRIGGER_ON       = IPartnerFunction.TRIGGER_IMPORT;
  static final Integer _ACTIVITY1_TYPE   = IWorkflowActivity.MAPPING_RULE;
  static final Integer _MAPPING_RULE_UID = new Integer(1);
  static final String  _ACTIVITY1_DESC   = "Mapping Rule";
  static final Integer _ACTIVITY2_TYPE   = IWorkflowActivity.SAVE_TO_FOLDER;
  static final String  _ACTIVITY2_DESC   = "Save To System Folder";
  static final Integer _ACTIVITY3_TYPE   = IWorkflowActivity.EXIT_TO_PORT;
  static final String  _ACTIVITY3_DESC   = "Exit To Port";
  static final Integer _PORT_UID         = new Integer(2);
  static final Integer _ACTIVITY4_TYPE   = IWorkflowActivity.EXIT_WORKFLOW;
  static final String  _ACTIVITY4_DESC   = "Exit Workflow";

  ISessionManagerHome _sessionHome;
  ISessionManagerObj _sessionRemote;
  String _sessionID;
  String _userID = "admin";

  BasicEventResponse _response;
  CreatePartnerFunctionEvent _event;
  IPartnerFunctionManagerHome _home;
  IPartnerFunctionManagerObj _remote;
  PartnerFunction _partnerFunction;

  StateMachine _sm = new StateMachine(null, null);

  public CreatePartnerFunctionActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(CreatePartnerFunctionActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws java.lang.Exception
  {
    Log.log("TEST", "[CreatePartnerFunctionActionTest.setUp] Enter");

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

    Log.log("TEST", "[CreatePartnerFunctionActionTest.setUp] Exit");
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[CreatePartnerFunctionActionTest.tearDown] Enter");
    deleteTestData();
    Log.log("TEST", "[CreatePartnerFunctionActionTest.tearDown] Exit");
  }

  protected CreatePartnerFunctionEvent createTestEvent()
  {
    Vector activities = new Vector();
    Vector activity1 = new Vector();
    activity1.add(_ACTIVITY1_TYPE);
    activity1.add(_ACTIVITY1_DESC);
    activity1.add(_MAPPING_RULE_UID);
    activities.add(activity1);
    Vector activity2 = new Vector();
    activity2.add(_ACTIVITY2_TYPE);
    activity2.add(_ACTIVITY2_DESC);
    activities.add(activity2);
    Vector activity3 = new Vector();
    activity3.add(_ACTIVITY3_TYPE);
    activity3.add(_ACTIVITY3_DESC);
    activity3.add(_PORT_UID);
    activities.add(activity3);
    Vector activity4 = new Vector();
    activity4.add(_ACTIVITY4_TYPE);
    activity4.add(_ACTIVITY4_DESC);
    activities.add(activity4);
    CreatePartnerFunctionEvent event = new CreatePartnerFunctionEvent(_PF_ID,
                                             _PF_DESC,
                                             _TRIGGER_ON,
                                             activities);
    return event;
  }

  private void createTestData()
  {
    Log.log("TEST", "[CreatePartnerFunctionActionTest.createTestData] Enter");

    try
    {
      PartnerFunction partnerFunction = new PartnerFunction();
      partnerFunction.setPartnerFunctionId(_PF_ID);
      partnerFunction.setDescription(_PF_DESC);
      partnerFunction.setTriggerOn(_TRIGGER_ON);
      WorkflowActivity activity = new WorkflowActivity();
      activity.setActivityType(_ACTIVITY1_TYPE);
      activity.setDescription(_ACTIVITY1_DESC);
      activity.addParam(_MAPPING_RULE_UID);
      Long uid = _remote.createWorkflowActivity(activity);
      partnerFunction.addWorkflowActivityUid(uid);

      _remote.createPartnerFunction(partnerFunction);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    Log.log("TEST", "[CreatePartnerFunctionActionTest.createTestData] Exit");

  }

  private void deleteTestData()
  {
    Log.log("TEST", "[CreatePartnerFunctionActionTest.deleteTestData] Enter");

    try
    {
      PartnerFunction partnerFunction = _remote.findPartnerFunction(_PF_ID);
      if (partnerFunction != null)
      {
        List wfList = partnerFunction.getWorkflowActivities();
        for (Iterator i = wfList.iterator(); i.hasNext();)
        {
          Long uid = new Long(i.next().toString());
          _remote.deleteWorkflowActivity(uid);
        }
        _remote.deletePartnerFunction((Long)partnerFunction.getKey());
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    Log.log("TEST", "[CreatePartnerFunctionActionTest.deleteTestData] Exit");
  }

  private BasicEventResponse performEvent(IEvent event)
    throws Exception
  {
    CreatePartnerFunctionAction action = new CreatePartnerFunctionAction();
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
    Log.log("TEST", "[CreatePartnerFunctionActionTest.testCreateNull] Enter ");

    try
    {
      _response = performEvent(null);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[CreatePartnerFunctionActionTest.testCreateNull]" +
        " Returning fail due to EventException: "+ex.getMessage());
      Log.log("TEST", "[CreatePartnerFunctionActionTest.testCreateNull] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[CreatePartnerFunctionActionTest.testCreateNull] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.CREATE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    assertNotNull("Error trace is null", _response.getErrorTrace());
    assertEquals("error type is incorrect", _response.getErrorType(), SystemException.SYSTEM);
    Log.log("TEST", "[CreatePartnerFunctionActionTest.testCreateNull] Exit ");
  }

  public void testCreateNew() throws Exception
  {
    Log.log("TEST", "[CreatePartnerFunctionActionTest.testCreateNew] Enter ");

    deleteTestData();
    _event = createTestEvent();
    assertNotNull("event PartnerFunction is null", _event.getPartnerFunctionId());
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
      Log.err("TEST", "[CreatePartnerFunctionActionTest.testCreateNew] Error Exit ", ex);
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
    assertEquals("Id is incorrect", _PF_ID, _partnerFunction.getPartnerFunctionId());
    assertEquals("Description is incorrect", _PF_DESC, _partnerFunction.getDescription());
    assertEquals("Trigger is incorrect", _TRIGGER_ON, _partnerFunction.getTriggerOn());

    Long uid = new Long(_partnerFunction.getWorkflowActivities().get(0).toString());
    WorkflowActivity workflow = _remote.findWorkflowActivity(uid);
    assertNotNull("WorkflowActivity1 is null", workflow);
    System.out.println("WorkflowActivity1 = "+workflow);
    assertEquals("WorkflowActivity1 Type is incorrect", _ACTIVITY1_TYPE, workflow.getActivityType());
    assertEquals("WorkflowActivity1 Desc is incorrect", _ACTIVITY1_DESC, workflow.getDescription());
    Vector params = workflow.getParamList();
    assertEquals("WorkflowActivity1 param is incorrect", _MAPPING_RULE_UID, new Integer(params.get(0).toString()));

    uid = new Long(_partnerFunction.getWorkflowActivities().get(1).toString());
    workflow = _remote.findWorkflowActivity(uid);
    assertNotNull("WorkflowActivity2 is null", workflow);
    System.out.println("WorkflowActivity2 = "+workflow);
    assertEquals("WorkflowActivity2 Type is incorrect", _ACTIVITY2_TYPE, workflow.getActivityType());
    assertEquals("WorkflowActivity2 Desc is incorrect", _ACTIVITY2_DESC, workflow.getDescription());

    uid = new Long(_partnerFunction.getWorkflowActivities().get(2).toString());
    workflow = _remote.findWorkflowActivity(uid);
    assertNotNull("WorkflowActivity3 is null", workflow);
    System.out.println("WorkflowActivity3 = "+workflow);
    assertEquals("WorkflowActivity3 Type is incorrect", _ACTIVITY3_TYPE, workflow.getActivityType());
    assertEquals("WorkflowActivity3 Desc is incorrect", _ACTIVITY3_DESC, workflow.getDescription());
    params = workflow.getParamList();
    assertEquals("WorkflowActivity3 param is incorrect", _PORT_UID, new Integer(params.get(0).toString()));

    uid = new Long(_partnerFunction.getWorkflowActivities().get(3).toString());
    workflow = _remote.findWorkflowActivity(uid);
    assertNotNull("WorkflowActivity3 is null", workflow);
    System.out.println("WorkflowActivity4 = "+workflow);
    assertEquals("WorkflowActivity3 Type is incorrect", _ACTIVITY4_TYPE, workflow.getActivityType());
    assertEquals("WorkflowActivity3 Desc is incorrect", _ACTIVITY4_DESC, workflow.getDescription());

    Log.log("TEST", "[CreatePartnerFunctionActionTest.testCreateNew] Exit ");
  }

  public void testCreateDuplicate() throws Exception
  {
    Log.log("TEST", "[CreatePartnerFunctionActionTest.testCreateDuplicate] Enter ");

    createTestData();
    _event = createTestEvent();

    try
    {
      _response = performEvent(_event);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[CreatePartnerFunctionActionTest.testCreateDuplicate] Error Exit ", ex);
      assertTrue("Event Exception", false);
    }

    //check response
    assertNotNull("response is null", _response);
    assertTrue("event status is incorrect", !_response.isEventSuccessful());
    assertEquals("Msg code incorrect", IErrorCode.CREATE_ENTITY_ERROR, _response.getMessageCode());
    assertNotNull("Error reason is null", _response.getErrorReason());
    System.out.println("_response.getErrorType() = "+_response.getErrorType());
    assertEquals("error type is incorrect", _response.getErrorType(), DuplicateEntityException.APPLICATION);

    Log.log("TEST", "[CreatePartnerFunctionActionTest.testCreateDuplicate] Exit ");
  }
}