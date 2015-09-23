/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 * Jan 10, 2007    Tam Wei Xiang       Added new field isRetry
 */
package com.gridnode.gtas.audit.common.model;

import java.util.Date;

/**
 * This class includes the necessary information about the IB or OB transaction document(
 * the Action & Signal messages) and specified information of process instance. The user document and 
 * the attachment of the action/signal message will be encapsulated in BusinessDocument instance(s).
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class DocInfo implements ITrailInfo
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 3182463392209045891L;

  /**
   * It indicates the user document number
   */
  private String _docNo;
  
  /**
   * It indicates the udoc doc type
   */
  private String _documentType;
  
  /**
   * IT indicates the griddocument folder name
   */
  private String _documentDirection;
  
  /**
   * It combines the griddocumet foldername and gdocID
   */
  private String _messageID;
  
  /**
   * It indicates the time that we sent out the document
   */
  private Date _dateTimeSent;
  
  /**
   * It indicate the time that we received the document
   */
  private Date _dateTimeReceived;
  
  /**
   * It can link all the events that has acted on a particular transaction doc
   */
  private String _tracingID;
  
  /**
   * It indicates the document size of the user document.
   */
  private Long _documentSize;
  
  /**
   * It indicate whether the transaction document has been received repeatedly
   */
  private Boolean _isDuplicate;
  
  /**
   * It indicate whether the transaction document has been resent
   */
  private Boolean _isRetry;
  
  /**
   * It indicates the gdoc'ss user trackingID
   */
  private String _userTrackingID;
  
  /**
   * It will be used to derive the groupName in the OTC.
   */
  private String _beID;
  
  /**
   * It composes of the process info related data
   */
  private ProcessSummary _processSummary;
  
  public DocInfo() {}
  
  public DocInfo(String docNo, String documentType, String documentDirection, String messageID, 
                 Date dateTimeSent, Date dateTimeReceived, String tracingID, Long documentSize,
                 Boolean isDuplicate, Boolean isRetry,String userTrackingID, String beID, ProcessSummary processSummary)
  {
    setDocNo(docNo);
    setDocumentType(documentType);
    setDocumentDirection(documentDirection);
    setMessageID(messageID);
    setDateTimeSent(dateTimeSent);
    setDateTimeReceived(dateTimeReceived);
    setTracingID(tracingID);
    setDocumentSize(documentSize);
    setIsDuplicate(isDuplicate);
    setRetry(isRetry);
    setUserTrackingID(userTrackingID);
    setBeID(beID);
    setProcessSummary(processSummary);
  }
  
  public String getBeID()
  {
    return _beID;
  }

  public void setBeID(String _beid)
  {
    _beID = _beid;
  }

  public Date getDateTimeReceived()
  {
    return _dateTimeReceived;
  }

  public void setDateTimeReceived(Date timeReceived)
  {
    _dateTimeReceived = timeReceived;
  }

  public Date getDateTimeSent()
  {
    return _dateTimeSent;
  }

  public void setDateTimeSent(Date timeSent)
  {
    _dateTimeSent = timeSent;
  }

  public String getDocNo()
  {
    return _docNo;
  }

  public void setDocNo(String no)
  {
    _docNo = no;
  }

  public String getDocumentDirection()
  {
    return _documentDirection;
  }

  public void setDocumentDirection(String direction)
  {
    _documentDirection = direction;
  }

  public Long getDocumentSize()
  {
    return _documentSize;
  }

  public void setDocumentSize(Long size)
  {
    _documentSize = size;
  }

  public String getDocumentType()
  {
    return _documentType;
  }

  public void setDocumentType(String type)
  {
    _documentType = type;
  }

  public Boolean isDuplicate()
  {
    return _isDuplicate;
  }

  public void setIsDuplicate(Boolean duplicate)
  {
    _isDuplicate = duplicate;
  }

  public String getMessageID()
  {
    return _messageID;
  }

  public void setMessageID(String _messageid)
  {
    _messageID = _messageid;
  }

  public ProcessSummary getProcessSummary()
  {
    return _processSummary;
  }

  public void setProcessSummary(ProcessSummary summary)
  {
    _processSummary = summary;
  }

  public String getTracingID()
  {
    return _tracingID;
  }

  public void setTracingID(String _tracingid)
  {
    _tracingID = _tracingid;
  }

  public String getUserTrackingID()
  {
    return _userTrackingID;
  }

  public void setUserTrackingID(String trackingID)
  {
    _userTrackingID = trackingID;
  }
  
  public Boolean isRetry()
  {
    return _isRetry;
  }

  public void setRetry(Boolean retry)
  {
    _isRetry = retry;
  }

  public String toString()
  {
    return "DocInfo[docNo: "+getDocNo()+" documentType: "+getDocumentType()+" documentDirection: "+getDocumentDirection()+
           " messageID: "+getMessageID()+" dateTimeSent: "+getDateTimeSent()+" dateTimeReceived: "+getDateTimeReceived()+
           " tracingID: "+getTracingID()+" documentSize: "+getDocumentSize()+" isDuplicate: "+isDuplicate()+" isRetry: "+isRetry()+
           " userTrackingID: "+getUserTrackingID()+" beID: "+getBeID()+
           " ProcessSummary: "+(getProcessSummary() == null? "": getProcessSummary())+"]";
  }
}
