/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditTrailNotifyArchiveMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 26, 2007    Tam Wei Xiang       Created
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 */
package com.gridnode.gtas.audit.archive.listener.ejb;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.gridnode.gtas.audit.archive.ArchiveHandler;
import com.gridnode.gtas.audit.archive.RestoreHandler;
import com.gridnode.gtas.audit.common.IGTArchiveConstant;
import com.gridnode.gtas.audit.exceptions.ILogErrorCodes;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.jms.JMSRedeliveredHandler;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This MD Bean is listening to the archive/restore status from GT and delegate the status
 * to the Archive/Restore handler to determine what will be the next step to perform.
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public class AuditTrailNotifyArchiveMDBean implements MessageListener, MessageDrivenBean
{
  /**
   * 
   */
  private static final long serialVersionUID = -4307953843883975000L;
  private MessageDrivenContext _msgCtxt = null;
  private Logger _logger = null;
  private static final String CLASS_NAME = "AuditTrailNotifyArchiveMDBean";
  
  public void onMessage(Message msg)
  {
    String methodName = "onMessage";
    _logger.logEntry(methodName, null);
    
    try
    {
      if(msg.getJMSRedelivered() && ! JMSRedeliveredHandler.isEnabledJMSRedelivered())
      {
        _logger.logMessage(methodName, null, "Redelivered msg found, ignored it. Message: "+msg);
        return;
      }
      
      if(msg instanceof MapMessage)
      {
        MapMessage mapMsg = (MapMessage)msg;
        Hashtable archiveStatus = convertMapMsgToHashtable(mapMsg);
        String archiveOp = (String)archiveStatus.get(IGTArchiveConstant.ARCHIVE_OPERATION);
        
        if(IGTArchiveConstant.ARCHIVE_OP_ARCHIVE.equals(archiveOp))
        {
          delegateGTArchiveStatus(archiveStatus);
        }
        else if(IGTArchiveConstant.ARCHIVE_OP_RESTORE.equals(archiveOp))
        {
          _logger.logMessage(methodName, null, "Receiving the GT Resetore status");
          RestoreHandler restoreHandler = new RestoreHandler();
          restoreHandler.handleRestoreStatus(archiveStatus);
        }
      }
    }
    catch(JMSException ex)
    {
      _logger.logError(ILogErrorCodes.AT_NOTIFY_MDB_ONMESSAGE_ERROR,
                       methodName, null, "Failed to read request message. Error: "+ex.getMessage(), ex);
    }
    catch(Exception ex)
    {
      _logger.logError(ILogErrorCodes.AT_NOTIFY_MDB_ONMESSAGE_ERROR,
                       methodName, null, "Failed to handle the archive/restore status from TM plug-in. Error: "+ex.getMessage(), ex);
    }
  }
  
  /**
   * Delegate the GT archive status to the archive handler
   * @param archiveStatus
   */
  private void delegateGTArchiveStatus(Hashtable archiveStatus)
  {
    String mn = "delegateGTArchiveStatus";
    _logger.logMessage(mn, null, "Receiving the GT Archive status. "+archiveStatus);
    
    String archiveID = (String)archiveStatus.get(IGTArchiveConstant.ARCHIVE_ID);
    String jobID = (String)archiveStatus.get(IGTArchiveConstant.ARCHIVE_JOBS_ID);
    Boolean isArchiveSuccess = (Boolean)archiveStatus.get(IGTArchiveConstant.ARCHIVE_STATUS);
    String summaryFilename = (String)archiveStatus.get(IGTArchiveConstant.ARCHIVE_SUMMARY_FILE);
    String archiveType = (String)archiveStatus.get(IGTArchiveConstant.ARCHIVE_TYPE);
    
    ArchiveHandler archiveHandler = new ArchiveHandler();
    try
    {
      archiveHandler.handleGTArchiveStatus(archiveStatus);
    }
    catch(Exception ex)
    {
      _logger.logError(ILogErrorCodes.AT_ARCHIVE_STATUS_UPDATE_ERROR, mn, null, "ArchiveID: "+archiveID+" jobID: "+jobID+" Archive type: "+archiveType+" Archive summary file: "+summaryFilename+" GT Archive status: "+isArchiveSuccess, ex);
    }
  }
  
  private Hashtable convertMapMsgToHashtable(MapMessage mapMsg) throws JMSException
  {
    Hashtable achiveStatus = new Hashtable();
    Enumeration names = mapMsg.getMapNames();
    while(names.hasMoreElements())
    {
      String name = (String)names.nextElement();
      achiveStatus.put(name, mapMsg.getObject(name));
    }
    return achiveStatus;
  }
  
  public void ejbCreate()
  {
    _logger = getLogger();
  }
  
  public void ejbRemove() throws EJBException
  {
     
  }

  public void setMessageDrivenContext(MessageDrivenContext ctxt) throws EJBException
  {
    _msgCtxt = ctxt;
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
