/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchivalTaskDistributor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 30, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.cluster.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import com.gridnode.gtas.audit.archive.ArchiveCriteria;
import com.gridnode.gtas.audit.archive.ArchiveSummary;
import com.gridnode.gtas.audit.archive.IArchiveConstant;
import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerHome;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerLocalHome;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerLocalObj;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerObj;
import com.gridnode.gtas.audit.archive.helper.ArchiveActivityHelper;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerHome;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerObj;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.tracking.helpers.IISATProperty;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is responsible to distribute the task to the different node. The necessary task information is also generated for example
 * the archiveID, jobID, archive summary file. 
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.1.2)
 */
public class ArchivalTaskDistributor
{
  private static final String ARCHIVAL_ID_FORMAT = "yyyyMMdd_hhmmssSSS";
  private static final String CLASS_NAME = ArchivalTaskDistributor.class.getSimpleName();
  
  private Logger _logger;
  private Hashtable _criteria;
  private String _archiveID;
  
  public ArchivalTaskDistributor()
  {
    _logger = getLogger();
  }
  
  public void setArchivalCriteria(Hashtable criteria)
  {
    _criteria = criteria;
  }
  
  public void distributeTask(Hashtable<String, ArchiveCriteria> archiveTasks, String archiveActivity, String archiveName,
                             String archiveDesc) throws ArchiveTrailDataException
  {
    String mn = "distributeTask";
    
    
    try
    {
      generateArchiveSummaryInfo(_archiveID);
      createArchiveTaskList(archiveTasks, _archiveID);
    
        Enumeration<String> jobsID = archiveTasks.keys();
        while(jobsID.hasMoreElements())
        {
          String jobID = jobsID.nextElement();
          ArchiveCriteria criteria = archiveTasks.get(jobID);
          String group = criteria.getGroup();
          
          Hashtable<String, Object> archiveInfo = new Hashtable<String, Object>();
          archiveInfo.put(IArchiveConstant.ARCHIVE_ACTIVITY, archiveActivity);
          archiveInfo.put(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME, criteria.getFromStartDate().getTime());
          archiveInfo.put(IArchiveConstant.CRITERIA_TO_START_DATE_TIME, criteria.getToStartDate().getTime());
          archiveInfo.put(IArchiveConstant.ARCHIVE_NAME, archiveName);
          
          if(archiveDesc != null)
          {
            archiveInfo.put(IArchiveConstant.ARCHIVE_DESCRIPTION, archiveDesc);
          }
          
          if(group != null)
          {
            archiveInfo.put(IArchiveConstant.GROUP_INFO, criteria.getGroup());
          }
          
          
          archiveInfo.put(IArchiveConstant.ARCHIVE_ORPHAN_RECORD, criteria.isArchiveOrphanRecord());
          archiveInfo.put(IArchiveConstant.ARCHIVE_ID, _archiveID);
          archiveInfo.put(IArchiveConstant.ARCHIVE_JOBS_ID, jobID);
        
          _logger.logMessage(mn, null, "Delegate archival task: "+archiveInfo);
          
          //TODO 01/11/2007: In future if we support multinode getting the sub-portion of the archive job,
          //the sending out of the tasks should be in Atomic. It can be achieved by specifying the JMS Session
          //as "TRANS".
          delegateToArchiveMsgBus(archiveInfo);
        }
    }
    catch(Exception ex)
    {
      throw new ArchiveTrailDataException("Error in distributing archive task to nodes "+ex.getMessage(), ex);
    }
  }
  
  public String getArchiveID()
  {
    return _archiveID;
  }
  
  public String generateArchiveID()
  {
    Calendar c = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat(ARCHIVAL_ID_FORMAT);
    _archiveID = formatter.format(c.getTime());
    
    return _archiveID;
  }
  
  
  public File generateArchiveSummaryInfo(String archiveID) throws Exception
  {
    IAuditTrailArchiveManagerObj archiveMgr = getArchiveMgr();
    String archiveFolderPath = archiveMgr.getArchiveFolderPath();
    initArchiveFolderPath(archiveFolderPath);
    
    File summaryFile = new File(archiveFolderPath + archiveID+".xml");
    if(summaryFile.isDirectory())
    {
      throw new ArchiveTrailDataException("The summary file path "+summaryFile.getAbsolutePath()+" is a dir !");
    }
    ArchiveActivityHelper.generateArchiveSummaryFile(createArchiveSummary(archiveID), summaryFile);
    
    _logger.logMessage("generateArchiveSummaryInfo", null, "Generated ! "+summaryFile.getAbsolutePath());
    return summaryFile; 
  }
  
  private void initArchiveFolderPath(String archiveFolderPath) throws Exception
  {
    if(archiveFolderPath != null && ! "".equals(archiveFolderPath))
    {
      File archiveFolder = new File(archiveFolderPath);
      if(! archiveFolder.exists())
      {
        boolean isCreatedSuccess = archiveFolder.mkdirs();
        if(! isCreatedSuccess)
        {
          throw new ArchiveTrailDataException("The archive folder dir "+archiveFolder.getAbsolutePath()+" is failed to be created.");
        }
      }
    }
  }
  
  private ArchiveSummary createArchiveSummary(String archiveID)
  {
    ArchiveSummary summary = new ArchiveSummary();
    summary.setGroup(archiveID);
    summary.setArchiveID(archiveID);
       
    if(_criteria != null && _criteria.size() > 0)
    {
      Long fromStartDateTime = (Long)_criteria.get(IArchiveConstant.CRITERIA_FROM_START_DATE_TIME);
      Long toStartDateTime = (Long)_criteria.get(IArchiveConstant.CRITERIA_TO_START_DATE_TIME);
      String archiveName = (String)_criteria.get(IArchiveConstant.ARCHIVE_NAME);
      String archiveDesc = (String)_criteria.get(IArchiveConstant.ARCHIVE_DESCRIPTION);
      String customerList = (String)_criteria.get(IArchiveConstant.GROUP_INFO);
      Boolean isArchiveOrphanRecord = (Boolean)_criteria.get(IArchiveConstant.ARCHIVE_ORPHAN_RECORD);
      
      ArchiveCriteria criteria = new ArchiveCriteria();
      criteria.setFromStartDate(new Date(fromStartDateTime));
      criteria.setToStartDate(new Date(toStartDateTime));
      criteria.setGroup(customerList);
      criteria.setArchiveOrphanRecord(isArchiveOrphanRecord);
      
      summary.setArchiveCriteria(criteria);
      summary.setArchiveName(archiveName);
      summary.setArchiveDescription(archiveDesc);
    }
    
    return summary;
  }
  
  private void createArchiveTaskList(Hashtable<String, ArchiveCriteria> archiveTasks, String archiveID)
    throws ArchiveTrailDataException
  {
    Enumeration<String> jobIDs = archiveTasks.keys();
    Properties prop = new Properties();
    
    while(jobIDs.hasMoreElements())
    {
      String jobID = jobIDs.nextElement();
      prop.put(jobID, Boolean.FALSE.toString());
    }
    
    try
    {
      IAuditTrailArchiveManagerObj archiveMgr = getArchiveMgr();
      String archiveFolderPath = archiveMgr.getArchiveFolderPath();
      initArchiveFolderPath(archiveFolderPath);
    
      File jobProperties = new File(archiveFolderPath+"/"+archiveID+".properties");
      OutputStream out = new FileOutputStream(jobProperties);
      prop.store(out, "");
    }
    catch(Exception ex)
    {
      throw new ArchiveTrailDataException("Can't create the archive task list for tracking ", ex);
    }
  }
  
  private void delegateToArchiveMsgBus(Hashtable<String, Object> criteria) throws Exception
  {
      sendToQueue(criteria);
  }
  
  private void sendToQueue(Hashtable<String,Object> map) throws Exception
  {
    JmsSender sender = new JmsSender();
    sender.setSendProperties(getJMSSenderProperties());
    sender.send(map);
  }
  
  private Properties getJMSSenderProperties() throws Exception
  {
    IAuditPropertiesManagerObj propertiesMgr = getPropertiesMgr();
    Properties pro = propertiesMgr.getAuditProperties(IISATProperty.ARCHIVE_JMS_CATEGORY);
    return pro;
  }
  
  private IAuditPropertiesManagerObj getPropertiesMgr() throws Exception
  {
    JndiFinder finder = new JndiFinder(null);
    IAuditPropertiesManagerHome home = (IAuditPropertiesManagerHome)finder.lookup(IAuditPropertiesManagerHome.class.getName(),
                                                                                  IAuditPropertiesManagerHome.class);
    return home.create();
  }
  
  private IAuditTrailArchiveManagerObj getArchiveMgr() throws Exception
  {
    JndiFinder finder = new JndiFinder(null);
    IAuditTrailArchiveManagerHome archiveHome = (IAuditTrailArchiveManagerHome)finder.lookup(IAuditTrailArchiveManagerHome.class.getName(), 
                                                                             IAuditTrailArchiveManagerHome.class);
    return archiveHome.create();
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
