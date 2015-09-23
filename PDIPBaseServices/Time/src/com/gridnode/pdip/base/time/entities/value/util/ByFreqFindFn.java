// %1023788050137:com.gridnode.pdip.base.time.value.util%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: 
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 */

/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.value.util;

import com.gridnode.pdip.base.time.entities.value.IByRule;
import com.gridnode.pdip.base.time.entities.value.IFrenqency;

public class ByFreqFindFn implements IFrenqency, IByRule
{
  static abstract class FindStartFn
  {
    protected int _freq;

    protected FindStartFn(int freq)
    {
      _freq = freq;
    }

    public boolean run(
      CalObjTime eventStart,
      CalObjTime eventEnd,
      RecurData recurData,
      CalObjTime intervalStart,
      CalObjTime intervaEnd,
      CalObjTime[] cotime)
    {
      if (intervaEnd != null && (eventStart.compare(intervaEnd, _freq) > 0))
        return true;
      if (eventEnd != null && eventEnd.compare(intervalStart, _freq) < 0)
        return true;
      cotime[0] = getRes(eventStart, recurData, intervalStart);
      return reachEnd(recurData, eventEnd, intervaEnd, cotime[0]);
    }

    CalObjTime getRes(CalObjTime eventStart, RecurData recurData, CalObjTime intervalStart)
    {
      CalObjTime res = (CalObjTime) eventStart.clone();
      if (shouldChange(eventStart, intervalStart))
      {
        int seconds = calValue(eventStart, intervalStart);
        seconds += recurData.getInterval() - 1;
        seconds -= seconds % recurData.getInterval();
        addValue(res, seconds);
      }
      return res;
    }

    boolean shouldChange(CalObjTime eventStart, CalObjTime intervalStart)
    {
      return eventStart.compare(intervalStart, _freq) < 0;
    }

    void addValue(CalObjTime cotime, int value)
    {
      cotime.fowardAdd(_freq, value);
    }

    boolean reachEnd(RecurData recur, CalObjTime eventEnd, CalObjTime intervalEnd, CalObjTime cotime)
    {
      if (eventEnd != null && cotime.compare(eventEnd, _freq) > 0)
        return true;
      if (intervalEnd != null && cotime.compare(intervalEnd, _freq) > 0)
        return true;
      return false;
    }

    abstract int calValue(CalObjTime eventStart, CalObjTime intervalStart);
  }

  static class FindNextFn
  {
    protected int _freq;

    FindNextFn(int freq)
    {
      _freq = freq;
    }

    public boolean run(CalObjTime cotime, CalObjTime eventEnd, RecurData recurData, CalObjTime intervalEnd)
    {
      increaseTime(recurData, cotime);
      return reachEnd(recurData, eventEnd, intervalEnd, cotime);
    }

    void increaseTime(RecurData recurData, CalObjTime cotime)
    {
      cotime.fowardAdd(_freq, recurData.getInterval());
    }

    boolean reachEnd(RecurData recurData, CalObjTime eventEnd, CalObjTime intervalEnd, CalObjTime cotime)
    {
      if (eventEnd != null && cotime.compare(eventEnd, _freq) > 0)
        return true;
      if (intervalEnd != null && cotime.compare(intervalEnd, _freq) > 0)
        return true;
      return false;
    }
  }
  //FSP == find Start Position Function
  //FNP == find Next Position  Function
  static FindStartFn secondlyFSP = new FindStartFn(SECONDLY)
  {
    int calValue(CalObjTime eventStart, CalObjTime intervalStart)
    {
      return CalObjTime.computeDuration(eventStart, intervalStart);
    }
  };
  static FindNextFn secondlyFNP = new FindNextFn(SECONDLY);
  static FindStartFn minutelyFSP = new FindStartFn(MINUTELY)
  {
    int calValue(CalObjTime eventStart, CalObjTime intervalStart)
    {
      return CalObjTime.computeDuration(eventStart, intervalStart) / 60;
    }
  };
  static FindNextFn minutelyFNP = new FindNextFn(MINUTELY);
  static FindStartFn hourlyFSP = new FindStartFn(HOURLY)
  {
    int calValue(CalObjTime eventStart, CalObjTime intervalStart)
    {
      return CalObjTime.computeDuration(eventStart, intervalStart) / (60 * 60);
    }
  };
  static FindNextFn hourlyFNP = new FindNextFn(HOURLY);
  static FindStartFn dailyFSP = new FindStartFn(DAILY)
  {
    int calValue(CalObjTime eventStart, CalObjTime intervalStart)
    {
      return CalObjTime.computeDuration(eventStart, intervalStart) / (60 * 60 * 24);
    }
  };
  static FindNextFn dailyFNP = new FindNextFn(DAILY);
  static FindStartFn weeklyFSP = new FindStartFn(DAILY)
  {
    CalObjTime getRes(CalObjTime eventStart, RecurData recurData, CalObjTime intervalStart)
    {
      CalObjTime res = (CalObjTime) eventStart.clone();
      long eventStartJulian = eventStart.getJulianDay();
      eventStartJulian -= recurData.getWeekOffset();
      long intervalStartJulian = intervalStart.getJulianDay();
      int interval_start_weekday_offset = intervalStart.weekdayOffset(recurData.getWeekStartDay());
      intervalStartJulian -= interval_start_weekday_offset;

      /* We want to find the first full week using the recurrence interval
      that intersects the given interval dates. */
      if (eventStartJulian < intervalStartJulian)
      {
        int weeks = new Long((intervalStartJulian - eventStartJulian) / 7).intValue();
        weeks += recurData.getInterval() - 1;
        weeks -= weeks % recurData.getInterval();
        addValue(res, weeks * 7);
      }
      return res;
    }

    int calValue(CalObjTime eventStart, CalObjTime intervalStart)
    {
      return 0;
    }

    boolean reachEnd(RecurData recurData, CalObjTime eventEnd, CalObjTime intervalEnd, CalObjTime cotime)
    {
      CalObjTime week_start = (CalObjTime) cotime.clone();
      week_start.addDays(-recurData.getWeekOffset());
      return super.reachEnd(recurData, eventEnd, intervalEnd, week_start);
    }
  };
  static FindNextFn weeklyFNP = new FindNextFn(DAILY)
  {
    void increaseTime(RecurData recurData, CalObjTime cotime)
    {
      cotime.add(DAILY, recurData.getInterval() * 7);
    }

    boolean reachEnd(RecurData recurData, CalObjTime eventEnd, CalObjTime intervalEnd, CalObjTime cotime)
    {
      CalObjTime week_start = (CalObjTime) cotime.clone();
      week_start.addDays(-recurData.getWeekOffset());
      return super.reachEnd(recurData, eventEnd, intervalEnd, week_start);
    }
  };
  static FindStartFn monthlyFSP = new FindStartFn(MONTHLY)
  {
    int calValue(CalObjTime eventStart, CalObjTime intervalStart)
    {
      return (intervalStart.year - eventStart.year) * 12 + intervalStart.month - eventStart.month;
    }
  };
  static FindNextFn monthlyFNP = new FindNextFn(MONTHLY);
  static FindStartFn yearlyFSP = new FindStartFn(YEARLY)
  {
    int calValue(CalObjTime eventStart, CalObjTime intervalStart)
    {
      return intervalStart.year - eventStart.year;
    }
  };
  static FindNextFn yearlyFNP = new FindNextFn(YEARLY);

  static FindStartFn getStart(int freq)
  {
    switch (freq)
    {

      case SECONDLY :
        return secondlyFSP;

      case MINUTELY :
        return minutelyFSP;

      case HOURLY :
        return hourlyFSP;

      case DAILY :
        return dailyFSP;

      case WEEKLY :
        return weeklyFSP;

      case MONTHLY :
        return monthlyFSP;

      case YEARLY :
        return yearlyFSP;
    }
    throw new IllegalArgumentException("freq = " + freq);
  }

  static FindNextFn getNext(int freq)
  {
    switch (freq)
    {

      case SECONDLY :
        return secondlyFNP;

      case MINUTELY :
        return minutelyFNP;

      case HOURLY :
        return hourlyFNP;

      case DAILY :
        return dailyFNP;

      case WEEKLY :
        return weeklyFNP;

      case MONTHLY :
        return monthlyFNP;

      case YEARLY :
        return yearlyFNP;
    }
    throw new IllegalArgumentException("freq = " + freq);
  }
}