/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TestCache.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 16, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.testbed;

import com.gridnode.gridtalk.tester.loopback.cfg.ConfigMgr;
import com.gridnode.gridtalk.tester.loopback.exception.TestNotFoundException;
import com.gridnode.gridtalk.tester.loopback.log.Logger;
import com.gridnode.gridtalk.tester.loopback.testbed.impl.TestTimeOutTimerTask;
import com.gridnode.gridtalk.tester.loopback.testbed.impl.inbound.GtIbTest;
import com.gridnode.gridtalk.tester.loopback.testbed.impl.inbound.ReceivedAckAndBackendFromGtState;
import com.gridnode.gridtalk.tester.loopback.testbed.impl.inbound.TimedOutState;
import com.gridnode.gridtalk.tester.loopback.testbed.impl.outbound.GtObTest;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Timer;

/**
 * In-memory tests cache
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class TestCache
{
	public static final int CACHE_INBOUND = 1;
	public static final int CACHE_OUTBOUND = 0;
	private static TestCache _self = null;
	private static Object _lock = new Object();
	private Hashtable<String, GtIbTest> _inboundCache;
	private Hashtable<String, GtObTest> _outboundCache;
	
	private TestCache()
	{
		_inboundCache = new Hashtable<String, GtIbTest>();
		_outboundCache = new Hashtable<String, GtObTest>();
	}
	
	public static TestCache getInstance()
	{
		if(_self == null)
		{
			synchronized(_lock)
			{
				if(_self == null)
				{
					synchronized(_lock)
					{
						_self = new TestCache();
					}
				}
			}
		}
		return _self;
	}
	
	public void storeGtInboundTest(GtIbTest test)
	{
		String mn = "storeGtInboundTest";
		
//		GtIbTest copyToStore = null;
		String testId = null;
//		try
//		{
			testId = test.getTestId();
//			copyToStore = (GtIbTest)test.clone();
//			
			if(!_inboundCache.containsKey(testId))
			{
				// New one, set a timer to time out
				long timeout = ConfigMgr.getMainConfig().getIbTimeOutInSecs();
				if(timeout > 0)
				{
					Logger.debug(this.getClass().getSimpleName(),
					             mn, 
					             "Time-out set at:"+timeout+"s. Test ID:"+testId);
					Timer timer = new Timer();
					TestTimeOutTimerTask timeoutTask = 
						new TestTimeOutTimerTask(testId, TestTimeOutTimerTask.TEST_TYPE_INBOUND);
					timer.schedule(timeoutTask, timeout*1000);
					test.setTimer(timer);
				}
				else
				{
					Logger.debug(this.getClass().getSimpleName(),
					             mn, 
					             "No time-out set. Test ID:"+testId);					
				}
			}
			_inboundCache.put(testId, test);
			Logger.debug(this.getClass().getSimpleName(),mn,"Nos. of tests in cache: " +_inboundCache.size());
//		}
//		catch (CloneNotSupportedException e)
//		{
//			Logger.warn(this.getClass().getSimpleName(), "storeGtInboundTest", "Unable to make copy for:" +testId, e);
//		}
//		catch(RuntimeException re)
//		{
//			Logger.warn(this.getClass().getSimpleName(), "storeGtInboundTest", "Test Id:" +testId, re);			
//		}
	}
	
	public GtIbTest retrieveGtInboundTest(String testId) throws TestNotFoundException
	{
		String mn = "retrieveGtInboundTest";
		Logger.debug(this.getClass().getSimpleName(), mn, "Retrieving Test ID:"+testId);
		GtIbTest storedCopy = _inboundCache.get(testId);
		if(storedCopy == null)
		{
			throw new TestNotFoundException(testId);
		}
//		Logger.debug(this.getClass().getSimpleName(), mn, "Test ID:"+testId+" found? "+(storedCopy!=null));
		
		return storedCopy;
	}
	
	public void removeGtInboundTest(String testId)
	{
		_inboundCache.remove(testId);
	}
	
	public void storeGtOutboundTest(GtObTest test)
	{
		String mn = "storeGtOutboundTest";
//		GtObTest copyToStore = null;
		String testId = null;
//		try
//		{
			testId = test.getTestId();
//			copyToStore = (GtObTest)test.clone();
//			
			if(!_outboundCache.containsKey(testId))
			{
				// New one, set a timer to time out
				long timeout = ConfigMgr.getMainConfig().getObTimeOutInSecs();
				if(timeout > 0)
				{
					Logger.debug(this.getClass().getSimpleName(),
					             mn, 
					             "Time-out set at:"+timeout+"s. Test ID:"+testId);
					Timer timer = new Timer();
					TestTimeOutTimerTask timeoutTask = 
						new TestTimeOutTimerTask(testId, TestTimeOutTimerTask.TEST_TYPE_OUTBOUND);
					timer.schedule(timeoutTask, timeout*1000);
					test.setTimer(timer);
				}
				else
				{
					Logger.debug(this.getClass().getSimpleName(),
					             mn, 
					             "No time-out set. Test ID:"+testId);					
				}
			}
			
			_outboundCache.put(testId, test);
			Logger.debug(this.getClass().getSimpleName(),mn,"Nos. of tests in cache: " +_outboundCache.size());
//		}
//		catch (CloneNotSupportedException e)
//		{
//			Logger.warn(this.getClass().getSimpleName(), "storeGtOutboundTest", "Unable to make copy for:" +testId, e);
//		}
//		catch(RuntimeException re)
//		{
//			Logger.warn(this.getClass().getSimpleName(), "storeGtOutboundTest", "Test Id:" +testId, re);			
//		}
	}
	
	public GtObTest retrieveGtOutboundTest(String testId) throws TestNotFoundException
	{
		String mn = "retrieveGtOutboundTest";
		Logger.debug(this.getClass().getSimpleName(), mn, "Retrieving Test ID:"+testId);
		GtObTest storedCopy = null;
//		GtObTest duplicate = null;
//		try
//		{
			storedCopy = _outboundCache.get(testId);
			if(storedCopy == null)
			{
				throw new TestNotFoundException(testId);
			}
//			Logger.debug(this.getClass().getSimpleName(), mn, "Test ID:"+testId+" found? "+(storedCopy!=null));
//			if(storedCopy != null)
//				duplicate = (GtObTest)storedCopy.clone();
//		}
//		catch (CloneNotSupportedException e)
//		{
//			Logger.warn(this.getClass().getSimpleName(), "retrieveGtOutboundTest", "Unable to make copy for:" +testId, e);
//		} 
		return storedCopy;
	}
	
	public void removeGtOutboundTest(String testId)
	{
		_outboundCache.remove(testId);
	}
	
	public void clearAll()
	{
		_inboundCache.clear();
		_outboundCache.clear();
	}
	
	public int getCacheSize(int cacheType)
	{
		switch (cacheType) {
		case CACHE_INBOUND:
			return _inboundCache.size();
		case CACHE_OUTBOUND:
			return _outboundCache.size();
		default:
			return -1;			
		}
	}
	
//	public void housekeep(Date threshold)
//	{
//		Enumeration<String> inboundTests = _inboundCache.keys();
//		while(inboundTests.hasMoreElements())
//		{
//			String key = inboundTests.nextElement();
//			GtIbTest ibTest = _inboundCache.get(key);
//			if(ibTest.getState().equals(TimedOutState.STATE) || 
//					ibTest.getState().equals(ReceivedAckAndBackendFromGtState.STATE) ||
//					)
//			{
//				if(ibTest.)
//			}
//		}
//	}
//	private void setTimer(String testId, int gtDirection)
//	{
//		long timeout = 0;
//		switch(gtDirection)
//		{
//			case TestTimeOutTimerTask.TEST_TYPE_INBOUND:
//				timeout = ConfigMgr.getMainConfig().getIbTimeOutInSecs();
//				break;
//			case TestTimeOutTimerTask.TEST_TYPE_OUTBOUND:
//				timeout = ConfigMgr.getMainConfig().getObTimeOutInSecs();
//				break;
//			default:
//		}
//			
//		if(timeout > 0 )
//		{
//		Logger.debug(this.getClass().getSimpleName(),
//		             "setTimer", 
//		             "Time-out set at:"+timeout+"s. Test ID:"+testId);
//		Timer timer = new Timer();
//		TestTimeOutTimerTask timeoutTask = 
//			new TestTimeOutTimerTask(testId, TestTimeOutTimerTask.TEST_TYPE_OUTBOUND);
//		timer.schedule(timeoutTask, ConfigMgr.getMainConfig().getObTimeOutInSecs()*1000);
//		test.setTimer(timer);
//		}
//		
//	}
}
