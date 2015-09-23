/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TestTimeOutTimerTask.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 16, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.impl;

import com.gridnode.gridtalk.tester.loopback.command.CommandException;
import com.gridnode.gridtalk.tester.loopback.command.impl.TimeOutCommand;
import com.gridnode.gridtalk.tester.loopback.log.Logger;

import java.util.TimerTask;

/**
 * Timer task class to time out test instances
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class TestTimeOutTimerTask extends TimerTask
{
	private String _testId = null;
	private int _testType = TEST_TYPE_UNDEFINED;
	public static final int TEST_TYPE_UNDEFINED = -1;
	public static final int TEST_TYPE_OUTBOUND = 0;
	public static final int TEST_TYPE_INBOUND = 1;
	
	public TestTimeOutTimerTask(String testId, int testType)
	{
		_testId = testId;
		_testType = testType;
	}
	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run()
	{
		Logger.debug(this.getClass().getSimpleName(),"run","Start");
		try
		{
			new TimeOutCommand(_testId, _testType).execute(null);
		}
		catch (CommandException e)
		{
			Logger.error(this.getClass().getSimpleName(), "run", "Error timing out. Test Id:"+_testId);
		}
		cancel();
		Logger.debug(this.getClass().getSimpleName(),"run","End");
	}
	
//	private void timeOutGtOutboundTest(String testId)
//	{
//		String mn = "timeOutGtOutboundTest";
//		Logger.warn(this.getClass().getSimpleName(), mn, "Test ID:"+testId);
//		GtObTest obTest = TestCache.getInstance().retrieveGtOutboundTest(testId);
//		try
//		{
//			obTest.timeOut();
//			TestCache.getInstance().removeGtOutboundTest(testId);
//			Logger.warn(this.getClass().getSimpleName(), mn, "Timed out: Test ID: "+testId);
//		}
//		catch (IllegalGtTestStateException e)
//		{
//			Logger.warn(this.getClass().getSimpleName(), mn, "Wrong state to time out GT-OB test: "+testId, e);
//		} catch (TestFailedException e) {
//			Logger.warn(this.getClass().getSimpleName(), mn, "Unable to time out GT-OB test: "+testId, e);
//		}
//	}
//
//	private void timeOutGtInboundTest(String testId)
//	{
//		Logger.warn(this.getClass().getSimpleName(), "timeOutGtInboundTest", "Test ID:"+testId);
//		GtIbTest ibTest = TestCache.getInstance().retrieveGtInboundTest(testId);
//		try
//		{
//			ibTest.timeOut();
//			Logger.warn(this.getClass().getSimpleName(), "timeOutGtInboundTest", "Timed out: Test ID: "+testId);
//			TestCache.getInstance().removeGtInboundTest(testId);
//		}
//		catch (IllegalGtTestStateException e)
//		{
//			Logger.warn(this.getClass().getSimpleName(), "timeOutGtInboundTest", "Wrong state to time out GT-IB test: "+testId, e);
//		} catch (TestFailedException e) {
//			Logger.warn(this.getClass().getSimpleName(), "timeOutGtInboundTest", "Unable to time out GT-IB test: "+testId, e);
//		}
//	}
}
