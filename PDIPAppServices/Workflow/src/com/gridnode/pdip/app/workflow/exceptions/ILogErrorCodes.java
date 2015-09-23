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
 * Feb 14 2007		Chong SoonFui				Created
 */
package com.gridnode.pdip.app.workflow.exceptions;

/**
 * Error codes for PDIP-App-Workflow Module 
 * @author Chong SoonFui
 * @since GT 4.0 VAN
 * @version GT 4.0 VAN
 */
public interface ILogErrorCodes
{
	public static final String PREFIX = "003.015.";
	
	/**
	 * Error while triggering process transactions
	 */
	public static final String WORKFLOW_PROCESS_TRANSACTION = PREFIX+"001";
	
	/**
	 * Error while broadcasting process transactions notification
	 */
	public static final String WORKFLOW_BROADCAST_NOTIFICATION_ERROR = PREFIX+"002";
	
	/**
	 * Error while resuming suspended activities
	 */
	public static final String WORKFLOW_RESUME_SUSPENDED_ACT = PREFIX+"003";
	
	/**
	 * Error while initialising XPDL 
	 */
	public static final String WORKFLOW_INITIALISE_XPDL = PREFIX+"004";
	
	/**
	 * Generic error in MDBean OnMessage()
	 */
	public static final String WORKFLOW_MDB_ONMESSAGE = PREFIX+"005";
	
	/**
	 * Error while invoking alarm
	 */
	public static final String WORKFLOW_INVOKE = PREFIX+"006";
	
	/**
	 * Error while sending signal
	 */
	public static final String WORKFLOW_SEND_SINGAL = PREFIX+"007";
	
	/**
	 * Error in Bpss ProcessInActive function
	 */
	public static final String WORKFLOW_BPSS_PROCESS_IN_ACTIVE = PREFIX+"008";
	
	/**
	 * Error in loading workflow config
	 */
	public static final String WORKFLOW_LOAD_CONFIG = PREFIX+"009";
	
	/**
	 * Error in running timeout task
	 */
	public static final String WORKFLOW_RUN_TIMEOUT_TASK = PREFIX+"010";
}
