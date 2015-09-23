/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNPersistenceTopicConnection.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Dec 02 2002    Goh Kan Mun                 Created
 * Dec 04 2002    Goh Kan Mun                 Modified - Review close method.
 *                                                       May cause error by
 *                                                       using Enumeration.
 *                                                     - fixed containListener()
 *                                                     - Update CLASS_NAME.
 * Jan 06 2003    Goh Kan Mun                 Modified - to close jndi connection after
 *                                                       use in addListener.
 *                                                     - remove static variable _configTransport.
 */
package com.gridnode.pdip.base.transport.jms.topic;

import java.io.Serializable;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.naming.NamingException;

import com.gridnode.pdip.base.transport.exceptions.GNTptPersistenceConnectionException;
import com.gridnode.pdip.base.transport.exceptions.GNTptWrongConfigException;
import com.gridnode.pdip.base.transport.exceptions.InvalidCommInfoException;
import com.gridnode.pdip.base.transport.helpers.ITransportConfig;
import com.gridnode.pdip.base.transport.helpers.TptLogger;
import com.gridnode.pdip.base.transport.jms.GNJMSExceptionHandler;
import com.gridnode.pdip.base.transport.jms.IGNJMSPersistenceConnection;
import com.gridnode.pdip.framework.util.JNDIFinder;

/**
 * This is the implemented JMS persistence connection for Topic and it contains methods
 * as specified by <code>IGNJMSPersistenceConnection</code>.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class GNPersistenceTopicConnection
       extends GNTopicConnection implements IGNJMSPersistenceConnection
{
  private Hashtable _listeners = new Hashtable();
  private static final String CLASS_NAME = "GNPersistenceTopicConnection";
  private String[] _header = null;

  /**
   * Constructor
   *
   * @param           host      host IP address or url of the JMS router
   * @param           port      port use by the JMS router.
   * @param           username  login name to the JMS router
   * @param           password  password for login to the JMS router
   * @param           header    the header passed down by the BL / Channel to be sent back
   *                            when an error occured to this persistence connection
   *
   * @exception       NamingException                     thrown when encounter a Naming error.
   * @exception       JMSException                        thrown when encounter a JMS error.
   * @exception       InvalidCommInfoException       thrown when the commInfo is invalid.
   * @exception       GNTptWrongConfigException           thrown when the configuration is not correct.
   */
  public GNPersistenceTopicConnection(String host, int port,
         String username, String password, String[] header)
          throws JMSException, NamingException, GNTptWrongConfigException,
                 InvalidCommInfoException
  {
    super(host, port, username, password);
    _header = header;
    TptLogger.debugLog(
            CLASS_NAME,
            "Construct",
            "Created connection to " + host + ":" + port + ":" + username
            );
  }

  /**
   * To check whether a subscriber is already present.
   * @param           topicName       topic name the listener is subscribed to
   *
   * @return          true if and only if the listener is already present.
   */
  public boolean containListener(String topicName)
  {
    return _listeners.containsKey(topicName);
  }

  /**
   * To remove a subscriber specified.
   * @param           topicName       topic name the listener is subscribed to
   *
   * @exception       GNTptPersistenceConnectionException thrown when encounter a persistence
   *                                                      connection error.
   * @exception       JMSException                        thrown when encounter a JMS error.
   */
  public void removeListener(String topicName)
            throws GNTptPersistenceConnectionException, JMSException
  {
    if (!containListener(topicName))
      throw new GNTptPersistenceConnectionException("Persistence listener does not exist");
    GNSubscriber receiver = (GNSubscriber) _listeners.get(topicName);
    if (receiver != null)
      receiver.close();
    _listeners.remove(topicName);
    TptLogger.debugLog(CLASS_NAME,
          "removeListener",
          "Receivers " + topicName + " removed."
          );
  }

  /**
   * To add a subscriber to the topic name specified.
   * @param           topicName       Topic name interested for receiving message
   *
   * @exception       GNTptPersistenceConnectionException thrown when encounter a persistence
   *                                                      connection error.
   * @exception       NamingException                     thrown when encounter a Naming error.
   * @exception       JMSException                        thrown when encounter a JMS error.
   */
  public void addListener(String topicName)
         throws GNTptPersistenceConnectionException, NamingException, JMSException,
                InvalidCommInfoException
  {
    if (containListener(topicName))
      throw new GNTptPersistenceConnectionException("Persistence listener already exist");
    boolean noLocal = _configTransport.getBoolean(ITransportConfig.EXTMQ_TOPIC_SUBSCRIBER_NOLOCAL, true);
    JNDIFinder jndi = getExtJNDIInitialContext(_host, _port);
    Topic topic = (Topic) jndi.lookup(topicName, Topic.class);
    jndi.close();
    GNSubscriber receiver = new GNSubscriber(topicName,
                       _topicConnection, topic, noLocal);
    _listeners.put(topicName, receiver);
    TptLogger.debugLog(CLASS_NAME,
          "addListener",
          "Receivers " + topicName + " added."
          );
  }

  /**
   * To close this persistence connection.
   *
   * @exception       JMSException              thrown when encounter a JMS error.
   */
  public void close() throws JMSException
  {
    String topicName = null;

    Object[] keys = _listeners.keySet().toArray();
    for (int i = 0; i < keys.length; i++)
    {
      try
      {
        topicName = (String) keys[i];
        removeListener(topicName);
      }
      catch (Exception e)
      {
        TptLogger.debugLog(CLASS_NAME,
              "close",
              "Error closing one of the receivers " + topicName + ".",
              e
              );
        if (containListener(topicName)) // if not removed successfully
          _listeners.remove(topicName); // remove the subscriber from the listeners.
      }
    }
    super.close(); // Should close everything including the subscribers.
  }

  /**
   * To send the specified message to the specified topic.
   * @param           topicName             name of the destination topic
   * @param           msg                   Serializable message to be sent
   *
   * @exception       GNTptPersistenceConnectionException thrown when encounter a persistence
   *                                                      connection error.
   * @exception       NamingException                     thrown when encounter a Naming error.
   * @exception       JMSException                        thrown when encounter a JMS error.
   */
  public void send(String topicName, Serializable msg)
         throws JMSException, NamingException, InvalidCommInfoException
  {
    super.send(topicName, msg);
  }

  /**
   * To retrieve the header information passed down by the BL/Channel.
   * @return          the header information.
   */
  public String[] getHeader()
  {
    return _header;
  }

  /**
   * To set a exception listener to this persistence connection.
   * @param           connectionKey       id of the persistence connection
   *
   * @exception       JMSException        thrown when encounter a JMS error.
   */
  public void setExceptionListener(String connectionKey) throws JMSException
  {
    _topicConnection.setExceptionListener(new GNJMSExceptionHandler(connectionKey));
  }

}
