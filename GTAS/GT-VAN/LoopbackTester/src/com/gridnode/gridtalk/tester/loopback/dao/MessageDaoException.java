/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MessageDaoException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 26, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.dao;

import com.gridnode.gridtalk.tester.loopback.exception.GlbtException;

/**
 * Exception class for DAO level
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class MessageDaoException extends GlbtException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8346859944380686275L;

	/**
	 * 
	 */
	public MessageDaoException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public MessageDaoException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MessageDaoException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public MessageDaoException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
