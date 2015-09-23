package com.gridnode.gtas.audit.archive.scheduler;

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
* Apr 02, 2007    Tam Wei Xiang       Compute the archive date range based on 
*                                     user timezone.
*/

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.gridnode.gtas.audit.archive.scheduler.dao.ArchiveScheduleDAO;
import com.gridnode.gtas.audit.archive.scheduler.exception.ArchiveScheduleException;
import com.gridnode.gtas.audit.archive.scheduler.model.ArchiveScheduler;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerHome;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerObj;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.tracking.helpers.IISATProperty;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

public class SchedulerHandler 
{
  private Logger _logger = null;
  private static final String CLASS_NAME = "SchedulerHandler";
  
	public SchedulerHandler()
	{
	  _logger = getLogger();
	}
	
	public void loadOutStandingTask(Date fromDate, Date toDate)
	{
		
	}
	
  public void addScheduleTask(String frequency, Date effectiveStartDate, int archiveRecordOlderThan, int archiveEveryNth,
                              List<String> customer, boolean isEnabled, TimeZone clientTimezone,
                              boolean isArchiveOrphanRecord) throws ArchiveScheduleException
  {
    if(! ISchedulerConstant.FREQUENCY_DAILY.equals(frequency) && ! ISchedulerConstant.FREQUENCY_WEEKLY.equals(frequency) &&
        ! ISchedulerConstant.FREQUENCY_MONTHLY.equals(frequency))
    {
      throw new IllegalArgumentException("["+CLASS_NAME+".addScheduleTask] The frequency "+frequency+" is currently not supported");
    }
    
    ArchiveScheduler scheduler = new ArchiveScheduler();
    scheduler.setEffectiveStartDate(effectiveStartDate);
    scheduler.setFrequency(frequency);
    scheduler.setArchiveRecordOlderThanNth(archiveRecordOlderThan);
    scheduler.setArchiveEveryNth(archiveEveryNth);
    scheduler.setCustomerList(convertCustomerListToStr(customer));
    scheduler.setActive(isEnabled);
    scheduler.setArchiveOrphanRecord(isArchiveOrphanRecord);
    
    System.out.println("compute next run time");
    computeNextRuntime(scheduler, scheduler.getEffectiveStartDate(), false);
    System.out.println(scheduler);
    
    //update the end date range - any record older or equal to the end date range will be archived
    Date[] archiveDateRange = computeArchiveRange(scheduler.getNextRunTime(), scheduler.getFrequency(), scheduler.getArchiveRecordOlderThanNth(), clientTimezone);
    scheduler.setArchiveStartDateRange(archiveDateRange[0]);
    scheduler.setArchiveEndDateRange(archiveDateRange[1]);
    
    if(clientTimezone != null)
    {
      scheduler.setTimezoneID(clientTimezone.getID());
    }
    
    System.out.println("Archive date range "+archiveDateRange[0]+" to date range "+archiveDateRange[1]);
    
    ArchiveScheduleDAO dao = new ArchiveScheduleDAO();
    dao.create(scheduler);
  }
  
  public void updateScheduleTask(String frequency, Date effectiveStartDateTime, int archiveRecordOlderThan, int archiveEveryNth,
                                 List<String> customer, boolean isEnabledNow, String archiveScheduleUID, TimeZone clientTZ,
                                 boolean isArchiveOrphanRecord) throws ArchiveScheduleException
  {
    String method = "updateScheduleTask";
    
    ArchiveScheduleDAO dao = new ArchiveScheduleDAO();
    ArchiveScheduler scheduler = (ArchiveScheduler)dao.get(ArchiveScheduler.class, archiveScheduleUID);
    if(scheduler == null)
    {
      throw new ArchiveScheduleException("["+CLASS_NAME+"."+method+"] No ArchiveSchedule record can be found given UID "+archiveScheduleUID+". Update failed");
    }
    else
    {
      _logger.logMessage(method, null, "Before performing update "+scheduler);
      boolean isEnablePreviously = scheduler.isActive();
      
      scheduler.setEffectiveStartDate(effectiveStartDateTime);
      scheduler.setFrequency(frequency);
      scheduler.setArchiveRecordOlderThanNth(archiveRecordOlderThan);
      scheduler.setArchiveEveryNth(archiveEveryNth);
      scheduler.setActive(isEnabledNow);
      scheduler.setCustomerList(convertCustomerListToStr(customer));
      scheduler.setArchiveOrphanRecord(isArchiveOrphanRecord);
      
      Date previousNextRuntime = scheduler.getNextRunTime();
      if(isEnabledNow && !isEnablePreviously && previousNextRuntime.getTime() < new Date().getTime())
      {
        computeNextRuntime(scheduler, effectiveStartDateTime, true);
      }
      else
      {
        computeNextRuntime(scheduler, effectiveStartDateTime, false);
      }
      Date[] archiveDateRange = computeArchiveRange(scheduler.getNextRunTime(), frequency, archiveRecordOlderThan, clientTZ);
      
      scheduler.setArchiveStartDateRange(archiveDateRange[0]);
      scheduler.setArchiveEndDateRange(archiveDateRange[1]);
      
      if(clientTZ != null)
      {
        scheduler.setTimezoneID(clientTZ.getID());
      }
      
      _logger.logMessage(method, null, "After update archive scheduler "+scheduler);
      
      dao.update(scheduler);
    }
  }
  
  /**
   * Given the nextRuntime, the frequency, and archiveBeforeNth, we will determine the date range for the archive
   * @param nextRuntime the time we will start the scheduler
   * @param frequency the scheduler frequency.
   * @param archiveRecordOlderThanNth the value is depend on the frequency. If frequency is ISchedulerConstant.FREQUENCY_WEEKLY,
   *                         the archiveBeforeNth mean archiveBeforeNth week
   * @param clientT if null, the computation of the archive date range will be based on 
   *                       current sys timezone. else, it will based on the specified time zone.                        
   * @return the from and to date range
   */
  public Date[] computeArchiveRange(Date nextRuntime, String frequency, int archiveRecordOlderThanNth, TimeZone clientTZ)
  {
    Date archiveStartDateTime = null;
    Date archiveEndDateTime = null;
    
    if(ISchedulerConstant.FREQUENCY_DAILY.equals(frequency))
    {
      return computeDailyArchiveDateRange(nextRuntime, archiveRecordOlderThanNth, clientTZ);
    }
    else if(ISchedulerConstant.FREQUENCY_WEEKLY.equals(frequency))
    {
      return computeWeeklyArchiveDateRange(nextRuntime, archiveRecordOlderThanNth, clientTZ);
    }
    else if(ISchedulerConstant.FREQUENCY_MONTHLY.equals(frequency))
    {
      return computeMonthlyArchiveDateRange(nextRuntime, archiveRecordOlderThanNth, clientTZ);
    }
    else
    {
      throw new IllegalArgumentException("[SchedulerHandler.computeArchiveRange] The frequency "+frequency+" is currently not supported");
    }
  }
  
  /**
   * Compute the date range for frequency Daily given the nextRuntime. We will infer the start/to date
   * range based on nextRuntime. 
   * 
   * The day will be included the current day specified by the nextRuntime
   * 
   * @param nextRuntime the time we will start the scheduler
   * @param archiveRecorldOlderThanNth archive before Nth day. For example, next run time is 17/3,and archiveBeforeNthDay is 3,
   *                            so we will archive the record on the day 14/3
   * @param clientTz if null, the computation of the archive date range will be based on 
   *                       current sys timezone. else, it will based on the specified time zone.                           
   * @return
   */
  private Date[] computeDailyArchiveDateRange(Date nextRuntime,  int archiveRecorldOlderThanNth, TimeZone clientTz)
  {
    Calendar c = Calendar.getInstance();
    
    c.setTimeInMillis(nextRuntime.getTime());
    if(clientTz != null)
    {
      c.setTimeZone(clientTz);
    }
    int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
    
    Date fromStartDateTime = getArchiveStartDate(clientTz);
    
    c.set(Calendar.DAY_OF_MONTH, dayOfMonth - archiveRecorldOlderThanNth); //Nth day before
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    c.set(Calendar.MILLISECOND, 999);
    Date toStartDateTime = c.getTime();
    
    return new Date[]{fromStartDateTime, toStartDateTime};
  }
  
  /**
   * Compute the date range for frequency Weekly given the nextRuntime. We will infer the start/to date
   * range based on nextRuntime.
   * 
   * The week will include the current week specified as the nextRuntime
   * @param nextRuntime the time we will start the scheduler
   * @param archiveRecordOlderThanNth archive before the Nth week given the week of the nextRuntime
   * @param clientTz if null, the computation of the archive date range will be based on 
   *                       current sys timezone. else, it will based on the specified time zone. 
   * @return
   */
  private Date[] computeWeeklyArchiveDateRange(Date nextRuntime, int archiveRecordOlderThanNth, TimeZone clientTz)
  {
    Calendar c = Calendar.getInstance();
    
    c.setTimeInMillis(nextRuntime.getTime());
    if(clientTz != null)
    {
      c.setTimeZone(clientTz);
    }
    int weekOfTheMonth = c.get(Calendar.WEEK_OF_MONTH);
    
    c.set(Calendar.WEEK_OF_MONTH, weekOfTheMonth - archiveRecordOlderThanNth);
    Date fromStartDateTime = getArchiveStartDate(clientTz);
    
    c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    c.set(Calendar.MILLISECOND, 999);
    Date toStartDateTime = c.getTime();
    
    return new Date[]{fromStartDateTime, toStartDateTime};
  }
  
  /**
   * Compute the date range for frequency Monthly given the nextRuntime. We will infer the start/to date
   * range based on nextRuntime.
   * 
   * The month will be incldued the current month as specified by the nextRuntime
   * @param nextRuntime the time we will start the scheduler
   * @param archiveOlderThanNthMonth archive before the Nth month given the month of the nextRuntime
   * @param clientTz if null, the computation of the archive date range will be based on 
   *                       current sys timezone. else, it will based on the specified time zone. 
   * @return
   */
  private Date[] computeMonthlyArchiveDateRange(Date nextRuntime, int archiveOlderThanNthMonth, TimeZone clientTz)
  {
    Calendar c = Calendar.getInstance();
    
    c.setTimeInMillis(nextRuntime.getTime());
    if(clientTz != null)
    {
      c.setTimeZone(clientTz);
    }
    
    int month = c.get(Calendar.MONTH);
    
    c.set(Calendar.MONTH, month - archiveOlderThanNthMonth-1); //month will start at the first day of the month of the nextRuntime. so 1 month older
                                                               //should mean the month of the nextRuntime - 2
    Date fromStartDateTime = getArchiveStartDate(clientTz);
    
    //Get the last day of the month
    c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
    c.set(Calendar.DAY_OF_MONTH, 0);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    c.set(Calendar.MILLISECOND, 999);
    Date toStartDateTime = c.getTime();
    
    return new Date[]{fromStartDateTime, toStartDateTime};
  }
  
  private Date getArchiveStartDate(TimeZone clientTz)
  {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(0);
    if(clientTz != null)
    {
      c.setTimeZone(clientTz);
    }
    return c.getTime();
  }
  
  /**
   * Compute the next run time of the scheduler based on the given effectiveStartDate and scheduler.getArchiveEveryNth()
   * @param scheduler the archive scheduler
   * @param effectiveStartDate
   * @param isEnabledSmartlyAdjust There will be no effect if the effectiveStartDate <= current time, the method will auto adjust to the
   *                               time that is 'just' pass the current time.
   *                               
   *                               if true, and the effectiveStartDate > current time, the next run time will be the effectiveStartDate.
   *                               
   *                               if false, and the effectiveStartDate > current time, it will return effectiveStartDate + scheduler.getArchiveEveryTh()
   *                               
   * @throws ArchiveScheduleException
   */
	public void computeNextRuntime(ArchiveScheduler scheduler, Date effectiveStartDate, boolean isEnabledSmartlyAdjust) throws ArchiveScheduleException
	{
		String frequency = scheduler.getFrequency();
		Date nextRunTime = null;
    
		if(ISchedulerConstant.FREQUENCY_DAILY.equals(frequency))
		{
      autoAdjustToNextRuntimeDaily(effectiveStartDate , scheduler.getArchiveEveryNth(), scheduler, isEnabledSmartlyAdjust);
		}
    else if(ISchedulerConstant.FREQUENCY_WEEKLY.equals(frequency))
    {
      autoAdjustToNextRuntimeWeekly(effectiveStartDate ,scheduler.getArchiveEveryNth(), scheduler, isEnabledSmartlyAdjust);
    }
    else if(ISchedulerConstant.FREQUENCY_MONTHLY.equals(frequency))
    {
      autoAdjustToNextRuntimeMonthly(effectiveStartDate, scheduler.getArchiveEveryNth(), scheduler, isEnabledSmartlyAdjust);
    }
	}
	
  /**
   * Auto adjust the next run time to the first day of next month.
   * @param effectiveDateTime
   * @param intervalMonth
   * @param scheduler
   * @throws Exception
   */
  private void autoAdjustToNextRuntimeMonthly(Date nextRuntime, int archiveEveryNthMonth, 
                                              ArchiveScheduler scheduler, boolean isEnabledSmartlyAdjust) throws ArchiveScheduleException
  {
    String methodName = "autoAdjustToNextRuntimeMonthly";
    Calendar currentTime = Calendar.getInstance();
    if(isEnabledSmartlyAdjust && validateNextRunTime(nextRuntime, currentTime.getTime()))
    {
      scheduler.setNextRunTime(nextRuntime);
      return;
    }
    else
    {
      Calendar nextRunC = Calendar.getInstance();
      nextRunC.setTime(nextRuntime);
    
      nextRunC.set(Calendar.MONTH, nextRunC.get(Calendar.MONTH) + archiveEveryNthMonth);
      nextRunC.set(Calendar.DAY_OF_MONTH, 1);                         //Now, we will set to the first day of the month
      int startOnNthDayOfMonth = nextRunC.get(Calendar.DAY_OF_MONTH); //In future, can allow user specify onm which day they want to run the scheduler
      
      autoAdjustToNextRuntimeMonthly(nextRunC.getTime(), archiveEveryNthMonth, scheduler,true);
    }
  }
  /**
   * Adjust the next run time if the given the frequency.
   * @param startTime
   * @param intervalWeek
   * @param startOnNthDayOfWeek
   * @param scheduler
   * @throws Exception
   */
  private void autoAdjustToNextRuntimeWeekly(Date nextRuntime, int archiveEveryNthWeek,
                                             ArchiveScheduler scheduler, boolean isEnabledSmartlyAdjust) throws ArchiveScheduleException
  {
    String methodName = "autoAdjustToNextRuntimeWeekly";
    Calendar currentTime = Calendar.getInstance();
    
    if(isEnabledSmartlyAdjust && validateNextRunTime(nextRuntime, currentTime.getTime()))
    {
      scheduler.setNextRunTime(nextRuntime);
      return;
    }
    else
    {
      Calendar nextRunC = Calendar.getInstance();
      nextRunC.setTime(nextRuntime);
    
      nextRunC.set(Calendar.WEEK_OF_MONTH, nextRunC.get(Calendar.WEEK_OF_MONTH) + archiveEveryNthWeek); //run on next week
    
      //_logger.logMessage(methodName, null, "AFter auto adjust "+nextRunC.getTime());
      autoAdjustToNextRuntimeWeekly(nextRunC.getTime(), archiveEveryNthWeek, scheduler, true);
    }
  }
  
  /**
   * Adjust to the next run time given the frequency.
   * 
   * If user want to start immediately, he can specify the effective start date time on yesterday time, 
   * however this way may miss the schedule time, cos at the time we add the next run time, it maybe earlier than
   * the time we run the MBean service which pick up the next task to run based on the next runtime.
   * 
   * Maybe a way to resolve this is to add in some checking on the next run time whether is too near to the
   * system current time. If so , we auto incremetn one day (depend on the frequency). This is more user friendly instead of let the user
   * to miss the schedule task.
   * 
   * @param startTime
   * @param startDate
   * @param intervalDay
   * @param scheduler
   * @throws Exception
   */
	private void autoAdjustToNextRuntimeDaily(Date nextRunTime,int archiveEveryNthDay, ArchiveScheduler scheduler, 
                                            boolean isEnabledSmartlyAdjust) throws ArchiveScheduleException
	{
    String methodName = "autoAdjustToNextRuntimeDaily";
    Calendar currentTime = Calendar.getInstance();
                                                                                           
    if(isEnabledSmartlyAdjust && validateNextRunTime(nextRunTime, currentTime.getTime()))  //better to check the next run time in advance the current time for an interval, so that
                                                                                              //MBean will not miss loading the archive scheduler task
    {
      scheduler.setNextRunTime(nextRunTime);
      return;
    }
    else
    {
      Calendar nextRunC = Calendar.getInstance();
      nextRunC.setTime(nextRunTime);
       
      nextRunC.set(Calendar.DAY_OF_MONTH, nextRunC.get(Calendar.DAY_OF_MONTH) + archiveEveryNthDay);
      nextRunTime = nextRunC.getTime();
      
      autoAdjustToNextRuntimeDaily(nextRunTime, archiveEveryNthDay, scheduler, true);
    }
    //_logger.logMessage(methodName, null,"effectiveStartDateTime: "+effectiveStartDateTime+" startOnEveryNthDay :"+intervalDay);
	}
	
	/**
	 * Validate that the next run time is later than the current time for 10 secs
	 * @param nextRunTime
	 * @return true is the next run time is later than the current time for 10 secs;
	 *         false otherwise.
	 */
  
	private boolean validateNextRunTime(Date nextRunTime, Date currentDateTime) throws ArchiveScheduleException
	{
    String method = "validateNextRunTime";
		long currentDateTimeInMilli = currentDateTime.getTime();
		long nextRunTimeInMilli = nextRunTime.getTime();
		
    _logger.logMessage(method, null, "Time different: "+ (nextRunTimeInMilli - currentDateTimeInMilli) +" time interval: "+getTimeIntervalForNextRuntime());
    
		if( (nextRunTimeInMilli - currentDateTimeInMilli) > getTimeIntervalForNextRuntime() * 1000)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
  
  private int getTimeIntervalForNextRuntime() throws ArchiveScheduleException
  {
    try
    {
      String timeInterval = getPropertiesManager().getAuditProperties(IISATProperty.CATEGORY, IISATProperty.ARCHIVE_SCHEDULER_TIME_INTERVAL, null);
      if(timeInterval == null)
      {
        throw new ArchiveScheduleException("The time interval for the next runtime is null, pls initialize in DB");
      }
      else
      {
        return Integer.parseInt(timeInterval);
      }
    }
    catch(Exception ex)
    {
      throw new ArchiveScheduleException("Error in getting the time interval for archive scheduler's next run time", ex);
    }
  }
  
  private IAuditPropertiesManagerObj getPropertiesManager() throws Exception
  {
      JndiFinder jndiFinder = new JndiFinder(null);
      IAuditPropertiesManagerHome home = (IAuditPropertiesManagerHome)jndiFinder.lookup(IAuditPropertiesManagerHome.class.getName(),
                                                                                      IAuditPropertiesManagerHome.class);
      return home.create();
  }
  
  /**
   * Set the startTime, and the specify day in the week given the startOnNthDayOfWeek to Date. The year, month
   * will be same as the current time. 
   * @param startTime
   * @param startOnNthDayOfWeek Range from Calendar Sunday to Saturday (1 to 7)
   * @return
   * @throws Exception
   */
  private Date convertDateTimeInStrToDate(String startTime, int startOnNthDayOfWeek) throws Exception
  {
    SimpleDateFormat dateFormatter = new SimpleDateFormat(ISchedulerConstant.TIME);
    Date time = dateFormatter.parse(startTime);
    Calendar c = Calendar.getInstance();
    c.setTime(time);
    
    c.set(Calendar.DAY_OF_WEEK, startOnNthDayOfWeek);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    
    return c.getTime();
  }
  
  public String convertCustomerListToStr(List<String> customerList)
  {
    String customerListInStr = "";
    for(int i = 0; customerList != null && i < customerList.size(); i++)
    {
      String customer = customerList.get(i);
      customerListInStr += customer + "@@;";
    }
    return customerListInStr;
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
