/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MainConfig.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 16, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.cfg;

import com.gridnode.gridtalk.tester.loopback.log.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MainConfig
{
	// PROPERTIES FILE KEYS
	private static final String KEY_HEARTBEAT_INTERVAL = "heartbeat.interval";
	private static final String KEY_OB_TIMEOUT = "outbound.test.timeout";
	private static final String KEY_IB_TIMEOUT = "inbound.test.timeout";
	private static final String KEY_TEST_INTERVAL = "test.interval";
	
	// DEFAULT VALUES
	private static final long HEARTBEAT_INTERVAL_NONE = 0;

	private static final long TEST_INTERVAL_NONE = 0;

	private long _heartBeatInterval = HEARTBEAT_INTERVAL_NONE;
	private long _obTimeOutInSecs = -1;
	private long _ibTimeOutInSecs = -1;
	private long _testInterval = TEST_INTERVAL_NONE;
	
	public MainConfig(File f)
	{
		load(f);
	}

	private void load(File f)
	{
		Properties p = new Properties();
		try
		{
			p.load(new FileInputStream(f));
			_heartBeatInterval = Long.parseLong(p.getProperty(KEY_HEARTBEAT_INTERVAL));
			_obTimeOutInSecs = Long.parseLong(p.getProperty(KEY_OB_TIMEOUT));
			_ibTimeOutInSecs = Long.parseLong(p.getProperty(KEY_IB_TIMEOUT));
			_testInterval = Long.parseLong(p.getProperty(KEY_TEST_INTERVAL));
			
		}
		catch (FileNotFoundException e)
		{
			Logger.error(this.getClass().getSimpleName(), "load", "File not found: "+f.getAbsolutePath(), e);
		}
		catch (IOException e)
		{
			Logger.error(this.getClass().getSimpleName(), "load", "Error reading file: "+f.getAbsolutePath(), e);
		}
		
	}
	
	public long getHeartBeatInterval()
	{
		return _heartBeatInterval; 
	}
	/**
	 * @return Returns the ibTimeOutInSecs.
	 */
	public long getIbTimeOutInSecs()
	{
		return _ibTimeOutInSecs;
	}

	/**
	 * @return Returns the obTimeOutInSecs.
	 */
	public long getObTimeOutInSecs()
	{
		return _obTimeOutInSecs;
	}

	/**
	 * @return Returns the testInterval.
	 */
	public long getTestInterval()
	{
		return _testInterval;
	}
}
