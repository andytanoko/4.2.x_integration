/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InitialIbState.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.impl.inbound;

import java.io.IOException;
import java.util.Date;

import com.gridnode.gridtalk.tester.loopback.log.Logger;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound.AbstractGtIbTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound.IGtIbTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound.IGtIbTestStateContext;
import com.gridnode.gridtalk.tester.loopback.util.RniftUtil;

/**
 * This class represents the initial state of a GridTalk-Inbound
 * initial state 
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class InitialIbState extends AbstractGtIbTestState
{
	public static final String STATE = "IB-Init";
	/**
	 * 
	 */
	private static final long serialVersionUID = -4507599062135629762L;

	private Date _createDt = null;
	
	/**
	 * 
	 */
	public InitialIbState()
	{
		_createDt = new Date();
	}

	@Override
	public IGtIbTestState sendRnifToGt(IGtIbTestStateContext test, String rnifMessage) 
		throws IllegalGtTestStateException, TestFailedException
	{
		String mn = "sendRnifToGt";
		IGtIbTestState state = this;
//		try
//		{
            try
						{
            	RniftUtil rnifSender = new RniftUtil();
							boolean sentOk = rnifSender.send(test.getGtRnifUrl(), 
							                                  rnifMessage,
							                                  test.getSendRnifMessageType(),
							                                  test.getRnifVersion(),
							                                  test.getSendRnifResponseType() );
							logInfo(mn, test.getTestId(),STATE,"HttpRespCode:"+rnifSender.getHttpResponseCode());							
							logInfo(mn, test.getTestId(),STATE,"HttpRespMesg:"+rnifSender.getHttpResponseMessage());							
							if(!sentOk)
							{
								throw new TestFailedException("Message not sent");
							}
						}
						catch (IOException e)
						{
							Logger.warn(this.getClass().getSimpleName(), mn, "Error sending HTTP message", e);
							throw new TestFailedException("Error sending HTTP message");
						}
			state = new SentRnifToGtState();
//		}
//		catch (Exception e)
//		{
//			Logger.warn(this.getClass().getSimpleName(), "sendRnifToGt", "Error sending out", e);
//			throw new TestFailedException(e.getMessage());
//		}
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
