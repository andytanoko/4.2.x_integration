/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionSetupActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.actions;

import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.connection.model.JmsRouter;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import java.util.Collection;
import com.gridnode.gtas.events.connection.GetConnectionSetupResultEvent;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.connection.model.ConnectionSetupResult;
import com.gridnode.gtas.server.connection.model.ConnectionSetupParam;
import com.gridnode.gtas.server.connection.helpers.ActionTestHelper;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.connection.SetupConnectionEvent;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;

import javax.ejb.Handle;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.sql.Timestamp;


/**
 * This test cases tests the Connection Setup action classes:<p>
 * <pre>
 * SetupConnectionAction
 * GetConnectionSetupResultAction
 * </pre>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectionSetupActionTest extends ActionTestHelper
{
  SetupConnectionEvent[] _events;
  GetConnectionSetupResultEvent _getEvent;
  ConnectionSetupResult _defResult;
  ConnectionSetupResult _setupResult;

  public ConnectionSetupActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ConnectionSetupActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ******************* ActionTestHelper methods **********************

  protected void cleanUp()
  {
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    Logger.debug("[ConnectionSetupActionTest.prepareTestData] Enter");
    _getEvent = new GetConnectionSetupResultEvent();
    _events = new SetupConnectionEvent[]
    {
      createTestEvent(COUNTRY_CODE, DUMMY_NETWORK_ROUTER), //wrong ip
      createTestEvent(COUNTRY_CODE, TEST_NETWORK_ROUTER), //Test router network
    };

    _defResult = new ConnectionSetupResult();
    ConnectionSetupParam param = new ConnectionSetupParam();
    param.setCurrentLocation(COUNTRY_CODE);
    param.setServicingRouter(TEST_NETWORK_ROUTER);
    param.setOriginalLocation(COUNTRY_CODE);
    param.setOriginalServicingRouter(TEST_NETWORK_ROUTER);
    _defResult.setSetupParams(param);

    createSessions(1);
    createStateMachines(1);
    Logger.debug("[ConnectionSetupActionTest.prepareTestData] Exit");
  }

  protected void unitTest() throws Exception
  {
    Logger.debug("[ConnectionSetupActionTest.unitTest] Enter");

    try
    {
      //perform Registration
      //registerProduct(_sessions[0], _sm[0]);

      //pass: connection setup result with def setup param
      //getCheckPass(_getEvent, _sessions[0], _sm[0], _defResult);

      //fail: wrong router ip
      //setupCheckFail(_events[0], _sessions[0], _sm[0], false);

      //pass: Master channel,cert,gm node, jmsrouters created.
      setupCheckPass(_events[1], _sessions[0], _sm[0]);

      //pass: getConnectionSetupResult matches previous setup return result.
      getCheckPass(_getEvent, _sessions[0], _sm[0], _setupResult);
    }
    finally
    {
      Logger.debug("[ConnectionSetupActionTest.unitTest] Exit");
    }
  }

  protected IEJBAction createNewAction()
  {
    return new SetupConnectionAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    SetupConnectionEvent setupEvent = (SetupConnectionEvent)event;

    try
    {
      Map resultMap = (Map)response.getReturnData();
      assertNotNull("Returned result is null", resultMap);
      assertEquals("Setup status incorrect", new Short(ConnectionSetupResult.STATUS_SUCCESS), resultMap.get(ConnectionSetupResult.STATUS));

      Map paramMap = (Map)resultMap.get(ConnectionSetupResult.SETUP_PARAMETERS);
      assertNotNull("Setup param map is null", paramMap);
      assertEquals("Current location is incorrect", setupEvent.getCurrentLocation(), paramMap.get(ConnectionSetupParam.CURRENT_LOCATION));
      assertEquals("Servicing router is incorrect", setupEvent.getServicingRouter(), paramMap.get(ConnectionSetupParam.SERVICING_ROUTER));

      _setupResult = getConnectionSetupResult();
      checkConnectionSetupResult((Map)response.getReturnData(), getConnectionSetupResult());
    }
    catch (Throwable t)
    {
      Log.err("TEST", "SetupConnectionActionTest.checkActionEffect]", t);
      assertTrue("checkActionEffect fail", false);
    }
  }

  // ************* Utility methods ***************************

  private SetupConnectionEvent createTestEvent(
    String country, String routerIp)
    throws Exception
  {
    return new SetupConnectionEvent(country, routerIp, PASSWORD);
  }

  private void setupCheckFail(
    SetupConnectionEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.CONNECTION_SETUP_ERROR);
//    BasicEventResponse response = null;
//    try
//    {
//      response = performEvent(createNewAction(), event, session, sm);
//    }
//    catch (Exception ex)
//    {
//      Log.err("TEST", "[SetupConnectionActionTest.setupCheckFail] Error Exit", ex);
//      assertTrue("Event Exception", false);
//    }
//
//    assertResponsePass(response, IErrorCode.NO_ERROR);
//
//    Map resultMap = (Map)response.getReturnData();
//    assertNotNull("Returned result is null", resultMap);
//    assertEquals("Setup status incorrect", new Short(ConnectionSetupResult.STATUS_FAILURE), resultMap.get(ConnectionSetupResult.STATUS));
  }

  private void setupCheckPass(
    SetupConnectionEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private void getCheckPass(
    GetConnectionSetupResultEvent event, String session, StateMachine sm,
    ConnectionSetupResult expected)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(new GetConnectionSetupResultAction(), event, session, sm);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[SetupConnectionActionTest.getCheckPass] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponsePass(response, IErrorCode.NO_ERROR);

    checkConnectionSetupResult((Map)response.getReturnData(), expected);
  }

  private ConnectionSetupResult getConnectionSetupResult() throws Exception
  {
    return ServiceLookupHelper.getConnectionService().getConnectionSetupResult();
  }

  private void checkConnectionSetupResult(Map resultMap, ConnectionSetupResult expected)
  {
    assertEquals("Failure reason is incorrect", expected.getFailureReason(), resultMap.get(ConnectionSetupResult.FAILURE_REASON));
    assertEquals("Status is incorrect", expected.getStatus(), resultMap.get(ConnectionSetupResult.STATUS));
    checkUIds("Available GridMasters",
      (Collection)expected.getAvailableGridMastersUIDs(),
      (Collection)resultMap.get(ConnectionSetupResult.AVAILABLE_GRIDMASTERS),
      GridNode.UID);
    checkUIds("Available Routers",
      (Collection)expected.getAvailableRouterUIDs(),
      (Collection)resultMap.get(ConnectionSetupResult.AVAILABLE_ROUTERS),
      JmsRouter.UID);
    checkSetupParam(expected.getSetupParams(), (Map)resultMap.get(ConnectionSetupResult.SETUP_PARAMETERS));
  }

  private void checkUIds(String type, Collection uids, Collection entities, Number keyId)
  {
    Object[] uidArray = uids.toArray();
    Object[] entityArray = entities.toArray();

    assertEquals(type+" count is incorrect", uidArray.length, entityArray.length);

    for (int i=0; i<uidArray.length; i++)
    {
      Map entity = (Map)entityArray[i];
      assertEquals(type+" UID is incorrect", uidArray[i], entity.get(keyId));
    }
  }

  private void checkSetupParam(ConnectionSetupParam expected, Map paramMap)
  {
    assertEquals("Current location is incorrect", expected.getCurrentLocation() , paramMap.get(ConnectionSetupParam.CURRENT_LOCATION));
    assertEquals("Original location is incorrect", expected.getOriginalLocation() , paramMap.get(ConnectionSetupParam.ORIGINAL_LOCATION));
    assertEquals("Original servicing router is incorrect", expected.getOriginalServicingRouter() , paramMap.get(ConnectionSetupParam.ORIGINAL_SERVICING_ROUTER));
    assertEquals("Servicing router is incorrect", expected.getServicingRouter() , paramMap.get(ConnectionSetupParam.SERVICING_ROUTER));
  }
}