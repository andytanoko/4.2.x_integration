/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAuditTrailConstant.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 24, 2006    Tam Wei Xiang       Created
 * Dec 26, 2006    Tam Wei Xiang       Added the Document Flow type
 * Jan 19, 2007    Tam Wei Xiang       Added IB, OB abbreviation
 * Feb 02, 2007    Tam Wei Xang        New event: Reprocess Doc
 */
package com.gridnode.gtas.audit.common;

/**
 * Contains the constants which shared by both GTAT and ISAT component 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public interface IAuditTrailConstant
{
  public static final String DIRECTION_OB = "Outbound";
  public static final String DIRECTION_IB = "Inbound";
  public static final String OB_IN_ABBREVIATION = "OB";
  public static final String IB_IN_ABBREVIATION = "IB";
  public static final String IP_IN_ABBREVIATION = "IP";
  
  public static final String PROCESS_STATE_RUNNING = "Running";
  public static final String PROCESS_STATE_COMPLETED = "Complete";
  public static final String PROCESS_STATE_ABNORMALLY_COMPLETED = "Abnormally Completed";
  public static final String PROCESS_STATE_ABNORMALLY_TERMINATED = "Abnormally Terminated";
  public static final String PROCESS_STATE_ABNORMALLY_ABORTED = "Abnormally Aborted";
  
  public static final String SIGNAL_MESSAGE_ACK = "RN_ACK";
  public static final String SIGNAL_MESSAGE_EXP = "RN_EXCEPTION";
  
  public static final String EVENT_TYPE_TRANS = "Trans";
  public static final String EVENT_TYPE_SIGNAL = "Sig";
  
  //Document Flow Type
  public static final String DOCUMENT_RECEIVED = "Document Received";
  public static final String UNPACK_PAYLOAD = "Unpack Payload";
  public static final String DOCUMENT_DELIVERED = "Document Delivery";
  public static final String PACK_PAYLOAD = "Pack Payload";
  public static final String DOCUMENT_ACKNOWLEDGED = "Document Acknowledged";
  
  public static final String DOCUMENT_IMPORT = "Document Import";
  public static final String OUTBOUND_PROCESSING_START = "Outbound Processing Start";
  public static final String OUTBOUND_PROCESSING_END = "Outbound Processing End";
  public static final String EXPORT_PROCESSING_START = "Export Processing Start";
  public static final String EXPORT_PROCESSING_END = "Export Processing End";
  
  public static final String MAPPING_RULE = "Mapping Rule";
  public static final String DOCUMENT_EXPORT = "Document Export"; //Exit To Port
  public static final String USER_PROCEDURE = "Procedure";
  public static final String CHANNEL_CONNECTIVITY = "Channel Connectivity";
  
  public static final String REPROCESS_DOC = "Reprocess Doc";
  public static final String PROCESS_INJECTION = "Process Injection";
  //end Document flow type
  
  public static final String STATUS_SUCCESS = "OK";
  public static final String STATUS_FAIL = "FAILED";
  
  public static final String SELF_INITIATOR = "SELF";
  
  //JMSHandler JNDI Name
  public static final String FAILED_JMS_CAT = "AT";
}
