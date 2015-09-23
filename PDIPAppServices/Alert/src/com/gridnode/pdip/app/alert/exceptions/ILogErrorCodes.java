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
 * Feb 13 2007		Chong SoonFui				Created
 */
package com.gridnode.pdip.app.alert.exceptions;

/**
 * Error codes for PDIP-App-Alert Module 
 * @author Chong SoonFui
 * @since GT 4.0 VAN
 * @version GT 4.0 VAN
 */
public interface ILogErrorCodes
{
	public static final String PREFIX = "003.001.";
	
	/**
	 * Error while writing to log file
	 */
	public static final String ALERT_WRITE_LOG = PREFIX+"001";
	
	/**
	 * Error while retrieving log file location
	 */
	public static final String ALERT_LOG_LOCATION_ERROR = PREFIX+"002";
	
	/**
	 * @deprecated
	 * Error while invoking listener
	 */
	public static final String ALERT_LISTENER_INVOKE = PREFIX+"003";

		
	/**
	 * Error while extracting value from XPath
	 */
	public static final String ALERT_XPATH_EXTRACT = PREFIX+"004";
	
	/**
	 * 003.001.005: Error while invoking email retry listener
	 */
	public static final String EMAIL_RETRY_LISTENER = PREFIX+"005";
	
	/**
	 * 003.001.006: Error while invoking JMS retry listener
	 */
	public static final String JMS_RETRY_LISTENER = PREFIX+"006";	
}
