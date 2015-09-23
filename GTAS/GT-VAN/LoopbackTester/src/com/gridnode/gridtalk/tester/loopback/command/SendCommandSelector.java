/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendCommandSelector.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.command;

import com.gridnode.gridtalk.tester.loopback.command.impl.SendBackendCommand;
import com.gridnode.gridtalk.tester.loopback.command.impl.SendPipCommand;

/**
 * @deprecated
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class SendCommandSelector implements ICommandSelection
{

	/**
	 * 
	 */
	public SendCommandSelector()
	{
		super();
	}

	/**
	 * 
	 * @param messageType
	 * @return
	 * @throws NoSuchCommandException
	 */
	public static ICommand getCommand(int messageType, String pipCode) throws NoSuchCommandException
	{
		ICommand cmd = null;
		
		switch(messageType)
		{
			case MT_BACKEND:
				cmd = new SendBackendCommand();
				break;
			case MT_RNIF:
				cmd = new SendPipCommand(pipCode);
				break;
			default:
				throw new NoSuchCommandException(String.valueOf(messageType));
		}
		return cmd;
	}
}
