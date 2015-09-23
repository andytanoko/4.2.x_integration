/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTArchiveDelegateMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 23, 2007    Tam Wei Xiang       Created
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 */
package com.gridnode.gtas.audit.archive.listeners.ejb;

import java.sql.Timestamp;
import java.util.Hashtable;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.gtas.audit.archive.helpers.GTArchiveDelegateHelper;
import com.gridnode.gtas.audit.common.IGTArchiveConstant;
import com.gridnode.gtas.audit.exceptions.ILogErrorCodes;
import com.gridnode.gtas.audit.extraction.helper.IGTATConstant;
import com.gridnode.gtas.events.dbarchive.ArchiveDocumentEvent;
import com.gridnode.gtas.events.dbarchive.RestoreDocumentEvent;
import com.gridnode.gtas.server.dbarchive.helpers.IDBArchiveConstants;
import com.gridnode.util.jms.JMSRedeliveredHandler;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class will listen to the Queue queue/gtvan/gtat/archiveQueue. The request from TM to perform archive/restore
 * will be sent to this queue. The archive/restore status of Gt will also send to this q.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class GTArchiveDelegateMDBean implements
                                    MessageListener,
                                    MessageDrivenBean
{
  /**
   * 
   */
  private static final long serialVersionUID = 3710920755826896555L;
  
  private Logger _logger = null;
  private MessageDrivenContext _msgCtxt = null;
  private static final String CLASS_NAME = "GTArchiveDelegateMDBean";
  
  public void onMessage(Message msg)
  {
    String methodName=  "onMessage";
    _logger.logEntry(methodName,null);
    
    try
    {
      if(msg.getJMSRedelivered() && ! JMSRedeliveredHandler.isEnabledJMSRedelivered())
      {
        _logger.logMessage(methodName, null, "Redelivered msg found, ignored it. Message: "+msg);
        return;
      }
    }
    catch(JMSException ex)
    {
      _logger.logError(ILogErrorCodes.AT_GTARCHIVE_MDB_ONMESSAGE_ERROR,
                       methodName, null, "Failed to read the archive request: "+ex.getMessage(), ex);
    }
    
    GTArchiveDelegateHelper delegateHelper = new GTArchiveDelegateHelper();
    if(msg instanceof MapMessage) //receive the archive/restore request from TM
    {
      _logger.logMessage(methodName, null, "Receive the archive/restore request from TM");
      MapMessage mapMsg = (MapMessage)msg;
      
      String archiveOp = "";
      try
      {
        archiveOp = mapMsg.getString(IGTArchiveConstant.ARCHIVE_OPERATION);
        
        if(IGTArchiveConstant.ARCHIVE_OP_ARCHIVE.equals(archiveOp))
        {
          deliveredArchiveEvent(mapMsg);
        }
        else if(IGTArchiveConstant.ARCHIVE_OP_RESTORE.equals(archiveOp))
        {
          _logger.logMessage(methodName, null, "creating RestoreDocumentEvent....");
          deliveredRestoreEvent(mapMsg);
        }
      }
      catch(JMSException e)
      {
        _logger.logError(ILogErrorCodes.AT_GTARCHIVE_MDB_ONMESSAGE_ERROR,
                         methodName, null, "Failed to read the archive request: "+e.getMessage(), e);      	
      }
      catch(Exception ex)
      {
        _logger.logError(ILogErrorCodes.AT_GTARCHIVE_MDB_ONMESSAGE_ERROR,
                         methodName, null, "Failed to handle the archive request. Archive Operation is "+archiveOp+". "+ex.getMessage(), ex);
      }
    }
    else if(msg instanceof ObjectMessage) //receive the archive status from GT
    {
      _logger.logMessage(methodName, null, "Receive the archive/restore status from GT");
      
      try
      {
        ObjectMessage objMsg = (ObjectMessage)msg;
        Hashtable archiveStatusMap = (Hashtable)objMsg.getObject();
        String archiveType = (String)archiveStatusMap.get(IDBArchiveConstants.ARCHIVE_TYPE);
        String archiveID = (String)archiveStatusMap.get(IDBArchiveConstants.ARCHIVE_ID);
        String archiveSummaryFile = (String) archiveStatusMap.get(IDBArchiveConstants.ARCHIVE_SUMMARY_FILE);
        String archiveJobID = (String)archiveStatusMap.get(IDBArchiveConstants.ARCHIVE_JOBS_ID);
        String archiveOp = (String)archiveStatusMap.get(IDBArchiveConstants.ARCHIVE_OPERATION);
        Boolean status = (Boolean)archiveStatusMap.get(IDBArchiveConstants.ARCHIVE_STATUS);
        
        _logger.logMessage(methodName, null, "Archive/Restore status is "+archiveStatusMap);
      
        try
        {
          delegateHelper.reportStatusToTM(archiveStatusMap);
        }
        catch(Exception ex)
        {
          if(IDBArchiveConstants.ARCHIVE_OP_ARCHIVE.equals(archiveOp))
          {
            _logger.logError(ILogErrorCodes.AT_ARCHIVE_EVENT_DELEGATE_ERROR,
                         methodName, null, "Error in delegating the GT archive status to TM. ArchiveID: "+archiveID+" jobID: "+archiveJobID+" Archive type: "+archiveType+" Archive summary file: "+archiveSummaryFile+" isArchiveSuccess: "+status, ex);
          }
          else
          {
            _logger.logError(ILogErrorCodes.AT_ARCHIVE_EVENT_DELEGATE_ERROR, methodName, null, "Error in delegating the Restore status to TM. Archive ID: "+archiveID+" isRestoreSuccess: "+status ,ex);
          }
        }
      }
      catch(JMSException e)
      {
        _logger.logError(ILogErrorCodes.AT_GTARCHIVE_MDB_ONMESSAGE_ERROR,
                         methodName, null, "Failed to read the archive request: "+e.getMessage(), e);       
      }
    }
    _logger.logExit(methodName,null);
  }
  
  /**
   * Delegate the archive request to GT.
   * @param msg
   * @throws JMSException if we failed to retrieve msg properties from the msg
   */
  private void deliveredArchiveEvent(MapMessage msg) throws JMSException
  {
    String mn = "deliveredArchiveEvent";
    GTArchiveDelegateHelper delegateHelper = new GTArchiveDelegateHelper();
    
    String archiveID = msg.getString(IGTArchiveConstant.ARCHIVE_ID);
    String archiveJobID = msg.getString(IGTArchiveConstant.ARCHIVE_JOBS_ID);
    String archiveType = msg.getString(IGTArchiveConstant.ARCHIVE_TYPE);
    
    try
    {
      _logger.logMessage(mn, null, "Delegating archive job to GT: archiveID "+archiveID+" archiveJobID: "+ archiveJobID+" archiveType: "+archiveType);
      ArchiveDocumentEvent archiveEvent = delegateHelper.createArchiveDocumentEvent(msg);
      delegateHelper.sendToGTArchiveQueue(archiveEvent);
    }
    catch(Throwable ex)
    {
      _logger.logError(ILogErrorCodes.AT_ARCHIVE_EVENT_DELEGATE_ERROR, mn, null, "Error in delegating the archive event to GT. ArchiveID: "+archiveID+" jobID: "+archiveJobID+" Archive type: "+archiveType+" isArchiveSuccess: "+Boolean.FALSE.toString(), ex);
    }
  }
  
  private void deliveredRestoreEvent(MapMessage mapMsg) throws JMSException
  {
    String mn = "deliveredRestoreEvent";
    GTArchiveDelegateHelper delegateHelper = new GTArchiveDelegateHelper();
    
    String summaryFileToBeRestored = mapMsg.getString(IGTArchiveConstant.ARCHIVE_SUMMARY_FILE);
    String archiveID = mapMsg.getString(IGTArchiveConstant.ARCHIVE_ID);
    String restoreType = mapMsg.getString(IGTArchiveConstant.ARCHIVE_TYPE);
    
    try
    {
      _logger.logMessage(mn, null, "Delegating restore job to GT: archiveID "+archiveID+" restoreType: "+restoreType+" summaryFileToBeRestored: "+summaryFileToBeRestored);
      RestoreDocumentEvent restoreEvent = delegateHelper.createRestoreDocumentEvent(mapMsg);
      delegateHelper.sendToGTArchiveQueue(restoreEvent);
    }
    catch(Throwable ex)
    {
      _logger.logError(ILogErrorCodes.AT_ARCHIVE_EVENT_DELEGATE_ERROR, mn, null, "Error in delegating the restore event to GT. archiveID: "+ archiveID+" restoreType: "+restoreType+" summaryFile to be restored: "+summaryFileToBeRestored, ex);
    }
  }
  
  public void ejbRemove() throws EJBException
  {
  }

  public void setMessageDrivenContext(MessageDrivenContext arg0) throws EJBException
  {
  }
  
  public void ejbCreate()
  {
    _logger = getLogger();
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IGTATConstant.LOG_TYPE, CLASS_NAME);
  }
}
