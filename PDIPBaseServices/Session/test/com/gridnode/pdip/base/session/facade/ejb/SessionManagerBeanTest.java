/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SessionManagerBeanTest.java
 *se
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 15 2002    Ooi Hui Linn         Created
 * Jun 05 2002    Neo Sok Lay          Changes to getSessionData(), setSessionData(),
 *                                     removeSessionData().
 * Jun 11 2002    Ooi Hui Linn         Add test for deleteSessions()
 */
package com.gridnode.pdip.base.session.facade.ejb;

import java.util.Date;
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gridnode.pdip.base.session.exceptions.ClosedSessionException;
import com.gridnode.pdip.base.session.exceptions.InvalidAuthSubjectException;
import com.gridnode.pdip.base.session.model.SessionData;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;

public class SessionManagerBeanTest extends TestCase
{
  ISessionManagerHome home;
  ISessionManagerObj  remote;
  String sessionId;

  public SessionManagerBeanTest(String name)
  {
      super(name);
  }

  public static Test suite()
  {
    return new TestSuite(SessionManagerBeanTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void setUp() throws Exception
  {
    home = (ISessionManagerHome) ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(ISessionManagerHome.class);
    assertNotNull("ISessionManagerHome is null", home);
    remote = home.create();
    assertNotNull("ISessionManagerObj is null", remote);
    remote.closeInactiveSessions(0);
  }

  protected void tearDown()
  {
//    remote.remove();
    try
    {
      remote.closeInactiveSessions(0);
      remote.deleteSessions(new Date());
    }
    catch (Exception ex)
    {
    }
  }

  public void testOpenSession() throws Exception
  {
    sessionId = remote.openSession();
    assertNotNull(sessionId);
    cleanUpSession(sessionId);
  }

  public void testGetAllSessionData() throws Exception
  {
    sessionId = remote.openSession();
    assertNotNull(sessionId);
    HashMap sessionData = remote.getAllSessionData(sessionId);
    assertNotNull("Session not found: "+sessionId, sessionData);
    System.out.println("Session data is "+ sessionData);
    cleanUpSession(sessionId);
  }

  //testActivitesWithInvalidSession
  //testActivitesWithClosedOrExpiredSession

  public void testSetSessionData() throws Exception
  {
    String key_1 = "key_1";
    String value_1 = "value_1";
    String update_value_1 = "update_value_1";

    sessionId = remote.openSession();
    assertNotNull(sessionId);

    //Valid Session Data (new session data key)
    assertTrue(remote.setSessionData(sessionId, new SessionData(key_1, value_1)));
    assertEquals(value_1, (String) remote.getSessionData(sessionId, key_1).getData());
    System.out.println("Session data is "+ remote.getAllSessionData(sessionId));

    //Valid Session Data (update existing session data key with new value)
    assertTrue(remote.setSessionData(sessionId, new SessionData(key_1, update_value_1)));
    assertEquals(update_value_1, (String) remote.getSessionData(sessionId, key_1).getData());
    System.out.println("Session data is "+ remote.getAllSessionData(sessionId));

    //Valid Session Data value=null
    assertTrue(remote.setSessionData(sessionId, new SessionData(key_1, null)));
    assertNull(remote.getSessionData(sessionId, key_1).getData());
    System.out.println("Session data is "+ remote.getAllSessionData(sessionId));

    //Valid Session Data key=null
    assertTrue(remote.setSessionData(sessionId, new SessionData(null, null)));
    System.out.println("Session data is "+ remote.getAllSessionData(sessionId));

    //Valid Session Data key=null
    assertTrue(remote.setSessionData(sessionId, new SessionData(null, "valid data")));
    System.out.println("Session data is "+ remote.getAllSessionData(sessionId));

    cleanUpSession(sessionId);
  }

  public void testGetSessionData() throws Exception
  {
    String key_1 = "key_1";
    String value_1 = "value_1";
    String update_value_1 = "update_value_1";
    String valid_data = "valid data";

    sessionId = remote.openSession();
    assertNotNull(sessionId);

    //Valid Session Data (new session data key)
    assertTrue(remote.setSessionData(sessionId, new SessionData(key_1, value_1)));
    assertEquals(value_1, (String) remote.getSessionData(sessionId, key_1).getData());

    //Valid Session Data (update existing session data key with new value)
    assertTrue(remote.setSessionData(sessionId, new SessionData(key_1, update_value_1)));
    assertEquals(update_value_1, (String) remote.getSessionData(sessionId, key_1).getData());

    //Valid Session Data value=null
    assertTrue(remote.setSessionData(sessionId, new SessionData(key_1, null)));
    assertNull(remote.getSessionData(sessionId, key_1).getData());

    //Valid Session Data key=null
    assertTrue(remote.setSessionData(sessionId, new SessionData(null, null)));
    assertNull(remote.getSessionData(sessionId, null).getData());

    //Valid Session Data key=null
    assertTrue(remote.setSessionData(sessionId, new SessionData(null, valid_data)));
    assertEquals(valid_data, (String) remote.getSessionData(sessionId, null).getData());

    cleanUpSession(sessionId);
  }

  public void testRemoveSessionData() throws Exception
  {
    String key_1 = "key_1";
    String value_1 = "value_1";
    String valid_data = "valid data";

    sessionId = remote.openSession();
    assertNotNull(sessionId);

    //Valid Session Data (new session data key)
    assertTrue(remote.setSessionData(sessionId, new SessionData(key_1, value_1)));
    assertEquals(value_1, (String) remote.getSessionData(sessionId, key_1).getData());
    assertEquals(value_1, (String) remote.removeSessionData(sessionId, key_1).getData());
    //System.out.println("Session data is "+ remote.getAllSessionData(sessionId));
    assertNull(remote.getSessionData(sessionId, key_1).getData());

    //Valid Session Data key=null
    assertTrue(remote.setSessionData(sessionId, new SessionData(null, valid_data)));
    assertEquals(valid_data, (String) remote.getSessionData(sessionId, null).getData());
    assertEquals(valid_data, (String) remote.removeSessionData(sessionId, null).getData());
    assertNull(remote.getSessionData(sessionId, null).getData());

    cleanUpSession(sessionId);
  }

  public void testAuthSession() throws Exception
  {
    sessionId = remote.openSession();
    assertNotNull(sessionId);

    String authSubject = "Auth Subject or Session Name";
    String authSubject2 = "Refuse 2nd time if diff Subj";

    assertTrue(!remote.isAuthSession(sessionId)); //Not auth'ed yet

    //Test with invalid authSubject
    try
    {
      assertTrue(!remote.authSession(sessionId, null));
    }
    catch(InvalidAuthSubjectException ex)
    { //authSubject throws InvalidAuthSubjectException
    }
    try
    {
      assertTrue(!remote.authSession(sessionId, ""));
    }
    catch(InvalidAuthSubjectException ex)
    { //authSubject throws InvalidAuthSubjectException
    }

    assertTrue(remote.authSession(sessionId, authSubject));
    assertTrue(remote.isAuthSession(sessionId));
    assertTrue(!remote.authSession(sessionId, authSubject2)); //Already auth'ed. Don't accept new one.
    assertTrue(remote.authSession(sessionId, authSubject)); //Already auth'ed. Accept same one.
    assertEquals(authSubject, remote.getSessionAuthSubject(sessionId));

    cleanUpSession(sessionId);
  }

  public void testKeepActiveSession() throws Exception
  {
    sessionId = remote.openSession();
    assertNotNull(sessionId);

    //Simple test to keep active
    assertTrue(remote.keepActiveSession(sessionId));

    //Test keep active works successfully (session not expired)
    Thread.sleep(5*1000);  //wait for 10 secs
    assertTrue(remote.keepActiveSession(sessionId));
    assertEquals(0, remote.closeInactiveSessions(1*1000));

    //Simple test to keep active returns failure (session has already expired)
    remote.closeInactiveSessions(0);  //expire session
    assertTrue(!remote.keepActiveSession(sessionId));

    cleanUpSession(sessionId);
  }

  public void testSuspendSession() throws Exception
  {
    sessionId = remote.openSession();
    assertNotNull(sessionId);

    assertTrue(remote.suspendSession(sessionId, true));

    //test that SUSPENDED and AUTH'ED states do not conflict
    String authSubject = "SuspendAuthTest";
    assertTrue(remote.authSession(sessionId, authSubject));
    assertTrue(remote.suspendSession(sessionId, true));
    assertTrue("Suspend Session is changed the authenticated state", remote.isAuthSession(sessionId));

    //test that suspended session is not expired => becos suspended will not be expired
    remote.closeInactiveSessions(0);  //expire session
    assertTrue(remote.keepActiveSession(sessionId));

    cleanUpSession(sessionId);
  }

  public void testCloseSession() throws Exception
  {
    sessionId = remote.openSession();
    assertNotNull(sessionId);

    assertTrue(remote.closeSession(sessionId));
    try
    {
      assertNull("Session is not closed properly", remote.getAllSessionData(sessionId));
    }
    catch(ClosedSessionException ex)
    {
      //remote.getAllSessionData(sessionId) is to throw this ex when the session is closed.
    }
  }

  public void testCloseInactiveSession() throws Exception
  {
    //Clear all active sessions
    remote.closeInactiveSessions(0);

    String sessionId1 = remote.openSession();
    assertNotNull(sessionId1);
    String sessionId2 = remote.openSession();
    assertNotNull(sessionId2);
    String sessionId3 = remote.openSession();
    assertNotNull(sessionId3);
    String sessionId4 = remote.openSession();
    assertNotNull(sessionId4);
    remote.suspendSession(sessionId4, true);
    Thread.sleep(10*1000);  //wait for 10 secs

    assertTrue(remote.keepActiveSession(sessionId2));
    assertTrue(remote.authSession(sessionId3, "authUser"));

    Thread.sleep(10*1000);  //wait for 10 secs
    String sessionId5 = remote.openSession();
    assertNotNull(sessionId5);

    assertEquals(1, remote.closeInactiveSessions(15*1000));  //expire session 1
//    Thread.sleep(5*1000);  //wait for 10 secs
    assertEquals(2, remote.closeInactiveSessions(10*1000));  //expire session 2 & 3

    /** should not need to **/
//    cleanUpSession(sessionId2);
//    cleanUpSession(sessionId3);
    /** -- should not need to **/

    cleanUpSession(sessionId4);
    cleanUpSession(sessionId5);
  }

  public void testDeleteSessions() throws Exception
  {
    //Clear all active sessions
    remote.closeInactiveSessions(0);
    remote.deleteSessions(new Date());

    String sessionId1 = remote.openSession();
    assertNotNull(sessionId1);
    String sessionId2 = remote.openSession();
    assertNotNull(sessionId2);
    String sessionId3 = remote.openSession();
    assertNotNull(sessionId3);
    String sessionId4 = remote.openSession();
    assertNotNull(sessionId4);

    remote.closeSession(sessionId1);  //close session 1
    assertEquals(1, remote.deleteSessions(new Date()));   //delete session 1

    remote.closeInactiveSessions(0);  //expire session 3-4
    assertEquals(0, remote.deleteSessions(new Date(System.currentTimeMillis()-(60*1000))));   //delete session closed earlier than 1 min ago --> 0
    assertEquals(3, remote.deleteSessions(new Date()));   //delete session closed earlier than 1 min ago --> 0
  }

  public void testAddSessionTimer() throws Exception
  {
    Long timerKey = remote.addSessionTimer();
    assertNotNull(timerKey);
  }

  private void cleanUpSession(String sessionId)
  {
    try
    {
      remote.closeSession(sessionId);
    }
    catch(Throwable t)
    {
      t.printStackTrace();
    }
  }
}