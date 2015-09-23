/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InitialObState.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.impl.outbound;

import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound.AbstractGtObTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound.IGtObTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound.IGtObTestStateContext;
import com.gridnode.gridtalk.tester.loopback.util.BackendUtil;

import java.util.Date;

/**
 * @todo Class documentation
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class InitialObState extends AbstractGtObTestState
{
	public static final String STATE = "OB-Init";
	private Date _createDt = null;
	public InitialObState()
	{
		_createDt = new Date();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1223251157082211609L;

	@Override
	public IGtObTestState sendBackendDocumentToGt(IGtObTestStateContext test, String backendDocumentMessage) 
		throws IllegalGtTestStateException, TestFailedException
	{
		String mn = "sendBackendDocumentToGt";
		/*
		 * Populate with document parameters
		 * Retrieve GridTalk Backend HTTP Connector URL
		 * Send to GridTalk Backend HTTP Connector
		 */
		IGtObTestState state = this;
		try
		{
//			logDebug(mn, "Sending:\n"+backendDocumentMessage);
			BackendUtil.send(test.getGtBackendUrl(), 
			                 backendDocumentMessage.getBytes(), 
			                 test.getHttpBackendMessageType());
			state = new SentBackEndDocumentToGtState();
		}
		catch(Exception e)
		{
			state = this;
			throw new TestFailedException("Failed to send back-end document", e);
		}
		return state;
	}
	
	@Override
	public IGtObTestState timeOut(IGtObTestStateContext test) throws IllegalGtTestStateException
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
