/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendAckCommand.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.command.impl;

import com.gridnode.gridtalk.tester.loopback.testbed.TestCache;
import com.gridnode.gridtalk.tester.loopback.testbed.impl.outbound.GtObTest;

/**
 * @deprecated
 * Command class to send an RN ACK to GridTalk
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class SendAckCommand extends AbstractCommand
{

	/**
	 * 
	 */
	public SendAckCommand()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.gridnode.gridtalk.tester.loopback.command.ISendCommand#execute(java.lang.String)
	 */
	public void execute(String testId)
	{
		// TODO Auto-generated method stub

	}
	@Override
	protected void logTestState()
	{
	}
}
