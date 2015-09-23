/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: ReceiveBackendCommand.java
 * 
 * ***************************************************************************
 * Date Author Changes
 * ***************************************************************************
 * Feb 15, 2007 Alain Ah Ming Created
 */
package com.gridnode.gridtalk.tester.loopback.command.impl;

import java.io.IOException;

import com.gridnode.gridtalk.tester.loopback.command.CommandException;
import com.gridnode.gridtalk.tester.loopback.exception.TestNotFoundException;
import com.gridnode.gridtalk.tester.loopback.helpers.BackendMessageHelper;
import com.gridnode.gridtalk.tester.loopback.testbed.TestCache;
import com.gridnode.gridtalk.tester.loopback.testbed.impl.inbound.GtIbTest;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;

/**
 * Command class to receive a back-end document from GridTalk
 * 
 * @author Alain Ah Ming
 * @since
 * @version
 */
public class ReceiveBackendCommand extends AbstractCommand
{
	private BackendMessageHelper _helper = null;
	private String _message = null;
	private String _testId = null;
	private GtIbTest _ibTest = null;
	
	/**
	 * 
	 */
	private ReceiveBackendCommand()
	{
		
	}

	public ReceiveBackendCommand(String message) throws CommandException
	{
		_helper = new BackendMessageHelper();
		_message = message;
		init();
	}

	private void init() throws CommandException
	{
		initTest();
	}
	
	private void initTest() throws CommandException
	{
		String mn = "initTest";
		try
		{
			_testId = _helper.findDocId(_message);
			_ibTest = TestCache.getInstance()
			.retrieveGtInboundTest(_testId);
		}
		catch (IOException e)
		{
			logWarn(mn, "Initialization IO error", e);
			throw new CommandException(CommandException.TEST_FAILED, _testId);
		}
		catch (TestNotFoundException e)
		{
			logWarn(mn, e.getMessage(), e);
			throw new CommandException(CommandException.TEST_NOT_FOUND, _testId);
		}
	}
	
	/**
	 * Find document Id Find test Run test (Update state)
	 * 
	 * @see com.gridnode.gridtalk.tester.loopback.command.ICommand#execute(java.lang.String)
	 */
	public void execute(String m) throws CommandException
	{
		String mn = "execute";
		logDebug(mn, "Start. Test Id:"+_testId);
		try
		{
			_ibTest.receiveBackendFromGt(_message);
			logTestState();
			// REturn backend
			
			new ReturnBackendCommand(_testId).execute(_ibTest.getPipCode());
			
			logDebug(mn, "End. Test Id: "+_testId);
			
		}
		catch (IllegalGtTestStateException e)
		{
			logWarn(mn, e.getMessage(), e);
			throw new CommandException(CommandException.TEST_FAILED, _testId);
		}
		catch (TestFailedException e)
		{
			logWarn(mn, e.getMessage(), e);
			throw new CommandException(CommandException.TEST_FAILED, _testId);
		}
		catch (Throwable t)
		{
			logWarn(mn, "Unexpected Error", t);
			throw new CommandException(CommandException.TEST_FAILED, _testId);
		}
	}

	protected void logTestState()
	{
		String mn = "logTestState";
		if(_ibTest != null)
		{
			logTestState(mn, _testId, _ibTest.getState());				
		}
		else
		{
			logError(mn, "No such test: "+_testId, null);
		}
	}

}
