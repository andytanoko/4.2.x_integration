/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICommand.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.command;

/**
 * Interface for all command classes
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public interface ICommand
{
	/**
	 * Receives a message a process it accordingly 
	 * @param message The message to process
	 * @since GLBT 0.1
	 * @version GLBT 0.1
	 */
	public void execute(String message) throws CommandException;
}
