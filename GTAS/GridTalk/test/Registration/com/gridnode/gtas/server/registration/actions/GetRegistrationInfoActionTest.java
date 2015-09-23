/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetRegistrationInfoActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 19 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.actions;

import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceObj;
import com.gridnode.gtas.server.registration.model.RegistrationInfo;
import com.gridnode.gtas.server.registration.helpers.ActionTestHelper;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.registration.GetRegistrationInfoEvent;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.pdip.app.coyprofile.model.*;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.PasswordMask;

import junit.framework.*;

import javax.ejb.Handle;

import java.util.Date;
import java.util.Map;
import java.sql.Timestamp;


/**
 * This test cases tests the GetRegistrationInfoAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GetRegistrationInfoActionTest extends ActionTestHelper
{
  GetRegistrationInfoEvent[] _events;

  public GetRegistrationInfoActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetRegistrationInfoActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ******************* ActionTestHelper methods **********************

  protected void cleanUp()
  {
    cleanUpCoys();
    cleanUpGridNodes();
    cleanUpLicenses();
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    undoRegistration();
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createSessions(1);
    createStateMachines(1);
  }

  protected void unitTest() throws Exception
  {
    GetRegistrationInfoEvent event = createTestEvent();

    // accepted: get without initRegistration
    getCheckPass(event, _sessions[0], _sm[0]);

    // accepted: get after initRegistration
    initRegistration(GRIDNODE_ID, GRIDNODE_NAME,
               PROD_KEY_F1, PROD_KEY_F2, PROD_KEY_F3, PROD_KEY_F4,
               getCreateCoyMap(createDefaultCoyProfile(false, 0, false)),
               _sessions[0], _sm[0]);
    getCheckPass(event, _sessions[0], _sm[0]);

    // accepted: get after confirmRegistration
    initRegistration(GRIDNODE_ID, GRIDNODE_NAME,
               PROD_KEY_F1, PROD_KEY_F2, PROD_KEY_F3, PROD_KEY_F4,
               getUpdCoyMap(getMyCoyProfile()),
               _sessions[0], _sm[0]);
    confirmRegistration(PASSWORD, _sessions[0], _sm[0]);
    getCheckPass(event, _sessions[0], _sm[0]);

  }

  protected IEJBAction createNewAction()
  {
    return new GetRegistrationInfoAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    GetRegistrationInfoEvent getEvent = (GetRegistrationInfoEvent)event;

    try
    {
      Handle handle = (Handle)sm.getAttribute(GetRegistrationInfoAction.REG_BEAN_HANDLE);
      //IRegistrationServiceObj regService = (IRegistrationServiceObj)handle.getEJBObject();

      Map regMap = (Map)response.getReturnData();
      assertNotNull("RegistrationInfo is null", regMap);

      RegistrationInfo regInfo = getRegService().getRegistrationInfo();

      CompanyProfile coy = regInfo.getCompanyProfile();
      if (coy == null) coy = new CompanyProfile();
      assertEquals("GridNodeID is incorrect", regInfo.getGridnodeID(), regMap.get(RegistrationInfo.GRIDNODE_ID));
      assertEquals("GridNodeName is incorrect", regInfo.getGridnodeName(), regMap.get(RegistrationInfo.GRIDNODE_NAME));
      checkCoy(coy, (Map)regMap.get(RegistrationInfo.COMPANY_PROFILE));
      assertEquals("ProductKey F1 is incorrect", regInfo.getProductKeyF1(), regMap.get(RegistrationInfo.PRODUCT_KEY_F1));
      assertEquals("ProductKey F2 is incorrect", regInfo.getProductKeyF2(), regMap.get(RegistrationInfo.PRODUCT_KEY_F2));
      assertEquals("ProductKey F3 is incorrect", regInfo.getProductKeyF3(), regMap.get(RegistrationInfo.PRODUCT_KEY_F3));
      assertEquals("ProductKey F4 is incorrect", regInfo.getProductKeyF4(), regMap.get(RegistrationInfo.PRODUCT_KEY_F4));
      assertEquals("BizConnections is incorrect", regInfo.getBizConnections(), regMap.get(RegistrationInfo.BIZ_CONNECTIONS));
      assertEquals("Category is incorrect", regInfo.getCategory(), regMap.get(RegistrationInfo.CATEGORY));
      checkDate(regInfo.getLicenseEndDate(), (Date)regMap.get(RegistrationInfo.LIC_END_DATE), "EndDate");
      checkDate(regInfo.getLicenseStartDate(), (Date)regMap.get(RegistrationInfo.LIC_START_DATE), "StartDate");
      assertEquals("RegistrationState is incorrect", new Short(regInfo.getRegistrationState()), regMap.get(RegistrationInfo.REGISTRATION_STATE));
    }
    catch (Throwable t)
    {
      Log.err("TEST", "GetRegistrationInfoActionTest.checkActionEffect]", t);
      assertTrue("checkActionEffect fail", false);
    }
  }

  // ************* Utility methods ***************************

  private GetRegistrationInfoEvent createTestEvent()
    throws Exception
  {
    return new GetRegistrationInfoEvent();
  }

  private void getCheckFail(
    GetRegistrationInfoEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.INVALID_REGISTRATION_ERROR);
  }

  private void getCheckPass(
    GetRegistrationInfoEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }
}