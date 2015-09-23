/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NetworkSettingActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.actions;

import java.util.HashMap;
import com.gridnode.gtas.server.connection.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.connection.model.NetworkSetting;
import com.gridnode.gtas.server.connection.helpers.ActionTestHelper;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.connection.GetNetworkSettingEvent;
import com.gridnode.gtas.events.connection.SaveNetworkSettingEvent;
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
import java.sql.Timestamp;


/**
 * This test cases tests the NetworkSetting Action classes:<p>
 * <li>GetNetworkSettingAction</li>
 * <li>SaveNetworkSettingAction</li>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class NetworkSettingActionTest extends ActionTestHelper
{
  SaveNetworkSettingEvent[] _events;
  GetNetworkSettingEvent _getEvent;

  public NetworkSettingActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(NetworkSettingActionTest.class);
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
    _getEvent = new GetNetworkSettingEvent();
    _events = new SaveNetworkSettingEvent[]
    {
      // 0. rejected: save - Missing Connection Level
      createTestEvent(null, LOCAL_JMS_ROUTER, HTTP_PROXY_SERVER,
        HTTP_PROXY_PORT, PROXY_AUTH_USER, PROXY_AUTH_PWD, KEEP_ALIVE_INTERVAL,
        RESPONSE_TIMEOUT),

      // 1. rejected: save No_firewall - Missing Local Jms Router
      createTestEvent(new Short(NetworkSetting.LEVEL_NO_FIREWALL),
        null, HTTP_PROXY_SERVER, HTTP_PROXY_PORT, PROXY_AUTH_USER,
        PROXY_AUTH_PWD, KEEP_ALIVE_INTERVAL, RESPONSE_TIMEOUT),

      // 2. rejected: save Proxy_no_auth - Missing Http Proxy Port
      createTestEvent(new Short(NetworkSetting.LEVEL_PROXY_NO_AUTH),
        LOCAL_JMS_ROUTER, null, HTTP_PROXY_PORT, PROXY_AUTH_USER,
        PROXY_AUTH_PWD, KEEP_ALIVE_INTERVAL, RESPONSE_TIMEOUT),

      // 3. rejected: save Proxy_no_auth - Missing Http Proxy Server
      createTestEvent(new Short(NetworkSetting.LEVEL_PROXY_NO_AUTH),
        LOCAL_JMS_ROUTER, HTTP_PROXY_SERVER, null, PROXY_AUTH_USER,
        PROXY_AUTH_PWD, KEEP_ALIVE_INTERVAL, RESPONSE_TIMEOUT),

      // 4. rejected: save Proxy_with_auth - Missing Http Proxy Port
      createTestEvent(new Short(NetworkSetting.LEVEL_PROXY_WITH_AUTH),
        LOCAL_JMS_ROUTER, null, HTTP_PROXY_PORT, PROXY_AUTH_USER,
        PROXY_AUTH_PWD, KEEP_ALIVE_INTERVAL, RESPONSE_TIMEOUT),

      // 5. rejected: save Proxy_with_auth - Missing Http Proxy Server
      createTestEvent(new Short(NetworkSetting.LEVEL_PROXY_WITH_AUTH),
        LOCAL_JMS_ROUTER, HTTP_PROXY_SERVER, null, PROXY_AUTH_USER,
        PROXY_AUTH_PWD, KEEP_ALIVE_INTERVAL, RESPONSE_TIMEOUT),

      // 6. rejected: save Proxy_with_auth - Missing Proxy Auth User
      createTestEvent(new Short(NetworkSetting.LEVEL_PROXY_WITH_AUTH),
        LOCAL_JMS_ROUTER, HTTP_PROXY_SERVER, HTTP_PROXY_PORT, null,
        PROXY_AUTH_PWD, KEEP_ALIVE_INTERVAL, RESPONSE_TIMEOUT),

      // 7. rejected: save Proxy_with_auth - Missing Proxy Auth Password
      createTestEvent(new Short(NetworkSetting.LEVEL_PROXY_WITH_AUTH),
        LOCAL_JMS_ROUTER, HTTP_PROXY_SERVER, HTTP_PROXY_PORT, PROXY_AUTH_USER,
        null, KEEP_ALIVE_INTERVAL, RESPONSE_TIMEOUT),

      // 8. rejected: save - Missing Keep Alive Interval
      createTestEvent(new Short(NetworkSetting.LEVEL_FIREWALL_NO_PROXY),
        LOCAL_JMS_ROUTER, HTTP_PROXY_SERVER, HTTP_PROXY_PORT, PROXY_AUTH_USER,
        PROXY_AUTH_PWD, null, RESPONSE_TIMEOUT),

      // 9. rejected: save - Missing Response Timeout
      createTestEvent(new Short(NetworkSetting.LEVEL_FIREWALL_NO_PROXY),
        LOCAL_JMS_ROUTER, HTTP_PROXY_SERVER, HTTP_PROXY_PORT, PROXY_AUTH_USER,
        PROXY_AUTH_PWD, KEEP_ALIVE_INTERVAL, null),

      // 10. accepted: save - No_firewall
      createTestEvent(new Short(NetworkSetting.LEVEL_NO_FIREWALL),
        LOCAL_JMS_ROUTER, null, null, null, null, KEEP_ALIVE_INTERVAL,
        RESPONSE_TIMEOUT),

      // 11. accepted: save - Firewall_no_proxy
      createTestEvent(new Short(NetworkSetting.LEVEL_FIREWALL_NO_PROXY),
        null, null, null, null, null, KEEP_ALIVE_INTERVAL, RESPONSE_TIMEOUT),

      // 12. accepted: save - Proxy_no_auth
      createTestEvent(new Short(NetworkSetting.LEVEL_PROXY_NO_AUTH),
        null, HTTP_PROXY_SERVER, HTTP_PROXY_PORT, null, null, KEEP_ALIVE_INTERVAL,
        RESPONSE_TIMEOUT),

     // 13. accepted: save - Proxy_with_auth
     createTestEvent(new Short(NetworkSetting.LEVEL_PROXY_WITH_AUTH),
        null, HTTP_PROXY_SERVER, HTTP_PROXY_PORT, PROXY_AUTH_USER,
        PROXY_AUTH_PWD, KEEP_ALIVE_INTERVAL, RESPONSE_TIMEOUT),

    };

    createSessions(1);
    createStateMachines(1);
  }

  protected void unitTest() throws Exception
  {
    // accepted: get without file
    getCheckPass(_getEvent, _sessions[0], _sm[0], new NetworkSetting());

    for (int i=0; i<10; i++)
      saveCheckFail(_events[i], _sessions[0], _sm[0], false);

    for (int i=10; i<_events.length; i++)
      saveCheckPass(_events[i], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {

    return new SaveNetworkSettingAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    SaveNetworkSettingEvent saveEvent = (SaveNetworkSettingEvent)event;

    try
    {
      checkNetworkSetting(saveEvent.getNetworkSetting(), getNetworkSetting());
    }
    catch (Throwable t)
    {
      Log.err("TEST", "NetworkSettingActionTest.checkActionEffect]", t);
      assertTrue("checkActionEffect fail", false);
    }
  }

  // ************* Utility methods ***************************

  private SaveNetworkSettingEvent createTestEvent(
    Short connectionLevel, String localJmsRouter, String httpProxyServer,
    Integer httpProxyPort, String proxyAuthUser, String proxyAuthPassword,
    Integer keepAliveInterval, Integer responseTimeout )
    throws Exception
  {
    Map map = new HashMap();

    map.put(NetworkSetting.CONNECTION_LEVEL, connectionLevel);
    map.put(NetworkSetting.LOCAL_JMS_ROUTER, localJmsRouter);
    map.put(NetworkSetting.HTTP_PROXY_SERVER, httpProxyServer);
    map.put(NetworkSetting.HTTP_PROXY_PORT, httpProxyPort);
    map.put(NetworkSetting.PROXY_AUTH_USER, proxyAuthUser);
    map.put(NetworkSetting.PROXY_AUTH_PASSWORD, proxyAuthPassword);
    map.put(NetworkSetting.KEEP_ALIVE_INTERVAL, keepAliveInterval);
    map.put(NetworkSetting.RESPONSE_TIMEOUT, responseTimeout);

    return new SaveNetworkSettingEvent(map);
  }

  private void saveCheckFail(
    SaveNetworkSettingEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.NETWORK_SETTING_ERROR);
  }

  private void saveCheckPass(
    SaveNetworkSettingEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private void getCheckPass(
    GetNetworkSettingEvent event, String session, StateMachine sm,
    NetworkSetting expected)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(new GetNetworkSettingAction(), event, session, sm);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[NetworkSettingActionTest.getCheckPass] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponsePass(response, IErrorCode.NO_ERROR);

    checkNetworkSetting((Map)response.getReturnData(), expected);
  }

  private NetworkSetting getNetworkSetting() throws Exception
  {
    return ServiceLookupHelper.getConnectionService().getNetworkSetting();
  }

  private void checkNetworkSetting(Map settingMap, NetworkSetting expected)
  {
    assertEquals("ConnectionLevel is incorrect", expected.getConnectionLevel(), settingMap.get(NetworkSetting.CONNECTION_LEVEL));
    assertEquals("HttpProxyPort is incorrect", expected.getHttpProxyPort(), settingMap.get(NetworkSetting.HTTP_PROXY_PORT));
    assertEquals("HttpProxyServer is incorrect", expected.getHttpProxyServer(), settingMap.get(NetworkSetting.HTTP_PROXY_SERVER));
    assertEquals("KeepAliveInterval is incorrect", expected.getKeepAliveInterval(), settingMap.get(NetworkSetting.KEEP_ALIVE_INTERVAL));
    assertEquals("LocalJmsRouter is incorrect", expected.getLocalJmsRouter(), settingMap.get(NetworkSetting.LOCAL_JMS_ROUTER));
    assertEquals("ProxyAuthPassword is incorrect", expected.getProxyAuthPassword(), settingMap.get(NetworkSetting.PROXY_AUTH_PASSWORD));
    assertEquals("ProxyAuthUser is incorrect", expected.getProxyAuthUser(), settingMap.get(NetworkSetting.PROXY_AUTH_USER));
    assertEquals("ResponseTimeout is incorrect", expected.getResponseTimeout(), settingMap.get(NetworkSetting.RESPONSE_TIMEOUT));
  }
}