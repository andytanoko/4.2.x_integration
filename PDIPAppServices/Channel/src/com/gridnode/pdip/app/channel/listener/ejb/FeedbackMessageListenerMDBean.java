/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All rights reserved.
 *
 * File: FeedbackMessageListenerMDBean.java
 *
 * **********************************************************
 * Date           Author            Changes
 * **********************************************************
 * Aug 20 2002    Jagadeesh         Created
 * Mar 29 2004    Jagadeesh         Modified: To get Old Version of FeedBack Message.
 *                                  To Check if Headers are Not Null.
 * Jul 25 2008    Tam Wei Xiang     #69: Support throw up of JMS related exception
 *                                         to indicate a rollback of current transaction
 *                                         is required.
 */

package com.gridnode.pdip.app.channel.listener.ejb;

import java.util.Map;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.pdip.app.channel.IMessageContext;
import com.gridnode.pdip.app.channel.MessageContext;
import com.gridnode.pdip.app.channel.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.channel.handler.MessageHandlerRegistry;
import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.helpers.ChannelSendHeader;
import com.gridnode.pdip.app.channel.helpers.IReceiveFeedbackHandler;
import com.gridnode.pdip.base.transport.helpers.Feedback;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.messaging.IAS2Headers;
import com.gridnode.pdip.framework.messaging.IRNHeaderConstants;

public class FeedbackMessageListenerMDBean
  implements MessageDrivenBean, MessageListener
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7426582287197719896L;

	private static final String CLASS_NAME = "FeedbackMessageListenerMDBean";
  
  private MessageDrivenContext _ctx;
  private MessageHandlerRegistry _registry;

  public void setMessageDrivenContext(MessageDrivenContext ctx)
  {
    _ctx = ctx;
  }

  public void ejbCreate()
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "ejbCreate()",
        "[In create -" + "Creating Registry for MDB Bean]");
      _registry = MessageHandlerRegistry.getHandlerRegistry();

      ChannelLogger.infoLog(
        CLASS_NAME,
        "ejbCreate()",
        "[Created Registry" + "For MDB Bean]");
    }
    catch (Exception ex)
    {
      ChannelLogger.errorLog(ILogErrorCodes.CHANNEL_CREATE,
        CLASS_NAME,
        "ejbCreate",
        "Could Not Create Registry. Error: "+ex.getMessage(),
        ex);
    }
  }

  /**
   * This method accepts incomming message, and invokes feedback listener
   * registered with a given eventid, obtained from header.
   *
   * The Message Object can be either of the instance type (i.e) MessageContext or
   * FeedBack.
   *
   * @param message - JMS Message Object.
   * @throws java.lang.Exception - throws any Checked Exception.
   */
  public void processMessageReceived(javax.jms.Message message)
    throws Throwable
  {
    Object messageObj = ((ObjectMessage) message).getObject();
    Feedback feedBackMsg = getFeedBackMessage(messageObj);
    if (feedBackMsg == null)
      ChannelLogger.infoLog(
        "CLASS_NAME",
        "processMessageReceived()",
        "[FeedBack Message or Header is Null or Not Expected Msg type - Feedback Not Sent]");
    else
      invokeFeedBackListener(feedBackMsg);
  }

  private void invokeFeedBackListener(Feedback feedBackMsg) throws Throwable
  {
    String[] header = feedBackMsg.getHeader();
    ChannelLogger.infoLog(
      CLASS_NAME,
      "invokeFeedBackListener()",
      "[Callback FeedbackMessage Listener with Id=][" + header[0] + "]");
    IReceiveFeedbackHandler listener =
      _registry.getFeedBackMessageHandler(header[0]);
    if (listener != null)
      listener.handlerFeedback(
        header,
        feedBackMsg.getStatus(),
        feedBackMsg.getDescription());
    else
      ChannelLogger.infoLog(
        CLASS_NAME,
        "invokeFeedBackListener()",
        "[No Listener Configured For this ID][" + header[0] + "]");
  }

  private Feedback getFeedBackMessage(Object messageObj) throws Exception
  {
    Feedback feedBackMessage = null;
    if (messageObj instanceof MessageContext)
    {
      MessageContext messageContext = (MessageContext) messageObj;
      feedBackMessage =
        (Feedback) messageContext.getAttribute(
          IMessageContext.FEEDBACK_MESSAGE);
      if (feedBackMessage != null)
      {
        com.gridnode.pdip.framework.messaging.Message message =
          feedBackMessage.getMessage();
        if (message != null) //If Message Object is null return null message.
        {
          Map header = message.getCommonHeaders();
          if (header != null)
          {
            Map messageHeaders = message.getMessageHeaders();

            if ((messageHeaders != null) && (messageHeaders.get(IAS2Headers.AS2_FROM) != null))
            {
              //AS2 message
              feedBackMessage.setHeader(
                ChannelSendHeader.getAllHeaders(header, messageHeaders));
            }
            else if ((messageHeaders != null) && (messageHeaders.get(IRNHeaderConstants.AUDIT_FILE_NAME) != null))
            {
              //for RNIF message
              //Note: only Common and AUDIT_FILE_NAME headers are set 
              feedBackMessage.setHeader(
                ChannelSendHeader.getCommonAndRNHeaders(header, messageHeaders)); 
            }
            else
              feedBackMessage.setHeader(
                ChannelSendHeader.getCommonHeaders(message.getCommonHeaders()));
          }
          else
          {
            feedBackMessage = null; //Return Null if Header is Null.
          }
        }
        else
        {
          feedBackMessage = null;
        }
      }
    }
    else if (messageObj instanceof Feedback)
    {
      //Get Feedback Message
      feedBackMessage = (Feedback) messageObj;
    }
    return feedBackMessage; //Return Null or Feedback message
  }

  public void onMessage(javax.jms.Message message)
  {
	String jmsMessageID = "";
    try
    {
        if(message.getJMSRedelivered())
        {
        	ChannelLogger.infoLog(CLASS_NAME,"onMessage", this.getClass().getName()+" Redelivered msg found. \n "+message);  
        }
    	
      ChannelLogger.infoLog(CLASS_NAME, "onMessage", "In onMessage Begin");
      jmsMessageID = message.getJMSMessageID();
      
      processMessageReceived(message);      
      ChannelLogger.infoLog(CLASS_NAME, "onMessage", "In onMessage End");
    }
    catch (Throwable ex)
    {
      if(JMSRedeliveredHandler.isRedeliverableException(ex)) //#69 25072008 TWX
      {
    	  ChannelLogger.warnLog("GWFRestrictionMDBean", "onMessage", "encounter JMSException, rolling back all transaction. JMSMessageID:"+jmsMessageID);
    		_ctx.setRollbackOnly();
      }
    	
      ChannelLogger.errorLog(ILogErrorCodes.CHANNEL_FEEDBACK_MESSAGE_LISTENER,
        CLASS_NAME,
        "onMessage",
        "Could not perform onMessage. Error: "+ex.getMessage(),
        ex);
      ex.printStackTrace();
    }

  }
  /*
  private MessageContext getMessageContext(Message message)
    throws JMSException, ApplicationException
  {
    if (message instanceof ObjectMessage)
    {
      ObjectMessage objectMessage = (ObjectMessage) message;
      Object messageContext = objectMessage.getObject();
      if (messageContext != null) //&&
        // messageContext.getClass().isAssignableFrom(IMessageContext.class))
      {
        return (MessageContext) messageContext;
      }
      else
        throw new ApplicationException(
          "["
            + CLASS_NAME
            + "][getMessageContext()]"
            + "[Message Object is Null or Not a Expected Message Type]");
    }
    else
      throw new ApplicationException(
        "["
          + CLASS_NAME
          + "][getMessageContext()]"
          + "[Unrecognized message type.]"
          + message.getClass().getName());
  }*/

  public void ejbRemove()
  {
    ChannelLogger.infoLog(CLASS_NAME, "ejbRemove()", "[In ejbRemove]");
    if (_registry != null)
    {
      _registry = null;
    }
    _ctx = null;

  }
  /*
  private MessageContext getMessageContext(javax.jms.Message message)
    throws JMSException, ApplicationException
  {
    if (message instanceof ObjectMessage)
    {
      ObjectMessage objectMessage = (ObjectMessage) message;
      Object messageContext = objectMessage.getObject();
      if (messageContext != null
        && messageContext.getClass().isAssignableFrom(IMessageContext.class))
      {
        return (MessageContext) messageContext;
      }
      else
        throw new ApplicationException(
          "["
            + CLASS_NAME
            + "][getMessageContext()]"
            + "[Message Object is Null or Not a Expected Message Type]");
    }
    else
      throw new ApplicationException(
        "["
          + CLASS_NAME
          + "][getMessageContext()]"
          + "[Unrecognized message type.]"
          + message.getClass().getName());
  }*/

}