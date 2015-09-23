/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: ReceivePipCommand.java
 * 
 * ***************************************************************************
 * Date Author Changes
 * ***************************************************************************
 * Feb 15, 2007 Alain Ah Ming Created
 */
package com.gridnode.gridtalk.tester.loopback.command.impl;

import com.gridnode.gridtalk.tester.loopback.command.CommandException;
import com.gridnode.gridtalk.tester.loopback.dao.MessageDaoException;
import com.gridnode.gridtalk.tester.loopback.dao.RnifMessageDao;
import com.gridnode.gridtalk.tester.loopback.entity.RnifMessageEntity;
import com.gridnode.gridtalk.tester.loopback.exception.TestNotFoundException;
import com.gridnode.gridtalk.tester.loopback.helpers.MessageHelperException;
import com.gridnode.gridtalk.tester.loopback.helpers.PipAckMessageHelper;
import com.gridnode.gridtalk.tester.loopback.helpers.PipMessageHelper;
import com.gridnode.gridtalk.tester.loopback.testbed.TestCache;
import com.gridnode.gridtalk.tester.loopback.testbed.impl.outbound.GtObTest;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;

/**
 * Command class to receive a PIP from GridTalk
 * 
 * @author Alain Ah Ming
 * @since
 * @version
 */
public class ReceivePipCommand extends AbstractCommand
{
	private String _content = null;

	private PipMessageHelper _rnifMsgHelper = null;

	private String _testId = null;

	private GtObTest _obTest = null;

	/**
	 * Contructs a command object to receive a PIP message
	 * @param content The message content
	 * @throws CommandException
	 * 
	 */
	public ReceivePipCommand(String content) throws CommandException
	{
		String mn = "<init>";
		_content = content;
		try
		{
			init();
		}
		catch (MessageDaoException e)
		{
			logWarn(mn, "Initialization failed", e);
			throw new CommandException(CommandException.TEST_FAILED, "N.A.");
		}
		catch (MessageHelperException e)
		{
			logWarn(mn, "Initialization failed", e);
			throw new CommandException(CommandException.TEST_FAILED, "N.A.");
		}
		catch (TestNotFoundException e)
		{
			logWarn(mn, e.getMessage(), e);
			throw new CommandException(CommandException.TEST_NOT_FOUND, "N.A.");
		}
	}

	private void init() throws MessageDaoException, MessageHelperException, TestNotFoundException
	{
		String mn = "init";
		PipMessageHelper defaultMsgHelper = new PipMessageHelper(
																															"DEF",
																															new RnifMessageDao());

		RnifMessageDao dao = new RnifMessageDao();
		RnifMessageEntity m = dao.getRnifMessageTemplate("DEF");
		String pipCode = defaultMsgHelper.findValue(_content, m
				.getPath(RnifMessageEntity.PATH_PIP_CODE));
		logDebug(mn, "PIP Code:" + pipCode);

		_rnifMsgHelper = new PipMessageHelper(pipCode, dao);

		retrieveTestId();
		retrieveTest();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gridnode.gridtalk.tester.loopback.command.ICommand#execute(java.lang.String)
	 */
	public void execute(String m) throws CommandException
	{
		String mn = "execute";
		logDebug(mn, "Start. Test Id: " + _testId);

		try
		{
			_obTest.receiveRnifFromGt();
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

		// Send Ack
		try
		{
			PipAckMessageHelper ackHelper = new PipAckMessageHelper(
																															new RnifMessageDao(),
																															_rnifMsgHelper);
			_obTest.sendRnAckToGt(ackHelper.generateAckMessage(_content));
			logDebug(mn, "End. Test Id: "+_testId);
			
		}
		catch (IllegalGtTestStateException e)
		{
			logWarn(mn, e.getMessage(), e);
			throw new CommandException(CommandException.TEST_FAILED, _testId);
		}
		catch (MessageHelperException e)
		{
			logWarn(mn, "Error generating ACK", e);
			throw new CommandException(CommandException.TEST_FAILED, _testId);
		}
		catch (TestFailedException e)
		{
			logWarn(mn, "Failed to send RN ACK. Test Id:" + _testId, e);
			throw new CommandException(CommandException.TEST_FAILED, _testId);
		}
		catch (Throwable t)
		{
			logError(mn, "Unexpected Error", t);
			throw new CommandException(CommandException.TEST_FAILED, _testId);
		}
	  finally
	  {
	  	logTestState();
	  }
	}

	private void retrieveTestId() throws MessageHelperException
	{
		_testId = _rnifMsgHelper.retrieveDocumentIdFromMessage(_content);
	}

	private void retrieveTest() throws TestNotFoundException
	{
		_obTest = TestCache.getInstance().retrieveGtOutboundTest(_testId);
	}

	@Override
	protected void logTestState()
	{
		String mn = "logTestState";
		if(_obTest != null)
		{
			logTestState(mn, _testId, _obTest.getState());				
		}
		else
		{
			logError(mn, "No such test: "+_testId, null);
		}
	}
}
