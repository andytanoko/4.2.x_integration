/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportScheduler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 19 2002    H.Sushil            Created
 */


package com.gridnode.pdip.app.reportgen.bean;


import com.gridnode.pdip.app.reportgen.facade.ejb.IReportGenManagerHome;
import com.gridnode.pdip.app.reportgen.facade.ejb.IReportGenManagerObj;

import com.gridnode.pdip.app.reportgen.helpers.Logger;
import com.gridnode.pdip.app.reportgen.value.OneOffReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.DailyReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.WeeklyReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.MonthlyReportScheduleParam;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.exceptions.SystemException;

import java.io.File;
import java.util.StringTokenizer;



 /** Java Bean Class for report Scheduling
   * Helps to access the session facade from web-Interfaces like JSPs,Servlets
   * @author H.Sushil
   *
   * @version 1.0
   * @since 1.0
   */

public class ReportScheduler
{

  private IReportGenManagerHome    _reportGenHome;
  private IReportGenManagerObj     _reportGenMgr;

  public ReportScheduler()
  {
    try{
      lookupReportGenMgr();
    }
    catch(Exception e)
    {
      Logger.err(" Error in look up of IReportGenManagerHome ",e);
    }
  }

  public void scheduleDailyReport(DailyReportScheduleParam dailyReportParam) throws SystemException
  {
    try{
      _reportGenMgr.scheduleDailyReport(dailyReportParam);
    }
    catch(Exception e)
    {
      Logger.err(" Error in ReportScheduler.scheduleDaily ",e);
      throw new SystemException(" Exception at ReportScheduler.scheduleDaily ",e);
    }
  }

  public void scheduleOneOffReport(OneOffReportScheduleParam oneOffReportParam)
  throws SystemException
  {
   try{
      _reportGenMgr.scheduleOneOffReport(oneOffReportParam);
    }
    catch(Exception e)
    {
      Logger.err(" Error in ReportScheduler.scheduleOneOffReport ",e);
      throw new SystemException(" Exception at ReportScheduler.scheduleOneOffReport ",e);
    }
  }
  public void scheduleMonthlyReport(MonthlyReportScheduleParam monthlyReportParam)
  throws SystemException
  {
    try{
      _reportGenMgr.scheduleMonthlyReport(monthlyReportParam);
    }
    catch(Exception e)
    {
      Logger.err(" Error in ReportScheduler.scheduleMonthlyReport ",e);
      throw new SystemException(" Exception at ReportScheduler.scheduleMonthlyReport ",e);
    }
  }

  public void scheduleWeeklyReport(WeeklyReportScheduleParam weeklyReportParam)
  throws SystemException
  {
    try{
      _reportGenMgr.scheduleWeeklyReport(weeklyReportParam);
    }
    catch(Exception e)
    {
      Logger.err(" Error in ReportScheduler.scheduleWeeklyReport ",e);
      throw new SystemException(" Exception at ReportScheduler.scheduleWeeklyReport ",e);
    }

  }

  private void lookupReportGenMgr() throws Exception
  {
   _reportGenHome = (IReportGenManagerHome)ServiceLookup.getInstance(
                  ServiceLookup.CLIENT_CONTEXT).getHome(
                  IReportGenManagerHome.class);
   _reportGenMgr = _reportGenHome.create();
  }

}