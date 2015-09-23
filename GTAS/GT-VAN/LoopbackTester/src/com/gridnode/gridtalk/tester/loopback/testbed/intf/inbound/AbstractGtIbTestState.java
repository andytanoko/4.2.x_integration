/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractGtIbTestState.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound;

import com.gridnode.gridtalk.tester.loopback.testbed.intf.AbstractGtTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;

/**
 * Abstract super class for GridTalk-Inbound test states
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public abstract class AbstractGtIbTestState extends AbstractGtTestState implements IGtIbTestState
{

	/* (non-Javadoc)
	 * @see com.gridnode.gridtalk.tester.loopback.testbed.IGtIbTestState#receiveBackendDocumentFromGt()
	 */
	public IGtIbTestState receiveBackendDocumentFromGt(IGtIbTestStateContext test,  String backendMessage)
		throws IllegalGtTestStateException, TestFailedException
	{
		throw new IllegalGtTestStateException(test.getTestId(), getState());
	}

	/* (non-Javadoc)
	 * @see com.gridnode.gridtalk.tester.loopback.testbed.IGtIbTestState#receiveAckFromGt()
	 */
	public IGtIbTestState receiveAckFromGt(IGtIbTestStateContext test, String ackMessage) 
		throws IllegalGtTestStateException, TestFailedException
	{
		throw new IllegalGtTestStateException(test.getTestId(), getState());
	}

	/* (non-Javadoc)
	 * @see com.gridnode.gridtalk.tester.loopback.testbed.IGtIbTestState#sendRnifToGt()
	 */
	public IGtIbTestState sendRnifToGt(IGtIbTestStateContext test, String rnifMessage) 
		throws IllegalGtTestStateException, TestFailedException
	{
		throw new IllegalGtTestStateException(test.getTestId(), getState());
	}
	
	public IGtIbTestState timeOut(IGtIbTestStateContext test) 
		throws IllegalGtTestStateException, TestFailedException
	{
		throw new IllegalGtTestStateException(test.getTestId(), getState());		
	}	
	
}
