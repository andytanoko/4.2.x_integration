/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SaveMyCompanyProfileActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 06 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.actions;

import com.gridnode.gtas.events.gridnode.SaveMyCompanyProfileEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.gridnode.helpers.ActionTestHelper;

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
 * This test cases tests the SaveMyCompanyProfileAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class SaveMyCompanyProfileActionTest extends ActionTestHelper
{
  SaveMyCompanyProfileEvent[] _events;

  public SaveMyCompanyProfileActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(SaveMyCompanyProfileActionTest.class);
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
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createSessions(1);
    createStateMachines(1);

  }

  protected void unitTest() throws Exception
  {
    SaveMyCompanyProfileEvent event = null;
    // ************** REJECTED ***************************

    // ************** ACCEPTED *****************************
    //not exist, create new
    event =  createTestEvent(getCreateCoyMap(
               createDefaultCoyProfile(false, 0, false)));
    updateCheckSuccess(event, _sessions[0], _sm[0]);

    CompanyProfile myProf = _coyMgr.findMyCompanyProfile();

    //exist, update
    event =  createTestEvent(getUpdCoyMap(myProf));
    updateCheckSuccess(event, _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new SaveMyCompanyProfileAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    SaveMyCompanyProfileEvent updEvent = (SaveMyCompanyProfileEvent)event;

    CompanyProfile coy = null;
    try
    {
      coy = _coyMgr.findMyCompanyProfile();
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[SaveMyCompanyProfileActionTest.checkActionEffect]", ex);

    }
    assertNotNull("CompanyProfile retrieved is null", coy);

    Map coyData = updEvent.getUpdProfile();
    checkCoy(coy, coyData);

  }


  // ************* Utility methods ***************************

  private SaveMyCompanyProfileEvent createTestEvent(
    Map profMap) throws Exception
  {
    return new SaveMyCompanyProfileEvent(profMap);
  }

  private void updateCheckFail(
    SaveMyCompanyProfileEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.SAVE_COY_PROFILE_ERROR);
  }

  private void updateCheckSuccess(
    SaveMyCompanyProfileEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }
}