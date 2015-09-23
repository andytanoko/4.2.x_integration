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
package com.gridnode.pdip.base.rnif.exception;

/**
 * Error codes for PDIP-Base-Rnif Module 
 * @author Chong SoonFui
 * @since GT 4.0 VAN
 * @version GT 4.0 VAN
 */
public interface ILogErrorCodes
{
	public static final String PREFIX = "002.011.";
	
	/**
	 * 002.011.001: Error while initializing RNIF 1.1 depackager
	 */
	public static final String RNIF_1_1_DEPACKAGER_INITIALIZE = PREFIX+"001";
	
	/**
	 * 002.011.002: Error while initializing RNIF 2.0 depackager
	 */
	public static final String RNIF_2_0_DEPACKAGER_INITIALIZE = PREFIX+"002";

}
