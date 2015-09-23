/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTArchiveHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 23, 2007    Tam Wei Xiang       Created
 * May 25, 2007    Tam Wei Xiang       Support archive by customer
 * Nov 27, 2007    Tam Wei Xiang       To ensure the jms msg we sent out still can survive during the fail over process.
 */
package com.gridnode.gtas.audit.archive.helpers;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.jms.MapMessage;

import com.gridnode.gtas.audit.archive.exception.ArchiveDelegateException;
import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.common.IGTArchiveConstant;
import com.gridnode.gtas.audit.extraction.helper.IGTATConstant;
import com.gridnode.gtas.events.dbarchive.ArchiveDocumentEvent;
import com.gridnode.gtas.events.dbarchive.RestoreDocumentEvent;
import com.gridnode.gtas.server.dbarchive.helpers.ArchiveHelper;
import com.gridnode.gtas.server.dbarchive.helpers.IDBArchiveConstants;
import com.gridnode.gtas.server.dbarchive.helpers.IDBArchivePathConfig;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.folder.ExportFolder;
import com.gridnode.gtas.server.document.folder.ImportFolder;
import com.gridnode.gtas.server.document.folder.InboundFolder;
import com.gridnode.gtas.server.document.folder.OutboundFolder;
import com.gridnode.gtas.server.document.model.DocumentType;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerHome;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerObj;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.util.SystemUtil;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.jms.JmsRetrySender;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.jms.JmsSenderException;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is used to prepare the necessary data that is required in delegating 
 * the request to the GT archive. It also handle the GT archive/restore status. 
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public class GTArchiveDelegateHelper
{
  private static final String CLASS_NAME = "GTArchiveHelper";
  private Logger _logger = null;
  
  public GTArchiveDelegateHelper()
  {
    _logger = getLogger();
  }
  
  public List<String> getAllProcessDefname() throws Exception
  {
    String methodName = "getAllProcessDefname";
    ArrayList<String> processDefNames = new ArrayList<String>();
    IRNProcessDefManagerObj processDefMgr = getProcessDefManager();
    Collection<ProcessDef> processDefs = processDefMgr.findProcessDefs(null);
    
    if(processDefs != null && processDefs.size() > 0)
    {
      Iterator<ProcessDef> ite = processDefs.iterator();
      while(ite.hasNext())
      {
        ProcessDef def = ite.next();
        processDefNames.add(def.getDefName());
      }
    }
    else
    {
      throw new ArchiveDelegateException("["+CLASS_NAME+"."+methodName+"] Can't find any process def in GT. This is needed in archive by process instance !");
    }
    return processDefNames;
  }
  
  public ArchiveDocumentEvent createArchiveDocumentEvent(MapMessage mapMsg) throws Exception
  {
    String archiveName = mapMsg.getString(IGTArchiveConstant.ARCHIVE_NAME);
    String description = mapMsg.getString(IGTArchiveConstant.ARCHIVE_DESCRIPTION);
    
    boolean isEnabledEStoreSearch = mapMsg.getBoolean(IGTArchiveConstant.IS_ENABLED_ARCHIVED_SEARCHED);
    boolean isEnabledRestore = mapMsg.getBoolean(IGTArchiveConstant.IS_ENABLED_RESTORE);
    
    Long fromStartDateInMilli = mapMsg.getLong(IGTArchiveConstant.FROM_START_DATE_TIME);
    Long toStartDateInMilli = mapMsg.getLong(IGTArchiveConstant.TO_START_DATE_TIME);
    String archiveID = mapMsg.getString(IGTArchiveConstant.ARCHIVE_ID);
    String archiveType = mapMsg.getString(IGTArchiveConstant.ARCHIVE_TYPE);
    
    Timestamp fromStartTime = convertToTimeStamp(fromStartDateInMilli);
    Timestamp toStartTime = convertToTimeStamp(toStartDateInMilli);
    
    String customerID = mapMsg.getString(IGTArchiveConstant.CUSTOMER_ID);
    boolean isArchiveOrphanRecord = mapMsg.getBoolean(IGTArchiveConstant.ARCHIVE_ORPHAN_RECORD);
    String archiveJobID = mapMsg.getString(IGTArchiveConstant.ARCHIVE_JOBS_ID);
    
    ArchiveDocumentEvent event = new ArchiveDocumentEvent();
    event.setArchiveName(archiveName);
    event.setArchiveDescription(description);
    event.setEnableSearchArchived(isEnabledEStoreSearch);
    event.setEnableRestoreArchived(isEnabledRestore);
    event.setFromStartTime(fromStartTime);
    event.setToStartTime(toStartTime);
    event.setArchiveID(archiveID);
    
    event.setIncludeIncompleteProcesses(true);
    event.setPartnerIDForArchive(new ArrayList<String>());
    event.setArchiveType(archiveType);
    
    ArrayList<String> customerIDList = convertStrToList(customerID);
    event.setCustomerIDForArchive(customerIDList);
    event.setArchiveOrphanRecord(isArchiveOrphanRecord);
    event.setArchiveJobID(archiveJobID);
    
    if(IGTArchiveConstant.ARCHIVE_TYPE_PI.equals(archiveType))
    {
      List<String> processDefNameList = getAllProcessDefname();
      event.setProcessDefNameList(processDefNameList);
    }
    else if(IGTArchiveConstant.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
    {
      List<String> docTypeList = getAllGTDocumentType();
      event.setDocumentTypeList(docTypeList);
      
      List<String> folderList = getFolderList();
      event.setFolderList(folderList);
    }
    
    _logger.logMessage("createArchiveDocumentEvent", null, "archive criteria [archiveName: "+archiveName+", description: "+description+
                       " enable estore search:"+isEnabledEStoreSearch+" enableRestore: "+isEnabledRestore+
                       " fromStartTime: "+fromStartTime+" toStartTime:"+toStartTime+" archiveID: "+archiveID+" archiveType:"+archiveType+"" +
                       " beIDList: "+customerIDList+" isArchiveOrphanRecord: "+isArchiveOrphanRecord+" archiveJobID "+archiveJobID+"]");
    
    return event;
  }
  
  public RestoreDocumentEvent createRestoreDocumentEvent(MapMessage mapMsg) throws Exception
  {
    String methodName = "createRestoreDocumentEvent";
    String summaryFileToBeRestored = mapMsg.getString(IGTArchiveConstant.ARCHIVE_SUMMARY_FILE);
    String archiveID = mapMsg.getString(IGTArchiveConstant.ARCHIVE_ID);
    String restoreType = mapMsg.getString(IGTArchiveConstant.ARCHIVE_TYPE);
    
    //Prepare the abs path for the summary file
    File gtArchiveFolder = FileUtil.getFile(IDBArchivePathConfig.PATH_ARCHIVE, "");
    if(gtArchiveFolder == null)
    {
      throw new ArchiveDelegateException("GT archive folder path does not exist");
    }
    _logger.logMessage(methodName, null, "GT Archive folder path is "+gtArchiveFolder);
    
    File summaryFile = new File(gtArchiveFolder.getAbsolutePath()+"/"+summaryFileToBeRestored);
    
    RestoreDocumentEvent restoreEvent = new RestoreDocumentEvent(summaryFile.getAbsolutePath());
    restoreEvent.setArchiveID(archiveID);
    restoreEvent.setRestoreType(restoreType);
    
    return restoreEvent;
  }
  
  /**
   * Get all the GT document types which is required in archive by document
   * @return
   * @throws Exception
   */
  private List<String> getAllGTDocumentType() throws Exception
  {
    String methodName = "getAllGTDocumentType";
    ArrayList<String> documentTypeDesc = new ArrayList<String>();
    Collection<DocumentType> docTypes = getDocumentMgr().findDocumentTypes(null);
    if(docTypes != null && docTypes.size() > 0)
    {
      Iterator<DocumentType> ite = docTypes.iterator();
      while(ite.hasNext())
      {
        DocumentType docType = ite.next();
        documentTypeDesc.add(docType.getName());
      }
      return documentTypeDesc;
    }
    else
    {
      throw new ArchiveDelegateException("["+CLASS_NAME+"."+methodName+"] No document type can be found in GT. Doc Type is compulsary field in Archive By Document !");
    }
  }
  
  private List<String> getFolderList()
  {
    ArrayList<String> folderList = new ArrayList<String>();
    folderList.add(InboundFolder.FOLDER_NAME);
    folderList.add(ImportFolder.FOLDER_NAME);
    folderList.add(OutboundFolder.FOLDER_NAME);
    folderList.add(ExportFolder.FOLDER_NAME);
    return folderList;
  }
  
/**
   * Convert back to the GT 'UTC' timestamp
   * @param timeInMilli
   * @return
   */
  private Timestamp convertToTimeStamp(Long timeInMilli)
  {
    if(timeInMilli == null)
    {
      throw new NullPointerException("[GTArchiveDelegateMDBean.convertToTimeStamp] the given timeInMilli is null !");
    }
    _logger.logMessage("convertToTimeStamp", null, "After convert to GT UTC is "+new Timestamp(TimeUtil.localToUtc(timeInMilli))+" date is "+new Date(TimeUtil.localToUtc(timeInMilli)));
    return new Timestamp(TimeUtil.localToUtc(timeInMilli));
  }
  
  private IRNProcessDefManagerObj getProcessDefManager()
  throws ServiceLookupException
  {
    return (IRNProcessDefManagerObj)ServiceLocator.instance(
                                                            ServiceLocator.CLIENT_CONTEXT).getObj(
                                                                                                  IRNProcessDefManagerHome.class.getName(),
                                                                                                  IRNProcessDefManagerHome.class,
                                                                                                  new Object[0]);
  }
  
  private IDocumentManagerObj getDocumentMgr() throws ServiceLookupException
  {
    return (IDocumentManagerObj)ServiceLocator.instance(
                                                            ServiceLocator.CLIENT_CONTEXT).getObj(
                                                                                                  IDocumentManagerHome.class.getName(),
                                                                                                  IDocumentManagerHome.class,
                                                                                                  new Object[0]);
  }
  
  /**
   * It reorganizes the Gt archive status information to the one understantable in TM.
   * @param gtArchiveStatus
   * @return
   */
  private Hashtable localizeGTArchiveStatus(Hashtable gtArchiveStatus)
  {
    
    if(gtArchiveStatus == null || gtArchiveStatus.size() == 0)
    {
      return null;
    }
    String archiveOperation = (String)gtArchiveStatus.get(IDBArchiveConstants.ARCHIVE_OPERATION); //Archive or Restore
    String archiveType = (String)gtArchiveStatus.get(IDBArchiveConstants.ARCHIVE_TYPE);
    String archiveID = (String)gtArchiveStatus.get(IDBArchiveConstants.ARCHIVE_ID);
    Boolean archiveStatus = (Boolean)gtArchiveStatus.get(IDBArchiveConstants.ARCHIVE_STATUS);
    String archiveSummaryFile = (String) gtArchiveStatus.get(IDBArchiveConstants.ARCHIVE_SUMMARY_FILE);
    String archiveJobID = (String)gtArchiveStatus.get(IDBArchiveConstants.ARCHIVE_JOBS_ID);
    Boolean isArchiveOrphanRecord = (Boolean)gtArchiveStatus.get(IDBArchiveConstants.ARCHIVE_OPRHAN_RECORD);
    isArchiveOrphanRecord = isArchiveOrphanRecord == null? false : isArchiveOrphanRecord;
    Date fromStartDate = (Date)gtArchiveStatus.get(IDBArchiveConstants.ARCHIVE_FROM_START_DATE);
    Date toStartDate = (Date)gtArchiveStatus.get(IDBArchiveConstants.ARCHIVE_TO_START_DATE);
    
    _logger.logMessage("localizeGTArchiveStatus", null, "archiveOp: "+archiveOperation+" archiveType:"+archiveType+" archiveID: "+archiveID+" archiveStatus "+archiveStatus+" archiveSummaryFile: "+archiveSummaryFile+" archiveJobID: "+archiveJobID);
    
    Hashtable<String, Object> archiveStatusHT = new Hashtable<String, Object>();
    archiveStatusHT.put(IGTArchiveConstant.ARCHIVE_OPERATION, getTMArchiveOperation(archiveOperation));
    archiveStatusHT.put(IGTArchiveConstant.ARCHIVE_ID, archiveID);
    archiveStatusHT.put(IGTArchiveConstant.ARCHIVE_STATUS, archiveStatus);
    archiveStatusHT.put(IGTArchiveConstant.ARCHIVE_TYPE, getTMArchiveType(archiveType));
    archiveStatusHT.put(IGTArchiveConstant.ARCHIVE_JOBS_ID, archiveJobID);
    archiveStatusHT.put(IGTArchiveConstant.ARCHIVE_ORPHAN_RECORD, isArchiveOrphanRecord);
    
    if(fromStartDate != null && toStartDate != null)
    {
      Long fromStartDateInMilli = TimeUtil.utcToLocal(fromStartDate.getTime());
      Long toStartDateInMilli = TimeUtil.utcToLocal(toStartDate.getTime());
      
      archiveStatusHT.put(IGTArchiveConstant.FROM_START_DATE_TIME, fromStartDateInMilli);
      archiveStatusHT.put(IGTArchiveConstant.TO_START_DATE_TIME, toStartDateInMilli);
    }
    
    if(IGTArchiveConstant.ARCHIVE_OP_ARCHIVE.equals(archiveOperation))
    {
      archiveStatusHT.put(IGTArchiveConstant.ARCHIVE_SUMMARY_FILE, archiveSummaryFile);
    }
    return archiveStatusHT;
  }
  
  public void reportStatusToTM(Hashtable gtArchiveStatus) throws Exception
  {
    String methodName = "reportStatusToTM";
    Hashtable localizedArchiveStatus = localizeGTArchiveStatus(gtArchiveStatus);
    Properties jmsProps = getArchiveNotifyJmsProperties();
    Hashtable<String,String> msgProps = new Hashtable<String, String>();
    msgProps.put(SystemUtil.HOSTID_PROP_KEY, SystemUtil.getHostId());
    
    try
    {
      JmsRetrySender retrySender = new JmsRetrySender();
      
      if(! retrySender.isSendViaDefMode())////TWX to ensure the jms msg we sent out still can survive during the fail over process.
      {
        _logger.debugMessage(methodName, null, "Send via JmsRetrySender");
        
        JndiFinder finder = new JndiFinder(null, LoggerManager.getOneTimeInstance());
        finder.setJndiSuffix("_AT");
        retrySender.setJNDIFinder(finder);
        
        retrySender.retrySendWithPersist(localizedArchiveStatus, msgProps, IAuditTrailConstant.FAILED_JMS_CAT, jmsProps, true);
      }
      else
      {
        JmsSender sender = new JmsSender();
        sender.setSendProperties(jmsProps);
        sender.send(localizedArchiveStatus, msgProps);
      }
    }
		catch (JmsSenderException e)
		{
      _logger.logWarn(methodName, null, "Error in sending report status to archive notify queue", e);
      throw new Exception("JMS Error", e);
		}
  }
  
  private Properties getArchiveNotifyJmsProperties()
  {
    ConfigurationStore configStore = ConfigurationStore.getInstance();
    return configStore.getProperties(IGTATConstant.ARCHIVE_NOTIFY_CATEGORY);
  }
  
  private String getTMArchiveType(String archiveType)
  {
    if(IDBArchiveConstants.ARCHIVE_PROCESSINSTANCE.equals(archiveType))
    {
      return IGTArchiveConstant.ARCHIVE_TYPE_PI;
    }
    else if(IDBArchiveConstants.ARCHIVE_GRIDDOCUMENT.equals(archiveType))
    {
      return IGTArchiveConstant.ARCHIVE_TYPE_DOCUMENT;
    }
    else
    {
      return "";
    }
  }
  
  private String getTMArchiveOperation(String archiveOp)
  {
    if(IDBArchiveConstants.ARCHIVE_OP_ARCHIVE.equals(archiveOp))
    {
      return IGTArchiveConstant.ARCHIVE_OP_ARCHIVE;
    }
    else if(IDBArchiveConstants.ARCHIVE_OP_RESTORE.equals(archiveOp))
    {
      return IGTArchiveConstant.ARCHIVE_OP_RESTORE;
    }
    else
    {
      return "";
    }
  }
  
  public void sendToGTArchiveQueue(ArchiveDocumentEvent archiveEvent) throws Throwable
  {
    ArchiveHelper.sendToArchiveDest(archiveEvent);
    //ArchiveHelper.sendToGTArchiveDest(archiveEvent);
  }
  
  public void sendToGTArchiveQueue(RestoreDocumentEvent restoreEvent) throws Throwable
  {
    ArchiveHelper.sendToArchiveDest(restoreEvent);
    //ArchiveHelper.sendToGTArchiveDest(restoreEvent);
  }
  
  private ArrayList<String> convertStrToList(String str)
  {
    ArrayList<String> strList = new ArrayList<String>();
    if(str != null)
    {
      StringTokenizer st = new StringTokenizer(str, ";");
      while(st.hasMoreTokens())
      {
        strList.add(st.nextToken());
      }
    }
    return strList;
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IGTATConstant.LOG_TYPE, CLASS_NAME);
  }
}
