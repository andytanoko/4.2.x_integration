/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGenReportHandler.java
 *
 ****************************************************************************
 * Date 	            Author              Changes
 ****************************************************************************
 * Jan 19, 2007        Regina Zeng          Created
 * Mar 14, 2007		   Regina Zeng			Added updateDeleteScheduler()
 */

package com.gridnode.gridtalk.genreport.ejb;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Properties;

import javax.ejb.EJBObject;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import com.gridnode.gridtalk.genreport.model.Report;

/**
 * @author Regina Zeng
 * This remote interface defines the business methods for GenReportHandlerBean.
 */
public interface IGenReportHandler extends EJBObject
{
  /**
   * Update a specific report to the database.
   * @param report The Report object itself
   * @param reportNo Report UID
   * @return Properties of the EmailSender
   * @throws RemoteException
   * @throws RuntimeException If unable to save report to database. 
   */
  public Properties updateReport(Report report, String reportNo) throws RemoteException;
  
  /**
   * Create a report stored in database after scheduled report is generated
   * @param  The Report object itself
   * @return reportNo [0]=reportNo [1]=template name [2]=template datasource type
   * @throws RuntimeException If unable to save report to database. 
   */
  public String[] createReport(Report report) throws RemoteException;
  
  /**
   * To retrieve the property value base on the category and property key
   * @param category
   * @param property key
   * @return property value
   * @throws RemoteException
   */
  public String getValue(String cat, String key) throws RemoteException;
  
  /**
   * To retrieve a specific report base on the uid
   * @param uid
   * @return Report
   * @throws RemoteException
   */
  public Report getReport(String uid) throws RemoteException;
  
  /**
   * To update the delete Scheduler report in the config_props table
   * @param cat
   * @param keyFrequency
   * @param keyNextDeleteDate
   * @param archiveDuration
   * @param frequency
   * @param byDate
   * @param numDays
   * @return Status 
   * @throws RemoteException
   */
  public boolean updateDeleteScheduler(String cat, String keyFrequency, String keyNextDeleteDate, 
		  							   String archiveDuration, String frequency, String byDate, 
		  							   String numDays) throws RemoteException;
  
  /**
   * To update the archive duration for all reports
   * @param numDays
   * @throws RemoteException
   */
  public void updateReport(String numDays) throws RemoteException;
  
  /**
   * To return a JasperPrint.
   * @param JasperReport jasperReport
   * @param HashMap as parameter
   * @param datasourceDAO for report template
   * @return JasperPrint
   * @throws RemoteException
   */
  public JasperPrint getJasperPrint(JasperReport jasperReport, HashMap parameter, String datasourceDAO) throws RemoteException;
}
