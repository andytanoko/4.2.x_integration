/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GtObTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 16, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed.impl.outbound;

import com.gridnode.gridtalk.tester.loopback.log.Logger;
import com.gridnode.gridtalk.tester.loopback.testbed.TestCache;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.TestFailedException;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound.IGtObTestState;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.outbound.IGtObTestStateContext;

import java.net.URL;
import java.util.Timer;

/**
 * @todo Class documentation
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class GtObTest implements IGtObTestStateContext, Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6328257022055105539L;
	private String _testId = null;
	private URL _gtRnifAckUrl = null;
	private URL _gtBackendUrl = null;
	private IGtObTestState _state = null;
	private String _httpBackendMessageType = "text/html";
	private Timer _timer;

	private GtObTest()
	{
		
	}
	
	public GtObTest(String testId, URL gtRnifAckUrl, URL gtBackendUrl)
	{
		_state = new InitialObState();
		_testId = testId;
		_gtRnifAckUrl = gtRnifAckUrl;
		_gtBackendUrl = gtBackendUrl;
	}
	
	public String getHttpBackendMessageType()
	{
		return _httpBackendMessageType;
	}
	
	/* (non-Javadoc)
	 * @see com.gridnode.gridtalk.tester.loopback.testbed.intf.IGtTestStateContext#getTestId()
	 */
	public String getTestId()
	{
		return _testId;
	}

	public URL getGtBackendUrl()
	{
		return _gtBackendUrl;
	}

	public URL getGtRnifAckUrl()
	{
		return _gtRnifAckUrl;
	}
	
	public void receiveRnifFromGt() throws IllegalGtTestStateException, TestFailedException
	{
		String mn = "receiveRnifFromGt";
		logDebug(mn, "Start. Test ID:"+_testId);
		_state = _state.receiveRnifFromGt(this);
		logDebug(mn, "End. Test ID:"+_testId);
	}
	
	public void sendBackendDocumentToGt(String backendDocumentMessage) 
		throws IllegalGtTestStateException, TestFailedException
	{
		String mn = "sendBackendDocumentToGt";
		logDebug(mn, "Start. Test ID:"+_testId);
		_state = _state.sendBackendDocumentToGt(this, backendDocumentMessage);
		logDebug(mn, "End. Test ID:"+_testId);
	}
	
	public void sendRnAckToGt(String rnifAckMessage) throws IllegalGtTestStateException, TestFailedException
	{
		String mn = "sendRnAckToGt";
		logDebug(mn, "Start. Test ID:"+_testId);
		_state = _state.sendAckToGt(this, rnifAckMessage);
		logDebug(mn, "End. Test ID:"+_testId);
	}
	
	public void timeOut() throws IllegalGtTestStateException, TestFailedException
	{
		String mn = "timeOut";
		logDebug(mn, "Start. Test ID:"+_testId);
		_state = _state.timeOut(this);
		logDebug(mn, "End. Test ID:"+_testId);
	}

	public void terminate()
	{
		if(_timer != null)
			_timer.cancel();
//		TestCache.getInstance().removeGtOutboundTest(_testId);
	}
	

	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
	
	/**
	 * @param timer The timer to set.
	 */
	public void setTimer(Timer timer)
	{
		_timer = timer;
	}

	public String getState()
	{
		return _state.getState();
	}

	private void logDebug(String methodName, String message)
	{
		Logger.debug(this.getClass().getSimpleName(), methodName, message);
	}
}
