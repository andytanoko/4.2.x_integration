/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ScheduleHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 18, 2004   Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.helpers;

import com.gridnode.gtas.model.scheduler.IScheduleFrequency;
import com.gridnode.gtas.server.scheduler.helpers.ActionHelper;
import com.gridnode.gtas.server.scheduler.model.Schedule;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Calendar;
import java.util.List;

/**
 * Utility class for scheduling.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class ScheduleHelper
{
  /**
   * Schedule the CheckLicense task from current date
   * 
   * @throws Exception
   */
  public static void scheduleCheckLicense() throws Exception
  {
    clearExistingSchedule();

    createSchedule();
  }

  /**
   * Create the schedule
   * 
   * @throws Exception
   */
  private static void createSchedule() throws Exception
  {
    Calendar cal = Calendar.getInstance();
    cal.set(
      cal.get(Calendar.YEAR),
      cal.get(Calendar.MONTH),
      cal.get(Calendar.DAY_OF_MONTH) + 1,
      0,
      0,
      0);

    Schedule schedule = new Schedule();
    schedule.setDelayPeriod(new Integer(1));
    schedule.setFrequency(new Integer(IScheduleFrequency.DAILY));
    schedule.setStartDate(cal.getTime());
    schedule.setStartTime("00:00");
    schedule.setDisabled(Boolean.FALSE);
    schedule.setTaskType(Schedule.TASK_CHECK_LICENSE);

    ActionHelper.createAlarmFromSchedule(schedule);
  }

  /**
   * Upgrade old CheckLicense schedule to the new format.
   * 
   * @throws Exception
   */
  public static void upgradeCheckLicenseSchedule() throws Exception
  {
    if (hasOldFormatSchedule())
    {
      createSchedule();
    }
  }

  /**
   * Checks if the system current has old format CheckLicense schedules. If yes, they will be removed.
   * 
   * @return <b>true</b> if there are old format CheckLicense schedules, <b>false</b> otherwise.
   * @throws Exception
   */
  private static boolean hasOldFormatSchedule() throws Exception
  {
    // retrieve old format schedules
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      iCalAlarm.CATEGORY,
      filter.getEqualOperator(),
      Schedule.TASK_CHECK_LICENSE,
      false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      iCalAlarm.PARENT_UID,
      filter.getEqualOperator(),
      null,
      false);
    List oldAlarms = ServiceLookupHelper.getTimeManager().findAlarms(filter);

    if (oldAlarms != null && !oldAlarms.isEmpty())
    {
      // clear old schedules
      ServiceLookupHelper.getTimeManager().cancelAlarmByFilter(filter);
      return true;
    }
    return false;
  }

  /**
   * Removes all existing CheckLicense schedules.
   */
  private static void clearExistingSchedule() throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      iCalAlarm.CATEGORY,
      filter.getEqualOperator(),
      Schedule.TASK_CHECK_LICENSE,
      false);
    ServiceLookupHelper.getTimeManager().cancelAlarmByFilter(filter);
  }

}
