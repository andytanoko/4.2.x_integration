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
package com.gridnode.pdip.app.channel.exceptions;

/**
 * Error codes for PDIP-App-Channel Module 
 * @author Chong SoonFui
 * @since GT 4.0 VAN
 * @version GT 4.0 VAN
 */
public interface ILogErrorCodes
{
	public static final String PREFIX = "003.003.";
	
	/**
	 * 003.003.001: Error while initialising channel message handler 
	 */
	public static final String CHANNEL_MESSAGE_HANDLER_REGISTRY_INIT = PREFIX+"001";
	
	/**
	 * 003.003.002: Error while updating channel's information
	 */
	public static final String CHANNEL_CHANNEL_UPDATE = PREFIX+"002";
	
	/**
	 * 003.003.003: Error while retrieving headers
	 */
	public static final String CHANNEL_GET_HEADER = PREFIX+"003";
	
	/**
	 * 003.003.004: Error on getting Jms message from onMessage()
	 */
	public static final String CHANNEL_ONMESSAGE_LISTENER = PREFIX+"004";
	
	/**
	 * 003.003.005: Error in creating channel feedback message listener
	 */
	public static final String CHANNEL_CREATE = PREFIX+"005";
	
	/**
	 * 003.003.006: Error initializing channel message dispatcher
	 */
	public static final String CHANNEL_MESSAGE_DISPATCHER_INIT = PREFIX+"006";
	
	/**
	 * 003.003.007: Error in running channel feedback message listener
	 */
	public static final String CHANNEL_FEEDBACK_MESSAGE_LISTENER = PREFIX+"007";
	
	/**
	 * 003.003.008: Error in channel receiver message listener
	 */
	public static final String CHANNEL_RECEIVE_MESSAGE_LISTENER = PREFIX+"008";
	
	/**
	 * 003.003.009: Error in channel send message listener
	 */
	public static final String CHANNEL_SEND_MESSAGE_LISTENER = PREFIX+"009";
	

}
