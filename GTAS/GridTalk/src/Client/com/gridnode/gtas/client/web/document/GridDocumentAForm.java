/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridDocumentAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-22     Andrew Hill         Created
 * 2002-12-05     Andrew Hill         Attachments
 * 2003-01-30     Andrew Hill         hasAttachment field
 * 2003-08-04     Andrew Hill         Use ffe transferAs property to rename udoc to orgiginal filename
 * 2003-08-21     Andrew Hill         userTrackingId and processInstanceId
 * 2003-08-25     Andrew Hill         processInstanceUid
 * 2006-11-07     Regina Zeng         Added _senderCert, _receiverCert, _auditFileName, 
 *                                    _receiptAuditFileName, _docTransStatus, _docRemarks
 *                                    _docMetaInfoUID, _senderCertName, _receiverCertName
 * 2006-12-19     Tam Wei Xiang       Added receiptAuditFile                                   
 */
package com.gridnode.gtas.client.web.document;

import org.apache.struts.upload.FormFile;

import com.gridnode.gtas.client.web.strutsbase.FormFileElement;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class GridDocumentAForm extends GTActionFormBase
{
  public final String FILE_FIELDNAME = "udocFilename";
  public final String ATTACHMENT_FILENAMES_FIELDNAME = "attachmentFilenames"; //20021205AH mod 20021209AH
  public final String AUDIT_FILENAME = "auditFileName";
  public final String RECEIPT_AUDIT_FILENAME = "receiptAuditFileName";
  public final String RECEIPT_AUDIT_FOR_GDOC_DETAIL = "receiptAuditFile";
  
  private String _gdocId;
  private String _refGdocId;
  private String _gdocFilename;
  private String _udocNum;
  private String _refUdocNum;
  private String _udocFilename;
  private String _refUdocFilename;
  private String _udocVersion;
  private String _udocDocType;
  private String _udocFileSize;
  private String _udocFileType;
  private String _isExported;
  private String _isViewAckReq;
  private String _isExportAckReq;
  private String _isReceiveAckReq;
  private String _isViewed;
  private String _isSent;
  private String _isLocalPending;
  private String _isExpired;
  private String _isReqAckProcessed;
  private String _encryptionLevel;
  private String _folder;
  private String _createBy;
  private String _recipientNodeId;
  private String _recipientPartnerId;
  private String _recipientPartnerType;
  private String _recipientPartnerGroup;
  private String _recipientBizEntityId;
  private String _recipientPartnerFunction;
  private String _recipientGdocId;
  private String _senderNodeId;
  private String _senderGdocId;
  private String _senderPartnerFunction;
  private String _senderUserId;
  private String _senderUserName;
  private String _senderBizEntityId;
  private String _senderRoute;
  private String _senderPartnerId;
  private String _senderPartnerType;
  private String _senderPartnerGroup;
  private String _dateTimeImport;
  private String _dateTimeSendEnd;
  private String _dateTimeReceiveEnd;
  private String _dateTimeExport;
  private String _dateTimeCreate;
  private String _dateTimeTransComplete;
  private String _dateTimeReceiveStart;
  private String _dateTimeView;
  private String _dateTimeSendStart;
  private String _dateTimeRecipientView;
  private String _dateTimeRecipientExport;
  private String _partnerFunction;
  private String _portUid;
  private String _actionCode;
  private String _srcFolder;
  private String _notifyUserEmail;
  private String _recipientChannelUid;
  private String _recipientChannelName;
  private String _recipientChannelProtocol;
  private String _recipientPartnerName;
  private String _senderPartnerName;
  private String _portName;
  private String _hasAttachment; //20030130AH
  private String _processInstanceId; //20030820AH
  private String _userTrackingId; //20030821AH
  private String _processInstanceUid; //20030825AH
  private String _senderCert;
  private String _receiverCert;
  private String _auditFileName;
  private String _receiptAuditFileName;
  private String _docTransStatus;
  private String _docRemarks;
  private String _docMetaInfoUID;
  private String _senderCertName;
  private String _receiverCertName;

  public String getGdocId()
  { return _gdocId; }

  public void setGdocId(String gdocId)
  { _gdocId=gdocId; }

  public String getRefGdocId()
  { return _refGdocId; }

  public void setRefGdocId(String refGdocId)
  { _refGdocId=refGdocId; }

  public String getGdocFilename()
  { return _gdocFilename; }

  public void setGdocFilename(String gdocFilename)
  { _gdocFilename=gdocFilename; }

  public String getUdocNum()
  { return _udocNum; }

  public void setUdocNum(String udocNum)
  { _udocNum=udocNum; }

  public String getRefUdocNum()
  { return _refUdocNum; }

  public void setRefUdocNum(String refUdocNum)
  { _refUdocNum=refUdocNum; }


  //properties for udocFilename field....
  public void setUdocFilename(String value)
  {
    addFileToField(FILE_FIELDNAME,value);
  }

  public String getUdocFilename()
  {
    return getFilename(FILE_FIELDNAME,false);
  }

  public void setUdocFilenameDelete(String[] deletions) // 08102002 DDJ
  {
    for(int i=0; i < deletions.length; i++)
    {
      removeFileFromField(FILE_FIELDNAME,deletions[i]);
    }
  }

  /*public void setUdocFilenameUpload(FormFile file)      // 08102002 DDJ
  {
    // 15102002 DDJ: User is only allow to update content and not filename
    FormFileElement ffe = getFormFileElement(FILE_FIELDNAME, true);
    if(ffe != null)
    {
      file.setFileName(ffe.getFileName());
    }

    addFileToField(FILE_FIELDNAME,file);
  }*/
  
  public void setUdocFilenameUpload(FormFile file)     
  { //20030731AH
    String fileName = file.getFileName();
    FormFileElement ffe = getFormFileElement(FILE_FIELDNAME, true);
    if(ffe != null) fileName = ffe.getFileName();
    if(addFileToField(FILE_FIELDNAME,file))
    {
      ffe = getFormFileElement(FILE_FIELDNAME, file);
      if( !ffe.getFileName().equals(fileName) )
      {
        ffe.setTransferAs(fileName);
      }
    }
  }
  //...

  public String getRefUdocFilename()
  { return _refUdocFilename; }

  public void setRefUdocFilename(String refUdocFilename)
  { _refUdocFilename=refUdocFilename; }

  public String getUdocVersion()
  { return _udocVersion; }

  public void setUdocVersion(String udocVersion)
  { _udocVersion=udocVersion; }

  public String getUdocDocType()
  { return _udocDocType; }

  public void setUdocDocType(String udocDocType)
  { _udocDocType=udocDocType; }

  public String getUdocFileSize()
  { return _udocFileSize; }

  public void setUdocFileSize(String udocFileSize)
  { _udocFileSize=udocFileSize; }

  public String getUdocFileType()
  { return _udocFileType; }

  public void setUdocFileType(String udocFileType)
  { _udocFileType=udocFileType; }

  public String getIsExported()
  { return _isExported; }

  public void setIsExported(String isExported)
  { _isExported=isExported; }

  public String getIsViewAckReq()
  { return _isViewAckReq; }

  public void setIsViewAckReq(String isViewAckReq)
  { _isViewAckReq=isViewAckReq; }

  public String getIsExportAckReq()
  { return _isExportAckReq; }

  public void setIsExportAckReq(String isExportAckReq)
  { _isExportAckReq=isExportAckReq; }

  public String getIsReceiveAckReq()
  { return _isReceiveAckReq; }

  public void setIsReceiveAckReq(String isReceiveAckReq)
  { _isReceiveAckReq=isReceiveAckReq; }

  public String getIsViewed()
  { return _isViewed; }

  public void setIsViewed(String isViewed)
  { _isViewed=isViewed; }

  public String getIsSent()
  { return _isSent; }

  public void setIsSent(String isSent)
  { _isSent=isSent; }

  public String getIsLocalPending()
  { return _isLocalPending; }

  public void setIsLocalPending(String isLocalPending)
  { _isLocalPending=isLocalPending; }

  public String getIsExpired()
  { return _isExpired; }

  public void setIsExpired(String isExpired)
  { _isExpired=isExpired; }

  public String getIsReqAckProcessed()
  { return _isReqAckProcessed; }

  public void setIsReqAckProcessed(String isReqAckProcessed)
  { _isReqAckProcessed=isReqAckProcessed; }

  public String getEncryptionLevel()
  { return _encryptionLevel; }

  public void setEncryptionLevel(String encryptionLevel)
  { _encryptionLevel=encryptionLevel; }

  public String getFolder()
  { return _folder; }

  public void setFolder(String folder)
  { _folder=folder; }

  public String getCreateBy()
  { return _createBy; }

  public void setCreateBy(String createBy)
  { _createBy=createBy; }

  public String getRecipientNodeId()
  { return _recipientNodeId; }

  public void setRecipientNodeId(String recipientNodeId)
  { _recipientNodeId=recipientNodeId; }

  public String getRecipientPartnerId()
  { return _recipientPartnerId; }

  public void setRecipientPartnerId(String recipientPartnerId)
  { _recipientPartnerId=recipientPartnerId; }

  public String getRecipientPartnerType()
  { return _recipientPartnerType; }

  public void setRecipientPartnerType(String recipientPartnerType)
  { _recipientPartnerType=recipientPartnerType; }

  public String getRecipientPartnerGroup()
  { return _recipientPartnerGroup; }

  public void setRecipientPartnerGroup(String recipientPartnerGroup)
  { _recipientPartnerGroup=recipientPartnerGroup; }

  public String getRecipientBizEntityId()
  { return _recipientBizEntityId; }

  public void setRecipientBizEntityId(String recipientBizEntityId)
  { _recipientBizEntityId=recipientBizEntityId; }

  public String getRecipientPartnerFunction()
  { return _recipientPartnerFunction; }

  public void setRecipientPartnerFunction(String recipientPartnerFunction)
  { _recipientPartnerFunction=recipientPartnerFunction; }

  public String getRecipientGdocId()
  { return _recipientGdocId; }

  public void setRecipientGdocId(String recipientGdocId)
  { _recipientGdocId=recipientGdocId; }

  public String getSenderNodeId()
  { return _senderNodeId; }

  public void setSenderNodeId(String senderNodeId)
  { _senderNodeId=senderNodeId; }

  public String getSenderGdocId()
  { return _senderGdocId; }

  public void setSenderGdocId(String senderGdocId)
  { _senderGdocId=senderGdocId; }

  public String getSenderPartnerFunction()
  { return _senderPartnerFunction; }

  public void setSenderPartnerFunction(String senderPartnerFunction)
  { _senderPartnerFunction=senderPartnerFunction; }

  public String getSenderUserId()
  { return _senderUserId; }

  public void setSenderUserId(String senderUserId)
  { _senderUserId=senderUserId; }

  public String getSenderUserName()
  { return _senderUserName; }

  public void setSenderUserName(String senderUserName)
  { _senderUserName=senderUserName; }

  public String getSenderBizEntityId()
  { return _senderBizEntityId; }

  public void setSenderBizEntityId(String senderBizEntityId)
  { _senderBizEntityId=senderBizEntityId; }

  public String getSenderRoute()
  { return _senderRoute; }

  public void setSenderRoute(String senderRoute)
  { _senderRoute=senderRoute; }

  public String getSenderPartnerId()
  { return _senderPartnerId; }

  public void setSenderPartnerId(String senderPartnerId)
  { _senderPartnerId=senderPartnerId; }

  public String getSenderPartnerType()
  { return _senderPartnerType; }

  public void setSenderPartnerType(String senderPartnerType)
  { _senderPartnerType=senderPartnerType; }

  public String getSenderPartnerGroup()
  { return _senderPartnerGroup; }

  public void setSenderPartnerGroup(String senderPartnerGroup)
  { _senderPartnerGroup=senderPartnerGroup; }

  public String getDateTimeImport()
  { return _dateTimeImport; }

  public void setDateTimeImport(String dateTimeImport)
  { _dateTimeImport=dateTimeImport; }

  public String getDateTimeSendEnd()
  { return _dateTimeSendEnd; }

  public void setDateTimeSendEnd(String dateTimeSendEnd)
  { _dateTimeSendEnd=dateTimeSendEnd; }

  public String getDateTimeReceiveEnd()
  { return _dateTimeReceiveEnd; }

  public void setDateTimeReceiveEnd(String dateTimeReceiveEnd)
  { _dateTimeReceiveEnd=dateTimeReceiveEnd; }

  public String getDateTimeExport()
  { return _dateTimeExport; }

  public void setDateTimeExport(String dateTimeExport)
  { _dateTimeExport=dateTimeExport; }

  public String getDateTimeCreate()
  { return _dateTimeCreate; }

  public void setDateTimeCreate(String dateTimeCreate)
  { _dateTimeCreate=dateTimeCreate; }

  public String getDateTimeTransComplete()
  { return _dateTimeTransComplete; }

  public void setDateTimeTransComplete(String dateTimeTransComplete)
  { _dateTimeTransComplete=dateTimeTransComplete; }

  public String getDateTimeReceiveStart()
  { return _dateTimeReceiveStart; }

  public void setDateTimeReceiveStart(String dateTimeReceiveStart)
  { _dateTimeReceiveStart=dateTimeReceiveStart; }

  public String getDateTimeView()
  { return _dateTimeView; }

  public void setDateTimeView(String dateTimeView)
  { _dateTimeView=dateTimeView; }

  public String getDateTimeSendStart()
  { return _dateTimeSendStart; }

  public void setDateTimeSendStart(String dateTimeSendStart)
  { _dateTimeSendStart=dateTimeSendStart; }

  public String getDateTimeRecipientView()
  { return _dateTimeRecipientView; }

  public void setDateTimeRecipientView(String dateTimeRecipientView)
  { _dateTimeRecipientView=dateTimeRecipientView; }

  public String getDateTimeRecipientExport()
  { return _dateTimeRecipientExport; }

  public void setDateTimeRecipientExport(String dateTimeRecipientExport)
  { _dateTimeRecipientExport=dateTimeRecipientExport; }

  public String getPartnerFunction()
  { return _partnerFunction; }

  public void setPartnerFunction(String partnerFunction)
  { _partnerFunction=partnerFunction; }

  public String getPortUid()
  { return _portUid; }

  public void setPortUid(String portUid)
  { _portUid=portUid; }

  public String getActionCode()
  { return _actionCode; }

  public void setActionCode(String actionCode)
  { _actionCode=actionCode; }

  public String getSrcFolder()
  { return _srcFolder; }

  public void setSrcFolder(String srcFolder)
  { _srcFolder=srcFolder; }

  public String getNotifyUserEmail()
  { return _notifyUserEmail; }

  public void setNotifyUserEmail(String notifyUserEmail)
  { _notifyUserEmail=notifyUserEmail; }

  public String getRecipientChannelUid()
  { return _recipientChannelUid; }

  public void setRecipientChannelUid(String recipientChannelUid)
  { _recipientChannelUid=recipientChannelUid; }

  public String getRecipientChannelName()
  { return _recipientChannelName; }

  public void setRecipientChannelName(String recipientChannelName)
  { _recipientChannelName=recipientChannelName; }

  public String getRecipientChannelProtocol()
  { return _recipientChannelProtocol; }

  public void setRecipientChannelProtocol(String recipientChannelProtocol)
  { _recipientChannelProtocol=recipientChannelProtocol; }

  public String getRecipientPartnerName()
  { return _recipientPartnerName; }

  public void setRecipientPartnerName(String recipientPartnerName)
  { _recipientPartnerName=recipientPartnerName; }

  public String getSenderPartnerName()
  { return _senderPartnerName; }

  public void setSenderPartnerName(String senderPartnerName)
  { _senderPartnerName=senderPartnerName; }

  public String getPortName()
  { return _portName; }

  public void setPortName(String portName)
  { _portName=portName; }

  //attachmentFilenames properties 20021205AH.............
  //switched to attachmentFilenames 20021209AH
  public void setAttachmentFilenames(String[] values)
  {
    for(int i=0; i < values.length; i++)
    {
      addFileToField(ATTACHMENT_FILENAMES_FIELDNAME,values[i]);
    }
  }

  public String[] getAttachmentFilenames()
  {
    return getFilenames(ATTACHMENT_FILENAMES_FIELDNAME,false);
  }

  public void setAttachmentFilenamesDelete(String[] deletions)
  {
    for(int i=0; i < deletions.length; i++)
    {
      removeFileFromField(ATTACHMENT_FILENAMES_FIELDNAME,deletions[i]);
    }
  }

  public void setAttachmentFilenamesUpload(FormFile file)
  { // ummm... we dont actually allow this anyhow read-only!
    FormFileElement ffe = getFormFileElement(ATTACHMENT_FILENAMES_FIELDNAME, true);
    if(ffe != null)
    {
      //20030801AH - WHY is this block here? Attachments is a multiple file field. Even tif it was editable why would you want to name all files the same sa the first one?
      file.setFileName(ffe.getFileName()); 
    }
    addFileToField(ATTACHMENT_FILENAMES_FIELDNAME,file);
  }
  //..................................................

  public void setHasAttachment(String hasAttachment)
  { //20030130AH
    _hasAttachment = hasAttachment;
  }

  public String getHasAttachment()
  { //20030130AH
    return _hasAttachment;
  }
  
  public String getProcessInstanceId()
  { //20030821AH
    return _processInstanceId;
  }

  public String getUserTrackingId()
  { //20030821AH
    return _userTrackingId;
  }

  public void setProcessInstanceId(String string)
  { //20030821AH
    _processInstanceId = string;
  }

  public void setUserTrackingId(String string)
  { //20030821AH
    _userTrackingId = string;
  }

  public String getProcessInstanceUid()
  {
    return _processInstanceUid;
  }

  public void setProcessInstanceUid(String string)
  {
    _processInstanceUid = string;
  }

  public String getAuditFileName()
  {
    //return _auditFileName;
    return getFilename(AUDIT_FILENAME,false);
  }

  public void setAuditFileName(String fileName)
  {
    addFileToField(AUDIT_FILENAME,fileName);
  }

  public String getDocTransStatus()
  {
    return _docTransStatus;
  }

  public void setDocTransStatus(String transStatus)
  {
    _docTransStatus = transStatus;
  }
  
  
  public String getReceiptAuditFileName()
  {
    //return _receiptAuditFileName;
    return getFilename(RECEIPT_AUDIT_FILENAME, false);
  }

  public void setReceiptAuditFileName(String auditFileName)
  {
    //_receiptAuditFileName = auditFileName;
    addFileToField(RECEIPT_AUDIT_FILENAME,auditFileName);
  } 
  
  public void setReceiptAuditFile(String filename)
  {
    addFileToField(RECEIPT_AUDIT_FOR_GDOC_DETAIL, filename);
  }
  
  public String getReceiptAuditFile()
  {
    return getFilename(RECEIPT_AUDIT_FOR_GDOC_DETAIL, false);
  }
  
  public String getReceiverCert()
  {
    return _receiverCert;
  }

  public void setReceiverCert(String cert)
  {
    _receiverCert = cert;
  }

  public String getSenderCert()
  {
    return _senderCert;
  }

  public void setSenderCert(String cert)
  {
    _senderCert = cert;
  }

  public String getDocRemarks()
  {
    return _docRemarks;
  }

  public void setDocRemarks(String remarks)
  {
    _docRemarks = remarks;
  }

  public String getDocMetaInfoUID()
  {
    return _docMetaInfoUID;
  }

  public void setDocMetaInfoUID(String metaInfoUID)
  {
    _docMetaInfoUID = metaInfoUID;
  }

  public String getReceiverCertName()
  {
    return _receiverCertName;
  }

  public void setReceiverCertName(String certName)
  {
    _receiverCertName = certName;
  }

  public String getSenderCertName()
  {
    return _senderCertName;
  }

  public void setSenderCertName(String certName)
  {
    _senderCertName = certName;
  }
}
