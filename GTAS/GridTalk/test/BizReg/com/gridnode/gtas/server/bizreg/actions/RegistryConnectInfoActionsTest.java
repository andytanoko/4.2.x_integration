/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryConnectInfoActionsTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 22 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.events.bizreg.CreateRegistryConnectInfoEvent;
import com.gridnode.gtas.events.bizreg.DeleteRegistryConnectInfoEvent;
import com.gridnode.gtas.events.bizreg.GetRegistryConnectInfoEvent;
import com.gridnode.gtas.events.bizreg.UpdateRegistryConnectInfoEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.bizreg.IRegistryConnectInfo;
import com.gridnode.gtas.server.bizreg.helpers.ActionTestHelper;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EntityActionResponseData;
import com.gridnode.pdip.framework.rpf.event.EntityListActionResponseData;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * This test cases tests the RegistryConnectInfo Action classes:<p>
 * <UL>
 *   <li>CreateRegistryConnectInfoAction</li>
 *   <li>UpdateRegistryConnectInfoAction</li>
 *   <li>DeleteRegistryConnectInfoAction</li>
 *   <li>GetRegistryConnectInfoAction</li>
 * </UL>
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class RegistryConnectInfoActionsTest extends ActionTestHelper
{
  CreateRegistryConnectInfoEvent _createEvent;
  UpdateRegistryConnectInfoEvent _updateEvent;
  GetRegistryConnectInfoEvent _getEvent;
  DeleteRegistryConnectInfoEvent _delEvent;

  Long[] _uids;
  
  public RegistryConnectInfoActionsTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(RegistryConnectInfoActionsTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ******************* ActionTestHelper methods **********************

  protected void cleanUp()
  {
    cleanUpConnInfos();
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createRegistryConnectInfos(1);
       
    _createEvent = new CreateRegistryConnectInfoEvent(
                      "RegistryConnectInfoActionsTest", 
                      "sample/url/publish1", 
                      "sample/url/query1",
                      "testuser", 
                      "testpassword");
    _updateEvent = new UpdateRegistryConnectInfoEvent(
                      _uids[0], 
                      "sample/url/publish2", 
                      "sample/url/query2",
                      "testuser", 
                      "testpassword");

    _getEvent = new GetRegistryConnectInfoEvent(_uids[0]);
    _delEvent = new DeleteRegistryConnectInfoEvent(Arrays.asList(_uids));
    
    createSessions(1);
    createStateMachines(1);
  }

  protected void unitTest() throws Exception
  {
    // create 1
    createCheckPass(_createEvent, _sessions[0], _sm[0]);
    
    // modify
    updateCheckPass(_updateEvent, _sessions[0], _sm[0]);

    // delete
    deleteCheckPass(_delEvent, _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return null;
  }

  protected void checkCreateActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    CreateRegistryConnectInfoEvent createEvent = (CreateRegistryConnectInfoEvent)event;
    
    Object retData = response.getReturnData();
    assertNotNull("ReturnData is null", retData);
    assertTrue("ReturnData not Map", retData instanceof Map);
    
    try
    {
      Map retMap = (Map)retData;
      checkRegConnInfo(retMap, createEvent);
      checkRegConnInfo(retMap, getRegistryConnectInfo((Long)retMap.get(IRegistryConnectInfo.UID), sm));
    }
    catch (Throwable t)
    {
      Log.err("TEST", "RegistryConnectInfoActionsTest.checkCreateActionEffect]", t);
      assertTrue("checkCreateActionEffect fail", false);
    }
  }

  protected void checkUpdateActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    UpdateRegistryConnectInfoEvent updEvent = (UpdateRegistryConnectInfoEvent)event;
    
    Object retData = response.getReturnData();
    assertNotNull("ReturnData is null", retData);
    assertTrue("ReturnData not Map", retData instanceof Map);
    
    try
    {
      checkRegConnInfo((Map)retData, updEvent);
      checkRegConnInfo((Map)retData, getRegistryConnectInfo(updEvent.getUid(), sm));
    }
    catch (Throwable t)
    {
      Log.err("TEST", "RegistryConnectInfoActionsTest.checkSaveActionEffect]", t);
      assertTrue("checkSaveActionEffect fail", false);
    }
  }

  protected void checkDeleteActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    DeleteRegistryConnectInfoEvent delEvent = (DeleteRegistryConnectInfoEvent)event;
    Long delUid = (Long)delEvent.getKeys().toArray()[0];
    
    Object retData = response.getReturnData();
    assertNotNull("ReturnData is null", retData);
    assertTrue("ReturnData not EntityListActionResponseData", retData instanceof EntityListActionResponseData);
    
    EntityListActionResponseData listResponseData = (EntityListActionResponseData)retData;
    assertTrue("Delete is not successful", listResponseData.isAllSuccess());
    EntityActionResponseData[] responseData = listResponseData.getResponseDataList();
    
    assertEquals("Deleted Name is incorrect", delUid, responseData[0].getKey());
    
    try
    {
      _bizRegMgr.findRegistryConnectInfo(delUid);
    }
    catch (FindEntityException ex)
    {
    }
    catch (Throwable t)
    {
      Log.err("TEST", "RegistryConnectInfoActionsTest.checkDeleteActionEffect]", t);
      assertTrue("checkDeleteActionEffect fail", false);
    }
  }

  // ************* Utility methods ***************************

  private void deleteCheckPass(
    DeleteRegistryConnectInfoEvent event, String session, StateMachine sm)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(new DeleteRegistryConnectInfoAction(), event, session, sm);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[RegistryConnectInfoActionsTest.deleteCheckPass] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponsePass(response, IErrorCode.NO_ERROR);

    checkDeleteActionEffect(response, event, sm);
  }

  private void createCheckPass(
    CreateRegistryConnectInfoEvent event, String session, StateMachine sm)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(new CreateRegistryConnectInfoAction(), event, session, sm);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[RegistryConnectInfoActionsTest.createCheckPass] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponsePass(response, IErrorCode.NO_ERROR);

    checkCreateActionEffect(response, event, sm);
  }

  private void updateCheckPass(
    UpdateRegistryConnectInfoEvent event, String session, StateMachine sm)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(new UpdateRegistryConnectInfoAction(), event, session, sm);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[RegistryConnectInfoActionsTest.updateCheckPass] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponsePass(response, IErrorCode.NO_ERROR);

    checkUpdateActionEffect(response, event, sm);
  }

  private Map getRegistryConnectInfo(Long uid, StateMachine sm) throws Throwable
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(new GetRegistryConnectInfoAction(), 
                              new GetRegistryConnectInfoEvent(uid), 
                              (String)sm.getAttribute(IAttributeKeys.SESSION_ID), 
                              sm);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[RegistryConnectInfoActionsTest.getRegistryConnectInfo] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponsePass(response, IErrorCode.NO_ERROR);
    
    return (Map)response.getReturnData();
  }

  private void checkRegConnInfo(Map infoMap, Map expectedMap)
  {
    assertEquals("Name incorrect", expectedMap.get(IRegistryConnectInfo.NAME), infoMap.get(IRegistryConnectInfo.NAME));
    assertEquals("Publish Url incorrect", expectedMap.get(IRegistryConnectInfo.PUBLISH_URL), infoMap.get(IRegistryConnectInfo.PUBLISH_URL));
    assertEquals("Publish Password incorrect", expectedMap.get(IRegistryConnectInfo.PUBLISH_PASSWORD), infoMap.get(IRegistryConnectInfo.PUBLISH_PASSWORD));
    assertEquals("Publish User incorrect", expectedMap.get(IRegistryConnectInfo.PUBLISH_USER), infoMap.get(IRegistryConnectInfo.PUBLISH_USER));
    assertEquals("Query Url incorrect", expectedMap.get(IRegistryConnectInfo.QUERY_URL), infoMap.get(IRegistryConnectInfo.QUERY_URL));
  }
  
  private void checkRegConnInfo(Map infoMap, CreateRegistryConnectInfoEvent event)
  {
    assertEquals("Name incorrect", event.getName(), infoMap.get(IRegistryConnectInfo.NAME));
    assertEquals("Publish Url incorrect", event.getPublishUrl(), infoMap.get(IRegistryConnectInfo.PUBLISH_URL));
    assertEquals("Publish Password incorrect", event.getPassword(), infoMap.get(IRegistryConnectInfo.PUBLISH_PASSWORD));
    assertEquals("Publish User incorrect", event.getUsername(), infoMap.get(IRegistryConnectInfo.PUBLISH_USER));
    assertEquals("Query Url incorrect", event.getQueryUrl(), infoMap.get(IRegistryConnectInfo.QUERY_URL));
  }

  private void checkRegConnInfo(Map infoMap, UpdateRegistryConnectInfoEvent event)
  {
    assertEquals("UID incorrect", event.getUid(), infoMap.get(IRegistryConnectInfo.UID));
    assertEquals("Publish Url incorrect", event.getPublishUrl(), infoMap.get(IRegistryConnectInfo.PUBLISH_URL));
    assertEquals("Publish Password incorrect", event.getPassword(), infoMap.get(IRegistryConnectInfo.PUBLISH_PASSWORD));
    assertEquals("Publish User incorrect", event.getUsername(), infoMap.get(IRegistryConnectInfo.PUBLISH_USER));
    assertEquals("Query Url incorrect", event.getQueryUrl(), infoMap.get(IRegistryConnectInfo.QUERY_URL));
  }
  
  /**
   * @see com.gridnode.gtas.server.bizreg.helpers.ActionTestHelper#checkActionEffect(com.gridnode.pdip.framework.rpf.event.BasicEventResponse, com.gridnode.pdip.framework.rpf.event.IEvent, com.gridnode.pdip.framework.rpf.ejb.StateMachine)
   */
  protected void checkActionEffect(
    BasicEventResponse response,
    IEvent event,
    StateMachine sm)
  {
    // TODO Auto-generated method stub

  }

  private void createRegistryConnectInfos(int num) throws Exception
  {
    _uids = new Long[num];
    for (int i=0; i<num; i++)
    {
      RegistryConnectInfo connInfo = new RegistryConnectInfo();
      connInfo.setName("RegistryConnectInfoActionsTest"+num);
      connInfo.setPublishPassword("testpassword");
      connInfo.setPublishUrl("sample/url/publish");
      connInfo.setPublishUser("testuser");
      connInfo.setQueryUrl("sample/url/query");
      
      _uids[i] = _bizRegMgr.createRegistryConnectInfo(connInfo);   
    }
  }
  
  private void cleanUpConnInfos()
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, RegistryConnectInfo.NAME,
        filter.getLikeOperator(), "RegistryConnectInfoActionsTest", false);
      
      Collection uids = _bizRegMgr.findRegistryConnectInfoKeys(filter);
      for (Iterator i=uids.iterator(); i.hasNext(); )
      {
        _bizRegMgr.deleteRegistryConnectInfo((Long)i.next());
      }
    }
    catch (Throwable e)
    {
    }
  }
}