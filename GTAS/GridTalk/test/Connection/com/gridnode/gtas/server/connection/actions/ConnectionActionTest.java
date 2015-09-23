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
 * Nov 16 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.actions;

import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.events.connection.DisconnectEvent;
import com.gridnode.gtas.events.connection.ConnectEvent;
import com.gridnode.gtas.server.connection.helpers.Logger;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.connection.helpers.ActionTestHelper;
import com.gridnode.gtas.exceptions.IErrorCode;
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
import java.util.Collection;
import java.sql.Timestamp;


/**
 * This test cases tests the Connection action classes:<p>
 * <pre>
 * ConnectAction
 * DisconnectAction
 * </pre>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ConnectionActionTest extends ActionTestHelper
{
  ConnectEvent[] _conEvents;
  DisconnectEvent[] _disEvents;
  private String _connGmNodeID;
  private Object _lock = new Object();

  public ConnectionActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ConnectionActionTest.class);
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
    Logger.debug("[ConnectionActionTest.prepareTestData] Enter");

    _conEvents = new ConnectEvent[]
    {
      createConnectEvent(DUMMY_STRING), //wrong password
      createConnectEvent(PASSWORD),     //correct password
    };

    _disEvents = new DisconnectEvent[]
    {
      new DisconnectEvent(),
    };

    createSessions(1);
    createStateMachines(1);
    Logger.debug("[ConnectionActionTest.prepareTestData] Exit");
  }

  protected void unitTest() throws Exception
  {
    Logger.debug("[ConnectionActionTest.unitTest] Enter");

    try
    {
      // pass: disconnect without connect, silently returns
      //disconnectCheckPass(_disEvents[0], _sessions[0], _sm[0]);

      // fail: connect with wrong password
      //connectCheckFail(_conEvents[0], _sessions[0], _sm[0], false,
      //  IErrorCode.INVALID_SEC_PWD_ERROR);

      // pass: connect with correct password
      connectCheckPass(_conEvents[1], _sessions[0], _sm[0]);

      // pass: connect after connected, silently returns
      //connectCheckPass(_conEvents[1], _sessions[0], _sm[0]);

      // pass: disconnect after connect
      disconnectCheckPass(_disEvents[0], _sessions[0], _sm[0]);
    }
    finally
    {
      Logger.debug("[ConnectionActionTest.unitTest] Exit");
    }
  }

  protected IEJBAction createNewAction()
  {
    return new ConnectAction();
  }

  protected synchronized void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    ConnectEvent conEvent = (ConnectEvent)event;

    try
    {
      wait(60000L);

      assertTrue("Gridmaster Post office not opened",
        _postOffice.isGridMasterPostOfficeOpened());

      //check my connection status online
      ConnectionStatus myConnStatus = findConnectionStatusByNodeID(GRIDNODE_ID.toString());
      _connGmNodeID = getConnectedGmNodeID();
      assertNotNull("Connected Gm NodeID not set", _connGmNodeID);
      checkConnectionStatus(myConnStatus, true, _connGmNodeID);

      //check gm connection status online
      ConnectionStatus gmConnStatus = findConnectionStatusByNodeID(_connGmNodeID);
      checkConnectionStatus(gmConnStatus, true, null);
    }
    catch (Throwable t)
    {
      Log.err("TEST", "ConnectionActionTest.checkActionEffect]", t);
      assertTrue("checkActionEffect fail", false);
    }
  }

  // ************* Utility methods ***************************

  private ConnectEvent createConnectEvent(
    String password)
    throws Exception
  {
    return new ConnectEvent(password);
  }

  private void connectCheckFail(
    ConnectEvent event, String session, StateMachine sm,
    boolean eventEx, short errorCode)
  {
    checkFail(event, session, sm, eventEx, errorCode);
  }

  private void connectCheckPass(
    ConnectEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private synchronized void disconnectCheckPass(
    DisconnectEvent event, String session, StateMachine sm)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(new DisconnectAction(), event, session, sm);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[SConnectionActionTest.getCheckPass] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponsePass(response, IErrorCode.NO_ERROR);

    try
    {
      wait(60000L);

      //check my connection status offline
      ConnectionStatus myConnStatus = findConnectionStatusByNodeID(GRIDNODE_ID.toString());
      checkConnectionStatus(myConnStatus, false, null);

      //check gm connection status offline
      if (_connGmNodeID != null)
      {
        ConnectionStatus gmConnStatus = findConnectionStatusByNodeID(_connGmNodeID);
        checkConnectionStatus(gmConnStatus, false, null);
      }

      assertTrue("Gridmaster Post office still opened",
        !_postOffice.isGridMasterPostOfficeOpened());

    }
    catch (Exception e)
    {
      Log.debug("TEST", e.getMessage());
    }
  }


  private void checkConnectionStatus(ConnectionStatus status, boolean online,
    String connectedServerNode)
  {
    if (online)
      assertEquals("Status should be online", ConnectionStatus.STATUS_ONLINE, status.getStatusFlag());
    else
      assertEquals("Status should be offline", ConnectionStatus.STATUS_OFFLINE, status.getStatusFlag());

    assertEquals("Connected Server node incorrect", connectedServerNode, status.getConnectedServerNode());
  }
}