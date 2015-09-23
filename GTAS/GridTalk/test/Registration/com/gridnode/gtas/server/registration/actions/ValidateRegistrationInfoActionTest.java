/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ValidateRegistrationInfoActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 12 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.actions;

import com.gridnode.gtas.server.registration.model.RegistrationInfo;
import javax.ejb.Handle;
import com.gridnode.gtas.events.registration.ValidateRegistrationInfoEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.registration.helpers.ActionTestHelper;

import com.gridnode.pdip.app.coyprofile.model.*;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.PasswordMask;

import junit.framework.*;
import java.util.Date;
import java.util.Map;
import java.sql.Timestamp;


/**
 * This test cases tests the ValidateRegistrationInfoAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class ValidateRegistrationInfoActionTest extends ActionTestHelper
{
  ValidateRegistrationInfoEvent[] _events;

  public ValidateRegistrationInfoActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ValidateRegistrationInfoActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ******************* ActionTestHelper methods **********************

  protected void cleanUp()
  {
    cleanUpCoys();
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
    ValidateRegistrationInfoEvent event = null;

    // ************** ACCEPTED *****************************
    //not exist company profile
    event =  createTestEvent(GRIDNODE_ID, GRIDNODE_NAME,
               PROD_KEY_F1, PROD_KEY_F2, PROD_KEY_F3, PROD_KEY_F4,
               getCreateCoyMap(createDefaultCoyProfile(false, 0, false)));

    validateCheckSuccess(event, _sessions[0], _sm[0]);

    //exist company profile
    event = createTestEvent(GRIDNODE_ID, GRIDNODE_NAME,
              PROD_KEY_F1, PROD_KEY_F2, PROD_KEY_F3, PROD_KEY_F4,
              getUpdCoyMap(getMyCoyProfile()));
    validateCheckSuccess(event, _sessions[0], _sm[0]);

    // ************** REJECTED ***************************
    // wrong product key
    event = createTestEvent(GRIDNODE_ID, GRIDNODE_NAME,
              PROD_KEY_F1, PROD_KEY_F2, PROD_KEY_F3, DUMMY_PK,
              getUpdCoyMap(getMyCoyProfile()));
    validateCheckFail(event, _sessions[0], _sm[0], false);

    // wrong gridnodeid
    event = createTestEvent(DUMMY_GNODE_ID, GRIDNODE_NAME,
              PROD_KEY_F1, PROD_KEY_F2, PROD_KEY_F3, PROD_KEY_F4,
              getUpdCoyMap(getMyCoyProfile()));
    validateCheckFail(event, _sessions[0], _sm[0], false);

    // wrong company profile
    event = createTestEvent(GRIDNODE_ID, GRIDNODE_NAME,
              PROD_KEY_F1, PROD_KEY_F2, PROD_KEY_F3, PROD_KEY_F4,
              getDummyCoyMap(getMyCoyProfile()));
    validateCheckFail(event, _sessions[0], _sm[0], false);

  }

  protected IEJBAction createNewAction()
  {
    return new ValidateRegistrationInfoAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    ValidateRegistrationInfoEvent valEvent = (ValidateRegistrationInfoEvent)event;

    Map regInfo = (Map)response.getReturnData();
    assertNotNull("RegistrationInfo is null", regInfo);

//    Handle handle = (Handle)sm.getAttribute(ValidateRegistrationInfoAction.REG_BEAN_HANDLE);
//    assertNotNull("RegistrationBeanHandle is null", handle);

    assertEquals("GridNodeID is incorrect", valEvent.getGridnodeID(), regInfo.get(RegistrationInfo.GRIDNODE_ID));
    assertEquals("GridNodeName is incorrect", valEvent.getGridnodeName(), regInfo.get(RegistrationInfo.GRIDNODE_NAME));
    checkCoy((Map)regInfo.get(RegistrationInfo.COMPANY_PROFILE), valEvent.getCompanyProfile());
//    assertEquals("ProductKey F1 is incorrect", valEvent.getProdKeyF1(), regInfo.get(RegistrationInfo.PRODUCT_KEY_F1));
//    assertEquals("ProductKey F2 is incorrect", valEvent.getProdKeyF2(), regInfo.get(RegistrationInfo.PRODUCT_KEY_F2));
//    assertEquals("ProductKey F3 is incorrect", valEvent.getProdKeyF3(), regInfo.get(RegistrationInfo.PRODUCT_KEY_F3));
//    assertEquals("ProductKey F4 is incorrect", valEvent.getProdKeyF4(), regInfo.get(RegistrationInfo.PRODUCT_KEY_F4));
    assertNotNull("BizConnections is null", regInfo.get(RegistrationInfo.BIZ_CONNECTIONS));
    assertNotNull("Category is null", regInfo.get(RegistrationInfo.CATEGORY));
    assertNotNull("License end date is null", regInfo.get(RegistrationInfo.LIC_END_DATE));
    assertNotNull("License start date is null", regInfo.get(RegistrationInfo.LIC_START_DATE));
    assertEquals("RegistrationState is incorrect", new Short(RegistrationInfo.STATE_REG_IN_PROGRESS), regInfo.get(RegistrationInfo.REGISTRATION_STATE));
  }


  // ************* Utility methods ***************************

  private ValidateRegistrationInfoEvent createTestEvent(
    Integer gridnodeID, String gridnodeName, String prodKeyF1,
    String prodKeyF2, String prodKeyF3, String prodKeyF4,
    Map profMap) throws Exception
  {
    return new ValidateRegistrationInfoEvent(
                 gridnodeID, gridnodeName, /*prodKeyF1, prodKeyF2,
                 prodKeyF3, prodKeyF4,*/ null, profMap);
  }

  private void validateCheckFail(
    ValidateRegistrationInfoEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.INVALID_REGISTRATION_ERROR);
  }

  private void validateCheckSuccess(
    ValidateRegistrationInfoEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }
}