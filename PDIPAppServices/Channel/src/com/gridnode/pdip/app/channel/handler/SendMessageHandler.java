/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendMessageHandler.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 21 2002    Goh Kan Mun             Created
 * Jul 03 2002    Goh Kan Mun             Modified - Change in the CommInfo and the ChannelInfo
 * Aug 19 2002    Jagadeesh               Modified - Change processSendMessage to Send the Message
 *                                        to Appservice - Channel Topic. To process the outgoing
 *                                        message.
 * Jan 17 2003    Goh Kan Mun             Modified - JMSChannelInfo change to DispatchMessage.
 * May 31 2004    Neo Sok Lay             GNDB00024921 - getSendMessageHandler() not properly
 *                                        synchronized. _self.initJMSProtocol() may be still
 *                                        executing in one thread while the other thread already
 *                                        got hold of _self.
 * Jan 29 2007    Neo Sok Lay             Use generic Destination instead of Topic       
 * Nov 22 2007    Tam Wei Xiang           Change to use JMSRetrySender which provide retry in case
 *                                        the jms sending failed.                                
 */


package com.gridnode.pdip.app.channel.handler;

import com.gridnode.pdip.app.channel.handler.jmschannel.DispatchMessage;
import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.helpers.IChannelConfig;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.jms.JMSRetrySender;
import com.gridnode.pdip.framework.jms.JMSSender;
import com.gridnode.pdip.framework.util.JNDIFinder;
import com.gridnode.pdip.framework.util.ServiceLocator;

import javax.jms.*;

import java.io.File;
import java.util.Hashtable;


public class SendMessageHandler
{

  private Connection _channelConnection;
  private Session _channelSession;
  private MessageProducer _channelProducer;
  private Destination _channelDest;
  //private static SendMessageHandler _self = null;
  private Hashtable<String, String> _jmsSendProps = new Hashtable<String,String>();
  
  private SendMessageHandler() throws Exception
  {
    initJMSProtocol();
  }
  
  public static SendMessageHandler getSendMessageHandler() throws Exception
  {
    /*
     if(_self == null)
          {
             _self = new SendMessageHandler();
     }
     
     return _self;
     */
    return new SendMessageHandler();
  }

  public void processSendMessage(
    ChannelInfo info,
    String[] dataToSend,
    File[] file,
    String[] header)
    throws Throwable
  {
    
    /**@todo invoke file splitting here*/
    try
    {
    ChannelLogger.infoLog(
      "SendMessageHandler",
      "processSendMessage()",
    "In process Send ");

    //String[] encryptedData = dataToSend;
    //byte[] encryptedFilePart = null;
    DispatchMessage wrapperInfo = new DispatchMessage();

    ChannelLogger.infoLog("SendMessageHandler","processSendMessage() ","Channel Info -->" + info);
    ChannelLogger.infoLog("SendMessageHandler","processSendMessage() ","Data Info -->" + dataToSend);
    ChannelLogger.infoLog("SendMessageHandler","processSendMessage() ","Event ID -->" + header[0]);
    
    wrapperInfo.setChannleInfo(info);
    wrapperInfo.setData(dataToSend);
    wrapperInfo.setFile(file);
    wrapperInfo.setHeader(header);
    ChannelLogger.infoLog("SendMessageHandler","processSendMessage() ","Message is -->" + wrapperInfo.toString());
    
      if(! JMSRetrySender.isSendViaDefMode())
      {
        ChannelLogger.infoLog("SendMessageHandler","processSendMessage() ","B4 Sending Object Message via URL:" + info.getTptCommInfo().getURL());
        ChannelLogger.debugLog("SendMessageHandler","processSendMessage() ", "Send via JMSRetrySender.");
        String destName = _jmsSendProps.get(JMSSender.JMS_DEST_NAME);
        JMSRetrySender.sendMessage(destName, wrapperInfo, null, _jmsSendProps);
        ChannelLogger.infoLog("SendMessageHandler","processSendMessage() ","After Sending Object Message via JMSRetrySender...");
      }
      else
      {
        ChannelLogger.infoLog("SendMessageHandler","processSendMessage() ","B4 Creating Object Message");
        ObjectMessage message =
        _channelSession.createObjectMessage(wrapperInfo);
      
        ChannelLogger.infoLog("SendMessageHandler","processSendMessage() ","Message is -->" + message.toString());
        ChannelLogger.infoLog("SendMessageHandler","processSendMessage() ","B4 Sending Object Message via URL:" + info.getTptCommInfo().getURL());
      
        _channelProducer.send(message);
        ChannelLogger.infoLog("SendMessageHandler","processSendMessage() ","After Sending Object Message via JMS...");
      }
    }
    finally
    {
      closeConnection();
    }
  }

  private void closeConnection()
  {
    if (_channelConnection != null)
    {
      try
      {
        _channelConnection.close();
      }
      catch (Exception ex)
      {
        
      }
    
    }
  }
  
  private void initJMSProtocol() throws Exception
  {
    ConfigurationManager mgr = ConfigurationManager.getInstance();
    Configuration configChannel = mgr.getConfig(IChannelConfig.CONFIG_NAME);
    
    //TWX init the jms send props
    _jmsSendProps.put(JMSSender.JNDI_SERVICE, ServiceLocator.CLIENT_CONTEXT);
    _jmsSendProps.put(JMSSender.CONN_FACTORY, configChannel.getString(IChannelConfig.APPSERVER_CONNECTION_FACTORY));
    _jmsSendProps.put(JMSSender.JMS_DEST_NAME,configChannel.getString(IChannelConfig.CHANNEL_DESTINATION_APP_TO_SEND));
    
    ChannelLogger.debugLog("SendMessageListener", "initJMSProtocol", "jms send Props: "+_jmsSendProps);
    
    try
    {
      JNDIFinder jndi = ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getJndiFinder();
      String cfName =
        configChannel.getString(
      IChannelConfig.APPSERVER_CONNECTION_FACTORY);
      ConnectionFactory cf =
        (ConnectionFactory) jndi.lookup(
          cfName,
          ConnectionFactory.class);
      ChannelLogger.debugLog(
        "SendMessageHandler",
        "initJMSProtocol",
      "After ConnectionFactory lookup");

      String destName =
        configChannel.getString(IChannelConfig.CHANNEL_DESTINATION_APP_TO_SEND);
      _channelDest = (Destination) jndi.lookup(destName, Destination.class);
      ChannelLogger.debugLog(
        "SendMessageHandler",
        "initJMSProtocol",
      "After Destination lookup");
      _channelConnection = cf.createConnection();

      _channelSession =
        _channelConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      ChannelLogger.debugLog(
        "SendMessageHandler",
        "initJMSProtocol",
      "After Session Creation");
      _channelProducer = _channelSession.createProducer(_channelDest);
    }
    catch (Exception e)
    {
      ChannelLogger.warnLog(
        "SendMessageHandler",
        "initJMSProtocol",
      e.getMessage());
      throw new Exception(
        "SendMessageHandler" + "initJMSProtocol" + e.getMessage());
    }
  }

  /*
  public void sendFeedBackToApp(FeedbackMsg msg)
  {
    Configuration configChannel = ConfigurationManager.getInstance(
    ).getConfig(ITransportConfig.CONFIG_NAME);
    try
    {
      JNDIFinder jndi = new JNDIFinder();
      String topicName = configChannel.getString(
      ITransportConfig.APPSERVER_TOPIC_TRANSPORT_FEEDBACK);
      Topic feedbackTopic = (Topic)jndi.lookup(topicName,Topic.class);
      TopicSession fsession = _channelConnection.createTopicSession(
      false,Session.AUTO_ACKNOWLEDGE);
      TopicPublisher feedBackPublisher = fsession.createPublisher(feedbackTopic);
      ObjectMessage message = fsession.createObjectMessage(msg);
      feedBackPublisher.publish(message);
      ChannelLogger.infoLog("SendMessageHandler","sendFeedBackToApp()",
      "Feedback sent success");
      feedBackPublisher.close();
      fsession.close();
    }
    catch(Exception ex)
    {
      ChannelLogger.errorLog("SendMessageHandler","sendFeedBackToApp()",
      "Cannot Send FeedBack ",ex);
    }

  }
  */
}
