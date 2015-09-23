/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceivedRnifFromGtState.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.impl.outbound;

import com.gridnode.gridtalk.tester.loopback.log.Logger;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound.AbstractGtObTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound.IGtObTestStateContext;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound.IGtObTestState;
import com.gridnode.gridtalk.tester.loopback.util.RniftUtil;

import java.io.IOException;
import java.util.Date;

/**
 * @todo Class documentation
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class ReceivedRnifFromGtState extends AbstractGtObTestState
{
	public static final String STATE = "OB-ReceivedRN";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1504927507339967641L;
	
	private Date _createDt = null;
	
	public ReceivedRnifFromGtState()
	{
		_createDt = new Date();
	}
	
	@Override
	public IGtObTestState sendAckToGt(IGtObTestStateContext test, String ackMessage) 
		throws IllegalGtTestStateException, TestFailedException
	{
		String mn = "sendAckToGt";
		IGtObTestState state = this;
		
		try
		{
			RniftUtil rnifSender = new RniftUtil();
			rnifSender.send(test.getGtRnifAckUrl(), ackMessage, "multipart/related", "RosettaNet/V02.00", "async");
			logInfo(mn, test.getTestId(),STATE,"HttpRespCode:"+rnifSender.getHttpResponseCode());							
			logInfo(mn, test.getTestId(),STATE,"HttpRespMesg:"+rnifSender.getHttpResponseMessage());							
			
			test.terminate();
			state = new SentRnAckToGtState();
		}
		catch (IOException e)
		{
			Logger.warn(this.getClass().getSimpleName(), "sendAckToGt", "Error sending HTTP message", e);
			throw new TestFailedException(e.getMessage());
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
		return _createDt;
	}
}
