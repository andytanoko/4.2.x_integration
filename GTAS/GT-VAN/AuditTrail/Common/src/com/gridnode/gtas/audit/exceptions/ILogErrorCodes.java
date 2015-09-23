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
 * Mar 05 2007    Chong SoonFui       Created 
 * Mar 07 2007    Alain Ah Ming       Add error code in documentation for easy reference
 */
package com.gridnode.gtas.audit.exceptions;

/**
 * Error codes for GTVAN Audit-Trail client module
 * @author Chong SoonFui
 * @since GTAS 4.0 VAN
 * @version GTAS 4.0 VAN
 */
public interface ILogErrorCodes
{
  /**
   * Code prefix for all GTVAN Audit-Trail client module error codes
   */
  public static final String PREFIX = "005.001.";

  /**
   * 005.001.001: Generic MDBean OnMessage() Error 
   */
  public static final String AT_GENERIC_MDB_ONMESSAGE_ERROR = PREFIX+"001";
  
  /**
   * 005.001.002: Generic error while posting HTTP response
   */
  public static final String AT_GENERIC_HTTP_RESPONSE_POSTING_ERROR = PREFIX+"002";
  
  /**
   * 005.001.003: Error in writing HTTP response object
   */
  public static final String AT_WRITE_RESPONSE_ERROR = PREFIX+"003";

  /**
   * 005.001.004: Error in reporting status to Transaction Monitoring
   * 
   */
  public static final String AT_STATUS_REPORT_ERROR = PREFIX+"004";
  
  /**
   * 005.001.005: Error in processing audit trail data
   */
  public static final String AT_PROCESS_AUDIT_TRAIL_DATA_ERROR = PREFIX+"005";
  
  /**
   * 005.001.006: Archive MDBean OnMessage() Error 
   */
  public static final String AT_ARCHIVE_MDB_ONMESSAGE_ERROR = PREFIX+"006";
  
  /**
   * 005.001.007: Notifier MDBean OnMessage() Error 
   */
  public static final String AT_NOTIFY_MDB_ONMESSAGE_ERROR = PREFIX+"007";
  
  /**
   * 005.001.008: GT Archive MDBean OnMessage() Error 
   */
  public static final String AT_GTARCHIVE_MDB_ONMESSAGE_ERROR = PREFIX+"008";
  
  /**
   * 005.001.009: Tx Flow MDBean OnMessage() Error 
   */
  public static final String AT_TXFLOW_MDB_ONMESSAGE_ERROR = PREFIX+"009";
  
  /**
   * 005.001.010: Audit Trail Reprocess OnMessage() Error 
   */
  public static final String AT_REPROCESS_MDB_ONMESSAGE_ERROR = PREFIX+"010";

  /**
   * 005.001.011: Audit Trail MDBean OnMessage() Error 
   */
  public static final String AT_AUDITTRAIL_MDB_ONMESSAGE_ERROR = PREFIX+"011";
  
  /**
   * 005.001.012: Save Common Resource MDBean OnMessage() Error 
   */
  public static final String AT_SAVE_COMMON_RESOURCE_MDB_ONMESSAGE_ERROR = PREFIX+"012";    
  
  /**
   * 005.001.013: Error in kick start the archive scheduler task
   */
  public static final String AT_ARCHIVE_SCHEDULER_TASK_KICK_START_ERROR = PREFIX+"013";
  
  /**
   * 005.001.014: Error in processing the archive scheduler request from client. 
   */
  public static final String AT_ARCHIVE_SCHEDULER_SERVICE_ERROR = PREFIX+"014";
  
  /**
   * 005.001.015: Error in updating the archive status into the archive properties file/archive summary.
   */
  public static final String AT_ARCHIVE_STATUS_UPDATE_ERROR = PREFIX+"015";
  
  /**
   * 005.001.016: Error in delegating the archive event (either GT archive request event or GT archive report event)
   */
  public static final String AT_ARCHIVE_EVENT_DELEGATE_ERROR = PREFIX+"016";
  
  /**
   * 005.001.017: Error in processing the failed jms saved.
   */
  public static final String AT_UTIL_FAILED_JMS_ERROR = PREFIX+"017";
}
