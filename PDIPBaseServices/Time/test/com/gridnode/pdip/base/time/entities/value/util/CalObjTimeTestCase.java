// %1023788051606:com.gridnode.pdip.base.time.entities.value.util%
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
import com.gridnode.pdip.base.time.entities.value.IByRule;
import com.gridnode.pdip.base.time.entities.value.IFrenqency;
import com.gridnode.pdip.base.time.entities.value.IWeekDay;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;
import com.gridnode.pdip.framework.log.Log;

import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CalObjTimeTestCase
  extends TestCase
  implements IByRule
{
  static String LogCat = "CalObjTimeTestCase";
  iCalEvent valueEntity = null;
  iCalPropertyV prop = null;

  /**
   * Creates a new CalObjTimeTestCase object.
   * 
   * @param name DOCUMENT ME!
   */
  public CalObjTimeTestCase(String name)
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
    return new TestSuite(CalObjTimeTestCase.class);
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
    Date now = new Date();
    CalObjTime cotime = new CalObjTime(now);
    Log.debug(LogCat, " Date = " + now + "cotime=" + cotime);
    Date newDate = cotime.toDate();
    CalObjTime newcotime = new CalObjTime(newDate);
    assertEquals("Two CalObjTime should be equals", cotime, newcotime);
  }

  /**
   * DOCUMENT ME!
   */
  public void testGet()
  {
    GregorianCalendar cal = new GregorianCalendar(2002, 4, 21, 20, 24, 10);
    CalObjTime cotime = new CalObjTime(cal.getTime());
    Log.debug(LogCat, "cotime=" + cotime);
    //    assertEquals("BY_SET_POS", 200,  get(BY_SET_POS));
    assertEquals("BY_WEEK_NO", 21, cotime.get(BY_WEEK_NO));
    assertEquals("BY_SECOND", 10, cotime.get(BY_SECOND));
    assertEquals("BY_MINUTE", 24, cotime.get(BY_MINUTE));
    assertEquals("BY_HOUR", 20, cotime.get(BY_HOUR));
    assertEquals("BY_DAY", IWeekDay.TUESDAY, cotime.get(BY_DAY));
    assertEquals("BY_MONTH_DAY", 21, cotime.get(BY_MONTH_DAY));
    assertEquals("BY_YEAR_DAY", 141, cotime.get(BY_YEAR_DAY));
    assertEquals("BY_MONTH", 4, cotime.get(BY_MONTH));
  }

  /**
   * DOCUMENT ME!
   */
  public void testClone()
  {
    GregorianCalendar cal = new GregorianCalendar(2002, 4, 21, 20, 24, 10);
    CalObjTime cotime = new CalObjTime(cal.getTime());
    CalObjTime newcotime = (CalObjTime)cotime.clone();
    assertEquals("Two CalObjTime should equals", cotime, newcotime);
    assertEquals("", cal.getTime(), cotime.toDate());
    assertEquals("Two Date should Equals", cotime.toDate(), newcotime.toDate());
  }

  /**
   * DOCUMENT ME!
   */
  public void testAdd()
  {
    class AddTestData
    {
      int freq;
      int value;
      CalObjTime res;

      AddTestData(int afreq, int avalue, CalObjTime ares)
      {
        freq = afreq;
        value = avalue;
        res = ares;
      }
    }

    AddTestData[] testDatas = new AddTestData[] 
    {

      new AddTestData(IFrenqency.SECONDLY, 55, 
                      new CalObjTime(2002, 4, 21, 20, 25, 5)), 
      new AddTestData(IFrenqency.MINUTELY, 45, 
                      new CalObjTime(2002, 4, 21, 21, 9, 10)), 
      new AddTestData(IFrenqency.HOURLY, 4, 
                      new CalObjTime(2002, 4, 22, 0, 24, 10)), 
      new AddTestData(IFrenqency.DAILY, 11, 
                      new CalObjTime(2002, 5, 1, 20, 24, 10)), 
      new AddTestData(IFrenqency.MONTHLY, 8, 
                      new CalObjTime(2003, 0, 21, 20, 24, 10)), 
      new AddTestData(IFrenqency.YEARLY, 20, 
                      new CalObjTime(2022, 4, 21, 20, 24, 10))
    };
    GregorianCalendar cal = new GregorianCalendar(2002, 4, 21, 20, 24, 10);
    for (int i = 0; i < testDatas.length; i++)
    {
      AddTestData addData = testDatas[i];
      CalObjTime cotime = new CalObjTime(cal.getTime());
      cotime.add(addData.freq, addData.value);
      assertEquals("addTestFailed for " + i, addData.res, cotime);
    }
  }

  /**
   * DOCUMENT ME!
   */
  public void testWeek()
  {
    GregorianCalendar cal = new GregorianCalendar(2002, 4, 21, 20, 24, 10);
    CalObjTime cotime = new CalObjTime(cal.getTime());
    int weekDayOffset = cotime.weekdayOffset(IWeekDay.THURSDAY);
    assertEquals("weekday offset is not correct", 5, weekDayOffset);
    class TestData
    {
      int weekstart;
      int weekoffset;
      CalObjTime res;

      TestData(int weekstart, int weekoffset, CalObjTime ares)
      {
        this.weekstart = weekstart;
        this.weekoffset = weekoffset;
        res = ares;
      }
    }

    TestData[] testDatas = new TestData[] 
    {
      new TestData(IWeekDay.MONDAY, 0, 
                   new CalObjTime(2001, 11, 31, 20, 24, 10)), 
      new TestData(IWeekDay.MONDAY, 5, new CalObjTime(2002, 0, 5, 20, 24, 10)), 
      new TestData(IWeekDay.FRIDAY, 1, new CalObjTime(2002, 0, 5, 20, 24, 10)), 
      new TestData(IWeekDay.FRIDAY, 5, new CalObjTime(2002, 0, 9, 20, 24, 10)), 
      new TestData(IWeekDay.SATURDAY, 1, 
                   new CalObjTime(2001, 11, 30, 20, 24, 10)), 
      new TestData(IWeekDay.SATURDAY, 5, 
                   new CalObjTime(2002, 0, 3, 20, 24, 10))
    };
    Log.debug(LogCat, "Test FindFirstWeek");
    for (int i = 0; i < testDatas.length; i++)
    {
      TestData addData = testDatas[i];
      cotime = new CalObjTime(cal.getTime());
      cotime = cotime.findFirstWeek(addData.weekstart, addData.weekoffset);
      assertEquals("WEEKTestFailed for " + i, addData.res, cotime);
    }
  }

  /**
   * DOCUMENT ME!
   */
  public void testJulianDay()
  {
    Log.debug(LogCat, "testJulianDay");
    CalObjTime cotime1 = new CalObjTime(1999, 10, 20, 20, 11, 12);
    CalObjTime cotime2 = new CalObjTime(2000, 10, 20, 1, 11, 12);
    assertEquals("JulianDay duration 1 is not correct", 366, 
                 cotime2.getJulianDay() - cotime1.getJulianDay());
    cotime2 = new CalObjTime(2001, 10, 20, 1, 11, 12);
    assertEquals("JulianDay duration 2 is not correct", 365 + 366, 
                 cotime2.getJulianDay() - cotime1.getJulianDay());
  }

  /**
   * DOCUMENT ME!
   */
  public void testFowaredAdd()
  {
    class AddTestData
    {
      int freq;
      int value;
      CalObjTime res;

      AddTestData(int afreq, int avalue, CalObjTime ares)
      {
        freq = afreq;
        value = avalue;
        res = ares;
      }
    }

    AddTestData[] testDatas = new AddTestData[] 
    {

      new AddTestData(IFrenqency.MONTHLY, -24, 
                      new CalObjTime(1998, 1, 29, 20, 24, 10)), 
      new AddTestData(IFrenqency.YEARLY, 2, 
                      new CalObjTime(2002, 1, 29, 20, 24, 10))
    };
    GregorianCalendar cal = new GregorianCalendar(2000, 1, 29, 20, 24, 10);
    try
    {
      for (int i = 0; i < testDatas.length; i++)
      {
        AddTestData addData = testDatas[i];
        CalObjTime cotime = new CalObjTime(cal.getTime());
        cotime.fowardAdd(addData.freq, addData.value);
        assertEquals("ForwardAdd TestFailed for " + i, addData.res, cotime);
      }
    }
    catch (Throwable t)
    {
      Log.debug(LogCat, "", t);
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