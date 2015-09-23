/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReturnBackendCommand.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 24, 2007			Alain Ah Ming						Created
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

import java.io.IOException;

/**
 * Command class to return a backend document
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class ReturnBackendCommand extends AbstractCommand
{
	private String _testId = null;
	private BackendMessageHelper _helper = null;
	/**
	 * 
	 */
	public ReturnBackendCommand(String requestDocId)
	{
		_testId = requestDocId;
		_helper = new BackendMessageHelper();
	}

	/* (non-Javadoc)
	 * @see com.gridnode.gridtalk.tester.loopback.command.ICommand#execute(java.lang.String)
	 */
	public void execute(String pipCode) throws CommandException
	{
    String mn = "execute";
    logDebug(mn, "Start. Test Id:"+_testId);
    String msg = null;
    try 
    {
        msg = _helper.generateBackendMessage(pipCode, _testId, _testId);
        PartnerConfig partnerCfg = ConfigMgr.getPartnerConfig(); 
        BackendConfig backendCfg = ConfigMgr.getBackendConfig();
        GtObTest sendBackEnd = new GtObTest(_testId, partnerCfg.getGridtalkRnUrl(), backendCfg.getHttpbcUrl());
        TestCache.getInstance().storeGtOutboundTest(sendBackEnd);
        sendBackEnd.sendBackendDocumentToGt(msg);
        logDebug(mn, "End. Test Id: "+_testId);
        
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
        logError(mn, "Error generating message. TestId:"+_testId, t);
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
				e.printStackTrace();
			}
		}
		else
		{
			logError(mn, "TestId is NULL", null);
		}
	}
}
