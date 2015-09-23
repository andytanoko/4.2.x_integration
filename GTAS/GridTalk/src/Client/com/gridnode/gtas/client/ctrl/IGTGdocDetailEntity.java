/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTGdocDetailEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 15 Nov         Regina Zeng         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.document.IGridDocument;

/**
 * This interface is implemented on top of IGTGridDocumentEntity
 * 
 * @author Regina Zeng
 * 
 * @since GT 4.0
 */
public interface IGTGdocDetailEntity extends IGTEntity
{
  //Folder type definitions
  public static final String FOLDER_IMPORT        = IGridDocument.FOLDER_IMPORT;
  public static final String FOLDER_OUTBOUND      = IGridDocument.FOLDER_OUTBOUND;
  public static final String FOLDER_INBOUND       = IGridDocument.FOLDER_INBOUND;
  public static final String FOLDER_EXPORT        = IGridDocument.FOLDER_EXPORT;

  // Route tyoe definitions
  public static final String ROUTE_DIRECT         = IGridDocument.ROUTE_DIRECT;
  public static final String ROUTE_GRIDMASTER     = IGridDocument.ROUTE_GRIDMASTER;

  // Encryption type definitions
  public static final Integer ENCRYPT_LEVEL_64    = IGridDocument.ENCRYPT_LEVEL_64;
  public static final Integer ENCRYPT_LEVEL_256   = IGridDocument.ENCRYPT_LEVEL_256;
  public static final Integer ENCRYPT_LEVEL_512   = IGridDocument.ENCRYPT_LEVEL_512;
  public static final Integer ENCRYPT_LEVEL_1024  = IGridDocument.ENCRYPT_LEVEL_1024;

  // Fields
  public static final Number UID                      = IGridDocument.UID;
  public static final Number G_DOC_ID                 = IGridDocument.G_DOC_ID;
  public static final Number REF_G_DOC_ID             = IGridDocument.REF_G_DOC_ID;
  public static final Number G_DOC_FILENAME           = IGridDocument.G_DOC_FILENAME;
  public static final Number REF_U_DOC_NUM            = IGridDocument.REF_U_DOC_NUM;
  public static final Number U_DOC_FILENAME           = IGridDocument.U_DOC_FILENAME;
  public static final Number REF_U_DOC_FILENAME       = IGridDocument.REF_U_DOC_FILENAME;
  public static final Number U_DOC_VERSION            = IGridDocument.U_DOC_VERSION;
  public static final Number U_DOC_DOC_TYPE           = IGridDocument.U_DOC_DOC_TYPE;
  public static final Number U_DOC_FILESIZE           = IGridDocument.U_DOC_FILESIZE;
  public static final Number U_DOC_FILE_TYPE          = IGridDocument.U_DOC_FILE_TYPE;
  public static final Number IS_EXPORTED              = IGridDocument.IS_EXPORTED;
  public static final Number IS_VIEW_ACK_REQ          = IGridDocument.IS_VIEW_ACK_REQ;
  public static final Number IS_EXPORT_ACK_REQ        = IGridDocument.IS_EXPORT_ACK_REQ;
  public static final Number IS_RECEIVE_ACK_REQ       = IGridDocument.IS_RECEIVE_ACK_REQ;
  public static final Number IS_VIEWED                = IGridDocument.IS_VIEWED;
  public static final Number IS_SENT                  = IGridDocument.IS_SENT;
  public static final Number IS_LOCAL_PENDING         = IGridDocument.IS_LOCAL_PENDING;
  public static final Number IS_EXPIRED               = IGridDocument.IS_EXPIRED;
  public static final Number IS_REQ_ACK_PROCESSED     = IGridDocument.IS_REC_ACK_PROCESSED;
  public static final Number ENCRYPTION_LEVEL         = IGridDocument.ENCRYPTION_LEVEL;
  public static final Number FOLDER                   = IGridDocument.FOLDER;
  public static final Number CREATE_BY                = IGridDocument.CREATE_BY;
  public static final Number R_NODE_ID                = IGridDocument.R_NODE_ID;
  public static final Number R_PARTNER_ID             = IGridDocument.R_PARTNER_ID;
  public static final Number R_PARTNER_TYPE           = IGridDocument.R_PARTNER_TYPE;
  public static final Number R_PARTNER_GROUP          = IGridDocument.R_PARTNER_GROUP;
  public static final Number R_BIZ_ENTITY_ID          = IGridDocument.R_BIZ_ENTITY_ID;
  public static final Number R_PARTNER_FUNCTION       = IGridDocument.R_PARTNER_FUNCTION;
  public static final Number R_G_DOC_ID               = IGridDocument.R_G_DOC_ID;
  public static final Number S_NODE_ID                = IGridDocument.S_NODE_ID;
  public static final Number S_G_DOC_ID               = IGridDocument.S_G_DOC_ID;
  public static final Number S_PARTNER_FUNCTION       = IGridDocument.S_PARTNER_FUNCTION;
  public static final Number S_USER_ID                = IGridDocument.S_USER_ID;
  public static final Number S_USER_NAME              = IGridDocument.S_USER_NAME;
  public static final Number S_BIZ_ENTITY_ID          = IGridDocument.S_BIZ_ENTITY_ID;
  public static final Number S_ROUTE                  = IGridDocument.S_ROUTE;
  public static final Number S_PARTNER_ID             = IGridDocument.S_PARTNER_ID;
  public static final Number S_PARTNER_TYPE           = IGridDocument.S_PARTNER_TYPE;
  public static final Number S_PARTNER_GROUP          = IGridDocument.S_PARTNER_GROUP;
  public static final Number DT_IMPORT                = IGridDocument.DT_IMPORT;
  public static final Number DT_SEND_END              = IGridDocument.DT_SEND_END;
  public static final Number DT_RECEIVE_END           = IGridDocument.DT_RECEIVE_END;
  public static final Number DT_EXPORT                = IGridDocument.DT_EXPORT;
  public static final Number DT_CREATE                = IGridDocument.DT_CREATE;
  public static final Number DT_TRANSACTION_COMPLETE  = IGridDocument.DT_TRANSACTION_COMPLETE;
  public static final Number DT_RECEIVE_START         = IGridDocument.DT_RECEIVE_START;
  public static final Number DT_VIEW                  = IGridDocument.DT_VIEW;
  public static final Number DT_SEND_START            = IGridDocument.DT_SEND_START;
  public static final Number DT_RECIPIENT_VIEW        = IGridDocument.DT_RECIPIENT_VIEW;
  public static final Number DT_RECIPIENT_EXPORT      = IGridDocument.DT_RECIPIENT_EXPORT;
  public static final Number PARTNER_FUNCTION         = IGridDocument.PARTNER_FUNCTION;
  public static final Number PORT_UID                 = IGridDocument.PORT_UID;
  public static final Number ACTION_CODE              = IGridDocument.ACTION_CODE;
  public static final Number SOURCE_FOLDER            = IGridDocument.SOURCE_FOLDER;
  public static final Number NOTIFY_USER_EMAIL        = IGridDocument.NOTIFY_USER_EMAIL;
  public static final Number ACTIVITY_LIST            = IGridDocument.ACTIVITY_LIST;
  public static final Number R_CHANNEL_UID            = IGridDocument.R_CHANNEL_UID;
  public static final Number R_CHANNEL_NAME           = IGridDocument.R_CHANNEL_NAME;
  public static final Number R_CHANNEL_PROTOCOL       = IGridDocument.R_CHANNEL_PROTOCOL;
  public static final Number R_PARTNER_NAME           = IGridDocument.R_PARTNER_NAME;
  public static final Number S_PARTNER_NAME           = IGridDocument.S_PARTNER_NAME;
  public static final Number PORT_NAME                = IGridDocument.PORT_NAME;
  //20021205AH
  public static final Number ATTACHMENTS              = IGridDocument.ATTACHMENTS;
  public static final Number IS_ATTACHMENT_LINK_UPDATED = IGridDocument.IS_ATTACHMENT_LINK_UPDATED;
  //20030820AH
  public static final Number PROCESS_INSTANCE_ID      = IGridDocument.PROCESS_INSTANCE_ID; //20030821AH
  
  //20030825AH
  public static final Number PROCESS_INSTANCE_UID     = IGridDocument.PROCESS_INSTANCE_UID; //20030825AH
  
  //VFields:
  //20021209AH
  /**
   * The ATTACHMENT_FILENAMES field is a convienience vfield (LOD) to make it easy to
   * manage the attachment files on the gdoc using the existing file rendering code.
   * The value it returns is simply a Collection of String filenames taken from the
   * FILENAME fields of the attachment entities refered to by the ATTACHMENTS field.
   */
  public static final Number ATTACHMENT_FILENAMES     = new Integer(-10);
  /**
   * The DOC_REMARKS field is from documentMetaInfo
   */
  public static final Number DOC_REMARKS              = new Integer(-20);     
  /**
   * The DOC_META_INFO_UID field is from documentMetaInfo 
   */
  public static final Number DOC_META_INFO_UID        = new Integer(-30);
  /**
   * The SENDER_CERT_NAME field is from certificate
   */
  public static final Number SENDER_CERT_NAME         = new Integer(-40);
  /**
   * The RECEIVER_CERT_NAME field is from certificate
   */
  public static final Number RECEIVER_CERT_NAME       = new Integer(-50);
  
  
  //20061213 TWX change to vfield we won't rely on the gdoc to provide the info, instead we 
  //             based on documentmetainfo entity
  public static final Number AUDIT_FILENAME           = new Integer(-60);
  public static final Number RECEIPT_AUDIT_FILENAME   = new Integer(-70);
  public static final Number HAS_ATTACHMENT           = new Integer(-80);
  public static final Number SENDER_CERT              = new Integer(-90);
  public static final Number RECEIVER_CERT            = new Integer(-100);
  public static final Number USER_TRACKING_ID         = new Integer(-110);
  public static final Number U_DOC_NUM                = new Integer(-120);
  public static final Number DOC_TRANS_STATUS         = new Integer(-130);
}
