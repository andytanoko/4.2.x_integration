/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateBusinessEntityActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.events.bizreg.UpdateBusinessEntityEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.bizreg.helpers.ActionTestHelper;

import com.gridnode.pdip.app.bizreg.model.*;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.PasswordMask;

import junit.framework.*;
import java.util.Date;
import java.util.Map;
import java.util.Vector;
import java.sql.Timestamp;


/**
 * This test cases tests the UpdateBusinessEntityAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class UpdateBusinessEntityActionTest extends ActionTestHelper
{
  UpdateBusinessEntityEvent[] _events;

  public UpdateBusinessEntityActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(UpdateBusinessEntityActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ******************* ActionTestHelper methods **********************

  protected void cleanUp()
  {
    cleanUpBEs("ENT1");
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
    Long uId = createBe("ENT1", "PBE", "Partner Business entity", true);
    createBes(2, 1);

    createSessions(1);
    createStateMachines(1);

    _events = new UpdateBusinessEntityEvent[]
              {
                //rejected
                createTestEvent(DUMMY_UID),
                createTestEvent(uId),

                //accepted
                createTestEvent(_uIDs[0]),
                createTestEvent(_uIDs[1]),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** REJECTED ***************************
    //non existing business entity
    updateCheckFail(_events[0], _sessions[0], _sm[0], true);
    //partner business entity (with enterprise id)
    updateCheckFail(_events[1], _sessions[0], _sm[0], true);

    // ************** ACCEPTED *****************************
    //own business entity
    updateCheckSuccess(_events[2], _sessions[0], _sm[0]);
    //partner business entity (without enterprise id)
    updateCheckSuccess(_events[3], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new UpdateBusinessEntityAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    UpdateBusinessEntityEvent updEvent = (UpdateBusinessEntityEvent)event;

    BusinessEntity be = findBizEntityByUId(updEvent.getBeUID());
    assertNotNull("Business entity retrieved is null", be);

    WhitePage wp = be.getWhitePage();
    assertNotNull("Whitepage is null", wp);

    assertNotNull("UID is null", be.getFieldValue(BusinessEntity.UID));
    assertEquals("Description is incorrect", updEvent.getUpdDescription(), be.getDescription());

    Map wpData = updEvent.getUpdWhitePage();
    checkWp(wp, wpData, false);

  }


  // ************* Utility methods ***************************

  private UpdateBusinessEntityEvent createTestEvent(
    Long uID) throws Exception
  {
    return new UpdateBusinessEntityEvent(uID, UPD_DESC, getUpdWhitePageData(uID), new Vector());
  }

  private void updateCheckFail(
    UpdateBusinessEntityEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.UPDATE_ENTITY_ERROR);
  }

  private void updateCheckSuccess(
    UpdateBusinessEntityEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }
}