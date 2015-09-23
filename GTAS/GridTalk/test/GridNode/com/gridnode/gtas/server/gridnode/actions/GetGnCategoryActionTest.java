/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetGnCategoryActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 09 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.actions;

import com.gridnode.gtas.model.gridnode.IGnCategory;
import com.gridnode.gtas.events.gridnode.GetGnCategoryEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.gridnode.helpers.ActionTestHelper;
import com.gridnode.gtas.server.gridnode.model.GnCategory;

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
 * This Test case tests the GetGnCategoryAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetGnCategoryActionTest extends ActionTestHelper
{
  GetGnCategoryEvent[] _events;


  public GetGnCategoryActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetGnCategoryActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *********************

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
    createSessions(1);
    createStateMachines(1);

    _events = new GetGnCategoryEvent[]
              {
                //rejected
                createTestEvent(DUMMY_STRING),

                //accepted
                createTestEvent(CAT_CODE),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** REJECTED ***************************
    //non existing country code
    getCheckFail(_events[0], _sessions[0], _sm[0], true);

    // ************** ACCEPTED *****************************
    getCheckSuccess(_events[1], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new GetGnCategoryAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    Object returnData = response.getReturnData();
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof Map);

    Map categoryMap = (Map)returnData;

    GetGnCategoryEvent getEvent = (GetGnCategoryEvent)event;
    GnCategory code = findGnCategoryByCode(getEvent.getCategoryCode());

    checkGnCategory(code, categoryMap);
  }


  // ********************* Utiltity methods *************************
  private GetGnCategoryEvent createTestEvent(String catCode) throws Exception
  {
    return new GetGnCategoryEvent(catCode);
  }

  private void getCheckFail(
    GetGnCategoryEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_BY_KEY_ERROR);
  }

  private void getCheckSuccess(
    GetGnCategoryEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}