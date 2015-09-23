/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportGenTimeHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 5 2002    H.Sushil            Created
 */

package com.gridnode.pdip.app.reportgen.helpers;

import com.gridnode.pdip.app.reportgen.value.IReportOptions;

import com.gridnode.pdip.app.reportgen.value.DailyReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.MonthlyReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.OneOffReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.WeeklyReportScheduleParam;

import com.gridnode.pdip.app.reportgen.helpers.Logger;
import com.gridnode.pdip.app.reportgen.helpers.ReportGenHelper;
import com.gridnode.pdip.app.reportgen.helpers.ReportGenDocServiceHelper;

import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;

import com.gridnode.pdip.base.time.entities.model.iCalEvent;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;

import com.gridnode.pdip.base.time.entities.value.IFrenqency;
import com.gridnode.pdip.base.time.entities.value.IWeekDay;
import com.gridnode.pdip.base.time.entities.value.iCalParameterKind;
import com.gridnode.pdip.base.time.entities.value.iCalParameterV;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyKind;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;
import com.gridnode.pdip.base.time.entities.value.iCalRecurrenceV;
import com.gridnode.pdip.base.time.entities.value.iCalTextV;
import com.gridnode.pdip.base.time.entities.value.IRelated;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.exceptions.SystemException;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.util.Date;

 /** Helper Class for accessing Time module
   * The services of time module like adding recurring events,alarms are
   * accessible from this class
   * @author H.Sushil
   *
   * @version 1.0
   * @since 1.0
   */


public class ReportGenTimeHelper
{

  private static ReportGenTimeHelper _reportGenTimeHelper = null;

  private IiCalTimeMgrHome mgrTimeHome;

  private static String MESSAGE_CATEGORY   = "ReportGenTimer";
  private static String RECEIVER_KEY       = "ReportGenHandler";
  private static String SENDER_KEY         = "ReportGenScheduler";

  private ReportGenTimeHelper()
  {

  }


  static public ReportGenTimeHelper instance()
  {
    if (_reportGenTimeHelper == null)
    {
          _reportGenTimeHelper = new ReportGenTimeHelper();
    }
    return _reportGenTimeHelper;
  }




  public void scheduleDailyReport(DailyReportScheduleParam dailyReportParam) throws SystemException
  {

      IiCalTimeMgrObj mgrTimeObject = null;
   try{
      int freq = IFrenqency.DAILY;
      iCalEvent valueEntity = new iCalEvent();

      valueEntity.setStartDt(dailyReportParam.getStartDate());
      valueEntity.setEndDt(null);

      iCalRecurrenceV recur = new iCalRecurrenceV();

      recur.setFrequency(freq);

      Integer totalOccurence  = dailyReportParam.getTotalOccurence();

      if(totalOccurence == null)
        recur.setUntil(dailyReportParam.getEndDate());
      else
	recur.setCount(dailyReportParam.getTotalOccurence().intValue());

      iCalPropertyV prop = new iCalPropertyV((short)iCalPropertyKind.ICAL_RRULE_PROPERTY);
      prop.setValue(recur);

      valueEntity.addProperty(prop);

      lookUpTimeManagerBean();
      mgrTimeObject = mgrTimeHome.create();

      // Build the report properties

      String strProperties  = dailyReportParam.getReportName() + IReportOptions.PROPERTY_SEPERATOR + dailyReportParam.getReportTargetPath() + IReportOptions.PROPERTY_SEPERATOR +  dailyReportParam.getReportDataSource() + IReportOptions.PROPERTY_SEPERATOR + dailyReportParam.getReportTemplate() + IReportOptions.PROPERTY_SEPERATOR + dailyReportParam.getReportFormat();

      iCalTextV value =  new iCalTextV(strProperties);
      iCalPropertyV desc = new iCalPropertyV(iCalPropertyKind.ICAL_DESCRIPTION_PROPERTY);
      desc.setValue(value);
      valueEntity.addProperty(desc);

      valueEntity = mgrTimeObject.addEvent(valueEntity);

      iCalAlarm alarm = new iCalAlarm();
      alarm.setCategory(MESSAGE_CATEGORY);
      alarm.setReceiverKey(RECEIVER_KEY);
      alarm.setRelated(new Integer(IRelated.START));
      alarm.setParentKind(iCalEvent.KIND_EVENT);
      alarm.setParentUid(new Long(valueEntity.getUId()));
      alarm = mgrTimeObject.addAlarm(alarm);


    }
    catch(Exception ex)
    {
     Logger.err( " Exception in ReportGenTimeHelper.scheduleDailyReport() : ", ex);
      throw new SystemException(" Exception in ReportGenTimeHelper.scheduleDailyReport",ex);
    }
    finally
    {
      mgrTimeHome       = null;
      mgrTimeObject     = null;
      dailyReportParam  = null;
    }
  }

  public void scheduleOneOffReport(OneOffReportScheduleParam oneOffReportParam)
  throws SystemException
  {
       IiCalTimeMgrObj mgrTimeObject = null;

   try{

      iCalEvent valueEntity = new iCalEvent();

      valueEntity.setStartDt(oneOffReportParam.getOneOffDate());

      lookUpTimeManagerBean();
      mgrTimeObject = mgrTimeHome.create();

       // Build the report properties

      String strProperties  = oneOffReportParam.getReportName() + IReportOptions.PROPERTY_SEPERATOR + oneOffReportParam.getReportTargetPath() + IReportOptions.PROPERTY_SEPERATOR +  oneOffReportParam.getReportDataSource() + IReportOptions.PROPERTY_SEPERATOR + oneOffReportParam.getReportTemplate() +  IReportOptions.PROPERTY_SEPERATOR + oneOffReportParam.getReportFormat();

      iCalTextV value =  new iCalTextV(strProperties);
      iCalPropertyV desc = new iCalPropertyV(iCalPropertyKind.ICAL_DESCRIPTION_PROPERTY);
      desc.setValue(value);
      valueEntity.addProperty(desc);

      valueEntity.setKind(iCalEvent.KIND_EVENT);

      valueEntity = mgrTimeObject.addEvent(valueEntity);

      iCalAlarm alarm = new iCalAlarm();
      alarm.setCategory(MESSAGE_CATEGORY);
      alarm.setReceiverKey(RECEIVER_KEY);
      alarm.setRelated(new Integer(IRelated.START));
      alarm.setParentKind(iCalEvent.KIND_EVENT);
      alarm.setParentUid(new Long(valueEntity.getUId()));
      alarm = mgrTimeObject.addAlarm(alarm);


    }
    catch(Exception ex)
    {
      Logger.err( " Exception in ReportGenTimeHelper.scheduleOneOffReport() : ", ex);
      throw new SystemException(" Exception in ReportGenTimeHelper.scheduleOneOffReport",ex);
    }
    finally
    {
      mgrTimeHome       = null;
      mgrTimeObject     = null;
      oneOffReportParam  = null;
    }
  }

  public void scheduleMonthlyReport(MonthlyReportScheduleParam monthlyReportParam)
  throws SystemException
  {
      IiCalTimeMgrObj mgrTimeObject = null;
   try{
      int freq = IFrenqency.MONTHLY;
      iCalEvent valueEntity = new iCalEvent();

      valueEntity.setStartDt(monthlyReportParam.getStartDate());
      valueEntity.setEndDt(null);

      iCalRecurrenceV recur = new iCalRecurrenceV();

      recur.setFrequency(freq);
      recur.setInterval(monthlyReportParam.getMonthInterval().intValue());

      Integer totalOccurence  = monthlyReportParam.getTotalOccurence();

      if(totalOccurence == null)
        recur.setUntil(monthlyReportParam.getEndDate());
      else
	recur.setCount(totalOccurence.intValue());

      iCalPropertyV prop = new iCalPropertyV((short)iCalPropertyKind.ICAL_RRULE_PROPERTY);
      prop.setValue(recur);

      valueEntity.addProperty(prop);

      lookUpTimeManagerBean();
      mgrTimeObject = mgrTimeHome.create();

      // Build the report properties

      String strProperties  = monthlyReportParam.getReportName() + IReportOptions.PROPERTY_SEPERATOR + monthlyReportParam.getReportTargetPath() + IReportOptions.PROPERTY_SEPERATOR +  monthlyReportParam.getReportDataSource() + IReportOptions.PROPERTY_SEPERATOR + monthlyReportParam.getReportTemplate() + IReportOptions.PROPERTY_SEPERATOR + monthlyReportParam.getReportFormat();

      iCalTextV value =  new iCalTextV(strProperties);
      iCalPropertyV desc = new iCalPropertyV(iCalPropertyKind.ICAL_DESCRIPTION_PROPERTY);
      desc.setValue(value);
      valueEntity.addProperty(desc);

      valueEntity = mgrTimeObject.addEvent(valueEntity);

      iCalAlarm alarm = new iCalAlarm();
      alarm.setCategory(MESSAGE_CATEGORY);
      alarm.setReceiverKey(RECEIVER_KEY);
      alarm.setRelated(new Integer(IRelated.START));
      alarm.setParentKind(iCalEvent.KIND_EVENT);
      alarm.setParentUid(new Long(valueEntity.getUId()));
      alarm = mgrTimeObject.addAlarm(alarm);


    }
    catch(Exception ex)
    {
     Logger.err( " Exception in ReportGenTimeHelper.scheduleMonthlyReport() : ", ex);
      throw new SystemException(" Exception in ReportGenTimeHelper.scheduleMonthlyReport",ex);
    }
    finally
    {
      mgrTimeHome       = null;
      mgrTimeObject     = null;
      monthlyReportParam  = null;
    }
  }

  public void scheduleWeeklyReport(WeeklyReportScheduleParam weeklyReportParam)
  throws SystemException
  {
      IiCalTimeMgrObj mgrTimeObject = null;
   try{
      int freq = IFrenqency.WEEKLY;
      iCalEvent valueEntity = new iCalEvent();

      valueEntity.setStartDt(weeklyReportParam.getStartDate());
      valueEntity.setEndDt(null);

      iCalRecurrenceV recur = new iCalRecurrenceV();

      recur.setFrequency(freq);
      recur.setInterval(weeklyReportParam.getWeekInterval().intValue());

      Integer totalOccurence  = weeklyReportParam.getTotalOccurence();

      if(totalOccurence == null)
        recur.setUntil(weeklyReportParam.getEndDate());
      else
	recur.setCount(totalOccurence.intValue());

      iCalPropertyV prop = new iCalPropertyV((short)iCalPropertyKind.ICAL_RRULE_PROPERTY);
      prop.setValue(recur);

      valueEntity.addProperty(prop);

      lookUpTimeManagerBean();
      mgrTimeObject = mgrTimeHome.create();

      // Build the report properties

      String strProperties  = weeklyReportParam.getReportName() + IReportOptions.PROPERTY_SEPERATOR + weeklyReportParam.getReportTargetPath() + IReportOptions.PROPERTY_SEPERATOR +  weeklyReportParam.getReportDataSource() + IReportOptions.PROPERTY_SEPERATOR + weeklyReportParam.getReportTemplate() + IReportOptions.PROPERTY_SEPERATOR + weeklyReportParam.getReportFormat();

      iCalTextV value =  new iCalTextV(strProperties);
      iCalPropertyV desc = new iCalPropertyV(iCalPropertyKind.ICAL_DESCRIPTION_PROPERTY);
      desc.setValue(value);
      valueEntity.addProperty(desc);

      valueEntity = mgrTimeObject.addEvent(valueEntity);

      iCalAlarm alarm = new iCalAlarm();
      alarm.setCategory(MESSAGE_CATEGORY);
      alarm.setReceiverKey(RECEIVER_KEY);
      alarm.setRelated(new Integer(IRelated.START));
      alarm.setParentKind(iCalEvent.KIND_EVENT);
      alarm.setParentUid(new Long(valueEntity.getUId()));
      alarm = mgrTimeObject.addAlarm(alarm);


    }
    catch(Exception ex)
    {
     Logger.err( " Exception in ReportGenTimeHelper.scheduleWeeklyReport() : ", ex);
      throw new SystemException(" Exception in ReportGenTimeHelper.scheduleWeeklyReport",ex);
    }
    finally
    {
      mgrTimeHome       = null;
      mgrTimeObject     = null;
      weeklyReportParam  = null;
    }

  }

  private void lookUpTimeManagerBean() throws SystemException
  {
  try
    {
      mgrTimeHome = (IiCalTimeMgrHome)ServiceLookup.getInstance(
                                      ServiceLookup.CLIENT_CONTEXT).getHome(
                    IiCalTimeMgrHome.class);
    }
    catch (Exception ex)
    {
      Logger.err( " Exception in ReportGenTimeHelper.lookUpTimeManagerBean() : ", ex);
      throw new SystemException(" Exception in lookup of IiCalTimeMgrHome",ex);
    }
  }
}