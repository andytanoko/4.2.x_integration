/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportGenManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 11 2002    H.Sushil            Created
 */

package com.gridnode.pdip.app.reportgen.facade.ejb;


import com.gridnode.pdip.app.reportgen.value.IReportOptions;

import com.gridnode.pdip.app.reportgen.value.DailyReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.MonthlyReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.OneOffReportScheduleParam;
import com.gridnode.pdip.app.reportgen.value.WeeklyReportScheduleParam;

import com.gridnode.pdip.app.reportgen.helpers.Logger;
import com.gridnode.pdip.app.reportgen.helpers.ReportGenHelper;
import com.gridnode.pdip.app.reportgen.helpers.ReportGenTimeHelper;
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
import com.gridnode.pdip.base.time.entities.value.iCalValueV;
import com.gridnode.pdip.base.time.entities.value.IRelated;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.exceptions.SystemException;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;

import java.io.File;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;



/**
 * This bean manages the Report Generation.
 *
 * @author H.Sushil
 *
 * @version 1.0
 * @since 1.0
 */

public class ReportGenManagerBean implements SessionBean
{
  transient private SessionContext _sessionCtx = null;

  private IiCalTimeMgrHome mgrTimeHome;

  private static String MESSAGE_CATEGORY   = "ReportGenTimer";
  private static String RECEIVER_KEY       = "ReportGenHandler";
  private static String SENDER_KEY         = "ReportGenScheduler";

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

// ************************ Implementing methods in IReportGenManagerObj

 /** Method for generating PDF Report
  * The report file is generated in the Temporary Directory
  *
  * @param reportDataSource the report datasource file path (with respect to the File Server)
  * @param reportTemplate the report template file path(with respect to the File Server)
  * @param reportTargetPath the report datasource file path(with respect to the File Server)
  * @param reportTargetFileName the report name without Extension
  *
  * @return true if report generation was successfull false otherwise
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */

  public boolean generatePDFReport(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName)
  throws SystemException
  {
    boolean flag=false;
    try
    {
      flag = ReportGenHelper.generatePDFReport(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName,false);
    }
    catch(Exception excep)
    {
      Logger.err("[ReportGenManagerBean.generatePDFReport] Error ", excep);
      throw new SystemException("ReportGenManagerBean.generatePDFReport() Error ",excep);
    }
    return flag;
  }

 /** Method for generating XML Report
  * The report file is generated in the Temporary Directory
  *
  * @param reportDataSource the report datasource file path (with respect to the File Server)
  * @param reportTemplate the report template file path(with respect to the File Server)
  * @param reportTargetPath the report datasource file path(with respect to the File Server)
  * @param reportTargetFileName the report name without Extension
  *
  * @return true if report generation was successfull false otherwise
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */


  public boolean generateXMLReport(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName)
  throws SystemException
  {
    boolean flag=false;
    try
    {
      flag = ReportGenHelper.generateXMLReport(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName,false);
    }
    catch(Exception excep)
    {
       Logger.err("[ReportGenManagerBean.generateXMLReport] Error ", excep);
       throw new SystemException("ReportGenManagerBean.generateXMLReport() Error ",excep);
    }
    return flag;
 }

 /** Method for generating HTML Report
  * The report file is generated in the Temporary Directory
  *
  * @param reportDataSource the report datasource file path (with respect to the File Server)
  * @param reportTemplate the report template file path(with respect to the File Server)
  * @param reportTargetPath the report datasource file path(with respect to the File Server)
  * @param reportTargetFileName the report name without Extension
  *
  * @return true if report generation was successfull false otherwise
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */


  public boolean generateHTMLReport(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName)
  throws SystemException
  {
    boolean flag=false;
    try
    {
      flag = ReportGenHelper.generateHTMLReport(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName,false);
    }
    catch(Exception excep)
    {
      Logger.err("[ReportGenManagerBean.generateHTMLReport] Error ", excep);
      throw new SystemException("ReportGenManagerBean.generateHTMLReport() Error ",excep);
    }
    return flag;
  }

 /** Method for generating PDF Report
  * The report file is generated in the Temporary Directory and also saved to the
  * specified directory in the File Server
  *
  * @param reportDataSource the report datasource file path (with respect to the File Server)
  * @param reportTemplate the report template file path(with respect to the File Server)
  * @param reportTargetPath the report datasource file path(with respect to the File Server)
  * @param reportTargetFileName the report name without Extension
  *
  * @return true if report generation was successfull false otherwise
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */

  public boolean generatePDFReportAndSave(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName)
  throws SystemException
  {
    boolean flag=false;
    try
    {
      flag = ReportGenHelper.generatePDFReport(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName,true);
    }
    catch(Exception excep)
    {
      Logger.err("[ReportGenManagerBean.generatePDFReportAndSave] Error ", excep);
      throw new SystemException("ReportGenManagerBean.generatePDFReportAndSave() Error ",excep);
    }
    return flag;
  }

 /** Method for generating XML Report
  * The report file is generated in the Temporary Directory and also saved to the
  * specified directory in the File Server
  *
  * @param reportDataSource the report datasource file path (with respect to the File Server)
  * @param reportTemplate the report template file path(with respect to the File Server)
  * @param reportTargetPath the report datasource file path(with respect to the File Server)
  * @param reportTargetFileName the report name without Extension
  *
  * @return true if report generation was successfull false otherwise
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */


  public boolean generateXMLReportAndSave(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName)
  throws SystemException
  {
    boolean flag=false;
    try
    {
      flag = ReportGenHelper.generateXMLReport(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName,true);
    }
    catch(Exception excep)
    {
       Logger.err("[ReportGenManagerBean.generateXMLReportAndSave] Error ", excep);
       throw new SystemException("ReportGenManagerBean.generateXMLReportAndSave() Error ",excep);
    }
    return flag;
  }

 /** Method for generating HTML Report
  * The report file is generated in the Temporary Directory and also saved to the
  * specified directory in the File Server
  *
  * @param reportDataSource the report datasource file path (with respect to the File Server)
  * @param reportTemplate the report template file path(with respect to the File Server)
  * @param reportTargetPath the report datasource file path(with respect to the File Server)
  * @param reportTargetFileName the report name without Extension
  *
  * @return true if report generation was successfull false otherwise
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */



  public boolean generateHTMLReportAndSave(String reportDataSource,String reportTemplate,String reportTargetPath,String reportTargetFileName)
  throws SystemException
  {
    boolean flag=false;
    try
    {
      flag = ReportGenHelper.generateHTMLReport(reportDataSource,reportTemplate,reportTargetPath,reportTargetFileName,true);
    }
    catch(Exception excep)
    {
      Logger.err("[ReportGenManagerBean.generateHTMLReportAndSave] Error ", excep);
      throw new SystemException("ReportGenManagerBean.generateHTMLReportAndSave() Error ",excep);
    }
    return flag;
  }

 /** Method for copying the report file from the Temporary directory to the
  * specified directory in the File Server
  *
  * @param reportName the report name without Extension
  * @param reportTargetPath the report datasource file path(with respect to the File Server)
  * @param reportMainFile The File Object for the report mail file
  * @param reportSubFiles The Array of File Objects for the report files
  *
  * @return true if the copy operation was successfull false otherwise
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */


  public boolean copyReportToFileServer(String reportName,String reportTargetPath,File reportMainFile,File[] reportSubFiles)
  throws SystemException
  {
    boolean flag=false;
    try
    {
      flag = ReportGenDocServiceHelper.instance().copyReportToFileServer(reportName,reportTargetPath,reportMainFile,reportSubFiles);
    }
    catch(Exception excep)
    {
      Logger.err("[ReportGenManagerBean.copyReportToFileServer] Error ", excep);
      throw new SystemException("ReportGenManagerBean.copyReportToFileServer() Error ",excep);
    }
    return flag;
  }

  /********** Methods for report scheduling using the Time module *************/


 /** Method for scheduling report generation on a daily basis
  *
  * @param dailyReportParam Object that has the settings for daily report generation
  *
  * @return none
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */


  public void scheduleDailyReport(DailyReportScheduleParam dailyReportParam) throws SystemException
  {
  try{
	ReportGenTimeHelper.instance().scheduleDailyReport(dailyReportParam);
     }
    catch(Exception ex)
    {
      Logger.err( " Exception in ReportGenManagerBean.scheduleDailyReport() : ", ex);
      throw new SystemException(" Exception in ReportGenManagerBean.scheduleDailyReport",ex);
    }
  }

 /** Method for scheduling report generation on a one-off basis
  *
  * @param oneOffReportParam Object that has the settings for a one-off generation
  *
  * @return none
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */


  public void scheduleOneOffReport(OneOffReportScheduleParam oneOffReportParam)
  throws SystemException
  {

   try{
        ReportGenTimeHelper.instance().scheduleOneOffReport(oneOffReportParam);
    }
    catch(Exception ex)
    {
      Logger.err( " Exception in ReportGenManagerBean.scheduleOneOffReport() : ", ex);
      throw new SystemException(" Exception in ReportGenManagerBean.scheduleOneOffReport",ex);
    }
  }

 /** Method for scheduling report generation on a Monthly basis
  *
  * @param monthlyReportParam Object that has the settings for monthly report generation
  *
  * @return none
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */


  public void scheduleMonthlyReport(MonthlyReportScheduleParam monthlyReportParam)
  throws SystemException
  {
   try{
      ReportGenTimeHelper.instance().scheduleMonthlyReport(monthlyReportParam);
    }
    catch(Exception ex)
    {
     Logger.err( " Exception in ReportGenManagerBean.scheduleMonthlyReport() : ", ex);
      throw new SystemException(" Exception in ReportGenManagerBean.scheduleMonthlyReport",ex);
    }
  }

 /** Method for scheduling report generation on a Weekly basis
  *
  * @param weeklyReportParam Object that has the settings for Weekly report generation
  *
  * @return none
  *
  * @author H.Sushil
  *
  * @version 1.0
  * @since 1.0
  */


  public void scheduleWeeklyReport(WeeklyReportScheduleParam weeklyReportParam)
  throws SystemException
  {
   try{
      ReportGenTimeHelper.instance().scheduleWeeklyReport(weeklyReportParam);
    }
    catch(Exception ex)
    {
     Logger.err( " Exception in ReportGenManagerBean.scheduleWeeklyReport() : ", ex);
      throw new SystemException(" Exception in ReportGenManagerBean.scheduleWeeklyReport",ex);
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
      Logger.err( " Exception in ReportGenManagerBean.lookUpTimeManagerBean() : ", ex);
      throw new SystemException(" Exception in lookup of IiCalTimeMgrHome",ex);
    }
  }
}