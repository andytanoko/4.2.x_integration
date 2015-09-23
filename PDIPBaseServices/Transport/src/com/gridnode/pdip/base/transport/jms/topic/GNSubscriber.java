/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNSubscriber.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * ??? ?? 2002    Jagadeesh, Jianyu       Created
 * Jun 21 2002    Goh Kan Mun             Modified - include the TPTLogger.
 *                                                 - throw appropriate exception
 * AUG 21 2002    Jagadeesh               Add - Method to Recieve.
 * Nov 07 2002    Goh Kan Mun             Modified - Refactor. To listen from SMQ
 *                                                   and send to App only.
 * Dec 02 2002    Goh Kan Mun             Modified - Change name from GNSMQBridgeReceive
 *                                                   to GNSubscriber
 *                                                 - Add createSubscriber method
 *                                                 - Add close method
 * Dec 03 2002    Kan Mun                 Modified - Remove the backward compatible
 *                                                   dependency on GNTransportPayload.
 * Dec 05 2002    Kan Mun                 Modified - Fixed close method throws IllegalStateException
 * Jan 14 2003    Jagadeesh               Modified - Added ITransportConstants Headers to
 *                                                   identify the message object.(either: GT/GTAS).
 * Nov 14 2003    Jagadeesh               Modified - Change ITransportConstants to ICommonHeaders
 *
 */

package com.gridnode.pdip.base.transport.jms.topic;

import java.util.Date;

import javax.jms.*;

import com.gridnode.pdip.base.transport.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.transport.helpers.GNBackwardPayload;
import com.gridnode.pdip.base.transport.helpers.GNTransportPayload;
import com.gridnode.pdip.base.transport.helpers.ITransportConstants;
import com.gridnode.pdip.base.transport.helpers.TptLogger;
import com.gridnode.pdip.base.transport.jms.GNJMSController;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;

/**
 * This is the message listener class (subscriber) that listens to the specified topic.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class GNSubscriber implements MessageListener
{
  private static final String CLASS_NAME = "GNSubscriber";
  private String _description = null;
  private TopicSession session = null;
  private TopicSubscriber subscriber = null;

  /**
   * Constructor
   * @param           description   a description for this subscriber.
   * @param           connection    the <code>TopicConnection</code> used to listen.
   * @param           topic         the <code>Topic</code> to be listened to.
   * @param           noLocal       an indication whether the listener receives message sent
   *                                locally.
   * @exception       JMSException  thrown when encounter a JMS error.
   */
  public GNSubscriber(String description, TopicConnection connection, Topic topic,
         boolean noLocal) throws JMSException
  {
    _description = description + new Date();
    createSubscriber(connection, topic, noLocal);
    TptLogger.debugLog(CLASS_NAME, "Constructor", "Created for " + _description);
  }

  /**
   * This method is invoked by the JMS if there is a message in the JMS router.
   *
   * @param           jmsMessage    message that is sent to the topic that this class listen to.
   */
  public void onMessage(Message jmsMessage)
  {
    try
    {
      TptLogger.debugLog(CLASS_NAME, "onMessage", "receiving from " + _description);
      if (jmsMessage != null)
      {
        if (!(jmsMessage instanceof ObjectMessage))
        {
          TptLogger.warnLog(CLASS_NAME,
                  "onMessage",
                  "Bridge Receiver: Error -- non-object message received");
          return;
        }
        final ObjectMessage objectMessage = (ObjectMessage) jmsMessage;
        GNTransportPayload payload = null;
        if (objectMessage.getObject() == null)
          throw new Exception("Payload is null");
        else if (GNBackwardPayload.isBackwardCompatiblePayload(objectMessage.getObject()))
        {
          payload = GNBackwardPayload.getGNTransportPayload(objectMessage.getObject());
          payload.getHeader().put(ICommonHeaders.PAYLOAD_TYPE,
          //ITransportConstants.PACKAGE_TYPE_KEY,
                                  ITransportConstants.PACKAGE_TYPE_GNBACKWARDCOMPATIBLE);
        }
        else if (objectMessage.getObject() instanceof GNTransportPayload)
        {
          payload = (GNTransportPayload) objectMessage.getObject();
          payload.getHeader().put(ICommonHeaders.PAYLOAD_TYPE,
          //ITransportConstants.PACKAGE_TYPE_KEY,
                                  ITransportConstants.PACKAGE_TYPE_GTAS);
        }
        else
          throw new Exception("unrecognized object message received: " + payload);

        GNJMSController.getInstance().sendMessageToApp(payload);

        TptLogger.debugLog(CLASS_NAME, "onMessage", "Object sent to AppServer MQ  ... from onMessage of SwiftMQ ");
      }// if message is not null
      else
      {
        TptLogger.debugLog(CLASS_NAME, "onMessage", "Message is Null  in onMessage for SwiftMQ ");
      }
    }//try
    catch(Throwable e)
    {
      TptLogger.errorLog(ILogErrorCodes.TPT_SUBSCRIBER_ONMSG,
                         CLASS_NAME, "onMessage", "Could not perform onMessage: "+e.getMessage(), e);
    }
  }

  /**
   * Retrieve the description.
   *
   * @return          description of this listener.
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * This method is used to create a subscriber.
   * @param           connection    the <code>TopicConnection</code> used to listen.
   * @param           topic         the <code>Topic</code> to be listened to.
   * @param           noLocal       an indication whether the listener receives message sent
   *                                locally.
   *
   * @exception       JMSException  thrown when encounter a JMS error.
   */
  private void createSubscriber(TopicConnection connection, Topic topic,
         boolean noLocal) throws JMSException
  {
    session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
    subscriber = session.createSubscriber(topic, null, noLocal);
    subscriber.setMessageListener(this);
    connection.start(); //start the delivery of incoming messages
  }

  /**
   * This method will close this subscriber.
   *
   * @exception       JMSException  thrown when encounter a JMS error.
   */
  public void close() throws JMSException
  {
    subscriber.close();
    session.close();
  }

}