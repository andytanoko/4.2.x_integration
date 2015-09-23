// %1023788050652:com.gridnode.pdip.base.time.value.util%
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
 * Apr 03 2012    Tam Wei Xiang       #3471 - Resolve ClassCastException
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.value.util;

import com.gridnode.pdip.base.time.entities.value.DayEntry;
import com.gridnode.pdip.base.time.entities.value.IByRule;
import com.gridnode.pdip.base.time.entities.value.IFrenqency;
import com.gridnode.pdip.base.time.entities.value.iCalRecurrenceV;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

import java.util.ArrayList;
import java.util.List;

/* This is what we use to pass to all the filter functions. */
class RecurData
  implements IFrenqency,
             IByRule
{
  static List[] getClonedListArray(List[] input)
  {
    int size = input.length;
    List[] res = new List[size];
    for (int i = 0; i < size; i++)
      res[i] = input[i] == null
               ? new ArrayList() : (List)((ArrayList)input[i]).clone();
    return res;
  }

  boolean[] getData(int byruleIndice)
  {
    return _data[byruleIndice];
  }

  void setInterval(int interval)
  {
    this.interval = interval;
  }

  int getInterval()
  {
    return interval;
  }

  private void setWeekStartDay(int week_start_day)
  {
    this.weekStartDay = week_start_day;
  }

  int getWeekStartDay()
  {
    return weekStartDay;
  }

  private void setWeekOffset(int weekday_offset)
  {
    this.weekOffset = weekday_offset;
  }

  int getWeekOffset()
  {
    return weekOffset;
  }

  private iCalRecurrenceV recur;
  private int weekOffset;
  private int weekStartDay;
  private int interval;
  private boolean[] _isOrigData = new boolean[9];
  boolean[] months = new boolean[12];
  boolean[] yeardays = new boolean[367];
  boolean[] neg_yeardays = new boolean[367];  /* Days are 1 - 366. */
  boolean[] monthdays = new boolean[32];
  boolean[] neg_monthdays = new boolean[32];  /* Days are 1 to 31. */
  boolean[] weekdays = new boolean[7];
  boolean[] hours = new boolean[24];
  boolean[] minutes = new boolean[60];
  boolean[] seconds = new boolean[62];
  private boolean[][] _data = new boolean[][] 
  {
    seconds, minutes, hours, weekdays, monthdays, yeardays, months
  };

  List getByRuleList(int byRule)
  {
    return _byruleList[byRule];
  }

  private List[] _byruleList;

  RecurData(iCalRecurrenceV value, CalObjTime event_start)
  {
    recur = value;
    _byruleList = getClonedListArray(
                      new List[] 
    {
      recur.getBySecond(), recur.getByMinute(), recur.getByHour(), 
      recur.getByDay(), recur.getByMonthDay(), recur.getByYearDay(), 
      recur.getByWeekNo(), recur.getByMonth(), recur.getBySetPos()
    });
    for (int i = 0; i < _byruleList.length; i++)
    {
      if (_byruleList[i] == null || _byruleList[i].isEmpty())
        _isOrigData[i] = false;
      else
        _isOrigData[i] = true;
    }
    setInterval(recur.getInterval());
    setWeekStartDay(recur.getWeekStart());
    // Set the weekday, used for the WEEKLY frequency and the BYWEEKNO modifier.
    setWeekOffset(event_start.weekdayOffset(getWeekStartDay()));
    for (int i = BY_SECOND; i <= BY_HOUR; i++)
    {
      List ruleList = _byruleList[i];
      boolean[] dataArray = _data[i];
      for (int num = 0; num < ruleList.size(); num++)
      {
        int pos = ((Integer)ruleList.get(num)).intValue();
        dataArray[pos] = true;
      }
    }
    //int month;
    //int yearday;
    //int monthday;
    //int weekday;
    //int week_num;
    List ruleList = getByRuleList(BY_MONTH);
    boolean[] dataArray = months;
    for (int num = 0; num < ruleList.size(); num++)
    {
      int pos = ((Integer)ruleList.get(num)).intValue();
      dataArray[pos] = true;
    }
    //Create an array of yeardays from byyearday for fast lookup. We create a
    //second array to handle the negative values. The first element there corresponds
    //to the last day of the year
    ruleList = getByRuleList(BY_YEAR_DAY);
    for (int num = 0; num < ruleList.size(); num++)
    {
      int pos = ((Integer)ruleList.get(num)).intValue();
      if (pos > 0)
        yeardays[pos] = true;
      else
        neg_yeardays[-pos] = true;
    }
    ruleList = getByRuleList(BY_MONTH_DAY);
    for (int num = 0; num < ruleList.size(); num++)
    {
      int pos = ((Integer)ruleList.get(num)).intValue();
      if (pos > 0)
        monthdays[pos] = true;
      else
        neg_monthdays[-pos] = true;
    }

    /* Create an array of weekdays from byday for fast lookup. and change to ListElement to
 *  Integer instead of DayEntry */
    ruleList = getByRuleList(BY_DAY);
    dataArray = weekdays;
    for (int num = 0; num < ruleList.size(); num++)
    {
      int pos = 0;
      int dayInMonth = 0;
      Object obj = ruleList.get(num);
      
      //#3471 TWX The loading of the icalAlarm somehow will include ArrayList
      //      hence we need to check the obj class type explicitly
      //      See iCalComponentBean.ejbLoad() for detail (ParseMime.parse())
      if(obj instanceof ArrayList)
      {
        ArrayList list = (ArrayList)obj;
        if(list.size() > 0)
        {
          DayEntry tempDayEntry = (DayEntry)list.get(0);
          pos = tempDayEntry.getDay();
          dayInMonth = tempDayEntry.dayEntryToInt();
        }
        else
        {
          throw new IllegalArgumentException("Expecting at least one DayEntry record!");
        }
      }
      else
      {
        DayEntry entry = (DayEntry)ruleList.get(num);
        pos = entry.getDay();
        dayInMonth = entry.dayEntryToInt();
      }
      
      dataArray[pos] = true;
      ruleList.set(num, new Integer(dayInMonth));
    }
  }

  boolean isEmpty(int byRuleIndice)
  {
    return !_isOrigData[byRuleIndice];
  }
  //
  //
  //  static CalRecurrence *
  //cal_recur_from_icalproperty (icalproperty *prop, gboolean exception,
  //              icaltimezone *zone, gboolean convert_end_date)
  //{
  //   struct icalrecurrencetype ir;
  //   CalRecurrence *r;
  //   gint max_elements, i;
  //   GList *elem;
  //
  //   g_return_val_if_fail (prop != NULL, NULL);
  //
  //   r = g_new (CalRecurrence, 1);
  //
  //   if (exception)
  //      ir = icalproperty_get_exrule (prop);
  //   else
  //      ir = icalproperty_get_rrule (prop);
  //
  //   r->freq = ir.freq;
  //   r->interval = ir.interval;
  //
  //   if (ir.count != 0) {
  //      /* If COUNT is set, we use the pre-calculated enddate.
  //         Note that this can be 0 if the RULE doesn't actually
  //         generate COUNT instances. */
  //      r->enddate = cal_recur_get_rule_end_date (prop, convert_end_date ? zone : NULL);
  //   } else {
  //      if (icaltime_is_null_time (ir.until)) {
  //         /* If neither COUNT or UNTIL is set, the event
  //            recurs forever. */
  //         r->enddate = 0;
  //      } else if (ir.until.is_date) {
  //         /* If UNTIL is a DATE, we stop at the end of
  //            the day, in local time (with the DTSTART timezone).
  //            Note that UNTIL is inclusive so we stop before
  //            midnight. */
  //         ir.until.hour = 23;
  //         ir.until.minute = 59;
  //         ir.until.second = 59;
  //         ir.until.is_date = FALSE;
  //
  //         r->enddate = icaltime_as_timet_with_zone (ir.until,
  //                          zone);
  //#if 0
  //         g_print ("  until: %li - %s", r->enddate, ctime (&r->enddate));
  //#endif
  //
  //      } else {
  //         /* If UNTIL is a DATE-TIME, it must be in UTC. */
  //         icaltimezone *utc_zone;
  //         utc_zone = icaltimezone_get_utc_timezone ();
  //         r->enddate = icaltime_as_timet_with_zone (ir.until,
  //                          utc_zone);
  //      }
  //   }
}