/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentFlowNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 26, 2006    Tam Wei Xiang       Created
 * Dec 26, 2006    Tam Wei Xiang       change the docFlowType from string to
 *                                     EDocumentFlowType.
 *                                     New instance variable docFlowAddInfo that
 *                                     capture the additional info about the docFlowType.
 *                                     For example, we can include the port's name, mappingRule's name
 *                                     
 */
package com.gridnode.pdip.framework.notification;

import java.io.File;
import java.util.Date;
import java.util.List;


/**
 * This class will be used to indicate some activities have happened on the business
 * documents by the Channel, User Procedure, and Document module.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0 
 */
public class DocumentFlowNotification extends AbstractNotification
{
  private EDocumentFlowType _docFlowType;
  private Date _eventOccurTime;
  private String _messageID;
  private boolean _isEventSuccess;
  private String _errorReason;
  private String _tracingID;
  private String _msgType;
  private byte[] _doc;
  private String _beID;
  private List _attachmentUIDs;
  private String _filePath;
  private boolean _isRequiredPack; //indicate whether we need to pack the doc as the mime msg
  private String _docFlowAddInfo;  //additional info about the docFlowType.
                                   //For example, we can include the port's name, mappingRule's name
  
  public DocumentFlowNotification(EDocumentFlowType docFlowType, Date eventOccurTime, String messageID,
                                  boolean isEventSuccess, String errorReason, String tracingID,
                                  String docType, byte[] doc, String beID, List attachmentUIDs,
                                  String filePath, boolean isRequiredPack, String docFlowAddInfo)
  {
    setDocFlowType(docFlowType);
    setEventOccurTime(eventOccurTime);
    setMessageID(messageID);
    setEventSuccess(isEventSuccess);
    setErrorReason(errorReason);
    setTracingID(tracingID);
    setMsgType(docType);
    setDoc(doc);
    setBeID(beID);
    setAttachmentUIDs(attachmentUIDs);
    setFilePath(filePath);
    setIsRequiredPack(isRequiredPack);
    setDocFlowAddInfo(docFlowAddInfo);
  }
  
  public String getBeID()
  {
    return _beID;
  }
  
  public void setBeID(String _beid)
  {
    _beID = _beid;
  }

  public byte[] getDoc()
  {
    return _doc;
  }

  public void setDoc(byte[] _doc)
  {
    this._doc = _doc;
  }

  public EDocumentFlowType getDocFlowType()
  {
    return _docFlowType;
  }

  public void setDocFlowType(EDocumentFlowType flowType)
  {
    _docFlowType = flowType;
  }

  public String getMsgType()
  {
    return _msgType;
  }

  public void setMsgType(String type)
  {
    _msgType = type;
  }

  public String getErrorReason()
  {
    return _errorReason;
  }

  public void setErrorReason(String reason)
  {
    _errorReason = reason;
  }

  public Date getEventOccurTime()
  {
    return _eventOccurTime;
  }

  public void setEventOccurTime(Date occurTime)
  {
    _eventOccurTime = occurTime;
  }

  public String getMessageID()
  {
    return _messageID;
  }

  public void setMessageID(String _messageid)
  {
    _messageID = _messageid;
  }

  public boolean isEventSuccess()
  {
    return _isEventSuccess;
  }

  public void setEventSuccess(boolean eventSuccess)
  {
    _isEventSuccess = eventSuccess;
  }

  public String getTracingID()
  {
    return _tracingID;
  }

  public void setTracingID(String _tracingid)
  {
    _tracingID = _tracingid;
  }

  public List getAttachmentUIDs()
  {
    return _attachmentUIDs;
  }

  public void setAttachmentUIDs(List ds)
  {
    _attachmentUIDs = ds;
  }
  
  public String getFilePath()
  {
    return _filePath;
  }

  public void setFilePath(String path)
  {
    _filePath = path;
  }
  
  public boolean isRequiredPack()
  {
    return _isRequiredPack;
  }

  public void setIsRequiredPack(boolean requiredPack)
  {
    _isRequiredPack = requiredPack;
  }

  /* (non-Javadoc)
   * @see com.gridnode.pdip.framework.notification.INotification#getNotificationID()
   */
  public String getNotificationID()
  {
    return "DocumentFlow";
  }
  
  public String getDocFlowAddInfo()
  {
    return _docFlowAddInfo;
  }

  public void setDocFlowAddInfo(String flowAddInfo)
  {
    _docFlowAddInfo = flowAddInfo;
  }

  public String toString()
  {
    return "DocFlowType :"+_docFlowType+" EventOccurTime :"+_eventOccurTime+" MessageID :"+_messageID+"\n"+
           "isEventSuccess :"+_isEventSuccess+" ErrorReason :"+_errorReason+" TracingID :"+_tracingID+"\n"+
           "MsgType :"+_msgType+" Doc :"+ ((_doc==null) ? "" : new String(_doc))+" BE_ID :"+_beID+"\n"+
           "attachmentUID :"+ (_attachmentUIDs == null ? "" : _attachmentUIDs)+" filePath "+_filePath+"\n"+
           "isRequiredPack :"+_isRequiredPack+" docFlowAddInfo :"+_docFlowAddInfo;
  }
}
