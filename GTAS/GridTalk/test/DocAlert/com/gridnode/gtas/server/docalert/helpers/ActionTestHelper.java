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
 * Feb 05 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.helpers;

import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.docalert.facade.ejb.IDocAlertManagerHome;
import com.gridnode.gtas.server.docalert.facade.ejb.IDocAlertManagerObj;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerHome;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerObj;
import com.gridnode.pdip.base.session.facade.ejb.ISessionManagerHome;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
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
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This helper class provides helper methods for use in the Action Test cases
 * of the DocAlert module.<p>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */

public abstract class ActionTestHelper extends TestCase
{
  protected static final Long   DUMMY_UID      = new Long(-9999);
  protected static final String DUMMY_STRING   = "XXX";

  protected ISessionManagerObj _sessionMgr;

  protected ArrayList _openedSessions = new ArrayList();
  protected static final String APPLICATION = "gridtalk";
  protected static final String ENTERPRISE = "521";
  protected static final String USER_ID   = "admin";

  protected String[]            _sessions;
  protected StateMachine[]      _sm;
  protected Long[]              _uIDs;

  public ActionTestHelper(String name)
  {
    super(name);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.log("TEST", "[ActionTestHelper.setUp] Enter");

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
//    cleanUp();
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
    Log.log("TEST", "[ActionTestHelper.testPerform] Enter ");
    prepareTestData();

    try
    {
      unitTest();
    }
    finally
    {
      cleanTestData();
      Log.log("TEST", "[ActionTestHelper.testPerform] Exit ");
    }
  }

  protected IDocumentManagerObj getDocMgr() throws Exception
  {
    return (IDocumentManagerObj)ServiceLocator.instance(
                             ServiceLocator.CLIENT_CONTEXT).getObj(
                             IDocumentManagerHome.class.getName(),
                             IDocumentManagerHome.class,
                             new Object[0]);
  }

  protected IDocAlertManagerObj getDocAlertMgr() throws Exception
  {
    return (IDocAlertManagerObj)ServiceLocator.instance(
                             ServiceLocator.CLIENT_CONTEXT).getObj(
                             IDocAlertManagerHome.class.getName(),
                             IDocAlertManagerHome.class,
                             new Object[0]);
  }

  protected IAlertManagerObj getAlertMgr() throws Exception
  {
    return (IAlertManagerObj)ServiceLocator.instance(
                             ServiceLocator.CLIENT_CONTEXT).getObj(
                             IAlertManagerHome.class.getName(),
                             IAlertManagerHome.class,
                             new Object[0]);
  }

  protected ISessionManagerObj getSessionMgr() throws Exception
  {
    ISessionManagerHome sessionHome = (ISessionManagerHome)ServiceLocator.instance(
                                         ServiceLocator.CLIENT_CONTEXT).getHome(
                                         ISessionManagerHome.class.getName(),
                                         ISessionManagerHome.class);
    return sessionHome.create();
  }

  protected String openSession() throws Exception
  {
    String session = _sessionMgr.openSession();
    _openedSessions.add(session);
    _sessionMgr.authSession(session, USER_ID);
    return session;
  }

  protected void closeSession(String sessionId) throws Exception
  {
    _sessionMgr.closeSession(sessionId);
    _openedSessions.remove(sessionId);
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

  protected void closeAllSessions() throws Exception
  {
    String[] sessions = (String[])_openedSessions.toArray(new String[0]);
    for (int i=0; i<sessions.length; i++)
      closeSession(sessions[i]);
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

  protected void checkDate(Date expDate, Date date, String prefix)
  {
    if (expDate == null)
    {
      assertNull(prefix+"is not null", date);
      return;
    }
    GregorianCalendar cal1 = new GregorianCalendar();
    GregorianCalendar cal2 = new GregorianCalendar();

    cal1.setTime(expDate);
    cal2.setTime(date);

    assertEquals(prefix+" Year is incorrect", cal1.get(cal1.YEAR), cal2.get(cal2.YEAR));
    assertEquals(prefix+" Month is incorrect", cal1.get(cal1.MONTH), cal2.get(cal2.MONTH));
    assertEquals(prefix+" Day is incorrect", cal1.get(cal1.DATE), cal2.get(cal2.DATE));
  }

  protected abstract void cleanUp();
  protected abstract void prepareTestData() throws Exception;
  protected abstract void cleanTestData() throws Exception;
  protected abstract void unitTest() throws Exception;
  protected abstract IEJBAction createNewAction();
  protected abstract void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm);
}