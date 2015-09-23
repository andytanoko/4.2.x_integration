/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGtIbTestState.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound;

import com.gridnode.gridtalk.tester.loopback.testbed.intf.IGtTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;

/**
 * Interface for all GridTalk-Inbound test states
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public interface IGtIbTestState extends IGtTestState
{
	/**
	 * Signal this state to receive a back-end document
	 * from GridTalk
	 * 
	 * @param test The interface to the GridTalk-Inbound test object
	 * @param backendMessage The back-end message in String object 
	 * @return The next state or the same state if the operation failed
	 * @throws IllegalGtTestStateException If this state is not
	 * allowed to receive any back-end document from GridTalk
	 * @throws TestFailedException If the test has run but failed
	 */
	public IGtIbTestState receiveBackendDocumentFromGt(IGtIbTestStateContext test, String backendMessage) 
		throws IllegalGtTestStateException, TestFailedException;
	
	/**
	 * Signal this state to receive an RN-ACK from GridTalk
	 * 
	 * @param test The interface to the GridTalk-Inbound test object
	 * 
	 * @return The next state or the same state if the operation failed
	 * @throws IllegalGtTestStateException If this state is not 
	 * allowed to receive any RN-ACK from GridTalk
	 * @throws TestFailedException If the test has run but failed
	 */
	public IGtIbTestState receiveAckFromGt(IGtIbTestStateContext test, String ackMessage) 
		throws IllegalGtTestStateException, TestFailedException;
	
	/**
	 * Signal this state to send an RNIF message to GridTalk
	 * 
	 * @param test The interface to the GridTalk-Inbound test object
	 * @param rnifMessage The RNIF message
	 * 
	 * @return The next state or the same state if the operation failed
	 * @throws IllegalGtTestStateException If this state is not
	 * allowed to send any RNIF message to GridTalk
	 * @throws TestFailedException If the test has run but failed
	 */
	public IGtIbTestState sendRnifToGt(IGtIbTestStateContext test, String rnifMessage) 
		throws IllegalGtTestStateException, TestFailedException;
	
	/**
	 * Signal this state to time
	 * @param test The interface to the GridTalk-Inbound test context
	 * @return The next state or the same state if the operation failed
	 * @throws IllegalGtTestStateException If this state is not allowed to time out
	 * @throws TestFailedException If the test has run but failed
	 */
	public IGtIbTestState timeOut(IGtIbTestStateContext test) 
		throws IllegalGtTestStateException, TestFailedException;
}
