/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendBackendCommand.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.command.impl;

import com.gridnode.gridtalk.tester.loopback.cfg.BackendConfig;
import com.gridnode.gridtalk.tester.loopback.cfg.ConfigMgr;
import com.gridnode.gridtalk.tester.loopback.cfg.PartnerConfig;
import com.gridnode.gridtalk.tester.loopback.command.CommandException;
import com.gridnode.gridtalk.tester.loopback.exception.TestNotFoundException;
import com.gridnode.gridtalk.tester.loopback.helpers.BackendMessageHelper;
import com.gridnode.gridtalk.tester.loopback.testbed.TestCache;
import com.gridnode.gridtalk.tester.loopback.testbed.impl.outbound.GtObTest;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;
import com.gridnode.gridtalk.tester.loopback.util.TestIdGenerator;

import java.io.IOException;

/**
 * Command class to send a back-end document to GridTalk
 * Back-end HTTP Connector
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class SendBackendCommand extends AbstractCommand
{
    private BackendMessageHelper _helper = null;
    private String _testId = null;
	/**
	 * 
	 */
	public SendBackendCommand()
	{
        _helper = new BackendMessageHelper();
        init();
	}

	private void init()
	{
		_testId = TestIdGenerator.generateTestId();
	}
	/**
     * Generate backend message
     * Create test
     * Store test
     * Send backend message (Update test state)
	 * @throws CommandException 
     * 
	 * @see AbstractCommand#execute(java.lang.String)
	 */
	public void execute(String pipCode) throws CommandException
	{
        String mn = "execute";
        
        logDebug(mn, "Start. Test Id:"+_testId);
        String msg = null;
        try 
        {
            msg = _helper.generateBackendMessage(pipCode, _testId);
            PartnerConfig partnerCfg = ConfigMgr.getPartnerConfig();
            BackendConfig backendCfg = ConfigMgr.getBackendConfig();
            GtObTest sendBackEnd = new GtObTest(_testId, partnerCfg.getGridtalkRnUrl(), backendCfg.getHttpbcUrl());
            TestCache.getInstance().storeGtOutboundTest(sendBackEnd);
            sendBackEnd.sendBackendDocumentToGt(msg);
            logDebug(mn, "End. Test Id:"+_testId);
        } 
        catch (IOException e) {
            logWarn(mn, "IO Error generating message. PIP Code:"+pipCode, e);
            throw new CommandException(CommandException.TEST_FAILED, _testId);
        }
        catch (IllegalGtTestStateException e) {
            logWarn(mn, e.getMessage(), e);
      			throw new CommandException(CommandException.TEST_FAILED, _testId);
        }
        catch (TestFailedException e) 
        {
            logWarn(mn, "Error while running test. TestId:"+_testId, e);
            throw new CommandException(CommandException.TEST_FAILED, _testId);
        }
        catch(Throwable t)
        {
            logError(mn, "Unexpected error. TestId:"+_testId, t);
            throw new CommandException(CommandException.TEST_FAILED, _testId);
        }
    	  finally
    	  {
    	  	logTestState();
    	  }
	}

	@Override
	protected void logTestState()
	{
		String mn = "logTestState";
		if(_testId != null)
		{
			GtObTest test;
			try
			{
				test = TestCache.getInstance().retrieveGtOutboundTest(_testId);
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
