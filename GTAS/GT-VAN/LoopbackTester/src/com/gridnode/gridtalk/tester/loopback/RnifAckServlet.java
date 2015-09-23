/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RnifAckServlet.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 23, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback;

import com.gridnode.gridtalk.tester.loopback.command.CommandException;
import com.gridnode.gridtalk.tester.loopback.command.impl.ReceivePipCommand;
import com.gridnode.gridtalk.tester.loopback.command.impl.SendPipCommand;
import com.gridnode.gridtalk.tester.loopback.testbed.intf.IllegalGtTestStateException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @deprecated
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public class RnifAckServlet extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7591392970829424410L;

	/**
	 * 
	 */
	public RnifAckServlet()
	{
		super();
		// TODO Auto-generated constructor stub
	}
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
//  	// Send RNIF Message
//    int code = HttpServletResponse.SC_OK;
//    String pip = request.getParameter("pip");
//    Logger.debug(this.getClass().getSimpleName(),"doGet","pip:"+pip);
//    if(pip != null && !pip.trim().equals(""))
//    {
//    	// Send RNIF Message
//    	try
//			{
//				sendRnifMessage(pip, getContent(request.getInputStream()));
//			}
//			catch (Exception e)
//			{
//				Logger.error(this.getClass().getSimpleName(), "doGet", "Error", e);
//			}
//    }
//    else
//    {
//    	Logger.debug(this.getClass().getSimpleName(), "doGet","No PIP specified. Message ignored");
//    }
//    
//    response.setStatus(code);
  }

  private void sendRnifMessage(String pipCode, String content) throws CommandException
  {
  	new SendPipCommand(pipCode).execute(content);
  }
  
  private void receiveRnifMessage(String pipCode, String content) throws CommandException
  {
  	new ReceivePipCommand(pipCode).execute(content);
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
//
//  private void receiveRnifAck(String content) throws CommandException
//  {
//  	new ReceiveAckCommand(new RnifMessageDao(),content).execute(null);
//  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
//  	String mn = "doPost";
//    ServletInputStream is = request.getInputStream();
//    String content = getContent(is);
//    response.setStatus(HttpServletResponse.SC_ACCEPTED);
//    try
//		{
//			handleMsg(content);
//		}
//		catch (CommandException e)
//		{
//			Logger.error(this.getClass().getSimpleName(), mn, "Error",e);
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//		}
  }
  
//  private void handleMsg(String content) throws CommandException
//  {
//    PipMessageHelper defaultMsgHelper = new PipMessageHelper("DEF", new RnifMessageDao());
//    
//    RnifMessageDao dao = new RnifMessageDao();
//    RnifMessageEntity m = dao.getRnifMessageTemplate("DEF");
//    String pipCode = defaultMsgHelper.findValue(content, m.getPath(RnifMessageEntity.PATH_PIP_CODE));
//    Logger.debug(this.getClass().getSimpleName(),"handleMsg","pipCode:"+pipCode);
//
//  	
//    //check if ack or action msg
//    //System.out.println("Content is "+content);  	
//    int svcContentIdx = content.indexOf("RN-Service-Content");
//    if (svcContentIdx < 0)
//    {
//      Logger.debug(this.getClass().getSimpleName(), "handleMsg","Received message is not RosettaNet, ignore.");
//      return;
//    }
//    
//    int ackDocTypeIdx = content.indexOf("<!DOCTYPE ReceiptAcknowledgment", svcContentIdx);
//    if (ackDocTypeIdx < 0)
//    {
//      receiveRnifMessage(pipCode, content);
//    }
//    else
//    {
//    	receiveRnifAck(content);
//    }
//  }
  
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
