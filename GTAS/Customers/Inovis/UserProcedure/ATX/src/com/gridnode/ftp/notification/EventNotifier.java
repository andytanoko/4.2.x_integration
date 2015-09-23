/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EventNotifier.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 8, 2009    Tam Wei Xiang       Created
 */
package com.gridnode.ftp.notification;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.gtas.audit.common.model.BusinessDocument;
import com.gridnode.gtas.audit.common.model.EventInfo;
import com.gridnode.util.io.IOUtil;
import com.gridnode.util.jms.JmsRetrySender;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;
import com.inovis.userproc.util.log.ILoggerConstant;

/**
 * @author <developer name>
 * @version
 * @since
 */
public class EventNotifier
{
  private static final String DOC_DELIVERED_TO_BACKEND = "txmr.event.doc.delivered";
  private static final String DOC_DELIVERED_EVENT_NAME = "Document Delivered To Backend";
  private String _jmsPropsFile;
  
  public EventNotifier(String jmsPropsFile)
  {
    _jmsPropsFile = jmsPropsFile;
  }
  
  public void setJMSPropsFile(String jmsPropsFile)
  {
    _jmsPropsFile = jmsPropsFile;
  }
  
  public String getJMSPropsFile()
  {
    return _jmsPropsFile;
  }
  
  public void notifyDocumentDeliveredToBackend(String tracingID, String messageID, int ftpResponseCode, String filepath)
  {
    
    try
    {
    	Properties jmsProps = getJMSProps();
    	String eventName = getDocDeliveredToBackendEvent(jmsProps);
    	EventInfo info = createEventInfo(eventName, messageID, true, "", tracingID, ftpResponseCode);
    
	    File file = new File(filepath);
	    byte[] content = convertToByteArr(file);
	    String filename = content == null ? "" : file.getName();
	    
	    
	    BusinessDocument bizDoc = new BusinessDocument(convertToByteArr(file), filename, true);
	    AuditTrailData trailData = new AuditTrailData(info, false, new BusinessDocument[]{bizDoc});//NOTE: isIndependent flag is no longer in used
	    
	    sendToDestination(trailData, jmsProps);
    }
    catch(Exception ex)
    {
    	getLogger().logError(ILoggerConstant.FTP_TXMR_EVENT_TRIGGER_FAILED, "notifyDocumentDeliveredToBackend", null, "Error in triggering event to txmr.", ex);
    }
  }
  
  public void notifyDocumentToBackendFailed(String tracingID,String messageID, int ftpResponseCode, String errorMessage) 
  {
    try
    {
    	Properties jmsProps = getJMSProps();
    	String eventName = getDocDeliveredToBackendEvent(jmsProps);
	    EventInfo info = createEventInfo(eventName, messageID, false, errorMessage, tracingID, ftpResponseCode);
	    
	    BusinessDocument[] bizDocument = new BusinessDocument[0];
	    AuditTrailData trailData = new AuditTrailData(info, false, bizDocument);//NOTE: isIndependent flag is no longer in used
	    sendToDestination(trailData, jmsProps);
    }
    catch(Exception ex)
    {
    	getLogger().logError(ILoggerConstant.FTP_TXMR_EVENT_TRIGGER_FAILED, "notifyDocumentDeliveredToBackend", null, "Error in triggering event to txmr.", ex);
    }
  }
  
  private EventInfo createEventInfo(String eventName, String messageID, boolean isEventSuccess, String errorReason, String tracingID,
                                    int ftpResponseCode)
  {
    Date eventOccuredTime = new Date();
    String status = isEventSuccess ? IAuditTrailConstant.STATUS_SUCCESS : IAuditTrailConstant.STATUS_FAIL;
    String eventType = ""; //Empty, since previous TXMR event has already notified the TXMR the type of this event related document
    
    String flowTypeAddInfo = "FTP Response code:"+ftpResponseCode;
    String beID = ""; //leave empty, previous TXMR event has captured such info.
    return new EventInfo(eventName, eventOccuredTime, messageID, status, errorReason,
                         tracingID, eventType, beID, flowTypeAddInfo);
  }
  
  private byte[] convertToByteArr(File file)
  {
    
    ByteArrayOutputStream out = null;
    FileInputStream in = null;
    try
    {
      out = new ByteArrayOutputStream();
      in = new FileInputStream(file);
      byte[] buffer = new byte[512];
      int readSoFar = 0;
      
      while ( (readSoFar = (in.read(buffer))) > -1)
      {
        out.write(buffer, 0, readSoFar);
      }
      
      return out.toByteArray();
    }
    catch(Exception ex)
    {
      //TODO: Use log
    	getLogger().logError("", "convertToByteArr", null,"Error in converting to byte arr given file:"+file.getAbsolutePath()+". Error is: "+ex.getMessage(), ex);
      ex.printStackTrace();
      
      return null;
    }
    finally
    {
      try
      {
        if(out != null)
        {
          out.close();
        }
        if(in != null)
        {
          in.close();
        }
      }
      catch(Exception ex1) {}
    }
  }
  
  public void sendToDestination(Serializable info, Properties jmsProps) throws Exception
  {
      JmsRetrySender jmsSender = new JmsRetrySender(jmsProps);
      jmsSender.retrySend(info, null, "", jmsProps);
  }
  
  private Logger getLogger()
  {
	  return (LoggerManager.getOneTimeInstance().getLogger(ILoggerConstant.LOG_TYPE, "EventNotifier"));
  }
  
  private String getDocDeliveredToBackendEvent(Properties props)
  {
	  String docDeliveredToBackend = props.getProperty(DOC_DELIVERED_TO_BACKEND, DOC_DELIVERED_EVENT_NAME);
	  return docDeliveredToBackend;
  }
  
  private Properties getJMSProps() throws IOException
  {
    Properties jmsProps = new Properties();
    jmsProps.load(new FileInputStream(getJMSPropsFile()));
    return jmsProps;
  }
}
