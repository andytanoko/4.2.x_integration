/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveSchedulerServlet.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 14, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.gtas.audit.archive.IArchiveConstant;
import com.gridnode.gtas.audit.archive.scheduler.exception.ArchiveScheduleException;
import com.gridnode.gtas.audit.archive.scheduler.facade.ejb.IArchiveSchedulerHome;
import com.gridnode.gtas.audit.archive.scheduler.facade.ejb.IArchiveSchedulerObj;
import com.gridnode.gtas.audit.exceptions.ILogErrorCodes;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerHome;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerObj;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.tracking.helpers.IISATProperty;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveSchedulerServlet extends HttpServlet
{
  private static final String CLASS_NAME = "ArchiveSchedulerServlet";
  private Logger _logger = null;
  
  public void init()
  {
    _logger = getLogger();
  }
  
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String method = "doGet";
    String debugInfo = "";
    String scheduleTask = request.getParameter(ISchedulerConstant.SCHEDULE_ACT);
    boolean isScheduleActSuccess = true;
    
    _logger.logMessage(method, null, "ScheduleTask: "+scheduleTask);
    try
    {
      if(ISchedulerConstant.SCHEDULE_ACT_CREATE.equals(scheduleTask))
      {
        Hashtable archiveScheduleData = getArchiveScheduleCreateData(request);
        getArchiveScheduleMgr().addArchiveSchedule(archiveScheduleData);
      }
      else if(ISchedulerConstant.SCHEDULE_ACT_UPDATE.equals(scheduleTask))
      {
        Hashtable archiveScheduleData = getArchiveScheduleUpdateData(request);
        String archiveScheduleUID = (String)archiveScheduleData.get(ISchedulerConstant.ARCHIVE_SCHEDULE_UID);
        debugInfo = archiveScheduleUID;
        getArchiveScheduleMgr().updateArchiveScheduler(archiveScheduleUID, archiveScheduleData);
      }
      else if(ISchedulerConstant.SCHEDULE_ACT_ARCHIVE_NOW.equals(scheduleTask))
      {
        String archiveScheduleUID = getArchiveScheduleUID(request);
        debugInfo = archiveScheduleUID;
        getArchiveScheduleMgr().runArchiveSchedulerNow(archiveScheduleUID);
      }
      else if(ISchedulerConstant.SCHEDULE_ACT_DELETE.equals(scheduleTask))
      {
        String archiveScheduleUID = getArchiveScheduleUID(request);
        debugInfo = archiveScheduleUID;
        getArchiveScheduleMgr().deleteArchiveSchedulerRecord(archiveScheduleUID);
      }
      else
      {
        throw new IllegalArgumentException("The given archive schedule task "+scheduleTask+" is not supported !");
      }
    }
    catch(Exception ex)
    {
      isScheduleActSuccess = false;
      _logger.logError(ILogErrorCodes.AT_ARCHIVE_SCHEDULER_SERVICE_ERROR, method, null, "Error in handling the Archive Scheduler services ["+scheduleTask+"]" +ex.getMessage()+ "Debug Info "+debugInfo, ex);
    }
    finally
    {
      writeResponseToClient(scheduleTask, response, isScheduleActSuccess);
    }
  }
  
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    doGet(request, response);
  }
  
  private void writeResponseToClient(String scheduleTask, HttpServletResponse response, boolean isScheduleActSuccess)
  {
    try
    {
      String responseURL = getArchiveScheduleMgr().getArchiveSchedulerResponseURL();
      if(responseURL == null)
      {
        throw new NullPointerException("The response URL is null. Pls initialize in DB");
      }
      else
      {
        int statusCode = isScheduleActSuccess ? 1 : 0;
        String archiveScheduleURL = responseURL+"?"+IArchiveConstant.ARCHIVE_ACT+"="+scheduleTask+"&"+IArchiveConstant.ARCHIVE_ACT_STATUS+"="+statusCode;
        _logger.logMessage("writeResponseToClient", null, "Archive Schedule URL is "+archiveScheduleURL);
      
        response.sendRedirect(archiveScheduleURL);
      }
    }
    catch(Exception ex)
    {
      _logger.logWarn("writeResponseToClient", null, "Error in writting respons to client", ex);
    }
  }
  
  private IArchiveSchedulerObj getArchiveScheduleMgr() throws Exception
  {
    JndiFinder jndiFinder = new JndiFinder(null);
    IArchiveSchedulerHome home = (IArchiveSchedulerHome)jndiFinder.lookup(IArchiveSchedulerHome.class.getName(), IArchiveSchedulerHome.class);
    return home.create();
  }
  
  private String getArchiveScheduleUID(HttpServletRequest request)
  {
    assertRunScheduleNow(request);
    return request.getParameter(ISchedulerConstant.ARCHIVE_SCHEDULE_UID);
  }
  
  private void assertRunScheduleNow(HttpServletRequest request)
  {
    String methodName = "assertRunScheduleNow";
    
    String archiveScheduleUID = request.getParameter(ISchedulerConstant.ARCHIVE_SCHEDULE_UID);
    _logger.logMessage(methodName, null, "Param retrieve -> archiveScheduleUID: "+archiveScheduleUID);
    
    if(archiveScheduleUID == null || "".equals(archiveScheduleUID))
    {
      throw new IllegalArgumentException("Archive schedule UID cannot be null or empty");
    }
  }
  
  private Hashtable getArchiveScheduleUpdateData(HttpServletRequest request) throws Exception
  {
    assertUpdateScheduleParam(request);
    
    String archiveScheduleUID = request.getParameter(ISchedulerConstant.ARCHIVE_SCHEDULE_UID);
    String frequency = request.getParameter(ISchedulerConstant.FREQUENCY);
    String runEveryTh = request.getParameter(ISchedulerConstant.ARCHIVE_EVERY_NTH);
    String effectiveStartDate = request.getParameter(ISchedulerConstant.EFFECTIVE_START_DATE);
    String effectiveStartTime = request.getParameter(ISchedulerConstant.EFFECTIVE_START_TIME);
    
    String isSchedulerEnabled = request.getParameter(ISchedulerConstant.IS_ENABLED);
    String[] customers = request.getParameterValues(ISchedulerConstant.CUSTOMER_LIST);
    String archiveRecordOlderThan = request.getParameter(ISchedulerConstant.ARCHIVE_OLDER_THAN);
    String userTimezone = request.getParameter(ISchedulerConstant.USER_TIME_ZONE);
    String isArchiveOrphanRecord = request.getParameter(ISchedulerConstant.IS_ARCHIVE_ORPHAN_RECORD);
    
    int convertRunEveryTh = Integer.parseInt(runEveryTh);
    
    Date convertEffStartDateTime = convertDateTimeInStrToDate(effectiveStartDate.trim() +" "+ effectiveStartTime.trim(), userTimezone);
    
    int scheduleEnable = Integer.parseInt(isSchedulerEnabled);
    boolean convertIsScheduledEnabled = scheduleEnable == 0 ? false : true;
    
    boolean convertIsArchiveOrphanRecord = false;
    if(isArchiveOrphanRecord != null)
    {
      int orphanEnable = Integer.parseInt(isArchiveOrphanRecord);
      convertIsArchiveOrphanRecord = orphanEnable == 0 ? false : true;
    }
    
    //TODO check the custoemr list
    //String convertCustomer = convertCustomerListInStr(customerList);
    ArrayList<String> customerList = new ArrayList<String>();
    if(customers != null && customers.length > 0)
    {
      for(String customer : customers)
      {
        customerList.add(customer);
      }
    }
    
    int convertArchiveRecordOlderThan = Integer.parseInt(archiveRecordOlderThan);
    
    Hashtable<String, Object> archiveSchedulerData = new Hashtable<String, Object>();
    archiveSchedulerData.put(ISchedulerConstant.FREQUENCY, frequency);
    archiveSchedulerData.put(ISchedulerConstant.ARCHIVE_EVERY_NTH, convertRunEveryTh);
    archiveSchedulerData.put(ISchedulerConstant.EFFECTIVE_START_DATE_TIME, convertEffStartDateTime);
    archiveSchedulerData.put(ISchedulerConstant.IS_ENABLED, convertIsScheduledEnabled);
    archiveSchedulerData.put(ISchedulerConstant.CUSTOMER_LIST, customerList);
    archiveSchedulerData.put(ISchedulerConstant.ARCHIVE_OLDER_THAN, convertArchiveRecordOlderThan);
    archiveSchedulerData.put(ISchedulerConstant.ARCHIVE_SCHEDULE_UID, archiveScheduleUID.trim());
    archiveSchedulerData.put(ISchedulerConstant.IS_ARCHIVE_ORPHAN_RECORD, convertIsArchiveOrphanRecord);
    
    if(userTimezone != null && ! "".equals(userTimezone))
    {
      archiveSchedulerData.put(ISchedulerConstant.USER_TIME_ZONE, TimeZone.getTimeZone(userTimezone));
    }
    
    return archiveSchedulerData;
  }
  
  private Hashtable getArchiveScheduleCreateData(HttpServletRequest request) throws Exception
  {
    assertCreateScheduleParam(request);
    
    String frequency = request.getParameter(ISchedulerConstant.FREQUENCY);
    String runEveryTh = request.getParameter(ISchedulerConstant.ARCHIVE_EVERY_NTH);
    String effectiveStartDate = request.getParameter(ISchedulerConstant.EFFECTIVE_START_DATE);
    String effectiveStartTime = request.getParameter(ISchedulerConstant.EFFECTIVE_START_TIME);
    
    String isSchedulerEnabled = request.getParameter(ISchedulerConstant.IS_ENABLED);
    String[] customers = request.getParameterValues(ISchedulerConstant.CUSTOMER_LIST);
    String archiveRecordOlderThan = request.getParameter(ISchedulerConstant.ARCHIVE_OLDER_THAN);
    String isArchiveOrphanRecord = request.getParameter(ISchedulerConstant.IS_ARCHIVE_ORPHAN_RECORD);
    
    String userTimezone = request.getParameter(ISchedulerConstant.USER_TIME_ZONE);
    _logger.logMessage("", null, "user timezone is "+userTimezone);
    
    
    int convertRunEveryTh = Integer.parseInt(runEveryTh);
    
    String effectiveStartDateTime = effectiveStartDate.trim() +" "+ effectiveStartTime.trim();
    Date convertEffStartDateTime = convertDateTimeInStrToDate(effectiveStartDateTime, userTimezone);
    
    _logger.logMessage("", null, "Convert date time "+convertEffStartDateTime);
    
    int scheduleEnable = Integer.parseInt(isSchedulerEnabled);
    boolean convertIsScheduledEnabled = scheduleEnable == 0 ? false : true;
    
    boolean convertIsArchiveOrphanRecord = false;
    if(isArchiveOrphanRecord != null)
    {
      int orphanEnable = Integer.parseInt(isArchiveOrphanRecord);
      convertIsArchiveOrphanRecord = orphanEnable == 0 ? false : true;
    }
    
    //TODO check the custoemr list
    //String convertCustomer = convertCustomerListInStr(customerList);
    ArrayList<String> customerList = new ArrayList<String>();
    if(customers != null && customers.length > 0)
    {
      for(String customer : customers)
      {
        customerList.add(customer);
      }
    }
    
    int convertArchiveRecordOlderThan = Integer.parseInt(archiveRecordOlderThan);
    
    Hashtable<String, Object> archiveSchedulerData = new Hashtable<String, Object>();
    archiveSchedulerData.put(ISchedulerConstant.FREQUENCY, frequency);
    archiveSchedulerData.put(ISchedulerConstant.ARCHIVE_EVERY_NTH, convertRunEveryTh);
    archiveSchedulerData.put(ISchedulerConstant.EFFECTIVE_START_DATE_TIME, convertEffStartDateTime);
    archiveSchedulerData.put(ISchedulerConstant.IS_ENABLED, convertIsScheduledEnabled);
    archiveSchedulerData.put(ISchedulerConstant.CUSTOMER_LIST, customerList);
    archiveSchedulerData.put(ISchedulerConstant.ARCHIVE_OLDER_THAN, convertArchiveRecordOlderThan);
    archiveSchedulerData.put(ISchedulerConstant.IS_ARCHIVE_ORPHAN_RECORD, convertIsArchiveOrphanRecord);
    
    if(userTimezone != null && ! "".equals(userTimezone))
    {
      archiveSchedulerData.put(ISchedulerConstant.USER_TIME_ZONE, TimeZone.getTimeZone(userTimezone));
    }
    
    return archiveSchedulerData;
  }
  
  private void assertCreateScheduleParam(HttpServletRequest request) throws Exception
  {
    String method = "assertCreateScheduleParam";
    
    String frequency = request.getParameter(ISchedulerConstant.FREQUENCY);
    String runEveryTh = request.getParameter(ISchedulerConstant.ARCHIVE_EVERY_NTH);
    String effectiveStartDate = request.getParameter(ISchedulerConstant.EFFECTIVE_START_DATE);
    String effectiveStartTime = request.getParameter(ISchedulerConstant.EFFECTIVE_START_TIME);
    
    String isSchedulerEnabled = request.getParameter(ISchedulerConstant.IS_ENABLED);
    String[] customerList = request.getParameterValues(ISchedulerConstant.CUSTOMER_LIST);
    String archiveRecordOlderThan = request.getParameter(ISchedulerConstant.ARCHIVE_OLDER_THAN);
    String isArchivedOrphanRecord = request.getParameter(ISchedulerConstant.IS_ARCHIVE_ORPHAN_RECORD);
    
    _logger.logMessage(method, null, "Param retrieve-> Frequency: "+frequency+" runEveryNTh: "+runEveryTh+" effectiveStartDate: "+effectiveStartDate+
                       " effectiveStartTime: "+effectiveStartTime+" isSchedulerEnabled: "+isSchedulerEnabled+
                       " customerList: "+customerList+" isArchiveOrphanRecord: "+isArchivedOrphanRecord);
    
    if(frequency == null || "".equals(frequency) || runEveryTh == null || "".equals(runEveryTh)
        || effectiveStartDate == null || "".equals(effectiveStartDate) || effectiveStartTime == null ||
        "".equals(effectiveStartTime) || isSchedulerEnabled == null || "".equals(isSchedulerEnabled) ||
        archiveRecordOlderThan == null || "".equals(archiveRecordOlderThan))
    {
      throw new IllegalArgumentException("Some of the compulsary field value is missing or the value is empty.");
    }
  }
  
  private void assertUpdateScheduleParam(HttpServletRequest request) throws Exception
  {
    String method = "assertUpdateScheduleParam";
    
    String frequency = request.getParameter(ISchedulerConstant.FREQUENCY);
    String runEveryTh = request.getParameter(ISchedulerConstant.ARCHIVE_EVERY_NTH);
    String effectiveStartDate = request.getParameter(ISchedulerConstant.EFFECTIVE_START_DATE);
    String effectiveStartTime = request.getParameter(ISchedulerConstant.EFFECTIVE_START_TIME);
    
    String isSchedulerEnabled = request.getParameter(ISchedulerConstant.IS_ENABLED);
    String[] customerList = request.getParameterValues(ISchedulerConstant.CUSTOMER_LIST);
    String archiveRecordOlderThan = request.getParameter(ISchedulerConstant.ARCHIVE_OLDER_THAN);
    String isArchivedOrphanRecord = request.getParameter(ISchedulerConstant.IS_ARCHIVE_ORPHAN_RECORD);
    
    String archiveSchedulerUID = request.getParameter(ISchedulerConstant.ARCHIVE_SCHEDULE_UID);
    
    _logger.logMessage(method, null, "Param retrieve-> Frequency: "+frequency+" runEveryNTh: "+runEveryTh+" effectiveStartDate: "+effectiveStartDate+
                       " effectiveStartTime: "+effectiveStartTime+" isSchedulerEnabled: "+isSchedulerEnabled+
                       " customerList: "+customerList+" archiveSchedulerUID "+archiveSchedulerUID+" isArchivedOrphanRecord: "+isArchivedOrphanRecord);
    
    if(frequency == null || "".equals(frequency) || runEveryTh == null || "".equals(runEveryTh)
        || effectiveStartDate == null || "".equals(effectiveStartDate) || effectiveStartTime == null ||
        "".equals(effectiveStartTime) || isSchedulerEnabled == null || "".equals(isSchedulerEnabled) ||
        archiveRecordOlderThan == null || "".equals(archiveRecordOlderThan)
        || archiveSchedulerUID == null || "".equals(archiveSchedulerUID))
    {
      throw new IllegalArgumentException("Some of the compulsary field value is missing or the value is empty.");
    }
  }
  
  private String convertCustomerListInStr(List<String> customerList)
  {
    SchedulerHandler handler = new SchedulerHandler();
    return handler.convertCustomerListToStr(customerList);
  }
  
  private Date convertDateTimeInStrToDate(String dateTimeInStr, String userTZ) throws Exception
  {
    String datePattern = getArchiveScheduleMgr().getArchiveSchedulerDatePattern();
    if(datePattern == null)
    {
      throw new NullPointerException("The Date pattern for parsing the date from UI is not found. Pls initialize in DB");
    }
    
    TimeZone tz = null;
    if(userTZ == null)
    {
      tz = TimeZone.getDefault();
    }
    else
    {
      tz = TimeZone.getTimeZone(userTZ);
    } 
    SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
    dateFormatter.setTimeZone(tz);
    
    Date dateTime = dateFormatter.parse(dateTimeInStr);
    return dateTime;
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
  public static void main(String[] args) throws Exception
  {
    Date d1 = Calendar.getInstance().getTime();
    String timezone = "PDT";
    TimeZone tz = TimeZone.getTimeZone(timezone);
    System.out.println(tz);
    
    
    String date = "2007-03-27 00:20:00 +8";
    String format = "yyyy-MM-dd HH:mm:ss Z";
    
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    
    formatter.setTimeZone(tz);
    //Date d = formatter.parse(date);
    
    System.out.println("fornmat "+formatter.format(d1));
    
    formatter = new SimpleDateFormat(format);
    
    //System.out.println("formatted "+d+ " "+formatter.parse(date));
    
    Calendar c = Calendar.getInstance();
    c.setTimeZone(TimeZone.getTimeZone(timezone));
    
  }
}
