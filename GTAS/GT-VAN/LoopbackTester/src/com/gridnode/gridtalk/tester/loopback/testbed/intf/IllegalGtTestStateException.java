/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IllegalGtTestStateException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.intf;

/**
 * Exception class to throw when the wrong method is being
 * called on an <code>IGtTestState</code> instance
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class IllegalGtTestStateException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5018864558573983607L;

	/**
	 * 
	 */
	public IllegalGtTestStateException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public IllegalGtTestStateException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public IllegalGtTestStateException(String testId, String currentTestState)
	{
		super("Illegal Test State [Test Id: "+testId+"][State: "+currentTestState+"]");
	}
	/**
	 * @param message
	 * @param cause
	 */
	public IllegalGtTestStateException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public IllegalGtTestStateException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
