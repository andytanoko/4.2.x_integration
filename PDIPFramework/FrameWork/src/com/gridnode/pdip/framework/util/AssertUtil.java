/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AssertUtil.java
 *
 ****************************************************************************
 * Date           	Author              		Changes
 ****************************************************************************
 * Oct 10 2005		  Sumedh Chalermkanjana   Created
 */
package com.gridnode.pdip.framework.util;

/**
 * This class contains assertion method.
 */
public class AssertUtil
{
	/**
	 * If condition is false, throws AssertionError.
	 */
	public static void assertTrue(boolean condition)
	{
		if (condition == false)
		{
			throw new AssertionError();
		}
	}
	
	/**
	 * If condition is false, throws AssertionError with specified message: <code>message</code>.
	 */
	public static void assertTrue(boolean condition, String message)
	{
		if (condition == false)
		{
			throw new AssertionError(message);
		}
	}
	
	/**
	 * Throws AssertionError with specified message: <code>message</code>.
	 */
	public static void fail(String message)
	{
		assertTrue(false, message);
	}
}
