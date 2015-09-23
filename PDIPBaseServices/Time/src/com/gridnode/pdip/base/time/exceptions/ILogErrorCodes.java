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
 * Feb 28 2007		Chong SoonFui				Created
 */
package com.gridnode.pdip.base.time.exceptions;

/**
 * Error codes for PDIP-Base-Time Module 
 * @author Chong SoonFui
 * @since GT 4.0 VAN
 * @version GT 4.0 VAN
 */
public interface ILogErrorCodes
{
	public static final String PREFIX = "002.016.";
	
	/**
	 * 002.016.001: Error in Time Invoke MDB onMessage()
	 */
	public static final String TIME_INVOKE_ONMESSAGE_ERROR = PREFIX+"001";
	
	/**
	 * 002.016.002: Error while parsing value to Mime content line
	 */
	public static final String TIME_CONTENT_PARSING_ERROR = PREFIX+"002";

	/**
	 * 002.016.003: Error while triggering alarm
	 */
	public static final String TIME_ALARM_TRIGGER_ERROR = PREFIX+"003";

	/**
	 * 002.016.004: Error while missed a alarm call
	 */
	public static final String TIME_ALARM_MISSED_ERROR = PREFIX+"004";

	/**
	 * 002.016.005: Error while loading task
	 */
	public static final String TIME_LOAD_TASK_ERROR = PREFIX+"005";

	/**
	 * 002.016.006: Error while running alarm
	 */
	public static final String TIME_RUN_ALARM_ERROR = PREFIX+"006";

	/**
	 * 002.016.007: Error while initilizing JMSSender
	 */
	public static final String TIME_JMSSENDER_INITIALIZATION = PREFIX+"007";

	/**
	 * 002.016.008: Error in Time Missed MDB onMessage()
	 */
	public static final String TIME_MISSED_MDB_ONMESSAGE_ERROR = PREFIX+"008";
	
	/**
	 * 002.016.009: Error in Time Schedule MDB onMessage()
	 */
	public static final String TIME_SCHEDULE_MDB_ONMESSAGE_ERROR = PREFIX+"009";
	

}
