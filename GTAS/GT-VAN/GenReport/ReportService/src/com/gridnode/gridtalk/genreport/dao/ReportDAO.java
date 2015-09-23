/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReportDAO.java
 *
 ****************************************************************************
 * Date		             Author              Changes
 ****************************************************************************
 * Jan 19, 2007        Regina Zeng        Created
 * Mar 12, 2007		     Regina Zeng		   	Added getReport()
 * Apr 10, 2007        Regina Zeng        Added getDatasourceType()    
 */

package com.gridnode.gridtalk.genreport.dao;

import com.gridnode.gridtalk.genreport.model.Report;
import com.gridnode.gridtalk.genreport.model.ReportType;
import com.gridnode.util.db.DAO;

/**
 * @author Regina Zeng
 * This DAO handles retrieval of report type properties and report properties
 */
public class ReportDAO extends DAO
{
  public ReportDAO()
  {          
  }  
  
  /**
   * This method uses queryOne() to fetch a report from database.
   * @param reportUid
   * @return Report object
   */
  public synchronized Report getReport(String reportUid)
  {
    Report report = null;
	
    String queryName = Report.class.getName()+".getReport";
    String[] paramNames = {"uid"};
    Object[] paramVals = {reportUid};
    
    associateTransactionContext(true);
    boolean hasTx = !hasTransaction();
    try
    {
      if(!hasTx)
      {
        beginTransaction();
      }
      report = (Report)queryOne(queryName, paramNames, paramVals);
      if(!hasTx)
      {
        commitTransaction();
      }
    }
    catch(Exception e)
    {
      System.out.println("[ReportDAO.getReport] Error fetching report.");
      e.printStackTrace(System.out);
      if(!hasTx)
      {
        rollbackTransaction();
      }
    }
    finally
    {
      if(!hasTx)
      {
        closeTransactionContext();
      }
    }
    return report;
  }
  
  /**
   * This method uses queryOne() to fetch a reportType from database.
   * @param reportType 
   * @return String report template name
   */
  public synchronized String getTemplateName(String reportType)
  {   
    String name = null;
    
    String queryName = ReportType.class.getName()+".getTemplate";
    String[] paramNames = {"report_type"};
    Object[] paramVals = {reportType};
    
    associateTransactionContext(true);
    boolean hasTx = !hasTransaction();
    try
    {
      if(!hasTx)
      {
        beginTransaction();
      }
      ReportType rt = (ReportType)queryOne(queryName, paramNames, paramVals);
      name = rt.getTemplate();
      if(!hasTx)
      {
        commitTransaction();
      }
    }
    catch(Exception e)
    {
      System.out.println("[ReportDAO.getTemplateName] Error fetching report template.");
      e.printStackTrace(System.out);
      if(!hasTx)
      {
        rollbackTransaction();
      }
    }
    finally
    {
      if(!hasTx)
      {
        closeTransactionContext();
      }
    }
    return name;
  }
  
  /**
   * This method uses queryOne() to fetch a reportType from database.
   * @param reportType
   * @return String template datasource type (Eg. 1 for DefaultDAO, 2 for UserDAO)
   */
  public synchronized String getDatasourceType(String reportType)
  {   
    String datasourceType = null;
    
    String queryName = ReportType.class.getName()+".getDatasourceType";
    String[] paramNames = {"report_type"};
    Object[] paramVals = {reportType};
    
    associateTransactionContext(true);
    boolean hasTx = !hasTransaction();
    try
    {
      if(!hasTx)
      {
        beginTransaction();
      }
      ReportType rt = (ReportType)queryOne(queryName, paramNames, paramVals);
      datasourceType = rt.getDatasourceType();
      if(!hasTx)
      {
        commitTransaction();
      }
    }
    catch(Exception e)
    {
      System.out.println("[ReportDAO.getDatasourceType] Error fetching report template datasource.");
      e.printStackTrace(System.out);
      if(!hasTx)
      {
        rollbackTransaction();
      }
    }
    finally
    {
      if(!hasTx)
      {
        closeTransactionContext();
      }
    }
    return datasourceType;
  }
  
  /**
   * This method update a report object into the database.
   * @param report object
   * @param reportNo report UID
   * @return true or false whether the SQL query has been updated to the database
   */
  public synchronized boolean updateQuery(Report report, String reportNo)
  {
    String queryName = Report.class.getName()+".updateReport";
    String[] paramNames = {"status", "report_content", "report_location", "uid"};
    Object[] paramVals = {report.getStatus(), report.getReportContent(), report.getReportLocation(), reportNo};
    
    int result = updateQuery(queryName, paramNames, paramVals);
    if (result != 1)
    {
      return false;
    }
    else
      return true;
  }
  
  /**
   * This method update all the report archive duration into the database
   * @param archiveDuration
   * @return status
   */
  public synchronized boolean updateArchiveDuration(String days)
  {
    int archiveDuration = Integer.parseInt(days);
    String queryName = Report.class.getName()+".updateArchiveDuration";
    String[] paramNames = {"archive_duration"};
    Object[] paramVals = {archiveDuration};
    
    int result = updateQuery(queryName, paramNames, paramVals);
    if (result != 1)
    {
      return false;
    }
    else
      return true;
  }
}

