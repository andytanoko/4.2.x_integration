/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRegistryConnectInfoListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 * Sep 19 2003    Neo Sok Lay         GetRegistryConnectInfoListAction now returns
 *                                    a EntityListResponseData to invoker.
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.events.bizreg.GetRegistryConnectInfoListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.bizreg.IRegistryConnectInfo;
import com.gridnode.gtas.server.bizreg.helpers.ActionTestHelper;
import com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Collection;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * This test cases tests the NetworkSetting Action classes:<p>
 * <li>GetRegistryConnectInfoListAction</li>
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class GetRegistryConnectInfoListActionTest extends ActionTestHelper
{
  GetRegistryConnectInfoListEvent[] _events;

  public GetRegistryConnectInfoListActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetRegistryConnectInfoListActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ******************* ActionTestHelper methods **********************

  protected void cleanUp()
  {
    cleanupConnInfos();
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    _events = new GetRegistryConnectInfoListEvent[]
    {
      //retrieve existing
      createTestEvent(),
      //after modification & saved
      createTestEvent(),
    };

    createSessions(1);
    createStateMachines(1);
  }

  protected void unitTest() throws Exception
  {
    // accepted: get existing
    getCheckPass(_events[0], _sessions[0], _sm[0]);

    modifyRegConnInfo();
    
    // accepted: get saved
    getCheckPass(_events[1], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new GetRegistryConnectInfoListAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    Object retData = response.getReturnData();
    assertNotNull("ReturnData is null", retData);
    assertTrue("ReturnData not instanceof Collection", retData instanceof EntityListResponseData);
    
    try
    {
      checkRegConnList((EntityListResponseData)retData, getRegConnInfoList());
    }
    catch (Throwable t)
    {
      Log.err("TEST", "GetRegistryConnectInfoListActionTest.checkActionEffect]", t);
      assertTrue("checkActionEffect fail", false);
    }
  }

  // ************* Utility methods ***************************

  private GetRegistryConnectInfoListEvent createTestEvent()
    throws Exception
  {
    return new GetRegistryConnectInfoListEvent();
  }

  private void getCheckPass(
    GetRegistryConnectInfoListEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private Collection getRegConnInfoList() throws Throwable
  {
    return _bizRegMgr.findRegistryConnectInfos(null);
  }

  private void saveRegConnInfo(RegistryConnectInfo save) throws Throwable
  {
    _bizRegMgr.createRegistryConnectInfo(save);
  }

  private void modifyRegConnInfo()
  {
    try
    {
      RegistryConnectInfo info = createConnectInfo(
                                "TestConn2", "sample/url/publish", "sample/url/query",
                                "testuser", "testpassword");
                                
    }
    catch (Throwable e)
    {
      Log.err("TEST", "Error saving RegistryConnInfo", e);
      assertTrue("Error saving RegistryConnInfo", false);
    }
  }

  private RegistryConnectInfo createConnectInfo(String name, String publishUrl, String queryUrl, String publishUser, String publishPassword)
  {
    RegistryConnectInfo newInfo = new RegistryConnectInfo();
    newInfo.setName(name);
    newInfo.setPublishPassword(publishPassword);
    newInfo.setPublishUrl(publishUrl);
    newInfo.setPublishUser(publishUser);
    newInfo.setQueryUrl(queryUrl);
    return newInfo;
  }
  
  private void checkRegConnList(EntityListResponseData responseData, Collection expected)
  {
    Object[] infos = responseData.getEntityList().toArray();
    RegistryConnectInfo[] expectedInfos = (RegistryConnectInfo[])expected.toArray(new RegistryConnectInfo[0]);    

    assertEquals("Return infoList size is incorrect", expectedInfos.length, infos.length);
    for (int i=0; i<expectedInfos.length; i++)
    {
      System.out.println("Return info="+infos[i]);
      assertTrue("Return info not instanceof Map", infos[i] instanceof Map);
      checkRegConnInfo((Map)infos[i], expectedInfos[i]);
    }
  }
  
  private void checkRegConnInfo(Map infoMap, RegistryConnectInfo expected)
  {
    assertEquals("Name incorrect", expected.getName(), infoMap.get(IRegistryConnectInfo.NAME));
    assertEquals("Publish Url incorrect", expected.getPublishUrl(), infoMap.get(IRegistryConnectInfo.PUBLISH_URL));
    assertEquals("Publish Password incorrect", expected.getPublishPassword(), infoMap.get(IRegistryConnectInfo.PUBLISH_PASSWORD));
    assertEquals("Publish User incorrect", expected.getPublishUser(), infoMap.get(IRegistryConnectInfo.PUBLISH_USER));
    assertEquals("Query Url incorrect", expected.getQueryUrl(), infoMap.get(IRegistryConnectInfo.QUERY_URL));
  }
  
  private void cleanupConnInfos()
  {
    try
    {
      RegistryConnectInfo info = _bizRegMgr.findRegistryConnectInfoByName("TestConn2");
      if (info != null)
      {
        _bizRegMgr.deleteRegistryConnectInfo((Long)info.getKey());  
      }
    }
    catch (Throwable t)
    {
    }
    
  }
}