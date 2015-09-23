/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlarmScheduler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 27 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.docalert.helpers;

import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;

import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;

import java.util.Date;

/**
 * This class provides the Alarm scheduling services required by the DocAlert
 * module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class AlarmScheduler
{
  /**
   * Schedule an alarm for triggering Reminder Alert.
   *
   * @param startDate The date when the alarm will be dued.
   * @return The alarm created.
   */
  public static iCalAlarm scheduleReminderAlarm(Date startDate) throws Throwable
  {
    iCalAlarm alarm = new iCalAlarm();
    alarm.setCategory("ReminderAlert");
    alarm.setReceiverKey("ReminderAlertMDBean");
    alarm.setSenderKey("AlarmScheduler");
    alarm.setStartDt(startDate);
    return getMgr().addAlarm(alarm);
  }

  /**
   * Cancel an alarm.
   *
   * @param alarmUID The UID of the alarm.
   */
  public static void cancelReminderAlarm(Long alarmUID)
  {
    try
    {
      getMgr().cancelAlarm(alarmUID);
    }
    catch (Exception ex)
    {
      Logger.warn("[AlarmScheduler.cancelReminderAlaram] Unable to cancel alarm",
                 ex);
    }
  }

  /**
   * Find an alarm.
   *
   * @param alarmUID The UID of the alarm to find.
   * @return The found alarm, or <b>null</b> if none exists.
   */
  public static iCalAlarm findAlarm(Long alarmUID)
  {
    iCalAlarm alarm = null;
    try
    {
      alarm = getMgr().getAlarm(alarmUID);
    }
    catch (Exception ex)
    {
      Logger.warn("[AlarmScheduler.findAlarm] Unable to find alarm",
                 ex);
    }
    return alarm;
  }

  /**
   * Get the EJBObject for iCalTimeMgrBean.
   *
   * @return the EJBObject for iCalTimeMgrBean.
   */
  protected static IiCalTimeMgrObj getMgr() throws ServiceLookupException
  {
    return (IiCalTimeMgrObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IiCalTimeMgrHome.class.getName(),
             IiCalTimeMgrHome.class,
             new Object[0]);
  }
}