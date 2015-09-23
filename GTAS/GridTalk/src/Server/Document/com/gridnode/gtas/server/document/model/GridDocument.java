/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridDocument.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 01 2002    Koh Han Sing        Created
 * Nov 19 2002    Koh Han Sing        Add in RnProfileUid
 * Nov 22 2002    Koh Han Sing        Add in attachment fields
 * Jan 20 2003    Neo Sok Lay         Add transient field ReceiveTrxId for
 *                                    use when sending back acknowledgement.
 * May 22 2003    Koh Han Sing        Add in UdocFullPath, UniqueDocIdentifier
 *                                    and ExportedUdocFullPath
 * Jun 20 2003    Koh Han Sing        Add in IsRejected
 * Jul 09 2003    Koh Han Sing        Add in 10 custom fields
 * Jul 18 2003    Neo Sok Lay         Change EntityDescr.
 * Aug 21 2003    Guo Jianyu          Add _processInstanceID and _userTrackingID
 * Aug 22 2003    Guo Jianyu          Add _processInstanceUid
 * Oct 01 2003    Neo Sok Lay         Add fields:
 *                                    - SenderBizEntityUuid
 *                                    - SenderRegistryQueryUrl
 *                                    - RecipientBizEntityUuid
 *                                    - RecipientRegistryQueryUrl
 * Oct 20 2003    Neo Sok Lay         Add non-persistent field:
 *                                    - OBPayloadFile
 * Oct 23 2003    Guo Jianyu          Add _docTransStatus
 * Dec 30 2003    Guo Jianyu          Add fields:
 *                                    - MessageDigest
 *                                    - AuditFileName
 *                                    - ReceiptAuditFileName
 * Feb 03 2004    Koh Han Sing        Add transient _tempUdocFilename
 * Oct 20 2006    Tam Wei Xiang       Add tracingID
 * Oct 07 2010    Tam Wei Xiang       #1897 Added isRead
 */
package com.gridnode.gtas.server.document.model;

import com.gridnode.gtas.server.mapper.model.IDocumentMetaInfo;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an object model for GridDocument entity. A GridDocument keeps
 * tracks of a user document which can flow from one GridNode to another and
 * from one system to another.<P>
 *
 * Typically, a "complete" GridDocument consists of the user document file and
 * the GridDocument xml file.<P>
 *
 * The Model:<BR><PRE>
 *   UId                 - UID for a GridDocument entity instance.
 *
 * <B>  Header Information</B>
 *   GdocId              - Unique Id to identify the GridDocument in a folder.
 *   GdocFilename        - Name of the GridDocument file.
 *   Folder              - Folder in which the GridDocument resides in.
 *   SenderPartnerId     - PartnerId of recipient Partner as perceived by the
 *                         sender GridNode.
 *   UdocNum             - User assigned document number.
 *   RefUdocNum          - UDocNum of a document that the user document
 *                         references.
 *   UdocFilename        - Name of the user document file.
 *   UdocVersion         - Version of the user document.
 *   UdocDocType         - DocumentType of the user document.
 *   UdocFileType        - Type of the user document file.
 *   SrcFolder           - Source folder where the GridDocument comes from,
 *                         base on the PartnerFunction triggered.
 *   NotifyUserEmail     - Email address to send notification when the
 *                         document is being received/processed.
 *   UniqueDocIdentifier - A unique identifier for the user document
 *   UdocFullPath        - Full path of the user document
 *   ExportedUdocFullPath - Full path of the exported user document to the PORT
 *   (Below are not persistent in DB)
 *   UdocFileSize        - Size of the user document file.
 *   AuditFileName       - Full path to the audit file of this document
 *   ReceiptAuditFileName - Full path to the audit file of the receipt to this document
 *   MessageDigest       - message digest of this document
 *
 * <B>  Status</B>
 *   Exported            - Whether the user document has been export.
 *   ViewAckReq          - Whether acknowledgement is required by partner when
 *                         user views this document.
 *   ExportAckReq        - Whether acknowledgement is required by partner when
 *                         user exports this document.
 *   ReceiveAckReq       - Whether acknowledgement is required by partner when
 *                         this document is received.
 *   View                - Whether the GridDocument has been opened (read) by
 *                         any of the users.
 *   Sent                - Whether the GridDocument has been sent to partners.
 *   LocalPending        - Whether the GridDocument is pending in the Outbound.
 *   Expired             - Whether the GridDocument has expired (exceeded period
 *                         to send).
 *   RecAckProcessed     - Whether the failure of recipient receive
 *                         acknowledgement has been sent out.
 *   IsRejected          - Whether the user document has been rejected by the
 *                         recipient.
 *   DocTransStatus      - the status of the document transaction, successful or failed
 *                         or ongoing.
 *
 * <B>  System</B>
 *   (Below are not persistent in DB)
 *   CreateBy            - User who created this GridDocument.
 *
 * <B>  Sender Information</B>
 *   GridNodeId            - GridNodeId of sender GridNode of the document.
 *   SenderGdocId          - GDocId of the corresponding GridDocument in the
 *                           sender GridNode.
 *   SenderRoute           - The route via which the document is sent, DIRECT
 *                           from GridTalk to GridTalk or via GRIDMASTER.
 *   SenderPartnerFunction - PartnerFunction used by the sender GridNode when
 *                           the document was sent.
 *   SenderUserId          - UserId of user who sent this GridDocument.
 *   SenderUserName        - Name of user who sent this GridDocument.
 *   SenderBizEntityId     - BusinessEntityId of sender Partner.
 *   SenderBizEntityUuid   - UUID of the Business Entity of sender Partner
 *   SenderRegistryQueryUrl- Query URL of the registry that can be used to discover
 *                           the Business Entity
 *
 * <B>  Mapped Information</B>
 *   SenderPartnerId       - PartnerId of recipient Partner as perceived by the
 *                           sender GridNode.
 *   SenderPartnerType     - PartnerType of recipient Partner as perceived by the
 *                           sender GridNode.
 *   SenderPartnerGroup    - PartnerGroup of recipient Partner as perceived by the
 *                           sender GridNode.
 *
 * <B>  Mapping</B>
 *   RefGdocId             - GDocId of a GridDocument this GridDocument
 *                           references.
 *   SenderPartnerFunction - PartnerFunction used by the sender GridNode when
 *                           the document was sent (not persistent in DB).
 *   RefUdocFilename       - Filename of the user document referenced by this
 *                           document (not persistent in DB).
 *
 * <B>  Transport</B>
 *   RecipientNodeId       - GridNodeId of recipient GridNode of the document
 *   RecipientPartnerId    - PartnerId of recipient Partner as perceived by
 *                           sender GridNode.
 *   RecipientPartnerFunction - PartnerFunction used by the recipient GridNode
 *                              when this GridDocument was received.
 *   EncryptionLevel       - Level of encryption used for user document.
 *   RecipientPartnerType  - PartnerType of recipient Partner as perceived by
 *                           recipient GridNode.
 *   RecipientPartnerGroup - PartnerGroup of recipient Partner as perceived by
 *                           recipient GridNode.
 *   RecipientBizEntityId  - BusinessEntityId of recipient Partner.
 *   RecipientBizEntityUuid- UUID of the Business entity of recipient Partner.
 *   RecipientRegistryQueryUrl - Query URL of registry that can be used to discover
 *                           the Business Entity
 *   RecipientGdocId       - GDocId of this GridDocument in recipient GridNode.
 *   RecipientChannelUid   - ChannelUid of the sender channel used.
 *   RecipientChannelName  - Name of the sender channel.
 *   RecipientChannelProtocol - Transport protocol used by the sender channel.
 *
 * <B>  Backend</B>
 *   PortUid             - UID of Port to use for export.
 *   (Below are not persistent in DB)
 *
 * <B>  History</B>
 *   DateTimeCreate          - Creation date of the GridDocument.
 *   DateTimeSendEnd         - Time that document was last sent.
 *   DateTimeReceiveEnd      - Time that document was received.
 *   DateTimeSendStart       - Time that document was last started sending to
 *                             the partner.
 *   DateTimeTransComplete   - Time that the document has last completed the
 *                             entire send and receive transaction.
 *   DateTimeExport          - Time that the document was last exported from
 *                             GridTalk.
 *   DateTimeImport          - Time that the document was imported into
 *                             GridTalk.
 *   DateTimeReceiveStart    - Time that the document was received from partner.
 *   DateTimeView            - Time that the document was last viewed by the
 *                             user.
 *   DateTimeRecipientView   - Time that the document was last viewed by the
 *                             recipient user of this document.
 *   DateTimeRecipientExport - Time that the document was last exported by the
 *                             recipient user of this document.
 *   ActivityList            - List of activities associated with the document
 *
 * <B>  RosettaNet</B>
 *   RnProfile               - The RosettaNet profile linked to this document
 *   ProcessDefId            - The process definition used for this document
 *   IsRequest               - Whether this document is a requesting RN document
 *   ProcessInstanceID       - ID of the process instance the document belongs to
 *   ProcessInstanceUID      - Process Instance UID (the key for the process instance)
 *   UserTrackingID          - User Tracking ID (a more user-friendly id for the process
 *                             instance)
 *
 * <B>  Custom Fields</B>
 *   Custom1                 - Custom Field 1
 *   Custom2                 - Custom Field 2
 *   Custom3                 - Custom Field 3
 *   Custom4                 - Custom Field 4
 *   Custom5                 - Custom Field 5
 *   Custom6                 - Custom Field 6
 *   Custom7                 - Custom Field 7
 *   Custom8                 - Custom Field 8
 *   Custom9                 - Custom Field 9
 *   Custom10                - Custom Field 10
 *
 * <B>  Transient</B>
 *   PartnerFunction     - The partner function to use during import.
 *   ActionCode          - Code for action performed on the document by the
 *                         user.
 *   ReceiveTrxId        - Transaction Id of the received document, if applicable.
 *   OBPayloadFile       - Path of the Outbound Payload File.
 *   TempUdocFilename    - The name of the temp udoc file in the temp folder.
 *   
 * <B> OnlineTracking</B>
 *   tracingID           - The ID is used to link all the events/activities that   
 *                         the business document has gone through.
 * <B> EStore </B>
 *   SenderCert    - the uid of the cert that we use to verify (using partner cert. IB) 
 *                   or sign (own cert. OB) depend on the folder direction
 *   ReceiverCert  - the uid of the cert that we use to decrypt(using own cert. IB) or 
 *                   encrypt(partner cert. OB) depend on the folder direction
 *   DocDateGen    - the udoc date
 *   OriginalDoc   - to indicate the audit file is the final version (during outbound eg all the necessary transform has done)
 * 	                 or original version (during inbound eg just receive from partner without applying any mapping rule).
 *                   For the re-send or re-receive of a particular doc, its original doc is true
 * 
 *
 * <P>
 * Not all attributes are stored in the database. Those marked "not persistent
 * in DB" are not stored in database. All are stored in the GridDocument xml
 * file. <B>Transient</B> attributes are not persistent at all (not in DB nor
 * in xml file).
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Koh Han Sing
 *
 * @version 2.3 I1
 * @since 2.0
 */
public class GridDocument
  extends    AbstractEntity
  implements IGridDocument, IDocumentMetaInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2975837793898951168L;
	protected Long      _gdocId;
  protected String    _gdocFilename;
  protected String    _folder;
  protected String    _senderPartnerId;
  protected String    _udocNum;
  protected String    _refUdocNum;
  protected String    _udocFilename;
  protected Integer   _udocVersion;
  protected String    _udocDocType;
  protected String    _udocFileType;
  protected String    _srcFolder            = "";
  protected String    _notifyUserEmail      = "";
  protected String    _uniqueDocIdentifier  = "";
  protected String    _udocFullPath         = "";
  protected String    _exportedUdocFullPath = "";
  protected Long      _udocFileSize         = new Long(0);
  protected String    _auditFileName;
  protected String    _receiptAuditFileName;
  protected String    _messageDigest;

  protected Boolean   _isExported           = Boolean.FALSE;
  protected Boolean   _isViewAckReq         = Boolean.FALSE;
  protected Boolean   _isExportAckReq       = Boolean.FALSE;
  protected Boolean   _isReceiveAckReq      = Boolean.FALSE;
  protected Boolean   _isViewed             = Boolean.FALSE;
  protected Boolean   _isSent               = Boolean.FALSE;
  protected Boolean   _isLocalPending       = Boolean.FALSE;
  protected Boolean   _isExpired            = Boolean.FALSE;
  protected Boolean   _isRecAckProcessed    = Boolean.FALSE;
  protected Boolean   _isRejected           = Boolean.FALSE;
  protected String    _docTransStatus;

  protected String    _createBy;

  protected Long      _senderNodeId;
  protected Long      _senderGdocId;
  protected String    _senderRoute;
  protected String    _senderPartnerFunction;
  protected String    _senderUserId;
  protected String    _senderUserName;
  protected String    _senderBizEntityId;
  protected String    _senderPartnerType;
  protected String    _senderPartnerGroup;
  protected String    _senderPartnerName;
  protected String    _senderBizEntityUuid;
  protected String    _senderRegistryQueryUrl;

  protected Long      _refGdocId;
  protected String    _refUdocFilename;

  protected Long      _recipientNodeId;
  protected String    _recipientPartnerId;
  protected String    _recipientPartnerFunction;
  protected Integer   _encryptionLevel     = ENCRYPT_LEVEL_64;
  protected String    _recipientPartnerType;
  protected String    _recipientPartnerGroup;
  protected String    _recipientPartnerName;
  protected String    _recipientBizEntityId;
  protected Long      _recipientGdocId;
  protected Long      _recipientChannelUid;
  protected String    _recipientChannelName;
  protected String    _recipientChannelProtocol;
  protected String    _recipientBizEntityUuid;
  protected String    _recipientRegistryQueryUrl;

  protected Long      _portUid = null;
  protected String    _portName;

  protected Date      _dateTimeCreate;
  protected Date      _dateTimeSendEnd;
  protected Date      _dateTimeReceiveEnd;
  protected Date      _dateTimeSendStart;
  protected Date      _dateTimeTransComplete;
  protected Date      _dateTimeExport;
  protected Date      _dateTimeImport;
  protected Date      _dateTimeReceiveStart;
  protected Date      _dateTimeView;
  protected Date      _dateTimeRecipientView;
  protected Date      _dateTimeRecipientExport;
  protected List      _activityList;

  protected String    _partnerFunction;
  protected String    _actionCode;
  //030120NSL
  protected String    _receiveTrxId;

  // Koh Han Sing 20021119
  protected Long      _rnProfileUid = null;
  protected String    _processDefId = "";
  protected Boolean   _isRequest = Boolean.FALSE;

  // Koh Han Sing 20021122
  protected Boolean   _hasAttachment            = Boolean.FALSE;
  protected Boolean   _isAttachmentLinkUpdated  = Boolean.TRUE;
  protected List      _attachments;
  protected Integer   _triggerType;

  // Koh Han Sing 20030709
  protected String    _custom1 = "";
  protected String    _custom2 = "";
  protected String    _custom3 = "";
  protected String    _custom4 = "";
  protected String    _custom5 = "";
  protected String    _custom6 = "";
  protected String    _custom7 = "";
  protected String    _custom8 = "";
  protected String    _custom9 = "";
  protected String    _custom10 = "";
  protected Long      _processInstanceUid;
  protected String    _processInstanceID;
  protected String    _userTrackingID;

  //031020
  protected String    _OBPayloadFile;

  //Koh Han Sing 20040203
  protected String    _tempUdocFilename = "";
  
  //TWX: 14 NOV 2005
  protected Long _senderCert; 
  protected Long _receiverCert;
  protected String _docDateGen;
  protected Boolean _originalDoc = Boolean.FALSE; 
  
  protected String _tracingID;
  protected Boolean _isRead = Boolean.FALSE; //TWX 20101007 to support the pulling of doc from external system
  
  public GridDocument()
  {
    _activityList = new ArrayList();
    _attachments = new ArrayList();
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return new StringBuffer("Folder ").append(getFolder()).append("/ID ").append(getGdocId()).toString();
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***********************

  public Long getGdocId()
  {
    return _gdocId;
  }

  public String getGdocFilename()
  {
    return _gdocFilename;
  }

  public String getFolder()
  {
    return _folder;
  }

  public String getSenderPartnerId()
  {
    return _senderPartnerId;
  }

  public String getUdocNum()
  {
    return _udocNum;
  }

  public String getRefUdocNum()
  {
    return _refUdocNum;
  }

  public String getUdocFilename()
  {
    return _udocFilename;
  }

  public Integer getUdocVersion()
  {
    return _udocVersion;
  }

  public String getUdocDocType()
  {
    return _udocDocType;
  }

  public String getUdocFileType()
  {
    return _udocFileType;
  }

  public String getSrcFolder()
  {
    return _srcFolder;
  }

  public String getNotifyUserEmail()
  {
    return _notifyUserEmail;
  }

  public String getUniqueDocIdentifier()
  {
    return _uniqueDocIdentifier;
  }

  public String getUdocFullPath()
  {
    return _udocFullPath;
  }

  public String getExportedUdocFullPath()
  {
    return _exportedUdocFullPath;
  }

  public Long getUdocFileSize()
  {
    return _udocFileSize;
  }

  public Boolean isExported()
  {
    return _isExported;
  }

  public Boolean isViewAckReq()
  {
    return _isViewAckReq;
  }

  public Boolean isExportAckReq()
  {
    return _isExportAckReq;
  }

  public Boolean isReceiveAckReq()
  {
    return _isReceiveAckReq;
  }

  public Boolean isViewed()
  {
    return _isViewed;
  }

  public Boolean isSent()
  {
    return _isSent;
  }

  public Boolean isLocalPending()
  {
    return _isLocalPending;
  }

  public Boolean isExpired()
  {
    return _isExpired;
  }

  public Boolean isRecAckProcessed()
  {
    return _isRecAckProcessed;
  }

  public Boolean isRejected()
  {
    return _isRejected;
  }

  public String getDocTransStatus()
  {
    return _docTransStatus;
  }

  public String getCreateBy()
  {
    return _createBy;
  }

  public Long getSenderNodeId()
  {
    return _senderNodeId;
  }

  public Long getSenderGdocId()
  {
    return _senderGdocId;
  }

  public String getSenderRoute()
  {
    return _senderRoute;
  }

  public String getSenderPartnerFunction()
  {
    return _senderPartnerFunction;
  }

  public String getSenderUserId()
  {
    return _senderUserId;
  }

  public String getSenderUserName()
  {
    return _senderUserName;
  }

  public String getSenderBizEntityId()
  {
    return _senderBizEntityId;
  }

  public String getSenderPartnerType()
  {
    return _senderPartnerType;
  }

  public String getSenderPartnerGroup()
  {
    return _senderPartnerGroup;
  }

  public String getSenderPartnerName()
  {
    return _senderPartnerName;
  }

  public Long getRefGdocId()
  {
    return _refGdocId;
  }

  public String getRefUdocFilename()
  {
    return _refUdocFilename;
  }

  public Long getRecipientNodeId()
  {
    return _recipientNodeId;
  }

  public String getRecipientPartnerId()
  {
    return _recipientPartnerId;
  }

  public String getRecipientPartnerFunction()
  {
    return _recipientPartnerFunction;
  }

  public Integer getEncryptionLevel()
  {
    return _encryptionLevel;
  }

  public String getRecipientPartnerType()
  {
    return _recipientPartnerType;
  }

  public String getRecipientPartnerGroup()
  {
    return _recipientPartnerGroup;
  }

  public String getRecipientPartnerName()
  {
    return _recipientPartnerName;
  }

  public String getRecipientBizEntityId()
  {
    return _recipientBizEntityId;
  }

  public Long getRecipientGdocId()
  {
    return _recipientGdocId;
  }

  public Long getRecipientChannelUid()
  {
    return _recipientChannelUid;
  }

  public String getRecipientChannelName()
  {
    return _recipientChannelName;
  }

  public String getRecipientChannelProtocol()
  {
    return _recipientChannelProtocol;
  }

  public Long getPortUid()
  {
    return _portUid;
  }

  public String getPortName()
  {
    return _portName;
  }

  public Date getDateTimeCreate()
  {
    return _dateTimeCreate;
  }

  public Date getDateTimeSendEnd()
  {
    return _dateTimeSendEnd;
  }

  public Date getDateTimeReceiveEnd()
  {
    return _dateTimeReceiveEnd;
  }

  public Date getDateTimeSendStart()
  {
    return _dateTimeSendStart;
  }

  public Date getDateTimeTransComplete()
  {
    return _dateTimeTransComplete;
  }

  public Date getDateTimeExport()
  {
    return _dateTimeExport;
  }

  public Date getDateTimeImport()
  {
    return _dateTimeImport;
  }

  public Date getDateTimeReceiveStart()
  {
    return _dateTimeReceiveStart;
  }

  public Date getDateTimeView()
  {
    return _dateTimeView;
  }

  public Date getDateTimeRecipientView()
  {
    return _dateTimeRecipientView;
  }

  public Date getDateTimeRecipientExport()
  {
    return _dateTimeRecipientExport;
  }

  public List getActivityList()
  {
    return _activityList;
  }

  public String getPartnerFunction()
  {
    return _partnerFunction;
  }

  public String getActionCode()
  {
    return _actionCode;
  }

  public String getReceiveTrxId()
  {
    return _receiveTrxId;
  }

  public Long getRnProfileUid()
  {
    return _rnProfileUid;
  }

  public String getProcessDefId()
  {
    return _processDefId;
  }

  public Boolean isRequest()
  {
    return _isRequest;
  }

  public Boolean hasAttachment()
  {
    return _hasAttachment;
  }

  public Boolean isAttachmentLinkUpdated()
  {
    return _isAttachmentLinkUpdated;
  }

  public List getAttachments()
  {
    return _attachments;
  }

  public Integer getTriggerType()
  {
    return _triggerType;
  }

  public String getCustom1()
  {
    return _custom1;
  }

  public String getCustom2()
  {
    return _custom2;
  }

  public String getCustom3()
  {
    return _custom3;
  }

  public String getCustom4()
  {
    return _custom4;
  }

  public String getCustom5()
  {
    return _custom5;
  }

  public String getCustom6()
  {
    return _custom6;
  }

  public String getCustom7()
  {
    return _custom7;
  }

  public String getCustom8()
  {
    return _custom8;
  }

  public String getCustom9()
  {
    return _custom9;
  }

  public String getCustom10()
  {
    return _custom10;
  }

  public String getOBPayloadFile()
  {
    return _OBPayloadFile;
  }

  public String getAuditFileName()
  {
    return _auditFileName;
  }

  public String getReceiptAuditFileName()
  {
    return _receiptAuditFileName;
  }

  public String getMessageDigest()
  {
    return _messageDigest;
  }
  // *************** Setters for attributes *************************

  public void setGdocId(Long gdocId)
  {
    _gdocId = gdocId;
  }

  public void setGdocFilename(String gdocFilename)
  {
    _gdocFilename = gdocFilename;
  }

  public void setFolder(String folder)
  {
    _folder = folder;
  }

  public void setSenderPartnerId(String senderPartnerId)
  {
    _senderPartnerId = senderPartnerId;
  }

  public void setUdocNum(String udocNum)
  {
    _udocNum = udocNum;
  }

  public void setRefUdocNum(String refUdocNum)
  {
    _refUdocNum = refUdocNum;
  }

  public void setUdocFilename(String udocFilename)
  {
    _udocFilename = udocFilename;
  }

  public void setUdocVersion(Integer udocVersion)
  {
    _udocVersion = udocVersion;
  }

  public void setUdocDocType(String udocDocType)
  {
    _udocDocType = udocDocType;
  }

  public void setUdocFileType(String udocFileType)
  {
    _udocFileType = udocFileType;
  }

  public void setSrcFolder(String srcFolder)
  {
    _srcFolder = srcFolder;
  }

  public void setNotifyUserEmail(String notifyUserEmail)
  {
    _notifyUserEmail = notifyUserEmail;
  }

  public void setUniqueDocIdentifier(String uniqueDocIdentifier)
  {
    _uniqueDocIdentifier = uniqueDocIdentifier;
  }

  public void setUdocFullPath(String udocFullPath)
  {
    _udocFullPath = udocFullPath;
  }

  public void setExportedUdocFullPath(String exportedUdocFullPath)
  {
    _exportedUdocFullPath = exportedUdocFullPath;
  }

  public void setUdocFileSize(Long udocFileSize)
  {
    _udocFileSize = udocFileSize;
  }

  public void setExported(Boolean isExported)
  {
    _isExported = isExported;
  }

  public void setViewAckReq(Boolean isViewAckReq)
  {
    _isViewAckReq = isViewAckReq;
  }

  public void setExportAckReq(Boolean isExportAckReq)
  {
    _isExportAckReq = isExportAckReq;
  }

  public void setReceiveAckReq(Boolean isReceiveAckReq)
  {
    _isReceiveAckReq = isReceiveAckReq;
  }

  public void setViewed(Boolean isViewed)
  {
    _isViewed = isViewed;
  }

  public void setSent(Boolean isSent)
  {
    _isSent = isSent;
  }

  public void setLocalPending(Boolean isLocalPending)
  {
    _isLocalPending = isLocalPending;
  }

  public void setExpired(Boolean isExpired)
  {
    _isExpired = isExpired;
  }

  public void setRejected(Boolean isRejected)
  {
    _isRejected = isRejected;
  }

  public void setDocTransStatus(String docTransStatus)
  {
    _docTransStatus = docTransStatus;
  }

  public void setRecAckProcessed(Boolean isRecAckProcessed)
  {
    _isRecAckProcessed = isRecAckProcessed;
  }

  public void setCreateBy(String createBy)
  {
    _createBy = createBy;
  }

  public void setSenderNodeId(Long senderNodeId)
  {
    _senderNodeId = senderNodeId;
  }

  public void setSenderGdocId(Long senderGdocId)
  {
    _senderGdocId = senderGdocId;
  }

  public void setSenderRoute(String senderRoute)
  {
    _senderRoute = senderRoute;
  }

  public void setSenderPartnerFunction(String senderPartnerFunction)
  {
    _senderPartnerFunction = senderPartnerFunction;
  }

  public void setSenderUserId(String senderUserId)
  {
    _senderUserId = senderUserId;
  }

  public void setSenderUserName(String senderUserName)
  {
    _senderUserName = senderUserName;
  }

  public void setSenderBizEntityId(String senderBizEntityId)
  {
    _senderBizEntityId = senderBizEntityId;
  }

  public void setSenderPartnerType(String senderPartnerType)
  {
    _senderPartnerType = senderPartnerType;
  }

  public void setSenderPartnerGroup(String senderPartnerGroup)
  {
    _senderPartnerGroup = senderPartnerGroup;
  }

  public void setSenderPartnerName(String senderPartnerName)
  {
    _senderPartnerName = senderPartnerName;
  }

  public void setRefGdocId(Long refGdocId)
  {
    _refGdocId = refGdocId;
  }

  public void setRefUdocFilename(String refUdocFilename)
  {
    _refUdocFilename = refUdocFilename;
  }

  public void setRecipientNodeId(Long recipientNodeId)
  {
    _recipientNodeId = recipientNodeId;
  }

  public void setRecipientPartnerId(String recipientPartnerId)
  {
    _recipientPartnerId = recipientPartnerId;
  }

  public void setRecipientPartnerFunction(String recipientPartnerFunction)
  {
    _recipientPartnerFunction = recipientPartnerFunction;
  }

  public void setEncryptionLevel(Integer encryptionLevel)
  {
    _encryptionLevel = encryptionLevel;
  }

  public void setRecipientPartnerType(String recipientPartnerType)
  {
    _recipientPartnerType = recipientPartnerType;
  }

  public void setRecipientPartnerGroup(String recipientPartnerGroup)
  {
    _recipientPartnerGroup = recipientPartnerGroup;
  }

  public void setRecipientPartnerName(String recipientPartnerName)
  {
    _recipientPartnerName = recipientPartnerName;
  }

  public void setRecipientBizEntityId(String recipientBizEntityId)
  {
    _recipientBizEntityId = recipientBizEntityId;
  }

  public void setRecipientGdocId(Long recipientGdocId)
  {
    _recipientGdocId = recipientGdocId;
  }

  public void setRecipientChannelUid(Long recipientChannelUid)
  {
    _recipientChannelUid = recipientChannelUid;
  }

  public void setRecipientChannelName(String recipientChannelName)
  {
    _recipientChannelName = recipientChannelName;
  }

  public void setRecipientChannelProtocol(String recipientChannelProtocol)
  {
    _recipientChannelProtocol = recipientChannelProtocol;
  }

  public void setPortUid(Long portUid)
  {
    _portUid = portUid;
  }

  public void setPortName(String portName)
  {
    _portName = portName;
  }

  public void setDateTimeCreate(Date dateTimeCreate)
  {
    _dateTimeCreate = dateTimeCreate;
  }

  public void setDateTimeSendEnd(Date dateTimeSendEnd)
  {
    _dateTimeSendEnd = dateTimeSendEnd;
  }

  public void setDateTimeReceiveEnd(Date dateTimeReceiveEnd)
  {
    _dateTimeReceiveEnd = dateTimeReceiveEnd;
  }

  public void setDateTimeSendStart(Date dateTimeSendStart)
  {
    _dateTimeSendStart = dateTimeSendStart;
  }

  public void setDateTimeTransComplete(Date dateTimeTransComplete)
  {
    _dateTimeTransComplete = dateTimeTransComplete;
  }

  public void setDateTimeExport(Date dateTimeExport)
  {
    _dateTimeExport = dateTimeExport;
  }

  public void setDateTimeImport(Date dateTimeImport)
  {
    _dateTimeImport = dateTimeImport;
  }

  public void setDateTimeReceiveStart(Date dateTimeReceiveStart)
  {
    _dateTimeReceiveStart = dateTimeReceiveStart;
  }

  public void setDateTimeView(Date dateTimeView)
  {
    _dateTimeView = dateTimeView;
  }

  public void setDateTimeRecipientView(Date dateTimeRecipientView)
  {
    _dateTimeRecipientView = dateTimeRecipientView;
  }

  public void setDateTimeRecipientExport(Date dateTimeRecipientExport)
  {
    _dateTimeRecipientExport = dateTimeRecipientExport;
  }

  public void addActivity(Activity activity)
  {
    _activityList.add(activity);
  }

  public void setPartnerFunction(String partnerFunction)
  {
    _partnerFunction = partnerFunction;
  }

  public void setActionCode(String actionCode)
  {
    _actionCode = actionCode;
  }

  public void setReceiveTrxId(String trxId)
  {
    _receiveTrxId = trxId;
  }

  public void setRnProfileUid(Long rnProfileUid)
  {
    _rnProfileUid = rnProfileUid;
  }

  public void setProcessDefId(String processDefId)
  {
    _processDefId = processDefId;
  }

  public void setIsRequest(Boolean isRequest)
  {
    _isRequest = isRequest;
  }

  public void setHasAttachment(Boolean hasAttachment)
  {
    _hasAttachment = hasAttachment;
  }

  public void setAttachmentLinkUpdated(Boolean isAttachmentLinkUpdated)
  {
    _isAttachmentLinkUpdated = isAttachmentLinkUpdated;
  }

  public void setAttachments(List attachments)
  {
    _attachments = attachments;
  }

  public void setTriggerType(Integer triggerType)
  {
    _triggerType = triggerType;
  }

  public void setCustom1(String custom1)
  {
    _custom1 = custom1;
  }

  public void setCustom2(String custom2)
  {
    _custom2 = custom2;
  }

  public void setCustom3(String custom3)
  {
    _custom3 = custom3;
  }

  public void setCustom4(String custom4)
  {
    _custom4 = custom4;
  }

  public void setCustom5(String custom5)
  {
    _custom5 = custom5;
  }

  public void setCustom6(String custom6)
  {
    _custom6 = custom6;
  }

  public void setCustom7(String custom7)
  {
    _custom7 = custom7;
  }

  public void setCustom8(String custom8)
  {
    _custom8 = custom8;
  }

  public void setCustom9(String custom9)
  {
    _custom9 = custom9;
  }

  public void setCustom10(String custom10)
  {
    _custom10 = custom10;
  }

  public Long getProcessInstanceUid()
  {
    return _processInstanceUid;
  }

  public void setProcessInstanceUid(Long value)
  {
    _processInstanceUid = value;
  }

  public String getProcessInstanceID()
  {
    return _processInstanceID;
  }

  public void setProcessInstanceID(String value)
  {
    _processInstanceID = value;
  }

  public String getUserTrackingID()
  {
    return _userTrackingID;
  }

  public void setUserTrackingID(String value)
  {
    _userTrackingID = value;
  }

  public String getRecipientBizEntityUuid()
  {
    return _recipientBizEntityUuid;
  }

  public String getRecipientRegistryQueryUrl()
  {
    return _recipientRegistryQueryUrl;
  }

  public String getSenderBizEntityUuid()
  {
    return _senderBizEntityUuid;
  }

  public String getSenderRegistryQueryUrl()
  {
    return _senderRegistryQueryUrl;
  }

  public void setRecipientBizEntityUuid(String recipientBizEntityUuid)
  {
    _recipientBizEntityUuid = recipientBizEntityUuid;
  }

  public void setRecipientRegistryQueryUrl(String recipientRegistryQueryUrl)
  {
    _recipientRegistryQueryUrl = recipientRegistryQueryUrl;
  }

  public void setSenderBizEntityUuid(String senderBizEntityUuid)
  {
    _senderBizEntityUuid = senderBizEntityUuid;
  }

  public void setSenderRegistryQueryUrl(String senderRegistryQueryUrl)
  {
    _senderRegistryQueryUrl = senderRegistryQueryUrl;
  }

  public void setOBPayloadFile(String payloadFile)
  {
    _OBPayloadFile = payloadFile;
  }

  public void setAuditFileName(String auditFileName)
  {
    _auditFileName = auditFileName;
  }

  public void setReceiptAuditFileName(String receiptAuditFileName)
  {
    _receiptAuditFileName = receiptAuditFileName;
  }

  public void setMessageDigest(String messageDigest)
  {
    _messageDigest = messageDigest;
  }

  public String getTempUdocFilename()
  {
    return _tempUdocFilename;
  }

  public void setTempUdocFilename(String tempUdocFilename)
  {
    _tempUdocFilename = tempUdocFilename;
  }
  
  //14 NOV 2005 TWX
	/**
	 * @return Returns the _docDateGen.
	 */
	public String getDocDateGen()
	{
		return _docDateGen;
	}

	/**
	 * @param dateGen The _docDateGen to set.
	 */
	public void setDocDateGen(String dateGen)
	{
		_docDateGen = dateGen;
	}

	/**
	 * @return Returns the _originalDoc.
	 */
	public Boolean isOriginalDoc()
	{
		return _originalDoc;
	}

	/**
	 * @param doc The _originalDoc to set.
	 */
	public void setOriginalDoc(Boolean doc)
	{
		_originalDoc = doc;
	}

	/**
	 * @return Returns the _senderCert.
	 */
	public Long getSenderCert()
	{
		return _senderCert;
	}

	/**
	 * @param cert The _senderCert to set.
	 */
	public void setSenderCert(Long cert)
	{
		_senderCert = cert;
	}

	/**
	 * @return Returns the _receiverCert.
	 */
	public Long getReceiverCert()
	{
		return _receiverCert;
	}

	/**
	 * @param cert The _receiverCert to set.
	 */
	public void setReceiverCert(Long cert)
	{
		_receiverCert = cert;
	}

  public String getTracingID()
  {
    return _tracingID;
  }

  public void setTracingID(String tracingid)
  {
    _tracingID = tracingid;
  }
  
  public boolean isRead()
  {
    return _isRead;
  }
  
  public void setRead(Boolean isRead)
  {
    _isRead = isRead;
  }
}
