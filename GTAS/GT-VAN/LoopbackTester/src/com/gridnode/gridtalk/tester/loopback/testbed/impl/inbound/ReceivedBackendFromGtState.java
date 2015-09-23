/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceivedBackendFromGtState.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.impl.inbound;

import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound.AbstractGtIbTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound.IGtIbTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound.IGtIbTestStateContext;

import java.util.Date;

/**
 * This class represents the GridTalk-Inbound state after
 * receiving the back-end message
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class ReceivedBackendFromGtState extends AbstractGtIbTestState
{
	public static final String STATE = "IB-ReceivedBackend";
	/**
	 * 
	 */
	private static final long serialVersionUID = 4858472247362305737L;
	
	private Date _createDt = null;
	public ReceivedBackendFromGtState()
	{
		_createDt = new Date();
	}
	
	@Override
	public IGtIbTestState receiveAckFromGt(IGtIbTestStateContext test, String ackMessage) 
		throws IllegalGtTestStateException, TestFailedException
	{
		IGtIbTestState s = this;
		test.terminate();
		s = new ReceivedAckAndBackendFromGtState();
		return s;
	}
	
	
	@Override
	public IGtIbTestState timeOut(IGtIbTestStateContext test) throws IllegalGtTestStateException
	{
		test.terminate();
		return new TimedOutState();
	}


	public String getState()
	{
		return STATE;
	}


	public Date getCreateDate()
	{
		return _createDt;
	}

}
