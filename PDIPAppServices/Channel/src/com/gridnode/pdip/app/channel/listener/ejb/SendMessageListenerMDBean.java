/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All rights reserved.
 *
 * File: JMSSendMessageMDBean.java
 *
 * **********************************************************
 * Date           Author            Changes
 * **********************************************************
 * Aug 20 2002    Jagadeesh         Created
 * Dec 06 2002    Jagadeesh         Modified: Channel to Send Feedback.
 * Jan 17 2003    Goh Kan Mun       Modified - Set the split values to the packaging info.
 *                                           - Add in data content when invoking the
 *                                             packaging process.
 * Jan 30 2003    Kan Mun           Modified - Change in DispatchMessage.
 *                                           - Refactored.
 *                                           - Added methods to handle split and split ack.
 * Mar 21 2003    Kan Mun           Modified - Make the file splitting process works.
 *                                           - Change the log to debug logging.
 * Apr 03 2003    Kan Mun           Modified - To make the sending of 10 packets work.
 *
 * Jul 22 2003    Jagadeesh         Modified - To use new Framework to perform Send.
 * Dec 05, 2007   Tam Wei Xiang     To add in the checking of the redelivered jms msg.
 * Jul 22, 2008   Tam Wei Xiang     #69: Explicitly check for redelivered jms msg. Drop it
 *                                       if it has been found.
 */

package com.gridnode.pdip.app.channel.listener.ejb;

import java.util.Map;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.gridnode.pdip.app.channel.IMessageContext;
import com.gridnode.pdip.app.channel.IMessageSender;
import com.gridnode.pdip.app.channel.MessageContext;
import com.gridnode.pdip.app.channel.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.helpers.MessageDispatcher;
import com.gridnode.pdip.app.channel.helpers.MessageHandlerFactory;
import com.gridnode.pdip.base.transport.helpers.Feedback;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.messaging.Message;

public class SendMessageListenerMDBean
  implements MessageDrivenBean, MessageListener
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6106604204596011875L;
	private MessageDrivenContext _ctx;
  //private String randomString;
  private static final String CLASS_NAME = "SendMessageListenerMDBean";

  /**
   * Standard EJB-MDB Method called by the container, and set's the
   * MessageDrivenContext to this instance.
   *
   * @param ctx - MessageDrivenContex - environment variable used as a
   * gateway to the Container environment.
   */
  public void setMessageDrivenContext(MessageDrivenContext ctx)
  {
    ChannelLogger.infoLog(
      CLASS_NAME,
      "setMessageDrivenContext",
      "Context Initilized ");
    _ctx = ctx;
  }

  /**
   * Standard EJB-MDB Method to create instance of MessageDrivenBean. And sets
   * this instance in the instance pool.
   */
  public void ejbCreate()
  {
    ChannelLogger.infoLog(CLASS_NAME, "ejbCreate", "Created");
  }

  public void ejbRemove()
  {
    try
    {
      ChannelLogger.infoLog(CLASS_NAME, "ejbRemove()", "[In ejbRemove]");
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "ejbRemove",
        "[Could Not Remove TPT Facade]",
        ex);
    }
  }

  public void onMessage(javax.jms.Message message)
  {
    String[] header = null;
    //com.gridnode.pdip.framework.messaging.Message messageToSend = null;
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "onMessage()", "[In onMessage Begin]");
      if(message.getJMSRedelivered()) //TWX 22072008 We will explicitly check for the redelivered message since we can't determine
      {                               //             is previous send to TP is successful or fail.
        ChannelLogger.infoLog(CLASS_NAME, "onMessage()", "Redelivered msg found, ignored it. Message: "+message);
        return;
      }
      
      Object messageObj = ((ObjectMessage) message).getObject();
      MessageContext messageContext = (MessageContext) messageObj;
      logMessageContext(messageContext);
      //messageToSend =
      //  (Message) messageContext.getAttribute(IMessageContext.OUTBOUND_MESSAGE);
      IMessageSender sendMessageHandler =
        MessageHandlerFactory.getMessageSender(messageContext);
      sendMessageHandler.send();
      ChannelLogger.debugLog(CLASS_NAME, "onMessage()", "[In onMessage End.]");
    } //End of try
    catch (Throwable t)
    {
      ChannelLogger.errorLog(ILogErrorCodes.CHANNEL_SEND_MESSAGE_LISTENER,
        CLASS_NAME,
        "onMessage",
        "Could not perform onMessage. Error: "+t.getMessage(),
        t);
      sendFeedback(false, t.getLocalizedMessage(), header);
    }
  }

  /*
  private MessageContext getMessageContext(javax.jms.Message message)
    throws JMSException, ApplicationException
  {
    if (message instanceof ObjectMessage)
    {
      ObjectMessage objectMessage = (ObjectMessage) message;
      Object messageContext = objectMessage.getObject();
      if (messageContext != null) //&&
        //messageContext.getClass().isAssignableFrom(IMessageContext.class))
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

  private void logMessageContext(MessageContext context)
  {
    Message outBoundMessage =
      (Message) context.getAttribute(IMessageContext.OUTBOUND_MESSAGE);
    String[] messageMetaData = outBoundMessage.getData();
    Map commonHeaders = outBoundMessage.getCommonHeaders();
    //System.out.println("CommonHeaders="+outBoundMessage.getCommonHeaders());
    logMessage("MetaData", messageMetaData);
    if (outBoundMessage.getPayLoad() != null)
    {
      logMessage("PayLoadLength", outBoundMessage.getPayLoad().length);
    }
    else
      ChannelLogger.debugLog(
        CLASS_NAME,
        "logMessageContext()",
        "[File PayLoad is Null]");
    if (commonHeaders != null)
    {
      String[] keys = (String[]) commonHeaders.keySet().toArray(new String[] {
      });
      Object[] values = (Object[]) commonHeaders.values().toArray();
      logMessage("CommonHeader Keys", keys);
      for (int i = 0; i < values.length; i++)
        ChannelLogger.debugLog(
          CLASS_NAME,
          "logMessageContext()",
          "[HeaderValue=" + values[i] + "]");
    }
    else
      ChannelLogger.debugLog(
        CLASS_NAME,
        "logMessageContext()",
        "[Common Headers Are Not Present - Message may not be delivered properly]");
  }

  private void logMessage(String header, String[] data)
  {
    if (data != null)
    {
      logMessage(header, data.length);
      for (int i = 0; i < data.length; i++)
      {
        ChannelLogger.debugLog(
          CLASS_NAME,
          "logMessage()",
          "[" + header + "][" + i + "]=[" + data[i] + "]");
      }
    }
    else
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "logMessage()",
        "[" + header + "is Null]");
    }
  }

  private void logMessage(String header, int length)
  {
    ChannelLogger.debugLog(
      CLASS_NAME,
      "logMessage()",
      "[" + header + "][Length=[" + length + "]]");
  }

  private void sendFeedback(boolean status, String desc, String[] header)
  {
    try
    {
      Feedback feedBackMsg = new Feedback(status, desc, header);
      MessageContext messageContext = new MessageContext();
      messageContext.setAttribute(
        IMessageContext.FEEDBACK_MESSAGE,
        feedBackMsg);
      MessageDispatcher.dispatchMessageToFeedbackListener(messageContext);
    }
    catch (Exception ex)
    {
      ChannelLogger.errorLog(ILogErrorCodes.CHANNEL_SEND_MESSAGE_LISTENER,
        CLASS_NAME,
        "sendFeedback",
        "Cannot Send FeedBack Message. Error: "+ex.getMessage(),
        ex);
    }
  }

}
/*import com.gridnode.pdip.base.transport.comminfo.SoapCommInfo; */
/**
 * This method has been refactored in the "tobe" released version. A centralized
 * CommInfoFactory at BaseServices is added to create CommInfo, which reads from
 * properties file to get the actual InfoObject to be initilized.
 *
 */
/*       else if(protocolType.equals(CommInfo.SOAP))
       {
          ChannelLogger.debugLog(CLASS_NAME, "getCommInfo",
                 "SoapCommInfo Created");
          SoapCommInfo soapCommInfo = new SoapCommInfo(commInfo.getURL());
          soapCommInfo.setTptImplVersion(commInfo.getTptImplVersion());
          return soapCommInfo;
       }
*/
