/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveAckCommand.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.command.impl;

import com.gridnode.gridtalk.tester.loopback.command.CommandException;
import com.gridnode.gridtalk.tester.loopback.dao.IRnifMessageDao;
import com.gridnode.gridtalk.tester.loopback.dao.MessageDaoException;
import com.gridnode.gridtalk.tester.loopback.dao.RnifMessageDao;
import com.gridnode.gridtalk.tester.loopback.entity.RnifMessageEntity;
import com.gridnode.gridtalk.tester.loopback.exception.TestNotFoundException;
import com.gridnode.gridtalk.tester.loopback.helpers.IPipMessageHelper;
import com.gridnode.gridtalk.tester.loopback.helpers.MessageHelperException;
import com.gridnode.gridtalk.tester.loopback.helpers.PipAckMessageHelper;
import com.gridnode.gridtalk.tester.loopback.helpers.PipMessageHelper;
import com.gridnode.gridtalk.tester.loopback.testbed.TestCache;
import com.gridnode.gridtalk.tester.loopback.testbed.impl.inbound.GtIbTest;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;


/**
 * Command class to receive an RN ACK
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class ReceiveAckCommand extends AbstractCommand
{
	private PipAckMessageHelper _ackHelper = null;
  private IRnifMessageDao _ackDao = null;
  private String _testId = null;
  private String _content = null;
	/**
	 * @throws CommandException 
	 * @throws MessageHelperException 
	 * 
	 */
	public ReceiveAckCommand(IRnifMessageDao ackDao, String content) throws CommandException
	{
		_ackDao = ackDao;
		_ackHelper = new PipAckMessageHelper(_ackDao);
		_content = content;
		init();
	}

	private void init() throws CommandException
	{
		try
		{
			initTestId();
		}
		catch (MessageHelperException e)
		{
			throw new CommandException("Initialization Error", e);
		}
	}
	
	private void initTestId() throws MessageHelperException
	{
		_testId = _ackHelper.findPipInstId(_content);
	}
	/**
     * Retrieve the PIP instance Id from the message
     * Complete initialization of ACK message helper
     * Retrieve the test from the outbound cache
     * Run the test to receive the ACK
	 * @see com.gridnode.gridtalk.tester.loopback.command.ICommand#execute(java.lang.String)
	 */
	public void execute(String m) throws CommandException
	{
		String mn = "execute";
		logDebug(mn, "Start. Test Id: "+_testId);
		try
		{
	    RnifMessageEntity rnifMsgTemplate = _ackDao.getRnifMessageTemplate(RnifMessageEntity.PIP_ACK);
	    String pipCodePath = rnifMsgTemplate.getPath(RnifMessageEntity.PATH_PIP_CODE);
	    String pipCode = _ackHelper.findValue(_content, pipCodePath);
	    IPipMessageHelper ackedMsgHelper = new PipMessageHelper(pipCode, new RnifMessageDao());
	    _ackHelper.setRefMessageHelper(ackedMsgHelper);
			GtIbTest ibTest = TestCache.getInstance().retrieveGtInboundTest(_testId);
			ibTest.receiveRnAckFromGt(_content);
			logDebug(mn, "End. Test Id: "+_testId);
		}
		catch (IllegalGtTestStateException e)
		{
			logWarn(mn, e.getMessage(), e);
			throw new CommandException(CommandException.TEST_FAILED, _testId);
		} 
		catch (TestFailedException e) 
		{
			logWarn(mn, "Error while running test. Test Id:"+_testId, e);
			throw new CommandException(CommandException.TEST_FAILED, _testId);
		}
		catch (MessageDaoException e)
		{
			logWarn(mn, "Error in DAO. Test Id:"+_testId, e);
			throw new CommandException(CommandException.TEST_FAILED, _testId);
		}
		catch (TestNotFoundException e)
		{
			logWarn(mn, e.getMessage(), e);
			throw new CommandException(CommandException.TEST_NOT_FOUND, _testId);
		}
	  catch(Throwable e)
	  {
	    logWarn(mn, "Unexpected Error. Test Id:"+_testId, e);
	    throw new CommandException(CommandException.TEST_FAILED, _testId);            
	  }
	  finally
	  {
	  	logTestState();
	  }
}

	protected void logTestState()
	{
		String mn = "logTestState";
		if(_testId != null)
		{
			GtIbTest test;
			try
			{
				test = TestCache.getInstance().retrieveGtInboundTest(_testId);
				logTestState(mn, _testId, test.getState());				
			}
			catch (TestNotFoundException e)
			{
				logWarn(mn, e.getMessage(), null);
			}
		}
		else
		{
			logError(mn, "TestId is NULL", null);
		}
	}
}
