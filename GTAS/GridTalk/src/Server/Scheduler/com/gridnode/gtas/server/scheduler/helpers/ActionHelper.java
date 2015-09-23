/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2004 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 09 2004    Koh Han Sing        Created
 * Feb 17 2004    Neo Sok Lay         Refactored the Actions and moved most
 *                                    functionalities here. Added:
 *                                    - constructICalEvent()
 *                                    - createAlarmFromSchedule()
 *                                    - updateAlarmFromSchedule()
 *                                    - findSchedulesByFilter()
 * Apr 03 2012    Tam Wei Xiang       #3471 - Resolve ClassCastException 
 */
package com.gridnode.gtas.server.scheduler.helpers;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gridnode.gtas.model.scheduler.IScheduleFrequency;
import com.gridnode.gtas.model.scheduler.ScheduleEntityFieldID;
import com.gridnode.gtas.server.scheduler.model.Schedule;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.time.entities.model.iCalEvent;
import com.gridnode.pdip.base.time.entities.value.*;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class ActionHelper
{
  private static final MessageFormat _textTimeFormat =
    new MessageFormat("{0,number,00}:{1,number,00}");

  /**
   * Obtain the EJBObject for the iCalTimeMgrBean.
   *
   * @return The EJBObject to the iCalTimeMgrBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.2.10
   */
  public static IiCalTimeMgrObj getManager() throws ServiceLookupException
  {
    return (IiCalTimeMgrObj) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        IiCalTimeMgrHome.class.getName(),
        IiCalTimeMgrHome.class,
        new Object[0]);
  }

  /**
   * Convert an iCalAlarm to Schedule object.
   *
   * @param alarm The iCalAlarm to convert.
   * @return A Schedule object converted from the specified iCalAlarm.
   *
   * @since 2.2.10
   */
  public static Schedule convertiCalAlarmToSchedule(iCalAlarm alarm)
  {
    Schedule schedule = new Schedule();
    schedule.setUId(alarm.getUId());
    schedule.setTaskType(alarm.getCategory());
    schedule.setTaskId(alarm.getTaskId());
    schedule.setDisabled(alarm.getDisabled());
    try
    {
      iCalEvent event = getManager().getEvent(alarm.getParentUid());

      Calendar startTime = Calendar.getInstance();
      startTime.setTime(event.getStartDt());
      schedule.setStartDate(startTime.getTime());

      SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

      if (event.hasNoRecurrence())
      {
        schedule.setFrequency(new Integer(IScheduleFrequency.ONCE));
        schedule.setTimesToRun(new Integer(1));
        schedule.setStartTime(timeFormat.format(startTime.getTime()));
      }
      else
      {
        List props =
          event.getPropertyList(iCalPropertyKind.ICAL_RRULE_PROPERTY);
        for (Iterator i = props.iterator(); i.hasNext();)
        {
          Object obj = i.next();
          iCalPropertyV prop = (iCalPropertyV) obj;
          iCalValueV value = prop.getValue();
          if (value instanceof iCalRecurrenceV)
          {
            iCalRecurrenceV recur = (iCalRecurrenceV) value;
            int interval = recur.getInterval();
            schedule.setDelayPeriod(new Integer(interval));
            int frequency = recur.getFrequency();
            schedule.setFrequency(new Integer(frequency));

            switch (frequency)
            {
              case IScheduleFrequency.MINUTELY :
              case IScheduleFrequency.HOURLY :
              case IScheduleFrequency.DAILY :
                schedule.setStartTime(timeFormat.format(startTime.getTime()));
                break;

              case IScheduleFrequency.WEEKLY :
                ArrayList newDaysOfWeek = new ArrayList();
                List daysOfWeek = recur.getByDay();
                for (Iterator j = daysOfWeek.iterator(); j.hasNext();)
                {
                  
                  //#3471 TWX The loading of the icalAlarm somehow will include ArrayList
                  //      hence we need to check the obj class type explicitly
                  //      See iCalComponentBean.ejbLoad() for detail (ParseMime.parse())
                  Object entryObj = j.next();
                  int weekDay = 0;
                  if(entryObj instanceof ArrayList)
                  {
                    ArrayList entryList = (ArrayList)entryObj;
                    if(entryList.size() > 0)
                    {
                      DayEntry entry = (DayEntry)entryList.get(0);
                      weekDay = entry.getDay();
                    }
                    else
                    {
                      throw new IllegalArgumentException("Expecting at least one DayEntry!");
                    }
                  }
                  else
                  {
                    DayEntry entry = (DayEntry) entryObj;
                    weekDay = entry.getDay();
                  }
                  
                  newDaysOfWeek.add(new Integer(weekDay));
                }
                schedule.setDaysOfWeek(newDaysOfWeek);
                
                if(recur.getByHour() != null && recur.getByMinute() != null)
                {
                  schedule.setStartTime(
                    formatTime(
                      recur.getByHour().get(0),
                      recur.getByMinute().get(0)));
                }
                break;

              case IScheduleFrequency.MONTHLY :
                List daysOfMonth = recur.getByMonthDay();
                List daysOfWeekMth = recur.getByDay();
                if (daysOfMonth != null && !daysOfMonth.isEmpty())
                {
                  int dayOfMonth = ((Integer) daysOfMonth.get(0)).intValue();
                  schedule.setDelayPeriod(new Integer(dayOfMonth));
                }
                else if (daysOfWeekMth != null && !daysOfWeekMth.isEmpty())
                {
                  ArrayList newDayOfWeek = new ArrayList();
                  DayEntry dayOfWeek = (DayEntry) daysOfWeekMth.get(0);
                  newDayOfWeek.add(new Integer(dayOfWeek.getDay()));
                  Integer weekOfMonth = new Integer(dayOfWeek.getWeekNo());
                  schedule.setDaysOfWeek(newDayOfWeek);
                  schedule.setWeekOfMonth(weekOfMonth);
                  schedule.setDelayPeriod(null);
                }
                
                if(recur.getByHour() != null && recur.getByMinute() != null)
                {
                  schedule.setStartTime(
                    formatTime(
                      recur.getByHour().get(0),
                      recur.getByMinute().get(0)));
                }
                break;
            }
            int count = recur.getCount();
            schedule.setTimesToRun(count == 0 ? null : new Integer(count));
          }
        }
      }
    }
    catch (Exception ex)
    {
      Logger.warn(
        "Error converting to Schedule from iCalAlarm :" + alarm.toString(),
        ex);
    }
    return schedule;
  }

  private static String formatTime(Object hour, Object time)
  {
    return _textTimeFormat.format(new Object[] { hour, time });
  }

  /**
   * Convert a collection of iCalAlarm to Schedule objects.
   *
   * @param alarmList The collection of iCalAlarm to convert.
   * @return A Collection of Schedule objects converted from the specified
   * collection of iCalAlarms.
   *
   * @since 2.2.10
   */
  public static Collection convertiCalAlarmToSchedules(Collection alarmList)
  {
    ArrayList schedules = new ArrayList();
    for (Iterator i = alarmList.iterator(); i.hasNext();)
    {
      iCalAlarm alarm = (iCalAlarm) i.next();
      Schedule schedule = convertiCalAlarmToSchedule(alarm);
      schedules.add(schedule);
    }
    return schedules;
  }

  /**
   * Convert an Schedule to Map object.
   *
   * @param schedule The Schedule to convert.
   * @return A Map object converted from the specified Schedule.
   *
   * @since 2.2.10
   */
  public static Map convertScheduleToMap(Schedule schedule)
  {
    return Schedule.convertToMap(
      schedule,
      ScheduleEntityFieldID.getEntityFieldID(),
      null);
  }

  /**
   * Convert a collection of Schedule to Map objects.
   *
   * @param scheduleList The collection of Schedule to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of Schedules.
   *
   * @since 2.2.10
   */
  public static Collection convertScheduleToMapObjects(Collection scheduleList)
  {
    return Schedule.convertEntitiesToMap(
      (Schedule[]) scheduleList.toArray(new Schedule[scheduleList.size()]),
      ScheduleEntityFieldID.getEntityFieldID(),
      null);
  }

  /**
   * Construct an iCalEvent entity based on the specified parameters.
   * 
   * @param eventUid The UID of the existing iCalEvent, if any, to be updated with the parameter values.
   * @param startDate Start Date of the event
   * @param startTime Running time of the event
   * @param frequency Type of Run frequency
   * @param interval Interval of Run frequency
   * @param count Number of times to restrict the running. If not specified, there will not be any restriction.
   * @param daysOfWeek List of Days in a week to run the event
   * @param weekOfMonth The Week of Month to run the event
   * @return The constructed iCalEvent
   * @throws Exception Error retrieving the existing iCalEvent using the specified eventUid, if any.
   */
  public static iCalEvent constructICalEvent(
    Long eventUid,
    Date startDate,
    String startTime,
    Integer frequency,
    Integer interval,
    Integer count,
    List daysOfWeek,
    Integer weekOfMonth)
    throws Exception
  {
    iCalEvent newiCalEvent =
      eventUid == null
        ? new iCalEvent()
        : ActionHelper.getManager().getEvent(eventUid);

    iCalRecurrenceV recur = new iCalRecurrenceV();

    Calendar startDateCal = Calendar.getInstance();
    startDateCal.clear();
    startDateCal.setTime(startDate);

    Integer startHour =
      new Integer(startTime.substring(0, startTime.indexOf(":")));
    ArrayList byHour = new ArrayList();
    byHour.add(startHour);

    Integer startMin =
      new Integer(startTime.substring(startTime.indexOf(":") + 1));
    ArrayList byMin = new ArrayList();
    byMin.add(startMin);

    int scheduleFrequency = frequency.intValue();
    switch (scheduleFrequency)
    {
      case IScheduleFrequency.ONCE :
        startDateCal.set(Calendar.HOUR, startHour.intValue());
        startDateCal.set(Calendar.MINUTE, startMin.intValue());
        break;

      case IScheduleFrequency.MINUTELY :
        recur.setFrequency(IFrenqency.MINUTELY);
        recur.setInterval(interval.intValue());

        startDateCal.set(Calendar.HOUR, startHour.intValue());
        startDateCal.set(Calendar.MINUTE, startMin.intValue());
        break;

      case IScheduleFrequency.HOURLY :
        recur.setFrequency(IFrenqency.HOURLY);
        recur.setInterval(interval.intValue());

        startDateCal.set(Calendar.HOUR, startHour.intValue());
        startDateCal.set(Calendar.MINUTE, startMin.intValue());
        break;

      case IScheduleFrequency.DAILY :
        recur.setFrequency(IFrenqency.DAILY);
        recur.setInterval(interval.intValue());

        startDateCal.set(Calendar.HOUR, startHour.intValue());
        startDateCal.set(Calendar.MINUTE, startMin.intValue());
        break;

      case IScheduleFrequency.WEEKLY :
        recur.setFrequency(IFrenqency.WEEKLY);
        recur.setInterval(interval.intValue());
        List byDay = new ArrayList();
        for (Iterator i = daysOfWeek.iterator(); i.hasNext();)
        {
          int weekDay = ((Integer) i.next()).intValue();
          byDay.add(new DayEntry(weekDay));
        }
        recur.setByDay(byDay);
        recur.setByHour(byHour);
        recur.setByMinute(byMin);
        break;

      case IScheduleFrequency.MONTHLY :
        recur.setFrequency(IFrenqency.MONTHLY);
        if (interval != null)
        {
          List byMonthDay = new ArrayList();
          byMonthDay.add(interval);
          recur.setByMonthDay(byMonthDay);
        }
        else if (weekOfMonth != null)
        {
          int weekDay = ((Integer) daysOfWeek.iterator().next()).intValue();
          List byWeekDay = new ArrayList();
          byWeekDay.add(new DayEntry(weekOfMonth.intValue(), weekDay));
          recur.setByDay(byWeekDay);
        }
        recur.setByHour(byHour);
        recur.setByMinute(byMin);
        break;
    }

    recur.setCount(count == null ? 0 : count.intValue());

    ArrayList props = new ArrayList();
    if (scheduleFrequency != IScheduleFrequency.ONCE)
    {
      List recProps =
        newiCalEvent.getPropertyList(iCalPropertyKind.ICAL_RRULE_PROPERTY);
      iCalPropertyV prop = null;
      if (recProps == null || recProps.isEmpty())
      {
        prop = new iCalPropertyV(iCalPropertyKind.ICAL_RRULE_PROPERTY);
        prop.setValue(recur);
      }
      else
      {
        prop = (iCalPropertyV) recProps.iterator().next();
      }
      prop.setValue(recur);
      props.add(prop);
    }
    newiCalEvent.setProperties(props);

    newiCalEvent.setStartDt(startDateCal.getTime());
    newiCalEvent.setEndDt(null);

    return newiCalEvent;
  }
  
  /**
   * Create an iCalAlarm based on the values in the Schedule entity.
   * 
   * @param schedule The Schedule containing the values
   * @return The iCalAlarm created in the database
   * @throws Exception Error creating the iCalAlarm in database
   */
  public static iCalAlarm createAlarmFromSchedule(Schedule schedule) throws Exception
  {
    iCalAlarm newiCalAlarm = new iCalAlarm();
    iCalEvent newiCalEvent = ActionHelper.constructICalEvent(
                                  null,
                                  schedule.getStartDate(),
                                  schedule.getStartTime(),
                                  schedule.getFrequency(),
                                  schedule.getDelayPeriod(),
                                  schedule.getTimesToRun(),
                                  schedule.getDaysOfWeek(),
                                  schedule.getWeekOfMonth());

    long uid = ActionHelper.getManager().addEvent(newiCalEvent).getUId();

    newiCalAlarm.setParentUid(new Long(uid));
    newiCalAlarm.setParentKind(iCalEvent.KIND_EVENT);

    newiCalAlarm.setCategory(schedule.getTaskType());
    newiCalAlarm.setTaskId(schedule.getTaskId());
    newiCalAlarm.setDisabled(schedule.getDisabled());
    newiCalAlarm.setRelated(new Integer(IRelated.START));
    /*040223NSL not required anymore
    int frequency = schedule.getFrequency().intValue();
    if (frequency == IFrenqency.WEEKLY || frequency == IFrenqency.MONTHLY)
    {
      // this is to prevent the task from running on the start date
      newiCalAlarm.setCurRecur(AlarmCaculator.date2Str(schedule.getStartDate()));
    }
    */
    return ActionHelper.getManager().addAlarm(newiCalAlarm);
  }
  
  /**
   * Update the iCalAlarm using the current values in the Schedule entity.
   * 
   * @param schedule The Schedule with new values
   * @param alarm The alarm to be updated.
   * @throws Exception Error updating the iCalAlarm in the database.
   */
  public static void updateAlarmFromSchedule(Schedule schedule, iCalAlarm alarm) throws Exception
  {
    Long eventUid = alarm.getParentUid();

    iCalEvent updiCalEvent = ActionHelper.constructICalEvent(
                                eventUid,
                                schedule.getStartDate(),
                                schedule.getStartTime(),
                                schedule.getFrequency(),
                                schedule.getDelayPeriod(),
                                schedule.getTimesToRun(),
                                schedule.getDaysOfWeek(),
                                schedule.getWeekOfMonth());

    ActionHelper.getManager().updateEvent(updiCalEvent, false);

    alarm.setCategory(schedule.getTaskType());
    alarm.setTaskId(schedule.getTaskId());
    alarm.setDisabled(schedule.getDisabled());

    /*040223NSL not required anymore
    int frequency = schedule.getFrequency().intValue();
    if (frequency == IFrenqency.WEEKLY || frequency == IFrenqency.MONTHLY)
    {
      // this is to prevent the task from running on the start date
      alarm.setCurRecur(AlarmCaculator.date2Str(schedule.getStartDate()));
    }
    */
    ActionHelper.getManager().updateAlarm(alarm, true);
  }
  
  /**
   * Find Schedule(s) base on filtering condition. The filtering fields must only be the 
   * fields existing in iCalAlarm.
   * 
   * @param filter The filtering condition
   * @return Collection of Schedule entities found.
   * @throws Exception Error executing the find.
   */
  public static Collection findSchedulesByFilter(IDataFilter filter) throws Exception
  {
    Collection alarms = ActionHelper.getManager().findAlarms(filter);
    return ActionHelper.convertiCalAlarmToSchedules(alarms);
  }
}
