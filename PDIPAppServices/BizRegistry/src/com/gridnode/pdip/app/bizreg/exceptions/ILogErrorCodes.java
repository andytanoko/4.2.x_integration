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
package com.gridnode.pdip.app.bizreg.exceptions;

/**
 * Error codes for PDIP-App-BizRegistry Module 
 * @author Chong SoonFui
 * @since GT 4.0 VAN
 * @version GT 4.0 VAN
 */
public interface ILogErrorCodes
{
	public static final String PREFIX = "003.002.";
	
	/**
	 * 003.002.001: Duplicate domain identifier error
	 */
	public static final String BIZREG_DUPLICATE_DOMAIN_IDENTIFIER = PREFIX+"001";
	
}
