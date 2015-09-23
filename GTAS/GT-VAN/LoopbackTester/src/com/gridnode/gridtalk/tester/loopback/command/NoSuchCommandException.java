/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NoSuchCommandException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.command;

/**
 * Exception for querying non-existing commands
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class NoSuchCommandException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7669932820897350141L;

	/**
	 * 
	 */
	public NoSuchCommandException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NoSuchCommandException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoSuchCommandException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NoSuchCommandException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
