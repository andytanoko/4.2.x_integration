/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractGtObTestState.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound;

import com.gridnode.gridtalk.tester.loopback.testbed.intf.AbstractGtTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;

/**
 * @todo Class documentation
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public abstract class AbstractGtObTestState extends AbstractGtTestState implements IGtObTestState
{
	/**
	 * @see IGtObTestState#sendBackendDocumentToGt(IGtObTestStateContext)
	 */
	public IGtObTestState sendBackendDocumentToGt(IGtObTestStateContext test, String backendDocumentMessage) 
		throws IllegalGtTestStateException, TestFailedException
	{
		throw new IllegalGtTestStateException(test.getTestId(), getState());
	}

	/**
	 * @see IGtObTestState#sendAckToGt(IGtObTestStateContext)
	 */
	public IGtObTestState sendAckToGt(IGtObTestStateContext test, String ackMessage) 
		throws IllegalGtTestStateException, TestFailedException
	{
		throw new IllegalGtTestStateException(test.getTestId(), getState());
	}

	/**
	 * @see IGtObTestState#receiveRnifFromGt(IGtObTestStateContext)
	 */
	public IGtObTestState receiveRnifFromGt(IGtObTestStateContext test) 
		throws IllegalGtTestStateException, TestFailedException
	{
		throw new IllegalGtTestStateException(test.getTestId(), getState());
	}

	public IGtObTestState timeOut(IGtObTestStateContext test) 
		throws IllegalGtTestStateException, TestFailedException
	{
		throw new IllegalGtTestStateException(test.getTestId(), getState());		
	}	
}
