/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SessionEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 06 2002    Ooi Hui Linn        Created
 * Jun 07 2002    Neo Sok Lay         Finders retrieve directly from DAO.
 * Jun 11 2002    Ooi Hui Linn        Update findExpiredSessions from using
 *                                    getEntityByFilterForReadOnly to
 *                                    getEntityByFilter (for updates).
 * Jun 11 2002    Ooi Hui Linn        Add findClosedSessions to be deleted.
 * Jul 25 2002    Neo Sok Lay         Remove ejbEntityMap dependency.
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.pdip.base.session.helpers;

import java.util.Collection;
import java.util.Date;

import com.gridnode.pdip.base.session.entities.ejb.ISessionAuditLocalHome;
import com.gridnode.pdip.base.session.entities.ejb.ISessionAuditLocalObj;
import com.gridnode.pdip.base.session.model.SessionAudit;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the UserBean.
 *
 * @author Ooi Hui Linn
 *
 * @version 2.0
 * @since 2.0
 */
public final class SessionEntityHandler
  extends          LocalEntityHandler
{

  private SessionEntityHandler()
  {
    super(SessionAudit.ENTITY_NAME);
  }

  /**
   * Get an instance of a SessionEntityHandler.
   */
  public static SessionEntityHandler getInstance()
  {
    SessionEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(SessionAudit.ENTITY_NAME, true))
    {
      handler = (SessionEntityHandler) EntityHandlerFactory.getHandlerFor(
                  SessionAudit.ENTITY_NAME, true);
    }
    else
    {
      handler = new SessionEntityHandler();
      EntityHandlerFactory.putEntityHandler(SessionAudit.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }


  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      ISessionAuditLocalHome.class.getName(),
      ISessionAuditLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return ISessionAuditLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return ISessionAuditLocalObj.class;
  }

  //****************** Finders ***************************************

  /**
   * Finds the list of SessionAudit records based on the SessionAudit's state.
   * It returns a collection of SessionAudits.
   *
   * @param state The session state.
   * @return A Collection of SessionAudits.
   *
   * @see SessionAudit#STATE_OPENED
   * @see SessionAudit#STATE_AUTHENTICATED
   * @see SessionAudit#STATE_SUSPENDED
   * @see SessionAudit#STATE_CLOSED
   */
  public Collection findByState(short state)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, SessionAudit.STATE,
      filter.getEqualOperator(), new Short(state), false);

    filter.setOrderFields(new Object[] {SessionAudit.LAST_ACTIVE_TIME});

    return getEntityByFilterForReadOnly(filter);
  }


  /**
   * Finds the list of SessionAudit records that are active and expired.
   * It returns a collection of SessionAudits.
   *
   * @param expiredLastActiveTime The expired last active time. All active sessions
   *                  with LastActiveTime <  expiredLastActiveTime will be returned.
   * @return A Collection of SessionAudits.
   *
   * @see SessionAudit#STATE_OPENED
   * @see SessionAudit#STATE_AUTHENTICATED
   * @see SessionAudit#STATE_SUSPENDED
   * @see SessionAudit#STATE_CLOSED
   */
  public Collection findExpiredSessions(Date expiredLastActiveTime)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();

    //Find all NOT CLOSED sessions
    filter.addSingleFilter(null, SessionAudit.STATE,
      filter.getEqualOperator(), new Short(SessionAudit.STATE_CLOSED), true);
    //AND all NOT SUSPENDED sessions
    filter.addSingleFilter(filter.getAndConnector(), SessionAudit.STATE,
      filter.getEqualOperator(), new Short(SessionAudit.STATE_SUSPENDED), true);
    //AND LastActiveTime <= expiredLastActiveTime
    filter.addSingleFilter(filter.getAndConnector(), SessionAudit.LAST_ACTIVE_TIME,
      filter.getLessOrEqualOperator(), expiredLastActiveTime, false);

    return getEntityByFilter(filter);
  }

  /**
   * Finds the list of SessionAudit records that are closed and is before the indicated date.
   * It returns a collection of SessionAudits.
   *
   * @param beforeTime The closed time.
   * @return A Collection of SessionAudits.
   *
   * @see SessionAudit#STATE_CLOSED
   */
  public Collection findClosedSessions(Date beforeTime)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();

    //Find all CLOSED sessions
    filter.addSingleFilter(null, SessionAudit.STATE,
      filter.getEqualOperator(), new Short(SessionAudit.STATE_CLOSED), false);
    //AND LastActiveTime <= beforeTime
    filter.addSingleFilter(filter.getAndConnector(), SessionAudit.DESTROY_TIME,
      filter.getLessOrEqualOperator(), beforeTime, false);

    return getEntityByFilterForReadOnly(filter);
  }

  /**
   * Find the SessionAuditBeans with the specified session id.
   *
   * @param sessionId The session id uniquely identifies the sessionAudit.
   * @return a SessionAudit.
   */
  public SessionAudit findBySessionId(String sessionId) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, SessionAudit.SESSION_ID,
      filter.getEqualOperator(), sessionId, false);

    Collection result = getKeyByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    Long key = (Long) result.iterator().next();
    return (SessionAudit)getEntityByKey(key);
   }


}