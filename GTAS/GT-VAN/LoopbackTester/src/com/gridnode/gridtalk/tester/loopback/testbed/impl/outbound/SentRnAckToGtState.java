/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SentRnAckToGtState.java
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

import java.util.Date;

/**
 * @todo Class documentation
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class SentRnAckToGtState extends AbstractGtObTestState
{
	public static final String STATE = "OB-SentAck";
	/**
	 * 
	 */
	private static final long serialVersionUID = 8886625371221954347L;
	
	private Date _createDt = null;
	
	public SentRnAckToGtState()
	{
		_createDt = new Date();
	}
	
	@Override
	public IGtObTestState timeOut(IGtObTestStateContext test) throws IllegalGtTestStateException, TestFailedException
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
