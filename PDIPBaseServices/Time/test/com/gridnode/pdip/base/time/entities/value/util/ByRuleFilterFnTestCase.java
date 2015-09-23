// %1023788051512:com.gridnode.pdip.base.time.entities.value.util%
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

import com.gridnode.pdip.base.time.entities.model.iCalEvent;
import com.gridnode.pdip.base.time.entities.value.DayEntry;
import com.gridnode.pdip.base.time.entities.value.IByRule;
import com.gridnode.pdip.base.time.entities.value.IFrenqency;
import com.gridnode.pdip.base.time.entities.value.IWeekDay;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;
import com.gridnode.pdip.base.time.entities.value.iCalRecurrenceV;
import com.gridnode.pdip.base.time.entities.value.util.ByRuleFilterFn.FilterFn;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ByRuleFilterFnTestCase
  extends TestCase
  implements IByRule
{
  static String LogCat = "ByRuleFilterFnTestCase";
  iCalEvent valueEntity = null;
  iCalPropertyV prop = null;

  /**
   * Creates a new ByRuleFilterFnTestCase object.
   * 
   * @param name DOCUMENT ME!
   */
  public ByRuleFilterFnTestCase(String name)
  {
    super(name);
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public static Test suite()
  {
    return new TestSuite(ByRuleFilterFnTestCase.class);
  }

  /**
   * DOCUMENT ME!
   */
  public void setUp()
  {
  }

  /**
   * DOCUMENT ME!
   */
  public void tearDown()
  {
  }

  /**
   * DOCUMENT ME!
   */
  public void testConvert2Date()
  {
    iCalRecurrenceV recur = new iCalRecurrenceV();
    recur.setFrequency(IFrenqency.MONTHLY);
    recur.setInterval(2);
    recur.setUntil(new GregorianCalendar(2002, 11, 31, 0, 0, 0).getTime());
    List byDay = new ArrayList();
    byDay.add(new DayEntry(IWeekDay.MONDAY));
    byDay.add(new DayEntry(2, IWeekDay.SATURDAY));
    byDay.add(new DayEntry(-2, IWeekDay.WEDNESDAY));
    recur.setByDay(byDay);
    List byHour = new ArrayList();
    byHour.add(new Integer(10));
    byHour.add(new Integer(11));
    byHour.add(new Integer(12));
    recur.setByHour(byHour);
    RecurData recurdata = new RecurData(recur, 
                                        new CalObjTime(2002, 4, 10, 0, 0, 0));
  }

  /**
   * DOCUMENT ME!
   */
  public void testGetFn()
  {
    class TestData
    {
      int freq;
      int byRule;
      FilterFn res;

      TestData(int freq, int byRule, FilterFn res)
      {
        this.freq = freq;
        this.byRule = byRule;
        this.res = res;
      }
    }

    TestData[] testDataList = new TestData[] 
    {

      new TestData(IFrenqency.YEARLY, IByRule.BY_MONTH, 
                   ByRuleFilterFn.byMonthExpand), 
      new TestData(IFrenqency.YEARLY, IByRule.BY_WEEK_NO, 
                   ByRuleFilterFn.byWeeknoExpand), 
      new TestData(IFrenqency.YEARLY, IByRule.BY_YEAR_DAY, 
                   ByRuleFilterFn.byYeardayExpand), 
      new TestData(IFrenqency.YEARLY, IByRule.BY_MONTH_DAY, 
                   ByRuleFilterFn.byMonthdayExpand), 
      new TestData(IFrenqency.YEARLY, IByRule.BY_DAY, 
                   ByRuleFilterFn.byDayExpandYearly), 
      new TestData(IFrenqency.YEARLY, IByRule.BY_HOUR, 
                   ByRuleFilterFn.byHourExpand), 
      new TestData(IFrenqency.YEARLY, IByRule.BY_MINUTE, 
                   ByRuleFilterFn.byMinuteExpand), 
      new TestData(IFrenqency.YEARLY, IByRule.BY_SECOND, 
                   ByRuleFilterFn.bySecondExpand), 
      new TestData(IFrenqency.MONTHLY, IByRule.BY_MONTH, 
                   ByRuleFilterFn.byMonthFilter), 
      //    new TestData( IFrenqency.MONTHLY, IByRule.BY_WEEK_NO, ByRuleFilterFn.byweeknoExpand),
      //    new TestData( IFrenqency.MONTHLY, IByRule.BY_YEAR_DAY, ByRuleFilterFn.byyeardayExpand),
      new TestData(IFrenqency.MONTHLY, IByRule.BY_MONTH_DAY, 
                   ByRuleFilterFn.byMonthdayExpand), 
      new TestData(IFrenqency.MONTHLY, IByRule.BY_DAY, 
                   ByRuleFilterFn.byDayExpandMonthly), 
      new TestData(IFrenqency.MONTHLY, IByRule.BY_HOUR, 
                   ByRuleFilterFn.byHourExpand), 
      new TestData(IFrenqency.MONTHLY, IByRule.BY_MINUTE, 
                   ByRuleFilterFn.byMinuteExpand), 
      new TestData(IFrenqency.MONTHLY, IByRule.BY_SECOND, 
                   ByRuleFilterFn.bySecondExpand), 
      new TestData(IFrenqency.WEEKLY, IByRule.BY_MONTH, 
                   ByRuleFilterFn.byMonthFilter), 
      //    new TestData( IFrenqency.WEEKLY, IByRule.BY_WEEK_NO, ByRuleFilterFn.byweeknoExpand),
      //    new TestData( IFrenqency.WEEKLY, IByRule.BY_YEAR_DAY, ByRuleFilterFn.byyeardayExpand),
      // new TestData( IFrenqency.WEEKLY, IByRule.BY_MONTH_DAY, ByRuleFilterFn.bymonthdayExpand),
      new TestData(IFrenqency.WEEKLY, IByRule.BY_DAY, 
                   ByRuleFilterFn.byDayExpandWeekly), 
      new TestData(IFrenqency.WEEKLY, IByRule.BY_HOUR, 
                   ByRuleFilterFn.byHourExpand), 
      new TestData(IFrenqency.WEEKLY, IByRule.BY_MINUTE, 
                   ByRuleFilterFn.byMinuteExpand), 
      new TestData(IFrenqency.WEEKLY, IByRule.BY_SECOND, 
                   ByRuleFilterFn.bySecondExpand), 
      new TestData(IFrenqency.DAILY, IByRule.BY_MONTH, 
                   ByRuleFilterFn.byMonthFilter), 
      //    new TestData( IFrenqency.DAILY, IByRule.BY_WEEK_NO, ByRuleFilterFn.byweeknoExpand),
      new TestData(IFrenqency.DAILY, IByRule.BY_YEAR_DAY, 
                   ByRuleFilterFn.byYeardayFilter), 
      new TestData(IFrenqency.DAILY, IByRule.BY_MONTH_DAY, 
                   ByRuleFilterFn.byMonthdayFilter), 
      new TestData(IFrenqency.DAILY, IByRule.BY_DAY, 
                   ByRuleFilterFn.byDayFilter), 
      new TestData(IFrenqency.DAILY, IByRule.BY_HOUR, 
                   ByRuleFilterFn.byHourExpand), 
      new TestData(IFrenqency.DAILY, IByRule.BY_MINUTE, 
                   ByRuleFilterFn.byMinuteExpand), 
      new TestData(IFrenqency.DAILY, IByRule.BY_SECOND, 
                   ByRuleFilterFn.bySecondExpand), 
      new TestData(IFrenqency.MINUTELY, IByRule.BY_MONTH, 
                   ByRuleFilterFn.byMonthFilter), 
      //    new TestData( IFrenqency.MINUTELY, IByRule.BY_WEEK_NO, ByRuleFilterFn.byweeknoExpand),
      new TestData(IFrenqency.MINUTELY, IByRule.BY_YEAR_DAY, 
                   ByRuleFilterFn.byYeardayFilter), 
      new TestData(IFrenqency.MINUTELY, IByRule.BY_MONTH_DAY, 
                   ByRuleFilterFn.byMonthdayFilter), 
      new TestData(IFrenqency.MINUTELY, IByRule.BY_DAY, 
                   ByRuleFilterFn.byDayFilter), 
      new TestData(IFrenqency.MINUTELY, IByRule.BY_HOUR, 
                   ByRuleFilterFn.byHourFilter), 
      new TestData(IFrenqency.MINUTELY, IByRule.BY_MINUTE, 
                   ByRuleFilterFn.byMinuteFilter), 
      new TestData(IFrenqency.MINUTELY, IByRule.BY_SECOND, 
                   ByRuleFilterFn.bySecondExpand)
    };
    for (int i = 0; i < testDataList.length; i++)
    {
      TestData testData = testDataList[i];
      FilterFn fn = ByRuleFilterFn.get(testData.freq, testData.byRule);
      assertSame("Error in testGet i = " + i, testData.res, fn);
    }
  }

  /**
   * DOCUMENT ME!
   * 
   * @param args DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public static void main(String[] args)
                   throws Exception
  {
    junit.textui.TestRunner.run(suite());
  }
}