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
package com.gridnode.pdip.app.servicemgmt.exceptions;

/**
 * Error codes for PDIP-App-ServiceMgmt Module 
 * @author Chong SoonFui
 * @since GT 4.0 VAN
 * @version GT 4.0 VAN
 */
public interface ILogErrorCodes
{
	public static final String PREFIX = "003.013.";
	
	/**
	 * 003.013.001: Error while initializing Password Digest Helper
	 */
	public static final String SERVICEMGMT_PASSWORD_DIGESTER_HELPER_INIT = PREFIX+"001";
	
	/**
	 * 003.013.002: Error in Service Management Http URL connect
	 */
	public static final String SERVICEMGMT_HTTP_CONNECT = PREFIX+"002";
	
	/**
	 * 003.013.003: Error in Service Management Soap Router Servlet
	 */
	public static final String SERVICEMGMT_MESSAGE_HEADER = PREFIX+"003";
}
