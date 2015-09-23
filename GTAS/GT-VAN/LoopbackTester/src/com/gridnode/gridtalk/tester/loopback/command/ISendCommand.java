/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISendCommand.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.command;

/**
 * @deprecated
 * 
 * 
 *  @author Alain Ah Ming
 *  @since GLBT 0.1
 *  @version GLBT 0.1
 */
public interface ISendCommand
{
	/**
	 * Sends a message for the specified test Id.
	 * @param testId The test Id to send a message for.
	 * 
	 * @since GLBT 0.1
	 * @version GLBT 0.1
	 */
	public void execute(String testId);
}
