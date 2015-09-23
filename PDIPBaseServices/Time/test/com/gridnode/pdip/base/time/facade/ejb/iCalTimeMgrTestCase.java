// %1023788051309:com.gridnode.pdip.base.time%
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
package com.gridnode.pdip.base.time.facade.ejb;

import com.gridnode.pdip.base.time.entities.helpers.AlarmCaculator;
import com.gridnode.pdip.base.time.entities.helpers.iCalUtil;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.time.entities.model.iCalEvent;
import com.gridnode.pdip.base.time.entities.value.*;
import com.gridnode.pdip.base.time.entities.value.DayEntry;
import com.gridnode.pdip.base.time.entities.value.IFrenqency;
import com.gridnode.pdip.base.time.entities.value.IRelated;
import com.gridnode.pdip.base.time.entities.value.IWeekDay;
import com.gridnode.pdip.base.time.entities.value.iCalParameterKind;
import com.gridnode.pdip.base.time.entities.value.iCalParameterV;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyKind;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;
import com.gridnode.pdip.base.time.entities.value.iCalRecurrenceV;
import com.gridnode.pdip.base.time.entities.value.exchange.GenMime;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.log.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class iCalTimeMgrTestCase
  extends TestCase
{
  static String LogCat = iCalTimeMgrTestCase.class.getName();
  private IiCalTimeMgrHome mgrHome;
  private IiCalTimeMgrObj mgrObject;

  /**
   * Creates a new AlarmCaculatorTestCase object.
   *
   * @param name DOCUMENT ME!
   */
  public iCalTimeMgrTestCase(String name)
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
    return new TestSuite(iCalTimeMgrTestCase.class);
  }

  /**
   * DOCUMENT ME!
   */
  public void setUp()
  {
    try
    {
      mgrHome = (IiCalTimeMgrHome)ServiceLookup.getInstance(
                                      ServiceLookup.CLIENT_CONTEXT).getHome(
                    IiCalTimeMgrHome.class);
      Log.debug(LogCat, "Object Found ... ");
    }
    catch (Exception ex)
    {
      Log.err(LogCat, " Exception in SetUp  : ", ex);
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @throws Exception DOCUMENT ME!
   */
  public void testAddCancleAlarm()
                          throws Exception
  {
    String receiverId = "pdip.base";
    String cat = "Test";
    Date startDt = iCalUtil.getTimeInSecod(new Date(new Date().getTime() + 1000*60*2));
    iCalAlarm alarm = new iCalAlarm();
    alarm.setReceiverKey(receiverId);
    alarm.setStartDt(startDt);
    alarm.setCategory(cat);
    mgrObject = mgrHome.create();
    alarm = mgrObject.addAlarm(alarm);
    assertEquals("nextDueTime should be the same as dtStart", startDt,
                 alarm.getNextDueTime());
    Long key = (Long)alarm.getKey();
    //long wait = new Random(200000).nextLong();
    try
    {
      //Thread.currentThread().sleep(200000);
      Thread.currentThread().sleep(5000);
    }
    catch (Exception ex)
    {
      Log.log(LogCat, "Error in wait", ex);
    }
    mgrObject.cancelAlarm(key);
  }

  /**
   * DOCUMENT ME!
   *
   * @throws Exception DOCUMENT ME!
   */
  public void testAddCancleRepeatedAlarm()
                                  throws Exception
  {
    String receiverId = "pdip.base_Repeat";
    String cat = "Test";
    Date startDt = iCalUtil.getTimeInSecod(new Date(new Date().getTime() + 1000*60*2));
    iCalAlarm alarm = new iCalAlarm();
    alarm.setReceiverKey(receiverId);
    alarm.setStartDt(startDt);
    alarm.setCategory(cat);
    alarm.setDelayPeriod(new Long(60)); //1 min
    alarm.setRepeat(new Integer(10));
    mgrObject = mgrHome.create();
    alarm = mgrObject.addAlarm(alarm);
    assertEquals("nextDueTime should be the same as dtStart", startDt,
                 alarm.getNextDueTime());
    Long key = (Long)alarm.getKey();
    //long wait = new Random(200000).nextLong();
    try
    {
      //Thread.currentThread().sleep(200000);
      Thread.currentThread().sleep(5000);
    }
    catch (Exception ex)
    {
      Log.log(LogCat, "Error in wait", ex);
    }
    mgrObject.cancelAlarm(key);
  }

  /**
   * DOCUMENT ME!
   *
   * @throws Exception DOCUMENT ME!
   */
  public void testAddCancleRecurredAlarm()
                                  throws Exception
  {
    String receiverId = "pdip.base_Recurrence";
    String cat = "Test";
    Date startDt = iCalUtil.getTimeInSecod(new Date(new Date().getTime() + 1000*3600));
    iCalAlarm alarm = new iCalAlarm();
    alarm.setReceiverKey(receiverId);
    alarm.setCategory(cat);

    iCalRecurrenceV recur = new iCalRecurrenceV();
    recur.setFrequency(IFrenqency.MINUTELY);
    recur.setInterval(5);
//    recur.setCount(5);

    iCalRecurrenceV[] recurs = new iCalRecurrenceV[]{recur};
    mgrObject = mgrHome.create();
    alarm = mgrObject.addRecurredAlarm(alarm, startDt, null, recurs, null);
    assertEquals("nextDueTime should be the same as dtStart", startDt,
                 alarm.getNextDueTime());
    Long key = (Long)alarm.getKey();
    //long wait = new Random(200000).nextLong();
    try
    {
      //Thread.currentThread().sleep(200000);
      Thread.currentThread().sleep(5000);
    }
    catch (Exception ex)
    {
      Log.log(LogCat, "Error in wait", ex);
    }
    mgrObject.cancelAlarm(key);
  }

 /**
   * DOCUMENT ME!
   *
   * @throws Exception DOCUMENT ME!
   */
  public void testAddCancleAlarmOfRecurredEvent()
                                  throws Exception
  {
    String receiverId = "pdip.base_Recurrence";
    String cat = "Test";
    Date startDt = iCalUtil.getTimeInSecod(new Date(new Date().getTime() + 1000*3600));

    iCalEvent event = new iCalEvent();
    event.setStartDt(startDt);
    event.setEndDt(null);
		setEventRecurProperty(event);
//    iCalRecurrenceV recur = new iCalRecurrenceV();
//    recur.setFrequency(IFrenqency.MINUTELY);
//    recur.setInterval(5);
//    iCalPropertyV  prop = new iCalPropertyV((short) iCalPropertyKind.ICAL_RRULE_PROPERTY);
//    prop.setValue(recur);
//    event.addProperty(prop);
      
    mgrObject = mgrHome.create();
    event = mgrObject.addEvent(event);
    
    iCalAlarm alarm = new iCalAlarm();
    alarm.setReceiverKey(receiverId);
    alarm.setCategory(cat);
    alarm.setRelated(new Integer(IRelated.START));
    alarm.setParentUid((Long)event.getKey());
    alarm.setParentKind(iCalEvent.KIND_EVENT);
//		alarm.setIncludeParentStartTime(Boolean.TRUE);
    alarm = mgrObject.addAlarm(alarm);
    Long key = (Long)alarm.getKey();
  
      
		setEventRecurProperty(event);
		mgrObject.updateEvent(event, true);
		
		alarm = mgrObject.getAlarm(key);
		alarm.setIncludeParentStartTime(Boolean.TRUE);
		alarm = mgrObject.updateAlarm(alarm, true);
		assertEquals("nextDueTime should be the same as dtStart", startDt,
								 alarm.getNextDueTime());
    //long wait = new Random(200000).nextLong();
    try
    {
      //Thread.currentThread().sleep(200000);
      Thread.currentThread().sleep(5000);
    }
    catch (Exception ex)
    {
      Log.log(LogCat, "Error in wait", ex);
    }
    mgrObject.cancelAlarm(key);
  }
  
//  public void testGenDailyReport()
//  {
//    try
//    {
//      ReportTest.instance().scheduleDailyReport();
//    }
//    catch(Throwable ex)
//    {
//      Log.err("testGenDailyReport", ex);
//      fail("testGenDailyReport");
//    }
//  }


	private void setEventRecurProperty(iCalEvent event)
	{
		int freq= IFrenqency.HOURLY;
		int interval= 1;
		iCalRecurrenceV recur= new iCalRecurrenceV();
		 recur.setFrequency(freq);
		 recur.setInterval(interval);
		 recur.setCount(20);
		 //recur.setUntil(new Date());
		 List byDay= new ArrayList();
		 byDay.add(new DayEntry(IWeekDay.MONDAY));
		 byDay.add(new DayEntry(2, IWeekDay.MONDAY));
		 byDay.add(new DayEntry(-2, IWeekDay.MONDAY));
		 recur.setByDay(byDay);
		 List byHour= new ArrayList();
		 byHour.add(new Integer(10));
		 byHour.add(new Integer(11));
		 byHour.add(new Integer(12));
		 recur.setByHour(byHour);

		 iCalPropertyV prop= new iCalPropertyV((short) iCalPropertyKind.ICAL_RRULE_PROPERTY);
		 iCalParameterV param= new iCalParameterV((short) iCalParameterKind.ICAL_ROLE_PARAMETER);
		 param.setValue(new iCalURIV("RoleA"));
		 prop.addParam(param);
		 prop.setValue(recur);
		 event.setProperties(null);
		 event.addProperty(prop);	
		 String mime= GenMime.genComponentProperties(event);
		 Log.debug(LogCat, "Event with before add to db is " + mime);
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
//    for (int i = 0; i < 1; i++)
//    {
//      new Thread("Thread" + i) {
//        public void run()
//        {
//          junit.textui.TestRunner.run(suite());
//        }
//      }.start();
//    }
  }
}