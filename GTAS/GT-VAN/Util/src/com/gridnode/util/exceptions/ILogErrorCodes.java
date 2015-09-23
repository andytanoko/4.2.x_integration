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
 * Mar 05 2007		Alain Ah Ming				Created
 */
package com.gridnode.util.exceptions;

/**
 * Error codes for Util module
 * @author Alain Ah Ming
 *
 */
public interface ILogErrorCodes
{
	/**
	 * Prefix that MUST be prepended to all codes.&nbsp;This must not be used.
	 */
	public static final String PREFIX = "005.004.";
	
	/**
	 * 005.004.001: Error code for missing email alert parameters
	 */
	public static final String EMAIL_ALERT_MISSING_PARAMS			= PREFIX+"001";
	
	/**
	 * 005.004.002: Error code for sending JMS message
	 */
	public static final String JMS_SENDER_SEND = PREFIX+"002"; 

	/**
	 * 005.004.003: Error code for JNDI finder lookup
	 */
	public static final String JNDI_FINDER_LOOKUP = PREFIX + "003";
	
	/**
	 * 005.004.004: Error code for email sender setting authentication
	 */
	public static final String EMAIL_SENDER_SET_AUTH = PREFIX + "004";
	
	/**
	 * 005.004.005: Error code for email sender sending message
	 */
	public static final String EMAIL_SENDER_SEND = PREFIX + "005";
	
	/**
	 * 005.004.006: Error code for email sender sending attachment
	 */
	public static final String EMAIL_SENDER_ATTACHEMENTS_SEND = PREFIX + "006";
	
	/**
	 * 005.004.007: Error code for email sender addresses
	 */
	public static final String EMAIL_SENDER_ADDRESSES = PREFIX + "007";
	
	/**
	 * 005.004.008: Error code for email sender recipient addresses
	 */
	public static final String EMAIL_SENDER_RECIPIENT_ADDRESSES = PREFIX + "008";
	
	/**
	 * 005.004.009: Error code for Proxy selector initialization
	 */
	public static final String GNPROXY_SELECTOR_INITIALIZE = PREFIX + "009";
	
	/**
	 * 005.004.010: Error code for proxy selector connection failure
	 */
	public static final String GNPROXY_SELECT_CONNECT = PREFIX+"010";
	
}
