/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTScheduledTaskEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2004-02-11     Neo Sok Lay         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.scheduler.IScheduleFrequency;
import com.gridnode.gtas.model.scheduler.ISchedule;
import com.gridnode.gtas.model.scheduler.IWeekDay;
import com.gridnode.gtas.model.scheduler.IWeekMonth;

public interface IGTScheduledTaskEntity extends IGTEntity
{
  // Fields
  public static final Number UID          = ISchedule.UID;
  public static final Number TYPE         = ISchedule.TASK_TYPE;
  public static final Number TASK_ID      = ISchedule.TASK_ID;
  public static final Number IS_DISABLED  = ISchedule.DISABLED;
  public static final Number START_DATE   = ISchedule.START_DATE;
  public static final Number RUN_TIME     = ISchedule.START_TIME;
  public static final Number FREQUENCY    = ISchedule.FREQUENCY;
  public static final Number COUNT        = ISchedule.TIMES_TO_RUN;
  public static final Number DAYS_OF_WEEK  = ISchedule.DAYS_OF_WEEK; //list
  public static final Number WEEK_OF_MONTH = ISchedule.WEEK_OF_MONTH;
  public static final Number INTERVAL     = ISchedule.DELAY_PERIOD;
  
  //virtual fields
  public static final Number MONTH_OPTION = new Integer(-10);
  public static final Number REVERSE_INTERVAL = new Integer(-20);
  public static final Number DAY_OF_WEEK = new Integer(-30);
  
  //TYPE values
  public static final String TYPE_USER_PROCEDURE = ISchedule.TASK_USER_PROCEDURE;
  public static final String TYPE_CHECK_LICENSE = ISchedule.TASK_CHECK_LICENSE;
  public static final String TYPE_HOUSEKEEPING_INFO = ISchedule.TASK_HOUSEKEEPING_INFO;
  public static final String TYPE_DB_ARCHIVE = ISchedule.TASK_DB_ARCHIVE;
  
  //DAYS_OF_WEEK values
  public static final Integer DAY_MONDAY = new Integer(IWeekDay.MONDAY);
  public static final Integer DAY_TUESDAY = new Integer(IWeekDay.TUESDAY);
  public static final Integer DAY_WEDNESDAY = new Integer(IWeekDay.WEDNESDAY);
  public static final Integer DAY_THURSDAY = new Integer(IWeekDay.THURSDAY);
  public static final Integer DAY_FRIDAY = new Integer(IWeekDay.FRIDAY);
  public static final Integer DAY_SATURDAY = new Integer(IWeekDay.SATURDAY);
  public static final Integer DAY_SUNDAY = new Integer(IWeekDay.SUNDAY);

  //FREQUENCY values
  public static final Integer FREQUENCY_ONCE = new Integer(IScheduleFrequency.ONCE);
  public static final Integer FREQUENCY_MINUTELY = new Integer(IScheduleFrequency.MINUTELY);
  public static final Integer FREQUENCY_HOURLY = new Integer(IScheduleFrequency.HOURLY);
  public static final Integer FREQUENCY_DAILY = new Integer(IScheduleFrequency.DAILY);
  public static final Integer FREQUENCY_WEEKLY = new Integer(IScheduleFrequency.WEEKLY);
  public static final Integer FREQUENCY_MONTHLY = new Integer(IScheduleFrequency.MONTHLY);
  
  //WEEK_OF_MONTH values
  public static final Integer WEEK_FIRST = new Integer(IWeekMonth.FIRST_WEEK);
  public static final Integer WEEK_SECOND = new Integer(IWeekMonth.SECOND_WEEK);
  public static final Integer WEEK_THIRD = new Integer(IWeekMonth.THIRD_WEEK);
  public static final Integer WEEK_FOURTH = new Integer(IWeekMonth.FOURTH_WEEK);
  public static final Integer WEEK_LAST = new Integer(IWeekMonth.LAST_WEEK);
  
  //MONTH_OPTION values
  public static final String MONTH_OPTION_DAY = "day";
  public static final String MONTH_OPTION_LASTDAY = "lastDay";
  public static final String MONTH_OPTION_WEEK = "week";
  
}
