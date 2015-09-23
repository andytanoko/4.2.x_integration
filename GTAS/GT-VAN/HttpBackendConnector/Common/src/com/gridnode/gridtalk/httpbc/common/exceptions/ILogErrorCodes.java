/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ILogErrorCodes.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 05, 2007   Alain Ah Ming       Created
 */
package com.gridnode.gridtalk.httpbc.common.exceptions;

/**
 * Error codes for ISHB
 * @author Alain Ah Ming
 *
 */
public interface ILogErrorCodes
{
  /**
   * 005.003.: Prefix constant for error codes within HTTP BC
   */
  public static final String PREFIX = "005.003.";

  /**
   * 005.003.001: Error code for sending event notification error
   */
  public static final String EVENT_NOTIFIER_SEND = PREFIX+"001";
  
  /**
   * 005.003.002: Error code for Save Transaction error
   */
  public static final String TRANSACTION_SAVE = PREFIX+"002";
  
  /**
   * 005.003.003: Error code for Query Transaction error
   */
  public static final String TRANSACTION_QUERY = PREFIX+"003";
  
  /**
   * 005.003.004: Error code for Delete Transaction error
   */
  public static final String TRANSACTION_DELETE = PREFIX+"004";

  /**
   * 005.003.005: Error code for Process Transaction error
   */
  public static final String TRANSACTION_PROCESS = PREFIX+"005";
  
  /**
   * 005.003.006: Error code for Backend servlet doGet error
   */
  public static final String BACKEND_SERVLET_GET = PREFIX+"006";  
  
  /**
   * 005.003.007: Error code for Backend servlet doGet error
   */
  public static final String BACKEND_SERVLET_POST = PREFIX+"007"; 

  /**
   * 005.003.008: Error code for Process Transaction error
   */
  public static final String INCOMING_TRANSACTION_PROCESS = PREFIX+"008";
  
  /**
   * 005.003.009: Error code for Process Transaction error
   */
  public static final String OUTGOING_TRANSACTION_PROCESS = PREFIX+"009";
  
  /**
   * 005.003.010: Error code for the failure of processing failed jms
   */
  public static final String PROCESS_FAILED_JMS = PREFIX+"010";

  /**
   * 005.003.011: Error code for the failure of processing in IncomingTransactionMDBean
   */
  public static final String INCOMING_TRANS_MDBEAN = PREFIX+"011";
  

  /**
   * 005.003.012: Error code for the failure of processing in IncomingTransactionMDBean
   */
  public static final String OUTGOING_TRANS_MDBEAN = PREFIX+"012";
  
}
