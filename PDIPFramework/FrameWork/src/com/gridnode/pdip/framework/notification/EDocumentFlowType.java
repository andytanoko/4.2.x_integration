/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EDocumentFlow.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 26, 2006    Tam Wei Xiang       Created
 * Feb 06, 2007    Tam Wei Xiang       Added new flow type : Process Injection
 */
package com.gridnode.pdip.framework.notification;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public enum EDocumentFlowType
{
  DOCUMENT_RECEIVED,
  UNPACK_PAYLOAD,
  DOCUMENT_DELIVERED,
  PACK_PAYLOAD,
  DOCUMENT_ACKNOWLEDGED,
  
  DOCUMENT_IMPORT,
  OUTBOUND_PROCESSING_START,
  OUTBOUND_PROCESSING_END,
  EXPORT_PROCESSING_START,
  EXPORT_PROCESSING_END,
  
  MAPPING_RULE,
  DOCUMENT_EXPORT, //Exit To Port
  USER_PROCEDURE,
  CHANNEL_CONNECTIVITY,
  PROCESS_INJECTION,
  REPROCESS_DOC
}
