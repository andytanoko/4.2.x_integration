/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetBusinessEntityActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.model.bizreg.IBusinessEntity;
import com.gridnode.gtas.model.bizreg.IWhitePage;
import com.gridnode.gtas.events.bizreg.GetBusinessEntityEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.bizreg.helpers.ActionTestHelper;

import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.WhitePage;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.PasswordMask;

import junit.framework.*;

import java.util.Map;
import java.util.Iterator;

/**
 * This Test case tests the GetBusinessEntityAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class GetBusinessEntityActionTest extends ActionTestHelper
{
  GetBusinessEntityEvent[] _events;

  public GetBusinessEntityActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetBusinessEntityActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *********************

  protected void cleanUp()
  {
    cleanUpBEs(null);
    cleanUpBEs(ENTERPRISE);
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createBes(1, 0);
    createSessions(1);
    createStateMachines(1);

    _events = new GetBusinessEntityEvent[]
              {
                //rejected
                createTestEvent(DUMMY_UID),

                //accepted
                createTestEvent(_uIDs[0]),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** REJECTED ***************************
    //non existing business entity
    getCheckFail(_events[0], _sessions[0], _sm[0], false);

    // ************** ACCEPTED *****************************
    getCheckSuccess(_events[1], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new GetBusinessEntityAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    Object returnData = response.getReturnData();
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof Map);

    Map beMap = (Map)returnData;

    Object wpObj = beMap.get(IBusinessEntity.WHITE_PAGE);
    assertNotNull("whitepage is null", wpObj);
    assertTrue("whitepage data type incorrect", wpObj instanceof Map);

    Map wpMap = (Map)wpObj;

    for (Iterator i=wpMap.keySet().iterator(); i.hasNext(); )
    {
      Object key = i.next();
      Log.debug("TEST", "Key="+key + ",value="+wpMap.get(key));
    }

    GetBusinessEntityEvent getEvent = (GetBusinessEntityEvent)event;
    BusinessEntity be = findBizEntityByUId(getEvent.getBeUID());

    checkBe(be, beMap);
    checkWp(be.getWhitePage(), wpMap, true);
  }


  // ********************* Utiltity methods *************************
  private GetBusinessEntityEvent createTestEvent(Long uID) throws Exception
  {
    return new GetBusinessEntityEvent(uID);
  }

  private void getCheckFail(
    GetBusinessEntityEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_BY_KEY_ERROR);
  }

  private void getCheckSuccess(
    GetBusinessEntityEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}