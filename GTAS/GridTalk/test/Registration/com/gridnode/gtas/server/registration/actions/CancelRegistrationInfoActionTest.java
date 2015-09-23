/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CancelRegistrationInfoActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 19 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.actions;

import com.gridnode.gtas.server.registration.helpers.ServiceLookupHelper;
import java.util.GregorianCalendar;
import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.pdip.app.license.model.License;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceObj;
import com.gridnode.gtas.server.registration.model.RegistrationInfo;
import com.gridnode.gtas.events.registration.CancelRegistrationInfoEvent;
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

import javax.ejb.Handle;

import java.util.Date;
import java.util.Map;
import java.sql.Timestamp;


/**
 * This test cases tests the CancelRegistrationInfoAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class CancelRegistrationInfoActionTest extends ActionTestHelper
{
  CancelRegistrationInfoEvent[] _events;

  public CancelRegistrationInfoActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(CancelRegistrationInfoActionTest.class);
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
    CancelRegistrationInfoEvent event = createTestEvent();

    // accepted: cancel with initRegistration
    cancelCheckPass(event, _sessions[0], _sm[0]);

    // accepted: cancel after initRegistration
    initRegistration(GRIDNODE_ID, GRIDNODE_NAME,
               PROD_KEY_F1, PROD_KEY_F2, PROD_KEY_F3, PROD_KEY_F4,
               getCreateCoyMap(createDefaultCoyProfile(false, 0, false)),
               _sessions[0], _sm[0]);
    cancelCheckPass(event, _sessions[0], _sm[0]);

    // rejected: cancel after confirmRegistration
    initRegistration(GRIDNODE_ID, GRIDNODE_NAME,
               PROD_KEY_F1, PROD_KEY_F2, PROD_KEY_F3, PROD_KEY_F4,
               getUpdCoyMap(getMyCoyProfile()),
               _sessions[0], _sm[0]);
    confirmRegistration(PASSWORD, _sessions[0], _sm[0]);
    cancelCheckFail(event, _sessions[0], _sm[0], false);

  }

  protected IEJBAction createNewAction()
  {
    return new CancelRegistrationInfoAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    CancelRegistrationInfoEvent valEvent = (CancelRegistrationInfoEvent)event;

    try
    {
      //Handle handle = (Handle)sm.getAttribute(CancelRegistrationInfoAction.REG_BEAN_HANDLE);
      //IRegistrationServiceObj regService = (IRegistrationServiceObj)handle.getEJBObject();
      RegistrationInfo regInfo = getRegService().getRegistrationInfo();

      assertNull("GridNode ID is not cleared", regInfo.getGridnodeID());
      assertNull("GridNode Name is not cleared", regInfo.getGridnodeName());
      assertNull("License enddate is not cleared", regInfo.getLicenseEndDate());
      assertNull("License startdate is not cleared", regInfo.getLicenseStartDate());
      assertNull("Productkey F1 is not cleared", regInfo.getProductKeyF1());
      assertNull("Productkey F2 is not cleared", regInfo.getProductKeyF2());
      assertNull("Productkey F3 is not cleared", regInfo.getProductKeyF3());
      assertNull("Productkey F4 is not cleared", regInfo.getProductKeyF4());
      assertEquals("RegistrationState is incorrect", regInfo.STATE_NOT_REGISTERED, regInfo.getRegistrationState());
    }
    catch (Throwable t)
    {
      Log.err("TEST", "CancelRegistrationInfoActionTest.checkActionEffect]", t);
      assertTrue("checkActionEffect fail", false);
    }
  }

  // ************* Utility methods ***************************

  private CancelRegistrationInfoEvent createTestEvent()
    throws Exception
  {
    return new CancelRegistrationInfoEvent();
  }

  private void cancelCheckFail(
    CancelRegistrationInfoEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.INVALID_REGISTRATION_ERROR);
  }

  private void cancelCheckPass(
    CancelRegistrationInfoEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }
}