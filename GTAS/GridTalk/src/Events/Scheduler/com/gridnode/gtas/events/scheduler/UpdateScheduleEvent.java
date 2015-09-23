/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2004 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateScheduleEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 09 2004    Koh Han Sing        Created
 */
package com.gridnode.gtas.events.scheduler;

import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.Date;
import java.util.List;

/**
 * This Event class contains the data for the updating of a Schedule(iCalAlarm).
 *
 * @author Koh Han Sing
 *
 * @version 2.2.10
 * @since 2.2.10
 */
public class UpdateScheduleEvent
    extends EventSupport
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 274318082221974399L;
	public static final String TASK_UID = "Task Uid";
  public static final String TASK_TYPE = "Task Type";
  public static final String TASK_ID = "Task Id";
  public static final String IS_DISABLE = "Is Disable";
  public static final String FREQUENCY = "Frequency";
  public static final String START_DATE = "Start Date";
  public static final String START_TIME = "Start Time";
  public static final String COUNT = "Count";
  public static final String INTERVAL = "Interval";
  public static final String DAY_OF_WEEK = "Day Of Week";
  public static final String WEEK_OF_MONTH = "Week Of Month";

  public UpdateScheduleEvent(Long taskUid, String taskType, String taskId,
                             Boolean isDisable, Integer frequency,
                             Date startDate, String startTime,
                             Integer count, Integer interval,
                             List dayOfWeek, Integer weekOfMonth)
  {
    setEventData(TASK_UID, taskUid);
    setEventData(TASK_TYPE, taskType);
    setEventData(TASK_ID, taskId);
    setEventData(IS_DISABLE, isDisable);
    setEventData(FREQUENCY, frequency);
    setEventData(START_DATE, startDate);
    setEventData(START_TIME, startTime);
    setEventData(COUNT, count);
    setEventData(INTERVAL, interval);
    setEventData(DAY_OF_WEEK, dayOfWeek);
    setEventData(WEEK_OF_MONTH, weekOfMonth);
  }

  public Long getTaskUid()
  {
    return (Long) getEventData(TASK_UID);
  }

  public String getTaskType()
  {
    return (String) getEventData(TASK_TYPE);
  }

  public String getTaskId()
  {
    return (String) getEventData(TASK_ID);
  }

  public Boolean isDisable()
  {
    return (Boolean) getEventData(IS_DISABLE);
  }

  public Integer getFrequency()
  {
    return (Integer) getEventData(FREQUENCY);
  }

  public Date getStartDate()
  {
    return (Date) getEventData(START_DATE);
  }

  public String getStartTime()
  {
    return (String) getEventData(START_TIME);
  }

  public Integer getCount()
  {
    return (Integer) getEventData(COUNT);
  }

  public Integer getInterval()
  {
    return (Integer) getEventData(INTERVAL);
  }

  public List getDayOfWeek()
  {
    return (List) getEventData(DAY_OF_WEEK);
  }

  public Integer getWeekOfMonth()
  {
    return (Integer) getEventData(WEEK_OF_MONTH);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateScheduleEvent";
  }

}
