/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ILogErrorCodes.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 15 2007    Chong SoonFui				Created 
 * Feb 26 2007    Tam Wei Xiang       Added generic error code to indicate
 *                                    error in delivering msg to queue.
 * May 16 2007    Tam Wei Xiang       Added error code if we can't update
 *                                    the customer be id into rtprocess doc. 
 * Jun 12 2007    Tam Wei Xiang       Added error code if we can't delivered
 *                                    the archive status to GTVan-plugin                                                                     
 */
package com.gridnode.gtas.exceptions;

/**
 * Error codes for GTAS client module
 * @author Chong SoonFui
 * @since GTAS 4.0 VAN
 * @version GTAS 4.0 VAN
 */
public interface ILogErrorCodes
{
	/**
	 * 004.001.: Code prefix for all GTAS client module error codes
	 */
	public static final String PREFIX = "004.001.";

	/**
	 * @deprecated
	 * Generic MDBean OnMessage() Error 
	 */
	public static final String GT_GENERIC_MDB_ONMESSAGE_ERROR = PREFIX+"001";
	
	/**
	 * @deprecated
	 * Generic Message Handler error 
	 */
	public static final String GT_GENERIC_MESSAGE_HANDLER = PREFIX+"002";
	
	/**
	 * @deprecated
	 * Error in notifying functions 
	 */
	public static final String GT_GENERIC_BROADCAST_NOTIFICATION_ERROR = PREFIX+"003";

	/**
	 * @deprecated
	 * Error while triggering alert
	 */
	public static final String GT_GENERIC_ALERT_TRIGGER = PREFIX+"004";
	
	/**
	 * @deprecated
	 * Generic invoking process
	 */
	public static final String GT_GENERIC_INVOKE_ERROR = PREFIX+"005";
	
	/**
	 * 004.001.006: Error while initialising Substitution List
	 */
	public static final String GT_ALERT_INITIALISE = PREFIX+"006";
	
	/**
	 * 006.001.007: Generic Service Handler error in Backend
	 */
	public static final String GT_BACKEND_SERVICE_HANDLER = PREFIX+"007";
	
	/**
	 * 006.001.008: Error while getting EStoreHelper instance
	 */
	public static final String GT_DBARCHIVE_ESTORE_INSTANCE = PREFIX+"008";
	
	/**
	 * 006.001.009: Error in update/add/cancel Timer to resend document
	 */
	public static final String GT_DOCUMENT_RESEND_TIMER = PREFIX+"009";

	/**
	 * 006.001.010: Error in getting GN Connection
	 */
	public static final String GT_ENTERPRISE_CONNECTION_ERROR = PREFIX+"010";
	
  /**
   * 006.001.011: Error in delivering jms msg to Queue
   */
  public static final String GT_GENERIC_DELIVERING_NOTIFICATION_ERROR = PREFIX+"011";
	/**
	 * 006.001.012: Error in sending online notification
	 */
	public static final String GT_CHANNEL_SEND_ONLINE_NOTIFICATIONS_ERROR = PREFIX+"012";
	/**
	 * 006.001.013: Error in sending 0A1 notification
	 */
	public static final String GT_RNIF_SEND_0_ERROR = PREFIX+"013";
	
	/**
	 * 004.001.014: Error in GT Backend Listener MDB
	 */
	public static final String GT_BACKEND_LISTENER_MDB = PREFIX+"014";
	
	/**
	 * 004.001.015: Error in GT DB Archive MDB
	 */
	public static final String GT_DB_ARCHIVE_MDB = PREFIX+"015";

	/**
	 * 004.001.016: Error in GT DB Archive listener
	 */
	public static final String GT_DB_ARCHIVE_LISTENER = PREFIX+"016";

	/**
	 * 004.001.017: Error in GT Backend Import MDB
	 */
	public static final String GT_BACKEND_IMPORT_MDB= PREFIX+"017";
	
	/**
	 * 004.001.018: Error in Connection Change MDB
	 */
	public static final String GT_CONNECTION_CHANGE_MDB= PREFIX+"018";

	/**
	 * 004.001.019: Error in Housekeeping Listener
	 */
	public static final String GT_HOUSEKEEPING_LISTENER= PREFIX+"019";

	/**
	 * 004.001.020: Error in License State Monitor MDB
	 */
	public static final String GT_LICENSE_STATE_MONITOR_MDB= PREFIX+"020";

	/**
	 * 004.001.021: Error in Partner Function Entity Change MDB
	 */
	public static final String GT_PARTNER_FUNCTION_ENTITY_CHANGE_MDB= PREFIX+"021";

	/**
	 * 004.001.022: Error in Receive RN Doc MDB
	 */
	public static final String GT_RECEIVE_RN_DOC_MDB= PREFIX+"022";
	
	/**
	 * 004.001.023: Error in Send RN Doc MDB
	 */
	public static final String GT_SEND_RN_DOC_MDB= PREFIX+"023";
	
	/**
	 * 004.001.024: Error in Activation Acknowledgement Receiver
	 */
	public static final String GT_ACTIVATION_ACKNOWLEDGEMENT_RECEIVER= PREFIX+"024";
	
	/**
	 * 004.001.025: Error in Activation Message Receiver
	 */
	public static final String GT_ACTIVATION_MESSAGE_RECEIVER= PREFIX+"025";
	
	/**
	 * 004.001.026: Error in Search Result Receiver
	 */
	public static final String GT_SEARCH_RESULT_RECEIVER= PREFIX+"026";
	
	/**
	 * 004.001.027: Error in Connection Ack receiver
	 */
	public static final String GT_CONNECTION_ACK_RECEIVER= PREFIX+"027";
	
	/**
	 * 004.001.028: Error in Connection lost listener
	 */
	public static final String GT_CONNECTION_LOST_LISTENER= PREFIX+"028";
	
	/**
	 * 004.001.029: Error in Connection Message Receiver
	 */
	public static final String GT_CONNECTION_MESSAGE_RECEIVER= PREFIX+"029";
	
	/**
	 * 004.001.030: Error in Receive Document Ack Handler
	 */
	public static final String GT_RECEIVE_DOC_ACK_HANDLER= PREFIX+"030";
	
	/**
	 * 004.001.031: Error in Receive Document Handler
	 */
	public static final String GT_RECEIVE_DOC_HANDLER= PREFIX+"031";
	
	/**
	 * 004.001.032: Error in Sync Message Receiver
	 */
	public static final String GT_SYNC_MESSAGE_RECEIVER= PREFIX+"032";
	
	/**
	 * 004.001.033: Error in Sync Resource Controller
	 */
	public static final String GT_SYNC_RESOURCE_CONTROLLER= PREFIX+"033";
	
	/**
	 * 004.001.034: Error in Activation Process Handler
	 */
	public static final String GT_ACTIVATION_PROCESS_HANDLER= PREFIX+"034";
	
	/**
	 * 004.001.035: Error in Import Doc Service
	 */
	public static final String GT_IMPORT_DOC_SERVICE= PREFIX+"035";
	
	/**
	 * 004.001.036: Error in Connection Delegate Helper
	 */
	public static final String GT_CONNECTION_DELEGATE_HELPER= PREFIX+"036";
	
	/**
	 * 004.001.037: Error in Document Alert Delegate
	 */
	public static final String GT_DOCUMENT_ALERT_DELEGATE= PREFIX+"037";
	
	/**
	 * 004.001.038: Error in Document Status Broadcaster
	 */
	public static final String GT_DOC_STATUS_BROADCASTER= PREFIX+"038";
	
	/**
	 * 004.001.039: Error in Document Transaction Handler
	 */
	public static final String GT_DOC_TRANSACTION_HANDLER= PREFIX+"039";
	
	/**
	 * 004.001.040: Error in Registration Alert Utility
	 */
	public static final String GT_REGISTRATION_ALERT_UTILITY= PREFIX+"040";
	
	/**
	 * 004.001.041: Error in RNIF Alert Utility
	 */
	public static final String GT_RNIF_ALERT_UTILITY= PREFIX+"041";
	
	/**
	 * 004.001.042: Error in User Procedure Alert Utility
	 */
	public static final String GT_USER_PROC_ALERT_UTILITY= PREFIX+"042";
	
	/**
	 * 004.001.043: Error in Expiry Checker Delegate
	 */
	public static final String GT_EXPIRY_CHECKER_DELEGATE= PREFIX+"043";
	
	/**
	 * 004.001.044: Error in GridDoc Archive 
	 */
	public static final String GT_GRIDDOC_ARCHIVE= PREFIX+"044";
	
	/**
	 * 004.001.045: Error in Process Instance Archive 
	 */
	public static final String GT_PROCESS_INSTANCE_ARCHIVE= PREFIX+"045";
	
	/**
	 * 004.001.046: Error in Housekeeping MDB 
	 */
	public static final String GT_HOUSEKEEPING_MDB= PREFIX+"046";

	/**
	 * 004.001.047: Error in Check License MDB 
	 */
	public static final String GT_CHECK_LICENSE_MDB= PREFIX+"047";

	/**
	 * 004.001.048: Error in RNIF Schema Manager 
	 */
	public static final String GT_RNIF_SCHEMA_MGR= PREFIX+"048";

	/**
	 * 004.001.049: Error in Execute User Procedure MDB 
	 */
	public static final String GT_EXECUTE_USER_PROC_MDB= PREFIX+"049";
	
  /**
   * 004.001.050: Error in updating the customer be id into rtprocess doc
   */
  public static final String GT_RNIF_ERROR_UPDATE_CUS_BEID = PREFIX+"050";
  
  /**
   * 004.001.051: Error in delivering the archive status to GT-plug in
   */
  public static final String GT_ARCHIVE_EVENT_DELIVERED_FAILURE = PREFIX+"051";
  
  public static final String GT_GDOC_ARCHIVE_MDB = PREFIX+"052";
  public static final String GT_PROCESS_INSTANCE_ARCHIVE_MDB = PREFIX+"053";
  
  /**
   * 004.001.054: Error in GT DB Archive TASK MDB
   */
  public static final String GT_DB_ARCHIVE_TASK_MDB = PREFIX+"054";
}
