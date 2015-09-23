/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionTestHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 23 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.alert.helpers;

import com.gridnode.gtas.server.alert.model.AlertTrigger;
import com.gridnode.gtas.server.alert.facade.ejb.IGridTalkAlertManagerObj;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.pdip.app.alert.model.*;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

import junit.framework.TestCase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
 import java.util.List;

public abstract class ActionTestHelper extends TestCase
{
  protected static final String ALERT_TYPE    = "DOC_RECV";
  protected static final String DOC_TYPE      = "UC";
  protected static final String PARTNER_TYPE  = "PU";
  protected static final String PARTNER_GROUP = "LC";
  protected static final String PARTNER_ID    = "P507";
  protected static final List   RECIPIENTS    = new ArrayList();
  protected static final String ALERT_TYPE_2    = "DOC_RESP_RECV";
  protected static final String DOC_TYPE_2      = "PO";
  protected static final String PARTNER_TYPE_2  = "SU";
  protected static final String PARTNER_GROUP_2 = "OC";
  protected static final String PARTNER_ID_2    = "P508";

  protected static final Integer INVALID_LEVEL      = new Integer(-1);
  protected static final Long    INVALID_UID        = new Long(-999);
  protected static final String  INVALID_ALERT_TYPE = "DUMMY";
  protected static final List    INVALID_RECIPIENTS = new ArrayList();

  protected static final String ENTERPRISE = "888888";
  protected static final String USER_ID    = "testuserid";

  protected IAlertManagerObj _alertMgr;
  protected IGridTalkAlertManagerObj  _gtAlertMgr;
  protected ISessionManagerObj _sessionMgr;

  protected ArrayList _openedSessions = new ArrayList();

  protected String[]            _sessions;
  protected StateMachine[]      _sm;

  public ActionTestHelper(String name)
  {
    super(name);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.setUp] Enter");

      _alertMgr = ServiceLookupHelper.getAlertManager();
      _gtAlertMgr  = ServiceLookupHelper.getGridTalkAlertMgr();
      _sessionMgr = getSessionMgr();

      cleanUp();
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.setUp] Exit");
    }
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[ActionTestHelper.tearDown] Enter");
    cleanUp();
    Log.log("TEST", "[ActionTestHelper.tearDown] Exit");
  }

  protected void createSessions(int numSessions) throws Exception
  {
    _sessions = new String[numSessions];
    for (int i=0; i<numSessions; i++)
      _sessions[i] = openSession();
  }

  protected void createStateMachines(int numSM)
  {
    _sm = new StateMachine[numSM];
    for (int i=0; i<numSM; i++)
      _sm[i] = new StateMachine(null, null);
  }

  public void testPerform() throws Exception
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.testPerform] Enter ");
      prepareTestData();

      unitTest();

      cleanTestData();
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.testPerform] Exit ");
    }

  }

  protected ISessionManagerObj getSessionMgr() throws Exception
  {
    ISessionManagerHome sessionHome = (ISessionManagerHome)ServiceLocator.instance(
                                         ServiceLocator.CLIENT_CONTEXT).getHome(
                                         ISessionManagerHome.class.getName(),
                                         ISessionManagerHome.class);
    return sessionHome.create();
  }

  protected void deleteAlertTrigger(Integer level, String alertType,
    String docType, String partnerType, String partnerGroup, String partnerId)
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.deleteAlertTrigger] Enter");

      AlertTrigger trigger = _gtAlertMgr.findAlertTrigger(level, alertType,
                               docType, partnerType, partnerGroup, partnerId);
      if (trigger != null)
      {
        _gtAlertMgr.deleteAlertTrigger((Long)trigger.getKey());
      }
    }
    catch (Exception ex)
    {
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.deleteAlertTrigger] Exit");
    }
  }

  protected AlertTrigger createAlertTrigger(Integer level, String alertType,
    String docType, String partnerType, String partnerGroup, String partnerId,
    Long alertUID, boolean isEnabled, boolean isAttachDoc, List recipients)
    throws Exception
  {
    AlertTrigger trigger = new AlertTrigger();
    trigger.setAlertType(alertType);
    trigger.setAlertUID(alertUID);
    trigger.setAttachDoc(isAttachDoc);
    trigger.setDocumentType(docType);
    trigger.setEnabled(isEnabled);
    trigger.setLevel(level.intValue());
    trigger.setPartnerGroup(partnerGroup);
    trigger.setPartnerId(partnerId);
    trigger.setPartnerType(partnerType);
    if (recipients != null)
      trigger.setRecipients(recipients);

    try
    {
      Log.log("TEST", "[ActionTestHelper.createAlertTrigger] Enter");

      Long uid = _gtAlertMgr.createAlertTrigger(trigger);
      trigger =  _gtAlertMgr.findAlertTrigger(uid);
    }
    finally
    {
      Log.log("TEST", "[ActionTestHelper.createAlertTrigger] Exit");
    }

    return trigger;
  }

  protected AlertTrigger findAlertTrigger(Long uid)
  {
    AlertTrigger trigger = null;
    try
    {
      trigger = _gtAlertMgr.findAlertTrigger(uid);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.findAlertTrigger] ", ex);
    }

    return trigger;
  }

  protected String openSession() throws Exception
  {
    String session = _sessionMgr.openSession();
    _sessionMgr.authSession(session, USER_ID);
    _openedSessions.add(session);
    return session;
  }

  protected void closeSession(String sessionId) throws Exception
  {
    _sessionMgr.closeSession(sessionId);
    _openedSessions.remove(sessionId);
  }

  protected void closeAllSessions() throws Exception
  {
    String[] sessions = (String[])_openedSessions.toArray(new String[0]);
    for (int i=0; i<sessions.length; i++)
      closeSession(sessions[i]);
  }

  protected void purgeSessions()
  {
    try
    {
      _sessionMgr.deleteSessions(new Date());
    }
    catch (Exception ex)
    {

    }

  }

  protected BasicEventResponse performEvent(
    IEJBAction action, IEvent event, String session, StateMachine sm)
    throws Exception
  {
    sm.setAttribute(IAttributeKeys.SESSION_ID, session);
    sm.setAttribute(IAttributeKeys.USER_ID, USER_ID);
    sm.setAttribute(IAttributeKeys.ENTERPRISE_ID, ENTERPRISE);

    action.init(sm);
    action.doStart();
    action.validateEvent(event);
    BasicEventResponse response = (BasicEventResponse) action.perform(event);
    action.doEnd();
    return response;
  }

  protected void assertResponseFail(BasicEventResponse response, short msgCode)
  {
    //check response
    assertNotNull("response is null", response);
    assertTrue("event status is incorrect", !response.isEventSuccessful());
    assertEquals("Msg code incorrect", msgCode, response.getMessageCode());
    assertNotNull("Error reason is null", response.getErrorReason());
    assertEquals("Error type is incorrect",
      ApplicationException.APPLICATION, response.getErrorType());
  }

  protected void assertResponsePass(BasicEventResponse response, short msgCode)
  {
    assertNotNull("response is null", response);
    assertTrue("event is not successful", response.isEventSuccessful());
    assertEquals("Msg code incorrect", msgCode, response.getMessageCode());
  }

  protected void checkFail(
    IEvent event, String session, StateMachine sm, boolean eventEx,
    short msgCode)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(createNewAction(), event, session, sm);
    }
    catch (EventException ex)
    {
      Log.log("TEST", "[ActionTestHelper.checkFail]" +
        " Returning fail due to EventException: "+ex.getMessage());
      if (!eventEx)
        assertTrue("Unexpected event exception caught: "+ex.getMessage(), false);

      Log.log("TEST", "[ActionTestHelper.checkFail] Exit ");
      return;
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.checkFail] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponseFail(response, msgCode);
  }

  protected void checkSuccess(
    IEvent event, String session, StateMachine sm, short msgCode)
  {
    BasicEventResponse response = null;
    try
    {
      response = performEvent(createNewAction(), event, session, sm);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ActionTestHelper.checkSuccess] Error Exit", ex);
      assertTrue("Event Exception", false);
    }

    assertResponsePass(response, msgCode);

    checkActionEffect(response, event, sm);
  }

  protected Long[] createAlerts() throws Exception
  {
    // message template x 2
    Long recRespMsgUId = createRecResponseMessageTemplate();

    // action, alert, alert action x2
    Long recRespActionUId = createAction("RECV_CONF_ACTION", "Send Confirmation Notification", recRespMsgUId);
    Long recRespAlertUId = createAlert(ALERT_TYPE, "Receive Order Confirmation", new Long(2));

    createAlertAction(recRespAlertUId, recRespActionUId);
    return new Long[]{recRespAlertUId};
  }

  protected void deleteAlerts()
  {
    deleteAlertAction(ALERT_TYPE);
    deleteAlert(ALERT_TYPE);
    deleteAction("RECV_CONF_ACTION");
    deleteRecResponseMessageTemplate();
  }

  private Long createRecResponseMessageTemplate()  throws Exception
  {
    MessageTemplate msg = new MessageTemplate();

    msg.setContentType(msg.CONTENT_TYPE_TEXT);
    msg.setFromAddr("<#USER=admin#>");
    StringBuffer buff= new StringBuffer("Dear Buyer,\n");
    buff.append("\tYou have received an order confirmation for doc. no ");
    buff.append("<%UserDocument.XPATH:/POR/PONumber%> from Vendor code ");
    buff.append("<%UserDocument.XPATH:/POR/VendorCode%> on ");
    buff.append("<%GridDocument.DT_CREATE%>.\n\nGridTalk Administrator");
    msg.setMessage(buff.toString());
    msg.setMessageType(msg.MSG_TYPE_EMAIL);
    msg.setName("Receive Confirmation");
    msg.setSubject("Purchase Order Confirmation");
    msg.setToAddr("<%UserDocument.EMAIL_CODE_RECIPIENTS%>");

    return _alertMgr.createMessageTemplate(msg);
  }

  private void deleteRecResponseMessageTemplate()
  {
    try
    {
      MessageTemplate msg = _alertMgr.getMessageTemplateByName("Receive Confirmation");
      if (msg != null)
        _alertMgr.deleteMessageTemplate((Long)msg.getKey());
    }
    catch (Exception ex)
    {

    }
  }

  private Long createAction(String name, String desc, Long msgTemplate)  throws Exception
  {
    Action action = new Action();
    action.setName(name);
    action.setDescr(desc);
    action.setMsgUid(msgTemplate);

    return _alertMgr.createAction(action);
  }

  private void deleteAction(String name)
  {
    try
    {
      Action action = _alertMgr.getActionByActionName(name);
      if (action != null)
        _alertMgr.deleteAction((Long)action.getKey());
    }
    catch (Exception ex)
    {

    }
  }

  private Long createAlert(String alertName, String desc, Long alertType)  throws Exception
  {
    Alert alert = new Alert();
    alert.setDescr(desc);
    alert.setName(alertName);
    alert.setAlertType(alertType);

    return _alertMgr.createAlert(alert);
  }

  private void deleteAlert(String name)
  {
    try
    {
      Alert alert = _alertMgr.getAlertByAlertName(name);
      if (alert != null)
        _alertMgr.deleteAlert((Long)alert.getKey());
    }
    catch (Exception ex)
    {

    }
  }

  private Long createAlertAction(Long alertUId, Long actionUId)  throws Exception
  {
    AlertAction alertAction = new AlertAction();
    alertAction.setActionUid(actionUId);
    alertAction.setAlertUid(alertUId);

    return _alertMgr.createAlertAction(alertAction);
  }

  private void deleteAlertAction(String alertName)
  {
    try
    {
      Alert alert = _alertMgr.getAlertByAlertName(alertName);
      if (alert != null)
      {
        Collection result = _alertMgr.getAlertActionsByAlertUId((Long)alert.getKey());
        for (Iterator i=result.iterator(); i.hasNext(); )
        {
          AlertAction alertAction = (AlertAction)i.next();
          _alertMgr.deleteAction((Long)alertAction.getKey());
        }
      }
    }
    catch (Exception ex)
    {

    }
  }

  protected Long[] getUIDs(IEntity[] entities)
  {
    Long[] uIDs = new Long[entities.length];
    for (int i=0; i<uIDs.length; i++)
      uIDs[i] = (Long)entities[i].getKey();

    return uIDs;
  }

  protected abstract void cleanUp();
  protected abstract void prepareTestData() throws Exception;
  protected abstract void cleanTestData() throws Exception;
  protected abstract void unitTest() throws Exception;
  protected abstract IEJBAction createNewAction();
  protected abstract void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm);
}