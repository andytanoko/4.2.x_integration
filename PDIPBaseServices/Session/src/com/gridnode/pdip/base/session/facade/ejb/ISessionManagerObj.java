/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISessionManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 14 2002    Ooi Hui Linn         Created
 * Jun 04 2002     Neo Sok Lay        Change BasicTypedException to
 *                                    SystemException.
 *                                    Use SessionData for saving/retrieving
 *                                    session data.
 */
package com.gridnode.pdip.base.session.facade.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;

import javax.ejb.EJBObject;

import com.gridnode.pdip.base.session.exceptions.*;
import com.gridnode.pdip.base.session.model.SessionData;
import com.gridnode.pdip.framework.exceptions.SystemException;

/**
 * (Remote/Local) Object for SessionManagerBean.
 *
 * @author Ooi Hui Linn
 *
 * @version 2.0
 * @since 2.0
 */
public interface ISessionManagerObj extends EJBObject
{
  /**
   * Open new session
   * @return Returns Session ID
   */
  public String openSession()
    throws SystemException, OpenSessionException, RemoteException;

  /**
   * Get all data available in existing valid session's (Read only).
   * It will throw ClosedSessionException if the session is closed/expired.
   * It will throw InvalidSessionException if the session does not exists.
   *
   * @param   sessionId SessionId that uniquely identifies this session.
   * @return  Returns HashMap that contains key-value pair of the session data
   */
  public HashMap getAllSessionData(String sessionId)
    throws GetSessionException, ClosedSessionException, InvalidSessionException, RemoteException;

  /**
   * Set Session data into Session.
   * If the key does not exist, a new record will be added into the session's data.
   * If the key exists, it will update the value of the key with the new value.
   * Both key & value have to be serializable since the data will be persisted (default into database).
   *
   * @param sessionId Session Id of the session to update.
   * @param sessionData Session data to store.
   *
   * @return  Returns the success/failure to update the value.
   */
  public boolean setSessionData(String sessionId, SessionData sessionData)
    throws ClosedSessionException, InvalidSessionException, RemoteException;

  /**
   * Get Session data from Session.
   * If the key does not exist, it will return null.
   *
   * @param sessionId Session Id of the session to update.
   * @param key The key to obtain the stored value.
   *
   * @return  Returns the stored value wrapped in a SessionData.
   */
  public SessionData getSessionData(String sessionId, String key)
    throws SystemException, ClosedSessionException, InvalidSessionException, RemoteException;

  /**
   * Remove Session data from Session.
   * If the key does not exist, it will return null.
   *
   * @param sessionId Session Id of the session to update.
   * @param dataKey       Session data key.
   *
   * @return  Returns the stored value wrapped in SessionData object.
   */
  public SessionData removeSessionData(String sessionId, String dataKey)
    throws SystemException, ClosedSessionException, InvalidSessionException, RemoteException;

  /**
   * Set Session to authenticated user (This session now belongs to the specified user).
   *
   * @param sessionId   Session Id of the session to update.
   * @param authSubject Authenticated user/subject.
   *
   * @return  Returns true if successful.
   *          Returns false if session has been authenticated and belongs to another authSubject.
   */
  public boolean authSession(String sessionId, String authSubject)
    throws SystemException, ClosedSessionException, InvalidAuthSubjectException, InvalidSessionException, RemoteException;

  /**
   * Check if Session belongs is authenticated.
   *
   * @param   sessionId Session Id of the session.
   * @return  Returns true if session is auth'ed, false if not.
   */
  public boolean isAuthSession(String sessionId)
    throws SystemException, ClosedSessionException, InvalidSessionException, RemoteException;

  /**
   * Get Session Name or Authenticated subject.
   *
   * @param   sessionId Session Id of the session.
   * @return  Returns Authenticated Subject.
   */
  public String getSessionAuthSubject(String sessionId)
    throws SystemException, ClosedSessionException, InvalidSessionException, RemoteException;

  /**
   * Keep Session active.
   *
   * @param   sessionId Session Id of the session.
   * @return  Returns true if session is successfully refreshed, false if session has already expired.
   */
  public boolean keepActiveSession(String sessionId)
    throws SystemException, InvalidSessionException, RemoteException;

  /**
   * Suspend active session. Suspended session will not be expired until specifically closed.
   *
   * @param   sessionId Session Id of the session.
   * @param   toSuspend True to suspend session. False to revert Session back to opened.
   * @return  Returns true if session is successfully suspended/unsuspended, false if session has already expired/closed.
   */
  public boolean suspendSession(String sessionId, boolean toSuspend)
    throws SystemException, InvalidSessionException, RemoteException;

  /**
   * Close active session.
   *
   * @param   sessionId Session Id of the session.
   * @return  Returns true if session is successfully closed.
   */
  public boolean closeSession(String sessionId)
    throws SystemException, InvalidSessionException, RemoteException;

  /**
   * Close Inactive / Idle sessions. This is called by the Session HouseKeeping Timer.
   *
   * @param   maxIdle Maximum time session can remain idle (in millisec).
   * @return  Returns number of sessions successfully expired (closed).
   */
  public int closeInactiveSessions(long maxIdle)
    throws SystemException, RemoteException;

  /**
   * Delete Closed sessions. This is a facility to purge old SessionAudit data.
   *
   * @param   before Purge SessionAudit records before this date/time.
   * @return  Returns number of sessions successfully deleted.
   */
  public int deleteSessions(Date before)
    throws SystemException, RemoteException;

//---------------- House Keeping Jobs ------------------------------------------
  /**
   * Setup the timer to periodically check for expired Sessions.
   * @return  Returns Timer Key Id.
   */
  public Long addSessionTimer()
    throws SystemException, RemoteException;

  /**
   * Other Session Timer utility currently unavailable.
   */
  //public Object? getSessionTimer(Object? SessionTimerKey?)
  //public boolean updateSessionTimer(Object?)

//------------------- SESSION PROPERTIES CONFIGURATIONS ------------------------

  /**
   * Get value of Session's Max Idle Time.
   * Default = 6 hours.
   *
   * @return  Returns the Max Idle Time Session in allowed to remain idle
   *          before it is automatically closed by the Session Management Timer.
   */
  public long getPropertySessionMaxIdleTime()
    throws SystemException, RemoteException;

  /**
   * Get value of Session Management's HouseKeeping Timer Interval.
   * Default = 1 hours.
   *
   * @return  Returns the Session Management's HouseKeeping Timer Interval.
   *          The Timer currently closes the idle Sessions.
   */
  public long getPropertySessionTimerInterval()
    throws SystemException, RemoteException;

}
