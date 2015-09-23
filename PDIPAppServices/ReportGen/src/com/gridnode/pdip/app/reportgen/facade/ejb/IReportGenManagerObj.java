/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IReportGenManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2002    H.Sushil              Created
 */
package com.gridnode.pdip.app.reportgen.facade.ejb;

import com.gridnode.pdip.app.reportgen.value.DailyReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.OneOffReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.MonthlyReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.WeeklyReportScheduleParam;


import com.gridnode.pdip.framework.exceptions.*;

import javax.ejb.EJBObject;
import javax.ejb.CreateException;
import javax.ejb.Handle;

import java.rmi.RemoteException;

import java.util.Collection;
import java.util.BitSet;

import java.io.File;

/**
 * Object for ReportGenManagerBean.
 *
 * @author H.Sushil
 *
 * @version 1.0
 * @since 1.0
 */
public interface IReportGenManagerObj
  extends        EJBObject
{
  public boolean generatePDFReport(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName)
  throws SystemException,RemoteException;

  public boolean generateXMLReport(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName)
  throws SystemException,RemoteException;

  public boolean generateHTMLReport(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName)
  throws SystemException,RemoteException;

  public boolean generatePDFReportAndSave(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName)
  throws SystemException,RemoteException;

  public boolean generateXMLReportAndSave(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName)
  throws SystemException,RemoteException;

  public boolean copyReportToFileServer(String reportName,String reportTargetPath,File reportMainFile,File[] reportSubFiles)
  throws SystemException,RemoteException;

  public boolean generateHTMLReportAndSave(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName)
  throws SystemException,RemoteException;

  public void scheduleDailyReport(DailyReportScheduleParam dailyReportParam)
  throws SystemException,RemoteException;

  public void scheduleOneOffReport(OneOffReportScheduleParam oneOffReportParam)
  throws SystemException,RemoteException;

  public void scheduleMonthlyReport(MonthlyReportScheduleParam monthlyReportParam)
  throws SystemException,RemoteException;

  public void scheduleWeeklyReport(WeeklyReportScheduleParam weeklyReportParam)
  throws SystemException,RemoteException;

}