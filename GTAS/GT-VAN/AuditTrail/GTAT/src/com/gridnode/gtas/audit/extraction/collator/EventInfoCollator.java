/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EventInfoCollator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 * Dec 26, 2006    Tam Wei Xiang       Map the docFlowType defined in GT into
 *                                     the expected format in OTC.
 * Feb 02, 2007    Tam Wei Xiang       Added in eventRemark in EventInfo 
 * Feb 06, 2007    Tam Wei Xiang       Added in new doc flow type: Reprocess Doc,
 *                                     Process Injection             
 * Mar 01, 2007    Tam Wei Xiang       Add prefix in EventInfo's eventRemark
 *                                     based on the EventName.                                                          
 */
package com.gridnode.gtas.audit.extraction.collator;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;

import com.gridnode.gtas.audit.extraction.exception.AuditInfoCollatorException;
import com.gridnode.gtas.audit.extraction.helper.AttachmentHelper;
import com.gridnode.gtas.audit.extraction.helper.BusinessDocumentHelper;
import com.gridnode.gtas.audit.extraction.helper.IGTATConstant;
import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.gtas.audit.common.model.BusinessDocument;
import com.gridnode.gtas.audit.common.model.EventInfo;
import com.gridnode.pdip.framework.notification.AbstractNotification;
import com.gridnode.pdip.framework.notification.DocumentFlowNotification;
import com.gridnode.pdip.framework.notification.EDocumentFlowType;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is used to collate the information from the DocumentFlowNotification and generate the
 * AuditTrailData based on the information.
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class EventInfoCollator extends AbstractInfoCollator
{
  private static EventInfoCollator _infoCollator = new EventInfoCollator();
  private String CLASS_NAME = "EventInfoCollator";
  
  private EventInfoCollator()
  {
  }
  
  public static EventInfoCollator getInstance()
  {
    return _infoCollator;
  }
  
  @Override
  public AuditTrailData gatherInfo(AbstractNotification notify) throws AuditInfoCollatorException
  {
    assertNotification(notify);
    DocumentFlowNotification notification = (DocumentFlowNotification)notify;
    
    try
    {
      return createAuditTrailData(notification);
    }
    catch(Exception ex)
    {
      throw new AuditInfoCollatorException("["+CLASS_NAME+".gatherInfo] Error in gathering info. ", ex);
    }
  }
  
  private AuditTrailData createAuditTrailData(DocumentFlowNotification notification) throws Exception
  {
    String method = "createAuditTrailData";
    Object[] param = new Object[]{notification};
    try
    {
      EventInfo eventInfo = createEventInfo(notification);
      BusinessDocument[] bizDocument = createBusinessDocument(notification);
      return new AuditTrailData(eventInfo, isIndependent(eventInfo) ,bizDocument);
    }
    catch(Exception ex)
    {
      getLogger().logWarn(method, param, "Error in creating AuditTrailData", ex);
      throw ex;
    }
  }
  
  private EventInfo createEventInfo(DocumentFlowNotification notification)
  {
    String eventName = getDocFlowType(notification.getDocFlowType());
    Date eventOccuredTime = notification.getEventOccurTime();
    String messageID = notification.getMessageID();
    String status = getStatus(notification.isEventSuccess());
    String errorReason = notification.getErrorReason();
    String tracingID = notification.getTracingID();
    
    String docType = notification.getMsgType();
    String eventType = "";
    
    if(docType!= null && ! "".equals(docType))
    {
      eventType = (IAuditTrailConstant.SIGNAL_MESSAGE_ACK.equals(docType) || IAuditTrailConstant.SIGNAL_MESSAGE_EXP.equals(docType)) ? 
                                                 IAuditTrailConstant.EVENT_TYPE_SIGNAL : IAuditTrailConstant.EVENT_TYPE_TRANS; 
    }
    
    //26 Dec 2006
    String flowTypeAddInfo = addPrefixOnDocFlowAddInfo(notification);
    String beID = notification.getBeID();
    return new EventInfo(eventName, eventOccuredTime, messageID, status, errorReason,
                         tracingID, eventType, beID, flowTypeAddInfo);
  }
  
  private String addPrefixOnDocFlowAddInfo(DocumentFlowNotification notification)
  {
    EDocumentFlowType docFlowType = notification.getDocFlowType();
    String prefix = "";
    
    switch(docFlowType)
    {
      case DOCUMENT_EXPORT:
        prefix = "Port Name:";
        break;
      case MAPPING_RULE:
        prefix = "Mapping Name:";
        break;
      case USER_PROCEDURE:
        prefix = "Procedure Name:";
        break;
      case DOCUMENT_DELIVERED:
        prefix = "HTTP Response:";
        break;
    }
    
    return prefix + notification.getDocFlowAddInfo();
  }
  
  /**
   * Create the BusinessDocument instance which wrap around the docContent as byte, the filename etc.
   * @param notification
   * @return
   * @throws Exception
   */
  private BusinessDocument[] createBusinessDocument(DocumentFlowNotification notification) throws Exception
  {
    //For sending the doc content, client can either include the content as the byte[] or a abs path to the actual file
    byte[] doc = notification.getDoc(); 
    File transactionDoc = getTransactionDoc(notification.getFilePath());
    boolean isRequiredPack = notification.isRequiredPack(); 
    
    ArrayList<BusinessDocument> businessDocList = new ArrayList<BusinessDocument>();
    
    //BusinessDocument for the payload
    BusinessDocument bizDocument = null;
    if(doc != null)
    {
      bizDocument = BusinessDocumentHelper.createBusinessDocument(doc, isRequiredPack);
    }
    else if(transactionDoc != null)
    {
      bizDocument = BusinessDocumentHelper.createBusinessDocument(transactionDoc, isRequiredPack);
    }
    
    if(bizDocument != null)
    {
      businessDocList.add(bizDocument);
    }
    
    //BusinessDocument for attachment. Attachment needed to be packed as MIME
    Collection<File> attFiles = AttachmentHelper.getAttachmentFiles(notification.getAttachmentUIDs());
    if(attFiles != null && attFiles.size() > 0)
    {
      Iterator<File> ite = attFiles.iterator();
      while(ite.hasNext())
      {
        bizDocument = BusinessDocumentHelper.createBusinessDocument(ite.next(), true);
        if(bizDocument != null)
        {
          businessDocList.add(bizDocument);
        }
      }
    }
    
    return businessDocList.toArray(new BusinessDocument[businessDocList.size()]);
  }
  
  /**
   * Get the transaction documents as represented by the filePath.
   * @param filePath The abs path that point to a File object.
   * @return
   */
  private File getTransactionDoc(String filePath) throws Exception
  {
    if(filePath == null || "".equals(filePath))
    {
      return null;
    }
    
    File transactionDoc = new File(filePath);
    if(! transactionDoc.exists())
    {
      throw new FileNotFoundException("The given file "+filePath+" cannot be found !");
    }
    
    if(! transactionDoc.isFile())
    {
      throw new IllegalArgumentException("The given file is a directory. Expecting physical file !");
    }
    
    return transactionDoc;
  }
  
  private void assertNotification(AbstractNotification notify)
  {
    if(! (notify instanceof DocumentFlowNotification))
    {
      throw new IllegalArgumentException("["+CLASS_NAME+".assertNotification] The given notification is not an instance of DocumentFlowNotification !");
    }
  }
  
  private String getDocFlowType(EDocumentFlowType docFlowType)
  {
    if(docFlowType == null)
    {
      throw new NullPointerException("["+CLASS_NAME+".getDocFlowType] docFlowType is null !!");
    }
    
    switch(docFlowType)
    {
      case DOCUMENT_RECEIVED:
        return IAuditTrailConstant.DOCUMENT_RECEIVED;
      case UNPACK_PAYLOAD:
        return IAuditTrailConstant.UNPACK_PAYLOAD;
      case DOCUMENT_DELIVERED:
        return IAuditTrailConstant.DOCUMENT_DELIVERED;
      case PACK_PAYLOAD:
        return IAuditTrailConstant.PACK_PAYLOAD;
      case DOCUMENT_ACKNOWLEDGED:
        return IAuditTrailConstant.DOCUMENT_ACKNOWLEDGED;
      case DOCUMENT_IMPORT:
        return IAuditTrailConstant.DOCUMENT_IMPORT;
      case OUTBOUND_PROCESSING_START:
        return IAuditTrailConstant.OUTBOUND_PROCESSING_START;
      case OUTBOUND_PROCESSING_END:
        return IAuditTrailConstant.OUTBOUND_PROCESSING_END;
      case EXPORT_PROCESSING_START:
        return IAuditTrailConstant.EXPORT_PROCESSING_START;
      case EXPORT_PROCESSING_END:
        return IAuditTrailConstant.EXPORT_PROCESSING_END;
      case MAPPING_RULE:
        return IAuditTrailConstant.MAPPING_RULE;
      case DOCUMENT_EXPORT:
        return IAuditTrailConstant.DOCUMENT_EXPORT;
      case USER_PROCEDURE:
        return IAuditTrailConstant.USER_PROCEDURE;
      case CHANNEL_CONNECTIVITY:
        return IAuditTrailConstant.CHANNEL_CONNECTIVITY;
      case REPROCESS_DOC:
        return IAuditTrailConstant.REPROCESS_DOC;
      case PROCESS_INJECTION:
        return IAuditTrailConstant.PROCESS_INJECTION;
      default:
        throw new IllegalArgumentException("["+CLASS_NAME+".getDocFlowType] docFlowType "+ docFlowType+" is not supported !!");
    }
  }
  
  /**
   * To indicate whether the EventInfo is independent from other AuditTrailData record (EG if the event
   * is DocumentReceived, it is dependent on the DocInfo).
   * @param info
   * @return
   */
  private boolean isIndependent(EventInfo info) throws AuditInfoCollatorException
  {
    String eventName = info.getEventName();
    if(eventName != null && ! "".equals(eventName))
    {
      if(eventName.equals(IAuditTrailConstant.DOCUMENT_RECEIVED) || eventName.equals(IAuditTrailConstant.DOCUMENT_DELIVERED) ||
          eventName.equals(IAuditTrailConstant.PACK_PAYLOAD))
      {
        getLogger().logMessage("isIndependent", null, "Event info "+info+" is dependent");
        return false;
      }
      else
      {
        return true;
      }
    }
    else
    {
      throw new AuditInfoCollatorException("The event info "+info+" is containing null or empty event name");
    }
  }
  
  private String getStatus(boolean eventStatus)
  {
    return eventStatus ? IAuditTrailConstant.STATUS_SUCCESS : IAuditTrailConstant.STATUS_FAIL;
  }
  
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IGTATConstant.LOG_TYPE, CLASS_NAME);
  }
}
