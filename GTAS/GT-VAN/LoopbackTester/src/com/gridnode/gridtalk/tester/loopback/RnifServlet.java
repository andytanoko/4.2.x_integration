/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RnifServlet.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 16 2007    Alain Ah Ming				Created
 * 																		Modified from com.gridnode.testkit.inovis.http.RnifServlet
 */

package com.gridnode.gridtalk.tester.loopback;

import com.gridnode.gridtalk.tester.loopback.command.CommandException;
import com.gridnode.gridtalk.tester.loopback.command.impl.ReceiveAckCommand;
import com.gridnode.gridtalk.tester.loopback.command.impl.ReceivePipCommand;
import com.gridnode.gridtalk.tester.loopback.command.impl.SendPipCommand;
import com.gridnode.gridtalk.tester.loopback.dao.RnifMessageDao;
import com.gridnode.gridtalk.tester.loopback.log.Logger;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * A servlet class to handle RNIF messages between this 
 * loopback tester and GridTalk.
 * @author Alain
 *
 */
public class RnifServlet extends HttpServlet
{
//  private static final Hashtable<String, RnifTestCaseRunner> _tests = new Hashtable<String, RnifTestCaseRunner>();
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 8552637901473302658L;


	/**
   * 
   */
  public RnifServlet()
  {
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
  	// Send RNIF Message
    int code = HttpServletResponse.SC_OK;
    String pip = request.getParameter("pip");
    Logger.debug(this.getClass().getSimpleName(),"doGet","pip:"+pip);
    if(pip != null && !pip.trim().equals(""))
    {
    	// Send RNIF Message
    	try
			{
				sendRnifMessage(pip);
			}
			catch (Exception e)
			{
				Logger.error(this.getClass().getSimpleName(), "doGet", "Error", e);
			}
    }
    else
    {
    	Logger.debug(this.getClass().getSimpleName(), "doGet","No PIP specified. Message ignored");
    }
    
    response.setStatus(code);
  }

  private void sendRnifMessage(String pipCode) throws CommandException
  {
  	new SendPipCommand(pipCode).execute(null);
  }
  
  private void receiveRnifMessage(String content) throws CommandException
  {
  	new ReceivePipCommand(content).execute(null);
  }
  public static void main(String[] args) throws IllegalGtTestStateException, InterruptedException
  {
//  	sendRnifMessage();
//  	receiveRnifMessage("<1><2></2><3>test</3></1>");
  	
  }

//  private static void sendRnifMessage() throws IllegalGtTestStateException
//  {
//  	Logger.debug("RnifServlet", "sendRnifMessage", "Start");
//  	String testId = TestIdGenerator.generateTestId();
//    GtIbTest inboundTest = new GtIbTest(testId, ConfigMgr.getRnifHubConfig().getURL());
//   	String rnifMessage = new String(RniftUtil.generateActionMsg(testId));
//		TestCache.getInstance().storeGtInboundTest(inboundTest);
//		inboundTest.sendRnifToGt(rnifMessage);
//  }

  private void receiveRnifAck(String content) throws CommandException
  {
  	new ReceiveAckCommand(new RnifMessageDao(), content).execute(null);
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
  	String mn = "doPost";
  	Logger.debug(this.getClass().getSimpleName(), "doPost", "Start");
    ServletInputStream is = request.getInputStream();
    String content = getContent(is);
    response.setStatus(HttpServletResponse.SC_OK);
    try
		{
			handleMsg(content);
		}
		catch (CommandException e)
		{
			Logger.warn(this.getClass().getSimpleName(), mn, e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
  	Logger.debug(this.getClass().getSimpleName(), "doPost", "End");
  }
  
  private void handleMsg(final String content) throws CommandException
  {
  	
    //check if ack or action msg
    //System.out.println("Content is "+content);  	
    int svcContentIdx = content.indexOf("RN-Service-Content");
    if (svcContentIdx < 0)
    {
      Logger.debug(this.getClass().getSimpleName(), "handleMsg","Received message is not RosettaNet, ignore.");
      return;
    }
    
    int ackDocTypeIdx = content.indexOf("<!DOCTYPE ReceiptAcknowledgment", svcContentIdx);
    if (ackDocTypeIdx < 0)
    {
    	Thread t = new Thread()
    	{
    		public void run()
    		{
    			try
					{
						receiveRnifMessage(content);
					}
					catch (CommandException e)
					{
						Logger.warn(this.getClass().getSimpleName(), "run", "Error receiving message", e);
					}		
    		}
    	};
      t.start();
    }
    else
    {
    	receiveRnifAck(content);
    }
  }
  
  private String getContent(ServletInputStream is) throws IOException
  {
//    System.out.println("RnifVersion: "+rnifVersion + ", ResponseType is "+synchronous);
    byte[] buff = new byte[1024];
    int len = -1;
    String content = "";
    while ((len = is.read(buff))>0)
    {
      content += new String(buff, 0, len);
    }
//    System.out.print("Content: "+content);
    return content;
  }
  
  
//  private String retrievePipInstIdFromReceivedRnAck(String rnAck)
//  {
//  	return RniftUtil.findValue(rnAck, ConfigMgr.getMainConfig().getGtInboundRnifAckPipIdPath());
//  }
//  
}
