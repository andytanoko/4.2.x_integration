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
 */
package com.gridnode.pdip.base.session.exceptions;

/**
 * Error codes for PDIP-Base-Session Module 
 * @author Chong SoonFui
 * @since GT 4.0 VAN
 * @version GT 4.0 VAN
 */
public interface ILogErrorCodes
{
	public static final String PREFIX = "002.015.";
	
	/**
	 * 002.015.001: Error in Session Time Bean invocation
	 */
	public static final String SESSION_TIMER_BEAN_INVOKE = PREFIX+"001";
	
}
