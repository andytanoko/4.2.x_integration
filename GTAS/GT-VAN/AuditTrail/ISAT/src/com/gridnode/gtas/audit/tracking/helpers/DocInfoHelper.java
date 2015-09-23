/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocInfoHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 23, 2006    Tam Wei Xiang       Created
 * Dec 26, 2006    Tam Wei Xiang       Add field isSignal into DocumentTransaction
 * Jan 08, 2007    Tam Wei Xiang       Change the UID from Long to String 
 */
package com.gridnode.gtas.audit.tracking.helpers;

import java.util.Collection;
import java.util.Date;

import com.gridnode.gtas.audit.common.IAuditTrailConstant;
import com.gridnode.gtas.audit.common.model.DocInfo;
import com.gridnode.gtas.audit.model.DocumentTransaction;
import com.gridnode.gtas.audit.tracking.exception.AuditTrailTrackingException;
import com.gridnode.gtas.audit.common.model.ProcessSummary;
import com.gridnode.gtas.audit.dao.AuditTrailEntityDAO;
import com.gridnode.gtas.audit.dao.DocTransDAO;
import com.gridnode.gtas.audit.dao.exception.AuditTrailDBServiceException;

/**
 * This class provides the necessary methods that to be used by the TrailInfoHandler to perform
 * creation, update, retrieve, and persistence for DocTransaction.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class DocInfoHelper extends AbstractInfoHelper
{
  private static DocTransDAO _dao = null;
  private static final String CLASS_NAME = "DocInfoHelper";
  
  public DocInfoHelper()
  {
    //super(_dao);
    _dao = new DocTransDAO(false);
  }
  
  /*
  public static DocInfoHelper getInstance()
  {
    return _helper;
  }*/
  
  protected AuditTrailEntityDAO getDAO()
  {
    return _dao;
  }
  
  /**
   * Long uid, String groupName,String documentNo, String documentType, String documentDirection,
                             String messageID, Date docTimeSent, Date docTimeReceived, Long bizDocumentUID,
                             String tracingID, Long processInstanceUID, Boolean isDuplicate, Integer documentSize,
                             String partnerName, String customerName
   * @param info
   * @param groupName
   * @return
   */
  public DocumentTransaction createDocTransaction(DocInfo info, String groupName, String bizDocumentUID)
  {
    String docNo = info.getDocNo();
    String documentType = info.getDocumentType();
    String documentDirection = info.getDocumentDirection();
    String messageID = info.getMessageID();
    Date docTimeSent = info.getDateTimeSent();
    Date docTimeReceived = info.getDateTimeReceived();
    String tracingID = info.getTracingID();
    Boolean isDuplicate = info.isDuplicate();
    Boolean isRetry = info.isRetry();
    Long documentSize = info.getDocumentSize();
    String userTrackingID = info.getUserTrackingID();
    
    //getting the process info
    Long processInstanceUID = null;
    String partnerName = "", customerName = "";
    ProcessSummary proSumm = getProcessSummary(info);
    if(proSumm != null)
    {
      processInstanceUID = proSumm.getProcessInstanceUID();
      partnerName = proSumm.getTradingPartnerName();
      customerName = proSumm.getCustomerName();
    }
    
    Boolean isSignal = isSignalMsg(info);
    
    return new DocumentTransaction(groupName, docNo, documentType, documentDirection,
                                   messageID, docTimeSent, docTimeReceived, bizDocumentUID,
                                   tracingID, processInstanceUID, isDuplicate,isRetry,documentSize,
                                   partnerName, customerName, isSignal, userTrackingID);
  }
  
  /**
   * For the case of GT resend Document, we will have more than one DocumentTransaction given the tracingID and direction
   * @param tracingID
   * @param direction
   * @return
   * @throws AuditTrailDBServiceException
   */
  public Collection<DocumentTransaction> getDocumentTransactions(String tracingID, String direction) throws AuditTrailDBServiceException
  {
      return _dao.retrieveDocumentTrans(tracingID, direction);
  }
  
  public Collection<DocumentTransaction> getDocumentTransByTraceIDAndMsgID(String tracingID, String msgID) throws AuditTrailDBServiceException
  {
    return _dao.retrieveDocTransTracingIDAndMsgID(tracingID, msgID);
  }
  
  /**
   * Retrieve a DocumentTransaction record. 
   * NOTE: use only while we sure it only return one result. Else underlying persitent layer will treat it as error.
   * @param tracingID
   * @param direction
   * @return
   * @throws AuditTrailDBServiceException
   */
  public DocumentTransaction getSingleDocumentTransaction(String tracingID, String direction) throws AuditTrailDBServiceException
  {
    return _dao.retrieveSingleDocumentTrans(tracingID, direction);
  }
  
  /**
   * Get the total count of the action document transaction (not the rn ack or rn_exception) given the direction and tracingID
   * @param direction
   * @param tracingID
   * @return
   * @throws AuditTrailTrackingException
   */
  public int getActionDocTransCountByDirection(String direction, String tracingID, Long processInstaceUID) throws AuditTrailTrackingException
  {
    try
    {
      return _dao.getActionDocTransCountByDirectionProcessUID(direction , tracingID, processInstaceUID);
    }
    catch(Exception ex)
    {
      throw new AuditTrailTrackingException("Failed to get document transaction count for tracingID "+tracingID+", direction: "+direction,ex);
    }
  }
  
  /**
   * Retrieve the ProcessSummary obj that embedded inside the DocInfo
   * @param info
   * @return
   */
  public ProcessSummary getProcessSummary(DocInfo info)
  {
    return info.getProcessSummary();
  }
  
  public boolean isSignalMsg(DocInfo docInfo)
  {
    String docType = docInfo.getDocumentType();
    return (IAuditTrailConstant.SIGNAL_MESSAGE_ACK.equals(docType) || IAuditTrailConstant.SIGNAL_MESSAGE_EXP.equals(docType));
  }
}
