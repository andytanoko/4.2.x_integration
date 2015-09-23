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
package com.gridnode.pdip.app.deploy.manager.exceptions;

/**
 * Error codes for PDIP-App-Deploy Module 
 * @author Chong SoonFui
 * @since GT 4.0 VAN
 * @version GT 4.0 VAN
 */
public interface ILogErrorCodes
{
	public static final String PREFIX = "003.005.";
	
	/**
	 * Error while deploying bpss object
	 */
	public static final String DEPLOY_MODEL_DEPLOY = PREFIX+"001";
}
