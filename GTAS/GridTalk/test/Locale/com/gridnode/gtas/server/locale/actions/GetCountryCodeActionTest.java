/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCountryCodeActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 04 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.locale.actions;

import com.gridnode.gtas.model.locale.ICountryCode;
import com.gridnode.gtas.events.locale.GetCountryCodeEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.locale.helpers.ActionTestHelper;

import com.gridnode.pdip.base.locale.model.CountryCode;

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
 * This Test case tests the GetCountryCodeAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetCountryCodeActionTest extends ActionTestHelper
{
  GetCountryCodeEvent[] _events;


  public GetCountryCodeActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetCountryCodeActionTest.class);
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

    _events = new GetCountryCodeEvent[]
              {
                //rejected
                createTestEvent(DUMMY_STRING),

                //accepted
                createTestEvent(CTY_ALP3),
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
    return new GetCountryCodeAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    Object returnData = response.getReturnData();
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof Map);

    Map countryMap = (Map)returnData;

    GetCountryCodeEvent getEvent = (GetCountryCodeEvent)event;
    CountryCode code = findCountryCodeByAlpha3Code(getEvent.getAlpha3Code());

    checkCountryCode(code, countryMap);
  }


  // ********************* Utiltity methods *************************
  private GetCountryCodeEvent createTestEvent(String alpha3Code) throws Exception
  {
    return new GetCountryCodeEvent(alpha3Code);
  }

  private void getCheckFail(
    GetCountryCodeEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_BY_KEY_ERROR);
  }

  private void getCheckSuccess(
    GetCountryCodeEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}