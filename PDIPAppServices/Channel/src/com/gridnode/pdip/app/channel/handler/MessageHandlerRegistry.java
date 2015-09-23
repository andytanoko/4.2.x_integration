/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MessageHandlerRegistry.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * AUG 20 2002    Jagadeesh               Created
 *
 */

package com.gridnode.pdip.app.channel.handler;

import com.gridnode.pdip.app.channel.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.helpers.IChannelConfig;
import com.gridnode.pdip.app.channel.helpers.IReceiveFeedbackHandler;
import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public class MessageHandlerRegistry
{

  private static Hashtable _receiveEventMap = null;
  private static Hashtable _feedbackEventMap = null;
  //private static Hashtable _eventClassMap = null;
  private static MessageHandlerRegistry _self = null;

  private static Object lock = new Object();
  private static final String EVENTMAP_RECEIVE = "receiveEventMap";
  private static final String EVENTMAP_FEEDBACK = "feedBackEventMap";

  private static final String CLASS_NAME = "MessageHandlerRegistry";

  /**
   * Read Properties File to get eventid and corresponding Handler class.
   * Cache this information to static Hashtable for any further reference to the
   * same eventid.
   */

  private MessageHandlerRegistry()
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "MessageHandlerRegistry()",
        "In MessageHandlerRegistry");
      _receiveEventMap = new Hashtable();
      _feedbackEventMap = new Hashtable();
      Configuration config =
        ConfigurationManager.getInstance().getConfig(
          IChannelConfig.RECEIVEEVENT_CONFIG_NAME);
      Properties receiveeventProps = config.getProperties();
      initilizeEventMap(
        receiveeventProps,
        MessageHandlerRegistry.EVENTMAP_RECEIVE);

      Configuration fconfig =
        ConfigurationManager.getInstance().getConfig(
          IChannelConfig.FEEDBACKEVENT_CONFIG_NAME);
      Properties feedbackeventProps = fconfig.getProperties();
      initilizeEventMap(
        feedbackeventProps,
        MessageHandlerRegistry.EVENTMAP_FEEDBACK);

      ChannelLogger.debugLog(
        CLASS_NAME,
        "MessageHandlerRegistry()",
        "Initialized Event Maps");
    }
    catch (Exception ex)
    {
      ChannelLogger.errorLog(ILogErrorCodes.CHANNEL_MESSAGE_HANDLER_REGISTRY_INIT,
        CLASS_NAME,
        "MessageHandlerRegistry()",
        "CannotInitialize " + ex.getMessage());
      ex.printStackTrace();
    }
  }

  private void initilizeEventMap(Properties props, String eventType)
    throws Exception
  {
    if (eventType.equals(MessageHandlerRegistry.EVENTMAP_RECEIVE))
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "initializeEventMap()",
        "Event Properties For Receive FeedBack" + " Handler-->" + props);
      Enumeration enm = props.keys();
      while (enm.hasMoreElements())
      {
        String eventId = (String) enm.nextElement();
        String className = (String) props.get(eventId);

        IReceiveMessageHandler listenerObj =
          (IReceiveMessageHandler) Class.forName(className).newInstance();
        ChannelLogger.infoLog(
          CLASS_NAME,
          "initializeEventMap()",
          "Event To Put-->"
            + eventId
            + "Event Class "
            + listenerObj.getClass().getName());
        _receiveEventMap.put(eventId, listenerObj);
      }

    }
    else if (eventType.equals(MessageHandlerRegistry.EVENTMAP_FEEDBACK))
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "initializeEventMap()",
        "Event Properties For FeedBack" + " Handler-->" + props);
      Enumeration enm = props.keys();
      while (enm.hasMoreElements())
      {
        String eventId = (String) enm.nextElement();
        String className = (String) props.get(eventId);
        IReceiveFeedbackHandler listenerObj =
          (IReceiveFeedbackHandler) Class.forName(className).newInstance();
        if (listenerObj instanceof IReceiveFeedbackHandler)
          ChannelLogger.infoLog(
            CLASS_NAME,
            "initializeEventMap()",
            "Event To Put-->"
              + eventId
              + "Event Class "
              + listenerObj.getClass().getName());
        _feedbackEventMap.put(eventId, listenerObj);
      }

    }

  }

  public static MessageHandlerRegistry getHandlerRegistry()
  {
    if (_self == null)
    {
      synchronized (lock)
      {
        if (_self == null)
          _self = new MessageHandlerRegistry();
      }
    }
    return _self;
  }

  /**
   * This Method provides "Read-Only" guarantee after a Hashtable is
   * populated with values.
   *
   * Also since direct access to member variables is usually frowned upon in OO,
   * you may not intend to use this Map as Member Variable access.
   *
   * Returns the ReceiveMessageHandler registered with the given ID.
   * @param eventId -EventID of the ReceiveMessageHandler
   * @return Class implementing IReceiveMessageHandler.
   */

  public IReceiveMessageHandler getReceiveMessageHandler(String eventId)
  {
    if (_receiveEventMap.containsKey(eventId))
    {
      return (IReceiveMessageHandler) _receiveEventMap.get(eventId);
    }
    ChannelLogger.infoLog(
      CLASS_NAME,
      "getMessageReceiveHandler()",
      "reveiveMap does not have Handler for this EventID " + eventId);
    return null;

  }

  /**
   * This Method provides "Read-Only" guarantee after a Hashtable is
   * populated with values.
   *
   * Also since direct access to member variables is usually frowned upon in OO,
   * you may not intend to use this Map as Member Variable access.
   *
   * Returns the FeedBackHandler registered with the given ID.
   * @param eventId -EventID of the FeedbackListener
   * @return Class implementing IReceiveFeedbackHandler.
   */

  public IReceiveFeedbackHandler getFeedBackMessageHandler(String eventId)
  {
    if (_feedbackEventMap.containsKey(eventId))
    {
      return (IReceiveFeedbackHandler) _feedbackEventMap.get(eventId);
    }
    ChannelLogger.infoLog(
      CLASS_NAME,
      "getMessageReceiveHandler()",
      "feedBackMap does not have Handler for this EventID " + eventId);
    return null;
  }

}