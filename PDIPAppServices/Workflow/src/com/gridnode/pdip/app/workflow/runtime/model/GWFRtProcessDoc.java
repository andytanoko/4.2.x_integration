/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Mahesh              Created
 * Apr 05 2007    Tam Wei Xiang       Added in customer be ID. To support 
 *                                    archive by customer
 */
package com.gridnode.pdip.app.workflow.runtime.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
 
 public class GWFRtProcessDoc
  extends AbstractEntity
  implements IGWFRtProcessDoc{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 374617657753846289L;
	protected String _documentId;
  protected String _documentType;
  protected String _documentName;
  protected String _businessTransActivityId;
  protected Long _binaryCollaborationUId;
  protected Long _rtBinaryCollaborationUId;
  protected Long _rtBusinessTransactionUId;
  protected Boolean _isPositiveResponse=Boolean.TRUE;
  protected Boolean _docProcessedFlag=Boolean.FALSE;
  protected Boolean _ackReceiptSignalFlag=Boolean.FALSE;
  protected Boolean _ackAcceptSignalFlag=Boolean.FALSE;
  protected String _exceptionSignalType;
  protected String _roleType;
  protected Integer _retryCount = new Integer(0);
  protected String _requestDocType;
  protected String _responseDocTypes;
  protected String _partnerKey;
  protected Integer _status;
  protected String _customerBEId;
  protected Object _reason;
  // ******************* Methods from AbstractEntity ******************
  public String getEntityName(){
    return ENTITY_NAME;
  }

  public String getEntityDescr(){
    return getEntityName();
  }

  public Number getKeyId(){
    return UID;
  }


   // *********************** Getters for attributes **********************

    public String getDocumentId(){
        return _documentId;
    }
    public String getDocumentType(){
        return _documentType;
    }
    public String getDocumentName(){
        return _documentName;
    }
    public String getBusinessTransActivityId(){
        return _businessTransActivityId;
    }
    public Long getBinaryCollaborationUId(){
        return _binaryCollaborationUId;
    }
    public Long getRtBinaryCollaborationUId(){
        return _rtBinaryCollaborationUId;
    }
    public Long getRtBusinessTransactionUId(){
        return _rtBusinessTransactionUId;
    }
    public Boolean getIsPositiveResponse(){
        return _isPositiveResponse;
    }

    public Boolean getDocProcessedFlag(){
        return _docProcessedFlag;
    }
    public Boolean getAckReceiptSignalFlag(){
        return _ackReceiptSignalFlag;
    }
    public Boolean getAckAcceptSignalFlag(){
        return _ackAcceptSignalFlag;
    }
    public String getExceptionSignalType(){
        return _exceptionSignalType;
    }
    public String getRoleType(){
        return _roleType;
    }
    public Integer getRetryCount(){
        return _retryCount;
    }
    public String getRequestDocType(){
        return _requestDocType;
    }
    public String getResponseDocTypes(){
        return _responseDocTypes;
    }
    public String getPartnerKey(){
        return _partnerKey;
    }
    public Integer getStatus(){
        return _status;
    }
    public Object getReason(){
        return _reason;
    }
    
    public String getCustomerBEId()
    {
      return _customerBEId;
    }
    
  // *********************** Setters for attributes **********************

    public void setDocumentId(String documentId){
        _documentId=documentId;
    }
    public void setDocumentType(String documentType){
        _documentType=documentType;
    }
    public void setDocumentName(String documentName){
        _documentName=documentName;
    }
    public void setBusinessTransActivityId(String businessTransActivityId){
        _businessTransActivityId=businessTransActivityId;
    }
    public void setBinaryCollaborationUId(Long binaryCollaborationUId){
        _binaryCollaborationUId=binaryCollaborationUId;
    }
    public void setRtBinaryCollaborationUId(Long rtBinaryCollaborationUId){
        _rtBinaryCollaborationUId=rtBinaryCollaborationUId;
    }
    public void setRtBusinessTransactionUId(Long rtBusinessTransactionUId){
        _rtBusinessTransactionUId=rtBusinessTransactionUId;
    }
    public void setIsPositiveResponse(Boolean isPositiveResponse){
        _isPositiveResponse=isPositiveResponse;
    }

    public void setDocProcessedFlag(Boolean docProcessedFlag){
        _docProcessedFlag=docProcessedFlag;
    }
    public void setAckReceiptSignalFlag(Boolean ackReceiptSignalFlag){
        _ackReceiptSignalFlag=ackReceiptSignalFlag;
    }
    public void setAckAcceptSignalFlag(Boolean ackAcceptSignalFlag){
        _ackAcceptSignalFlag=ackAcceptSignalFlag;
    }
    public void setExceptionSignalType(String exceptionSignalType){
        _exceptionSignalType=exceptionSignalType;
    }
    public void setRoleType(String roleType){
        _roleType=roleType;
    }
    public void setRetryCount(Integer retryCount){
        _retryCount=retryCount;
    }
    public void setRequestDocType(String requestDocType){
        _requestDocType=requestDocType;
    }
    public void setResponseDocTypes(String responseDocTypes){
        _responseDocTypes=responseDocTypes;
    }
    public void setPartnerKey(String partnerKey){
        _partnerKey=partnerKey;
    }
    public void setStatus(Integer status){
        _status=status;
    }
    public void setReason(Object reason){
        _reason=reason;
    }
    
    public void setCustomerBEId(String customerBEID)
    {
      _customerBEId = customerBEID;
    }
}
