/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MessageHelperException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 26, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.helpers;

import com.gridnode.gridtalk.tester.loopback.exception.GlbtException;

/**
 * @todo Class documentation
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class MessageHelperException extends GlbtException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5052100081766276549L;

	/**
	 * 
	 */
	public MessageHelperException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public MessageHelperException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MessageHelperException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public MessageHelperException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
