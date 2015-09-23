// %1023788051902:com.gridnode.pdip.base.time.entities.value.util%
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
import com.gridnode.pdip.base.time.entities.value.IFrenqency;
import com.gridnode.pdip.base.time.entities.value.IWeekDay;
import com.gridnode.pdip.base.time.entities.value.TimeInterval;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyKind;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;
import com.gridnode.pdip.base.time.entities.value.iCalRecurrenceV;
import com.gridnode.pdip.base.time.entities.value.util.IRecurCB;
import com.gridnode.pdip.framework.log.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RecurTestCase
  extends TestCase
{
  static String LogCat = "Recur";
  iCalEvent valueEntity = null;
  iCalPropertyV prop = null;

  /**
   * Creates a new RecurTestCase object.
   * 
   * @param name DOCUMENT ME!
   */
  public RecurTestCase(String name)
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
    return new TestSuite(RecurTestCase.class);
  }

  /**
   * DOCUMENT ME!
   */
  public void setUp()
  {
    try
    {
      Date dtStart = new Date(97, 8, 2, 9, 11, 11);
      Date dtEnd = new Date(97, 8, 3, 0, 0, 0);
      Integer owner = new Integer(1000);
      valueEntity = new iCalEvent();
      valueEntity.setStartDt(dtStart);
      valueEntity.setEndDt(dtEnd);
      valueEntity.setOwnerId(owner);
      prop = new iCalPropertyV((short)iCalPropertyKind.ICAL_RRULE_PROPERTY);
      valueEntity.addProperty(prop);
    }
    catch (Exception ex)
    {
      Log.err(LogCat, "tearDown failed ", ex);
    }
  }

  static class CallBackFn
    implements IRecurCB
  {
    CallBackFn(int count, List resArray)
    {
      this.count = count;
      this.resArray = resArray;
    }

    int count;
    int instances;
    List resArray;

    public boolean onValue(Date instance_start, Date instance_end)
    {
      if (instances == count)
      {
        return false;
      }
      instances++;
      TimeInterval interval = new TimeInterval(instance_start, instance_end);
      resArray.add(interval);
      return true;
    }
  }
  
  /**
   * DOCUMENT ME!
   */
  public void tearDown()
  {
    try
    {
      List res = new ArrayList();
      IRecurCB cbFn = new CallBackFn(20, res);
      iCalRecurUtil icalUtil = new iCalRecurUtil(valueEntity, null, null, null, 
                                                 cbFn);
      icalUtil.genInstances();
      Log.debug(LogCat, "RecurGenerated are " + res.size() + " " + res);
    }
    catch (Exception ex)
    {
      Log.err(LogCat, "tearDown failed ", ex);
    }
  }

  //FREQ=DAILY; COUNT =10

  /**
   * DOCUMENT ME!
   */
  public void testRecur1()
  {
    int freq = IFrenqency.DAILY;
    try
    {
      Log.debug(LogCat, "In Recur1 ");
      iCalRecurrenceV recur = new iCalRecurrenceV();
      recur.setFrequency(freq);
      recur.setCount(10);
      prop.setValue(recur);
    }
    catch (Exception ex)
    {
      Log.err(LogCat, "testRecur1 failed ", ex);
    }
  }

  
      //FREQ=DAILY; UNTIL =19971224T000000Z
    public void testRecur2()
    {
      int freq= IFrenqency.DAILY;
      try
      {
        Log.debug(LogCat, "In Recur2");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        Date util = new Date(97, 11, 24, 0, 0, 0);
        recur.setUntil(util);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur2 failed ", ex);
      }
    }
  
  
        //FREQ=DAILY; INTERVAL=2
    public void testRecur3()
    {
      int freq= IFrenqency.DAILY;
      try
      {
        Log.debug(LogCat, "In Recur3");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setInterval(2);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur3 failed ", ex);
      }
    }
  
        //FREQ=DAILY; INTERVAL=10, COUNT=5
    public void testRecur4()
    {
      int freq= IFrenqency.DAILY;
      try
      {
        Log.debug(LogCat, "In Recur4");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setInterval(10);
        recur.setCount(5);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur4 failed ", ex);
      }
    }
  
    //FREQ=YEARLY; BYMONTH=1;BYDAY=SU,MO,TU,WE,TH,FR,SA; UNTIL=2000013109000z
    public void testRecur5()
    {
      int freq= IFrenqency.YEARLY;
      try
      {
        Log.debug(LogCat, "In Recur5");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        Date util = new Date(100, 0, 31, 8, 0, 0);
        recur.setUntil(util);
        List byMonth = new ArrayList();
        byMonth.add(new Integer(0));
        recur.setByMonth(byMonth);
        List byDay= new ArrayList();
        byDay.add(new DayEntry(IWeekDay.MONDAY));
        byDay.add(new DayEntry(IWeekDay.TUESDAY));
  //      byDay.add(new DayEntry(IWeekDay.WEDNESDAY));
  //      byDay.add(new DayEntry(IWeekDay.THURSDAY));
  //      byDay.add(new DayEntry(IWeekDay.FRIDAY));
  //      byDay.add(new DayEntry(IWeekDay.SATURDAY));
  //      byDay.add(new DayEntry(IWeekDay.SUNDAY));
        recur.setByDay(byDay);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur5 failed ", ex);
      }
    }
      //FREQ=WEEKLY; COUNT=10
    public void testRecur6()
    {
      int freq= IFrenqency.WEEKLY;
      try
      {
        Log.debug(LogCat, "In Recur6");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setCount(10);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur6 failed ", ex);
      }
    }
  
      //FREQ=WEEKLY; UNTIL =19971224T000000Z,
    public void testRecur7()
    {
      int freq= IFrenqency.WEEKLY;
      try
      {
        Log.debug(LogCat, "In Recur7");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        Date util = new Date(97, 11, 24, 0, 0, 0);
        recur.setUntil(util);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur7 failed ", ex);
      }
    }
      //FREQ=WEEKLY; INTERVAL =2; WKST=SU//UNTIL =19971224T000000Z
    public void testRecur8()
    {
      int freq= IFrenqency.WEEKLY;
      try
      {
        Log.debug(LogCat, "In Recur8");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setInterval(2);
        recur.setWeekStart(IWeekDay.SUNDAY);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur8 failed ", ex);
      }
    }
  
    //FREQ=WEEKLY; WKST=SU; BYDAY=TU,TH
    public void testRecur9()
    {
      int freq= IFrenqency.WEEKLY;
      try
      {
        Log.debug(LogCat, "In Recur9");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setWeekStart(IWeekDay.SUNDAY);
        Date util = new Date(97, 9, 7, 0, 0, 0);
        recur.setUntil(util);
        List byDay= new ArrayList();
        byDay.add(new DayEntry(IWeekDay.TUESDAY));
        byDay.add(new DayEntry(IWeekDay.THURSDAY));
        recur.setByDay(byDay);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur9 failed ", ex);
      }
    }
      //FREQ=WEEKLY; INTERVAL =2; WKST=SU; BYDAY=MO, WE, FR//UNTIL =19971224T000000Z
    public void testRecur10()
    {
      int freq= IFrenqency.WEEKLY;
      try
      {
        Log.debug(LogCat, "In Recur10");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setInterval(2);
        recur.setWeekStart(IWeekDay.SUNDAY);
        Date util = new Date(97, 11, 24, 0, 0, 0);
        recur.setUntil(util);
        List byDay= new ArrayList();
        byDay.add(new DayEntry(IWeekDay.MONDAY));
        byDay.add(new DayEntry(IWeekDay.WEDNESDAY));
        byDay.add(new DayEntry(IWeekDay.FRIDAY));
        recur.setByDay(byDay);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur10 failed ", ex);
      }
    }
  
    //FREQ=WEEKLY; WKST=SU; INTERVAL=2; BYDAY=TU,TH
    public void testRecur11()
    {
      int freq= IFrenqency.WEEKLY;
      try
      {
        Log.debug(LogCat, "In Recur11");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setInterval(2);
        recur.setWeekStart(IWeekDay.SUNDAY);
        List byDay= new ArrayList();
        byDay.add(new DayEntry(IWeekDay.TUESDAY));
        byDay.add(new DayEntry(IWeekDay.THURSDAY));
        recur.setByDay(byDay);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur11 failed ", ex);
      }
    }
      //FREQ=MONTHLY; BYDAY=1Fr; COUNT=10
    public void testRecur12()
    {
      int freq= IFrenqency.MONTHLY;
      try
      {
        Log.debug(LogCat, "In Recur12");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setCount(10);
        List byDay= new ArrayList();
        byDay.add(new DayEntry(1, IWeekDay.FRIDAY));
        recur.setByDay(byDay);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur12 failed ", ex);
      }
    }
  
    //FREQ=MONTHLY; INTERVAL =2; BYDAY=1SU, -1SU; COUNT=10
    public void testRecur13()
    {
      int freq= IFrenqency.MONTHLY;
      try
      {
        Log.debug(LogCat, "In Recur13");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setInterval(2);
        recur.setCount(10);
        List byDay= new ArrayList();
        byDay.add(new DayEntry(1, IWeekDay.SUNDAY));
        byDay.add(new DayEntry(-1, IWeekDay.SUNDAY));
        recur.setByDay(byDay);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur13 failed ", ex);
      }
    }
      //FREQ=MONTHLY; BYDAY=-2MO; COUNT=10
    public void testRecur14()
    {
      int freq= IFrenqency.MONTHLY;
      try
      {
        Log.debug(LogCat, "In Recur14");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setCount(10);
        List byDay= new ArrayList();
        byDay.add(new DayEntry(-2, IWeekDay.MONDAY));
        recur.setByDay(byDay);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur14 failed ", ex);
      }
    }
  
    //FREQ=MONTHLY; BYMONTHDAY=-3
    public void testRecur15()
    {
      int freq= IFrenqency.MONTHLY;
      try
      {
        Log.debug(LogCat, "In Recur15");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setCount(10);
        List byMonthDay= new ArrayList();
        byMonthDay.add(new Integer(-3));
        recur.setByMonthDay(byMonthDay);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur15 failed ", ex);
      }
    }
   //FREQ=MONTHLY; INTERVAL=18; COUNT=10; BYMONTHDAY=10, 11, 12, 13, 14, 15
    public void testRecur16()
    {
      int freq= IFrenqency.MONTHLY;
      try
      {
        Log.debug(LogCat, "In Recur16");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setCount(10);
        recur.setInterval(18);
        List byMonthDay= new ArrayList();
        byMonthDay.add(new Integer(10));
        byMonthDay.add(new Integer(11));
        byMonthDay.add(new Integer(12));
        byMonthDay.add(new Integer(13));
        byMonthDay.add(new Integer(14));
        byMonthDay.add(new Integer(15));
        recur.setByMonthDay(byMonthDay);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur16 failed ", ex);
      }
    }
     //FREQ=YEARLY; INTERVAL=2; COUNT=10; BYMONTH=6, 7
    public void testRecur17()
    {
      int freq= IFrenqency.YEARLY;
      try
      {
        Log.debug(LogCat, "In Recur17");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setCount(10);
        recur.setInterval(2);
        List byMonth= new ArrayList();
        byMonth.add(new Integer(5));
        byMonth.add(new Integer(6));
        recur.setByMonth(byMonth);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur17 failed ", ex);
      }
    }
   //FREQ=YEARLY; INTERVAL=3; COUNT=10;BYEARDAY= 1, 100, 200
    public void testRecur19()
    {
      int freq= IFrenqency.YEARLY;
      try
      {
        Log.debug(LogCat, "In Recur19");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setCount(10);
        recur.setInterval(3);
        List byYearDay= new ArrayList();
        byYearDay.add(new Integer(1));
        byYearDay.add(new Integer(100));
        byYearDay.add(new Integer(200));
        recur.setByYearDay(byYearDay);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur19 failed ", ex);
      }
    }
    //FREQ=YEARLY; COUNT=5; BYDAY=20MO
    public void testRecur20()
    {
      int freq= IFrenqency.YEARLY;
      try
      {
        Date startDt = new Date(97, 4, 19, 8, 0, 0);
        valueEntity.setStartDt(startDt);
        valueEntity.setEndDt(null);
        Log.debug(LogCat, "In Recur20");
        iCalRecurrenceV recur= new iCalRecurrenceV();
        recur.setFrequency(freq);
        recur.setCount(5);
        List byDay= new ArrayList();
        byDay.add(new DayEntry(20, IWeekDay.MONDAY));
        recur.setByDay(byDay);
        prop.setValue(recur);
  
      }
      catch (Exception ex)
      {
        Log.err(LogCat, "testRecur20 failed ", ex);
      }
    }
  
  //FREQ=YEARLY; COUNT=5; BYWEEKNO=20; BYDAY=MO

  /**
   * DOCUMENT ME!
   */
  public void testRecur21()
  {
    int freq = IFrenqency.YEARLY;
    try
    {
      Log.debug(LogCat, "In Recur21");
      Date startDt = new Date(97, 2, 12, 11, 11, 11);
      valueEntity.setStartDt(startDt);
      valueEntity.setEndDt(null);
      iCalRecurrenceV recur = new iCalRecurrenceV();
      recur.setFrequency(freq);
      List byDay = new ArrayList();
      byDay.add(new DayEntry(IWeekDay.MONDAY));
      recur.setByDay(byDay);
      List byWeekNo = new ArrayList();
      byWeekNo.add(new Integer(20));
      recur.setByWeekNo(byWeekNo);
      prop.setValue(recur);
    }
    catch (Exception ex)
    {
      Log.err(LogCat, "testRecur21 failed ", ex);
    }
  }

  //FREQ=DAILY;BYHOUR=9,10, 11, 12, 13, 14, 15, 16; BYMINUTE=0, 20, 40

  /**
   * DOCUMENT ME!
   */
  public void testRecur22()
  {
    int freq = IFrenqency.DAILY;
    try
    {
      Log.debug(LogCat, "In Recur22");
      Date startDt = new Date(103, 8, 2, 0, 0, 0);
      valueEntity.setStartDt(startDt);
      valueEntity.setEndDt(null);
      iCalRecurrenceV recur = new iCalRecurrenceV();
      recur.setFrequency(freq);
      List byHours = new ArrayList();
      byHours.add(new Integer(9));
      byHours.add(new Integer(10));
      byHours.add(new Integer(11));
      byHours.add(new Integer(12));
      byHours.add(new Integer(13));
      byHours.add(new Integer(14));
      byHours.add(new Integer(15));
      byHours.add(new Integer(16));
      recur.setByHour(byHours);
      List byMinites = new ArrayList();
      byMinites.add(new Integer(0));
      byMinites.add(new Integer(20));
      byMinites.add(new Integer(40));
      recur.setByMinute(byMinites);
      prop.setValue(recur);
    }
    catch (Exception ex)
    {
      Log.err(LogCat, "testRecur22 failed ", ex);
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