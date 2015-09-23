/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetAlertTriggerListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 23 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.actions;

import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.gtas.model.alert.IAlertTrigger;
import com.gridnode.gtas.events.alert.GetAlertTriggerListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.server.alert.helpers.ActionTestHelper;

import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import junit.framework.*;

import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This Test case tests the GetAlertTriggerListAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class GetAlertTriggerListActionTest extends ActionTestHelper
{
  private static final int NUM_ROWS = 10;
  private static final int MAX_ROWS_PER_PAGE = 4;
  private static final int START_ROW = 3;

  private static final ArrayList TEST_UIDS = new ArrayList();
  private static final DataFilterImpl FILTER = new DataFilterImpl();

  GetAlertTriggerListEvent[] _events;
  AlertTrigger[]             _triggers;
  String _listID;

  public GetAlertTriggerListActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetAlertTriggerListActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ***************** ActionTestHelper methods *************************

  protected void cleanUp()
  {
    deleteAlertTriggers();
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void prepareTestData() throws Exception
  {
    createAlertTriggers();

    for (int i=0; i<NUM_ROWS; i++)
      TEST_UIDS.add(new Long(_triggers[i].getUId()));

    FILTER.addDomainFilter(null, AlertTrigger.UID, TEST_UIDS, false);
    FILTER.setOrderFields(new Number[]{AlertTrigger.UID});

    createSessions(1);
    createStateMachines(1);

    _events = new GetAlertTriggerListEvent[]
              {
                //accepted
                createTestEvent(FILTER),
                createTestEvent(FILTER, MAX_ROWS_PER_PAGE),
                createTestEvent(FILTER, MAX_ROWS_PER_PAGE, START_ROW),
                createTestEvent(FILTER, MAX_ROWS_PER_PAGE, 0),
              };
  }

  protected void unitTest() throws Exception
  {
    // ************** ACCEPTED *****************************
    getCheckSuccess(_events[0], _sessions[0], _sm[0]);
    getCheckSuccess(_events[1], _sessions[0], _sm[0]);
    getCheckSuccess(_events[2], _sessions[0], _sm[0]);

    getCheckSuccess(_events[3], _sessions[0], _sm[0]);
    for (int i=0; i<NUM_ROWS; i+=MAX_ROWS_PER_PAGE)
    {
      GetAlertTriggerListEvent getEvent =
        new GetAlertTriggerListEvent(_listID, MAX_ROWS_PER_PAGE, i);
        getCheckSuccess(getEvent, _sessions[0], _sm[0]);
    }
  }

  protected IEJBAction createNewAction()
  {
    return new GetAlertTriggerListAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    GetAlertTriggerListEvent getEvent = (GetAlertTriggerListEvent)event;
    assertNotNull("responsedata is null", response.getReturnData());
    assertTrue("response data type incorrect", response.getReturnData() instanceof EntityListResponseData);

    EntityListResponseData listData = (EntityListResponseData) response.getReturnData();

    int expStart = getEvent.getStartRow();
    int expRows = getEvent.getMaxRows() > 0 ?
                  (NUM_ROWS-expStart >= getEvent.getMaxRows() ?
                   getEvent.getMaxRows() :
                   NUM_ROWS-expStart) :
                  NUM_ROWS;

    int remainingRows = NUM_ROWS - expStart - expRows;
    assertEquals("Rows remaining is incorrect", remainingRows, listData.getRowsRemaining());
    assertEquals("start row is incorrect", expStart, listData.getStartRow());
    assertNotNull("List ID is null", listData.getListID());
    _listID = listData.getListID();

    Collection entityList = listData.getEntityList();
    assertNotNull("Entity list is null", entityList);

    int numPage = (int)Math.ceil((double) (NUM_ROWS - expStart) / (double)MAX_ROWS_PER_PAGE);
    assertEquals("Entity list count is incorrect", expRows, entityList.size());

    Object[] entityObjs = entityList.toArray();
    for (int i = 0; i < expRows; i++ )
    {
      checkAlertTrigger(entityObjs[i], _triggers[expStart + i]);
    }
  }

  // ******************* Own methods ****************************

  private void createAlertTriggers() throws Exception
  {
    Long[] alertUIDs = createAlerts();

    RECIPIENTS.add("EMAIL_ADDRESS:emaila@one.com");
    RECIPIENTS.add("USER:admin");
    RECIPIENTS.add("ROLE:User");
    RECIPIENTS.add("EMAIL_CODE:code1");
    RECIPIENTS.add("EMAIL_CODE_XPATH:/EmailCodeRef/EmailCode/@id");

    _triggers = new AlertTrigger[10];
    _triggers[0] = createAlertTrigger(AlertTrigger.LEVEL_0, ALERT_TYPE,
                     null, null, null, null, alertUIDs[0], true, false, null);
    _triggers[1] = createAlertTrigger(AlertTrigger.LEVEL_1, ALERT_TYPE,
                     DOC_TYPE, null, null, null, alertUIDs[0], true, false, null);
    _triggers[2] = createAlertTrigger(AlertTrigger.LEVEL_2, ALERT_TYPE,
                     DOC_TYPE, PARTNER_TYPE, null, null, alertUIDs[0], true, false, null);
    _triggers[3] = createAlertTrigger(AlertTrigger.LEVEL_3, ALERT_TYPE,
                     DOC_TYPE, PARTNER_TYPE, PARTNER_GROUP, null, alertUIDs[0], true, false, null);
    _triggers[4] = createAlertTrigger(AlertTrigger.LEVEL_4, ALERT_TYPE,
                     DOC_TYPE, null, null, PARTNER_ID, alertUIDs[0], true, false, null);
    _triggers[5] = createAlertTrigger(AlertTrigger.LEVEL_0, ALERT_TYPE_2,
                     null, null, null, null, alertUIDs[0], true, false, RECIPIENTS);
    _triggers[6] = createAlertTrigger(AlertTrigger.LEVEL_1, ALERT_TYPE_2,
                     DOC_TYPE, null, null, null, alertUIDs[0], true, false, RECIPIENTS);
    _triggers[7] = createAlertTrigger(AlertTrigger.LEVEL_2, ALERT_TYPE_2,
                     DOC_TYPE, PARTNER_TYPE, null, null, alertUIDs[0], true, false, RECIPIENTS);
    _triggers[8] = createAlertTrigger(AlertTrigger.LEVEL_3, ALERT_TYPE_2,
                     DOC_TYPE, PARTNER_TYPE, PARTNER_GROUP, null, alertUIDs[0], true, false, RECIPIENTS);
    _triggers[9] = createAlertTrigger(AlertTrigger.LEVEL_4, ALERT_TYPE_2,
                     DOC_TYPE, null, null, PARTNER_ID, alertUIDs[0], true, false, RECIPIENTS);
  }

  private void deleteAlertTriggers()
  {
    deleteAlertTrigger(AlertTrigger.LEVEL_0, ALERT_TYPE, null, null, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_1, ALERT_TYPE, DOC_TYPE, null, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_2, ALERT_TYPE, DOC_TYPE, PARTNER_TYPE, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_3, ALERT_TYPE, DOC_TYPE, PARTNER_TYPE, PARTNER_GROUP, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_4, ALERT_TYPE, DOC_TYPE, null, null, PARTNER_ID);
    deleteAlertTrigger(AlertTrigger.LEVEL_0, ALERT_TYPE_2, null, null, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_1, ALERT_TYPE_2, DOC_TYPE, null, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_2, ALERT_TYPE_2, DOC_TYPE, PARTNER_TYPE, null, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_3, ALERT_TYPE_2, DOC_TYPE, PARTNER_TYPE, PARTNER_GROUP, null);
    deleteAlertTrigger(AlertTrigger.LEVEL_4, ALERT_TYPE_2, DOC_TYPE, null, null, PARTNER_ID);

    deleteAlerts();

  }

  private GetAlertTriggerListEvent createTestEvent(IDataFilter filter)
  {
    return new GetAlertTriggerListEvent(filter);
  }

  private GetAlertTriggerListEvent createTestEvent(
    IDataFilter filter, int maxRows)
  {
    return new GetAlertTriggerListEvent(filter, maxRows);
  }

  private GetAlertTriggerListEvent createTestEvent(
    IDataFilter filter, int maxRows, int startRow)
  {
    return new GetAlertTriggerListEvent(filter, maxRows, startRow);
  }

  private GetAlertTriggerListEvent createTestEvent(
    String listID, int maxRows, int startRow) throws Exception
  {
    return new GetAlertTriggerListEvent(listID, maxRows, startRow);
  }

  private void checkAlertTrigger(Object entityObj, AlertTrigger trigger)
  {
    assertNotNull("responsedata is null", entityObj);
    assertTrue("response data type incorrect", entityObj instanceof Map);

    Map triggerMap = (Map)entityObj;

    assertEquals("UID is incorrect", trigger.getKey(), triggerMap.get(IAlertTrigger.UID));
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

  private void getCheckFail(
    GetAlertTriggerListEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_LIST_ERROR);
  }

  private void getCheckSuccess(
    GetAlertTriggerListEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

}