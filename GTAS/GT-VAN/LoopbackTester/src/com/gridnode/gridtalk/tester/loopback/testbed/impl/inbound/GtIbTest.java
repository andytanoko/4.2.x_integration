/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GtIbTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 15, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.impl.inbound;


import com.gridnode.gridtalk.tester.loopback.log.Logger;
import com.gridnode.gridtalk.tester.loopback.testbed.TestCache;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound.IGtIbTestStateContext;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.inbound.IGtIbTestState;

import java.net.URL;
import java.util.Timer;

/**
 * The implementation class for GridTalk-Inbound test
 * @todo Remove hard-coded attributes
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class GtIbTest implements IGtIbTestStateContext
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3069942538608538191L;
	
	private String _testId = null;
	private URL _gtRnifUrl;
	private IGtIbTestState _state = null;
	private Timer _timer = null;
	private String _pipCode = null;
	
	/**
	 * 
	 */
	private GtIbTest()
	{
		this(null, null, null);
	}

	public GtIbTest(String testId, URL gtRnifUrl, String pipCode)
	{
		String mn = "init";
		_testId = testId;
		_gtRnifUrl = gtRnifUrl;
		_pipCode = pipCode;
		logDebug(mn, "Initializing test ID: "+testId);
		_state = new InitialIbState();
		logDebug(mn, "Initialized test ID: "+testId);
	}

	public synchronized void receiveRnAckFromGt(String ackMessage) 
		throws IllegalGtTestStateException, TestFailedException
	{
		String mn = "receiveRnAckFromGt";
		
		logDebug(mn, "Start. Test ID:"+_testId);
		_state = _state.receiveAckFromGt(this, ackMessage);
		logDebug(mn, "End. Test ID:"+_testId);
	}
	
	public synchronized void receiveBackendFromGt(String backendMessage) throws IllegalGtTestStateException, TestFailedException
	{
		String mn = "receiveBackendFromGt";
		logDebug(mn, "Start. Test ID:"+_testId);
		_state = _state.receiveBackendDocumentFromGt(this, backendMessage);
		logDebug(mn, "End. Test ID:"+_testId);
	}
	
	/**
	 * Sends an RNIF message to GridTalk.
	 * @param rnifMessage The RNIF message to send
	 * @throws IllegalGtTestStateException 
	 * @throws TestFailedException 
	 */
	public synchronized void sendRnifToGt(String rnifMessage) throws IllegalGtTestStateException, TestFailedException
	{
		String mn = "sendRnifToGt";
		
		logDebug(mn, "Start. Test ID:"+_testId);
		_state = _state.sendRnifToGt(this, rnifMessage);
		logDebug(mn, "End. Test ID:"+_testId);
	}

	public synchronized void timeOut() throws IllegalGtTestStateException, TestFailedException
	{
		String mn = "timeOut";
		
		logDebug(mn, "Start. Test ID:"+_testId);
		_state = _state.timeOut(this);
		logDebug(mn, "End. Test ID:"+_testId);
	}

	public synchronized void terminate()
	{
		if(_timer != null)
			_timer.cancel();
//		TestCache.getInstance().removeGtInboundTest(_testId);
	}
	
	public URL getGtRnifUrl()
	{
		return _gtRnifUrl;
	}

	public String getTestId()
	{
		return _testId;
	}

	public String getRnifVersion()
	{
		return "RosettaNet/V02.00";
	}

	public String getSendRnifResponseType()
	{
		return "async";
	}

	public String getSendRnifMessageType()
	{
		return "multipart/related";
	}
	
	public void setTimer(Timer timer)
	{
		_timer = timer;
	}
	
	public void logDebug(String mn, String message)
	{
		Logger.debug(this.getClass().getSimpleName(), mn, message);
	}

	/**
	 * @return Returns the pipCode.
	 */
	public String getPipCode()
	{
		return _pipCode;
	}
	
	public String getState()
	{
		return _state.getState();
	}
}
