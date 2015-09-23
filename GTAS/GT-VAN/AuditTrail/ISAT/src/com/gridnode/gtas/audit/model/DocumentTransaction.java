/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentTransaction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 23, 2006    Tam Wei Xiang       Created
 * Dec 26, 2006    Tam Wei Xiang       Added new variable _isSignal
 * Jan 10, 2007    Tam Wei Xiang       Added new variable _isRetry
 * Jan 18, 2007    Tam Wei Xiang       ADded userTrackingID
 */
package com.gridnode.gtas.audit.model;

import java.util.Date;

import com.gridnode.util.db.AbstractPersistable;

/**
 * The instance of this class has a direct mapping on one of the record in the DocumentTransaction
 *  table
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 * 
 * @hibernate.class table = "`isat_document_transaction`"
 * 
 * @hibernate.query name = "getDocTrans"
 *      query = "FROM DocumentTransaction dt WHERE dt.tracingID = :tracingID AND dt.documentDirection = :direction"
 * @hibernate.query name = "getTranDocsByTraceIDAndMsgID"
 *      query = "FROM DocumentTransaction dt WHERE dt.tracingID = :tracingID AND dt.messageID = :messageID"
 * @hibernate.query
 *   query = "SELECT COUNT (*) FROM DocumentTransaction dt WHERE dt.tracingID = :tracingID AND dt.documentDirection = :direction AND dt.documentType != :docTypeSigAck AND dt.documentType != :docTypeSigExp AND dt.processInstanceUID= :processInstanceUID"
 *   name = "getActionDocTransCount"      
 * @hibernate.query
 *   query = "FROM DocumentTransaction dt WHERE dt.tracingID = :tracingID AND dt.documentDirection = :direction AND dt.signal = :isSignal"
 *   name = "getDocTransByActionType"         
 *   
 * @hibernate.query
 *   query = "FROM DocumentTransaction dt WHERE dt.processInstanceUID = :processInstanceUID"
 *   name = "getDocumentTransByProcessInstanceUID"        
 *   
 * @hibernate.query
 *   query = "FROM DocumentTransaction dt WHERE dt.tracingID = :tracingID AND dt.processInstanceUID != :processInstanceUID"
 *   name = "getDocumentTransByTracingIDAndProcessInstanceUID"  
 *   
 * @hibernate.query
 *   query = "SELECT COUNT(*) FROM DocumentTransaction dt WHERE dt.tracingID = :tracingID"
 *   name = "getDocTransCount"  
 */
public class DocumentTransaction extends AbstractPersistable implements IAuditTrailEntity
{
  private String _documentNo;
  private String _documentType;
  private String _documentDirection;
  private String _messageID;
  private Date _docTimeSent;
  private Date _docTimeReceived;
  private String _bizDocumentUID;
  private String _tracingID;
  private Long _processInstanceUID;
  private Boolean _isDuplicate; //re-receive doc from Partner or we re-sent out the doc (in two action pip, responding role eg repeatly send out 3A4C)
  private Boolean _isRetry; //re-send out from GT
  private Long _documentSize;
  private String _partnerName;
  private String _customerName;
  private String _groupName;
  private Boolean _isSignal;
  private String _userTrackingID;
  
  public DocumentTransaction()
  {
  }
  
  public DocumentTransaction(String groupName,String documentNo, String documentType, String documentDirection,
                             String messageID, Date docTimeSent, Date docTimeReceived, String bizDocumentUID,
                             String tracingID, Long processInstanceUID, Boolean isDuplicate, Boolean isRetry, Long documentSize,
                             String partnerName, String customerName, Boolean isSignal, String userTrackingID)
  {
    setGroupName(groupName);
    setDocumentNo(documentNo);
    setDocumentType(documentType);
    setDocumentDirection(documentDirection);
    setMessageID(messageID);
    setDocTimeSent(docTimeSent);
    setDocTimeReceived(docTimeReceived);
    setBizDocumentUID(bizDocumentUID);
    setTracingID(tracingID);
    setProcessInstanceUID(processInstanceUID);
    setDuplicate(isDuplicate);
    setRetry(isRetry);
    setDocumentSize(documentSize);
    setPartnerName(partnerName);
    setCustomerName(customerName);
    setSignal(isSignal);
    setUserTrackingID(userTrackingID);
  }
  
/**
   * @hibernate.property name="groupName" column = "`group_name`"
   */
  public String getGroupName()
  {
    return _groupName;
  }

  public void setGroupName(String name)
  {
    _groupName = name;
  }

  /**
   * hibernate.many-to-one constrained = "true" foreign-key = "bizDocumentUID" class = "com.gridnode.gtas.audit.model.BizDocument" not-null = "false" column = "biz_document_uid"
   * @hibernate.property column = "`biz_document_uid`"
   * @return
   */
  public String getBizDocumentUID()
  {
    return _bizDocumentUID;
  }

  public void setBizDocumentUID(String documentUID)
  {
    _bizDocumentUID = documentUID;
  }
  
  /**
   * @hibernate.property name = "customerName" column = "`customer_name`"
   * @return
   */
  public String getCustomerName()
  {
    return _customerName;
  }
  
  public void setCustomerName(String name)
  {
    _customerName = name;
  }
  
  /**
   * @hibernate.property name = "docTimeReceived" type = "timestamp" column = "`doc_time_received`"
   * @return
   */
  public Date getDocTimeReceived()
  {
    return _docTimeReceived;
  }

  public void setDocTimeReceived(Date timeReceived)
  {
    _docTimeReceived = timeReceived;
  }
  
  /**
   * @hibernate.property name = "docTimeSent" type = "timestamp" column = "`doc_time_sent`"
   * @return
   */
  public Date getDocTimeSent()
  {
    return _docTimeSent;
  }

  public void setDocTimeSent(Date timeSent)
  {
    _docTimeSent = timeSent;
  }
  
  /**
   * @hibernate.property name = "documentDirection" column = "`direction`"
   * @return
   */
  public String getDocumentDirection()
  {
    return _documentDirection;
  }

  public void setDocumentDirection(String direction)
  {
    _documentDirection = direction;
  }
  
  /**
   * @hibernate.property name = "documentNo" column = "`doc_no`"
   * @return
   */
  public String getDocumentNo()
  {
    return _documentNo;
  }

  public void setDocumentNo(String no)
  {
    _documentNo = no;
  }
  
  /**
   * @hibernate.property name = "documentSize" type = "long" column = "`document_size`"
   * @return
   */
  public Long getDocumentSize()
  {
    return _documentSize;
  }

  public void setDocumentSize(Long size)
  {
    _documentSize = size;
  }
  
  /**
   * @hibernate.property name = "documentType"  column = "`document_type`"
   * @return
   */
  public String getDocumentType()
  {
    return _documentType;
  }

  public void setDocumentType(String type)
  {
    _documentType = type;
  }
  
  /**
   * @hibernate.property name = "Duplicate" type = "boolean" column = "`is_duplicate`"
   * @return
   */
  public Boolean isDuplicate()
  {
    return _isDuplicate;
  }

  public void setDuplicate(Boolean duplicate)
  {
    _isDuplicate = duplicate;
  }
  
  /**
   * @hibernate.property name = "messageID" column = "`message_id`"
   * @return
   */
  public String getMessageID()
  {
    return _messageID;
  }

  public void setMessageID(String _messageid)
  {
    _messageID = _messageid;
  }
  
  /**
   * @hibernate.property name = "partnerName" column = "`partner_name`"
   * @return
   */
  public String getPartnerName()
  {
    return _partnerName;
  }

  public void setPartnerName(String name)
  {
    _partnerName = name;
  }
  
  /**
   * @hibernate.property column = "`process_instance_uid`"
   * @return
   */
  public Long getProcessInstanceUID()
  {
    return _processInstanceUID;
  }

  public void setProcessInstanceUID(Long instanceUID)
  {
    _processInstanceUID = instanceUID;
  }
  
  /**
   * @hibernate.property name = "tracingID" column = "`tracing_id`"
   * @return
   */
  public String getTracingID()
  {
    return _tracingID;
  }

  public void setTracingID(String _tracingid)
  {
    _tracingID = _tracingid;
  }
  
  /**
   * @hibernate.property name = "Signal" column = "`is_signal`" 
   * @return
   */
  public Boolean isSignal()
  {
    return _isSignal;
  }

  public void setSignal(Boolean signal)
  {
    _isSignal = signal;
  }
  
  /**
   * @hibernate.property name = "Retry" column = "`is_retry`"
   * @return
   */
  public Boolean isRetry()
  {
    return _isRetry;
  }

  public void setRetry(Boolean retry)
  {
    _isRetry = retry;
  }
  
  /**
   * @hibernate.property name = "userTrackingID" column = "`user_tracking_id`"
   * @return
   */
  public String getUserTrackingID()
  {
    return _userTrackingID;
  }

  public void setUserTrackingID(String trackingID)
  {
    _userTrackingID = trackingID;
  }

  public String toString()
  {
    return "DocumentTransaction[UID: "+(getUID()== null ? "" : getUID())+" documentNo: "+getDocumentNo()+" documentType: "+getDocumentType()+
           " documentDirection: "+getDocumentDirection()+" messageID: "+getMessageID()+" docTimeSent: "+getDocTimeSent()+
           " docTimeReceived: "+getDocTimeReceived()+" bizDocumentUID: "+getBizDocumentUID()+
           " tracingID: "+getTracingID()+" processInstanceUID: "+getProcessInstanceUID()+" isDuplicate: "+isDuplicate()+"isRetry: "+isRetry()+
           " documentSize: "+getDocumentSize()+" partnerName: "+getPartnerName()+" customerName: "+getCustomerName()+" groupName: "+getGroupName()+
           " isSignal: "+isSignal()+" userTrackingID: "+getUserTrackingID()+"]";
  }
}
