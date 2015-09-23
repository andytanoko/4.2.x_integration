/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ILogErrorCodes.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 12 2007		Chong SoonFui				Created
 * Sep 30 2010    Tam Wei Xiang       #1897 - Add err code for TPT_SOAP_RECEIVE_EXCEPTION_HANDLE
 */
package com.gridnode.pdip.base.transport.exceptions;

/**
 * Error codes for PDIP-Base-Transport Module 
 * @author Chong SoonFui
 * @since GT 4.0 VAN
 * @version GT 4.0 VAN
 */
public interface ILogErrorCodes
{
	public static final String PREFIX = "002.017.";
	
	/**
	 * Error while invoking jms connection
	 */
	public static final String TRANSPORT_JMS_INVOKE = PREFIX+"001";
	
	/**
	 *  002.017.002: Error while invoking persistance connection
	 */
	public static final String TRANSPORT_PERSISTANCE_INVOKE = PREFIX+"002";
	
	/**
	 * 002.017.003: Error in JMS controller while sending feedback message
	 */
	public static final String TRANSPORT_FEEDBACK_SEND = PREFIX+"003";

	/**
	 *  002.017.004: Error in subscriber while processing message
	 */
	public static final String TPT_SUBSCRIBER_ONMSG = PREFIX+"004";

	/**
	 * 002.017.005: Error while getting service handler from SOAP service handler registry
	 */
	public static final String TPT_SOAP_SVC_HANDLER_REGISTRY_GET_SVC_HANDLER = PREFIX+"005";
	
	/**
	 * 002.017.006: Error in initializing SOAP message receiver
	 */
	public static final String TPT_SOAP_MSG_RCVR_SVC_INIT = PREFIX+"006"; 

	/**
	 * 002.017.007: Error in JMS controller handling persistence connection exception
	 */
	public static final String TPT_JMS_CTLR_CONN_EXCEPTION_HANDLE = PREFIX+"006"; 

	/**
	 * 002.017.008: Error in handling JMS exception
	 */
	public static final String TPT_JMS_EXCEPTION_HANDLE = PREFIX+"008"; 

  /**
   * 002.017.009: Error in handling the incoming message via the SOAP protocol
   */
  public static final String TPT_SOAP_RECEIVE_EXCEPTION_HANDLE = PREFIX + "009";
}
