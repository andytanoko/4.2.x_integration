/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SessionManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 14 2002    Ooi Hui Linn        Created
 * Jun 04 2002    Neo Sok Lay         Change BasicTypedException to
 *                                    SystemException.
 *                                    Use SessionData for saving & retrieving
 *                                    session data contents.
 * Jun 11 2002    Ooi Hui Linn        Add deleteSessions to delete old closed sessions.
 *                                    This is to allow appications to purge old records.
 * Jun 13 2002    Ooi Hui Linn        Change Freeze status to Suspend
 * Jun 14 2002    Ooi Hui Linn        Add in Session HouseKeeping (Timer) and Session Properties methods.
 */
package com.gridnode.pdip.base.session.facade.ejb;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.base.session.exceptions.*;
import com.gridnode.pdip.base.session.helpers.Logger;
import com.gridnode.pdip.base.session.helpers.SessionEntityHandler;
import com.gridnode.pdip.base.session.helpers.SessionIdKeyGen;
import com.gridnode.pdip.base.session.helpers.SessionTimerHandler;
import com.gridnode.pdip.base.session.model.SessionAudit;
import com.gridnode.pdip.base.session.model.SessionData;
import com.gridnode.pdip.framework.exceptions.SystemException;

/**
 * This bean manages the Sessions.
 *
 * @author Ooi Hui Linn
 *
 * @version 2.0
 * @since 2.0
 */

public class SessionManagerBean
  implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5995149384761228673L;
	transient private SessionContext _sessionCtx = null;
  static final private long _MAX_IDLE_TIME          = 6 * 60 * 60 * 1000; //6 hours
  static final private long _SESSION_TIMER_INTERVAL = 1 * 60 * 60 * 1000; //1 hours

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  // ************************ Implementing methods in ISessionManagerObj

  /**
   * Open a new Session.
   */
  public String openSession()
    throws OpenSessionException, SystemException
  {
    Logger.debug("[SessionManagerBean.openSession] Enter");

    SessionAudit session = new SessionAudit();
    try
    {
      session.setSessionId(SessionIdKeyGen.getNextId());
      session.setState(SessionAudit.STATE_OPENED);
      session.setSessionData(new HashMap());
      session.setOpenTime(new Date());
      session.setLastActiveTime(session.getOpenTime());
      getEntityHandler().createEntity(session);
    }
    catch (CreateException ex)
    {
      Logger.warn("[SessionManagerBean.openSession] CreateException ", ex);
      throw new OpenSessionException(ex.getLocalizedMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[SessionManagerBean.openSession] Error ", ex);
      throw new SystemException(
        "SessionManagerBean.openSession (session:"+session+") Error ",
        ex);
    }

    Logger.debug("[SessionManagerBean.openSession] Created SessionAudit " + session.getSessionId());
    return session.getSessionId();
  }

  /**
   * Get all data from existing Session.
   */
  public HashMap getAllSessionData(String sessionId)
    throws GetSessionException, ClosedSessionException, InvalidSessionException
  {
    Logger.debug("[SessionManagerBean.getAllSessionData] Enter");

    SessionAudit session = null;
    try
    {
      session = getEntityHandler().findBySessionId(sessionId);
      session.setLastActiveTime(new Date());
      getEntityHandler().update(session);
    }
    catch (Throwable ex)
    {
      Logger.warn("[SessionManagerBean.getSession] Error ", ex);
      throw new GetSessionException("Error getting session:" + sessionId, ex);
    }

    if(session == null) //Invalid session id
      throw new InvalidSessionException("Session not found for session id "+ sessionId);
    else if(session.getState() == SessionAudit.STATE_CLOSED)
      throw new ClosedSessionException("Session has already been closed. Session id "+ sessionId);

    Logger.debug("[SessionManagerBean.getSession] Retrieved SessionAudit " + session);
    return (HashMap) session.getSessionData().clone();
  }


  /**
   * Set data into Session.
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
    throws ClosedSessionException, InvalidSessionException
  {
    Logger.debug("[SessionManagerBean.setSessionData] Enter");

    SessionAudit session = null;
    try
    {
      session = getEntityHandler().findBySessionId(sessionId);

      if(session == null) //Invalid session id
        throw new InvalidSessionException("Session not found for session id "+ sessionId);
      else if(session.getState() == SessionAudit.STATE_CLOSED)
        throw new ClosedSessionException("Session has already been closed. Session id "+ sessionId);
      session.getSessionData().put(sessionData.getDataKey(), sessionData.getDataContents());

      session.setLastActiveTime(new Date());
      getEntityHandler().update(session);
      return true;
    }
    catch (ClosedSessionException ex1)
    {
      throw ex1;
    }
    catch (InvalidSessionException ex2)
    {
      throw ex2;
    }
    catch (Throwable t)
    {
      Logger.warn("[SessionManagerBean.setSessionData] Error ", t);
      return false;
    }
  }

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
    throws ClosedSessionException, InvalidSessionException, SystemException
  {
    Logger.debug("[SessionManagerBean.getSessionData] Enter");

    SessionAudit session = null;
    try
    {
      session = getEntityHandler().findBySessionId(sessionId);

      if(session == null) //Invalid session id
        throw new InvalidSessionException("Session not found for session id "+ sessionId);
      else if(session.getState() == SessionAudit.STATE_CLOSED)
        throw new ClosedSessionException("Session has already been closed. Session id "+ sessionId);

      session.setLastActiveTime(new Date());
      getEntityHandler().update(session);

      byte[] contents = (byte[])session.getSessionData().get(key);
      return new SessionData(key, contents);
    }
    catch (ClosedSessionException ex1)
    {
      throw ex1;
    }
    catch (InvalidSessionException ex2)
    {
      throw ex2;
    }
    catch (Throwable t)
    {
      Logger.warn("[SessionManagerBean.getSessionData] Error ", t);
      throw new SystemException("SessionManagerBean.getSessionData(sessionId,key) Error ", t);
    }
  }

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
    throws SystemException, ClosedSessionException, InvalidSessionException
  {
    Logger.debug("[SessionManagerBean.removeSessionData] Enter");

    SessionAudit session = null;
    try
    {
      session = getEntityHandler().findBySessionId(sessionId);

      if(session == null) //Invalid session id
        throw new InvalidSessionException("Session not found for session id "+ sessionId);
      else if(session.getState() == SessionAudit.STATE_CLOSED)
        throw new ClosedSessionException("Session has already been closed. Session id "+ sessionId);

      byte[] value = (byte[]) session.getSessionData().remove(dataKey);
      session.setLastActiveTime(new Date());
      getEntityHandler().update(session);
      return new SessionData(dataKey, value);
    }
    catch (ClosedSessionException ex1)
    {
      throw ex1;
    }
    catch (InvalidSessionException ex2)
    {
      throw ex2;
    }
    catch (Throwable t)
    {
      Logger.warn("[SessionManagerBean.removeSessionData] Error ", t);
      throw new SystemException("SessionManagerBean.removeSessionData(sessionId,key) Error ", t);
    }
  }

  /**
   * Set Session to authenticated user (This session now belongs to the specified user.
   *
   * @param sessionId   Session Id of the session to update.
   * @param authSubject Authenticated user/subject.
   *
   * @return  Returns true if successful.
   *          Returns false if session has been authenticated and belongs to another authSubject.
   */
  public boolean authSession(String sessionId, String authSubject)
    throws SystemException, ClosedSessionException, InvalidAuthSubjectException, InvalidSessionException
  {
    Logger.debug("[SessionManagerBean.authSession] Enter");

    //Check if valid authSubject
    if(authSubject == null || authSubject.length() <1)
      throw new InvalidAuthSubjectException("Authenticated subject must not be null or empty");

    SessionAudit session = null;
    try
    {
      session = getEntityHandler().findBySessionId(sessionId);

      if(session == null) //Invalid session id
        throw new InvalidSessionException("Session not found for session id "+ sessionId);
      else if(session.getState() == SessionAudit.STATE_CLOSED)
        throw new ClosedSessionException("Session has already been closed. Session id "+ sessionId);

      if(session.getSessionName() != null  && session.getSessionName().length() > 0)
      {
        if(session.getSessionName().equals(authSubject))  //Same authSubject
          return true;
        else
          return false;
      }
      session.setSessionName(authSubject);
//      session.setState(session.STATE_AUTHENTICATED);
      session.setLastActiveTime(new Date());
      getEntityHandler().update(session);
      return true;
    }
    catch (ClosedSessionException ex1)
    {
      throw ex1;
    }
    catch (InvalidSessionException ex2)
    {
      throw ex2;
    }
    catch (Throwable t)
    {
      Logger.warn("[SessionManagerBean.authSession] Error ", t);
      throw new SystemException("SessionManagerBean.authSession(sessionId,authSubject) Error ", t);
    }
  }

  /**
   * Check if Session belongs is authenticated.
   *
   * @param   sessionId Session Id of the session.
   * @return  Returns true if session is auth'ed, false if not.
   */
  public boolean isAuthSession(String sessionId)
    throws SystemException, ClosedSessionException, InvalidSessionException
  {
    Logger.debug("[SessionManagerBean.isAuthSession] Enter");

    SessionAudit session = null;
    try
    {
      session = getEntityHandler().findBySessionId(sessionId);

      if(session == null) //Invalid session id
        throw new InvalidSessionException("Session not found for session id "+ sessionId);
      else if(session.getState() == SessionAudit.STATE_CLOSED)
        throw new ClosedSessionException("Session has already been closed. Session id "+ sessionId);

      session.setLastActiveTime(new Date());
      getEntityHandler().update(session);
      if(session.getSessionName() == null)
        return false;
      if(session.getSessionName().length() < 1)
        return false;
      return true;
    }
    catch (ClosedSessionException ex1)
    {
      throw ex1;
    }
    catch (InvalidSessionException ex2)
    {
      throw ex2;
    }
    catch (Throwable t)
    {
      Logger.warn("[SessionManagerBean.isAuthSession] Error ", t);
      throw new SystemException("SessionManagerBean.isAuthSession(sessionId) Error ", t);
    }
  }

  /**
   * Get Session Name or Authenticated subject.
   *
   * @param   sessionId Session Id of the session.
   * @return  Returns Authenticated Subject.
   */
  public String getSessionAuthSubject(String sessionId)
    throws SystemException, ClosedSessionException, InvalidSessionException
  {
    Logger.debug("[SessionManagerBean.getSessionAuthSubject] Enter");

    SessionAudit session = null;
    try
    {
      session = getEntityHandler().findBySessionId(sessionId);

      if(session == null) //Invalid session id
        throw new InvalidSessionException("Session not found for session id "+ sessionId);
      else if(session.getState() == SessionAudit.STATE_CLOSED)
        throw new ClosedSessionException("Session has already been closed. Session id "+ sessionId);

      session.setLastActiveTime(new Date());
      getEntityHandler().update(session);
      return session.getSessionName();
    }
    catch (ClosedSessionException ex1)
    {
      throw ex1;
    }
    catch (InvalidSessionException ex2)
    {
      throw ex2;
    }
    catch (Throwable t)
    {
      Logger.warn("[SessionManagerBean.getSessionAuthSubject] Error ", t);
      throw new SystemException("SessionManagerBean.getSessionAuthSubject(sessionId) Error ", t);
    }
  }

  /**
   * Keep Session active.
   *
   * @param   sessionId Session Id of the session.
   * @return  Returns true if session is succussfully refreshed, false if session has already expired.
   */
  public boolean keepActiveSession(String sessionId)
    throws SystemException, InvalidSessionException
  {
    Logger.debug("[SessionManagerBean.keepActiveSession] Enter");

    SessionAudit session = null;
    try
    {
      session = getEntityHandler().findBySessionId(sessionId);

      if(session == null) //Invalid session id
        throw new InvalidSessionException("Session not found for session id "+ sessionId);
      else if(session.getState() == SessionAudit.STATE_CLOSED)
        return false;
      session.setLastActiveTime(new Date());
      getEntityHandler().update(session);
      return true;
    }
    catch (InvalidSessionException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      Logger.warn("[SessionManagerBean.keepActiveSession] Error ", t);
      throw new SystemException("SessionManagerBean.keepActiveSession(sessionId) Error ", t);
    }
  }

  /**
   * Suspend active session. Suspended session will not be expired until specifically closed.
   *
   * @param   sessionId Session Id of the session.
   * @param   toSuspend True to suspend session. False to revert Session back to opened.
   * @return  Returns true if session is successfully suspended/unsuspended, false if session has already expired/closed.
   */
  public boolean suspendSession(String sessionId, boolean toSuspend)
    throws SystemException, InvalidSessionException
  {
    Logger.debug("[SessionManagerBean.suspendSession] Enter");

    SessionAudit session = null;
    try
    {
      session = getEntityHandler().findBySessionId(sessionId);

      if(session == null) //Invalid session id
        throw new InvalidSessionException("Session not found for session id "+ sessionId);
      else if(session.getState() == SessionAudit.STATE_CLOSED)
        return false;
      session.setState(toSuspend? SessionAudit.STATE_SUSPENDED : SessionAudit.STATE_OPENED);
      session.setLastActiveTime(new Date());
      getEntityHandler().update(session);
      return true;
    }
    catch (InvalidSessionException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      Logger.warn("[SessionManagerBean.suspendSession] Error ", t);
      throw new SystemException("SessionManagerBean.suspendSession(sessionId, toSuspend) Error ", t);
    }
  }

  /**
   * Close active session.
   *
   * @param   sessionId Session Id of the session.
   * @return  Returns true if session is successfully closed.
   */
  public boolean closeSession(String sessionId)
    throws SystemException, InvalidSessionException
  {
    Logger.debug("[SessionManagerBean.closeSession] Enter");

    SessionAudit session = null;
    try
    {
      session = getEntityHandler().findBySessionId(sessionId);

      if(session == null) //Invalid session id
        throw new InvalidSessionException("Session not found for session id "+ sessionId);
      if(session.getState() != SessionAudit.STATE_CLOSED)
        invalidateSession(session, new Date());
      //else session already closed
      return true;
    }
    catch (InvalidSessionException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      Logger.warn("[SessionManagerBean.closeSession] Error ", t);
      throw new SystemException("SessionManagerBean.closeSession(sessionId) Error ", t);
    }
  }

  /**
   * Close Inactive / Idle sessions. This is called by the Session HouseKeeping Timer.
   *
   * @param   maxIdle Maximum time session can remain idle (in millisec).
   * @return  Returns number of sessions successfully expired (closed).
   */
  public int closeInactiveSessions(long maxIdle)
    throws SystemException
  {
    Logger.debug("[SessionManagerBean.closeInactiveSessions] Enter");

    try
    {
      Date now = new Date();
      Date expiredTime = new Date(System.currentTimeMillis() - maxIdle);
      Collection expiredSessions = getEntityHandler().findExpiredSessions(expiredTime);
      if(expiredSessions == null || expiredSessions.isEmpty())
        return 0;
      SessionAudit session = null;
      for(Iterator iter=expiredSessions.iterator(); iter.hasNext();)
      {
        session = (SessionAudit) iter.next();
        invalidateSession(session, now);
      }
      return expiredSessions.size();
    }
    catch (Throwable t)
    {
      Logger.warn("[SessionManagerBean.closeInactiveSessions] Error ", t);
      throw new SystemException("Error: "+t.getMessage(), t);
    }
  }

  /**
   * Delete Closed sessions. This is a facility to purge old SessionAudit data.
   *
   * @param   before Purge SessionAudit records before this date/time.
   * @return  Returns number of sessions successfully deleted.
   */
  public int deleteSessions(Date before)
    throws SystemException
  {
    Logger.debug("[SessionManagerBean.deleteSessions] Enter");

    try
    {
      Collection expiredSessions = getEntityHandler().findClosedSessions(before);
      if(expiredSessions == null || expiredSessions.isEmpty())
        return 0;

      SessionAudit session = null;
      for(Iterator iter=expiredSessions.iterator(); iter.hasNext();)
      {
        session = (SessionAudit) iter.next();
        Logger.debug("[SessionManagerBean.deleteSessions] Deleting closed session "+session.getSessionId());
        getEntityHandler().remove(new Long(session.getUId()));
      }

      return expiredSessions.size();
    }
    catch (Throwable t)
    {
      Logger.warn("[SessionManagerBean.deleteSessions] Error ", t);
      throw new SystemException("SessionManagerBean.deleteSessions Error ", t);
    }
  }

//------------------- HOUSEKEEPING ---------------------------------------------
  /**
   * Setup the timer to periodically check for expired Sessions.
   * @return  Returns Timer Key Id.
   */
  public Long addSessionTimer()
    throws SystemException
  {
    try
    {
      Logger.debug("[SessionManagerBean.addSessionTimer] Setup Session Timer");
      Long timerKey = SessionTimerHandler.addTimer(new Long(_SESSION_TIMER_INTERVAL));
      Logger.debug("[SessionManagerBean.addSessionTimer] Session Timer Key = " + timerKey);
      return timerKey;
    }
    catch(Throwable t)
    {
      Logger.warn("[SessionManagerBean.addSessionTimer] Error ");
      throw new SystemException("SessionManagerBean.addSessionTimer Error ", t);
    }
  }

  /**
   * Other Session Timer utility currently unavailable.
   */
  //public Object? getSessionTimer(Object? SessionTimerKey?)
  //public boolean updateSessionTimer(Object?)

//------------------- SESSION PROPERTIES CONFIGURATIONS ------------------------
//--- To change the current implementation on the configuration, please update
//--- these methods.

  /**
   * Get value of Session's Max Idle Time.
   * Default = 6 hours.
   *
   * @return  Returns the Max Idle Time Session in allowed to remain idle
   *          before it is automatically closed by the Session Management Timer.
   */
  public long getPropertySessionMaxIdleTime()
    throws SystemException
  {
    Logger.debug("[SessionManagerBean.getPropertySessionMaxIdleTime] Enter");
    return _MAX_IDLE_TIME;
  }

  /**
   * Get value of Session Management's HouseKeeping Timer Interval.
   * Default = 1 hours.
   *
   * @return  Returns the Session Management's HouseKeeping Timer Interval.
   *          The Timer currently closes the idle Sessions.
   */
  public long getPropertySessionTimerInterval()
    throws SystemException
  {
    Logger.debug("[SessionManagerBean.getPropertySessionTimerInterval] Enter");
    return _SESSION_TIMER_INTERVAL;
  }

//---------------------PRIVATE--------------------------------------------------
  private SessionEntityHandler getEntityHandler()
  {
     return SessionEntityHandler.getInstance();
  }

  private void invalidateSession(SessionAudit session, Date now)
  {
    try
    {
      Logger.debug("[SessionManagerBean.invalidateSession] Close session "+session.getSessionId());
      session.setSessionData(null); //clear session Data
      session.setState(SessionAudit.STATE_CLOSED);
      session.setDestroyTime(now);
      getEntityHandler().update(session);
    }
    catch(Throwable t)
    {
      Logger.warn("[SessionManagerBean.invalidateSession] Error while invalidating session "+session, t);
    }
  }
}