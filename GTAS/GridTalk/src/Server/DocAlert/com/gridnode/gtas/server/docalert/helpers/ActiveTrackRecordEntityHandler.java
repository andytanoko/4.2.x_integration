/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActiveTrackRecordEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 25 2003    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.docalert.helpers;

import java.util.Collection;

import com.gridnode.gtas.server.docalert.entities.ejb.IActiveTrackRecordLocalHome;
import com.gridnode.gtas.server.docalert.entities.ejb.IActiveTrackRecordLocalObj;
import com.gridnode.gtas.server.docalert.model.ActiveTrackRecord;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the ActiveTrackRecordBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public final class ActiveTrackRecordEntityHandler
  extends          LocalEntityHandler
{
  private ActiveTrackRecordEntityHandler()
  {
    super(ActiveTrackRecord.ENTITY_NAME);
  }

  /**
   * Get an instance of a ActiveTrackRecordEntityHandler.
   */
  public static ActiveTrackRecordEntityHandler getInstance()
  {
    ActiveTrackRecordEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(ActiveTrackRecord.ENTITY_NAME, true))
    {
      handler = (ActiveTrackRecordEntityHandler)EntityHandlerFactory.getHandlerFor(
                  ActiveTrackRecord.ENTITY_NAME, true);
    }
    else
    {
      handler = new ActiveTrackRecordEntityHandler();
      EntityHandlerFactory.putEntityHandler(ActiveTrackRecord.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IActiveTrackRecordLocalHome.class.getName(),
      IActiveTrackRecordLocalHome.class);
  }*/

  
  protected Class getHomeInterfaceClass() throws Exception
	{
		return IActiveTrackRecordLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IActiveTrackRecordLocalObj.class;
  }

  // *************************** Own methods ********************************

  /**
   * Create a ActiveTrackRecord.
   *
   * @param trackRecordUID UID of the ResponseTrackRecord.
   * @param alarmUID UID of the alarm that will trigger the ReminderAlert.
   * @param sentGridDocUID UID of the GridDocument that is sent.
   * @param daysToReminder Number of days after tracking date that no response
   * received before sending reminder email.
   */
  public void createActiveTrackRecord(Long trackRecordUID,
                                      Long alarmUID,
                                      Long sentGridDocUID,
                                      int daysToReminder) throws Throwable
  {
    ActiveTrackRecord record = new ActiveTrackRecord();

    record.setAlarmUID(alarmUID);
    record.setDaysToReminder(daysToReminder);
    record.setSentGridDocUID(sentGridDocUID);
    record.setTrackRecordUID(trackRecordUID);

    create(record);
  }

  /**
   * Find ActiveTrackRecord(s) for a ResponseTrackRecord.
   *
   * @param trackRecordUID UID of the ResponseTrackRecord.
   *
   * @return Collection of ActiveTrackRecord(s).
   */
  public Collection findActiveTrackRecords(Long trackRecordUID) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ActiveTrackRecord.TRACK_RECORD_UID,
                           filter.getEqualOperator(), trackRecordUID, false);

    return getEntityByFilterForReadOnly(filter);
  }

  /**
   * Find an ActiveTrackRecord that is related to a particular alarm.
   *
   * @param alarmUID UID of the alarm.
   * @return The ActiveTrackRecord retrieved, or <b>null</b> if none exists.
   */
  public ActiveTrackRecord findActiveTrackRecordByAlarm(Long alarmUID) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ActiveTrackRecord.ALARM_UID,
                           filter.getEqualOperator(), alarmUID, false);

    ActiveTrackRecord atr = null;

    Collection results = getEntityByFilterForReadOnly(filter);

    if (!results.isEmpty())
      atr = (ActiveTrackRecord)results.toArray()[0];

    return atr;
  }
}