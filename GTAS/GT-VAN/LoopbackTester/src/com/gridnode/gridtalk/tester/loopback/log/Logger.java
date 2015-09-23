/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Logger.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 16, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.log;

import org.apache.log4j.MDC;

public class Logger
{
	private static final String CATEGORY = "GLBT";
	private static final String MDC_TEST_ID = "testId";
	private static final String MDC_TEST_STATE = "testState";
	
	private static String formatMessage(String className,
																			String methodName,
																			String message)
	{
		StringBuffer buff = new StringBuffer();
		buff.append("[");
		buff.append(className);
		buff.append(".");
		buff.append(methodName);
		buff.append("]");
		buff.append(" ");
		buff.append(message);
		return buff.toString();
	}
	public static void info(String className, String methodName, String testId, String state, String message)
	{
		if(testId != null)
			MDC.put(MDC_TEST_ID, testId);
		if(state != null)	
			MDC.put(MDC_TEST_STATE, state);
		Log4jAdapter.log(CATEGORY, formatMessage(className, methodName, message));
		MDC.remove(MDC_TEST_ID);
		MDC.remove(MDC_TEST_STATE);
	}
	
	public static void logTestState(String className, String methodName, String testId, String state)
	{
		if(testId != null)
			MDC.put(MDC_TEST_ID, testId);
		if(state != null)	
			MDC.put(MDC_TEST_STATE, state);
		String message = "Test State";
		Log4jAdapter.log(CATEGORY+".TEST", formatMessage(className, methodName, message));
		MDC.remove(MDC_TEST_ID);
		MDC.remove(MDC_TEST_STATE);
	}
	
	public static void logHeartbeat(String className, String methodName, String message)
	{
		Log4jAdapter.log(CATEGORY+".HB", formatMessage(className, methodName, message));
	}
	
	public static void debug(String className, String methodName, String message)
	{
		Log4jAdapter.debug(CATEGORY, formatMessage(className, methodName, message));
	}
	
	public static void warn(String className, String methodName, String message)
	{
		Log4jAdapter.warn(CATEGORY, formatMessage(className, methodName, message));
	}
	
	public static void error(String className, String methodName, String message)
	{
		Log4jAdapter.err(CATEGORY, formatMessage(className, methodName, message));
	}
	
	public static void warn(String className, String methodName, String message, Throwable t)
	{
		Log4jAdapter.warn(CATEGORY, formatMessage(className, methodName, message),t);
	}
	
	public static void error(String className, String methodName, String message, Throwable t)
	{	
		Log4jAdapter.err(CATEGORY, formatMessage(className, methodName, message),t);
	}	
}
