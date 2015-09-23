/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGtObTestState.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound;

import com.gridnode.gridtalk.tester.loopback.testbed.intf.IGtTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;

/**
 * Interface for all GridTalk-Outbound test states
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public interface IGtObTestState extends IGtTestState
{
	/**
	 * Signal this state to send a back-end document to GridTalk
	 * @param test The GridTalk-Outbound test interface 
	 * @return The next state or the same state if the operation failed
	 * @throws IllegalGtTestStateException If this state is not allowed 
	 * to send any back-end document to GT. 
	 * @throws TestFailedException If the test has run but failed
	 */
	public IGtObTestState sendBackendDocumentToGt(IGtObTestStateContext test, String backendDocumentMessage) 
		throws IllegalGtTestStateException, TestFailedException;
	
	/**
	 * Signal this state to send an RN-ACK document to GridTalk
	 * @param test The GridTalk-Outbound test interface 
	 * @return The next state or the same state if the operation failed
	 * @throws IllegalGtTestStateException If this state is not allowed
	 * to send any RN-ACK
	 * @throws TestFailedException If the test has run but failed
	 */
	public IGtObTestState sendAckToGt(IGtObTestStateContext test, String ackMessage) 
		throws IllegalGtTestStateException, TestFailedException;
	
	/**
	 * Signal this state to receive an RNIF messsage from GridTalk
	 * @return The next state or the same state if the operation failed
	 * @throws IllegalGtTestStateException If this state is not allowed
	 * to receive any RNIF message
	 * @throws TestFailedException If the test has run but failed
	 */
	public IGtObTestState receiveRnifFromGt(IGtObTestStateContext test) 
		throws IllegalGtTestStateException, TestFailedException;
	
	/**
	 * Signal this state to time out
	 * @param test The GridTalk-Outbound test interface
	 * @return The next state or the same state if the operation failed
	 * @throws IllegalGtTestStateException If this is state is not allowed to time out
	 * @throws TestFailedException If the test has run but failed
	 */
	public IGtObTestState timeOut(IGtObTestStateContext test) 
		throws IllegalGtTestStateException, TestFailedException;
}
