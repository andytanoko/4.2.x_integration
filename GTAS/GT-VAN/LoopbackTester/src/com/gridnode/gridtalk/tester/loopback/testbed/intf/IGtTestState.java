/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGtTestState.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.intf;

import java.io.Serializable;
import java.util.Date;

/**
 * Interface class representing the state of a <code>AbstractGtTest</code> instance
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public interface IGtTestState extends Serializable
{
//	/**
//	 * Initialize the state of a <code>AbstractGtTest</code>
//	 * @throws IllegalGtTestStateException If the <code>AbstractGtTest</code> is not in
//	 * a state to start up
//	 */
//	public void startUp() throws IllegalGtTestStateException;
//	
//	/**
//	 * Terminate the state of a <code>AbstractGtTest</code>
//	 * @throws IllegalGtTestStateException If the <code>AbstractGtTest</code> is not in
//	 * a state to terminate
//	 */
//	public void terminate() throws IllegalGtTestStateException;
	/**
	 * Signal this state to time out.
	 * @param test The interface for the test state context 
	 * @return The next state or the same state if the operation failed
	 * @throws IllegalGtTestStateException If this state is not allowed to time out
	 */
//	public IGtTestState timeOut(IGtTestStateContext test) throws IllegalGtTestStateException;
	public String getState(); 
	
	public Date getCreateDate();
}
