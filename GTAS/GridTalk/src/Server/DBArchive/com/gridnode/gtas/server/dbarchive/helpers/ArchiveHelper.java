/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveHelper.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 29, 2004 			Mahesh             	Created
 * Nov 10 2005      Neo Sok Lay         Use Local context to lookup XMLServiceLocal bean
 * Feb 07 2007      Neo Sok Lay         Change sendToArchiveTopic() to sendToArchiveDest().
 * Feb 26 2007      Tam Wei Xiang       Add method to send back the archive/restore status
 *                                      to OTC-plug in.
 * Nov 27 2007      Tam Wei Xiang       To ensure the archive event & restore event we send via
 *                                      JMS is not impacted by the fail over process. The jms msg
 *                                      will be retried if encounter jms exception.
 * Mar 26 2009      Tam Wei Xiang       #122 Add method getArchiveDataRange
 * Apr 12 2009      Tam Wei Xiang       #122 1) Add method computeMinutelyArchiveDateRange(Date, int, TimeZone),
 *                                           computeHourlyArchiveDateRange(..)
 *                                           2) The creation of the "ArchiveTask" should be managed
 *                                           by a SessionBean (ArchiveManager)
 */
package com.gridnode.gtas.server.dbarchive.helpers;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import com.gridnode.gtas.events.dbarchive.CreateArchiveEvent;
import com.gridnode.gtas.model.scheduler.IScheduleFrequency;
import com.gridnode.gtas.server.dbarchive.facade.ejb.IArchiveManagerHome;
import com.gridnode.gtas.server.dbarchive.facade.ejb.IArchiveManagerObj;
import com.gridnode.gtas.server.dbarchive.model.ArchiveMetaInfo;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerHome;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerObj;
import com.gridnode.gtas.server.rnif.helpers.RnifException;
import com.gridnode.gtas.server.scheduler.helpers.ActionHelper;
import com.gridnode.gtas.server.scheduler.model.Schedule;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerHome;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerHome;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerObj;
import com.gridnode.pdip.base.time.entities.model.IiCalAlarm;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalHome;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalObj;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.jms.JMSRetrySender;
import com.gridnode.pdip.framework.jms.JMSSender;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.SystemUtil;
import com.gridnode.pdip.framework.util.TimeUtil;


public class ArchiveHelper
{

  private static Properties _archiveProps;
  
  public static synchronized void loadProperties() throws SystemException 
  {
    try
    {
      if(_archiveProps!=null)
        return;
      Configuration config = ConfigurationManager.getInstance().getConfig(IDBArchivePathConfig.CONFIG_NAME);
      _archiveProps=config.getProperties();
    }
    catch(Throwable th)
    {
      Logger.warn("[ArchiveHelper.loadProperties] Unable to load archive properties ",th);
      throw new SystemException("Unable to load archive properties. Unexpected Error: "+th.getMessage(),th);
    }
  }

  public static Properties getProperties(String propertyConfigFileKey) throws SystemException {
      try{
          Configuration config = ConfigurationManager.getInstance().getConfig(propertyConfigFileKey);
          return config.getProperties();
      }catch(Throwable th){
          Logger.warn("[ArchiveHelper.getProperties] Unable to load properties file, propertyConfigFileKey="+propertyConfigFileKey,th);
          throw new SystemException("[ArchiveHelper.getProperties] Unable to load workflow properties file, propertyFileKey="+propertyConfigFileKey,th);
      }
  }

  public static String getProperty(String key) throws SystemException {
      if(_archiveProps==null)
          loadProperties();
      return _archiveProps.getProperty(key);
  }
  
  
  public static IDocumentManagerObj getDocumentManager() throws ServiceLookupException
  {
    return (IDocumentManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IDocumentManagerHome.class.getName(),
      IDocumentManagerHome.class,
      new Object[0]);
  }

  public static IXMLServiceLocalObj getXMLManager() throws ServiceLookupException
  {
    return (IXMLServiceLocalObj) ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getObj(
      IXMLServiceLocalHome.class.getName(),
      IXMLServiceLocalHome.class,
      new Object[0]);
  }
  
  public static IGWFWorkflowManagerObj getWorkflowMgr() throws ServiceLookupException
  {
    return (IGWFWorkflowManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IGWFWorkflowManagerHome.class.getName(),
      IGWFWorkflowManagerHome.class,
      new Object[0]);
  }
  
  public static IRnifManagerObj getRnifManager() throws RnifException, ServiceLookupException
  {
    return (IRnifManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IRnifManagerHome.class.getName(),
      IRnifManagerHome.class,
      new Object[0]);
  }  
  
  public static IiCalTimeMgrObj getIiCalTimeMgr()  throws ServiceLookupException 
  {
    return (IiCalTimeMgrObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IiCalTimeMgrHome.class.getName(),
      IiCalTimeMgrHome.class,
      new Object[0]);
  } 
  
  public static IAlertManagerObj getAlertManager()  throws ServiceLookupException 
  {
    return (IAlertManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
    IAlertManagerHome.class.getName(),
    IAlertManagerHome.class,
      new Object[0]);
  } 

  public static IArchiveManagerObj getArchiveManager() throws ServiceLookupException
  {
    return (IArchiveManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
            IArchiveManagerHome.class.getName(), IArchiveManagerHome.class, new Object[0]);
  }
  
  public static void sendToArchiveDest(Serializable objMsg) throws Throwable
  {
    String archiveDest = getProperty(IDBArchivePathConfig.ARCHIVE_DEST);
    Logger.log("[ArchiveHelper.sendToArchiveDest] archiveDest="+archiveDest);
    
    if(! JMSRetrySender.isSendViaDefMode()) //TWX to ensure the jms msg we sent out still can survive during the fail over process.
    {
      Logger.debug("[ArchiveHelper.sendToArchiveDest] send via JMSRetrySender");
      JMSRetrySender.sendMessageWithPersist(IFrameworkConfig.FRAMEWORK_JMS_CONFIG, getProperty(IDBArchivePathConfig.ARCHIVE_DEST), objMsg, null);
    }
    else
    {
      JMSSender.sendMessage(IFrameworkConfig.FRAMEWORK_JMS_CONFIG, getProperty(IDBArchivePathConfig.ARCHIVE_DEST), objMsg);
    }
  }
  
  //TODO to be enhance as multinode
  public static void sendToGTArchiveDest(Serializable objMsg) throws Throwable
  {
    String archiveDest = getProperty(IDBArchivePathConfig.ARCHIVE_DEST);
    Logger.log("[ArchiveHelper.sendToGTArchiveDest] archiveDest="+archiveDest);
    Logger.log("Send to GT archive queue using local jndi lookup: current node: "+SystemUtil.getHostId());
    
    Hashtable msgProps = new Hashtable();
    msgProps.put(SystemUtil.HOSTID_PROP_KEY, SystemUtil.getHostId());
    
    if(! JMSRetrySender.isSendViaDefMode()) //TWX to ensure the jms msg we sent out still can survive during the fail over process.
    {
      Logger.debug("[ArchiveHelper.sendToGTArchiveDest] send via JMSRetrySender");
      JMSRetrySender.sendMessageWithPersist(IFrameworkConfig.FRAMEWORK_JMS_CONFIG, getProperty(IDBArchivePathConfig.ARCHIVE_DEST), objMsg, msgProps);
    }
    else
    {
      JMSSender.sendMessage(IFrameworkConfig.FRAMEWORK_JNDI_CONFIG, getProperty(IDBArchivePathConfig.ARCHIVE_DEST), objMsg, msgProps);
    }
  }
  
  /**
   * Send back the archive/restore status to TM plug in.
   * @param objMsg
   * @throws SystemException
   */
  public static void sendToTMArchiveDelegateDest(Serializable objMsg) throws Throwable
  {
    String archiveDest = getProperty(IDBArchivePathConfig.TM_ARCHIVE_DELEGATE_DEST);
    Logger.log("[ArchiveHelper.sendToTMArchiveDelegateDest] archiveDest="+archiveDest);
    
    Logger.log("Send back request to TM: current node "+SystemUtil.getHostId());
    
    //TODO to be enhanced
    //Hashtable msgProps = new Hashtable();
    //msgProps.put(SystemUtil.HOSTID_PROP_KEY, SystemUtil.getHostId());
    //JMSSender.sendMessage(IFrameworkConfig.FRAMEWORK_JMS_CONFIG, archiveDest, objMsg, msgProps);
    if(! JMSRetrySender.isSendViaDefMode()) //TWX to ensure the jms msg we sent out still can survive during the fail over process.
    {
      Logger.debug("[ArchiveHelper.sendToTMArchiveDelegateDest] send via JMSRetrySender");
      JMSRetrySender.sendMessageWithPersist(IFrameworkConfig.FRAMEWORK_JMS_CONFIG, archiveDest, objMsg, null);
    }
    else
    {
      JMSSender.sendMessage(IFrameworkConfig.FRAMEWORK_JMS_CONFIG, archiveDest, objMsg);
    }
  }
  
  /**
   * Get the total allowed entry within the DB In Operator
   * @return
   * @throws SystemException
   */
  public static int getTotalAllowedEntryWithinInOperator() throws SystemException
  {
    String totalAllowed = getProperty(IDBArchivePathConfig.MAX_ENTRY_IN_DB_IN_OP);
    Logger.log("[ArchiveHelper.getTotalAllowedEntryWithinInOperator "+totalAllowed);
    if(totalAllowed == null)
    {
      return 1000;
    }
    else
    {
      return Integer.parseInt(totalAllowed);
    }
  }
  public static void saveToArchive(Serializable objMsg)throws Throwable{
    CreateArchiveEvent event=(CreateArchiveEvent)objMsg;
    String archiveID = event.getArchiveID();
    String archiveDescription = event.getArchiveDescription();
    String archiveType = event.getArchiveType();
    
    Boolean isEnableSearchArchived = event.isEnableSearchArchived();
    Boolean isEnableRestoreArchived = event.isEnableRestoreArchived();
   
    String fromStartDate = event.getFromStartDate();
    String toStartDate = event.getToStartDate();
    String fromStartTime = event.getFromStartTime();
    String toStartTime = event.getToStartTime();
    Integer archiveOlderThan  = event.getArchiveOlderThan();
    Boolean isArchiveFrequenceOnce = event.isArchiveFrequencyOnce() == null ? false : event.isArchiveFrequencyOnce();
    String clientTZ = event.getClientTZ();
    
    List partnerIDForArchive =event.getPartnerIDForArchive();
 //   List customerIDList = event.getCustomerIDForArchive();
//    Boolean isArchiveOrphanRecord = event.isArchiveOrphanRecord();

    ArchiveMetaInfo metainfo = new ArchiveMetaInfo();
    metainfo.setArchiveID(archiveID);
    metainfo.setArchiveDescription(archiveDescription);
    if(isEnableSearchArchived==null)
      isEnableSearchArchived=new Boolean(false);
    metainfo.setEnableSearchArchived(isEnableSearchArchived);
    if(isEnableRestoreArchived==null)
      isEnableRestoreArchived=new Boolean(false);
    metainfo.setEnableRestoreArchived(isEnableRestoreArchived);
    metainfo.setFromStartDate(fromStartDate);
    metainfo.setToStartDate(toStartDate);
    metainfo.setFromStartTime(fromStartTime);
    metainfo.setToStartTime(toStartTime);
    
    metainfo.setIncludeIncompleteProcesses(new Boolean(false));
    metainfo.setPartnerIDForArchive(ArchiveMetaInfo.convertToString(partnerIDForArchive));
    metainfo.setArchiveType(archiveType);
    metainfo.setArchiveRecordOlderThan(archiveOlderThan);
    metainfo.setArchiveFrequencyOnce(isArchiveFrequenceOnce);
    metainfo.setClientTz(clientTZ);

 //   metainfo.setPartnerIDForArchive(metainfo.convertToString(partnerIDForArchive));
    
  //  metainfo.setCustomerIDForArchive(metainfo.convertToString(customerIDList));

//    metainfo.setArchiveOrphanRecord(isArchiveOrphanRecord);

    if (ArchiveMetaInfo.ARCHIVE_TYPE_PROCESS_INSTANCE.equals(archiveType))
    {
      List processDefNameList = event.getProcessDefNameList();
      metainfo.setProcessDefNameList(ArchiveMetaInfo.convertToString(processDefNameList));
      metainfo.setIncludeIncompleteProcesses(event.isIncludeIncompleteProcesses());
    }
    else if (ArchiveMetaInfo.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
    {
      String docTypeList = ArchiveMetaInfo.convertToString(event.getDocumentTypeList());
      metainfo.setDocumentTypeList(docTypeList);

      List folderList = event.getFolderList();
      metainfo.setFolderList(ArchiveMetaInfo.convertToString(folderList));
    }
    getArchiveManager().createArchiveMetaInfo(metainfo);  
  }
  
  /**
   * Infer the archive date range based on the given archiveFrequency and archiveOlderThan, tz
   * @param archiveFrequency It can be Daily, Weekly, Monthly
   * @param archiveOlderThan indicate record age threshold that we will keep in the system 
   * @param tz the client timezone of the user when he/she create the archiveTask. The date range will be
   *           infered based on the client timezone.
   * @return
   */
  public static Date[] getArchiveDataRange(int archiveFrequency, int archiveOlderThan, TimeZone tz)
  {
    if(archiveFrequency == IScheduleFrequency.DAILY)
    {
      return computeDailyArchiveDateRange(new Date(), archiveOlderThan, tz);
    }
    else if(archiveFrequency == IScheduleFrequency.WEEKLY)
    {
      return computeWeeklyArchiveDateRange(new Date(), archiveOlderThan, tz);
    }
    else if(archiveFrequency == IScheduleFrequency.MONTHLY)
    {
      return computeMonthlyArchiveDateRange(new Date(), archiveOlderThan, tz);
    }
    else if(archiveFrequency == IScheduleFrequency.MINUTELY)
    {
      return computeMinutelyArchiveDateRange(new Date(), archiveOlderThan, tz);
    }
    else if(archiveFrequency == IScheduleFrequency.HOURLY)
    {
      return computeHourlyArchiveDateRange(new Date(), archiveOlderThan, tz);
    }
    else
    {
      throw new IllegalArgumentException("archiveFrequency : "+archiveFrequency+" is currently not supported !");
    }
  }
  
  private static Date[] computeMinutelyArchiveDateRange(Date nextRunTime, int archiveRecordOlderThanNth, TimeZone clientTz)
  {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(nextRunTime.getTime());
    if(clientTz == null)
    {
      clientTz = TimeZone.getDefault();
    }
    
    c.setTimeZone(clientTz);
    
    c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) - archiveRecordOlderThanNth);
    //c.set(Calendar.SECOND, 59);
    //c.set(Calendar.MILLISECOND, 999);
    Date toSTartDateTime = c.getTime();
    
    Date fromStartTime = getArchiveStartDate(clientTz);
    
    return new Date[]{fromStartTime, toSTartDateTime};
  }
  
  private static Date[] computeHourlyArchiveDateRange(Date nextRunTime, int archiveRecordOlderThanNth, TimeZone clientTz)
  {
    Calendar c = Calendar.getInstance();
    //c.setTimeInMillis(nextRunTime.getTime());
    if(clientTz == null)
    {
      clientTz = TimeZone.getDefault();
    }
    
    c.setTimeZone(clientTz);
    System.out.println("After setTZ: "+c.getTime());
    
    c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) - archiveRecordOlderThanNth);
    //c.set(Calendar.MINUTE, 59);
    //c.set(Calendar.SECOND, 59);
    //c.set(Calendar.MILLISECOND, 999);
    Date toSTartDateTime = c.getTime();

    Date fromStartTime = getArchiveStartDate(clientTz);
    
    return new Date[]{fromStartTime, toSTartDateTime};
  }
  
  private static Date[] computeDailyArchiveDateRange(Date nextRuntime,  int archiveRecorldOlderThanNth, TimeZone clientTz)
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
    System.out.println("Daily toStartTime: "+toStartDateTime);
    
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
  private static Date[] computeWeeklyArchiveDateRange(Date nextRuntime, int archiveRecordOlderThanNth, TimeZone clientTz)
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
  private static Date[] computeMonthlyArchiveDateRange(Date nextRuntime, int archiveOlderThanNthMonth, TimeZone clientTz)
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
  
  private static Date getArchiveStartDate(TimeZone clientTz)
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
   * Get the alarmUID corresponding Schedule which encapsulate the info eg. Schedule task frequency
   * @param alarmUID
   * @return Schedule that related to the given alarmUID
   */
  public static Schedule getTaskSchedule(Long alarmUID) throws Exception
  {
    iCalAlarm alarm = getIiCalTimeMgr().getAlarm(alarmUID);
    return ActionHelper.convertiCalAlarmToSchedule(alarm);
  }
  
  /**
   * Convert the given date to "UTC" timezone.
   * @param date the archive date in format "yyyy-MM-dd"
   * @param time the archive time in format "HH:mm"
   * @param clientTZ the user logon timezone
   * @return the Timestamp in UTC timezone
   * @throws ParseException
   */
  public static Timestamp convertToUTC(String date, String time, TimeZone clientTZ) throws ParseException
  {
    if (clientTZ == null)
    {
      clientTZ = TimeZone.getDefault();
    }
    Calendar c = Calendar.getInstance(clientTZ);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    formatter.setTimeZone(clientTZ);
    Date givenDate = formatter.parse(date);
    c.setTimeInMillis(givenDate.getTime());
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    c = Calendar.getInstance(clientTZ);
    formatter = new SimpleDateFormat("HH:mm");
    formatter.setTimeZone(clientTZ);
    Date givenTime = formatter.parse(time);
    c.setTimeInMillis(givenTime.getTime());
    int hour = c.get(Calendar.HOUR_OF_DAY); // 24 hour
    int minute = c.get(Calendar.MINUTE);

    c = Calendar.getInstance(clientTZ);
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month);
    c.set(Calendar.DAY_OF_MONTH, day);
    c.set(Calendar.HOUR_OF_DAY, hour);
    c.set(Calendar.MINUTE, minute);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);

    return new Timestamp(TimeUtil.localToUtc(c.getTimeInMillis()));
  }
  
  public static Timestamp convertToUTC(Date date)
  {
    return new Timestamp(TimeUtil.localToUtc(date.getTime()));
  }
}
