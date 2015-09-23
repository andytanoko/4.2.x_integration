/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveSchedulerManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 13, 2007    Tam Wei Xiang       Created
 * Apr 02, 2007    Tam Wei Xiang       Passed in the user timezone.
 * Jun 01, 2007    Tam Wei Xiang       Support archive by customer
 * Jan 15, 2008    Tam Wei Xiang       Review the logging category
 */
package com.gridnode.gtas.audit.archive.scheduler.facade.ejb;

import java.rmi.RemoteException;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.ejb.SessionContext;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.audit.archive.ArchivalInitiator;
import com.gridnode.gtas.audit.archive.ArchiveInitiatorFactory;
import com.gridnode.gtas.audit.archive.IArchivalInitiator;
import com.gridnode.gtas.audit.archive.IArchiveConstant;
import com.gridnode.gtas.audit.archive.scheduler.ArchiveScheduleTask;
import com.gridnode.gtas.audit.archive.scheduler.ISchedulerConstant;
import com.gridnode.gtas.audit.archive.scheduler.SchedulerHandler;
import com.gridnode.gtas.audit.archive.scheduler.dao.ArchiveScheduleDAO;
import com.gridnode.gtas.audit.archive.scheduler.exception.ArchiveScheduleException;
import com.gridnode.gtas.audit.archive.scheduler.model.ArchiveScheduler;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerHome;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerObj;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.tracking.helpers.IISATProperty;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveSchedulerManagerBean implements SessionBean
{
  private SessionContext _ctxt = null;
  private static final String CLASS_NAME = "ArchiveSchedulerManagerBean";
  private Logger _logger = null;
  
  /**
   * Add the Archive Scheduler. The next runtime, archive start/end date range will be determined also.
   * @param frequency
   * @param effectiveStartDateTime
   * @param archiveRecordOlderThan
   * @param archiveEveryNth
   * @param customer
   * @param isEnabled
   * @throws ArchiveScheduleException
   */
  public void addArchiveSchedule(Hashtable archiveScheduleData) throws ArchiveScheduleException
  {
    String method = "addArchiveSchedule";
    assertParam(archiveScheduleData);
    
    _logger.logMessage(method, null, "Add Archive scheduler with value "+archiveScheduleData);
    
    String frequency = (String)archiveScheduleData.get(ISchedulerConstant.FREQUENCY);
    Date effectiveStartDateTime = (Date)archiveScheduleData.get(ISchedulerConstant.EFFECTIVE_START_DATE_TIME);
    Integer archiveRecordOlderThan = (Integer)archiveScheduleData.get(ISchedulerConstant.ARCHIVE_OLDER_THAN);
    Integer archiveEveryNth = (Integer)archiveScheduleData.get(ISchedulerConstant.ARCHIVE_EVERY_NTH);
    List customer = (List)archiveScheduleData.get(ISchedulerConstant.CUSTOMER_LIST);
    Boolean isEnabled = (Boolean)archiveScheduleData.get(ISchedulerConstant.IS_ENABLED);
    TimeZone userTimezone = (TimeZone)archiveScheduleData.get(ISchedulerConstant.USER_TIME_ZONE);
    Boolean isArchiveOrphanRecord = (Boolean)archiveScheduleData.get(ISchedulerConstant.IS_ARCHIVE_ORPHAN_RECORD);
    
    isArchiveOrphanRecord = isArchiveOrphanRecord == null? false : isArchiveOrphanRecord;
    
    SchedulerHandler handler = new SchedulerHandler();
    handler.addScheduleTask(frequency, effectiveStartDateTime, archiveRecordOlderThan, archiveEveryNth,customer, isEnabled, userTimezone, isArchiveOrphanRecord);
    
    _logger.logMessage(method, null, "Archive scheduler added");
  }
  
  /**
   * Load all the outstanding archive schedule task given the specified range.
   * @param range
   */
  public void loadOutstandingTask(Date startDateRange, Date endDateRange)
  {
    String methodName = "loadOutstandingTask";
    _logger.debugMessage(methodName, null, "Loading Outstanding Task. StartDate range :"+startDateRange+" EndDate Range: "+endDateRange);
    
    ArchiveScheduleDAO dao = new ArchiveScheduleDAO();
    Collection<ArchiveScheduler> archiveSchedulerList = dao.getArchiveScheduleTask(startDateRange, endDateRange, true);
    _logger.debugMessage(methodName, null, "ArchiveScheduler record loaded size is "+archiveSchedulerList.size());
    
    if(archiveSchedulerList != null && archiveSchedulerList.size() > 0)
    {
      Iterator<ArchiveScheduler> ite = archiveSchedulerList.iterator();
      Timer timer = new Timer();
      while(ite.hasNext())
      {
        ArchiveScheduler scheduler = ite.next();
        ArchiveScheduleTask scheduleTask = getArchiveScheduleTask(scheduler);
        
        if(scheduler.isActive())
        {
          timer.schedule(scheduleTask, scheduler.getNextRunTime());
          _logger.logMessage(methodName, null, "Adding archive schedule task "+scheduleTask+" for running at "+scheduler.getNextRunTime());
        }
        else
        {
          _logger.logMessage(methodName, null, "Archive schedule task "+scheduleTask+" is currently inactive ! ");
        }
      }
    }
    
    _logger.debugMessage(methodName, null, "End loaded outstanding Task");
  }
  
  private ArchiveScheduleTask getArchiveScheduleTask(ArchiveScheduler scheduler)
  {
    SchedulerHandler handler = new SchedulerHandler();
    //Date[] dateRange = handler.computeArchiveRange(scheduler.getNextRunTime(), scheduler.getFrequency(), scheduler.getArchiveRecordOlderThanNth(), null);
    
    Date fromStartDate = scheduler.getArchiveStartDateRange();
    Date toStartDate = scheduler.getArchiveEndDateRange();
    String customerList = scheduler.getCustomerList();
    String archiveDesc = getArchiveDescription();
    String archiveName = getArchiveName();
    String schedulerUID = scheduler.getUID();
    boolean isArchiveOrphanRecord = scheduler.isArchiveOrphanRecord();
    
    long fromStartDateInLong = fromStartDate.getTime();
    long toStartDateInLong = toStartDate.getTime();
    
    return new ArchiveScheduleTask(fromStartDateInLong, toStartDateInLong, customerList, schedulerUID, archiveDesc, archiveName, isArchiveOrphanRecord);
  }
  
  /**
   * Update the archive Invoke status for the ArchiveScheduler identified by the archiveSchedulerUID.
   * As long as the invocation of the archive request is successfully, the next run time, and the
   * next archive date range will be determined.
   * 
   * @param archiveInvokeSuccess the status for invoking the archival task  
   * @param archiveSchedulerUID the UID of the archive scheduler
   */
  public void updateArchiveSchedulerStatus(boolean archiveInvokeSuccess, String archiveSchedulerUID) throws ArchiveScheduleException
  {
    String method = "updateArchiveSchedulerStatus";
    _logger.logMessage(method, null, "Start update the archive scheduler status with UID "+archiveSchedulerUID+" to "+archiveInvokeSuccess);
    
    ArchiveScheduler scheduler = getArchiveScheduler(archiveSchedulerUID);
    updateArchiveSchedulerStatus(archiveInvokeSuccess, scheduler);
    
    _logger.logMessage(method, null,"Updated archive scheduler is "+scheduler);
  }
  
  /**
   * Update the archive Invoke status for the ArchiveScheduler identified by the archiveSchedulerUID.
   * As long as the invocation of the archive request is successfully, the next run time, and the
   * next archive date range will be determined.
   * 
   * @param archiveInvokeSuccess the status for invoking the archival task  
   * @param scheduler the archive scheduler
   */
  private void updateArchiveSchedulerStatus(boolean archiveInvokeSuccess, ArchiveScheduler scheduler) throws ArchiveScheduleException
  {
    String method = "updateArchiveSchedulerStatus";
    
    //if success invoke , update the next run time, last Runtime, lastArchiveOlderThan
    if(archiveInvokeSuccess)
    {
      SchedulerHandler handler = new SchedulerHandler();
      Date nextRuntime = scheduler.getNextRunTime();
      scheduler.setLastRunTime(nextRuntime);
      
      
      handler.computeNextRuntime(scheduler, nextRuntime, false);
      scheduler.setSuccessInvoke(true);
      
      String timezoneID = scheduler.getTimezoneID();
      TimeZone tz = null;
      if(timezoneID != null && ! "".equals(timezoneID))
      {
        tz = TimeZone.getTimeZone(timezoneID);
      }
        
      Date[] archiveDateRange = handler.computeArchiveRange(scheduler.getNextRunTime(), scheduler.getFrequency(), scheduler.getArchiveRecordOlderThanNth(), tz);
      scheduler.setArchiveStartDateRange(archiveDateRange[0]);
      scheduler.setArchiveEndDateRange(archiveDateRange[1]);
    }
    else
    {
//    false, next run time remain, last runtime same as next runtime. User allow to trigger the schedule again
      scheduler.setLastRunTime(scheduler.getNextRunTime());
      scheduler.setSuccessInvoke(false);
    }
    
    ArchiveScheduleDAO scheduleDAO = new ArchiveScheduleDAO();
    scheduleDAO.update(scheduler);
    
    _logger.logMessage(method, null, "After update ArchiveScheduler "+scheduler);
  }
  
  
  /**
   * Update the ArchiveScheduler record as identified by the schedulerUID
   * @param schedulerUID the PK of the ArchiveScheduler
   * @param archiveScheduleData the data need to be updated to ArchiveScheduler
   * @throws ArchiveScheduleException if we have problem updating the ArchiveScheduler record
   */
  public void updateArchiveScheduler(String schedulerUID, Hashtable archiveScheduleData) throws ArchiveScheduleException 
  {
    String method = "updateArchiveScheduler";
    assertParam(archiveScheduleData);
    _logger.logMessage(method, null, "Update the archive scheduler for UID: "+schedulerUID+" given data: "+archiveScheduleData);
    
    String frequency = (String)archiveScheduleData.get(ISchedulerConstant.FREQUENCY);
    Date effectiveStartDateTime = (Date)archiveScheduleData.get(ISchedulerConstant.EFFECTIVE_START_DATE_TIME);
    Integer archiveRecordOlderThan = (Integer)archiveScheduleData.get(ISchedulerConstant.ARCHIVE_OLDER_THAN);
    Integer archiveEveryNth = (Integer)archiveScheduleData.get(ISchedulerConstant.ARCHIVE_EVERY_NTH);
    List customer = (List)archiveScheduleData.get(ISchedulerConstant.CUSTOMER_LIST);
    Boolean isEnabled = (Boolean)archiveScheduleData.get(ISchedulerConstant.IS_ENABLED);
    TimeZone tz = (TimeZone)archiveScheduleData.get(ISchedulerConstant.USER_TIME_ZONE);
    Boolean isArchiveOrphanRecord = (Boolean)archiveScheduleData.get(ISchedulerConstant.IS_ARCHIVE_ORPHAN_RECORD);
    
    isArchiveOrphanRecord = isArchiveOrphanRecord == null? false : isArchiveOrphanRecord;
    
    SchedulerHandler handler = new SchedulerHandler();
    
    handler.updateScheduleTask(frequency, effectiveStartDateTime, 
                               archiveRecordOlderThan, archiveEveryNth, customer, isEnabled, schedulerUID, tz, isArchiveOrphanRecord);
    
    _logger.logMessage(method, null, "Update archive scheduler for UID: "+schedulerUID+" completed.");
  }
  
  /**
   * Invoke the archive scheduler directly without waiting to its next runtime. The archive date range 
   * criteria will be based on the date value of archiveStartDate and archiveEndDate.
   * 
   * This allow the user to force the archive start immediately.
   * 
   * This will be useful if the scheduler fail to invoke the archive task (fail to deliver the request to JMS)
   * @param archiveUID
   * @throws ArchiveScheduleException
   */
  public void runArchiveSchedulerNow(String archiveUID) throws ArchiveScheduleException
  {
    String methodName = "runArchiveSchedulerNow";
    
    _logger.logMessage(methodName, null, "Run archive schedule now for scheduler with UID "+archiveUID);
    
    //no update the runtime
    ArchiveScheduleDAO scheduleDAO = new ArchiveScheduleDAO();
    ArchiveScheduler scheduler = (ArchiveScheduler)scheduleDAO.get(ArchiveScheduler.class, archiveUID);
    if(scheduler == null)
    {
      throw new ArchiveScheduleException("Error in locating ArchiveScheduler entity given "+archiveUID);
    }
    
    if(! scheduler.isActive())
    {
      _logger.logMessage(methodName, null, "Archive Scheduler with UID "+archiveUID+" currently is set to disable. It will not be run.");
      return;
    }
    Date archiveFromStartDate = scheduler.getArchiveStartDateRange();
    Date archiveToStartDate = scheduler.getArchiveEndDateRange();

    Hashtable archiveCriteria = getArchiveCriteria(scheduler, archiveFromStartDate, archiveToStartDate);
    
    try
    {
      //sendToQueue(archiveCriteria);
      IArchivalInitiator initiator = ArchiveInitiatorFactory.getInstance().getArchivalInitiator();
      initiator.initArchive(archiveCriteria);
    }
    catch(Exception ex)
    {
      _logger.logWarn(methodName, null, "Error in delivering the Archive Request.", ex);
      throw new ArchiveScheduleException("Error in delivering the Archive Request. Archive scheduler uid is "+archiveUID, ex);
    }
      
    //    update runtime if current time > next run time
    Date nextRunTime = scheduler.getNextRunTime();
    Date currentDate = new Date();
    
    //if(currentDate.getTime() >= nextRunTime.getTime())
    //{
      updateArchiveSchedulerStatus(true, scheduler);
    //}
    _logger.logMessage(methodName, null, "End run archive schedule now for scheduler with UID "+archiveUID);
  }
  
  /**
   * Delete the ArchiveScheduler record given the archiveSchedulerUID
   * @param archiveSchedulerUID the PK of the ArchiveScheduler record
   * @throws ArchiveScheduleException throw if such a record is not exist in DB
   */
  public void deleteArchiveSchedulerRecord(String archiveSchedulerUID) throws ArchiveScheduleException
  {
    String method = "deleteArchiveSchedulerRecord";
    _logger.logMessage(method, null, "Start Delete archive scheduler record with UID: "+archiveSchedulerUID);
    
    ArchiveScheduleDAO dao = new ArchiveScheduleDAO();
    ArchiveScheduler scheduler = (ArchiveScheduler)dao.get(ArchiveScheduler.class, archiveSchedulerUID);
    if(scheduler == null)
    {
      throw new ArchiveScheduleException("The ArchiveScheduler record cannot be found given UID "+archiveSchedulerUID);
    }
    else
    {
      dao.delete(scheduler);
    }
    
    _logger.logMessage(method, null, "Deleted archive scheduler record with UID: "+archiveSchedulerUID);
  }
  
  public String getArchiveSchedulerResponseURL()
  {
    ConfigurationStore configStore = ConfigurationStore.getInstance();
    return configStore.getProperty(IISATProperty.CATEGORY, IISATProperty.ARCHIVE_STATUS_RESPONSE_URL, null);
  }
  
  public String getArchiveSchedulerDatePattern()
  {
    ConfigurationStore configStore = ConfigurationStore.getInstance();
    return configStore.getProperty(IISATProperty.CATEGORY, IISATProperty.ARCHIVE_SCHEDULER_DATE_PATTERN, null);
  }
  
  /**
   * Get the time (in seconds) to determine whether to set the next run time as the client spec.
   * For example, if we specify the nextRuntime is today 10:40 am, the current date time is 10:39:55,
   * if we set the next run time as 10:40 am (see ScheduleHandler.computeNextRuntime()), the MBean may miss the loading the scheduler to run, so
   * we set the time interval to determine whether to use the  time (10:40 am ) as the next run time. 
   * @return
   */
  public int getTimeIntervalForNewAddScheduleTask() throws ArchiveScheduleException
  {
    ConfigurationStore configStore = ConfigurationStore.getInstance();
    String allowableLoadTime = configStore.getProperty(IISATProperty.CATEGORY, "", null);
    if(allowableLoadTime == null)
    {
      throw new ArchiveScheduleException("The time interval for adding the schedule task is not set in DB, pls initialize it.");
    }
    else
    {
      return Integer.parseInt(allowableLoadTime);
    }
  }
  
  private Hashtable getArchiveCriteria(ArchiveScheduler scheduler, Date fromStartDate, Date toStartDate)
  {
    Hashtable<String, Object> archiveCriteria = new Hashtable<String, Object>();
    archiveCriteria.put(IArchiveConstant.ARCHIVE_ACTIVITY, IArchiveConstant.ARCHIVE_ACT_ARCHIVE);
    archiveCriteria.put(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME, fromStartDate.getTime());
    archiveCriteria.put(IArchiveConstant.CRITERIA_TO_START_DATE_TIME, toStartDate.getTime());
    archiveCriteria.put(IArchiveConstant.ARCHIVE_NAME, getArchiveName());
    archiveCriteria.put(IArchiveConstant.ARCHIVE_DESCRIPTION, getArchiveDescription());
    archiveCriteria.put(IArchiveConstant.GROUP_INFO, getGroupInfo(scheduler.getCustomerList()));
    archiveCriteria.put(IArchiveConstant.ARCHIVE_ORPHAN_RECORD, scheduler.isArchiveOrphanRecord());
    return archiveCriteria;
  }
  
  private String getGroupInfo(String groupList)
  {
    String groups = "";
    StringTokenizer st = new StringTokenizer(groupList, "@@;");
    while(st.hasMoreTokens())
    {
      groups += st.nextToken() + ";";
    }
    return groups;
  }
  
  private String getArchiveName()
  {
    String method = "getArchiveName";
    try
    {
      IAuditPropertiesManagerObj propMgr = getPropertiesMgr();
      return propMgr.getAuditProperties(IISATProperty.CATEGORY, IISATProperty.ARCHIVE_SCHEDULER_NAME_PREFIX, "");
    }
    catch(Exception ex)
    {
      _logger.logWarn(method, null, "Error in getting the name of Archive from the properties", ex);
    }
    
    return "";
  }
  
  private String getArchiveDescription()
  {
    String method = "getArchiveDescription";
    try
    {
      IAuditPropertiesManagerObj propMgr = getPropertiesMgr();
      return propMgr.getAuditProperties(IISATProperty.CATEGORY, IISATProperty.ARCHIVE_SCHEDULER_DESCRIPTION, "") + new Date();
    }
    catch(Exception ex)
    {
      _logger.logWarn(method, null, "Error in getting the Archive Description from the properties", ex);
    }
    
    return "";
  }
  
  private IAuditPropertiesManagerObj getPropertiesMgr() throws Exception
  {
    JndiFinder jndiFinder = new JndiFinder(null);
    IAuditPropertiesManagerHome mgrHome = (IAuditPropertiesManagerHome)
                                              jndiFinder.lookup(IAuditPropertiesManagerHome.class.getName(), IAuditPropertiesManagerHome.class);
    return mgrHome.create();
  }
  
  private ArchiveScheduler getArchiveScheduler(String UID) throws ArchiveScheduleException
  {
    ArchiveScheduleDAO dao = new ArchiveScheduleDAO();
    ArchiveScheduler scheduler = (ArchiveScheduler)dao.get(ArchiveScheduler.class, UID);
    if(scheduler == null)
    {
      throw new ArchiveScheduleException("No ArchiveScheduler record can't be located given UID "+UID);    
    }
    return scheduler;
  }
  
  private void assertParam(Hashtable archiveScheduleData)
  {
    String method = "assertParam";
    
    String frequency = (String)archiveScheduleData.get(ISchedulerConstant.FREQUENCY);
    Date effectiveStartDateTime = (Date)archiveScheduleData.get(ISchedulerConstant.EFFECTIVE_START_DATE_TIME);
    Integer archiveRecordOlderThan = (Integer)archiveScheduleData.get(ISchedulerConstant.ARCHIVE_OLDER_THAN);
    Integer archiveEveryNth = (Integer)archiveScheduleData.get(ISchedulerConstant.ARCHIVE_EVERY_NTH);
    List customer = (List)archiveScheduleData.get(ISchedulerConstant.CUSTOMER_LIST);
    Boolean isEnabled = (Boolean)archiveScheduleData.get(ISchedulerConstant.IS_ENABLED);
    
    if(frequency == null || effectiveStartDateTime == null || archiveRecordOlderThan == null || archiveEveryNth == null
       || isEnabled == null || "".equals(frequency))
    {
      _logger.logMessage(method, null, "Some of the param value is missing . Given archive schedule data is "+archiveScheduleData);
      throw new IllegalArgumentException("Some of the necessary value are missing");
    }
  }
  
  public void ejbCreate()
  {
    _logger = getLogger();
  }
  
  public void ejbActivate() throws EJBException, RemoteException
  {
  }

  public void ejbPassivate() throws EJBException, RemoteException
  {
  }

  public void ejbRemove() throws EJBException, RemoteException
  {
  }
  
  public void setSessionContext(SessionContext ctxt) throws EJBException,
                                                    RemoteException
  {
    _ctxt = ctxt;
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }

}


