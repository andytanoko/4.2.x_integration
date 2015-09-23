/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MaximumActivePartnersReachedException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28, 2006   i00107             Created
 */

package com.gridnode.gtas.server.partner.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

/**
 * @author i00107
 * Exception to indicate that the number of active partners
 * has reached the maximum.
 */
public class MaximumActivePartnersReachedException extends ApplicationException
{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8587133002611664439L;

	/**
	 * @param msg
	 */
	public MaximumActivePartnersReachedException(String msg)
	{
		super(msg);
	}

}
