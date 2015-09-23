/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: InitServlet.java
 * 
 * ***************************************************************************
 * Date Author Changes
 * ***************************************************************************
 * Feb 22, 2007 Alain Ah Ming Created
 */
package com.gridnode.gridtalk.tester.loopback;

import com.gridnode.gridtalk.tester.loopback.cfg.ConfigMgr;
import com.gridnode.gridtalk.tester.loopback.command.CommandException;
import com.gridnode.gridtalk.tester.loopback.command.impl.SendPipCommand;
import com.gridnode.gridtalk.tester.loopback.log.Logger;
import com.gridnode.gridtalk.tester.loopback.util.Heartbeat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Initializer servlet class
 * 
 * @author Alain Ah Ming
 * @since
 * @version
 */
public class InitServlet extends HttpServlet
{
	private Timer _heartbeatTimer = null;
	private Timer _testTimer = null;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3597013137726505931L;

	/**
	 * 
	 */
	public InitServlet()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException
	{
		String mn = "init";
		long heartBeatInterval= ConfigMgr.getMainConfig().getHeartBeatInterval();
		Logger.info(this.getClass().getSimpleName(), mn,null,null, "Heartbeat interval:"+heartBeatInterval+"s");
		
		if(heartBeatInterval > 0)
		{
			startHeartbeat(heartBeatInterval* 1000);
		}
		else
		{
			Logger.warn(this.getClass().getSimpleName(), mn, "Heartbeat disabled");
		}
		
		// Start inbound test
		long period = ConfigMgr.getMainConfig().getTestInterval();
		if(period > 0)
		{
			Logger.info(this.getClass().getSimpleName(), mn, null, null, "Test Interval:"+period+"s");			
			startInboundTest(period);
		}
		else
		{
			Logger.warn(this.getClass().getSimpleName(),mn, "No test scheduled");
		}
	}
	
	@Override
	public void destroy()
	{
		shutDown();
	}
	
	private void shutDown()
	{
		String mn = "shutDown";
		Logger.info(this.getClass().getSimpleName(), mn,null, null,"GLBT shutting down");		
		stopHeartbeat();
		stopTestTimer();
	}
	
	private void startHeartbeat(long period)
	{
		_heartbeatTimer = new Timer();
		_heartbeatTimer .schedule(new Heartbeat(),0,period);
		
	}

	private void stopHeartbeat()
	{
		String mn = "stopHeartbeat";
		if(_heartbeatTimer != null)
		{
			_heartbeatTimer.cancel();
			Logger.info(this.getClass().getSimpleName(), mn,null, null,"Heartbeat stopped");		
		}
		else
		{
			Logger.info(this.getClass().getSimpleName(), mn,null, null,"No heartbeat to stop");		
		}
	}
	private void startInboundTest(long period)
	{
		String mn ="startInboundTest";
		_testTimer = new Timer();
		_testTimer.schedule(new InboundTimerTask(), 0, period*1000);
	}

	private void stopTestTimer()
	{
		String mn = "stopTestTimer";
		if(_testTimer != null)
		{
			_testTimer.cancel();
			Logger.info(this.getClass().getSimpleName(), mn,null, null,"Test timer stopped");		
		}
		else
		{
			Logger.info(this.getClass().getSimpleName(), mn,null, null,"No test timer to stop");		
		}
	}
	
	class InboundTimerTask extends TimerTask
	{

		@Override
		public void run()
		{
			String pipCode = ConfigMgr.getPartnerConfig().getPipCode();
			try
			{
				new SendPipCommand(pipCode).execute(null);
			}
			catch (CommandException e)
			{
				Logger.error(this.getClass().getSimpleName(), "run", "Error starting inbound test", e);
			}
		}
	}	
}
