/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TimeOutCommand.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 26, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.command.impl;

import com.gridnode.gridtalk.tester.loopback.command.CommandException;
import com.gridnode.gridtalk.tester.loopback.exception.TestNotFoundException;
import com.gridnode.gridtalk.tester.loopback.log.Logger;
import com.gridnode.gridtalk.tester.loopback.testbed.TestCache;
import com.gridnode.gridtalk.tester.loopback.testbed.impl.inbound.GtIbTest;
import com.gridnode.gridtalk.tester.loopback.testbed.impl.outbound.GtObTest;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;

/**
 * @todo Class documentation
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class TimeOutCommand extends AbstractCommand
{
	private String _testId = null;
	private int _testDirection = -1;	
	private String _testState = null;
	/**
	 * 
	 */
	private TimeOutCommand()
	{
		
	}
	
	/**
	 * Construct a command object to time out a test instance
	 * @param testId The ID of the test to time out
	 * @param direction  The direction (from GridTalk point of view) of the test
	 * 
	 *  @see com.gridnode.gridtalk.tester.loopback.testbed.TestCache#CACHE_INBOUND
	 *  @see com.gridnode.gridtalk.tester.loopback.testbed.TestCache#CACHE_OUTBOUND
	 */
	public TimeOutCommand(String testId, int direction)
	{
		_testId = testId;
		_testDirection = direction;
	}

	/* (non-Javadoc)
	 * @see com.gridnode.gridtalk.tester.loopback.command.impl.AbstractCommand#logTestState()
	 */
	@Override
	protected void logTestState()
	{
		String mn = "logTestState";
		logTestState(mn, _testId, _testState);
	}

	/* (non-Javadoc)
	 * @see com.gridnode.gridtalk.tester.loopback.command.ICommand#execute(java.lang.String)
	 */
	public void execute(String message) throws CommandException
	{
		String mn = "execute";
		logDebug(mn, "Start. Test Id:" + _testId);
		switch(_testDirection)
		{
			case(TestCache.CACHE_INBOUND):
				timeOutGtInboundTest();
				break;
			case(TestCache.CACHE_OUTBOUND):
				timeOutGtOutboundTest();
				break;
			default:
				logError(mn,"Undefined direction: "+_testDirection, null);
		}
		logTestState();
		logDebug(mn, "End. Test Id:" + _testId);
		
	}

	private void timeOutGtOutboundTest()
	{
		String mn = "timeOutGtOutboundTest";
		Logger.warn(this.getClass().getSimpleName(), mn, "Test ID:"+_testId);
		GtObTest obTest = null;
		try
		{
			obTest = TestCache.getInstance().retrieveGtOutboundTest(_testId);
			obTest.timeOut();
			TestCache.getInstance().removeGtOutboundTest(_testId);
			Logger.warn(this.getClass().getSimpleName(), mn, "Timed out: Test ID: "+_testId);
			_testState = obTest.getState();
		}
		catch (IllegalGtTestStateException e)
		{
			Logger.warn(this.getClass().getSimpleName(), mn, "Wrong state to time out GT-OB test: "+_testId, e);
			if(obTest != null)
				_testState = obTest.getState();
		} 
		catch (TestFailedException e) {
			Logger.warn(this.getClass().getSimpleName(), mn, "Unable to time out GT-OB test: "+_testId, e);
			if(obTest != null)
				_testState = obTest.getState();
		}
		catch (TestNotFoundException e)
		{
			Logger.warn(this.getClass().getSimpleName(), mn, e.getMessage());
		}
	}

	private void timeOutGtInboundTest()
	{
		String mn = "timeOutGtInboundTest";
		Logger.warn(this.getClass().getSimpleName(), mn, "Test ID:"+_testId);
		GtIbTest ibTest = null;
		try
		{
			ibTest = TestCache.getInstance().retrieveGtInboundTest(_testId);;
			ibTest.timeOut();
			Logger.warn(this.getClass().getSimpleName(), mn, "Timed out: Test ID: "+_testId);
			TestCache.getInstance().removeGtInboundTest(_testId);
			_testState = ibTest.getState();
		}
		catch (IllegalGtTestStateException e)
		{
			Logger.warn(this.getClass().getSimpleName(), "timeOutGtInboundTest", "Wrong state to time out GT-IB test: "+_testId, e);
			if(ibTest != null)
				_testState = ibTest.getState();
		} catch (TestFailedException e) {
			Logger.warn(this.getClass().getSimpleName(), "timeOutGtInboundTest", "Unable to time out GT-IB test: "+_testId, e);
			if(ibTest != null)
				_testState = ibTest.getState();
		}
		catch (TestNotFoundException e)
		{
			Logger.warn(this.getClass().getSimpleName(), mn, e.getMessage());
		}
	}

}
