/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 23 2002    MAHESH              Created
 * Jul 18 2002    MAHESH              Added functionality for topic and
 *                                    retry sending message
 * Feb 07 2007		Alain Ah Ming				Deprecate logError(String, Throwable) method
 * 																		Add logError(String, String, Throwable) method 
 * 																			to handle new error codes                                   
 */

package com.gridnode.pdip.framework.jms;


import java.util.HashMap;

import javax.jms.*;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;


/**
 * This JMSObject is the utility class to send and receive messages to Topic ,Queue
 * @author Mahesh
 * @since
 * @version GT 4.0 VAN
 */
public class JMSObject
{
    public static final int QUEUE_TYPE = 1;
    public static final int TOPIC_TYPE = 2;

    private int type;
    String jndiService;
    Connection connection;
    Session session;
    MessageProducer msgProducer;
    MessageConsumer msgConsumer;
    int msgRetryCount = 0;
    long msgRetryDelay = 0;

    public JMSObject(int type, Connection connection, String jndiService, int msgRetryCount, long msgRetryDelay)
    {
        this.type = type;
        this.connection = connection;
        this.jndiService = jndiService;
        this.msgRetryCount = msgRetryCount;
        this.msgRetryDelay = msgRetryDelay;
    }

    /**
     * Creates a session with transacted=false and ackType=Session.AUTO_ACKNOWLEDGE
     * @throws JMSException
     */
    public void initSession() throws JMSException
    {
        initSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    /**
     * Creates a session with specified transacted,ackType parameters
     * @param transacted
     * @param ackType
     * @throws JMSException
     */
    public void initSession(boolean transacted, int ackType) throws JMSException
    {
        try
        {
            if (type == QUEUE_TYPE)
                session = ((QueueConnection) connection).createQueueSession(transacted, ackType);
            else if (type == TOPIC_TYPE)
                session = ((TopicConnection) connection).createTopicSession(transacted, ackType);
        }
        catch (JMSException jex)
        {
            logWarn("[JMSObject.initSession] unable to start session ", jex);
            throw jex;
        }
    }

    /**
     * Creates QueueSender or TopicPublisher depending upon the type of JMSObject
     * and assumes producerDestination to be Topic or Queue depending upon the type of JMSObject
     * @param producerDestination can be name of topic or queue
     * @throws JMSException
     */
    public void initProducer(String producerDestination) throws Exception
    {
        initProducer(getDestination(producerDestination));
    }

    /**
     * Creates QueueSender or TopicPublisher depending upon the type of JMSObject
     * and assumes producerDestination  to be Topic or Queue depending upon the type of JMSObject
     * @param producerDestination can be topic or queue
     * @throws JMSException
     */
    public void initProducer(Destination producerDestination) throws JMSException
    {
        try
        {
            if (producerDestination == null)
                throw new JMSException("[JMSObject.initProducer] producerDestination is null");
            if (type == QUEUE_TYPE)
                msgProducer = ((QueueSession) session).createSender((Queue) producerDestination);
            else if (type == TOPIC_TYPE)
                msgProducer = ((TopicSession) session).createPublisher((Topic) producerDestination);
        }
        catch (JMSException jex)
        {
        	logWarn("[JMSObject.initProducer] unable to create MessageProducer :" + producerDestination, jex);
            throw jex;
        }
    }

    /**
     * Creates QueueSender or TopicPublisher depending upon the type of JMSObject
     * and assumes consumerDestination to be Topic or Queue depending upon the type of JMSObject
     * @param consumerDestination Topic or Queue
     * @param messageListener
     * @throws JMSException
     */
    public void initConsumer(String consumerDestination, MessageListener messageListener) throws Exception
    {
        initConsumer(getDestination(consumerDestination), messageListener);
    }

    /**
     * Creates QueueReceiver or TopicSubscriber depending upon the type of JMSObject
     * and assumes consumerDestination to be Topic or Queue depending upon the type of JMSObject
     * @param consumerDestination can be topic or queue
     * @param messageListener
     * @throws JMSException
     */
    public void initConsumer(Destination consumerDestination, MessageListener messageListener) throws JMSException
    {
        initConsumer(consumerDestination, null, false, messageListener);
    }

    /**
     * Creates QueueReceiver or TopicSubscriber depending upon the type of JMSObject
     * and assumes consumerDestination to be Topic or Queue depending upon the type of JMSObject.
     * @param consumerDestination can be topic or queue
     * @param selector message selector
     * @param inhibited
     * @param messageListener
     * @throws JMSException
     */
    public void initConsumer(Destination consumerDestination, String selector, boolean inhibited, MessageListener messageListener) throws JMSException
    {
        try
        {
            if (type == QUEUE_TYPE)
            {
                if (selector != null)
                    msgConsumer = ((QueueSession) session).createReceiver((Queue) consumerDestination, selector);
                else msgConsumer = ((QueueSession) session).createReceiver((Queue) consumerDestination);
            } else if (type == TOPIC_TYPE)
            {
                if (selector != null)
                    msgConsumer = ((TopicSession) session).createSubscriber((Topic) consumerDestination, selector, inhibited);
                else msgConsumer = ((TopicSession) session).createSubscriber((Topic) consumerDestination);
            }
            msgConsumer.setMessageListener(messageListener);
            connection.start();
        }
        catch (JMSException jex)
        {
        	logWarn("[JMSObject.initConsumer] unable to create MessageConsumer :" + consumerDestination, jex);
            throw jex;
        }
    }

    /**
     * Converts the Map object to MapMessage and sends to the destination specified by
     * message producer
     * @param map
     * @return
     * @throws JMSException
     */
    public Message sendMessage(HashMap map) throws JMSException
    {
        try
        {
            ObjectMessage objMessage = session.createObjectMessage();
            objMessage.setObject(map);
            sendMessage((Destination) null, objMessage);
            return objMessage;
        }
        catch (JMSException jex)
        {
        	logWarn("[JMSObject.sendMessage] unable to send map message " + map, jex);
            throw jex;
        }
    }

    /**
     * Sends the specified message to the destination specified by the message producer
     * @param message
     * @throws JMSException
     */
    public void sendMessage(Message message) throws JMSException
    {
        sendMessage((Destination) null, message);
    }

    /**
     * Sends the specified message to the destination specified by the destination parameter
     * @param destination can be name of topic or queue depending upon type of JMSObject
     * @param message
     * @throws JMSException
     */
    public void sendMessage(String destination, Message message) throws Exception
    {
        if (destination == null)
            sendMessage(message);
        else sendMessage(getDestination(destination), message);
    }

    /**
     * Sends the specified message to the destination specified by the destination parameter
     * @param destination can be topic or queue depending upon type of JMSObject
     * @param message
     * @throws JMSException
     */
    public void sendMessage(Destination destination, Message message) throws JMSException
    {
        JMSException rjex = null;
        int count = 0;

        do
        {
            if (rjex != null)
            {
                try
                {
                    Thread.sleep(msgRetryDelay);
                }
                catch (Exception e)
                {
                }
            }
            try
            {
                if (type == QUEUE_TYPE)
                {
                    sendMessageToQueue((Queue) destination, message);
                } else if (type == TOPIC_TYPE)
                {
                    sendMessageToTopic((Topic) destination, message);
                }
                rjex = null;
            }
            catch (JMSException jex)
            {
            	logWarn("[JMSObject.sendMessage] unable to send message count=" + count + ", destination=" + destination, jex);
                rjex = jex;
            }
        }
        while (count++ < msgRetryCount && rjex != null);
        if (rjex != null)
            throw rjex;
    }

    private void sendMessageToQueue(Queue queue, Message message) throws JMSException
    {
        try
        {
            if (queue == null)
                queue = ((QueueSender) msgProducer).getQueue();
            ((QueueSender) msgProducer).send(queue, message);
        }
        catch (JMSException jex)
        {
        	logWarn("[JMSObject.sendMessageToQueue] unable to send message to queue :" + queue, jex);
            throw jex;
        }
    }

    private void sendMessageToTopic(Topic topic, Message message) throws JMSException
    {
        try
        {
            if (topic == null)
                topic = ((TopicPublisher) msgProducer).getTopic();
            ((TopicPublisher) msgProducer).publish(topic, message);
        }
        catch (JMSException jex)
        {
            logWarn("[JMSObject.sendMessageToTopic] unable to send message to topic :" + topic, jex);
            throw jex;
        }
    }

    /**
     * Creates topic or queue depending upon the type of JMSObject
     * @param destinationName
     * @return topic or queue depending upon the type of JMSObject
     * @throws JMSException
     */
    public Destination createDestination(String destinationName) throws JMSException
    {
        Destination destination = null;
        try{
            destination=getDestination(destinationName);
        }
				catch (ServiceLookupException ex)
				{
          logError(ILogErrorCodes.JMS_GENERIC, "[JMSObject.createDestination] unable to getDestination =" + destinationName+". Service lookup error: "+ex.getMessage(), ex);
				}
				catch (NameNotFoundException ex)
				{
          logError(ILogErrorCodes.JMS_GENERIC, "[JMSObject.createDestination] unable to getDestination =" + destinationName+". Naming error: "+ex.getMessage(), ex);
				}
				catch (NamingException ex)
				{
          logError(ILogErrorCodes.JMS_GENERIC, "[JMSObject.createDestination] unable to getDestination =" + destinationName+". Naming error: "+ex.getMessage(), ex);
				}
        catch(Exception ex)
        {
            logError(ILogErrorCodes.JMS_GENERIC, "[JMSObject.createDestination] unable to getDestination =" + destinationName+". Unexpected error: "+ex.getMessage(), ex);
        }

        if (destination == null)
        {
            if (type == QUEUE_TYPE)
                destination = ((QueueSession) session).createQueue(destinationName);
            else if (type == TOPIC_TYPE)
                destination = ((TopicSession) session).createTopic(destinationName);
            System.out.println("destination :" + destination);
        }
        return destination;
    }

    /**
     * Creates temporary topic or queue depending upon the type of JMSObject
     * @return topic or queue depending upon the type of JMSObject
     * @throws JMSException
     */
    public Destination createTempDestination() throws JMSException
    {
        Destination destination = null;

        if (type == QUEUE_TYPE)
            destination = ((QueueSession) session).createTemporaryQueue();
        else if (type == TOPIC_TYPE)
            destination = ((TopicSession) session).createTemporaryTopic();
        return destination;
    }

    /**
     * It will do lookup for the specified destination and returns it
     *
     * @param destination
     * @return Destination object, return value is null if it fails to do lookup
     * @throws NamingException 
     * @throws NameNotFoundException 
     * @throws ServiceLookupException 
     */
    public Destination getDestination(String destination) throws ServiceLookupException, NameNotFoundException, NamingException
    {
        //return (Destination) ServiceLookup.getInstance(jndiService).lookup(destination);
      return (Destination) ServiceLocator.instance(jndiService).getJndiFinder().lookup(destination, Destination.class);
    }

    /**
     * This method returns 0 or 1 where
     * 1 : QUEUE_TYPE
     * 2 : TOPIC_TYPE
     * @return 1 or 2
     */
    public int getType()
    {
        return type;
    }

    public Connection getConnection()
    {
        return connection;
    }

    public Session getSession()
    {
        return session;
    }

    public MessageProducer getMessageProducer()
    {
        return msgProducer;
    }

    public MessageConsumer getMessageConsumer()
    {
        return msgConsumer;
    }

    public void closeMessageProducer()
    {
        try
        {
            if (msgProducer != null)
                msgProducer.close();
        }
        catch (JMSException jex)
        {
            logError(ILogErrorCodes.JMS_GENERIC, "[JMSObject.closeMessageProducer] unable to closeMessageProducer ", jex);
        }
        msgProducer = null;
    }

    public void closeMessageConsumer()
    {
        try
        {
            if (msgConsumer != null)
                msgConsumer.close();
        }
        catch (JMSException jex)
        {
            logError(ILogErrorCodes.JMS_GENERIC,  "[JMSObject.closeMessageConsumer] unable to closeMessageConsumer ", jex);
        }
        msgConsumer = null;
    }

    public void closeSession()
    {
        try
        {
            closeMessageProducer();
            closeMessageConsumer();
            if (session != null)
                session.close();
        }
        catch (JMSException jex)
        {
            logError(ILogErrorCodes.JMS_GENERIC, "[JMSObject.closeSession] unable to closeSession ", jex);
        }
        session = null;
    }
    
    private static void logInfo(String msg)
    {
        Log.log(Log.FRAMEWORK, msg);
    }

    private static void logDebug(String msg)
    {
        Log.debug(Log.FRAMEWORK, msg);
    }

    private static void logWarn(String msg, Throwable t)
    {
    	Log.warn(Log.FRAMEWORK, msg, t);
    }
    
    private static void logError(String errorCode, String msg, Throwable th)
    {
        Log.error(errorCode, Log.FRAMEWORK, msg, th);
    }
    
    protected void finalize() throws Throwable
    {
        try
        {
            closeSession();
        }
        catch (Throwable t)
        {
        }
        super.finalize();
    }

}
