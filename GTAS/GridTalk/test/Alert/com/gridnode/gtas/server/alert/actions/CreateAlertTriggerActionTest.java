/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateAlertTriggerActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 23 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.actions;

import com.gridnode.gtas.server.alert.helpers.Logger;
import java.util.Collection;
import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.gtas.events.alert.CreateAlertTriggerEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.alert.helpers.ActionTestHelper;

import com.gridnode.pdip.app.alert.model.*;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.PasswordMask;

import junit.framework.*;

public class CreateAlertTriggerActionTest extends ActionTestHelper
{
  CreateAlertTriggerEvent[] _events;
  //Alert[]                   _alerts;

  public CreateAlertTriggerActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(CreateAlertTriggerActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }


  // *************** ActionTestHelper methods ****************************

  protected void cleanUp()
  {
    deleteAlerts();
    deleteAlertTriggers();
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

    INVALID_RECIPIENTS.add("EMAIL_ADDRESS:emaila@one.com");
    INVALID_RECIPIENTS.add("USER:admin");
    INVALID_RECIPIENTS.add("ROLE:User");
    INVALID_RECIPIENTS.add("EMAIL_CODE:code1");
    INVALID_RECIPIENTS.add("EMAIL_CODE_XPATH:/EmailCodeRef/EmailCode/@id");
    //invalid
    INVALID_RECIPIENTS.add("EMAIL_CODE=code1");

    createSessions(1);
    createStateMachines(1);

    _events = new CreateAlertTriggerEvent[]
              {
                //accepted: 0-4
                createTestEvent(AlertTrigger.LEVEL_0, ALERT_TYPE,
                  null, null, null, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent(AlertTrigger.LEVEL_1, ALERT_TYPE,
                  DOC_TYPE, null, null, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, RECIPIENTS),
                createTestEvent(AlertTrigger.LEVEL_2, ALERT_TYPE,
                  DOC_TYPE, PARTNER_TYPE, null, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent(AlertTrigger.LEVEL_3, ALERT_TYPE,
                  DOC_TYPE, PARTNER_TYPE, PARTNER_GROUP, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent(AlertTrigger.LEVEL_4, ALERT_TYPE,
                  DOC_TYPE, null, null, PARTNER_ID, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),

                //rejected: 5-21
                createTestEvent(INVALID_LEVEL, ALERT_TYPE,
                  null, null, null, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),

                // level 0: 6-8
                createTestEvent(AlertTrigger.LEVEL_0, ALERT_TYPE,
                  null, null, null, null, INVALID_UID, Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent(AlertTrigger.LEVEL_0, INVALID_ALERT_TYPE,
                  null, null, null, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent(AlertTrigger.LEVEL_0, ALERT_TYPE,
                  null, null, null, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, INVALID_RECIPIENTS),

                // level 1: 9
                createTestEvent(AlertTrigger.LEVEL_1, ALERT_TYPE,
                  null, null, null, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),

                // level 2: 10-11
                createTestEvent(AlertTrigger.LEVEL_2, ALERT_TYPE,
                  "", PARTNER_TYPE, null, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent(AlertTrigger.LEVEL_2, ALERT_TYPE,
                  DOC_TYPE, "", null, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),

                // level 3: 12-14
                createTestEvent(AlertTrigger.LEVEL_3, ALERT_TYPE,
                  "", PARTNER_TYPE, PARTNER_GROUP, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent(AlertTrigger.LEVEL_3, ALERT_TYPE,
                  DOC_TYPE, "", PARTNER_GROUP, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent(AlertTrigger.LEVEL_3, ALERT_TYPE,
                  DOC_TYPE, PARTNER_TYPE, "", null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),

                // level 4: 15-16
                createTestEvent(AlertTrigger.LEVEL_4, ALERT_TYPE,
                  "", null, null, PARTNER_ID, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent(AlertTrigger.LEVEL_4, ALERT_TYPE,
                  DOC_TYPE, null, null, "", alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),

                // duplicate: 17-21
                createTestEvent(AlertTrigger.LEVEL_0, ALERT_TYPE,
                  null, null, null, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent(AlertTrigger.LEVEL_1, ALERT_TYPE,
                  DOC_TYPE, null, null, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, RECIPIENTS),
                createTestEvent(AlertTrigger.LEVEL_2, ALERT_TYPE,
                  DOC_TYPE, PARTNER_TYPE, null, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent(AlertTrigger.LEVEL_3, ALERT_TYPE,
                  DOC_TYPE, PARTNER_TYPE, PARTNER_GROUP, null, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),
                createTestEvent(AlertTrigger.LEVEL_4, ALERT_TYPE,
                  DOC_TYPE, null, null, PARTNER_ID, alertUIDs[0], Boolean.TRUE, Boolean.FALSE, null),

              };
  }

  protected void unitTest() throws Exception
  {
    // ************** ACCEPTED *****************************
    //level 0
    createCheckSuccess(_events[0], _sessions[0], _sm[0]);
    //level 1
    createCheckSuccess(_events[1], _sessions[0], _sm[0]);
    //level 2
    createCheckSuccess(_events[2], _sessions[0], _sm[0]);
    //level 3
    createCheckSuccess(_events[3], _sessions[0], _sm[0]);
    //level 4
    createCheckSuccess(_events[4], _sessions[0], _sm[0]);

    // ************** REJECTED ***************************
    //invalid level
    //createCheckFail(_events[5], _sessions[0], _sm[0], false);

    //Level 0: invalid alert uid
    createCheckFail(_events[6], _sessions[0], _sm[0], true);

    //Level 0: invalid alert type
    createCheckFail(_events[7], _sessions[0], _sm[0], true);

    //Level 0: invalid recipient format
    createCheckFail(_events[8], _sessions[0], _sm[0], false);

    //Level 1: missing doc type
    createCheckFail(_events[9], _sessions[0], _sm[0], false);

    //Level 2: missing doc type
    createCheckFail(_events[10], _sessions[0], _sm[0], false);

    //Level 2: missing partner type
    createCheckFail(_events[11], _sessions[0], _sm[0], false);

    //Level 3: missing doc type
    createCheckFail(_events[12], _sessions[0], _sm[0], false);

    //Level 3: missing partner type
    createCheckFail(_events[13], _sessions[0], _sm[0], false);

    //Level 3: missing partner group
    createCheckFail(_events[14], _sessions[0], _sm[0], false);

    //Level 4: missing doc type
    createCheckFail(_events[15], _sessions[0], _sm[0], false);

    //Level 4: missing partner id
    createCheckFail(_events[16], _sessions[0], _sm[0], false);

    //existing Level 0
    createCheckFail(_events[17], _sessions[0], _sm[0], false);
    //existing Level 1
    createCheckFail(_events[18], _sessions[0], _sm[0], false);
    //existing Level 2
    createCheckFail(_events[19], _sessions[0], _sm[0], false);
    //existing Level 3
    createCheckFail(_events[20], _sessions[0], _sm[0], false);
    //existing Level 4
    createCheckFail(_events[21], _sessions[0], _sm[0], false);

  }

  protected IEJBAction createNewAction()
  {
    return new CreateAlertTriggerAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    CreateAlertTriggerEvent createEvent = (CreateAlertTriggerEvent)event;

    AlertTrigger trigger = findAlertTrigger(createEvent);
    assertNotNull("AlertTrigger not created", trigger);

    assertEquals("Alert UID is incorrect", createEvent.getAlertUID(), trigger.getAlertUID());
    assertEquals("isEnabled is incorrect", createEvent.getIsEnabled(), new Boolean(trigger.isEnabled()));
    assertEquals("isAttachDoc is incorrect", createEvent.getIsAttachDoc(), new Boolean(trigger.isAttachDoc()));

    if (createEvent.getRecipients() == null)
      assertTrue("Recipients is incorrect", trigger.getRecipients().isEmpty());
    else
      assertEquals("Recipients is incorrect", createEvent.getRecipients(), trigger.getRecipients());
  }

  // ****************** Utility methods ****************************

  private void deleteAlertTriggers()
  {
    deleteAlertTrigger(AlertTrigger.LEVEL_0, ALERT_TYPE, null, null, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_1, ALERT_TYPE, DOC_TYPE, null, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_2, ALERT_TYPE, DOC_TYPE, PARTNER_TYPE, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_3, ALERT_TYPE, DOC_TYPE, PARTNER_TYPE, PARTNER_GROUP, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_4, ALERT_TYPE, DOC_TYPE, null, null, PARTNER_ID);
  }

  private AlertTrigger findAlertTrigger(CreateAlertTriggerEvent event)
  {
    AlertTrigger trigger = null;
    try
    {
      trigger = _gtAlertMgr.findAlertTrigger(event.getLevel(), event.getAlertType(),
               event.getDocType(), event.getPartnerType(), event.getPartnerGroup(),
               event.getPartnerID());
    }
    catch (Exception ex)
    {
      Logger.err("AlertTrigger not found", ex);
    }
    return trigger;
  }

  private CreateAlertTriggerEvent createTestEvent(
    Integer level,
    String alertType,
    String docType,
    String partnerType,
    String partnerGroup,
    String partnerId,
    Long   alertUID,
    Boolean isEnabled,
    Boolean isAttachDoc,
    Collection recipients) throws Exception
  {
    return new CreateAlertTriggerEvent(level, alertType, docType, partnerType,
                 partnerGroup, partnerId, alertUID, isEnabled, isAttachDoc,
                 recipients);
  }

  private void createCheckFail(
    CreateAlertTriggerEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.CREATE_ENTITY_ERROR);
  }

  private void createCheckSuccess(
    CreateAlertTriggerEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }
}