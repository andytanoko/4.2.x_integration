/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SentBackEndDocumentToGtState.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.impl.outbound;

import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound.AbstractGtObTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound.IGtObTestStateContext;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound.IGtObTestState;

import java.util.Date;

/**
 * @todo Class documentation
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class SentBackEndDocumentToGtState extends AbstractGtObTestState
{
	public static final String STATE = "OB-SentBackend";
	/**
	 * 
	 */
	private static final long serialVersionUID = -7615295028136990650L;

	private Date _createDt = null;
	/**
	 * Default constructor to instantiate a new SentBackEndDocumentToGtState 
	 */
	public SentBackEndDocumentToGtState()
	{
		_createDt = new Date();
	}

	@Override 
	public IGtObTestState receiveRnifFromGt(IGtObTestStateContext test) throws IllegalGtTestStateException
	{
		return new ReceivedRnifFromGtState();
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
