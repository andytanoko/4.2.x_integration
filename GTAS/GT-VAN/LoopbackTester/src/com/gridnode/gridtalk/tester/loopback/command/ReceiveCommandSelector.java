/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveCommandSelector.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.command;


/**
 * Command selector for receiving or sending messages.
 * 
 *  @author Alain Ah Ming
 *  @since 
 *  @version
 */
public class ReceiveCommandSelector implements ICommandSelection
{
	/**
	 * Default Construtor 
	 */
	public ReceiveCommandSelector()
	{
	}

	/**
	 * Returns an instance of <code>ICommand</code> based on the
	 * specified <code>messageType</code>
	 * @see ICommandSelection
	 * @param messageType
	 * @return
	 * @throws NoSuchCommandException
	 */
	public static ICommand getCommand(int messageType) throws NoSuchCommandException
	{
		ICommand cmd = null;
//		
//		switch(messageType)
//		{
//			case MT_ACK:
//				cmd = new ReceiveAckCommand(new RnifMessageDao());
//				break;
//			case MT_BACKEND:
//				cmd = new ReceiveBackendCommand();
//				break;
//			case MT_RNIF:
//				cmd = new ReceivePipCommand();
//				break;
//			default:
//				throw new NoSuchCommandException(String.valueOf(messageType));
//		}
		return cmd;
	}
}
