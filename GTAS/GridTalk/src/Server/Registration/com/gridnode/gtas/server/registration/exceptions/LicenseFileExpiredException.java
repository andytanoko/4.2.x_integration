/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LicenseFileExpiredException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 22, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.registration.exceptions;

/**
 * Throw while the license file has expired
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class LicenseFileExpiredException extends ProductRegistrationException
{

	public LicenseFileExpiredException(String msg)
	{
		super(msg);
	}
	
	public LicenseFileExpiredException(String msg, Throwable t)
	{
		super(msg, t);
	}
}
