/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ScheduleDAO.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Jan 19, 2007        Regina Zeng          Created
 */

package com.gridnode.gridtalk.genreport.dao;

import java.util.Date;
import java.util.List;

import com.gridnode.gridtalk.genreport.model.Report;
import com.gridnode.gridtalk.genreport.model.ReportType;
import com.gridnode.gridtalk.genreport.model.Schedule;
import com.gridnode.util.db.DAO;

/**
 * @author Regina Zeng
 * This DAO handles the persistence for Schedule.
 */
public class ScheduleDAO extends DAO
{  
  public ScheduleDAO()
  {    
  }
  
  public synchronized List deleteReport()
  {
    List<Report> list = null;
    String queryName = Report.class.getName()+".deleteReport";
    String[] paramNames = {};
    Object[] paramVals = {};
    
    associateTransactionContext(true);
    boolean hasTx = !hasTransaction();
    try
    {
      if(!hasTx)
      {
        beginTransaction();
      }
      list = (List)query(queryName, paramNames, paramVals);
      if(!hasTx)
      {
        commitTransaction();
      }
    }
    catch(Exception e)
    {
      System.out.println("[ScheduleDAO.deleteReport] Error fetching report.");
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
    return list;
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
      System.out.println("[ScheduleDAO.getTemplateName] Error fetching report template.");
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
   * @return String datasource type 
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
      System.out.println("[ScheduleDAO.getTemplateName] Error fetching report template datasource.");
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
   * @param schedule object
   * @param scheduleNo report UID
   * @return true or false whether the SQL query has been updated to the database
   */
  public synchronized boolean updateQuery(Schedule schedule, String scheduleNo)
  {
    String queryName = Schedule.class.getName()+".updateSchedule";
    String[] paramNames = {"next_run_date_time", "next_start_date_time", "next_end_date_time", "uid"};
    Object[] paramVals = {schedule.getNextRunDateTime(), schedule.getNextStartDateTime(), schedule.getNextEndDateTime(), scheduleNo};
    
    int result = updateQuery(queryName, paramNames, paramVals);
    if (result != 1)
    {
      return false;
    }
    else
      return true;
  }
  
  /**
   * This method update a report object into the database.
   * @param schedule object
   * @return true or false whether the SQL query has been updated to the database
   */
  public synchronized boolean updateQuery(int scheduleId, String reportFormat, String customerList, String emailList, String username, String group, Date modifiedDateTime)
  {
    String queryName = Schedule.class.getName()+".modifySchedule";
    String[] paramNames = {"id", "format", "cust", "email", "name", "groupName", "date"};
    Object[] paramVals = {scheduleId, reportFormat, customerList, emailList, username, group, modifiedDateTime};
    
    int result = updateQuery(queryName, paramNames, paramVals);
    if (result != 1)
    {
      return false;
    }
    else
      return true;
  }
  
  public synchronized Schedule getSchedule(int scheduleId)
  {
    Schedule s = null;
    
    String queryName = Schedule.class.getName()+".getSchedule";
    String[] paramNames = {"id"};
    Object[] paramVals = {scheduleId};
    
    associateTransactionContext(true);
    boolean hasTx = !hasTransaction();
    try
    {
      if(!hasTx)
      {
        beginTransaction();
      }
      s = (Schedule)queryOne(queryName, paramNames, paramVals);
      if(!hasTx)
      {
        commitTransaction();
      }
    }
    catch(Exception e)
    {
      System.out.println("[ScheduleDAO.getSchedule] Error fetching a schedule: "+scheduleId);
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
    return s;
  }
  
  /**
   * This method uses query() to fetch a List of schedule from database, 
   * where the nextRunDateTime is between the startRangeCall and the endRangeCall.
   * @param startRangeCall - start range of the calling
   * @param endRangeCall - end range of the calling
   * @return List of schedules
   */
  public synchronized List getSchedule(Date startRangeCall, Date endRangeCall)
  {   
    List<Schedule> list = null;
    String queryName = Schedule.class.getName()+".getScheduleReport";
    String[] paramNames = {"startRangeCall", "endRangeCall"};
    Object[] paramVals = {startRangeCall, endRangeCall};
    
    associateTransactionContext(true);
    boolean hasTx = !hasTransaction();
    try
    {
      if(!hasTx)
      {
        beginTransaction();
      }
      list = (List)query(queryName, paramNames, paramVals);
      if(!hasTx)
      {
        commitTransaction();
      }
    }
    catch(Exception e)
    {
      System.out.println("[ScheduleDAO.getScheduleReport] Error fetching schedule reports.");
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
    return list;
  }
}
