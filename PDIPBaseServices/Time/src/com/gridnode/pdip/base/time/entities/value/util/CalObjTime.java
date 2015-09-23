// %1023788050418:com.gridnode.pdip.base.time.value.util%
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.gridnode.pdip.base.time.entities.value.IByRule;
import com.gridnode.pdip.base.time.entities.value.IFrenqency;
import com.gridnode.pdip.base.time.entities.value.IWeekDay;
 
class CalObjTime
{
  int year;
  int month;  /* 0 - 11 */
  int date;  /* 1 - 31 */
  int hour;  /* 0 - 23 */
  int minute;  /* 0 - 59 */
  int second;  /* 0 - 59 (maybe up to 61 for leap seconds) */
  boolean flags = false;

  /* The meaning of this depends on where the CalObjTime is used. In most cases this
 *  is set to TRUE to indicate that this is an RDATE with an end or a duration set.
 *  In the exceptions code, this is set to TRUE to indicate that this is an EXDATE
 * with a  DATE value. */
  CalObjTime(int ayear, int amonth, int adate, int ahour, int aminute, 
             int asecond)
  {
    this.year = ayear;
    this.month = amonth;
    this.date = adate;
    this.hour = ahour;
    this.minute = aminute;
    this.second = asecond;
  }

  CalObjTime(Date date)
  {
    GregorianCalendar tempcal = new GregorianCalendar();
    tempcal.setTime(date);
    getField(tempcal);
    this.flags = false;
  }

  private void getField(GregorianCalendar cal)
  {
    year = cal.get(Calendar.YEAR);
    month = cal.get(Calendar.MONTH);
    date = cal.get(Calendar.DATE);
    hour = cal.get(Calendar.HOUR_OF_DAY);
    minute = cal.get(Calendar.MINUTE);
    second = cal.get(Calendar.SECOND);
  }

  protected CalObjTime()
  {
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Object clone()
  {
    CalObjTime res = new CalObjTime();
    res.copy(this);
    return res;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param another DOCUMENT ME!
   */
  public void copy(CalObjTime another)
  {
    this.year = another.year;
    this.month = another.month;
    this.date = another.date;
    this.hour = another.hour;
    this.minute = another.minute;
    this.second = another.second;
    this.flags = another.flags;
  }

  Date toDate()
  {
    GregorianCalendar tempcal = getCalendar();
    return tempcal.getTime();
  }

  void set(int byRuleIndice, int value)
  {
    switch (byRuleIndice)
    {

    case IByRule.BY_SECOND:
      second = value;
      break;

    case IByRule.BY_MINUTE:
      minute = value;
      break;

    case IByRule.BY_HOUR:
      hour = value;
      break;

    //              case IByRule.BY_DAY :
    //                return Calendar.DAY_OF_WEEK;
    //              case IByRule.BY_MONTH_DAY :
    //                return Calendar.DAY_OF_MONTH;
    //              case IByRule.BY_YEAR_DAY :
    //                return Calendar.DAY_OF_YEAR;
    case IByRule.BY_MONTH:
      month = value;
      break;

    default:
      throw new IllegalArgumentException();
    }
  }

  int get(int byRuleIndice)
  {
    if (byRuleIndice == IByRule.BY_DAY)
      return getWeekDay();
    int calindex = getCalendarIndex(byRuleIndice);
    GregorianCalendar tempcal = getCalendar();
    return tempcal.get(calindex);
  }

  private GregorianCalendar getCalendar()
  {
    return new GregorianCalendar(year, month, date, hour, minute, second);
  }

  void add(int freq, int value)
  {
    int calindex = getCalendarIndexByFreq(freq);
    GregorianCalendar tempcal = getCalendar();
    tempcal.add(calindex, value);
    getField(tempcal);
  }

  int getWeekDay()  //
  {
    GregorianCalendar tempcal = getCalendar();
    return weekDayCal2IWeek(tempcal.get(Calendar.DAY_OF_WEEK));
  }

  static int weekDayCal2IWeek(int weekDay)
  {
    switch (weekDay)
    {

    case Calendar.MONDAY:
      return IWeekDay.MONDAY;

    case Calendar.TUESDAY:
      return IWeekDay.TUESDAY;

    case Calendar.WEDNESDAY:
      return IWeekDay.WEDNESDAY;

    case Calendar.THURSDAY:
      return IWeekDay.THURSDAY;

    case Calendar.FRIDAY:
      return IWeekDay.FRIDAY;

    case Calendar.SATURDAY:
      return IWeekDay.SATURDAY;

    case Calendar.SUNDAY:
      return IWeekDay.SUNDAY;
    }
    return -1;
  }

  void addDays(int value)
  {
    add(IFrenqency.DAILY, value);
  }

  void addSecond(int second)
  {
    add(IFrenqency.SECONDLY, second);
  }

  //only add the value to the larger scale, don't change it for the small
  //it only affects the freq of month and year;  The day may now be invalid, e.g. 29th Feb
  void fowardAdd(int freq, int value)
  {
    if (freq == IFrenqency.YEARLY)
    {
      year = year + value;
      return;
    }
    if (freq == IFrenqency.MONTHLY)
    {
      int total = (value + this.month);
      this.month = total % 12;
      if (total > 0)
      {
        this.year += total / 12;
      }
      else
      {
        int years = total / 12;
        if (this.month != 0)
        {
          this.month += 12;
          years -= 1;
        }
        this.year += years;
      }
      return;
    }
    add(freq, value);
  }

  /* Returns the weekday of the given CalObjTime, from 0 - 6. The week start
day is Monday by default, but can be set in the recurrence rule. */
  int weekdayOffset(int wkstartday)
  {
    int weekday = getWeekDay();
    int offset = (weekday + 7 - wkstartday) % 7;
    return offset;
  }

  /* Finds the first week in the given CalObjTime's year, using the same weekday
   as the event start day (i.e. from the RecurData).The first week of the year is the first week starting from the specified
   week start day that has 4 days in the new year. It may be in the previous
   year. */
  CalObjTime findFirstWeek(int wkstartday, int weekday_offset)
  {
    GregorianCalendar tempcal = new GregorianCalendar(year, 0, 1);
    int weekday = weekDayCal2IWeek(tempcal.get(Calendar.DAY_OF_WEEK));
    int first_full_week_start_offset = (wkstartday + 7 - weekday) % 7;
    if (first_full_week_start_offset >= 4)
      first_full_week_start_offset -= 7;

    /* Now add the days to get to the event's weekday. */
    int offset = first_full_week_start_offset + weekday_offset;
    CalObjTime res = new CalObjTime();
    res.year = year;
    res.month = 0;
    res.date = 1;
    res.addDays(offset);
    res.hour = this.hour;
    res.minute = this.minute;
    res.second = this.second;
    return res;
  }

  int compare(CalObjTime another, int freq)
  {
    if (this.year < another.year)
      return -1;
    if (this.year > another.year)
      return 1;
    if (freq == IFrenqency.YEARLY)
      return 0;
    if (this.month < another.month)
      return -1;
    if (this.month > another.month)
      return 1;
    if (freq == IFrenqency.MONTHLY)
      return 0;
    if (this.date < another.date)
      return -1;
    if (this.date > another.date)
      return 1;
    if (freq == IFrenqency.DAILY)
      return 0;
    if (this.hour < another.hour)
      return -1;
    if (this.hour > another.hour)
      return 1;
    if (freq == IFrenqency.HOURLY)
      return 0;
    if (this.minute < another.minute)
      return -1;
    if (this.minute > another.minute)
      return 1;
    if (freq == IFrenqency.MINUTELY)
      return 0;
    if (this.second < another.second)
      return -1;
    if (this.second > another.second)
      return 1;
    return 0;
  }

  int compareTo(CalObjTime another)
  {
    return compare(another, IFrenqency.SECONDLY);
  }

  int compareDate(CalObjTime another)
  {
    return compare(another, IFrenqency.DAILY);
  }

  /**
   * DOCUMENT ME!
   * 
   * @param another DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public boolean equals(Object another)
  {
    if (another == null || !(another instanceof CalObjTime))
      return false;
    if (flags != ((CalObjTime)another).flags)
      return false;
    return (0 == compareTo((CalObjTime)another));
  }

  static int computeDuration(CalObjTime start, CalObjTime end)
  {
    long start_seconds = start.toDate().getTime() / 1000;
    long end_seconds = end.toDate().getTime() / 1000;
    int seconds = new Long(end_seconds - start_seconds).intValue();
    return seconds;
  }

  static final String[] weekdays = new String[] 
  {
    "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
  };

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String toString()
  {
    int weekDay = getWeekDay();
    return weekdays[weekDay] + " " + date + " " + (month + 1) + " " + year + 
           " " + hour + ":" + minute + ":" + second;
  }

  static int getCalendarIndex(int byRuleIndex)
  {
    switch (byRuleIndex)
    {

    case IByRule.BY_SECOND:
      return Calendar.SECOND;

    case IByRule.BY_MINUTE:
      return Calendar.MINUTE;

    case IByRule.BY_HOUR:
      return Calendar.HOUR_OF_DAY;

    //      case IByRule.BY_DAY :
    //        return Calendar.DAY_OF_WEEK;
    case IByRule.BY_MONTH_DAY:
      return Calendar.DAY_OF_MONTH;

    case IByRule.BY_YEAR_DAY:
      return Calendar.DAY_OF_YEAR;

    case IByRule.BY_MONTH:
      return Calendar.MONTH;

    case IByRule.BY_WEEK_NO:
      return Calendar.WEEK_OF_YEAR;

    default:
      throw new IllegalArgumentException("byRuleIndex = " + byRuleIndex);
      //      case IByRule.BY_SET_POS:
      //        return Calendar.;
      //
    }
  }

  static int getCalendarIndexByFreq(int freq)
  {
    switch (freq)
    {

    case IFrenqency.SECONDLY:
      return Calendar.SECOND;

    case IFrenqency.MINUTELY:
      return Calendar.MINUTE;

    case IFrenqency.HOURLY:
      return Calendar.HOUR_OF_DAY;

    case IFrenqency.DAILY:
      return Calendar.DAY_OF_MONTH;

    case IFrenqency.MONTHLY:
      return Calendar.MONTH;

    case IFrenqency.YEARLY:
      return Calendar.YEAR;

    default:
      throw new IllegalArgumentException("freq = " + freq);
      //  case IFrenqency.WEEKLY:
    }
  }

  static int daysInMonth(int year, int month)
  {
    GregorianCalendar tempcal = new GregorianCalendar();
    tempcal.set(year, month, 1);
    return tempcal.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  private static final int EPOCH_JULIAN_DAY = 2440588;

  // Jaunary 1, 1970 (Gregorian)
  long getJulianDay()
  {
    GregorianCalendar tempcal = new GregorianCalendar(year, month, date);
    long milsec = tempcal.getTime().getTime();
    return EPOCH_JULIAN_DAY + floorDivide(milsec, 24 * 60 * 60 * 1000);
  }

  /**
   * Divide two integers, returning the floor of the quotient.
   * 
   * <p>
   * Unlike the built-in division, this is mathematically well-behaved. E.g.,
   * <code>-1/4</code> => 0 but <code>floorDivide(-1,4)</code> => -1.
   * </p>
   * 
   * @param numerator the numerator
   * @param denominator a divisor which must be > 0
   * @return the floor of the quotient.
   */
  private static final long floorDivide(long numerator, int denominator)
  {
    // We do this computation in order to handle
    // a numerator of Integer.MIN_VALUE correctly
    return (numerator >= 0)
           ? numerator / denominator : ((numerator + 1) / denominator) - 1;
  }
}