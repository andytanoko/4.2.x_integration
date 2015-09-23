/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommandException.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 21, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.command;

/**
 * Exception class for Command classes
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class CommandException extends Exception
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4232269284496423210L;

	public static final String TEST_FAILED = "Test Failed";
	
	public static final String TEST_NOT_FOUND = "Test Not Found";
	
	private String _testId = null;
	private String _failureMessage = null;
	
	/**
	 * 
	 */
	public CommandException()
	{
		super();
	}

	/**
	 * @param message
	 */
	public CommandException(String message)
	{
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CommandException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public CommandException(String failureMsg, String testId)
	{
		this(constructMessage(failureMsg, testId));
		_failureMessage = failureMsg;
		_testId = testId;
	}
	/**
	 * @param cause
	 */
	public CommandException(Throwable cause)
	{
		super(cause);
	}

	private static String constructMessage(String failureMsg, String testId)
	{
		StringBuffer buf = new StringBuffer(failureMsg);
		buf.append(" [TestId: ");
		buf.append(testId);
		buf.append("]");
		return buf.toString();
	}

	public String getFailureMessage()
	{
		return _failureMessage;
	}

	public String getTestId()
	{
		return _testId;
	}
}
