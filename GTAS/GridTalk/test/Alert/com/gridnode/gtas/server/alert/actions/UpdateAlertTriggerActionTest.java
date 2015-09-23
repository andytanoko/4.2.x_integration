/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateAlertTriggerActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 23 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.actions;

import java.util.Collection;
import com.gridnode.gtas.events.alert.UpdateAlertTriggerEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.alert.helpers.ActionTestHelper;

import com.gridnode.gtas.server.alert.model.*;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;
import java.util.Date;
import java.sql.Timestamp;

/**
 * This test cases tests the UpdateAlertTriggerAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class UpdateAlertTriggerActionTest extends ActionTestHelper
{
  UpdateAlertTriggerEvent[] _events;
  AlertTrigger[]            _triggers;
  Long[]                    _alertUIDs;

  public UpdateAlertTriggerActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(UpdateAlertTriggerActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ******************* ActionTestHelper methods **********************

  protected void cleanUp()
  {
    try
    {
      Log.log("TEST", "[UpdateAlertTriggerActionTest.cleanUp] Starts");
      deleteAlertTriggers();
      purgeSessions();
    }
    finally
    {
      Log.log("TEST", "[UpdateAlertTriggerActionTest.cleanUp] Ends");
    }
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createAlertTriggers();

    createSessions(1);
    createStateMachines(1);

    _events = new UpdateAlertTriggerEvent[]
              {
                //accepted: 0-4
                createTestEvent((Long)_triggers[0].getKey(),
                  null, null, null, null, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, RECIPIENTS),
                createTestEvent((Long)_triggers[1].getKey(),
                  DOC_TYPE, null, null, null, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, RECIPIENTS),
                createTestEvent((Long)_triggers[2].getKey(),
                  DOC_TYPE_2, PARTNER_TYPE, null, null, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent((Long)_triggers[3].getKey(),
                  DOC_TYPE, PARTNER_TYPE_2, PARTNER_GROUP, null, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent((Long)_triggers[4].getKey(),
                  DOC_TYPE, null, null, PARTNER_ID, _alertUIDs[0], Boolean.FALSE, Boolean.FALSE, null),

                //rejected: 5-18
                createTestEvent((Long)_triggers[0].getKey(),
                  null, null, null, null, INVALID_UID, Boolean.TRUE, Boolean.FALSE, null),

                // level 0: 6
                createTestEvent((Long)_triggers[0].getKey(),
                  null, null, null, null, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, INVALID_RECIPIENTS),

                // level 1: 7
                createTestEvent((Long)_triggers[1].getKey(),
                  null, null, null, null, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),

                // level 2: 8-9
                createTestEvent((Long)_triggers[2].getKey(),
                  "", PARTNER_TYPE, null, null, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent((Long)_triggers[2].getKey(),
                  DOC_TYPE, "", null, null, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),

                // level 3: 10-12
                createTestEvent((Long)_triggers[3].getKey(),
                  "", PARTNER_TYPE, PARTNER_GROUP, null, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent((Long)_triggers[3].getKey(),
                  DOC_TYPE, "", PARTNER_GROUP, null, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent((Long)_triggers[3].getKey(),
                  DOC_TYPE, PARTNER_TYPE, "", null, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),

                // level 4: 13-14
                createTestEvent((Long)_triggers[4].getKey(),
                  "", null, null, PARTNER_ID, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent((Long)_triggers[4].getKey(),
                  DOC_TYPE, null, null, "", _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),

                // duplicate: 15-18
                createTestEvent((Long)_triggers[5].getKey(),
                  DOC_TYPE, null, null, null, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, RECIPIENTS),
                createTestEvent((Long)_triggers[6].getKey(),
                  DOC_TYPE_2, PARTNER_TYPE, null, null, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent((Long)_triggers[7].getKey(),
                  DOC_TYPE, PARTNER_TYPE_2, PARTNER_GROUP, null, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent((Long)_triggers[8].getKey(),
                  DOC_TYPE, null, null, PARTNER_ID, _alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
              };

  }

  protected void unitTest() throws Exception
  {
    // ************** ACCEPTED *****************************
    for (int i=0; i<5; i++)
      updateCheckSuccess(_events[i], _sessions[0], _sm[0]);

    // ************** REJECTED ***************************
    updateCheckFail(_events[5], _sessions[0], _sm[0], true);

    for (int i=6; i<18; i++)
      updateCheckFail(_events[i], _sessions[0], _sm[0], false);
  }

  protected IEJBAction createNewAction()
  {
    return new UpdateAlertTriggerAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    UpdateAlertTriggerEvent updEvent = (UpdateAlertTriggerEvent)event;

    AlertTrigger trigger = findAlertTrigger(updEvent.getUID());
    assertNotNull("AlertTrigger retrieved is null", trigger);

    assertEquals("Doc Type is incorrect", updEvent.getDocType(), trigger.getDocumentType());
    assertEquals("Partner Type is incorrect", updEvent.getPartnerType(), trigger.getPartnerType());
    assertEquals("Partner Group is incorrect", updEvent.getPartnerGroup(), trigger.getPartnerGroup());
    assertEquals("Partner ID is incorrect", updEvent.getPartnerID(), trigger.getPartnerId());
    assertEquals("Alert UID is incorrect", updEvent.getAlertUID(), trigger.getAlertUID());
    assertEquals("isEnabled is incorrect", updEvent.getIsEnabled(), new Boolean(trigger.isEnabled()));
    assertEquals("isAttachDoc is incorrect", updEvent.getIsAttachDoc(), new Boolean(trigger.isAttachDoc()));

    if (updEvent.getRecipients() == null)
      assertTrue("Recipients is incorrect", trigger.getRecipients().isEmpty());
    else
      assertEquals("Recipients is incorrect", updEvent.getRecipients(), trigger.getRecipients());

  }


  // ************* Utility methods ***************************

  private void createAlertTriggers() throws Exception
  {
    _alertUIDs = createAlerts();

    RECIPIENTS.add("EMAIL_ADDRESS:emaila@one.com");
    RECIPIENTS.add("USER:admin");
    RECIPIENTS.add("ROLE:User");
    RECIPIENTS.add("EMAIL_CODE:code1");
    RECIPIENTS.add("EMAIL_CODE_XPATH:/EmailCodeRef/EmailCode/@id");

    INVALID_RECIPIENTS.add("EMAIL_ADDRESS:emaila@one.com");
    INVALID_RECIPIENTS.add("USER:admin");
    INVALID_RECIPIENTS.add("ROLE:User");
    INVALID_RECIPIENTS.add("EMAIL_CODE:code1");
    INVALID_RECIPIENTS.add("EMAIL_CODE_XPATH:/EmailCodeRef/EmailCode/@id");
    //invalid
    INVALID_RECIPIENTS.add("EMAIL_CODE=code1");

    _triggers = new AlertTrigger[9];
    _triggers[0] = createAlertTrigger(AlertTrigger.LEVEL_0, ALERT_TYPE,
                     null, null, null, null, _alertUIDs[0], true, false, null);
    _triggers[1] = createAlertTrigger(AlertTrigger.LEVEL_1, ALERT_TYPE,
                     DOC_TYPE, null, null, null, _alertUIDs[0], true, false, null);
    _triggers[2] = createAlertTrigger(AlertTrigger.LEVEL_2, ALERT_TYPE,
                     DOC_TYPE, PARTNER_TYPE, null, null, _alertUIDs[0], true, false, null);
    _triggers[3] = createAlertTrigger(AlertTrigger.LEVEL_3, ALERT_TYPE,
                     DOC_TYPE, PARTNER_TYPE, PARTNER_GROUP, null, _alertUIDs[0], true, false, null);
    _triggers[4] = createAlertTrigger(AlertTrigger.LEVEL_4, ALERT_TYPE,
                     DOC_TYPE, null, null, PARTNER_ID, _alertUIDs[0], true, false, null);

    _triggers[5] = createAlertTrigger(AlertTrigger.LEVEL_1, ALERT_TYPE,
                     DOC_TYPE_2, null, null, null, _alertUIDs[0], true, false, RECIPIENTS);
    _triggers[6] = createAlertTrigger(AlertTrigger.LEVEL_2, ALERT_TYPE,
                     DOC_TYPE, PARTNER_TYPE_2, null, null, _alertUIDs[0], true, false, RECIPIENTS);
    _triggers[7] = createAlertTrigger(AlertTrigger.LEVEL_3, ALERT_TYPE,
                     DOC_TYPE, PARTNER_TYPE, PARTNER_GROUP_2, null, _alertUIDs[0], true, false, RECIPIENTS);
    _triggers[8] = createAlertTrigger(AlertTrigger.LEVEL_4, ALERT_TYPE,
                     DOC_TYPE, null, null, PARTNER_ID_2, _alertUIDs[0], true, false, RECIPIENTS);
  }

  private void deleteAlertTriggers()
  {
    deleteAlertTrigger(AlertTrigger.LEVEL_0, ALERT_TYPE, null, null, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_1, ALERT_TYPE, DOC_TYPE, null, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_2, ALERT_TYPE, DOC_TYPE, PARTNER_TYPE, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_3, ALERT_TYPE, DOC_TYPE, PARTNER_TYPE, PARTNER_GROUP, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_4, ALERT_TYPE, DOC_TYPE, null, null, PARTNER_ID);

    deleteAlertTrigger(AlertTrigger.LEVEL_1, ALERT_TYPE, DOC_TYPE_2, null, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_2, ALERT_TYPE, DOC_TYPE, PARTNER_TYPE_2, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_3, ALERT_TYPE, DOC_TYPE, PARTNER_TYPE, PARTNER_GROUP_2, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_4, ALERT_TYPE, DOC_TYPE, null, null, PARTNER_ID_2);

    deleteAlertTrigger(AlertTrigger.LEVEL_2, ALERT_TYPE, DOC_TYPE_2, PARTNER_TYPE, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_3, ALERT_TYPE, DOC_TYPE, PARTNER_TYPE_2, PARTNER_GROUP, null);

    deleteAlerts();
  }

  private UpdateAlertTriggerEvent createTestEvent(
    Long uID,
    String docType,
    String partnerType,
    String partnerGroup,
    String partnerId,
    Long   alertUID,
    Boolean isEnabled,
    Boolean isAttachDoc,
    Collection recipients) throws Exception
  {
    return new UpdateAlertTriggerEvent(uID, docType, partnerType,
                 partnerGroup, partnerId, alertUID, isEnabled, isAttachDoc,
                 recipients);
  }

  private void updateCheckFail(
    UpdateAlertTriggerEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.UPDATE_ENTITY_ERROR);
  }

  private void updateCheckSuccess(
    UpdateAlertTriggerEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }
}