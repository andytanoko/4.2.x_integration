/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceivedAckAndBackendFromGtState.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.impl.inbound;

import com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound.AbstractGtIbTestState;

import java.util.Date;

/**
 * This class represents the state of a GridTalk-Inbound test
 * after successfully receiving both the RN-ACK and Back-end messages
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class ReceivedAckAndBackendFromGtState extends AbstractGtIbTestState
{
	public static final String STATE = "IB-ReceivedAckAndBackEnd";
	/**
	 * 
	 */
	private static final long serialVersionUID = -8212732146256346235L;
	private Date _createDt = null;
	/**
	 * 
	 */
	public ReceivedAckAndBackendFromGtState()
	{
		_createDt = new Date();
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
