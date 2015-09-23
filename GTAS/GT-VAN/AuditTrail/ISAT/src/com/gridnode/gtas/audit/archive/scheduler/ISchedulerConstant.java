package com.gridnode.gtas.audit.archive.scheduler;

public interface ISchedulerConstant 
{
	public static final String FREQUENCY_ONCE = "once";
	public static final String FREQUENCY_DAILY = "daily";
	public static final String FREQUENCY_WEEKLY = "weekly";
	public static final String FREQUENCY_MONTHLY = "monthly";
	
  public static final String TIME = "HH:mm";
  public static final String DATE = "yyyy-MM-dd";
  
  //HTTP Servlet  
  public static final String SCHEDULE_ACT = "action";
  public static final String SCHEDULE_ACT_CREATE = "addSchedule";
  public static final String SCHEDULE_ACT_UPDATE = "updateSchedule";
  public static final String SCHEDULE_ACT_DELETE = "deleteSchedule";
  public static final String SCHEDULE_ACT_ARCHIVE_NOW  = "runSchedule";
  
  public static final String FREQUENCY = "frequency";
  public static final String EFFECTIVE_START_DATE = "startDate";
  public static final String EFFECTIVE_START_TIME = "runTime";
  public static final String ARCHIVE_OLDER_THAN = "archiveOlderThan";
  public static final String ARCHIVE_EVERY_NTH = "runEvery";
  public static final String CUSTOMER_LIST = "customer";
  public static final String IS_ENABLED = "scheduleStatus";
  public static final String ARCHIVE_SCHEDULE_UID = "scheduleUID";
  public static final String USER_TIME_ZONE = "timezone";
  public static final String IS_ARCHIVE_ORPHAN_RECORD = "archiveOrphanRecord";
  
  public static final String EFFECTIVE_START_DATE_TIME = "effStartDateTime";
}
