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
package com.gridnode.gridtalk.tester.loopback.testbed.impl.outbound;

import com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound.AbstractGtObTestState;

import java.util.Date;

/**
 * @todo Class documentation
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class TimedOutState extends AbstractGtObTestState
{
	public static final String STATE = "OB-TimedOut";
	/**
	 * 
	 */
	private static final long serialVersionUID = -2906346163333164212L;

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
		return STATE;
	}

	public Date getCreateDate()
	{
		return _createDt;
	}
}
