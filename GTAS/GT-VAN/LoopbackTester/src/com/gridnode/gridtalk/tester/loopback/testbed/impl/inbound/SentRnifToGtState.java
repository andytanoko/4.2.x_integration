/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SentRnifToGtState.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.impl.inbound;

import com.gridnode.gridtalk.tester.loopback.log.Logger;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound.AbstractGtIbTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound.IGtIbTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound.IGtIbTestStateContext;
import com.gridnode.gridtalk.tester.loopback.util.BackendUtil;

import java.io.IOException;
import java.util.Date;

/**
 * This class represents the GridTalk-Inbound test state
 * after sending the RNIF message
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class SentRnifToGtState extends AbstractGtIbTestState
{
	public static final String STATE = "IB-SentRN";
	/**
	 * 
	 */
	private static final long serialVersionUID = -832337538737164027L;

	private Date _createDt = null;
	/**
	 * 
	 */
	public SentRnifToGtState()
	{
		_createDt = new Date();
	}

	@Override
	public IGtIbTestState receiveAckFromGt(IGtIbTestStateContext test, String ackMessage) throws IllegalGtTestStateException
	{
		IGtIbTestState s = this;
		s = new ReceivedRnAckFromGtState();
		return s;
	}
	
	@Override
	public IGtIbTestState receiveBackendDocumentFromGt(IGtIbTestStateContext test, String backendMessage)
		throws IllegalGtTestStateException, TestFailedException
	{
		String mn = "receiveBackendDocumentFromGt";
		IGtIbTestState state = this;
		try
		{
			if(BackendUtil.validateBackendMessageFromGt(test.getTestId(), backendMessage))
			{
				state = new ReceivedBackendFromGtState();
			}
		}
		catch (IOException e)
		{
			Logger.warn(this.getClass().getSimpleName(), mn, "Error reading message config file", e);
			throw new TestFailedException("Error reading message config file");
		}
		return state;
	}
	
	
	@Override
	public IGtIbTestState timeOut(IGtIbTestStateContext test) throws IllegalGtTestStateException
	{
		test.terminate();
		return new TimedOutState();
	}

	public String getState()
	{
		// TODO Auto-generated method stub
		return STATE;
	}

	public Date getCreateDate()
	{
		// TODO Auto-generated method stub
		return _createDt;
	}
}
