/**
* This software is the proprietary information of GridNode Pte Ltd.
* Use is subjected to license terms.
*
* Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
*
* File: MessageDispatcher.java
*
****************************************************************************
* Date           Author                  Changes
****************************************************************************
* Nov 07 2003    Jagadeesh             Created.
* Jan 29 2007    Neo Sok Lay             Use generic Destination instead of Topic/Queue
*/

package com.gridnode.pdip.app.channel.helpers;

import java.util.Hashtable;

import com.gridnode.pdip.app.channel.MessageContext;
import com.gridnode.pdip.app.channel.exceptions.ChannelException;
import com.gridnode.pdip.app.channel.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.channel.exceptions.TransportException;
import com.gridnode.pdip.base.transport.helpers.ITransportConfig;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.jms.JMSRetrySender;
import com.gridnode.pdip.framework.jms.JMSSender;
import com.gridnode.pdip.framework.util.JNDIFinder;
import com.gridnode.pdip.framework.util.ServiceLocator;

import javax.jms.*;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

public class MessageDispatcher
{

  private static final String CLASS_NAME = "MessageDispatcher";

  private static ConnectionFactory _connectionFactory;
  private static Destination _channelSendDest;
  private static Destination _channelFeedbackDest;
  
  private static Hashtable<String, String> _jmsSendProps = new Hashtable<String, String>();
  private static Hashtable<String, String> _jmsFeedbackProps = new Hashtable<String,String>();
  private static String _channelSendDestName;
  private static String _channelFeedbackName;
  
  static {
    try
    {
      initDispatcher();
    }
    catch (NameNotFoundException ex)
    {
      logError(ILogErrorCodes.CHANNEL_MESSAGE_DISPATCHER_INIT,
        "staticInitilizer",
        "JMS Error: "+ex.getMessage(),
        ex);
    }
    catch (NamingException ex)
    {
      logError(ILogErrorCodes.CHANNEL_MESSAGE_DISPATCHER_INIT,
        "staticInitilizer",
        "JMS Naming Error: "+ex.getMessage(),
        ex);
    }
    catch (ServiceLookupException ex)
    {
      logError(ILogErrorCodes.CHANNEL_MESSAGE_DISPATCHER_INIT,
        "staticInitilizer",
        "Service Lookup Error: "+ex.getMessage(),
        ex);
    }
    catch (ApplicationException ex)
    {
      logError(ILogErrorCodes.CHANNEL_MESSAGE_DISPATCHER_INIT,
        "staticInitilizer",
        "Application Error: "+ex.getMessage(),
        ex);

    }
  }

  public static void dispatchMessageToSendListener(MessageContext messageContext)
    throws TransportException, ChannelException
  {
    
    if(! JMSRetrySender.isSendViaDefMode())
    {
      try
      {
        JMSRetrySender.sendMessage(_channelSendDestName,messageContext, null, _jmsSendProps);
      }
      catch(Throwable th)
      {
        throw new TransportException("Unable to deliver Message to FeedbackMessageListener",th);
      }
      return;
    }
    
    Connection tConnection = null;
    Session tSession = null;
    MessageProducer tProducer = null;
    try
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "dispatchMessageToSendListener()",
        "[Deliviring the MessageContext -- ]" + messageContext);
      if (_connectionFactory != null && messageContext != null)
      {
        tConnection = _connectionFactory.createConnection();
        tSession =
          tConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        tProducer = tSession.createProducer(_channelSendDest);
        if (messageContext == null)
          throw new ChannelException("[MessageContext cannot be Null]");
        ObjectMessage message = tSession.createObjectMessage(messageContext);
        if (message == null)
          throw new ChannelException("[Object Message Create was Null]");
        tProducer.send(message);
        ChannelLogger.debugLog(
          CLASS_NAME,
          "dispatchMessageToSendListener()",
          "[MessageContext - Delivired Successfully ]");
      }
      else
        throw new ChannelException(
          "["
            + CLASS_NAME
            + "[dispatchMessageToSendListener()]"
            + "[Unable to deliver Message to SendMessageListener check if MessageContex or "
            + "ConnectionFactory Initilized]");
    }
    catch (Exception ex)
    {
      throw new TransportException(
        "["
          + CLASS_NAME
          + "]"
          + "[dispatchMessageToSendListener()]"
          + "[Unable to deliver Message to SendMessageListener]",
        ex);
    }
    finally
    {
      if (tConnection != null)
      {
        try
        {
          tConnection.close();
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }
    }
  }

  public static void dispatchMessageToFeedbackListener(MessageContext messageContext)
    throws TransportException, ChannelException
  {
    if(! JMSRetrySender.isSendViaDefMode())
    {
      try
      {
        JMSRetrySender.sendMessage(_channelFeedbackName,messageContext, null, _jmsFeedbackProps);
      }
      catch(Throwable th)
      {
        throw new TransportException("Unable to deliver Message to FeedbackMessageListener",th);
      }
      return;
    }
    
    Connection tConnection = null;
    Session tSession = null;
    MessageProducer tProducer = null;
    try
    {
      if (_connectionFactory != null && messageContext != null)
      {
        tConnection = _connectionFactory.createConnection();
        tSession =
          tConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        tProducer = tSession.createProducer(_channelFeedbackDest);
        ObjectMessage message = tSession.createObjectMessage(messageContext);
        tProducer.send(message);
      }
      else
        throw new ChannelException("Unable to deliver Message to FeedbackMessageListener check if MessageContex or ConnectionFactory Initilized");
    }
    catch (Exception ex)
    {
      throw new TransportException("Unable to deliver Message to FeedbackMessageListener",ex);
    }
    finally
    {
      if (tConnection != null)
      {
        try
        {
          tConnection.close();
        }
        catch (Exception ex)
        {
        }
      }
    }
  }

  private static void initDispatcher()
    throws NamingException, NameNotFoundException, ServiceLookupException, ApplicationException
  {
    Configuration channelConfig =
      ConfigurationManager.getInstance().getConfig(IChannelConfig.CONFIG_NAME);
    Configuration tptConfig =
      ConfigurationManager.getInstance().getConfig(
        ITransportConfig.CONFIG_NAME);

    String tConnectionFactoryName =
      channelConfig.getString(
        IChannelConfig.APPSERVER_CONNECTION_FACTORY);
    String destToSend =
      channelConfig.getString(IChannelConfig.CHANNEL_DESTINATION_APP_TO_SEND);
    String destToFeedBack =
      tptConfig.getString(ITransportConfig.APPSERVER_DESTINATION_TRANSPORT_FEEDBACK);
    
    //TWX 21 NOV 2007
    _channelFeedbackName = destToFeedBack;
    _channelSendDestName = destToSend;
    
    _jmsSendProps.put(JMSSender.CONN_FACTORY, tConnectionFactoryName);
    _jmsSendProps.put(JMSSender.JNDI_SERVICE, ServiceLocator.CLIENT_CONTEXT);
    _jmsSendProps.put(JMSSender.JMS_DEST_NAME, _channelSendDestName);
    
    _jmsFeedbackProps.put(JMSSender.CONN_FACTORY, tConnectionFactoryName);
    _jmsFeedbackProps.put(JMSSender.JNDI_SERVICE, ServiceLocator.CLIENT_CONTEXT);
    _jmsFeedbackProps.put(JMSSender.JMS_DEST_NAME, _channelFeedbackName);
    
    ChannelLogger.debugLog("MessageDispatcher", "initDispatcher", "ChannelFeedback name: "+_channelFeedbackName+" channel dest name: "+_channelSendDestName+" sendProps: "+_jmsSendProps);
    
    
    if (!checkNullEmptyString(tConnectionFactoryName)
      && !checkNullEmptyString(destToSend)
      && !checkNullEmptyString(destToFeedBack))
    {
      _connectionFactory =
        (ConnectionFactory) getManagedObject(tConnectionFactoryName,
          ConnectionFactory.class);
      _channelSendDest = (Destination) getManagedObject(destToSend, Destination.class);
      _channelFeedbackDest =
        (Destination) getManagedObject(destToFeedBack, Destination.class);
    }
    else
    {
      throw new ApplicationException("Error in Channel Configuration Properties");
    }
  }

  private static Object getManagedObject(String objName, Class classObj)
    throws NamingException, NameNotFoundException, ServiceLookupException
  {
    JNDIFinder findHelper = ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getJndiFinder(); //new JNDIFinder();
    return findHelper.lookup(objName, classObj);
  }

  /*
  private static void initilizeManagedObjects(
    String tConnectionFactoryName,
    String topicToSend,
    String topicToFeedBack)
    throws NamingException, NameNotFoundException
  {
    JNDIFinder findHelper = new JNDIFinder();
    TopicConnectionFactory topicConnectionFactory =
      (TopicConnectionFactory) findHelper.lookup(
        tConnectionFactoryName,
        TopicConnectionFactory.class);
    _channelSendTopic = (Topic) findHelper.lookup(topicToSend, Topic.class);
    _channelFeedbackTopic =
      (Topic) findHelper.lookup(topicToFeedBack, Topic.class);
  }*/

  public static boolean checkNullEmptyString(String emptyString)
  {
    if (emptyString == null
      || emptyString.equals("")
      || emptyString.equals(" "))
      return true;
    else
      return false;
  }

  private static void logError(
                               String errorCode,
    String methodName,
    String message,
    Throwable error)
  {
    ChannelLogger.errorLog(errorCode,
                           CLASS_NAME, methodName, message, error);
  }

}