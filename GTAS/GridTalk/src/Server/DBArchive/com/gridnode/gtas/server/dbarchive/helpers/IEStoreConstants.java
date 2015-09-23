/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEStoreConstants.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 5, 2006    Tam Wei Xiang       Created
 * Nov 13, 2006   Regina Zeng         Added ESTORE_FOLDER
 */
package com.gridnode.gtas.server.dbarchive.helpers;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public interface IEStoreConstants
{
	public final String ILLEGAL_CHAR = "/\\:*?\"<>| "; //white space is not a illegal char in
	                                                     //window, but illegal in unix
	public final String SIGNAL_MSG_RN_ACK = "RN_ACK";
	public final String SIGNAL_MSG_RN_EXCEPTION = "RN_EXCEPTION";
  public final String ESTORE_FOLDER = "estore";
	public final String TEMP_SUB_FOLDER = "tempContent";
	public final String BY_DOC_FOLDER_NAME = "By_Doc/";
	public final String BY_PI_FOLDER_NAME = "By_PI/";
	public final String CLEAN_UP_TYPE_ERROR = "err";
	
	//30 May 2006 EStore known exceptions code
  public static final String ESTORE_FAILED_NO_DOCS = "No Docs";
  public static final String ESTORE_FAILED_MISSING_AUDIT_FILES = "Missing Audit";
  public static final String ESTORE_FAILED_MISSING_RECEIPT_AUDIT_FILE = "Missing Receipt Audit";
  public static final String ESTORE_FAILED_MISSING_UDOC = "Missing User doc";
  //public static final String ESTORE_FAILED_EMPTY_DOC_NO = "Empty Doc No.";
  //public static final String ESTORE_FAILED_EMPTY_DOC_DATE = "Empty Doc Date";
  public static final String ESTORE_FAILED_MISSING_GDOC = "Missing Gdoc";
  public static final String ESTORE_FAILED_MISSING_ATTACHMENT = "Missing Attachment";
  public static final String ESTORE_FAILED_DOCS_FAILED = "Docs invalid";
  
  public static final String INBOUND_FOLDER = "Inbound/";
  public static final String OUTBOUND_FOLDER = "Outbound/";
  public static final String IMPORT_FOLDER = "Import/";
  public static final String EXPORT_FOLDER = "Export/";
  public static final String ATTACHMENT_FOLDER = "attachment/";
  public static final String AUDIT_FOLDER = "audit/";
  public static final String AS2_FOLDER = "AS2/";
  public static final String AS2_AUDIT_FOLDER = AUDIT_FOLDER + AS2_FOLDER;
  public static final String UDOC_FOLDER = "udoc/";
  public static final String GDOC_FOLDER = "gdoc/";
  
  public static final String AUDIT_TYPE_RECEIPT_AUDIT = "receiptAudit"; 
  public static final String AUDIT_TYPE_AUDIT = "audit";
  
  public static String ESTORE_COMPLETE_ALERT_NAME="ESTORE_COMPLETE_ALERT";
  
  public static final String DATE_TIME_PATTERN = "MM/dd/yyyy hh:mm:ss a";
  
  public static final Integer PROCESS_INSTANCE_ID = new Integer(0);
  public static final Integer PROCESS_STATE = new Integer(1);
  public static final Integer START_TIME = new Integer(2);
  public static final Integer END_TIME = new Integer(3);
  public static final Integer PROCESS_DEF = new Integer(4);
  public static final Integer ROLE_TYPE = new Integer(5);
  public static final Integer ORIGINATOR_ID = new Integer(6);
  public static final Integer FAILED_REASON = new Integer(7);
  public static final Integer DETAIL_REASON = new Integer(8);
  public static final Integer RETRY_NUMBER = new Integer(9);
}
