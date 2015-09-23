/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConfirmRegistrationInfoActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 18 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.actions;

import java.io.File;
import com.gridnode.gtas.events.registration.ConfirmRegistrationInfoEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.registration.product.RegistrationLogic;
import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceObj;
import com.gridnode.gtas.server.registration.model.RegistrationInfo;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.registration.helpers.ActionTestHelper;

import com.gridnode.pdip.app.coyprofile.model.*;
import com.gridnode.pdip.app.license.model.License;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

import junit.framework.*;

import javax.ejb.Handle;

import java.util.Date;
import java.util.Map;
import java.sql.Timestamp;


/**
 * This test cases tests the ConfirmRegistrationInfoAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class ConfirmRegistrationInfoActionTest extends ActionTestHelper
{
  private static final String CERT_DIR     = "D:/J2EE/jboss-3.0.0alpha/bin/gtas/data/sys/cert/";

  ConfirmRegistrationInfoEvent[] _events;

  public ConfirmRegistrationInfoActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ConfirmRegistrationInfoActionTest.class);
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
    ConfirmRegistrationInfoEvent event = null;

    // ************** REJECTED ***************************
    // rejected: no initRegistration
    event = createTestEvent(PASSWORD);
    confirmCheckFail(event, _sessions[0], _sm[0], false);

    // accepted: confirm after initRegistration
    initRegistration(GRIDNODE_ID, GRIDNODE_NAME,
               PROD_KEY_F1, PROD_KEY_F2, PROD_KEY_F3, PROD_KEY_F4,
               getCreateCoyMap(createDefaultCoyProfile(false, 0, false)),
               _sessions[0], _sm[0]);
    confirmCheckPass(event, _sessions[0], _sm[0]);

    // rejected: double confirm
    confirmCheckFail(event, _sessions[0], _sm[0], false);
  }

  protected IEJBAction createNewAction()
  {
    return new ConfirmRegistrationInfoAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    ConfirmRegistrationInfoEvent valEvent = (ConfirmRegistrationInfoEvent)event;

    try
    {
      //Handle handle = (Handle)sm.getAttribute(ConfirmRegistrationInfoAction.REG_BEAN_HANDLE);
      //IRegistrationServiceObj regService = (IRegistrationServiceObj)handle.getEJBObject();

      RegistrationInfo regInfo = getRegService().getRegistrationInfo();

      assertEquals("RegistrationState is incorrect", regInfo.STATE_REGISTERED, regInfo.getRegistrationState());

      // check license
      checkLicense(regInfo);
      // check gridnode
      checkGridNode(regInfo);
      // check connection status
      checkConnectionStatus(regInfo);
      // check gridnode cert, certReq, gm cert
      checkSecuritySetup(regInfo);
    }
    catch (Throwable t)
    {
      Log.err("TEST", "ConfirmRegistrationInfoActionTest.checkActionEffect]", t);
      assertTrue("checkActionEffect fail", false);
    }
  }

  private void checkLicense(RegistrationInfo regInfo)
  {
    String prodKey = regInfo.getProductKeyF1() +
                        regInfo.getProductKeyF2() + regInfo.getProductKeyF3() +
                        regInfo.getProductKeyF4();
    License license = findLicenseByProductKey(prodKey);

    assertNotNull("License is not created!", license);

    assertEquals("ProductKey is incorrect", prodKey, license.getProductKey());
    checkDate(regInfo.getLicenseEndDate(), license.getEndDate(), "EndDate");
    checkDate(regInfo.getLicenseStartDate(), license.getStartDate(), "StartDate");
  }

  private void checkGridNode(RegistrationInfo regInfo)
  {
    GridNode gridnode = findGridNodeByNodeID(regInfo.getGridnodeID().toString());

    assertNotNull("Gridnode is not created!", gridnode);

    assertEquals("Category is incorrect", regInfo.getCategory(), gridnode.getCategory());
    assertEquals("CompanyProfile is incorrect", regInfo.getCompanyProfile().getKey(), gridnode.getCoyProfileUID());
    assertEquals("GridNodeID is incorrect", regInfo.getGridnodeID().toString(), gridnode.getID());
    assertEquals("GridNodeName is incorrect", regInfo.getGridnodeName(), gridnode.getName());
    assertEquals("State is incorrect", GridNode.STATE_ME, gridnode.getState());
  }

  private void checkConnectionStatus(RegistrationInfo regInfo)
  {
    ConnectionStatus status = findConnectionStatusByNodeID(regInfo.getGridnodeID().toString());

    assertNotNull("ConnectionStatus is not created!", status);

    assertEquals("GridNodeID is incorrect", regInfo.getGridnodeID().toString(), status.getGridNodeID());

  }

  private void checkSecuritySetup(RegistrationInfo regInfo)
  {
    // check gridnode cert, certReq, gm cert
    assertCertExists(regInfo.getGridnodeID(), RegistrationLogic.GT_CERT_NAME);

    assertCertExists(RegistrationLogic.GM_NODEID, RegistrationLogic.GM_CERT_NAME);
    assertCertReqExists(regInfo.getGridnodeID(), RegistrationLogic.GT_CERT_NAME,
      RegistrationLogic.REQ_FILE_EXT);
  }

  // ************* Utility methods ***************************

  private ConfirmRegistrationInfoEvent createTestEvent(String password)
    throws Exception
  {
    return new ConfirmRegistrationInfoEvent(password);
  }

  private void confirmCheckFail(
    ConfirmRegistrationInfoEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.INVALID_REGISTRATION_ERROR);
  }

  private void confirmCheckPass(
    ConfirmRegistrationInfoEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private void assertCertExists(Integer nodeID, String certName)
  {
    try
    {
      _certMgr.findCertificateByIDAndName(nodeID.intValue(), certName);
    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ConfirmRegistrationInfoACtionTest.assertCertExists] Can't find cert", t);
      assertTrue("Cert does not exists for "+nodeID+" "+certName, false);
    }

  }

  private void assertCertReqExists(Integer nodeID, String certName, String ext)
  {
    try
    {
      assertTrue("CertRequest does not exist",
      new File(CERT_DIR + certName+nodeID+ext).exists());

    }
    catch (Throwable t)
    {
      Log.err("TEST", "[ConfirmRegistrationInfoACtionTest.assertCertReqExists] Can't find certrequest", t);
      assertTrue("CertRequest not generated", false);

    }


  }
}