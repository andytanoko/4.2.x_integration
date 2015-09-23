/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GenScheduleHandlerBean.java
 *
 ****************************************************************************
 * Date             	Author               Changes
 ****************************************************************************
 * Jan 19, 2007         Regina Zeng          Created
 * Mar 05, 2007			Alain Ah Ming		 Added error code to error logs
 * Jun 08, 2007			Regina Zeng			 Modified generateReport()
 */

package com.gridnode.gridtalk.genreport.ejb;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gridtalk.genreport.dao.ScheduleDAO;
import com.gridnode.gridtalk.genreport.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.genreport.generate.ReportManager;
import com.gridnode.gridtalk.genreport.model.Report;
import com.gridnode.gridtalk.genreport.model.Schedule;
import com.gridnode.gridtalk.genreport.util.DateUtil;
import com.gridnode.gridtalk.genreport.util.ILogTypes;
import com.gridnode.gridtalk.genreport.util.IReportConstants;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;
import com.gridnode.util.mail.EmailSender;

/**
 * @author Regina Zeng
 * Handles the sending of emails timers.
 */
public class GenScheduleHandlerBean implements SessionBean
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -7030354000263379814L;
  private SessionContext _sc;
  private Logger _logger;
  private ScheduleDAO _dao;
  private TimeZone _tz = TimeZone.getDefault();
    
  public void ejbCreate() throws EJBException, RemoteException
  {
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_GENREPORT_EJB, "GenScheduleHandlerBean");
    _dao = new ScheduleDAO();
  }

  public void ejbActivate() throws EJBException, RemoteException
  {
  }

  public void ejbPassivate() throws EJBException, RemoteException
  {
  }

  public void ejbRemove() throws EJBException, RemoteException
  {
    _logger = null;
    _dao = null;
  }

  public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException
  {
    _sc = arg0;
  }
  
  public boolean deleteReport()
  {
    boolean status = false;
    String mtdName = "deleteReport";
    Object[] params = {};
    
    _logger.logEntry(mtdName, params);
    String nextDeleteDate = (String)ConfigurationStore.getInstance().getProperty(IReportConstants.CAT_2, IReportConstants.KEY_NEXT_DELETE_DATE, null);
    
    long curDate = System.currentTimeMillis();
    Date currentDate = new Date(curDate);
    DateFormat df = new SimpleDateFormat(IReportConstants.DATE_FORMAT);
    String systemDate = (String)df.format(currentDate);
    _logger.logMessage(mtdName, params, "Delete Date is "+nextDeleteDate+ " and System Date is "+systemDate);
    if(nextDeleteDate.equals(systemDate))
    {
      try
      {
        List<Report> listReport = (List)_dao.deleteReport();
        for(int i=0; i<listReport.size(); i++)
        {
          Report r = (Report)listReport.get(i);
          _logger.logMessage(mtdName, params, "Report with UID "+r.getUID()+" has been deleted from the database.");
          _dao.delete(r);
          status = true;
        }
      }
      finally
      {
        _logger.logExit(mtdName, params);
      }
    }
    else
    {
      _logger.logExit(mtdName, params);
    }
    return status;
  }
  
  public void generateReport(Date timeOfCall, int _endRangeCall) 
  {
    ReportManager rm = new ReportManager(_tz);
    String mtdName = "generateReport";
    Object[] params = {timeOfCall};
    
    _logger.logEntry(mtdName, params);
    Date endRangeCall = DateUtil.processEndRangeDate(timeOfCall, _endRangeCall);
    _logger.logMessage(mtdName, params, "End Range Call: "+DateUtil.processDate(endRangeCall));    
    
    try
    {      
      List<Schedule> listSchedule = (List)_dao.getSchedule(timeOfCall, endRangeCall);
      for(int i=0; i<listSchedule.size(); i++)
      {
        Schedule s = (Schedule)listSchedule.get(i);
        String templateName = (String)getTemplateName(s.getReportType());
        String datasourceType = (String)getDatasourceType(s.getReportType());
        rm.scheduleReport(s, s.getUID(), timeOfCall, templateName, datasourceType);
      }
    }
    finally
    {
      _logger.logExit(mtdName, params);
    }
  }
  
  public String createSchedule(Schedule schedule)
  {
    String mtdName = "createSchedule";
    Object[] params = {schedule};
    _logger.logEntry(mtdName, params);
    _dao.associateTransactionContext(false); 
    String groupName = null;
    try
    {
      _dao.beginTransaction();
      String scheduleNo = (String)_dao.create(schedule);
      _dao.commitTransaction();
      _dao.flushSession();
      
      _dao.closeTransactionContext();
      groupName = schedule.getGroupName();
      String msg = "Saved Schedule "+scheduleNo+" for group name "+groupName;
      _logger.logMessage(mtdName, params, msg);
      return scheduleNo;
    }
    catch(Throwable t)
    {
      String msg = "Unable to create schedule for group name "+groupName+": Unexpected Error: "+t.getMessage();
      _logger.logError(ILogErrorCodes.SCHEDULE_HANDLER_CREATE, mtdName, params, msg, t);
      
      //if transaction is still active (this is true if non-db errors are encountered), rollback
      if (_dao.hasTransaction())
      {
        _dao.rollbackTransaction();
      }
      throw new RuntimeException(msg, t);
    }
    finally
    {
      _logger.logExit(mtdName, params);
    }
  }
  
  public boolean updateSchedule(int scheduleId, String reportFormat, String customerList, String emailList, String username, String group, Date currentDateTime)
  {
    String mtdName = "updateSchedule";
    Object[] params = {scheduleId, reportFormat, customerList, emailList, username, group, currentDateTime};
    
    _logger.logEntry(mtdName, params);
    _dao.associateTransactionContext(false); 
    
    try
    {
      _dao.beginTransaction();      
      boolean status = (boolean)_dao.updateQuery(scheduleId, reportFormat, customerList, emailList, username, group, currentDateTime);
      if(status)
        _logger.logMessage(mtdName, params, "Schedule Report updated successfully!");
      else
        _logger.logWarn(mtdName, params, "No record is updated.", null);
      _dao.commitTransaction();
      _dao.flushSession();
      
      _dao.closeTransactionContext();
      String msg = "Schedule updated for scheduleId "+scheduleId;
      _logger.logMessage(mtdName, params, msg);
      return status;
    }
    catch(Throwable t)
    {
      String msg = "Unable to update schedule for scheduleId "+scheduleId+": Unexpected Error: "+t.getMessage();
      _logger.logError(ILogErrorCodes.SCHEDULE_HANDLER_UPDATE, mtdName, params, msg, t);
      
      //if transaction is still active (this is true if non-db errors are encountered), rollback
      if (_dao.hasTransaction())
      {
        _dao.rollbackTransaction();
      }
      throw new RuntimeException(msg, t);
    }
    finally
    {
      _logger.logExit(mtdName, params);
    }
  }
  
  public Properties updateSchedule(Schedule s, String scheduleNo)
  {
    String mtdName = "updateSchedule";
    Object[] params = {s};
    
    _logger.logEntry(mtdName, params);
    Properties props = (Properties)ConfigurationStore.getInstance().getProperties(EmailSender.DEFAULT_SMTP_SERVER);
    
    _dao.associateTransactionContext(false); 
    int scheduleId = 0;
    try
    {
      _dao.beginTransaction();
      boolean status = (boolean)_dao.updateQuery(s, scheduleNo);
      if(status)
        _logger.logMessage(mtdName, params, "Schedule Report updated successfully!");
      else
        _logger.logWarn(mtdName, params, "No record is updated.", null);
      
      _dao.commitTransaction();
      _dao.flushSession();
      
      _dao.closeTransactionContext();
      scheduleId = s.getScheduleId();
      String msg = "Schedule updated for scheduleId "+scheduleId;
      _logger.logMessage(mtdName, params, msg);
      return props;
    }
    catch(Throwable t)
    {
      String msg = "Unable to update schedule for scheduleId "+scheduleId+": Unexpected Error: " +t.getMessage();
      _logger.logError(ILogErrorCodes.SCHEDULE_HANDLER_UPDATE, mtdName, params, msg, null);
      
      //if transaction is still active (this is true if non-db errors are encountered), rollback
      if (_dao.hasTransaction())
      {
        _dao.rollbackTransaction();
      }
      throw new RuntimeException(msg, t);
    }
    finally
    {
      _logger.logExit(mtdName, params);
    }
  }
  
  public Schedule getSchedule(int id)
  {
    String mtdName = "getSchedule";
    Object[] params = {id};
    
    _logger.logEntry(mtdName, params);
    Schedule s = (Schedule)_dao.getSchedule(id);
    _logger.logExit(mtdName, params);
    return s;
  }
  
  public String getTemplateName(String reportType)
  {
    String mtdName = "getTemplateName";
    Object[] params = {reportType};
    
    _logger.logEntry(mtdName, params);
    String templateName = (String)_dao.getTemplateName(reportType);
    _logger.logExit(mtdName, params);
    return templateName;
  }
  
  public String getDatasourceType(String reportType)
  {
    String mtdName = "getDatasourceType";
    Object[] params = {reportType};
    
    _logger.logEntry(mtdName, params);
    String datasourceType = (String)_dao.getDatasourceType(reportType);
    _logger.logExit(mtdName, params);
    return datasourceType;
  }
  
  public String getValue(String cat, String key)
  {
    String mtdName = "getValue";
    Object[] params = {cat};
    _logger.logEntry(mtdName, params);
    Properties props = (Properties)ConfigurationStore.getInstance().getProperties(cat);
    _logger.logExit(mtdName, params);
    return (String)props.getProperty(key);
  }
  
  public void updateNextDeleteDate()
  {
    String mtdName = "updateNextDeleteDate";
    Object[] params = {};
    _logger.logEntry(mtdName, params);
    
    String frequency = (String)ConfigurationStore.getInstance().getProperty(IReportConstants.CAT_2, IReportConstants.KEY_FREQUENCY, null);
    String oldDelDate = (String)ConfigurationStore.getInstance().getProperty(IReportConstants.CAT_2, IReportConstants.KEY_NEXT_DELETE_DATE, null);
    DateFormat df = new SimpleDateFormat(IReportConstants.DATE_FORMAT);
    Date oldDeleteDate = null;
    try 
    {
      oldDeleteDate = df.parse(oldDelDate);
    } 
    catch (ParseException e) 
    {
      e.printStackTrace();
    }
    
    if(!frequency.equals("Once") && oldDeleteDate != null)
    {
      String nextDeleteDate = generateNextDeleteDate(oldDeleteDate, frequency, df);
      if(nextDeleteDate != null)
      {
        boolean nextDeleteDateStatus = (boolean)ConfigurationStore.getInstance().updateProperties(IReportConstants.CAT_2, IReportConstants.KEY_NEXT_DELETE_DATE, nextDeleteDate);
        if(nextDeleteDateStatus)
        {
          _logger.logMessage(mtdName, params, "Next Delete Date will be "+nextDeleteDate);
        }
        else
        {
          _logger.logWarn(mtdName, params, "No record is updated.", null);
        }
      }
    }
    
    _logger.logExit(mtdName, params);
  }
  
  private String generateNextDeleteDate(Date oldDeleteDate, String frequency, DateFormat df)
  {
    String deleteDate = null;
    if(frequency.equals("Daily"))
    {
      deleteDate = (String)df.format(DateUtil.getNextDaily(oldDeleteDate));	
    }
    else if(frequency.equals("Weekly"))
    {
      deleteDate = (String)df.format(DateUtil.getNextWeekly(oldDeleteDate));	
    }
    else 
    {
      deleteDate = (String)df.format(DateUtil.getNextMonthy(oldDeleteDate));	
    }
    return deleteDate;
  }
}
