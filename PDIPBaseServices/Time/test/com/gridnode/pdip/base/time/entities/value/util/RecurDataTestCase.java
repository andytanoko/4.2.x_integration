// %1023788051871:com.gridnode.pdip.base.time.entities.value.util%
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
import com.gridnode.pdip.base.time.entities.value.iCalPropertyKind;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;
import com.gridnode.pdip.base.time.entities.value.iCalRecurrenceV;
import com.gridnode.pdip.framework.log.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RecurDataTestCase
  extends TestCase
  implements IByRule
{
  static String LogCat = "RecurDataTestCase";
  iCalEvent valueEntity = null;
  iCalPropertyV prop = null;

  /**
   * Creates a new RecurDataTestCase object.
   * 
   * @param name DOCUMENT ME!
   */
  public RecurDataTestCase(String name)
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
    return new TestSuite(RecurDataTestCase.class);
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
    assertEquals("WeekStartDay", 0, recurdata.getWeekStartDay());
    assertEquals("weekDayOffSet", 4, recurdata.getWeekOffset());
    assertEquals("Interval", 2, recurdata.getInterval());
    for (int i = IByRule.BY_SECOND; i <= IByRule.BY_SET_POS; i++)
    {
      boolean value = (i == IByRule.BY_DAY || i == IByRule.BY_HOUR)
                      ? false : true;
      assertEquals("isEmpty Failed for byRule = " + i, value, 
                   recurdata.isEmpty(i));
    }
    Log.debug(LogCat, recurdata.getByRuleList(BY_DAY));
    List byDayList = recurdata.getByRuleList(BY_DAY);
    assertEquals("ByDay Length", 3, byDayList.size());
    assertEquals("ByDay_1", new Integer(0), byDayList.get(0));
    assertEquals("ByDay_2", new Integer(21), byDayList.get(1));
    assertEquals("ByDay_3", new Integer(-18), byDayList.get(2));
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