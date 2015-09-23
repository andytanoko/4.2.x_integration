/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetMyCompanyProfileActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 06 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.actions;

import com.gridnode.gtas.model.gridnode.ICompanyProfile;
import com.gridnode.gtas.events.gridnode.GetMyCompanyProfileEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.gridnode.helpers.ActionTestHelper;

import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;

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
 * This Test case tests the GetMyCompanyProfileAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class GetMyCompanyProfileActionTest extends ActionTestHelper
{
  GetMyCompanyProfileEvent[] _events;

  public GetMyCompanyProfileActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetMyCompanyProfileActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *********************

  protected void cleanUp()
  {
    cleanUpCoys();
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

    _events = new GetMyCompanyProfileEvent[]
              {
                //accepted
                createTestEvent(),
                createTestEvent(),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** REJECTED ***************************

    // ************** ACCEPTED *****************************
    //not exist, return empty Map
    getCheckSuccess(_events[0], _sessions[0], _sm[0]);

    createCoyProfile(false, 0, false);
    //exist
    getCheckSuccess(_events[1], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new GetMyCompanyProfileAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    Object returnData = response.getReturnData();
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof Map);

    Map coyMap = (Map)returnData;

    GetMyCompanyProfileEvent getEvent = (GetMyCompanyProfileEvent)event;

    CompanyProfile coy = null;

    try
    {
      coy = _coyMgr.findMyCompanyProfile();
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[GetMyCompanyProfileActionTest.checkActionEffect]", ex);
    }

    if (coy == null)
    {
      coy = new CompanyProfile();
      coy.setPartner(Boolean.FALSE);
    }
      checkCoy(coy, coyMap);

  }


  // ********************* Utiltity methods *************************
  private GetMyCompanyProfileEvent createTestEvent() throws Exception
  {
    return new GetMyCompanyProfileEvent();
  }

  private void getCheckFail(
    GetMyCompanyProfileEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_BY_KEY_ERROR);
  }

  private void getCheckSuccess(
    GetMyCompanyProfileEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}