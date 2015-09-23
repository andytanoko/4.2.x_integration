/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DBArchiveListener.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Sep 15, 2004 			Mahesh             	Created
 * Oct 5, 2005        Lim Soon Hsiung     Trigger eStore alert upon eStore failure 
 * Apr 04, 2006       Tam Wei Xiang       New archive alert added to handle the case
 *                                        where archive completed.   
 * Apr 05, 2006       Tam Wei Xiang       change the archive fail alert to a new one
 * Aug 31, 2006       Tam Wei Xiang       Merge from ESTORE stream.  
 * Sep 14, 2006       Tam Wei Xiang       Added in new archive criteria : isEnableSearchArchive,
 *                                        isEnableRestoreArchive, archiveByPartner   
 * Feb 07 2007        Neo Sok Lay         Remove startListening().     
 * Feb 26 2007        Tam Wei Xiang       Send back the Archive/Restore status to TM module.
 * May 18 2007        Tam Wei Xiang       Handle archive by customer    .
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.                                                                 
 * Apr 11 2008        Tam Wei Xiang       The archive ID should set to "" if under normal
 *                                        GT.                                                                     
 */
package com.gridnode.gtas.server.dbarchive.listener;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gtas.events.dbarchive.ArchiveDocumentEvent;
import com.gridnode.gtas.events.dbarchive.RestoreDocumentEvent;
import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.dbarchive.helpers.*;
import com.gridnode.gtas.server.docalert.helpers.Logger;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.pdip.app.alert.providers.DefaultProviderList;
import com.gridnode.pdip.app.alert.providers.ExceptionDetails;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.TimeUtil;

public class DBArchiveListener implements  MessageListener
{
  
  private File _summaryFile = null;
  /*
  public static void startListening() throws Throwable
  {
    String topicName = ArchiveHelper.getProperty(IDBArchivePathConfig.ARCHIVE_TOPIC);
    TopicConnection tconn =(TopicConnection)JMSSender.getConnection(IFrameworkConfig.FRAMEWORK_JMS_CONFIG,topicName,JMSSender.TOPIC_TYPE);
    Topic topic = (Topic)JMSSender.getDestination(IFrameworkConfig.FRAMEWORK_JMS_CONFIG,topicName);
    TopicSession subSession  = tconn.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
    TopicSubscriber subscriber = subSession.createSubscriber(topic);
    subscriber.setMessageListener(new DBArchiveListener());
    tconn.start();
  } */ 

  public void onMessage(Message msg)
  {
    Logger.log("[DBArchiveListener.onMessage] Enter");
    IEvent event = null;
    boolean isArchiveSuccess = true;
    
    try
    {
      
      if(msg.getJMSRedelivered() && ! JMSRedeliveredHandler.isEnabledJMSRedelivered())
      {
        Logger.log("[DBArchiveListener.onMessage] Redelivered msg found, ignored it. Message: "+msg);
        return;
      }
      
      event = (IEvent)((ObjectMessage)msg).getObject();
      
	    if(event != null && event instanceof ArchiveDocumentEvent)
	    {
	      archive((ArchiveDocumentEvent)event);
	    }
	    else if(event != null && event instanceof RestoreDocumentEvent)
	    {
	      restore((RestoreDocumentEvent)event);
	    }
	    else 
	    {
	    	Logger.warn("[DBArchiveListener.onMessage] Unknown Event, event type");
	    }
    }
    catch(Throwable th)
    {
      isArchiveSuccess = false; 
      Logger.error(ILogErrorCodes.GT_DB_ARCHIVE_LISTENER,
                   "[DBArchiveListener.onMessage] Error, event="+event+" : "+th.getMessage(),th);
    }
    finally
    {
//    TWX send back the archive/restore status to the TM module
      reportArchiveStatus(event, isArchiveSuccess);
    }
  }

  private void archive(ArchiveDocumentEvent event) throws Throwable
  {
  	Logger.log("[DBArchiveListener.archive] event.isEnableSearchArchived()  = " + event.isEnableSearchArchived()+" event.isEnableRestoreArchived = "+event.isEnableRestoreArchived());
  	//TWX 14092006 Determine option that user has chosen for enabling searchArchive, restoreArchive
    Boolean isEnableSearchArchive = event.isEnableSearchArchived() == null ? false : event.isEnableSearchArchived();
    Boolean isEnableRestoreArchive = event.isEnableRestoreArchived() == null ? false : event.isEnableRestoreArchived();
  
    Logger.log("[DBArchiveListener.archive] Enter, archiveType="+event.getArchiveType());
    String archiveType       = event.getArchiveType();
    String archiveName       = event.getArchiveName();
    String archiveDesc       = event.getArchiveDescription();
    Timestamp fromStartTime  = event.getFromStartTime();
    Timestamp toStartTime    = event.getToStartTime();
    
    List<String> partnerID   = event.getPartnerIDForArchive();
    List<String> customerID  = event.getCustomerIDForArchive();
    boolean isArchiveOrphanRecord = event.isArchiveOrphanRecord() == null ? false : event.isArchiveOrphanRecord();
    String archiveID = event.getArchiveID() == null? "": event.getArchiveID(); //11 Apr 2008: The archive ID will set to "" if in normal GT.
    
    String pathKey =  IDBArchivePathConfig.PATH_ARCHIVE;
    Logger.debug("[DBArchiveListener.archive] archiveType="+archiveType+", archiveName="+archiveName);

    File summaryFile = null;
    
    DefaultProviderList providerList = new DefaultProviderList();
    DbArchive dbArchive = new DbArchive();
    dbArchive.setArchiveName(archiveName);
    dbArchive.setDescription(archiveDesc);
    dbArchive.setArchiveType(archiveType);
    dbArchive.setArchiveID(archiveID);
    
    //TWX: 04 Mar 2006 new archive complete alert added and it will replace the existing 
    //     IDBArchiveConstants.ARCHIVE_ALERT_NAME.
    String archiveAlertType = IDBArchiveConstants.ARCHIVE_COMPLETE_ALERT;
    ArchiveAlertProvider provider = null;
    try
    {
      dbArchive.createArchive();
      if(archiveType.equals(IDBArchiveConstants.ARCHIVE_PROCESSINSTANCE))
      {
        Boolean includeInComplete = event.getIncludeIncompleteProcesses();
        List processDefNameList   = event.getProcessDefNameList();
        dbArchive.archiveProcessInstance(fromStartTime,toStartTime,includeInComplete,processDefNameList, isEnableSearchArchive,
                                          isEnableRestoreArchive,  partnerID, customerID, isArchiveOrphanRecord);
      }
      else if(archiveType.equals(IDBArchiveConstants.ARCHIVE_GRIDDOCUMENT))
      {
        List folderList  = event.getFolderList();
        List docTypeList =  event.getDocumentTypeList();
        dbArchive.archiveGridDoc(fromStartTime,toStartTime,folderList,docTypeList,  isEnableSearchArchive,  isEnableRestoreArchive,
                                  partnerID, customerID, isArchiveOrphanRecord);
      }
      else throw new Exception("No filter defined for archivetype = "+archiveType+", archiveName="+archiveName);       
    }
    catch(Throwable th)
    {
      Logger.warn("[DBArchiveListener.archive] Error in archive",th);
      archiveAlertType = IDBArchiveConstants.ARCHIVE_FAILED_ALERT; //TWX: 05 Apr 2006 new archive failed alert
      provider = getArchiveAlertProvider(archiveType, event);
      providerList.addProvider(new ExceptionDetails(th));
      throw th;
    }
    finally
    {
    	//TWX 07092006 If error occured, the docs we have archived so far will still be keep and generated
    	if(provider == null && archiveType.equals(IDBArchiveConstants.ARCHIVE_PROCESSINSTANCE))
    	{
    		provider = dbArchive.getArchiveByProcessProvider();
    	}
    	else if(provider == null && archiveType.equals(IDBArchiveConstants.ARCHIVE_GRIDDOCUMENT))
    	{
    		provider = dbArchive.getArchiveByDocumentProvider();
    	}
    	
    	if(provider != null)
    	{
    		providerList.addProvider(provider);
    	}
    	
    	String summaryFileName = "";
    	try
    	{
    		summaryFileName = dbArchive.saveResultTo(pathKey);
    		summaryFile = FileUtil.getFile(pathKey,summaryFileName);
    	}
    	catch(Exception ex)
    	{
    		Logger.warn("[DBArchiveListener.archive] error in generating the summary file. ", ex);
    	}
    	Logger.log("[DBArchiveListener.archive] archiveing is completed, summaryFileName="+summaryFileName);
    	IAlertManagerObj alertMgr = ArchiveHelper.getAlertManager();
      alertMgr.triggerAlert(archiveAlertType,providerList,
        		                  summaryFile == null? null :summaryFile.getAbsolutePath());
        
      dbArchive.closeArchive();
      _summaryFile = summaryFile;
      Logger.log("[DBArchiveListener.archive] Exit");
    }
  }

  private void restore(RestoreDocumentEvent event) throws Throwable
  {
    Logger.log("[DBArchiveListener.restore] Enter");
    String  restoreFile = event.getArhiveFile();
    Logger.log("[DBArchiveListener.restore] restoreFile="+restoreFile);
    DefaultProviderList providerList = new DefaultProviderList();
    File restoreSummaryFile = null;
    try
    {
      DbArchive dbArchive =new DbArchive();
      dbArchive.restoreFromSummary(restoreFile);
      restoreSummaryFile = dbArchive.generateRestoreSummaryFile();
      providerList.addProvider(new ArchiveAlertProvider("Successful"));
      Logger.log("[DBArchiveListener.restore] restoring is Successful");
    }
    catch(Throwable th)
    {
      Logger.warn("[DBArchiveListener.restore] Error in restore",th);
      providerList.addProvider(new ArchiveAlertProvider("Failed"));
      providerList.addProvider(new ExceptionDetails(th));
      throw th;
    }
    finally
    {
      IAlertManagerObj alertMgr = ArchiveHelper.getAlertManager();
      String restoreSummaryFileName = restoreFile;
      if(restoreSummaryFile!=null)
        restoreSummaryFileName=restoreSummaryFile.getAbsolutePath();
      alertMgr.triggerAlert(IDBArchiveConstants.RESTORE_ALERT_NAME,providerList,restoreSummaryFileName);
      Logger.log("[DBArchiveListener.restore] Exit");
    }
  }
  
  //TWX 5 Apr 2006: Provider for the case where archive failed
  private ArchiveAlertProvider getArchiveAlertProvider(String archiveType, ArchiveDocumentEvent event)
  {
//  all date time in UTC time.
  	Date archiveFailedTime = new Date(TimeUtil.localToUtc(Calendar.getInstance().getTimeInMillis())); //the actual day, hour, minute, second
                                                                                                      //have been converted to UTC time
  	Timestamp fromStartTime   = event.getFromStartTime();
    Timestamp toStartTime     = event.getToStartTime();
    List<String> partnerIDForArchive = event.getPartnerIDForArchive();
    Boolean isEnableSearchArchive = event.isEnableSearchArchived() == null ? false : event.isEnableSearchArchived();
    Boolean isEnableRestoreArchive = event.isEnableRestoreArchived() == null ? false : event.isEnableRestoreArchived(); 
    Boolean isArchiveOrphanRecord = event.isArchiveOrphanRecord();
    List<String> beIDs = event.getCustomerIDForArchive();
    String archiveID = event.getArchiveID();
    
  	if(archiveType.equals(IDBArchiveConstants.ARCHIVE_PROCESSINSTANCE))
  	{
  		Boolean includeInComplete = event.getIncludeIncompleteProcesses();
      List processDefNameList   = event.getProcessDefNameList();
      
      return new ArchiveAlertProvider(Boolean.TRUE, archiveFailedTime, 
      		                            new Date(fromStartTime.getTime()), new Date(toStartTime.getTime()),
      		                            processDefNameList, includeInComplete, "", isEnableRestoreArchive, isEnableSearchArchive,
      		                            partnerIDForArchive, beIDs, archiveID, isArchiveOrphanRecord);
  	}
  	else if(archiveType.equals(IDBArchiveConstants.ARCHIVE_GRIDDOCUMENT))
    {
  		List folderList  = event.getFolderList();
      List docTypeList = event.getDocumentTypeList();
      return new ArchiveAlertProvider(Boolean.FALSE, archiveFailedTime,
      		                            new Date(fromStartTime.getTime()), new Date(toStartTime.getTime()),
      		                            docTypeList, folderList, "", isEnableRestoreArchive, isEnableSearchArchive,
      		                            partnerIDForArchive, beIDs, archiveID, isArchiveOrphanRecord);
    }
  	else
  	{
  		Logger.warn("[DBArchiveListener.getArchiveAlertProvider] No filter defined for archivetype = "+archiveType); 
  		return null;
  	}
  }
  
  private void reportArchiveStatus(IEvent event, boolean isSuccess)
  {
    String archiveOperation = "";
    String archiveType = "";
    String archiveID = "";
    String archiveSummaryFile = "";
    String archiveJobID = "";
    boolean isArchiveOrphanRecord = false; 
    Date archiveStartDate = null;
    Date archiveEndDate = null;
    
    if(event == null)
    {
      return;
    }
    if(event instanceof ArchiveDocumentEvent)
    {
      ArchiveDocumentEvent archiveEvent = ((ArchiveDocumentEvent)event);
      archiveType = archiveEvent.getArchiveType();
      archiveOperation = IDBArchiveConstants.ARCHIVE_OP_ARCHIVE;
      archiveID = archiveEvent.getArchiveID();
      archiveJobID = archiveEvent.getArchiveJobID();
      isArchiveOrphanRecord = archiveEvent.isArchiveOrphanRecord() == null ? false : archiveEvent.isArchiveOrphanRecord();
      archiveSummaryFile = _summaryFile != null ? _summaryFile.getName() : "";
      
      archiveStartDate = archiveEvent.getFromStartTime(); //make sure current node will still stick to the date range
      archiveEndDate = archiveEvent.getToStartTime();
    }
    else if(event instanceof RestoreDocumentEvent)
    {
      RestoreDocumentEvent restoreEvent = ((RestoreDocumentEvent)event);
      archiveOperation = IDBArchiveConstants.ARCHIVE_OP_RESTORE;
      archiveID = restoreEvent.getArchiveID();
      archiveType = restoreEvent.getRestoreType();
    }
    else 
    {
      return;
    }
    
    if("".equals(archiveID) || archiveID == null) //The archive/restore operation is triggered from GT UI, the archive status for TM is not required.
    {
      Logger.log("[DBArchiveListener.reportArchiveStatus] No report status is required. The archive/restore request is through GT UI.");
      return;
    }
    
    Hashtable archiveStatusMap = new Hashtable();
    archiveStatusMap.put(IDBArchiveConstants.ARCHIVE_TYPE, archiveType);
    archiveStatusMap.put(IDBArchiveConstants.ARCHIVE_OPERATION, archiveOperation);
    archiveStatusMap.put(IDBArchiveConstants.ARCHIVE_ID, archiveID);
    archiveStatusMap.put(IDBArchiveConstants.ARCHIVE_STATUS, isSuccess);
    archiveStatusMap.put(IDBArchiveConstants.ARCHIVE_SUMMARY_FILE, archiveSummaryFile);
    archiveStatusMap.put(IDBArchiveConstants.ARCHIVE_JOBS_ID, archiveJobID);
    archiveStatusMap.put(IDBArchiveConstants.ARCHIVE_OPRHAN_RECORD, isArchiveOrphanRecord);
    
    if(archiveStartDate != null && archiveEndDate != null)
    {
      archiveStatusMap.put(IDBArchiveConstants.ARCHIVE_FROM_START_DATE, archiveStartDate);
      archiveStatusMap.put(IDBArchiveConstants.ARCHIVE_TO_START_DATE, archiveEndDate);
    }
    
    Logger.log("[DBArchiveListener.reportArchiveStatus] report status to TM .... report status is "+archiveStatusMap);
    
    try
    {
      ArchiveHelper.sendToTMArchiveDelegateDest(archiveStatusMap);
    }
    catch(Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_ARCHIVE_EVENT_DELIVERED_FAILURE, "Error in reporting the archive/restore status to TM plug-in. Archive Status Map: "+archiveStatusMap+"  Error: "+ex.getMessage(), ex);
    }
  }
}
