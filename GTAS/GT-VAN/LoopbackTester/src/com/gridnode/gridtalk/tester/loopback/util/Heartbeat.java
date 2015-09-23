/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Heartbeat.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 22, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.util;

import com.gridnode.gridtalk.tester.loopback.log.Logger;

import java.util.TimerTask;

/**
 * Timer class to simulate a heartbeat
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class Heartbeat extends TimerTask
{
	private static long beat = 0;
	/**
	 * 
	 */
	public Heartbeat()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run()
	{
		beat();
	}

	private static synchronized void beat()
	{
		if(++beat > 0)
		{
			if(beat == 1)
			{
				Logger.logHeartbeat("Heartbeat", "beat", "Start");
			}
			Logger.logHeartbeat("Heartbeat", "beat", String.valueOf(beat));			
		}
	}
	
}
