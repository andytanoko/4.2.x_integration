/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridDocumentEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 01 2002    Koh Han Sing        Created
 * Jun 24 2003    Neo Sok Lay         Add IS_REJECTED field.
 * Jul 09 2003    Koh Han Sing        Add in 10 custom fields
 * Oct 01 2003    Neo Sok Lay         Add fields:
 *                                    - S_BIZ_ENTITY_UUID
 *                                    - S_REGISTRY_QUERY_URL
 *                                    - R_BIZ_ENTITY_UUID
 *                                    - R_REGISTRY_QUERY_URL
 * Oct 20 2003    Neo Sok Lay         Add field:
 *                                    - OB_PAYLOAD_FILE
 * Dec 30 2003    Guo Jianyu          Add field: DOC_TRANS_STATUS
 * Dec 30 2003    Guo Jianyu          Add fields:
 *                                    - MessageDigest
 *                                    - AuditFileName
 *                                    - ReceiptAuditFileName
 * Nov 23 2006    Regina Zeng         Add field:
 *                                    - SenderCert
 *                                    - ReceiverCert                                   
 */
package com.gridnode.gtas.model.document;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the GridDocument module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Koh Han Sing
 *
 * @version 2.3 I1
 * @since 2.0
 */
public class GridDocumentEntityFieldID
{
  private Hashtable _table;
  private static GridDocumentEntityFieldID _self = null;

  private GridDocumentEntityFieldID()
  {
    _table = new Hashtable();

    //GridDocument
    _table.put(IGridDocument.ENTITY_NAME,
      new Number[]
      {
        IGridDocument.ACTION_CODE,
        IGridDocument.CREATE_BY,
        IGridDocument.DT_CREATE,
        IGridDocument.DT_EXPORT,
        IGridDocument.DT_IMPORT,
        IGridDocument.DT_RECEIVE_END,
        IGridDocument.DT_RECEIVE_START,
        IGridDocument.DT_RECIPIENT_EXPORT,
        IGridDocument.DT_RECIPIENT_VIEW,
        IGridDocument.DT_SEND_END,
        IGridDocument.DT_SEND_START,
        IGridDocument.DT_TRANSACTION_COMPLETE,
        IGridDocument.DT_VIEW,
        IGridDocument.ENCRYPTION_LEVEL,
        IGridDocument.FOLDER,
        IGridDocument.G_DOC_FILENAME,
        IGridDocument.G_DOC_ID,
        IGridDocument.IS_EXPIRED,
        IGridDocument.IS_EXPORTED,
        IGridDocument.IS_EXPORT_ACK_REQ,
        IGridDocument.IS_LOCAL_PENDING,
        IGridDocument.IS_RECEIVE_ACK_REQ,
        IGridDocument.IS_REC_ACK_PROCESSED,
        IGridDocument.IS_SENT,
        IGridDocument.IS_VIEWED,
        IGridDocument.IS_VIEW_ACK_REQ,
        IGridDocument.IS_REJECTED,
        IGridDocument.NOTIFY_USER_EMAIL,
        IGridDocument.PARTNER_FUNCTION,
        IGridDocument.PORT_NAME,
        IGridDocument.PORT_UID,
        IGridDocument.REF_G_DOC_ID,
        IGridDocument.REF_U_DOC_FILENAME,
        IGridDocument.REF_U_DOC_NUM,
        IGridDocument.R_BIZ_ENTITY_ID,
        IGridDocument.R_BIZ_ENTITY_UUID,
        IGridDocument.R_CHANNEL_NAME,
        IGridDocument.R_CHANNEL_PROTOCOL,
        IGridDocument.R_CHANNEL_UID,
        IGridDocument.R_G_DOC_ID,
        IGridDocument.R_NODE_ID,
        IGridDocument.R_PARTNER_FUNCTION,
        IGridDocument.R_PARTNER_GROUP,
        IGridDocument.R_PARTNER_ID,
        IGridDocument.R_PARTNER_NAME,
        IGridDocument.R_PARTNER_TYPE,
        IGridDocument.R_REGISTRY_QUERY_URL,
        IGridDocument.SOURCE_FOLDER,
        IGridDocument.S_BIZ_ENTITY_ID,
        IGridDocument.S_BIZ_ENTITY_UUID,
        IGridDocument.S_G_DOC_ID,
        IGridDocument.S_NODE_ID,
        IGridDocument.S_PARTNER_FUNCTION,
        IGridDocument.S_PARTNER_GROUP,
        IGridDocument.S_PARTNER_ID,
        IGridDocument.S_PARTNER_NAME,
        IGridDocument.S_PARTNER_TYPE,
        IGridDocument.S_REGISTRY_QUERY_URL,
        IGridDocument.S_ROUTE,
        IGridDocument.S_USER_ID,
        IGridDocument.S_USER_NAME,
        IGridDocument.UID,
        IGridDocument.U_DOC_DOC_TYPE,
        IGridDocument.U_DOC_FILENAME,
        IGridDocument.U_DOC_FILESIZE,
        IGridDocument.U_DOC_FILE_TYPE,
        IGridDocument.U_DOC_NUM,
        IGridDocument.U_DOC_VERSION,
        IGridDocument.RN_PROFILE_UID,
        IGridDocument.HAS_ATTACHMENT,
        IGridDocument.IS_ATTACHMENT_LINK_UPDATED,
        IGridDocument.ATTACHMENTS,
        IGridDocument.UNIQUE_DOC_IDENTIFIER,
        IGridDocument.U_DOC_FULL_PATH,
        IGridDocument.EXPORTED_U_DOC_FULL_PATH,
        IGridDocument.CUSTOM1,
        IGridDocument.CUSTOM2,
        IGridDocument.CUSTOM3,
        IGridDocument.CUSTOM4,
        IGridDocument.CUSTOM5,
        IGridDocument.CUSTOM6,
        IGridDocument.CUSTOM7,
        IGridDocument.CUSTOM8,
        IGridDocument.CUSTOM9,
        IGridDocument.CUSTOM10,
        IGridDocument.PROCESS_INSTANCE_ID,
        IGridDocument.PROCESS_INSTANCE_UID,
        IGridDocument.USER_TRACKING_ID,
        IGridDocument.OB_PAYLOAD_FILE,
        IGridDocument.DOC_TRANS_STATUS,
        IGridDocument.AUDIT_FILE_NAME,
        IGridDocument.RECEIPT_AUDIT_FILE_NAME,
        IGridDocument.MESSAGE_DIGEST,
        IGridDocument.SENDER_CERT,
        IGridDocument.RECEIVER_CERT
      });

    //Attachment
    _table.put(IAttachment.ENTITY_NAME,
      new Number[]
      {
        IAttachment.FILENAME,
        IAttachment.ORIGINAL_FILENAME,
        IAttachment.UID
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new GridDocumentEntityFieldID();
    }
    return _self._table;
  }
}