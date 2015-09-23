/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAlertTriggerActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 23 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.actions;

import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.gtas.server.alert.helpers.ActionTestHelper;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.model.alert.IAlertTrigger;
import com.gridnode.gtas.events.alert.GetAlertTriggerEvent;
import com.gridnode.gtas.exceptions.IErrorCode;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;

import java.util.Map;
import java.util.Iterator;

/**
 * This Test case tests the GetAlertTriggerAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class GetAlertTriggerActionTest extends ActionTestHelper
{
  GetAlertTriggerEvent[] _events;
  AlertTrigger[]         _triggers;

  public GetAlertTriggerActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetAlertTriggerActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *********************

  protected void cleanUp()
  {
    deleteAlertTrigger(AlertTrigger.LEVEL_3, ALERT_TYPE, DOC_TYPE, PARTNER_TYPE,
                             PARTNER_GROUP, null);
    deleteAlerts();
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    Long[] alertUIDs =  createAlerts();

    RECIPIENTS.add("EMAIL_ADDRESS:emaila@one.com");
    RECIPIENTS.add("USER:admin");
    RECIPIENTS.add("ROLE:User");
    RECIPIENTS.add("EMAIL_CODE:code1");
    RECIPIENTS.add("EMAIL_CODE_XPATH:/EmailCodeRef/EmailCode/@id");

    AlertTrigger trigger = createAlertTrigger(AlertTrigger.LEVEL_3, ALERT_TYPE, DOC_TYPE, PARTNER_TYPE,
                             PARTNER_GROUP, null, alertUIDs[0], true, false, RECIPIENTS);
    _triggers = new AlertTrigger[] {trigger};

    createSessions(1);
    createStateMachines(1);

    _events = new GetAlertTriggerEvent[]
              {
                //rejected
                createTestEvent(INVALID_UID),

                //accepted
                createTestEvent(new Long(_triggers[0].getUId())),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** REJECTED ***************************
    //non existing trigger
    getCheckFail(_events[0], _sessions[0], _sm[0], false);

    // ************** ACCEPTED *****************************
    getCheckSuccess(_events[1], _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new GetAlertTriggerAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    Object returnData = response.getReturnData();
    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof Map);

    Map triggerMap = (Map)returnData;

    GetAlertTriggerEvent getEvent = (GetAlertTriggerEvent)event;
    AlertTrigger trigger = findAlertTrigger(getEvent.getAlertTriggerUID());

    assertEquals("Alert Type is incorrect", trigger.getAlertType(), triggerMap.get(IAlertTrigger.ALERT_TYPE));
    assertEquals("Doc Type is incorrect", trigger.getDocumentType(), triggerMap.get(IAlertTrigger.DOC_TYPE));
    assertEquals("Partner Type is incorrect", trigger.getPartnerType(), triggerMap.get(IAlertTrigger.PARTNER_TYPE));
    assertEquals("Partner Group is incorrect", trigger.getPartnerGroup(), triggerMap.get(IAlertTrigger.PARTNER_GROUP));
    assertEquals("Partner ID is incorrect", trigger.getPartnerId(), triggerMap.get(IAlertTrigger.PARTNER_ID));
    assertEquals("Alert UID is incorrect", trigger.getAlertUID(), triggerMap.get(IAlertTrigger.ALERT_UID));
    assertEquals("isEnabled is incorrect", new Boolean(trigger.isEnabled()), triggerMap.get(IAlertTrigger.IS_ENABLED));
    assertEquals("isAttachDoc is incorrect", new Boolean(trigger.isAttachDoc()), triggerMap.get(IAlertTrigger.IS_ATTACH_DOC));
    assertEquals("Recipients is incorrect", trigger.getRecipients(), triggerMap.get(IAlertTrigger.RECIPIENTS));
  }


  // ********************* Utiltity methods *************************
  private GetAlertTriggerEvent createTestEvent(Long uid) throws Exception
  {
    return new GetAlertTriggerEvent(uid);
  }

  private void getCheckFail(
    GetAlertTriggerEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_BY_KEY_ERROR);
  }

  private void getCheckSuccess(
    GetAlertTriggerEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}