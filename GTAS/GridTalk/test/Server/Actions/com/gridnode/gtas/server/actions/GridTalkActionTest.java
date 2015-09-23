/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Ang Meng Hua        Created
 * Jul 21 2003    Neo Sok Lay         Add various methods for common handling.
 */
package com.gridnode.gtas.server.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ReflectionUtility;
import com.gridnode.pdip.framework.util.ServiceLocator;

import junit.framework.TestCase;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This defines the interface for testing the GridTalk Action class.
 *
 * @author Ang Meng Hua
 *
 * @version GT 2.2 I1
 * @since 2.0.2
 */
public abstract class GridTalkActionTest extends TestCase
{

  protected class ExpectedResult
  {
    boolean _isSuccess;
    int _failCode;
    int _errorType;
    
    public ExpectedResult(boolean success, int errorType, int failCode)
    {
      _isSuccess = success;
      _errorType = errorType;
      _failCode = failCode;
    }
  }

  private static final String LOG_PATTERN = "[{0}.{1}] {2}";

  protected static final int ERROR_TYPE_NA = -1;
  protected static final int ERROR_TYPE_SYSTEM = SystemException.SYSTEM;
  protected static final int ERROR_TYPE_APPLICATION = ApplicationException.APPLICATION;
  
  protected static final String ADMIN_USERID = "admin";

  private static String DEFAULT_SESSION_ID   = "001";
  private static String DEFAULT_USER_ID      = "admin";

  protected Map _testDataSet = null;
  protected BasicEventResponse _response = null;
  protected Map _resultDataSet;
  protected String _currTestID = null;
  protected String[] TEST_IDS;

  StateMachine _sm = new StateMachine(null, null);

  protected ISessionManagerObj _sessionMgr;
  protected ArrayList _openedSessions = new ArrayList();
  protected String[]            _sessions;
  protected StateMachine[]      _stateMs;

  public GridTalkActionTest(String name)
  {
    super(name);
  }

  // **************** Setup & CleanUp *************************
  
  /**
   * This method should lookup the Home interface for the EntityBean.
   *
   * @return The Home interface object for the EntityBean.
   */
  protected void setUp() throws java.lang.Exception
  {
    try
    {
      log("setUp", "Enter");

      _sessionMgr = getSessionMgr();
      setupManagers();
      
      cleanUp();
    }
    finally
    {
      log("setUp", "Exit");
    }
  }

  protected void setupManagers() throws Exception
  {
  }
  
  protected void cleanUp() throws Exception
  {
  }
  
  protected void tearDown() throws java.lang.Exception
  {
    try
    {
      log("tearDown", "Enter");
      cleanUp();
    }
    finally
    {
      log("tearDown", "Exit");
    }
  }

  protected abstract Class getEventClass();

  protected abstract Class getActionClass();

  protected abstract String getClassName();

  protected abstract short getDefaultMsgCode();

  protected short getDefaultErrorType()
  {
    return ApplicationException.APPLICATION;
  }

  protected Object getTestData(String testDataID)
  {
    return _testDataSet.get(testDataID);
  }

  protected void putTestData(String testDataID, Object testData)
  {
    if (_testDataSet == null)
      _testDataSet = new HashMap();

    _testDataSet.put(testDataID, testData);
  }

  protected void putResultData(String testDataID, Object resultData)
  {
    if (_resultDataSet == null)
      _resultDataSet = new HashMap();

    _resultDataSet.put(testDataID, resultData);
  }

  protected Object getResultData(String testDataID)
  {
    return _resultDataSet.get(testDataID);
  }

  protected ArrayList prepareEventData(String testDataID)
  {
    return null;
  }

  protected IEvent createTestEvent(
    Class eventClass, Class[] paramClass, Object[] params)
  {
    return (IEvent)ReflectionUtility.newInstance(
             eventClass.getName(),
             paramClass,
             params);
  }

  protected IEvent createTestEvent(Class eventClass, Object[] params)
  {
    return (IEvent)ReflectionUtility.newInstance(
             eventClass.getName(),
             ReflectionUtility.getSignature(params),
             params);
  }

  protected AbstractGridTalkAction createTestAction(Class actionClass, Object[] params)
  {
    return (AbstractGridTalkAction)ReflectionUtility.newInstance(
             actionClass.getName(),
             ReflectionUtility.getSignature(params),
             params);
  }

  protected IEJBAction createNewAction()
  {
    return createTestAction(getActionClass(), new Object[0]);
  }

  // ************* Run the test and check results ******************
  
  protected BasicEventResponse performEvent(IEvent event) throws java.lang.Exception
  {
    IEJBAction action = createTestAction(getActionClass(), new Object[0]);
    _sm.setAttribute(IAttributeKeys.SESSION_ID, DEFAULT_SESSION_ID);
    _sm.setAttribute(IAttributeKeys.USER_ID, DEFAULT_USER_ID);

    action.init(_sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse)action.perform(event);
    action.doEnd();
    return response;
  }

  protected BasicEventResponse performEvent(
    IEJBAction action, IEvent event, String session, StateMachine sm)
    throws Exception
  {
    long startTime = System.currentTimeMillis();

    sm.setAttribute(IAttributeKeys.SESSION_ID, session);
    sm.setAttribute(IAttributeKeys.USER_ID, ADMIN_USERID);

    action.init(sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    
    long endTime = System.currentTimeMillis();
    log("performEvent", "Event TimeToPerform (ms): "+(endTime-startTime));    
    return response;
  }
  
  protected void checkFail(
    IEvent event, String session, StateMachine sm, boolean eventEx,
    short msgCode)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(createNewAction(), event, session, sm);
    }
    catch (EventException ex)
    {
      log("checkFail", "Returning fail due to EventException: "+ex.getMessage());
      if (!eventEx)
        assertTrue("Unexpected event exception caught: "+ex.getMessage(), false);

      log("checkFail", "Exit");
      return;
    }
    catch (Exception ex)
    {
      err("checkFail", "Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponseFail(response, msgCode);
    checkFailEffect(response, event, sm);
  }

  protected void checkSuccess(
    IEvent event, String session, StateMachine sm, short msgCode)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(createNewAction(), event, session, sm);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.checkSuccess] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponsePass(response, msgCode);

    checkSuccessEffect(response, event, sm);
  }

  protected void assertResponseFail(BasicEventResponse response, short msgCode)
  {
    assertNotNull("Null Response", response);
    assertEquals("Incorrect Event Status", false, response.isEventSuccessful());
    assertEquals("Incorrect Msg Code", msgCode, response.getMessageCode());
    assertNotNull("Null Error reason", response.getErrorReason());
    assertEquals("Incorrect Error Type", getDefaultErrorType(), response.getErrorType());
  }

  protected void assertResponsePass(BasicEventResponse response, short msgCode)
  {
    assertNotNull("Null Response", response);
    assertEquals("Incorrect Event Status", true, response.isEventSuccessful());
    assertEquals("Incorrect Msg code", msgCode, response.getMessageCode());
    assertEquals("Incorrect Error Type", -1, response.getErrorType());
  }

  protected void checkSuccessEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
  }
  
  protected void checkFailEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
  }
  
  protected void assertOnNormalExit() throws java.lang.Exception
  {
    assertNotNull("Null Response", _response);
    assertTrue("Event not Successful", _response.isEventSuccessful());
    assertEquals("Incorrect Message Code", IErrorCode.NO_ERROR, _response.getMessageCode());

  }

  protected void assertOnFailedExit() throws java.lang.Exception
  {
    assertNotNull("Null Response", _response);
    assertTrue("Incorrect Event Status", !_response.isEventSuccessful());
    assertEquals("Incorrect Message Code", getDefaultMsgCode(), _response.getMessageCode());
    assertNotNull("Null Error reason", _response.getErrorReason());
    assertEquals("Incorrect Error Type", getDefaultErrorType(), _response.getErrorType());
  }
  
  public void testPerform() throws Exception
  {
    try
    {
      log("testPerform", "Enter");
      prepareTestData();

      unitTest();

      cleanTestData();
    }
    finally
    {
      log("testPerform", "Exit");
    }

  }
  
  protected void prepareTestData() throws Exception
  {
  }
  
  protected void unitTest() throws Exception
  {
  }
  
  protected void cleanTestData() throws Exception
  {
  }
  

  // ****************** Session Setup *********************  
  protected ISessionManagerObj getSessionMgr() throws Exception
  {
    ISessionManagerHome sessionHome = (ISessionManagerHome)ServiceLocator.instance(
                                         ServiceLocator.CLIENT_CONTEXT).getHome(
                                         ISessionManagerHome.class.getName(),
                                         ISessionManagerHome.class);
    return sessionHome.create();
  }

  protected String openSession() throws Exception
  {
    String session = _sessionMgr.openSession();
    _openedSessions.add(session);
    _sessionMgr.authSession(session, ADMIN_USERID);
    return session;
  }

  protected void closeSession(String sessionId) throws Exception
  {
    _sessionMgr.closeSession(sessionId);
    _openedSessions.remove(sessionId);
  }

  protected void closeAllSessions() throws Exception
  {
    String[] sessions = (String[])_openedSessions.toArray(new String[0]);
    for (int i=0; i<sessions.length; i++)
      closeSession(sessions[i]);
  }

  protected void createSessions(int numSessions) throws Exception
  {
    _sessions = new String[numSessions];
    for (int i=0; i<numSessions; i++)
      _sessions[i] = openSession();
  }

  protected void createStateMachines(int numSM)
  {
    _stateMs = new StateMachine[numSM];
    for (int i=0; i<numSM; i++)
      _stateMs[i] = new StateMachine(null, null);
  }


  // ****************** Logging utilities ********************
  
  protected void log(String method, String message)
  {
    String logmsg = MessageFormat.format(LOG_PATTERN,
                      new Object[] {
                        getClassName(),
                        method,
                        message});
    Log.log("TEST", logmsg);                    
  }

  protected void err(String method, String message, Throwable t)
  {
    String logmsg = MessageFormat.format(LOG_PATTERN,
                      new Object[] {
                        getClassName(),
                        method,
                        message});

    Log.err("TEST", logmsg, t);    
  }
   
  
}