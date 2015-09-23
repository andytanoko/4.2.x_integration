/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractGtTestState.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 16, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.intf;

import com.gridnode.gridtalk.tester.loopback.log.Logger;

/**
 * Abstract super class for GT test states
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public abstract class AbstractGtTestState implements IGtTestState
{
	protected void logDebug(String methodName, String message)
	{
		Logger.debug(this.getClass().getSimpleName(), methodName, message);
	}
//	protected void logError(String methodName, String message, Throwable t)
//	{
//		Logger.error(this.getClass().getSimpleName(), methodName, message, t);
//	}
	
	protected void logInfo(String methodName, String testId, String testState, String message)
	{
		Logger.info(this.getClass().getSimpleName(), methodName, testId, testState, message);
	}

	protected void logWarn(String methodName, String message, Throwable t)
	{
		Logger.warn(this.getClass().getSimpleName(), methodName, message, t);
	}
}
