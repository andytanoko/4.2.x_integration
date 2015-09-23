// %1023788050324:com.gridnode.pdip.base.time.value.util%
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

import java.util.ArrayList;
import java.util.List;

import com.gridnode.pdip.base.time.entities.value.DayEntry;
import com.gridnode.pdip.base.time.entities.value.IByRule;
import com.gridnode.pdip.base.time.entities.value.IFrenqency;

public class ByRuleFilterFn implements IFrenqency, IByRule
{
  static abstract class FilterFn
  {
    int _byRule;

    FilterFn(int byRule)
    {
      _byRule = byRule;
    }

    List cal(RecurData recurData, List occs)
    {

      /* If _byRule has not been specified, or the array is empty, just
      return the array. */
      if (recurData.isEmpty(_byRule) || occs.isEmpty())
        return occs;
      return run(recurData, occs);
    }

    protected abstract List run(RecurData recurData, List occs);
  }

  static class ExpandFn extends FilterFn
  {
    ExpandFn(int byRule)
    {
      super(byRule);
    }

    //    List run(RecurData recurData, List occs)
    //    {
    //       boolean[] dataArray = recurData._data[_byRule];
    //      List newOccs= new ArrayList();
    //      int size= occs.size();
    //      for (int i= 0; i < size; i++)
    //      {
    //        CalObjTime occ= (CalObjTime) occs.get(i);
    //       for(int num =0; num<dataArray.length; num++)
    //       {
    //          if(!dataArray[num])
    //            continue;
    //          CalObjTime newOcc= (CalObjTime) occ.clone();
    //          occ.set(_byRule, num);
    //          newOccs.add(newOcc);
    //        }
    //      }
    //      return newOccs;
    //    }
    //

    /* If the _byRule rule is specified it expands each occurrence in occs, by
    using each of the value in the _byruleList(_byRule) list. */
    protected List run(RecurData recurData, List occs)
    {
      List byruleList = recurData.getByRuleList(_byRule);
      if (byruleList == null || byruleList.isEmpty())
        return occs;
      List newOccs = new ArrayList();
      int size = occs.size();
      for (int i = 0; i < size; i++)
      {
        CalObjTime occ = (CalObjTime) occs.get(i);
        for (int num = 0; num < byruleList.size(); num++)
        {
          int value = ((Integer) byruleList.get(num)).intValue();
          List newOcc = getExpandValue(recurData, occ, value);
          if (newOcc != null)
            newOccs.addAll(newOcc);
        }
      }
      return newOccs;
    }

    List getExpandValue(RecurData recurData, CalObjTime occ, int value)
    {
      CalObjTime newOcc = (CalObjTime) occ.clone();
      newOcc.set(_byRule, value);
      List res = new ArrayList();
      res.add(newOcc);
      return res;
    }
  }

  static class ContractFn extends FilterFn
  {
    ContractFn(int byRule)
    {
      super(byRule);
    }

    protected List run(RecurData recurData, List occs)
    {
      List newOccs = new ArrayList();
      int size = occs.size();
      for (int i = 0; i < size; i++)
      {
        CalObjTime occ = (CalObjTime) occs.get(i);
        if (!shouldStay(recurData, occ))
          continue;
        CalObjTime newOcc = (CalObjTime) occ.clone();
        newOccs.add(newOcc);
      }
      return newOccs;
    }

    boolean shouldStay(RecurData recurData, CalObjTime occ)
    {
      boolean[] dataArray = recurData.getData(_byRule);
      int value = getCompareValue(occ);
      return dataArray[value];
    }

    int getCompareValue(CalObjTime occ)
    {
      return occ.get(_byRule);
    }
  }

  static FilterFn bySetposFilter = new ContractFn(BY_SET_POS)
  {
    protected List run(RecurData recurData, List occs)
    {
      int len = occs.size();
      List byruleList = recurData.getByRuleList(_byRule);
      if (byruleList == null || byruleList.isEmpty() || len <= 0)
        return occs;
      List newOccs = new ArrayList();
      for (int num = 0; num < byruleList.size(); num++)
      {
        int pos = ((Integer) byruleList.get(num)).intValue();

        /* Negative values count back from the end of the array. */
        if (pos < 0)
          pos += len;

        /* Positive values need to be decremented since the array is
        0-based. */
        else
          pos--;
        if (pos >= 0 && pos < len)
        {
          CalObjTime occ = (CalObjTime) occs.get(pos);
          CalObjTime newOcc = (CalObjTime) occ.clone();
          newOccs.add(newOcc);
        }
      }
      return newOccs;
    }
  };
  static FilterFn bySecondExpand = new ExpandFn(BY_SECOND);
  static FilterFn bySecondFilter = new ContractFn(BY_SECOND);
  static FilterFn byMinuteExpand = new ExpandFn(BY_MINUTE);
  static FilterFn byMinuteFilter = new ContractFn(BY_MINUTE);
  static FilterFn byHourExpand = new ExpandFn(BY_HOUR);
  static FilterFn byHourFilter = new ContractFn(BY_HOUR);
  static FilterFn byDayExpandWeekly = new ExpandFn(BY_DAY)
  {
    List getExpandValue(RecurData recurData, CalObjTime occ, int value)
    {

      /* I think we should skip all elements where week_num != 0.
      The spec isn't clear about this. */
      value = DayEntry.dayOfWeek(value);
      CalObjTime newOcc = (CalObjTime) occ.clone();
      int wkstartday = recurData.getWeekStartDay();
      int weekday_offset = occ.weekdayOffset(wkstartday);
      int new_weekday_offset = (value + 7 - wkstartday) % 7;
      newOcc.addDays(new_weekday_offset - weekday_offset);
      List res = new ArrayList();
      res.add(newOcc);
      return res;
    }
  };
  static FilterFn byDayExpandMonthly = new ExpandFn(BY_DAY)
  {
    List getExpandValue(RecurData recurData, CalObjTime occ, int value)
    {
      List res = new ArrayList();
      int weekday = DayEntry.dayOfWeek(value);
      int week_num = DayEntry.weekNo(value);
      int year = occ.year;
      int month = occ.month;
      if (week_num == 0)
      {

        /* Expand to every Mon/Tue/etc. in the month.*/
        CalObjTime tempOcc = (CalObjTime) occ.clone();
        tempOcc.date = 1;
        int first_weekday = tempOcc.getWeekDay();
        int offset = (weekday + 7 - first_weekday) % 7;
        tempOcc.addDays(offset);
        while (tempOcc.year == year && tempOcc.month == month)
        {
          CalObjTime newOcc = (CalObjTime) tempOcc.clone();
          res.add(newOcc);
          tempOcc.addDays(7);
        }
      } else if (week_num > 0)
      {

        /* Add the nth Mon/Tue/etc. in the month. */
        CalObjTime newOcc = (CalObjTime) occ.clone();
        newOcc.date = 1;
        int first_weekday = newOcc.getWeekDay();
        int offset = (weekday + 7 - first_weekday) % 7;
        offset += (week_num - 1) * 7;
        newOcc.addDays(offset);
        if (newOcc.year == year && newOcc.month == month)
          res.add(newOcc);
      } else
      {

        /* Add the -nth Mon/Tue/etc. in the month. */
        CalObjTime newOcc = (CalObjTime) occ.clone();
        newOcc.date = CalObjTime.daysInMonth(newOcc.year, newOcc.month);
        int last_weekday = newOcc.getWeekDay();

        /* This calculates the number of days to step
        backwards from the last day of the month
        to the weekday we want. */
        int offset = (last_weekday + 7 - weekday) % 7;

        /* This adds on the weeks. */
        offset += (-week_num - 1) * 7;
        newOcc.addDays(-offset);
        if (newOcc.year == year && newOcc.month == month)
          res.add(newOcc);
      }
      return res;
    }
  };
  static FilterFn byDayExpandYearly = new ExpandFn(BY_DAY)
  {
    List getExpandValue(RecurData recurData, CalObjTime occ, int value)
    {
      List res = new ArrayList();
      int weekday = DayEntry.dayOfWeek(value);
      int week_num = DayEntry.weekNo(value);
      int year = occ.year;
      if (week_num == 0)
      {

        /* Expand to every Mon/Tue/etc. in the year. */
        /* Expand to every Mon/Tue/etc. in the month.*/
        CalObjTime tempOcc = (CalObjTime) occ.clone();
        tempOcc.date = 1;
        tempOcc.month = 0;
        int first_weekday = tempOcc.getWeekDay();
        int offset = (weekday + 7 - first_weekday) % 7;
        tempOcc.addDays(offset);
        while (tempOcc.year == year)
        {
          CalObjTime newOcc = (CalObjTime) tempOcc.clone();
          res.add(newOcc);
          tempOcc.addDays(7);
        }
      } else if (week_num > 0)
      {

        /* Add the nth Mon/Tue/etc. in the year. */
        CalObjTime newOcc = (CalObjTime) occ.clone();
        newOcc.month = 0;
        newOcc.date = 1;
        int first_weekday = newOcc.getWeekDay();
        int offset = (weekday + 7 - first_weekday) % 7;
        offset += (week_num - 1) * 7;
        newOcc.addDays(offset);
        if (newOcc.year == year)
          res.add(newOcc);
      } else
      {

        /* Add the -nth Mon/Tue/etc. in the year. */
        CalObjTime newOcc = (CalObjTime) occ.clone();
        newOcc.month = 11;
        newOcc.date = 31;
        int last_weekday = newOcc.getWeekDay();
        int offset = (last_weekday + 7 - weekday) % 7;
        offset += (week_num - 1) * 7;
        newOcc.addDays(-offset);
        if (newOcc.year == year)
          res.add(newOcc);
      }
      return res;
    }
  };

  /* Note: occs must not contain invalid dates, e.g. 31st September. */
  static FilterFn byDayFilter = new ContractFn(BY_DAY)
  {
    int getCompareValue(CalObjTime occ)
    {
      return occ.getWeekDay();
    }
  };
  static FilterFn byMonthdayExpand = new ExpandFn(BY_MONTH_DAY)
  {
    List getExpandValue(RecurData recurData, CalObjTime occ, int value)
    {
      CalObjTime cotime = null;

      /* Find the day that would correspond to day 1. */
      CalObjTime month_start_cotime = (CalObjTime) occ.clone();
      month_start_cotime.date = 1;

      /* Find the day that would correspond to day 1 of the next
      month, which we use for -ve day numbers. */
      CalObjTime month_end_cotime = (CalObjTime) occ.clone();
      month_end_cotime.month++;
      month_end_cotime.date = 1;
      if (value > 0)
      {
        cotime = month_start_cotime;
        cotime.addDays(value - 1);
      } else
      {
        cotime = month_end_cotime;
        cotime.addDays(value);
      }

      /* Skip occurrences if they fall outside the month. */
      if (cotime.month != occ.month)
        return null;
      List res = new ArrayList();
      res.add(cotime);
      return res;
    }
  };
  static FilterFn byMonthdayFilter = new ContractFn(BY_MONTH_DAY)
  {
    boolean shouldStay(RecurData recurData, CalObjTime occ)
    {
      boolean[] dataArray = recurData.getData(_byRule);
      int value = getCompareValue(occ);
      if (dataArray[value])
        return true;
      int days_in_month = CalObjTime.daysInMonth(occ.year, occ.month);
      return (recurData.neg_monthdays[days_in_month + 1 - occ.date]);
    }
  };
  static FilterFn byYeardayExpand = new ExpandFn(BY_YEAR_DAY)
  {
    List getExpandValue(RecurData recurData, CalObjTime occ, int value)
    {
      CalObjTime cotime = null;
      if (value > 0)
      {
        CalObjTime year_start_cotime = (CalObjTime) occ.clone();

        /* Find the day that would correspond to day 1. */
        year_start_cotime.month = 0;
        year_start_cotime.date = 1;
        cotime = year_start_cotime;
        cotime.addDays(value - 1);
      } else
      {
        CalObjTime year_end_cotime = (CalObjTime) occ.clone();

        /* Find the day that would correspond to day 1 of the next
        year, which we use for -ve day numbers. */
        year_end_cotime.year++;
        year_end_cotime.month = 0;
        year_end_cotime.date = 1;
        cotime = year_end_cotime;
        cotime.addDays(value);
      }

      /* Skip occurrences if they fall outside the year. */
      if (cotime.year != occ.year)
        return null;
      List res = new ArrayList();
      res.add(cotime);
      return res;
    }
  };
  static FilterFn byYeardayFilter = new ContractFn(BY_YEAR_DAY)
  {
    boolean shouldStay(RecurData recurData, CalObjTime occ)
    {
      boolean[] dataArray = recurData.getData(_byRule);
      int value = getCompareValue(occ);
      if (dataArray[value])
        return true;
      return recurData.neg_yeardays[occ.get(BY_YEAR_DAY) + 1 - value];
    }
  };
  static FilterFn byWeeknoExpand = new ExpandFn(BY_WEEK_NO)
  {
    List getExpandValue(RecurData recurData, CalObjTime occ, int value)
    {
      int week_start = recurData.getWeekStartDay();
      int weekDayOffset = recurData.getWeekOffset();

      /* Find the day that would correspond to week 1 (note that
      week 1 is the first week starting from the specified week
      start day that has 4 days in the new year). */
      CalObjTime year_start_cotime = occ.findFirstWeek(week_start, weekDayOffset);

      /* Find the day that would correspond to week 1 of the next
      year, which we use for -ve week numbers. */
      CalObjTime year_end_cotime = (CalObjTime) occ.clone();
      year_end_cotime.year++;
      year_end_cotime = year_end_cotime.findFirstWeek(week_start, weekDayOffset);
      CalObjTime cotime = null;
      if (value > 0)
      {
        cotime = year_start_cotime;
        cotime.addDays((value - 1) * 7);
      } else
      {
        cotime = year_end_cotime;
        cotime.addDays(value * 7);
      }

      /* Skip occurrences if they fall outside the year. */
      if (cotime.year != occ.year)
        return null;
      List res = new ArrayList();
      res.add(cotime);
      return res;
    }
  };
  static FilterFn byMonthExpand = new ExpandFn(BY_MONTH);
  static FilterFn byMonthFilter = new ContractFn(BY_MONTH);

  static FilterFn get(int freq, int byRuleIndex)
  {
    switch (byRuleIndex)
    {

      case IByRule.BY_SECOND :
        if (freq == SECONDLY)
          return bySecondFilter;
        return bySecondExpand;

      case IByRule.BY_MINUTE :
        if (freq <= MINUTELY)
          return byMinuteFilter;
        return byMinuteExpand;

      case IByRule.BY_HOUR :
        if (freq <= HOURLY)
          return byHourFilter;
        return byHourExpand;

      case IByRule.BY_DAY :
        if (freq <= DAILY)
          return byDayFilter;
        switch (freq)
        {

          case WEEKLY :
            return byDayExpandWeekly;

          case MONTHLY :
            return byDayExpandMonthly;

          case YEARLY :
            return byDayExpandYearly;

          default :
            throw new IllegalArgumentException("freq=" + freq + ";byrule=" + byRuleIndex);
        }

      case IByRule.BY_MONTH_DAY :
        if (freq <= DAILY)
          return byMonthdayFilter;
        switch (freq)
        {

          case MONTHLY :
          case YEARLY :
            return byMonthdayExpand;

          case WEEKLY :
          default :
            throw new IllegalArgumentException("freq=" + freq + ";byrule=" + byRuleIndex);
        }

      case IByRule.BY_YEAR_DAY :
        if (freq <= DAILY)
          return byYeardayFilter;
        switch (freq)
        {

          case YEARLY :
            return byYeardayExpand;

          case WEEKLY :
          case MONTHLY :
          default :
            throw new IllegalArgumentException("freq=" + freq + ";byrule=" + byRuleIndex);
        }

      case IByRule.BY_WEEK_NO :
        if (freq == YEARLY)
          return byWeeknoExpand;
        throw new IllegalArgumentException(); /* BYWEEKNO is only applicable to YEARLY frequency. */

      case IByRule.BY_MONTH :
        if (freq <= MONTHLY)
          return byMonthFilter;
        return byMonthExpand;

      default :
        throw new IllegalArgumentException();
        //      case IByRule.BY_SET_POS:
        //        return Calendar.;
        //
    }
  }
}