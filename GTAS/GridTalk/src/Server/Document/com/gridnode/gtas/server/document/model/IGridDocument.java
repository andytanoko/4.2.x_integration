/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGridDocument.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 31 2002    Koh Han Sing        Created
 * Nov 22 2002    Koh Han Sing        Add in attachment fields
 * May 22 2003    Koh Han Sing        Add in UdocFullPath, UniqueDocIdentifier
 *                                    and ExportedUdocFullPath
 * Jun 20 2003    Koh Han Sing        Add in IsRejected
 * Jul 09 2003    Koh Han Sing        Add in 10 custom fields
 * Aug 21 2003    Guo Jianyu          Add ProcessInstanceID and UserTrackingID
 * Oct 01 2003    Neo Sok Lay         Add fields:
 *                                    - S_BIZ_ENTITY_UUID
 *                                    - S_REGISTRY_QUERY_URL
 *                                    - R_BIZ_ENTITY_UUID
 *                                    - R_REGISTRY_QUERY_URL
 * Oct 20 2003    Neo Sok Lay         Add field:
 *                                    - OB_PAYLOAD_FILE
 * Oct 23 2003    Guo Jianyu          Add DocTransStatus
 * Dec 30 2003    Guo Jianyu          Add fields:
 *                                    - MessageDigest
 *                                    - AuditFileName
 *                                    - ReceiptAuditFileName
 * Oct 20 2006    Tam Wei Xiang       Added field TRACING_ID
 * Oct 07 2010    Tam Wei Xiang       #1897 Added field IS_READ                                   
 */
package com.gridnode.gtas.server.document.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in GridDocument entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.3 I1
 * @since 2.0
 */
public interface IGridDocument
{
  /**
   * Name for GridDocument entity.
   */
  public static final String ENTITY_NAME = "GridDocument";

  /**
   * Folder for Imported documents
   */
  public static final String FOLDER_IMPORT = "Import";

  /**
   * Folder for Outbound documents
   */
  public static final String FOLDER_OUTBOUND = "Outbound";

  /**
   * Folder for Inbound documents
   */
  public static final String FOLDER_INBOUND = "Inbound";

  /**
   * Folder for Exported documents
   */
  public static final String FOLDER_EXPORT = "Export";

  /**
   * DIRECT to Partner
   */
  public static final String ROUTE_DIRECT = "DIRECT";

  /**
   * ROUTE through GridMaster
   */
  public static final String ROUTE_GRIDMASTER = "GRIDMASTER";

  /**
   * EncryptionLevel with 64-bit key
   */
  public static final Integer ENCRYPT_LEVEL_64 = new Integer(64);

  /**
   * EncryptionLevel with 256-bit key
   */
  public static final Integer ENCRYPT_LEVEL_256 = new Integer(256);

  /**
   * EncryptionLevel with 512-bit key
   */
  public static final Integer ENCRYPT_LEVEL_512 = new Integer(512);

  /**
   * EncryptionLevel with 1024-bit key
   */
  public static final Integer ENCRYPT_LEVEL_1024 = new Integer(1024);

  /**
   * Trigger on Import
   */
  public static final Integer TRIGGER_IMPORT = new Integer(0);

  /**
   * Trigger on Receive
   */
  public static final Integer TRIGGER_RECEIVE = new Integer(1);

  /**
   * Trigger on Manual Send
   */
  public static final Integer TRIGGER_MANUAL_SEND = new Integer(2);

  /**
   * Trigger on Manual Export
   */
  public static final Integer TRIGGER_MANUAL_EXPORT = new Integer(3);

  /**
   * FieldId for UID. A Number.
   */
  public static final Number UID = new Integer(0);  //Long

  /**
   * FieldId for Whether-the-GridDocument-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(1); //Boolean

  /**
   * FieldId for Version. A double.
   */
  public static final Number VERSION = new Integer(2); //Double

  /**
   * FieldId for GdocId. A Number.
   */
  public static final Number G_DOC_ID = new Integer(3);  //Long

  /**
   * FieldId for RefGdocId. A Number.
   */
  public static final Number REF_G_DOC_ID = new Integer(4);  //Long

  /**
   * FieldId for GdocFilename. A String.
   */
  public static final Number G_DOC_FILENAME = new Integer(5);  //String(80)

  /**
   * FieldId for UdocNum. A String.
   */
  public static final Number U_DOC_NUM = new Integer(6);  //String(20)

  /**
   * FieldId for RefUdocNum. A String.
   */
  public static final Number REF_U_DOC_NUM = new Integer(7);  //String(20)

  /**
   * FieldId for UdocFilename. A String.
   */
  public static final Number U_DOC_FILENAME = new Integer(8);  //String(80)

  /**
   * FieldId for RefUdocFilename. A String.
   */
  public static final Number REF_U_DOC_FILENAME = new Integer(9);  //String(80)

  /**
   * FieldId for UdocVersion. A Integer.
   */
  public static final Number U_DOC_VERSION = new Integer(10);  //Integer

  /**
   * FieldId for UdocDocType. A String.
   */
  public static final Number U_DOC_DOC_TYPE = new Integer(11);  //String(12)

  /**
   * FieldId for UdocFileSize. A Long. Not persistent in DB
   */
  public static final Number U_DOC_FILESIZE = new Integer(12);  //Long

  /**
   * FieldId for UdocFileType. A String.
   */
  public static final Number U_DOC_FILE_TYPE = new Integer(13);  //String(10)

  /**
   * FieldId for Exported flag. A Boolean.
   */
  public static final Number IS_EXPORTED = new Integer(14);  //Boolean

  /**
   * FieldId for ViewAckReq flag. A Boolean.
   */
  public static final Number IS_VIEW_ACK_REQ = new Integer(15);  //Boolean

  /**
   * FieldId for ExportAckReq flag. A Boolean.
   */
  public static final Number IS_EXPORT_ACK_REQ = new Integer(16);  //Boolean

  /**
   * FieldId for ReceiveAckReq flag. A Boolean.
   */
  public static final Number IS_RECEIVE_ACK_REQ = new Integer(17);  //Boolean

  /**
   * FieldId for Viewed flag. A Boolean.
   */
  public static final Number IS_VIEWED = new Integer(18);  //Boolean

  /**
   * FieldId for Sent flag. A Boolean.
   */
  public static final Number IS_SENT = new Integer(19);  //Boolean

  /**
   * FieldId for LocalPending flag. A Boolean.
   */
  public static final Number IS_LOCAL_PENDING = new Integer(20);  //Boolean

  /**
   * FieldId for Expired flag. A Boolean.
   */
  public static final Number IS_EXPIRED = new Integer(21);  //Boolean

  /**
   * FieldId for RecAckProcessed flag. A Boolean.
   */
  public static final Number IS_REC_ACK_PROCESSED = new Integer(22);  //Boolean

  /**
   * FieldId for EncryptionLevel. A Number.
   *
   * @see #ENCRYPT_LEVEL_64
   * @see #ENCRYPT_LEVEL_256
   * @see #ENCRYPT_LEVEL_512
   * @see #ENCRYPT_LEVEL_1024
   */
  public static final Number ENCRYPTION_LEVEL = new Integer(23);  //Integer

  /**
   * FieldId for Folder. A String.
   */
  public static final Number FOLDER = new Integer(24);  //String(10)

  /**
   * FieldId for CreateBy. A String.
   */
  public static final Number CREATE_BY = new Integer(25);  //String(15)

  /**
   * FieldId for RecipientNodeId. An Integer.
   */
  public static final Number R_NODE_ID = new Integer(26);  //Long

  /**
   * FieldId for RecipientPartnerId. A String.
   */
  public static final Number R_PARTNER_ID = new Integer(27);  //String(15)

  /**
   * FieldId for RecipientPartnerType. A String.
   */
  public static final Number R_PARTNER_TYPE = new Integer(28);  //String

  /**
   * FieldId for RecipientPartnerGroup. A String.
   */
  public static final Number R_PARTNER_GROUP = new Integer(29);  //String

  /**
   * FieldId for RecipientBizEntityId. A String.
   */
  public static final Number R_BIZ_ENTITY_ID = new Integer(30);  //String

  /**
   * FieldId for RecipientPartnerFunction. A String.
   */
  public static final Number R_PARTNER_FUNCTION = new Integer(31);  //String(4)

  /**
   * FieldId for RecipientGdocId. A Number.
   */
  public static final Number R_G_DOC_ID = new Integer(32);  //Long

  /**
   * FieldId for SenderNodeId. An Integer.
   */
  public static final Number S_NODE_ID = new Integer(33);  //Long

  /**
   * FieldId for SenderGdocId. A Number.
   */
  public static final Number S_G_DOC_ID = new Integer(34);  //Long

  /**
   * FieldId for SenderPartnerFunction. A String. Not persistent in DB.
   */
  public static final Number S_PARTNER_FUNCTION = new Integer(35);  //String

  /**
   * FieldId for SenderUserId. A String.
   */
  public static final Number S_USER_ID = new Integer(36);  //String

  /**
   * FieldId for SenderUserName. A String.
   */
  public static final Number S_USER_NAME = new Integer(37);  //String

  /**
   * FieldId for SenderBizEntityId. A String.
   */
  public static final Number S_BIZ_ENTITY_ID = new Integer(38);  //String

  /**
   * FieldId for SenderRoute. A String.
   */
  public static final Number S_ROUTE = new Integer(39);  //String

  /**
   * FieldId for SenderPartnerId. A String.
   */
  public static final Number S_PARTNER_ID = new Integer(40);  //String(15)

  /**
   * FieldId for SenderPartnerType. A String.
   */
  public static final Number S_PARTNER_TYPE = new Integer(41);  //String

  /**
   * FieldId for SenderPartnerGroup. A String.
   */
  public static final Number S_PARTNER_GROUP = new Integer(42);  //String

  /**
   * FieldId for DateTimeImport. A {@link java.util.Date Date} or
   * {#link java.sql.Timestamp Timestamp}.
   */
  public static final Number DT_IMPORT = new Integer(43);  //Date

  /**
   * FieldId for DateTimeSendEnd. A {@link java.util.Date Date} or
   * {#link java.sql.Timestamp Timestamp}.
   */
  public static final Number DT_SEND_END = new Integer(44);  //Date

  /**
   * FieldId for DateTimeReceiveEnd. A {@link java.util.Date Date} or
   * {#link java.sql.Timestamp Timestamp}.
   */
  public static final Number DT_RECEIVE_END = new Integer(45);  //Date

  /**
   * FieldId for DateTimeExport. A {@link java.util.Date Date} or
   * {#link java.sql.Timestamp Timestamp}.
   */
  public static final Number DT_EXPORT = new Integer(46);  //Date

  /**
   * FieldId for DateTimeCreate. A {@link java.util.Date Date} or
   * {#link java.sql.Timestamp Timestamp}.
   */
  public static final Number DT_CREATE = new Integer(47);  //Date

  /**
   * FieldId for DateTimeTransComplete. A {@link java.util.Date Date} or
   * {#link java.sql.Timestamp Timestamp}.
   */
  public static final Number DT_TRANSACTION_COMPLETE = new Integer(48);  //Date

  /**
   * FieldId for DateTimeReceiveStart. A {@link java.util.Date Date} or
   * {#link java.sql.Timestamp Timestamp}.
   */
  public static final Number DT_RECEIVE_START = new Integer(49);  //Date

  /**
   * FieldId for DateTimeView. A {@link java.util.Date Date} or
   * {#link java.sql.Timestamp Timestamp}.
   */
  public static final Number DT_VIEW = new Integer(50);  //Date

  /**
   * FieldId for DateTimeSendStart. A {@link java.util.Date Date} or
   * {#link java.sql.Timestamp Timestamp}.
   */
  public static final Number DT_SEND_START = new Integer(51);  //Date

  /**
   * FieldId for DateTimeRecipientView. A {@link java.util.Date Date} or
   * {#link java.sql.Timestamp Timestamp}.
   */
  public static final Number DT_RECIPIENT_VIEW = new Integer(52);  //Date

  /**
   * FieldId for DateTimeRecipientExport. A {@link java.util.Date Date} or
   * {#link java.sql.Timestamp Timestamp}.
   */
  public static final Number DT_RECIPIENT_EXPORT = new Integer(53);  //Date

  /**
   * FieldId for PartnerFunction. A String. Not persistent in DB.
   */
  public static final Number PARTNER_FUNCTION = new Integer(54);  //String

  /**
   * FieldId for PortUid. A Number.
   */
  public static final Number PORT_UID = new Integer(55);  //Long

  /**
   * FieldId for ActionCode. A String. Not persistent in DB.
   */
  public static final Number ACTION_CODE = new Integer(56);  //String

  /**
   * FieldId for SrcFolder. A String.
   */
  public static final Number SOURCE_FOLDER = new Integer(57);  //String(10)

  /**
   * FieldId for NotifyUserEmail. A String.
   */
  public static final Number NOTIFY_USER_EMAIL = new Integer(58);  //String(50)

  /**
   * FieldId for ActivityList. A List of
   * {@link com.gridnode.gtas.server.document.model.Activity}. Not persistent
   * in DB.
   */
  public static final Number ACTIVITY_LIST = new Integer(59);  //ArrayList

  /**
   * FieldId for RecipientChannelUid. A Number. Not persistent in DB.
   */
  public static final Number R_CHANNEL_UID = new Integer(60);  //Long

  /**
   * FieldId for RecipientChannelName. A String. Not persistent in DB.
   */
  public static final Number R_CHANNEL_NAME = new Integer(61);  //String(30)

  /**
   * FieldId for RecipientChannelProtocol. A String. Not persistent in DB.
   */
  public static final Number R_CHANNEL_PROTOCOL = new Integer(62);  //String(10)

  /**
   * FieldId for RecipientPartnerName. A String.
   */
  public static final Number R_PARTNER_NAME = new Integer(63);  //String(20)

  /**
   * FieldId for SenderPartnerName. A String.
   */
  public static final Number S_PARTNER_NAME = new Integer(64);  //String(20)

  /**
   * FieldId for PortName. A String.
   */
  public static final Number PORT_NAME = new Integer(65);  //String(15)

  /**
   * FieldId for RnProfileUid. A Long.
   */
  public static final Number RN_PROFILE_UID = new Integer(66);  //Long

  /**
   * FieldId for ProcessDefId. A String.
   */
  public static final Number PROCESS_DEF_ID = new Integer(67);  //String(80)

  /**
   * FieldId for IsRequest. A Boolean.
   */
  public static final Number IS_REQUEST = new Integer(68);  //Boolean

  /**
   * FieldId for HasAttachment. A Boolean.
   */
  public static final Number HAS_ATTACHMENT = new Integer(69);  //Boolean

  /**
   * FieldId for IsAttachmentLinkUpdated. A Boolean.
   */
  public static final Number IS_ATTACHMENT_LINK_UPDATED = new Integer(70);  //Boolean

  /**
   * FieldId for Attachments. A List.
   */
  public static final Number ATTACHMENTS = new Integer(71);  //ArrayList

  /**
   * FieldId for TriggerType. A Integer.
   */
  public static final Number TRIGGER_TYPE = new Integer(72);  //Integer

  /**
   * FieldId for UniqueDocIdentifier. A String.
   */
  public static final Number UNIQUE_DOC_IDENTIFIER = new Integer(73);  //String(80)

  /**
   * FieldId for UdocFullPath. A String.
   */
  public static final Number U_DOC_FULL_PATH = new Integer(74);  //String(255)

  /**
   * FieldId for ExportedUdocFullPath. A String.
   */
  public static final Number EXPORTED_U_DOC_FULL_PATH = new Integer(75);  //String(255)

  /**
   * FieldId for IsRejected. A Boolean.
   */
  public static final Number IS_REJECTED = new Integer(76);  //Boolean

  /**
   * FieldId for Custom1. A String.
   */
  public static final Number CUSTOM1 = new Integer(77);  //String

  /**
   * FieldId for Custom2. A String.
   */
  public static final Number CUSTOM2 = new Integer(78);  //String

  /**
   * FieldId for Custom3. A String.
   */
  public static final Number CUSTOM3 = new Integer(79);  //String

  /**
   * FieldId for Custom4. A String.
   */
  public static final Number CUSTOM4 = new Integer(80);  //String

  /**
   * FieldId for Custom5. A String.
   */
  public static final Number CUSTOM5 = new Integer(81);  //String

  /**
   * FieldId for Custom6. A String.
   */
  public static final Number CUSTOM6 = new Integer(82);  //String

  /**
   * FieldId for Custom7. A String.
   */
  public static final Number CUSTOM7 = new Integer(83);  //String

  /**
   * FieldId for Custom8. A String.
   */
  public static final Number CUSTOM8 = new Integer(84);  //String

  /**
   * FieldId for Custom9. A String.
   */
  public static final Number CUSTOM9 = new Integer(85);  //String

  /**
   * FieldId for Custom10. A String.
   */
  public static final Number CUSTOM10 = new Integer(86);  //String

  /**
   * FieldId for ProcessInstanceUid. A Long
   */
  public static final Number PROCESS_INSTANCE_UID = new Integer(87);  //Long

  /**
   * FieldId for ProcessInstanceID. A String.
   */
  public static final Number PROCESS_INSTANCE_ID = new Integer(88);  //String

  /**
   * FieldId for UserTrackingID. A String.
   */
  public static final Number USER_TRACKING_ID = new Integer(89);  //String

  /**
   * FieldId for SenderBizEntityUuid. A String.
   */
  public static final Number S_BIZ_ENTITY_UUID = new Integer(90);

  /**
   * FieldId for SenderRegistryQueryUrl. A String.
   */
  public static final Number S_REGISTRY_QUERY_URL = new Integer(91);

  /**
   * FieldId for RecipientBizEntityUuid. A String.
   */
  public static final Number R_BIZ_ENTITY_UUID = new Integer(92);

  /**
   * FieldId for RecipientRegistryQueryUrl. A String.
   */
  public static final Number R_REGISTRY_QUERY_URL = new Integer(93);

  /**
   * FieldId for OBPayloadFile. A String.
   */
  public static final Number OB_PAYLOAD_FILE = new Integer(94);


  /**
   * FieldId for HasDocTransFailed. A String
   */
  public static final Number DOC_TRANS_STATUS = new Integer(95); //String

  /**
   * FieldId for AuditFileName. A String
   */
  public static final Number AUDIT_FILE_NAME = new Integer(96); //String

  /**
   * FieldId for ReceiptAuditFileName. A String
   */
  public static final Number RECEIPT_AUDIT_FILE_NAME = new Integer(97);

  /**
   * FieldId for MessageDigest. A String
   */
  public static final Number MESSAGE_DIGEST = new Integer(98);
  
  /**
   * FieldId for Sender Cert. A Number
   */
  public static final Number SENDER_CERT = new Integer(99); //Long
  
  /**
   * FieldId for Trading Receiver Cert. A Number
   */
  public static final Number RECEIVER_CERT = new Integer(100); //Long
  
  /**
   * FieldId for Doc Date Generated. A Date
   */
  public static final Number DOC_DATE_GEN = new Integer(101); //String
  
  /**
   * FieldId for Original Doc. A Boolean
   */
  public static final Number ORIGINAL_DOC = new Integer(102); //Boolean
  
  /**
   * FieldId for Tracing ID. A String
   */
  public static final Number TRACING_ID = new Integer(103); //String
  
  /**
   * FieldId for IsRead. A boolean
   */
  public static final Number IS_READ = new Integer(104); //Boolean
}
