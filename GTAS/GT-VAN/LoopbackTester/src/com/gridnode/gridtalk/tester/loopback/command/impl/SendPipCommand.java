/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendPipCommand.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.command.impl;

import com.gridnode.gridtalk.tester.loopback.cfg.ConfigMgr;
import com.gridnode.gridtalk.tester.loopback.command.CommandException;
import com.gridnode.gridtalk.tester.loopback.dao.RnifMessageDao;
import com.gridnode.gridtalk.tester.loopback.exception.TestNotFoundException;
import com.gridnode.gridtalk.tester.loopback.helpers.AbstractSingleActionPipMessageHelper;
import com.gridnode.gridtalk.tester.loopback.helpers.MessageHelperException;
import com.gridnode.gridtalk.tester.loopback.helpers.PipMessageHelper;
import com.gridnode.gridtalk.tester.loopback.testbed.TestCache;
import com.gridnode.gridtalk.tester.loopback.testbed.impl.inbound.GtIbTest;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;
import com.gridnode.gridtalk.tester.loopback.util.TestIdGenerator;

/**
 * Command class to send a PIP 0C2 message
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class SendPipCommand extends AbstractCommand
{
	protected AbstractSingleActionPipMessageHelper _rnifMsgHelper = null;
	private String _pipCode = null;
	private String _testId = null;
	/**
	 * 
	 */
	public SendPipCommand(String pipCode)
	{
		_pipCode = pipCode;
		_rnifMsgHelper = new PipMessageHelper(_pipCode, new RnifMessageDao());
		init();
	}

	private void init()
	{
		_testId = TestIdGenerator.generateTestId();		
	}
	/* (non-Javadoc)
	 * @see com.gridnode.gridtalk.tester.loopback.command.ISendCommand#execute(java.lang.String)
	 */
	public void execute(String content) throws CommandException
	{
		String mn = "execute";
		
		logDebug(mn, "Start. Test Id:"+_testId);
		try 
		{
			String message = generateMessage(_testId);
			GtIbTest test = createTest(_testId);
			storeTest(test);
			test.sendRnifToGt(message);
			logDebug(mn, "End. Test Id:"+_testId);
		} catch (IllegalGtTestStateException e) {
			logWarn(mn, e.getMessage(), e);
			throw new CommandException(CommandException.TEST_FAILED, _testId);
		} catch (TestFailedException e) {
			logWarn(mn, "Error while running test. Test Id:"+_testId, e);
      throw new CommandException(CommandException.TEST_FAILED, _testId);
		}
		catch (MessageHelperException e)
		{
			logWarn(mn, "Error in message helper:"+_testId, e);
      throw new CommandException(CommandException.TEST_FAILED, _testId);
		}
		catch(Throwable t)
		{
			logWarn(mn, "Unexpected error",t);
      throw new CommandException(CommandException.TEST_FAILED, _testId);			
		}
	  finally
	  {
	  	logTestState();
	  }
		
	}

	protected String generateMessage(String pipInstId) throws MessageHelperException
	{
		String message = null;
		String senderDuns = ConfigMgr.getPartnerConfig().getDuns();
		String receiverDuns = ConfigMgr.getBackendConfig().getDuns();
		String documentId = pipInstId;
		
		message = _rnifMsgHelper.generatePipMessage(senderDuns, receiverDuns, pipInstId, documentId);
		return message;
	}
	
	protected GtIbTest createTest(String testId)
	{
		GtIbTest test = new GtIbTest(testId,ConfigMgr.getPartnerConfig().getGridtalkRnUrl(), _pipCode);
		return test;
	}
	
	protected void storeTest(GtIbTest test)
	{
		TestCache.getInstance().storeGtInboundTest(test);
	}
	
	@Override
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