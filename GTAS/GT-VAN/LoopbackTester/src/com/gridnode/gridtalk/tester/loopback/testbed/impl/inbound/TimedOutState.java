/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TimedOutState.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 16, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.impl.inbound;

import com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound.AbstractGtIbTestState;

import java.util.Date;

/**
 * This class represents the GridTalk-Inbound test state
 * after time-out
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class TimedOutState extends AbstractGtIbTestState
{
	public static final String STATE = "IB-TimedOut";
	/**
	 * 
	 */
	private static final long serialVersionUID = -3996538688466795266L;

	private Date _createDt = null;
	
	/**
	 * 
	 */
	public TimedOutState()
	{
		_createDt = new Date();
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
