/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveScheduleTask.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 13, 2007    Tam Wei Xiang       Created
 * Jun 01, 2007    Tam Wei Xiang       Support archive by customer
 * Jun 28, 2007    Tam Wei Xiang       To support the single node case while
 *                                     invoking the initiator.
 */
package com.gridnode.gtas.audit.archive.scheduler;

import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimerTask;

import com.gridnode.gtas.audit.archive.ArchivalInitiator;
import com.gridnode.gtas.audit.archive.ArchiveInitiatorFactory;
import com.gridnode.gtas.audit.archive.IArchivalInitiator;
import com.gridnode.gtas.audit.archive.IArchiveConstant;
import com.gridnode.gtas.audit.archive.scheduler.facade.ejb.IArchiveSchedulerHome;
import com.gridnode.gtas.audit.archive.scheduler.facade.ejb.IArchiveSchedulerObj;
import com.gridnode.gtas.audit.exceptions.ILogErrorCodes;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerHome;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerObj;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveScheduleTask extends TimerTask
{
  private static String CLASS_NAME = "ArchiveScheduleTask";
  private Logger _logger;
  
  private long _archiveFromStartDate;
  private long _archiveToStartDate;
  private String _customerList;
  private String _archiveSchedulerUID;
  private String _archiveDescription;
  private String _archiveName;
  private boolean _isArchiveOrphanRecord;
  
  public ArchiveScheduleTask(long archiveFromStartDate, long archiveToStartDate,
                             String customerList, String archiveSchedulerUID,
                             String archiveDescription, String archiveName,
                             boolean isArchiveOrphanRecord)
  {
    setArchiveFromStartDate(archiveFromStartDate);
    setArchiveToStartDate(archiveToStartDate);
    setGroupList(customerList);
    setArchiveSchedulerUID(archiveSchedulerUID);
    setArchiveDescription(archiveDescription);
    setArchiveName(archiveName);
    setArchiveOrphanRecord(isArchiveOrphanRecord);
    _logger = getLogger();
  }
  
  @Override
  public void run()
  {
    String methodName = "run";
    _logger.logMessage(methodName, null, "Start delegating archive request to the handler. scheduler UID "+getArchiveSchedulerUID());
    //sendToQueue(getArchiveCriteria());
    
    boolean isInvokeSuccess = true;
    IArchivalInitiator initiator = ArchiveInitiatorFactory.getInstance().getArchivalInitiator();
    try
    {
      initiator.initArchive(getArchiveCriteria());
    }
    catch(Exception ex)
    {
      isInvokeSuccess = false;
      _logger.logError(ILogErrorCodes.AT_ARCHIVE_SCHEDULER_TASK_KICK_START_ERROR, "sendToQeueu", null, "Error in sending the archive request", ex);
    }
    finally
    {
      updateArchiveInvokeStatus(getArchiveSchedulerUID(), isInvokeSuccess);
    }
    
    _logger.logMessage(methodName, null, "End delegated archive request to the handler. scheduler UID "+getArchiveSchedulerUID());
  }
  
  private Hashtable getArchiveCriteria()
  {
    Hashtable<String, Object> archiveCriteria = new Hashtable<String, Object>();
    archiveCriteria.put(IArchiveConstant.ARCHIVE_ACTIVITY, IArchiveConstant.ARCHIVE_ACT_ARCHIVE);
    archiveCriteria.put(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME, getArchiveFromStartDate());
    archiveCriteria.put(IArchiveConstant.CRITERIA_TO_START_DATE_TIME, getArchiveToStartDate());
    archiveCriteria.put(IArchiveConstant.ARCHIVE_NAME, getArchiveName());
    
    if(getArchiveDescription() != null)
    {
      archiveCriteria.put(IArchiveConstant.ARCHIVE_DESCRIPTION, getArchiveDescription());
    }
    archiveCriteria.put(IArchiveConstant.ARCHIVE_ORPHAN_RECORD, isArchiveOrphanRecord());
    
    String groups = "";
    String groupList = getGroupList();
    StringTokenizer st = new StringTokenizer(groupList, "@@;");
    while(st.hasMoreTokens())
    {
      groups += st.nextToken() + ";";
    }
    
    archiveCriteria.put(IArchiveConstant.GROUP_INFO, groups);
    return archiveCriteria;
  }
  
  /*
  private void sendToQueue(Hashtable archiveCriteria)
  {
    boolean isInvokeSucces = true;
    try
    {
      JmsSender sender = new JmsSender();
      IAuditPropertiesManagerObj propertiesMgr = getPropertiesMgr();
      Properties pro = propertiesMgr.getAuditProperties(IISATProperty.ARCHIVE_JMS_CATEGORY);
      
      sender.setSendProperties(pro);
      sender.send(archiveCriteria);
    }
    catch(Exception ex)
    {
      isInvokeSucces = false;
      _logger.logError(ILogErrorCodes.AT_ARCHIVE_SCHEDULER_TASK_KICK_START_ERROR, "sendToQeueu", null, "Error in sending the archive request", ex);
    }
    finally
    {
      updateArchiveInvokeStatus(getArchiveSchedulerUID(), isInvokeSucces);
    }
  }*/
  
  private void updateArchiveInvokeStatus(String archiveSchedulerUID, boolean isSuccessInvoke)
  {
    try
    {
      //update status, update next run time
      getArchiveSchedulerMgr().updateArchiveSchedulerStatus(isSuccessInvoke, archiveSchedulerUID);
    }
    catch(Exception ex)
    {
      _logger.logError(ILogErrorCodes.AT_ARCHIVE_SCHEDULER_SERVICE_ERROR, "updateArchiveInvokeStatus", null, "Error in updating the archive invoke status to ArchiveScheduler with UID "+archiveSchedulerUID+" Its invoke status is "+isSuccessInvoke, ex);
    }
  }
  
  private IArchiveSchedulerObj getArchiveSchedulerMgr() throws Exception
  {
    JndiFinder jndiFinder = new JndiFinder(null);
    IArchiveSchedulerHome mgrHome = (IArchiveSchedulerHome)
                                              jndiFinder.lookup(IArchiveSchedulerHome.class.getName(), IArchiveSchedulerHome.class);
    return mgrHome.create();
  }
  
  private IAuditPropertiesManagerObj getPropertiesMgr() throws Exception
  {
    JndiFinder jndiFinder = new JndiFinder(null);
    IAuditPropertiesManagerHome mgrHome = (IAuditPropertiesManagerHome)
                                              jndiFinder.lookup(IAuditPropertiesManagerHome.class.getName(), IAuditPropertiesManagerHome.class);
    return mgrHome.create();
  }
  
  public String getArchiveDescription()
  {
    return _archiveDescription;
  }

  public void setArchiveDescription(String description)
  {
    _archiveDescription = description;
  }

  public long getArchiveFromStartDate()
  {
    return _archiveFromStartDate;
  }

  public void setArchiveFromStartDate(long fromStartDate)
  {
    _archiveFromStartDate = fromStartDate;
  }

  public String getArchiveName()
  {
    return _archiveName;
  }

  public void setArchiveName(String name)
  {
    _archiveName = name;
  }

  public String getArchiveSchedulerUID()
  {
    return _archiveSchedulerUID;
  }

  public void setArchiveSchedulerUID(String schedulerUID)
  {
    _archiveSchedulerUID = schedulerUID;
  }

  public long getArchiveToStartDate()
  {
    return _archiveToStartDate;
  }

  public void setArchiveToStartDate(long toStartDate)
  {
    _archiveToStartDate = toStartDate;
  }

  public String getGroupList()
  {
    return _customerList;
  }

  public void setGroupList(String list)
  {
    _customerList = list;
  }
  
  public boolean isArchiveOrphanRecord()
  {
    return _isArchiveOrphanRecord;
  }

  public void setArchiveOrphanRecord(boolean archiveOrphanRecord)
  {
    _isArchiveOrphanRecord = archiveOrphanRecord;
  }

  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
  /**
   * private Date _archiveFromStartDate;
  private Date _archiveToStartDate;
  private String _customerList;
  private String _archiveSchedulerUID;
  private String _archiveDescription;
  private String _archiveName;
   */
  public String toString()
  {
    return "ArchiveScheduleTask [achiveFromStartDate: "+ new Date(getArchiveFromStartDate())+" archiveToStartDate "+ new Date(getArchiveToStartDate())+
           " customerList: "+getGroupList()+" archiveSchedulerUID "+getArchiveSchedulerUID()+
           " archiveDescription: "+getArchiveDescription()+" archiveName: "+getArchiveName()+"]";
  }
}
