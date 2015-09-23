/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuditTrailArchiveListener.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 16, 2007    Tam Wei Xiang       Created
 * Mar 07 2007		Alain Ah Ming				Refined some catch clause
 * May 28  2007    Tam Wei Xiang       Support archive by customer
 * Jun 05  2007    Tam Wei Xiang       Performance tuning on archival
 * Dec 05, 2007   Tam Wei Xiang       To add in the checking of the redelivered jms msg.
 */
package com.gridnode.gtas.audit.archive.listener.ejb;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenContext;
import javax.ejb.MessageDrivenBean;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.gridnode.gtas.audit.archive.ArchiveHandler;
import com.gridnode.gtas.audit.archive.IArchiveConstant;
import com.gridnode.gtas.audit.archive.RestoreHandler;
import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.exceptions.ILogErrorCodes;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.jms.JMSRedeliveredHandler;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This MDBean will listen the archive/restore request, and delegate that request to appropriate handler.
 * It listen on the queue 'queue/gtvan/isat/archiveTrailDataQueue'
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class AuditTrailArchiveMDBean implements
                                      MessageDrivenBean,
                                      MessageListener
{
  /**
   * 
   */
  private static final long serialVersionUID = 4769336383994833815L;
  private static final String CLASS_NAME = "AuditTrailArchiveMDBean";
  private Logger _logger = null;
  private MessageDrivenContext _ctxt;
  
  public void onMessage(Message msg)
  {
    String methodName = "onMessage";
    _logger.logEntry(methodName, null);
    
    String archiveID = "";
    if(msg instanceof MapMessage)
    {
      try
      {
        if(msg.getJMSRedelivered() && ! JMSRedeliveredHandler.isEnabledJMSRedelivered())
        {
          _logger.logMessage(methodName, null, "Redelivered msg found, ignored it. Message: "+msg);
          return;
        }
        
        MapMessage mapMsg = (MapMessage)msg;
        String archiveAct = mapMsg.getString(IArchiveConstant.ARCHIVE_ACTIVITY);
        boolean isArchiveOrphanRecord = mapMsg.getBoolean(IArchiveConstant.ARCHIVE_ACT_ORPHAN);
        archiveID = mapMsg.getString(IArchiveConstant.ARCHIVE_ID);
        String jobID = mapMsg.getString(IArchiveConstant.ARCHIVE_JOBS_ID);
        
        boolean isTMArchiveSuccess = true;
        
        _logger.logMessage(methodName, null, "Receive archive/restore request from TM. Archive Activity is "+archiveAct);
        
        if(IArchiveConstant.ARCHIVE_ACT_ARCHIVE.equals(archiveAct))
        {
          Hashtable criteria = convertMapMsg(mapMsg);
          ArchiveHandler archiveHandler = new ArchiveHandler(criteria, archiveID, jobID);
          
          /* s node
          String archiveID = archiveHandler.generateArchiveID();
          archiveHandler.generateArchiveSummaryInfo(); */
          archiveHandler.generateArchiveSubJobProperties(jobID);
         
          try
          {
            archiveHandler.archiveTMGTRecord(criteria, archiveID);
          }
          catch(ArchiveTrailDataException ex)
          {
            isTMArchiveSuccess = false;
            throw ex;
          }
          catch(Throwable ex) 
          {
            isTMArchiveSuccess = false;
            throw ex;
          }
          finally
          {
            archiveHandler.updateTMArchiveStatus(isTMArchiveSuccess);
          }
        }
        else if(IArchiveConstant.ARCHIVE_ACT_RESTORE.equals(archiveAct))
        {
          String restoreSummary = mapMsg.getString(IArchiveConstant.RESTORE_SUMMARY_FILE);
          RestoreHandler restoreHandler = new RestoreHandler();
          restoreHandler.restoreTMGTArchive(restoreSummary);
        }
      }
			catch (JMSException e)
			{
        _logger.logError(ILogErrorCodes.AT_ARCHIVE_MDB_ONMESSAGE_ERROR,
                         methodName, null, "Failed to read request: Unable to retrieve JMS message information: "+e.getErrorCode()+"-"+e.getMessage(), e);
			}
			catch (Throwable e)
			{
        _logger.logError(ILogErrorCodes.AT_ARCHIVE_MDB_ONMESSAGE_ERROR,
                         methodName, null, "Failed to archive/restore audit trail: Error: "+e.getMessage()+ "Archive ID :"+archiveID,e);
			}
    }
    _logger.logExit(methodName, null);
  }
  
  private Hashtable convertMapMsg(MapMessage mapMsg) throws JMSException
  {
    Hashtable criteria = new Hashtable();
    Enumeration names = mapMsg.getMapNames();
    while(names.hasMoreElements())
    {
      String name = (String)names.nextElement();
      criteria.put(name, mapMsg.getObject(name));
    }
    return criteria;
  }
  
  public void ejbCreate() 
  {
    _logger = LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
  
  public void ejbRemove() throws EJBException
  {
  
  }

  public void setMessageDrivenContext(MessageDrivenContext ctxt) throws EJBException
  {
    _ctxt = ctxt;
  }

}
