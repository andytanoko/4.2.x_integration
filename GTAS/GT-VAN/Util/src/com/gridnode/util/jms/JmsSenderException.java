/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsSender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 07 2007		Alain Ah Ming				Created
 */
package com.gridnode.util.jms;

/**
 * Exception class for JmsSender
 * @author Alain Ah Ming
 *
 */
public class JmsSenderException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8923065633255393252L;

	/**
	 * 
	 */
	public JmsSenderException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public JmsSenderException(String arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public JmsSenderException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public JmsSenderException(Throwable arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
