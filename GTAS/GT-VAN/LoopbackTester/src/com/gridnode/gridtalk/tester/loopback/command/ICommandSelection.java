/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICommandSelection.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.command;

/**
 * Constants for command selection
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public interface ICommandSelection
{
	/**
	 * Constant for receiving RNIF command
	 */
	public static final int MT_RNIF = 1;
	
	/**
	 * Constant for receiving ACK command
	 */
	public static final int MT_ACK = 2;
	
	/**
	 * Constant for receiving BACK END command
	 */
	public static final int MT_BACKEND = 3;
	
}
